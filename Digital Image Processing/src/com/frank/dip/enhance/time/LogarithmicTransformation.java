/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved. LogarithmicTransformation.java is PROPRIETARY/CONFIDENTIAL built in
 * 2013. Use is subject to license terms.
 */
package com.frank.dip.enhance.time;

import java.util.Properties;

import com.frank.dip.BinaryImage;
import com.frank.dip.ColorImage;
import com.frank.dip.GrayImage;
import com.frank.dip.IllegalImageTypeException;
import com.frank.dip.Image;

/**
 * Logarithmic transformation.
 * <p>
 * The logarithmic transformation is transformation which specified formula:
 * 
 * <pre>
 * {@code s} = {@code c} * log(1 + {@code r}) + {@code b}
 * {@code r} = e<sup>({@code s}-{@code b})/{@code c}</sup> - 1
 * {@code c}: the constant value
 * {@code b}: the constant value
 * {@code r}: the pixel input
 * {@code s}: the pixel output
 * </pre>
 * 
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @param <T>
 *            the image type
 * @version 1.0.0
 */
public abstract class LogarithmicTransformation<T extends Image> extends
		EnhanceTransformation<T> implements Transform
{
	/**
	 * The string represents the parameter {@code c}.
	 */
	public static final String	PARAM_C	= "c";	//$NON-NLS-1$
	/**
	 * The string represents the parameter {@code b}.
	 */
	public static final String	PARAM_B	= "b";	//$NON-NLS-1$

	/**
	 * Construct an instance of <tt>LogarithmicTransformation</tt> with default
	 * parameters.
	 * 
	 * <pre>
	 * {@code c} = {@linkplain EnhanceTransformation#SCALE_LEVEL} - 1 / log({@linkplain EnhanceTransformation#SCALE_LEVEL})
	 * {@code b} = 0.0
	 * </pre>
	 */
	public LogarithmicTransformation()
	{
		this(SCALE_LEVEL / Math.log(SCALE_LEVEL), 0.0);
	}

	/**
	 * Construct an instance of <tt>LogarithmicTransformation</tt> with
	 * specified constant value {@code c}.
	 * 
	 * @param c
	 *            the constant value {@code c}
	 * @param b
	 *            the constant value {@code b}
	 */
	public LogarithmicTransformation(double c, double b)
	{
		this.c = c;
		this.b = b;
	}

	/**
	 * The binary logarithm transformation.
	 * <p>
	 * This class is created for type compatibility. In this transformation, no
	 * transformation will be performed on the binary image, due to the
	 * logarithmic transformation cannot make any effect in a binary image.
	 * </p>
	 * 
	 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
	 * @version 1.0.0
	 */
	public static final class Binary extends
			LogarithmicTransformation<BinaryImage>
	{
		/**
		 * Construct an instance of <tt>LogarithmicTransformationBinary</tt>
		 * with specified constant value {@code c} and {@code b}.
		 * 
		 * @param c
		 *            the constant value {@code c}
		 * @param b
		 *            the constant value {@code b}
		 */
		public Binary(double c, double b)
		{
			super(c, b);
		}

		/**
		 * Construct an instance of <tt>LogarithmicTransformationBinary</tt>
		 * with default parameters.
		 * 
		 * <pre>
		 * {@code c} = {@linkplain EnhanceTransformation#SCALE_LEVEL} - 1 / log({@linkplain EnhanceTransformation#SCALE_LEVEL})
		 * {@code b} = 0.0
		 * </pre>
		 */
		public Binary()
		{
			super();
		}

		/**
		 * Due to the logarithmic transformation cannot make any effect in a
		 * binary image, this method returns a copy of the current binary image
		 * without any transformation.
		 * 
		 * @see com.frank.dip.enhance.EnhanceTransformationation#operate(com.frank.dip.Image)
		 */
		@Override
		public BinaryImage operate(BinaryImage source)
		{
			return source.clone();
		}

		/**
		 * @see com.frank.dip.enhance.Transformation#function(double)
		 */
		@Override
		public double function(double r)
		{
			return r;
		}

		/**
		 * @see com.frank.dip.math.Function#toFunction()
		 */
		@Override
		public String toFunction()
		{
			return "s = r";//$NON-NLS-1$
		}

		/**
		 * @see com.frank.dip.enhance.Transformation#getFunctionString()
		 */
		@Override
		public String getFunctionString()
		{
			return "s = r";//$NON-NLS-1$
		}

		/**
		 * Due to the logarithmic transformation cannot make any effect in a
		 * binary image, the inverse transformation is the binary logarithmic
		 * transformation itself.
		 * 
		 * @see com.frank.dip.enhance.time.Transform#getInverseTransform()
		 */
		@Override
		public Binary getInverseTransform()
				throws UnsupportedOperationException
		{
			return new Binary();
		}
	}

	/**
	 * The gray logarithm transformation.
	 * <p>
	 * In this transformation, the logarithm transformation will be performed on
	 * gray channel.
	 * </p>
	 * 
	 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
	 * @version 1.0.0
	 */
	public static final class Gray extends LogarithmicTransformation<GrayImage>
	{
		/**
		 * Construct an instance of <tt>LogarithmicTransformationGray</tt> with
		 * specified constant value {@code c} and {@code b}.
		 * 
		 * @param c
		 *            the constant value {@code c}
		 * @param b
		 *            the constant value {@code b}
		 */
		public Gray(double c, double b)
		{
			super(c, b);
		}

		/**
		 * Construct an instance of <tt>LogarithmicTransformationGray</tt> with
		 * default parameters.
		 * 
		 * <pre>
		 * {@code c} = {@linkplain EnhanceTransformation#SCALE_LEVEL} - 1 / log({@linkplain EnhanceTransformation#SCALE_LEVEL})
		 * {@code b} = 0.0
		 * </pre>
		 */
		public Gray()
		{
			super();
		}

		/**
		 * @see com.frank.dip.math.Function#toFunction()
		 */
		@Override
		public String toFunction()
		{
			return String.format("s = %f*log(1+r)%+f", c, b);//$NON-NLS-1$
		}

		/**
		 * @see com.frank.dip.enhance.Transformation#getFunctionString()
		 */
		@Override
		public String getFunctionString()
		{
			return "s = c * log(1+r) + b";//$NON-NLS-1$
		}

		/**
		 * @see com.frank.dip.enhance.time.Transform#getInverseTransform()
		 */
		@Override
		public InverseGray getInverseTransform()
				throws UnsupportedOperationException
		{
			return new InverseGray(c, b);
		}
	}

	/**
	 * The color logarithm transformation.
	 * <p>
	 * In this transformation, the logarithm transformation will be performed on
	 * red, green and blue channels.
	 * </p>
	 * 
	 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
	 * @version 1.0.0
	 */
	public static final class Color extends
			LogarithmicTransformation<ColorImage>
	{
		/**
		 * Construct an instance of <tt>LogarithmicTransformationColor</tt> with
		 * specified constant value {@code c} and {@code b}.
		 * 
		 * @param c
		 *            the constant value {@code c}
		 * @param b
		 *            the constant value {@code b}
		 */
		public Color(double c, double b)
		{
			super(c, b);
		}

		/**
		 * Construct an instance of <tt>LogarithmicTransformationColor</tt> with
		 * default parameters.
		 * 
		 * <pre>
		 * {@code c} = {@linkplain EnhanceTransformation#SCALE_LEVEL} - 1 / log({@linkplain EnhanceTransformation#SCALE_LEVEL})
		 * {@code b} = 0.0
		 * </pre>
		 */
		public Color()
		{
			super();
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
		 * @see com.frank.dip.math.Function#toFunction()
		 */
		@Override
		public String toFunction()
		{
			return String.format("s = %f*log(1+r)%+f", c, b);//$NON-NLS-1$
		}

		/**
		 * @see com.frank.dip.enhance.Transformation#getFunctionString()
		 */
		@Override
		public String getFunctionString()
		{
			return "s = c * log(1+r) + b";//$NON-NLS-1$
		}

		/**
		 * @see com.frank.dip.enhance.time.Transform#getInverseTransform()
		 */
		@Override
		public InverseColor getInverseTransform()
				throws UnsupportedOperationException
		{
			return new InverseColor(c, b);
		}
	}

	/**
	 * The inverse gray logarithm transformation.
	 * <p>
	 * In this transformation, the inverse logarithm transformation will be
	 * performed on gray channel.
	 * </p>
	 * 
	 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
	 * @version 1.0.0
	 */
	public static final class InverseGray extends
			LogarithmicTransformation<GrayImage>
	{
		/**
		 * Construct an instance of
		 * <tt>InverseLogarithmicTransformationGray</tt> with specified constant
		 * value {@code c} and {@code b}.
		 * 
		 * @param c
		 *            the constant value {@code c}
		 * @param b
		 *            the constant value {@code b}
		 */
		public InverseGray(double c, double b)
		{
			super(c, b);
		}

		/**
		 * Construct an instance of
		 * <tt>InverseLogarithmicTransformationGray</tt> with default
		 * parameters.
		 * 
		 * <pre>
		 * {@code c} = {@linkplain EnhanceTransformation#SCALE_LEVEL} - 1 / log({@linkplain EnhanceTransformation#SCALE_LEVEL})
		 * {@code b} = 0.0
		 * </pre>
		 */
		public InverseGray()
		{
			super();
		}

		/**
		 * @see com.frank.dip.enhance.Transformation#function(double)
		 */
		@Override
		public double function(double r)
		{
			double exp = Math.expm1((r - b) / c);
			if (exp >= 0 && exp < SCALE_LEVEL)
				return exp;
			else
				return exp < 0 ? 0 : (SCALE_LEVEL - 1);
		}

		/**
		 * @see com.frank.dip.math.Function#toFunction()
		 */
		@Override
		public String toFunction()
		{
			return String.format("s = exp( (r%+f) / %f ) - 1", -b, c);//$NON-NLS-1$
		}

		/**
		 * @see com.frank.dip.enhance.Transformation#getFunctionString()
		 */
		@Override
		public String getFunctionString()
		{
			return "s = exp( (r - b) / c ) - 1";//$NON-NLS-1$
		}

		/**
		 * @see com.frank.dip.enhance.time.Transform#getInverseTransform()
		 */
		@Override
		public Gray getInverseTransform() throws UnsupportedOperationException
		{
			return new Gray(c, b);
		}
	}

	/**
	 * The inverse color logarithm transformation.
	 * <p>
	 * In this transformation, the logarithm transformation will be performed on
	 * red, green and blue channels.
	 * </p>
	 * 
	 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
	 * @version 1.0.0
	 */
	public static final class InverseColor extends
			LogarithmicTransformation<ColorImage>
	{
		/**
		 * Construct an instance of
		 * <tt>InverseLogarithmicTransformationColor</tt> with specified
		 * constant value {@code c} and {@code b}.
		 * 
		 * @param c
		 *            the constant value {@code c}
		 * @param b
		 *            the constant value {@code b}
		 */
		public InverseColor(double c, double b)
		{
			super(c, b);
		}

		/**
		 * Construct an instance of
		 * <tt>InverseLogarithmicTransformationColor</tt> with default
		 * parameters.
		 * 
		 * <pre>
		 * {@code c} = {@linkplain EnhanceTransformation#SCALE_LEVEL} - 1 / log({@linkplain EnhanceTransformation#SCALE_LEVEL})
		 * {@code b} = 0.0
		 * </pre>
		 */
		public InverseColor()
		{
			super();
		}

		/**
		 * @see com.frank.dip.enhance.Transformation#function(double)
		 */
		@Override
		public double function(double r)
		{
			double exp = Math.expm1((r - b) / c);
			if (exp >= 0 && exp < SCALE_LEVEL)
				return exp;
			else
				return exp < 0 ? 0 : (SCALE_LEVEL - 1);
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
		 * @see com.frank.dip.math.Function#toFunction()
		 */
		@Override
		public String toFunction()
		{
			return String.format("s = exp( (r%+f) / %f ) - 1", -b, c);//$NON-NLS-1$
		}

		/**
		 * @see com.frank.dip.enhance.Transformation#getFunctionString()
		 */
		@Override
		public String getFunctionString()
		{
			return "s = exp( (r - b) / c ) - 1";//$NON-NLS-1$
		}

		/**
		 * @see com.frank.dip.enhance.time.Transform#getInverseTransform()
		 */
		@Override
		public Color getInverseTransform() throws UnsupportedOperationException
		{
			return new Color(c, b);
		}
	}

	/**
	 * The constant value {@code c}.
	 */
	protected double	c;
	/**
	 * The constant value {@code b}.
	 */
	protected double	b;

	/**
	 * Perform logarithmic transformation to the specified image.
	 * 
	 * @param source
	 *            the source image
	 * @param c
	 *            the constant value {@code c}
	 * @param b
	 *            the constant value {@code b}
	 * @return the transformed image
	 * @throws IllegalImageTypeException
	 *             if the image type is not supported
	 */
	public static <T extends Image> T logarithm(T source, double c, double b)
			throws IllegalArgumentException
	{
		if (source instanceof GrayImage)
			return (T) (new Gray(c, b).operate((GrayImage) source));
		if (source instanceof BinaryImage)
			return (T) (new Binary(c, b).operate((BinaryImage) source));
		if (source instanceof ColorImage)
			return (T) (new Color(c, b).operate((ColorImage) source));
		throw new IllegalArgumentException(
				String.format(
						"Current logarithmic transformation cannot support image type: %s",
						source.getClass().toString()));
	}

	/**
	 * Get logarithmic transformation according to the specified image type.
	 * 
	 * @param type
	 *            the type of source image
	 * @param c
	 *            the constant value {@code c}
	 * @param b
	 *            the constant value {@code b}
	 * @return the transformation
	 * @throws IllegalImageTypeException
	 *             if the image type is not supported
	 */
	public static <T extends Image> LogarithmicTransformation<T> getLogarithmTransformation(
			Class<T> type, double c, double b) throws IllegalImageTypeException
	{
		if (type == GrayImage.class)
			return (LogarithmicTransformation<T>) new Gray(c, b);
		if (type == BinaryImage.class)
			return (LogarithmicTransformation<T>) new Binary(c, b);
		if (type == ColorImage.class)
			return (LogarithmicTransformation<T>) new Color(c, b);
		throw new IllegalImageTypeException(LogarithmicTransformation.class,
				type);
	}

	/**
	 * Get inverse logarithmic transformation according to the specified image
	 * type.
	 * 
	 * @param type
	 *            the type of source image
	 * @param c
	 *            the constant value {@code c}
	 * @param b
	 *            the constant value {@code b}
	 * @return the transformation
	 * @throws IllegalImageTypeException
	 *             if the image type is not supported
	 */
	public static <T extends Image> LogarithmicTransformation<T> getInverseLogarithmTransformation(
			Class<T> type, double c, double b) throws IllegalImageTypeException
	{
		if (type == GrayImage.class)
			return (LogarithmicTransformation<T>) new InverseGray(c, b);
		if (type == BinaryImage.class)
			return (LogarithmicTransformation<T>) new Binary(c, b);
		if (type == ColorImage.class)
			return (LogarithmicTransformation<T>) new InverseColor(c, b);
		throw new IllegalImageTypeException(LogarithmicTransformation.class,
				type);
	}

	/**
	 * Perform inverse logarithmic transformation to the specified image.
	 * 
	 * @param source
	 *            the source image
	 * @param c
	 *            the constant value {@code c}
	 * @param b
	 *            the constant value {@code b}
	 * @return the transformed image
	 * @throws IllegalImageTypeException
	 *             if the image type is not supported
	 */
	public static <T extends Image> T inverseLogarithm(T source, double c,
			double b) throws IllegalImageTypeException
	{
		if (source instanceof GrayImage)
			return (T) (new InverseGray(c, b).operate((GrayImage) source));
		if (source instanceof BinaryImage)
			return (T) (new Binary(c, b).operate((BinaryImage) source));
		if (source instanceof ColorImage)
			return (T) (new InverseColor(c, b).operate((ColorImage) source));
		throw new IllegalImageTypeException(LogarithmicTransformation.class,
				source.getClass());
	}

	/**
	 * Getter for the constant value {@code c}.
	 * 
	 * @return the constant value {@code c}
	 */
	public double getC()
	{
		return c;
	}

	/**
	 * Setter for the constant value {@code c}.
	 * 
	 * @param c
	 *            the constant value {@code c}
	 */
	public void setC(double c)
	{
		this.c = c;
	}

	/**
	 * Returns an empty properties instance of current inversion transformation,
	 * due to this transformation needs no parameter.
	 * 
	 * @see com.frank.dip.enhance.Transformation#getProperties()
	 */
	@Override
	public Properties getProperties()
	{
		Properties p = new Properties();
		p.put(PARAM_C, c);
		p.put(PARAM_B, b);
		return p;
	}

	/**
	 * @see com.frank.dip.enhance.Transformation#setProperties(java.util.Properties)
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
	}

	/**
	 * Getter for the constant value {@code b}.
	 * 
	 * @return the constant value {@code b}
	 */
	public double getB()
	{
		return b;
	}

	/**
	 * Setter for the constant value {@code b}.
	 * 
	 * @param b
	 *            the constant value {@code b}
	 */
	public void setB(double b)
	{
		this.b = b;
	}

	/**
	 * @see com.frank.dip.enhance.Transformation#function(double)
	 */
	@Override
	public double function(double r)
	{
		double log = c * Math.log1p(r) + b;
		if (log >= 0 && log < SCALE_LEVEL)
			return log;
		else
			return log < 0 ? 0 : (SCALE_LEVEL - 1);
	}

	/**
	 * @see com.frank.dip.enhance.time.Transform#toString()
	 */
	@Override
	public String toString()
	{
		return toFunction();
	}
}
