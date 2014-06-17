/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved.
 * ImageDisplayPanel.java is PROPRIETARY/CONFIDENTIAL built in 3:19:27 PM, Feb
 * 28, 2014.
 * Use is subject to license terms.
 */
package com.frank.dip.analyze;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;

import com.frank.dip.ColorImage;
import com.frank.dip.Image;
import com.frank.dip.geom.Geometry;

/**
 * Image display panel is a panel which can display the image.
 * <p>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class ImageDisplayPanel extends JPanel implements ImageDisplay
{
	/**
	 * An observable class for update GUI.
	 * 
	 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
	 * @version 1.0.0
	 */
	private class InnerObservable extends Observable
	{
		@Override
		public void setChanged()
		{
			super.setChanged();
		}
	}

	/**
	 * serialVersionUID.
	 */
	private static final long	serialVersionUID	= -6706355780874371503L;
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
	/**
	 * Reset the current image, both rotate angle and scale rate.
	 */
	private JButton				btnReset;
	/**
	 * <code>true</code> if display the alpha channel info.
	 */
	private boolean				showAlphaInfo;
	/**
	 * The observable.
	 */
	InnerObservable				observable;

	/**
	 * Construct an instance of <tt>ImageDisplayDialog</tt>.
	 * 
	 * @param title
	 *            the title of the image instance
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
	public ImageDisplayPanel(String title, Image image, boolean showPixelInfo,
			boolean showAlphaInfo, boolean hidIfExit)
	{
		// Initialize fields.
		setName(String.format(
				"%s%s",
				title,
				image == null ? "" : String.format(" - %d\u00d7%d",
						image.width(), image.height())));
		this.image = image;
		this.newImage = image.clone();
		this.imageTitle = title;
		this.geom = Geometry.getGeometry(image, Geometry.TYPE_BICUBIC,
				Geometry.FILL_WITH_BLANK);
		this.observable = new InnerObservable();
		// Initialize the GUI.
		setLayout(new BorderLayout(0, 0));
		lblPixels = new JLabel("");
		lblPixels.setHorizontalAlignment(SwingConstants.LEFT);
		add(lblPixels, BorderLayout.SOUTH);
		canvas = new JLabel("");
		canvas.setHorizontalAlignment(SwingConstants.CENTER);
		JScrollPane scrollPane = new JScrollPane(canvas);
		add(scrollPane, BorderLayout.CENTER);
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
				.getResource("/com/frank/dip/res/redo.png")));//$NON-NLS-1$
		toolBar.add(btnRotateright);
		btnReset = new JButton("");//$NON-NLS-1$
		btnReset.setToolTipText("Reset the current image to the source image.");
		btnReset.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				reset();
			}
		});
		btnReset.setIcon(new ImageIcon(ImageDisplayDialog.class
				.getResource("/com/frank/dip/res/refresh-16.png")));
		toolBar.add(btnReset);
		comboBox = new JComboBox();
		comboBox.setEditable(true);
		comboBox.setModel(new DefaultComboBoxModel(new String[] { "10%", //$NON-NLS-1$
				"20%", "50%", "100%", //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
				"150%", "200%", "300%", //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
				"1000%" }));//$NON-NLS-1$
		comboBox.setSelectedIndex(3);
		comboBox.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Object obj = comboBox.getSelectedItem();
				if (obj == null)
					return;
				String s = ((String) obj).replaceAll("%", "");//$NON-NLS-1$//$NON-NLS-2$
				Double rate = 1.0;
				try
				{
					rate = Double.valueOf(s) / 100.0;
					scaleImage(rate);
				}
				catch (NumberFormatException e1)
				{
					comboBox.setSelectedItem("100%");//$NON-NLS-1$
				}
			}
		});
		toolBar.add(comboBox);
		initialize(showPixelInfo, hidIfExit);
	}

	/**
	 * Reset the current image.
	 */
	protected void reset()
	{
		newImage = ImageDisplayPanel.this.image;
		rotate = 0;
		currentRate = 1.0;
		comboBox.setSelectedItem("100%");//$NON-NLS-1$
		refreshImage();
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
					ImageDisplayPanel.this.validate();
					int height = lblPixels.getHeight();
					Dimension d = ImageDisplayPanel.this.getSize();
					ImageDisplayPanel.this.setSize(d.width, d.height + height);
					ImageDisplayPanel.this.setCursor(Cursor
							.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
					observable.setChanged();
					observable.notifyObservers(height);
				}

				@Override
				public void mouseExited(MouseEvent e)
				{
					int height = lblPixels.getHeight();
					Dimension d = ImageDisplayPanel.this.getSize();
					ImageDisplayPanel.this.setSize(d.width, d.height - height);
					ImageDisplayPanel.this.setCursor(Cursor.getDefaultCursor());
					observable.setChanged();
					observable.notifyObservers(-height);
					lblPixels.setText("");//$NON-NLS-1$
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
			if (showAlphaInfo)
				lblPixels.setText(String.format(
						"(%d,%d) - ARGB(%d,%d,%d,%d)", point.x,//$NON-NLS-1$
						point.y, ci.getRed(point.x, point.y),
						ci.getGreen(point.x, point.y),
						ci.getBlue(point.x, point.y)));
			else
				lblPixels.setText(String.format(
						"(%d,%d) - RGB(%d,%d,%d)", point.x,//$NON-NLS-1$
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
		setName(String.format(
				"%s%s",//$NON-NLS-1$
				imageTitle,
				newImage == null ? "" : String.format(" - %d\u00d7%d",//$NON-NLS-1$//$NON-NLS-2$
						newImage.width(), newImage.height())));
		observable.notifyObservers(getName());
		observable.setChanged();
	}

	/**
	 * Adds an observer to the set of observers for this object, provided
	 * that it is not the same as some observer already in the set.
	 * The order in which notifications will be delivered to multiple
	 * observers is not specified. See the class comment.
	 * 
	 * @param o
	 *            an observer to be added.
	 * @throws NullPointerException
	 *             if the parameter o is null.
	 */
	public void addObserver(Observer o)
	{
		observable.addObserver(o);
	}

	/**
	 * Deletes an observer from the set of observers of this object.
	 * Passing <CODE>null</CODE> to this method will have no effect.
	 * 
	 * @param o
	 *            the observer to be deleted.
	 */
	public void deleteObserver(Observer o)
	{
		observable.deleteObserver(o);
	}

	/**
	 * Clears the observer list so that this object no longer has any observers.
	 */
	public void deleteObservers()
	{
		observable.deleteObservers();
	}

	/**
	 * @see com.frank.dip.demo.ImageDisplay#copyImage()
	 */
	@Override
	public void copyImage()
	{
		// Set to clip board image.
		Transferable trans = new Transferable()
		{
			public DataFlavor[] getTransferDataFlavors()
			{
				return new DataFlavor[] { DataFlavor.imageFlavor };
			}

			public boolean isDataFlavorSupported(DataFlavor flavor)
			{
				return DataFlavor.imageFlavor.equals(flavor);
			}

			public Object getTransferData(DataFlavor flavor)
					throws UnsupportedFlavorException, IOException
			{
				if (isDataFlavorSupported(flavor))
					return image.restore();
				throw new UnsupportedFlavorException(flavor);
			}
		};
		Toolkit.getDefaultToolkit().getSystemClipboard()
				.setContents(trans, null);
		// Show notice message.
		JOptionPane.showConfirmDialog(canvas,
				"The image has been copied to the system clipboard.", "Notice",
				JOptionPane.CLOSED_OPTION, JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * @see com.frank.dip.demo.ImageDisplay#refreshImage()
	 */
	@Override
	public void refreshImage()
	{
		updateTitle();
		canvas.setIcon(new javax.swing.ImageIcon(newImage.restore()));
		Runtime.getRuntime().gc();// recycle the memory
	}

	/**
	 * @see com.frank.dip.demo.ImageDisplay#getImage()
	 */
	@Override
	public Image getImage()
	{
		return image;
	}

	/**
	 * @see com.frank.dip.demo.ImageDisplay#setImage(com.frank.dip.Image,
	 *      java.lang.String)
	 */
	@Override
	public void setImage(Image newImage, String newTitle)
	{
		image = newImage;
		imageTitle = newTitle;
		reset();
	}
}
