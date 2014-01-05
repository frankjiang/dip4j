/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved. hcogramDialog.java is PROPRIETARY/CONFIDENTIAL built in 2013. Use
 * is subject to license terms.
 */
package com.frank.dip.analyze;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import com.frank.math.MathUtils;

/**
 * The histogram dialog.
 * <p>
 * This dialog is the one to display the image histogram information.
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class HistogramDialog extends JDialog
{
	/**
	 * The side margin.
	 */
	private static final int	MARGIN				= 10;
	/**
	 * The width and height of arrow region.
	 */
	private static final int	ARROW_SIZE			= 4;
	/**
	 * serialVersionUID.
	 */
	private static final long	serialVersionUID	= -7372976210418271789L;
	/**
	 * The content panel which to draw the histograms.
	 */
	private final JPanel		contentPanel		= new CanvasPanel();
	/**
	 * The mouse position for displaying the histogram information.
	 */
	protected volatile Point	mouse;
	/**
	 * The histogram to display.
	 */
	protected Histogram			hist;
	/**
	 * Histogram original data of each channel.
	 */
	protected int[]				gray, red, green, blue;
	/**
	 * Histogram PDF data of each channel.
	 */
	protected float[]			grayPDF, redPDF, greenPDF, bluePDF;
	/**
	 * Histogram normalized PDF data of each channel.
	 */
	protected float[]			grayNPDF, redNPDF, greenNPDF, blueNPDF;
	/**
	 * The maximum value in the PDF.
	 */
	protected float				maxPDF;
	/**
	 * The check box of gray channel.
	 */
	private JCheckBox			chckbxGray;
	/**
	 * The check box of red channel.
	 */
	private JCheckBox			chckbxRed;
	/**
	 * The check box of green channel.
	 */
	private JCheckBox			chckbxGreen;
	/**
	 * The check box of blue channel.
	 */
	private JCheckBox			chckbxBlue;

	/**
	 * Canvas panel is the canvas to draw histograms.
	 * 
	 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
	 * @version 1.0.0
	 */
	private class CanvasPanel extends JPanel
	{
		/**
		 * serialVersionUID.
		 */
		private static final long	serialVersionUID	= -8052232429067449207L;

		/**
		 * @see javax.swing.JComponent#paint(java.awt.Graphics)
		 */
		public void paint(Graphics g)
		{
			super.paint(g);
			drawHistogram(g);
		}
	}

	/**
	 * Construct an instance of <tt>HistogramDialog</tt> with specified parent,
	 * title, histogram data.
	 * 
	 * @param parent
	 *            the parent window of the dialog
	 * @param title
	 *            the title of the dialog
	 * @param hist
	 *            the histogram instance of the specified image
	 */
	public HistogramDialog(Window parent, String title, Histogram hist)
	{
		this(parent, title, hist, true);
	}

	/**
	 * Construct an instance of <tt>HistogramDialog</tt> with specified parent,
	 * title, histogram data and modal type.
	 * 
	 * @param parent
	 *            the parent window of the dialog
	 * @param title
	 *            the title of the dialog
	 * @param hist
	 *            the histogram instance of the specified image
	 * @param modal
	 *            specifies whether dialog blocks input to other windows when
	 *            shown; calling to <code>setModal(true)</code> is equivalent to
	 *            <code>setModalityType(Dialog.DEFAULT_MODALITY_TYPE)</code>,
	 *            and calling to <code>setModal(false)</code> is equvivalent to
	 *            <code>setModalityType(Dialog.ModalityType.MODELESS)</code>
	 */
	public HistogramDialog(Window parent, String title, Histogram hist,
			boolean modal)
	{
		super(parent, title);
		setModal(modal);
		this.hist = hist;
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 700, 310);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		// initialize button panel
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton btnOK = new JButton("OK");
				btnOK.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						dispose();
					}
				});
				btnOK.setActionCommand("Cancel");
				buttonPane.add(btnOK);
			}
		}
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Channel",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		getContentPane().add(panel, BorderLayout.EAST);
		panel.setLayout(new GridLayout(4, 1, 0, 0));
		chckbxGray = new JCheckBox("Gray");
		chckbxGray.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				contentPanel.repaint();
			}
		});
		chckbxGray.setSelected(true);
		panel.add(chckbxGray);
		chckbxRed = new JCheckBox("Red");
		chckbxRed.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				contentPanel.repaint();
			}
		});
		chckbxRed.setSelected(true);
		panel.add(chckbxRed);
		chckbxGreen = new JCheckBox("Green");
		chckbxGreen.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				contentPanel.repaint();
			}
		});
		chckbxGreen.setSelected(true);
		panel.add(chckbxGreen);
		chckbxBlue = new JCheckBox("Blue");
		chckbxBlue.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				contentPanel.repaint();
			}
		});
		chckbxBlue.setSelected(true);
		panel.add(chckbxBlue);
		// add mouse listeners
		contentPanel.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseExited(MouseEvent e)
			{
				setMouse(null);
			}
		});
		contentPanel.addMouseMotionListener(new MouseMotionAdapter()
		{
			@Override
			public void mouseMoved(MouseEvent e)
			{
				setMouse(e.getPoint());
			}
		});
		initializeHistogram(hist);
		setVisible(true);
		// draw histogram
	}

	/**
	 * Draw the axis of the histogram.
	 * 
	 * @param g
	 *            the graphics to draw
	 * @param c
	 *            the color of the axis
	 * @param r
	 *            the region of drawing histogram
	 * @param minX
	 *            the minimum value of X
	 * @param maxX
	 *            the maximum value of X
	 * @param xs
	 *            the point count of the X-axis
	 * @param minY
	 *            the minimum value of Y
	 * @param maxY
	 *            the maximum value of Y
	 * @param ys
	 *            the point count of the Y-axis
	 */
	protected void drawAxis(Graphics g, Color c, Rectangle r, float minX,
			float maxX, int xs, float minY, float maxY, int ys)
	{
		FontMetrics fm = g.getFontMetrics();
		g.setColor(c);
		int x = r.x, y = r.y, w = r.width, h = r.height, m = MARGIN, m2 = MARGIN * 2;
		// draw axis
		{
			// draw axis line
			g.drawLine(x + m, y + h - m, x + m, y);// y-axis
			g.drawLine(x + m, y + h - m, x + w, y + h - m);// x-axis
			// draw arrows
			int d = ARROW_SIZE;
			g.drawLine(x + m, y, x + m - d, y + d);
			g.drawLine(x + m, y, x + m + d, y + d);
			g.drawLine(x + w, y + h - m, x + w - d, y + h - m - d);
			g.drawLine(x + w, y + h - m, x + w - d, y + h - m + d);
		}
		float p, d;
		int xt, yt;
		// draw axis points
		{
			// draw original point
			int dw, dh = fm.getHeight();
			String str;
			if (minX == minY)
			{
				str = format(minX);
				dw = fm.stringWidth(str);
				g.drawString(str, x + m - dw / 2, y + h - m + dh);
			}
			else
			{
				str = format(minX);
				dw = fm.stringWidth(str);
				g.drawString(str, x + m - dw / 2, y + h - m + dh);
				str = format(minY);
				dw = fm.stringWidth(str);
				g.drawString(str, x + m - dw - 7, y + h - m + dh / 2);
			}
			// draw max x point
			str = format(maxX);
			dw = fm.stringWidth(str);
			g.drawString(str, x + w - m - dw / 2, y + h - m + dh);
			// draw max y point
			str = format(maxPDF);
			dw = fm.stringWidth(str);
			g.drawString(str, x + m - dw - 10, y + m + dh / 2);
			// draw x-axis points
			p = 0.0f;
			d = 1.0f / xs;
			yt = y + h - m;
			for (int i = 0; i <= xs; i++, p += d)
			{
				xt = (int) ((w - m2) * p + x + m);
				g.drawLine(xt, yt - 1, xt, yt + 1);
			}
			// draw y-axis points
			p = 0.0f;
			d = 1.0f / ys;
			xt = x + m;
			for (int i = 0; i <= ys; i++, p += d)
			{
				yt = (int) ((h - m2) * p + y + m);
				g.drawLine(xt - 1, yt, xt + 1, yt);
			}
		}
		g.drawString("PDF", r.x, r.y);
		g.drawString("Scale", r.x + r.width, r.y + r.height - fm.getHeight()
				/ 2);
	}

	/**
	 * Draws color channel information. The information string will be drawing
	 * next to the histogram region {@code r}.
	 * 
	 * @param g
	 *            the graphics to draw
	 * @param r
	 *            the region of drawing histogram
	 * @param p
	 *            the position of current mouse, it can be null
	 */
	protected void drawInfo(Graphics g, Rectangle r, Point p)
	{
		if (p == null)
			return;
		if (!new Rectangle(r.x + MARGIN, r.y + MARGIN, r.width - 2 * MARGIN,
				r.height - 2 * MARGIN).contains(p))
			return;
		int i = p.x - r.x - MARGIN;
		if (i < 0 && i >= Histogram.COLOR_SCALE_LEVEL)
			return;
		Color c, old = g.getColor();
		FontMetrics fm = g.getFontMetrics();
		int d = fm.getHeight();
		int y = r.y + r.height / 3;
		int x = r.x + r.width;
		int max = fm.stringWidth("Green:");
		int minV, maxV;
		// paint gray channel if need
		if (chckbxGray.isSelected())
		{
			minV = hist.getMinimum();
			maxV = hist.getMaximum();
			c = Color.BLACK;
			g.setColor(c);
			g.drawString("Gray:", x, y);
			g.drawString(String.format("%d, Value: %d PDF: %.4f%%, [%d,%d]", i,
					gray[i], grayPDF[i] * 100, minV, maxV), x + max, y);
			y += d;
		}
		// return if the source image contains not color information
		if (red == null)
			return;
		Histogram.Color histC = (Histogram.Color) hist;
		// paint red channel if need
		if (chckbxRed.isSelected())
		{
			minV = histC.getMinimumRed();
			maxV = histC.getMaximumRed();
			c = Color.RED;
			g.setColor(c);
			g.drawString("Red:", x, y);
			g.drawString(String.format("%d, Value: %d PDF: %.4f%%, [%d,%d]", i,
					red[i], redPDF[i] * 100, minV, maxV), x + max, y);
			y += d;
		}
		// paint green channel if need
		if (chckbxGreen.isSelected())
		{
			minV = histC.getMinimumGreen();
			maxV = histC.getMaximumGreen();
			c = Color.GREEN;
			g.setColor(c);
			g.drawString("Green:", x, y);
			g.drawString(String.format("%d, Value: %d PDF: %.4f%%, [%d,%d]", i,
					green[i], greenPDF[i] * 100, minV, maxV), x + max, y);
			y += d;
		}
		// paint blue channel if need
		if (chckbxBlue.isSelected())
		{
			minV = histC.getMinimumBlue();
			maxV = histC.getMaximumBlue();
			c = Color.BLUE;
			g.setColor(c);
			g.drawString("Blue:", x, y);
			g.drawString(String.format("%d, Value: %d PDF: %.4f%%, [%d,%d]", i, blue[i],
					bluePDF[i] * 100, minV, maxV), x + max, y);
			y += d;
		}
		g.setColor(old);
	}

	/**
	 * Draw histograms.
	 * 
	 * @param g
	 *            the graphics to draw
	 */
	protected void drawHistogram(Graphics g)
	{
		FontMetrics fm = g.getFontMetrics();
		int dw = fm.stringWidth(format(maxPDF));
		Rectangle r = new Rectangle(10 + dw, 10, 256 + MARGIN * 2,
				200 + MARGIN * 2);
		if (hist instanceof Histogram.Gray)
		{
			if (chckbxGray.isSelected())
				drawHistogram(g, grayNPDF, Color.GRAY, r);
		}
		else if (hist instanceof Histogram.Color)
		{
			if (chckbxBlue.isSelected())
				drawHistogram(g, blueNPDF, Color.BLUE, r);
			if (chckbxGreen.isSelected())
				drawHistogram(g, greenNPDF, Color.GREEN, r);
			if (chckbxRed.isSelected())
				drawHistogram(g, redNPDF, Color.RED, r);
			if (chckbxGray.isSelected())
				drawHistogram(g, grayNPDF, Color.GRAY, r);
		}
		else
		{
			Color old = g.getColor();
			g.setColor(Color.RED);
			g.drawString(String.format("%s not supported", hist.getClass()
					.toString()), r.x + MARGIN, r.y + r.height / 2);
			g.setColor(old);
		}
		drawAxis(g, Color.BLACK, r, 0, 256, 5, 0, 1, 3);
		drawInfo(g, r, getMouse());
	}

	/**
	 * Draws the histogram of specified color channel.
	 * 
	 * @param g
	 *            the graphics to draw
	 * @param data
	 *            the normalized PDF data of the histogram
	 * @param c
	 *            the color of histogram
	 * @param r
	 *            the drawing region
	 */
	protected void drawHistogram(Graphics g, float[] data, Color c, Rectangle r)
	{
		Color old = g.getColor();
		g.setColor(c);
		int yt = r.y + r.height - MARGIN;
		int length = r.height - 2 * MARGIN;
		for (int x = r.x + MARGIN, i = 0; x < r.x + r.width - MARGIN; x++, i++)
			g.drawLine(x, yt, x, (int) (yt - length * data[i]));
		g.setColor(old);
	}

	/**
	 * Normalize the upper bound of the specified data.
	 * <p>
	 * The specified data should be non-negative.
	 * </p>
	 * 
	 * @param data
	 *            non-negative data
	 * @param max
	 *            maximum of the data
	 */
	protected void normalize(float[] data, double max)
	{
		for (int i = 0; i < data.length; i++)
			data[i] /= max;
	}

	/**
	 * Set the mouse position and repaint the content panel.
	 * 
	 * @param mouse
	 *            the mouse position
	 */
	public synchronized void setMouse(Point mouse)
	{
		this.mouse = mouse;
		contentPanel.repaint();
	}

	/**
	 * Returns the mouse position.
	 * 
	 * @return the mouse position
	 */
	public synchronized Point getMouse()
	{
		return mouse;
	}

	/**
	 * Format the double value to 0.00 style.
	 * 
	 * @param d
	 *            the double value to format
	 * @return float string
	 */
	public static String format(double d)
	{
		if ((int) d == d)
			return Integer.toString((int) d);
		return Double.toString(((int) (d * 10000)) / 100.0) + "%";
	}

	/**
	 * Initialize the histogram data arrays.
	 * 
	 * @param hist
	 *            the source histogram
	 */
	protected void initializeHistogram(Histogram hist)
	{
		if (hist instanceof Histogram.Gray)
		{
			gray = hist.getData().clone();
			grayPDF = hist.getPDF().clone();
			grayNPDF = grayPDF.clone();
			maxPDF = MathUtils.maximum(grayPDF);
			normalize(grayNPDF, maxPDF);
			red = green = blue = null;
			redPDF = greenPDF = bluePDF = null;
			redNPDF = greenNPDF = blueNPDF = null;
			chckbxRed.setEnabled(false);
			chckbxGreen.setEnabled(false);
			chckbxBlue.setEnabled(false);
		}
		else if (hist instanceof Histogram.Color)
		{
			Histogram.Color hc = (Histogram.Color) hist;
			gray = hc.getData().clone();
			grayPDF = hc.getPDF().clone();
			grayNPDF = grayPDF.clone();
			red = hc.getRed().clone();
			redPDF = hc.getPDFRed().clone();
			redNPDF = redPDF.clone();
			green = hc.getGreen().clone();
			greenPDF = hc.getPDFGreen().clone();
			greenNPDF = greenPDF.clone();
			blue = hc.getBlue().clone();
			bluePDF = hc.getPDFBlue().clone();
			blueNPDF = bluePDF.clone();
			float[] maxs = new float[] { MathUtils.maximum(redPDF),
					MathUtils.maximum(grayPDF), MathUtils.maximum(greenPDF),
					MathUtils.maximum(bluePDF) };
			Arrays.sort(maxs);
			maxPDF = maxs[maxs.length - 1];
			normalize(grayNPDF, maxPDF);
			normalize(redNPDF, maxPDF);
			normalize(greenNPDF, maxPDF);
			normalize(blueNPDF, maxPDF);
		}
		else
		{
			gray = red = green = blue = null;
			grayPDF = redPDF = greenPDF = bluePDF = null;
			grayNPDF = redNPDF = greenNPDF = blueNPDF = null;
		}
		// throw new IllegalArgumentException(String.format(
		// "%s format is not supported", hist.getClass().toString()));
	}
}
