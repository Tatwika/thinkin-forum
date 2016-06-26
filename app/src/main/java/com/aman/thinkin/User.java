package com.aman.thinkin;

public class User {
    //  private String UserID;
    private String fname;
    private String lname;
    private String branch;
    private String section;
    private String year;

    public User(){}

    public User( String fname, String lname,String branch,String section,String year){
        //this.UserID = UserID;
        this.fname = fname;
        this.lname=lname;
        this.branch=branch;
        this.section=section;
        this.year=year;
    }

    // public String getUserID() {
    //return UserID;
    //}

    public String getFname() {
        return fname;
    }
    public String getLname() {
        return lname;
    }

    public String getBranch() {
        return branch;
    }
    public String getSection() {
        return section;
    }
    public String getYear() {
        return year;
    }

}

