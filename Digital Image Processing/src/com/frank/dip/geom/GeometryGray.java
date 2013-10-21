/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights reserved.
 * GeometryGray.java is PROPRIETARY/CONFIDENTIAL built in 2013.
 * Use is subject to license terms.
 */
package com.frank.dip.geom;

import java.awt.Rectangle;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

import com.frank.dip.GrayImage;

/**
 * The geometry operator for gray image.
 * <p>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class GeometryGray extends Geometry<GrayImage>
{
	/**
	 * Construct an instance of <tt>GeometryGray</tt>.
	 */
	public GeometryGray()
	{
		super(TYPE_BILINEAR, FILL_WITH_BLANK);
	}

	/**
	 * Construct an instance of <tt>GeometryGray</tt>.
	 * 
	 * @param type
	 *            the type of interpolation
	 * @param fillScheme
	 *            the edge filling scheme
	 */
	public GeometryGray(int type, int fillScheme)
	{
		super(type, fillScheme);
	}

	/**
	 * @see com.frank.dip.geom.Geometry#transform(com.frank.dip.Image,
	 *      com.frank.dip.geom.GeometryTransform)
	 */
	@Override
	public GrayImage transform(GrayImage image, GeometryTransform transform)
			throws NoninvertibleTransformException
	{
		int height = image.height();
		int width = image.width();
		Rectangle rect = transform.createTransformedShape(new Rectangle(width,
				height));
		GrayImage gi = new GrayImage(rect.width, rect.height);
		int xt, yt, p00, p10, p01, p11;
		float ex, ey, dx, dy;
		Point2D.Float p = new Point2D.Float();
		switch (type)
		{
			case TYPE_BILINEAR:
			default:
			{
				for (int x = 0; x < rect.width; x++)
					for (int y = 0; y < rect.height; y++)
					{
						p.setLocation(x + rect.x, y + rect.y);
						transform.inverseTransform(p, p);
						dx = p.x;
						dy = p.y;
						xt = (int) dx;
						yt = (int) dy;
						ex = dx - xt;
						ey = dy - yt;
						// manage the edge
						// -----------------------------------
						if ((xt > width - 1) || (xt < -1) || (yt > height - 1)
								|| (yt < -1))
						{
							if (fillScheme == FILL_WITH_WHITE)
								gi.setPixel(x, y, 255);
							continue;
						}
						if (xt >= width - 1)
							xt = width - 2;
						else if (xt < 0)
							xt = 0;
						if (yt >= height - 1)
							yt = height - 2;
						else if (yt < 0)
							yt = 0;
						// -----------------------------------
						p00 = image.getPixel(xt, yt);
						p01 = image.getPixel(xt, yt + 1);
						p10 = image.getPixel(xt + 1, yt);
						p11 = image.getPixel(xt + 1, yt + 1);
						gi.setPixel(x, y, Interpolation.bilinear(ex, ey, p00,
								p01, p10, p11));
					}
			}
				break;
			case TYPE_NEAREST_NEIGHBOR:
			{
				for (int x = 0; x < rect.width; x++)
					for (int y = 0; y < rect.height; y++)
					{
						p.setLocation(x + rect.x, y + rect.y);
						transform.inverseTransform(p, p);
						// manage the edge
						// -----------------------------------
						if ((p.x > width - 1) || (p.x < 0)
								|| (p.y > height - 1) || (p.y < 0))
						{
							if (fillScheme == FILL_WITH_WHITE)
								gi.setPixel(x, y, 255);
							continue;
						}
						// -----------------------------------
						gi.setPixel(x, y, image.getPixel(Math.round(p.x),
								Math.round(p.y)));
					}
			}
				break;
			case TYPE_BICUBIC:
			{
				for (int x = 0; x < rect.width; x++)
					for (int y = 0; y < rect.height; y++)
					{
						p.setLocation(x + rect.x, y + rect.y);
						transform.inverseTransform(p, p);
						dx = p.x;
						dy = p.y;
						xt = (int) dx;
						yt = (int) dy;
						ex = dx - xt;
						ey = dy - yt;
						// manage the edge
						// -----------------------------------
						if ((xt > width - 1) || (xt < -1) || (yt > height - 1)
								|| (yt < -1))
						{
							if (fillScheme == FILL_WITH_WHITE)
								gi.setPixel(x, y, 255);
							continue;
						}
						if (xt >= width - 1)
							xt = width - 2;
						else if (xt < 0)
							xt = 0;
						if (yt >= height - 1)
							yt = height - 2;
						else if (yt < 0)
							yt = 0;
						// -----------------------------------
						p00 = image.getPixel(xt, yt);
						p01 = image.getPixel(xt, yt + 1);
						p10 = image.getPixel(xt + 1, yt);
						p11 = image.getPixel(xt + 1, yt + 1);
						gi.setPixel(x, y, Interpolation.bicubic(ex, ey, p00,
								p01, p10, p11));
					}
			}
				break;
		}
		return gi;
	}
}
