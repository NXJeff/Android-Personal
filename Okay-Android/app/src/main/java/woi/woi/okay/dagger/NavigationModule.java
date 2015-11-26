package woi.woi.okay.dagger;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import woi.woi.okay.IntentStarter;

/**
 * Created by YeekFeiTan on 11/26/2015.
 */
@Module()
public class NavigationModule {

    @Singleton
    @Provides
    public IntentStarter providesIntentStarter() {
        return new IntentStarter();
    }
}
