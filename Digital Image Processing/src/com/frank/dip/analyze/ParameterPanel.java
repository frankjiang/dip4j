/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved. ParameterPanel.java is PROPRIETARY/CONFIDENTIAL built in 2013. Use
 * is subject to license terms.
 */
package com.frank.dip.analyze;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observer;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * The parameter displaying and selection panel.
 * <p>
 * In this panel, the parameter selection can be displaying by input and slider.
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class ParameterPanel extends JPanel
{
	/**
	 * The accuracy of the parameter selecting.
	 */
	public static final double	ACCURACY	= 10000;

	/**
	 * Parameter observable class.
	 * 
	 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
	 * @version 1.0.0
	 */
	private final class Observable extends java.util.Observable
	{
		/**
		 * Set the status changed and notify all the observers.
		 * 
		 * @param args
		 *            the arguments to pass
		 */
		public void toNotify(Object... args)
		{
			setChanged();
			notifyObservers(args);
		}
	}

	/**
	 * serialVersionUID.
	 */
	private static final long	serialVersionUID	= 8314277529144909961L;
	/**
	 * The value text of parameter to display.
	 */
	private JTextField			textField;
	/**
	 * The slider of parameter selection.
	 */
	private JSlider				slider;
	/**
	 * The maximum value of the parameter.
	 */
	private double				max;
	/**
	 * The minimum value of the parameter.
	 */
	private double				min;
	/**
	 * The parameter name
	 */
	private String				paramName;
	/**
	 * The parameter name label.
	 */
	private JLabel				lblParam;
	/**
	 * The button of setting new parameter.
	 */
	private JButton				btnSet;
	/**
	 * The observable of the parameter.
	 */
	private Observable			observable;

	/**
	 * Create the panel.
	 */
	public ParameterPanel(String paramName, double max, double min, double value)
	{
		this.paramName = paramName;
		observable = new Observable();
		this.setMax(max);
		this.setMin(min);
		FlowLayout flowLayout = (FlowLayout) getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		lblParam = new JLabel(String.format("%s:", paramName));
		add(lblParam);
		slider = new JSlider();
		slider.setMinimum(0);
		slider.setMaximum((int) ((max - min) * ACCURACY));
		slider.addChangeListener(new ChangeListener()
		{
			public void stateChanged(ChangeEvent e)
			{
				double value = getValue();
				textField.setText(Double.toString(value));
				String toolTipText = String.format("%s = %f",
						ParameterPanel.this.paramName, value);
				textField.setToolTipText(toolTipText);
				slider.setToolTipText(toolTipText);
				observable.toNotify();
			}
		});
		add(slider);
		textField = new JTextField();
		add(textField);
		textField.setColumns(10);
		btnSet = new JButton("Set");
		btnSet.setToolTipText("Set the input text value to the parameter");
		btnSet.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String s = textField.getText();
				try
				{
					setValue(Double.valueOf(s));
				}
				catch (NumberFormatException e0)
				{
				}
			}
		});
		add(btnSet);
		setValue(value);
	}

	/**
	 * Set the parameter name and update the UI.
	 * 
	 * @param paramName
	 */
	public void setParamName(String paramName)
	{
		this.paramName = paramName;
		repaint();
	}

	/**
	 * Returns the parameter name.
	 * 
	 * @return the parameter name
	 */
	public String getParamName()
	{
		return paramName;
	}

	/**
	 * Getter for max.
	 * 
	 * @return the max
	 */
	public double getMax()
	{
		return max;
	}

	/**
	 * Setter for max.
	 * 
	 * @param max
	 *            the value of max
	 */
	public void setMax(double max)
	{
		this.max = max;
	}

	/**
	 * Getter for min.
	 * 
	 * @return the min
	 */
	public double getMin()
	{
		return min;
	}

	/**
	 * Setter for min.
	 * 
	 * @param min
	 *            the value of min
	 */
	public void setMin(double min)
	{
		this.min = min;
	}

	/**
	 * Returns the current value of the parameter.
	 * 
	 * @return the value
	 */
	public double getValue()
	{
		return (int) ((slider.getValue() * (max - min) / slider.getMaximum() + min) * ACCURACY)
				/ ACCURACY;
	}

	/**
	 * Set the parameter value, update the UI and notify all the observers.
	 * 
	 * @param value
	 *            the parameter value to set
	 */
	public void setValue(double value)
	{
		if (value > max || value < min)
		{
			if (value < min)
				value = min;
			else
				value = max;
		}
		slider.setValue((int) ((value - min) * slider.getMaximum() / (max - min)));
		textField.setText(Double.toString(value));
		String toolTipText = String.format("%s = %f", paramName, value);
		textField.setToolTipText(toolTipText);
		slider.setToolTipText(toolTipText);
		observable.toNotify();
	}

	/**
	 * Add the current parameter to the specified properties.
	 * 
	 * @param p
	 *            the specified properties
	 */
	public void addToProperty(Properties p)
	{
		p.put(paramName, getValue());
	}

	/**
	 * Adds an observer to the set of observers for this object, provided that
	 * it is not the same as some observer already in the set. The order in
	 * which notifications will be delivered to multiple observers is not
	 * specified. See the class comment.
	 * 
	 * @param o
	 *            an observer to be added.
	 */
	public void addObserver(Observer o)
	{
		if (o != null)
			observable.addObserver(o);
	}
}
