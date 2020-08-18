package tw.com.bussinessmeet.bean;

import java.util.Date;

public class ProblemReportBean {
    private Integer problemReportNo;
    private String content;
    private String userId;
    private String createDate;
    private String modifyDate;
public Integer getProblemReportNo() {
        return problemReportNo;
    }

    public void setProblemReportNo(Integer problemReportNo) {
        this.problemReportNo = problemReportNo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
