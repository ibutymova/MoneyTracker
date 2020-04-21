package com.example.butymovamoneytracker.di;

import com.example.butymovamoneytracker.App;
import com.example.butymovamoneytracker.di.AppComponent.AppComponent;
import com.example.butymovamoneytracker.di.AppComponent.DaggerAppComponent;
import com.example.butymovamoneytracker.di.base.BaseComponent;
import com.example.butymovamoneytracker.di.base.BaseComponentBuilder;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;

public class ComponentsHolder {
    private final App app;
    private AppComponent appComponent;

    @Inject
    Map<Class<?>, Provider<BaseComponentBuilder>> builders;
    private Map<Class<?>, BaseComponent> components;

    public ComponentsHolder(App app) {
        this.app = app;
    }

    public void init(){
        appComponent = DaggerAppComponent.builder().withApp(app). build();
        appComponent.injectComponentsHolder(this);
        components = new HashMap<>();
    }

    public BaseComponent getActivityComponent(Class<?> cls){
        BaseComponent component = components.get(cls);
        if (component==null){
            BaseComponentBuilder builder = builders.get(cls).get();
            component = builder.build();
            components.put(cls, component);
        }
        return component;
    }

    public void releaseActivityComponent(Class<?> cls){
        components.put(cls, null);
    }


    public AppComponent getAppComponent() {
        return appComponent;
    }
}
