/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights reserved.
 * Morph.java is PROPRIETARY/CONFIDENTIAL built in 2013.
 * Use is subject to license terms.
 */
package com.frank.dip.morph;

import com.frank.dip.BinaryImage;
import com.frank.dip.ColorImage;
import com.frank.dip.GrayImage;
import com.frank.dip.Image;

/**
 * The morphology operators.
 * <p>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @param <T>
 *            the type of the image
 * @version 1.0.0
 */
public abstract class Morph<T extends Image>
{
	/**
	 * The 3&times;3 square morphology structure.
	 * <table>
	 * <tr align="right">
	 * <td>|</td>
	 * <td>1</td>
	 * <td>1</td>
	 * <td>1</td>
	 * <td>|</td>
	 * </tr>
	 * <tr align="right">
	 * <td>|</td>
	 * <td>1</td>
	 * <td>1</td>
	 * <td>1</td>
	 * <td>|</td>
	 * </tr>
	 * <tr align="right">
	 * <td>|</td>
	 * <td>1</td>
	 * <td>1</td>
	 * <td>1</td>
	 * <td>|</td>
	 * </tr>
	 * </table>
	 */
	public static final int	STRUCTURE_SQUARE		= 0;
	/**
	 * The 3&times;3 diamond morphology structure.
	 * <table>
	 * <tr align="right">
	 * <td>|</td>
	 * <td>0</td>
	 * <td>1</td>
	 * <td>0</td>
	 * <td>|</td>
	 * </tr>
	 * <tr align="right">
	 * <td>|</td>
	 * <td>1</td>
	 * <td>1</td>
	 * <td>1</td>
	 * <td>|</td>
	 * </tr>
	 * <tr align="right">
	 * <td>|</td>
	 * <td>0</td>
	 * <td>1</td>
	 * <td>0</td>
	 * <td>|</td>
	 * </tr>
	 * </table>
	 */
	public static final int	STRUCTURE_DIAMOND		= 1;
	/**
	 * The 5&times;5 eight corner morphology structure.
	 * <table>
	 * <tr align="right">
	 * <td>|</td>
	 * <td>0</td>
	 * <td>1</td>
	 * <td>1</td>
	 * <td>1</td>
	 * <td>0</td>
	 * <td>|</td>
	 * </tr>
	 * <tr align="right">
	 * <td>|</td>
	 * <td>1</td>
	 * <td>1</td>
	 * <td>1</td>
	 * <td>1</td>
	 * <td>1</td>
	 * <td>|</td>
	 * </tr>
	 * <tr align="right">
	 * <td>|</td>
	 * <td>1</td>
	 * <td>1</td>
	 * <td>1</td>
	 * <td>1</td>
	 * <td>1</td>
	 * <td>|</td>
	 * </tr>
	 * <tr align="right">
	 * <td>|</td>
	 * <td>1</td>
	 * <td>1</td>
	 * <td>1</td>
	 * <td>1</td>
	 * <td>1</td>
	 * <td>|</td>
	 * </tr>
	 * <tr align="right">
	 * <td>|</td>
	 * <td>0</td>
	 * <td>1</td>
	 * <td>1</td>
	 * <td>1</td>
	 * <td>0</td>
	 * <td>|</td>
	 * </tr>
	 * </table>
	 */
	public static final int	STRUCTURE_EIGHT_CORNER	= 2;
	/**
	 * The type of morphology structure.
	 */
	protected int			type;

	/**
	 * Construct an instance of morphology operator.
	 * 
	 * @param type
	 *            the type of morphology structure
	 * @see #STRUCTURE_DIAMOND
	 * @see #STRUCTURE_EIGHT_CORNER
	 * @see #STRUCTURE_SQUARE
	 */
	protected Morph(int type)
	{
		this.type = type;
	}

	/**
	 * Erode the specified image {@code A} according to the morphology type
	 * {@code B}.
	 * 
	 * <pre>
	 * {@code S} = {@code A} &Theta; {@code B}
	 * {@code A}: the image to erode
	 * {@code B}: the morphology structure
	 * </pre>
	 * 
	 * @param image
	 *            the specified image to erode
	 * @return the image after erode operation
	 */
	public abstract T erode(T image);

	/**
	 * Dilate the specified image {@code A} according to the morphology type
	 * {@code B}.
	 * 
	 * <pre>
	 * {@code S} = {@code A} &oplus; {@code B}
	 * {@code A}: the image to dilate
	 * {@code B}: the morphology structure
	 * </pre>
	 * 
	 * @param image
	 *            the specified image to erode
	 */
	public abstract T dilate(T image);

	/**
	 * Perform open operation to the specified image.
	 * 
	 * <pre>
	 * {@code S} = {@code A} &Theta; {@code B}
	 * {@code S} = {@code S} &oplus; {@code B}
	 * </pre>
	 * <p>
	 * Open operation is the classic operation in morphology, which can remove
	 * useless pixels, such as the pepper noise. Open operation also can be used
	 * for avoiding pixel adhesions.
	 * </p>
	 * 
	 * @param image
	 *            the image to perform
	 * @return the image after open operation
	 */
	public abstract T open(T image);

	/**
	 * Perform close operation to the specified image.
	 * 
	 * <pre>
	 * {@code S} = {@code A} &oplus; {@code B}
	 * {@code S} = {@code S} &Theta; {@code B}
	 * </pre>
	 * <p>
	 * Close operation is the classic operation in morphology, which can fix the
	 * breaks of the lines or textures in the image.
	 * </p>
	 * 
	 * @param image
	 *            the image to perform
	 * @return the image after close operation
	 */
	public abstract T close(T image);

	/**
	 * Returns the specified morphology operator according to the specified
	 * image type.
	 * 
	 * @param c
	 *            the image type
	 * @param type
	 *            the type of morphology structure
	 * @return the morphology operator
	 * @see #STRUCTURE_DIAMOND
	 * @see #STRUCTURE_EIGHT_CORNER
	 * @see #STRUCTURE_SQUARE
	 */
	public static Morph getMorph(Class<? extends Image> c, int type)
	{
		if (c == BinaryImage.class)
			return new MorphBinary(type);
		if (c == GrayImage.class)
			return new MorphGray(type);
		if (c == ColorImage.class)
			return new MorphColor(type);
		throw new IllegalArgumentException(String.format(
				"Current morphology operator cannot support image type: %s",
				c.toString()));
	}

	/**
	 * Returns the specified morphology operator according to the specified
	 * image instance.
	 * 
	 * @param image
	 *            the specified image
	 * @param type
	 *            the type of morphology structure
	 * @return the morphology operator
	 * @see #STRUCTURE_DIAMOND
	 * @see #STRUCTURE_EIGHT_CORNER
	 * @see #STRUCTURE_SQUARE
	 */
	public static Morph getMorph(Image image, int type)
	{
		if (image instanceof BinaryImage)
			return new MorphBinary(type);
		if (image instanceof GrayImage)
			return new MorphGray(type);
		if (image instanceof ColorImage)
			return new MorphColor(type);
		throw new IllegalArgumentException(String.format(
				"Current morphology operator cannot support image type: %s",
				image.getClass().toString()));
	}
}
