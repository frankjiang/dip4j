/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved. FuctionDislpayDialog.java is PROPRIETARY/CONFIDENTIAL built in
 * 2013. Use is subject to license terms.
 */
package com.frank.dip.analyze;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import com.frank.dip.math.Function;
import com.frank.dip.math.ScalableFunction;

/**
 * Function display dialog.
 * <p>
 * A function display dialog is used for function line displaying and function
 * parameter selection.
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class FunctionDislpayDialog extends JDialog implements Observer
{
	/**
	 * The flag for clicking OK button to close the dialog.
	 */
	public static final int				OK_CLICKED			= 0;
	/**
	 * The flag for clicking Cancel button to close the dialog.
	 */
	public static final int				CANCEL_CLICKED		= 1;
	/**
	 * The side margin.
	 */
	private static final int			MARGIN				= 10;
	/**
	 * The width and height of arrow region.
	 */
	private static final int			ARROW_SIZE			= 4;
	/**
	 * serialVersionUID.
	 */
	private static final long			serialVersionUID	= 3904116568664480495L;
	/**
	 * The content panel.
	 */
	private final JPanel				contentPanel		= new JPanel();
	/**
	 * The canvas panel which to draw the function line.
	 */
	private CanvasPanel					canvas;
	/**
	 * The function to display.
	 */
	private Function					function;
	/**
	 * The parameter panels.
	 */
	private Collection<ParameterPanel>	pps;
	/**
	 * The close event type flag.
	 * 
	 * @see #OK_CLICKED
	 * @see #CANCEL_CLICKED
	 */
	private int							closeType			= CANCEL_CLICKED;
	/**
	 * A string represents specified function.
	 */
	private String						functionString;
	/**
	 * The string of X-axis.
	 */
	private String						xAxisString			= "X";
	/**
	 * The string of Y-axis.
	 */
	private String						yAxisString			= "Y";
	/**
	 * The max X-axis value.
	 */
	private Double						maxXValue			= null;
	/**
	 * The max Y-axis value.
	 */
	private Double						maxYValue			= null;

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
			drawFuction(g);
		}
	}

	/**
	 * Construct an instance of <tt>FunctionDislpayDialog</tt>.
	 * 
	 * @param window
	 *            the parent window
	 * @param title
	 *            the dialog title
	 * @param keys
	 *            the parameter names
	 * @param properties
	 *            the parameter properties
	 * @param function
	 *            the function instance
	 * @wbp.parser.constructor
	 */
	public FunctionDislpayDialog(Window window, String title, String[] keys,
			Properties properties, Function function)
	{
		this(window, title, keys, properties, function, true);
	}

	/**
	 * Construct an instance of <tt>FunctionDislpayDialog</tt>.
	 * 
	 * @param window
	 *            the parent window
	 * @param title
	 *            the dialog title
	 * @param keys
	 *            the parameter names
	 * @param properties
	 *            the parameter properties
	 * @param function
	 *            the function instance
	 * @param modal
	 *            specifies whether dialog blocks input to other windows when
	 *            shown; calling to <code>setModal(true)</code> is equivalent to
	 *            <code>setModalityType(Dialog.DEFAULT_MODALITY_TYPE)</code>,
	 *            and calling to <code>setModal(false)</code> is equvivalent to
	 *            <code>setModalityType(Dialog.ModalityType.MODELESS)</code>
	 */
	public FunctionDislpayDialog(Window window, String title, String[] keys,
			Properties properties, Function function, boolean modal)
	{
		super(window, title);
		setModal(modal);
		this.function = function;
		if (function != null)
		{
			this.functionString = function.getFunctionString();
			for (Entry<Object, Object> e : function.getProperties().entrySet())
				if (!properties.containsKey(e.getKey()))
					properties.put(e.getKey(), e.getValue());
			function.setProperties(properties);
		}
		setBounds(100, 100, 465, 364);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			JPanel panel = new JPanel();
			getContentPane().add(panel, BorderLayout.SOUTH);
			panel.setLayout(new BorderLayout(0, 0));
			{
				JPanel buttonPane = new JPanel();
				panel.add(buttonPane, BorderLayout.SOUTH);
				buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
				{
					JButton okButton = new JButton("OK");
					okButton.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							dispose();
							closeType = OK_CLICKED;
						}
					});
					okButton.setActionCommand("OK");
					buttonPane.add(okButton);
					getRootPane().setDefaultButton(okButton);
				}
				{
					JButton cancelButton = new JButton("Cancel");
					cancelButton.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							dispose();
							closeType = CANCEL_CLICKED;
						}
					});
					cancelButton.setActionCommand("Cancel");
					buttonPane.add(cancelButton);
				}
			}
			{
				JPanel paramPane = new JPanel();
				paramPane.setBorder(new TitledBorder(null, "Parameters",
						TitledBorder.LEADING, TitledBorder.TOP, null, null));
				panel.add(paramPane, BorderLayout.NORTH);
				paramPane.setLayout(new GridLayout(keys.length, 1, 0, 0));
				{
					canvas = new CanvasPanel();
					Dimension preferredSize = new Dimension(300, 250);
					canvas.setPreferredSize(preferredSize);
					canvas.setSize(preferredSize);
					getContentPane().add(canvas, BorderLayout.CENTER);
				}
				pps = new java.util.Vector<ParameterPanel>(keys.length);
				for (String key : keys)
					if (properties.containsKey(key)
							&& properties.containsKey(key + "_max")//$NON-NLS-1$
							&& properties.containsKey(key + "_min"))//$NON-NLS-N$
					{
						ParameterPanel pp = new ParameterPanel(
								key,
								((Number) properties.get(key + "_max")).doubleValue(),//$NON-NLS-1$
								((Number) properties.get(key + "_min")).doubleValue(),//$NON-NLS-1$
								((Number) properties.get(key)).doubleValue());
						pp.addObserver(this);
						paramPane.add(pp);
						pps.add(pp);
					}
				pack();
			}
		}
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	/**
	 * Draw the function line.
	 * 
	 * @param g
	 *            the graphics to draw
	 */
	protected void drawFuction(Graphics g)
	{
		FontMetrics fm = g.getFontMetrics();
		int dw = maxYValue == null ? fm.stringWidth("255") : //$NON-NLS-1$
				fm.stringWidth(String.format("%3g", maxYValue));//$NON-NLS-1$
		Rectangle r = new Rectangle(10 + dw, 10, 256 + MARGIN * 2,
				200 + MARGIN * 2);
		float minX = 0;
		float maxX = 255;
		float minY = 0;
		float maxY = 255;
		// drawing axis
		drawAxis(g, Color.black, r, minX, maxX, 5, minY, maxY, 3);
		// drawing function lines
		if (function == null)
		{
			g.setColor(Color.BLACK);
			String noFunc = "No function to display.";
			dw = fm.stringWidth(noFunc);
			g.drawString(noFunc, r.x + r.width / 2 - dw / 2, r.y + r.height / 3);
		}
		else
		{
			g.setColor(Color.BLUE);
			int x = r.x + MARGIN, y = r.y + r.height - MARGIN, xt, yt;
			if (function instanceof ScalableFunction)
			{
				ScalableFunction sf = (ScalableFunction) function;
				for (float i = minX; i <= maxX; i += 1)
				{
					xt = (int) (r.x + MARGIN + i);
					yt = r.y
							+ r.height
							- MARGIN
							- (int) (Math.round((sf.scaledFunction(i) - minY)
									/ (maxY - minY) * (r.height - 2 * MARGIN)));
					g.drawLine(x, y, xt, yt);
					x = xt;
					y = yt;
				}
			}
			else
				for (float i = minX; i <= maxX; i += 1)
				{
					xt = (int) (r.x + MARGIN + i);
					yt = r.y
							+ r.height
							- MARGIN
							- (int) (Math.round((function.function(i) - minY)
									/ (maxY - minY) * (r.height - 2 * MARGIN)));
					g.drawLine(x, y, xt, yt);
					x = xt;
					y = yt;
				}
			g.setColor(Color.BLACK);
			dw = fm.stringWidth(functionString);
			Rectangle rec = getContentPane().getBounds();
			g.drawString(functionString,
					dw < r.width ? (r.x + r.width / 2 - dw / 2)
							: (rec.width - 10 - dw), fm.getHeight());
		}
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
			if (maxXValue == null)
				str = format(maxX);
			else
				str = String.format("%3g", maxXValue);
			dw = fm.stringWidth(str);
			g.drawString(str, x + w - m - dw / 2, y + h - m + dh);
			// draw max y point
			if (maxYValue == null)
				str = format(maxY);
			else
				str = String.format("%3g", maxYValue);
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
		g.drawString(yAxisString, r.x, r.y);
		g.drawString(xAxisString, r.x + r.width,
				r.y + r.height - fm.getHeight() / 2);
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
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable o, Object arg)
	{
		Properties p = new Properties();
		for (ParameterPanel pp : pps)
			pp.addToProperty(p);
		if (function != null)
			function.setProperties(p);
		repaint();
	}

	/**
	 * Getter for functionString.
	 * 
	 * @return the functionString
	 */
	public String getFunctionString()
	{
		return functionString;
	}

	/**
	 * Setter for functionString.
	 * 
	 * @param functionString
	 *            the value of functionString
	 */
	public void setFunctionString(String functionString)
	{
		this.functionString = functionString;
	}

	/**
	 * Returns the parameters of the current function.
	 * 
	 * @return the parameters, <tt>null</tt> if canceled
	 */
	public Properties getProperties()
	{
		if (closeType == CANCEL_CLICKED)
			return null;
		Properties p = new Properties();
		for (ParameterPanel pp : pps)
			pp.addToProperty(p);
		return p;
	}

	/**
	 * Set the X-axis string.
	 * 
	 * @param xAxisString
	 *            the X-axis string
	 */
	public void setXAxisString(String xAxisString)
	{
		this.xAxisString = xAxisString;
	}

	/**
	 * Set the Y-axis string.
	 * 
	 * @param yAxisString
	 *            the Y-axis string
	 */
	public void setYAxisString(String yAxisString)
	{
		this.yAxisString = yAxisString;
	}

	/**
	 * Setter for maxXValue.
	 * 
	 * @param maxXValue
	 *            the value of maxXValue
	 */
	public void setMaxXValue(double maxXValue)
	{
		this.maxXValue = maxXValue;
	}

	/**
	 * Setter for maxYValue.
	 * 
	 * @param maxYValue
	 *            the value of maxYValue
	 */
	public void setMaxYValue(double maxYValue)
	{
		this.maxYValue = maxYValue;
	}
}
