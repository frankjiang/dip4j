/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights
 * reserved. ConvolveDialog.java is PROPRIETARY/CONFIDENTIAL built in 2013. Use
 * is subject to license terms.
 */
package com.frank.dip.enhance.convolver;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.StringTokenizer;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

/**
 * The convolve dialog.
 * <p>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class ConvolveDialog extends JDialog
{
	/**
	 * serialVersionUID.
	 */
	private static final long	serialVersionUID	= 621977713904475193L;
	/**
	 * The content panel.
	 */
	private final JPanel		contentPanel		= new JPanel();
	/**
	 * The text area of kernel values.
	 */
	private JTextArea			txtKernel;
	/**
	 * The text field of kernel width.
	 */
	private JTextField			txtWidth;
	/**
	 * The text field of kernel height.
	 */
	private JTextField			txtHeight;
	/**
	 * The convolver.
	 */
	private Convolver			convolver;
	/**
	 * The combo box of edge filling strategy selecting.
	 */
	private JComboBox			comboEdge;
	/**
	 * The combo box of accuracy calculating strategy selecting.
	 */
	private JComboBox			comboAccuracy;

	/**
	 * Create a convolve dialog.
	 * 
	 * @param window
	 *            the parent window, can be <tt>null</tt>
	 */
	public ConvolveDialog(Window window)
	{
		super(window);
		setTitle("Convolve Dialog");
		setModal(true);
		setBounds(100, 100, 450, 368);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JPanel panelSize = new JPanel();
			panelSize.setBorder(new TitledBorder(null, "Kernel Dimension",
					TitledBorder.LEADING, TitledBorder.TOP, null, null));
			contentPanel.add(panelSize, BorderLayout.SOUTH);
			GridBagLayout gbl_panelSize = new GridBagLayout();
			gbl_panelSize.columnWidths = new int[] { 0, 0, 0 };
			gbl_panelSize.rowHeights = new int[] { 0, 0, 0, 0, 0 };
			gbl_panelSize.columnWeights = new double[] { 0.0, 1.0,
					Double.MIN_VALUE };
			gbl_panelSize.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0,
					Double.MIN_VALUE };
			panelSize.setLayout(gbl_panelSize);
			{
				JLabel lblWidth = new JLabel("width:");
				lblWidth.setHorizontalAlignment(SwingConstants.RIGHT);
				GridBagConstraints gbc_lblWidth = new GridBagConstraints();
				gbc_lblWidth.insets = new Insets(0, 5, 5, 5);
				gbc_lblWidth.anchor = GridBagConstraints.EAST;
				gbc_lblWidth.gridx = 0;
				gbc_lblWidth.gridy = 0;
				panelSize.add(lblWidth, gbc_lblWidth);
			}
			{
				txtWidth = new JTextField();
				txtWidth.setToolTipText("Set the width of kernel pattern.");
				txtWidth.setText("3");
				GridBagConstraints gbc_txtWidth = new GridBagConstraints();
				gbc_txtWidth.insets = new Insets(0, 0, 5, 0);
				gbc_txtWidth.fill = GridBagConstraints.HORIZONTAL;
				gbc_txtWidth.gridx = 1;
				gbc_txtWidth.gridy = 0;
				panelSize.add(txtWidth, gbc_txtWidth);
				txtWidth.setColumns(10);
			}
			{
				JLabel lblHeight = new JLabel("height:");
				lblHeight.setHorizontalAlignment(SwingConstants.RIGHT);
				GridBagConstraints gbc_lblHeight = new GridBagConstraints();
				gbc_lblHeight.anchor = GridBagConstraints.EAST;
				gbc_lblHeight.insets = new Insets(0, 5, 5, 5);
				gbc_lblHeight.gridx = 0;
				gbc_lblHeight.gridy = 1;
				panelSize.add(lblHeight, gbc_lblHeight);
			}
			{
				txtHeight = new JTextField();
				txtHeight.setToolTipText("set the height of kernel pattern.");
				txtHeight.setText("3");
				GridBagConstraints gbc_txtHeight = new GridBagConstraints();
				gbc_txtHeight.insets = new Insets(0, 0, 5, 0);
				gbc_txtHeight.fill = GridBagConstraints.HORIZONTAL;
				gbc_txtHeight.gridx = 1;
				gbc_txtHeight.gridy = 1;
				panelSize.add(txtHeight, gbc_txtHeight);
				txtHeight.setColumns(10);
			}
			{
				JLabel lblEdge = new JLabel("edge:");
				lblEdge.setHorizontalAlignment(SwingConstants.RIGHT);
				GridBagConstraints gbc_lblEdge = new GridBagConstraints();
				gbc_lblEdge.anchor = GridBagConstraints.EAST;
				gbc_lblEdge.insets = new Insets(0, 5, 5, 5);
				gbc_lblEdge.gridx = 0;
				gbc_lblEdge.gridy = 2;
				panelSize.add(lblEdge, gbc_lblEdge);
			}
			{
				comboEdge = new JComboBox();
				comboEdge.setToolTipText("Select the edge filling strategy.");
				comboEdge.setModel(new DefaultComboBoxModel(new String[] {
						"Fill with zero.", "Fill with source pixel value." }));
				comboEdge.setSelectedIndex(0);
				GridBagConstraints gbc_comboEdge = new GridBagConstraints();
				gbc_comboEdge.insets = new Insets(0, 0, 5, 0);
				gbc_comboEdge.fill = GridBagConstraints.HORIZONTAL;
				gbc_comboEdge.gridx = 1;
				gbc_comboEdge.gridy = 2;
				panelSize.add(comboEdge, gbc_comboEdge);
			}
			{
				JLabel lblAccuracy = new JLabel("accuracy:");
				lblAccuracy.setHorizontalAlignment(SwingConstants.RIGHT);
				GridBagConstraints gbc_lblAccuracy = new GridBagConstraints();
				gbc_lblAccuracy.anchor = GridBagConstraints.EAST;
				gbc_lblAccuracy.insets = new Insets(0, 5, 0, 5);
				gbc_lblAccuracy.gridx = 0;
				gbc_lblAccuracy.gridy = 3;
				panelSize.add(lblAccuracy, gbc_lblAccuracy);
			}
			{
				comboAccuracy = new JComboBox();
				comboAccuracy
						.setToolTipText("Select the accuracy calculating strategy.");
				comboAccuracy.setModel(new DefaultComboBoxModel(new String[] {
						"Interrupt the out of range value.",
						"Normalize all the pixel values." }));
				GridBagConstraints gbc_comboAccuracy = new GridBagConstraints();
				gbc_comboAccuracy.fill = GridBagConstraints.HORIZONTAL;
				gbc_comboAccuracy.gridx = 1;
				gbc_comboAccuracy.gridy = 3;
				panelSize.add(comboAccuracy, gbc_comboAccuracy);
			}
		}
		{
			JScrollPane scrollPane = new JScrollPane();
			contentPanel.add(scrollPane, BorderLayout.CENTER);
			{
				txtKernel = new JTextArea();
				txtKernel
						.setToolTipText("Set the kernel values, base on line order first, sperate them with tab, space, comma, or enter.");
				scrollPane.setViewportView(txtKernel);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						String widthStr = txtWidth.getText();
						String heightStr = txtHeight.getText();
						int width = 0, height = 0;
						try
						{
							width = Integer.valueOf(widthStr);
						}
						catch (NumberFormatException e1)
						{
							Toolkit.getDefaultToolkit().beep();
							JOptionPane.showConfirmDialog(
									contentPanel,
									String.format(
											"The width string \"%s\" is not an integer.",
											widthStr), "Error",
									JOptionPane.CLOSED_OPTION,
									JOptionPane.ERROR_MESSAGE);
							convolver = null;
							return;
						}
						if (width < 1)
						{
							Toolkit.getDefaultToolkit().beep();
							JOptionPane.showConfirmDialog(contentPanel, String
									.format("The width(%d) must be positive.",
											width), "Error",
									JOptionPane.CLOSED_OPTION,
									JOptionPane.ERROR_MESSAGE);
							convolver = null;
							return;
						}
						try
						{
							height = Integer.valueOf(heightStr);
						}
						catch (NumberFormatException e1)
						{
							Toolkit.getDefaultToolkit().beep();
							JOptionPane.showConfirmDialog(
									contentPanel,
									String.format(
											"The height string \"%s\" is not an integer.",
											heightStr), "Error",
									JOptionPane.CLOSED_OPTION,
									JOptionPane.ERROR_MESSAGE);
							convolver = null;
							return;
						}
						if (height < 1)
						{
							Toolkit.getDefaultToolkit().beep();
							JOptionPane.showConfirmDialog(contentPanel, String
									.format("The height(%d) must be positive.",
											height), "Error",
									JOptionPane.CLOSED_OPTION,
									JOptionPane.ERROR_MESSAGE);
							convolver = null;
							return;
						}
						float[] values = new float[width * height];
						float t;
						String s;
						int i = 0;
						StringTokenizer st = new StringTokenizer(txtKernel
								.getText(), " \r\n\t\f,\uff0c");
						while (st.hasMoreTokens() && i < values.length)
						{
							s = st.nextToken();
							try
							{
								t = Float.valueOf(s);
							}
							catch (NumberFormatException e1)
							{
								Toolkit.getDefaultToolkit().beep();
								JOptionPane.showConfirmDialog(contentPanel,
										String.format(
												"Illegal float value: %s.", s),
										"Error", JOptionPane.CLOSED_OPTION,
										JOptionPane.ERROR_MESSAGE);
								convolver = null;
								return;
							}
							values[i++] = t;
						}
						if (i < values.length)
						{
							Toolkit.getDefaultToolkit().beep();
							JOptionPane.showConfirmDialog(
									contentPanel,
									String.format(
											"The amount of values input is %d, however width(%d) * height(%d) = %d is expect.",
											i, width, height, width * height),
									"Error", JOptionPane.CLOSED_OPTION,
									JOptionPane.ERROR_MESSAGE);
							convolver = null;
							return;
						}
						convolver = new Convolver(
								new SingleKernel(width, height, values),
								comboAccuracy.getSelectedIndex() == 0 ? Convolver.HINT_ACCURACY_INTERRUPT
										: Convolver.HINT_ACCURACY_NORMALIZE,
								comboEdge.getSelectedIndex() == 0 ? Convolver.HINT_EDGE_FILL_ZERO
										: Convolver.HINT_EDGE_SOURCE);
						dispose();
					}
				});
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						convolver = null;
						dispose();
					}
				});
				buttonPane.add(cancelButton);
			}
		}
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	/**
	 * Returns the convolver.
	 * 
	 * @return the convolver
	 */
	public Convolver getConvolver()
	{
		return convolver;
	}
}
