package com.automatoDev.gcdelivery.provider;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;

import java.util.HashMap;
import java.util.Map;

public class UserProvider implements Parcelable {
    private String userName;;
    private String userEmail;
    private String userUser;
    private String userPhone;
    private String userUid;
    private String userUrlPhoto;
    private String userStreet;
    private String userNumberHome;
    private int userCep;
    private String userComplement;
    private String userSector;
    private String userCity;
    private String userState;
    private String status;
    private Timestamp timestamp;
    private double totalPayment;
    private int qtdItens;
    private String timeDateConvert;
    private String typePayement;

    public UserProvider(String userName, String userEmail, String userUser,
                        String userPhone, String userUid, String userEstreet,
                        String userNumberHome, int userCep, String userComplement,
                        String userSector, String userCity, String userState) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userUser = userUser;
        this.userPhone = userPhone;
        this.userUid = userUid;

        this.userStreet = userEstreet;
        this.userNumberHome = userNumberHome;
        this.userCep = userCep;
        this.userComplement = userComplement;
        this.userSector = userSector;
        this.userCity = userCity;
        this.userState = userState;
    }
    public UserProvider(String userName, String userEmail, String userUser,
                        String userPhone,String userUid,String userUrlPhoto,String userEstreet,
                        String userNumberHome, int userCep, String userComplement,
                        String userSector, String userCity, String userState) {

        this.userName = userName;
        this.userEmail = userEmail;
        this.userUser = userUser;
        this.userPhone = userPhone;
        this.userUid = userUid;
        this.userUrlPhoto = userUrlPhoto;

        this.userStreet = userEstreet;
        this.userNumberHome = userNumberHome;
        this.userCep = userCep;
        this.userComplement = userComplement;
        this.userSector = userSector;
        this.userCity = userCity;
        this.userState = userState;
    }
    public UserProvider() {
    }

    protected UserProvider(Parcel in) {
       timeDateConvert = String.valueOf(timestamp);
        userName = in.readString();
        userEmail = in.readString();
        userUser = in.readString();
        userPhone = in.readString();
        userUid = in.readString();
        userUrlPhoto = in.readString();

        status = in.readString();
        timeDateConvert = in.readString();
        qtdItens = in.readInt();
        totalPayment = in.readDouble();
        typePayement = in.readString();

        userStreet = in.readString();
        userNumberHome = in.readString();
        userCep = in.readInt();
        userComplement = in.readString();
        userSector = in.readString();
        userCity = in.readString();
        userState = in.readString();
    }

    public static final Creator<UserProvider> CREATOR = new Creator<UserProvider>() {
        @Override
        public UserProvider createFromParcel(Parcel in) {
            return new UserProvider(in);
        }

        @Override
        public UserProvider[] newArray(int size) {
            return new UserProvider[size];
        }
    };

    public String getUserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getUserUrlPhoto() {
        return userUrlPhoto;
    }

    public String getUserUser() {
        return userUser;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public String getUserUid() {
        return userUid;
    }

    public String getUserStreet() {
        return userStreet;
    }

    public String getUserNumberHome() {
        return userNumberHome;
    }

    public int getUserCep() {
        return userCep;
    }

    public String getUserComplement() {
        return userComplement;
    }

    public String getUserSector() {
        return userSector;
    }

    public String getUserCity() {
        return userCity;
    }

    public String getUserState() {
        return userState;
    }

    public String getStatus() {
        return status;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public double getTotalPayment() {
        return totalPayment;
    }

    public int getQtdItens() {
        return qtdItens;
    }

    public String getTypePayement() {
        return typePayement;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public void setTotalPayment(double totalPayment) {
        this.totalPayment = totalPayment;
    }

    public void setQtdItens(int qtdItens) {
        this.qtdItens = qtdItens;
    }

    public void setTypePayement(String typePayement) {
        this.typePayement = typePayement;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userName);
        dest.writeString(userEmail);
        dest.writeString(userUser);
        dest.writeString(userPhone);
        dest.writeString(status);
        dest.writeDouble(totalPayment);
        dest.writeString(typePayement);
        dest.writeInt(qtdItens);
        dest.writeString(String.valueOf(timestamp));
        dest.writeString(userUid);
        dest.writeString(userUrlPhoto);
        dest.writeString(userStreet);
        dest.writeString(userNumberHome);
        dest.writeInt(userCep);
        dest.writeString(userComplement);
        dest.writeString(userSector);
        dest.writeString(userCity);
        dest.writeString(userState);
    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("userName",userName);
        result.put("userEmail",userEmail);
        result.put("userUser", userUser);
        result.put("userPhone",userPhone);
        result.put("userUid",userUid);
        result.put("userUrlPhoto",userUrlPhoto);

        result.put("status",status);
        result.put("timestamp",timestamp);
        result.put("typePayment",typePayement);
        result.put("totalPayment",totalPayment);
        result.put("qtdItens",qtdItens);

        result.put("userStreet",userStreet);
        result.put("userNumberHome",userNumberHome);
        result.put("userCep",userCep);
        result.put("userComplement",userComplement);
        result.put("userSector",userSector);
        result.put("userCity",userCity);
        result.put("userState",userState);

        return result;
    }

}

