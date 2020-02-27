package com.automatoDev.gcdelivery.provider;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

public class CarProvider implements Parcelable {
    String dishUid;
    String dishName;
    String dishDescOne;
    double totalOrder;
    int qtdOrder;
    String dishUrlPhoto;

    public CarProvider() {
    }

    public CarProvider(String dishUid, String dishName, String dishDescOne, double totalOrder, int qtdOrder, String dishUrlPhoto) {
        this.dishUid = dishUid;
        this.dishUrlPhoto = dishUrlPhoto;
        this.qtdOrder = qtdOrder;
        this.dishName = dishName;
        this.dishDescOne = dishDescOne;
        this.totalOrder = totalOrder;
    }

    protected CarProvider(Parcel in) {
        dishUid = in.readString();
        dishName = in.readString();
        dishUrlPhoto = in.readString();
        dishDescOne = in.readString();
        qtdOrder = in.readInt();
        totalOrder = in.readDouble();
    }

    public static final Creator<CarProvider> CREATOR = new Creator<CarProvider>() {
        @Override
        public CarProvider createFromParcel(Parcel in) {
            return new CarProvider(in);
        }

        @Override
        public CarProvider[] newArray(int size) {
            return new CarProvider[size];
        }
    };

    public String getDishUid() {
        return dishUid;
    }

    public String getDishName() {
        return dishName;
    }

    public String getDishDescOne() {
        return dishDescOne;
    }

    public double getTotalOrder() {
        return totalOrder;
    }

    public int getQtdOrder() {
        return qtdOrder;
    }

    public String getDishUrlPhoto() {
        return dishUrlPhoto;
    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("dishUid",dishUid);
        result.put("dishName",dishName);
        result.put("dishDescOne", dishDescOne);
        result.put("totalOrder", totalOrder);
        result.put("dishUrlPhoto", dishUrlPhoto);
        result.put("qtdOrder",qtdOrder);

        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(dishUid);
        dest.writeString(dishName);
        dest.writeString(dishUrlPhoto);
        dest.writeString(dishDescOne);
        dest.writeInt(qtdOrder);
        dest.writeDouble(totalOrder);
    }
}

