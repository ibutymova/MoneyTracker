package com.example.butymovamoneytracker.states;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Observable;

public class SearchState extends Observable implements Parcelable {

    private String searchStr;

    public SearchState(String searchStr) {
        this.searchStr = searchStr;
    }

    protected SearchState(Parcel in) {
        searchStr = in.readString();
    }

    public static final Creator<SearchState> CREATOR = new Creator<SearchState>() {
        @Override
        public SearchState createFromParcel(Parcel in) {
            return new SearchState(in);
        }

        @Override
        public SearchState[] newArray(int size) {
            return new SearchState[size];
        }
    };

    public String getSearchStr() {
        return searchStr;
    }

    public void setSearchStr(String searchStr) {
        this.searchStr = searchStr;
        this.setChanged();
        this.notifyObservers(this);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(searchStr);
    }
}
