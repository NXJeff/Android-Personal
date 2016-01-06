package tools.woi.com.woitools.archive.fragment;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import tools.woi.com.woitools.MainActivity;
import tools.woi.com.woitools.R;
import tools.woi.com.woitools.archive.service.ArchiveService;
import tools.woi.com.woitools.base.BaseFragment;
import tools.woi.com.woitools.system.domain.SystemConfiguration;
import tools.woi.com.woitools.system.domain.SystemPropertyKey;

/**
 * Created by YeekFeiTan on 12/29/2015.
 */
public class ArchiveFragment extends BaseFragment {

    public static final String CHOOSE_DESTINATION_PATH = "tools.woi.com.woitools.archive.fragment.ArchiveFragment.destinationPath";
    public static final String TAG = "tools.woi.com.woitools.archive.fragment.ArchiveFragment";

    @Bind(R.id.destination_directory_path)
    TextView tvDestinationPath;

    @Bind(R.id.tvArchiveLog)
    TextView tvArchiveLog;

    SystemConfiguration destinationConfiguration;

    public ArchiveFragment() {
        super();
        this.setTagText(TAG);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_archive_main, container,
                false);
        ButterKnife.bind(this, view);
        initialize();
        initFabOnFragment();

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
        destinationConfiguration = SystemConfiguration.find(SystemPropertyKey.Archieve_DestinationPath);
        if (destinationConfiguration != null) {
            tvDestinationPath.setText(destinationConfiguration.getValue());
        }
    }

    private void initFabOnFragment() {
        getFab().setImageDrawable(new IconicsDrawable(getActivity())
                .icon(GoogleMaterial.Icon.gmd_archive)
                .color(Color.WHITE));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getFab().setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green_300, getActivity().getTheme())));
        } else {
            getFab().setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green_300)));
        }
    }

    public void onDirectoryChoosen(String path) {
        if (destinationConfiguration == null) {
            destinationConfiguration = new SystemConfiguration(SystemPropertyKey.Archieve_DestinationPath, path);
        }
        destinationConfiguration.setValue(path);
        tvDestinationPath.setText(path);
        destinationConfiguration.save();
    }

    @Override
    public void fabButtonAction() {
        MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                .title(R.string.archive_manager)
                .content(R.string.confirmation_archive)
                .positiveText(R.string.archive_now)
                .negativeText(R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        startArchive();
                    }
                })
                .show();
    }

    private void startArchive() {
        getContext().startService(new Intent(getContext(), ArchiveService.class));
    }

    public void onEvent(String event) {
        addLineToArchiveLog(event);
    };

    public void addLineToArchiveLog(String line) {
        tvArchiveLog.append("\n" + line);
    }
}
