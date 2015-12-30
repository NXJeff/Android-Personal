package tools.woi.com.woitools.archive.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import tools.woi.com.woitools.R;
import tools.woi.com.woitools.archive.domain.ArchiveItem;

/**
 * Created by YeekFeiTan on 12/29/2015.
 */
public class ArchiveItemAdapter extends
        RecyclerView.Adapter<ArchiveItemAdapter.ViewHolder> {

    private List<ArchiveItem> mArchiveItems;

    // Pass in the contact array into the constructor
    public ArchiveItemAdapter(List<ArchiveItem> mArchiveItems) {
        this.mArchiveItems = mArchiveItems;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView nameTextView;
        public Button messageButton;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            nameTextView = (TextView) itemView.findViewById(R.id.archive_name);
            messageButton = (Button) itemView.findViewById(R.id.archive_button);
        }
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public ArchiveItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_archive, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(ArchiveItemAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        ArchiveItem contact = mArchiveItems.get(position);

        // Set item views based on the data model
        TextView textView = viewHolder.nameTextView;
        textView.setText(contact.getName());

        Button button = viewHolder.messageButton;

//        if (contact.isOnline()) {
//            button.setText("Message");
//            button.setEnabled(true);
//        } else {
//            button.setText("Offline");
//            button.setEnabled(false);
//        }

    }

    // Return the total count of items
    @Override
    public int getItemCount() {
        return mArchiveItems.size();
    }
}