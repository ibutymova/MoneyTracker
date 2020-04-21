package com.example.butymovamoneytracker.screens.main.balance;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.butymovamoneytracker.App;
import com.example.butymovamoneytracker.R;
import com.example.butymovamoneytracker.di.BalanceFragmentSubComponent.BalanceFragmentSubComponent;
import com.example.butymovamoneytracker.utils.DiagrammView;
import com.example.butymovamoneytracker.utils.FormatUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BalanceFragment extends Fragment {

    @BindView(R.id.tvBalance)
    TextView tvBalance;

    @BindView(R.id.tvExpense)
    TextView tvExpence;

    @BindView(R.id.tvIncome)
    TextView tvIncome;

    @BindView(R.id.diagramm)
    DiagrammView diagramm;

    private Unbinder unbinder;
    private BalanceFragmentViewModel viewModel;

    @Inject
    ViewModelProvider.Factory factory;

    @Inject
    FormatUtils formatUtils;

    public static BalanceFragment newInstance() {
        return new BalanceFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_balance, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        unbinder = ButterKnife.bind(this, view);

        ((BalanceFragmentSubComponent)((App)requireActivity().getApplication()).getComponentsHolder().getActivityComponent(getClass())).inject(this);

        viewModel = new ViewModelProvider(this, factory).get(BalanceFragmentViewModelImpl.class);

        initOutputs();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        viewModel.onStop();
    }

    private void initOutputs(){
       viewModel.setBalanceEvent().observe(getViewLifecycleOwner(), balanceEntity -> {
           tvBalance.setText(getResources().getString(R.string.price_format, formatUtils.getPriceStr(balanceEntity.getBalance()), getResources().getString(R.string.RUB)));
           tvExpence.setText(getResources().getString(R.string.price_format, formatUtils.getPriceStr(balanceEntity.getTotal_expense()), getResources().getString(R.string.RUB)));
           tvIncome.setText(getResources().getString(R.string.price_format,  formatUtils.getPriceStr(balanceEntity.getTotal_income()), getResources().getString(R.string.RUB)));
           diagramm.update(balanceEntity.getTotal_expense(), balanceEntity.getTotal_income());
       });

        viewModel.showErrorEvent().observe(getViewLifecycleOwner(), this::showToast);
    }

    private void showToast(@StringRes Integer resId){
        Toast.makeText(getContext(), getString(resId), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroyView() {
        viewModel=null;

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
