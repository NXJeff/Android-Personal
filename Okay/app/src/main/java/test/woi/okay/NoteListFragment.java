package test.woi.okay;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import github.chenupt.dragtoplayout.DragTopLayout;
import test.woi.okay.adapter.NotesSwipeAdapter;
import test.woi.okay.model.Note;

/**
 * A placeholder fragment containing a simple view.
 */
public class NoteListFragment extends Fragment {

    public NoteListFragment() {
    }
    private View v;
    private NotesSwipeAdapter mLogNotesAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_main, container, false);
        Toolbar mToolbarMain = (Toolbar) v.findViewById(R.id.toolbar_journal_main);
        mToolbarMain.inflateMenu(R.menu.menu_main);
        mToolbarMain.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                switch (itemId) {
                    case R.id.action_new:
                        mCallbacks.newNote();
                        break;
                    case R.id.action_voice:
                        mCallbacks.newNoteVoice();
                        break;
                }
                return false;
            }
        });

        ListView mListNotes = (ListView) v.findViewById(R.id.listNotes);
        mLogNotesAdapter = new NotesSwipeAdapter(getActivity(), Note.listAll(Note.class));
        mListNotes.setAdapter(mLogNotesAdapter);
        mLogNotesAdapter.notifyDataSetChanged();
        updateList();
        /**
         * TODO currently disabled till next update
         */
        DragTopLayout mDragLayout = (DragTopLayout) v.findViewById(R.id.drag_layout);
        mDragLayout.setOverDrag(false);
        mDragLayout.setTouchMode(false);
        mDragLayout.toggleTopView();

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).newNote();
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });

        return v;
    }

    private void updateList() {
        TextView noNotes = (TextView) v.findViewById(R.id.noNotes);
        if (mLogNotesAdapter.getCount() == 0) {
            noNotes.setVisibility(View.VISIBLE);
        } else {
            noNotes.setVisibility(View.GONE);
        }
    }

    /**
     * Callbacks
     */
    private MainFragment mCallbacks;

    public interface MainFragment {
        void newNote();

        void newNoteVoice();
    }

//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        try {
//            mCallbacks = (MainFragment) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
//        }
//    }

    @TargetApi(23)
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onAttachToContext(context);
    }

    /*
     * Deprecated on API 23
     * Use onAttachToContext instead
     */
    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            onAttachToContext(activity);
        }
    }

    /*
     * Called when the fragment attaches to the context
     */
    protected void onAttachToContext(Context context) {
        try {
            mCallbacks = (MainFragment) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    private void showMessageOnSnackBar(int arg) {
        String msg;

        switch(arg) {
            case 0:
                msg = "Note is created.";
                break;
            case 1:
                msg = "Note is saved.";
                break;
            case 2:
                msg = "Note is deleted.";
                break;
        }


        Snackbar.make(v, "Note is created.", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
}
