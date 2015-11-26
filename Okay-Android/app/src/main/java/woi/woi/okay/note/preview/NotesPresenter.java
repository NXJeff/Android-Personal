package woi.woi.okay.note.preview;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

/**
 * Created by YeekFeiTan on 11/26/2015.
 */
public class NotesPresenter extends MvpBasePresenter<NotesView> {

    private EventBus eventBus;

    @Inject
    public NotesPresenter(EventBus eventBus) {
        this.eventBus = eventBus;
    }
}
