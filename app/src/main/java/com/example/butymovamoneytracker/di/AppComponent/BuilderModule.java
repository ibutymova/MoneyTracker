package com.example.butymovamoneytracker.di.AppComponent;

import com.example.butymovamoneytracker.di.AddItemActivitySubComponent.AddItemActivitySubComponent;
import com.example.butymovamoneytracker.di.BalanceFragmentSubComponent.BalanceFragmentSubComponent;
import com.example.butymovamoneytracker.di.ItemsFragmentSubComponent.ItemsFragmentSubComponent;
import com.example.butymovamoneytracker.di.MainActivitySubComponent.MainActivitySubComponent;
import com.example.butymovamoneytracker.di.StartActivitySubComponent.StartActivitySubComponent;
import com.example.butymovamoneytracker.di.base.BaseComponentBuilder;
import com.example.butymovamoneytracker.screens.StartActivity;
import com.example.butymovamoneytracker.screens.addItem.AddItemActivity;
import com.example.butymovamoneytracker.screens.main.MainActivity;
import com.example.butymovamoneytracker.screens.main.balance.BalanceFragment;
import com.example.butymovamoneytracker.screens.main.items.ItemsFragment;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;

@Module
public interface BuilderModule {

    @Binds
    @IntoMap
    @ClassKey(MainActivity.class)
    BaseComponentBuilder bindMainActivityBuilder(MainActivitySubComponent.Builder builder);

    @Binds
    @IntoMap
    @ClassKey(ItemsFragment.class)
    BaseComponentBuilder bindItemsFragmentBuilder(ItemsFragmentSubComponent.Builder builder);

    @Binds
    @IntoMap
    @ClassKey(StartActivity.class)
    BaseComponentBuilder bindStartActivityBuilder(StartActivitySubComponent.Builder builder );

    @Binds
    @IntoMap
    @ClassKey(BalanceFragment.class)
    BaseComponentBuilder bindBalanceFragmentBuilder(BalanceFragmentSubComponent.Builder builder);

    @Binds
    @IntoMap
    @ClassKey(AddItemActivity.class)
    BaseComponentBuilder bindAddItemActivityBuilder(AddItemActivitySubComponent.Builder builder);

}
