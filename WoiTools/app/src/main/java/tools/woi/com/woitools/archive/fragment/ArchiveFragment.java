package tools.woi.com.woitools.archive.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tools.woi.com.woitools.MainActivity;
import tools.woi.com.woitools.R;
import tools.woi.com.woitools.archive.view.OnDialogChooserListener;
import tools.woi.com.woitools.base.BaseFragment;
import tools.woi.com.woitools.system.domain.SystemConfiguration;

/**
 * Created by YeekFeiTan on 12/29/2015.
 */
public class ArchiveFragment extends BaseFragment {

    public static final String CHOOSE_DESTINATION_PATH = "tools.woi.com.woitools.archive.fragment.ArchiveFragment.destinationPath";
    public static final String TAG = "tools.woi.com.woitools.archive.fragment.ArchiveFragment";

    @Bind(R.id.destination_directory_path)
    TextView tvDestinationPath;

    public ArchiveFragment() {
        super();
        this.setTagText(TAG);
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_archive_main, container,
                false);
        ButterKnife.bind(this, view);
        initialize();
        return view;
    }

    @OnClick(R.id.destination_directory_path)
    public void showDestinationFolderChooserDialog() {
        ((MainActivity) getActivity()).showFolderChooser(CHOOSE_DESTINATION_PATH, Environment.getExternalStorageDirectory().getAbsolutePath());
    }

    @OnClick(R.id.manage_backup_item)
    public void navigateToArchivesManager() {
        ((MainActivity) getActivity()).navigateToFragment(ArchiveListFragment.TAG);
    }

    private void initialize() {
        List<SystemConfiguration> configs = SystemConfiguration.listAll(SystemConfiguration.class);
        if (configs != null && !configs.isEmpty()) {
            SystemConfiguration conf = configs.get(0);
            tvDestinationPath.setText(conf.getValue());
        }
    }

    public void onDirectoryChoosen (String path) {
        tvDestinationPath.setText(path);
    }

    @Override
    public void fabButtonAction() {
    }
}
