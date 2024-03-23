package ibf.ssf.day19.day19.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ibf.ssf.day19.day19.model.User;
import ibf.ssf.day19.day19.repo.UserRepo;

@Service
public class UserService {
    
    @Autowired
    UserRepo repo;

    // CRUD
    public Boolean createUser(User user) {
        if (!repo.doesUserExist(user.getUsername())) {
            repo.createUser(user);
            return true;
        }
        return false;
    }

    public User retrieveUser(String username) {
        String userString = repo.retrieveUser(username);
        User user;
        try {
            user = User.strToUser(userString);
        } catch (Exception e) {
            e.printStackTrace();
            user = new User();
        }
        return user;
    }

    public Boolean updateUser(User user) {
        if (repo.doesUserExist(user.getUsername())) {
            repo.updateUser(user);
            return true;
        }
        return false;
    }

    public Boolean deleteUser(User user) {
        String username = user.getUsername();
        // check if password correct, verify identity before deleting
        String enteredPassword = user.getPassword();
        String recordedPassword;
        try {
            recordedPassword = User.strToUser(repo.retrieveUser(username)).getPassword();
        } catch (Exception e) {
            e.printStackTrace();
            recordedPassword = null;
        }

        if (enteredPassword.equals(recordedPassword)) {
            repo.deleteUser(username);
            return true;
        }
        return false;
    }

    public Boolean doesUserExist(User loginUser) {
        return repo.doesUserExist(loginUser.getUsername());
    }

    public Boolean doesUserExist(String username) {
        return repo.doesUserExist(username);

    }

    public Boolean isPasswordCorrect(User loginUser) {
        if (doesUserExist(loginUser)) {
            String username = loginUser.getUsername();
            String enteredPassword = loginUser.getPassword();
            String recordedPassword;
            try {
                recordedPassword = User.strToUser(repo.retrieveUser(username)).getPassword();
            } catch (Exception e) {
                e.printStackTrace();
                recordedPassword = null;
            }
            return enteredPassword.equals(recordedPassword);
        }
        return false;
    }

    public Boolean isSecurityAnswerCorrect(User loginUser) {
        if (doesUserExist(loginUser)) {
            String username = loginUser.getUsername();
            String enteredAnswer = loginUser.getSecurityAnswer();
            String recordedAnswer;
            try {
                recordedAnswer = User.strToUser(repo.retrieveUser(username)).getSecurityAnswer();
            } catch (Exception e) {
                e.printStackTrace();
                recordedAnswer = null;
            }
            return enteredAnswer.equals(recordedAnswer);
        }
        return false;
    }

    public void updateUserPassword(String username, String newPassword) {
        User retrievedUser = retrieveUser(username);
        retrievedUser.setPassword(newPassword);
        repo.updateUser(retrievedUser);
    }

}
