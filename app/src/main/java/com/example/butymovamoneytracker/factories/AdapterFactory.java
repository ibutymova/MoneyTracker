package com.example.butymovamoneytracker.factories;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.butymovamoneytracker.di.scopes.FragmentScope;
import com.example.butymovamoneytracker.screens.main.adapters.TypePage;

import java.util.Map;
import javax.inject.Inject;
import javax.inject.Provider;

@FragmentScope
public class AdapterFactory  {

    private Map<TypePage, Provider<RecyclerView.Adapter>> adapters;

    @Inject
    AdapterFactory(Map<TypePage, Provider<RecyclerView.Adapter>> adapters) {
        this.adapters = adapters;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public <T extends RecyclerView.Adapter> T get(@NonNull TypePage typePage) {
        Provider<RecyclerView.Adapter> provider = adapters.get(typePage);

        if (provider==null){
            throw new IllegalArgumentException("adapter not found");
        }

        return (T) provider.get();
    }
}

