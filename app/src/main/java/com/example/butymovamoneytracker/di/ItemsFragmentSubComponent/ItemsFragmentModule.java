package com.example.butymovamoneytracker.di.ItemsFragmentSubComponent;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.butymovamoneytracker.di.keys.ViewModelKey;
import com.example.butymovamoneytracker.di.qualifiers.Expense;
import com.example.butymovamoneytracker.di.qualifiers.Income;
import com.example.butymovamoneytracker.di.scopes.FragmentScope;
import com.example.butymovamoneytracker.factories.ViewModelFactory;
import com.example.butymovamoneytracker.screens.main.adapters.TypePage;
import com.example.butymovamoneytracker.screens.main.items.ItemsExpenseFragmentViewModelImpl;
import com.example.butymovamoneytracker.screens.main.items.ItemsIncomeFragmentViewModelImpl;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;

@Module
abstract class ItemsFragmentModule {
    @Binds
    @IntoMap
    @ViewModelKey(ItemsIncomeFragmentViewModelImpl.class)
    abstract ViewModel bindItemsIncomeFragmentViewModel(ItemsIncomeFragmentViewModelImpl viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ItemsExpenseFragmentViewModelImpl.class)
    abstract ViewModel bindItemsExpenseFragmentViewModel(ItemsExpenseFragmentViewModelImpl viewModel);

    @FragmentScope
    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);

    @FragmentScope
    @Provides
    @Expense
    static TypePage provideTypePageExpense(){
        return TypePage.PAGE_EXPENSE;
    }

    @FragmentScope
    @Provides
    @Income
    static TypePage provideTypePageIncome(){
        return TypePage.PAGE_INCOME;
    }
}
