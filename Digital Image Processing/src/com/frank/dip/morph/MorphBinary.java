/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved.
 * Morph.java is PROPRIETARY/CONFIDENTIAL built in 2013.
 * Use is subject to license terms.
 */
package com.frank.dip.morph;

import com.frank.dip.BinaryImage;

/**
 * The morphology operators for {@code BinaryImage}.
 * <p>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class MorphBinary extends Morph<BinaryImage>
{
	/**
	 * Construct an instance of morphology operator for binary image.
	 * 
	 * @param type
	 *            the type of morphology structure
	 * @see #STRUCTURE_DIAMOND
	 * @see #STRUCTURE_EIGHT_CORNER
	 * @see #STRUCTURE_SQUARE
	 */
	public MorphBinary(int type)
	{
		super(type);
	}

	/**
	 * Perform union operation to the left image {@code L} with right image
	 * {@code R}.
	 * 
	 * <pre>
	 * {@code L} = {@code L} &cup; {@code R}
	 * </pre>
	 * 
	 * @param left
	 *            the left image {@code L}
	 * @param right
	 *            the right image {@code R}
	 * @return the union image {@code S}
	 */
	public static void union(BinaryImage left, BinaryImage right)
	{
		union(left.getBinaryMatrix(), right.getBinaryMatrix());
	}

	/**
	 * Returns the intersect image for the left image {@code L} and right image
	 * {@code B}.
	 * 
	 * <pre>
	 * {@code L} = {@code L} &cap; {@code R}
	 * </pre>
	 * 
	 * @param left
	 *            the left image {@code L}
	 * @param right
	 *            the right image {@code R}
	 */
	public static void intersect(BinaryImage left, BinaryImage right)
	{
		intersect(left.getBinaryMatrix(), right.getBinaryMatrix());
	}

	/**
	 * Returns the subtraction image for the left image {@code L} and right
	 * image {@code B}.
	 * 
	 * <pre>
	 * {@code L} = {@code L} &minus; {@code R}
	 * </pre>
	 * 
	 * @param left
	 *            the left image {@code L}
	 * @param right
	 *            the right image {@code R}
	 * @return the subtraction image {@code S}
	 */
	public static void minus(BinaryImage left, BinaryImage right)
	{
		minus(left.getBinaryMatrix(), right.getBinaryMatrix());
	}

	/**
	 * @see com.frank.dip.morph.Morph#erode(com.frank.dip.Image)
	 */
	@Override
	public BinaryImage erode(BinaryImage image)
	{
		return new BinaryImage(erode(image.getBinaryMatrix(), type));
	}

	/**
	 * Skeletonize the specified image {@code A} according to the morphology
	 * type {@code B} with specified erode loop count {@code k}.
	 * <p>
	 * {@code S} = {@code A}<br>
	 * <strong>for</strong> i = 1 <strong>to</strong> k<br>
	 * <strong>do</strong><br>
	 * &nbsp;&nbsp;&nbsp;&nbsp;{@code S} = {@code S} &Theta; {@code B}<br>
	 * <strong>end</strong><br>
	 * <strong>return</strong> {@code S}
	 * </p>
	 * 
	 * @param image
	 *            the image to skeletonize
	 * @param k
	 *            the specified erode loop count
	 * @return the skeletonized image
	 */
	public BinaryImage skeletonize(BinaryImage image, int k)
	{
		return new BinaryImage(skeletonize(image.getBinaryMatrix(), type, k));
	}

	/**
	 * @see com.frank.dip.morph.Morph#dilate(com.frank.dip.Image)
	 */
	@Override
	public BinaryImage dilate(BinaryImage image)
	{
		return new BinaryImage(dilate(image.getBinaryMatrix(), type));
	}

	/**
	 * Returns the complement of the source image.
	 * 
	 * <pre>
	 * {@code S} = {@code A}<sup>C</sup>
	 * </pre>
	 * 
	 * @param image
	 *            the source image
	 * @return the complement image
	 */
	public static BinaryImage complement(BinaryImage image)
	{
		return new BinaryImage(complement(image.getBinaryMatrix()));
	}

	/**
	 * Perform hit or not hit transformation to the specified image.
	 * 
	 * <pre>
	 * {@code L} = {@code A} &Theta; {@code B}
	 * {@code R} = {@code L}<sup>C</sup> &Theta; {@code B}
	 * {@code S} = {@code L} &cap; {@code R}
	 * {@code A}: the image to perform
	 * {@code B}: the morphology structure
	 * </pre>
	 * 
	 * @param image
	 *            the image to perform
	 * @return the image after hit operation
	 */
	public BinaryImage hit(BinaryImage image)
	{
		return new BinaryImage(hit(image.getBinaryMatrix(), type));
	}

	/**
	 * Perform thin operation to the specified image.
	 * 
	 * <pre>
	 * {@code L} = {@code A} &Theta; {@code B}
	 * {@code R} = {@code L}<sup>C</sup> &Theta; {@code B}
	 * {@code L} = {@code L} &cap; {@code R}
	 * {@code S} = {@code A} &minus; {@code L}
	 * {@code A}: the image to perform
	 * {@code B}: the morphology structure
	 * </pre>
	 * 
	 * @param image
	 *            the image to perform
	 * @return the image after thin operation
	 */
	public BinaryImage thin(BinaryImage image)
	{
		return new BinaryImage(thin(image.getBinaryMatrix(), type));
	}

	/**
	 * @see com.frank.dip.morph.Morph#open(com.frank.dip.Image)
	 */
	@Override
	public BinaryImage open(BinaryImage image)
	{
		return new BinaryImage(open(image.getBinaryMatrix(), type));
	}

	/**
	 * @see com.frank.dip.morph.Morph#close(com.frank.dip.Image)
	 */
	@Override
	public BinaryImage close(BinaryImage image)
	{
		return new BinaryImage(close(image.getBinaryMatrix(), type));
	}

	/**
	 * Extract the edges of the graphics in the specified image.
	 * 
	 * <pre>
	 * {@code S} = {@code A} &minus; ({@code A} &Theta; {@code B})
	 * </pre>
	 * 
	 * @param image
	 *            the specified image
	 * @return the edge image
	 */
	public BinaryImage edge(BinaryImage image)
	{
		return new BinaryImage(edge(image.getBinaryMatrix(), type));
	}

	/**
	 * Perform union operation.
	 * 
	 * <pre>
	 * {@code A} = {@code A} &cup; {@code B}
	 * </pre>
	 * 
	 * @param a
	 *            the binary matrix to be added
	 * @param b
	 *            the binary matrix to add
	 */
	protected static final void union(boolean[][] a, boolean[][] b)
	{
		for (int x = 0; x < a.length && x < b.length; x++)
			for (int y = 0; y < a[x].length; y++)
				try
				{
					a[x][y] = a[x][y] | b[x][y];
				}
				catch (Exception e)
				{
				}
	}

	/**
	 * Perform {@code A} = {@code A} &cup; {@code B}.
	 * 
	 * @param a
	 * @param b
	 */
	protected static final void intersect(boolean[][] a, boolean[][] b)
	{
		for (int x = 0; x < a.length; x++)
			for (int y = 0; y < a[x].length; y++)
				try
				{
					a[x][y] = a[x][y] & b[x][y];
				}
				catch (Exception e)
				{
				}
	}

	/**
	 * Perform {@code A} = {@code A} &minus; {@code B}.
	 * 
	 * @param a
	 * @param b
	 */
	protected static final void minus(boolean[][] a, boolean[][] b)
	{
		for (int x = 0; x < b.length; x++)
			for (int y = 0; y < b[x].length; y++)
				try
				{
					a[x][y] = a[x][y] && !b[x][y];
				}
				catch (Exception e)
				{
				}
	}

	/**
	 * Erode image: {@code S} = {@code A} &Theta; {@code B}
	 * 
	 * @param b
	 *            {@code A}
	 * @param type
	 *            {@code B}
	 * @return {@code S}
	 */
	protected static final boolean[][] erode(boolean[][] b, int type)
	{
		int width = b.length;
		int height = b[0].length;
		boolean[][] out = new boolean[width][height];
		int x, y;
		switch (type)
		{
			case STRUCTURE_SQUARE:
			default:
				for (x = 1; x < width - 1; x++)
					for (y = 1; y < height - 1; y++)
						if (b[x][y] && b[x - 1][y - 1] && b[x - 1][y]
								&& b[x - 1][y + 1] && b[x][y - 1]
								&& b[x][y + 1] && b[x + 1][y - 1]
								&& b[x + 1][y] && b[x + 1][y + 1])
							out[x][y] = true;
				break;
			case STRUCTURE_DIAMOND:
				for (x = 1; x < width - 1; x++)
					for (y = 1; y < height - 1; y++)
						if (b[x][y] && b[x - 1][y] && b[x + 1][y]
								&& b[x][y - 1] && b[x][y + 1])
							out[x][y] = true;
				break;
			case STRUCTURE_EIGHT_CORNER:
				for (x = 2; x < width - 2; x++)
					for (y = 2; y < height - 2; y++)
						if (b[x][y] && b[x - 2][y - 1] && b[x - 2][y]
								&& b[x - 2][y + 1] && b[x - 1][y - 2]
								&& b[x - 1][y - 1] && b[x - 1][y]
								&& b[x - 1][y + 1] && b[x - 1][y + 2]
								&& b[x][y - 2] && b[x][y - 1] && b[x][y + 1]
								&& b[x][y + 2] && b[x + 1][y - 2]
								&& b[x + 1][y - 1] && b[x + 1][y]
								&& b[x + 1][y + 1] && b[x + 1][y + 2]
								&& b[x + 2][y - 1] && b[x + 2][y]
								&& b[x + 2][y + 1])
							out[x][y] = true;
				break;
		}
		return out;
	}

	/**
	 * Skeletonize the specified image {@code A} according to the morphology
	 * type {@code B} with specified erode loop count {@code k}.
	 * <p>
	 * {@code S} = {@code A}<br>
	 * <strong>for</strong> i = 1 <strong>to</strong> k<br>
	 * <strong>do</strong><br>
	 * &nbsp;&nbsp;&nbsp;&nbsp;{@code S} = {@code S} &Theta; {@code B}<br>
	 * <strong>end</strong><br>
	 * <strong>return</strong> {@code S}
	 * </p>
	 * 
	 * @param b
	 *            the pixels matrix {@code A} to skeletonize
	 * @param k
	 *            the specified erode loop count
	 * @return the skeletonized image
	 */
	protected static final boolean[][] skeletonize(boolean[][] b, int type,
			int k)
	{
		int width = b.length;
		int height = b[0].length;
		boolean[][] m = new boolean[width][height];
		boolean[][] n;
		for (int i = 0; i < k; i++)
		{
			erode0(b, type);
			n = b.clone();
			minus(n, open(b, type));
			union(m, n);
			n = null;
		}
		return m;
	}

	/**
	 * Erode the image itself.
	 * <p>
	 * {@code A} = {@code A} &Theta; {@code B}
	 * </p>
	 * 
	 * @param b
	 *            {@code A}
	 * @param type
	 *            {@code B}
	 */
	private static final void erode0(boolean[][] b, int type)
	{
		int width = b.length;
		int height = b[0].length;
		int x, y;
		switch (type)
		{
			case STRUCTURE_SQUARE:
			default:
				for (x = 1; x < width - 1; x++)
					for (y = 1; y < height - 1; y++)
						if (b[x][y] && b[x - 1][y - 1] && b[x - 1][y]
								&& b[x - 1][y + 1] && b[x][y - 1]
								&& b[x][y + 1] && b[x + 1][y - 1]
								&& b[x + 1][y] && b[x + 1][y + 1])
							b[x][y] = true;
				break;
			case STRUCTURE_DIAMOND:
				for (x = 1; x < width - 1; x++)
					for (y = 1; y < height - 1; y++)
						if (b[x][y] && b[x - 1][y] && b[x + 1][y]
								&& b[x][y - 1] && b[x][y + 1])
							b[x][y] = true;
				break;
			case STRUCTURE_EIGHT_CORNER:
				for (x = 2; x < width - 2; x++)
					for (y = 2; y < height - 2; y++)
						if (b[x][y] && b[x - 2][y - 1] && b[x - 2][y]
								&& b[x - 2][y + 1] && b[x - 1][y - 2]
								&& b[x - 1][y - 1] && b[x - 1][y]
								&& b[x - 1][y + 1] && b[x - 1][y + 2]
								&& b[x][y - 2] && b[x][y - 1] && b[x][y + 1]
								&& b[x][y + 2] && b[x + 1][y - 2]
								&& b[x + 1][y - 1] && b[x + 1][y]
								&& b[x + 1][y + 1] && b[x + 1][y + 2]
								&& b[x + 2][y - 1] && b[x + 2][y]
								&& b[x + 2][y + 1])
							b[x][y] = true;
				break;
		}
	}

	/**
	 * Dilate image: {@code S} = {@code A} &oplus; {@code B}.
	 * 
	 * @param b
	 *            {@code A}
	 * @param type
	 *            {@code B}
	 * @return {@code A} after dilate
	 */
	protected static final boolean[][] dilate(boolean[][] b, int type)
	{
		int width = b.length;
		int height = b[0].length;
		boolean[][] out = new boolean[width][height];
		int x, y;
		switch (type)
		{
			case STRUCTURE_SQUARE:
			default:
				for (x = 1; x < width - 1; x++)
					for (y = 1; y < height - 1; y++)
						if (b[x][y] || b[x - 1][y - 1] || b[x - 1][y]
								|| b[x - 1][y + 1] || b[x][y - 1]
								|| b[x][y + 1] || b[x + 1][y - 1]
								|| b[x + 1][y] || b[x + 1][y + 1])
						{
							out[x][y] = true;
							continue;
						}
				break;
			case STRUCTURE_DIAMOND:
				for (x = 1; x < width - 1; x++)
					for (y = 1; y < height - 1; y++)
						if (b[x][y] || b[x - 1][y] || b[x + 1][y]
								|| b[x][y - 1] || b[x][y + 1])
						{
							out[x][y] = true;
							continue;
						}
				break;
			case STRUCTURE_EIGHT_CORNER:
				for (x = 2; x < width - 2; x++)
					for (y = 2; y < height - 2; y++)
						if (b[x][y] || b[x - 2][y - 1] || b[x - 2][y]
								|| b[x - 2][y + 1] || b[x - 1][y - 2]
								|| b[x - 1][y - 1] || b[x - 1][y]
								|| b[x - 1][y + 1] || b[x - 1][y + 2]
								|| b[x][y - 2] || b[x][y - 1] || b[x][y + 1]
								|| b[x][y + 2] || b[x + 1][y - 2]
								|| b[x + 1][y - 1] || b[x + 1][y]
								|| b[x + 1][y + 1] || b[x + 1][y + 2]
								|| b[x + 2][y - 1] || b[x + 2][y]
								|| b[x + 2][y + 1])
						{
							out[x][y] = true;
							continue;
						}
				break;
		}
		return out;
	}

	/**
	 * Returns the complement of {@code A}, {@code A}<sup>c</sup>.
	 * 
	 * @param b
	 *            {@code A}
	 * @return {@code A}<sup>c</sup>
	 */
	protected static final boolean[][] complement(boolean b[][])
	{
		int width = b.length;
		int height = b[0].length;
		boolean[][] out = new boolean[width][height];
		for (int x = 0; x < width; x++)
			for (int y = 0; y < height; y++)
				out[x][y] = b[x][y];
		return out;
	}

	/**
	 * Hit or not hit transform.
	 * 
	 * <pre>
	 * {@code L} = {@code A} &Theta; {@code B}
	 * {@code R} = {@code L}<sup>C</sup> &Theta; {@code B}
	 * {@code S} = {@code L} &cap; {@code R}
	 * {@code A}: the image to perform
	 * {@code B}: the morphology structure
	 * </pre>
	 * 
	 * @param b
	 *            the matrix of {@code A}
	 * @param type
	 *            {@code B}
	 * @return {@code S}
	 */
	protected static final boolean[][] hit(boolean[][] b, int type)
	{
		boolean[][] b1 = erode(b, type);
		boolean[][] b2 = erode(complement(b), type);
		intersect(b1, b2);
		return b1;
	}

	/**
	 * Thin.
	 * 
	 * <pre>
	 * {@code L} = {@code A} &Theta; {@code B}
	 * {@code R} = {@code L}<sup>C</sup> &Theta; {@code B}
	 * {@code L} = {@code L} &cap; {@code R}
	 * {@code S} = {@code A} &minus; {@code L}
	 * {@code A}: the image to perform
	 * {@code B}: the morphology structure
	 * </pre>
	 * 
	 * @param b
	 *            {@code A}
	 * @param type
	 *            {@code B}
	 * @return {@code S}
	 */
	protected static final boolean[][] thin(boolean[][] b, int type)
	{
		boolean[][] a = b.clone();
		b = hit(b, type);
		minus(a, b);
		return a;
	}

	/**
	 * Open.
	 * 
	 * <pre>
	 * {@code S} = {@code A} &omicron; {@code B}=({@code A} &Theta; {@code B}) &oplus; {@code B}
	 * </pre>
	 * 
	 * @param b
	 *            {@code A}
	 * @param type
	 *            {@code B}
	 * @return {@code S}
	 */
	protected static final boolean[][] open(boolean[][] b, int type)
	{
		b = erode(b, type);
		b = dilate(b, type);
		return b;
	}

	/**
	 * Close.
	 * 
	 * <pre>
	 * {@code S} = {@code A} &bull; {@code B}=({@code A} &oplus; {@code B}) &Theta; {@code B}
	 * </pre>
	 * 
	 * @param b
	 *            {@code A}
	 * @param type
	 *            {@code B}
	 * @return {@code S}
	 */
	protected static final boolean[][] close(boolean[][] b, int type)
	{
		b = dilate(b, type);
		b = erode(b, type);
		return b;
	}

	/**
	 * Extract the edges of the graphics in the specified image.
	 * 
	 * <pre>
	 * {@code S} = {@code A} &minus; ({@code A} &Theta; {@code B})
	 * </pre>
	 * 
	 * @param b
	 *            {@code A}
	 * @param type
	 *            {@code B}
	 * @return {@code S}
	 */
	protected static final boolean[][] edge(boolean[][] b, int type)
	{
		boolean[][] a = b.clone();
		a = erode(a, type);
		minus(b, a);
		return b;
	}
}
