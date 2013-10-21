/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved. AdaptiveThresholding.java is PROPRIETARY/CONFIDENTIAL built in
 * 2013. Use is subject to license terms.
 */
package com.frank.dip.threshold;

import com.frank.dip.BinaryImage;
import com.frank.dip.GrayImage;

/**
 * Frank's adaptive thresholding method.
 * <p>
 * In this method, the global thresholding and local thresholding is combined.
 * First, the global thresholding is processed. The global threshold {@code t}
 * is past to retrieve all the pixels. If the gray scale of the pixel value is
 * in the {@code fuzzy region}, the local thresholding method is processed to
 * segment the image.
 * <p>
 * The global thresholding method is adapted to any algorithm which implemented
 * interface {@linkplain ThresholdFinder}. The local thresholding methold is
 * using {@linkplain FBClustering}.
 * </p>
 * <p>
 * This method is published on Frank Jiang's graduate paper: <br>
 * Fan Jiang. Research of encoding decoding and discrimination for character two
 * dimensional bar-code. Zhejiang University of Technology, bachelor paper,
 * 2013.
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class FrankThresholding extends Thresholding
{
	/**
	 * A fuzzy ratio refers to the fuzzy region of gray scale. in which the
	 * global threshold may mis-sort. Default value is 0.2, in other word, up
	 * and down 20% of the global threshold.
	 */
	protected double			fuzzy;
	/**
	 * The ratio of clustering window radius to image size, default 0.1 of the
	 * image dimension.
	 */
	protected double			radius;
	/**
	 * The global threshold finder.
	 */
	protected ThresholdFinder	finder;

	/**
	 * Construct an instance of <tt>FrankThresholding</tt> with default radius
	 * 0.1, fuzzy radio 0.2 and global threshold finder {@linkplain Otsu}.
	 */
	public FrankThresholding()
	{
		radius = 0.1;
		fuzzy = 0.2;
		finder = new Otsu();
	}

	/**
	 * Construct an instance of <tt>FrankThresholding</tt> with specified
	 * radius, fuzzy radio and global threshold finder.
	 * 
	 * @param radius
	 *            the specified clustering window radius, the radius value using
	 *            the rate of radius / window_size
	 * @param finder
	 *            the global threshold finder
	 * @param radius
	 * @param fuzzy
	 *            the threshold fuzzy radio
	 * @param finder
	 */
	public FrankThresholding(double radius, double fuzzy, ThresholdFinder finder)
	{
		if (fuzzy > 1 || fuzzy < 0 || radius > 1 || radius < 0)
			throw new IllegalArgumentException(
					"The threshold fuzzy radio(%f) and clustering window radius(%f) both must be in the range of [0,1].");
		this.radius = radius;
		this.fuzzy = fuzzy;
		this.finder = finder;
	}

	/**
	 * @see com.frank.dip.Operator#operate(com.frank.dip.Image)
	 */
	@Override
	public BinaryImage operate(GrayImage source)
	{
		int width = source.width();
		int height = source.height();
		int sizeW = (int) (width * radius);
		int sizeH = (int) (height * radius);
		if (sizeW < 1)
			sizeW = 1;
		if (sizeH < 1)
			sizeH = 1;
		// build integral image
		int[][] count = new int[width][height];
		int[][] sumF = new int[width][height];
		int[][] sumB = new int[width][height];
		int iCount, iSumF, iSumB, pixel;
		int threshold = finder.threshold(source);
		// statistic for the foreground and background
		for (int x = 0; x < width; x++)
		{
			iCount = iSumF = iSumB = 0;
			for (int y = 0; y < height; y++)
			{
				pixel = source.getPixel(x, y);
				if (pixel > threshold)
				{
					iCount++;
					iSumF += pixel;
				}
				else
					iSumB += pixel;
				if (x == 0)
				{
					count[x][y] = iCount;
					sumF[x][y] = iSumF;
					sumB[x][y] = iSumB;
				}
				else
				{
					count[x][y] = count[x - 1][y] + iCount;
					sumF[x][y] = sumF[x - 1][y] + iSumF;
					sumB[x][y] = sumB[x - 1][y] + iSumB;
				}
			}
		}
		// process thresholding
		int x0, xt, y0, yt;
		int totalF, totalB, countF, countB;
		BinaryImage dst = new BinaryImage(width, height);
		for (int x = 0; x < width; x++)
			for (int y = 0; y < height; y++)
			{
				pixel = source.getPixel(x, y);
				if (pixel > threshold * (1 + fuzzy)
						|| pixel < threshold * (1 - fuzzy))
					// perform global thresholding
					dst.setPixel(x, y, pixel > threshold);
				else
				{
					// perform local thresholding
					x0 = x - sizeW;
					xt = x + sizeW;
					y0 = y - sizeH;
					yt = y + sizeH;
					if (x0 < 0)
						x0 = 0;
					if (xt >= width)
						xt = width - 1;
					if (y0 < 0)
						y0 = 0;
					if (yt >= height)
						yt = height - 1;
					totalF = sumF[xt][yt] - sumF[x0][yt] - sumF[xt][y0]
							+ sumF[x0][y0];
					totalB = sumB[xt][yt] - sumB[x0][yt] - sumB[xt][y0]
							+ sumB[x0][y0];
					countF = count[xt][yt] - count[x0][yt] - count[xt][y0]
							+ count[x0][y0];
					countB = (xt - x0 + 1) * (yt - y0 + 1) - countF;
					if (countF == 0)
						countF++;
					if (countB == 0)
						countB++;
					dst.setPixel(x, y, pixel > ((totalF / countF + totalB
							/ countB) / 2.0f));
				}
			}
		return dst;
	}

	/**
	 * Get the thresholding finder.
	 * 
	 * @return the thresholding finder
	 */
	public ThresholdFinder getFinder()
	{
		return finder;
	}

	/**
	 * Set the thresholding finder.
	 * 
	 * @param finder
	 *            the thresholding finder
	 */
	public void setFinder(ThresholdFinder finder)
	{
		this.finder = finder;
	}

	/**
	 * Getter for radius.
	 * 
	 * @return the radius
	 */
	public double getRadius()
	{
		return radius;
	}

	/**
	 * Setter for radius.
	 * 
	 * @param radius
	 *            the value of radius
	 */
	public void setRadius(double radius)
	{
		this.radius = radius;
	}

	/**
	 * @see com.frank.dip.threshold.Thresholding#toString()
	 */
	@Override
	public String toString()
	{
		return "Frank's Tresholding";
	}
}
