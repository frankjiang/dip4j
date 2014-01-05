/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights reserved.
 * MenuFilter.java is PROPRIETARY/CONFIDENTIAL built in 2013.
 * Use is subject to license terms.
 */
package com.frank.dip.demo.comp;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import com.frank.dip.Image;
import com.frank.dip.demo.DIPFrame;
import com.frank.dip.enhance.convolver.ConvolveDialog;
import com.frank.dip.enhance.convolver.Convolver;
import com.frank.sys.TestUtils;
import com.frank.sys.TestUtils.Timer;

/**
 * The Filter menu loader.
 * <p>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class MenuFilter extends MenuLoader
{
	/**
	 * Construct an instance of <tt>MenuFilter</tt>.
	 * 
	 * @param dip
	 *            the digital image processing frame
	 */
	public MenuFilter(DIPFrame dip)
	{
		super(dip, null);
	}

	/**
	 * @see com.frank.dip.demo.comp.MenuLoader#load(java.lang.Object[])
	 */
	@Override
	protected void load(Object... args)
	{
		JMenu mnFilter = new JMenu("Filter(I)");
		mnFilter.setMnemonic('I');
		dip.getBar().add(mnFilter);
		// initialize linear smooth filter menu
		new MenuFilterNoise(dip, mnFilter);
		// initialize sharpen filter menu
		new MenuFilterSharpen(dip, mnFilter);
		// initialize convolve enhance filter menu
		new MenuFilterConvolveEnhance(dip, mnFilter);
		// initialize frequency filter menu
		new MenuFilterFrequency(dip, mnFilter);
		// custom convolution
		JMenuItem mntmCustom = new JMenuItem("Custom");
		mntmCustom.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.new Performance("Filter", "Custom Convolve")
				{
					@Override
					protected Image perform(Image image)
					{
						ConvolveDialog dialog = new ConvolveDialog(
								dip);
						dialog.setVisible(true);
						Convolver convolver = dialog.getConvolver();
						if (convolver == null)
							return null;
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = convolver.operate(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnFilter.add(mntmCustom);
	}
}
