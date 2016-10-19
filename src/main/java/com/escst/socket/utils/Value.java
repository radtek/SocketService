package com.escst.socket.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 约定的常量
 */
public class Value
{
    /**
     * 编码字符集
     */
    public final static String CharSet = "UTF-8";
    /**
     * 请求的包头标记
     */
    public final static int RequestHeaderTag = 0xDDCC;

    /**
     * 响应的包头标记
     */
    public final static int ResponseHeaderTag = 0xCCDD;

    /**
     * 包头标记Key
     */
    public final static String Head_Tag = "Tag";
    /**
     * 包长Key
     */
    public final static String Head_PkgLen = "PkgLen";
    /**
     * 命令字Key
     */
    public final static String Head_CMD = "cmd";
    /**
     * 包索引Key
     */
    public final static String Head_PkgIndex = "PkgIndex";
    /**
     * 包总数Key
     */
    public final static String Head_PkgCount = "PkgCount";
    /**
     * 状态码Key
     */
    public final static String Head_State = "State";
    /**
     * 包头SN
     */
    public final static String Head_SN = "sn";


    /**
     * DES Key 上传
     */
    public final static int CMD_DES = 0xFF;

    /**
     * 心跳包
     */
    public final static int CMD_Heartbeat = 0x10;

    /**
     * 上传设备信息
     */
    public final static int CMD_DevInfo = 0x11;

    /**
     * 设置时间
     */
    public final static int CMD_SetTime = 0x12;

    /**
     * 设置配置参数
     */
    public final static int CMD_SetConfig = 0x13;

    /**
     * 删除设备数据
     */
    public final static int CMD_DelData = 0x14;

    /**
     * 下发人员信息
     */
    public final static int CMD_SendStaffInfo = 0x15;

    /**
     * 加载并使用新的人员及模版信息
     */
    public final static int CMD_LoadNewMod = 0x16;

    /**
     * 下发用户照片
     */
    public final static int CMD_SendPhoto = 0x17;

    /**
     * 下发文件
     */
    public final static int CMD_SendFile = 0x18;

    /**
     * 上传识别结果
     */
    public final static int CMD_IdentifyLog = 0x19;

    /**
     * 上传脱机注册信息
     */
    public final static int CMD_RemoteStaffInfo = 0x1A;

    /**
     * 重启设备
     */
    public final static int CMD_Reboot = 0x1B;

    /**
     * 上传运行日志
     */
    public final static int CMD_RuntimeLog = 0x1C;

    /**
     * 设置设备状态
     */
    public final static int CMD_SetState = 0x1E;

    /**
     * 下发已注册的模版
     */
    public final static int CMD_SendMod = 0x1F;

    /**
     * 开始注册
     */
    public final static int CMD_Enroll_Start = 0x20;

    /**
     * 取消注册
     */
    public final static int CMD_Enroll_Cancel = 0x21;

    /**
     * 上传注册结果
     */
    public final static int CMD_Enroll_Data = 0x22;
    
    public final static Map<Integer, String> CMD_MAP = new HashMap<Integer, String>();
    
    static
    {
        CMD_MAP.put(CMD_DelData, "删除设备数据");
        CMD_MAP.put(CMD_DevInfo, "上传设备信息");
        CMD_MAP.put(CMD_Enroll_Cancel, "取消注册");
        CMD_MAP.put(CMD_Enroll_Data, "获取注册结果");
        CMD_MAP.put(CMD_Enroll_Start, "开始注册");
        CMD_MAP.put(CMD_LoadNewMod, "加载并使用新的人员及模版信息");
        CMD_MAP.put(CMD_Reboot, "重启设备");
        CMD_MAP.put(CMD_RemoteStaffInfo, "获取脱机注册信息");
        CMD_MAP.put(CMD_RuntimeLog, "获取运行日志");
        CMD_MAP.put(CMD_SendFile, "下发文件");
        CMD_MAP.put(CMD_SendMod, "下发已注册的模版");
        CMD_MAP.put(CMD_SendPhoto, "下发用户照片");
        CMD_MAP.put(CMD_SendStaffInfo, "下发人员信息");
        CMD_MAP.put(CMD_SetConfig, "设置配置参数");
        CMD_MAP.put(CMD_SetState, "设置设备状态");
        CMD_MAP.put(CMD_SetTime, "设置时间");
    }
    

}
