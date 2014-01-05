/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights reserved.
 * MenuUtils.java is PROPRIETARY/CONFIDENTIAL built in 2013.
 * Use is subject to license terms.
 */
package com.frank.dip.demo.comp;

import java.awt.Component;

import javax.swing.JOptionPane;

import com.frank.dip.geom.Geometry;

/**
 * The menu utilities.
 * <p>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class MenuUtils
{
	/**
	 * Returns the interpolation scheme selected by user.
	 * 
	 * @param comp
	 *            the parent component
	 * @return the interpolation value, {@code null} if the user gives up
	 *         selection
	 */
	public static Integer selectInterpolationType(Component comp)
	{
		String[] types = new String[] { "Bilinear", "Nearest Neighbour",
				"Bicubic" };
		Object obj = JOptionPane.showInputDialog(comp,
				"Select interploation scheme:", "Interpolation",
				JOptionPane.INFORMATION_MESSAGE, null, types, types[0]);
		if (obj == null)
			return null;
		Integer r = 0;
		for (int i = 0; i < types.length; i++)
			if (obj.equals(types[i]))
				r = i;
		switch (r)
		{
			case 1:
				return Geometry.TYPE_NEAREST_NEIGHBOR;
			case 3:
				return Geometry.TYPE_BICUBIC;
			default:
			case 2:
				return Geometry.TYPE_BILINEAR;
		}
	}
}
