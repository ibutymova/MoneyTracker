package com.example.butymovamoneytracker.di.StartActivitySubComponent;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.butymovamoneytracker.di.keys.ViewModelKey;
import com.example.butymovamoneytracker.di.scopes.ActivityScope;
import com.example.butymovamoneytracker.factories.ViewModelFactory;
import com.example.butymovamoneytracker.screens.StartActivityViewModelImpl;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public interface StartActivityModule {
    @Binds
    @IntoMap
    @ViewModelKey(StartActivityViewModelImpl.class)
    ViewModel bindStartActivityViewModel(StartActivityViewModelImpl viewModel);

    @ActivityScope
    @Binds
    ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);
}
