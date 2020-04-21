package com.example.butymovamoneytracker.screens;

import android.content.Intent;

import androidx.lifecycle.LiveData;

import com.example.butymovamoneytracker.data.Api;
import com.example.butymovamoneytracker.prefs.Prefs;
import com.example.butymovamoneytracker.states.FilterState;
import com.example.butymovamoneytracker.utils.SingleLiveEvent;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.TimeZone;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static com.example.butymovamoneytracker.screens.StartActivity.KEY_AUTO_SIGN_IN;

public class StartActivityViewModelImpl extends StartActivityViewModel {

    private SingleLiveEvent<Void> showSignInEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<String> signInResultTrueEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<Void> signInResultFalseEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<Void> autoSignInEvent = new SingleLiveEvent<>();

    private Api api;
    private Prefs prefs;
    private GoogleApiClient googleApiClient;
    private FilterState filterState;
    private CompositeDisposable compositeDisposable;

    @Inject
    StartActivityViewModelImpl(Api api,
                               Prefs prefs,
                               GoogleApiClient googleApiClient,
                               FilterState filterState,
                               CompositeDisposable compositeDisposable) {
        this.api = api;
        this.prefs = prefs;
        this.googleApiClient = googleApiClient;
        this.filterState = filterState;
        this.compositeDisposable = compositeDisposable;

        if (prefs.getLastTimeZone()==null) {
            prefs.setLastTimeZone(TimeZone.getDefault().getID());
        }
    }

    @Override
    void init(Intent intent) {
        if (intent!=null && intent.getBooleanExtra(KEY_AUTO_SIGN_IN, false)) {
            filterState.setFilterDate(false);
            autoSignInEvent.call();
        }
    }

    @Override
    public void btnSignInClick() {
        showSignInEvent.call();
    }

    @Override
    void signIn(int requestCode, int resultCode, Intent intent) {

        if (requestCode!=StartActivity.SIGN_IN)
            return;

        GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(intent);
        if (googleSignInResult == null || !googleSignInResult.isSuccess()) {
            signInFailed();
            return;
        }

        GoogleSignInAccount googleSignInAccount = googleSignInResult.getSignInAccount();
        if (googleSignInAccount == null) {
           signInFailed();
           return;
        }

        compositeDisposable.add( api.auth(googleSignInAccount.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(authResult -> {
                    if (authResult != null && authResult.getAuth_token().trim().length() > 0)
                        signInSuccess(authResult.getAuth_token().trim(), googleSignInAccount.getDisplayName());
                    else
                        signInFailed();
                }, throwable -> signInFailed()));
    }

    private void signInFailed(){
        prefs.setAuthTokenEmpty();
        Auth.GoogleSignInApi.signOut(googleApiClient);
        signInResultFalseEvent.call();
    }

    private void signInSuccess(String token, String name){
        prefs.setAuthToken(token);
        signInResultTrueEvent.setValue(name);
    }

    @Override
    LiveData<Void> showSignInEvent(){
        return showSignInEvent;
    }

    @Override
    LiveData<String> signInResultTrueEvent() {
        return signInResultTrueEvent;
    }

    @Override
    LiveData<Void> signInResultFalseEvent() {
        return signInResultFalseEvent;
    }

    @Override
    LiveData<Void> autoSignInEvent() {
        return autoSignInEvent;
    }

    @Override
    protected void onCleared() {
        compositeDisposable.dispose();
        super.onCleared();
    }
}
