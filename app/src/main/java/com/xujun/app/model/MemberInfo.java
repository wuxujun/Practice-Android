package com.xujun.app.model;

/**
 * Created by xujunwu on 15/9/30.
 */
public class MemberInfo extends BaseEntity {

    private String      userName;
    private int         sex;
    private String      srcPlace;
    private String      brithday;
    private String      schoolName;
    private String      specialty;
    private String      graduation;
    private String      educational;
    private String      gradepoint;

    private String      mobile;
    private String      email;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getSrcPlace() {
        return srcPlace;
    }

    public void setSrcPlace(String srcPlace) {
        this.srcPlace = srcPlace;
    }

    public String getBrithday() {
        return brithday;
    }

    public void setBrithday(String brithday) {
        this.brithday = brithday;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getGraduation() {
        return graduation;
    }

    public void setGraduation(String graduation) {
        this.graduation = graduation;
    }

    public String getEducational() {
        return educational;
    }

    public void setEducational(String educational) {
        this.educational = educational;
    }

    public String getGradepoint() {
        return gradepoint;
    }

    public void setGradepoint(String gradepoint) {
        this.gradepoint = gradepoint;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
