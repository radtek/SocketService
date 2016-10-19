package com.escst.socket.dao.localSqlServer.guangyang;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.mybatis.spring.annotation.MapperScan;



import java.util.Map;

/**
 * Created by Administrator on 2016/7/8.
 */
@MapperScan
public interface LiftDao {

    /**
     * 根据升降机ID查询项目ID 和项目名称
     * @param liftNo
     * @return
     */
    @Select("SELECT *  FROM bk_LiftList WHERE  LIFTNO=#{liftNo}")
    Map<String,Object> findId(@Param("liftNo") String liftNo);

    /**
     * 升降机历史记录插入
     * @param list
     * @param architecturalid
     * @param architecturalname
     */
    @Insert("INSERT  INTO bk_LiftInfo (SWITCH_STATE,DRIVERID,LOAD,RELFLOOR,CALL_FLOOR,PM,PM10,ELEVATOR_SPEED,CHECKSTATE,RELAY_STATE,ALARM,EARLY_ALARM,architecturalid,architecturalname,liftNo,people_Num,wind, elevator_Height,angle,CREATETIME)  VALUES(#{list[3]},#{list[5]},#{list[6]},#{list[8]},#{list[9]},#{list[14]},#{list[15]},#{list[18]},#{list[19]},#{list[20]},#{alarm[1]},#{alarm[0]},#{architecturalid},#{architecturalname},#{liftNo},#{list[13]},#{list[11]},#{list[10]},#{list[12]},getdate())")
    void insertHistoryStore(@Param("list") String[] list, @Param("liftNo") String liftNo, @Param("architecturalid") String architecturalid, @Param("architecturalname") String architecturalname, @Param("alarm") String[] alarm);



    /**
     * 升降机报警数据插入
     * @param list
     * @param architecturalid
     * @param architecturalname
     * @param alarm
     */
    @Insert("INSERT  INTO bk_LiftAlarm (SWITCH_STATE,DRIVERID,LOAD,RELFLOOR,CALL_FLOOR,PM,PM10,ELEVATOR_SPEED,CHECKSTATE,RELAY_STATE,ALARM,EARLY_ALARM,architecturalid,architecturalname,liftNo,people_Num,wind, elevator_Height,angle,CREATETIME)  VALUES(#{list[3]},#{list[5]},#{list[6]},#{list[8]},#{list[9]},#{list[14]},#{list[15]},#{list[18]},#{list[19]},#{list[20]},#{alarm[1]},#{alarm[0]},#{architecturalid},#{architecturalname},#{liftNo},#{list[13]},#{list[11]},#{list[10]},#{list[12]},getdate())")
    void insertAlarmStore(@Param("list") String[] list, @Param("liftNo") String liftNo, @Param("architecturalid") String architecturalid, @Param("architecturalname") String architecturalname, @Param("alarm") String[] alarm);


    /**
     * 查询该升降机设备
     * @param liftNo
     * @return
     */
    @Select("SELECT COUNT(*)  AS total FROM  bk_LiftRealtime   WHERE LIFTNO=#{liftNo}")
    int findLiftRealByNo(@Param("liftNo") String liftNo);

    /**
     * 更新升降机实时数据记录
     * @param list
     * @param architecturalid
     * @param architecturalname
     * @param liftNo
     * @param alarm
     */
    @Update("UPDATE  bk_LiftRealtime  SET SWITCH_STATE=#{list[3]},DRIVERID=#{list[5]},LOAD=#{list[6]},RELFLOOR=#{list[8]},CALL_FLOOR=#{list[9]},PM=#{list[14]},PM10=#{list[15]},ELEVATOR_SPEED=#{list[18]}, CHECKSTATE=#{list[19]},RELAY_STATE=#{list[20]},ALARM=#{alarm[1]},EARLY_ALARM=#{alarm[0]},architecturalid=#{architecturalid},architecturalname=#{architecturalname},people_Num=#{list[13]},wind=#{list[11]},elevator_Height=#{list[10]},angle=#{list[12]},CREATETIME=getdate()    WHERE  LIFTNO=#{liftNo}")
    void upLiftDateStore(@Param("list") String[] list, @Param("architecturalid") String architecturalid, @Param("architecturalname") String architecturalname, @Param("liftNo") String liftNo, @Param("alarm") String[] alarm);

    /**
     * 插入升降机实时记录
      * @param list
     * @param liftNo
     * @param architecturalid
     * @param architecturalname
     * @param alarm
     */
    @Insert("INSERT  INTO bk_LiftRealtime (SWITCH_STATE,DRIVERID,LOAD,RELFLOOR,CALL_FLOOR,PM,PM10,ELEVATOR_SPEED,CHECKSTATE,RELAY_STATE,ALARM,EARLY_ALARM,architecturalid,architecturalname,liftNo,people_Num,wind, elevator_Height,angle,CREATETIME)  VALUES(#{list[3]},#{list[5]},#{list[6]},#{list[8]},#{list[9]},#{list[14]},#{list[15]},#{list[18]},#{list[19]},#{list[20]},#{alarm[1]},#{alarm[0]},#{architecturalid},#{architecturalname},#{liftNo},#{list[13]},#{list[11]},#{list[10]},#{list[12]},getdate())")
   void  insertRealtimeStore(@Param("list") String[] list, @Param("liftNo") String liftNo, @Param("architecturalid") String architecturalid, @Param("architecturalname") String architecturalname, @Param("alarm") String[] alarm);
}
