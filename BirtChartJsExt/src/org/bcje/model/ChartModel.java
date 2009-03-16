
package org.bcje.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 */

public class ChartModel
{

	public static final String DIMENSION_2D = "2D"; //$NON-NLS-1$
	public static final String DIMENSION_2D_DEPTH = "2.5D"; //$NON-NLS-1$
	public static final String DIMENSION_3D = "3D"; //$NON-NLS-1$

	private final String type, dimension, format;
	private final double width, height;
	private final List<ChartCategory> categories = new ArrayList<ChartCategory>( 4 );
	private final List<ChartDataSet> datasets = new ArrayList<ChartDataSet>( 4 );
	private String title;
	private String script;

	public ChartModel( String type, String dimension, String format,
			double width, double height )
	{
		this.type = type.toUpperCase( );
		this.dimension = dimension.toUpperCase( );
		this.format = format.toUpperCase( );
		this.width = width;
		this.height = height;
	}

	/**
	 * @param title
	 *            The title to set.
	 */
	public void setTitle( String title )
	{
		this.title = title;
	}

	/**
	 * @return Returns the title.
	 */
	public String getTitle( )
	{
		return title;
	}

	/**
	 * @return Returns the categories.
	 */
	public List<ChartCategory> getCategories( )
	{
		return categories;
	}

	/**
	 * @return Returns the datasets.
	 */
	public List<ChartDataSet> getDatasets( )
	{
		return datasets;
	}

	/**
	 * @return Returns the type.
	 */
	public String getType( )
	{
		return type;
	}

	/**
	 * @return Returns the dimension.
	 */
	public String getDimension( )
	{
		return dimension;
	}

	/**
	 * @return Returns the width.
	 */
	public double getWidth( )
	{
		return width;
	}

	/**
	 * @return Returns the height.
	 */
	public double getHeight( )
	{
		return height;
	}

	/**
	 * @return Returns the format.
	 */
	public String getFormat( )
	{
		return format;
	}

	/**
	 * @param script
	 *            The script to set.
	 */
	public void setScript( String script )
	{
		this.script = script;
	}

	/**
	 * @return Returns the script.
	 */
	public String getScript( )
	{
		return script;
	}
}
