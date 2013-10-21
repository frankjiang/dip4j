/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved.
 * ObservablePerformanceManager.java is PROPRIETARY/CONFIDENTIAL built in 2013.
 * Use is subject to license terms.
 */
package com.frank.dip.demo;

import com.frank.swing.PerformanceManager;

/**
 * A performance manager which can notify the observers when it changes its
 * content.
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class ObservablePerformanceManager<T> extends PerformanceManager<T>
{
	/**
	 * Construct an instance of <tt>ObservablePerformanceManager</tt>.
	 */
	public ObservablePerformanceManager()
	{
		super();
	}

	/**
	 * Construct an instance of <tt>ObservablePerformanceManager</tt>.
	 * 
	 * @param maxStorage
	 */
	public ObservablePerformanceManager(int maxStorage)
	{
		super(maxStorage);
	}

	/**
	 * Reset the steps and clear the memory. After reset, notify all the
	 * observers.
	 * 
	 * @see com.frank.swing.PerformanceManager#reset()
	 */
	public void reset()
	{
		super.reset();
		setChanged();
		notifyObservers();
	}

	/**
	 * Perform one step and record. After reset, notify all the
	 * observers.
	 * 
	 * @see com.frank.swing.PerformanceManager#perform(java.lang.Object)
	 */
	public void perform(T record)
	{
		super.perform(record);
		setChanged();
		notifyObservers();
	}
}
