package tw.com.businessmeet.bean;

import java.util.Date;

public class FriendGroupBean {

    private static String[] column = new String[]{"friend_group_no","group_no","friend_no","create_date","modify_date"};
    private Integer friendGroupNo;
    private Integer groupNo;
    private Integer friendNo;
    private String createDate;
    private String modifyDate;
    private Integer statusCode;

    public static String[] getColumn() {
        return column;
    }

    public Integer getFriendGroupNo() {
        return friendGroupNo;
    }

    public void setFriendGroupNo(Integer friendGroupNo) {
        this.friendGroupNo = friendGroupNo;
    }

    public Integer getGroupNo() {
        return groupNo;
    }

    public void setGroupNo(Integer groupNo) {
        this.groupNo = groupNo;
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

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }
}
