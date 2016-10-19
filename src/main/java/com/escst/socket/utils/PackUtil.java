package com.escst.socket.utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 设备操作中会用到的工具方法
 *
 * @author Tian
 */
public class PackUtil
{
    /**
     * 亦或运算校验数据包
     *
     * @param pack 要校验的包
     * @param len  包长
     * @return 校验码
     */
    public static byte xOR(byte[] pack, int len)
    {
        byte BBC = 0;
        for (int i = 0; i < len; i++)
        {
            BBC ^= pack[i];
        }
        return BBC;
    }

    /**
     * 亦或运算校验数据包
     *
     * @param pack 要校验的包
     * @return 校验码
     */
    public static byte xOR(byte[] pack)
    {
        byte BBC = 0;
        int len = pack.length;
        for (int i = 0; i < len; i++)
        {
            BBC ^= pack[i];
        }
        return BBC;
    }

    /**
     * 解析请求的包头
     *
     * @param header 包头字节数组
     * @return headerMap 包头数据，以下字段没能少，也不能为null,Key可以中Value中取出,Value.Head_*
     * <p>Tag:包头标记</p>
     * <p>PkgLen:包长（包头+包体+包尾）</p>
     * <p>cmd:命令字</p>
     * <p>PkgIndex:包序号</p>
     * <p>PkgCount:包总数</p>
     * <p>State:设备状态</p>
     * <p>sn:设备sn</p>
     */
    public static Map<String, Object> HeaderAnalysis(byte[] header)
    {
        Map<String, Object> map = new HashMap<String, Object>();
        //包头标记
        int tag = byte2int(new byte[]{header[0], header[1]});
        map.put(Value.Head_Tag, tag);
        //包长
        int len = byte2int(new byte[]{header[2], header[3]});
        map.put(Value.Head_PkgLen, len);
        //命令字
        map.put(Value.Head_CMD, (int) header[4]);
        //包序号
        int pkgIndex = byte2int(new byte[]{header[5], header[6]});
        map.put(Value.Head_PkgIndex, pkgIndex);
        //包总数
        int pkgCount = byte2int(new byte[]{header[7], header[8]});
        map.put(Value.Head_PkgCount, pkgCount);
        //设备状态
        map.put(Value.Head_State, (int) header[9]);

        if(header.length==26)
        {
            byte[] snbts = new byte[16];
            System.arraycopy(header,10,snbts,0,16);
            map.put(Value.Head_SN,new String(snbts));
        }

        return map;
    }

    /**
     * 封装响应的包头
     *
     * @param headerMap 包头数据，以下字段没能少，也不能为null,Key可以中Value中取出,Value.Head_*
     *                  <p>Tag:包头标记</p>
     *                  <p>PkgLen:包长（包头+包体+包尾）</p>
     *                  <p>cmd:命令字</p>
     *                  <p>PkgIndex:包序号</p>
     *                  <p>PkgCount:包总数</p>
     *                  <p>State:设备状态</p>
     * @return 包头字节数组
     */
    public static byte[] responseHeaderMake(Map<String, Object> headerMap)
    {
        byte[] rsbytes = new byte[26];
        //包头标记
        byte[] tags = int2byte(Value.ResponseHeaderTag);
        rsbytes[0] = tags[0];
        rsbytes[1] = tags[1];
        //包长
        byte[] lens = int2byte((Integer) headerMap.get(Value.Head_PkgLen));
        rsbytes[2] = lens[0];
        rsbytes[3] = lens[1];
        //命令字
        Integer cmd = (Integer) headerMap.get(Value.Head_CMD);
        rsbytes[4] = cmd.byteValue();
        //包序号
        byte[] pkgIndexes = int2byte((Integer) headerMap.get(Value.Head_PkgIndex));
        rsbytes[5] = pkgIndexes[0];
        rsbytes[6] = pkgIndexes[1];
        //包序号
        byte[] pkgCounts = int2byte((Integer) headerMap.get(Value.Head_PkgCount));
        rsbytes[7] = pkgCounts[0];
        rsbytes[8] = pkgCounts[1];
        //设备状态
        Integer state = (Integer) headerMap.get(Value.Head_State);
        rsbytes[9] = state.byteValue();
        String sn = (String) headerMap.get(Value.Head_SN);
        byte[] bts = sn.getBytes();
        System.arraycopy(bts,0,rsbytes,10,bts.length);
        return rsbytes;
    }

    /**
     * 2位字节数组转成int
     *
     * @param res 二位字节数组，高位在前
     * @return int
     */
    public static int byte2int(byte[] res)
    {
        int targets = ((res[0] << 8) & 0xff00) | (res[1] & 0xff);
        return targets;
    }

    /**
     * int转成一个二位字节数组
     *
     * @param res int
     * @return 二位字节数组，高位在前
     */
    public static byte[] int2byte(int res)
    {
        byte[] targets = new byte[2];

        targets[0] = (byte) ((res >> 8) & 0xff);// 高位
        targets[1] = (byte) (res & 0xff);// 低位
        return targets;
    }

    /**
     * 封装包数据
     *
     * @param cmd    命令字
     * @param body   发送的数据
     * @param state  返回结果或者下一个命令的命令字
     * @param flowerCount 后续包总数
     * @param isFirst 是否为第一个包
     * @return 解析出来的包，做过分包处理的
     * @throws Exception
     */
    public static List<byte[]> responsePackMak(int cmd, byte[] body, int state ,String sn,int flowerCount,boolean isFirst) throws Exception
    {

        List<byte[]> packList = new ArrayList<byte[]>();
        if (body != null)
        {
         // 最大包体长度
            int maxBodyLen = 10240 - 27;
            
            
            int pkgCount = flowerCount;
            
            //封装包头
            Map<String, Object> headMap = new HashMap<String, Object>();
            headMap.put(Value.Head_Tag, Value.ResponseHeaderTag);
            headMap.put(Value.Head_CMD, cmd);
            headMap.put(Value.Head_SN, sn);
//            if(pkgCount==1)
//            {
//                headMap.put(Value.Head_PkgCount, 0x00);
//
//            }else
//            {
                headMap.put(Value.Head_PkgCount, pkgCount);
//            }

            if(!isFirst)
            {
                for (int i = 1; i < pkgCount +1; i++)
                {
                    //此包的包体的长度
                    int tempLen = maxBodyLen;
                    //如果是最后一个包，并且最后一个包不够最大包长，则这个包体的长度应该是余数
                    if (i == pkgCount)
                    {
                        tempLen = body.length % maxBodyLen==0?maxBodyLen:body.length % maxBodyLen;
                    }
                    //此包的包体
                    byte[] tempBody = new byte[tempLen];
                    System.arraycopy(body,(i-1)*maxBodyLen,tempBody,0,tempLen);

                    //此包的总长
                    int pkgLen = tempLen + 27;

                    headMap.put(Value.Head_PkgLen, pkgLen);

                    headMap.put(Value.Head_PkgIndex, i);

//                    if (i == pkgCount - 1)
//                    {
                        //如果是最后一个包，则要处理下一个命令
                        headMap.put(Value.Head_State, state);
//                    } else
//                    {
//                        //非最后一个包，下一包命令还是当前
//                        headMap.put(Value.Head_State, cmd);
//                    }

                    //包头
                    byte[] head = responseHeaderMake(headMap);

                    //包头加包体，用于计算校验码
                    byte[] headANDbody = new byte[pkgLen - 1];
                    System.arraycopy(head, 0, headANDbody, 0, 26);
                    System.arraycopy(tempBody, 0, headANDbody, 26, tempLen);

                    //校验码
                    byte xor = xOR(headANDbody);
                    //整包
                    byte[] fullPkg = new byte[pkgLen];
                    System.arraycopy(headANDbody, 0, fullPkg, 0, pkgLen-1);
                    fullPkg[pkgLen - 1] = xor;

                    packList.add(fullPkg);

                }
            }else
            {
                //此包的包体

                //此包的总长
                int pkgLen = body.length + 27;

                headMap.put(Value.Head_PkgLen, pkgLen);

                headMap.put(Value.Head_PkgIndex, 0);

//                if (flowerCount==0)
//                {
                    //如果是最后一个包，则要处理下一个命令
                    headMap.put(Value.Head_State, state);
//                } else
//                {
//                    //非最后一个包，下一包命令还是当前
//                    headMap.put(Value.Head_State, cmd);
//                }

                //包头
                byte[] head = responseHeaderMake(headMap);

                //包头加包体，用于计算校验码
                byte[] headANDbody = new byte[pkgLen - 1];
                System.arraycopy(head, 0, headANDbody, 0, 26);
                System.arraycopy(body, 0, headANDbody, 26, body.length);

                //校验码
                byte xor = xOR(headANDbody);
                //整包
                byte[] fullPkg = new byte[pkgLen];
                System.arraycopy(headANDbody, 0, fullPkg, 0, pkgLen-1);
                fullPkg[pkgLen - 1] = xor;

                packList.add(fullPkg);
            }
            
            return packList;
        } else
        {
            //没有包体要发送，只有包头

            //封装包头
            Map<String, Object> headMap = new HashMap<String, Object>();
            headMap.put(Value.Head_Tag, Value.ResponseHeaderTag);
            headMap.put(Value.Head_CMD, cmd);
            headMap.put(Value.Head_PkgIndex,0x00);
            headMap.put(Value.Head_PkgCount, 0x00);
            headMap.put(Value.Head_State, state);
            headMap.put(Value.Head_PkgLen, 27);
            headMap.put(Value.Head_SN,sn);
            byte[] head = responseHeaderMake(headMap);

            packList.add(head);
            byte xorbyte = PackUtil.xOR(head);
            packList.add(new byte[]{xorbyte});
            return packList;
        }
    }

    /**
     * 亦或校验结果
     * @param head
     * @param body
     * @param xors
     * @return
     */
    public static boolean checkXor(byte[] head,byte[] body,byte[] xors)
    {
        byte[] headANDbody = new byte[head.length+body.length];
        System.arraycopy(head, 0, headANDbody, 0, 26);
        System.arraycopy(body, 0, headANDbody, 26, body.length);
        byte xor = PackUtil.xOR(headANDbody);
        byte remoteXor = xors[0];
        return xor==remoteXor;
    }
}
