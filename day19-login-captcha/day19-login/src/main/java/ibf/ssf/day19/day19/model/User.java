package ibf.ssf.day19.day19.model;

import java.util.IllegalFormatException;

import jakarta.validation.constraints.Size;

public class User {

    @Size(min = 5, message = "Username must have at least 5 characters")
    @Size(max = 20, message = "The maximum length for username is 20 characters")
    private String username;
    @Size(min = 8, message = "Password must have at least 8 characters")
    @Size(max = 30, message = "The maximum length for password is 30 characters")
    private String password;
    private String securityQuestion;
    @Size(min = 5, message = "Security Answer must have at least 5 characters")
    @Size(max = 50, message = "The maximum length for Security Answer is 50 characters")
    private String securityAnswer;
    
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
    public String getSecurityQuestion() {
        return securityQuestion;
    }
    public void setSecurityQuestion(String securityQuestion) {
        this.securityQuestion = securityQuestion;
    }
    public String getSecurityAnswer() {
        return securityAnswer;
    }
    public void setSecurityAnswer(String securityAnswer) {
        this.securityAnswer = securityAnswer;
    }
    @Override
    public String toString() {
        // ensure format matches with strToUser() method
        return username + "," + password + "," + securityQuestion + "," + securityAnswer;
    }

    // strToUser method here to cross-check that it matches with toString()
    public static User strToUser(String userString) throws IllegalFormatException, ArrayIndexOutOfBoundsException {
        String[] userFields = userString.split(",");
        User user = new User();
        user.setUsername(userFields[0]);
        user.setPassword(userFields[1]);
        user.setSecurityQuestion(userFields[2]);
        user.setSecurityAnswer(userFields[3]);
        return user;
    }
    
}
