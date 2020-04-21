package com.example.butymovamoneytracker.di.AppComponent;

import com.example.butymovamoneytracker.di.qualifiers.Expense;
import com.example.butymovamoneytracker.di.qualifiers.Income;
import com.example.butymovamoneytracker.di.scopes.AppScope;
import com.example.butymovamoneytracker.prefs.Prefs;
import com.example.butymovamoneytracker.screens.main.adapters.TypePage;
import com.example.butymovamoneytracker.states.ActionModeState;
import com.example.butymovamoneytracker.states.FilterState;
import com.example.butymovamoneytracker.states.ItemsCountState;
import com.example.butymovamoneytracker.states.SearchState;
import com.example.butymovamoneytracker.states.SelectedItems;
import com.example.butymovamoneytracker.states.TimeZoneState;
import com.example.butymovamoneytracker.states.ToolbarState;

import java.util.HashMap;
import java.util.TimeZone;

import dagger.Module;
import dagger.Provides;

@Module
public abstract class StatesModule {

    @AppScope
    @Provides
    static FilterState provideFilterState(Prefs prefs){
        return new FilterState(false, prefs.getFilterMinDate(), prefs.getFilterMaxDate());
    }

    @AppScope
    @Provides
    static SearchState provideSearchState(){
        return new SearchState("");
    }

    @AppScope
    @Provides
    static ToolbarState provideToolbarState(){
        return ToolbarState.TOOLBAR;
    }

    @AppScope
    @Provides
    static ActionModeState provideActionModeState(){
        return new ActionModeState(false, TypePage.PAGE_EXPENSE);
    }

    @AppScope
    @Provides
    static SelectedItems provideSelectedItems(){
        return new SelectedItems(new HashMap<>());
    }

    @AppScope
    @Provides
    static TimeZoneState provideTimeZoneState(){
        return new TimeZoneState(TimeZone.getDefault().getID());
    }

    @AppScope
    @Provides
    @Income
    static ItemsCountState provideItemsIncomeCountState(){
        return new ItemsCountState(0);
    }

    @AppScope
    @Provides
    @Expense
    static ItemsCountState provideItemsExpenseCountState(){
        return new ItemsCountState(0);
    }
}
