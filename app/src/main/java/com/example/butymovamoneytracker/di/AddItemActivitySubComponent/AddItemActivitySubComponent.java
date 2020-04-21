package com.example.butymovamoneytracker.di.AddItemActivitySubComponent;

import com.example.butymovamoneytracker.di.base.BaseComponent;
import com.example.butymovamoneytracker.di.base.BaseComponentBuilder;
import com.example.butymovamoneytracker.di.scopes.ActivityScope;
import com.example.butymovamoneytracker.screens.addItem.AddItemActivity;
import dagger.Subcomponent;

@ActivityScope
@Subcomponent(modules = {AddItemActivityModule.class})
public interface AddItemActivitySubComponent extends BaseComponent<AddItemActivity> {

    @Subcomponent.Builder
    interface Builder extends BaseComponentBuilder<AddItemActivitySubComponent> {

    }
}
