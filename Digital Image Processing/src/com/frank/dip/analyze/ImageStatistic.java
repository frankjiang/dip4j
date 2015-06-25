/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved.
 * ImageStatistic.java is PROPRIETARY/CONFIDENTIAL built in 5:43:51 AM, May 2,
 * 2014.
 * Use is subject to license terms.
 */
package com.frank.dip.analyze;

import java.awt.geom.Point2D;

import com.frank.dip.BinaryImage;
import com.frank.dip.GrayImage;
import com.frank.dip.Image;
import com.frank.dip.threshold.GlobalAverage;
import com.frank.dip.threshold.GlobalThresholding;

/**
 * The image statistic class.
 * <p>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class ImageStatistic
{
	/**
	 * Returns the gravity center of the specified image.
	 * 
	 * @param image
	 *            the specified image
	 * @return the gravity center
	 */
	public static Point2D center(Image image)
	{
		int xc = 0, yc = 0;
		int width = image.getWidth();
		int height = image.getHeight();
		boolean[][] m = null;
		if (image instanceof BinaryImage)
			m = ((BinaryImage) image).getBinaryMatrix();
		else
		{
			if (image instanceof GrayImage)
				m = new GlobalThresholding(new GlobalAverage()).operate(
						(GrayImage) image).getBinaryMatrix();
			else
				m = new GlobalThresholding(new GlobalAverage()).operate(
						new GrayImage(image)).getBinaryMatrix();
		}
		int count = 0;
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
				if (m[y][x])
				{
					xc += x;
					yc += y;
					count++;
				}
		return new Point2D.Double(xc / (double) count, yc / (double) count);
		//		if (image instanceof BinaryImage)
		//		{
		//			boolean[][] m = ((BinaryImage) image).getBinaryMatrix();
		//			int count = 0;
		//			for (int y = 0; y < height; y++)
		//				for (int x = 0; x < width; x++)
		//					if (m[y][x])
		//					{
		//						xc += x;
		//						yc += y;
		//						count++;
		//					}
		//			return new Point2D.Double(xc / (double) count, yc / (double) count);
		//		}
		//		GrayImage gi = null;
		//		if (image instanceof GrayImage)
		//			gi = (GrayImage) image;
		//		else
		//			gi = new GrayImage(image);
		//		double count = 0;
		//		for (int y = 0; y < height; y++)
		//			for (int x = 0; x < width; x++)
		//			{
		//				double g = gi.getPixel(x, y)
		//						/ (double) ColorScaleLevel.COLOR_SCALE_LEVEL;
		//				count += g;
		//				xc += x;
		//				yc += y;
		//			}
		//		return new Point2D.Double(xc / (double) count, yc / (double) count);
	}
	//	public static double circle(BinaryImage image)
	//	{
	//		// TODO image circle
	//		return 0.0;
	//	}
}
