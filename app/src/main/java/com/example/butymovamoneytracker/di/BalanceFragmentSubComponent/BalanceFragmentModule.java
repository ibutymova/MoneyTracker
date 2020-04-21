package com.example.butymovamoneytracker.di.BalanceFragmentSubComponent;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.butymovamoneytracker.di.keys.ViewModelKey;
import com.example.butymovamoneytracker.di.scopes.FragmentScope;
import com.example.butymovamoneytracker.factories.ViewModelFactory;
import com.example.butymovamoneytracker.screens.main.balance.BalanceFragmentViewModelImpl;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public interface BalanceFragmentModule {
    @Binds
    @IntoMap
    @ViewModelKey(BalanceFragmentViewModelImpl.class)
    ViewModel bindBalanceFragmentViewModel(BalanceFragmentViewModelImpl viewModel);

    @FragmentScope
    @Binds
    ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);
}
