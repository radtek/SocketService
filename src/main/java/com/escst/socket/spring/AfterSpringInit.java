package com.escst.socket.spring;

import com.escst.socket.dao.localSqlServer.guangyang.EnvorimentDBDao;
import com.escst.socket.dao.localSqlServer.guangyang.LiftDao;
import com.escst.socket.dao.localSqlServer.guangyang.PropellingMovementDBDao;
import com.escst.socket.dao.localSqlServer.guangyang.TowerDBDao;
import com.escst.socket.dao.localSqlServer99.guangyang.EnvorimentDBDao99;
import com.escst.socket.dao.localSqlServer99.guangyang.TowerDBDao99;
import com.escst.socket.dao.localSqlServer.hongmo.DeviceTaskDao;
import com.escst.socket.dao.localSqlServer.hongmo.RegisterUserInfoDao;
import com.escst.socket.dao.localSqlServer.hongmo.UserInfoRecordsDao;
import com.escst.socket.dao.localSqlServer99.hongmo.DeviceTaskDao99;
import com.escst.socket.dao.localSqlServer99.hongmo.RegisterUserInfoDao99;
import com.escst.socket.dao.localSqlServer99.hongmo.UserInfoRecordsDao99;
import com.escst.socket.service.guangyang.DeviceGYService;
import com.escst.socket.service.hongmo.DeviceService;
import org.springframework.beans.factory.InitializingBean;

import javax.inject.Inject;

/**
 * socket服务启动类
 * Created by zcf on 2016/6/21.
 */
public class AfterSpringInit implements InitializingBean{

    @Override
    public void afterPropertiesSet() throws Exception {
        //虹膜socket
        Thread deviceService = new DeviceService(8856,deviceTaskDao,userInfoRecordsDao,registerUserInfoDao,
                deviceTaskDao99,userInfoRecordsDao99,registerUserInfoDao99);
        deviceService.start();

        //光洋socket服务启动
        Thread gyService = new DeviceGYService(6680,envorimentDBDao,towerDBDao,liftDao,propellingMovementDBDao,envorimentDBDao99,towerDBDao99);
        gyService.start();
    }

    @Inject
    private DeviceTaskDao deviceTaskDao;

    @Inject
    private UserInfoRecordsDao userInfoRecordsDao;

    @Inject
    private RegisterUserInfoDao registerUserInfoDao;

    @Inject
    private EnvorimentDBDao envorimentDBDao;

    @Inject
    private TowerDBDao towerDBDao;

    @Inject
    private LiftDao liftDao;

	@Inject
	private PropellingMovementDBDao propellingMovementDBDao;

    // 连接99数据库
    @Inject
    private EnvorimentDBDao99 envorimentDBDao99;
    @Inject
    private TowerDBDao99 towerDBDao99;
    @Inject
    private DeviceTaskDao99 deviceTaskDao99;
    @Inject
    private UserInfoRecordsDao99 userInfoRecordsDao99;
    @Inject
    private RegisterUserInfoDao99 registerUserInfoDao99;

}
