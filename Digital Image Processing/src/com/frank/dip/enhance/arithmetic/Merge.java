/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved.
 * Merge.java is PROPRIETARY/CONFIDENTIAL built in 4:46:30 AM, May 2, 2014.
 * Use is subject to license terms.
 */
package com.frank.dip.enhance.arithmetic;

import com.frank.dip.BinaryImage;
import com.frank.dip.ColorImage;
import com.frank.dip.ColorScaleLevel;
import com.frank.dip.GrayImage;
import com.frank.dip.IllegalImageTypeException;
import com.frank.dip.Image;

/**
 * The image merging implementation.
 * <p>
 * This operator can merge a collection of images to produce a new image with
 * the average value of each original pixel.
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class Merge
{
	/**
	 * Merge the specified image collection.
	 * <p>
	 * The before and after merging image type comparison table.
	 * <table>
	 * <tr>
	 * <td><strong>Before</strong></td>
	 * <td><strong>After</strong></td>
	 * </tr>
	 * <tr>
	 * <td>Binary Image</td>
	 * <td>Gray Image</td>
	 * </tr>
	 * <tr>
	 * <td>Gray Image</td>
	 * <td>Gray Image</td>
	 * </tr>
	 * <tr>
	 * <td>Color Image</td>
	 * <td>Color Image</td>
	 * </tr>
	 * <tr>
	 * <td>Other Image</td>
	 * <td>Display Error</td>
	 * </tr>
	 * </table>
	 * </p>
	 * 
	 * @param images
	 *            the specified image collection to merge
	 * @return the merged image collection
	 */
	public Image operate(java.util.Collection<? extends Image> images)
	{
		if (images.isEmpty())
			throw new IllegalArgumentException(
					"The image collection can not be empty.");
		Image first = images.iterator().next();
		int width = first.getWidth();
		int height = first.getHeight();
		double size = images.size();
		long[][] data = new long[height][width];
		String errorFormat = "The image size(%d,%d) is not agreed with (%d, %d).";
		// binary image
		if (first instanceof BinaryImage)
		{
			for (Image image : images)
			{
				if (image.getWidth() != width || image.getHeight() != height)
					throw new IllegalArgumentException(String.format(
							errorFormat, image.getWidth(), image.getHeight(), width,
							height));
				BinaryImage bi = (BinaryImage) image;
				boolean[][] m = bi.getBinaryMatrix();
				for (int y = 0; y < height; y++)
					for (int x = 0; x < width; x++)
						if (m[y][x])
							data[y][x]++;
			}
			GrayImage image = new GrayImage(width, height);
			for (int y = 0; y < height; y++)
				for (int x = 0; x < width; x++)
					image.setPixel(x, y, data[y][x] / size
							* ColorScaleLevel.COLOR_SCALE_LEVEL);
			return image;
		}
		// gray image
		if (first instanceof GrayImage)
		{
			for (Image image : images)
			{
				if (image.getWidth() != width || image.getHeight() != height)
					throw new IllegalArgumentException(String.format(
							errorFormat, image.getWidth(), image.getHeight(), width,
							height));
				GrayImage gi = (GrayImage) image;
				for (int y = 0; y < height; y++)
					for (int x = 0; x < width; x++)
						data[y][x] += gi.getPixel(x, y);
			}
			GrayImage image = new GrayImage(width, height);
			for (int y = 0; y < height; y++)
				for (int x = 0; x < width; x++)
					image.setPixel(x, y, data[y][x] / size);
			return image;
		}
		// color image
		if (first instanceof ColorImage)
		{
			long[][] red, green, blue;
			red = new long[height][width];
			green = new long[height][width];
			blue = new long[height][width];
			for (Image image : images)
			{
				if (image.getWidth() != width || image.getHeight() != height)
					throw new IllegalArgumentException(String.format(
							errorFormat, image.getWidth(), image.getHeight(), width,
							height));
				ColorImage ci = (ColorImage) image;
				for (int y = 0; y < height; y++)
					for (int x = 0; x < width; x++)
					{
						data[y][x] += ci.getAlpha(x, y);
						red[y][x] += ci.getRed(x, y);
						green[y][x] += ci.getGreen(x, y);
						blue[y][x] += ci.getBlue(x, y);
					}
			}
			ColorImage image = new ColorImage(width, height);
			for (int y = 0; y < height; y++)
				for (int x = 0; x < width; x++)
					image.setPixel(x, y, (int) Math.round(data[y][x] / size),
							(int) Math.round(red[y][x] / size),
							(int) Math.round(green[y][x] / size),
							(int) Math.round(blue[y][x] / size));
			return image;
		}
		throw new IllegalImageTypeException(getClass(), first.getClass());
	}
}
