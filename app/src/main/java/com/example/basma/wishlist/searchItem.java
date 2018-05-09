package com.example.basma.wishlist;


public class searchItem {

    User user;
    String searchBookStatus;

    public searchItem(User user, String searchBookStatus) {
        this.user = user;
        this.searchBookStatus = searchBookStatus;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getSearchBookStatus() {
        return searchBookStatus;
    }

    public void setSearchBookStatus(String searchBookStatus) {
        this.searchBookStatus = searchBookStatus;
    }
}
