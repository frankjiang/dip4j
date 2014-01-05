/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights reserved.
 * MenuMorph.java is PROPRIETARY/CONFIDENTIAL built in 2013.
 * Use is subject to license terms.
 */
package com.frank.dip.demo.comp;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import com.frank.dip.BinaryImage;
import com.frank.dip.Image;
import com.frank.dip.demo.DIPFrame;
import com.frank.dip.morph.Morph;
import com.frank.dip.morph.MorphBinary;
import com.frank.swing.SwingUtils;
import com.frank.sys.TestUtils;
import com.frank.sys.TestUtils.Timer;

/**
 * The morphology menu loader.
 * <p>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class MenuMorph extends MenuLoader
{
	/**
	 * Construct an instance of <tt>MenuMorph</tt>.
	 * 
	 * @param dip
	 *            the digital image processing frame
	 */
	public MenuMorph(DIPFrame dip)
	{
		super(dip, null);
	}

	/**
	 * @see com.frank.dip.demo.comp.MenuLoader#load(java.lang.Object[])
	 */
	@Override
	protected void load(Object... args)
	{
		JMenu mnMorphm = new JMenu("Morph(M)");
		mnMorphm.setMnemonic('M');
		dip.getBar().add(mnMorphm);
		JMenuItem mntmMorphErode = new JMenuItem("Erode");
		mntmMorphErode.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.new Performance("Morph", "Erode")
				{
					@Override
					protected Image perform(Image image)
					{
						Integer type = selectMorphologyStructure();
						if (type == null)
							return null;
						Morph morph = null;
						try
						{
							morph = Morph.getMorph(image, type);
						}
						catch (Exception e1)
						{
							SwingUtils.errorMessage(dip,
									e1.getLocalizedMessage());
							return null;
						}
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = morph.erode(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnMorphm.add(mntmMorphErode);
		JMenuItem mntmMorphDilate = new JMenuItem("Dilate");
		mntmMorphDilate.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.new Performance("Morph", "Dilate")
				{
					@Override
					protected Image perform(Image image)
					{
						Integer type = selectMorphologyStructure();
						if (type == null)
							return null;
						Morph morph = null;
						try
						{
							morph = Morph.getMorph(image, type);
						}
						catch (Exception e1)
						{
							SwingUtils.errorMessage(dip,
									e1.getLocalizedMessage());
							return null;
						}
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = morph.dilate(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnMorphm.add(mntmMorphDilate);
		JMenuItem mntmMorphOpen = new JMenuItem("Open");
		mntmMorphOpen.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.new Performance("Morph", "Open")
				{
					@Override
					protected Image perform(Image image)
					{
						Integer type = selectMorphologyStructure();
						if (type == null)
							return null;
						Morph morph = null;
						try
						{
							morph = Morph.getMorph(image, type);
						}
						catch (Exception e1)
						{
							SwingUtils.errorMessage(dip,
									e1.getLocalizedMessage());
							return null;
						}
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = morph.open(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnMorphm.add(mntmMorphOpen);
		JMenuItem mntmMorphClose = new JMenuItem("Close");
		mntmMorphClose.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.new Performance("Morph", "Close")
				{
					@Override
					protected Image perform(Image image)
					{
						Integer type = selectMorphologyStructure();
						if (type == null)
							return null;
						Morph morph = null;
						try
						{
							morph = Morph.getMorph(image, type);
						}
						catch (Exception e1)
						{
							SwingUtils.errorMessage(dip,
									e1.getLocalizedMessage());
							return null;
						}
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = morph.close(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnMorphm.add(mntmMorphClose);
		JMenu mnMorphBinary = new JMenu("Binary Morph");
		mnMorphm.add(mnMorphBinary);
		JMenuItem mntmMorphSkeletonize = new JMenuItem("Skeletonize");
		mntmMorphSkeletonize.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.new Performance("Morph", "Skeletonize")
				{
					@Override
					protected Image perform(Image image)
					{
						if (!(image instanceof BinaryImage))
						{
							SwingUtils.errorMessage(
									dip,
									String.format(
											"Skeletonize cannot support image type: %s",
											image.getClass().toString()));
							return null;
						}
						Integer type = selectMorphologyStructure();
						if (type == null)
							return null;
						String str = SwingUtils.inputDialog(dip,
								"Erode Loop Counter",
								"Input an integer for erode loop counter:");
						if (str == null)
							return null;
						Integer k = null;
						try
						{
							k = Integer.valueOf(str);
						}
						catch (NumberFormatException e1)
						{
							SwingUtils.errorMessage(dip,
									e1.getLocalizedMessage());
							return null;
						}
						BinaryImage bi = (BinaryImage) image.clone();
						MorphBinary morph = new MorphBinary(type);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = morph.skeletonize(bi, k);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnMorphBinary.add(mntmMorphSkeletonize);
		JMenuItem mntmMorphHit = new JMenuItem("Hit");
		mntmMorphHit.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.new Performance("Morph", "Hit")
				{
					@Override
					protected Image perform(Image image)
					{
						if (!(image instanceof BinaryImage))
						{
							SwingUtils.errorMessage(dip, String.format(
									"Hit cannot support image type: %s", image
											.getClass().toString()));
							return null;
						}
						Integer type = selectMorphologyStructure();
						if (type == null)
							return null;
						MorphBinary morph = new MorphBinary(type);
						BinaryImage bi = (BinaryImage) image.clone();
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = morph.hit(bi);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnMorphBinary.add(mntmMorphHit);
		JMenuItem mntmMorphThin = new JMenuItem("Thin");
		mntmMorphThin.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.new Performance("Morph", "Thin")
				{
					@Override
					protected Image perform(Image image)
					{
						if (!(image instanceof BinaryImage))
						{
							SwingUtils.errorMessage(dip, String.format(
									"Thin cannot support image type: %s", image
											.getClass().toString()));
							return null;
						}
						Integer type = selectMorphologyStructure();
						if (type == null)
							return null;
						MorphBinary morph = new MorphBinary(type);
						BinaryImage bi = (BinaryImage) image.clone();
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = morph.thin(bi);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnMorphBinary.add(mntmMorphThin);
		JMenuItem mntmMorphEdge = new JMenuItem("Edge");
		mntmMorphEdge.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.new Performance("Morph", "Edge")
				{
					@Override
					protected Image perform(Image image)
					{
						if (!(image instanceof BinaryImage))
						{
							SwingUtils.errorMessage(dip, String.format(
									"Thin cannot support image type: %s", image
											.getClass().toString()));
							return null;
						}
						Integer type = selectMorphologyStructure();
						if (type == null)
							return null;
						MorphBinary morph = new MorphBinary(type);
						BinaryImage bi = (BinaryImage) image.clone();
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = morph.thin(bi);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnMorphBinary.add(mntmMorphEdge);
	}

	/**
	 * Returns the morphology structure type selected by the user.
	 * 
	 * @return the morphology structure type, {@code null} if the user gives up
	 *         selection
	 */
	private Integer selectMorphologyStructure()
	{
		String[] types = new String[] { "Square", "Diamond", "Eight Corner" };
		Object obj = JOptionPane.showInputDialog(dip,
				"Select the morphology structure:", "Morphology Structure",
				JOptionPane.INFORMATION_MESSAGE, null, types, types[0]);
		if (obj == null)
			return null;
		for (int i = 0; i < types.length; i++)
			if (obj.equals(types[i]))
				return i;
		return 0;
	}
}
