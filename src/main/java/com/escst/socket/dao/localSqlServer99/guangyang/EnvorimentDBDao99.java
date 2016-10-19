package com.escst.socket.dao.localSqlServer99.guangyang;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.mybatis.spring.annotation.MapperScan;

import java.sql.SQLException;
import java.util.Map;

/**
 * 光洋环境数据接入
 * @author zcf
 *
 */
@MapperScan
public interface EnvorimentDBDao99 {

	/**
	 * 环境历史数据写入
	 * @param list 数据集合
	 * @param IP 客户端IP
	 * @param architecturalid 工地ID
	 * @param architecturalname 工地名称
     * @param sensorNo  设备编号
     */
	@Insert("INSERT INTO bk_EnvironmentSensorInfo(ArchitecturalID,ArchitecturalName,Humidity,Tempture,Noise,PM25,CreateDate,DATESTR,IP,PM10,COMBUSTIBLEGAS," +
			"SENSORID,TOXICGAS) " +
			" VALUES (#{architecturalid},#{architecturalname},#{list[17]},#{list[16]},#{list[18]},#{list[14]}," +
			" getdate(),#{dateStr},#{IP},#{list[15]},#{list[20]},#{sensorNo},#{list[19]})")
	void insertHistoryStore(@Param("list") String[] list,
							@Param("IP") String IP,
							@Param("architecturalid") String architecturalid,
							@Param("architecturalname") String architecturalname,
							@Param("sensorNo") String sensorNo,
							@Param("dateStr") String dateStr);

	/**
	 * 环境实时数据写入
	 * @param list 数据集合
	 * @param IP 客户端IP
	 * @param architecturalid 工地ID
	 * @param architecturalname 工地名称
	 * @param sensorNo  设备编号
     */
	@Insert("INSERT INTO bk_EnvironmentSensorRealtime(ArchitecturalID,ArchitecturalName,Humidity,Tempture,Noise,PM25,CreateDate,IP,PM10," +
			"COMBUSTIBLEGAS,SENSORID,TOXICGAS)" +
			" VALUES (#{architecturalid},#{architecturalname},#{list[17]},#{list[16]},#{list[18]}," +
			"#{list[14]},getdate(),#{IP},#{list[15]},#{list[20]},#{sensorNo},#{list[19]})")
	void insertRealStore(@Param("list") String[] list,
						 @Param("IP") String IP,
						 @Param("architecturalid") String architecturalid,
						 @Param("architecturalname") String architecturalname,
						 @Param("sensorNo") String sensorNo);

	/**
	 *  环境报警数据写入
	 * @param list 数据集合
	 * @param architecturalid 工地ID
	 * @param architecturalname 工地名称
	 * @param sensorNo  设备编号
     * @param alarmValue 报警值
     */
	@Insert("INSERT INTO bk_EnvironmentSensorAlarm(ArchitecturalID,ArchitecturalName,alarmvalue,name,alarmtype,status,createdate,SENSORID)" +
			" VALUES (#{architecturalid},#{architecturalname},#{alarmValue},#{list[0]},#{list[1]},0,getdate(),#{sensorNo})")
	void insertSensorStore(@Param("list") String[] list,
						   @Param("architecturalid") String architecturalid,
						   @Param("architecturalname") String architecturalname,
						   @Param("sensorNo") String sensorNo,
						   @Param("alarmValue") float alarmValue);

	/**
	 * 更新传感器实时数据表
	 * @param list 数据集合
	 * @param architecturalid 工地ID
	 * @param architecturalname 工地名称
	 * @param sensorNo  设备编号
     * @param IP 客户端IP
     */
	@Update("UPDATE bk_EnvironmentSensorRealtime SET ArchitecturalID = #{architecturalid},ArchitecturalName = #{architecturalname}," +
			"Humidity = #{list[17]},Tempture = #{list[16]},Noise = #{list[18]},PM25 = #{list[14]}," +
			"CreateDate = getdate(),IP = #{IP},PM10 = #{list[15]},COMBUSTIBLEGAS = #{list[20]},TOXICGAS = #{list[19]} WHERE SENSORID = #{sensorNo}")
	void updateStore(@Param("list") String[] list,
					 @Param("architecturalid") String architecturalid,
					 @Param("architecturalname") String architecturalname,
					 @Param("sensorNo") String sensorNo,
					 @Param("IP") String IP);
	/**
	 * 根据传感器编号查询传感器所属的工地ID和工地名称
	 * @param no
	 * @return
	 * @throws SQLException
	 */
	@Select("SELECT ARCHITECTURALID,ARCHITECTURALNAME FROM bk_EnvironmentSensorList WHERE SENSORID = #{no}")
	Map<String, String> findSensorNo(@Param("no") String no);

	/**
	 * 查询环境实时数据是否存在
	 * @param sensorNo
	 * @return
	 * @throws SQLException
	 */
	@Select("SELECT count(*) AS total FROM bk_EnvironmentSensorRealtime where SENSORID = #{sensorNo}")
	int findSensorRealByNo(@Param("sensorNo") String sensorNo);

}
