package com.example.butymovamoneytracker.di.AppComponent;

import com.example.butymovamoneytracker.App;
import com.example.butymovamoneytracker.di.ComponentsHolder;
import com.example.butymovamoneytracker.di.scopes.AppScope;
import com.example.butymovamoneytracker.receivers.TimeZoneReceiver;

import dagger.BindsInstance;
import dagger.Component;

@AppScope
@Component(modules = {AppModule.class, BuilderModule.class, StatesModule.class})
public interface AppComponent {

   @Component.Builder
   interface Builder{
      AppComponent build();

      @BindsInstance
      AppComponent.Builder withApp(App app);
   }

   void injectComponentsHolder(ComponentsHolder componentsHolder);

   void injectTimeZoneReceiver(TimeZoneReceiver timeZoneReceiver);
}
