/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved.
 * PPMFormat.java is PROPRIETARY/CONFIDENTIAL built in 8:11:12 PM, Feb 25, 2014.
 * Use is subject to license terms.
 */
package com.frank.dip.io.format;

/**
 * The Netpbm format interface.
 * <p>
 * <table border="1">
 * <tbody>
 * <tr>
 * <th>Magic Number</th>
 * <th>Type</th>
 * <th>Encoding</th>
 * </tr>
 * <tr>
 * <td><code>P1</code></td>
 * <td>Portable bitmap</td>
 * <td>ASCII</td>
 * </tr>
 * <tr>
 * <td><code>P2</code></td>
 * <td>Portable graymap</td>
 * <td>ASCII</td>
 * </tr>
 * <tr>
 * <td><code>P3</code></td>
 * <td>Portable pixmap</td>
 * <td>ASCII</td>
 * </tr>
 * <tr>
 * <td><code>P4</code></td>
 * <td>Portable bitmap</td>
 * <td>Binary</td>
 * </tr>
 * <tr>
 * <td><code>P5</code></td>
 * <td>Portable graymap</td>
 * <td>Binary</td>
 * </tr>
 * <tr>
 * <td><code>P6</code></td>
 * <td>Portable pixmap</td>
 * <td>Binary</td>
 * </tr>
 * </tbody>
 * </table>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public interface NetpbmFormat
{
	/**
	 * The supported formats by the Netpbm, both read and write.
	 */
	public final static String[]	SUPPORTED_FORMAT	= { "pbm", "pgm", "ppm" };
	/**
	 * The image type of image type PBM(P1).
	 * <p>
	 * This type is a portable bitmap, encoded with ASCII.
	 * </p>
	 */
	public final static String		TYPE_NETPBM_P1		= "P1";					//$NON-NLS-1$
	/**
	 * The image type of image type PGM(P2).
	 * <p>
	 * This type is a portable graymap, encoded with ASCII.
	 * </p>
	 */
	public final static String		TYPE_NETPBM_P2		= "P2";					//$NON-NLS-1$
	/**
	 * The image type of image type PPM(P3).
	 * <p>
	 * This type is a portable pixmap, encoded with ASCII.
	 * </p>
	 */
	public final static String		TYPE_NETPBM_P3		= "P3";					//$NON-NLS-1$
	/**
	 * The image type of image type PBM(P4).
	 * <p>
	 * This type is a portable bitmap, encoded with binary.
	 * </p>
	 */
	public final static String		TYPE_NETPBM_P4		= "P4";					//$NON-NLS-1$
	/**
	 * The image type of image type PGM(P5).
	 * <p>
	 * This type is a portable graymap, encoded with binary.
	 * </p>
	 */
	public final static String		TYPE_NETPBM_P5		= "P5";					//$NON-NLS-1$
	/**
	 * The image type of image type PPM(P6).
	 * <p>
	 * This type is a portable pixmap, encoded with binary.
	 * </p>
	 */
	public final static String		TYPE_NETPBM_P6		= "P6";					//$NON-NLS-1$
}
