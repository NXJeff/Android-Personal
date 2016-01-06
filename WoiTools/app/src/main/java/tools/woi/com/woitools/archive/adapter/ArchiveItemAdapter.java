package tools.woi.com.woitools.archive.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import tools.woi.com.woitools.R;
import tools.woi.com.woitools.archive.activity.AddArchiveActivity;
import tools.woi.com.woitools.archive.domain.ArchiveItem;
import tools.woi.com.woitools.archive.domain.ArchiveMode;

/**
 * Created by YeekFeiTan on 12/29/2015.
 */
public class ArchiveItemAdapter extends
        RecyclerView.Adapter<ArchiveItemAdapter.ViewHolder> {

    private List<ArchiveItem> mArchiveItems;
    private Fragment fragment;

    // Pass in the contact array into the constructor
    public ArchiveItemAdapter(Fragment fragment, List<ArchiveItem> mArchiveItems) {
        this.mArchiveItems = mArchiveItems;
        this.fragment = fragment;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        @Bind(R.id.archive_name) TextView nameTextView;
        @Bind(R.id.firstLine) TextView tvFirstLine;
        @Bind(R.id.secondLine) TextView tvSecondLine;
        @Bind(R.id.archive_button) Button messageButton;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public ArchiveItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.item_archive, parent, false);

        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ArchiveItemAdapter.ViewHolder viewHolder, int position) {
        final ArchiveItem item = mArchiveItems.get(position);

        viewHolder.nameTextView.setText(item.getName());
        viewHolder.tvFirstLine.setText(item.getSourcePath());

        String syncText;
        if(item.getArchieveMode().equals(ArchiveMode.OlderThan)) {
            syncText = "Archive files older than " + item.getDayOrMonthNumber() + ". ";
        } else {
            syncText = "Archive files more than " + item.getMaxFilesNo() + ". ";
        }

        if(item.getLastSync()!=null) {
            syncText += "Last archived on " + item.getLastSync().toString();
        }

        viewHolder.tvSecondLine.setText(syncText);

        Button button = viewHolder.messageButton;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(fragment.getActivity(), AddArchiveActivity.class);
                i.putExtra("id", item.getId());
                fragment.startActivityForResult(i, 111);
            }
        });

    }

    // Return the total count of items
    @Override
    public int getItemCount() {
        return mArchiveItems.size();
    }
}