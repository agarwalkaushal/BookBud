package com.bookbud.hp.firebasebook;

/**
 * Created by HP on 07-08-2017.
 */

public class book {
    public  String a;
    public  String b;
    public  String c;
    public  String k;
    public  String e;
    public  String f;
    public String g;
    public String h;
    public String i;
    public String j;
    public String d;
    public String image;
    public book()
    {}
    public book(String nameofuser,String authorofbook, String coursename,String coursecode,
                String regnumber,String phnumber,String emailid,String pricei,String desci,String edi, String pubi, String img) {
        a = nameofuser;
        b=phnumber;
        c=authorofbook;
        d=coursename;
        e=coursecode;
        f=regnumber;
        g=emailid;
        h=pricei;
        j=edi;
        k=pubi;
        i=desci;
        image=img;
    }
    public String returnName()
    {
        return  a;}
    public  String returnAuthor()
    {
        return  c;}
    public String returnCourse()
    {
        return d ;}
    public String returnCode()
    {
        return  e;}
    public String returnContact()
    {
        return  b;}
    public String returnRegno()
    {
        return  f;}
    public String returnEmail()
    {
        return  g;}
    public String returnPrice()
    {
        return h;
    }
    public String returnDesc()
    {
        return i;
    }
    public String returnEdition()
    {
        return j;
    }
    public String returnPublisher()
    {
        return k;
    }
    public String returnImage(){ return image;}

}
