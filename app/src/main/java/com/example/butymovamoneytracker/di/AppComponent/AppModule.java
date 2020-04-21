package com.example.butymovamoneytracker.di.AppComponent;

import com.example.butymovamoneytracker.App;
import com.example.butymovamoneytracker.data.Api;
import com.example.butymovamoneytracker.data.ApiInitializer;
import com.example.butymovamoneytracker.data.GoogleApiClientInitializer;
import com.example.butymovamoneytracker.data.db.AppDatabaseInitializer;
import com.example.butymovamoneytracker.data.db.Database;
import com.example.butymovamoneytracker.data.db.ItemEntityMapper;
import com.example.butymovamoneytracker.di.AddItemActivitySubComponent.AddItemActivitySubComponent;
import com.example.butymovamoneytracker.di.BalanceFragmentSubComponent.BalanceFragmentSubComponent;
import com.example.butymovamoneytracker.di.ItemsFragmentSubComponent.ItemsFragmentSubComponent;
import com.example.butymovamoneytracker.di.MainActivitySubComponent.MainActivitySubComponent;
import com.example.butymovamoneytracker.di.StartActivitySubComponent.StartActivitySubComponent;
import com.example.butymovamoneytracker.di.scopes.AppScope;
import com.example.butymovamoneytracker.prefs.Prefs;
import com.example.butymovamoneytracker.prefs.PrefsImpl;
import com.example.butymovamoneytracker.utils.FormatUtils;
import com.google.android.gms.common.api.GoogleApiClient;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

@Module(subcomponents = {StartActivitySubComponent.class,
                         MainActivitySubComponent.class,
                         ItemsFragmentSubComponent.class,
                         BalanceFragmentSubComponent.class,
                         AddItemActivitySubComponent.class})
abstract class AppModule {

    @AppScope
    @Provides
    static Api provideApi() {
        return new ApiInitializer().initApi();
    }

    @AppScope
    @Provides
    static Database provideDatabase(App app) {
        return new AppDatabaseInitializer().initDatabase(app);
    }

    @AppScope
    @Provides
    static Prefs providePrefs(App app, FormatUtils formatUtils) {
        return new PrefsImpl(app, formatUtils);
    }

    @AppScope
    @Provides
    static FormatUtils provideFormatUtils(App app) {
        return new FormatUtils();
    }

    @AppScope
    @Provides
    static ItemEntityMapper provideMapper(FormatUtils formatUtils) {
        return new ItemEntityMapper(formatUtils);
    }

    @AppScope
    @Provides
    static GoogleApiClient provideGoogleApiClient(App app) {
        GoogleApiClient googleApiClient = new GoogleApiClientInitializer().initGoogleApiClient(app);
        googleApiClient.connect();
        return googleApiClient;
    }

    @Provides
    static CompositeDisposable provideCompositeDisposable(){
        return new CompositeDisposable();
    }
}