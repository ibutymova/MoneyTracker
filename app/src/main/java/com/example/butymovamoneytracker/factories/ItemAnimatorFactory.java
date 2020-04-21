package com.example.butymovamoneytracker.factories;

import androidx.recyclerview.widget.RecyclerView;

import com.example.butymovamoneytracker.di.scopes.FragmentScope;
import com.example.butymovamoneytracker.screens.main.adapters.TypePage;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;

@FragmentScope
public class ItemAnimatorFactory {
    private Map<TypePage, Provider<RecyclerView.ItemAnimator>> animators;

    @Inject
    ItemAnimatorFactory(Map<TypePage, Provider<RecyclerView.ItemAnimator>> animators) {
        this.animators = animators;
    }

    public RecyclerView.ItemAnimator get(TypePage typePage){
        Provider provider = animators.get(typePage);
        if (provider==null)
            throw  new IllegalArgumentException("itemAnimator not found");

        return (RecyclerView.ItemAnimator) provider.get();
    }
}
