package com.example.butymovamoneytracker.screens.main.items;

import com.example.butymovamoneytracker.data.Api;
import com.example.butymovamoneytracker.data.db.Database;
import com.example.butymovamoneytracker.data.db.ItemEntityMapper;
import com.example.butymovamoneytracker.prefs.Prefs;
import com.example.butymovamoneytracker.di.qualifiers.Income;
import com.example.butymovamoneytracker.screens.main.adapters.TypePage;
import com.example.butymovamoneytracker.states.ActionModeState;
import com.example.butymovamoneytracker.states.FilterState;
import com.example.butymovamoneytracker.states.ItemsCountState;
import com.example.butymovamoneytracker.states.SearchState;
import com.example.butymovamoneytracker.states.SelectedItems;
import com.example.butymovamoneytracker.states.TimeZoneState;

import javax.inject.Inject;
import io.reactivex.disposables.CompositeDisposable;

public class ItemsIncomeFragmentViewModelImpl extends ItemsFragmentViewModelImpl {

    @Inject
    ItemsIncomeFragmentViewModelImpl(Api api,
                                     Database database,
                                     ItemEntityMapper mapper,
                                     Prefs prefs,
                                     @Income TypePage typePage,
                                     SelectedItems selectedItems,
                                     ActionModeState actionModeState,
                                     FilterState filterState,
                                     SearchState searchState,
                                     @Income ItemsCountState itemsCountState,
                                     TimeZoneState timeZoneState,
                                     CompositeDisposable compositeDisposable) {
        super(api,
                database,
                mapper,
                prefs,
                typePage,
                selectedItems,
                actionModeState,
                filterState,
                searchState,
                itemsCountState,
                timeZoneState,
                compositeDisposable);
    }
}
