package woi.woi.okay;

import com.orm.SugarApp;

import woi.woi.okay.dagger.DaggerNotesAppComponent;
import woi.woi.okay.dagger.NotesAppComponent;


/**
 * Created by YeekFeiTan on 11/26/2015.
 */

public class NoteApplication extends SugarApp {

    private static NotesAppComponent noteComponent;

    @Override public void onCreate() {
        super.onCreate();
        noteComponent = DaggerNotesAppComponent.create();
    }

    public static NotesAppComponent getNoteComponent() {
        return noteComponent;
    }
}
