package com.example.butymovamoneytracker.screens;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.butymovamoneytracker.App;
import com.example.butymovamoneytracker.R;
import com.example.butymovamoneytracker.di.StartActivitySubComponent.StartActivitySubComponent;
import com.example.butymovamoneytracker.screens.main.MainActivity;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class StartActivity extends AppCompatActivity {

    public static final int SIGN_IN = 999;
    public static final String KEY_AUTO_SIGN_IN = "signin";

    @BindView(R.id.btnSignIn)
    Button btnSignIn;

    @BindView(R.id.ivImage)
    ImageView ivImage;

    private Unbinder unbinder;
    private StartActivityViewModel viewModel;

    @Inject
    ViewModelProvider.Factory factory;
    @Inject
    GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        unbinder = ButterKnife.bind(this);
        ivImage.setVisibility(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? View.VISIBLE : View.GONE);

        ((StartActivitySubComponent)((App)getApplication()).getComponentsHolder().getActivityComponent(getClass())).inject(this);

        viewModel = new ViewModelProvider(this, factory).get(StartActivityViewModelImpl.class);

        initOutputs();
        initInputs();
        viewModel.init(getIntent());
    }


    private void initOutputs(){
        btnSignIn.setOnClickListener(v -> viewModel.btnSignInClick());
   }

    private void initInputs(){
        viewModel.showSignInEvent().observe(this, aVoid -> showSignIn(googleApiClient));

        viewModel.signInResultFalseEvent().observe(this, aVoid -> {
            showSignInResult(false, "");
        });

        viewModel.signInResultTrueEvent().observe(this, name -> showSignInResult(true, name));

        viewModel.autoSignInEvent().observe(this, aVoid -> btnSignIn.performClick());
    }

    private void showSignInResult(Boolean result, String name){
        if (result) {
            Toast.makeText(this, getResources().getString(R.string.signIn_result_true, name), Toast.LENGTH_SHORT).show();
            MainActivity.startInNewTask(this);
        }
        else {
            Toast.makeText(this, getResources().getString(R.string.signIn_result_false), Toast.LENGTH_SHORT).show();
            finishAndRemoveTask();
        }
    }

    private void  showSignIn(GoogleApiClient googleApiClient){
       Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
       startActivityForResult(intent, SIGN_IN);
   }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        viewModel.signIn(requestCode, resultCode, intent);
    }

    @Override
    protected void onDestroy() {
        viewModel=null;
        if (isFinishing())
            ((App) getApplication()).getComponentsHolder().releaseActivityComponent(getClass());

        unbinder.unbind();
        super.onDestroy();
    }
}
