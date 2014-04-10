/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved.
 * MenuExperiment.java is PROPRIETARY/CONFIDENTIAL built in 6:06:21 PM, Apr 10,
 * 2014.
 * Use is subject to license terms.
 */
package com.frank.dip.demo.comp;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.TimeUnit;

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
import com.frank.dip.feature.util.RegionExtractor;
import com.frank.dip.geom.GeometryColor;
import com.frank.swing.SwingUtils;
import com.frank.sys.TestUtils;
import com.frank.sys.TestUtils.Timer;

/**
 * Menu experiment.
 * <p>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class MenuExperiment extends MenuLoader
{
	/**
	 * Construct an instance of <tt>MenuExperiment</tt>.
	 * 
	 * @param dip
	 */
	public MenuExperiment(DIPFrame dip)
	{
		super(dip, null);
	}

	/**
	 * @see com.frank.dip.demo.comp.MenuLoader#load(java.lang.Object[])
	 */
	@Override
	protected void load(Object... args)
	{
		JMenu mnExperiment = new JMenu("Experiment(P)");
		mnExperiment.setMnemonic('P');
		dip.getBar().add(mnExperiment);
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
		mnExperiment.add(mntm3ChannelsEdge);
		JMenuItem mntmRegionExtract = new JMenuItem("Region Extract");
		mntmRegionExtract.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.new Performance("Test", "Region Extract")
				{
					@Override
					protected Image perform(Image image)
					{
						RegionExtractor re = new RegionExtractor(Color.BLACK,
								16, 16);
						Timer t = TestUtils.getTimer();
						Image result = re.operate(image);
						System.out.println(result);
						time = t.getTime(TimeUnit.MILLISECONDS);
						return result;
					}
				}.perform();
			}
		});
		mnExperiment.add(mntmRegionExtract);
	}
}
