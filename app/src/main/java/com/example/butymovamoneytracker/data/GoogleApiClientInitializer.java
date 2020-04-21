package com.example.butymovamoneytracker.data;

import android.content.Context;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;

public class GoogleApiClientInitializer {

    public GoogleApiClient initGoogleApiClient(Context context) {
        GoogleSignInOptions googleSignInOptions = createGoogleSignInOptions();
        return createGoogleApiClient(googleSignInOptions, context);
    }

    private GoogleSignInOptions createGoogleSignInOptions(){
        return new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
    }

    private GoogleApiClient createGoogleApiClient(GoogleSignInOptions googleSignInOptions, Context context){
        return new GoogleApiClient.Builder(context)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();

    }
}
