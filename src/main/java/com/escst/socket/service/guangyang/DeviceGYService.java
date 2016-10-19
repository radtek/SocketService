package com.escst.socket.service.guangyang;

import com.escst.socket.dao.localSqlServer.guangyang.EnvorimentDBDao;
import com.escst.socket.dao.localSqlServer.guangyang.LiftDao;
import com.escst.socket.dao.localSqlServer.guangyang.PropellingMovementDBDao;
import com.escst.socket.dao.localSqlServer.guangyang.TowerDBDao;
import com.escst.socket.dao.localSqlServer99.guangyang.EnvorimentDBDao99;
import com.escst.socket.dao.localSqlServer99.guangyang.TowerDBDao99;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

/**
 * 光洋环境启动类
 * Created by zcf on 2016/7/1.
 */
public class DeviceGYService  extends Thread{
    private Integer port;
    private ServerSocket serverSocket;
    private EnvorimentDBDao envorimentDBDao;
    private TowerDBDao towerDBDao;
    private  LiftDao liftDao;
	private PropellingMovementDBDao propellingMovementDBDao;
    private EnvorimentDBDao99 envorimentDBDao99;
    private TowerDBDao99 towerDBDao99;

    public DeviceGYService(Integer port,EnvorimentDBDao envorimentDBDao,TowerDBDao towerDBDao,LiftDao liftDao,PropellingMovementDBDao propellingMovementDBDao,
                           EnvorimentDBDao99 envorimentDBDao99,TowerDBDao99 towerDBDao99)
    {
        this.port = port;
        this.envorimentDBDao = envorimentDBDao;
        this.towerDBDao = towerDBDao;
        this.liftDao=liftDao;
		this.propellingMovementDBDao = propellingMovementDBDao;
        this.envorimentDBDao99 = envorimentDBDao99;
        this.towerDBDao99 = towerDBDao99;
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
            System.out.println(new Date() + "光洋socket服务端启动，端口号为："+port);
            Socket socket = serverSocket.accept();
            //设置连接超时
            socket.setSoTimeout(20000);
            try
            {
                String ip = socket.getInetAddress().getHostAddress();
//            	System.out.println("客户端接入ip:"+ip+":"+port);
                //思源虹膜门禁数据接入
                System.out.println(new Date() + "端口号8856光洋数据接入......");
                Thread dealConnect = new GYEnvorimentService(socket,envorimentDBDao,towerDBDao,liftDao,propellingMovementDBDao,envorimentDBDao99,towerDBDao99);
                System.out.println(new Date() + "-->>光洋数据开始处理连接:IP="+ ip + ",threadId="+dealConnect.getId());
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
