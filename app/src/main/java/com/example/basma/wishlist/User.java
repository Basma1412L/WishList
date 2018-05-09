package com.example.basma.wishlist;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

public class User {


    String id;
    String name;
    String email;
    String gender;
    String faculty;
    String years;
    String profilePicture;
    List<Book> books=new ArrayList<>();


    public User()
    {

    }

    public User(String id, String name, String email,String gender, String faculty, String years) {
        this.id = id;
        this.name = name;
        this.email=email;
        this.gender = gender;
        this.faculty = faculty;
        this.years = years;
    }


    public User(String id, String name, String email,String gender, String faculty, String years,String profilePicture) {
        this.id = id;
        this.name = name;
        this.email=email;
        this.gender = gender;
        this.faculty = faculty;
        this.years = years;
        this.profilePicture=profilePicture;
    }




    public User(String id, String name, String email,String gender, String faculty, String years,String profilePicture, List<Book> books) {
        this.id = id;
        this.name = name;
        this.email=email;
        this.gender = gender;
        this.faculty = faculty;
        this.years = years;
        this.profilePicture=profilePicture;
        this.books = books;

    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public User(String id, String name, String email, String gender, String faculty, String years, List<Book> books) {
        this.id = id;
        this.name = name;
        this.email=email;
        this.gender = gender;
        this.faculty = faculty;
        this.years = years;
        this.books = books;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void addBook(Book book)
    {
        if(books==null)

        {
            books=new ArrayList<>();
        }

        books.add(book);

    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getFaculty() {
        return faculty;
    }

    public String getYears() {
        return years;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public void setYears(String years) {
        this.years = years;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
