package com.escst.socket.entity.hongmo;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Tian on 2016/1/22.
 */
public class DeviceTask implements Serializable
{
	private static final long serialVersionUID = 1L;
    private String id;//主键
    private String sn;//设备序列号
    private String data;//要发送的数据
    private Byte cmd;//命令字
    private String filePath;//文件路径,有文件操作时存放文件的位置
    private Byte result;//执行结果 1成功 2失败
    private String resultDetail;//执行结果的详细信息
    private Date oprateTime;//操作时间
    private Date returnTime;//执行时间
    private Byte taskState;//任务状态，0：未处理，1：已处理 2：处理异常
    private String oprator;//操作员
    private Byte exeTimes;//执行次数
    private String projectId;//项目ID

    public DeviceTask()
    {
    }

    public DeviceTask(String sn, String data, Byte cmd, String filePath, Byte result, String resultDetail, Date oprateTime, Date returnTime,Byte taskState)
    {
        this.sn = sn;
        this.data = data;
        this.cmd = cmd;
        this.filePath = filePath;
        this.result = result;
        this.resultDetail = resultDetail;
        this.oprateTime = oprateTime;
        this.returnTime = returnTime;
        this.taskState = taskState;
    }

    public DeviceTask(String sn, String data, Byte cmd, Date oprateTime, Byte taskState, String oprator)
    {
        this.sn = sn;
        this.data = data;
        this.cmd = cmd;
        this.oprateTime = oprateTime;
        this.taskState = taskState;
        this.oprator = oprator;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getSn()
    {
        return sn;
    }

    public void setSn(String sn)
    {
        this.sn = sn;
    }

    public String getData()
    {
        return data;
    }

    public void setData(String data)
    {
        this.data = data;
    }

    public Byte getCmd()
    {
        return cmd;
    }

    public void setCmd(Byte cmd)
    {
        this.cmd = cmd;
    }

    public String getFilePath()
    {
        return filePath;
    }

    public void setFilePath(String filePath)
    {
        this.filePath = filePath;
    }

    public Byte getResult()
    {
        return result;
    }

    public void setResult(Byte result)
    {
        this.result = result;
    }

    public String getResultDetail()
    {
        return resultDetail;
    }

    public void setResultDetail(String resultDetail)
    {
        this.resultDetail = resultDetail;
    }

    public Date getOprateTime()
    {
        return oprateTime;
    }

    public void setOprateTime(Date oprateTime)
    {
        this.oprateTime = oprateTime;
    }

    public Date getReturnTime()
    {
        return returnTime;
    }

    public void setReturnTime(Date returnTime)
    {
        this.returnTime = returnTime;
    }

    public Byte getTaskState()
    {
        return taskState;
    }

    public void setTaskState(Byte taskState)
    {
        this.taskState = taskState;
    }

    public String getOprator()
    {
        return oprator;
    }

    public void setOprator(String oprator)
    {
        this.oprator = oprator;
    }

    public Byte getExeTimes()
    {
        return exeTimes;
    }

    public void setExeTimes(Byte exeTimes)
    {
        this.exeTimes = exeTimes;
    }

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
    
    
    
}

