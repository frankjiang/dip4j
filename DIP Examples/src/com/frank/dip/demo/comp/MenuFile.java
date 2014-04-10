/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights reserved.
 * FileMenu.java is PROPRIETARY/CONFIDENTIAL built in 2013.
 * Use is subject to license terms.
 */
package com.frank.dip.demo.comp;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;

import com.frank.dip.Image;
import com.frank.dip.demo.DIPFrame;
import com.frank.dip.demo.ImageDisplayDialog;

/**
 * The file menu loader.
 * <p>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class MenuFile extends MenuLoader
{
	/**
	 * Construct an instance of <tt>FileMenu</tt>.
	 * 
	 * @param dip
	 *            the digital image processing frame
	 */
	public MenuFile(DIPFrame dip)
	{
		super(dip, null);
	}

	/**
	 * @see com.frank.dip.demo.comp.MenuLoader#load(java.lang.Object[])
	 */
	@Override
	protected void load(Object... args)
	{
		JMenu mnFile = new JMenu("File(F)");
		mnFile.setMnemonic('F');
		dip.getBar().add(mnFile);
		int controlKey = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
		JMenuItem mntmOpen = new JMenuItem("Open");
		mntmOpen.setIcon(new ImageIcon(DIPFrame.class
				.getResource("/com/frank/dip/res/file - open.png")));////$NON-NLS-1$
		mntmOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
				controlKey));
		mntmOpen.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.open();
			}
		});
		mnFile.add(mntmOpen);
		JMenuItem mntmSave = new JMenuItem("Save");
		mntmSave.setIcon(new ImageIcon(DIPFrame.class
				.getResource("/com/frank/dip/res/file - save.png")));////$NON-NLS-1$
		mntmSave.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.save();
			}
		});
		mntmSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				controlKey));
		mnFile.add(mntmSave);
		JMenuItem mntmOpenInNew = new JMenuItem("Open in New");
		mntmOpenInNew.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Image image = dip.current();
				if (image == null)
					return;
				ImageDisplayDialog idd = new ImageDisplayDialog(dip,
						"Sub-Image: " + dip.getFilename(), false, image);
				idd.pack();
				idd.setVisible(true);
			}
		});
		mntmOpenInNew.setToolTipText("Open the current image in a new window");
		mnFile.add(mntmOpenInNew);
		JSeparator separator = new JSeparator();
		mnFile.add(separator);
		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dip.dispose();
			}
		});
		mnFile.add(mntmExit);
	}
}
