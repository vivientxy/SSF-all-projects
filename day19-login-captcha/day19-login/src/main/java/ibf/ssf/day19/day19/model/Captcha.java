package ibf.ssf.day19.day19.model;

import java.util.Random;

public class Captcha {

    private Integer num1;
    private Integer num2;
    private Integer ans;

    public Captcha() {
    }
    public Integer getNum1() {
        return num1;
    }
    public void setNum1(Integer num1) {
        this.num1 = num1;
    }
    public Integer getNum2() {
        return num2;
    }
    public void setNum2(Integer num2) {
        this.num2 = num2;
    }
    public Integer getAns() {
        return ans;
    }
    public void setAns(Integer ans) {
        this.ans = ans;
    }
    public static Integer generate() {
        Random rand = new Random();
        return rand.nextInt(10);
    }
    public Boolean isCorrect() {
        if (null == this.ans) {
            return false;
        }
        Integer ans1 = this.ans/10;
        Integer ans2 = this.ans%10;
        System.out.println("========== answers =========");
        System.out.println(ans1);
        System.out.println(ans2);
        return (this.num1==ans1 && this.num2==ans2);
    }
    public Boolean isEmpty() {
        return (null == this.num1);
    }
    @Override
    public String toString() {
        return "Captcha [num1=" + num1 + ", num2=" + num2 + ", ans=" + ans + "]";
    }
}
