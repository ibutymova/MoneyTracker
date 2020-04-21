package com.example.butymovamoneytracker.di.keys;

import com.example.butymovamoneytracker.screens.main.adapters.TypePage;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dagger.MapKey;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@MapKey
public @interface TypePageKey {
    TypePage value();
}
