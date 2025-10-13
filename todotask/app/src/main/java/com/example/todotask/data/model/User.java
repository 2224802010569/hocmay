package com.example.todotask.data.model;

public class User {
    private int userId;
    private String name;
    private String gmail;
    private String password;

    public User() {}

    public User(int userId, String name, String gmail, String password) {
        this.userId = userId;
        this.name = name;
        this.gmail = gmail;
        this.password = password;
    }

    // Getter - Setter
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getGmail() { return gmail; }
    public void setGmail(String gmail) { this.gmail = gmail; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
