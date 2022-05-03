


package com.example.olio_ht;

public class User {
    private String password, username, email;

    public User(String username, String password, String email) {
        username = "";
        password = "";
        email = "";
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public void setUsername(String Username) {
        this.username = Username;
    }

    public void setPassword(String Password) {
        this.password = Password;
    }

    public void setEmail(String Email) {
        this.email = Email;
    }
}
