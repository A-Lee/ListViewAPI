package com.example.ali.listviewapi;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Comparator;


//Klasse som lagrer de mest interessante informasjonene om selskapet. Implementerer Parcelable for å kunne lagres i Bundle object
public class listObject implements Parcelable, Comparator {
    private String orgName;
    private String orgNumber;
    private String hjemmeSide;
    private String adress;
    private String postSted;
    private String postNr;

    public listObject(String orgName, String orgNumber, String hjemmeSide, String adress, String postSted,String postNr)
    {
        this.orgName = orgName;
        this.orgNumber = orgNumber;
        this.hjemmeSide = hjemmeSide;
        this.adress = adress;
        this.postSted = postSted;
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
    public String getPostSted()
    {
        return postSted;
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

    @Override
    public int compare(Object o1, Object o2) {
        return 0;
    }
    public static Comparator<listObject> firmaNavnComparableAsc = new Comparator<listObject>() {

        public int compare(listObject s1, listObject s2) {
            String firmaNavn1 = s1.getOrgName().toUpperCase();
            String firmaNavn2 = s2.getOrgName().toUpperCase();


            return firmaNavn1.compareTo(firmaNavn2);

        }};
    public static Comparator<listObject> firmaNavnComparableDesc = new Comparator<listObject>() {

        public int compare(listObject s1, listObject s2) {
            String firmaNavn1 = s1.getOrgName().toUpperCase();
            String firmaNavn2 = s2.getOrgName().toUpperCase();

            return firmaNavn2.compareTo(firmaNavn1);
        }};

    public static Comparator<listObject> orgNummberComparableAsc = new Comparator<listObject>() {

        public int compare(listObject s1, listObject s2) {

            int orgNr1 = Integer.parseInt(s1.getOrgNumber());
            int orgNr2 = Integer.parseInt(s2.getOrgNumber());

            return orgNr1-orgNr2;

        }};

    public static Comparator<listObject> orgNummberComparableDesc = new Comparator<listObject>() {

        public int compare(listObject s1, listObject s2) {

            int orgNr1 = Integer.parseInt(s1.getOrgNumber());
            int orgNr2 = Integer.parseInt(s2.getOrgNumber());

            return orgNr2-orgNr1;
        }};


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