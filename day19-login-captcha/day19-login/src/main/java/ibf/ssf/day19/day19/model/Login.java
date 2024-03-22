package ibf.ssf.day19.day19.model;

public class Login {

    private User loginUser;
    private Captcha captcha;
    private int attempts;
    
    public Login() {
    }
    public User getLoginUser() {
        return loginUser;
    }
    public void setLoginUser(User loginUser) {
        this.loginUser = loginUser;
    }
    public Captcha getCaptcha() {
        return captcha;
    }
    public void setCaptcha(Captcha captcha) {
        this.captcha = captcha;
    }
    public int getAttempts() {
        return attempts;
    }
    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }
    @Override
    public String toString() {
        return "Login [loginUser=" + loginUser.toString() + ", captcha=" + captcha.toString() + ", attempts=" + attempts + "]";
    }

}
