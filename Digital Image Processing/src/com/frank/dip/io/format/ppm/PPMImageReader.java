/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved.
 * PPMImageReader.java is PROPRIETARY/CONFIDENTIAL built in 7:37:59 PM, Feb 25,
 * 2014.
 * Use is subject to license terms.
 */
package com.frank.dip.io.format.ppm;

import java.io.EOFException;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.stream.ImageInputStream;

import com.frank.dip.ColorImage;
import com.frank.dip.Image;
import com.frank.dip.io.ImageReader;

/**
 * The image reader of format "ppm".
 * <p>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class PPMImageReader extends ImageReader implements PPMFormat
{
	/**
	 * @see com.frank.dip.io.ImageReader#read(javax.imageio.stream.ImageInputStream)
	 */
	@Override
	public Image read(ImageInputStream iis) throws IOException
	{
		iis.mark();
		String type = iis.readLine();
		System.out.println(Arrays.toString(type.toCharArray()));
		if (type.equals(TYPE_P6))
		{
			String line = iis.readLine();
			StringBuilder description = new StringBuilder();
			while (line != null && line.startsWith("#"))
			{
				description.append(line.substring(1));
				line = iis.readLine();
			}
			boolean b = false;
			ColorImage ci = null;
			if (line != null)
				try
				{
					String[] s = line.split(" ");
					int width = Integer.valueOf(s[0]);
					int height = Integer.valueOf(s[1]);
					ci = new ColorImage(width, height);
					int max = Integer.valueOf(iis.readLine());
					if (max < 256)
					{
						byte[] buf = new byte[3];
						for (int y = 0; y < height; y++)
							for (int x = 0; x < width; x++)
								if (iis.read(buf) == -1)
								{
									x = width;
									y = height;
								}
								else
									ci.setPixel(x, y, 255, buf[0] & 0xff,
											buf[1] & 0xff, buf[2] & 0xff);
					}
					else
					{
						int red, green, blue;
						for (int y = 0; y < height; y++)
							for (int x = 0; x < width; x++)
								try
								{
									red = iis.readShort() & 0xffff;
									green = iis.readShort() & 0xffff;
									blue = iis.readInt() & 0xffff;
									ci.setPixel(x, y, 255, red, green, blue);
								}
								catch (EOFException e)
								{
									x = width;
									y = height;
								}
					}
				}
				catch (IOException e)
				{
					b = true;
				}
			else
				b = true;
			if (b)
				throw new IOException("The image completeness is damaged.");
			else
			{
				if (description.length() != 0)
					ci.setDescription(description.toString());
				return ci;
			}
		}
		else
		{
			iis.reset();
			throw new IOException("Current image format is not ppm(P6).");
		}
	}
}
