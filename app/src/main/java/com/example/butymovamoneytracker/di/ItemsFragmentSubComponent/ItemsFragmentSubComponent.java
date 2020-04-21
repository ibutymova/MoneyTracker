package com.example.butymovamoneytracker.di.ItemsFragmentSubComponent;

import com.example.butymovamoneytracker.di.base.BaseComponent;
import com.example.butymovamoneytracker.di.base.BaseComponentBuilder;
import com.example.butymovamoneytracker.di.scopes.FragmentScope;
import com.example.butymovamoneytracker.screens.main.items.ItemsFragment;

import dagger.Subcomponent;

@FragmentScope
@Subcomponent(modules = {ItemsFragmentModule.class, ItemAnimatorModule.class, AdapterModule.class})
public interface ItemsFragmentSubComponent extends BaseComponent<ItemsFragment> {

    @Subcomponent.Builder
    interface Builder extends BaseComponentBuilder<ItemsFragmentSubComponent> {

    }
}
