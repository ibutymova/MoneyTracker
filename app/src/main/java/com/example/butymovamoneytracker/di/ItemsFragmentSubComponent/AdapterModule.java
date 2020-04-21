package com.example.butymovamoneytracker.di.ItemsFragmentSubComponent;

import androidx.recyclerview.widget.RecyclerView;

import com.example.butymovamoneytracker.di.keys.TypePageKey;
import com.example.butymovamoneytracker.di.scopes.FragmentScope;
import com.example.butymovamoneytracker.screens.main.adapters.TypePage;
import com.example.butymovamoneytracker.screens.main.items.adapters.ItemsAdapter;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class AdapterModule {

    @Binds
    @IntoMap
    @FragmentScope
    @TypePageKey(TypePage.PAGE_EXPENSE)
    abstract RecyclerView.Adapter bindItemsExpenseAdapter(ItemsAdapter adapter);

    @Binds
    @IntoMap
    @FragmentScope
    @TypePageKey(TypePage.PAGE_INCOME)
    abstract RecyclerView.Adapter bindItemsIncomeAdapter(ItemsAdapter adapter);
}
