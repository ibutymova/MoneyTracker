package com.example.butymovamoneytracker.screens;

import android.content.Intent;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

abstract class StartActivityViewModel extends ViewModel {

    abstract void init(Intent intent);

    abstract void btnSignInClick();

    abstract void signIn(int requestCode, int resultCode, Intent intent);

    abstract LiveData<Void> showSignInEvent();

    abstract LiveData<String> signInResultTrueEvent();

    abstract LiveData<Void> signInResultFalseEvent();

    abstract LiveData<Void> autoSignInEvent();

}
