package com.example.butymovamoneytracker.data.extra;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.PluralsRes;

public class RemoveData implements Parcelable {

    private int count;

    @PluralsRes
    private int resId;

    public RemoveData(int count, int resId) {
        this.count = count;
        this.resId = resId;
    }


    protected RemoveData(Parcel in) {
        count = in.readInt();
        resId = in.readInt();
    }

    public static final Creator<RemoveData> CREATOR = new Creator<RemoveData>() {
        @Override
        public RemoveData createFromParcel(Parcel in) {
            return new RemoveData(in);
        }

        @Override
        public RemoveData[] newArray(int size) {
            return new RemoveData[size];
        }
    };

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(count);
        parcel.writeInt(resId);
    }
}
