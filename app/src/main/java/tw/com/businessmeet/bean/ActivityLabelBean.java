package tw.com.businessmeet.bean;

import java.util.Date;

public class ActivityLabelBean {
    private static String[] column = new String[]{"activityLabel_no","activity_no","content","create_date","modify_date"};
    private Integer activityLabelNo;
    private Integer activityNo;
    private String content;
    private String createDate;
    private String modifyDate;
    private Integer statusCode;

    public static String[] getColumn() {
        return column;
    }

    public Integer getActivityLabelNo() {
        return activityLabelNo;
    }

    public void setActivityLabelNo(Integer activityLabelNo) {
        this.activityLabelNo = activityLabelNo;
    }

    public Integer getActivityNo() {
        return activityNo;
    }

    public void setActivityNo(Integer activityNo) {
        this.activityNo = activityNo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
