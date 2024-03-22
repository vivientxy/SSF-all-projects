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
        if (null == repo.retrieveUserPassword(user.getUsername())) {
            repo.createUser(user.getUsername(), user.getPassword());
            return true;
        }
        return false;
    }

    public String retrieveUserPassword(String username) {
        return repo.retrieveUserPassword(username);
    }

    public Boolean updateUserPassword(User user) {
        if (null != repo.retrieveUserPassword(user.getUsername())) {
            repo.updateUserPassword(user.getUsername(), user.getPassword());
            return true;
        }
        return false;
    }

    public Boolean deleteUser(User user) {
        if (user.getPassword().equals(repo.retrieveUserPassword(user.getUsername()))) {
            repo.deleteUser(user.getUsername());
            return true;
        }
        return false;
    }

    public Boolean doesUserExist(User loginUser) {
        if (null == repo.retrieveUserPassword(loginUser.getUsername())) {
            return false;
        }
        return true;
    }

    public Boolean isPasswordCorrect(User loginUser) {
        if (doesUserExist(loginUser)) {
            String username = loginUser.getUsername();
            String attemptedPassword = loginUser.getPassword();
            String correctPassword = repo.retrieveUserPassword(username);
            return attemptedPassword.equals(correctPassword);
        }
        return false;
    }

    public String setDefaultPassword(User user) {
        String password = user.getUsername() + "'sPassword";
        user.setPassword(password);
        return password;
    }

}
