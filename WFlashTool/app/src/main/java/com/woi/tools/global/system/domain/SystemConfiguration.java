package com.woi.tools.global.system.domain;

import com.orm.SugarRecord;
import com.orm.dsl.Table;

import java.util.List;

/**
 * Created by YeekFeiTan on 12/29/2015.
 */
@Table
public class SystemConfiguration extends SugarRecord {

    SystemPropertyKey key;
    String value;

    public SystemConfiguration(SystemPropertyKey key, String value) {
        this.key = key;
        this.value = value;
    }

    public SystemPropertyKey getKey() {
        return key;
    }

    public void setKey(SystemPropertyKey key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public SystemConfiguration find(SystemPropertyKey key) {
        List<SystemConfiguration> results = find(SystemConfiguration.class, "key = ?", key.name());
        if(!results.isEmpty()) {
            results.get(0);
        }
        return null;
    }
}
