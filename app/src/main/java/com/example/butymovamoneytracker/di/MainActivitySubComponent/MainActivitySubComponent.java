package com.example.butymovamoneytracker.di.MainActivitySubComponent;

import com.example.butymovamoneytracker.di.base.BaseComponent;
import com.example.butymovamoneytracker.di.base.BaseComponentBuilder;
import com.example.butymovamoneytracker.di.scopes.ActivityScope;
import com.example.butymovamoneytracker.screens.main.MainActivity;
import dagger.Subcomponent;

@ActivityScope
@Subcomponent(modules = {MainActivityModule.class})
public interface MainActivitySubComponent extends BaseComponent<MainActivity> {

    @Subcomponent.Builder
    interface Builder extends BaseComponentBuilder<MainActivitySubComponent> {

    }
}
