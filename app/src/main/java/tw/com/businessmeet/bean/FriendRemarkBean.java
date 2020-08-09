package tw.com.businessmeet.bean;

import java.util.Date;

public class FriendRemarkBean {

    private static String[] column = new String[]{"friend_remarks_no","friend_label_no","friend_customization_no","create_date","modify_date"};
    private Integer friendRemarksNo;
    private Integer friendLabelNo;
    private Integer friendCustomizationNo;
    private String createDate;
    private String modifyDate;
    private Integer statusCode;

    public static String[] getColumn() {
        return column;
    }

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

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }
}
