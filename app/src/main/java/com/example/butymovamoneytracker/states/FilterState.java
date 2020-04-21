package com.example.butymovamoneytracker.states;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Observable;

public class FilterState extends Observable implements Parcelable {

    private boolean isFilterDate;
    private long maxDate;
    private long minDate;

    public FilterState(boolean isFilterDate, long minDate, long maxDate) {
        this.isFilterDate = isFilterDate;
        this.minDate = minDate;
        this.maxDate = maxDate;
    }

    protected FilterState(Parcel in) {
        isFilterDate = in.readByte() != 0;
        maxDate = in.readLong();
        minDate = in.readLong();
    }

    public static final Creator<FilterState> CREATOR = new Creator<FilterState>() {
        @Override
        public FilterState createFromParcel(Parcel in) {
            return new FilterState(in);
        }

        @Override
        public FilterState[] newArray(int size) {
            return new FilterState[size];
        }
    };

    public boolean getFilterDate() {
        return isFilterDate;
    }

    public void setFilterDate(boolean filterDate) {
        this.isFilterDate = filterDate;
        this.setChanged();
        this.notifyObservers(this);
    }

    public Long getMinDate() {
        return minDate;
    }

    public void setMinDate(long minDate) {
        this.minDate = minDate;
    }

    public Long getMaxDate() {
        return maxDate;
    }

    public void setMaxDate(long maxDate) {
        this.maxDate = maxDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte((byte) (isFilterDate ? 1 : 0));
        parcel.writeLong(maxDate);
        parcel.writeLong(minDate);
    }
}
