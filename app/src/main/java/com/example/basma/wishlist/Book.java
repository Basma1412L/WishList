package com.example.basma.wishlist;


public class Book {

    String book_Name;
    String book_Status;
    String exchanger_name;

    public Book()
    {}

    public Book(String book_Name, String book_Status) {
        this.book_Name = book_Name;
        this.book_Status = book_Status;
    }


    public Book(String book_Name, String book_Status,String exchanger_name) {
        this.book_Name = book_Name;
        this.book_Status = book_Status;
        this.exchanger_name=exchanger_name;
    }


    public String getExchanger_name() {
        return exchanger_name;
    }

    public void setExchanger_name(String exchanger_name) {
        this.exchanger_name = exchanger_name;
    }

    public String getBook_Name() {
        return book_Name;
    }

    public String getBook_Status() {
        return book_Status;
    }

    public void setBook_Status(String book_Status) {
        this.book_Status = book_Status;
    }

    public void setBook_Name(String book_Name) {
        this.book_Name = book_Name;
    }
}
