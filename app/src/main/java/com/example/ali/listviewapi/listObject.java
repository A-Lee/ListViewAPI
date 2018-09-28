package com.example.ali.listviewapi;

public class listObject
{
    private String orgName, orgNumber, hjemmeSide, adress, postNr;

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
}
