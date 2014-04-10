/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved.
 * FourierLaplacianFilter.java is PROPRIETARY/CONFIDENTIAL built in 2013.
 * Use is subject to license terms.
 */
package com.frank.dip.enhance.frequency;

import com.frank.dip.Image;
import com.frank.math.MathUtils;

/**
 * The Laplacian filter in frequency domain.
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class FourierLaplacianFilter extends AbstractFourierFilter
{
	/**
	 * @see com.frank.dip.analyze.FourierTransformation#complex(com.frank.dip.Image)
	 */
	public void complex(Image source)
	{
		super.complex(source);
		double cx = width / 2.0;
		double cy = height / 2.0;
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
				complex[y][x] = complex[y][x].multiply(-MathUtils.hypot(x - cx,
						y - cy));
	}
}
