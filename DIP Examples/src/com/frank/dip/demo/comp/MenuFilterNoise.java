/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights reserved.
 * MenuFilterNoise.java is PROPRIETARY/CONFIDENTIAL built in 2013.
 * Use is subject to license terms.
 */
package com.frank.dip.demo.comp;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import com.frank.dip.Image;
import com.frank.dip.demo.DIPFrame;
import com.frank.dip.enhance.convolver.ArithmeticMeanKernel;
import com.frank.dip.enhance.convolver.Convolver;
import com.frank.dip.enhance.convolver.GeometricMeanKernel;
import com.frank.dip.enhance.convolver.HarmonicMeanKernel;
import com.frank.dip.enhance.convolver.Kernel;
import com.frank.sys.TestUtils;
import com.frank.sys.TestUtils.Timer;

/**
 * The noise filter menu loader.
 * <p>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class MenuFilterNoise extends MenuLoader
{
	/**
	 * Construct an instance of <tt>MenuFilterNoise</tt>.
	 * 
	 * @param dip
	 *            the digital image processing frame
	 * @param menu
	 *            the menu to for the new menu to add on, if <code>null</code>,
	 *            the new menu will be add on the root menu bar
	 */
	public MenuFilterNoise(DIPFrame dip, JMenu menu)
	{
		super(dip, menu);
	}

	/**
	 * @see com.frank.dip.demo.comp.MenuLoader#load(java.lang.Object[])
	 */
	@Override
	protected void load(Object... args)
	{
		JMenu mnNoiseFilter = new JMenu("Noise Filter");
		menu.add(mnNoiseFilter);
		JMenuItem mnArithmeticMean = new JMenu("Arithmetic Mean");
		mnNoiseFilter.add(mnArithmeticMean);
		JMenuItem mntmLinearSmooth9 = new JMenuItem("9");
		mntmLinearSmooth9.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.new Performance("Filter", "Arithmetic Mean 9")
				{
					@Override
					protected Image perform(Image image)
					{
						Kernel kernel = new ArithmeticMeanKernel(
								ArithmeticMeanKernel.KERNEL_9);
						Convolver conv = new Convolver(kernel);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = conv.operate(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnArithmeticMean.add(mntmLinearSmooth9);
		JMenuItem mntmLinearSmooth16 = new JMenuItem("16");
		mntmLinearSmooth16.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.new Performance("Filter", "Arithmetic Mean 16")
				{
					@Override
					protected Image perform(Image image)
					{
						Kernel kernel = new ArithmeticMeanKernel(
								ArithmeticMeanKernel.KERNEL_16);
						Convolver conv = new Convolver(kernel);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = conv.operate(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnArithmeticMean.add(mntmLinearSmooth16);
		JMenu mnGeometricMean = new JMenu("Geometric Mean");
		mnNoiseFilter.add(mnGeometricMean);
		JMenuItem mntmGeometricMean9 = new JMenuItem("9");
		mntmGeometricMean9.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.new Performance("Filter", "Geometric Mean 9")
				{
					@Override
					protected Image perform(Image image)
					{
						Kernel kernel = new GeometricMeanKernel(
								GeometricMeanKernel.KERNEL_9);
						Convolver conv = new Convolver(kernel);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = conv.operate(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnGeometricMean.add(mntmGeometricMean9);
		JMenuItem mntmGeometricMean16 = new JMenuItem("16");
		mntmGeometricMean16.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.new Performance("Filter", "Geometric Mean 16")
				{
					@Override
					protected Image perform(Image image)
					{
						Kernel kernel = new GeometricMeanKernel(
								GeometricMeanKernel.KERNEL_16);
						Convolver conv = new Convolver(kernel);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = conv.operate(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnGeometricMean.add(mntmGeometricMean16);
		JMenu mnHarmonicMean = new JMenu("Harmonic Mean");
		mnNoiseFilter.add(mnHarmonicMean);
		JMenuItem mntmHarmonicMean9 = new JMenuItem("9");
		mntmHarmonicMean9.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.new Performance("Filter", "Harmonic Mean 9")
				{
					@Override
					protected Image perform(Image image)
					{
						Kernel kernel = new HarmonicMeanKernel(
								HarmonicMeanKernel.KERNEL_9);
						Convolver conv = new Convolver(kernel);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = conv.operate(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnHarmonicMean.add(mntmHarmonicMean9);
		JMenuItem mntmHarmonicMean16 = new JMenuItem("16");
		mntmHarmonicMean16.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.new Performance("Filter", "Harmonic Mean 16")
				{
					@Override
					protected Image perform(Image image)
					{
						Kernel kernel = new HarmonicMeanKernel(
								HarmonicMeanKernel.KERNEL_16);
						Convolver conv = new Convolver(kernel);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = conv.operate(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnHarmonicMean.add(mntmHarmonicMean16);
	}
}
