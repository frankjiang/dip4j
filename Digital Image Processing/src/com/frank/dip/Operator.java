/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved.
 * Operator.java is PROPRIETARY/CONFIDENTIAL built in 2013.
 * Use is subject to license terms.
 */
package com.frank.dip;


/**
 * The digital image processing operator.
 * <p>
 * In this operator, the source image is defined, the real operators can be
 * implemented based on this abstract operator.
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @param <SOURCE>
 *            the type of source image
 * @param <RESULT>
 *            the type of result image
 * @version 1.0.0
 */
public abstract class Operator<SOURCE extends Image, RESULT extends Image>
{
	/**
	 * Operate the source image with specified method which is waiting to be
	 * performed.
	 * 
	 * @param source
	 *            the source image
	 * @return the result image after operator performed
	 */
	public abstract RESULT operate(SOURCE source);
}
