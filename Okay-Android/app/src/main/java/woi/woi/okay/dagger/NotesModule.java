package woi.woi.okay.dagger;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.greenrobot.event.EventBus;

/**
 * Created by YeekFeiTan on 11/26/2015.
 */
@Module()
public class NotesModule {

    @Singleton
    @Provides
    public EventBus providesEventBus() {
        return EventBus.getDefault();
    }
}

