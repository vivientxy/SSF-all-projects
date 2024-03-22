package ibf.ssf.day19.day19.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class User {

    @NotNull(message = "Username cannot be null!")
    @Size(min = 5, max = 15, message = "Username length should be between 5 and 15 characters")
    private String username;
    @NotNull(message = "Password cannot be null!")
    @Size(min = 8, max = 25, message = "Password length should be between 8 and 25 characters")
    private String password;
    
    public User() {
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    @Override
    public String toString() {
        return username + "," + password;
    }
    public String generateDefaultPassword() {
        return (username + "'sPassword");
    }
}
