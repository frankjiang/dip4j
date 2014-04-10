/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved.
 * IllegalImageTypeExcpetion.java is PROPRIETARY/CONFIDENTIAL built in 3:56:07
 * PM, Mar 5, 2014.
 * Use is subject to license terms.
 */
package com.frank.dip;

/**
 * Illegal image type exception infers the image type is not supported by the
 * current system/operator.
 * <p>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class IllegalImageTypeException extends RuntimeException
{
	/**
	 * serialVersionUID.
	 */
	private static final long	serialVersionUID	= 180073945112540058L;

	/**
	 * Construct an instance of <tt>IllegalImageTypeExcpetion</tt>.
	 * 
	 * @param component
	 *            the component name
	 * @param imageType
	 *            the image type
	 */
	public IllegalImageTypeException(String component, Class imageType)
	{
		super(String.format("%s cannot support image type: %s", imageType));
	}

	/**
	 * Construct an instance of <tt>IllegalImageTypeExcpetion</tt>.
	 * 
	 * @param componentType
	 *            the component type
	 * @param imageType
	 *            the image type
	 */
	public IllegalImageTypeException(Class componentType, Class imageType)
	{
		super(String.format("%s cannot support image type: %s", componentType,
				imageType));
	}
}
