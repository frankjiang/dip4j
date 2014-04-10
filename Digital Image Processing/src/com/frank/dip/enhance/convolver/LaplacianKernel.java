/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved. LaplaceKernel.java is PROPRIETARY/CONFIDENTIAL built in 2013. Use
 * is subject to license terms.
 */
package com.frank.dip.enhance.convolver;

/**
 * Laplacian operator.
 * <p>
 * Laplacian operator divides into two normal forms.
 * <table>
 * <tr align="right">
 * <td width="20%">|</td>
 * <td width="20%">0</td>
 * <td width="20%">1</td>
 * <td width="20%">0</td>
 * <td width="20%">|</td>
 * </tr>
 * <tr align="right">
 * <td>|</td>
 * <td>1</td>
 * <td>-4</td>
 * <td>1</td>
 * <td>|</td>
 * </tr>
 * <tr align="right" >
 * <td>|</td>
 * <td>0</td>
 * <td>1</td>
 * <td>0</td>
 * <td>|</td>
 * </tr>
 * </table>
 * and
 * <table>
 * <tr align="right" width="33%">
 * <td width="20%">|</td>
 * <td width="20%">1</td>
 * <td width="20%">1</td>
 * <td width="20%">1</td>
 * <td width="20%">|</td>
 * </tr>
 * <tr align="right" width="33%">
 * <td>|</td>
 * <td>1</td>
 * <td>-8</td>
 * <td>1</td>
 * <td>|</td>
 * </tr>
 * <tr align="right" >
 * <td>|</td>
 * <td>1</td>
 * <td>1</td>
 * <td>1</td>
 * <td>|</td>
 * </tr>
 * </table>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class LaplacianKernel extends SingleKernel
{
	/**
	 * Laplacian kernel.
	 * <p>
	 * <table>
	 * <tr align="right">
	 * <td width="20%">|</td>
	 * <td width="20%">0</td>
	 * <td width="20%">1</td>
	 * <td width="20%">0</td>
	 * <td width="20%">|</td>
	 * </tr>
	 * <tr align="right">
	 * <td>|</td>
	 * <td>1</td>
	 * <td>-4</td>
	 * <td>1</td>
	 * <td>|</td>
	 * </tr>
	 * <tr align="right" >
	 * <td>|</td>
	 * <td>0</td>
	 * <td>1</td>
	 * <td>0</td>
	 * <td>|</td>
	 * </tr>
	 * </table>
	 * </p>
	 */
	public static final int	KERNEL_4	= 4;
	/**
	 * Laplacian kernel.
	 * <p>
	 * <table>
	 * <tr align="right" width="33%">
	 * <td width="20%">|</td>
	 * <td width="20%">1</td>
	 * <td width="20%">1</td>
	 * <td width="20%">1</td>
	 * <td width="20%">|</td>
	 * </tr>
	 * <tr align="right" width="33%">
	 * <td>|</td>
	 * <td>1</td>
	 * <td>-8</td>
	 * <td>1</td>
	 * <td>|</td>
	 * </tr>
	 * <tr align="right" >
	 * <td>|</td>
	 * <td>1</td>
	 * <td>1</td>
	 * <td>1</td>
	 * <td>|</td>
	 * </tr>
	 * </table>
	 * </p>
	 */
	public static final int	KERNEL_8	= 8;

	/**
	 * Construct an instance of <tt>LaplaceKernel</tt>.
	 * 
	 * @param type
	 *            the type of Laplace convolution
	 * @see #KERNEL_4
	 * @see #KERNEL_8
	 */
	public LaplacianKernel(int type)
	{
		switch (type)
		{
			case KERNEL_4:
				initialize(3, 3, 0, 1, 0, 1, -4, 1, 0, 1, 0);
				break;
			case KERNEL_8:
				initialize(3, 3, 1, 1, 1, 1, -8, 1, 1, 1, 1);
				break;
			default:
				throw new IllegalArgumentException(String.format(
						"Unknown Laplace convolution type: %s", type));
		}
	}
}
