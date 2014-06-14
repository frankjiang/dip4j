/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights reserved.
 * MenuAnalyze.java is PROPRIETARY/CONFIDENTIAL built in 2013.
 * Use is subject to license terms.
 */
package com.frank.dip.demo.comp;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import com.frank.dip.ColorImage;
import com.frank.dip.Image;
import com.frank.dip.analyze.FourierTransformation;
import com.frank.dip.analyze.Histogram;
import com.frank.dip.analyze.HistogramDialog;
import com.frank.dip.analyze.ImageDisplayDialog;
import com.frank.dip.demo.DIPFrame;
import com.frank.dip.enhance.GuassNoiseGenerator;
import com.frank.swing.SwingUtils;
import com.frank.sys.TestUtils;
import com.frank.sys.TestUtils.Timer;

/**
 * The analyze menu.
 * <p>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class MenuAnalyze extends MenuLoader
{
	/**
	 * Construct an instance of <tt>MenuAnalyze</tt>.
	 * 
	 * @param dip
	 *            the digital image processing frame
	 */
	public MenuAnalyze(DIPFrame dip)
	{
		super(dip, null);
	}

	/**
	 * @see com.frank.dip.demo.comp.MenuLoader#load(java.lang.Object[])
	 */
	@Override
	protected void load(Object... args)
	{
		JMenu mnAnalyze = new JMenu("Analyze(A)");
		mnAnalyze.setMnemonic('A');
		dip.getBar().add(mnAnalyze);
		JMenuItem mntmPixelsHistogram = new JMenuItem("Pixels Histogram");
		mntmPixelsHistogram.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Image image = dip.current();
				if (image == null)
					return;
				new HistogramDialog(dip, dip.getFilename(), Histogram
						.histogram(image), false);
			}
		});
		mnAnalyze.add(mntmPixelsHistogram);
		JMenuItem mntmFFT = new JMenuItem("FFT");
		mntmFFT.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.new Performance("Analyze", "Fast Fourier Transformation")
				{
					@Override
					protected Image perform(Image image)
					{
						FourierTransformation trans = new FourierTransformation();
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = new ColorImage(trans.forward(image));
						time = t.getTime(timeunit);
						ImageDisplayDialog idd = new ImageDisplayDialog(dip,
								"Fourier Power Spectrum", false, res);
						idd.pack();
						idd.setLocationRelativeTo(dip);
						idd.setVisible(true);
						return image;
					}
				}.perform();
			}
		});
		mntmFFT.setToolTipText("Fast Fourier Transformation");
		mnAnalyze.add(mntmFFT);
		JMenuItem mntmGuassNoise = new JMenuItem("Guass Noise");
		mntmGuassNoise.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.new Performance("Analyze", "Guass Noise Generation")
				{
					@Override
					protected Image perform(Image image)
					{
						String muS, sigmaS;
						double mu, sigma;
						muS = SwingUtils.inputDialog(dip, "Input Mu",
								"Input the mean: ", "0");
						if (muS == null)
							return null;
						mu = Double.valueOf(muS);
						sigmaS = SwingUtils.inputDialog(dip, "Input Sigma",
								"Input the standard deviation: ", "30");
						if (sigmaS == null)
							return null;
						sigma = Double.valueOf(sigmaS);
						GuassNoiseGenerator gng = new GuassNoiseGenerator(mu,
								sigma);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = gng.operate(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnAnalyze.add(mntmGuassNoise);
	}
}
