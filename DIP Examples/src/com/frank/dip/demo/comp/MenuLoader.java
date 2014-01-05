/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights reserved.
 * MenuComponent.java is PROPRIETARY/CONFIDENTIAL built in 2013.
 * Use is subject to license terms.
 */
package com.frank.dip.demo.comp;

import java.util.concurrent.TimeUnit;

import javax.swing.JMenu;

import com.frank.dip.demo.DIPFrame;

/**
 * The menu component loader.
 * <p>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public abstract class MenuLoader
{
	/**
	 * The time unit.
	 */
	protected TimeUnit	timeunit	= TimeUnit.MILLISECONDS;
	/**
	 * The digital image processing frame.
	 */
	protected DIPFrame	dip;
	/**
	 * The menu to for the new menu to add on. If <code>null</code>, the new
	 * menu will be add on the root menu bar.
	 */
	protected JMenu		menu;

	/**
	 * Construct an instance of <tt>MenuComponent</tt>.
	 * 
	 * @param dip
	 *            the digital image processing frame
	 * @param menu
	 *            the menu to for the new menu to add on, if <code>null</code>,
	 *            the new menu will be add on the root menu bar
	 * @param args
	 *            the other configuration arguments
	 */
	public MenuLoader(DIPFrame dip, JMenu menu, Object... args)
	{
		this.dip = dip;
		this.menu = menu;
		load(dip, args);
	}

	/**
	 * Load the components to the frame.
	 * 
	 * @param args
	 *            the configuration arguments
	 */
	protected abstract void load(Object... args);
}
