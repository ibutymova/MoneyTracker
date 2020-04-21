package com.example.butymovamoneytracker.screens.main.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.butymovamoneytracker.screens.main.balance.BalanceFragment;
import com.example.butymovamoneytracker.screens.main.items.ItemsFragment;
import com.example.butymovamoneytracker.states.ItemsCountState;

import java.util.Objects;

import static com.example.butymovamoneytracker.screens.main.adapters.TypePage.PAGE_EXPENSE;
import static com.example.butymovamoneytracker.screens.main.adapters.TypePage.PAGE_INCOME;

public class MainPagerAdapter extends FragmentPagerAdapter {

    private String[] titles;
    private ItemsCountState itemsExpenseCountState;
    private ItemsCountState itemsIncomeCountState;

    public MainPagerAdapter(@NonNull FragmentManager fm, String[] titles, ItemsCountState itemsExpenseCountState, ItemsCountState itemsIncomeCountState) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.titles = titles;
        this.itemsExpenseCountState = itemsExpenseCountState;
        this.itemsIncomeCountState = itemsIncomeCountState;

        itemsExpenseCountState.addObserver((o, itemsExpenseCountState1) -> notifyDataSetChanged());

        itemsIncomeCountState.addObserver((o, itemsIncomeCountState1) -> notifyDataSetChanged());
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (Objects.requireNonNull(TypePage.getType(position))){
          case PAGE_EXPENSE:{
              return ItemsFragment.newInstance(PAGE_EXPENSE);
            }
          case PAGE_INCOME:{
              return ItemsFragment.newInstance(PAGE_INCOME);
            }
          case PAGE_BALANCE:{
              return BalanceFragment.newInstance();
          }
          default: throw new RuntimeException("Unknown type page");
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (Objects.requireNonNull(TypePage.getType(position))){
            case PAGE_EXPENSE:{
                return String.format(titles[position], itemsExpenseCountState.getCount());
            }
            case PAGE_INCOME:{
                return String.format(titles[position], itemsIncomeCountState.getCount());
            }
            case PAGE_BALANCE:{
                return titles[position];
            }
            default: throw new RuntimeException("Unknown type page");
        }
    }

    @Override
    public int getCount() {
        return titles.length;
    }
}
