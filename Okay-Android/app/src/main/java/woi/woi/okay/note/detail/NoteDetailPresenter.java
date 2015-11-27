package woi.woi.okay.note.detail;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;
import woi.woi.okay.note.preview.NotesView;

/**
 * Created by YeekFeiTan on 11/26/2015.
 */
public class NoteDetailPresenter  extends MvpBasePresenter<NoteDetailView> {

    private EventBus eventBus;

    @Inject
    public NoteDetailPresenter(EventBus eventBus) {
        this.eventBus = eventBus;
    }
}
