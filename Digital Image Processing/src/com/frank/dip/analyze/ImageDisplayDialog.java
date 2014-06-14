/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved.
 * ImageDisplayDialog.java is PROPRIETARY/CONFIDENTIAL built in 2013.
 * Use is subject to license terms.
 */
package com.frank.dip.analyze;

import java.awt.BorderLayout;
import java.awt.Window;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JDialog;

import com.frank.dip.Image;

/**
 * The image displaying dialog.
 * <p>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class ImageDisplayDialog extends JDialog implements Observer,
		ImageDisplay
{
	/**
	 * serialVersionUID.
	 */
	private static final long	serialVersionUID	= 2640533463429902280L;
	/**
	 * The image to display.
	 */
	private ImageDisplayPanel	panel;

	/**
	 * Construct an instance of <tt>ImageDisplayDialog</tt>.
	 * 
	 * @param window
	 *            the parent window
	 * @param title
	 *            the title of the dialog
	 * @param modal
	 *            specifies whether dialog blocks input to other windows when
	 *            shown; calling to <code>setModal(true)</code> is equivalent to
	 *            <code>setModalityType(Dialog.DEFAULT_MODALITY_TYPE)</code>,
	 *            and calling to <code>setModal(false)</code> is equvivalent to
	 *            <code>setModalityType(Dialog.ModalityType.MODELESS)</code>
	 * @param image
	 *            the image to display
	 * @wbp.parser.constructor
	 */
	public ImageDisplayDialog(Window window, String title, boolean modal,
			Image image)
	{
		this(window, title, modal, image, true, false, true);
	}

	/**
	 * Construct an instance of <tt>ImageDisplayDialog</tt>.
	 * 
	 * @param window
	 *            the parent window
	 * @param title
	 *            the title of the dialog
	 * @param modal
	 *            specifies whether dialog blocks input to other windows when
	 *            shown; calling to <code>setModal(true)</code> is equivalent to
	 *            <code>setModalityType(Dialog.DEFAULT_MODALITY_TYPE)</code>,
	 *            and calling to <code>setModal(false)</code> is equvivalent to
	 *            <code>setModalityType(Dialog.ModalityType.MODELESS)</code>
	 * @param image
	 *            the image to display
	 * @param showPixelInfo
	 *            <code>true</code> if need to display pixel information when
	 *            the mouse is on the image
	 * @param showAlphaInfo
	 *            <code>true</code> if need to display the alpha channel. <br>
	 *            The alpha channel info will be displayed only if
	 *            <code>showPixelInfo</code> and <code>showAlphaInfo</code> are
	 *            both <code>true</code>.
	 * @param hidIfExit
	 *            <code>true</code> if need to hid the pixel information
	 *            displaying label when the mouse exits the image range
	 */
	public ImageDisplayDialog(Window window, String title, boolean modal,
			Image image, boolean showPixelInfo, boolean showAlphaInfo,
			boolean hidIfExit)
	{
		super(window);
		setModal(modal);
		panel = new ImageDisplayPanel(title, image, showPixelInfo,
				showAlphaInfo, hidIfExit);
		panel.addObserver(this);
		String pname = panel.getName();
		setTitle(pname);
		getContentPane().setLayout(new BorderLayout(0, 0));
		getContentPane().add(panel, BorderLayout.CENTER);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	/**
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable o, Object arg)
	{
		if (o == panel.observable)
		{
			if (arg instanceof Integer)
				setSize(getWidth(), getHeight() + (Integer) arg);
			if (arg instanceof String)
				setTitle((String) arg);
		}
	}

	/**
	 * @see com.frank.dip.demo.ImageDisplay#copyImage()
	 */
	@Override
	public void copyImage()
	{
		panel.copyImage();
	}

	/**
	 * @see com.frank.dip.demo.ImageDisplay#refreshImage()
	 */
	@Override
	public void refreshImage()
	{
		panel.refreshImage();
	}

	/**
	 * @see com.frank.dip.demo.ImageDisplay#getImage()
	 */
	@Override
	public Image getImage()
	{
		return panel.getImage();
	}

	/**
	 * @see com.frank.dip.demo.ImageDisplay#setImage(com.frank.dip.Image,
	 *      java.lang.String)
	 */
	@Override
	public void setImage(Image newImage, String newTitle)
	{
		panel.setImage(newImage, newTitle);
	}
}
