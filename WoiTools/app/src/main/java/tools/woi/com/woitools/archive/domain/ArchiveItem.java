package tools.woi.com.woitools.archive.domain;

import com.orm.SugarRecord;

import java.util.Date;

/**
 * Created by YeekFeiTan on 12/29/2015.
 */
public class ArchiveItem extends SugarRecord {
    private String name;
    private String sourcePath;
    private ArchiveMode archieveMode;
    private DayOrMonthMode dayOrMonthMode;
    private int dayOrMonthNumber;
    private int maxFilesNo;
    private Date lastScan;
    private Date lastSync;
    private long numberOfFilesArchieved;
    private boolean includeSubFolder;

    public ArchiveItem() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSourcePath() {
        return sourcePath;
    }

    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }

    public ArchiveMode getArchieveMode() {
        return archieveMode;
    }

    public void setArchieveMode(ArchiveMode archieveMode) {
        this.archieveMode = archieveMode;
    }

    public int getDayOrMonthNumber() {
        return dayOrMonthNumber;
    }

    public void setDayOrMonthNumber(int dayOrMonthNumber) {
        this.dayOrMonthNumber = dayOrMonthNumber;
    }

    public int getMaxFilesNo() {
        return maxFilesNo;
    }

    public void setMaxFilesNo(int maxFilesNo) {
        this.maxFilesNo = maxFilesNo;
    }

    public Date getLastScan() {
        return lastScan;
    }

    public void setLastScan(Date lastScan) {
        this.lastScan = lastScan;
    }

    public Date getLastSync() {
        return lastSync;
    }

    public void setLastSync(Date lastSync) {
        this.lastSync = lastSync;
    }

    public long getNumberOfFilesArchieved() {
        return numberOfFilesArchieved;
    }

    public void setNumberOfFilesArchieved(long numberOfFilesArchieved) {
        this.numberOfFilesArchieved = numberOfFilesArchieved;
    }

    public DayOrMonthMode getDayOrMonthMode() {
        return dayOrMonthMode;
    }

    public void setDayOrMonthMode(DayOrMonthMode dayOrMonthMode) {
        this.dayOrMonthMode = dayOrMonthMode;
    }

    public boolean isIncludeSubFolder() {
        return includeSubFolder;
    }

    public void setIncludeSubFolder(boolean includeSubFolder) {
        this.includeSubFolder = includeSubFolder;
    }
}
