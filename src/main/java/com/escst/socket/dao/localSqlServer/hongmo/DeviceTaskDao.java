package com.escst.socket.dao.localSqlServer.hongmo;

import com.escst.socket.entity.hongmo.Device;
import com.escst.socket.entity.hongmo.DeviceTask;
import com.escst.socket.entity.hongmo.UserTemplates;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.ognl.ObjectArrayPool;
import org.mybatis.spring.annotation.MapperScan;

import java.util.List;
import java.util.Map;

@MapperScan
public interface DeviceTaskDao {

	/**
	 * 根据sn序列号查询设备任务
	 * @param sn
	 * @return
	 */
	@Select("SELECT * FROM bk_deviceTask t WHERE id = (SELECT top 1 id FROM " +
			" bk_deviceTask WHERE sn= #{sn} AND taskState = 0 ORDER BY id ASC)")
	DeviceTask findTaskBySn(@Param("sn") String sn);

	/**
	 * 设备任务信息更新
	 * @param deviceTask
	 */
	@Update("UPDATE bk_deviceTask SET result=#{result},resultDetail=#{resultDetail},returnTime=#{returnTime},taskState=#{taskState},exeTimes=#{exeTimes} WHERE id=#{id}")
	void updateTask(DeviceTask deviceTask);

	/**
	 * 根据sn序列号查询设备信息
	 * @param sn
	 * @return
	 */
	@Select("SELECT * FROM bk_AttendanceEquipment t WHERE t.sn = #{sn}")
	Device findDeviceBySn(@Param("sn") String sn);


	/**
	 * 设备信息更新
	 * @param device
	 */
	@Update("UPDATE bk_AttendanceEquipment SET state=#{state},lastConnTime=#{lastConnTime} WHERE id=#{id}")
	void updateDevice(Device device);

	/**
	 * 查询用户模板
	 * @param numbr 用户工号
	 * @param projectId 项目ID
	 */
	@Select("SELECT * FROM bk_userTemplates WHERE numbr = #{numbr} AND architecturalid = #{projectId} ORDER BY type ASC")
	List<UserTemplates> findTemplatesBySn(@Param("numbr") String numbr, @Param("projectId") String projectId);

	/**
	 * 查询所有的考勤设备以及设备所在工地的上下班时间
	 * @return
     */
	@Select("SELECT * FROM  bk_people_time p")
	List<Map<Object,Object>> findAllDevice();

}
