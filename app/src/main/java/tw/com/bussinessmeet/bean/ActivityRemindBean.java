package tw.com.bussinessmeet.bean;

import java.util.Date;

public class ActivityRemindBean {
    private Integer activityRemindNo;
    private Date time;
    private Integer activityNo;
    private String createDate;
    private String modifyDate;
public Integer getActivityRemindNo() {
        return activityRemindNo;
    }

    public void setActivityRemindNo(Integer activityRemindNo) {
        this.activityRemindNo = activityRemindNo;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Integer getActivityNo() {
        return activityNo;
    }

    public void setActivityNo(Integer activityNo) {
        this.activityNo = activityNo;
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
