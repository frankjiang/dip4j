/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved. PowerLawTransformation.java is PROPRIETARY/CONFIDENTIAL built in
 * 2013. Use is subject to license terms.
 */
package com.frank.dip.enhance.time;

import java.util.Properties;

import com.frank.dip.BinaryImage;
import com.frank.dip.ColorImage;
import com.frank.dip.GrayImage;
import com.frank.dip.Image;

/**
 * Power-law transform.
 * <p>
 * The power-law transformation is transform which specified formula:
 * 
 * <pre>
 * {@code s} = {@code c} * ({@code r} / ({@code L}-1)) ^ <code>&gamma;</code> + {@code b}
 * {@code r} = ( (s - b) / c ) ^ ( 1/<code>&gamma;</code> )
 * {@code c}: the constant value
 * {@code b}: the constant value
 * {@code L}: the scale level
 * <code>&gamma;</code>: the gamma parameter
 * {@code r}: the pixel input
 * {@code s}: the pixel output
 * </pre>
 * 
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @param <T>
 * @version 1.0.0
 */
public abstract class PowerLawTransformation<T extends Image> extends
		EnhanceTransformation<T> implements Transform
{
	/**
	 * The constant value {@code c}.
	 */
	protected double			c;
	/**
	 * The constant value <code>&gamma;</code>.
	 */
	protected double			gamma;
	/**
	 * The constant value {@code b}.
	 */
	protected double			b;
	/**
	 * The string represents the parameter {@code c}.
	 */
	public static final String	PARAM_C		= "c";		//$NON-NLS-1$
	/**
	 * The string represents the parameter {@code b}.
	 */
	public static final String	PARAM_B		= "b";		//$NON-NLS-1$
	/**
	 * The string represents the parameter <code>&gamma;</code>.
	 */
	public static final String	PARAM_GAMMA	= "\u03b3"; //$NON-NLS-1$

	/**
	 * Construct an instance of <tt>PowerLawTransformation</tt> with default
	 * parameters.
	 * 
	 * <pre>
	 * {@code c} = {@linkplain EnhanceTransformation#SCALE_LEVEL} - 1
	 * <code>&gamma;</code> = 1.0
	 * {@code b} = 0.0
	 * </pre>
	 */
	public PowerLawTransformation()
	{
		this.c = SCALE_LEVEL - 1;
		this.b = 0;
		this.gamma = 1;
	}

	/**
	 * Construct an instance of <tt>PowerLawTransformation</tt> with specified
	 * parameters.
	 * 
	 * @param c
	 *            the constant value {@code c}
	 * @param gamma
	 *            the constant value of <code>&gamma;</code>
	 * @param b
	 *            the constant value {@code b}
	 */
	public PowerLawTransformation(double c, double gamma, double b)
	{
		this.c = c;
		this.b = b;
		this.gamma = gamma;
		if (gamma < 0)
			throw new IllegalArgumentException(
					"The parameter \u03b3 must be positive.");
	}

	/**
	 * The binary power-law transformation.
	 * <p>
	 * This class is created for type compatibility. In this transform, no
	 * transform will be performed on the binary image, due to the power-law
	 * transform cannot make any effect in a binary image.
	 * </p>
	 * 
	 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
	 * @version 1.0.0
	 */
	public final static class Binary extends
			PowerLawTransformation<BinaryImage>
	{
		/**
		 * Construct an instance of <tt>PowerLawTransformationBinary</tt> with
		 * default parameters.
		 * 
		 * <pre>
		 * {@code c} = {@linkplain EnhanceTransformation#SCALE_LEVEL} - 1
		 * <code>&gamma;</code> = 1.0
		 * {@code b} = 0.0
		 * </pre>
		 */
		public Binary()
		{
			super();
		}

		/**
		 * Construct an instance of <tt>PowerLawTransformationBinary</tt>with
		 * specified parameters.
		 * 
		 * @param c
		 *            the constant value {@code c}
		 * @param gamma
		 *            the constant value of <code>&gamma;</code>
		 * @param b
		 *            the constant value {@code b}
		 */
		public Binary(double c, double gamma, double b)
		{
			super(c, gamma, b);
		}

		/**
		 * Due to the power-law transformation cannot make any effect in a
		 * binary image, this method returns a copy of the current binary image
		 * without any transform.
		 * 
		 * @see com.frank.dip.enhance.EnhanceTransformationation#operate(com.frank.dip.Image)
		 */
		@Override
		public BinaryImage operate(BinaryImage source)
		{
			return source.clone();
		}

		/**
		 * @see com.frank.dip.enhance.time.Transform#function(double)
		 */
		@Override
		public double function(double r)
		{
			return r;
		}

		/**
		 * @see com.frank.dip.enhance.time.Transform#getInverseTransform()
		 */
		@Override
		public Transform getInverseTransform()
				throws UnsupportedOperationException
		{
			return new Binary();
		}

		/**
		 * @see com.frank.dip.enhance.time.Transform#getFunctionString()
		 */
		@Override
		public String getFunctionString()
		{
			return "s = r";//$NON-NLS-1$
		}

		/**
		 * @see com.frank.dip.enhance.time.Transform#toString()
		 */
		@Override
		public String toString()
		{
			return getFunctionString();
		}
	}

	/**
	 * The gray image power-law transformation.
	 * <p>
	 * In this transform, the power-law transformation will be performed on gray
	 * channel.
	 * </p>
	 * 
	 * <pre>
	 * {@code s} = {@code c} * ({@code r} / ({@code L}-1)) ^ <code>&gamma;</code> + {@code b}
	 * {@code r} = ( (s - b) / c ) ^ ( 1/<code>&gamma;</code> )
	 * {@code c}: the constant value
	 * {@code b}: the constant value
	 * {@code L}: the scale level
	 * <code>&gamma;</code>: the gamma parameter
	 * {@code r}: the pixel input
	 * {@code s}: the pixel output
	 * </pre>
	 * 
	 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
	 * @version 1.0.0
	 */
	public final static class Gray extends PowerLawTransformation<GrayImage>
	{
		/**
		 * Construct an instance of <tt>PowerLawTransformationGray</tt> with
		 * default parameters.
		 * 
		 * <pre>
		 * {@code c} = {@linkplain EnhanceTransformation#SCALE_LEVEL} - 1
		 * <code>&gamma;</code> = 1.0
		 * {@code b} = 0.0
		 * </pre>
		 */
		public Gray()
		{
			super();
		}

		/**
		 * Construct an instance of <tt>PowerLawTransformationGray</tt> with
		 * specified parameters.
		 * 
		 * @param c
		 *            the constant value {@code c}
		 * @param gamma
		 *            the constant value of <code>&gamma;</code>
		 * @param b
		 *            the constant value {@code b}
		 */
		public Gray(double c, double gamma, double b)
		{
			super(c, gamma, b);
		}

		/**
		 * @see com.frank.dip.enhance.time.Transform#getInverseTransform()
		 */
		@Override
		public Transform getInverseTransform()
				throws UnsupportedOperationException
		{
			return new InverseGray(c, gamma, b);
		}
	}

	/**
	 * The color power-law transformation.
	 * <p>
	 * In this transform, the power-law transformation will be performed on red,
	 * green and blue channels.
	 * </p>
	 * 
	 * <pre>
	 * {@code s} = {@code c} * ({@code r} / ({@code L}-1)) ^ <code>&gamma;</code> + {@code b}
	 * {@code r} = ( (s - b) / c ) ^ ( 1/<code>&gamma;</code> )
	 * {@code c}: the constant value
	 * {@code b}: the constant value
	 * {@code L}: the scale level
	 * <code>&gamma;</code>: the gamma parameter
	 * {@code r}: the pixel input
	 * {@code s}: the pixel output
	 * </pre>
	 * 
	 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
	 * @version 1.0.0
	 */
	public static final class Color extends PowerLawTransformation<ColorImage>
	{
		/**
		 * Construct an instance of <tt>PowerLawTransformationColor</tt> with
		 * default parameters.
		 * 
		 * <pre>
		 * {@code c} = {@linkplain EnhanceTransformation#SCALE_LEVEL} - 1
		 * <code>&gamma;</code> = 1.0
		 * {@code b} = 0.0
		 * </pre>
		 */
		public Color()
		{
			super();
		}

		/**
		 * Construct an instance of <tt>PowerLawTransformationColor</tt>with
		 * specified parameters.
		 * 
		 * @param c
		 *            the constant value {@code c}
		 * @param gamma
		 *            the constant value of <code>&gamma;</code>
		 * @param b
		 *            the constant value {@code b}
		 */
		public Color(double c, double gamma, double b)
		{
			super(c, gamma, b);
		}

		/**
		 * @see com.frank.dip.enhance.time.EnhanceTransformation#perform(double)
		 */
		@Override
		public int perform(double r)
		{
			int r0, alpha, red, green, blue;
			r0 = (int) r;
			alpha = r0 >> 24;
			red = (r0 >> 16) & 0xff;
			green = (r0 >> 8) & 0xff;
			blue = r0 & 0xff;
			red = (int) Math.round(function(red));
			green = (int) Math.round(function(green));
			blue = (int) Math.round(function(blue));
			return alpha << 24 | red << 16 | green << 8 | blue;
		}

		/**
		 * @see com.frank.dip.enhance.time.Transform#getInverseTransform()
		 */
		@Override
		public InverseColor getInverseTransform()
				throws UnsupportedOperationException
		{
			return new InverseColor(c, gamma, b);
		}
	}

	/**
	 * The inverse gray image power-law transformation.
	 * <p>
	 * In this transform, the power-law transformation will be performed on gray
	 * channel.
	 * </p>
	 * 
	 * <pre>
	 * {@code s} = {@code c} * ({@code r} / ({@code L}-1)) ^ <code>&gamma;</code> + {@code b}
	 * {@code r} = ( (s - b) / c ) ^ ( 1/<code>&gamma;</code> )
	 * {@code c}: the constant value
	 * {@code b}: the constant value
	 * {@code L}: the scale level
	 * <code>&gamma;</code>: the gamma parameter
	 * {@code r}: the pixel input
	 * {@code s}: the pixel output
	 * </pre>
	 * 
	 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
	 * @version 1.0.0
	 */
	public final static class InverseGray extends
			PowerLawTransformation<GrayImage>
	{
		/**
		 * Construct an instance of <tt>InversePowerLawTransformationGray</tt>
		 * with default parameters.
		 * 
		 * <pre>
		 * {@code c} = {@linkplain EnhanceTransformation#SCALE_LEVEL} - 1
		 * <code>&gamma;</code> = 1.0
		 * {@code b} = 0.0
		 * </pre>
		 */
		public InverseGray()
		{
			super();
		}

		/**
		 * Construct an instance of <tt>InversePowerLawTransformationGray</tt>
		 * with specified parameters.
		 * 
		 * @param c
		 *            the constant value {@code c}
		 * @param gamma
		 *            the constant value of <code>&gamma;</code>
		 * @param b
		 *            the constant value {@code b}
		 */
		public InverseGray(double c, double gamma, double b)
		{
			super(c, gamma, b);
		}

		/**
		 * @see com.frank.dip.enhance.time.Transform#function(double)
		 */
		@Override
		public double function(double r)
		{
			double power = (SCALE_LEVEL - 1) * Math.pow((r - b) / c, 1 / gamma);
			if (power >= 0 && power < SCALE_LEVEL)
				return power;
			else
				return power < 0 ? 0 : (SCALE_LEVEL - 1);
		}

		/**
		 * @see com.frank.dip.enhance.time.Transform#getInverseTransform()
		 */
		@Override
		public Transform getInverseTransform()
				throws UnsupportedOperationException
		{
			return new Gray(c, gamma, b);
		}

		/**
		 * @see com.frank.dip.enhance.time.Transform#getFunctionString()
		 */
		@Override
		public String getFunctionString()
		{
			return "s = (L-1) * ((s-b) / c) ^ (1 / \u03b3)";//$NON-NLS-1$
		}

		/**
		 * @see com.frank.dip.enhance.time.Transform#toString()
		 */
		@Override
		public String toString()
		{
			return String
					.format("r = %d * ((s%+f) / %f) ^ %f", SCALE_LEVEL - 1, -b, c, 1 / gamma);//$NON-NLS-1$
		}
	}

	/**
	 * The inverse color power-law transformation.
	 * <p>
	 * In this transform, the power-law transformation will be performed on red,
	 * green and blue channels.
	 * </p>
	 * 
	 * <pre>
	 * {@code s} = {@code c} * ({@code r} / ({@code L}-1)) ^ <code>&gamma;</code> + {@code b}
	 * {@code r} = ( (s - b) / c ) ^ ( 1/<code>&gamma;</code> )
	 * {@code c}: the constant value
	 * {@code b}: the constant value
	 * {@code L}: the scale level
	 * <code>&gamma;</code>: the gamma parameter
	 * {@code r}: the pixel input
	 * {@code s}: the pixel output
	 * </pre>
	 * 
	 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
	 * @version 1.0.0
	 */
	public static final class InverseColor extends
			PowerLawTransformation<ColorImage>
	{
		/**
		 * Construct an instance of <tt>InversePowerLawTransformationColor</tt>
		 * with default parameters.
		 * 
		 * <pre>
		 * {@code c} = {@linkplain EnhanceTransformation#SCALE_LEVEL} - 1
		 * <code>&gamma;</code> = 1.0
		 * {@code b} = 0.0
		 * </pre>
		 */
		public InverseColor()
		{
			super();
		}

		/**
		 * Construct an instance of <tt>InversePowerLawTransformationColor</tt>
		 * with specified parameters.
		 * 
		 * @param c
		 *            the constant value {@code c}
		 * @param gamma
		 *            the constant value of <code>&gamma;</code>
		 * @param b
		 *            the constant value {@code b}
		 */
		public InverseColor(double c, double gamma, double b)
		{
			super(c, gamma, b);
		}

		/**
		 * @see com.frank.dip.enhance.time.Transform#function(double)
		 */
		@Override
		public double function(double r)
		{
			double power = (SCALE_LEVEL - 1) * Math.pow((r - b) / c, 1 / gamma);
			if (power >= 0 && power < SCALE_LEVEL)
				return power;
			else
				return power < 0 ? 0 : (SCALE_LEVEL - 1);
		}

		/**
		 * @see com.frank.dip.enhance.time.EnhanceTransformation#perform(double)
		 */
		@Override
		public int perform(double r)
		{
			int r0, alpha, red, green, blue;
			r0 = (int) r;
			alpha = r0 >> 24;
			red = (r0 >> 16) & 0xff;
			green = (r0 >> 8) & 0xff;
			blue = r0 & 0xff;
			red = (int) Math.round(function(red));
			green = (int) Math.round(function(green));
			blue = (int) Math.round(function(blue));
			return alpha << 24 | red << 16 | green << 8 | blue;
		}

		/**
		 * @see com.frank.dip.enhance.time.Transform#getInverseTransform()
		 */
		@Override
		public Color getInverseTransform() throws UnsupportedOperationException
		{
			return new Color(c, gamma, b);
		}

		/**
		 * @see com.frank.dip.enhance.time.Transform#getFunctionString()
		 */
		@Override
		public String getFunctionString()
		{
			return "s = (L-1) * ((s-b) / c) ^ (1 / \u03b3)";//$NON-NLS-1$
		}

		/**
		 * @see com.frank.dip.enhance.time.Transform#toString()
		 */
		@Override
		public String toString()
		{
			return String
					.format("r = %d * ((s%+f) / %f) ^ %f", SCALE_LEVEL - 1, -b, c, 1 / gamma);//$NON-NLS-1$
		}
	}

	/**
	 * @see com.frank.dip.enhance.time.Transform#getProperties()
	 */
	@Override
	public Properties getProperties()
	{
		Properties p = new Properties();
		p.put(PARAM_C, c);
		p.put(PARAM_B, b);
		p.put(PARAM_GAMMA, gamma);
		return p;
	}

	/**
	 * @see com.frank.dip.enhance.time.Transform#setProperties(java.util.Properties)
	 */
	@Override
	public void setProperties(Properties p)
	{
		Object obj = null;
		obj = p.get(PARAM_C);
		if (obj != null && obj instanceof Number)
			this.c = ((Number) obj).doubleValue();
		obj = p.get(PARAM_B);
		if (obj != null && obj instanceof Number)
			this.b = ((Number) obj).doubleValue();
		obj = p.get(PARAM_GAMMA);
		if (obj != null && obj instanceof Number)
			this.gamma = ((Number) obj).doubleValue();
	}

	/**
	 * @see com.frank.dip.enhance.time.Transform#getFunctionString()
	 */
	@Override
	public String getFunctionString()
	{
		return "s = c * (r / (L-1)) ^ \u03b3 + b";//$NON-NLS-1$
	}

	/**
	 * @see com.frank.dip.enhance.time.Transform#toString()
	 */
	@Override
	public String toString()
	{
		return toFunction();
	}

	/**
	 * @see com.frank.dip.math.Function#toFunction()
	 */
	@Override
	public String toFunction()
	{
		return String.format(
				"s = %f*(r/%d)^%f%+f", c, SCALE_LEVEL - 1, gamma, b);//$NON-NLS-1$
	}

	/**
	 * Getter for the constant value c.
	 * 
	 * @return {@code c}
	 */
	public double getC()
	{
		return c;
	}

	/**
	 * Setter for the constant value {@code c}.
	 * 
	 * @param c
	 *            the value of {@code c}
	 */
	public void setC(double c)
	{
		this.c = c;
	}

	/**
	 * Getter for constant value <code>&gamma;</code>.
	 * 
	 * @return <code>&gamma;</code>.
	 */
	public double getGamma()
	{
		return gamma;
	}

	/**
	 * Setter for <code>&gamma;</code>.
	 * 
	 * @param gamma
	 *            the value of <code>&gamma;</code>.
	 */
	public void setGamma(double gamma)
	{
		this.gamma = gamma;
	}

	/**
	 * Getter for the constant value {@code b}.
	 * 
	 * @return {@code b}
	 */
	public double getB()
	{
		return b;
	}

	/**
	 * Setter for the constant value {@code b}.
	 * 
	 * @param b
	 *            the value of {@code b}
	 */
	public void setB(double b)
	{
		this.b = b;
	}

	/**
	 * @see com.frank.dip.enhance.time.Transform#function(double)
	 */
	public double function(double r)
	{
		double power = c * Math.pow(r / (SCALE_LEVEL - 1), gamma) + b;
		if (power >= 0 && power < SCALE_LEVEL)
			return (int) power;
		else
			return power < 0 ? 0 : (SCALE_LEVEL - 1);
	}

	/**
	 * Perform power-law transformation to the specified image.
	 * 
	 * <pre>
	 * {@code s} = {@code c} * ({@code r} / ({@code L}-1)) ^ <code>&gamma;</code> + {@code b}
	 * </pre>
	 * 
	 * @param source
	 *            the source image
	 * @param c
	 *            the constant value {@code c}
	 * @param gamma
	 *            the constant value <code>&gamma;</code>
	 * @param b
	 *            the constant value {@code b}
	 * @return the transformed image
	 * @throws IllegalArgumentException
	 *             if the image type not supported
	 */
	public static <T extends Image> T poweLaw(T source, double c, double gamma,
			double b) throws IllegalArgumentException
	{
		if (source instanceof GrayImage)
			return (T) (new Gray(c, gamma, b).operate((GrayImage) source));
		if (source instanceof BinaryImage)
			return (T) (new Binary(c, gamma, b).operate((BinaryImage) source));
		if (source instanceof ColorImage)
			return (T) (new Color(c, gamma, b).operate((ColorImage) source));
		throw new IllegalArgumentException(
				String.format(
						"Current power-law transformationation cannot support image type: %s",
						source.getClass().toString()));
	}

	/**
	 * Get power-law transformation according to the specified image type.
	 * 
	 * <pre>
	 * {@code s} = {@code c} * ({@code r} / ({@code L}-1)) ^ <code>&gamma;</code> + {@code b}
	 * </pre>
	 * 
	 * @param type
	 *            the type of source image
	 * @param c
	 *            the constant value {@code c}
	 * @param gamma
	 *            the constant value <code>&gamma;</code>
	 * @param b
	 *            the constant value {@code b}
	 * @return the transform
	 * @throws IllegalArgumentException
	 *             if the image type not supported
	 */
	public static <T extends Image> PowerLawTransformation<T> getPowerLawTransformation(
			Class<T> type, double c, double gamma, double b)
			throws IllegalArgumentException
	{
		if (type == GrayImage.class)
			return (PowerLawTransformation<T>) new Gray(c, gamma, b);
		if (type == BinaryImage.class)
			return (PowerLawTransformation<T>) new Binary(c, gamma, b);
		if (type == ColorImage.class)
			return (PowerLawTransformation<T>) new Color(c, gamma, b);
		throw new IllegalArgumentException(
				String.format(
						"Current power-law transformationation cannot support image type: %s",
						type.toString()));
	}

	/**
	 * Get inverse power-law transformation according to the specified image
	 * type.
	 * 
	 * <pre>
	 * {@code r} = ( (s - b) / c ) ^ ( 1/<code>&gamma;</code> )
	 * </pre>
	 * 
	 * @param source
	 *            the source image
	 * @param c
	 *            the constant value {@code c}
	 * @param gamma
	 *            the constant value <code>&gamma;</code>
	 * @param b
	 *            the constant value {@code b}
	 * @return the transform
	 * @throws IllegalArgumentException
	 *             if the image type not supported
	 */
	public static PowerLawTransformation inversePowerLawTransformation(
			Image source, double c, double gamma, double b)
			throws IllegalArgumentException
	{
		if (source instanceof GrayImage)
			return new InverseGray(c, gamma, b);
		if (source instanceof BinaryImage)
			return new Binary(c, gamma, b);
		if (source instanceof ColorImage)
			return new InverseColor(c, gamma, b);
		throw new IllegalArgumentException(
				String.format(
						"Current power-law transformationation cannot support image type: %s",
						source.getClass().toString()));
	}

	/**
	 * Perform inverse power-law transform to the specified image.
	 * 
	 * <pre>
	 * {@code r} = ( (s - b) / c ) ^ ( 1/<code>&gamma;</code> )
	 * </pre>
	 * 
	 * @param source
	 *            the source image
	 * @param c
	 *            the constant value {@code c}
	 * @param b
	 *            the constant value {@code b}
	 * @return the transformed image
	 * @throws IllegalArgumentException
	 *             if the image type not supported
	 */
	public static <T extends Image> T inversePowerLaw(T source, double c,
			double gamma, double b) throws IllegalArgumentException
	{
		if (source instanceof GrayImage)
			return (T) (new InverseGray(c, gamma, b)
					.operate((GrayImage) source));
		if (source instanceof BinaryImage)
			return (T) (new Binary(c, gamma, b).operate((BinaryImage) source));
		if (source instanceof ColorImage)
			return (T) (new InverseColor(c, gamma, b)
					.operate((ColorImage) source));
		throw new IllegalArgumentException(
				String.format(
						"Current power-law transformationation cannot support image type: %s",
						source.getClass().toString()));
	}
}
