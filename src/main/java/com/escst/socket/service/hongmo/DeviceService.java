package com.escst.socket.service.hongmo;

import com.escst.socket.dao.localSqlServer.hongmo.DeviceTaskDao;
import com.escst.socket.dao.localSqlServer.hongmo.RegisterUserInfoDao;
import com.escst.socket.dao.localSqlServer.hongmo.UserInfoRecordsDao;
import com.escst.socket.dao.localSqlServer99.hongmo.DeviceTaskDao99;
import com.escst.socket.dao.localSqlServer99.hongmo.RegisterUserInfoDao99;
import com.escst.socket.dao.localSqlServer99.hongmo.UserInfoRecordsDao99;
import com.escst.socket.service.hongmo.DealConnectService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class DeviceService extends Thread{

    private Integer port;
    private ServerSocket serverSocket;
    private DeviceTaskDao deviceTaskDao;
    private UserInfoRecordsDao userInfoRecordsDao;
    private RegisterUserInfoDao registerUserInfoDao;
    private DeviceTaskDao99 deviceTaskDao99;
    private UserInfoRecordsDao99 userInfoRecordsDao99;
    private RegisterUserInfoDao99 registerUserInfoDao99;

    public DeviceService(Integer port,DeviceTaskDao deviceTaskDao,UserInfoRecordsDao userInfoRecordsDao, RegisterUserInfoDao registerUserInfoDao,
                         DeviceTaskDao99 deviceTaskDao99,UserInfoRecordsDao99 userInfoRecordsDao99, RegisterUserInfoDao99 registerUserInfoDao99)
    {
        this.port = port;
        this.deviceTaskDao = deviceTaskDao;
        this.userInfoRecordsDao = userInfoRecordsDao;
        this.registerUserInfoDao = registerUserInfoDao;
        this.deviceTaskDao99 = deviceTaskDao99;
        this.userInfoRecordsDao99 = userInfoRecordsDao99;
        this.registerUserInfoDao99 = registerUserInfoDao99;
    }

    /**
     * 启动监听
     * @throws IOException
     */
    public void listen() throws IOException
    {
        serverSocket = new ServerSocket(port);
        while (!serverSocket.isClosed())
        {
            System.out.println(new Date() + "socket服务端启动，端口号为："+port);
            Socket socket = serverSocket.accept();
            //设置连接超时
             socket.setSoTimeout(20000);
            try
            {
                String ip = socket.getInetAddress().getHostAddress();
//            	System.out.println("客户端接入ip:"+ip+":"+port);
                //思源虹膜门禁数据接入
                System.out.println(new Date() + "端口号8856门禁数据接入......");
                Thread dealConnect = new DealConnectService(socket,deviceTaskDao,userInfoRecordsDao,registerUserInfoDao,
                        deviceTaskDao99,userInfoRecordsDao99,registerUserInfoDao99);
                System.out.println(new Date() + "-->>门禁数据开始处理连接:IP="+ ip + ",threadId="+dealConnect.getId());
                dealConnect.start();

            }catch (Exception e)
            {
                System.out.println(new Date() + "DeviceService.listen(),deal connection error:"+e);
            }
        }
    }

    @Override
    public void run()
    {
        try
        {
            listen();
        } catch (IOException e)
        {
            System.out.println(new Date() + "启动socket服务错误:"+e);
        }
    }
}
