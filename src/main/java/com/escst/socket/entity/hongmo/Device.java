package com.escst.socket.entity.hongmo;

import java.util.Date;

/**
 * �豸��Ϣʵ����
 * 
 * @author guozhiqiang
 * @date 2015-8-17
 */
public class Device
{
    private String id; // 设备ID
    private String name; // 设备名称
    private String kind; // 设备类型
    private String ip; // IP
    private String sn; // 序列号
    private String func; // 设备功能
    private Byte isEnroll=0; // 是否注册机
    private Byte isDebug=0; // 是否调试状态
    private Byte isStats=1; // 是否统计
    private Byte cardFormat=0; // 输出卡号格式
    private String gateway; // 网关
    private String mask; // 子网掩码
    private Byte dhcp=0; // 是否启用dhcp
    private String version; // 版本
    private Byte delFlag = 0; // 删除标记
    private Byte syncTime = 1;// 自动同步时间
    private Date lastConnTime;//最后通讯时间
    private Byte state;//连接状态
    private String architecturalid; //工地ID
    private String architecturalname;//工地名称

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getKind()
    {
        return kind;
    }

    public void setKind(String kind)
    {
        this.kind = kind;
    }

    public String getIp()
    {
        return ip;
    }

    public void setIp(String ip)
    {
        this.ip = ip;
    }

    public String getSn()
    {
        return sn;
    }

    public void setSn(String sn)
    {
        this.sn = sn;
    }

    public String getFunc()
    {
        return func;
    }

    public void setFunc(String func)
    {
        this.func = func;
    }

    public Byte getIsEnroll()
    {
        return isEnroll;
    }

    public void setIsEnroll(Byte isEnroll)
    {
        this.isEnroll = isEnroll;
    }

    public Byte getIsDebug()
    {
        return isDebug;
    }

    public void setIsDebug(Byte isDebug)
    {
        this.isDebug = isDebug;
    }

    public Byte getIsStats()
    {
        return isStats;
    }

    public void setIsStats(Byte isStats)
    {
        this.isStats = isStats;
    }

    public Byte getCardFormat()
    {
        return cardFormat;
    }

    public void setCardFormat(Byte cardFormat)
    {
        this.cardFormat = cardFormat;
    }

    public String getGateway()
    {
        return gateway;
    }

    public void setGateway(String gateway)
    {
        this.gateway = gateway;
    }

    public String getMask()
    {
        return mask;
    }

    public void setMask(String mask)
    {
        this.mask = mask;
    }

    public Byte getDhcp()
    {
        return dhcp;
    }

    public void setDhcp(Byte dhcp)
    {
        this.dhcp = dhcp;
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }

    public Byte getDelFlag()
    {
        return delFlag;
    }

    public void setDelFlag(Byte delFlag)
    {
        this.delFlag = delFlag;
    }

    public Byte getSyncTime()
    {
        return syncTime;
    }

    public void setSyncTime(Byte syncTime)
    {
        this.syncTime = syncTime;
    }

    public Date getLastConnTime()
    {
        return lastConnTime;
    }

    public void setLastConnTime(Date lastConnTime)
    {
        this.lastConnTime = lastConnTime;
    }

    public Byte getState()
    {
        return state;
    }

    public void setState(Byte state)
    {
        this.state = state;
    }
    
    public String getArchitecturalid() {
		return architecturalid;
	}

	public void setArchitecturalid(String architecturalid) {
		this.architecturalid = architecturalid;
	}

	public String getArchitecturalname() {
		return architecturalname;
	}

	public void setArchitecturalname(String architecturalname) {
		this.architecturalname = architecturalname;
	}

	@Override
    public String toString()
    {
        return "Device [id=" + id + ", name=" + name + ", kind=" + kind
                + ", ip=" + ip + ", sn=" + sn + ", func=" + func
                + ", isEnroll=" + isEnroll
                + ", isDebug=" + isDebug + ", isStats=" + isStats
                + ", cardFormat=" + cardFormat + ", gateway=" + gateway
                + ", mask=" + mask + ", dhcp=" + dhcp + ", version=" + version
                + ", delFlag=" + delFlag + ", syncTime=" + syncTime + ", lastConnTime=" + lastConnTime+ ", state=" + state + "]";
    }

    
}
