/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights reserved.
 * GeometryGray.java is PROPRIETARY/CONFIDENTIAL built in 2013.
 * Use is subject to license terms.
 */
package com.frank.dip.geom;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

import com.frank.dip.ColorImage;

/**
 * The geometry operator for color image.
 * <p>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class GeometryColor extends Geometry<ColorImage>
{
	/**
	 * Construct an instance of <tt>GeometryColor</tt>.
	 */
	public GeometryColor()
	{
		super(TYPE_BILINEAR, FILL_WITH_BLANK);
	}

	/**
	 * Construct an instance of <tt>GeometryColor</tt>.
	 * 
	 * @param type
	 *            the type of interpolation
	 * @param fillScheme
	 *            the edge filling scheme
	 */
	public GeometryColor(int type, int fillScheme)
	{
		super(type, fillScheme);
	}

	/**
	 * @see com.frank.dip.geom.Geometry#transform(com.frank.dip.Image,
	 *      com.frank.dip.geom.GeometryTransform)
	 */
	@Override
	public ColorImage transform(ColorImage image, GeometryTransform transform)
			throws NoninvertibleTransformException
	{
		int height = image.height();
		int width = image.width();
		Rectangle rect = transform.createTransformedShape(new Rectangle(width,
				height));
		ColorImage ci = new ColorImage(rect.width, rect.height);
		int xt, yt, p00, p10, p01, p11, alpha, red, green, blue;
		float ex, ey, dx, dy;
		Point2D.Float p = new Point2D.Float();
		final int BLACK = Color.black.getRGB(), WHITE = Color.white.getRGB();
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
							switch (fillScheme)
							{
								case FILL_WITH_BLANK:
								default:
									break;
								case FILL_WITH_BLACK:
									ci.setPixel(x, y, BLACK);
									break;
								case FILL_WITH_WHITE:
									ci.setPixel(x, y, WHITE);
									break;
							}
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
						// do interpolation in spite of alpha channel
						// alpha channel
						// -----------------------------------
						p00 = image.getAlpha(xt, yt);
						p01 = image.getAlpha(xt, yt + 1);
						p10 = image.getAlpha(xt + 1, yt);
						p11 = image.getAlpha(xt + 1, yt + 1);
						alpha = Interpolation.bilinear(ex, ey, p00, p01, p10,
								p11);
						// red channel
						// -----------------------------------
						p00 = image.getRed(xt, yt);
						p01 = image.getRed(xt, yt + 1);
						p10 = image.getRed(xt + 1, yt);
						p11 = image.getRed(xt + 1, yt + 1);
						red = Interpolation
								.bilinear(ex, ey, p00, p01, p10, p11);
						// green channel
						// -----------------------------------
						p00 = image.getGreen(xt, yt);
						p01 = image.getGreen(xt, yt + 1);
						p10 = image.getGreen(xt + 1, yt);
						p11 = image.getGreen(xt + 1, yt + 1);
						green = Interpolation.bilinear(ex, ey, p00, p01, p10,
								p11);
						// blue channel
						// -----------------------------------
						p00 = image.getBlue(xt, yt);
						p01 = image.getBlue(xt, yt + 1);
						p10 = image.getBlue(xt + 1, yt);
						p11 = image.getBlue(xt + 1, yt + 1);
						blue = Interpolation.bilinear(ex, ey, p00, p01, p10,
								p11);
						// set pixel
						ci.setPixel(x, y, alpha, red, green, blue);
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
							switch (fillScheme)
							{
								case FILL_WITH_BLANK:
								default:
									break;
								case FILL_WITH_BLACK:
									ci.setPixel(x, y, BLACK);
									break;
								case FILL_WITH_WHITE:
									ci.setPixel(x, y, WHITE);
									break;
							}
							continue;
						}
						// -----------------------------------
						ci.setPixel(x, y, image.getPixel(Math.round(p.x),
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
							switch (fillScheme)
							{
								case FILL_WITH_BLANK:
								default:
									break;
								case FILL_WITH_BLACK:
									ci.setPixel(x, y, BLACK);
									break;
								case FILL_WITH_WHITE:
									ci.setPixel(x, y, WHITE);
									break;
							}
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
						// do interpolation in spite of alpha channel
						// alpha channel
						// -----------------------------------
						p00 = image.getAlpha(xt, yt);
						p01 = image.getAlpha(xt, yt + 1);
						p10 = image.getAlpha(xt + 1, yt);
						p11 = image.getAlpha(xt + 1, yt + 1);
						alpha = Interpolation.bicubic(ex, ey, p00, p01, p10,
								p11);
						// red channel
						// -----------------------------------
						p00 = image.getRed(xt, yt);
						p01 = image.getRed(xt, yt + 1);
						p10 = image.getRed(xt + 1, yt);
						p11 = image.getRed(xt + 1, yt + 1);
						red = Interpolation.bicubic(ex, ey, p00, p01, p10, p11);
						// green channel
						// -----------------------------------
						p00 = image.getGreen(xt, yt);
						p01 = image.getGreen(xt, yt + 1);
						p10 = image.getGreen(xt + 1, yt);
						p11 = image.getGreen(xt + 1, yt + 1);
						green = Interpolation.bicubic(ex, ey, p00, p01, p10,
								p11);
						// blue channel
						// -----------------------------------
						p00 = image.getBlue(xt, yt);
						p01 = image.getBlue(xt, yt + 1);
						p10 = image.getBlue(xt + 1, yt);
						p11 = image.getBlue(xt + 1, yt + 1);
						blue = Interpolation
								.bicubic(ex, ey, p00, p01, p10, p11);
						// set pixel
						ci.setPixel(x, y, alpha, red, green, blue);
					}
			}
				break;
		}
		return ci;
	}
}
