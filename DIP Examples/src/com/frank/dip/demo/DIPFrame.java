package com.frank.dip.demo;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import com.frank.dip.BinaryImage;
import com.frank.dip.ColorImage;
import com.frank.dip.GrayImage;
import com.frank.dip.Image;
import com.frank.dip.ImageUtils;
import com.frank.dip.analyze.FourierTransformation;
import com.frank.dip.analyze.FunctionDislpayDialog;
import com.frank.dip.analyze.Histogram;
import com.frank.dip.analyze.HistogramDialog;
import com.frank.dip.enhance.GrayScaleAverage;
import com.frank.dip.enhance.GrayScaleCoefficient;
import com.frank.dip.enhance.color.FakeColorTransform;
import com.frank.dip.enhance.convolver.ArithmeticMeanKernel;
import com.frank.dip.enhance.convolver.ConvolveDialog;
import com.frank.dip.enhance.convolver.Convolver;
import com.frank.dip.enhance.convolver.GeometricMeanKernel;
import com.frank.dip.enhance.convolver.HarmonicMeanKernel;
import com.frank.dip.enhance.convolver.Kernel;
import com.frank.dip.enhance.convolver.KirschKernel;
import com.frank.dip.enhance.convolver.LaplacianKernel;
import com.frank.dip.enhance.convolver.LoGKernel;
import com.frank.dip.enhance.convolver.PrewittKernel;
import com.frank.dip.enhance.convolver.RobertKernel;
import com.frank.dip.enhance.convolver.SobelKernel;
import com.frank.dip.enhance.entropy.FloatImage;
import com.frank.dip.enhance.frequency.AbstractFourierFilter;
import com.frank.dip.enhance.frequency.FourierDistanceFunctionFilter;
import com.frank.dip.enhance.frequency.FourierLaplacianFilter;
import com.frank.dip.enhance.frequency.HomomorphicFilter;
import com.frank.dip.enhance.time.ConvolveEnhance;
import com.frank.dip.enhance.time.HistogramNormalization;
import com.frank.dip.enhance.time.InversionTransformation;
import com.frank.dip.enhance.time.LogarithmicTransformation;
import com.frank.dip.enhance.time.PiecewiseLinearTransformation;
import com.frank.dip.enhance.time.PowerLawTransformation;
import com.frank.dip.geom.AffineTransform;
import com.frank.dip.geom.Geometry;
import com.frank.dip.geom.GeometryColor;
import com.frank.dip.math.ButternworthFunction;
import com.frank.dip.math.CoefGaussFunction;
import com.frank.dip.math.EnhanceFilterFunction;
import com.frank.dip.math.Function;
import com.frank.dip.math.GaussFunction;
import com.frank.dip.math.HomomorphicEnhanceFilterFunction;
import com.frank.dip.math.Quadratic;
import com.frank.dip.math.Radius;
import com.frank.dip.math.ScalableFunction;
import com.frank.dip.morph.Morph;
import com.frank.dip.morph.MorphBinary;
import com.frank.dip.threshold.FBClustering;
import com.frank.dip.threshold.FrankThresholding;
import com.frank.dip.threshold.Fuzzy;
import com.frank.dip.threshold.GlobalThresholding;
import com.frank.dip.threshold.Intermodes;
import com.frank.dip.threshold.IsoData;
import com.frank.dip.threshold.MaxEntropy;
import com.frank.dip.threshold.Mean;
import com.frank.dip.threshold.MinCrossEntropy;
import com.frank.dip.threshold.MinError;
import com.frank.dip.threshold.Minimum;
import com.frank.dip.threshold.Moments;
import com.frank.dip.threshold.Otsu;
import com.frank.dip.threshold.Percentile;
import com.frank.dip.threshold.RenyiEntropy;
import com.frank.dip.threshold.Shanbhag;
import com.frank.dip.threshold.ThresholdFinder;
import com.frank.dip.threshold.Thresholding;
import com.frank.dip.threshold.Triangle;
import com.frank.dip.threshold.Yen;
import com.frank.math.MathUtils;
import com.frank.swing.PerformanceManager;
import com.frank.swing.SwingUtils;
import com.frank.sys.SystemUtils;
import com.frank.sys.TestUtils;
import com.frank.sys.TestUtils.Timer;

/**
 * Digital image processing test frame.
 * <p>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class DIPFrame extends JFrame implements Observer
{
	/**
	 * The opened displaying windows.
	 */
	private java.util.Vector<java.awt.Window>	wins					= new java.util.Vector<java.awt.Window>();
	// /com/frank/dip/res/lena-gray.png
	/**
	 * The image of test image.
	 */
	public static final String					TEST_IMAGE_PATH			= "/com/frank/dip/res/lena-gray.png";		// "/com/frank/dip/res/weak/0001.jpg";		//$NON-NLS-1$;
	/**
	 * serialVersionUID.
	 */
	private static final long					serialVersionUID		= -8404661955977571199L;
	/**
	 * The time unit.
	 */
	private TimeUnit							timeunit				= TimeUnit.MILLISECONDS;
	/**
	 * The short string of {@linkplain #timeunit}.
	 */
	private String								timeunitStr				= "ms";									//$NON-NLS-1$
	/**
	 * Current image file name.
	 */
	private String								filename				= "No Image";
	/**
	 * The flag of whether the position label should be showing.
	 */
	private boolean								flagOfShowingPosition	= false;
	/**
	 * The flag of whether the pixels information should be showing in the
	 * status bar.
	 */
	private boolean								flagOfShowingPixels		= true;
	/**
	 * The panel which contains the frame content.
	 */
	private JPanel								contentPane;
	/**
	 * The menu bar of the frame.
	 */
	private JMenuBar							menuBar;
	/**
	 * The canvas label.
	 */
	private JLabel								canvas;
	/**
	 * The canvas scroll pane.
	 */
	private JScrollPane							canvasPane;
	/**
	 * The performance manager.
	 */
	private PerformanceManager<Image>			pm;
	/**
	 * The menu item of action re-do.
	 */
	private JMenuItem							mntmRedo;
	/**
	 * The menu item of action undo.
	 */
	private JMenuItem							mntmUndo;
	/**
	 * The menu item of action copy current image to system clip board.
	 */
	private JMenuItem							mntmCopy;
	/**
	 * The menu item of action paste image from system clip board.
	 */
	private JMenuItem							mntmPaste;
	/**
	 * Tool bar button of open image file.
	 */
	private JButton								btnOpen;
	/**
	 * Tool bar button of save image
	 */
	private JButton								btnSave;
	/**
	 * Tool bar button of experimental test.
	 */
	private JButton								btnTest;
	/**
	 * Tool bar button of whether display the pixel value of the pointed pixel.
	 */
	private JButton								btnShowPixels;
	/**
	 * The quick access tool bar.
	 */
	private JToolBar							toolBar;
	/**
	 * The label of application status.
	 */
	private JLabel								lblStatus;
	/**
	 * The label of mouse position in the image.
	 */
	private JLabel								lblPosition;
	/**
	 * The label of process comment.
	 */
	private JLabel								lblComment;
	/**
	 * The split panes in status panel.
	 */
	private JSplitPane							splitPane, splitPane2;
	private JMenu								mnFilter;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					DIPFrame frame = new DIPFrame();
					frame.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public DIPFrame()
	{
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				DIPFrame.class.getResource("/com/frank/dip/res/icon.png")));//$NON-NLS-1$
		// Set system frame style.
		SwingUtils.setSystemLookAndFeel(this);
		// Initialize the image processing history record.
		pm = new ObservablePerformanceManager<Image>();
		pm.addObserver(this);
		// Initialize frame contents.
		setTitle("Digital Image Processing");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 650);
		createMenu();
		createContent();
		createToolBar();
		createStatusPanel();
		updateCanvasTitle(null);
		setLocationRelativeTo(null);
	}

	/**
	 * Create the components in the content pane.
	 */
	private void createContent()
	{
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		canvasPane = new JScrollPane();
		contentPane.add(canvasPane, BorderLayout.CENTER);
		canvas = new JLabel("No Image");
		canvas.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseExited(MouseEvent e)
			{
				updateMouseMotion(null);
			}
		});
		canvas.addMouseMotionListener(new MouseMotionAdapter()
		{
			@Override
			public void mouseMoved(MouseEvent e)
			{
				if (flagOfShowingPosition)
				{
					Point p = e.getPoint();
					Dimension main = ((JLabel) e.getSource()).getSize();
					Image image = pm.current();
					int w0 = image.width();
					int h0 = image.height();
					int w = (w0 - main.width) / 2;
					int h = (h0 - main.height) / 2;
					p.translate(w < 0 ? w : 0, h < 0 ? h : 0);
					updateMouseMotion(p.x < 0 || p.y < 0 || p.x >= w0
							|| p.y >= h0 ? null : p);
				}
			}
		});
		canvas.setHorizontalAlignment(SwingConstants.CENTER);
		canvasPane.setViewportView(canvas);
	}

	/**
	 * Create the tool bar.
	 */
	private void createToolBar()
	{
		toolBar = new JToolBar();
		contentPane.add(toolBar, BorderLayout.NORTH);
		btnOpen = new JButton("");//$NON-NLS-1$
		btnOpen.setToolTipText("Open");
		btnOpen.setIcon(new ImageIcon(DIPFrame.class
				.getResource("/com/frank/dip/res/file - open.png")));//$NON-NLS-1$
		toolBar.add(btnOpen);
		btnSave = new JButton("");
		btnSave.setIcon(new ImageIcon(DIPFrame.class
				.getResource("/com/frank/dip/res/file - save.png")));//$NON-NLS-1$
		btnSave.setToolTipText("Save");
		toolBar.add(btnSave);
		btnShowPixels = new JButton("");
		btnShowPixels.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				flagOfShowingPixels = !flagOfShowingPixels;
				if (flagOfShowingPixels)
				{
					btnShowPixels.setIcon(new ImageIcon(DIPFrame.class
							.getResource("/com/frank/dip/res/ok.png")));//$NON-NLS-1$
					btnShowPixels
							.setToolTipText("Click to stop showing pixels information");
				}
				else
				{
					btnShowPixels.setIcon(new ImageIcon(DIPFrame.class
							.getResource("/com/frank/dip/res/cancel.png")));//$NON-NLS-1$
					btnShowPixels
							.setToolTipText("Click to show pixels information");
				}
			}
		});
		btnShowPixels.setIcon(new ImageIcon(DIPFrame.class
				.getResource("/com/frank/dip/res/ok.png")));//$NON-NLS-1$
		btnShowPixels
				.setToolTipText("Click to stop showing pixels information");
		toolBar.add(btnShowPixels);
		JButton button = new JButton("");
		button.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				openImage(DIPFrame.class.getResourceAsStream(TEST_IMAGE_PATH),
						TEST_IMAGE_PATH);
			}
		});
		button.setToolTipText("Load the default image.");
		button.setIcon(new ImageIcon(
				DIPFrame.class
						.getResource("/com/sun/java/swing/plaf/windows/icons/HardDrive.gif")));//$NON-NLS-1$
		toolBar.add(button);
		btnTest = new JButton("");
		btnTest.setToolTipText("Test Algorithms");
		btnTest.setIcon(new ImageIcon(DIPFrame.class
				.getResource("/javax/swing/plaf/basic/icons/JavaCup16.png")));//$NON-NLS-1$
		toolBar.add(btnTest);
		JButton btnCloseDialogs = new JButton("");
		btnCloseDialogs.setMnemonic('C');
		btnCloseDialogs.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				for (java.awt.Window win : wins)
					win.dispose();
				wins.clear();
			}
		});
		btnCloseDialogs.setIcon(new ImageIcon(DIPFrame.class
				.getResource("/com/frank/dip/res/cance-2.png")));
		btnCloseDialogs
				.setToolTipText("Alt+C: Close all the image displaying dialogs.");
		toolBar.add(btnCloseDialogs);
		btnTest.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				test();
			}
		});
		btnSave.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				save();
			}
		});
		btnOpen.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				open();
			}
		});
	}

	/**
	 * Create the button panel.
	 */
	private void createStatusPanel()
	{
		splitPane = new JSplitPane();
		splitPane.setContinuousLayout(true);
		contentPane.add(splitPane, BorderLayout.SOUTH);
		lblStatus = new JLabel(filename);
		splitPane.setLeftComponent(lblStatus);
		splitPane2 = new JSplitPane();
		splitPane.setRightComponent(splitPane2);
		lblPosition = new JLabel("");
		splitPane2.setLeftComponent(lblPosition);
		lblComment = new JLabel("");
		lblComment.setHorizontalAlignment(SwingConstants.RIGHT);
		splitPane2.setRightComponent(lblComment);
	}

	/**
	 * TODO Test method.
	 */
	private void test()
	{
		new Performance("Filter", "Local Entropy Image")
		{
			@Override
			protected Image perform(Image image)
			{
				FloatImage fi = new FloatImage(image,
						GrayImage.COLOR_SCALE_LEVEL);
				int w = 5;
				int h = 5;
				Timer t = TestUtils.getTimer();
				fi.entropy(w, h);
				fi.normalize(0, 255);
				Image res = fi.toGrayImage();
				time = t.getTime(timeunit);
				return res;
			}
		}.perform();
	}

	/**
	 * Returns the current image or {@code null} if not have one.
	 * <p>
	 * If the current image is {@code null}, an error message dialog will be
	 * displayed.
	 * </p>
	 * 
	 * @return the current image
	 */
	private Image current()
	{
		Image image = pm.current();
		if (image == null)
		{
			SwingUtils.errorMessage(this, "No image to process!");
			return null;
		}
		return image;
	}

	/**
	 * Update canvas title according to the specified title string.
	 * 
	 * @param image
	 *            the current image
	 */
	public void updateCanvasTitle(Image image)
	{
		String title = image == null ? "No Image" : String.format(
				"%s - %d\u00d7%d", filename, image.width(), image.height());
		lblStatus.setText(title);
		splitPane.resetToPreferredSizes();
		if (pm.isEmpty())
			setTitle("Digital Image Processing");
		else
			setTitle(String.format("Digital Image Processing - %s", title));
	}

	// /**
	// * Update canvas title according to the specified title string.
	// *
	// * @param title
	// * the title string
	// */
	// private void updateCanvasTitle(String title)
	// {
	// lblStatus.setText(title);
	// splitPane.resetToPreferredSizes();
	// if (pm.isEmpty())
	// setTitle("Digital Image Processing");
	// else
	// setTitle(String.format("Digital Image Processing - %s", title));
	// }
	/**
	 * Initialize the menu bar.
	 */
	private void createMenu()
	{
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		menuFile();
		menuEdit();
		menuAnalyse();
		menuEnhance();
		menuThreshold();
		menuFilter();
		menuMorph();
		menuGeometry();
	}

	/**
	 * Create filter menu.
	 */
	private void menuFilter()
	{
		mnFilter = new JMenu("Filter(I)");
		mnFilter.setMnemonic('I');
		menuBar.add(mnFilter);
		// initialize linear smooth filter menu
		menuFilterNoise(mnFilter);
		// initialize sharpen filter menu
		menuFilterSharpen(mnFilter);
		// initialize convolve enhance filter menu
		menuFilterConvolveEnhance(mnFilter);
		// initialize frequency filter menu
		menuFilterFrequency(mnFilter);
		// custom convolution
		JMenuItem mntmCustom = new JMenuItem("Custom");
		mntmCustom.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new Performance("Filter", "Custom Convolve")
				{
					@Override
					protected Image perform(Image image)
					{
						ConvolveDialog dialog = new ConvolveDialog(
								DIPFrame.this);
						dialog.setVisible(true);
						Convolver convolver = dialog.getConvolver();
						if (convolver == null)
							return null;
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = convolver.operate(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnFilter.add(mntmCustom);
	}

	/**
	 * Returns the morphology structure type selected by the user.
	 * 
	 * @return the morphology structure type, {@code null} if the user gives up
	 *         selection
	 */
	private Integer selectMorphologyStructure()
	{
		String[] types = new String[] { "Square", "Diamond", "Eight Corner" };
		Object obj = JOptionPane.showInputDialog(this,
				"Select the morphology structure:", "Morphology Structure",
				JOptionPane.INFORMATION_MESSAGE, null, types, types[0]);
		if (obj == null)
			return null;
		for (int i = 0; i < types.length; i++)
			if (obj.equals(types[i]))
				return i;
		return 0;
	}

	/**
	 * Returns the interpolation scheme selected by user.
	 * 
	 * @return the interpolation value, {@code null} if the user gives up
	 *         selection
	 */
	private Integer selectInterpolationType()
	{
		String[] types = new String[] { "Bilinear", "Nearest Neighbour",
				"Bicubic" };
		Object obj = JOptionPane.showInputDialog(this,
				"Select interploation scheme:", "Interpolation",
				JOptionPane.INFORMATION_MESSAGE, null, types, types[0]);
		if (obj == null)
			return null;
		Integer r = 0;
		for (int i = 0; i < types.length; i++)
			if (obj.equals(types[i]))
				r = i;
		switch (r)
		{
			case 1:
				return Geometry.TYPE_NEAREST_NEIGHBOR;
			case 3:
				return Geometry.TYPE_BICUBIC;
			default:
			case 2:
				return Geometry.TYPE_BILINEAR;
		}
	}

	/**
	 * Create menu morphology.
	 */
	private void menuMorph()
	{
		JMenu mnMorphm = new JMenu("Morph(M)");
		mnMorphm.setMnemonic('M');
		menuBar.add(mnMorphm);
		JMenuItem mntmMorphErode = new JMenuItem("Erode");
		mntmMorphErode.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new Performance("Morph", "Erode")
				{
					@Override
					protected Image perform(Image image)
					{
						Integer type = selectMorphologyStructure();
						if (type == null)
							return null;
						Morph morph = null;
						try
						{
							morph = Morph.getMorph(image, type);
						}
						catch (Exception e1)
						{
							SwingUtils.errorMessage(DIPFrame.this,
									e1.getLocalizedMessage());
							return null;
						}
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = morph.erode(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnMorphm.add(mntmMorphErode);
		JMenuItem mntmMorphDilate = new JMenuItem("Dilate");
		mntmMorphDilate.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new Performance("Morph", "Dilate")
				{
					@Override
					protected Image perform(Image image)
					{
						Integer type = selectMorphologyStructure();
						if (type == null)
							return null;
						Morph morph = null;
						try
						{
							morph = Morph.getMorph(image, type);
						}
						catch (Exception e1)
						{
							SwingUtils.errorMessage(DIPFrame.this,
									e1.getLocalizedMessage());
							return null;
						}
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = morph.dilate(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnMorphm.add(mntmMorphDilate);
		JMenuItem mntmMorphOpen = new JMenuItem("Open");
		mntmMorphOpen.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new Performance("Morph", "Open")
				{
					@Override
					protected Image perform(Image image)
					{
						Integer type = selectMorphologyStructure();
						if (type == null)
							return null;
						Morph morph = null;
						try
						{
							morph = Morph.getMorph(image, type);
						}
						catch (Exception e1)
						{
							SwingUtils.errorMessage(DIPFrame.this,
									e1.getLocalizedMessage());
							return null;
						}
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = morph.open(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnMorphm.add(mntmMorphOpen);
		JMenuItem mntmMorphClose = new JMenuItem("Close");
		mntmMorphClose.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new Performance("Morph", "Close")
				{
					@Override
					protected Image perform(Image image)
					{
						Integer type = selectMorphologyStructure();
						if (type == null)
							return null;
						Morph morph = null;
						try
						{
							morph = Morph.getMorph(image, type);
						}
						catch (Exception e1)
						{
							SwingUtils.errorMessage(DIPFrame.this,
									e1.getLocalizedMessage());
							return null;
						}
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = morph.close(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnMorphm.add(mntmMorphClose);
		JMenu mnMorphBinary = new JMenu("Binary Morph");
		mnMorphm.add(mnMorphBinary);
		JMenuItem mntmMorphSkeletonize = new JMenuItem("Skeletonize");
		mntmMorphSkeletonize.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new Performance("Morph", "Skeletonize")
				{
					@Override
					protected Image perform(Image image)
					{
						if (!(image instanceof BinaryImage))
						{
							SwingUtils.errorMessage(
									DIPFrame.this,
									String.format(
											"Skeletonize cannot support image type: %s",
											image.getClass().toString()));
							return null;
						}
						Integer type = selectMorphologyStructure();
						if (type == null)
							return null;
						String str = SwingUtils.inputDialog(DIPFrame.this,
								"Erode Loop Counter",
								"Input an integer for erode loop counter:");
						if (str == null)
							return null;
						Integer k = null;
						try
						{
							k = Integer.valueOf(str);
						}
						catch (NumberFormatException e1)
						{
							SwingUtils.errorMessage(DIPFrame.this,
									e1.getLocalizedMessage());
							return null;
						}
						MorphBinary morph = new MorphBinary(type);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = morph.skeletonize((BinaryImage) image, k);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnMorphBinary.add(mntmMorphSkeletonize);
		JMenuItem mntmMorphHit = new JMenuItem("Hit");
		mntmMorphHit.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new Performance("Morph", "Hit")
				{
					@Override
					protected Image perform(Image image)
					{
						if (!(image instanceof BinaryImage))
						{
							SwingUtils.errorMessage(
									DIPFrame.this,
									String.format(
											"Hit cannot support image type: %s",
											image.getClass().toString()));
							return null;
						}
						Integer type = selectMorphologyStructure();
						if (type == null)
							return null;
						MorphBinary morph = new MorphBinary(type);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = morph.hit((BinaryImage) image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnMorphBinary.add(mntmMorphHit);
		JMenuItem mntmMorphThin = new JMenuItem("Thin");
		mntmMorphThin.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new Performance("Morph", "Thin")
				{
					@Override
					protected Image perform(Image image)
					{
						if (!(image instanceof BinaryImage))
						{
							SwingUtils.errorMessage(
									DIPFrame.this,
									String.format(
											"Thin cannot support image type: %s",
											image.getClass().toString()));
							return null;
						}
						Integer type = selectMorphologyStructure();
						if (type == null)
							return null;
						MorphBinary morph = new MorphBinary(type);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = morph.thin((BinaryImage) image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnMorphBinary.add(mntmMorphThin);
		JMenuItem mntmMorphEdge = new JMenuItem("Edge");
		mntmMorphEdge.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new Performance("Morph", "Edge")
				{
					@Override
					protected Image perform(Image image)
					{
						if (!(image instanceof BinaryImage))
						{
							SwingUtils.errorMessage(
									DIPFrame.this,
									String.format(
											"Thin cannot support image type: %s",
											image.getClass().toString()));
							return null;
						}
						Integer type = selectMorphologyStructure();
						if (type == null)
							return null;
						MorphBinary morph = new MorphBinary(type);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = morph.thin((BinaryImage) image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnMorphBinary.add(mntmMorphEdge);
	}

	/**
	 * Initialize the noise filter menu.
	 * 
	 * @param mnFilter
	 *            the filter menu
	 */
	private void menuFilterNoise(JMenuItem mnFilter)
	{
		JMenu mnNoiseFilter = new JMenu("Noise Filter");
		mnFilter.add(mnNoiseFilter);
		JMenuItem mnArithmeticMean = new JMenu("Arithmetic Mean");
		mnNoiseFilter.add(mnArithmeticMean);
		JMenuItem mntmLinearSmooth9 = new JMenuItem("9");
		mntmLinearSmooth9.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new Performance("Filter", "Arithmetic Mean 9")
				{
					@Override
					protected Image perform(Image image)
					{
						Kernel kernel = new ArithmeticMeanKernel(
								ArithmeticMeanKernel.KERNEL_9);
						Convolver conv = new Convolver(kernel);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = conv.operate(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnArithmeticMean.add(mntmLinearSmooth9);
		JMenuItem mntmLinearSmooth16 = new JMenuItem("16");
		mntmLinearSmooth16.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new Performance("Filter", "Arithmetic Mean 16")
				{
					@Override
					protected Image perform(Image image)
					{
						Kernel kernel = new ArithmeticMeanKernel(
								ArithmeticMeanKernel.KERNEL_16);
						Convolver conv = new Convolver(kernel);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = conv.operate(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnArithmeticMean.add(mntmLinearSmooth16);
		JMenu mnGeometricMean = new JMenu("Geometric Mean");
		mnNoiseFilter.add(mnGeometricMean);
		JMenuItem mntmGeometricMean9 = new JMenuItem("9");
		mntmGeometricMean9.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new Performance("Filter", "Geometric Mean 9")
				{
					@Override
					protected Image perform(Image image)
					{
						Kernel kernel = new GeometricMeanKernel(
								GeometricMeanKernel.KERNEL_9);
						Convolver conv = new Convolver(kernel);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = conv.operate(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnGeometricMean.add(mntmGeometricMean9);
		JMenuItem mntmGeometricMean16 = new JMenuItem("16");
		mntmGeometricMean16.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new Performance("Filter", "Geometric Mean 16")
				{
					@Override
					protected Image perform(Image image)
					{
						Kernel kernel = new GeometricMeanKernel(
								GeometricMeanKernel.KERNEL_16);
						Convolver conv = new Convolver(kernel);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = conv.operate(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnGeometricMean.add(mntmGeometricMean16);
		JMenu mnHarmonicMean = new JMenu("Harmonic Mean");
		mnNoiseFilter.add(mnHarmonicMean);
		JMenuItem mntmHarmonicMean9 = new JMenuItem("9");
		mntmHarmonicMean9.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new Performance("Filter", "Harmonic Mean 9")
				{
					@Override
					protected Image perform(Image image)
					{
						Kernel kernel = new HarmonicMeanKernel(
								HarmonicMeanKernel.KERNEL_9);
						Convolver conv = new Convolver(kernel);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = conv.operate(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnHarmonicMean.add(mntmHarmonicMean9);
		JMenuItem mntmHarmonicMean16 = new JMenuItem("16");
		mntmHarmonicMean16.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new Performance("Filter", "Harmonic Mean 16")
				{
					@Override
					protected Image perform(Image image)
					{
						Kernel kernel = new HarmonicMeanKernel(
								HarmonicMeanKernel.KERNEL_16);
						Convolver conv = new Convolver(kernel);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = conv.operate(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnHarmonicMean.add(mntmHarmonicMean16);
	}

	/**
	 * Initialize frequency filter menu.
	 * 
	 * @param mnFilter
	 *            the filter menu
	 */
	private void menuFilterFrequency(JMenu mnFilter)
	{
		//
		// Fourier high boost filters
		//
		JMenu mnFrequencyFilter = new JMenu("Frequency Filter");
		mnFilter.add(mnFrequencyFilter);
		// Fourier filter: radius low pass
		JMenuItem mntmRadiusFourierLPF = new JMenuItem("Radius Fourier LPF");
		mntmRadiusFourierLPF.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new Performance("Filter", "Radius Fourier Low Pass Filter")
				{
					@Override
					protected Image perform(Image image)
					{
						Function function = new Radius(image.width() * 0.05,
								true);
						Properties properties = function.getProperties();
						properties.put(rs(Radius.PARAM_D, true),
								image.width() > image.height() ? image.width()
										: image.height());
						properties.put(rs(Radius.PARAM_D, false), 0);
						FunctionDislpayDialog dialog = new FunctionDislpayDialog(
								null, "Radius Low Pass",
								new String[] { Radius.PARAM_D }, properties,
								function);
						dialog.setXAxisString("D(u,v)");
						dialog.setYAxisString("H(u,v)");
						dialog.setVisible(true);
						Properties p = dialog.getProperties();
						if (p == null)
							return null;
						function.setProperties(p);
						AbstractFourierFilter filter = new FourierDistanceFunctionFilter(
								function);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = filter.filter(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnFrequencyFilter.add(mntmRadiusFourierLPF);
		// Fourier filter: radius high pass
		JMenuItem mntmRadiusFourierHPF = new JMenuItem("Radius Fourier HPF");
		mntmRadiusFourierHPF.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new Performance("Filter", "Radius Fourier High Pass Filter")
				{
					@Override
					protected Image perform(Image image)
					{
						Function function = new Radius(image.width() * 0.05,
								false);
						Properties properties = function.getProperties();
						properties.put(rs(Radius.PARAM_D, true),
								image.width() > image.height() ? image.width()
										: image.height());
						properties.put(rs(Radius.PARAM_D, false), 0);
						FunctionDislpayDialog dialog = new FunctionDislpayDialog(
								null, "Radius High Pass",
								new String[] { Radius.PARAM_D }, properties,
								function);
						dialog.setXAxisString("D(u,v)");
						dialog.setYAxisString("H(u,v)");
						dialog.setVisible(true);
						Properties p = dialog.getProperties();
						if (p == null)
							return null;
						function.setProperties(p);
						AbstractFourierFilter filter = new FourierDistanceFunctionFilter(
								function);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = filter.filter(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnFrequencyFilter.add(mntmRadiusFourierHPF);
		// Fourier filter: Butternworth low pass
		JMenuItem mntmButternworthLPF = new JMenuItem("Butternworth LPF");
		mntmButternworthLPF.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new Performance("Filter", "Butternworth Low Pass Filter")
				{
					@Override
					protected Image perform(Image image)
					{
						ScalableFunction function = new ButternworthFunction(
								10, 1, 255, true);
						Properties properties = function.getProperties();
						properties.put(rs(ButternworthFunction.PARAM_D, true),
								1000);
						properties.put(rs(ButternworthFunction.PARAM_D, false),
								Double.MIN_VALUE);
						properties.put(rs(ButternworthFunction.PARAM_N, true),
								10);
						properties.put(rs(ButternworthFunction.PARAM_N, false),
								0);
						FunctionDislpayDialog dialog = new FunctionDislpayDialog(
								null, "Butternworth Low Pass", new String[] {
										ButternworthFunction.PARAM_D,
										ButternworthFunction.PARAM_N },
								properties, function);
						dialog.setXAxisString("D(u,v)");
						dialog.setYAxisString("H(u,v)");
						dialog.setVisible(true);
						Properties p = dialog.getProperties();
						if (p == null)
							return null;
						function.setProperties(p);
						AbstractFourierFilter filter = new FourierDistanceFunctionFilter(
								function);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = filter.filter(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnFrequencyFilter.add(mntmButternworthLPF);
		// Fourier filter: Butternworth high pass
		JMenuItem mntmButternworthHPF = new JMenuItem("Butternworth HPF");
		mntmButternworthHPF.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new Performance("Filter", "Butternworth High Pass Filter")
				{
					@Override
					protected Image perform(Image image)
					{
						ScalableFunction function = new ButternworthFunction(
								10, 1, 255, false);
						Properties properties = function.getProperties();
						properties.put(rs(ButternworthFunction.PARAM_D, true),
								1000);
						properties.put(rs(ButternworthFunction.PARAM_D, false),
								Double.MIN_VALUE);
						properties.put(rs(ButternworthFunction.PARAM_N, true),
								10);
						properties.put(rs(ButternworthFunction.PARAM_N, false),
								0);
						FunctionDislpayDialog dialog = new FunctionDislpayDialog(
								null, "Butternworth High Pass", new String[] {
										ButternworthFunction.PARAM_D,
										ButternworthFunction.PARAM_N },
								properties, function);
						dialog.setXAxisString("D(u,v)");
						dialog.setYAxisString("H(u,v)");
						dialog.setVisible(true);
						Properties p = dialog.getProperties();
						if (p == null)
							return null;
						function.setProperties(p);
						AbstractFourierFilter filter = new FourierDistanceFunctionFilter(
								function);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = filter.filter(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnFrequencyFilter.add(mntmButternworthHPF);
		// Fourier filter: Gauss low pass
		JMenuItem mntmGaussLPF = new JMenuItem("Gauss LPF");
		mntmGaussLPF.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new Performance("Filter", "Gauss Low Pass Filter")
				{
					@Override
					protected Image perform(Image image)
					{
						ScalableFunction function = new GaussFunction(1.0, 255,
								true);
						Properties properties = function.getProperties();
						properties.put(rs(GaussFunction.PARAM_SIGMA, true),
								1000);
						properties.put(rs(GaussFunction.PARAM_SIGMA, false),
								Double.MIN_VALUE);
						FunctionDislpayDialog dialog = new FunctionDislpayDialog(
								null, "Gauss Low Pass",
								new String[] { GaussFunction.PARAM_SIGMA },
								properties, function);
						dialog.setXAxisString("D(u,v)");
						dialog.setYAxisString("H(u,v)");
						dialog.setVisible(true);
						Properties p = dialog.getProperties();
						if (p == null)
							return null;
						function.setProperties(p);
						AbstractFourierFilter filter = new FourierDistanceFunctionFilter(
								function);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = filter.filter(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnFrequencyFilter.add(mntmGaussLPF);
		// Fourier filter: Gauss high pass
		JMenuItem mntmGaussHPF = new JMenuItem("Gauss HPF");
		mntmGaussHPF.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new Performance("Filter", "Gauss High Pass Filter")
				{
					@Override
					protected Image perform(Image image)
					{
						ScalableFunction function = new GaussFunction(1.0, 255,
								false);
						Properties properties = function.getProperties();
						properties.put(rs(GaussFunction.PARAM_SIGMA, true),
								1000);
						properties.put(rs(GaussFunction.PARAM_SIGMA, false),
								Double.MIN_VALUE);
						FunctionDislpayDialog dialog = new FunctionDislpayDialog(
								null, "Gauss High Pass",
								new String[] { GaussFunction.PARAM_SIGMA },
								properties, function);
						dialog.setXAxisString("D(u,v)");
						dialog.setYAxisString("H(u,v)");
						dialog.setVisible(true);
						Properties p = dialog.getProperties();
						if (p == null)
							return null;
						function.setProperties(p);
						AbstractFourierFilter filter = new FourierDistanceFunctionFilter(
								function);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = filter.filter(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnFrequencyFilter.add(mntmGaussHPF);
		// Fourier filter: Laplacian sharpen
		JMenuItem mntmFourierLaplacian = new JMenuItem("Fourier Laplacian");
		mntmFourierLaplacian.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new Performance("Filter", "Fourier Laplacian Filter")
				{
					@Override
					protected Image perform(Image image)
					{
						AbstractFourierFilter filter = new FourierLaplacianFilter();
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = filter.filter(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnFrequencyFilter.add(mntmFourierLaplacian);
		//
		// Fourier high boost filters
		//
		JMenu mnHighBoostFilter = new JMenu("High Boost Filter");
		mnHighBoostFilter.setToolTipText("High Frequency Boost Filter");
		mnFrequencyFilter.add(mnHighBoostFilter);
		// High boost filter: radius
		JMenuItem mntmHighBoostFilterRadius = new JMenuItem("Radius");
		mntmHighBoostFilterRadius.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new Performance("Filter", "Radius High Boost Filter")
				{
					@Override
					protected Image perform(Image image)
					{
						Function func2 = new Radius(image.width() * 0.05, false);
						EnhanceFilterFunction function = new EnhanceFilterFunction(
								func2, 1.0, 1.0);
						Properties properties = function.getProperties();
						properties.put(rs(Radius.PARAM_D, true),
								image.width() > image.height() ? image.width()
										: image.height());
						properties.put(rs(Radius.PARAM_D, false), 0);
						properties.put(rs(EnhanceFilterFunction.PARAM_A, true),
								3);
						properties.put(
								rs(EnhanceFilterFunction.PARAM_A, false), 0);
						properties.put(rs(EnhanceFilterFunction.PARAM_B, true),
								10);
						properties.put(
								rs(EnhanceFilterFunction.PARAM_B, false),
								Double.MIN_VALUE);
						FunctionDislpayDialog dialog = new FunctionDislpayDialog(
								null, "Radius High Pass", new String[] {
										Radius.PARAM_D,
										EnhanceFilterFunction.PARAM_A,
										EnhanceFilterFunction.PARAM_B },
								properties, function);
						dialog.setXAxisString("D(u,v)");
						dialog.setYAxisString("H(u,v)");
						dialog.setVisible(true);
						Properties p = dialog.getProperties();
						if (p == null)
							return null;
						function.setProperties(p);
						AbstractFourierFilter filter = new FourierDistanceFunctionFilter(
								function);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = filter.filter(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnHighBoostFilter.add(mntmHighBoostFilterRadius);
		// High boost filter: Butternworth
		JMenuItem mntmHighBoostFilterButternworth = new JMenuItem(
				"Butternworth");
		mntmHighBoostFilterButternworth.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new Performance("Filter", "Butternworth High Boost Filter")
				{
					@Override
					protected Image perform(Image image)
					{
						ScalableFunction func2 = new ButternworthFunction(10,
								1, 255, false);
						EnhanceFilterFunction function = new EnhanceFilterFunction(
								func2, 1.0, 1.0);
						Properties properties = function.getProperties();
						properties.put(rs(ButternworthFunction.PARAM_D, true),
								1000);
						properties.put(rs(ButternworthFunction.PARAM_D, false),
								Double.MIN_VALUE);
						properties.put(rs(ButternworthFunction.PARAM_N, true),
								10);
						properties.put(rs(ButternworthFunction.PARAM_N, false),
								0);
						properties.put(rs(EnhanceFilterFunction.PARAM_A, true),
								3);
						properties.put(
								rs(EnhanceFilterFunction.PARAM_A, false), 0);
						properties.put(rs(EnhanceFilterFunction.PARAM_B, true),
								10);
						properties.put(
								rs(EnhanceFilterFunction.PARAM_B, false),
								Double.MIN_VALUE);
						FunctionDislpayDialog dialog = new FunctionDislpayDialog(
								null, "Butternworth High Pass", new String[] {
										ButternworthFunction.PARAM_D,
										ButternworthFunction.PARAM_N,
										EnhanceFilterFunction.PARAM_A,
										EnhanceFilterFunction.PARAM_B },
								properties, function);
						dialog.setXAxisString("D(u,v)");
						dialog.setYAxisString("H(u,v)");
						dialog.setVisible(true);
						Properties p = dialog.getProperties();
						if (p == null)
							return null;
						function.setProperties(p);
						AbstractFourierFilter filter = new FourierDistanceFunctionFilter(
								function);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = filter.filter(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnHighBoostFilter.add(mntmHighBoostFilterButternworth);
		// High boost filter: Gauss
		JMenuItem mntmHighBoostFilterGauss = new JMenuItem("Gauss");
		mntmHighBoostFilterGauss.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new Performance("Filter", "Gauss High Boost Filter")
				{
					@Override
					protected Image perform(Image image)
					{
						ScalableFunction func2 = new GaussFunction(1.0, 255,
								false);
						EnhanceFilterFunction function = new EnhanceFilterFunction(
								func2, 1.0, 1.0);
						Properties properties = function.getProperties();
						properties.put(rs(GaussFunction.PARAM_SIGMA, true),
								1000);
						properties.put(rs(GaussFunction.PARAM_SIGMA, false),
								Double.MIN_VALUE);
						properties.put(rs(EnhanceFilterFunction.PARAM_A, true),
								3);
						properties.put(
								rs(EnhanceFilterFunction.PARAM_A, false), 0);
						properties.put(rs(EnhanceFilterFunction.PARAM_B, true),
								10);
						properties.put(
								rs(EnhanceFilterFunction.PARAM_B, false),
								Double.MIN_VALUE);
						FunctionDislpayDialog dialog = new FunctionDislpayDialog(
								null, "Gauss High Pass", new String[] {
										GaussFunction.PARAM_SIGMA,
										EnhanceFilterFunction.PARAM_A,
										EnhanceFilterFunction.PARAM_B },
								properties, function);
						dialog.setXAxisString("D(u,v)");
						dialog.setYAxisString("H(u,v)");
						dialog.setVisible(true);
						Properties p = dialog.getProperties();
						if (p == null)
							return null;
						function.setProperties(p);
						AbstractFourierFilter filter = new FourierDistanceFunctionFilter(
								function);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = filter.filter(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnHighBoostFilter.add(mntmHighBoostFilterGauss);
		//
		// Homomorphic filters.
		//
		JMenu mnHomomorphic = new JMenu("Homomorphic");
		mnHomomorphic.setToolTipText("Homomorphic Filter");
		mnFrequencyFilter.add(mnHomomorphic);
		// Homomorphic filter: radius low pass
		JMenuItem mntmHomomorphicRadiusLP = new JMenuItem("Radius LP");
		mntmHomomorphicRadiusLP.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new Performance("Filter", "Radius Low Pass Homomorphic Filter")
				{
					@Override
					protected Image perform(Image image)
					{
						Function func2 = new Radius(image.width() * 0.05, true);
						HomomorphicEnhanceFilterFunction function = new HomomorphicEnhanceFilterFunction(
								func2, 1.0, 1.0);
						Properties properties = function.getProperties();
						properties.put(rs(Radius.PARAM_D, true),
								image.width() > image.height() ? image.width()
										: image.height());
						properties.put(rs(Radius.PARAM_D, false), 0);
						properties.put(
								rs(HomomorphicEnhanceFilterFunction.PARAM_H,
										true), 3);
						properties.put(
								rs(HomomorphicEnhanceFilterFunction.PARAM_H,
										false), 0);
						properties.put(
								rs(HomomorphicEnhanceFilterFunction.PARAM_L,
										true), 10);
						properties.put(
								rs(HomomorphicEnhanceFilterFunction.PARAM_L,
										false), Double.MIN_VALUE);
						FunctionDislpayDialog dialog = new FunctionDislpayDialog(
								null,
								"Radius Low Pass",
								new String[] {
										Radius.PARAM_D,
										HomomorphicEnhanceFilterFunction.PARAM_H,
										HomomorphicEnhanceFilterFunction.PARAM_L },
								properties, function);
						dialog.setXAxisString("D(u,v)");
						dialog.setYAxisString("H(u,v)");
						dialog.setMaxXValue(1.0);
						dialog.setMaxYValue(2.0);
						dialog.setVisible(true);
						Properties p = dialog.getProperties();
						if (p == null)
							return null;
						function.setProperties(p);
						AbstractFourierFilter filter = new HomomorphicFilter(
								function);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = filter.filter(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnHomomorphic.add(mntmHomomorphicRadiusLP);
		// Homomorphic filter: radius high pass
		JMenuItem mntmHomomorphicRadiusHP = new JMenuItem("Radius HP");
		mntmHomomorphicRadiusHP.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new Performance("Filter", "Radius High Pass Homomorphic Filter")
				{
					@Override
					protected Image perform(Image image)
					{
						Function func2 = new Radius(image.width() * 0.05, false);
						HomomorphicEnhanceFilterFunction function = new HomomorphicEnhanceFilterFunction(
								func2, 1.0, 1.0);
						Properties properties = function.getProperties();
						properties.put(rs(Radius.PARAM_D, true),
								image.width() > image.height() ? image.width()
										: image.height());
						properties.put(rs(Radius.PARAM_D, false), 0);
						properties.put(
								rs(HomomorphicEnhanceFilterFunction.PARAM_H,
										true), 3);
						properties.put(
								rs(HomomorphicEnhanceFilterFunction.PARAM_H,
										false), 0);
						properties.put(
								rs(HomomorphicEnhanceFilterFunction.PARAM_L,
										true), 10);
						properties.put(
								rs(HomomorphicEnhanceFilterFunction.PARAM_L,
										false), Double.MIN_VALUE);
						FunctionDislpayDialog dialog = new FunctionDislpayDialog(
								null,
								"Radius High Pass",
								new String[] {
										Radius.PARAM_D,
										HomomorphicEnhanceFilterFunction.PARAM_H,
										HomomorphicEnhanceFilterFunction.PARAM_L },
								properties, function);
						dialog.setXAxisString("D(u,v)");
						dialog.setYAxisString("H(u,v)");
						dialog.setMaxXValue(1.0);
						dialog.setMaxYValue(2.0);
						dialog.setVisible(true);
						Properties p = dialog.getProperties();
						if (p == null)
							return null;
						function.setProperties(p);
						AbstractFourierFilter filter = new HomomorphicFilter(
								function);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = filter.filter(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnHomomorphic.add(mntmHomomorphicRadiusHP);
		// Homomorphic filter: Butternworth low pass
		JMenuItem mntmHomomorphicButternworthLP = new JMenuItem(
				"Butternworth LP");
		mntmHomomorphicButternworthLP.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new Performance("Filter",
						"Butternworth Low Pass Homomorphic Filter")
				{
					@Override
					protected Image perform(Image image)
					{
						ScalableFunction func2 = new ButternworthFunction(10,
								1, 255, true);
						HomomorphicEnhanceFilterFunction function = new HomomorphicEnhanceFilterFunction(
								func2, 1.0, 1.0);
						Properties properties = function.getProperties();
						properties.put(rs(ButternworthFunction.PARAM_D, true),
								1000);
						properties.put(rs(ButternworthFunction.PARAM_D, false),
								Double.MIN_VALUE);
						properties.put(rs(ButternworthFunction.PARAM_N, true),
								10);
						properties.put(rs(ButternworthFunction.PARAM_N, false),
								0);
						properties.put(
								rs(HomomorphicEnhanceFilterFunction.PARAM_H,
										true), 3);
						properties.put(
								rs(HomomorphicEnhanceFilterFunction.PARAM_H,
										false), 0);
						properties.put(
								rs(HomomorphicEnhanceFilterFunction.PARAM_L,
										true), 10);
						properties.put(
								rs(HomomorphicEnhanceFilterFunction.PARAM_L,
										false), Double.MIN_VALUE);
						FunctionDislpayDialog dialog = new FunctionDislpayDialog(
								null,
								"Butternworth Low Pass",
								new String[] {
										ButternworthFunction.PARAM_D,
										ButternworthFunction.PARAM_N,
										HomomorphicEnhanceFilterFunction.PARAM_H,
										HomomorphicEnhanceFilterFunction.PARAM_L },
								properties, function);
						dialog.setXAxisString("D(u,v)");
						dialog.setYAxisString("H(u,v)");
						dialog.setMaxXValue(1.0);
						dialog.setMaxYValue(2.0);
						dialog.setVisible(true);
						Properties p = dialog.getProperties();
						if (p == null)
							return null;
						function.setProperties(p);
						AbstractFourierFilter filter = new HomomorphicFilter(
								function);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = filter.filter(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnHomomorphic.add(mntmHomomorphicButternworthLP);
		// Homomorphic filter: Butternworth high pass
		JMenuItem mntmHomomorphicButternworthHP = new JMenuItem(
				"Butternworth HP");
		mntmHomomorphicButternworthHP.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new Performance("Filter",
						"Butternworth High Pass Homomorphic Filter")
				{
					@Override
					protected Image perform(Image image)
					{
						ScalableFunction func2 = new ButternworthFunction(10,
								1, 255, false);
						HomomorphicEnhanceFilterFunction function = new HomomorphicEnhanceFilterFunction(
								func2, 1.0, 1.0);
						Properties properties = function.getProperties();
						properties.put(rs(ButternworthFunction.PARAM_D, true),
								1000);
						properties.put(rs(ButternworthFunction.PARAM_D, false),
								Double.MIN_VALUE);
						properties.put(rs(ButternworthFunction.PARAM_N, true),
								10);
						properties.put(rs(ButternworthFunction.PARAM_N, false),
								0);
						properties.put(
								rs(HomomorphicEnhanceFilterFunction.PARAM_H,
										true), 3);
						properties.put(
								rs(HomomorphicEnhanceFilterFunction.PARAM_H,
										false), 0);
						properties.put(
								rs(HomomorphicEnhanceFilterFunction.PARAM_L,
										true), 10);
						properties.put(
								rs(HomomorphicEnhanceFilterFunction.PARAM_L,
										false), Double.MIN_VALUE);
						FunctionDislpayDialog dialog = new FunctionDislpayDialog(
								null,
								"Butternworth High Pass",
								new String[] {
										ButternworthFunction.PARAM_D,
										ButternworthFunction.PARAM_N,
										HomomorphicEnhanceFilterFunction.PARAM_H,
										HomomorphicEnhanceFilterFunction.PARAM_L },
								properties, function);
						dialog.setXAxisString("D(u,v)");
						dialog.setYAxisString("H(u,v)");
						dialog.setMaxXValue(1.0);
						dialog.setMaxYValue(2.0);
						dialog.setVisible(true);
						Properties p = dialog.getProperties();
						if (p == null)
							return null;
						function.setProperties(p);
						AbstractFourierFilter filter = new HomomorphicFilter(
								function);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = filter.filter(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnHomomorphic.add(mntmHomomorphicButternworthHP);
		// Homomorphic filter: Gauss low pass
		JMenuItem mntmHomomorphicGaussLP = new JMenuItem("Gauss LP");
		mntmHomomorphicGaussLP.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new Performance("Filter", "Gauss Low Pass Homomorphic Filter")
				{
					@Override
					protected Image perform(Image image)
					{
						ScalableFunction func2 = new CoefGaussFunction(1.0,
								0.5, 255, true);
						HomomorphicEnhanceFilterFunction function = new HomomorphicEnhanceFilterFunction(
								func2, 1.0, 1.0);
						Properties properties = function.getProperties();
						properties.put(rs(CoefGaussFunction.PARAM_SIGMA, true),
								1000);
						properties.put(
								rs(CoefGaussFunction.PARAM_SIGMA, false),
								Double.MIN_VALUE);
						properties.put(rs(CoefGaussFunction.PARAM_C, true),
								1000);
						properties.put(rs(CoefGaussFunction.PARAM_C, false),
								Double.MIN_VALUE);
						properties.put(
								rs(HomomorphicEnhanceFilterFunction.PARAM_H,
										true), 3);
						properties.put(
								rs(HomomorphicEnhanceFilterFunction.PARAM_H,
										false), 0);
						properties.put(
								rs(HomomorphicEnhanceFilterFunction.PARAM_L,
										true), 10);
						properties.put(
								rs(HomomorphicEnhanceFilterFunction.PARAM_L,
										false), Double.MIN_VALUE);
						FunctionDislpayDialog dialog = new FunctionDislpayDialog(
								null,
								"Gauss Low Pass",
								new String[] {
										CoefGaussFunction.PARAM_SIGMA,
										CoefGaussFunction.PARAM_C,
										HomomorphicEnhanceFilterFunction.PARAM_H,
										HomomorphicEnhanceFilterFunction.PARAM_L },
								properties, function);
						dialog.setXAxisString("D(u,v)");
						dialog.setYAxisString("H(u,v)");
						dialog.setMaxXValue(1.0);
						dialog.setMaxYValue(2.0);
						dialog.setVisible(true);
						Properties p = dialog.getProperties();
						if (p == null)
							return null;
						function.setProperties(p);
						AbstractFourierFilter filter = new HomomorphicFilter(
								function);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = filter.filter(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnHomomorphic.add(mntmHomomorphicGaussLP);
		// Homomorphic filter: Gauss high pass
		JMenuItem mntmHomomorphicGaussHP = new JMenuItem("Gauss HP");
		mntmHomomorphicGaussHP.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new Performance("Filter", "Gauss High Pass Homomorphic Filter")
				{
					@Override
					protected Image perform(Image image)
					{
						ScalableFunction func2 = new CoefGaussFunction(1.0,
								0.5, 255, false);
						HomomorphicEnhanceFilterFunction function = new HomomorphicEnhanceFilterFunction(
								func2, 1.0, 1.0);
						Properties properties = function.getProperties();
						properties.put(rs(CoefGaussFunction.PARAM_SIGMA, true),
								1000);
						properties.put(
								rs(CoefGaussFunction.PARAM_SIGMA, false),
								Double.MIN_VALUE);
						properties.put(rs(CoefGaussFunction.PARAM_C, true),
								1000);
						properties.put(rs(CoefGaussFunction.PARAM_C, false),
								Double.MIN_VALUE);
						properties.put(
								rs(HomomorphicEnhanceFilterFunction.PARAM_H,
										true), 3);
						properties.put(
								rs(HomomorphicEnhanceFilterFunction.PARAM_H,
										false), 0);
						properties.put(
								rs(HomomorphicEnhanceFilterFunction.PARAM_L,
										true), 10);
						properties.put(
								rs(HomomorphicEnhanceFilterFunction.PARAM_L,
										false), Double.MIN_VALUE);
						FunctionDislpayDialog dialog = new FunctionDislpayDialog(
								null,
								"Gauss High Pass",
								new String[] {
										CoefGaussFunction.PARAM_SIGMA,
										CoefGaussFunction.PARAM_C,
										HomomorphicEnhanceFilterFunction.PARAM_H,
										HomomorphicEnhanceFilterFunction.PARAM_L },
								properties, function);
						dialog.setXAxisString("D(u,v)");
						dialog.setYAxisString("H(u,v)");
						dialog.setMaxXValue(1.0);
						dialog.setMaxYValue(2.0);
						dialog.setVisible(true);
						Properties p = dialog.getProperties();
						if (p == null)
							return null;
						function.setProperties(p);
						AbstractFourierFilter filter = new HomomorphicFilter(
								function);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = filter.filter(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnHomomorphic.add(mntmHomomorphicGaussHP);
	}

	/**
	 * Initialize convolve enhance filter menu.
	 * 
	 * @param mnFilter
	 *            the filter menu
	 */
	private void menuFilterConvolveEnhance(JMenu mnFilter)
	{
		JMenu mnConvolveEnhance = new JMenu("Convolve Enhance");
		mnFilter.add(mnConvolveEnhance);
		JMenuItem mntmConvolveEnhanceLaplacian4 = new JMenuItem("Laplacian 4");
		mntmConvolveEnhanceLaplacian4.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new Performance("Convolve Enhance", "Laplacian 4 Enhance")
				{
					@Override
					protected Image perform(Image image)
					{
						Kernel kernel = new LaplacianKernel(
								LaplacianKernel.KERNEL_4);
						ConvolveEnhance convolveEnhance = new ConvolveEnhance(
								kernel,
								ConvolveEnhance.HINT_ACCURACY_INTERRUPT,
								ConvolveEnhance.HINT_EDGE_SOURCE);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = convolveEnhance.operate(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnConvolveEnhance.add(mntmConvolveEnhanceLaplacian4);
		JMenuItem mntmConvolveEnhanceLaplacian8 = new JMenuItem("Laplacian 8");
		mntmConvolveEnhanceLaplacian8.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new Performance("Convolve Enhance", "Laplacian 8 Enhance")
				{
					@Override
					protected Image perform(Image image)
					{
						Kernel kernel = new LaplacianKernel(
								LaplacianKernel.KERNEL_8);
						ConvolveEnhance convolveEnhance = new ConvolveEnhance(
								kernel,
								ConvolveEnhance.HINT_ACCURACY_INTERRUPT,
								ConvolveEnhance.HINT_EDGE_SOURCE);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = convolveEnhance.operate(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnConvolveEnhance.add(mntmConvolveEnhanceLaplacian8);
		JMenuItem mntmConvolveEnhanceLog = new JMenuItem("LoG");
		mntmConvolveEnhanceLog.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new Performance("Convolve Enhance", "LoG Enhance")
				{
					@Override
					protected Image perform(Image image)
					{
						Kernel kernel = new LaplacianKernel(
								LaplacianKernel.KERNEL_8);
						ConvolveEnhance convolveEnhance = new ConvolveEnhance(
								kernel,
								ConvolveEnhance.HINT_ACCURACY_INTERRUPT,
								ConvolveEnhance.HINT_EDGE_SOURCE);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = convolveEnhance.operate(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnConvolveEnhance.add(mntmConvolveEnhanceLog);
		JMenuItem mntmConvolveEnhanceRobert = new JMenuItem("Robert");
		mntmConvolveEnhanceRobert.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new Performance("Convolve Enhance", "Robert Enhance")
				{
					@Override
					protected Image perform(Image image)
					{
						Kernel kernel = new RobertKernel();
						ConvolveEnhance convolveEnhance = new ConvolveEnhance(
								kernel,
								ConvolveEnhance.HINT_ACCURACY_INTERRUPT,
								ConvolveEnhance.HINT_EDGE_SOURCE);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = convolveEnhance.operate(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnConvolveEnhance.add(mntmConvolveEnhanceRobert);
		JMenuItem mntmConvolveEnhanceSobel = new JMenuItem("Sobel");
		mntmConvolveEnhanceSobel.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new Performance("Convolve Enhance", "Sobel Enhance")
				{
					@Override
					protected Image perform(Image image)
					{
						Kernel kernel = new SobelKernel();
						ConvolveEnhance convolveEnhance = new ConvolveEnhance(
								kernel,
								ConvolveEnhance.HINT_ACCURACY_INTERRUPT,
								ConvolveEnhance.HINT_EDGE_SOURCE);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = convolveEnhance.operate(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnConvolveEnhance.add(mntmConvolveEnhanceSobel);
		JMenuItem mntmConvolveEnhancePrewitt = new JMenuItem("Prewitt");
		mntmConvolveEnhancePrewitt.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new Performance("Convolve Enhance", "Prewitt Enhance")
				{
					@Override
					protected Image perform(Image image)
					{
						Kernel kernel = new PrewittKernel();
						ConvolveEnhance convolveEnhance = new ConvolveEnhance(
								kernel,
								ConvolveEnhance.HINT_ACCURACY_INTERRUPT,
								ConvolveEnhance.HINT_EDGE_SOURCE);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = convolveEnhance.operate(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnConvolveEnhance.add(mntmConvolveEnhancePrewitt);
		JMenuItem mntmConvolveEnhanceKirsch = new JMenuItem("Kirsch");
		mntmConvolveEnhanceKirsch.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new Performance("Convolve Enhance", "Kirsch Enhance")
				{
					@Override
					protected Image perform(Image image)
					{
						Kernel kernel = new KirschKernel();
						ConvolveEnhance convolveEnhance = new ConvolveEnhance(
								kernel,
								ConvolveEnhance.HINT_ACCURACY_INTERRUPT,
								ConvolveEnhance.HINT_EDGE_SOURCE);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = convolveEnhance.operate(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnConvolveEnhance.add(mntmConvolveEnhanceKirsch);
	}

	/**
	 * Initialize the sharpen filter menu.
	 * 
	 * @param mnFilter
	 *            the filter menu
	 */
	private void menuFilterSharpen(JMenu mnFilter)
	{
		JMenu mnSharpen = new JMenu("Sharpen");
		mnFilter.add(mnSharpen);
		JMenuItem mntmSharpenLaplacian4 = new JMenuItem("Laplacian 4");
		mntmSharpenLaplacian4.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new Performance("Filter", "Laplace Sharpen 4")
				{
					@Override
					protected Image perform(Image image)
					{
						Kernel kernel = new LaplacianKernel(
								LaplacianKernel.KERNEL_4);
						Convolver conv = new Convolver(kernel);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = conv.operate(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnSharpen.add(mntmSharpenLaplacian4);
		JMenuItem mntmSharpenLaplacian8 = new JMenuItem("Laplacian 8");
		mntmSharpenLaplacian8.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new Performance("Filter", "Laplace Sharpen 8")
				{
					@Override
					protected Image perform(Image image)
					{
						Kernel kernel = new LaplacianKernel(
								LaplacianKernel.KERNEL_8);
						Convolver conv = new Convolver(kernel);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = conv.operate(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnSharpen.add(mntmSharpenLaplacian8);
		JMenuItem mntmSharpenLoG = new JMenuItem("LoG");
		mntmSharpenLoG.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new Performance("Filter", "LoG Sharpen")
				{
					@Override
					protected Image perform(Image image)
					{
						Kernel kernel = new LoGKernel();
						Convolver conv = new Convolver(kernel);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = conv.operate(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnSharpen.add(mntmSharpenLoG);
		JMenuItem mntmSharpenRobert = new JMenuItem("Robert");
		mntmSharpenRobert.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new Performance("Filter", "Robert Sharpen")
				{
					@Override
					protected Image perform(Image image)
					{
						Kernel kernel = new RobertKernel();
						Convolver conv = new Convolver(kernel);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = conv.operate(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnSharpen.add(mntmSharpenRobert);
		JMenuItem mntmSharpenSobel = new JMenuItem("Sobel");
		mntmSharpenSobel.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new Performance("Filter", "Sobel Sharpen")
				{
					@Override
					protected Image perform(Image image)
					{
						Kernel kernel = new SobelKernel();
						Convolver conv = new Convolver(kernel);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = conv.operate(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnSharpen.add(mntmSharpenSobel);
		JMenuItem mntmSharpenPrewitt = new JMenuItem("Prewitt Sharpen");
		mntmSharpenPrewitt.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new Performance("Filter", "content")
				{
					@Override
					protected Image perform(Image image)
					{
						Kernel kernel = new PrewittKernel();
						Convolver conv = new Convolver(kernel);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = conv.operate(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnSharpen.add(mntmSharpenPrewitt);
		JMenuItem mntmSharpenKirsch = new JMenuItem("Kirsch");
		mntmSharpenKirsch.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new Performance("Filter", "Kirsch Sharpen")
				{
					@Override
					protected Image perform(Image image)
					{
						Kernel kernel = new KirschKernel();
						Convolver conv = new Convolver(kernel);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = conv.operate(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnSharpen.add(mntmSharpenKirsch);
	}

	/**
	 * Create analyze menu.
	 */
	private void menuAnalyse()
	{
		JMenu mnAnalyze = new JMenu("Analyze(A)");
		mnAnalyze.setMnemonic('A');
		menuBar.add(mnAnalyze);
		JMenuItem mntmPixelsHistogram = new JMenuItem("Pixels Histogram");
		mntmPixelsHistogram.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Image image = current();
				if (image == null)
					return;
				new HistogramDialog(DIPFrame.this, filename, Histogram
						.histogram(image), false);
			}
		});
		mnAnalyze.add(mntmPixelsHistogram);
		JMenuItem mntmFFT = new JMenuItem("FFT");
		mntmFFT.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new Performance("Analyze", "Fast Fourier Transformation")
				{
					@Override
					protected Image perform(Image image)
					{
						FourierTransformation trans = new FourierTransformation();
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = trans.forward(image);
						time = t.getTime(timeunit);
						ImageDisplayDialog idd = new ImageDisplayDialog(
								DIPFrame.this, "Fourier Power Spectrum", false,
								res);
						idd.setLocationRelativeTo(DIPFrame.this);
						idd.setVisible(true);
						return null;
					}
				}.perform();
			}
		});
		mntmFFT.setToolTipText("Fast Fourier Transformation");
		mnAnalyze.add(mntmFFT);
	}

	/**
	 * Create enhance menus.
	 */
	private void menuEnhance()
	{
		JMenu mnEnhance = new JMenu("Enhance(E)");
		mnEnhance.setMnemonic('E');
		menuBar.add(mnEnhance);
		JMenuItem mntmInverse = new JMenuItem("Inverse");
		mntmInverse.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new Performance("Filter", "Inverse")
				{
					@Override
					protected Image perform(Image image)
					{
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = InversionTransformation.inverse(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		JMenu mnGrayScale = new JMenu("Gray Scale");
		mnEnhance.add(mnGrayScale);
		JMenuItem mntmGrayScaleAverage = new JMenuItem("Average");
		mntmGrayScaleAverage.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new Performance("Enhance", "Gray Scale Average")
				{
					@Override
					protected Image perform(Image image)
					{
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = image instanceof ColorImage ? new GrayScaleAverage()
								.operate((ColorImage) image) : image;
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnGrayScale.add(mntmGrayScaleAverage);
		JMenuItem mntmGrayScaleCoefficient = new JMenuItem("Coefficient");
		mntmGrayScaleCoefficient.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new Performance("Enhance", "Gray Scale Coefficient")
				{
					@Override
					protected Image perform(Image image)
					{
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = image instanceof ColorImage ? new GrayScaleCoefficient()
								.operate((ColorImage) image) : image;
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnGrayScale.add(mntmGrayScaleCoefficient);
		mnEnhance.add(mntmInverse);
		JMenuItem mntmLogarithm = new JMenuItem("Logarithm");
		mntmLogarithm.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new Performance("Enhance", "Logarithm")
				{
					@Override
					protected Image perform(Image image)
					{
						LogarithmicTransformation trans = LogarithmicTransformation
								.getLogarithmTransformation(image.getClass(),
										255 / Math.log1p(256), 0.0);
						Properties properties = new Properties();
						String minB = LogarithmicTransformation.PARAM_B
								+ "_min";//$NON-NLS-1$
						String maxB = LogarithmicTransformation.PARAM_B
								+ "_max";//$NON-NLS-1$
						String minC = LogarithmicTransformation.PARAM_C
								+ "_min";//$NON-NLS-1$
						String maxC = LogarithmicTransformation.PARAM_C
								+ "_max";//$NON-NLS-1$
						properties.put(minB, -255);
						properties.put(maxB, 255);
						properties.put(minC, -255);
						properties.put(maxC, 255);
						FunctionDislpayDialog fdd = new FunctionDislpayDialog(
								DIPFrame.this, "Logarithmic Transformation",
								new String[] {
										LogarithmicTransformation.PARAM_C,
										LogarithmicTransformation.PARAM_B },
								properties, trans, true);
						fdd.setVisible(true);
						properties = fdd.getProperties();
						if (properties == null)
							return null;
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = trans.operate(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnEnhance.add(mntmLogarithm);
		JMenuItem mntmInverseLogarithm = new JMenuItem("Inverse Logarithm");
		mntmInverseLogarithm.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new Performance("Enhance", "Inverse Logarithm")
				{
					@Override
					protected Image perform(Image image)
					{
						LogarithmicTransformation trans = LogarithmicTransformation
								.getInverseLogarithmTransformation(
										image.getClass(),
										255 / Math.log1p(256), 0.0);
						Properties properties = new Properties();
						String minB = rs(LogarithmicTransformation.PARAM_B,
								false);
						String maxB = rs(LogarithmicTransformation.PARAM_B,
								true);
						String minC = rs(LogarithmicTransformation.PARAM_C,
								false);
						String maxC = rs(LogarithmicTransformation.PARAM_C,
								true);
						properties.put(minB, -255);
						properties.put(maxB, 255);
						properties.put(minC, -255);
						properties.put(maxC, 255);
						FunctionDislpayDialog fdd = new FunctionDislpayDialog(
								DIPFrame.this, "<Enhance> Inverse Logarithmic",
								new String[] {
										LogarithmicTransformation.PARAM_C,
										LogarithmicTransformation.PARAM_B },
								properties, trans, true);
						fdd.setVisible(true);
						properties = fdd.getProperties();
						if (properties == null)
							return null;
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = trans.operate(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnEnhance.add(mntmInverseLogarithm);
		JMenuItem mntmPowerLaw = new JMenuItem("Power Law");
		mntmPowerLaw.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new Performance("Enhance", "Power-law")
				{
					@Override
					protected Image perform(Image image)
					{
						PowerLawTransformation trans = PowerLawTransformation
								.getPowerLawTransformation(image.getClass(),
										PowerLawTransformation.SCALE_LEVEL - 1,
										1.0, 0.0);
						Properties properties = new Properties();
						String minB = rs(PowerLawTransformation.PARAM_B, false);
						String maxB = rs(PowerLawTransformation.PARAM_B, true);
						String minC = rs(PowerLawTransformation.PARAM_C, false);
						String maxC = rs(PowerLawTransformation.PARAM_C, true);
						String minG = rs(PowerLawTransformation.PARAM_GAMMA,
								false);
						String maxG = rs(PowerLawTransformation.PARAM_GAMMA,
								true);
						properties.put(minB, -255);
						properties.put(maxB, 255);
						properties.put(minC, 0);
						properties.put(maxC, 512);
						properties.put(minG, 0.1);
						properties.put(maxG, 10);
						FunctionDislpayDialog fdd = new FunctionDislpayDialog(
								DIPFrame.this, "Power-law Transformation",
								new String[] { PowerLawTransformation.PARAM_C,
										PowerLawTransformation.PARAM_B,
										PowerLawTransformation.PARAM_GAMMA },
								properties, trans, true);
						fdd.setVisible(true);
						properties = fdd.getProperties();
						if (properties == null)
							return null;
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = trans.operate(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnEnhance.add(mntmPowerLaw);
		JMenuItem mntmInversePowerlaw = new JMenuItem("Inverse Power-law");
		mntmInversePowerlaw.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new Performance("Enhance", "Inverse Power-law")
				{
					@Override
					protected Image perform(Image image)
					{
						PowerLawTransformation trans = PowerLawTransformation
								.inversePowerLawTransformation(image,
										PowerLawTransformation.SCALE_LEVEL - 1,
										1.0, 0.0);
						Properties properties = new Properties();
						String minB = rs(PowerLawTransformation.PARAM_B, false);
						String maxB = rs(PowerLawTransformation.PARAM_B, true);
						String minC = rs(PowerLawTransformation.PARAM_C, false);
						String maxC = rs(PowerLawTransformation.PARAM_C, true);
						String minG = rs(PowerLawTransformation.PARAM_GAMMA,
								false);
						String maxG = rs(PowerLawTransformation.PARAM_GAMMA,
								true);
						properties.put(minB, -255);
						properties.put(maxB, 255);
						properties.put(minC, 0);
						properties.put(maxC, 512);
						properties.put(minG, 0.1);
						properties.put(maxG, 10);
						FunctionDislpayDialog fdd = new FunctionDislpayDialog(
								DIPFrame.this,
								"Inverse Power-law Transformation",
								new String[] { PowerLawTransformation.PARAM_C,
										PowerLawTransformation.PARAM_B,
										PowerLawTransformation.PARAM_GAMMA },
								properties, trans, true);
						fdd.setVisible(true);
						properties = fdd.getProperties();
						if (properties == null)
							return null;
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = trans.operate(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnEnhance.add(mntmInversePowerlaw);
		JMenuItem mntmPiecewiseLinear = new JMenuItem("Piecewise Linear");
		mntmPiecewiseLinear.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new Performance("Enhance", "Piecewise Linear")
				{
					@Override
					protected Image perform(Image image)
					{
						PiecewiseLinearTransformation trans = PiecewiseLinearTransformation
								.getPiecewiseLinearTransformation(
										image.getClass(), 50, 100, 150, 200);
						Properties properties = new Properties();
						String minAX = rs(
								PiecewiseLinearTransformation.PARAM_A_X, false);
						String maxAX = rs(
								PiecewiseLinearTransformation.PARAM_A_X, true);
						String minAY = rs(
								PiecewiseLinearTransformation.PARAM_A_Y, false);
						String maxAY = rs(
								PiecewiseLinearTransformation.PARAM_A_Y, true);
						String minBX = rs(
								PiecewiseLinearTransformation.PARAM_B_X, false);
						String maxBX = rs(
								PiecewiseLinearTransformation.PARAM_B_X, true);
						String minBY = rs(
								PiecewiseLinearTransformation.PARAM_B_Y, false);
						String maxBY = rs(
								PiecewiseLinearTransformation.PARAM_B_Y, true);
						properties.put(minAX, 1);
						properties.put(maxAX, 254);
						properties.put(minAY, 1);
						properties.put(maxAY, 254);
						properties.put(minBX, 1);
						properties.put(maxBX, 254);
						properties.put(minBY, 1);
						properties.put(maxBY, 254);
						FunctionDislpayDialog fdd = new FunctionDislpayDialog(
								DIPFrame.this,
								"Piecewise Linear Transformation",
								new String[] {
										PiecewiseLinearTransformation.PARAM_A_X,
										PiecewiseLinearTransformation.PARAM_A_Y,
										PiecewiseLinearTransformation.PARAM_B_X,
										PiecewiseLinearTransformation.PARAM_B_Y },
								properties, trans, true);
						fdd.setVisible(true);
						properties = fdd.getProperties();
						if (properties == null)
							return null;
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = trans.operate(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnEnhance.add(mntmPiecewiseLinear);
		JMenu mnHistogramStretch = new JMenu("Histogram Stretch");
		mnEnhance.add(mnHistogramStretch);
		JMenuItem mntmHistogramStretchAverage = new JMenuItem("Average");
		mntmHistogramStretchAverage.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new Performance("Enhance", "Average Histogram Stretch")
				{
					@Override
					protected Image perform(Image image)
					{
						HistogramNormalization trans = HistogramNormalization
								.getHistogramNormalizatoin(
										HistogramNormalization.getAverage(),
										image.getClass());
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = trans.operate(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnHistogramStretch.add(mntmHistogramStretchAverage);
		JMenuItem mntmHistogramStretchQuadratic = new JMenuItem("Quadratic");
		mntmHistogramStretchQuadratic.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new Performance("Enhance", "Quadratic Histogram Stretch")
				{
					@Override
					protected Image perform(Image image)
					{
						Function function = new Quadratic(1.5 / Math
								.pow(128, 3), -3.0 / 128 / 128, 3.0 / 256);
						HistogramNormalization trans = HistogramNormalization
								.getHistogramNormalizatoin(function,
										image.getClass());
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = trans.operate(image);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnHistogramStretch.add(mntmHistogramStretchQuadratic);
		JMenuItem mntmFakeColor = new JMenuItem("Fake Color");
		mntmFakeColor.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new Performance("Enhance", "Fake Color Transform")
				{
					@Override
					protected Image perform(Image image)
					{
						double termCount, ratio;
						String numberFormatErrorMsg = "%s is not an integer or a float.";
						String s = SwingUtils.inputDialog(DIPFrame.this,
								"Term Count",
								"Input the term count (integer or float):");
						if (s == null)
							return null;
						try
						{
							termCount = Double.valueOf(s);
						}
						catch (NumberFormatException e)
						{
							SwingUtils.errorMessage(DIPFrame.this,
									String.format(numberFormatErrorMsg, s));
							return null;
						}
						s = SwingUtils.inputDialog(DIPFrame.this, "Ratio",
								"Input the ratio (integer or float):");
						if (s == null)
							return null;
						try
						{
							ratio = Double.valueOf(s);
						}
						catch (NumberFormatException e)
						{
							SwingUtils.errorMessage(DIPFrame.this,
									String.format(numberFormatErrorMsg, s));
							return null;
						}
						FakeColorTransform trans = FakeColorTransform
								.getTermBasedInstance(termCount, ratio);
						GrayImage gi = null;
						if (image instanceof GrayImage)
							gi = (GrayImage) image;
						else
						{
							SwingUtils
									.errorMessage(DIPFrame.this,
											"The source image of fake color transformation must be a gray image.");
						}
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = trans.operate(gi);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnEnhance.add(mntmFakeColor);
	}

	/**
	 * Create geometry menus.
	 */
	private void menuGeometry()
	{
		JMenu mnGeometry = new JMenu("Geometry(G)");
		menuBar.add(mnGeometry);
		JMenuItem mntmScale = new JMenuItem("Scale");
		mntmScale.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new Performance("Geometry", "Scale")
				{
					@Override
					protected Image perform(Image image)
					{
						Integer type = selectInterpolationType();
						if (type == null)
							return null;
						String s = SwingUtils.inputDialog(DIPFrame.this,
								"Scale", "Input the scale ratio:");
						Double v = null;
						try
						{
							v = Double.valueOf(s);
						}
						catch (NumberFormatException e1)
						{
							return null;
						}
						Geometry g = Geometry.getGeometry(image.getClass(),
								type, Geometry.FILL_WITH_BLANK);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = null;
						res = g.scaleByRate(image, v);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnGeometry.add(mntmScale);
		JMenuItem mntmRotate = new JMenuItem("Rotate");
		mntmRotate.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new Performance("Geometry", "Rotate")
				{
					@Override
					protected Image perform(Image image)
					{
						Integer type = selectInterpolationType();
						if (type == null)
							return null;
						String s = SwingUtils.inputDialog(DIPFrame.this,
								"Rotate", "Input rotate angle in degree:");
						if (s == null)
							return null;
						Double v = null;
						try
						{
							v = Double.valueOf(s);
						}
						catch (NumberFormatException e1)
						{
							SwingUtils.errorMessage(
									DIPFrame.this,
									String.format(
											"%s is not a number(neither float or integer)",
											s));
							return null;
						}
						Geometry geom = Geometry.getGeometry(image, type,
								Geometry.FILL_WITH_BLANK);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = null;
						res = geom.rotateByDegree(image, v);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnGeometry.add(mntmRotate);
		JMenuItem mntmAffineTransform = new JMenuItem("Affine Transform");
		mntmAffineTransform.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new Performance("Geometry", "Scale")
				{
					@Override
					protected Image perform(Image image)
					{
						Integer type = selectInterpolationType();
						if (type == null)
							return null;
						String s = SwingUtils
								.inputDialog(
										DIPFrame.this,
										"Input Kernel",
										"Input six kernel values in the order of\r\n"
												+ "m00, m10, m01, m11, m02, m12\r\n"
												+ "seperate values with ASCII common\",\":\r\n"
												+ "e.g. \"1,0,0,1,0,0\"");
						if (s == null)
							return null;
						String[] ks = s.split(",|\uff0c");
						AffineTransform transform = null;
						try
						{
							transform = new AffineTransform(Double
									.valueOf(ks[0]), Double.valueOf(ks[1]),
									Double.valueOf(ks[2]), Double
											.valueOf(ks[3]), Double
											.valueOf(ks[4]), Double
											.valueOf(ks[5]));
						}
						catch (Exception e)
						{
							SwingUtils.errorMessage(DIPFrame.this, String
									.format("Incorrect format for \"%s\".", s));
							return null;
						}
						Geometry geom = Geometry.getGeometry(image, type,
								Geometry.FILL_WITH_BLANK);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = null;
						try
						{
							res = geom.transform(image, transform);
						}
						catch (NoninvertibleTransformException e)
						{
							e.printStackTrace();
							SwingUtils.errorMessage(
									DIPFrame.this,
									String.format(
											"The affine transform defined contains no inverse transform: %s",
											e.getLocalizedMessage()));
							return null;
						}
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnGeometry.add(mntmAffineTransform);
		JMenuItem mntmSpatialTransform = new JMenuItem("Spatial Transform");
		mntmSpatialTransform.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new Performance("Geometry", "Scale")
				{
					@Override
					protected Image perform(Image image)
					{
						String s = SwingUtils
								.inputDialog(
										DIPFrame.this,
										"Input Anchors",
										"Input four anchors from\r\n"
												+ "left-top to left-bottom clockwise\r\n"
												+ "and seperate coordinates with ASCII common\",\"\r\n"
												+ "anchors with spliter \"|\" or ASCII colon\":\"\r\n"
												+ "e.g. \"1,2:3,4|5,6:7,8\"");
						if (s == null)
							return null;
						String[] ps = s.split(":|\\|");
						String[] xy;
						Point2D p00, p10, p11, p01;
						try
						{
							xy = ps[0].split(",|\uff0c");
							p00 = new Point2D.Double(Double.valueOf(xy[0]),
									Double.valueOf(xy[1]));
							xy = ps[1].split(",|\uff0c");
							p10 = new Point2D.Double(Double.valueOf(xy[0]),
									Double.valueOf(xy[1]));
							xy = ps[2].split(",|\uff0c");
							p11 = new Point2D.Double(Double.valueOf(xy[0]),
									Double.valueOf(xy[1]));
							xy = ps[3].split(",|\uff0c");
							p01 = new Point2D.Double(Double.valueOf(xy[0]),
									Double.valueOf(xy[1]));
						}
						catch (Exception e)
						{
							SwingUtils.errorMessage(DIPFrame.this, String
									.format("Incorrect format for \"%s\".", s));
							return null;
						}
						Integer type = selectInterpolationType();
						if (type == null)
							return null;
						Geometry geom = Geometry.getGeometry(image, type,
								Geometry.FILL_WITH_BLANK);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = null;
						res = geom.spatialTransform(image, p00, p01, p11, p10);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnGeometry.add(mntmSpatialTransform);
		JMenu mnExperimentp = new JMenu("Experiment(P)");
		mnExperimentp.setMnemonic('P');
		menuBar.add(mnExperimentp);
		JMenuItem mntm3ChannelsEdge = new JMenuItem("3 Channels Edge Detect");
		mntm3ChannelsEdge.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new Performance("Test", "3 Channels Edge Detect")
				{
					@Override
					protected Image perform(Image image)
					{
						ColorImage ci = null;
						if (image instanceof ColorImage)
							ci = (ColorImage) image;
						else
						{
							SwingUtils.errorMessage(DIPFrame.this,
									"Not a color image!");
							return null;
						}
						if (ci.width() > 256 && ci.height() > 256)
							ci = new GeometryColor().scale(ci, 256);
						GrayImage red = ChannelGrabber.grab(ci,
								ChannelGrabber.RED);
						GrayImage green = ChannelGrabber.grab(ci,
								ChannelGrabber.GREEN);
						GrayImage blue = ChannelGrabber.grab(ci,
								ChannelGrabber.BLUE);
						GrayImage gray = ChannelGrabber.grab(ci,
								ChannelGrabber.GRAY);
						Convolver<GrayImage> conv = new Convolver<GrayImage>(
								new LoGKernel());
						GrayImage red_c = conv.operate(red);
						GrayImage green_c = conv.operate(green);
						GrayImage blue_c = conv.operate(blue);
						GrayImage gray_c = conv.operate(gray);
						java.awt.Window window = DIPFrame.this;
						boolean modal = false;
						Dimension screen = Toolkit.getDefaultToolkit()
								.getScreenSize();
						int x = 0, y = 0, dx = screen.width / 4, dy = screen.height / 2;
						// source image displaying
						ImageDisplayDialog red_d = new ImageDisplayDialog(
								window, "Red Channel", modal, red);
						wins.add(red_d);
						red_d.setLocation(x, y);
						x += dx;
						ImageDisplayDialog green_d = new ImageDisplayDialog(
								window, "Green Channel", modal, green);
						wins.add(green_d);
						green_d.setLocation(x, y);
						x += dx;
						ImageDisplayDialog blue_d = new ImageDisplayDialog(
								window, "Blue Channel", modal, blue);
						wins.add(blue_d);
						blue_d.setLocation(x, y);
						x += dx;
						ImageDisplayDialog gray_d = new ImageDisplayDialog(
								window, "Gray Image", modal, gray);
						wins.add(gray_d);
						gray_d.setLocation(x, y);
						x += dx;
						// convolved image displaying
						x = 0;
						y += dy;
						ImageDisplayDialog red_c_d = new ImageDisplayDialog(
								window, "Convolve Red", modal, red_c);
						wins.add(red_c_d);
						wins.add(red_c_d);
						red_c_d.setLocation(x, y);
						x += dx;
						ImageDisplayDialog green_c_d = new ImageDisplayDialog(
								window, "Convolve Green", modal, green_c);
						wins.add(green_c_d);
						green_c_d.setLocation(x, y);
						x += dx;
						ImageDisplayDialog blue_c_d = new ImageDisplayDialog(
								window, "Convolve Blue", modal, blue_c);
						wins.add(blue_c_d);
						blue_c_d.setLocation(x, y);
						x += dx;
						ImageDisplayDialog gray_c_d = new ImageDisplayDialog(
								window, "Convolve Gray", modal, gray_c);
						wins.add(gray_c_d);
						gray_c_d.setLocation(x, y);
						x += dx;
						// set visibilities
						boolean b = true;
						red_d.setVisible(b);
						green_d.setVisible(b);
						blue_d.setVisible(b);
						gray_d.setVisible(b);
						red_c_d.setVisible(b);
						green_c_d.setVisible(b);
						blue_c_d.setVisible(b);
						gray_c_d.setVisible(b);
						return null;
					}
				}.perform();
			}
		});
		mnExperimentp.add(mntm3ChannelsEdge);
	}

	/**
	 * Create thresholding menus.
	 */
	private void menuThreshold()
	{
		JMenu mnThreshold = new JMenu("Threshold(T)");
		mnThreshold.setMnemonic('T');
		menuBar.add(mnThreshold);
		JMenuItem mntmQuickTransform = new JMenuItem("Quick Binary Transform");
		mntmQuickTransform.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new Performance("Thresholding", null)
				{
					@Override
					protected Image perform(Image image)
					{
						GrayImage src = new GrayImage(image);
						ThresholdFinder finder = new ThresholdFinder()
						{
							@Override
							public int threshold(GrayImage image)
							{
								return 127;
							}

							@Override
							public String getFinderName()
							{
								return "Quick Binary Transform(127)";
							}
						};
						Thresholding thresholding = new GlobalThresholding(
								finder);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = thresholding.operate(src);
						time = t.getTime(timeunit);
						content = finder.getFinderName();
						return res;
					}
				}.perform();
			}
		});
		mntmQuickTransform.setToolTipText("Threshold the gray image with 127.");
		mnThreshold.add(mntmQuickTransform);
		JMenuItem mntmManualThreshold = new JMenuItem("Manual Threshold");
		mntmManualThreshold.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new Performance("Thresholding", null)
				{
					@Override
					protected Image perform(Image image)
					{
						GrayImage src = new GrayImage(image);
						String s = SwingUtils.inputDialog(DIPFrame.this,
								"Global Threshold",
								"Input the global threshold:");
						if (s == null)
							return null;
						ThresholdFinder finder = null;
						Thresholding thresholding = null;
						try
						{
							final Integer threshold = Integer.valueOf(s);
							if (threshold == null)
								return null;
							finder = new ThresholdFinder()
							{
								@Override
								public int threshold(GrayImage image)
								{
									return threshold;
								}

								@Override
								public String getFinderName()
								{
									return String.format(
											"Manual Thresholding: %d",
											threshold);
								}
							};
							thresholding = new GlobalThresholding(finder);
						}
						catch (NumberFormatException e1)
						{
							SwingUtils.errorMessage(DIPFrame.this,
									e1.getLocalizedMessage());
							return null;
						}
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = thresholding.operate(src);
						time = t.getTime(timeunit);
						content = finder.getFinderName();
						return res;
					}
				}.perform();
			}
		});
		mntmManualThreshold
				.setToolTipText("Manual select the threshold of the image.");
		mnThreshold.add(mntmManualThreshold);
		JMenuItem mntmFrank = new JMenuItem("Frank");
		mntmFrank.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new Performance("Thresholding", "Frank")
				{
					@Override
					protected Image perform(Image image)
					{
						GrayImage src = new GrayImage(image);
						// Parameter selecting
						String fuzzyRadio = "fuzzy radio";
						String clusteringWindowRadius = "clustering radius";
						ThresholdFinder finder = null;
						Properties p = new Properties();
						{
							String frMin = rs(fuzzyRadio, false), frMax = rs(
									fuzzyRadio, true);
							String cwMin = rs(clusteringWindowRadius, false), cwMax = rs(
									clusteringWindowRadius, true);
							p.put(fuzzyRadio, 0.2);
							p.put(frMin, 0.01);
							p.put(frMax, 1);
							p.put(clusteringWindowRadius, 0.1);
							p.put(cwMin, 0.01);
							p.put(cwMax, 1);
							FunctionDislpayDialog fdd = new FunctionDislpayDialog(
									DIPFrame.this, "Frank Thresholding",
									new String[] { clusteringWindowRadius,
											fuzzyRadio }, p, null, true);
							fdd.setVisible(true);
							p = fdd.getProperties();
							if (p == null)
								return null;
							ThresholdFinder[] finders = new ThresholdFinder[] {
									new IsoData(), new Intermodes(),
									new Minimum(), new Fuzzy(),
									new MaxEntropy(), new Mean(),
									new MinCrossEntropy(), new MinError(),
									new Moments(), new Otsu(),
									new Percentile(), new RenyiEntropy(),
									new Shanbhag(), new Triangle(), new Yen() };
							String[] selections = new String[finders.length];
							for (int i = 0; i < finders.length; i++)
								selections[i] = finders[i].getFinderName();
							MathUtils.combineSort(finders, selections);
							Arrays.sort(selections);
							Object selection = JOptionPane.showInputDialog(
									DIPFrame.this,
									"Select the global thresholding method:",
									"Select", JOptionPane.INFORMATION_MESSAGE,
									null, selections,
									new IsoData().getFinderName());
							if (selection == null)
								return null;
							int i = Arrays.binarySearch(selections, selection);
							if (i < 0)
								return null;
							finder = finders[i];
						}
						double radius = (Double) p.get(clusteringWindowRadius);
						double fuzzy = (Double) p.get(fuzzyRadio);
						Thresholding thresholding = new FrankThresholding(
								radius, fuzzy, finder);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = thresholding.operate(src);
						time = t.getTime(timeunit);
						content = finder.getFinderName();
						return res;
					}
				}.perform();
			}
		});
		mnThreshold.add(mntmFrank);
		JMenuItem mntmOtsu = new JMenuItem("Otsu");
		mntmOtsu.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new Performance("Thresholding", null)
				{
					@Override
					protected Image perform(Image image)
					{
						GrayImage src = new GrayImage(image);
						ThresholdFinder finder = new Otsu();
						Thresholding thresholding = new GlobalThresholding(
								finder);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = thresholding.operate(src);
						time = t.getTime(timeunit);
						content = finder.getFinderName();
						return res;
					}
				}.perform();
			}
		});
		mnThreshold.add(mntmOtsu);
		JMenuItem mntmMaxEntropy = new JMenuItem("Max Entropy");
		mntmMaxEntropy.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new Performance("Thresholding", null)
				{
					@Override
					protected Image perform(Image image)
					{
						GrayImage src = new GrayImage(image);
						ThresholdFinder finder = new MaxEntropy();
						Thresholding thresholding = new GlobalThresholding(
								finder);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = thresholding.operate(src);
						time = t.getTime(timeunit);
						content = finder.getFinderName();
						return res;
					}
				}.perform();
			}
		});
		mnThreshold.add(mntmMaxEntropy);
		JMenuItem mntmMinError = new JMenuItem("Min Error");
		mntmMinError.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new Performance("Thresholding", null)
				{
					@Override
					protected Image perform(Image image)
					{
						GrayImage src = new GrayImage(image);
						ThresholdFinder finder = new MinError();
						Thresholding thresholding = new GlobalThresholding(
								finder);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = thresholding.operate(src);
						time = t.getTime(timeunit);
						content = finder.getFinderName();
						return res;
					}
				}.perform();
			}
		});
		mnThreshold.add(mntmMinError);
		JMenuItem mntmFuzzy = new JMenuItem("Fuzzy");
		mntmFuzzy.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new Performance("Thresholding", null)
				{
					@Override
					protected Image perform(Image image)
					{
						GrayImage src = new GrayImage(image);
						ThresholdFinder finder = new Fuzzy();
						Thresholding thresholding = new GlobalThresholding(
								finder);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = thresholding.operate(src);
						time = t.getTime(timeunit);
						content = finder.getFinderName();
						return res;
					}
				}.perform();
			}
		});
		mnThreshold.add(mntmFuzzy);
		JMenuItem mntmIntermodes = new JMenuItem("Intermodes");
		mntmIntermodes.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new Performance("Thresholding", null)
				{
					@Override
					protected Image perform(Image image)
					{
						GrayImage src = new GrayImage(image);
						ThresholdFinder finder = new Intermodes();
						Thresholding thresholding = new GlobalThresholding(
								finder);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = thresholding.operate(src);
						time = t.getTime(timeunit);
						content = finder.getFinderName();
						return res;
					}
				}.perform();
			}
		});
		mnThreshold.add(mntmIntermodes);
		JMenuItem mntmIsodata = new JMenuItem("IsoData");
		mntmIsodata.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new Performance("Thresholding", null)
				{
					@Override
					protected Image perform(Image image)
					{
						GrayImage src = new GrayImage(image);
						ThresholdFinder finder = new IsoData();
						Thresholding thresholding = new GlobalThresholding(
								finder);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = thresholding.operate(src);
						time = t.getTime(timeunit);
						content = finder.getFinderName();
						return res;
					}
				}.perform();
			}
		});
		mnThreshold.add(mntmIsodata);
		JMenuItem mntmMinCrossEntropy = new JMenuItem("Min Cross Entropy");
		mntmMinCrossEntropy.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new Performance("Thresholding", null)
				{
					@Override
					protected Image perform(Image image)
					{
						GrayImage src = new GrayImage(image);
						ThresholdFinder finder = new MinCrossEntropy();
						Thresholding thresholding = new GlobalThresholding(
								finder);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = thresholding.operate(src);
						time = t.getTime(timeunit);
						content = finder.getFinderName();
						return res;
					}
				}.perform();
			}
		});
		mnThreshold.add(mntmMinCrossEntropy);
		JMenuItem mntmMean = new JMenuItem("Mean");
		mntmMean.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new Performance("Thresholding", null)
				{
					@Override
					protected Image perform(Image image)
					{
						GrayImage src = new GrayImage(image);
						ThresholdFinder finder = new Mean();
						Thresholding thresholding = new GlobalThresholding(
								finder);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = thresholding.operate(src);
						time = t.getTime(timeunit);
						content = finder.getFinderName();
						return res;
					}
				}.perform();
			}
		});
		mnThreshold.add(mntmMean);
		JMenuItem mntmMoments = new JMenuItem("Moments");
		mntmMoments.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new Performance("Thresholding", null)
				{
					@Override
					protected Image perform(Image image)
					{
						GrayImage src = new GrayImage(image);
						ThresholdFinder finder = new Moments();
						Thresholding thresholding = new GlobalThresholding(
								finder);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = thresholding.operate(src);
						time = t.getTime(timeunit);
						content = finder.getFinderName();
						return res;
					}
				}.perform();
			}
		});
		JMenuItem mntmMinimum = new JMenuItem("Minimum");
		mntmMinimum.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new Performance("Thresholding", null)
				{
					@Override
					protected Image perform(Image image)
					{
						GrayImage src = new GrayImage(image);
						ThresholdFinder finder = new Minimum();
						Thresholding thresholding = new GlobalThresholding(
								finder);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = thresholding.operate(src);
						time = t.getTime(timeunit);
						content = finder.getFinderName();
						return res;
					}
				}.perform();
			}
		});
		mnThreshold.add(mntmMinimum);
		mnThreshold.add(mntmMoments);
		JMenuItem mntmPercentile = new JMenuItem("Percentile");
		mntmPercentile.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new Performance("Thresholding", null)
				{
					@Override
					protected Image perform(Image image)
					{
						GrayImage src = new GrayImage(image);
						ThresholdFinder finder = new Percentile();
						Thresholding thresholding = new GlobalThresholding(
								finder);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = thresholding.operate(src);
						time = t.getTime(timeunit);
						content = finder.getFinderName();
						return res;
					}
				}.perform();
			}
		});
		mnThreshold.add(mntmPercentile);
		JMenuItem mntmRenyiEntropy = new JMenuItem("Renyi's Entropy");
		mntmRenyiEntropy.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new Performance("Thresholding", null)
				{
					@Override
					protected Image perform(Image image)
					{
						GrayImage src = new GrayImage(image);
						ThresholdFinder finder = new RenyiEntropy();
						Thresholding thresholding = new GlobalThresholding(
								finder);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = thresholding.operate(src);
						time = t.getTime(timeunit);
						content = finder.getFinderName();
						return res;
					}
				}.perform();
			}
		});
		mnThreshold.add(mntmRenyiEntropy);
		JMenuItem mntmShanbhag = new JMenuItem("Shanbhag");
		mntmShanbhag.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new Performance("Thresholding", null)
				{
					@Override
					protected Image perform(Image image)
					{
						GrayImage src = new GrayImage(image);
						ThresholdFinder finder = new Shanbhag();
						Thresholding thresholding = new GlobalThresholding(
								finder);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = thresholding.operate(src);
						time = t.getTime(timeunit);
						content = finder.getFinderName();
						return res;
					}
				}.perform();
			}
		});
		mnThreshold.add(mntmShanbhag);
		JMenuItem mntmTriangle = new JMenuItem("Triangle");
		mntmTriangle.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new Performance("Thresholding", null)
				{
					@Override
					protected Image perform(Image image)
					{
						GrayImage src = new GrayImage(image);
						ThresholdFinder finder = new Triangle();
						Thresholding thresholding = new GlobalThresholding(
								finder);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = thresholding.operate(src);
						time = t.getTime(timeunit);
						content = finder.getFinderName();
						return res;
					}
				}.perform();
			}
		});
		mnThreshold.add(mntmTriangle);
		JMenuItem mntmYen = new JMenuItem("Yen");
		mntmYen.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new Performance("Thresholding", null)
				{
					@Override
					protected Image perform(Image image)
					{
						GrayImage src = new GrayImage(image);
						ThresholdFinder finder = new Yen();
						Thresholding thresholding = new GlobalThresholding(
								finder);
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = thresholding.operate(src);
						time = t.getTime(timeunit);
						content = finder.getFinderName();
						return res;
					}
				}.perform();
			}
		});
		mnThreshold.add(mntmYen);
		JMenuItem mntmFbclustering = new JMenuItem("FBClustering");
		mntmFbclustering.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				new Performance("Thresholding", "FBClustering")
				{
					@Override
					protected Image perform(Image image)
					{
						GrayImage src = new GrayImage(image);
						Thresholding thresholding = new FBClustering();
						Timer t = TestUtils.getTimer();
						t.start();
						Image res = thresholding.operate(src);
						time = t.getTime(timeunit);
						return res;
					}
				}.perform();
			}
		});
		mnThreshold.add(mntmFbclustering);
	}

	/**
	 * Create menu edit.
	 */
	private void menuEdit()
	{
		JMenu mnEdit = new JMenu("Edit(D)");
		mnEdit.addMenuListener(new MenuListener()
		{
			@Override
			public void menuSelected(MenuEvent e)
			{
				// update the status of paste menu item
				Clipboard clipboard = Toolkit.getDefaultToolkit()
						.getSystemClipboard();
				Transferable transferable = clipboard.getContents(null);
				mntmPaste.setEnabled(transferable != null
						&& transferable
								.isDataFlavorSupported(DataFlavor.imageFlavor));
				// update the status of copy menu item
				mntmCopy.setEnabled(!pm.isEmpty());
			}

			@Override
			public void menuDeselected(MenuEvent e)
			{
				// empty implementation
			}

			@Override
			public void menuCanceled(MenuEvent e)
			{
				// empty implementation
			}
		});
		mnEdit.setMnemonic('D');
		menuBar.add(mnEdit);
		mntmUndo = new JMenuItem("Undo");
		mntmUndo.setIcon(new ImageIcon(DIPFrame.class
				.getResource("/com/frank/dip/res/undo.png")));
		mntmUndo.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				pm.undo();
				Image curr = pm.current();
				paint(curr);
				updateCanvasTitle(curr);
				updateStep();
			}
		});
		mntmUndo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z,
				InputEvent.CTRL_MASK));
		mnEdit.add(mntmUndo);
		mntmRedo = new JMenuItem("Redo");
		mntmRedo.setIcon(new ImageIcon(DIPFrame.class
				.getResource("/com/frank/dip/res/redo.png")));
		mntmRedo.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				pm.redo();
				Image curr = pm.current();
				paint(curr);
				updateCanvasTitle(curr);
				updateStep();
			}
		});
		mntmRedo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y,
				InputEvent.CTRL_MASK));
		mnEdit.add(mntmRedo);
		mntmCopy = new JMenuItem("Copy");
		mntmCopy.setIcon(new ImageIcon(DIPFrame.class
				.getResource("/com/frank/dip/res/clipboard - copy.png")));
		mntmCopy.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Image im = pm.current();
				if (im != null)
					SystemUtils.setClipboardImage(im.restore());
			}
		});
		mntmCopy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
				InputEvent.CTRL_MASK));
		mnEdit.add(mntmCopy);
		mntmPaste = new JMenuItem("Paste");
		mntmPaste.setIcon(new ImageIcon(DIPFrame.class
				.getResource("/com/frank/dip/res/clipboard - paste.png")));
		mntmPaste.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				try
				{
					java.awt.Image im = SystemUtils.getImageFromClipboard();
					if (im == null)
						return;
					canvas.setText("");
					pm.reset();
					recordAndPaint(new ColorImage(im, null));
					filename = "System Image";
					updateCanvasTitle(pm.current());
				}
				catch (Exception e1)
				{
					e1.printStackTrace();
				}
			}
		});
		mntmPaste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,
				InputEvent.CTRL_MASK));
		mnEdit.add(mntmPaste);
		updateStep();
	}

	/**
	 * Create menu file.
	 */
	private void menuFile()
	{
		JMenu mnFile = new JMenu("File(F)");
		mnFile.setMnemonic('F');
		menuBar.add(mnFile);
		JMenuItem mntmOpen = new JMenuItem("Open");
		mntmOpen.setIcon(new ImageIcon(DIPFrame.class
				.getResource("/com/frank/dip/res/file - open.png")));
		mntmOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
				InputEvent.CTRL_MASK));
		mntmOpen.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				open();
			}
		});
		mnFile.add(mntmOpen);
		JMenuItem mntmSave = new JMenuItem("Save");
		mntmSave.setIcon(new ImageIcon(DIPFrame.class
				.getResource("/com/frank/dip/res/file - save.png")));
		mntmSave.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				save();
			}
		});
		mntmSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				InputEvent.CTRL_MASK));
		mnFile.add(mntmSave);
		JSeparator separator = new JSeparator();
		mnFile.add(separator);
		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dispose();
			}
		});
		mnFile.add(mntmExit);
	}

	/**
	 * Paint image to canvas.
	 * 
	 * @param image
	 *            the image to paint
	 */
	private void paint(Image image)
	{
		if (image == null)
			throw new NullPointerException("The image to paint is null.");
		canvas.setIcon(new javax.swing.ImageIcon(image.restore()));
	}

	/**
	 * Paint image to canvas and record this step.
	 * 
	 * @param image
	 *            the image to paint
	 */
	private void recordAndPaint(Image image)
	{
		if (image == null)
			throw new NullPointerException("The image to paint is null.");
		pm.perform(image);
		updateStep();
		canvas.setIcon(new javax.swing.ImageIcon(image.restore()));
	}

	/**
	 * Update the UI according to the step counter.
	 */
	private void updateStep()
	{
		mntmRedo.setEnabled(pm.hasNext());
		mntmUndo.setEnabled(pm.hasPrevious());
	}

	/**
	 * Update the mouse position.
	 * 
	 * @param point
	 *            the point to display
	 */
	private void updateMouseMotion(Point point)
	{
		if (!flagOfShowingPixels)
			lblPosition.setText((point == null) ? "" : String.format("(%d,%d)",
					point.x, point.y));
		else if (point != null)
		{
			Image im = pm.current();
			if (im instanceof ColorImage)
			{
				ColorImage ci = (ColorImage) im;
				lblPosition.setText(String.format(
						"(%d,%d) - ARGB(%d,%d,%d,%d)", point.x, point.y,
						ci.getAlpha(point.x, point.y),
						ci.getRed(point.x, point.y),
						ci.getGreen(point.x, point.y),
						ci.getBlue(point.x, point.y)));
			}
			else
				lblPosition.setText(String.format("(%d,%d) - Gray(%d)",
						point.x, point.y, im.getPixel(point.x, point.y)));
		}
		splitPane2.resetToPreferredSizes();
	}

	/**
	 * Update comment by function time spent.
	 * 
	 * @param name
	 *            function name
	 * @param time
	 *            time spent
	 */
	private void updateCommentByFunc(String name, long time)
	{
		String t = String.format("%s: %d%s", name, time, timeunitStr);//$NON-NLS-1$
		lblComment.setText(t);
		lblComment.setToolTipText(t);
	}

	/**
	 * Open the specified image input stream.
	 * 
	 * @param is
	 *            the image input stream
	 * @param filename
	 *            the display name
	 */
	private void openImage(java.io.InputStream is, String filename)
	{
		canvas.setText("");
		try
		{
			pm.reset();
			Image im = ImageUtils.createImage(is);
			recordAndPaint(im);
			this.filename = filename;
			updateCanvasTitle(im);
		}
		catch (Exception e1)
		{
			e1.printStackTrace();
			SwingUtils.errorMessage(DIPFrame.this, String.format(
					"Not spported image format/file: %s", filename));
			canvas.setText("No Image");
			updateCanvasTitle(null);
			canvas.setIcon(null);
		}
	}

	/**
	 * Open the specified image file.
	 * 
	 * @param file
	 *            the specified image file
	 */
	private void openImage(final File file)
	{
		canvas.setText("");
		pm.reset();
		Timer t = TestUtils.getTimer();
		Image image;
		try
		{
			image = ImageUtils.createImage(file);
			filename = file.getName();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			SwingUtils.errorMessage(
					DIPFrame.this,
					String.format("Not spported image format/file: %s",
							file.getName()));
			canvas.setText("No Image");
			updateCanvasTitle(null);
			canvas.setIcon(null);
			return;
		}
		long time = t.getTime(timeunit);
		recordAndPaint(image);
		updateCanvasTitle(image);
		updateCommentByFunc("<Edit> Open Image", time);
	}

	/**
	 * Open image from file system.
	 */
	private void open()
	{
		File file = SwingUtils.selectLoadFile(DIPFrame.this, "Open Image File");
		if (file != null)
			openImage(file);
	}

	/**
	 * Save the current image to the specified path. The specified path will be
	 * selected by the user.
	 */
	private void save()
	{
		if (!pm.isEmpty())
		{
			FileDialog fd = new FileDialog(this, "Save Image File",
					FileDialog.SAVE);
			fd.setVisible(true);
			String directory = fd.getDirectory();
			String filename = fd.getFile();
			if (filename != null)
			{
				int index = filename.lastIndexOf('.');
				String formatName = null;
				if (index == -1)
				{
					formatName = "png";//$NON-NLS-1$
					filename += ".png";//$NON-NLS-1$
				}
				else
					formatName = filename.substring(index + 1);
				File output = new File(directory, filename);
				Image image = pm.current();
				if (image == null)
					throw new NullPointerException(
							"The current image instance is null.");
				try
				{
					ImageIO.write(image.restore(), formatName, output);
				}
				catch (Exception e)
				{
					e.printStackTrace();
					SwingUtils.errorMessage(
							this,
							String.format("Save image failed: %s",
									e.getLocalizedMessage()));
				}
			}
		}
	}

	/**
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable o, Object arg)
	{
		if (o == pm)
			flagOfShowingPosition = !pm.isEmpty();
	}

	/**
	 * Returns the constraints string of the specified parameter.
	 * 
	 * @param paramName
	 * @param isMaxValue
	 * @return the constraints string
	 */
	String rs(String paramName, boolean isMaxValue)
	{
		return String.format(isMaxValue ? "%s_max" : "%s_min", paramName);//$NON-NLS-1$//$NON-NLS-2$
	}

	/**
	 * The digital image processing performance.
	 * <p>
	 * </p>
	 * 
	 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
	 * @version 1.0.0
	 */
	private abstract class Performance
	{
		/**
		 * The time spent in millisecond.
		 */
		protected long		time;
		/**
		 * The title of the procedure.
		 */
		protected String	title;
		/**
		 * The content of the process.
		 */
		protected String	content;

		/**
		 * Construct an instance of <tt>Performance</tt>.
		 * 
		 * @param title
		 *            the title of the procedure
		 * @param content
		 *            the content of the process
		 */
		public Performance(String title, String content)
		{
			this.title = title;
			this.content = content;
		}

		/**
		 * Perform the digital image processing and returns the result of the
		 * procedure.
		 * <p>
		 * In this procedure, the time spent in millisecond should be recorded.
		 * </p>
		 * 
		 * @return the time spent, {@code null} if failed
		 */
		protected abstract Image perform(Image image);

		/**
		 * Perform the processing.
		 */
		public void perform()
		{
			Image image = current();
			if (image == null)
				return;
			Image res = perform(image);
			if (res != null)
			{
				updateCommentByFunc(String.format("<%s> %s", title, content),
						time);
				recordAndPaint(res);
				updateCanvasTitle(res);
			}
		}
	}
}
