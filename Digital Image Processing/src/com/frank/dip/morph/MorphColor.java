/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved.
 * MorphGray.java is PROPRIETARY/CONFIDENTIAL built in 2013.
 * Use is subject to license terms.
 */
package com.frank.dip.morph;

import com.frank.dip.ColorImage;
import com.frank.math.MathUtils;

/**
 * The morphology operators for gray image.
 * <p>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class MorphColor extends Morph<ColorImage>
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
	protected MorphColor(int type)
	{
		super(type);
	}

	/**
	 * @see com.frank.dip.morph.Morph#erode(com.frank.dip.Image)
	 */
	@Override
	public ColorImage erode(ColorImage image)
	{
		int width = image.width();
		int height = image.height();
		int x, y;
		ColorImage res = image.clone();
		switch (type)
		{
			case STRUCTURE_SQUARE:
			default:
				for (x = 1; x < width - 1; x++)
					for (y = 1; y < height - 1; y++)
						res.setPixel(
								x,
								y,
								image.getAlpha(x, y),
								MathUtils.minimum(new int[] {
										image.getRed(x, y),
										image.getRed(x - 1, y),
										image.getRed(x + 1, y),
										image.getRed(x, y - 1),
										image.getRed(x - 1, y - 1),
										image.getRed(x + 1, y - 1),
										image.getRed(x, y + 1),
										image.getRed(x - 1, y + 1),
										image.getRed(x + 1, y + 1) }),
								MathUtils.minimum(new int[] {
										image.getGreen(x, y),
										image.getGreen(x - 1, y),
										image.getGreen(x + 1, y),
										image.getGreen(x, y - 1),
										image.getGreen(x - 1, y - 1),
										image.getGreen(x + 1, y - 1),
										image.getGreen(x, y + 1),
										image.getGreen(x - 1, y + 1),
										image.getGreen(x + 1, y + 1) }),
								MathUtils.minimum(new int[] {
										image.getBlue(x, y),
										image.getBlue(x - 1, y),
										image.getBlue(x + 1, y),
										image.getBlue(x, y - 1),
										image.getBlue(x - 1, y - 1),
										image.getBlue(x + 1, y - 1),
										image.getBlue(x, y + 1),
										image.getBlue(x - 1, y + 1),
										image.getBlue(x + 1, y + 1) }));
				break;
			case STRUCTURE_DIAMOND:
				for (x = 1; x < width - 1; x++)
					for (y = 1; y < height - 1; y++)
						res.setPixel(
								x,
								y,
								image.getAlpha(x, y),
								MathUtils.minimum(new int[] {
										image.getRed(x, y),
										image.getRed(x - 1, y),
										image.getRed(x + 1, y),
										image.getRed(x, y - 1),
										image.getRed(x, y + 1) }),
								MathUtils.minimum(new int[] {
										image.getGreen(x, y),
										image.getGreen(x - 1, y),
										image.getGreen(x + 1, y),
										image.getGreen(x, y - 1),
										image.getGreen(x, y + 1) }),
								MathUtils.minimum(new int[] {
										image.getBlue(x, y),
										image.getBlue(x - 1, y),
										image.getBlue(x + 1, y),
										image.getBlue(x, y - 1),
										image.getBlue(x, y + 1) }));
				break;
			case STRUCTURE_EIGHT_CORNER:
				for (x = 2; x < width - 2; x++)
					for (y = 2; y < height - 2; y++)
						res.setPixel(
								x,
								y,
								image.getAlpha(x, y),
								MathUtils.minimum(new int[] {
										image.getRed(x, y),
										image.getRed(x - 1, y),
										image.getRed(x - 2, y),
										image.getRed(x + 1, y),
										image.getRed(x + 2, y),
										image.getRed(x, y + 1),
										image.getRed(x - 1, y + 1),
										image.getRed(x - 2, y + 1),
										image.getRed(x + 1, y + 1),
										image.getRed(x + 2, y + 1),
										image.getRed(x, y - 1),
										image.getRed(x - 1, y - 1),
										image.getRed(x - 2, y - 1),
										image.getRed(x + 1, y - 1),
										image.getRed(x + 2, y - 1),
										image.getRed(x, y + 2),
										image.getRed(x - 1, y + 2),
										image.getRed(x + 1, y + 2),
										image.getRed(x, y - 2),
										image.getRed(x - 1, y - 2),
										image.getRed(x + 1, y - 2) }),
								MathUtils.minimum(new int[] {
										image.getGreen(x, y),
										image.getGreen(x - 1, y),
										image.getGreen(x - 2, y),
										image.getGreen(x + 1, y),
										image.getGreen(x + 2, y),
										image.getGreen(x, y + 1),
										image.getGreen(x - 1, y + 1),
										image.getGreen(x - 2, y + 1),
										image.getGreen(x + 1, y + 1),
										image.getGreen(x + 2, y + 1),
										image.getGreen(x, y - 1),
										image.getGreen(x - 1, y - 1),
										image.getGreen(x - 2, y - 1),
										image.getGreen(x + 1, y - 1),
										image.getGreen(x + 2, y - 1),
										image.getGreen(x, y + 2),
										image.getGreen(x - 1, y + 2),
										image.getGreen(x + 1, y + 2),
										image.getGreen(x, y - 2),
										image.getGreen(x - 1, y - 2),
										image.getGreen(x + 1, y - 2) }),
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
	public ColorImage dilate(ColorImage image)
	{
		int width = image.width();
		int height = image.height();
		int x, y;
		ColorImage res = image.clone();
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
	public ColorImage open(ColorImage image)
	{
		return erode(dilate(image));
	}

	/**
	 * @see com.frank.dip.morph.Morph#close(com.frank.dip.Image)
	 */
	@Override
	public ColorImage close(ColorImage image)
	{
		return dilate(erode(image));
	}
}
