package tw.com.bussinessmeet.bean;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MatchedBean {
    private Integer MSno;
    private String matchedBlueTooth;
    private String blueTooth;
    private String memorandum;
    private String createDate;
    private String modifyDate;
    public Integer getMSno() {
        return MSno;
    }

    public void setMSno(Integer MSno) {
        this.MSno = MSno;
    }

    public String getMatchedBlueTooth() {
        return matchedBlueTooth;
    }

    public void setMatchedBlueTooth(String matchedBlueTooth) {
        this.matchedBlueTooth = matchedBlueTooth;
    }

    public String getBlueTooth() {
        return blueTooth;
    }

    public void setBlueTooth(String blueTooth) {
        this.blueTooth = blueTooth;
    }

    public String getMemorandum() {
        return memorandum;
    }

    public void setMemorandum(String memorandum) {
        this.memorandum = memorandum;
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
