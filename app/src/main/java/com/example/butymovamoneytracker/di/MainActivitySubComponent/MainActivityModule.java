package com.example.butymovamoneytracker.di.MainActivitySubComponent;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.butymovamoneytracker.di.keys.ViewModelKey;
import com.example.butymovamoneytracker.di.scopes.ActivityScope;
import com.example.butymovamoneytracker.factories.ViewModelFactory;
import com.example.butymovamoneytracker.screens.main.MainActivityViewModelImpl;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class MainActivityModule {
    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModelImpl.class)
    abstract ViewModel bindMainActivityViewModel(MainActivityViewModelImpl viewModel);

    @ActivityScope
    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);
}
