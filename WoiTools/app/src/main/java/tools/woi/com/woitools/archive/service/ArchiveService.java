package tools.woi.com.woitools.archive.service;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.sromku.simple.storage.SimpleStorage;
import com.sromku.simple.storage.Storage;
import com.sromku.simple.storage.helpers.OrderType;

import org.joda.time.DateTime;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.greenrobot.event.EventBus;
import tools.woi.com.woitools.archive.domain.ArchiveItem;
import tools.woi.com.woitools.archive.domain.ArchiveMode;
import tools.woi.com.woitools.archive.domain.DayOrMonthMode;
import tools.woi.com.woitools.system.domain.SystemConfiguration;
import tools.woi.com.woitools.system.domain.SystemPropertyKey;

/**
 * Created by YeekFeiTan on 1/6/2016.
 */
public class ArchiveService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * Called when the service is being created.
     */
    @Override
    public void onCreate() {
        Log.d("ArchiveService", "Service created");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        post("initializing..");
        archiveTask();

        Log.d("ArchiveService", "Service started.");
        return Service.START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {

        Log.d("ArchiveService", "Service destroyed.");
        super.onDestroy();
    }

    private void post(String msg) {
        EventBus.getDefault().post(msg);
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

            File[] files = f.listFiles();
            for (File inFile : files) {
                if (inFile.isDirectory()) {
                    post("Sub folder: " + inFile.getName());
                } else {
                    Date fileCreatedDate = tryGetDateFromURL(inFile.getName());

                    if (fileCreatedDate != null && !(new DateTime(fileCreatedDate)).isAfter(comparisonDate)) {
                        post("Archive File name: " + inFile.getName() + "| last accessed: " + tryGetDateFromURL(inFile.getName()));
                    }
                }
            }
        }


    }

    private String removeFirstPathSeperator(String directoryPath) {
        String extStoragePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
        String path = directoryPath.replace(extStoragePath, "");

        if (path.substring(0, 1).equals("/")) {
            path = path.substring(1);
        }
        return path;
    }

    private Date tryGetDateFromURL(String path) {
        String[] indexes = path.split("\\W");
        Date date = null;
        if (indexes.length > 0) {
            for (String txt : indexes) {
                try {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
                    date = formatter.parse(txt);
                    return date;
                } catch (ParseException pe) {
                    Log.d("ArchiveService", "unable to parse the string to date.");
                }
            }
        }

        return date;
    }
}
