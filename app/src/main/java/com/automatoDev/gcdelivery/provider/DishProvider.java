package com.automatoDev.gcdelivery.provider;


import android.os.Parcel;
import android.os.Parcelable;

public class DishProvider implements Parcelable {
    String dishUid;
    String dishName;
    String dishDescOne;
    String dishDescTwo;
    String dishDescTree;
    String dishDescFour;
    String dishDescFive;
    double dishValue;
    String dishUrlPhoto;

    public DishProvider(String dishUid, String dishName, String dishDescOne,
                        String dishDescTwo, String dishDescTree, String dishDescFour,
                        String dishDescFive, double dishValue, String dishUrlPhoto) {
        this.dishUid = dishUid;
        this.dishName = dishName;
        this.dishDescOne = dishDescOne;
        this.dishDescTwo = dishDescTwo;
        this.dishDescTree = dishDescTree;
        this.dishDescFour = dishDescFour;
        this.dishDescFive = dishDescFive;
        this.dishValue = dishValue;
        this.dishUrlPhoto = dishUrlPhoto;
    }

    protected DishProvider(Parcel in) {
        dishUid = in.readString();
        dishName = in.readString();
        dishDescOne = in.readString();
        dishDescTwo = in.readString();
        dishDescTree = in.readString();
        dishDescFour = in.readString();
        dishDescFive = in.readString();
        dishValue = in.readDouble();
        dishUrlPhoto = in.readString();
    }

    public static final Creator<DishProvider> CREATOR = new Creator<DishProvider>() {
        @Override
        public DishProvider createFromParcel(Parcel in) {
            return new DishProvider(in);
        }

        @Override
        public DishProvider[] newArray(int size) {
            return new DishProvider[size];
        }
    };

    public void setDishUid(String dishUid) {
        this.dishUid = dishUid;
    }

    public void setDishUrlPhoto(String dishUrlPhoto) {
        this.dishUrlPhoto = dishUrlPhoto;
    }

    public DishProvider() {
    }

    public String getDishUid() {
        return dishUid;
    }

    public String getDishName() {
        return dishName;
    }

    public String getDishDescOne() {
        return dishDescOne;
    }

    public String getDishDescTwo() {
        return dishDescTwo;
    }

    public String getDishDescTree() {
        return dishDescTree;
    }

    public String getDishDescFour() {
        return dishDescFour;
    }

    public String getDishDescFive() {
        return dishDescFive;
    }

    public double getDishValue() {
        return dishValue;
    }

    public String getDishUrlPhoto() {
        return dishUrlPhoto;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(dishUid);
        dest.writeString(dishName);
        dest.writeString(dishDescOne);
        dest.writeString(dishDescTwo);
        dest.writeString(dishDescTree);
        dest.writeString(dishDescFour);
        dest.writeString(dishDescFive);
        dest.writeDouble(dishValue);
        dest.writeString(dishUrlPhoto);
    }
}
