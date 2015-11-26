package test.woi.okay;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import test.woi.okay.model.Note;

public class MainActivity extends AppCompatActivity implements SlidingUpPanelLayout.PanelSlideListener, AddNewNoteFragment.MainFragment,
        NoteListFragment.MainFragment, FragmentNotesOld.FragmentNotesOldCallbacks {

    public static int REQ_CODE_SPEECH_INPUT = 100;
    public static String VOICE = "prompt_speech_input";
    private boolean slidindPanelVisibility = false;
    private SlidingUpPanelLayout mSlidingLayout;
    private AddNewNoteFragment mFragmentAddNewNote;
    private FragmentNotesOld mFragmentNotesOld;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        mSlidingLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        mSlidingLayout.setPanelSlideListener(this);
        mSlidingLayout.setTouchEnabled(false);

        getSupportFragmentManager().beginTransaction().replace(R.id.container_list, new NoteListFragment()).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public void newNote() {
        mFragmentAddNewNote = new AddNewNoteFragment();
        Bundle extras = new Bundle();
        REQ_CODE_SPEECH_INPUT = 100;
        extras.putInt(VOICE, 0);
        mFragmentAddNewNote.setArguments(extras);
        getSupportFragmentManager().beginTransaction().replace(R.id.container_notes, mFragmentAddNewNote).commit();
        mSlidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
    }

    @Override
    public void newNoteVoice() {
        mFragmentAddNewNote = new AddNewNoteFragment();
        Bundle extras = new Bundle();
        REQ_CODE_SPEECH_INPUT = 100;
        extras.putInt(VOICE, 1);
        mFragmentAddNewNote.setArguments(extras);
        getSupportFragmentManager().beginTransaction().replace(R.id.container_notes, mFragmentAddNewNote).commit();
        mSlidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
    }

    public void noteClicked(Note note, int position) {
        REQ_CODE_SPEECH_INPUT = 100;
        mFragmentAddNewNote = null;
        mFragmentNotesOld = new FragmentNotesOld();
        mSlidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
        mFragmentNotesOld.setLogNotes(note, position);
        getSupportFragmentManager().beginTransaction().replace(R.id.container_notes, mFragmentNotesOld).commit();
    }

    @Override
    public void saveNewNote() {
        getSupportFragmentManager().beginTransaction().replace(R.id.container_list, new NoteListFragment()).commit();
        mSlidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        Snackbar.make(mSlidingLayout, "Note is created.", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public void updateNote() {
        mSlidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        getSupportFragmentManager().beginTransaction().replace(R.id.container_list, new NoteListFragment()).commit();
        Snackbar.make(mSlidingLayout, "Note is saved.", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public void justViewNote() {
        mSlidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
    }

    public void deleteListViaSwipe() {
        getSupportFragmentManager().beginTransaction().replace(R.id.container_list, new NoteListFragment()).commit();
    }

    @Override
    public void deleteNote() {
        mSlidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        getSupportFragmentManager().beginTransaction().replace(R.id.container_list, new NoteListFragment()).commit();
        Snackbar.make(mSlidingLayout, "Note is deleted.", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }


    /**
     * Handle sliding up panel listener
     */

    @Override
    public void onPanelSlide(View panel, float slideOffset) {

    }

    @Override
    public void onPanelCollapsed(View panel) {
        slidindPanelVisibility = false;
        REQ_CODE_SPEECH_INPUT = 0;

        if (mFragmentAddNewNote != null) {
            getSupportFragmentManager().beginTransaction().detach(mFragmentAddNewNote).commit();
        }

        if (mFragmentNotesOld != null) {
            getSupportFragmentManager().beginTransaction().detach(mFragmentNotesOld).commit();
        }
    }

    @Override
    public void onPanelExpanded(View panel) {
        slidindPanelVisibility = true;
        if (mFragmentAddNewNote != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }


    }

    @Override
    public void onPanelAnchored(View panel) {

    }

    @Override
    public void onPanelHidden(View panel) {

    }

    public void onBackPressed() {
        if (slidindPanelVisibility) {
            if (mFragmentAddNewNote != null) {
                mFragmentAddNewNote.onSave();
            }
            if (mFragmentNotesOld != null) {
                mFragmentNotesOld.onSave();
            }
            mSlidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {
            super.onBackPressed();
        }

    }
}
