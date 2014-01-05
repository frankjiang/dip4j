/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights reserved.
 * MenuGeometry.java is PROPRIETARY/CONFIDENTIAL built in 2013.
 * Use is subject to license terms.
 */
package com.frank.dip.demo.comp;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import com.frank.dip.ColorImage;
import com.frank.dip.GrayImage;
import com.frank.dip.Image;
import com.frank.dip.demo.ChannelGrabber;
import com.frank.dip.demo.DIPFrame;
import com.frank.dip.demo.ImageDisplayDialog;
import com.frank.dip.enhance.convolver.Convolver;
import com.frank.dip.enhance.convolver.LoGKernel;
import com.frank.dip.geom.AffineTransform;
import com.frank.dip.geom.Geometry;
import com.frank.dip.geom.GeometryColor;
import com.frank.swing.SwingUtils;
import com.frank.sys.TestUtils;
import com.frank.sys.TestUtils.Timer;

/**
 * The geometry menu.
 * <p>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class MenuGeometry extends MenuLoader
{
	/**
	 * Construct an instance of <tt>MenuGeometry</tt>.
	 * 
	 * @param dip
	 *            the digital image processing frame
	 */
	public MenuGeometry(DIPFrame dip)
	{
		super(dip, null);
	}

	/**
	 * @see com.frank.dip.demo.comp.MenuLoader#load(java.lang.Object[])
	 */
	@Override
	protected void load(Object... args)
	{
		JMenu mnGeometry = new JMenu("Geometry(G)");
		dip.getBar().add(mnGeometry);
		JMenuItem mntmScale = new JMenuItem("Scale");
		mntmScale.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.new Performance("Geometry", "Scale")
				{
					@Override
					protected Image perform(Image image)
					{
						Integer type = MenuUtils.selectInterpolationType(dip);
						if (type == null)
							return null;
						String s = SwingUtils.inputDialog(dip, "Scale",
								"Input the scale ratio:");
						Double v = null;
						try
						{
							v = Double.valueOf(s);
						}
						catch (NumberFormatException e1)
						{
							return null;
						}
						Geometry g = Geometry.getGeometry(image.getClass(),
								type, Geometry.FILL_WITH_BLANK);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = null;
						res = g.scaleByRate(image, v);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnGeometry.add(mntmScale);
		JMenuItem mntmRotate = new JMenuItem("Rotate");
		mntmRotate.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.new Performance("Geometry", "Rotate")
				{
					@Override
					protected Image perform(Image image)
					{
						Integer type = MenuUtils.selectInterpolationType(dip);
						if (type == null)
							return null;
						String s = SwingUtils.inputDialog(dip, "Rotate",
								"Input rotate angle in degree:");
						if (s == null)
							return null;
						Double v = null;
						try
						{
							v = Double.valueOf(s);
						}
						catch (NumberFormatException e1)
						{
							SwingUtils.errorMessage(
									dip,
									String.format(
											"%s is not a number(neither float or integer)",
											s));
							return null;
						}
						Geometry geom = Geometry.getGeometry(image, type,
								Geometry.FILL_WITH_BLANK);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = null;
						res = geom.rotateByDegree(image, v);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnGeometry.add(mntmRotate);
		JMenuItem mntmAffineTransform = new JMenuItem("Affine Transform");
		mntmAffineTransform.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.new Performance("Geometry", "Scale")
				{
					@Override
					protected Image perform(Image image)
					{
						Integer type = MenuUtils.selectInterpolationType(dip);
						if (type == null)
							return null;
						String s = SwingUtils
								.inputDialog(
										dip,
										"Input Kernel",
										"Input six kernel values in the order of\r\n"
												+ "m00, m10, m01, m11, m02, m12\r\n"
												+ "seperate values with ASCII common\",\":\r\n"
												+ "e.g. \"1,0,0,1,0,0\"");
						if (s == null)
							return null;
						String[] ks = s.split(",|\uff0c");
						AffineTransform transform = null;
						try
						{
							transform = new AffineTransform(Double
									.valueOf(ks[0]), Double.valueOf(ks[1]),
									Double.valueOf(ks[2]), Double
											.valueOf(ks[3]), Double
											.valueOf(ks[4]), Double
											.valueOf(ks[5]));
						}
						catch (Exception e)
						{
							SwingUtils.errorMessage(dip, String.format(
									"Incorrect format for \"%s\".", s));
							return null;
						}
						Geometry geom = Geometry.getGeometry(image, type,
								Geometry.FILL_WITH_BLANK);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = null;
						try
						{
							res = geom.transform(image, transform);
						}
						catch (NoninvertibleTransformException e)
						{
							e.printStackTrace();
							SwingUtils.errorMessage(
									dip,
									String.format(
											"The affine transform defined contains no inverse transform: %s",
											e.getLocalizedMessage()));
							return null;
						}
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnGeometry.add(mntmAffineTransform);
		JMenuItem mntmSpatialTransform = new JMenuItem("Spatial Transform");
		mntmSpatialTransform.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.new Performance("Geometry", "Scale")
				{
					@Override
					protected Image perform(Image image)
					{
						String s = SwingUtils
								.inputDialog(
										dip,
										"Input Anchors",
										"Input four anchors from\r\n"
												+ "left-top to left-bottom clockwise\r\n"
												+ "and seperate coordinates with ASCII common\",\"\r\n"
												+ "anchors with spliter \"|\" or ASCII colon\":\"\r\n"
												+ "e.g. \"1,2:3,4|5,6:7,8\"");
						if (s == null)
							return null;
						String[] ps = s.split(":|\\|");
						String[] xy;
						Point2D p00, p10, p11, p01;
						try
						{
							xy = ps[0].split(",|\uff0c");
							p00 = new Point2D.Double(Double.valueOf(xy[0]),
									Double.valueOf(xy[1]));
							xy = ps[1].split(",|\uff0c");
							p10 = new Point2D.Double(Double.valueOf(xy[0]),
									Double.valueOf(xy[1]));
							xy = ps[2].split(",|\uff0c");
							p11 = new Point2D.Double(Double.valueOf(xy[0]),
									Double.valueOf(xy[1]));
							xy = ps[3].split(",|\uff0c");
							p01 = new Point2D.Double(Double.valueOf(xy[0]),
									Double.valueOf(xy[1]));
						}
						catch (Exception e)
						{
							SwingUtils.errorMessage(dip, String.format(
									"Incorrect format for \"%s\".", s));
							return null;
						}
						Integer type = MenuUtils.selectInterpolationType(dip);
						if (type == null)
							return null;
						Geometry geom = Geometry.getGeometry(image, type,
								Geometry.FILL_WITH_BLANK);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = null;
						res = geom.spatialTransform(image, p00, p01, p11, p10);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnGeometry.add(mntmSpatialTransform);
		JMenu mnExperimentp = new JMenu("Experiment(P)");
		mnExperimentp.setMnemonic('P');
		dip.getBar().add(mnExperimentp);
		JMenuItem mntm3ChannelsEdge = new JMenuItem("3 Channels Edge Detect");
		mntm3ChannelsEdge.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.new Performance("Test", "3 Channels Edge Detect")
				{
					@Override
					protected Image perform(Image image)
					{
						ColorImage ci = null;
						if (image instanceof ColorImage)
							ci = (ColorImage) image;
						else
						{
							SwingUtils.errorMessage(dip, "Not a color image!");
							return null;
						}
						if (ci.width() > 256 && ci.height() > 256)
							ci = new GeometryColor().scale(ci, 256);
						GrayImage red = ChannelGrabber.grab(ci,
								ChannelGrabber.RED);
						GrayImage green = ChannelGrabber.grab(ci,
								ChannelGrabber.GREEN);
						GrayImage blue = ChannelGrabber.grab(ci,
								ChannelGrabber.BLUE);
						GrayImage gray = ChannelGrabber.grab(ci,
								ChannelGrabber.GRAY);
						Convolver<GrayImage> conv = new Convolver<GrayImage>(
								new LoGKernel());
						GrayImage red_c = conv.operate(red);
						GrayImage green_c = conv.operate(green);
						GrayImage blue_c = conv.operate(blue);
						GrayImage gray_c = conv.operate(gray);
						java.awt.Window window = dip;
						boolean modal = false;
						Dimension screen = Toolkit.getDefaultToolkit()
								.getScreenSize();
						int x = 0, y = 0, dx = screen.width / 4, dy = screen.height / 2;
						// source image displaying
						ImageDisplayDialog red_d = new ImageDisplayDialog(
								window, "Red Channel", modal, red);
						dip.addWindow(red_d);
						red_d.setLocation(x, y);
						x += dx;
						ImageDisplayDialog green_d = new ImageDisplayDialog(
								window, "Green Channel", modal, green);
						dip.addWindow(green_d);
						green_d.setLocation(x, y);
						x += dx;
						ImageDisplayDialog blue_d = new ImageDisplayDialog(
								window, "Blue Channel", modal, blue);
						dip.addWindow(blue_d);
						blue_d.setLocation(x, y);
						x += dx;
						ImageDisplayDialog gray_d = new ImageDisplayDialog(
								window, "Gray Image", modal, gray);
						dip.addWindow(gray_d);
						gray_d.setLocation(x, y);
						x += dx;
						// convolved image displaying
						x = 0;
						y += dy;
						ImageDisplayDialog red_c_d = new ImageDisplayDialog(
								window, "Convolve Red", modal, red_c);
						dip.addWindow(red_c_d);
						dip.addWindow(red_c_d);
						red_c_d.setLocation(x, y);
						x += dx;
						ImageDisplayDialog green_c_d = new ImageDisplayDialog(
								window, "Convolve Green", modal, green_c);
						dip.addWindow(green_c_d);
						green_c_d.setLocation(x, y);
						x += dx;
						ImageDisplayDialog blue_c_d = new ImageDisplayDialog(
								window, "Convolve Blue", modal, blue_c);
						dip.addWindow(blue_c_d);
						blue_c_d.setLocation(x, y);
						x += dx;
						ImageDisplayDialog gray_c_d = new ImageDisplayDialog(
								window, "Convolve Gray", modal, gray_c);
						dip.addWindow(gray_c_d);
						gray_c_d.setLocation(x, y);
						x += dx;
						// set visibilities
						boolean b = true;
						red_d.setVisible(b);
						green_d.setVisible(b);
						blue_d.setVisible(b);
						gray_d.setVisible(b);
						red_c_d.setVisible(b);
						green_c_d.setVisible(b);
						blue_c_d.setVisible(b);
						gray_c_d.setVisible(b);
						return null;
					}
				}.perform();
			}
		});
		mnExperimentp.add(mntm3ChannelsEdge);
	}
}
