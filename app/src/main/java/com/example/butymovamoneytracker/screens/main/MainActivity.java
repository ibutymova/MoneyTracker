package com.example.butymovamoneytracker.screens.main;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.example.butymovamoneytracker.App;
import com.example.butymovamoneytracker.R;
import com.example.butymovamoneytracker.di.MainActivitySubComponent.MainActivitySubComponent;
import com.example.butymovamoneytracker.di.qualifiers.Expense;
import com.example.butymovamoneytracker.di.qualifiers.Income;
import com.example.butymovamoneytracker.dialogs.DateDialog;
import com.example.butymovamoneytracker.dialogs.LogoutDialog;
import com.example.butymovamoneytracker.prefs.FilterDate;
import com.example.butymovamoneytracker.screens.main.adapters.MainPagerAdapter;
import com.example.butymovamoneytracker.screens.main.items.actionMode.ActionModeCallback;
import com.example.butymovamoneytracker.screens.main.listeners.ActionModeListener;
import com.example.butymovamoneytracker.screens.main.listeners.ClickListener;
import com.example.butymovamoneytracker.states.ItemsCountState;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity implements  TextView.OnEditorActionListener,
                                                                DatePickerDialog.OnDateSetListener,
                                                                ActionModeListener,
                                                                ActionModeCallback.Listener,
                                                                View.OnClickListener{

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.pager)
    ViewPager pager;

    @BindView(R.id.tab)
    TabLayout tab;

    @BindView(R.id.llFilterDate)
    ViewGroup llFilterDate;

    @BindView(R.id.etDateMin)
    EditText etDateMin;

    @BindView(R.id.etDateMax)
    EditText etDateMax;

    @BindView(R.id.btnFilter)
    ImageButton btnFilter;

    @BindView(R.id.llSearch)
    ViewGroup llSearch;

    @BindView(R.id.etSearch)
    EditText etSearch;

    @BindView(R.id.btnSerch)
    ImageButton btnSearch;

    @BindView(R.id.FAB)
    FloatingActionButton fab;

    private Unbinder unbinder;
    private MainActivityViewModel viewModel;
    private ActionMode actionMode;
    private int offScreenPageLimit = 3;

    @Inject
    ViewModelProvider.Factory factory;

    @Expense
    @Inject
    ItemsCountState itemsExpenseCountState;

    @Income
    @Inject
    ItemsCountState itemsIncomeCountState;

    public static void startInNewTask(Context context){
        Intent intent = new Intent(context, MainActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        unbinder = ButterKnife.bind(this);

        ((MainActivitySubComponent)((App)getApplication()).getComponentsHolder().getActivityComponent(getClass())).inject(this);

        pager.setAdapter(new MainPagerAdapter(getSupportFragmentManager(), getResources().getStringArray(R.array.tab_titles), itemsExpenseCountState, itemsIncomeCountState));
        tab.setupWithViewPager(pager);
        setSupportActionBar(toolbar);
        pager.setOffscreenPageLimit(offScreenPageLimit);

        viewModel = new ViewModelProvider(this, factory).get(MainActivityViewModelImpl.class);

        Fragment logOutDialog = getSupportFragmentManager().findFragmentByTag(LogoutDialog.LOGOUT_DIALOG_TAG);
        if (logOutDialog!=null){
           ((LogoutDialog) logOutDialog).setViewModel(viewModel);
        }

        Fragment selectionDateMinDialog = getSupportFragmentManager().findFragmentByTag(DateDialog.DATE_MIN_DIALOG_TAG);
        if (selectionDateMinDialog!=null){
           ((DateDialog) selectionDateMinDialog).setListener(this);
        }

        Fragment selectionDateMaxDialog = getSupportFragmentManager().findFragmentByTag(DateDialog.DATE_MAX_DIALOG_TAG);
        if (selectionDateMaxDialog!=null){
            ((DateDialog) selectionDateMaxDialog).setListener(this);
        }

        etDateMin.setOnClickListener(this);
        etDateMax.setOnClickListener(this);

        ViewPager.SimpleOnPageChangeListener listener = new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                viewModel.onPageChange(position, getLifecycle().getCurrentState());
            }
        };

        pager.addOnPageChangeListener(listener);

        etSearch.setOnEditorActionListener(this);

        initOutputs();
        initInputs();

        viewModel.init(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        viewModel.onRestoreInstanceState(savedInstanceState);
    }

    private void initInputs() {
        btnFilter.setOnClickListener(v -> viewModel.onBtnFilterClick());

        btnSearch.setOnClickListener(v -> viewModel.onBtnSearchClick(etSearch.getText().toString().trim()));

        fab.setOnClickListener(v -> {
            List<Fragment> fragments = getSupportFragmentManager().getFragments();
            for(Fragment fragment: fragments){
               if (fragment instanceof ClickListener && fragment.isResumed()){
                   ((ClickListener) fragment).onFabClick();
               }
            }
        });
    }

   @Override
   public void startActionMode(){
       viewModel.startActionMode();
   }

    private void finishActionModeInner(){
       if (actionMode!=null)
           actionMode.finish();
    }

    private void startActionModeInner(){
        if (actionMode==null)
            startSupportActionMode(new ActionModeCallback(this));
    }

    @Override
    public void finishActionMode() {
       viewModel.finishActionMode();
    }

    @Override
    public void refreshTitle(Integer count) {
        if (actionMode!=null) {
            actionMode.setTitle(getString(R.string.app_name_selected_items, String.valueOf(count)));
        }
    }

    public void refreshSubtitle(String subtitle){
        toolbar.setSubtitle(getResources().getString(R.string.toolbar_subtitle, subtitle));
    }

    private void showToolbar(ViewGroup viewGroup, Boolean show){
        viewGroup.setVisibility(show ? View.VISIBLE : View.GONE);

        invalidateOptionsMenu();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(show);
            getSupportActionBar().setDisplayHomeAsUpEnabled(show);
        }
    }

    private void initOutputs(){
        viewModel.onMenuItemRemoveClickEvent().observe(this, aVoid -> {
            List<Fragment> fragments = getSupportFragmentManager().getFragments();
            for(Fragment fragment: fragments){
                if (fragment instanceof ClickListener && fragment.isResumed()){
                    ((ClickListener) fragment).onMenuItemRemoveClick();
                }
            }
        });

        viewModel.changeToolbarStateEvent().observe(this, toolbarState -> {
            switch (toolbarState){
                case TOOLBAR:{
                    showToolbar(llFilterDate, false);
                    showToolbar(llSearch, false);
                    return;
                }
                case FILTER_TOOLBAR:{
                    showToolbar(llFilterDate, true);
                    return;
                }
                case SEARCH_TOOLBAR:{
                    showToolbar(llSearch, true);
                    return;
                }
                case ACTION_BAR:{
                    startActionModeInner();
                    return;
                }
            }
        });

        viewModel.showDateMinDialogEvent().observe(this, filterDate -> showFilterDateDialog(filterDate, DateDialog.DATE_MIN_DIALOG_TAG));

        viewModel.showDateMaxDialogEvent().observe(this, filterDate -> showFilterDateDialog(filterDate, DateDialog.DATE_MAX_DIALOG_TAG));

        viewModel.saveDateMinEvent().observe(this, this::showDateMinFilter);

        viewModel.saveDateMaxEvent().observe(this, this::showDateMaxFilter);

        viewModel.showLogOutDialogEvent().observe(this, aVoid -> showLogOutDialog());

        viewModel.showToastEvent().observe(this, this::showToast);

        viewModel.finishTaskEvent().observe(this, aVoid -> finishAndRemoveTask());

        viewModel.showFABEvent().observe(this, isVisible -> fab.setVisibility(isVisible ?  View.VISIBLE: View.GONE));

        viewModel.setSubtitleEvent().observe(this, this::refreshSubtitle);

        viewModel.setActionModeTitleEvent().observe(this, this::refreshTitle);

        viewModel.finishActionModeEvent().observe(this, aVoid -> finishActionModeInner());
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        viewModel.onSaveInstanceState(outState);
    }

    private void showLogOutDialog(){
        LogoutDialog dialog = new LogoutDialog();
        dialog.setViewModel(viewModel);
        dialog.show(getSupportFragmentManager(), LogoutDialog.LOGOUT_DIALOG_TAG);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (llFilterDate.isShown()||llSearch.isShown())
            getMenuInflater().inflate(R.menu.menu_empty, menu);
        else
            getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_main_logout: {
                viewModel.onMenuMainLogoutClick();
                return true;
            }
            case R.id.menu_main_filter: {
                viewModel.onMenuMainFilterClick();
                return true;

            }
            case R.id.menu_main_search:{
                viewModel.onMenuMainSearchClick();
                return true;
            }
            case android.R.id.home: {
                viewModel.onMenuMainHomeClick();
                return true;
            }
            default: return false;
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId==EditorInfo.IME_ACTION_SEARCH){
            viewModel.onEditorActionSearchClick(v.getText().toString());
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
       viewModel.onBackPressed();
    }

    private void showDateMinFilter(String date){
       etDateMin.setText(date);
    }

    private void showDateMaxFilter(String date){
       etDateMax.setText(date);
    }

    private void showFilterDateDialog(FilterDate filterDate, String dateTag){
        DateDialog dialog = DateDialog.newInstance(filterDate.getYear(),
                                                   filterDate.getMonth(),
                                                   filterDate.getDayOfMonth());

        dialog.setListener(this);
        dialog.show(getSupportFragmentManager(), dateTag);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        viewModel.onDataSet(view.getTag(), year, month, dayOfMonth);
    }

    private void showToast(@StringRes int resId){
        Toast.makeText(this, getString(resId), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCreateActionMode(ActionMode mode) {
        actionMode = mode;
        viewModel.onCreatedActionMode();
    }

    @Override
    public void onDestroyActionMode() {
        actionMode=null;
        viewModel.onDestroyedActionMode();
    }

    @Override
    public void onMenuItemRemoveClick() {
        viewModel.onMenuItemRemoveClick();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.etDateMin: {
                viewModel.onEtDateMinClick();
                break;
            }
            case R.id.etDateMax: {
                viewModel.onEtDateMaxClick();
                break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        itemsExpenseCountState.deleteObservers();
        itemsIncomeCountState.deleteObservers();
        viewModel = null;
        actionMode = null;
        if (isFinishing())
            ((App) getApplication()).getComponentsHolder().releaseActivityComponent(getClass());

        unbinder.unbind();
        super.onDestroy();
    }
}



