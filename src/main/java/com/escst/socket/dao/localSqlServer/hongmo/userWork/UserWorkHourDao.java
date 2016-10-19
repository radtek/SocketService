package com.escst.socket.dao.localSqlServer.hongmo.userWork;

import com.escst.socket.entity.hongmo.userWork.UserWorkRecordEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.mybatis.spring.annotation.MapperScan;

import java.util.List;
import java.util.Map;

/**
 * Created by zcf on 2016/6/21.
 */
@MapperScan
public interface UserWorkHourDao {

    @Select("SELECT u.user_id AS id,p.name,t.typename AS duty,CONVERT(varchar(10), u.recog_time, 112) AS time," +
            " (SELECT MIN(recog_time) FROM bk_UserInfoRecords WHERE auth_stat = 0 " +
            " AND CONVERT(VARCHAR(10), recog_time, 112) = CONVERT(varchar(10), u.recog_time, 112) AND user_id = p.numbr) AS amStartTime," +
            " (SELECT MAX(recog_time) FROM bk_UserInfoRecords WHERE auth_stat = 1 " +
            " AND CONVERT(VARCHAR(10), recog_time, 112) = CONVERT(varchar(10), u.recog_time, 112) AND user_id = p.numbr) AS pmEndTime," +
            " (SELECT MIN(recog_time) FROM bk_UserInfoRecords WHERE auth_stat = 2 " +
            " AND CONVERT(VARCHAR(10), recog_time, 112) = CONVERT(varchar(10), u.recog_time, 112) AND user_id = p.numbr) AS overStartTime," +
            " (SELECT MAX(recog_time) FROM bk_UserInfoRecords WHERE auth_stat = 3 " +
            " AND CONVERT(VARCHAR(10), recog_time, 112) = CONVERT(varchar(10), u.recog_time, 112) AND user_id = p.numbr) AS overEndTime," +
            " p.projectid,p.projectname" +
            " FROM bk_UserInfoRecords u JOIN bk_AttendanceEquipment ae ON ae.sn = u.sn" +
            " JOIN tbl_PersonInfoList p ON p.numbr = u.user_id" +
            " JOIN t_s_type t ON t.typecode = p.ZHICHENG" +
            " WHERE ae.architecturalid = #{architecturalid}" +
            " AND CONVERT(VARCHAR(10), u.recog_time, 112) = CONVERT(varchar(100), dateadd(day, -1, getdate()), 23)" +
            " GROUP BY CONVERT(varchar(10), u.recog_time, 112),p.name,t.typename,p.projectid,p.projectname,u.user_id,p.numbr")
    List<UserWorkRecordEntity> getUserWorkRecord(@Param("architecturalid") String architecturalid);


    @Select("select * from bk_UserInfoRecords p JOIN bk_AttendanceEquipment ae ON ae.sn = p.sn where p.user_id=#{user_id} and ae.architecturalid = #{architecturalid}" +
            " AND CONVERT(VARCHAR(10), recog_time, 112) = CONVERT(varchar(10),#{time}, 112)" +
            " AND p.auth_stat in (4,5) ORDER BY p.recog_time ASC")
    List<Map<String,Object>> getOutTimeList(@Param("architecturalid") String architecturalid,
                                            @Param("time") String time,
                                            @Param("user_id") String user_id);

    @Insert("INSERT INTO bk_UserWorkHours (user_id,name,duty,amStartTime,amEndTime,time,workdate,architecturalid," +
            "architecturalname,overStartTime,overEndTime,overDate,outDate,pmStartTime,pmEndTime) VALUES (#{id},#{name}," +
            "#{duty},#{amStartTime},#{amEndTime},#{time},ROUND(#{workDate},2),#{projectid},#{projectname},#{overStartTime}," +
            "#{overEndTime},ROUND(#{overDate},2),ROUND(#{outDate},2),#{pmStartTime},#{pmEndTime})")
    void insertUserWorkDate(UserWorkRecordEntity userWorkRecordEntity);


    @Select("SELECT u.user_id AS id,p.NAME,t.typename AS duty," +
            " CONVERT(varchar(10), u.recog_time, 112) AS time," +
            " (SELECT MIN(recog_time) FROM bk_UserInfoRecords WHERE auth_stat = 0 AND" +
            " CONVERT(VARCHAR(10), recog_time, 112) = CONVERT(VARCHAR(10), u.recog_time, 112)) AS amStartTime," +
            " (SELECT MAX(recog_time) FROM bk_UserInfoRecords WHERE auth_stat = 1" +
            " AND DATEPART(hh,recog_time) >= 11 AND DATEPART(hh,recog_time) <= DATEPART(hh,#{pmStartTime})" +
            " AND CONVERT(VARCHAR(10), recog_time, 112) = CONVERT(VARCHAR(10), u.recog_time, 112)) AS amEndTime," +
            " (SELECT MAX(recog_time) FROM bk_UserInfoRecords WHERE auth_stat = 0" +
            " AND DATEPART(hh,recog_time) >= 12 AND DATEPART(hh,recog_time) <= 17" +
            " AND CONVERT(VARCHAR(10), recog_time, 112) = CONVERT(VARCHAR(10), u.recog_time, 112)) AS pmStartTime," +
            " (SELECT MAX(recog_time) FROM bk_UserInfoRecords WHERE auth_stat = 1 AND" +
            " CONVERT(VARCHAR(10), recog_time, 112) = CONVERT(VARCHAR(10), u.recog_time, 112)) AS pmEndTime," +
            " (SELECT MIN(recog_time) FROM bk_UserInfoRecords WHERE auth_stat = 2 " +
            " AND CONVERT(VARCHAR(10), recog_time, 112) = CONVERT(varchar(10), u.recog_time, 112)) AS overStartTime," +
            " (SELECT MAX(recog_time) FROM bk_UserInfoRecords WHERE auth_stat = 3 " +
            " AND CONVERT(VARCHAR(10), recog_time, 112) = CONVERT(varchar(10), u.recog_time, 112)) AS overEndTime," +
            " p.projectid,p.projectname" +
            " FROM bk_UserInfoRecords u " +
            " JOIN bk_AttendanceEquipment ae ON ae.sn = u.sn" +
            " JOIN tbl_PersonInfoList p ON p.numbr = u.user_id" +
            " JOIN t_s_type t ON t.typecode = p.ZHICHENG" +
            " WHERE ae.architecturalid = #{architecturalid}" +
            " AND CONVERT(VARCHAR(10), u.recog_time, 112) = CONVERT(varchar(100), dateadd(day, -1, getdate()), 23)" +
            " GROUP BY CONVERT(varchar(10), u.recog_time, 112),p.NAME,t.typename,p.PROJECTID,p.PROJECTNAME,u.user_id")
    List<UserWorkRecordEntity> getUserFourWorkRecord(@Param("architecturalid") String architecturalid,
                                                     @Param("pmStartTime") String pmStartTime);


    @Select("select count(a.recog_time)" +
            " from bk_UserInfoRecords a" +
            " inner join tbl_PersonInfoList b on a.user_id = b.numbr" +
            " where b.projectid = #{architecturalid} and a.auth_stat = #{authstat}" +
            " and b.zhicheng = #{zhicheng} and a.recog_time like '${recogtime}%'")
    int getUserRecogcount(@Param("architecturalid") String architecturalid,
                         @Param("authstat") int authstat,
                         @Param("recogtime") String recogtime,
                         @Param("zhicheng") String zhicheng);

    @Select("select b.name,b.numbr" +
            " from bk_UserInfoRecords a" +
            " inner join tbl_PersonInfoList b on a.user_id = b.numbr" +
            " where b.projectid = #{architecturalid}" +
            " and b.zhicheng = #{zhicheng}")
    List<Map<String,Object>> getUserInfoName(@Param("architecturalid") String architecturalid,
                                             @Param("zhicheng") String zhicheng);
}
