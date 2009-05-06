
package applets;

import java.applet.Applet;
import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

import javax.swing.JFrame;

import org.bcje.model.ChartModel;
import org.bcje.utils.ChartConvertor;
import org.bcje.utils.ChartXMLParser;
import org.eclipse.birt.chart.api.ChartEngine;
import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.device.IUpdateNotifier;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.GeneratedChartState;
import org.eclipse.birt.chart.factory.Generator;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.util.SecurityUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.PlatformConfig;

public class ChartApplet extends Applet
{

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = -2328575139648049465L;
	static final String DEFAULT_XML = "xmls/3DBarChart.xml";
	// static final String DEFAULT_XML =
	// "http://birt-chart-js-ext.googlecode.com/svn/trunk/BirtChartJsExt/WebContent/xmls/3DBarChart.xml";
	ChartCanvas canvas; // The drawing area to display arcs

	public void init( )
	{
		setLayout( new BorderLayout( ) );
		canvas = new ChartCanvas( );
		String dataXML = getParameter( "dataXML" );
		if ( dataXML == null )
		{
			dataXML = DEFAULT_XML;
		}
		canvas.setDataXML( loadXML( dataXML ) );

		add( BorderLayout.CENTER, canvas );
	}

	public void destroy( )
	{
		remove( canvas );
	}

	public void start( )
	{

	}

	public void stop( )
	{

	}

	public void processEvent( AWTEvent e )
	{
		if ( e.getID( ) == Event.WINDOW_DESTROY )
		{
			System.exit( 0 );
		}
	}

	public static void main( String args[] )
	{
		JFrame jf = new JFrame( "BIRT Chart applet test" );
		jf.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		ChartApplet applet = new ChartApplet( );

		applet.init( );
		applet.start( );

		jf.add( BorderLayout.CENTER, applet );
		jf.setSize( 500, 500 );
		jf.show( );
	}

	private String loadXML( String strUrl )
	{
		StringBuffer xml = new StringBuffer( );
		try
		{
			InputStream is = null;
			if ( strUrl.startsWith( "http:" ) ) //$NON-NLS-1$
			{
				final URL url = SecurityUtil.newURL( strUrl );
				try
				{
					is = AccessController.doPrivileged( new PrivilegedExceptionAction<InputStream>( ) {

						public InputStream run( ) throws IOException
						{
							return url.openStream( );
						}

					} );
				}
				catch ( PrivilegedActionException e )
				{
					e.printStackTrace( );
				}

			}
			else
			{
				is = new URL( getDocumentBase( ), strUrl ).openStream( );
			}
			if ( is != null )
			{
				byte[] buffer = new byte[1024];
				int readSize = 0;
				while ( ( readSize = is.read( buffer ) ) != -1 )
				{
					xml.append( new String( buffer, 0, readSize ) );
				}
				is.close( );
			}
		}
		catch ( IOException e )
		{
			e.printStackTrace( );
		}
		catch ( IllegalArgumentException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace( );
		}
		// catch ( URISyntaxException e )
		// {
		// // TODO Auto-generated catch block
		// e.printStackTrace( );
		// }
		return xml.toString( );
	}

	public String getAppletInfo( )
	{
		return "An interactive test of the Graphics.drawArc and \nGraphics.fillArc routines. Can be run \neither as a standalone application by typing 'java ArcTest' \nor as an applet in the AppletViewer.";
	}

	class ChartCanvas extends Canvas implements IUpdateNotifier
	{

		/**
		 * Comment for <code>serialVersionUID</code>
		 */
		private static final long serialVersionUID = -2250867230327136759L;
		Chart cm = null;
		boolean bNeedsGeneration = true;
		private GeneratedChartState gcs;
		private String dataXML;

		public void setDataXML( String data )
		{
			this.dataXML = data;
		}

		public void paint( Graphics g )
		{
			Graphics2D g2d = (Graphics2D) g;
			try
			{
				if ( dataXML == null )
				{
					throw new IllegalArgumentException( "XML for Chart model is required." );
				}
				ChartModel chart = new ChartXMLParser( this.dataXML ).createChart( );
				this.cm = ChartConvertor.convertToBIRTChart( chart );
				// this.cm = SampleHelper.createSampleChart();

				PlatformConfig config = new PlatformConfig( );
				config.setProperty( "STANDALONE", "true" ); //$NON-NLS-1$ //$NON-NLS-2$
				IDeviceRenderer idr = ChartEngine.instance( config )
						.getRenderer( "dv.SWING" );//$NON-NLS-1$

				idr.setProperty( IDeviceRenderer.GRAPHICS_CONTEXT, g2d );
				idr.setProperty( IDeviceRenderer.UPDATE_NOTIFIER, this );

				Dimension d = getSize( );
				Bounds bo = BoundsImpl.create( 0, 0, d.width, d.height );
				bo.scale( 72d / idr.getDisplayServer( ).getDpiResolution( ) );

				Generator gr = Generator.instance( );
				RunTimeContext rtc = new RunTimeContext( );
				gr.bindData( ChartConvertor.convertToEvaluator( chart ),
						cm,
						rtc );
				gcs = gr.build( idr.getDisplayServer( ),
						cm,
						bo,
						null,
						rtc,
						null );
				gr.render( idr, gcs );
			}
			catch ( ChartException ex )
			{
				showException( g2d, ex );
			}
			catch ( SecurityException e )
			{
				showException( g2d, e );
			}

		}

		private final void showException( Graphics2D g2d, Exception ex )
		{
			String sWrappedException = ex.getClass( ).getName( );
			Throwable th = ex;
			while ( ex.getCause( ) != null )
			{
				ex = (Exception) ex.getCause( );
			}
			String sException = ex.getClass( ).getName( );
			if ( sWrappedException.equals( sException ) )
			{
				sWrappedException = null;
			}

			String sMessage = null;
			if ( th instanceof BirtException )
			{
				sMessage = ( (BirtException) th ).getLocalizedMessage( );
			}
			else
			{
				sMessage = ex.getMessage( );
			}

			if ( sMessage == null )
			{
				sMessage = "<null>";//$NON-NLS-1$
			}

			StackTraceElement[] stea = ex.getStackTrace( );
			Dimension d = getSize( );

			Font fo = new Font( "Monospaced", Font.BOLD, 14 );//$NON-NLS-1$
			g2d.setFont( fo );
			FontMetrics fm = g2d.getFontMetrics( );
			g2d.setColor( Color.WHITE );
			g2d.fillRect( 20, 20, d.width - 40, d.height - 40 );
			g2d.setColor( Color.BLACK );
			g2d.drawRect( 20, 20, d.width - 40, d.height - 40 );
			g2d.setClip( 20, 20, d.width - 40, d.height - 40 );
			int x = 25, y = 20 + fm.getHeight( );
			g2d.drawString( "Exception:", x, y );//$NON-NLS-1$
			x += fm.stringWidth( "Exception:" ) + 5;//$NON-NLS-1$
			g2d.setColor( Color.RED );
			g2d.drawString( sException, x, y );
			x = 25;
			y += fm.getHeight( );
			if ( sWrappedException != null )
			{
				g2d.setColor( Color.BLACK );
				g2d.drawString( "Wrapped In:", x, y );//$NON-NLS-1$
				x += fm.stringWidth( "Wrapped In:" ) + 5;//$NON-NLS-1$
				g2d.setColor( Color.RED );
				g2d.drawString( sWrappedException, x, y );
				x = 25;
				y += fm.getHeight( );
			}
			g2d.setColor( Color.BLACK );
			y += 10;
			g2d.drawString( "Message:", x, y );//$NON-NLS-1$
			x += fm.stringWidth( "Message:" ) + 5;//$NON-NLS-1$
			g2d.setColor( Color.BLUE );
			g2d.drawString( sMessage, x, y );
			x = 25;
			y += fm.getHeight( );
			g2d.setColor( Color.BLACK );
			y += 10;
			g2d.drawString( "Trace:", x, y );//$NON-NLS-1$
			x = 40;
			y += fm.getHeight( );
			g2d.setColor( Color.GREEN.darker( ) );
			for ( int i = 0; i < stea.length; i++ )
			{
				g2d.drawString( stea[i].getClassName( ) + ":"//$NON-NLS-1$
						+ stea[i].getMethodName( )
						+ "(...):"//$NON-NLS-1$
						+ stea[i].getLineNumber( ), x, y );
				x = 40;
				y += fm.getHeight( );
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.chart.device.swing.IUpdateNotifier#update()
		 */
		public void regenerateChart( )
		{
			bNeedsGeneration = true;
			repaint( );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.chart.device.swing.IUpdateNotifier#update()
		 */
		public void repaintChart( )
		{
			repaint( );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.birt.chart.device.swing.IUpdateNotifier#peerInstance()
		 */
		public Object peerInstance( )
		{
			return this;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.birt.chart.device.swing.IUpdateNotifier#getDesignTimeModel
		 * ()
		 */
		public Chart getDesignTimeModel( )
		{
			return cm;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.birt.chart.device.swing.IUpdateNotifier#getRunTimeModel()
		 */
		public Chart getRunTimeModel( )
		{
			return gcs.getChartModel( );
		}
	}
}
