package com.example.butymovamoneytracker.di.BalanceFragmentSubComponent;

import com.example.butymovamoneytracker.di.base.BaseComponent;
import com.example.butymovamoneytracker.di.base.BaseComponentBuilder;
import com.example.butymovamoneytracker.di.scopes.FragmentScope;
import com.example.butymovamoneytracker.screens.main.balance.BalanceFragment;
import dagger.Subcomponent;

@FragmentScope
@Subcomponent(modules = {BalanceFragmentModule.class})
public interface BalanceFragmentSubComponent extends BaseComponent<BalanceFragment> {

    @Subcomponent.Builder
    interface Builder extends BaseComponentBuilder<BalanceFragmentSubComponent> {

    }
}
