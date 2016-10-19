package com.escst.socket.dao.localSqlServer.guangyang;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.mybatis.spring.annotation.MapperScan;

import java.util.Map;

/**
 * 光洋塔吊和环境推送数据接入
 * @author caozx
 *
 */
@MapperScan
public interface PropellingMovementDBDao {
    /**
     *
     * @throws java.sql.SQLException
     * @throws Exception
     */
    @Insert("INSERT INTO propellingMovement(content,createtime,type,style,architecturalid,name)" +
            "VALUES (#{content},getdate(),#{type},#{style},#{architecturalid},#{name})")
    void insert(@Param("content") String content, @Param("type") int type,
                @Param("style") int style,@Param("architecturalid") String architecturalid,
                @Param("name") String name);

}

