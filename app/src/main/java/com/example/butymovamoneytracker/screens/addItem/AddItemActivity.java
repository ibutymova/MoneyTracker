package com.example.butymovamoneytracker.screens.addItem;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.example.butymovamoneytracker.App;
import com.example.butymovamoneytracker.R;
import com.example.butymovamoneytracker.di.AddItemActivitySubComponent.AddItemActivitySubComponent;
import com.example.butymovamoneytracker.screens.main.adapters.TypePage;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AddItemActivity extends AppCompatActivity {

    public static final int RC_ADD_ITEM = 100;
    public static final String EXTRA_TYPE = "type";
    public static final String EXTRA_NAME = "name";

    private TypePage typePage;
    private Unbinder unbinder;

    @BindView(R.id.etName)
    EditText etName;

    @BindView(R.id.etPrice)
    EditText etPrice;

    @BindView(R.id.FAB_add)
    FloatingActionButton FAB_add;

    @BindView(R.id.toolBarAdd)
    Toolbar toolbar;

    private AddItemActivityViewModel viewModel;

    @Inject
    ViewModelProvider.Factory factory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        typePage = (TypePage) getIntent().getSerializableExtra(EXTRA_TYPE);
        unbinder = ButterKnife.bind(this);

        ((AddItemActivitySubComponent)((App)getApplication()).getComponentsHolder().getActivityComponent(getClass())).inject(this);

        viewModel = new ViewModelProvider(this, factory).get(AddItemActivityViewModelImpl.class);

        setSupportActionBar(toolbar);
        if (getSupportActionBar()!=null){
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setTitle(getResources().getStringArray(R.array.add_item)[typePage.getTypeValue()]);

        initOutputs();
        initInputs();
    }

    private void initInputs() {
        FAB_add.setOnClickListener(v -> viewModel.btnAddEnable(etName.getText().toString().trim(), etPrice.getText().toString().trim(), String.valueOf(typePage.getTypeValue())));
    }

    private void initOutputs(){

        viewModel.setFocusNameEvent().observe(this, aVoid -> etName.requestFocus());

        viewModel.setFocusPriceEvent().observe(this, aVoid -> etPrice.requestFocus());

        viewModel.btnAddEnabledEvent().observe(this, aBoolean -> FAB_add.setEnabled(aBoolean));

        viewModel.addItemCorrectEvent().observe(this, name -> sendResult(name, true));

        viewModel.addItemWrongEvent().observe(this, name -> sendResult(name, false));
    }

    private void sendResult(String name, Boolean result){
        Intent intent = new Intent();
        intent.putExtra(EXTRA_NAME, name);
        if (result)
            setResult(RESULT_OK, intent);
        else
            setResult(RESULT_CANCELED, intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home: {
                finish();
                return true;
            }
            default: return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        viewModel = null;

        if (isFinishing())
            ((App)getApplication()).getComponentsHolder().releaseActivityComponent(getClass());

        unbinder.unbind();
        super.onDestroy();
    }
}
