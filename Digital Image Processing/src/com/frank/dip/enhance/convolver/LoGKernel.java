/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved. LoGKernel.java is PROPRIETARY/CONFIDENTIAL built in 2013. Use is
 * subject to license terms.
 */
package com.frank.dip.enhance.convolver;

/**
 * LoG(Laplace and Gauss) operator.
 * <p>
 * <table>
 * <tr align="right">
 * <td width="14%">|</td>
 * <td width="14%">-2</td>
 * <td width="14%">-4</td>
 * <td width="14%">-4</td>
 * <td width="14%">-4</td>
 * <td width="14%">2</td>
 * <td width="14%">|</td>
 * </tr>
 * <tr align="right">
 * <td width="14%">|</td>
 * <td width="14%">-4</td>
 * <td width="14%">0</td>
 * <td width="14%">8</td>
 * <td width="14%">0</td>
 * <td width="14%">-4</td>
 * <td width="14%">|</td>
 * </tr>
 * <tr align="right">
 * <td width="14%">|</td>
 * <td width="14%">-4</td>
 * <td width="14%">8</td>
 * <td width="14%">24</td>
 * <td width="14%">8</td>
 * <td width="14%">-4</td>
 * <td width="14%">|</td>
 * </tr>
 * <tr align="right">
 * <td width="14%">|</td>
 * <td width="14%">-4</td>
 * <td width="14%">0</td>
 * <td width="14%">8</td>
 * <td width="14%">0</td>
 * <td width="14%">-4</td>
 * <td width="14%">|</td>
 * </tr>
 * <tr align="right">
 * <td width="14%">|</td>
 * <td width="14%">-2</td>
 * <td width="14%">-4</td>
 * <td width="14%">-4</td>
 * <td width="14%">-4</td>
 * <td width="14%">2</td>
 * <td width="14%">|</td>
 * </tr>
 * </table>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class LoGKernel extends SingleKernel
{
	/**
	 * Construct an instance of <tt>LoGKernel</tt>.
	 */
	public LoGKernel()
	{
		super(5, 5, -2, -4, -4, -4, -2, -4, 0, 8, 0, -4, -4, 8, 24, 8, -4, -4,
				0, 8, 0, -4, -2, -4, -4, -4, -2);
	}
}
