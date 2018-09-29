package com.example.ali.listviewapi;

import android.os.Parcel;
import android.os.Parcelable;

public class listObject implements Parcelable {
    private String orgName;
    private String orgNumber;
    private String hjemmeSide;
    private String adress;
    private String postNr;

    public listObject(String orgName, String orgNumber, String hjemmeSide, String adress, String postNr)
    {
        this.orgName = orgName;
        this.orgNumber = orgNumber;
        this.hjemmeSide = hjemmeSide;
        this.adress = adress;
        this.postNr = postNr;

    }

    public String getOrgName() {
        return orgName;
    }

    public String getOrgNumber() {
        return orgNumber;
    }

    public String getHjemmeSide() {
        return hjemmeSide;
    }

    public String getAdress() {
        return adress;
    }

    public String getPostNr() {
        return postNr;
    }

    protected listObject(Parcel in) {
        orgName = in.readString();
        orgNumber = in.readString();
        hjemmeSide = in.readString();
        adress = in.readString();
        postNr = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(orgName);
        dest.writeString(orgNumber);
        dest.writeString(hjemmeSide);
        dest.writeString(adress);
        dest.writeString(postNr);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<listObject> CREATOR = new Parcelable.Creator<listObject>() {
        @Override
        public listObject createFromParcel(Parcel in) {
            return new listObject(in);
        }

        @Override
        public listObject[] newArray(int size) {
            return new listObject[size];
        }
    };
}