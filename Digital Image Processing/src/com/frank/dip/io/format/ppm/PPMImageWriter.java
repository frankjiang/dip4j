/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved.
 * PPMImageWriter.java is PROPRIETARY/CONFIDENTIAL built in 8:05:33 PM, Feb 25,
 * 2014.
 * Use is subject to license terms.
 */
package com.frank.dip.io.format.ppm;

import java.io.IOException;
import java.util.StringTokenizer;

import javax.imageio.stream.ImageOutputStream;

import com.frank.dip.Image;
import com.frank.dip.io.ImageWriter;

/**
 * The image writer of format "ppm".
 * <p>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class PPMImageWriter extends ImageWriter implements PPMFormat
{
	/**
	 * @see com.frank.dip.io.ImageWriter#write(javax.imageio.stream.ImageOutputStream,
	 *      com.frank.dip.Image)
	 */
	@Override
	public void write(ImageOutputStream ios, Image image) throws IOException
	{
		String s = image.getDescription();
		String[] strs = null;
		if (s != null)
		{
			StringTokenizer st = new StringTokenizer(s, "\r\n\t\f");//$NON-NLS-1$
			strs = new String[st.countTokens()];
			int i = 0;
			while (st.hasMoreTokens())
				strs[i++] = st.nextToken();
		}
		ios.writeBytes(TYPE_P6);
		ios.writeBytes("\r\n");
		if (strs != null)
			for (int i = 0; i < strs.length; i++)
			{
				ios.writeByte('#');
				ios.writeBytes(strs[i]);
				ios.write('\n');
			}
		int width = image.width();
		int height = image.height();
		ios.writeBytes(String.format("%d %d\n", width, height));//$NON-NLS-1$
		ios.writeBytes("255\n");//$NON-NLS-1$
		int rgb;
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
			{
				rgb = image.getRGB(x, y);
				ios.writeByte(rgb >> 16);
				ios.writeByte(rgb >> 8);
				ios.write(rgb);
			}
	}
}
