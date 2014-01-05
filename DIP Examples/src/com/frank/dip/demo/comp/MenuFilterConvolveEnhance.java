/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights reserved.
 * MenuFilterConvolveEnhance.java is PROPRIETARY/CONFIDENTIAL built in 2013.
 * Use is subject to license terms.
 */
package com.frank.dip.demo.comp;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import com.frank.dip.Image;
import com.frank.dip.demo.DIPFrame;
import com.frank.dip.enhance.convolver.Kernel;
import com.frank.dip.enhance.convolver.KirschKernel;
import com.frank.dip.enhance.convolver.LaplacianKernel;
import com.frank.dip.enhance.convolver.PrewittKernel;
import com.frank.dip.enhance.convolver.RobertKernel;
import com.frank.dip.enhance.convolver.SobelKernel;
import com.frank.dip.enhance.time.ConvolveEnhance;
import com.frank.sys.TestUtils;
import com.frank.sys.TestUtils.Timer;

/**
 * The convolve enhance filter menu loader.
 * <p>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class MenuFilterConvolveEnhance extends MenuLoader
{
	/**
	 * Construct an instance of <tt>MenuFilterConvolveEnhance</tt>.
	 * 
	 * @param dip
	 *            the digital image processing frame
	 * @param menu
	 *            the menu to for the new menu to add on, if <code>null</code>,
	 *            the new menu will be add on the root menu bar
	 */
	public MenuFilterConvolveEnhance(DIPFrame dip, JMenu menu)
	{
		super(dip, menu);
	}

	/**
	 * @see com.frank.dip.demo.comp.MenuLoader#load(java.lang.Object[])
	 */
	@Override
	protected void load(Object... args)
	{
		JMenu mnConvolveEnhance = new JMenu("Convolve Enhance");
		menu.add(mnConvolveEnhance);
		JMenuItem mntmConvolveEnhanceLaplacian4 = new JMenuItem("Laplacian 4");
		mntmConvolveEnhanceLaplacian4.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.new Performance("Convolve Enhance", "Laplacian 4 Enhance")
				{
					@Override
					protected Image perform(Image image)
					{
						Kernel kernel = new LaplacianKernel(
								LaplacianKernel.KERNEL_4);
						ConvolveEnhance convolveEnhance = new ConvolveEnhance(
								kernel,
								ConvolveEnhance.HINT_ACCURACY_INTERRUPT,
								ConvolveEnhance.HINT_EDGE_SOURCE);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = convolveEnhance.operate(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnConvolveEnhance.add(mntmConvolveEnhanceLaplacian4);
		JMenuItem mntmConvolveEnhanceLaplacian8 = new JMenuItem("Laplacian 8");
		mntmConvolveEnhanceLaplacian8.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.new Performance("Convolve Enhance", "Laplacian 8 Enhance")
				{
					@Override
					protected Image perform(Image image)
					{
						Kernel kernel = new LaplacianKernel(
								LaplacianKernel.KERNEL_8);
						ConvolveEnhance convolveEnhance = new ConvolveEnhance(
								kernel,
								ConvolveEnhance.HINT_ACCURACY_INTERRUPT,
								ConvolveEnhance.HINT_EDGE_SOURCE);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = convolveEnhance.operate(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnConvolveEnhance.add(mntmConvolveEnhanceLaplacian8);
		JMenuItem mntmConvolveEnhanceLog = new JMenuItem("LoG");
		mntmConvolveEnhanceLog.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.new Performance("Convolve Enhance", "LoG Enhance")
				{
					@Override
					protected Image perform(Image image)
					{
						Kernel kernel = new LaplacianKernel(
								LaplacianKernel.KERNEL_8);
						ConvolveEnhance convolveEnhance = new ConvolveEnhance(
								kernel,
								ConvolveEnhance.HINT_ACCURACY_INTERRUPT,
								ConvolveEnhance.HINT_EDGE_SOURCE);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = convolveEnhance.operate(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnConvolveEnhance.add(mntmConvolveEnhanceLog);
		JMenuItem mntmConvolveEnhanceRobert = new JMenuItem("Robert");
		mntmConvolveEnhanceRobert.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.new Performance("Convolve Enhance", "Robert Enhance")
				{
					@Override
					protected Image perform(Image image)
					{
						Kernel kernel = new RobertKernel();
						ConvolveEnhance convolveEnhance = new ConvolveEnhance(
								kernel,
								ConvolveEnhance.HINT_ACCURACY_INTERRUPT,
								ConvolveEnhance.HINT_EDGE_SOURCE);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = convolveEnhance.operate(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnConvolveEnhance.add(mntmConvolveEnhanceRobert);
		JMenuItem mntmConvolveEnhanceSobel = new JMenuItem("Sobel");
		mntmConvolveEnhanceSobel.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.new Performance("Convolve Enhance", "Sobel Enhance")
				{
					@Override
					protected Image perform(Image image)
					{
						Kernel kernel = new SobelKernel();
						ConvolveEnhance convolveEnhance = new ConvolveEnhance(
								kernel,
								ConvolveEnhance.HINT_ACCURACY_INTERRUPT,
								ConvolveEnhance.HINT_EDGE_SOURCE);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = convolveEnhance.operate(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnConvolveEnhance.add(mntmConvolveEnhanceSobel);
		JMenuItem mntmConvolveEnhancePrewitt = new JMenuItem("Prewitt");
		mntmConvolveEnhancePrewitt.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.new Performance("Convolve Enhance", "Prewitt Enhance")
				{
					@Override
					protected Image perform(Image image)
					{
						Kernel kernel = new PrewittKernel();
						ConvolveEnhance convolveEnhance = new ConvolveEnhance(
								kernel,
								ConvolveEnhance.HINT_ACCURACY_INTERRUPT,
								ConvolveEnhance.HINT_EDGE_SOURCE);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = convolveEnhance.operate(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnConvolveEnhance.add(mntmConvolveEnhancePrewitt);
		JMenuItem mntmConvolveEnhanceKirsch = new JMenuItem("Kirsch");
		mntmConvolveEnhanceKirsch.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.new Performance("Convolve Enhance", "Kirsch Enhance")
				{
					@Override
					protected Image perform(Image image)
					{
						Kernel kernel = new KirschKernel();
						ConvolveEnhance convolveEnhance = new ConvolveEnhance(
								kernel,
								ConvolveEnhance.HINT_ACCURACY_INTERRUPT,
								ConvolveEnhance.HINT_EDGE_SOURCE);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = convolveEnhance.operate(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnConvolveEnhance.add(mntmConvolveEnhanceKirsch);
		
	}
}
