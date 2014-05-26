/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved.
 * ExtractorList.java is PROPRIETARY/CONFIDENTIAL built in 9:13:46 PM, May 3,
 * 2014.
 * Use is subject to license terms.
 */
package com.frank.dip.feature;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * The feature extractor list.
 * <p>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class ExtractorList implements List<FeatureExtractor>
{
	/**
	 * The feature extractors.
	 */
	protected LinkedList<FeatureExtractor>	extractors;

	/**
	 * Construct an instance of <tt>ExtractorList</tt>.
	 */
	public ExtractorList(FeatureExtractor... extractors)
	{
		this.extractors = new LinkedList<FeatureExtractor>();
		for (FeatureExtractor e : extractors)
			this.extractors.add(e);
	}

	/**
	 * Returns the feature size.
	 * <p>
	 * Every time call this method, the feature size will be recalculated.
	 * </p>
	 * 
	 * @return the feature size
	 */
	public int getFeatureSize()
	{
		int size = 0;
		for (FeatureExtractor e : extractors)
			size += e.size();
		return size;
	}

	/**
	 * Extract the features from the specified data.
	 * 
	 * @param data
	 *            the specified data
	 * @param target
	 *            the target value
	 * @return the extracted sample
	 */
	public Sample extract(Object data, Double target)
	{
		Sample s = new Sample(getFeatureSize(), target);
		int beginIndex = 0;
		for (FeatureExtractor extractor : extractors)
			beginIndex = extractor.extract(s, data, beginIndex);
		return s;
	}

	/**
	 * @see java.util.List#size()
	 */
	@Override
	public int size()
	{
		return extractors.size();
	}

	/**
	 * @see java.util.List#isEmpty()
	 */
	@Override
	public boolean isEmpty()
	{
		return extractors.isEmpty();
	}

	/**
	 * @see java.util.List#contains(java.lang.Object)
	 */
	@Override
	public boolean contains(Object o)
	{
		return extractors.contains(o);
	}

	/**
	 * @see java.util.List#iterator()
	 */
	@Override
	public Iterator<FeatureExtractor> iterator()
	{
		return extractors.iterator();
	}

	/**
	 * @see java.util.List#toArray()
	 */
	@Override
	public Object[] toArray()
	{
		return extractors.toArray();
	}

	/**
	 * @see java.util.List#toArray(java.lang.Object[])
	 */
	@Override
	public <T> T[] toArray(T[] a)
	{
		return extractors.toArray(a);
	}

	/**
	 * @see java.util.List#add(java.lang.Object)
	 */
	@Override
	public boolean add(FeatureExtractor e)
	{
		return extractors.add(e);
	}

	/**
	 * @see java.util.List#remove(java.lang.Object)
	 */
	@Override
	public boolean remove(Object o)
	{
		return extractors.remove(o);
	}

	/**
	 * @see java.util.List#containsAll(java.util.Collection)
	 */
	@Override
	public boolean containsAll(Collection<?> c)
	{
		return extractors.containsAll(c);
	}

	/**
	 * @see java.util.List#addAll(java.util.Collection)
	 */
	@Override
	public boolean addAll(Collection<? extends FeatureExtractor> c)
	{
		return extractors.addAll(c);
	}

	/**
	 * @see java.util.List#removeAll(java.util.Collection)
	 */
	@Override
	public boolean removeAll(Collection<?> c)
	{
		return extractors.removeAll(c);
	}

	/**
	 * @see java.util.List#retainAll(java.util.Collection)
	 */
	@Override
	public boolean retainAll(Collection<?> c)
	{
		return extractors.retainAll(c);
	}

	/**
	 * @see java.util.List#clear()
	 */
	@Override
	public void clear()
	{
		extractors.clear();
	}

	/**
	 * @see java.util.List#addAll(int, java.util.Collection)
	 */
	@Override
	public boolean addAll(int index, Collection<? extends FeatureExtractor> c)
	{
		return extractors.addAll(index, c);
	}

	/**
	 * @see java.util.List#get(int)
	 */
	@Override
	public FeatureExtractor get(int index)
	{
		return extractors.get(index);
	}

	/**
	 * @see java.util.List#set(int, java.lang.Object)
	 */
	@Override
	public FeatureExtractor set(int index, FeatureExtractor element)
	{
		return extractors.set(index, element);
	}

	/**
	 * @see java.util.List#add(int, java.lang.Object)
	 */
	@Override
	public void add(int index, FeatureExtractor element)
	{
		extractors.add(index, element);
	}

	/**
	 * @see java.util.List#remove(int)
	 */
	@Override
	public FeatureExtractor remove(int index)
	{
		return extractors.remove(index);
	}

	/**
	 * @see java.util.List#indexOf(java.lang.Object)
	 */
	@Override
	public int indexOf(Object o)
	{
		return extractors.indexOf(o);
	}

	/**
	 * @see java.util.List#lastIndexOf(java.lang.Object)
	 */
	@Override
	public int lastIndexOf(Object o)
	{
		return extractors.lastIndexOf(o);
	}

	/**
	 * @see java.util.List#listIterator()
	 */
	@Override
	public ListIterator<FeatureExtractor> listIterator()
	{
		return extractors.listIterator();
	}

	/**
	 * @see java.util.List#listIterator(int)
	 */
	@Override
	public ListIterator<FeatureExtractor> listIterator(int index)
	{
		return extractors.listIterator(index);
	}

	/**
	 * @see java.util.List#subList(int, int)
	 */
	@Override
	public List<FeatureExtractor> subList(int fromIndex, int toIndex)
	{
		return extractors.subList(fromIndex, toIndex);
	}
}
