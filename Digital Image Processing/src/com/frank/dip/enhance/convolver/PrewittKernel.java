/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved. PrewittKernel.java is PROPRIETARY/CONFIDENTIAL built in 2013. Use
 * is subject to license terms.
 */
package com.frank.dip.enhance.convolver;

/**
 * Prewitt operator.
 * <p>
 * <table>
 * <tr align="right">
 * <td width="9%">|</td>
 * <td width="9%">-1</td>
 * <td width="9%">0</td>
 * <td width="9%">1</td>
 * <td width="9%">|</td>
 * <td></td>
 * <td width="9%">|</td>
 * <td width="9%">1</td>
 * <td width="9%">1</td>
 * <td width="9%">1</td>
 * <td width="9%">|</td>
 * </tr>
 * <tr align="right">
 * <td>|</td>
 * <td>-1</td>
 * <td>0</td>
 * <td>1</td>
 * <td>|</td>
 * <td></td>
 * <td>|</td>
 * <td>0</td>
 * <td>0</td>
 * <td>0</td>
 * <td>|</td>
 * </tr>
 * <tr align="right">
 * <td>|</td>
 * <td>-1</td>
 * <td>0</td>
 * <td>1</td>
 * <td>|</td>
 * <td></td>
 * <td>|</td>
 * <td>-1</td>
 * <td>-1</td>
 * <td>-1</td>
 * <td>|</td>
 * </tr>
 * </table>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class PrewittKernel extends MultiKernel
{
	/**
	 * Construct an instance of <tt>PrewittKernel</tt>.
	 */
	public PrewittKernel()
	{
		initialize(new SingleKernel(3, 3, -1, 0, 1, -1, 0, 1, -1, 0, 1),
				new SingleKernel(3, 3, 1, 1, 1, 0, 0, 0, -1, -1 - 1));
	}
}
