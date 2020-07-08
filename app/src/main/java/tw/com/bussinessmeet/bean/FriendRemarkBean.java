package tw.com.bussinessmeet.bean;

import java.util.Date;

public class FriendRemarkBean {
    private Integer friendRemarksNo;
    private Integer friendLabelNo;
    private Integer friendCustomizationNo;
    private Integer friendNo;
    private String createDate;
    private String modifyDate;
public Integer getFriendRemarksNo() {
        return friendRemarksNo;
    }

    public void setFriendRemarksNo(Integer friendRemarksNo) {
        this.friendRemarksNo = friendRemarksNo;
    }

    public Integer getFriendLabelNo() {
        return friendLabelNo;
    }

    public void setFriendLabelNo(Integer friendLabelNo) {
        this.friendLabelNo = friendLabelNo;
    }

    public Integer getFriendCustomizationNo() {
        return friendCustomizationNo;
    }

    public void setFriendCustomizationNo(Integer friendCustomizationNo) {
        this.friendCustomizationNo = friendCustomizationNo;
    }

    public Integer getFriendNo() {
        return friendNo;
    }

    public void setFriendNo(Integer friendNo) {
        this.friendNo = friendNo;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(String modifyDate) {
        this.modifyDate = modifyDate;
    }
}
