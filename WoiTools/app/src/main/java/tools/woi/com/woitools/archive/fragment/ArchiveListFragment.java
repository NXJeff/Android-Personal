package tools.woi.com.woitools.archive.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tools.woi.com.woitools.R;
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
        return view;
    }

    private void initialize() {
        archiveItems = ArchiveItem.listAll(ArchiveItem.class);
        mAdapter = new ArchiveItemAdapter(archiveItems);
        mRecyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    @Override
    public void fabButtonAction() {
        ArchiveItem item = new ArchiveItem();
        item.setName("Whatsapp");
        item.setSourcePath("/sdcard/");
        item.setArchieveMode(ArchiveMode.OlderThan);
        item.setMonth(1);
        item.save();
        archiveItems.add(item);
        mAdapter.notifyDataSetChanged();
    }
}
