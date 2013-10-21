package com.frank.dip.demo;

import java.io.File;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import com.frank.dip.GrayImage;
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
import com.frank.dip.threshold.Thresholding;
import com.frank.dip.threshold.Triangle;
import com.frank.dip.threshold.Yen;
import com.frank.sys.TestUtils;
import com.frank.sys.TestUtils.Timer;

/**
 * Test class for statistic the time spent of thresholding algorithms.
 * <p>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class StatisticThresholdingTime
{
	public static void main(String[] args) throws Exception
	{
		GrayImage gi = new GrayImage(ImageIO.read(new File("big.png")), null);
		Timer t = TestUtils.getTimer();
		TimeUnit timeunit = TimeUnit.MILLISECONDS;
		TreeMap<String, Long> map = new TreeMap<String, Long>();
		int times = 100;
		long time = 0;
		for (int i = 0; i < times; i++)
		{
			Thresholding th = new FBClustering();
			t.start();
			th.operate(gi);
			time = t.getTime(timeunit);
			time = t.getTime(timeunit);
			put(map, getName(th), time);
			th = new FrankThresholding();
			t.start();
			th.operate(gi);
			time = t.getTime(timeunit);
			put(map, getName(th), time);
			th = new GlobalThresholding(new Fuzzy());
			t.start();
			th.operate(gi);
			time = t.getTime(timeunit);
			put(map, getName(th), time);
			th = new GlobalThresholding(new Intermodes());
			t.start();
			th.operate(gi);
			time = t.getTime(timeunit);
			put(map, getName(th), time);
			th = new GlobalThresholding(new IsoData());
			t.start();
			th.operate(gi);
			time = t.getTime(timeunit);
			put(map, getName(th), time);
			th = new GlobalThresholding(new MaxEntropy());
			t.start();
			th.operate(gi);
			time = t.getTime(timeunit);
			put(map, getName(th), time);
			th = new GlobalThresholding(new Mean());
			t.start();
			th.operate(gi);
			time = t.getTime(timeunit);
			put(map, getName(th), time);
			th = new GlobalThresholding(new MinCrossEntropy());
			t.start();
			th.operate(gi);
			time = t.getTime(timeunit);
			put(map, getName(th), time);
			th = new GlobalThresholding(new MinError());
			t.start();
			th.operate(gi);
			time = t.getTime(timeunit);
			put(map, getName(th), time);
			th = new GlobalThresholding(new Minimum());
			t.start();
			th.operate(gi);
			time = t.getTime(timeunit);
			put(map, getName(th), time);
			th = new GlobalThresholding(new Moments());
			t.start();
			th.operate(gi);
			time = t.getTime(timeunit);
			put(map, getName(th), time);
			th = new GlobalThresholding(new Otsu());
			t.start();
			th.operate(gi);
			time = t.getTime(timeunit);
			put(map, getName(th), time);
			th = new GlobalThresholding(new Percentile());
			t.start();
			th.operate(gi);
			time = t.getTime(timeunit);
			put(map, getName(th), time);
			th = new GlobalThresholding(new RenyiEntropy());
			t.start();
			th.operate(gi);
			time = t.getTime(timeunit);
			put(map, getName(th), time);
			th = new GlobalThresholding(new Shanbhag());
			t.start();
			th.operate(gi);
			time = t.getTime(timeunit);
			put(map, getName(th), time);
			th = new GlobalThresholding(new Triangle());
			t.start();
			th.operate(gi);
			time = t.getTime(timeunit);
			put(map, getName(th), time);
			th = new GlobalThresholding(new Yen());
			t.start();
			th.operate(gi);
			time = t.getTime(timeunit);
			put(map, getName(th), time);
		}
		Entry<String, Long> max = null, min = null;
		String name;
		for (Entry<String, Long> e : map.entrySet())
		{
			name = e.getKey();
			time = e.getValue();
			System.out.printf("%s = %d %s\r\n", name, time, timeunit.toString());
			if (max == null || max.getValue() < time)
				max = e;
			if (min == null || min.getValue() > time)
				min = e;
		}
		System.out.println("---------------------------------------------");
		System.out.printf("Max:\t%.2f %s - %s\r\n", max.getValue()
				/ (double) times, timeunit.toString(), max.getKey());
		System.out.printf("Min:\t%.2f %s - %s\r\n", min.getValue()
				/ (double) times, timeunit.toString(), min.getKey());
	}

	public static void put(TreeMap<String, Long> map, String key, Long value)
	{
		if (map.containsKey(key))
			map.put(key, map.get(key) + value);
		else
			map.put(key, value);
	}

	public static String getName(Thresholding th)
	{
		String s = null;
		if (th instanceof GlobalThresholding)
		{
			GlobalThresholding gth = (GlobalThresholding) th;
			s = gth.getFinder().getClass().toString();
			s = s.substring(s.lastIndexOf('.') + 1, s.length());
		}
		else
		{
			s = th.getClass().toString();
			s = s.substring(s.lastIndexOf('.') + 1, s.length());
		}
		return s;
	}
}
