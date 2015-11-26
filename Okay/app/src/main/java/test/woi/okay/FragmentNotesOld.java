package test.woi.okay;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import test.woi.okay.adapter.NotesSwipeAdapter;
import test.woi.okay.model.Note;

/**
 * Created by YeekFeiTan on 11/25/2015.
 */
public class FragmentNotesOld extends Fragment {
    private static String NOTE_LOG = "note_log_position";
    private Note note;
    private EditText mEtNote;
    private int LogPosition;
    private Runnable mMyRunnable;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            NotesSwipeAdapter logNotesSwipeAdapter = new NotesSwipeAdapter(getActivity(), Note.listAll(Note.class));
            if (logNotesSwipeAdapter.getCount() > 0)
                note = logNotesSwipeAdapter.getItem(savedInstanceState.getInt(NOTE_LOG));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_new_note, container, false);
        Toolbar mToolbarNote = (Toolbar) v.findViewById(R.id.toolbar_note);
        mToolbarNote.setNavigationIcon(R.mipmap.ic_arrow_back);
        mToolbarNote.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                onSave();
            }
        });
        mToolbarNote.inflateMenu(R.menu.menu_delete);
        mToolbarNote.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                switch (itemId) {
                    case R.id.action_delete:
                        note.delete();
                        mCallbacks.deleteNote();
                        mEtNote.setText("");
                        break;
                    case R.id.action_voice:
                        promptSpeechInput();
                        break;
                }
                return false;
            }
        });
        if (note != null) {
            TextView mTxtDate = (TextView) v.findViewById(R.id.txtDate);
            mTxtDate.setText(DateFormat.format("MMM dd, yyy", note.getDate()));
            mEtNote = (EditText) v.findViewById(R.id.etNote);
            mEtNote.setText(note.getNote());
        }
        return v;
    }

    public void onSave() {
        mMyRunnable = new Runnable() {
            @Override
            public void run() {
                if (!note.getNote().equals(mEtNote.getText().toString())) {
                    Note note = FragmentNotesOld.this.note;
                    note.setNote(mEtNote.getText().toString());
                    note.setDate(new Date());
                    note.save();
                    mCallbacks.updateNote();
                    mEtNote.setText("");
                } else {
                    mCallbacks.justViewNote();
                }
            }
        };
        /**
         * Really don't want to run a runbable but view is getting caught up on
         * the softKeyboard
         */
        Handler myHandler = new Handler();
        myHandler.postDelayed(mMyRunnable, 150);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(NOTE_LOG, LogPosition);
    }

    /**
     * Sets LogNote from activity
     */
    public void setLogNotes(Note note, int position) {
        this.note = note;
        LogPosition = position;
    }

    /**
     * Speech Input
     * Voice search then implements search method based on result
     */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say Something");
        try {
            startActivityForResult(intent, MainActivity.REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getActivity().getApplicationContext(), "Not Supported", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MainActivity.REQ_CODE_SPEECH_INPUT) {
            if (resultCode == Activity.RESULT_OK && null != data) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                mEtNote.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
                if (mEtNote.getText().toString().length() == 0)
                    mEtNote.setText(result.get(0));
                else {
                    mEtNote.setText(mEtNote.getText().toString() + " " + result.get(0));
                }
                int position = mEtNote.length();
                Editable etext = mEtNote.getText();
                Selection.setSelection(etext, position);
            }
        }
    }

    /**
     * Interface
     */
    private FragmentNotesOldCallbacks mCallbacks;

    public interface FragmentNotesOldCallbacks {
        void updateNote();

        void deleteNote();

        void justViewNote();
    }


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
            mCallbacks = (FragmentNotesOldCallbacks) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }


}