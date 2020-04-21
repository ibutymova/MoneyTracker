package com.example.butymovamoneytracker.screens.main.items;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.butymovamoneytracker.App;
import com.example.butymovamoneytracker.R;
import com.example.butymovamoneytracker.data.extra.RemoveData;
import com.example.butymovamoneytracker.di.ItemsFragmentSubComponent.ItemsFragmentSubComponent;
import com.example.butymovamoneytracker.dialogs.RemoveDialog;
import com.example.butymovamoneytracker.factories.AdapterFactory;
import com.example.butymovamoneytracker.factories.ItemAnimatorFactory;
import com.example.butymovamoneytracker.screens.addItem.AddItemActivity;
import com.example.butymovamoneytracker.screens.main.adapters.TypePage;
import com.example.butymovamoneytracker.screens.main.items.adapters.ItemsAdapter;
import com.example.butymovamoneytracker.screens.main.listeners.ActionModeListener;
import com.example.butymovamoneytracker.screens.main.listeners.ClickListener;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.example.butymovamoneytracker.screens.addItem.AddItemActivity.EXTRA_TYPE;
import static com.example.butymovamoneytracker.screens.addItem.AddItemActivity.RC_ADD_ITEM;

public class ItemsFragment extends Fragment implements ClickListener {

    private static final String KEY_TYPE_PAGE = "page";
    private TypePage typePage = TypePage.PAGE_EXPENSE;

    @BindView(R.id.recycler)
    RecyclerView recycler;

    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;

    @BindView(R.id.progress)
    FrameLayout progress;

    private Unbinder unbinder;
    private ItemsFragmentViewModel viewModel;
    private ItemsAdapter adapter;

    @Inject
    ViewModelProvider.Factory factory;

    @Inject
    AdapterFactory adapterFactory;

    @Inject
    ItemAnimatorFactory animatorFactory;

    public static ItemsFragment newInstance(TypePage typePage){
         ItemsFragment fragment = new ItemsFragment();
         Bundle bundle = new Bundle();
         bundle.putSerializable(KEY_TYPE_PAGE, typePage);
         fragment.setArguments(bundle);
         return fragment;
    }

   @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_items, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        unbinder = ButterKnife.bind(this, view);

        if (getArguments() == null || !getArguments().containsKey(KEY_TYPE_PAGE))
            throw new IllegalArgumentException("TypePage not found");

        typePage = (TypePage) getArguments().getSerializable(KEY_TYPE_PAGE);

        ((ItemsFragmentSubComponent)((App)requireActivity().getApplication()).getComponentsHolder().getActivityComponent(getClass())).inject(this);

        adapter = adapterFactory.get(typePage);

        switch (typePage){
            case PAGE_EXPENSE: {
                viewModel = new ViewModelProvider(this, factory).get(ItemsExpenseFragmentViewModelImpl.class);
                break;
            }
            case PAGE_INCOME: {
                viewModel = new ViewModelProvider(this, factory).get(ItemsIncomeFragmentViewModelImpl.class);
                break;
            }
            default: throw new RuntimeException("Unknown type page");
        }

        adapter.setViewModel(viewModel);
        recycler.setAdapter(adapter);
        recycler.setHasFixedSize(true);
        recycler.setItemAnimator(animatorFactory.get(typePage));
        recycler.setLayoutManager(new LinearLayoutManager(requireActivity()));

        initOutputs();
        initInputs();
    }

    private void initInputs(){
        refresh.setOnRefreshListener(() -> viewModel.onRefresh());
    }

    private void initOutputs(){

        viewModel.getItemsDoneEvent().observe(getViewLifecycleOwner(), result -> viewModel.getItemsDataBase());

        viewModel.itemEntityList().observe(getViewLifecycleOwner(), itemEntities -> adapter.setItems(itemEntities));

        viewModel.setProgressEvent().observe(getViewLifecycleOwner(), isProgressing -> progress.setVisibility(isProgressing ? View.VISIBLE : View.GONE ));

        viewModel.setRefreshEvent().observe(getViewLifecycleOwner(), isRefreshing -> refresh.setRefreshing(isRefreshing));

        viewModel.showAddItem().observe(getViewLifecycleOwner(), aVoid -> showAddItemActivity());

        viewModel.removeItemDialogShowEvent().observe(getViewLifecycleOwner(), this::showConfirmationDelDialog);

        viewModel.removeItemResultTrueEvent().observe(getViewLifecycleOwner(), data -> showToast(R.string.remove_item_result_correct, getResources().getQuantityString(R.plurals.plurals_remove, data.getCount()), getResources().getQuantityString(data.getResId(), data.getCount(), data.getCount())));

        viewModel.removeItemResultFalseEvent().observe(getViewLifecycleOwner(), aVoid -> showToast(R.string.remove_item_result_wrong, getResources().getStringArray(R.array.items)[typePage.getTypeValue()], ""));

        viewModel.startActionModeEvent().observe(getViewLifecycleOwner(), aVoid -> {
            if (requireActivity() instanceof ActionModeListener)
                ((ActionModeListener)requireActivity()).startActionMode();
        });

        viewModel.finishActionModeEvent().observe(getViewLifecycleOwner(), aVoid -> {
            if (requireActivity() instanceof ActionModeListener)
                ((ActionModeListener)requireActivity()).finishActionMode();
        });

        viewModel.refreshItemEvent().observe(getViewLifecycleOwner(), position -> adapter.notifyItemChanged(position));

        viewModel.refreshTitleEvent().observe(getViewLifecycleOwner(), count -> {
            if (requireActivity() instanceof ActionModeListener)
                ((ActionModeListener)requireActivity()).refreshTitle(count);
        });

        viewModel.refreshItemsEvent().observe(getViewLifecycleOwner(), aVoid -> adapter.notifyDataSetChanged());

        viewModel.addItemResultTrueEvent().observe(getViewLifecycleOwner(), name -> showToast(R.string.add_item_result_correct, getResources().getStringArray(R.array.add_item)[typePage.getTypeValue()], name));

        viewModel.addItemResultFalseEvent().observe(getViewLifecycleOwner(), name -> showToast(R.string.add_item_result_wrong, getResources().getStringArray(R.array.add_item)[typePage.getTypeValue()], name));

        viewModel.showErrorEvent().observe(getViewLifecycleOwner(), resId -> showToast(resId, "", ""));

        viewModel.scrollToPosition().observe(getViewLifecycleOwner(), position -> recycler.scrollToPosition(position));
    }

    private void showConfirmationDelDialog(RemoveData data) {
        RemoveDialog dialog = new RemoveDialog();
        dialog.setTargetFragment(this, RemoveDialog.RC_REMOVE_DIALOG);
        dialog.setRemoveData(data);
        dialog.show(requireActivity().getSupportFragmentManager(), RemoveDialog.REMOVE_DIALOG_TAG);
    }

    private void showAddItemActivity(){
        Intent intent = new Intent(getActivity(), AddItemActivity.class);
        intent.putExtra(EXTRA_TYPE, typePage);
        startActivityForResult(intent, RC_ADD_ITEM);
    }

    private void showToast(@StringRes int resId, String name, String type){
        Toast.makeText(getContext(), getString(resId, name, type), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        viewModel.onActivityResult(requestCode, resultCode, intent);
    }

    @Override
    public void onFabClick() {
        viewModel.onFabClick();
    }

    @Override
    public void onMenuItemRemoveClick() {
        viewModel.onMenuItemRemoveClick();
    }

    @Override
    public void onDestroyView() {
        viewModel=null;
        recycler.setAdapter(null);
        unbinder.unbind();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        if (requireActivity().isFinishing())
            ((App)requireActivity().getApplication()).getComponentsHolder().releaseActivityComponent(getClass());
        super.onDestroy();
    }
}
