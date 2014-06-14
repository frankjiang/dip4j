/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved.
 * ImageDisplay.java is PROPRIETARY/CONFIDENTIAL built in 3:54:04 PM, Feb 28,
 * 2014.
 * Use is subject to license terms.
 */
package com.frank.dip.analyze;

import com.frank.dip.Image;

/**
 * The interface for a component which can display an image.
 * <p>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public interface ImageDisplay
{
	/**
	 * Copy the current image to clip board.
	 */
	public void copyImage();

	/**
	 * Refresh the image and update the title.
	 */
	public void refreshImage();

	/**
	 * Returns the source image in the component.
	 * 
	 * @return the source image.
	 */
	public Image getImage();

	/**
	 * Display a new image in the component.
	 * 
	 * @param newImage
	 *            the new image
	 * @param newTitle
	 *            the new title
	 */
	public void setImage(Image newImage, String newTitle);
}
