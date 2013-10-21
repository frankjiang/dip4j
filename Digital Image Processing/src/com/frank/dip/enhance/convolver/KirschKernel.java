/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved. KirschKernel.java is PROPRIETARY/CONFIDENTIAL built in 2013. Use is
 * subject to license terms.
 */
package com.frank.dip.enhance.convolver;


/**
 * Kirsch operator.
 * <p>
 * <table>
 * <tr align="right">
 * <td width="4.3%">|</td>
 * <td width="4.3%">5</td>
 * <td width="4.3%">5</td>
 * <td width="4.3%">5</td>
 * <td width="4.3%">|</td>
 * <td width="4.3%"></td>
 * <td width="4.3%">|</td>
 * <td width="4.3%">-3</td>
 * <td width="4.3%">5</td>
 * <td width="4.3%">5</td>
 * <td width="4.3%">|</td>
 * <td width="4.3%"></td>
 * <td width="4.3%">|</td>
 * <td width="4.3%">-3</td>
 * <td width="4.3%">-3</td>
 * <td width="4.3%">5</td>
 * <td width="4.3%">|</td>
 * <td width="4.3%"></td>
 * <td width="4.3%">|</td>
 * <td width="4.3%">-3</td>
 * <td width="4.3%">-3</td>
 * <td width="4.3%">-3</td>
 * <td width="4.3%">|</td>
 * </tr>
 * <tr align="right">
 * <td>|</td>
 * <td>-3</td>
 * <td>0</td>
 * <td>-3</td>
 * <td>|</td>
 * <td></td>
 * <td>|</td>
 * <td>-3</td>
 * <td>0</td>
 * <td>5</td>
 * <td>|</td>
 * <td></td>
 * <td>|</td>
 * <td>-3</td>
 * <td>0</td>
 * <td>5</td>
 * <td>|</td>
 * <td></td>
 * <td>|</td>
 * <td>-3</td>
 * <td>0</td>
 * <td>5</td>
 * <td>|</td>
 * </tr>
 * <tr align="right">
 * <td>|</td>
 * <td>-3</td>
 * <td>-3</td>
 * <td>-3</td>
 * <td>|</td>
 * <td></td>
 * <td>|</td>
 * <td>-3</td>
 * <td>-3</td>
 * <td>-3</td>
 * <td>|</td>
 * <td></td>
 * <td>|</td>
 * <td>-3</td>
 * <td>-3</td>
 * <td>5</td>
 * <td>|</td>
 * <td></td>
 * <td>|</td>
 * <td>-3</td>
 * <td>5</td>
 * <td>5</td>
 * <td>|</td>
 * </tr>
 * <tr>
 * <td>&nbsp;</td>
 * </tr>
 * <tr align="right">
 * <td>|</td>
 * <td>5</td>
 * <td>5</td>
 * <td>5</td>
 * <td>|</td>
 * <td></td>
 * <td>|</td>
 * <td>-3</td>
 * <td>-3</td>
 * <td>-3</td>
 * <td>|</td>
 * <td></td>
 * <td>|</td>
 * <td>5</td>
 * <td>-3</td>
 * <td>-3</td>
 * <td>|</td>
 * <td></td>
 * <td>|</td>
 * <td>5</td>
 * <td>5</td>
 * <td>-3</td>
 * <td>|</td>
 * </tr>
 * <tr align="right">
 * <td>|</td>
 * <td>-3</td>
 * <td>0</td>
 * <td>-3</td>
 * <td>|</td>
 * <td></td>
 * <td>|</td>
 * <td>5</td>
 * <td>0</td>
 * <td>-3</td>
 * <td>|</td>
 * <td></td>
 * <td>|</td>
 * <td>5</td>
 * <td>0</td>
 * <td>-3</td>
 * <td>|</td>
 * <td></td>
 * <td>|</td>
 * <td>5</td>
 * <td>0</td>
 * <td>-3</td>
 * <td>|</td>
 * </tr>
 * <tr align="right">
 * <td>|</td>
 * <td>5</td>
 * <td>5</td>
 * <td>5</td>
 * <td>|</td>
 * <td></td>
 * <td>|</td>
 * <td>5</td>
 * <td>5</td>
 * <td>-3</td>
 * <td>|</td>
 * <td></td>
 * <td>|</td>
 * <td>5</td>
 * <td>-3</td>
 * <td>-3</td>
 * <td>|</td>
 * <td></td>
 * <td>|</td>
 * <td>-3</td>
 * <td>-3</td>
 * <td>-3</td>
 * <td>|</td>
 * </tr>
 * <table>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class KirschKernel extends MultiKernel
{
	/**
	 * Construct an instance of <tt>KirschKernel</tt>.
	 */
	public KirschKernel()
	{
		initialize(new SingleKernel(3, 3, 5, 5, 5, -3, 0, -3, -3, -3, -3),
				new SingleKernel(3, 3, -3, 5, 5, -3, 0, 5, -3, -3, -3),
				new SingleKernel(3, 3, -3, -3, 5, -3, 0, 5, -3, -3, 5),
				new SingleKernel(3, 3, -3, -3, -3, -3, 0, 5, -3, 5, 5),
				new SingleKernel(3, 3, -3, -3, -3, -3, 0, -3, 5, 5, 5),
				new SingleKernel(3, 3, -3, -3, -3, 5, 0, -3, 5, 5, -3),
				new SingleKernel(3, 3, 5, -3, -3, 5, 0, -3, 5, -3, -3),
				new SingleKernel(3, 3, 5, 5, -3, 5, 0, -3, -3, -3, -3));
	}
}
