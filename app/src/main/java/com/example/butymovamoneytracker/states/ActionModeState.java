package com.example.butymovamoneytracker.states;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.butymovamoneytracker.screens.main.adapters.TypePage;

import java.util.Observable;

public class ActionModeState extends Observable implements Parcelable {

    private boolean isInActionMode;
    private TypePage typePage;

    public ActionModeState(boolean isInActionMode, TypePage typePage) {
        this.isInActionMode = isInActionMode;
        this.typePage = typePage;
    }

    protected ActionModeState(Parcel in) {
        isInActionMode = in.readByte() != 0;
    }

    public static final Creator<ActionModeState> CREATOR = new Creator<ActionModeState>() {
        @Override
        public ActionModeState createFromParcel(Parcel in) {
            return new ActionModeState(in);
        }

        @Override
        public ActionModeState[] newArray(int size) {
            return new ActionModeState[size];
        }
    };

    public boolean isInActionMode() {
        return isInActionMode;
    }

    public void setInActionMode(boolean inActionMode) {
        isInActionMode = inActionMode;
        this.setChanged();
        this.notifyObservers(this);
    }

    public TypePage getTypePage() {
        return typePage;
    }

    public void setTypePage(TypePage typePage) {
        this.typePage = typePage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte((byte) (isInActionMode ? 1 : 0));
    }
}
