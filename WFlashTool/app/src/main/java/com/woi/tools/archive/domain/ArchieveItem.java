package com.woi.tools.archive.domain;

import com.orm.SugarRecord;
import com.orm.dsl.Table;

import java.util.Date;

/**
 * Created by YeekFeiTan on 12/29/2015.
 */
@Table
public class ArchieveItem extends SugarRecord{

    private String name;
    private String sourcePath;
    private ArchieveMode archieveMode;
    private int month;
    private int day;
    private int maxFilesNo;
    private Date lastScan;
    private Date lastSync;
    private long numberOfFilesArchieved;


    public ArchieveItem() {
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

    public ArchieveMode getArchieveMode() {
        return archieveMode;
    }

    public void setArchieveMode(ArchieveMode archieveMode) {
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
