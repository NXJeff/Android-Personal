package tools.woi.com.woitools.archive.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.joda.time.DateTime;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import de.greenrobot.event.EventBus;
import tools.woi.com.woitools.archive.domain.ArchiveItem;
import tools.woi.com.woitools.archive.domain.ArchiveMode;
import tools.woi.com.woitools.archive.domain.DayOrMonthMode;
import tools.woi.com.woitools.archive.util.DateUtil;
import tools.woi.com.woitools.system.domain.SystemConfiguration;
import tools.woi.com.woitools.system.domain.SystemPropertyKey;

/**
 * Created by YeekFeiTan on 1/7/2016.
 */
public class ArchiveIntentService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public ArchiveIntentService(String name) {
        super(name);
    }

    public ArchiveIntentService() {
        super("ArchiveIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        post("initializing..");
        archiveTask();

    }

    private void archiveTask() {
        //Checking destination configuration
        SystemConfiguration conf = SystemConfiguration.find(SystemPropertyKey.Archieve_DestinationPath);
        String destinationPath;
        if (conf == null) {
            post("Archive Destination configuration not found. Aborting current task.");
            return;
        } else if (conf.getValue() == null || conf.getValue().equals("")) {
            post("Archive Destination is not set. Aborting current task.");
            return;
        }

        destinationPath = conf.getValue();
        File destinationDirectory = new File(destinationPath);
        boolean dirExists = destinationDirectory.exists();

        if (!dirExists) {
            post(destinationPath + " does not exist. Creating directory..");
            destinationDirectory.mkdir();
        } else {
            post(destinationPath + " found. Proceed to archive.");
        }

        List<ArchiveItem> archiveItems = ArchiveItem.listAll(ArchiveItem.class);
        for (ArchiveItem item : archiveItems) {
            post("Processing archive " + item.getName());
            if (item.getSourcePath() == null) {
                post("Archive source path of " + item.getName() + " is not configured. Skipping this archive item. ");
                continue;
            }
            String sourcePath = item.getSourcePath();

            File f = new File(sourcePath);
            if (!f.exists()) {
                post("Archive source path is not existed. Archive of this item will be abort.");
                continue;
            }

            String destinationPathForThisArchive = destinationPath + "/" + item.getName() + "/";
            File[] files = f.listFiles();
            processFolder(destinationPathForThisArchive, f, item);
        }

        post("Processing archive ended.");
    }

    private void processFolder(String destinationPath, File file, ArchiveItem item) {

        DateTime comparisonDate = null;
        Integer maxFiles = null;
        if (item.getArchieveMode().equals(ArchiveMode.OlderThan)) {
            int x = item.getDayOrMonthNumber();
            if (item.getDayOrMonthMode().equals(DayOrMonthMode.Month)) {
                comparisonDate = new DateTime().minusMonths(x);
            } else {
                comparisonDate = new DateTime().minusDays(x);
            }
        } else if (item.getArchieveMode().equals(ArchiveMode.MoreThan)) {
            maxFiles = item.getMaxFilesNo();
        }

        sortFiles(file.listFiles());
        int count = 0;

        for (File inFile : file.listFiles()) {

            if (inFile.isDirectory()) {
                if (item.isIncludeSubFolder()) {
                    String subFolderPath = destinationPath + "/" + inFile.getName() + "/";
                    processFolder(subFolderPath, inFile, item);
                }
            } else {

                if (item.getArchieveMode().equals(ArchiveMode.OlderThan)) {

                    Date fileCreatedDate = tryGetDateFromURL(inFile);


                    if (fileCreatedDate != null && !(new DateTime(fileCreatedDate)).isAfter(comparisonDate)) {

                        moveFile(inFile, new File(destinationPath + inFile.getName()));
                    }
                } else if (item.getArchieveMode().equals(ArchiveMode.MoreThan) && maxFiles != null) {
                    if (count > maxFiles) {
                        moveFile(inFile, new File(destinationPath + inFile.getName()));
                    }
                }
            }

            count++;
        }
    }

    private void moveFile(File source, File destination) {
        try {
            FileUtils.moveFile(source, destination);
            post("Archived File name: " + destination.getAbsolutePath());
        } catch (IOException e) {
            post("Failed to archive file: " + destination.getAbsolutePath());
            Log.e("ArchiveService", "Failed to move file: " + destination.getName() + " because " + e.getMessage());
        }
    }

    private void sortFiles(File[] files) {

        Arrays.sort(files, new Comparator() {
            public int compare(Object o1, Object o2) {

                Date o1date = tryGetDateFromURL(((File) o1));
                Date o2date = tryGetDateFromURL(((File) o2));

                if (o1date != null && o2date != null) {
                    return o1date.compareTo(o2date);
                }

                if (((File) o1).lastModified() > ((File) o2).lastModified()) {
                    return -1;
                } else if (((File) o1).lastModified() < ((File) o2).lastModified()) {
                    return +1;
                } else {
                    return 0;
                }
            }
        });
    }

    private Date tryGetDateFromURL(File file) {

        //First level check: split by _
        String ext = FilenameUtils.getExtension(file.getName());
        String filename = file.getName().replace(ext, "");
        Iterable<String> indexes = Splitter.on(CharMatcher.anyOf("_.")).omitEmptyStrings().split(filename);

        Date date = findMatchesOfDate(indexes);

        //Second level check: split by _
        if (date == null) {
            indexes = Splitter.on(CharMatcher.anyOf("_")).omitEmptyStrings().split(filename);
            date = findMatchesOfDate(indexes);
        }

        //Last level : use lastModified
        if (date == null) {
            date = new Date(file.lastModified());
        }

//        Date date = null;
//        if (indexes.length > 0) {
//            for (String txt : indexes) {
//                try {
//                    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
//                    date = formatter.parse(txt);
//                    return date;
//                } catch (ParseException pe) {
//                }
//
//                try {
//                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//                    date = formatter.parse(txt);
//                    return date;
//                } catch (ParseException pe) {
//                }
//            }
//        }

        return date;
    }

    private Date findMatchesOfDate(Iterable<String> indexes) {
        for (String txt : indexes) {
            if (txt.length() >= 6 && txt.length() <= 8) {
                String dateFormat = DateUtil.determineIfDateFormat(txt);

                if (dateFormat != null) {
                    SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
                    try {
                        return formatter.parse(txt);
                    } catch (ParseException e) {
                        Log.e(this.getClass().getSimpleName(), "ERROR WHILE PARSING - INCORRECT DATE FORMAT");
                    }
                }
            }
        }

        return null;
    }


    private void post(String msg) {
        EventBus.getDefault().post(msg);
    }
}
