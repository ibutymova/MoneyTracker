package com.example.butymovamoneytracker.di.ItemsFragmentSubComponent;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import com.example.butymovamoneytracker.di.keys.TypePageKey;
import com.example.butymovamoneytracker.screens.main.adapters.TypePage;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;

@Module
public abstract class ItemAnimatorModule {

    @Provides
    @IntoMap
    @TypePageKey(TypePage.PAGE_EXPENSE)
    static RecyclerView.ItemAnimator provideItemAnimatorExpense(){
        RecyclerView.ItemAnimator animator = new DefaultItemAnimator();
        animator.setAddDuration(500);
        animator.setRemoveDuration(500);
        return animator;
    }

    @Provides
    @IntoMap
    @TypePageKey(TypePage.PAGE_INCOME)
    static RecyclerView.ItemAnimator provideItemAnimatorIncome(){
        RecyclerView.ItemAnimator animator = new DefaultItemAnimator();
        animator.setAddDuration(500);
        animator.setRemoveDuration(500);
        return animator;
    }
}
