package com.escst.socket.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by Tian on 2016/1/22.
 */
public class FreeIO
{
    static Logger logger = LoggerFactory.getLogger(FreeIO.class);
    public static void free(Object o)
    {
        if(o!=null)
        {
            if(o instanceof InputStream)
            {
                try
                {
                    ((InputStream) o).close();
                } catch (IOException e)
                {
                    logger.error("FreeIO.free()--"+o,e);
                }
            }else if(o instanceof OutputStream)
            {
                try
                {
                    ((OutputStream) o).close();
                } catch (IOException e)
                {
                    logger.error("FreeIO.free()--"+o,e);
                }
            }else if(o instanceof Socket)
            {
//                try
//                {
////                    ((Socket) o).close();
//                } catch (IOException e)
//                {
//                    logger.error("FreeIO.free()--"+o,e);
//                }
            }
        }

    }
}
