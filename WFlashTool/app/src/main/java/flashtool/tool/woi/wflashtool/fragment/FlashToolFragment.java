package flashtool.tool.woi.wflashtool.fragment;

import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.File;

import flashtool.tool.woi.wflashtool.R;
import flashtool.tool.woi.wflashtool.dialog.FolderChooserDialog;

/**
 * Created by YeekFeiTan on 4/28/2015.
 */
public class FlashToolFragment extends Fragment implements
        FolderChooserDialog.FolderSelectCallback {

    private Toast mToast;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.flash_tool_fragment, container,
                false);

        view.findViewById(R.id.cloneInitDFolderPath).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showFolderChooser();
            }
        });

        /** For Pull to refresh **/
//        mPullToRefreshLayout = (PullToRefreshLayout) view.findViewById(R.id.carddemo_extra_ptr_layout);

        // Now setup the PullToRefreshLayout
//        ActionBarPullToRefresh.from(getActivity())
//                .allChildrenArePullable()
//                .listener(this)
//                .setup(mPullToRefreshLayout);
//
//        initFAB(view);
//        parentView = view;

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void showFolderChooser() {
        new FolderChooserDialog().show(this);
    }

    @Override
    public void onFolderSelection(File folder) {
        showToast(folder.getAbsolutePath());
    }

    private void showToast(String message) {
        if (mToast != null) {
            mToast.cancel();
            mToast = null;
        }
        mToast = Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT);
        mToast.show();
    }

    private void showToast(@StringRes int message) {
        showToast(getString(message));
    }


}
