package tools.woi.com.woitools.archive.domain;

import com.orm.SugarRecord;

import java.util.Date;

/**
 * Created by YeekFeiTan on 12/29/2015.
 */
public class ArchiveItem extends SugarRecord{
    private String name;
    private String sourcePath;
    private ArchiveMode archieveMode;
    private int month;
    private int day;
    private int maxFilesNo;
    private Date lastScan;
    private Date lastSync;
    private long numberOfFilesArchieved;

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

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
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
}
