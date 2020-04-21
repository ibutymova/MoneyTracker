package com.example.butymovamoneytracker;

import android.app.Application;
import android.content.Context;

import com.example.butymovamoneytracker.di.ComponentsHolder;

public class App extends Application {
    private ComponentsHolder componentsHolder;

    public ComponentsHolder getComponentsHolder() {
        return componentsHolder;
    }

    public static App getApp(Context context){
        return (App)context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        componentsHolder = new ComponentsHolder(this);
        componentsHolder.init();
    }
}
