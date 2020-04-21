package com.example.butymovamoneytracker.di.AddItemActivitySubComponent;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.butymovamoneytracker.di.keys.ViewModelKey;
import com.example.butymovamoneytracker.di.scopes.ActivityScope;
import com.example.butymovamoneytracker.factories.ViewModelFactory;
import com.example.butymovamoneytracker.screens.addItem.AddItemActivityViewModelImpl;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public interface AddItemActivityModule {
    @Binds
    @IntoMap
    @ViewModelKey(AddItemActivityViewModelImpl.class)
    ViewModel bindAddItemActivityViewModel(AddItemActivityViewModelImpl viewModel);

    @ActivityScope
    @Binds
    ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);
}
