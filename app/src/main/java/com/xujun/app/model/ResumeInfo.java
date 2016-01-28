package com.xujun.app.model;

/**
 * Created by xujunwu on 15/11/28.
 */
public class ResumeInfo extends BaseEntity{

    private int         mid;
    private String    name;
    private int    sex; //0 女 1 男
    private String      srcPlace;
    private String      brithday;

    private String      school;
    private String      schoolCode;
    private String      specialty;
    private String      specialtyCode;
    private String      graduation;
    private String      educational;
    private String      educationalCode;
    private String      gradePoint;

    private String      mobile;
    private String      email;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
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

    public String getGradePoint() {
        return gradePoint;
    }

    public void setGradePoint(String gradepoint) {
        this.gradePoint = gradepoint;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSchoolCode() {
        return schoolCode;
    }

    public void setSchoolCode(String schoolCode) {
        this.schoolCode = schoolCode;
    }

    public String getSpecialtyCode() {
        return specialtyCode;
    }

    public void setSpecialtyCode(String specialtyCode) {
        this.specialtyCode = specialtyCode;
    }

    public String getEducationalCode() {
        return educationalCode;
    }

    public void setEducationalCode(String educationalCode) {
        this.educationalCode = educationalCode;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }
}
