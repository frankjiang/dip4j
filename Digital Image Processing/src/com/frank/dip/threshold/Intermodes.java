/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved.
 * Intermodes.java is PROPRIETARY/CONFIDENTIAL built in 2013.
 * Use is subject to license terms.
 */
package com.frank.dip.threshold;

import com.frank.dip.GrayImage;

/**
 * Intermodes threshold selecting method.
 * <p>
 * This assumes a bimodal histogram. The histogram is iteratively smoothed using
 * a running average of size 3, until there are only two local maxima: j and k.
 * The threshold t is then computed as (j+k)/2. Images with histograms having
 * extremely unequal peaks or a broad and ﬂat valley are unsuitable for this
 * method. method
 * </p>
 * <p>
 * See the source paper at: <br>
 * <a href="http://www3.interscience.
 * wiley.com/journal/119758871/abstract?CRETRY=1&SRETRY=0"> Prewitt, JMS &
 * Mendelsohn, ML (1966), "The analysis of cell images", Annals of the New York
 * Academy of Sciences 128: 1035-1053</a>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class Intermodes extends BimodalThreshold
{
	/**
	 * @see com.frank.dip.threshold.ThresholdFinder#threshold(com.frank.dip.GrayImage)
	 */
	@Override
	public int threshold(GrayImage image)
	{
		int L = 256;
		int[] data = new int[L];
		for (int pixel : image.getPixelsArray())
			data[pixel]++;
		double[] iHisto = new double[256];
		int iter = 0;
		int threshold = -1;
		for (int i = 0; i < L; i++)
			iHisto[i] = (double) data[i];
		double[] tHisto = iHisto;
		while (!bimodalTest(iHisto))
		{
			// smooth with a 3 point running mean filter
			for (int i = 1; i < L - 1; i++)
				tHisto[i] = (iHisto[i - 1] + iHisto[i] + iHisto[i + 1]) / 3;
			tHisto[0] = (iHisto[0] + iHisto[1]) / 3; // 0 outside
			tHisto[255] = (iHisto[254] + iHisto[255]) / 3; // 0 outside
			iHisto = tHisto;
			iter++;
			if (iter > maxIteration)
			{
				threshold = -1;
				System.err.printf(
						"Intermodes Threshold not found after %d iterations.",
						maxIteration);
				return threshold;
			}
		}
		// The threshold is the mean between the two peaks.
		int tt = 0;
		for (int i = 1; i < 255; i++)
			if (iHisto[i - 1] < iHisto[i] && iHisto[i + 1] < iHisto[i])
				tt += i;
		threshold = (int) Math.floor(tt / 2.0);
		return threshold;
	}

	/**
	 * @see com.frank.dip.threshold.ThresholdFinder#getFinderName()
	 */
	@Override
	public String getFinderName()
	{
		return "intermodes";
	}
}
