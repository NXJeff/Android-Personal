package com.woi.tools.archive.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.woi.tools.MainActivity;
import com.woi.tools.R;
import com.woi.tools.global.system.domain.SystemConfiguration;
import com.woi.tools.global.system.domain.SystemPropertyKey;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by YeekFeiTan on 12/29/2015.
 */
public class ArchiveFragment extends Fragment {

    @Bind(R.id.destination_directory_path)
    TextView tvDestinationPath;

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
        ((MainActivity) getActivity()).showFolderChooser("/sdcard/");
    }

    private void initialize() {
        List<SystemConfiguration> configs = SystemConfiguration.listAll(SystemConfiguration.class);
        if(configs != null && !configs.isEmpty()) {
            SystemConfiguration conf = configs.get(0);
            tvDestinationPath.setText(conf.getValue());
        }
    }
}
