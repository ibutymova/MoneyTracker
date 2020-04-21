package com.example.butymovamoneytracker.states;

import android.os.Parcel;
import android.os.Parcelable;

public enum ToolbarState implements Parcelable {

    TOOLBAR (0),
    FILTER_TOOLBAR (1),
    SEARCH_TOOLBAR (2),
    ACTION_BAR (3);

    private int state;

    ToolbarState(int state) {
        this.state = state;
    }

    ToolbarState(Parcel in) {
        state = in.readInt();
    }

    public static final Creator<ToolbarState> CREATOR = new Creator<ToolbarState>() {
        @Override
        public ToolbarState createFromParcel(Parcel in) {
            return ToolbarState.values()[in.readInt()];
        }

        @Override
        public ToolbarState[] newArray(int size) {
            return new ToolbarState[size];
        }
    };

    public int getState() {
        return state;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(ordinal());
    }
}

