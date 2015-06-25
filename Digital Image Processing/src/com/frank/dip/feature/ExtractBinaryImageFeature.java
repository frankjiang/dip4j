/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved.
 * BinaryFeature.java is PROPRIETARY/CONFIDENTIAL built in 4:29:18 PM, Apr 11,
 * 2014.
 * Use is subject to license terms.
 */
package com.frank.dip.feature;

import com.frank.dip.BinaryImage;
import com.frank.dip.GrayImage;
import com.frank.dip.Image;
import com.frank.dip.threshold.Thresholding;

/**
 * The feature extractor for a binary image feature.
 * <p>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public abstract class ExtractBinaryImageFeature extends FeatureExtractor<Image>
{
	/**
	 * The thresholding method of transfer a other color type image to binary
	 * image.
	 */
	protected Thresholding	thresholding;

	/**
	 * Construct an instance of <tt>ExtractBinaryImageFeature</tt>.
	 * 
	 * @param thresholding
	 *            the thresholding method
	 */
	public ExtractBinaryImageFeature(Thresholding thresholding)
	{
		this.thresholding = thresholding;
	}

	/**
	 * Returns thresholding method.
	 * 
	 * @return the thresholding method
	 */
	public Thresholding getThresholding()
	{
		return thresholding;
	}

	/**
	 * Set thresholding method.
	 * 
	 * @param thresholding
	 *            the value of thresholding method
	 */
	public void setThresholding(Thresholding thresholding)
	{
		this.thresholding = thresholding;
	}

	/**
	 * Extract the features from the binary images.
	 * 
	 * @param s
	 *            the specified sample
	 * @param bi
	 *            the boolean matrix of the binary image
	 * @param width
	 *            the width of the image
	 * @param height
	 *            the height of the image
	 * @param beginIndex
	 *            the begin index of the features to insert in
	 * @return the end index of the inserted features
	 */
	protected abstract int extract(Sample s, boolean[][] bi, int width,
			int height, int beginIndex);

	/**
	 * Check whether the image instance is valid.
	 * 
	 * @param image
	 *            the image instance to check
	 * @throws IllegalArgumentException
	 *             if the image instance is illegal
	 */
	protected abstract void checkValid(Image image)
			throws IllegalArgumentException;

	/**
	 * @see com.frank.dip.feature.FeatureExtractor#extract(com.frank.dip.feature.Sample,
	 *      com.frank.dip.Image, int)
	 */
	@Override
	public int extract(Sample s, Image image, int beginIndex)
	{
		// check the validity
		checkValid(image);
		// prepare binary image
		int width = image.getWidth();
		int height = image.getHeight();
		boolean[][] bi;
		if (image instanceof BinaryImage)
			bi = ((BinaryImage) image).getBinaryMatrix();
		else if (image instanceof GrayImage)
			bi = (thresholding.operate((GrayImage) image)).getBinaryMatrix();
		else
			bi = (thresholding.operate(new GrayImage(image))).getBinaryMatrix();
		return extract(s, bi, width, height, beginIndex);
	}
}
