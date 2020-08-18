package tw.com.bussinessmeet.bean;

import java.util.Date;

public class FriendLabelBean {
    private Integer friendLabelNo;
    private String content;
    private Integer friendCustomizationNo;
    private String createDate;
    private String modifyDate;
public Integer getFriendLabelNo() {
        return friendLabelNo;
    }

    public void setFriendLabelNo(Integer friendLabelNo) {
        this.friendLabelNo = friendLabelNo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
}
