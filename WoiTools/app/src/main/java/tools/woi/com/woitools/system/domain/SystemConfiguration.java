package tools.woi.com.woitools.system.domain;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

import java.util.List;

/**
 * Created by YeekFeiTan on 12/29/2015.
 */
public class SystemConfiguration extends SugarRecord {

    @Unique
    SystemPropertyKey key;
    String value;

    public SystemConfiguration() {
    }

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

    @Override
    public String toString() {
        return key + "|" + value;
    }

    public static SystemConfiguration find(SystemPropertyKey key) {
        List<SystemConfiguration> results = find(SystemConfiguration.class, "key = ?", key.name());
        if(!results.isEmpty()) {
            return results.get(0);
        }
        return null;
    }
}
