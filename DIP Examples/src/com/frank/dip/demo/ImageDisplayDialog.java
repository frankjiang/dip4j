/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved.
 * ImageDisplayDialog.java is PROPRIETARY/CONFIDENTIAL built in 2013.
 * Use is subject to license terms.
 */
package com.frank.dip.demo;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;

import com.frank.dip.ColorImage;
import com.frank.dip.Image;
import com.frank.dip.geom.Geometry;
import com.frank.swing.SwingUtils;
import com.frank.sys.SystemUtils;

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
	 * The new image.
	 */
	private Image				newImage;
	/**
	 * The canvas to display the image.
	 */
	private JLabel				canvas;
	/**
	 * The label to display the pixels information.
	 */
	private JLabel				lblPixels;
	/**
	 * The tool bar.
	 */
	private JToolBar			toolBar;
	/**
	 * Copy image button.
	 */
	private JButton				btnCopyImage;
	/**
	 * The scale combo box.
	 */
	private JComboBox			comboBox;
	/**
	 * The current scale rate.
	 */
	private double				currentRate			= 1;
	/**
	 * The image title.
	 */
	private String				imageTitle;
	/**
	 * The geometry instance for scaling the image.
	 */
	private Geometry			geom;
	/**
	 * The button of rotate left.
	 */
	private JButton				btnRotateleft;
	/**
	 * The button of rotate right.
	 */
	private JButton				btnRotateright;
	/**
	 * The rotate degree from 0-3, means 0 degree to 270 degree.
	 */
	private int					rotate				= 0;
	private JButton				btnReset;

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
		this(window, title, modal, image, true, true);
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
	 * @param hidIfExit
	 *            <code>true</code> if need to hid the pixel information
	 *            displaying label when the mouse exits the image range
	 */
	public ImageDisplayDialog(Window window, String title, boolean modal,
			Image image, boolean showPixelInfo, boolean hidIfExit)
	{
		super(window, String.format(
				"%s%s",
				title,
				image == null ? "" : String.format(" - %d\u00d7%d",
						image.width(), image.height())));
		setModal(modal);
		this.image = image;
		this.imageTitle = title;
		newImage = image.clone();
		geom = Geometry.getGeometry(image, Geometry.TYPE_BICUBIC,
				Geometry.FILL_WITH_BLANK);
		getContentPane().setLayout(new BorderLayout(0, 0));
		lblPixels = new JLabel("");
		lblPixels.setHorizontalAlignment(SwingConstants.LEFT);
		getContentPane().add(lblPixels, BorderLayout.SOUTH);
		canvas = new JLabel("");
		canvas.setHorizontalAlignment(SwingConstants.CENTER);
		JScrollPane scrollPane = new JScrollPane(canvas);
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		toolBar = new JToolBar();
		scrollPane.setColumnHeaderView(toolBar);
		btnCopyImage = new JButton("");
		btnCopyImage.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				copyImage();
			}
		});
		btnCopyImage.setIcon(new ImageIcon(ImageDisplayDialog.class
				.getResource("/com/frank/dip/res/clipboard - copy.png")));//$NON-NLS-1$
		btnCopyImage.setToolTipText("Copy the source image.");
		toolBar.add(btnCopyImage);
		btnRotateleft = new JButton("");
		btnRotateleft
				.setToolTipText("Left rotate the current image with 90 degree.");
		btnRotateleft.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				newImage = geom.rotate(newImage, -Math.PI / 2);
				rotate--;
				rotate %= 4;
				refreshImage();
			}
		});
		btnRotateleft.setIcon(new ImageIcon(ImageDisplayDialog.class
				.getResource("/com/frank/dip/res/undo.png")));
		toolBar.add(btnRotateleft);
		btnRotateright = new JButton("");
		btnRotateright
				.setToolTipText("Right rotate the current image with 90 degree.");
		btnRotateright.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				newImage = geom.rotate(newImage, Math.PI / 2);
				rotate++;
				rotate %= 4;
				refreshImage();
			}
		});
		btnRotateright.setIcon(new ImageIcon(ImageDisplayDialog.class
				.getResource("/com/frank/dip/res/redo.png")));
		toolBar.add(btnRotateright);
		btnReset = new JButton("");
		btnReset.setToolTipText("Reset the current image to the source image.");
		btnReset.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				newImage = ImageDisplayDialog.this.image;
				rotate = 0;
				currentRate = 1.0;
				refreshImage();
			}
		});
		btnReset.setIcon(new ImageIcon(ImageDisplayDialog.class
				.getResource("/com/frank/dip/res/refresh-16.png")));
		toolBar.add(btnReset);
		comboBox = new JComboBox();
		comboBox.setEditable(true);
		comboBox.setModel(new DefaultComboBoxModel(new String[] { "10%", "20%",
				"50%", "100%", "150%", "200%", "300%", "1000%" }));
		comboBox.setSelectedIndex(3);
		comboBox.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Object obj = comboBox.getSelectedItem();
				if (obj == null)
					return;
				String s = ((String) obj).replaceAll("%", "");
				Double rate = 1.0;
				try
				{
					rate = Double.valueOf(s) / 100.0;
					scaleImage(rate);
				}
				catch (NumberFormatException e1)
				{
					comboBox.setSelectedItem("100%");
				}
			}
		});
		toolBar.add(comboBox);
		initialize(showPixelInfo, hidIfExit);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	/**
	 * Initialize the settings.
	 * 
	 * @param showPixelInfo
	 *            <code>true</code> if need to display pixel information when
	 *            the mouse is on the image
	 * @param hidIfExit
	 *            <code>true</code> if need to hid the pixel information
	 *            displaying label when the mouse exits the image range
	 */
	private void initialize(boolean showPixelInfo, boolean hidIfExit)
	{
		if (image == null)
		{
			canvas.setText("No Image");
			return;
		}
		else
			canvas.setIcon(new javax.swing.ImageIcon(image.restore()));
		if (showPixelInfo)
			canvas.addMouseMotionListener(new MouseMotionAdapter()
			{
				@Override
				public void mouseMoved(MouseEvent e)
				{
					Point p = e.getPoint();
					Dimension main = ((JLabel) e.getSource()).getSize();
					int w0 = newImage.width();
					int h0 = newImage.height();
					int w = (w0 - main.width) / 2;
					int h = (h0 - main.height) / 2;
					p.translate(w < 0 ? w : 0, h < 0 ? h : 0);
					updateMouseMotion(p.x < 0 || p.y < 0 || p.x >= w0
							|| p.y >= h0 ? null : p);
				}
			});
		if (hidIfExit)
			canvas.addMouseListener(new MouseAdapter()
			{
				@Override
				public void mouseEntered(MouseEvent e)
				{
					Point p = e.getPoint();
					Dimension main = ((JLabel) e.getSource()).getSize();
					int w0 = newImage.width();
					int h0 = newImage.height();
					int w = (w0 - main.width) / 2;
					int h = (h0 - main.height) / 2;
					p.translate(w < 0 ? w : 0, h < 0 ? h : 0);
					updateMouseMotion(p.x < 0 || p.y < 0 || p.x >= w0
							|| p.y >= h0 ? null : p);
					ImageDisplayDialog.this.validate();
					int height = lblPixels.getHeight();
					Dimension d = ImageDisplayDialog.this.getSize();
					ImageDisplayDialog.this.setSize(d.width, d.height + height);
				}

				@Override
				public void mouseExited(MouseEvent e)
				{
					int height = lblPixels.getHeight();
					Dimension d = ImageDisplayDialog.this.getSize();
					ImageDisplayDialog.this.setSize(d.width, d.height - height);
					lblPixels.setText("");
				}
			});
		setBounds(0, 0, 328, 211);
	}

	/**
	 * Update the mouse position.
	 * 
	 * @param point
	 *            the point to display
	 */
	private void updateMouseMotion(Point point)
	{
		if (newImage == null)
			return;
		if (point == null)
		{
			lblPixels.setText("No Pixels");
			return;
		}
		lblPixels.setVisible(true);
		if (newImage instanceof ColorImage)
		{
			ColorImage ci = (ColorImage) newImage;
			lblPixels
					.setText(String.format("(%d,%d) - RGB(%d,%d,%d)", point.x,
							point.y, ci.getRed(point.x, point.y),
							ci.getGreen(point.x, point.y),
							ci.getBlue(point.x, point.y)));
		}
		else
			lblPixels.setText(String.format("(%d,%d) - Gray(%d)", point.x,
					point.y, newImage.getPixel(point.x, point.y)));
	}

	/**
	 * Scale the image with specified rate.
	 * 
	 * @param rate
	 *            the scale rate
	 */
	private void scaleImage(double rate)
	{
		if (rate == currentRate)
			return;
		currentRate = rate;
		if (rotate % 4 != 0)
		{
			if (rate > 1)
			{
				newImage = geom.rotate(image, Math.PI / 2 * rotate);
				newImage = geom.scaleByRate(newImage, rate);
			}
			else if (rate == 1)
				newImage = geom.rotate(image, Math.PI / 2 * rotate);
			else
			{
				newImage = geom.scaleByRate(image, rate);
				newImage = geom.rotate(newImage, Math.PI / 2 * rotate);
			}
		}
		else
		{
			if (rate != 1)
				newImage = geom.scaleByRate(image, rate);
			else
				newImage = image;
		}
		refreshImage();
	}

	/**
	 * Update the dialog title.
	 */
	private void updateTitle()
	{
		setTitle(String.format(
				"%s%s",
				imageTitle,
				newImage == null ? "" : String.format(" - %d\u00d7%d",
						newImage.width(), newImage.height())));
	}

	/**
	 * Copy the current image to clip board.
	 */
	private void copyImage()
	{
		SystemUtils.setClipboardImage(image.restore());
		SwingUtils.noticeMessage(canvas,
				"The image has been copied to the system clipboard.");
	}

	/**
	 * Refresh the image and update the title.
	 */
	private void refreshImage()
	{
		updateTitle();
		canvas.setIcon(new javax.swing.ImageIcon(newImage.restore()));
		Runtime.getRuntime().gc();// recycle the memory
	}
}
