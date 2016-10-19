package com.escst.socket.schedule;

import com.escst.socket.dao.localSqlServer.hongmo.DeviceTaskDao;
import com.escst.socket.dao.localSqlServer.hongmo.userWork.UserWorkHourDao;
import com.escst.socket.entity.hongmo.userWork.UserWorkRecordEntity;
import com.escst.socket.utils.FormatDateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 用户工时统计定时任务
 * Created by zcf on 2016/6/21.
 */
public class UserWorkHourSchedule {
    Logger logger = LoggerFactory.getLogger(UserWorkHourSchedule.class);

    public void userWorkHourTask(){
        logger.info("考勤人员工时统计开始......");
        //查询考勤设备
        List<Map<Object,Object>> deviceList = deviceTaskDao.findAllDevice();

        try {
            //统计各个考勤设备的考勤记录，统计每个人的工时
            for (Map<Object, Object> deviceMap : deviceList) {
                //上午上班时间
                String amUp = (String)deviceMap.get("amup");
                //上午下班时间
                String amDown = (String)deviceMap.get("amdown");
                //下午上班时间
                String pmUp = (String)deviceMap.get("pmup");
                //下午下班时间
                String pmDown = (String)deviceMap.get("pmdown");
                //加班开始时间
                String overStartTime = (String)deviceMap.get("overStartTime");
                //午休时间
                double noontimeRest = (double)deviceMap.get("noontimeRest");
//                double noontimeRest = Double.parseDouble(noontimeRestStr);

                //工地ID
                String architecturalid = (String)deviceMap.get("architecturalid");

                //判断一天打两次卡还是四次卡，通过上午下班打卡和下午上班打卡是否为空来判断
                if(!amDown.isEmpty() && !amDown.equals("") && !pmUp.isEmpty() && !pmUp.equals(""))
                {
                    //一天打四次卡
                    //查询所有的打卡记录
                    List<UserWorkRecordEntity> userWorkRecordList = userWorkHourDao.getUserFourWorkRecord(architecturalid,pmUp);
                    //上班和下班时间格式转换
                    Date amUpTime = FormatDateUtil.getStringToDate(amUp);
                    Date amEndTime = FormatDateUtil.getStringToDate(amDown);
                    Date pmUpTime = FormatDateUtil.getStringToDate(pmUp);
                    Date pmDownTime = FormatDateUtil.getStringToDate(pmDown);
                    for (UserWorkRecordEntity userWorkRecord : userWorkRecordList) {
                        //上午工时统计
                        //上午上班签到时间
                        Date amStartRecordTime = userWorkRecord.getAmStartTime();
                        //上午下班签退时间
                        Date amEndRecordTime = userWorkRecord.getAmEndTime();
                        //打卡时间小于规定时间返回false，否则返回true
                        float amHour = 0;//工时
                        if(null != amStartRecordTime && null != amEndRecordTime){
                            boolean amStartTimeFlag = FormatDateUtil.getDateCompany(amStartRecordTime, amUp);
                            boolean amEndTimeFlag = FormatDateUtil.getDateCompany(amEndRecordTime, amDown);
                            logger.info("UserWorkHourSchedule startTimeFlag:"+amStartTimeFlag +"\tendTimeFlag:"+amEndTimeFlag);

                            //正常情况
                            if(amStartTimeFlag == false && amEndTimeFlag == true){
                                amHour = FormatDateUtil.getWorkHour(amUpTime,amEndTime);
                            }

                            //迟到
                            if(amStartTimeFlag == true && amEndTimeFlag == true){
                                amHour = FormatDateUtil.getWorkHour(amStartRecordTime,amEndTime);
                            }

                            //早退
                            if(amStartTimeFlag == false && amEndTimeFlag == false){
                                amHour = FormatDateUtil.getWorkHour(amUpTime,amEndRecordTime);
                            }
                            //迟到早退
                            if(amStartTimeFlag == true && amEndTimeFlag == false){
                                amHour = FormatDateUtil.getWorkHour(amStartRecordTime,amEndRecordTime);
                            }
                        }

                        //下午工时计算
                        //下午上班签到时间
                        Date pmStartRecordTime = userWorkRecord.getPmStartTime();
                        //下午下班签退时间
                        Date pmEndRecordTime = userWorkRecord.getPmEndTime();
                        //打卡时间小于规定时间返回false，否则返回true
                        float pmHour = 0;//工时
                        if(null != pmStartRecordTime && null != pmEndRecordTime){
                            boolean pmStartTimeFlag = FormatDateUtil.getDateCompany(pmStartRecordTime, amUp);
                            boolean pmEndTimeFlag = FormatDateUtil.getDateCompany(pmEndRecordTime, amDown);
                            logger.info("UserWorkHourSchedule pmStartTimeFlag:"+pmStartTimeFlag +"\tpmEndTimeFlag:"+pmEndTimeFlag);

                            //正常情况
                            if(pmStartTimeFlag == false && pmEndTimeFlag == true){
                                pmHour = FormatDateUtil.getWorkHour(pmUpTime,pmDownTime);
                            }

                            //迟到
                            if(pmStartTimeFlag == true && pmEndTimeFlag == true){
                                pmHour = FormatDateUtil.getWorkHour(pmStartRecordTime,pmDownTime);
                            }

                            //早退
                            if(pmStartTimeFlag == false && pmEndTimeFlag == false){
                                pmHour = FormatDateUtil.getWorkHour(pmUpTime,pmEndRecordTime);
                            }
                            //迟到早退
                            if(pmStartTimeFlag == true && pmEndTimeFlag == false){
                                pmHour = FormatDateUtil.getWorkHour(pmStartRecordTime,pmEndRecordTime);
                            }
                        }
                        float hour = amHour + pmHour;//整天工时
                        DecimalFormat dft = new DecimalFormat("0.00");//格式化小数
                        hour = Float.parseFloat(dft.format(hour));
                        logger.info("UserWorkHourSchedule 2/day hour:"+hour);
                        //计算加班时间
                        float overHour = 0;
                        Date overTime = FormatDateUtil.getStringToDate(overStartTime);//加班签到时间
                        Date overStartRecordTime = userWorkRecord.getOverStartTime();
                        Date overEndRecordTime = userWorkRecord.getOverEndTime();

                        if(overStartRecordTime != null && overEndRecordTime != null){
                            boolean overTimeFlag2 = FormatDateUtil.getDateCompany(overStartRecordTime, overStartTime);
                            if(overTimeFlag2){
                                overHour = FormatDateUtil.getWorkHour(overStartRecordTime,overEndRecordTime);
                            }else{
                                overHour = FormatDateUtil.getWorkHour(overTime,overEndRecordTime);
                            }
                        }else{
                            //没有加班签到和签退时间，判断下班时间和加班开始时间
                            boolean overTimeFlag = FormatDateUtil.getDateCompany(pmEndRecordTime, overStartTime);
                            if(overTimeFlag){
                                overHour = FormatDateUtil.getWorkHour(overTime,pmEndRecordTime);
                            }
                        }
                        overHour = Float.parseFloat(dft.format(overHour));
                        logger.info("UserWorkHourSchedule 2/day overHour:"+overHour);

                        //计算外出时间
                        //获取外出打卡时间记录
                        float outHour = 0;
                        List<Map<String,Object>> outRecordList = userWorkHourDao.getOutTimeList(architecturalid,userWorkRecord.getTime(),userWorkRecord.getId());
                        int length = outRecordList.size();
                        if(length>=2){
                            Date beginTime = null;
                            Date endTime = null;
                            for(Map<String,Object> outMap : outRecordList){
                                Date recog_time = FormatDateUtil.getDateTime((String)outMap.get("recog_time"));
                                int auth_stat = (int)outMap.get("auth_stat");
                                if(auth_stat == 4){
                                    beginTime = recog_time;
                                }
                                if(auth_stat == 5){
                                    endTime = recog_time;
                                }
                                if(null != beginTime && null != endTime){
                                    outHour = outHour + FormatDateUtil.getWorkHour(beginTime,endTime);
                                    beginTime = null;
                                    endTime = null;
                                }
                            }
                        }

                        outHour = Float.parseFloat(dft.format(outHour));
                        logger.info("UserWorkHourSchedule 2/day outHour:"+outHour);

                        userWorkRecord.setWorkDate(hour);
                        userWorkRecord.setOverDate(overHour);
                        userWorkRecord.setOutDate(outHour);
                        //记录算出的员工工时
                        userWorkHourDao.insertUserWorkDate(userWorkRecord);
                    }
                }else{
                    //一天打两次卡
                    //查询所有的打卡记录
                    List<UserWorkRecordEntity> userWorkRecordList = userWorkHourDao.getUserWorkRecord(architecturalid);
                    for (UserWorkRecordEntity userWorkRecord : userWorkRecordList) {
                        //上班签到时间
                        Date amStartTime = userWorkRecord.getAmStartTime();
                        //下班签退时间
                        Date pmEndTime = userWorkRecord.getPmEndTime();
                        DecimalFormat dft = new DecimalFormat("0.00");//格式化小数
                        float hour = 0;//工时
                        float overHour = 0;//加班工时
                        float outHour = 0;//外出工时
                        if(null != amStartTime && null != pmEndTime){
                            //打卡时间小于规定时间返回false，否则返回true
                            boolean startTimeFlag = FormatDateUtil.getDateCompany(amStartTime, amUp);
                            logger.info("UserWorkHourSchedule startTimeFlag:"+startTimeFlag);

                            //上班打卡时间和中午开始午休时间对比;打卡时间小于规定时间返回false，否则返回true
                            boolean noonTimeAmFlag = FormatDateUtil.getDateCompany(amStartTime, "12:00:00");
                            boolean noonTimePmFlag = FormatDateUtil.getDateCompany(pmEndTime, "13:00:00");
                            logger.info("UserWorkHourSchedule noonTimeAmFlag:"+noonTimeAmFlag);
                            logger.info("UserWorkHourSchedule noonTimePmFlag:"+noonTimePmFlag);

                            boolean endTimeFlag = FormatDateUtil.getDateCompany(pmEndTime, pmDown);

                            //上班和下班时间格式转换
                            Date upTime = FormatDateUtil.getStringToDate(amUp);
                            Date downTime = FormatDateUtil.getStringToDate(pmDown);

                            //正常情况
                            if(startTimeFlag == false && endTimeFlag == true){
                                hour = FormatDateUtil.getWorkHour(upTime,downTime);
                            }

                            //迟到情况
                            if(startTimeFlag == true && endTimeFlag == true){
                                hour = FormatDateUtil.getWorkHour(amStartTime,downTime);
                            }

                            //早退情况
                            if(startTimeFlag == false && endTimeFlag == false){
                                hour = FormatDateUtil.getWorkHour(upTime,pmEndTime);
                            }

                            //迟到早退情况
                            if(startTimeFlag == true && endTimeFlag == false){
                                hour = FormatDateUtil.getWorkHour(amStartTime,pmEndTime);
                            }

                            //下午打的上班卡就不减午休时间
                            if(noonTimeAmFlag == false && noonTimePmFlag == true){
                                hour = hour - (float)noontimeRest;//减去午休的时间
                            }

                            hour = Float.parseFloat(dft.format(hour));
                            logger.info("UserWorkHourSchedule 2/day hour:"+hour);

                            //计算加班时间

                            Date overTime = FormatDateUtil.getStringToDate(overStartTime);//加班签到时间
                            Date overStartTime2 = userWorkRecord.getOverStartTime();
                            Date overEndTime = userWorkRecord.getOverEndTime();

                            if(overStartTime2 != null && overEndTime != null){
                                boolean overTimeFlag2 = FormatDateUtil.getDateCompany(overStartTime2, overStartTime);
                                if(overTimeFlag2){
                                    overHour = FormatDateUtil.getWorkHour(overStartTime2,overEndTime);
                                }else{
                                    overHour = FormatDateUtil.getWorkHour(overTime,overEndTime);
                                }
                            }else{
                                //没有加班签到和签退时间，判断下班时间和加班开始时间
                                boolean overTimeFlag = FormatDateUtil.getDateCompany(pmEndTime, overStartTime);
                                if(overTimeFlag){
                                    overHour = FormatDateUtil.getWorkHour(overTime,pmEndTime);
                                }
                            }
                            overHour = Float.parseFloat(dft.format(overHour));
                            logger.info("UserWorkHourSchedule 2/day overHour:"+overHour);
                        }
                        //计算外出时间
                        //获取外出打卡时间记录

                        List<Map<String,Object>> outRecordList = userWorkHourDao.getOutTimeList(architecturalid,userWorkRecord.getTime(),userWorkRecord.getId());
                        int length = outRecordList.size();
                        if(length>=2){
                            Date beginTime = null;
                            Date endTime = null;
                            for(Map<String,Object> outMap : outRecordList){
                                Date recog_time = FormatDateUtil.getDateTime((String)outMap.get("recog_time"));
                                int auth_stat = (int)outMap.get("auth_stat");
                                if(auth_stat == 4){
                                    beginTime = recog_time;
                                }
                                if(auth_stat == 5){
                                    endTime = recog_time;
                                }
                                if(null != beginTime && null != endTime){
                                    outHour = outHour + FormatDateUtil.getWorkHour(beginTime,endTime);
                                    beginTime = null;
                                    endTime = null;
                                }
                            }
                        }

                        outHour = Float.parseFloat(dft.format(outHour));
                        logger.info("UserWorkHourSchedule 2/day outHour:"+outHour);

                        userWorkRecord.setWorkDate(hour);
                        userWorkRecord.setOverDate(overHour);
                        userWorkRecord.setOutDate(outHour);
                        //记录算出的员工工时
                        userWorkHourDao.insertUserWorkDate(userWorkRecord);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Inject
    private DeviceTaskDao deviceTaskDao;
    @Inject
    private UserWorkHourDao userWorkHourDao;
}
