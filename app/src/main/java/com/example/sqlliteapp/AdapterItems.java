package com.example.sqlliteapp;

public class AdapterItems
{
    public  long ID;
    public  String DateTime;
    public  String Title;
    public  String Description;

    AdapterItems(long ID, String DateTime, String Title, String Description)
    {
        this.ID=ID;
        this.DateTime=DateTime;
        this.Title=Title;
        this.Description=Description;
    }
}
