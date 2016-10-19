package com.escst.socket.service.guangyang;

import com.escst.socket.dao.localSqlServer.guangyang.EnvorimentDBDao;
import com.escst.socket.dao.localSqlServer.guangyang.LiftDao;
import com.escst.socket.dao.localSqlServer.guangyang.PropellingMovementDBDao;
import com.escst.socket.dao.localSqlServer.guangyang.TowerDBDao;
import com.escst.socket.dao.localSqlServer99.guangyang.EnvorimentDBDao99;
import com.escst.socket.dao.localSqlServer99.guangyang.TowerDBDao99;
import com.escst.socket.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

/**
 * 光洋环境数据解析
 * Created by zcf on 2016/7/1.
 */
public class GYEnvorimentService extends Thread {
    Logger logger = LoggerFactory.getLogger(GYEnvorimentService.class);
    private Socket socket;
    private InputStream is;
    private OutputStream os;
    private EnvorimentDBDao envorimentDBDao;
    private TowerDBDao towerDBDao;
    private PropellingMovementDBDao propellingMovementDBDao;
    // 连接99数据库
    private EnvorimentDBDao99 envorimentDBDao99;
    private TowerDBDao99 towerDBDao99;
    private LiftDao liftDao;

    public GYEnvorimentService(Socket socket, EnvorimentDBDao envorimentDBDao, TowerDBDao towerDBDao, LiftDao liftDao, PropellingMovementDBDao propellingMovementDBDao,
                               EnvorimentDBDao99 envorimentDBDao99, TowerDBDao99 towerDBDao99) {
        this.socket = socket;
        this.envorimentDBDao = envorimentDBDao;
        this.towerDBDao = towerDBDao;
        this.propellingMovementDBDao = propellingMovementDBDao;
        this.envorimentDBDao99 = envorimentDBDao99;
        this.towerDBDao99 = towerDBDao99;
        this.liftDao = liftDao;
    }

    @Override
    public void run() {
        try {
            is = socket.getInputStream();
            os = socket.getOutputStream();
            //塔吊数据
            //&01-tc_5610-t-1-00-50.7-0.0666707-22.7456-29.8054-50-36-0-110.4-110.7-0-0-00-00-00-00-00-4#-56$
            String resultStr = "SUCCESS";
            os.write(resultStr.getBytes());
            os.flush();
            String dataArr = InputToByteArray.toByteArray(is);
            logger.info("西安光洋数据：" + dataArr);
            String clientIp = socket.getInetAddress().getHostAddress();
            logger.info("客户端IP：" + clientIp);

            //解析数据
            doParamsHandle(dataArr, clientIp);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("GYEnvorimentService ERROR:" + e.getMessage());
        } finally {
            FreeIO.free(is);
            FreeIO.free(os);
            FreeIO.free(socket);
        }
    }

    public void doParamsHandle(String params, String IP) throws IOException {
        //转换成String数组
        String[] paramArr = params.split("-");
//		&06-00_00- -00-00-00-00-00-00-00-00-40-00-00-8-8-00-00-00-00-00-11#-82$
//		1编号  2规格型号 3区分类型（t塔吊 h环境） 4开机状态 5报警内容  6运行角度  7当前吊重 8吊钩高度9小车位移
//		10大臂长度  11 塔机高度 12风速  13倾角A  14倾角B 15颗粒物（PM2.5  16颗粒物（PM10
//		17温度   18湿度   19噪声  20有毒气体   21可燃气体  22站号    23校验

        //区分数据是设备的还是环境的（t塔吊 h环境）
        String type = paramArr[2];

        if (type.equals("t")) {
            //塔吊数据
            System.out.println("西安光洋塔吊数据解析......");
            //写入.txt文档
            contentToTxt("D:\\towerLogs.txt", params);
            contentToTxt("D:\\towerLogs.txt", "-------------------------------------------------------------------------------------");

            storeToDb(paramArr);
        } else if (type.equals("h")) {
            //环境数据
            System.out.println("西安光洋环境数据解析......");
            //写入.txt文档
            contentToTxt("D:\\envorimentLogs.txt", params);
            contentToTxt("D:\\envorimentLogs.txt", "-------------------------------------------------------------------------------------");

            storeToDb(paramArr, IP);
        } else if (type.equals("s")) {
            System.out.println("西安光洋升降机数据解析");
            //写入txt文档
            contentToTxt("D:\\liftLogs.txt", params);
            contentToTxt("D:\\liftLogs.txt", "-----------------------------------------------------------------------------------------");
            storeToDbLift(paramArr);

        }
    }

    public void contentToTxt(String filePath, Object message) {
        String str = new String(); // 原有txt内容
        String s1 = new String();// 内容更新
        try {
            File f = new File(filePath);
            if (f.exists()) {
                System.out.print("文件存在");
            } else {
                System.out.print("文件不存在");
                f.createNewFile();// 不存在则创建
            }
            BufferedReader input = new BufferedReader(new FileReader(f));

            while ((str = input.readLine()) != null) {
                s1 += str + "\n";
            }
//			System.out.println(s1);
            input.close();
            s1 += message;

            BufferedWriter output = new BufferedWriter(new FileWriter(f));
            output.write(s1);
            output.close();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void storeToDb(String[] list) throws IOException {
        try {
            System.out.println("塔吊参数个数：" + list.length);
//            String str = list[0].trim();
//            String sensorNo = str.substring(str.indexOf("&")+1, str.length());
            String sensorNo = list[1].trim();
//			String sensorNo = noStr.substring(1, noStr.length());
            //根据传感器编号查询项目ID和项目名称
            String towercraneno = "";
            String architecturalid = "";
            String architecturalname = "";
            Map<String, String> sensorList = towerDBDao.findTd(sensorNo);
            if (null != sensorList && sensorList.size() > 0) {
                towercraneno = sensorList.get("TOWERCRANENO");
                architecturalid = sensorList.get("ARCHITECTURALID");
                architecturalname = sensorList.get("ARCHITECTURALNAME");
            }
            String warningAndAlarm = ParamsConvertUtil.towerParamsConvert(list[4]);
            String[] alarm = warningAndAlarm.split("#");
            //历史数据
            towerDBDao.insertStore(list, towercraneno, architecturalid, architecturalname, alarm);

            // 99数据库
            //根据传感器编号查询项目ID和项目名称
            String towercraneno99 = "";
            String architecturalid99 = "";
            String architecturalname99 = "";
            Map<String, String> sensorList99 = towerDBDao99.findTd(sensorNo);
            if (null != sensorList99 && sensorList99.size() > 0) {
                towercraneno99 = sensorList99.get("TOWERCRANENO");
                architecturalid99 = sensorList99.get("ARCHITECTURALID");
                architecturalname99 = sensorList99.get("ARCHITECTURALNAME");
            }
            //历史数据
            towerDBDao99.insertStore(list, towercraneno99, architecturalid99, architecturalname99, alarm);
            //是否为报警数据
            String result = list[4];
            if (!result.equals("N")) {
                towerDBDao.insertAlarmStore(list, towercraneno, architecturalid, architecturalname, alarm);
                towerDBDao99.insertAlarmStore(list, towercraneno99, architecturalid99, architecturalname99, alarm);
                //caozx add 添加塔吊推送数据
                if(alarm[1] != null && !"无报警".equals(alarm[1])){
                    String content = "塔吊" + alarm[1];//推送内容
                    int type = 0;
                    int style = 3;//2环境报警 3设备报警 4代办 5待阅 6到场提醒
                    propellingMovementDBDao.insert(content, type, style, architecturalid, null);
                }
            }
            //查询实时数据表中是否存在此设备的数据
            int total = towerDBDao.findTowerRealByNo(sensorNo);
            if (total > 0) {
                //更新塔吊实时数据
                towerDBDao.updateStore(list, towercraneno, architecturalid, architecturalname, alarm);
            } else {
                //写入塔吊实时数据
                towerDBDao.insertRealStore(list, towercraneno, architecturalid, architecturalname, alarm);
            }

            // 更新99数据库
            int total99 = towerDBDao99.findTowerRealByNo(sensorNo);
            if (total99 > 0) {
                //更新塔吊实时数据
                towerDBDao99.updateStore(list, towercraneno99, architecturalid99, architecturalname99, alarm);
            } else {
                //写入塔吊实时数据
                towerDBDao99.insertRealStore(list, towercraneno99, architecturalid99, architecturalname99, alarm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 光洋环境数据存储
     *
     * @param paramArr
     * @param IP
     * @throws IOException
     */
    public void storeToDb(String[] paramArr, String IP) throws IOException {
        // Test bfr = new;
        try {
            //根据传感器编号查询项目ID和项目名称
            String sss = paramArr[0].trim();
            String sensorNo = sss.substring(sss.indexOf("&") + 1, sss.length());

            Map<String, String> sensorList = envorimentDBDao.findSensorNo(sensorNo);
            String architecturalid = sensorList.get("ARCHITECTURALID");
            String architecturalname = sensorList.get("ARCHITECTURALNAME");

            String dateStr = FormatDateUtil.getGYDateToString(new Date());
            //环境历史数据保存
            envorimentDBDao.insertHistoryStore(paramArr, IP, architecturalid, architecturalname, sensorNo, dateStr);
            // 99数据库更新
            Map<String, String> sensorList99 = envorimentDBDao99.findSensorNo(sensorNo);
            String architecturalid99 = sensorList99.get("ARCHITECTURALID");
            String architecturalname99 = sensorList99.get("ARCHITECTURALNAME");
            envorimentDBDao99.insertHistoryStore(paramArr, IP, architecturalid99, architecturalname99, sensorNo, dateStr);

            //环境报警数据保存
            String alarmStr = paramArr[4];
            if (!alarmStr.equals("N")) {
                /** 报警值类型 */
                String[] alarmStrArr = paramArr[4].split("#");
                for (int i = 0; i < alarmStrArr.length; i++) {
                    String paramStr = ParamsConvertUtil.envorimentParamsConvert(alarmStrArr[i]);
                    String[] list = paramStr.split("#");
                    String alarmType = list[1];
                    float alarmValue = 0;
                    if (alarmType.equals("0")) {
                        //噪音
                        alarmValue = Float.parseFloat(paramArr[18]);
                    } else if (alarmType.equals("1")) {
                        //粉尘PM2.5
                        alarmValue = Float.parseFloat(paramArr[14]);
                    } else if (alarmType.equals("2")) {
                        // 温度
                        alarmValue = Float.parseFloat(paramArr[16]);
                    } else if (alarmType.equals("3")) {
                        //湿度
                        alarmValue = Float.parseFloat(paramArr[17]);
                    } else if (alarmType.equals("4")) {
                        //有毒气体
                        alarmValue = Float.parseFloat(paramArr[19]);
                    } else if (alarmType.equals("5")) {
                        //可燃气体
                        alarmValue = Float.parseFloat(paramArr[20]);
                    } else if (alarmType.equals("6")) {
                        //风速
                        alarmValue = Float.parseFloat(paramArr[11]);
                    } else if (alarmType.equals("7")) {
                        //粉尘PM10
                        alarmValue = Float.parseFloat(paramArr[15]);
                    }
                    envorimentDBDao.insertSensorStore(list, architecturalid, architecturalname, sensorNo, alarmValue);
                    envorimentDBDao99.insertSensorStore(list, architecturalid, architecturalname, sensorNo, alarmValue);
                    //caozx add 添加环境推送数据
                    if(list[0] != null && !"无报警".equals(list[0])){
                        String content = list[0] + "报警";//推送内容
                        int type = 0;
                        int style = 2;//2环境报警 3设备报警 4代办 5待阅 6到场提醒
                        propellingMovementDBDao.insert(content, type, style, architecturalid, null);
                    }
                }

            }
            //环境实时数据
            //查询是否存在此传感器的数据
            int total = envorimentDBDao.findSensorRealByNo(sensorNo);
            if (total == 0) {
                //插入实时数据
                envorimentDBDao.insertRealStore(paramArr, IP, architecturalid, architecturalname, sensorNo);
            } else {
                //更新实时数据
                envorimentDBDao.updateStore(paramArr, architecturalid, architecturalname, sensorNo, IP);
            }

            // 99数据库更新
            int total99 = envorimentDBDao99.findSensorRealByNo(sensorNo);
            if (total99 == 0) {
                //插入实时数据
                envorimentDBDao99.insertRealStore(paramArr, IP, architecturalid, architecturalname, sensorNo);
            } else {
                //更新实时数据
                envorimentDBDao99.updateStore(paramArr, architecturalid, architecturalname, sensorNo, IP);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void storeToDbLift(String[] list) {
        System.out.println("升降机参数个数：" + list.length);
        String str = list[0].trim();
        if ("00".equals(list[3])) {
            list[3] = "0";
        }
        if ("00".equals(list[20])) {
            list[20] = "0";
        }
        if ("00".equals(list[19])) {
            list[19] = "0";
        }
        //获取升降机编号
        String liftNo = str.substring(str.indexOf("&") + 1, str.length());

        System.out.println("升降机编号:" + liftNo);
        //    根据升降机编号查询其对应的项目ID和项目名


        Map<String, Object> liftList = liftDao.findId(liftNo);

        liftNo = (String) liftList.get("LIFTNO");

        String architecturalid = (String) liftList.get("ARCHITECTURALID");
        String architecturalname = (String) liftList.get("ARCHITECTURALNAME");

        String warningndAlarmp = ParamsConvertUtil.liftParamsConvert(list[4]);
        String[] alarm = warningndAlarmp.split("#");
        //插入历史数据表
        System.out.println(Arrays.toString(alarm));
        liftDao.insertHistoryStore(list, liftNo, architecturalid, architecturalname, alarm);
        System.out.println("历史数据插入成功！");
        //判断是否为报警数据
        String result = list[4];
        if (!result.equals("00")) {

            liftDao.insertAlarmStore(list, liftNo, architecturalid, architecturalname, alarm);
            System.out.println("报警数据插入成功！");
        }
        //查询实时数据表是否存在此设备的数据
        Integer total = liftDao.findLiftRealByNo(liftNo);
        System.out.println("该设备数据条数为：" + total);
        //若查询出来数据大于0，则说明实时数据表存在此设备的数据 进行更新操作
        if (total > 0) {
            liftDao.upLiftDateStore(list, architecturalid, architecturalname, liftNo, alarm);
            System.out.println("实时数据更新成功！");
        } else {
            //若不大于0 则说明实时数据表不存在此设备的数据 进行插入操作
            liftDao.insertRealtimeStore(list, liftNo, architecturalid, architecturalname, alarm);
            System.out.println("实时数据插入成功");
        }
    }
}
