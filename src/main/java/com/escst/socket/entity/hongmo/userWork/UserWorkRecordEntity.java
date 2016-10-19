package com.escst.socket.entity.hongmo.userWork;

import java.util.Date;

/**
 * 员工打卡记录实体类
 * Created by zcf on 2016/6/21.
 */
public class UserWorkRecordEntity {
    private String id;
    private String sn;//设备序列号
    private String name; //人员姓名
    private String duty; //职务
    private String time; //打卡日期
    private Date amStartTime; //上午打卡上班时间
    private Date amEndTime; //上午打卡下班时间
    private Date pmStartTime; //下午打卡上班时间
    private Date pmEndTime; //下午打卡下班时间
    private Date overStartTime;//加班签到打卡时间
    private Date overEndTime;//加班签退打卡时间
    private Date outStartTime;//外出签到打卡时间
    private Date outEndTime;//外出签退打卡时间
    private String projectid;
    private String projectname;
    private float workDate;//工作时长
    private float outDate;//外出时长
    private float overDate;//加班时长

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Date getAmStartTime() {
        return amStartTime;
    }

    public void setAmStartTime(Date amStartTime) {
        this.amStartTime = amStartTime;
    }

    public Date getAmEndTime() {
        return amEndTime;
    }

    public void setAmEndTime(Date amEndTime) {
        this.amEndTime = amEndTime;
    }

    public Date getPmStartTime() {
        return pmStartTime;
    }

    public void setPmStartTime(Date pmStartTime) {
        this.pmStartTime = pmStartTime;
    }

    public Date getPmEndTime() {
        return pmEndTime;
    }

    public void setPmEndTime(Date pmEndTime) {
        this.pmEndTime = pmEndTime;
    }

    public Date getOverStartTime() {
        return overStartTime;
    }

    public void setOverStartTime(Date overStartTime) {
        this.overStartTime = overStartTime;
    }

    public Date getOverEndTime() {
        return overEndTime;
    }

    public void setOverEndTime(Date overEndTime) {
        this.overEndTime = overEndTime;
    }

    public Date getOutStartTime() {
        return outStartTime;
    }

    public void setOutStartTime(Date outStartTime) {
        this.outStartTime = outStartTime;
    }

    public Date getOutEndTime() {
        return outEndTime;
    }

    public void setOutEndTime(Date outEndTime) {
        this.outEndTime = outEndTime;
    }

    public String getProjectid() {
        return projectid;
    }

    public void setProjectid(String projectid) {
        this.projectid = projectid;
    }

    public String getProjectname() {
        return projectname;
    }

    public void setProjectname(String projectname) {
        this.projectname = projectname;
    }

    public float getWorkDate() {
        return workDate;
    }

    public void setWorkDate(float workDate) {
        this.workDate = workDate;
    }

    public float getOutDate() {
        return outDate;
    }

    public void setOutDate(float outDate) {
        this.outDate = outDate;
    }

    public float getOverDate() {
        return overDate;
    }

    public void setOverDate(float overDate) {
        this.overDate = overDate;
    }
}
