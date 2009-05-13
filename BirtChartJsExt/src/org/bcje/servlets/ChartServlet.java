
package org.bcje.servlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bcje.model.ChartModel;
import org.bcje.utils.ChartConvertor;
import org.bcje.utils.ChartXMLParser;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.viewer.internal.util.ChartImageManager;
import org.eclipse.birt.chart.viewer.internal.util.ImageHTMLEmitter;

/**
 * 
 */

public class ChartServlet extends HttpServlet
{

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = -970069279731222139L;
	private static ILogger logger = Logger.getLogger( "BirtChartJsExt" ); //$NON-NLS-1$


	private ServletContext context;

	public void init( ServletConfig config ) throws ServletException
	{
		super.init( config );
		this.context = config.getServletContext( );

	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}

	public void doGet( HttpServletRequest request, HttpServletResponse response )
			throws IOException, ServletException
	{
		String html = null;
		String xml = request.getParameter( "dataXML" ); //$NON-NLS-1$
		String url = request.getParameter( "dataURL" ); //$NON-NLS-1$
		if ( url != null )
		{
			xml = loadXML( url );
		}
		logger.log(ILogger.INFORMATION, xml);
		ChartModel chart = null;

		try
		{
			Chart cm = null;
			if ( xml != null )
			{
				chart = new ChartXMLParser( xml ).createChart( );
				cm = ChartConvertor.convertToBIRTChart( chart );
			}
			if ( cm != null )
			{
				ChartImageManager imageManager = new ChartImageManager( request,
						cm,
						chart.getFormat( ),
						ChartConvertor.convertToEvaluator( chart ),
						null,
						null,
						null );
				File imageFile = imageManager.getImage( );
				String imageId = imageFile.getName( );
				imageId = imageId.substring( 0, imageId.lastIndexOf( '.' ) );

				html = createEmitter( chart,
						imageId,
						imageManager.getRelativeImageFolder( )
								+ "/" + imageFile.getName( ), imageManager.getImageMap( ) ).generateHTML( ); //$NON-NLS-1$
			}
			else
			{
				html = xml.replaceAll( "<", "&lt;" ).replace( ">", "&gt;" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$//$NON-NLS-4$
				html = "<b>Invalid XML:</b><br/>" + html; //$NON-NLS-1$
			}
		}
		catch ( Exception e )
		{
			e.printStackTrace( );
			html = e.getMessage( );
		}
		response.setContentType( "text/xml" ); //$NON-NLS-1$
		response.setHeader( "Cache-Control", "no-cache" ); //$NON-NLS-1$ //$NON-NLS-2$
		response.getWriter( ).write( "<chart><![CDATA[" + html + "]]></chart>" ); //$NON-NLS-1$ //$NON-NLS-2$
	}

	private ImageHTMLEmitter createEmitter( ChartModel chart, String id,
			String src, String imageMap )
	{
		ImageHTMLEmitter emitter = new ImageHTMLEmitter( );
		emitter.ext = chart.getFormat( );
		emitter.height = (int) chart.getHeight( );
		emitter.width = (int) chart.getWidth( );
		emitter.id = id;
		emitter.src = src;
		emitter.imageMap = imageMap;
		return emitter;
	}

	private String loadXML( String strUrl )
	{
		StringBuffer xml = new StringBuffer( );
		try
		{
			InputStream is;
			if ( strUrl.startsWith( "http:" ) ) //$NON-NLS-1$
			{
				URL url = new URL( strUrl );
				is = url.openStream( );
			}
			else
			{
				File file = new File( strUrl );
				if ( file.exists( ) )
				{
					is = new FileInputStream( file );
				}
				else
				{
					is = context.getResourceAsStream( strUrl );
				}
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
		return xml.toString( );
	}
}
