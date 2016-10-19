package com.escst.socket.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.escst.socket.entity.hongmo.Device;

/**
 * Created by Tian on 2015/12/22.
 */
public class AllDevicePool
{
    /**
     * 所有有效的设备
     */
    public static Map<String, Device> ALL_DEVICE_POOL = new TreeMap<String, Device>();
    
    /**
     * 正在执行的任务
     */
    public static Map<String,String> DEVICETASK_POOL = new HashMap<String,String>();

    /******************** 99数据库用 ******************/
    /**
     * 所有有效的设备
     */
    public static Map<String, Device> ALL_DEVICE_POOL_99 = new TreeMap<String, Device>();

    /**
     * 正在执行的任务
     */
    public static Map<String,String> DEVICETASK_POOL_99 = new HashMap<String,String>();
    
}
