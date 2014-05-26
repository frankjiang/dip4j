/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved.
 * IllegalImageContentException.java is PROPRIETARY/CONFIDENTIAL built in
 * 3:58:21 AM, May 3, 2014.
 * Use is subject to license terms.
 */
package com.frank.dip;

/**
 * The illegal image content exception indicates that the assertion of a image
 * is failed.
 * <p>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class IllegalImageContentException extends IllegalArgumentException
{
	/**
	 * serialVersionUID.
	 */
	private static final long	serialVersionUID	= 2110113106293566322L;

	/**
	 * Construct an instance of <tt>IllegalImageContentException</tt>.
	 */
	public IllegalImageContentException()
	{
	}

	/**
	 * Construct an instance of <tt>IllegalImageContentException</tt>.
	 * 
	 * @param s
	 *            the detail message.
	 */
	public IllegalImageContentException(String s)
	{
		super(s);
	}

	/**
	 * Construct an instance of <tt>IllegalImageContentException</tt>.
	 * 
	 * @param cause
	 *            the cause (which is saved for later retrieval by the
	 *            {@link Throwable#getCause()} method). (A <tt>null</tt> value
	 *            is permitted, and indicates that the cause is nonexistent or
	 *            unknown.)
	 */
	public IllegalImageContentException(Throwable cause)
	{
		super(cause);
	}

	/**
	 * Construct an instance of <tt>IllegalImageContentException</tt>.
	 * 
	 * @param message
	 *            the detail message (which is saved for later retrieval
	 *            by the {@link Throwable#getMessage()} method).
	 * @param cause
	 *            the cause (which is saved for later retrieval by the
	 *            {@link Throwable#getCause()} method). (A <tt>null</tt> value
	 *            is permitted, and indicates that the cause is nonexistent or
	 *            unknown.)
	 */
	public IllegalImageContentException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
