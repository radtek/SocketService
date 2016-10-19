package com.escst.socket.dao.localSqlServer.guangyang;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.mybatis.spring.annotation.MapperScan;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 光洋塔吊数据接入
 * @author zcf
 *
 */
@MapperScan
public interface TowerDBDao {
    /**
     * 塔吊报警数据写入
     *
     * @param list
     * @throws SQLException
     * @throws Exception
     */
    @Insert("INSERT INTO bk_TowerCraneAlarm(towercraneno,architecturalid,architecturalname,angle,radius,height,tdload,windspeed," +
            "obliquity,fall,earlyalarm,alarm,sensoralarm,createdate) " +
            "VALUES (#{towercraneno},#{architecturalid},#{architecturalname}," +
            "#{list[5]},#{list[8]},#{list[7]},#{list[6]},#{list[11]},#{list[12]},#{list[13]},#{alarm[0]},#{alarm[1]},#{alarm[2]},getdate())")
    void insertAlarmStore(@Param("list") String[] list, @Param("towercraneno") String towercraneno,
                          @Param("architecturalid") String architecturalid,
                          @Param("architecturalname") String architecturalname,
                          @Param("alarm") String[] alarm);


    /**
     * 塔吊历史数据写入
     *
     * @param list
     * @throws SQLException
     * @throws Exception
     */
    @Insert("INSERT INTO bk_TowerCraneInfo(towercraneno,architecturalid,architecturalname,angle,radius,height,tdload,windspeed," +
            " obliquity,fall,earlyalarm,alarm,sensoralarm,createdate) " +
            " VALUES (#{towercraneno},#{architecturalid},#{architecturalname}," +
            " #{list[5]},#{list[8]},#{list[7]},#{list[6]},#{list[11]},#{list[12]},#{list[13]},#{alarm[0]},#{alarm[1]},#{alarm[2]},getdate())")
    void insertStore(@Param("list") String[] list,
                     @Param("towercraneno") String towercraneno,
                     @Param("architecturalid") String architecturalid,
                     @Param("architecturalname") String architecturalname,
                     @Param("alarm") String[] alarm);


    /**
     * 塔吊实时数据写入
     *
     * @param list
     * @throws SQLException
     * @throws Exception
     */
    @Insert("INSERT INTO bk_TowerCraneRealtime(towercraneno,architecturalid,architecturalname,angle,radius,height,tdload,windspeed," +
            " obliquity,fall,earlyalarm,alarm,sensoralarm,createdate)" +
            " VALUES (#{towercraneno},#{architecturalid},#{architecturalname}," +
            " #{list[5]},#{list[8]},#{list[7]},#{list[6]},#{list[11]},#{list[12]},#{list[13]},#{alarm[0]},#{alarm[1]},#{alarm[2]},getdate())")
    void insertRealStore(@Param("list") String[] list,
                         @Param("towercraneno") String towercraneno,
                         @Param("architecturalid") String architecturalid,
                         @Param("architecturalname") String architecturalname,
                         @Param("alarm") String[] alarm);

    /**
     * 更新塔吊实时数据表
     *
     * @param list
     * @throws SQLException
     */
    @Update("UPDATE bk_TowerCraneRealtime SET architecturalid = #{architecturalid},architecturalname = #{architecturalname}," +
            "angle = #{list[5]},radius = #{list[8]},height = #{list[7]},tdload = #{list[6]}," +
            "windspeed = #{list[11]},obliquity = #{list[12]},fall = #{list[13]},earlyalarm = #{alarm[0]},alarm = #{alarm[1]},sensoralarm = #{alarm[2]}," +
            "createdate = getdate() WHERE towercraneno = #{towercraneno}")
    void updateStore(@Param("list") String[] list,
                     @Param("towercraneno") String towercraneno,
                     @Param("architecturalid") String architecturalid,
                     @Param("architecturalname") String architecturalname,
                     @Param("alarm") String[] alarm);

    /**
     * 根据塔吊编号查询塔吊所属的工地ID和工地名称
     * @param no
     * @return
     * @throws SQLException
     */
    @Select("SELECT * FROM bk_TowerCraneList WHERE towercraneno = #{no}")
     Map<String, String> findTd(@Param("no") String no);


    /**
     * 查询塔吊实时数据是否存在
     * @param towerNo 塔吊编号
     * @return
     * @throws SQLException
     */
    @Select("SELECT COUNT(*) AS total FROM bk_TowerCraneRealtime WHERE towercraneno = #{towerNo}")
    int findTowerRealByNo(@Param("towerNo") String towerNo);

}

