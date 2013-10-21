/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved. Triangle.java is PROPRIETARY/CONFIDENTIAL built in 2013. Use is
 * subject to license terms.
 */
package com.frank.dip.threshold;

import com.frank.dip.GrayImage;

/**
 * Triangle threshold selecting algorithm.
 * <p>
 * The Triangle algorithm, a geometric method, cannot tell whether the data is
 * skewed to one side or another, but assumes a maximum peak (mode) near one end
 * of the histogram and searches towards the other end. This causes a problem in
 * the absence of information of the type of image to be processed, or when the
 * maximum is not near one of the histogram extremes (resulting in two possible
 * threshold regions between that max and the extremes). Here the algorithm was
 * extended to find on which side of the max peak the data goes the furthest and
 * searches for the threshold within that largest range.
 * </p>
 * <p>
 * See the source paper at: <br>
 * <a href="http://www.jhc.org/cgi/pmidlookup?view=long&pmid=70454">Zack GW,
 * Rogers WE, Latt SA (1977),
 * "Automatic measurement of sister chromatid exchange frequency", J. Histochem.
 * Cytochem. 25 (7): 741–53, PMID 70454 </a> <br>
 * see also: <a href=
 * "http://www.ph.tn.tudelft.nl/Courses/FIP/noframes/fip-Segmenta.html#Heading118"
 * >http://www.ph.tn.tudelft.nl/Courses/FIP/noframes/fip-Segmenta.html#
 * Heading118</a>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class Triangle implements ThresholdFinder
{
	/**
	 * @see com.frank.dip.threshold.ThresholdFinder#threshold(com.frank.dip.GrayImage)
	 */
	@Override
	public int threshold(GrayImage image)
	{
		int[] data = new int[256];
		for (int pixel : image.getPixelsArray())
			data[pixel]++;
		int min = 0, dmax = 0, max = 0, min2 = 0;
		for (int i = 0; i < data.length; i++)
		{
			if (data[i] > 0)
			{
				min = i;
				break;
			}
		}
		if (min > 0)
			min--; // line to the (p==0) point, not to data[min]
		// The Triangle algorithm cannot tell whether the data is skewed to one
		// side or another.
		// This causes a problem as there are 2 possible thresholds between the
		// max and the 2 extremes
		// of the histogram.
		// Here I propose to find out to which side of the max point the data is
		// furthest, and use that as
		// the other extreme.
		for (int i = 255; i > 0; i--)
		{
			if (data[i] > 0)
			{
				min2 = i;
				break;
			}
		}
		if (min2 < 255)
			min2++; // line to the (p==0) point, not to data[min]
		for (int i = 0; i < 256; i++)
		{
			if (data[i] > dmax)
			{
				max = i;
				dmax = data[i];
			}
		}
		// find which is the furthest side
		// IJ.log(""+min+" "+max+" "+min2);
		boolean inverted = false;
		if ((max - min) < (min2 - max))
		{
			// reverse the histogram
			// IJ.log("Reversing histogram.");
			inverted = true;
			int left = 0; // index of leftmost element
			int right = 255; // index of rightmost element
			while (left < right)
			{
				// exchange the left and right elements
				int temp = data[left];
				data[left] = data[right];
				data[right] = temp;
				// move the bounds toward the center
				left++;
				right--;
			}
			min = 255 - min2;
			max = 255 - max;
		}
		if (min == max)
			return min;
		// describe line by nx * x + ny * y - d = 0
		double nx, ny, d;
		// nx is just the max frequency as the other point has freq=0
		nx = data[max]; // -min; // data[min]; // lowest value bmin = (p=0)% in
						// the image
		ny = min - max;
		d = Math.sqrt(nx * nx + ny * ny);
		nx /= d;
		ny /= d;
		d = nx * min + ny * data[min];
		// find split point
		int split = min;
		double splitDistance = 0;
		for (int i = min + 1; i <= max; i++)
		{
			double newDistance = nx * i + ny * data[i] - d;
			if (newDistance > splitDistance)
			{
				split = i;
				splitDistance = newDistance;
			}
		}
		split--;
		if (inverted)
		{
			// The histogram might be used for something else, so let's reverse
			// it back
			int left = 0;
			int right = 255;
			while (left < right)
			{
				int temp = data[left];
				data[left] = data[right];
				data[right] = temp;
				left++;
				right--;
			}
			return (255 - split);
		}
		else
			return split;
	}

	/**
	 * @see com.frank.dip.threshold.ThresholdFinder#getFinderName()
	 */
	@Override
	public String getFinderName()
	{
		return "Triangle";
	}
}
