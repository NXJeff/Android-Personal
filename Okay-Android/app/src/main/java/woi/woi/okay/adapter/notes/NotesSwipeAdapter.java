package woi.woi.okay.adapter.notes;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.hannesdorfmann.annotatedadapter.AbsListViewAnnotatedAdapter;
import com.hannesdorfmann.annotatedadapter.annotation.ViewField;
import com.hannesdorfmann.annotatedadapter.annotation.ViewType;

import java.util.List;

import butterknife.Bind;
import woi.woi.okay.R;
import woi.woi.okay.model.notes.Note;

/**
 * Created by YeekFeiTan on 11/26/2015.
 */
public class NotesSwipeAdapter extends BaseSwipeAdapter {
    private Context context;
    public static List<Note> mLog;
    private Note note;

    public NotesSwipeAdapter(Context context, List<Note> mylist) {
        this.context = context;
        this.mLog = mylist;
    }

    @Override
    public int getSwipeLayoutResourceId(int i) {
        return R.id.swipe;
    }

    @Override
    public int getCount() {
        return mLog.size();
    }

    @Override
    public Note getItem(int position) {
        return mLog.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View generateView(final int i, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.note_list_item, null);
    }

    @Override
    public void fillValues(final int i, View view) {
        note = getItem(i);

        final SwipeLayout swipeLayout = (SwipeLayout) view.findViewById(getSwipeLayoutResourceId(i));
        final RelativeLayout clicking = (RelativeLayout) view.findViewById(R.id.clicking);
        clicking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                note = getItem(i);
//                MainActivity mainActivity = (MainActivity) context;
//                mainActivity.noteClicked(note, i);
            }
        });
        swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onStartOpen(SwipeLayout swipeLayout) {
            }

            @Override
            public void onOpen(SwipeLayout swipeLayo) {
                clicking.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        swipeLayout.close();
                    }
                });
            }

            @Override
            public void onStartClose(SwipeLayout swipepyout) {
            }

            @Override
            public void onClose(SwipeLayout swipeLayout) {
                clicking.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        note = getItem(i);
//                        MainActivity mainActivity = (MainActivity) context;
//                        mainActivity.noteClicked(note, i);
                    }
                });
            }

            @Override
            public void onUpdate(SwipeLayout swipeLayout, int i, int i1) {
            }

            @Override
            public void onHandRelease(SwipeLayout swipeLayout, float v, float v1) {
            }
        });
        TextView text = (TextView) view.findViewById(R.id.txtNote);
        text.setText(note.getNote());
        TextView date = (TextView) view.findViewById(R.id.txtDate);
        date.setText(DateFormat.format("MMM dd, yyy", note.getDate()));
        ImageView trash = (ImageView) view.findViewById(R.id.trash);
        trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                note = getItem(i);
//                note.delete();
//                MainActivity mainActivity = (MainActivity) context;
//                mainActivity.deleteListViaSwipe();
            }
        });
    }

}

