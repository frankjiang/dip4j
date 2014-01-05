/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights reserved.
 * MenuFilterSharpen.java is PROPRIETARY/CONFIDENTIAL built in 2013.
 * Use is subject to license terms.
 */
package com.frank.dip.demo.comp;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import com.frank.dip.Image;
import com.frank.dip.demo.DIPFrame;
import com.frank.dip.enhance.convolver.Convolver;
import com.frank.dip.enhance.convolver.Kernel;
import com.frank.dip.enhance.convolver.KirschKernel;
import com.frank.dip.enhance.convolver.LaplacianKernel;
import com.frank.dip.enhance.convolver.LoGKernel;
import com.frank.dip.enhance.convolver.PrewittKernel;
import com.frank.dip.enhance.convolver.RobertKernel;
import com.frank.dip.enhance.convolver.SobelKernel;
import com.frank.sys.TestUtils;
import com.frank.sys.TestUtils.Timer;

/**
 * The sharpen filter menu loader.
 * <p>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class MenuFilterSharpen extends MenuLoader
{
	/**
	 * Construct an instance of <tt>MenuFilterSharpen</tt>.
	 * 
	 * @param dip
	 *            the digital image processing frame
	 * @param menu
	 *            the menu to for the new menu to add on, if <code>null</code>,
	 *            the new menu will be add on the root menu bar
	 */
	public MenuFilterSharpen(DIPFrame dip, JMenu menu)
	{
		super(dip, menu);
	}

	/**
	 * @see com.frank.dip.demo.comp.MenuLoader#load(java.lang.Object[])
	 */
	@Override
	protected void load(Object... args)
	{
		JMenu mnSharpen = new JMenu("Sharpen");
		menu.add(mnSharpen);
		JMenuItem mntmSharpenLaplacian4 = new JMenuItem("Laplacian 4");
		mntmSharpenLaplacian4.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.new Performance("Filter", "Laplace Sharpen 4")
				{
					@Override
					protected Image perform(Image image)
					{
						Kernel kernel = new LaplacianKernel(
								LaplacianKernel.KERNEL_4);
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
		mnSharpen.add(mntmSharpenLaplacian4);
		JMenuItem mntmSharpenLaplacian8 = new JMenuItem("Laplacian 8");
		mntmSharpenLaplacian8.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.new Performance("Filter", "Laplace Sharpen 8")
				{
					@Override
					protected Image perform(Image image)
					{
						Kernel kernel = new LaplacianKernel(
								LaplacianKernel.KERNEL_8);
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
		mnSharpen.add(mntmSharpenLaplacian8);
		JMenuItem mntmSharpenLoG = new JMenuItem("LoG");
		mntmSharpenLoG.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.new Performance("Filter", "LoG Sharpen")
				{
					@Override
					protected Image perform(Image image)
					{
						Kernel kernel = new LoGKernel();
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
		mnSharpen.add(mntmSharpenLoG);
		JMenuItem mntmSharpenRobert = new JMenuItem("Robert");
		mntmSharpenRobert.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.new Performance("Filter", "Robert Sharpen")
				{
					@Override
					protected Image perform(Image image)
					{
						Kernel kernel = new RobertKernel();
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
		mnSharpen.add(mntmSharpenRobert);
		JMenuItem mntmSharpenSobel = new JMenuItem("Sobel");
		mntmSharpenSobel.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.new Performance("Filter", "Sobel Sharpen")
				{
					@Override
					protected Image perform(Image image)
					{
						Kernel kernel = new SobelKernel();
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
		mnSharpen.add(mntmSharpenSobel);
		JMenuItem mntmSharpenPrewitt = new JMenuItem("Prewitt Sharpen");
		mntmSharpenPrewitt.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.new Performance("Filter", "content")
				{
					@Override
					protected Image perform(Image image)
					{
						Kernel kernel = new PrewittKernel();
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
		mnSharpen.add(mntmSharpenPrewitt);
		JMenuItem mntmSharpenKirsch = new JMenuItem("Kirsch");
		mntmSharpenKirsch.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.new Performance("Filter", "Kirsch Sharpen")
				{
					@Override
					protected Image perform(Image image)
					{
						Kernel kernel = new KirschKernel();
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
		mnSharpen.add(mntmSharpenKirsch);
	}
}
