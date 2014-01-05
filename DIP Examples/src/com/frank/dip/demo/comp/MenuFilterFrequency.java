/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights reserved.
 * MenuFilterFrequency.java is PROPRIETARY/CONFIDENTIAL built in 2013.
 * Use is subject to license terms.
 */
package com.frank.dip.demo.comp;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import com.frank.dip.Image;
import com.frank.dip.analyze.FunctionDislpayDialog;
import com.frank.dip.demo.DIPFrame;
import com.frank.dip.enhance.frequency.AbstractFourierFilter;
import com.frank.dip.enhance.frequency.FourierDistanceFunctionFilter;
import com.frank.dip.enhance.frequency.FourierLaplacianFilter;
import com.frank.dip.enhance.frequency.HomomorphicFilter;
import com.frank.dip.math.ButternworthFunction;
import com.frank.dip.math.CoefGaussFunction;
import com.frank.dip.math.EnhanceFilterFunction;
import com.frank.dip.math.Function;
import com.frank.dip.math.GaussFunction;
import com.frank.dip.math.HomomorphicEnhanceFilterFunction;
import com.frank.dip.math.Radius;
import com.frank.dip.math.ScalableFunction;
import com.frank.sys.TestUtils;
import com.frank.sys.TestUtils.Timer;

/**
 * The frequency filter.
 * <p>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class MenuFilterFrequency extends MenuLoader
{
	/**
	 * Construct an instance of <tt>MenuFilterFrequency</tt>.
	 * 
	 * @param dip
	 *            the digital image processing frame
	 * @param menu
	 *            the menu to for the new menu to add on, if <code>null</code>,
	 *            the new menu will be add on the root menu bar
	 */
	public MenuFilterFrequency(DIPFrame dip, JMenu menu)
	{
		super(dip, menu);
	}

	/**
	 * @see com.frank.dip.demo.comp.MenuLoader#load(java.lang.Object[])
	 */
	@Override
	protected void load(Object... args)
	{
		//
		// Fourier high boost filters
		//
		JMenu mnFrequencyFilter = new JMenu("Frequency Filter");
		menu.add(mnFrequencyFilter);
		// Fourier filter: radius low pass
		JMenuItem mntmRadiusFourierLPF = new JMenuItem("Radius Fourier LPF");
		mntmRadiusFourierLPF.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.new Performance("Filter", "Radius Fourier Low Pass Filter")
				{
					@Override
					protected Image perform(Image image)
					{
						Function function = new Radius(image.width() * 0.05,
								true);
						Properties properties = function.getProperties();
						properties.put(rs(Radius.PARAM_D, true),
								image.width() > image.height() ? image.width()
										: image.height());
						properties.put(rs(Radius.PARAM_D, false), 0);
						FunctionDislpayDialog dialog = new FunctionDislpayDialog(
								null, "Radius Low Pass",
								new String[] { Radius.PARAM_D }, properties,
								function);
						dialog.setXAxisString("D(u,v)");
						dialog.setYAxisString("H(u,v)");
						dialog.setVisible(true);
						Properties p = dialog.getProperties();
						if (p == null)
							return null;
						function.setProperties(p);
						AbstractFourierFilter filter = new FourierDistanceFunctionFilter(
								function);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = filter.filter(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnFrequencyFilter.add(mntmRadiusFourierLPF);
		// Fourier filter: radius high pass
		JMenuItem mntmRadiusFourierHPF = new JMenuItem("Radius Fourier HPF");
		mntmRadiusFourierHPF.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.new Performance("Filter", "Radius Fourier High Pass Filter")
				{
					@Override
					protected Image perform(Image image)
					{
						Function function = new Radius(image.width() * 0.05,
								false);
						Properties properties = function.getProperties();
						properties.put(rs(Radius.PARAM_D, true),
								image.width() > image.height() ? image.width()
										: image.height());
						properties.put(rs(Radius.PARAM_D, false), 0);
						FunctionDislpayDialog dialog = new FunctionDislpayDialog(
								null, "Radius High Pass",
								new String[] { Radius.PARAM_D }, properties,
								function);
						dialog.setXAxisString("D(u,v)");
						dialog.setYAxisString("H(u,v)");
						dialog.setVisible(true);
						Properties p = dialog.getProperties();
						if (p == null)
							return null;
						function.setProperties(p);
						AbstractFourierFilter filter = new FourierDistanceFunctionFilter(
								function);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = filter.filter(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnFrequencyFilter.add(mntmRadiusFourierHPF);
		// Fourier filter: Butternworth low pass
		JMenuItem mntmButternworthLPF = new JMenuItem("Butternworth LPF");
		mntmButternworthLPF.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.new Performance("Filter", "Butternworth Low Pass Filter")
				{
					@Override
					protected Image perform(Image image)
					{
						ScalableFunction function = new ButternworthFunction(
								10, 1, 255, true);
						Properties properties = function.getProperties();
						properties.put(rs(ButternworthFunction.PARAM_D, true),
								1000);
						properties.put(rs(ButternworthFunction.PARAM_D, false),
								Double.MIN_VALUE);
						properties.put(rs(ButternworthFunction.PARAM_N, true),
								10);
						properties.put(rs(ButternworthFunction.PARAM_N, false),
								0);
						FunctionDislpayDialog dialog = new FunctionDislpayDialog(
								null, "Butternworth Low Pass", new String[] {
										ButternworthFunction.PARAM_D,
										ButternworthFunction.PARAM_N },
								properties, function);
						dialog.setXAxisString("D(u,v)");
						dialog.setYAxisString("H(u,v)");
						dialog.setVisible(true);
						Properties p = dialog.getProperties();
						if (p == null)
							return null;
						function.setProperties(p);
						AbstractFourierFilter filter = new FourierDistanceFunctionFilter(
								function);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = filter.filter(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnFrequencyFilter.add(mntmButternworthLPF);
		// Fourier filter: Butternworth high pass
		JMenuItem mntmButternworthHPF = new JMenuItem("Butternworth HPF");
		mntmButternworthHPF.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.new Performance("Filter", "Butternworth High Pass Filter")
				{
					@Override
					protected Image perform(Image image)
					{
						ScalableFunction function = new ButternworthFunction(
								10, 1, 255, false);
						Properties properties = function.getProperties();
						properties.put(rs(ButternworthFunction.PARAM_D, true),
								1000);
						properties.put(rs(ButternworthFunction.PARAM_D, false),
								Double.MIN_VALUE);
						properties.put(rs(ButternworthFunction.PARAM_N, true),
								10);
						properties.put(rs(ButternworthFunction.PARAM_N, false),
								0);
						FunctionDislpayDialog dialog = new FunctionDislpayDialog(
								null, "Butternworth High Pass", new String[] {
										ButternworthFunction.PARAM_D,
										ButternworthFunction.PARAM_N },
								properties, function);
						dialog.setXAxisString("D(u,v)");
						dialog.setYAxisString("H(u,v)");
						dialog.setVisible(true);
						Properties p = dialog.getProperties();
						if (p == null)
							return null;
						function.setProperties(p);
						AbstractFourierFilter filter = new FourierDistanceFunctionFilter(
								function);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = filter.filter(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnFrequencyFilter.add(mntmButternworthHPF);
		// Fourier filter: Gauss low pass
		JMenuItem mntmGaussLPF = new JMenuItem("Gauss LPF");
		mntmGaussLPF.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.new Performance("Filter", "Gauss Low Pass Filter")
				{
					@Override
					protected Image perform(Image image)
					{
						ScalableFunction function = new GaussFunction(1.0, 255,
								true);
						Properties properties = function.getProperties();
						properties.put(rs(GaussFunction.PARAM_SIGMA, true),
								1000);
						properties.put(rs(GaussFunction.PARAM_SIGMA, false),
								Double.MIN_VALUE);
						FunctionDislpayDialog dialog = new FunctionDislpayDialog(
								null, "Gauss Low Pass",
								new String[] { GaussFunction.PARAM_SIGMA },
								properties, function);
						dialog.setXAxisString("D(u,v)");
						dialog.setYAxisString("H(u,v)");
						dialog.setVisible(true);
						Properties p = dialog.getProperties();
						if (p == null)
							return null;
						function.setProperties(p);
						AbstractFourierFilter filter = new FourierDistanceFunctionFilter(
								function);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = filter.filter(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnFrequencyFilter.add(mntmGaussLPF);
		// Fourier filter: Gauss high pass
		JMenuItem mntmGaussHPF = new JMenuItem("Gauss HPF");
		mntmGaussHPF.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.new Performance("Filter", "Gauss High Pass Filter")
				{
					@Override
					protected Image perform(Image image)
					{
						ScalableFunction function = new GaussFunction(1.0, 255,
								false);
						Properties properties = function.getProperties();
						properties.put(rs(GaussFunction.PARAM_SIGMA, true),
								1000);
						properties.put(rs(GaussFunction.PARAM_SIGMA, false),
								Double.MIN_VALUE);
						FunctionDislpayDialog dialog = new FunctionDislpayDialog(
								null, "Gauss High Pass",
								new String[] { GaussFunction.PARAM_SIGMA },
								properties, function);
						dialog.setXAxisString("D(u,v)");
						dialog.setYAxisString("H(u,v)");
						dialog.setVisible(true);
						Properties p = dialog.getProperties();
						if (p == null)
							return null;
						function.setProperties(p);
						AbstractFourierFilter filter = new FourierDistanceFunctionFilter(
								function);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = filter.filter(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnFrequencyFilter.add(mntmGaussHPF);
		// Fourier filter: Laplacian sharpen
		JMenuItem mntmFourierLaplacian = new JMenuItem("Fourier Laplacian");
		mntmFourierLaplacian.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.new Performance("Filter", "Fourier Laplacian Filter")
				{
					@Override
					protected Image perform(Image image)
					{
						AbstractFourierFilter filter = new FourierLaplacianFilter();
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = filter.filter(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnFrequencyFilter.add(mntmFourierLaplacian);
		//
		// Fourier high boost filters
		//
		JMenu mnHighBoostFilter = new JMenu("High Boost Filter");
		mnHighBoostFilter.setToolTipText("High Frequency Boost Filter");
		mnFrequencyFilter.add(mnHighBoostFilter);
		// High boost filter: radius
		JMenuItem mntmHighBoostFilterRadius = new JMenuItem("Radius");
		mntmHighBoostFilterRadius.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.new Performance("Filter", "Radius High Boost Filter")
				{
					@Override
					protected Image perform(Image image)
					{
						Function func2 = new Radius(image.width() * 0.05, false);
						EnhanceFilterFunction function = new EnhanceFilterFunction(
								func2, 1.0, 1.0);
						Properties properties = function.getProperties();
						properties.put(rs(Radius.PARAM_D, true),
								image.width() > image.height() ? image.width()
										: image.height());
						properties.put(rs(Radius.PARAM_D, false), 0);
						properties.put(rs(EnhanceFilterFunction.PARAM_A, true),
								3);
						properties.put(
								rs(EnhanceFilterFunction.PARAM_A, false), 0);
						properties.put(rs(EnhanceFilterFunction.PARAM_B, true),
								10);
						properties.put(
								rs(EnhanceFilterFunction.PARAM_B, false),
								Double.MIN_VALUE);
						FunctionDislpayDialog dialog = new FunctionDislpayDialog(
								null, "Radius High Pass", new String[] {
										Radius.PARAM_D,
										EnhanceFilterFunction.PARAM_A,
										EnhanceFilterFunction.PARAM_B },
								properties, function);
						dialog.setXAxisString("D(u,v)");
						dialog.setYAxisString("H(u,v)");
						dialog.setVisible(true);
						Properties p = dialog.getProperties();
						if (p == null)
							return null;
						function.setProperties(p);
						AbstractFourierFilter filter = new FourierDistanceFunctionFilter(
								function);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = filter.filter(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnHighBoostFilter.add(mntmHighBoostFilterRadius);
		// High boost filter: Butternworth
		JMenuItem mntmHighBoostFilterButternworth = new JMenuItem(
				"Butternworth");
		mntmHighBoostFilterButternworth.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.new Performance("Filter", "Butternworth High Boost Filter")
				{
					@Override
					protected Image perform(Image image)
					{
						ScalableFunction func2 = new ButternworthFunction(10,
								1, 255, false);
						EnhanceFilterFunction function = new EnhanceFilterFunction(
								func2, 1.0, 1.0);
						Properties properties = function.getProperties();
						properties.put(rs(ButternworthFunction.PARAM_D, true),
								1000);
						properties.put(rs(ButternworthFunction.PARAM_D, false),
								Double.MIN_VALUE);
						properties.put(rs(ButternworthFunction.PARAM_N, true),
								10);
						properties.put(rs(ButternworthFunction.PARAM_N, false),
								0);
						properties.put(rs(EnhanceFilterFunction.PARAM_A, true),
								3);
						properties.put(
								rs(EnhanceFilterFunction.PARAM_A, false), 0);
						properties.put(rs(EnhanceFilterFunction.PARAM_B, true),
								10);
						properties.put(
								rs(EnhanceFilterFunction.PARAM_B, false),
								Double.MIN_VALUE);
						FunctionDislpayDialog dialog = new FunctionDislpayDialog(
								null, "Butternworth High Pass", new String[] {
										ButternworthFunction.PARAM_D,
										ButternworthFunction.PARAM_N,
										EnhanceFilterFunction.PARAM_A,
										EnhanceFilterFunction.PARAM_B },
								properties, function);
						dialog.setXAxisString("D(u,v)");
						dialog.setYAxisString("H(u,v)");
						dialog.setVisible(true);
						Properties p = dialog.getProperties();
						if (p == null)
							return null;
						function.setProperties(p);
						AbstractFourierFilter filter = new FourierDistanceFunctionFilter(
								function);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = filter.filter(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnHighBoostFilter.add(mntmHighBoostFilterButternworth);
		// High boost filter: Gauss
		JMenuItem mntmHighBoostFilterGauss = new JMenuItem("Gauss");
		mntmHighBoostFilterGauss.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.new Performance("Filter", "Gauss High Boost Filter")
				{
					@Override
					protected Image perform(Image image)
					{
						ScalableFunction func2 = new GaussFunction(1.0, 255,
								false);
						EnhanceFilterFunction function = new EnhanceFilterFunction(
								func2, 1.0, 1.0);
						Properties properties = function.getProperties();
						properties.put(rs(GaussFunction.PARAM_SIGMA, true),
								1000);
						properties.put(rs(GaussFunction.PARAM_SIGMA, false),
								Double.MIN_VALUE);
						properties.put(rs(EnhanceFilterFunction.PARAM_A, true),
								3);
						properties.put(
								rs(EnhanceFilterFunction.PARAM_A, false), 0);
						properties.put(rs(EnhanceFilterFunction.PARAM_B, true),
								10);
						properties.put(
								rs(EnhanceFilterFunction.PARAM_B, false),
								Double.MIN_VALUE);
						FunctionDislpayDialog dialog = new FunctionDislpayDialog(
								null, "Gauss High Pass", new String[] {
										GaussFunction.PARAM_SIGMA,
										EnhanceFilterFunction.PARAM_A,
										EnhanceFilterFunction.PARAM_B },
								properties, function);
						dialog.setXAxisString("D(u,v)");
						dialog.setYAxisString("H(u,v)");
						dialog.setVisible(true);
						Properties p = dialog.getProperties();
						if (p == null)
							return null;
						function.setProperties(p);
						AbstractFourierFilter filter = new FourierDistanceFunctionFilter(
								function);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = filter.filter(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnHighBoostFilter.add(mntmHighBoostFilterGauss);
		//
		// Homomorphic filters.
		//
		JMenu mnHomomorphic = new JMenu("Homomorphic");
		mnHomomorphic.setToolTipText("Homomorphic Filter");
		mnFrequencyFilter.add(mnHomomorphic);
		// Homomorphic filter: radius low pass
		JMenuItem mntmHomomorphicRadiusLP = new JMenuItem("Radius LP");
		mntmHomomorphicRadiusLP.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.new Performance("Filter",
						"Radius Low Pass Homomorphic Filter")
				{
					@Override
					protected Image perform(Image image)
					{
						Function func2 = new Radius(image.width() * 0.05, true);
						HomomorphicEnhanceFilterFunction function = new HomomorphicEnhanceFilterFunction(
								func2, 1.0, 1.0);
						Properties properties = function.getProperties();
						properties.put(rs(Radius.PARAM_D, true),
								image.width() > image.height() ? image.width()
										: image.height());
						properties.put(rs(Radius.PARAM_D, false), 0);
						properties.put(
								rs(HomomorphicEnhanceFilterFunction.PARAM_H,
										true), 3);
						properties.put(
								rs(HomomorphicEnhanceFilterFunction.PARAM_H,
										false), 0);
						properties.put(
								rs(HomomorphicEnhanceFilterFunction.PARAM_L,
										true), 10);
						properties.put(
								rs(HomomorphicEnhanceFilterFunction.PARAM_L,
										false), Double.MIN_VALUE);
						FunctionDislpayDialog dialog = new FunctionDislpayDialog(
								null,
								"Radius Low Pass",
								new String[] {
										Radius.PARAM_D,
										HomomorphicEnhanceFilterFunction.PARAM_H,
										HomomorphicEnhanceFilterFunction.PARAM_L },
								properties, function);
						dialog.setXAxisString("D(u,v)");
						dialog.setYAxisString("H(u,v)");
						dialog.setMaxXValue(1.0);
						dialog.setMaxYValue(2.0);
						dialog.setVisible(true);
						Properties p = dialog.getProperties();
						if (p == null)
							return null;
						function.setProperties(p);
						AbstractFourierFilter filter = new HomomorphicFilter(
								function);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = filter.filter(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnHomomorphic.add(mntmHomomorphicRadiusLP);
		// Homomorphic filter: radius high pass
		JMenuItem mntmHomomorphicRadiusHP = new JMenuItem("Radius HP");
		mntmHomomorphicRadiusHP.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.new Performance("Filter",
						"Radius High Pass Homomorphic Filter")
				{
					@Override
					protected Image perform(Image image)
					{
						Function func2 = new Radius(image.width() * 0.05, false);
						HomomorphicEnhanceFilterFunction function = new HomomorphicEnhanceFilterFunction(
								func2, 1.0, 1.0);
						Properties properties = function.getProperties();
						properties.put(rs(Radius.PARAM_D, true),
								image.width() > image.height() ? image.width()
										: image.height());
						properties.put(rs(Radius.PARAM_D, false), 0);
						properties.put(
								rs(HomomorphicEnhanceFilterFunction.PARAM_H,
										true), 3);
						properties.put(
								rs(HomomorphicEnhanceFilterFunction.PARAM_H,
										false), 0);
						properties.put(
								rs(HomomorphicEnhanceFilterFunction.PARAM_L,
										true), 10);
						properties.put(
								rs(HomomorphicEnhanceFilterFunction.PARAM_L,
										false), Double.MIN_VALUE);
						FunctionDislpayDialog dialog = new FunctionDislpayDialog(
								null,
								"Radius High Pass",
								new String[] {
										Radius.PARAM_D,
										HomomorphicEnhanceFilterFunction.PARAM_H,
										HomomorphicEnhanceFilterFunction.PARAM_L },
								properties, function);
						dialog.setXAxisString("D(u,v)");
						dialog.setYAxisString("H(u,v)");
						dialog.setMaxXValue(1.0);
						dialog.setMaxYValue(2.0);
						dialog.setVisible(true);
						Properties p = dialog.getProperties();
						if (p == null)
							return null;
						function.setProperties(p);
						AbstractFourierFilter filter = new HomomorphicFilter(
								function);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = filter.filter(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnHomomorphic.add(mntmHomomorphicRadiusHP);
		// Homomorphic filter: Butternworth low pass
		JMenuItem mntmHomomorphicButternworthLP = new JMenuItem(
				"Butternworth LP");
		mntmHomomorphicButternworthLP.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.new Performance("Filter",
						"Butternworth Low Pass Homomorphic Filter")
				{
					@Override
					protected Image perform(Image image)
					{
						ScalableFunction func2 = new ButternworthFunction(10,
								1, 255, true);
						HomomorphicEnhanceFilterFunction function = new HomomorphicEnhanceFilterFunction(
								func2, 1.0, 1.0);
						Properties properties = function.getProperties();
						properties.put(rs(ButternworthFunction.PARAM_D, true),
								1000);
						properties.put(rs(ButternworthFunction.PARAM_D, false),
								Double.MIN_VALUE);
						properties.put(rs(ButternworthFunction.PARAM_N, true),
								10);
						properties.put(rs(ButternworthFunction.PARAM_N, false),
								0);
						properties.put(
								rs(HomomorphicEnhanceFilterFunction.PARAM_H,
										true), 3);
						properties.put(
								rs(HomomorphicEnhanceFilterFunction.PARAM_H,
										false), 0);
						properties.put(
								rs(HomomorphicEnhanceFilterFunction.PARAM_L,
										true), 10);
						properties.put(
								rs(HomomorphicEnhanceFilterFunction.PARAM_L,
										false), Double.MIN_VALUE);
						FunctionDislpayDialog dialog = new FunctionDislpayDialog(
								null,
								"Butternworth Low Pass",
								new String[] {
										ButternworthFunction.PARAM_D,
										ButternworthFunction.PARAM_N,
										HomomorphicEnhanceFilterFunction.PARAM_H,
										HomomorphicEnhanceFilterFunction.PARAM_L },
								properties, function);
						dialog.setXAxisString("D(u,v)");
						dialog.setYAxisString("H(u,v)");
						dialog.setMaxXValue(1.0);
						dialog.setMaxYValue(2.0);
						dialog.setVisible(true);
						Properties p = dialog.getProperties();
						if (p == null)
							return null;
						function.setProperties(p);
						AbstractFourierFilter filter = new HomomorphicFilter(
								function);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = filter.filter(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnHomomorphic.add(mntmHomomorphicButternworthLP);
		// Homomorphic filter: Butternworth high pass
		JMenuItem mntmHomomorphicButternworthHP = new JMenuItem(
				"Butternworth HP");
		mntmHomomorphicButternworthHP.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.new Performance("Filter",
						"Butternworth High Pass Homomorphic Filter")
				{
					@Override
					protected Image perform(Image image)
					{
						ScalableFunction func2 = new ButternworthFunction(10,
								1, 255, false);
						HomomorphicEnhanceFilterFunction function = new HomomorphicEnhanceFilterFunction(
								func2, 1.0, 1.0);
						Properties properties = function.getProperties();
						properties.put(rs(ButternworthFunction.PARAM_D, true),
								1000);
						properties.put(rs(ButternworthFunction.PARAM_D, false),
								Double.MIN_VALUE);
						properties.put(rs(ButternworthFunction.PARAM_N, true),
								10);
						properties.put(rs(ButternworthFunction.PARAM_N, false),
								0);
						properties.put(
								rs(HomomorphicEnhanceFilterFunction.PARAM_H,
										true), 3);
						properties.put(
								rs(HomomorphicEnhanceFilterFunction.PARAM_H,
										false), 0);
						properties.put(
								rs(HomomorphicEnhanceFilterFunction.PARAM_L,
										true), 10);
						properties.put(
								rs(HomomorphicEnhanceFilterFunction.PARAM_L,
										false), Double.MIN_VALUE);
						FunctionDislpayDialog dialog = new FunctionDislpayDialog(
								null,
								"Butternworth High Pass",
								new String[] {
										ButternworthFunction.PARAM_D,
										ButternworthFunction.PARAM_N,
										HomomorphicEnhanceFilterFunction.PARAM_H,
										HomomorphicEnhanceFilterFunction.PARAM_L },
								properties, function);
						dialog.setXAxisString("D(u,v)");
						dialog.setYAxisString("H(u,v)");
						dialog.setMaxXValue(1.0);
						dialog.setMaxYValue(2.0);
						dialog.setVisible(true);
						Properties p = dialog.getProperties();
						if (p == null)
							return null;
						function.setProperties(p);
						AbstractFourierFilter filter = new HomomorphicFilter(
								function);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = filter.filter(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnHomomorphic.add(mntmHomomorphicButternworthHP);
		// Homomorphic filter: Gauss low pass
		JMenuItem mntmHomomorphicGaussLP = new JMenuItem("Gauss LP");
		mntmHomomorphicGaussLP.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.new Performance("Filter",
						"Gauss Low Pass Homomorphic Filter")
				{
					@Override
					protected Image perform(Image image)
					{
						ScalableFunction func2 = new CoefGaussFunction(1.0,
								0.5, 255, true);
						HomomorphicEnhanceFilterFunction function = new HomomorphicEnhanceFilterFunction(
								func2, 1.0, 1.0);
						Properties properties = function.getProperties();
						properties.put(rs(CoefGaussFunction.PARAM_SIGMA, true),
								1000);
						properties.put(
								rs(CoefGaussFunction.PARAM_SIGMA, false),
								Double.MIN_VALUE);
						properties.put(rs(CoefGaussFunction.PARAM_C, true),
								1000);
						properties.put(rs(CoefGaussFunction.PARAM_C, false),
								Double.MIN_VALUE);
						properties.put(
								rs(HomomorphicEnhanceFilterFunction.PARAM_H,
										true), 3);
						properties.put(
								rs(HomomorphicEnhanceFilterFunction.PARAM_H,
										false), 0);
						properties.put(
								rs(HomomorphicEnhanceFilterFunction.PARAM_L,
										true), 10);
						properties.put(
								rs(HomomorphicEnhanceFilterFunction.PARAM_L,
										false), Double.MIN_VALUE);
						FunctionDislpayDialog dialog = new FunctionDislpayDialog(
								null,
								"Gauss Low Pass",
								new String[] {
										CoefGaussFunction.PARAM_SIGMA,
										CoefGaussFunction.PARAM_C,
										HomomorphicEnhanceFilterFunction.PARAM_H,
										HomomorphicEnhanceFilterFunction.PARAM_L },
								properties, function);
						dialog.setXAxisString("D(u,v)");
						dialog.setYAxisString("H(u,v)");
						dialog.setMaxXValue(1.0);
						dialog.setMaxYValue(2.0);
						dialog.setVisible(true);
						Properties p = dialog.getProperties();
						if (p == null)
							return null;
						function.setProperties(p);
						AbstractFourierFilter filter = new HomomorphicFilter(
								function);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = filter.filter(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnHomomorphic.add(mntmHomomorphicGaussLP);
		// Homomorphic filter: Gauss high pass
		JMenuItem mntmHomomorphicGaussHP = new JMenuItem("Gauss HP");
		mntmHomomorphicGaussHP.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.new Performance("Filter",
						"Gauss High Pass Homomorphic Filter")
				{
					@Override
					protected Image perform(Image image)
					{
						ScalableFunction func2 = new CoefGaussFunction(1.0,
								0.5, 255, false);
						HomomorphicEnhanceFilterFunction function = new HomomorphicEnhanceFilterFunction(
								func2, 1.0, 1.0);
						Properties properties = function.getProperties();
						properties.put(rs(CoefGaussFunction.PARAM_SIGMA, true),
								1000);
						properties.put(
								rs(CoefGaussFunction.PARAM_SIGMA, false),
								Double.MIN_VALUE);
						properties.put(rs(CoefGaussFunction.PARAM_C, true),
								1000);
						properties.put(rs(CoefGaussFunction.PARAM_C, false),
								Double.MIN_VALUE);
						properties.put(
								rs(HomomorphicEnhanceFilterFunction.PARAM_H,
										true), 3);
						properties.put(
								rs(HomomorphicEnhanceFilterFunction.PARAM_H,
										false), 0);
						properties.put(
								rs(HomomorphicEnhanceFilterFunction.PARAM_L,
										true), 10);
						properties.put(
								rs(HomomorphicEnhanceFilterFunction.PARAM_L,
										false), Double.MIN_VALUE);
						FunctionDislpayDialog dialog = new FunctionDislpayDialog(
								null,
								"Gauss High Pass",
								new String[] {
										CoefGaussFunction.PARAM_SIGMA,
										CoefGaussFunction.PARAM_C,
										HomomorphicEnhanceFilterFunction.PARAM_H,
										HomomorphicEnhanceFilterFunction.PARAM_L },
								properties, function);
						dialog.setXAxisString("D(u,v)");
						dialog.setYAxisString("H(u,v)");
						dialog.setMaxXValue(1.0);
						dialog.setMaxYValue(2.0);
						dialog.setVisible(true);
						Properties p = dialog.getProperties();
						if (p == null)
							return null;
						function.setProperties(p);
						AbstractFourierFilter filter = new HomomorphicFilter(
								function);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = filter.filter(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnHomomorphic.add(mntmHomomorphicGaussHP);
	}
}
