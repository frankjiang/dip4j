/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved.
 * Li.java is PROPRIETARY/CONFIDENTIAL built in 2013.
 * Use is subject to license terms.
 */
package com.frank.dip.threshold;

import com.frank.dip.GrayImage;

/**
 * Minimum cross entropy thresholding method.
 * <p>
 * Implements Li's Minimum Cross Entropy thresholding method based on the
 * iterative version (2nd reference below) of the algorithm.
 * </p>
 * <p>
 * See source paper at:<br>
 * <a href="http://citeseer.ist.psu.edu/sezgin04survey.html"> Li, CH & Lee, CK
 * (1993), "Minimum Cross Entropy Thresholding", Pattern Recognition 26(4):
 * 617-625</a><br>
 * Li, CH & Tam, PKS (1998),
 * "An Iterative Algorithm for Minimum Cross Entropy Thresholding", Pattern
 * Recognition Letters 18(8): 771-776<br>
 * Sezgin, M & Sankur, B (2004),
 * "Survey over Image Thresholding Techniques and Quantitative Performance Evaluation"
 * , Journal of Electronic Imaging 13(1): 146-165
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class MinCrossEntropy implements ThresholdFinder
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
		int threshold;
		int ih;
		int num_pixels;
		int sum_back; // sum of the background pixels at a given threshold
		int sum_obj; // sum of the object pixels at a given threshold
		int num_back; // number of background pixels at a given threshold
		int num_obj; // number of object pixels at a given threshold
		double old_thresh;
		double new_thresh;
		double mean_back; // mean of the background pixels at a given threshold
		double mean_obj; // mean of the object pixels at a given threshold
		double mean; // mean gray-level in the image
		double tolerance; // threshold tolerance
		double temp;
		tolerance = 0.5;
		num_pixels = 0;
		for (ih = 0; ih < 256; ih++)
			num_pixels += data[ih];
		// Calculate the mean gray-level
		mean = 0.0;
		for (ih = 0 + 1; ih < 256; ih++)
			mean += ih * data[ih];
		mean /= num_pixels;
		// Initial estimate
		new_thresh = mean;
		do
		{
			old_thresh = new_thresh;
			threshold = (int) (old_thresh + 0.5); // range
			// Calculate the means of background and object pixels
			/* Background */
			sum_back = 0;
			num_back = 0;
			for (ih = 0; ih <= threshold; ih++)
			{
				sum_back += ih * data[ih];
				num_back += data[ih];
			}
			mean_back = (num_back == 0 ? 0.0 : (sum_back / (double) num_back));
			/* Object */
			sum_obj = 0;
			num_obj = 0;
			for (ih = threshold + 1; ih < 256; ih++)
			{
				sum_obj += ih * data[ih];
				num_obj += data[ih];
			}
			mean_obj = (num_obj == 0 ? 0.0 : (sum_obj / (double) num_obj));
			/* Calculate the new threshold: Equation (7) in Ref. 2 */
			// new_thresh = simple_round ( ( mean_back - mean_obj ) / ( Math.log
			// ( mean_back ) - Math.log ( mean_obj ) ) );
			// simple_round ( double x ) {
			// return ( int ) ( IS_NEG ( x ) ? x - .5 : x + .5 );
			// }
			//
			// #define IS_NEG( x ) ( ( x ) < -DBL_EPSILON )
			// DBL_EPSILON = 2.220446049250313E-16
			temp = (mean_back - mean_obj)
					/ (Math.log(mean_back) - Math.log(mean_obj));
			if (temp < -2.220446049250313E-16)
				new_thresh = (int) (temp - 0.5);
			else
				new_thresh = (int) (temp + 0.5);
			/*
			 * Stop the iterations when the difference between the
			 * new and old threshold values is less than the tolerance
			 */
		}
		while (Math.abs(new_thresh - old_thresh) > tolerance);
		return threshold;
	}

	/**
	 * @see com.frank.dip.threshold.ThresholdFinder#getFinderName()
	 */
	@Override
	public String getFinderName()
	{
		return "minimum cross entropy";
	}
}
