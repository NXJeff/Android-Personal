package woi.woi.okay.note.preview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;


import woi.woi.okay.NoteApplication;
import woi.woi.okay.R;
import woi.woi.okay.base.view.BaseViewStateFragment;

import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;

import butterknife.Bind;

/**
 * Created by YeekFeiTan on 11/26/2015.
 */
public class NotesFragment extends BaseViewStateFragment<NotesView, NotesPresenter>
        implements NotesView {

    @Bind(R.id.listNotes)
    public ListView mListNotes;

    private NotesComponent notesComponent;
//    private NotesSwipeAdapter mLogNotesAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar mToolbarMain = (Toolbar) view.findViewById(R.id.toolbar_journal_main);
        mToolbarMain.inflateMenu(R.menu.toolbar_menu);

//        mListNotes.setAdapter();

    }

    @Override
    protected int getLayoutRes() {
        return woi.woi.okay.R.layout.fragment_notes;
    }

    @Override
    public ViewState createViewState() {
        return new NotesViewState();
    }

    @Override
    public void onNewViewStateInstance() {
        showNotes();
    }

    @Override
    public NotesPresenter createPresenter() {
        return notesComponent.presenter();
    }

    @Override
    public void showNotes() {
        NotesViewState vs = (NotesViewState) viewState;
        vs.setShowNotes();
    }

    @Override
    public void showNoRecord() {
        NotesViewState vs = (NotesViewState) viewState;
        vs.setShowNoRecord();
    }

    @Override
    protected void injectDependencies() {
        notesComponent = DaggerNotesComponent.builder().notesAppComponent(NoteApplication.getNoteComponent()).build();
    }
}
