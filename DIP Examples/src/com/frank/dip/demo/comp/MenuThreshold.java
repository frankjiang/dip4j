/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved.
 * MenuThreshold.java is PROPRIETARY/CONFIDENTIAL built in 5:19:49 PM, Apr 10,
 * 2014.
 * Use is subject to license terms.
 */
package com.frank.dip.demo.comp;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Properties;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import com.frank.dip.GrayImage;
import com.frank.dip.Image;
import com.frank.dip.analyze.FunctionDislpayDialog;
import com.frank.dip.demo.DIPFrame;
import com.frank.dip.threshold.FBClustering;
import com.frank.dip.threshold.FrankThresholding;
import com.frank.dip.threshold.Fuzzy;
import com.frank.dip.threshold.GlobalThresholding;
import com.frank.dip.threshold.Intermodes;
import com.frank.dip.threshold.IsoData;
import com.frank.dip.threshold.MaxEntropy;
import com.frank.dip.threshold.Mean;
import com.frank.dip.threshold.MinCrossEntropy;
import com.frank.dip.threshold.MinError;
import com.frank.dip.threshold.Minimum;
import com.frank.dip.threshold.Moments;
import com.frank.dip.threshold.Otsu;
import com.frank.dip.threshold.Percentile;
import com.frank.dip.threshold.RenyiEntropy;
import com.frank.dip.threshold.Shanbhag;
import com.frank.dip.threshold.ThresholdFinder;
import com.frank.dip.threshold.Thresholding;
import com.frank.dip.threshold.Triangle;
import com.frank.dip.threshold.Yen;
import com.frank.math.MathUtils;
import com.frank.swing.SwingUtils;
import com.frank.sys.TestUtils;
import com.frank.sys.TestUtils.Timer;

/**
 * Load menu of thresholding.
 * <p>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class MenuThreshold extends MenuLoader
{
	/**
	 * Construct an instance of <tt>MenuThreshold</tt>.
	 * 
	 * @param dip
	 */
	public MenuThreshold(DIPFrame dip)
	{
		super(dip, null);
	}

	/**
	 * @see com.frank.dip.demo.comp.MenuLoader#load(java.lang.Object[])
	 */
	@Override
	protected void load(Object... args)
	{
		JMenu mnThreshold = new JMenu("Threshold(T)");
		mnThreshold.setMnemonic('T');
		dip.getBar().add(mnThreshold);
		JMenuItem mntmQuickTransform = new JMenuItem("Quick Binary Transform");
		mntmQuickTransform.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.new Performance("Thresholding", null)
				{
					@Override
					protected Image perform(Image image)
					{
						GrayImage src = new GrayImage(image);
						ThresholdFinder finder = new ThresholdFinder()
						{
							@Override
							public int threshold(GrayImage image)
							{
								return 127;
							}

							@Override
							public String getFinderName()
							{
								return "Quick Binary Transform(127)";
							}
						};
						Thresholding thresholding = new GlobalThresholding(
								finder);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = thresholding.operate(src);
						time = t.getTime(timeunit);
						content = finder.getFinderName();
						return res;
					}
				}.perform();
			}
		});
		mntmQuickTransform.setToolTipText("Threshold the gray image with 127.");
		mnThreshold.add(mntmQuickTransform);
		JMenuItem mntmManualThreshold = new JMenuItem("Manual Threshold");
		mntmManualThreshold.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.new Performance("Thresholding", null)
				{
					@Override
					protected Image perform(Image image)
					{
						GrayImage src = new GrayImage(image);
						String s = SwingUtils.inputDialog(dip,
								"Global Threshold",
								"Input the global threshold:");
						if (s == null)
							return null;
						ThresholdFinder finder = null;
						Thresholding thresholding = null;
						try
						{
							final Integer threshold = Integer.valueOf(s);
							if (threshold == null)
								return null;
							finder = new ThresholdFinder()
							{
								@Override
								public int threshold(GrayImage image)
								{
									return threshold;
								}

								@Override
								public String getFinderName()
								{
									return String.format(
											"Manual Thresholding: %d",
											threshold);
								}
							};
							thresholding = new GlobalThresholding(finder);
						}
						catch (NumberFormatException e1)
						{
							SwingUtils.errorMessage(dip,
									e1.getLocalizedMessage());
							return null;
						}
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = thresholding.operate(src);
						time = t.getTime(timeunit);
						content = finder.getFinderName();
						return res;
					}
				}.perform();
			}
		});
		mntmManualThreshold
				.setToolTipText("Manual select the threshold of the image.");
		mnThreshold.add(mntmManualThreshold);
		JMenuItem mntmFrank = new JMenuItem("Frank");
		mntmFrank.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.new Performance("Thresholding", "Frank")
				{
					@Override
					protected Image perform(Image image)
					{
						GrayImage src = new GrayImage(image);
						// Parameter selecting
						String fuzzyRadio = "fuzzy radio";
						String clusteringWindowRadius = "clustering radius";
						ThresholdFinder finder = null;
						Properties p = new Properties();
						{
							String frMin = rs(fuzzyRadio, false), frMax = rs(
									fuzzyRadio, true);
							String cwMin = rs(clusteringWindowRadius, false), cwMax = rs(
									clusteringWindowRadius, true);
							p.put(fuzzyRadio, 0.2);
							p.put(frMin, 0.01);
							p.put(frMax, 1);
							p.put(clusteringWindowRadius, 0.1);
							p.put(cwMin, 0.01);
							p.put(cwMax, 1);
							FunctionDislpayDialog fdd = new FunctionDislpayDialog(
									dip, "Frank Thresholding",
									new String[] { clusteringWindowRadius,
											fuzzyRadio }, p, null, true);
							fdd.setVisible(true);
							p = fdd.getProperties();
							if (p == null)
								return null;
							ThresholdFinder[] finders = new ThresholdFinder[] {
									new IsoData(), new Intermodes(),
									new Minimum(), new Fuzzy(),
									new MaxEntropy(), new Mean(),
									new MinCrossEntropy(), new MinError(),
									new Moments(), new Otsu(),
									new Percentile(), new RenyiEntropy(),
									new Shanbhag(), new Triangle(), new Yen() };
							String[] selections = new String[finders.length];
							for (int i = 0; i < finders.length; i++)
								selections[i] = finders[i].getFinderName();
							MathUtils.combineSort(finders, selections);
							Arrays.sort(selections);
							Object selection = JOptionPane.showInputDialog(dip,
									"Select the global thresholding method:",
									"Select", JOptionPane.INFORMATION_MESSAGE,
									null, selections,
									new IsoData().getFinderName());
							if (selection == null)
								return null;
							int i = Arrays.binarySearch(selections, selection);
							if (i < 0)
								return null;
							finder = finders[i];
						}
						double radius = (Double) p.get(clusteringWindowRadius);
						double fuzzy = (Double) p.get(fuzzyRadio);
						Thresholding thresholding = new FrankThresholding(
								radius, fuzzy, finder);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = thresholding.operate(src);
						time = t.getTime(timeunit);
						content = finder.getFinderName();
						return res;
					}
				}.perform();
			}
		});
		mnThreshold.add(mntmFrank);
		JMenuItem mntmOtsu = new JMenuItem("Otsu");
		mntmOtsu.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.new Performance("Thresholding", null)
				{
					@Override
					protected Image perform(Image image)
					{
						GrayImage src = new GrayImage(image);
						ThresholdFinder finder = new Otsu();
						Thresholding thresholding = new GlobalThresholding(
								finder);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = thresholding.operate(src);
						time = t.getTime(timeunit);
						content = finder.getFinderName();
						return res;
					}
				}.perform();
			}
		});
		mnThreshold.add(mntmOtsu);
		JMenuItem mntmMaxEntropy = new JMenuItem("Max Entropy");
		mntmMaxEntropy.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.new Performance("Thresholding", null)
				{
					@Override
					protected Image perform(Image image)
					{
						GrayImage src = new GrayImage(image);
						ThresholdFinder finder = new MaxEntropy();
						Thresholding thresholding = new GlobalThresholding(
								finder);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = thresholding.operate(src);
						time = t.getTime(timeunit);
						content = finder.getFinderName();
						return res;
					}
				}.perform();
			}
		});
		mnThreshold.add(mntmMaxEntropy);
		JMenuItem mntmMinError = new JMenuItem("Min Error");
		mntmMinError.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.new Performance("Thresholding", null)
				{
					@Override
					protected Image perform(Image image)
					{
						GrayImage src = new GrayImage(image);
						ThresholdFinder finder = new MinError();
						Thresholding thresholding = new GlobalThresholding(
								finder);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = thresholding.operate(src);
						time = t.getTime(timeunit);
						content = finder.getFinderName();
						return res;
					}
				}.perform();
			}
		});
		mnThreshold.add(mntmMinError);
		JMenuItem mntmFuzzy = new JMenuItem("Fuzzy");
		mntmFuzzy.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.new Performance("Thresholding", null)
				{
					@Override
					protected Image perform(Image image)
					{
						GrayImage src = new GrayImage(image);
						ThresholdFinder finder = new Fuzzy();
						Thresholding thresholding = new GlobalThresholding(
								finder);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = thresholding.operate(src);
						time = t.getTime(timeunit);
						content = finder.getFinderName();
						return res;
					}
				}.perform();
			}
		});
		mnThreshold.add(mntmFuzzy);
		JMenuItem mntmIntermodes = new JMenuItem("Intermodes");
		mntmIntermodes.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.new Performance("Thresholding", null)
				{
					@Override
					protected Image perform(Image image)
					{
						GrayImage src = new GrayImage(image);
						ThresholdFinder finder = new Intermodes();
						Thresholding thresholding = new GlobalThresholding(
								finder);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = thresholding.operate(src);
						time = t.getTime(timeunit);
						content = finder.getFinderName();
						return res;
					}
				}.perform();
			}
		});
		mnThreshold.add(mntmIntermodes);
		JMenuItem mntmIsodata = new JMenuItem("IsoData");
		mntmIsodata.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.new Performance("Thresholding", null)
				{
					@Override
					protected Image perform(Image image)
					{
						GrayImage src = new GrayImage(image);
						ThresholdFinder finder = new IsoData();
						Thresholding thresholding = new GlobalThresholding(
								finder);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = thresholding.operate(src);
						time = t.getTime(timeunit);
						content = finder.getFinderName();
						return res;
					}
				}.perform();
			}
		});
		mnThreshold.add(mntmIsodata);
		JMenuItem mntmMinCrossEntropy = new JMenuItem("Min Cross Entropy");
		mntmMinCrossEntropy.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.new Performance("Thresholding", null)
				{
					@Override
					protected Image perform(Image image)
					{
						GrayImage src = new GrayImage(image);
						ThresholdFinder finder = new MinCrossEntropy();
						Thresholding thresholding = new GlobalThresholding(
								finder);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = thresholding.operate(src);
						time = t.getTime(timeunit);
						content = finder.getFinderName();
						return res;
					}
				}.perform();
			}
		});
		mnThreshold.add(mntmMinCrossEntropy);
		JMenuItem mntmMean = new JMenuItem("Mean");
		mntmMean.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.new Performance("Thresholding", null)
				{
					@Override
					protected Image perform(Image image)
					{
						GrayImage src = new GrayImage(image);
						ThresholdFinder finder = new Mean();
						Thresholding thresholding = new GlobalThresholding(
								finder);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = thresholding.operate(src);
						time = t.getTime(timeunit);
						content = finder.getFinderName();
						return res;
					}
				}.perform();
			}
		});
		mnThreshold.add(mntmMean);
		JMenuItem mntmMoments = new JMenuItem("Moments");
		mntmMoments.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.new Performance("Thresholding", null)
				{
					@Override
					protected Image perform(Image image)
					{
						GrayImage src = new GrayImage(image);
						ThresholdFinder finder = new Moments();
						Thresholding thresholding = new GlobalThresholding(
								finder);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = thresholding.operate(src);
						time = t.getTime(timeunit);
						content = finder.getFinderName();
						return res;
					}
				}.perform();
			}
		});
		JMenuItem mntmMinimum = new JMenuItem("Minimum");
		mntmMinimum.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.new Performance("Thresholding", null)
				{
					@Override
					protected Image perform(Image image)
					{
						GrayImage src = new GrayImage(image);
						ThresholdFinder finder = new Minimum();
						Thresholding thresholding = new GlobalThresholding(
								finder);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = thresholding.operate(src);
						time = t.getTime(timeunit);
						content = finder.getFinderName();
						return res;
					}
				}.perform();
			}
		});
		mnThreshold.add(mntmMinimum);
		mnThreshold.add(mntmMoments);
		JMenuItem mntmPercentile = new JMenuItem("Percentile");
		mntmPercentile.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.new Performance("Thresholding", null)
				{
					@Override
					protected Image perform(Image image)
					{
						GrayImage src = new GrayImage(image);
						ThresholdFinder finder = new Percentile();
						Thresholding thresholding = new GlobalThresholding(
								finder);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = thresholding.operate(src);
						time = t.getTime(timeunit);
						content = finder.getFinderName();
						return res;
					}
				}.perform();
			}
		});
		mnThreshold.add(mntmPercentile);
		JMenuItem mntmRenyiEntropy = new JMenuItem("Renyi's Entropy");
		mntmRenyiEntropy.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.new Performance("Thresholding", null)
				{
					@Override
					protected Image perform(Image image)
					{
						GrayImage src = new GrayImage(image);
						ThresholdFinder finder = new RenyiEntropy();
						Thresholding thresholding = new GlobalThresholding(
								finder);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = thresholding.operate(src);
						time = t.getTime(timeunit);
						content = finder.getFinderName();
						return res;
					}
				}.perform();
			}
		});
		mnThreshold.add(mntmRenyiEntropy);
		JMenuItem mntmShanbhag = new JMenuItem("Shanbhag");
		mntmShanbhag.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.new Performance("Thresholding", null)
				{
					@Override
					protected Image perform(Image image)
					{
						GrayImage src = new GrayImage(image);
						ThresholdFinder finder = new Shanbhag();
						Thresholding thresholding = new GlobalThresholding(
								finder);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = thresholding.operate(src);
						time = t.getTime(timeunit);
						content = finder.getFinderName();
						return res;
					}
				}.perform();
			}
		});
		mnThreshold.add(mntmShanbhag);
		JMenuItem mntmTriangle = new JMenuItem("Triangle");
		mntmTriangle.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.new Performance("Thresholding", null)
				{
					@Override
					protected Image perform(Image image)
					{
						GrayImage src = new GrayImage(image);
						ThresholdFinder finder = new Triangle();
						Thresholding thresholding = new GlobalThresholding(
								finder);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = thresholding.operate(src);
						time = t.getTime(timeunit);
						content = finder.getFinderName();
						return res;
					}
				}.perform();
			}
		});
		mnThreshold.add(mntmTriangle);
		JMenuItem mntmYen = new JMenuItem("Yen");
		mntmYen.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.new Performance("Thresholding", null)
				{
					@Override
					protected Image perform(Image image)
					{
						GrayImage src = new GrayImage(image);
						ThresholdFinder finder = new Yen();
						Thresholding thresholding = new GlobalThresholding(
								finder);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = thresholding.operate(src);
						time = t.getTime(timeunit);
						content = finder.getFinderName();
						return res;
					}
				}.perform();
			}
		});
		mnThreshold.add(mntmYen);
		JMenuItem mntmFbclustering = new JMenuItem("FBClustering");
		mntmFbclustering.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.new Performance("Thresholding", "FBClustering")
				{
					@Override
					protected Image perform(Image image)
					{
						GrayImage src = new GrayImage(image);
						Thresholding thresholding = new FBClustering();
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = thresholding.operate(src);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnThreshold.add(mntmFbclustering);
	}
}
