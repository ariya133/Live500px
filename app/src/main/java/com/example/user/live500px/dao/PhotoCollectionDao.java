package com.example.user.live500px.dao;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PhotoCollectionDao implements Parcelable {
    @SerializedName("success")
    private boolean success;
    @SerializedName("data")
    private ArrayList<PhotoDao> data;

    public PhotoCollectionDao() {

    }

    protected PhotoCollectionDao(Parcel in) {
        success = in.readByte() != 0;
        data = in.createTypedArrayList(PhotoDao.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (success ? 1 : 0));
        dest.writeTypedList(data);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PhotoCollectionDao> CREATOR = new Creator<PhotoCollectionDao>() {
        @Override
        public PhotoCollectionDao createFromParcel(Parcel in) {
            return new PhotoCollectionDao(in);
        }

        @Override
        public PhotoCollectionDao[] newArray(int size) {
            return new PhotoCollectionDao[size];
        }
    };

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ArrayList<PhotoDao> getData() {
        return data;
    }

    public void setData(ArrayList<PhotoDao> data) {
        this.data = data;
    }
}
