package com.escst.socket.schedule;

import com.escst.socket.dao.localSqlServer.guangyang.PropellingMovementDBDao;
import com.escst.socket.dao.localSqlServer.hongmo.DeviceTaskDao;
import com.escst.socket.dao.localSqlServer.hongmo.userWork.UserWorkHourDao;
import com.escst.socket.utils.FormatDateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.inject.Inject;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Timer;

/**
 * 用户未打卡提醒推送定时任务
 * @author caozx
 */
public class UserRecogPushSchedule {

    private static final Logger logger = LoggerFactory.getLogger(UserRecogPushSchedule.class);
    private static final int AUTH_STAT_SB = 0; //上班
    private static final int AUTH_STAT_XB = 1; //下班

    @Inject
    private DeviceTaskDao deviceTaskDao;
    @Inject
    private UserWorkHourDao userWorkHourDao;
    @Inject
    private PropellingMovementDBDao propellingMovementDBDao;

    public void userRecogTask(){
        String todayStr = FormatDateUtil.getFormatDate("yyyy-MM-dd");
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
                //提醒时间
                double warnTime = (double)deviceMap.get("warnTime");
                //工地ID
                String architecturalid = (String)deviceMap.get("architecturalid");
                //工地名称
                String architecturalname = (String)deviceMap.get("architecturalname");

                Date amStartTime = FormatDateUtil.getDateTime(todayStr + " " + amUp); //上午上班时间
                Date pmEndTime = FormatDateUtil.getDateTime(todayStr + " " + pmDown); //下午下班时间
                int warnMinit = (int)(warnTime * 60);  //提醒时间

                amStartTime = FormatDateUtil.addMinute(amStartTime,warnMinit);
                pmEndTime = FormatDateUtil.addMinute(pmEndTime,warnMinit);

                System.out.println("工地" + architecturalname + ",上班打卡提醒时间为：" + FormatDateUtil.getDateToString(amStartTime));
                System.out.println("工地" + architecturalname + ",下班打卡提醒时间为：" + FormatDateUtil.getDateToString(pmEndTime));
                //上午打卡提醒任务
                UserRecogPushTask amtask = new UserRecogPushTask(userWorkHourDao,propellingMovementDBDao,architecturalid,AUTH_STAT_SB,todayStr);
                Timer amtimer = new Timer();
                amtimer.schedule(amtask,amStartTime);

                //下午打卡提醒任务
                UserRecogPushTask pmtask = new UserRecogPushTask(userWorkHourDao,propellingMovementDBDao,architecturalid,AUTH_STAT_XB,todayStr);
                Timer pmtimer = new Timer();
                pmtimer.schedule(pmtask,pmEndTime);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}