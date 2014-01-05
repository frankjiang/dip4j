/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights reserved.
 * MenuEnhance.java is PROPRIETARY/CONFIDENTIAL built in 2013.
 * Use is subject to license terms.
 */
package com.frank.dip.demo.comp;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import com.frank.dip.ColorImage;
import com.frank.dip.GrayImage;
import com.frank.dip.Image;
import com.frank.dip.analyze.FunctionDislpayDialog;
import com.frank.dip.demo.DIPFrame;
import com.frank.dip.enhance.GrayScaleAverage;
import com.frank.dip.enhance.GrayScaleCoefficient;
import com.frank.dip.enhance.color.FakeColorTransform;
import com.frank.dip.enhance.time.HistogramNormalization;
import com.frank.dip.enhance.time.InversionTransformation;
import com.frank.dip.enhance.time.LogarithmicTransformation;
import com.frank.dip.enhance.time.PiecewiseLinearTransformation;
import com.frank.dip.enhance.time.PowerLawTransformation;
import com.frank.dip.math.Function;
import com.frank.dip.math.Quadratic;
import com.frank.swing.SwingUtils;
import com.frank.sys.TestUtils;
import com.frank.sys.TestUtils.Timer;

/**
 * The enhance menu.
 * <p>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class MenuEnhance extends MenuLoader
{
	/**
	 * Construct an instance of <tt>MenuEnhance</tt>.
	 * 
	 * @param dip
	 *            the digital image processing frame
	 */
	public MenuEnhance(DIPFrame dip)
	{
		super(dip, null);
	}

	/**
	 * @see com.frank.dip.demo.comp.MenuLoader#load(java.lang.Object[])
	 */
	@Override
	protected void load(Object... args)
	{
		JMenu mnEnhance = new JMenu("Enhance(E)");
		mnEnhance.setMnemonic('E');
		dip.getBar().add(mnEnhance);
		JMenuItem mntmInverse = new JMenuItem("Inverse");
		mntmInverse.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.new Performance("Filter", "Inverse")
				{
					@Override
					protected Image perform(Image image)
					{
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = InversionTransformation.inverse(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		JMenu mnGrayScale = new JMenu("Gray Scale");
		mnEnhance.add(mnGrayScale);
		JMenuItem mntmGrayScaleAverage = new JMenuItem("Average");
		mntmGrayScaleAverage.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.new Performance("Enhance", "Gray Scale Average")
				{
					@Override
					protected Image perform(Image image)
					{
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = image instanceof ColorImage ? new GrayScaleAverage()
								.operate((ColorImage) image) : image;
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnGrayScale.add(mntmGrayScaleAverage);
		JMenuItem mntmGrayScaleCoefficient = new JMenuItem("Coefficient");
		mntmGrayScaleCoefficient.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.new Performance("Enhance", "Gray Scale Coefficient")
				{
					@Override
					protected Image perform(Image image)
					{
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = image instanceof ColorImage ? new GrayScaleCoefficient()
								.operate((ColorImage) image) : image;
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnGrayScale.add(mntmGrayScaleCoefficient);
		mnEnhance.add(mntmInverse);
		JMenuItem mntmLogarithm = new JMenuItem("Logarithm");
		mntmLogarithm.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.new Performance("Enhance", "Logarithm")
				{
					@Override
					protected Image perform(Image image)
					{
						LogarithmicTransformation trans = LogarithmicTransformation
								.getLogarithmTransformation(image.getClass(),
										255 / Math.log1p(256), 0.0);
						Properties properties = new Properties();
						String minB = LogarithmicTransformation.PARAM_B
								+ "_min";//$NON-NLS-1$
						String maxB = LogarithmicTransformation.PARAM_B
								+ "_max";//$NON-NLS-1$
						String minC = LogarithmicTransformation.PARAM_C
								+ "_min";//$NON-NLS-1$
						String maxC = LogarithmicTransformation.PARAM_C
								+ "_max";//$NON-NLS-1$
						properties.put(minB, -255);
						properties.put(maxB, 255);
						properties.put(minC, -255);
						properties.put(maxC, 255);
						FunctionDislpayDialog fdd = new FunctionDislpayDialog(
								dip, "Logarithmic Transformation",
								new String[] {
										LogarithmicTransformation.PARAM_C,
										LogarithmicTransformation.PARAM_B },
								properties, trans, true);
						fdd.setVisible(true);
						properties = fdd.getProperties();
						if (properties == null)
							return null;
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = trans.operate(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnEnhance.add(mntmLogarithm);
		JMenuItem mntmInverseLogarithm = new JMenuItem("Inverse Logarithm");
		mntmInverseLogarithm.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.new Performance("Enhance", "Inverse Logarithm")
				{
					@Override
					protected Image perform(Image image)
					{
						LogarithmicTransformation trans = LogarithmicTransformation
								.getInverseLogarithmTransformation(
										image.getClass(),
										255 / Math.log1p(256), 0.0);
						Properties properties = new Properties();
						String minB = rs(LogarithmicTransformation.PARAM_B,
								false);
						String maxB = rs(LogarithmicTransformation.PARAM_B,
								true);
						String minC = rs(LogarithmicTransformation.PARAM_C,
								false);
						String maxC = rs(LogarithmicTransformation.PARAM_C,
								true);
						properties.put(minB, -255);
						properties.put(maxB, 255);
						properties.put(minC, -255);
						properties.put(maxC, 255);
						FunctionDislpayDialog fdd = new FunctionDislpayDialog(
								dip, "<Enhance> Inverse Logarithmic",
								new String[] {
										LogarithmicTransformation.PARAM_C,
										LogarithmicTransformation.PARAM_B },
								properties, trans, true);
						fdd.setVisible(true);
						properties = fdd.getProperties();
						if (properties == null)
							return null;
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = trans.operate(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnEnhance.add(mntmInverseLogarithm);
		JMenuItem mntmPowerLaw = new JMenuItem("Power Law");
		mntmPowerLaw.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.new Performance("Enhance", "Power-law")
				{
					@Override
					protected Image perform(Image image)
					{
						PowerLawTransformation trans = PowerLawTransformation
								.getPowerLawTransformation(image.getClass(),
										PowerLawTransformation.SCALE_LEVEL - 1,
										1.0, 0.0);
						Properties properties = new Properties();
						String minB = rs(PowerLawTransformation.PARAM_B, false);
						String maxB = rs(PowerLawTransformation.PARAM_B, true);
						String minC = rs(PowerLawTransformation.PARAM_C, false);
						String maxC = rs(PowerLawTransformation.PARAM_C, true);
						String minG = rs(PowerLawTransformation.PARAM_GAMMA,
								false);
						String maxG = rs(PowerLawTransformation.PARAM_GAMMA,
								true);
						properties.put(minB, -255);
						properties.put(maxB, 255);
						properties.put(minC, 0);
						properties.put(maxC, 512);
						properties.put(minG, 0.1);
						properties.put(maxG, 10);
						FunctionDislpayDialog fdd = new FunctionDislpayDialog(
								dip, "Power-law Transformation", new String[] {
										PowerLawTransformation.PARAM_C,
										PowerLawTransformation.PARAM_B,
										PowerLawTransformation.PARAM_GAMMA },
								properties, trans, true);
						fdd.setVisible(true);
						properties = fdd.getProperties();
						if (properties == null)
							return null;
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = trans.operate(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnEnhance.add(mntmPowerLaw);
		JMenuItem mntmInversePowerlaw = new JMenuItem("Inverse Power-law");
		mntmInversePowerlaw.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.new Performance("Enhance", "Inverse Power-law")
				{
					@Override
					protected Image perform(Image image)
					{
						PowerLawTransformation trans = PowerLawTransformation
								.inversePowerLawTransformation(image,
										PowerLawTransformation.SCALE_LEVEL - 1,
										1.0, 0.0);
						Properties properties = new Properties();
						String minB = rs(PowerLawTransformation.PARAM_B, false);
						String maxB = rs(PowerLawTransformation.PARAM_B, true);
						String minC = rs(PowerLawTransformation.PARAM_C, false);
						String maxC = rs(PowerLawTransformation.PARAM_C, true);
						String minG = rs(PowerLawTransformation.PARAM_GAMMA,
								false);
						String maxG = rs(PowerLawTransformation.PARAM_GAMMA,
								true);
						properties.put(minB, -255);
						properties.put(maxB, 255);
						properties.put(minC, 0);
						properties.put(maxC, 512);
						properties.put(minG, 0.1);
						properties.put(maxG, 10);
						FunctionDislpayDialog fdd = new FunctionDislpayDialog(
								dip, "Inverse Power-law Transformation",
								new String[] { PowerLawTransformation.PARAM_C,
										PowerLawTransformation.PARAM_B,
										PowerLawTransformation.PARAM_GAMMA },
								properties, trans, true);
						fdd.setVisible(true);
						properties = fdd.getProperties();
						if (properties == null)
							return null;
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = trans.operate(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnEnhance.add(mntmInversePowerlaw);
		JMenuItem mntmPiecewiseLinear = new JMenuItem("Piecewise Linear");
		mntmPiecewiseLinear.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.new Performance("Enhance", "Piecewise Linear")
				{
					@Override
					protected Image perform(Image image)
					{
						PiecewiseLinearTransformation trans = PiecewiseLinearTransformation
								.getPiecewiseLinearTransformation(
										image.getClass(), 50, 100, 150, 200);
						Properties properties = new Properties();
						String minAX = rs(
								PiecewiseLinearTransformation.PARAM_A_X, false);
						String maxAX = rs(
								PiecewiseLinearTransformation.PARAM_A_X, true);
						String minAY = rs(
								PiecewiseLinearTransformation.PARAM_A_Y, false);
						String maxAY = rs(
								PiecewiseLinearTransformation.PARAM_A_Y, true);
						String minBX = rs(
								PiecewiseLinearTransformation.PARAM_B_X, false);
						String maxBX = rs(
								PiecewiseLinearTransformation.PARAM_B_X, true);
						String minBY = rs(
								PiecewiseLinearTransformation.PARAM_B_Y, false);
						String maxBY = rs(
								PiecewiseLinearTransformation.PARAM_B_Y, true);
						properties.put(minAX, 1);
						properties.put(maxAX, 254);
						properties.put(minAY, 1);
						properties.put(maxAY, 254);
						properties.put(minBX, 1);
						properties.put(maxBX, 254);
						properties.put(minBY, 1);
						properties.put(maxBY, 254);
						FunctionDislpayDialog fdd = new FunctionDislpayDialog(
								dip,
								"Piecewise Linear Transformation",
								new String[] {
										PiecewiseLinearTransformation.PARAM_A_X,
										PiecewiseLinearTransformation.PARAM_A_Y,
										PiecewiseLinearTransformation.PARAM_B_X,
										PiecewiseLinearTransformation.PARAM_B_Y },
								properties, trans, true);
						fdd.setVisible(true);
						properties = fdd.getProperties();
						if (properties == null)
							return null;
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = trans.operate(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnEnhance.add(mntmPiecewiseLinear);
		JMenu mnHistogramStretch = new JMenu("Histogram Stretch");
		mnEnhance.add(mnHistogramStretch);
		JMenuItem mntmHistogramStretchAverage = new JMenuItem("Average");
		mntmHistogramStretchAverage.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.new Performance("Enhance", "Average Histogram Stretch")
				{
					@Override
					protected Image perform(Image image)
					{
						HistogramNormalization trans = HistogramNormalization
								.getHistogramNormalizatoin(
										HistogramNormalization.getAverage(),
										image.getClass());
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = trans.operate(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnHistogramStretch.add(mntmHistogramStretchAverage);
		JMenuItem mntmHistogramStretchQuadratic = new JMenuItem("Quadratic");
		mntmHistogramStretchQuadratic.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.new Performance("Enhance", "Quadratic Histogram Stretch")
				{
					@Override
					protected Image perform(Image image)
					{
						Function function = new Quadratic(1.5 / Math
								.pow(128, 3), -3.0 / 128 / 128, 3.0 / 256);
						HistogramNormalization trans = HistogramNormalization
								.getHistogramNormalizatoin(function,
										image.getClass());
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = trans.operate(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnHistogramStretch.add(mntmHistogramStretchQuadratic);
		JMenuItem mntmFakeColor = new JMenuItem("Fake Color");
		mntmFakeColor.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.new Performance("Enhance", "Fake Color Transform")
				{
					@Override
					protected Image perform(Image image)
					{
						double termCount, ratio;
						String numberFormatErrorMsg = "%s is not an integer or a float.";
						String s = SwingUtils.inputDialog(dip, "Term Count",
								"Input the term count (integer or float):");
						if (s == null)
							return null;
						try
						{
							termCount = Double.valueOf(s);
						}
						catch (NumberFormatException e)
						{
							SwingUtils.errorMessage(dip,
									String.format(numberFormatErrorMsg, s));
							return null;
						}
						s = SwingUtils.inputDialog(dip, "Ratio",
								"Input the ratio (integer or float):");
						if (s == null)
							return null;
						try
						{
							ratio = Double.valueOf(s);
						}
						catch (NumberFormatException e)
						{
							SwingUtils.errorMessage(dip,
									String.format(numberFormatErrorMsg, s));
							return null;
						}
						FakeColorTransform trans = FakeColorTransform
								.getTermBasedInstance(termCount, ratio);
						GrayImage gi = null;
						if (image instanceof GrayImage)
							gi = (GrayImage) image;
						else
						{
							SwingUtils
									.errorMessage(dip,
											"The source image of fake color transformation must be a gray image.");
						}
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = trans.operate(gi);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnEnhance.add(mntmFakeColor);
	}
}
