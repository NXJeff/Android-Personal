package woi.woi.okay.note.preview;

import javax.inject.Singleton;

import dagger.Component;
import woi.woi.okay.dagger.NotesAppComponent;
import woi.woi.okay.dagger.NotesModule;

/**
 * Created by YeekFeiTan on 11/26/2015.
 */
@Singleton
@Component(
        modules = NotesModule.class, dependencies = NotesAppComponent.class
)
public interface NotesComponent {

    public NotesPresenter presenter();
}
