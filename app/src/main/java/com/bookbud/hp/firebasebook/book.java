package com.bookbud.hp.firebasebook;

/**
 * Created by HP on 07-08-2017.
 */

public class book {
    public  String name;
    public  String contact;
    public  String author;
    public  String course;
    public  String code;
    public  String regno;
    public String email;
    public String price;
    public String desc;
    public String edition;
    public String publisher;
    public book()
    {}
    public book(String nameofuser,String authorofbook, String coursename,String coursecode,String regnumber,String phnumber,String emailid,String pricei,String desci,String edi, String pubi) {
        name = nameofuser;
        contact=phnumber;
        author=authorofbook;
        course=coursename;
        code=coursecode;
        regno=regnumber;
        email=emailid;
        price=pricei;
        edition=edi;
        publisher=pubi;
        desc=desci;
    }
    public String returnName()
    {
        return  name;}
    public  String returnAuthor()
    {
        return  author;}
    public String returnCourse()
    {
        return  course;}
    public String returnCode()
    {
        return  code;}
    public String returnContact()
    {
        return  contact;}
    public String returnRegno()
    {
        return  regno;}
    public String returnEmail()
    {
        return  email;}
    public String returnPrice()
    {
        return price;
    }
    public String returnDesc()
    {
        return desc;
    }
    public String returnEdition()
    {
        return edition;
    }
    public String returnPublisher()
    {
        return publisher;
    }

}
