package com.escst.socket.schedule;

import com.escst.socket.dao.localSqlServer.guangyang.PropellingMovementDBDao;
import com.escst.socket.dao.localSqlServer.hongmo.userWork.UserWorkHourDao;
import com.escst.socket.utils.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

/**
 * 人员打卡推送提醒定时任务
 * Created by caozx on 2016/8/10.
 */
public class UserRecogPushTask extends TimerTask {

    private static final Logger logger = LoggerFactory.getLogger(UserRecogPushSchedule.class);
    /**职称为项目经理**/
    private static final String ZHI_CHENG_XMJL = "zhicheng04";

    private String architecturalid;//项目id
    private int authstat;         // 0上班 1下班 2加班签到 3加班签退 4外出签到 5外出签退
    private String recogtime;    //当前日期，格式：yyyy-MM-dd
    private UserWorkHourDao userWorkHourDao;
    private PropellingMovementDBDao propellingMovementDBDao;

    public UserRecogPushTask(UserWorkHourDao userWorkHourDao,PropellingMovementDBDao propellingMovementDBDao,String architecturalid,int authstat,String recogtime){
        super();
        this.userWorkHourDao = userWorkHourDao;
        this.propellingMovementDBDao = propellingMovementDBDao;
        this.architecturalid = architecturalid;
        this.authstat = authstat;
        this.recogtime = recogtime;
    }

    @Override
    public void run() {
        String dktype = "";
        if(authstat == 0){
            dktype = "上午";
        }else if(authstat == 1){
            dktype = "下午";
        }
        logger.info("用户" + dktype + "打卡推送开始......");
        try {
            int count = userWorkHourDao.getUserRecogcount(architecturalid,authstat,recogtime,ZHI_CHENG_XMJL);
            String name = "";
            List<Map<String,Object>> userinfoList = userWorkHourDao.getUserInfoName(architecturalid,ZHI_CHENG_XMJL);
            if(ArrayUtils.hasObject(userinfoList)){
                Map<String,Object> map = userinfoList.get(0);
                name = (String)map.get("name");
                if(StringUtils.isEmpty(name)){
                    logger.info("工地id:" + architecturalid + "，无项目经理！");
                    return;
                }
                //没有打卡记录就在推送表存一条记录
                if(count == 0){
                    logger.info("时间："+recogtime +  dktype + "，" + name + "无打卡记录！");
                    String content = "您还没有打卡！";
                    int type = 0;
                    int style = 6;//2环境报警 3设备报警 4代办 5待阅 6到场提醒
                    propellingMovementDBDao.insert(content,type,style,architecturalid,name);
                }else{
                    logger.info("时间："+recogtime +  dktype + "，" + name + "有打卡记录！");
                }
            }else{
                logger.info("工地id:" + architecturalid + "，无项目经理！");
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
