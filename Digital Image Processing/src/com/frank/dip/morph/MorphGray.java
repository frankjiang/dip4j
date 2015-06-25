/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved.
 * MorphGray.java is PROPRIETARY/CONFIDENTIAL built in 2013.
 * Use is subject to license terms.
 */
package com.frank.dip.morph;

import com.frank.dip.GrayImage;
import com.frank.math.MathUtils;

/**
 * The morphology operators for gray image.
 * <p>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class MorphGray extends Morph<GrayImage>
{
	/**
	 * Construct an instance of <tt>MorphGray</tt>.
	 * 
	 * @param type
	 *            the type of morphology structure
	 * @see #STRUCTURE_DIAMOND
	 * @see #STRUCTURE_EIGHT_CORNER
	 * @see #STRUCTURE_SQUARE
	 */
	protected MorphGray(int type)
	{
		super(type);
	}

	/**
	 * @see com.frank.dip.morph.Morph#erode(com.frank.dip.Image)
	 */
	@Override
	public GrayImage erode(GrayImage image)
	{
		int width = image.getWidth();
		int height = image.getHeight();
		int x, y;
		GrayImage res = image.clone();
		switch (type)
		{
			case STRUCTURE_SQUARE:
			default:
				for (x = 1; x < width - 1; x++)
					for (y = 1; y < height - 1; y++)
						res.setPixel(
								x,
								y,
								MathUtils.minimum(new int[] {
										image.getPixel(x, y),
										image.getPixel(x - 1, y),
										image.getPixel(x + 1, y),
										image.getPixel(x, y - 1),
										image.getPixel(x - 1, y - 1),
										image.getPixel(x + 1, y - 1),
										image.getPixel(x, y + 1),
										image.getPixel(x - 1, y + 1),
										image.getPixel(x + 1, y + 1) }));
				break;
			case STRUCTURE_DIAMOND:
				for (x = 1; x < width - 1; x++)
					for (y = 1; y < height - 1; y++)
						res.setPixel(
								x,
								y,
								MathUtils.minimum(new int[] {
										image.getPixel(x, y),
										image.getPixel(x - 1, y),
										image.getPixel(x + 1, y),
										image.getPixel(x, y - 1),
										image.getPixel(x, y + 1) }));
				break;
			case STRUCTURE_EIGHT_CORNER:
				for (x = 2; x < width - 2; x++)
					for (y = 2; y < height - 2; y++)
						res.setPixel(
								x,
								y,
								MathUtils.minimum(new int[] {
										image.getPixel(x, y),
										image.getPixel(x - 1, y),
										image.getPixel(x - 2, y),
										image.getPixel(x + 1, y),
										image.getPixel(x + 2, y),
										image.getPixel(x, y + 1),
										image.getPixel(x - 1, y + 1),
										image.getPixel(x - 2, y + 1),
										image.getPixel(x + 1, y + 1),
										image.getPixel(x + 2, y + 1),
										image.getPixel(x, y - 1),
										image.getPixel(x - 1, y - 1),
										image.getPixel(x - 2, y - 1),
										image.getPixel(x + 1, y - 1),
										image.getPixel(x + 2, y - 1),
										image.getPixel(x, y + 2),
										image.getPixel(x - 1, y + 2),
										image.getPixel(x + 1, y + 2),
										image.getPixel(x, y - 2),
										image.getPixel(x - 1, y - 2),
										image.getPixel(x + 1, y - 2) }));
				break;
		}
		return res;
	}

	/**
	 * @see com.frank.dip.morph.Morph#dilate(com.frank.dip.Image)
	 */
	@Override
	public GrayImage dilate(GrayImage image)
	{
		int width = image.getWidth();
		int height = image.getHeight();
		int x, y;
		GrayImage res = image.clone();
		switch (type)
		{
			case STRUCTURE_SQUARE:
			default:
				for (x = 1; x < width - 1; x++)
					for (y = 1; y < height - 1; y++)
						res.setPixel(
								x,
								y,
								MathUtils.maximum(new int[] {
										image.getPixel(x, y),
										image.getPixel(x - 1, y),
										image.getPixel(x + 1, y),
										image.getPixel(x, y - 1),
										image.getPixel(x - 1, y - 1),
										image.getPixel(x + 1, y - 1),
										image.getPixel(x, y + 1),
										image.getPixel(x - 1, y + 1),
										image.getPixel(x + 1, y + 1) }));
				break;
			case STRUCTURE_DIAMOND:
				for (x = 1; x < width - 1; x++)
					for (y = 1; y < height - 1; y++)
						res.setPixel(
								x,
								y,
								MathUtils.maximum(new int[] {
										image.getPixel(x, y),
										image.getPixel(x - 1, y),
										image.getPixel(x + 1, y),
										image.getPixel(x, y - 1),
										image.getPixel(x, y + 1) }));
				break;
			case STRUCTURE_EIGHT_CORNER:
				for (x = 2; x < width - 2; x++)
					for (y = 2; y < height - 2; y++)
						res.setPixel(
								x,
								y,
								MathUtils.maximum(new int[] {
										image.getPixel(x, y),
										image.getPixel(x - 1, y),
										image.getPixel(x - 2, y),
										image.getPixel(x + 1, y),
										image.getPixel(x + 2, y),
										image.getPixel(x, y + 1),
										image.getPixel(x - 1, y + 1),
										image.getPixel(x - 2, y + 1),
										image.getPixel(x + 1, y + 1),
										image.getPixel(x + 2, y + 1),
										image.getPixel(x, y - 1),
										image.getPixel(x - 1, y - 1),
										image.getPixel(x - 2, y - 1),
										image.getPixel(x + 1, y - 1),
										image.getPixel(x + 2, y - 1),
										image.getPixel(x, y + 2),
										image.getPixel(x - 1, y + 2),
										image.getPixel(x + 1, y + 2),
										image.getPixel(x, y - 2),
										image.getPixel(x - 1, y - 2),
										image.getPixel(x + 1, y - 2) }));
				break;
		}
		return res;
	}

	/**
	 * @see com.frank.dip.morph.Morph#open(com.frank.dip.Image)
	 */
	@Override
	public GrayImage open(GrayImage image)
	{
		return dilate(erode(image));
	}

	/**
	 * @see com.frank.dip.morph.Morph#close(com.frank.dip.Image)
	 */
	@Override
	public GrayImage close(GrayImage image)
	{
		return erode(dilate(image));
	}
}
