package com.example.butymovamoneytracker.di.StartActivitySubComponent;

import com.example.butymovamoneytracker.di.base.BaseComponent;
import com.example.butymovamoneytracker.di.base.BaseComponentBuilder;
import com.example.butymovamoneytracker.di.scopes.ActivityScope;
import com.example.butymovamoneytracker.screens.StartActivity;

import dagger.Subcomponent;

@ActivityScope
@Subcomponent(modules = {StartActivityModule.class})
public interface StartActivitySubComponent extends BaseComponent<StartActivity> {

    @Subcomponent.Builder
    interface Builder extends BaseComponentBuilder<StartActivitySubComponent> {

    }
}
