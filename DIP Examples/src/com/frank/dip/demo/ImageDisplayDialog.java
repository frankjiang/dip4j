/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights reserved.
 * ImageDisplayDialog.java is PROPRIETARY/CONFIDENTIAL built in 2013.
 * Use is subject to license terms.
 */
package com.frank.dip.demo;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import com.frank.dip.ColorImage;
import com.frank.dip.Image;

import javax.swing.SwingConstants;

/**
 * The image displaying dialog.
 * <p>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class ImageDisplayDialog extends JDialog
{
	/**
	 * serialVersionUID.
	 */
	private static final long	serialVersionUID	= 2640533463429902280L;
	/**
	 * The image to display.
	 */
	private Image				image;
	/**
	 * The canvas to display the image.
	 */
	private JLabel				canvas;
	/**
	 * The label to display the pixels information.
	 */
	private JLabel				lblPixels;

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
	 */
	public ImageDisplayDialog(Window window, String title, boolean modal,
			Image image)
	{
		super(window, String.format(
				"%s%s",
				title,
				image == null ? "" : String.format(" - %d\u00d7%d",
						image.width(), image.height())));
		setModal(modal);
		this.image = image;
		getContentPane().setLayout(new BorderLayout(0, 0));
		lblPixels = new JLabel("");
		lblPixels.setHorizontalAlignment(SwingConstants.LEFT);
		getContentPane().add(lblPixels, BorderLayout.SOUTH);
		canvas = new JLabel("");
		canvas.setHorizontalAlignment(SwingConstants.CENTER);
		JScrollPane scrollPane = new JScrollPane(canvas);
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		initialize();
	}

	/**
	 * Initialize the settings.
	 */
	private void initialize()
	{
		if (image == null)
		{
			canvas.setText("No Image");
			return;
		}
		else
			canvas.setIcon(new javax.swing.ImageIcon(image.restore()));
		canvas.addMouseMotionListener(new MouseMotionAdapter()
		{
			@Override
			public void mouseMoved(MouseEvent e)
			{
				Point p = e.getPoint();
				Dimension main = ((JLabel) e.getSource()).getSize();
				int w0 = image.width();
				int h0 = image.height();
				int w = (w0 - main.width) / 2;
				int h = (h0 - main.height) / 2;
				p.translate(w < 0 ? w : 0, h < 0 ? h : 0);
				updateMouseMotion(p.x < 0 || p.y < 0 || p.x >= w0 || p.y >= h0 ? null
						: p);
			}
		});
		setBounds(0, 0, image.width() + 30, image.height() + 60);
	}

	/**
	 * Update the mouse position.
	 * 
	 * @param point
	 *            the point to display
	 */
	private void updateMouseMotion(Point point)
	{
		if (image == null)
			return;
		if (point == null)
		{
			lblPixels.setText("No Pixels");
			return;
		}
		if (image instanceof ColorImage)
		{
			ColorImage ci = (ColorImage) image;
			lblPixels
					.setText(String.format("(%d,%d) - RGB(%d,%d,%d)", point.x,
							point.y, ci.getRed(point.x, point.y),
							ci.getGreen(point.x, point.y),
							ci.getBlue(point.x, point.y)));
		}
		else
			lblPixels.setText(String.format("(%d,%d) - Gray(%d)", point.x,
					point.y, image.getPixel(point.x, point.y)));
	}
}
