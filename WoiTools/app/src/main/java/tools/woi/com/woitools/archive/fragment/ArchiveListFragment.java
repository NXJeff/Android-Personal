package tools.woi.com.woitools.archive.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tools.woi.com.woitools.R;
import tools.woi.com.woitools.archive.activity.AddArchiveActivity;
import tools.woi.com.woitools.archive.adapter.ArchiveItemAdapter;
import tools.woi.com.woitools.archive.domain.ArchiveItem;
import tools.woi.com.woitools.archive.domain.ArchiveMode;
import tools.woi.com.woitools.base.BaseFragment;
import tools.woi.com.woitools.base.FabActionInterface;

/**
 * Created by YeekFeiTan on 12/29/2015.
 */
public class ArchiveListFragment extends BaseFragment {

    public static final String TAG = "tools.woi.com.woitools.archive.fragment.ArchiveListFragment";

    @Bind(R.id.archive_recycler_view)
    RecyclerView mRecyclerView;

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    List<ArchiveItem> archiveItems;

    public ArchiveListFragment() {
        super();
        this.setTagText(TAG);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_archive_list, container,
                false);
        ButterKnife.bind(this, view);
        initialize();
        initFabOnFragment();
        return view;
    }

    private void initialize() {
        archiveItems = ArchiveItem.listAll(ArchiveItem.class);
        mAdapter = new ArchiveItemAdapter(this, archiveItems);
        mRecyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    @Override
    public void fabButtonAction() {
        Intent i = new Intent(getActivity(), AddArchiveActivity.class);
        startActivityForResult(i, 111);
    }

    private void initFabOnFragment() {
        getFab().setImageDrawable(new IconicsDrawable(getActivity())
                .icon(GoogleMaterial.Icon.gmd_library_add)
                .color(Color.WHITE));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getFab().setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.yellow_900, getActivity().getTheme())));
        } else {
            getFab().setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.yellow_900)));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == 111) {
            if(resultCode == Activity.RESULT_OK) {
                initialize();
            }
        }
    }
}
