package woi.woi.okay.note.preview;

import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;

/**
 * Created by YeekFeiTan on 11/26/2015.
 */
public class NotesViewState implements ViewState<NotesView> {

    final int STATE_SHOW_NOTES = 0;
    final int STATE_SHOW_NO_RECORD = 1;

    int state = STATE_SHOW_NOTES;

    public void apply(NotesView view, boolean retained) {

        switch (state) {
            case STATE_SHOW_NOTES:
                view.showNotes();
                break;

            case STATE_SHOW_NO_RECORD:
                view.showNoRecord();
                break;
        }
    }

    public void setShowNotes() {
        state = STATE_SHOW_NOTES;
    }

    public void setShowNoRecord() {
        state = STATE_SHOW_NO_RECORD;
    }

}
