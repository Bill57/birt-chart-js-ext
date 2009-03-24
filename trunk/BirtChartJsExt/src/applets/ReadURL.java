
package applets;

import java.applet.Applet;
import java.awt.Graphics;
import java.io.DataInputStream;
import java.io.InputStream;
import java.net.URL;

/**
 * 
 * This applet is used to read content from file specified by URL
 */
public class ReadURL extends Applet implements Runnable
{

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 2511821048048551820L;
	public String fileURL;
	public String message;
	public String fileContent;
	public int finished;
	public int nOfReads;
	public int nOfLines;
	InputStream is;
	DataInputStream dis;
	Thread myThread;

	public void init( )
	{
		fileURL = null;
		message = "";
		fileContent = "";
		finished = 0;
		nOfReads = 0;
		nOfLines = 0;
		fileURL = getParameter( "fileURL" );
		if ( fileURL == null )
		{
			finished = 1;
		}
		myThread = new Thread( this );
	}

	public void run( )
	{
		do
		{
			if ( finished == 0 )
			{
				try
				{
					Thread.currentThread( ).setPriority( 1 );
					is = new URL( getDocumentBase( ), fileURL ).openStream( );
					dis = new DataInputStream( is );
					String s = "";
					fileContent = "";
					nOfLines = 0;
					do
					{
						String s1 = dis.readLine( );
						if ( s1 == null )
						{
							is.close( );
							break;
						}
						fileContent += s1 + "\r\n";
						nOfLines++;
					} while ( true );
				}
				catch ( Exception exception )
				{
					message = exception.toString( );
					repaint( );
				}
				nOfReads++;
				finished = 1;
			}
			try
			{
				Thread.sleep( 10L );
			}
			catch ( InterruptedException interruptedexception )
			{
				message = interruptedexception.toString( );
				repaint( );
			}
		} while ( true );
	}

	public void start( )
	{
		if ( myThread != null )
		{
			myThread.start( );
		}
	}

	public int readFile( String s )
	{
		if ( finished == 0 )
		{
			return 0;
		}
		else
		{
			fileURL = s;
			finished = 0;
			return 1;
		}
	}

	public void stop( )
	{
		if ( myThread != null )
		{
			myThread.stop( );
		}
	}

	public void paint( Graphics g )
	{
		g.drawString( message, 20, 30 );
	}

}
