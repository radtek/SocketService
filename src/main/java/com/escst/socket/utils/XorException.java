package com.escst.socket.utils;
/**
 * Created by Tian on 2016/1/23.
 */
public class XorException extends Exception
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public XorException()
    {
    }

    public XorException(String message)
    {
        super(message);
    }

    public XorException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public XorException(Throwable cause)
    {
        super(cause);
    }

    public XorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
    {
        super(message, cause);
    }
}
