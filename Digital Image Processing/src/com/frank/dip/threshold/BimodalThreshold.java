/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved.
 * BimodalThreshold.java is PROPRIETARY/CONFIDENTIAL built in 2013.
 * Use is subject to license terms.
 */
package com.frank.dip.threshold;

/**
 * A bi-modal threshold finder defines the threshold finding algorithm including
 * bi-modal test.
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public abstract class BimodalThreshold implements ThresholdFinder
{
	/**
	 * The maximum value of iteration times.
	 */
	protected int	maxIteration	= 10000;

	/**
	 * Get maximum value of iteration times.
	 * 
	 * @return maximum value of iteration times
	 */
	public int getMaxIteration()
	{
		return maxIteration;
	}

	/**
	 * Set maximum value of iteration times.
	 * 
	 * @param maxIteration
	 *            maximum value of iteration times
	 */
	public void setMaxIteration(int maxIteration)
	{
		this.maxIteration = maxIteration;
	}

	/**
	 * Bi-modal test.
	 * 
	 * @param y
	 * @return <tt>true</tt> if modes equals 2
	 */
	protected boolean bimodalTest(double[] y)
	{
		boolean b = false;
		int modes = 0;
		for (int k = 1; k < y.length - 1; k++)
		{
			if (y[k - 1] < y[k] && y[k + 1] < y[k])
			{
				modes++;
				if (modes > 2)
					return false;
			}
		}
		if (modes == 2)
			b = true;
		return b;
	}
}
