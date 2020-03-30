package tw.com.bussinessmeet.bean;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class UserInformationBean {

    private String blueTooth;

    private String userName;
    private String company;
    private String position;
    private String avatar;
    private String email;
    private String tel;
    private Date createDate;
    private Date modifyDate;

    public String getBlueTooth() {
        return blueTooth;
    }

    public void setBlueTooth(String blueTooth) {
        this.blueTooth = blueTooth;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }
}
