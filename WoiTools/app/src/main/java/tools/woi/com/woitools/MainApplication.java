package tools.woi.com.woitools;

import com.orm.SugarApp;

import net.danlew.android.joda.JodaTimeAndroid;

/**
 * Created by YeekFeiTan on 1/6/2016.
 */
public class MainApplication extends SugarApp {

    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);
    }
}
