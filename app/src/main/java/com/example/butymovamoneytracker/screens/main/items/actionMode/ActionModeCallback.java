package com.example.butymovamoneytracker.screens.main.items.actionMode;

import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.view.ActionMode;

import com.example.butymovamoneytracker.R;

public class ActionModeCallback implements ActionMode.Callback {

    private Listener listener;

    public ActionModeCallback(Listener listener) {
        this.listener = listener;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        if (listener!=null)
            listener.onCreateActionMode(mode);
         return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        mode.getMenuInflater().inflate(R.menu.menu_items, menu);
        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_remove:{
                if (listener!=null)
                    listener.onMenuItemRemoveClick();
                return true;
            }
            default: return false;
        }
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        if (listener!=null) {
            listener.onDestroyActionMode();
            listener=null;
        }
    }

    public interface Listener {
        void onCreateActionMode(ActionMode mode);
        void onDestroyActionMode();
        void onMenuItemRemoveClick();
    }
}
