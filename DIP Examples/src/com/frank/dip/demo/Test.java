package com.frank.dip.demo;

import javax.imageio.ImageIO;
import javax.swing.JDialog;

import com.frank.dip.ColorScaleLevel;
import com.frank.dip.GrayImage;
import com.frank.dip.Image;
import com.frank.dip.analyze.FourierTransformation;
import com.frank.swing.SwingUtils;

public class Test implements ColorScaleLevel
{
	/**
	 * The angle of a fan shape for a kind of color channel.
	 */
	public static float	FAN_ANGLE			= (float) (Math.PI * 2 / 3);
	/**
	 * The angle of two fan shapes for color channels.
	 */
	public static float	DOUBLE_FAN_ANGLE	= (float) (Math.PI * 4 / 3);
	/**
	 * A perigon.
	 */
	public static float	PERIGON				= (float) (Math.PI * 2);
	/**
	 * An angle of 60 degree.
	 */
	public static float	ANGLE_60			= (float) (Math.PI / 3);

	/**
	 * Try encode.
	 * 
	 * @param rgb
	 * @param hsi
	 * @param rgb2
	 */
	public static void f(int[] rgb, float[] hsi, float[] rgb2)
	{
		float h, s, i;
		// encode
		{
			int r = rgb[0], g = rgb[1], b = rgb[2], rb, rg, gb, min;
			float theta, sum;
			rb = r - b;
			rg = r - g;
			gb = g - b;
			sum = r + g + b;
			// transform
			// ----------------------------
			// theta
			theta = (float) ((rg + rb) / Math.sqrt(rg * rg + rb * gb) / 2);
			theta = (float) Math.acos(theta);
			// hue
			if (b > g)
				theta = PERIGON - theta;
			h = theta;
			hsi[0] = h;
			// saturation
			min = r;
			if (g < min)
				min = g;
			if (b < min)
				min = b;
			s = 1f - 3 * min / sum;
			hsi[1] = s;
			// intensity
			i = sum / 3;
			hsi[2] = i;
			// ----------------------------
		}
		// decode
		{
			float r, g, b;
			if (h < FAN_ANGLE)
			{
				b = i * (1 - s);
				r = (float) (i * (1 + Math.cos(h) * s / Math.cos(ANGLE_60 - h)));
				g = 3 * i - r - b;
			}
			else if (h < DOUBLE_FAN_ANGLE)
			{
				h -= FAN_ANGLE;
				r = i * (1 - s);
				g = (float) (i * (1 + Math.cos(h) * s / Math.cos(ANGLE_60 - h)));
				b = 3 * i - r - g;
			}
			else
			{
				h -= DOUBLE_FAN_ANGLE;
				g = i * (1 - s);
				b = (float) (i * (1 + Math.cos(h) * s / Math.cos(ANGLE_60 - h)));
				r = 3 * i - g - b;
			}
			rgb2[0] = r;
			rgb2[1] = g;
			rgb2[2] = b;
		}
	}

	public static final String	TEST_IMAGE_PATH	= "/com/frank/dip/res/lena-gray.png";	//$NON-NLS-1$;

	/**
	 * Main.
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception
	{
		Image source = new GrayImage(ImageIO.read(Test.class
				.getResourceAsStream(TEST_IMAGE_PATH)), null);
		FourierTransformation ft = new FourierTransformation();
		ft.forward(source);
		GrayImage gi = ft.backward();
		ImageDisplayDialog idd1 = new ImageDisplayDialog(null, "Source", false,
				source);
		ImageDisplayDialog idd2 = new ImageDisplayDialog(null, "Result", false,
				gi);
		SwingUtils.setSystemLookAndFeel(idd1);
		SwingUtils.setSystemLookAndFeel(idd2);
		idd1.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		idd2.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		idd1.setVisible(true);
		idd2.setVisible(true);
	}
}
