package com.frank.dip.demo;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import com.frank.dip.ColorImage;
import com.frank.dip.Image;
import com.frank.dip.ImageUtils;
import com.frank.dip.demo.comp.MenuAnalyze;
import com.frank.dip.demo.comp.MenuEnhance;
import com.frank.dip.demo.comp.MenuExperiment;
import com.frank.dip.demo.comp.MenuFile;
import com.frank.dip.demo.comp.MenuFilter;
import com.frank.dip.demo.comp.MenuGeometry;
import com.frank.dip.demo.comp.MenuMorph;
import com.frank.dip.demo.comp.MenuThreshold;
import com.frank.dip.enhance.convolver.Convolver;
import com.frank.dip.enhance.convolver.GaussianBlurImprovedOperator;
import com.frank.dip.enhance.convolver.GaussianBlurKernel;
import com.frank.dip.geom.Geometry;
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
			public void mouseEntered(MouseEvent e)
			{
				canvas.setCursor(Cursor
						.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e)
			{
				updateMouseMotion(null);
				canvas.setCursor(Cursor.getDefaultCursor());
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
	 * Test method.
	 */
	private void test()
	{
		new Performance("Test", "Test Method")
		{
			@SuppressWarnings("unused")
			Image separate(Image image, double sigma)
			{
				GaussianBlurImprovedOperator op = new GaussianBlurImprovedOperator(
						sigma);
				return op.operate(image);
			}

			@SuppressWarnings("unused")
			Image scale(Image image, double sigma, double theta)
			{
				Geometry geom = Geometry.getGeometry(image,
						Geometry.TYPE_BICUBIC, Geometry.FILL_WITH_BLANK);
				Convolver conv = new Convolver(new GaussianBlurKernel(7, 7,
						(float) sigma), Convolver.HINT_ACCURACY_INTERRUPT,
						Convolver.HINT_EDGE_SOURCE);
				Image down = geom.scaleByRate(image, image.width() * theta,
						image.height() * theta);
				down = conv.operate(down);
				return geom.scale(down, image.width(), image.height());
			}

			Image norm(Image image, double sigma)
			{
				Convolver conv = new Convolver(new GaussianBlurKernel(7, 7,
						(float) sigma), Convolver.HINT_ACCURACY_INTERRUPT,
						Convolver.HINT_EDGE_SOURCE);
				return conv.operate(image);
			}

			@Override
			protected Image perform(Image image)
			{
				String sigmaStr = SwingUtils.inputDialog(DIPFrame.this,
						"SIGMA", "Input sigma:", "2.0");
				if (sigmaStr == null)
					return null;
				double sigma = Double.valueOf(sigmaStr);
				return norm(image, sigma);
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
	public Image current()
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

	/**
	 * Initialize the menu bar.
	 */
	private void createMenu()
	{
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		new MenuFile(this);
		menuEdit();
		new MenuAnalyze(this);
		new MenuEnhance(this);
		new MenuThreshold(this);
		new MenuFilter(this);
		new MenuMorph(this);
		new MenuGeometry(this);
		new MenuExperiment(this);
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
		int controlKey = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
		mntmUndo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z,
				controlKey));
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
				controlKey));
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
				controlKey));
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
				controlKey));
		mnEdit.add(mntmPaste);
		updateStep();
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
		else
			lblPosition.setText("");
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
		Image image;
		long time;
		Timer t = TestUtils.getTimer();
		t.start();
		try
		{
			image = com.frank.dip.io.ImageIO.read(file);
			time = t.getTime(timeunit);
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
		recordAndPaint(image);
		updateCanvasTitle(image);
		updateCommentByFunc("<Edit> Open Image", time);
	}

	/**
	 * Open image from file system.
	 */
	public void open()
	{
		File file = SwingUtils.selectLoadFile(DIPFrame.this, "Open Image File");
		if (file != null)
			openImage(file);
	}

	/**
	 * Save the current image to the specified path. The specified path will be
	 * selected by the user.
	 */
	public void save()
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
	 * The digital image processing performance.
	 * <p>
	 * </p>
	 * 
	 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
	 * @version 1.0.0
	 */
	public abstract class Performance
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
			Image res = null;
			try
			{
				res = perform(image);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				SwingUtils.errorMessage(DIPFrame.this, e.getLocalizedMessage());
			}
			if (res != null)
			{
				updateCommentByFunc(String.format("<%s> %s", title, content),
						time);
				recordAndPaint(res);
				updateCanvasTitle(res);
			}
		}

		/**
		 * Returns the constraints string of the specified parameter.
		 * 
		 * @param paramName
		 * @param isMaxValue
		 * @return the constraints string
		 */
		protected String rs(String paramName, boolean isMaxValue)
		{
			return String.format(isMaxValue ? "%s_max" : "%s_min", paramName);//$NON-NLS-1$//$NON-NLS-2$
		}
	}

	/**
	 * Returns the current menu bar.
	 * 
	 * @return the current menu bar
	 */
	public JMenuBar getBar()
	{
		return menuBar;
	}

	/**
	 * Returns the filename of the current image.
	 * 
	 * @return the filename of the current image.
	 */
	public String getFilename()
	{
		return filename;
	}

	/**
	 * Add a new window to the windows manager list.
	 * 
	 * @param w
	 *            the new window to add
	 */
	public void addWindow(Window w)
	{
		wins.add(w);
	}
}
