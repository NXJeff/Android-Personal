package tools.woi.com.woitools.base;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;

/**
 * Created by YeekFeiTan on 12/29/2015.
 */
public abstract class BaseFragment extends Fragment implements FabActionInterface {

    String tagText;

    BaseFragment backFragment;
    FloatingActionButton fab;

    public BaseFragment() {
        super();
    }

    public String getTagText() {
        return tagText;
    }

    public void setTagText(String tagText) {
        this.tagText = tagText;
    }

    public BaseFragment getBackFragment() {
        return backFragment;
    }

    public void setBackFragment(BaseFragment backFragment) {
        this.backFragment = backFragment;
    }

    public FloatingActionButton getFab() {
        return fab;
    }

    public void setFab(FloatingActionButton fab) {
        this.fab = fab;
    }
}
