
package org.bcje.model;

/**
 * 
 */

public class ChartDataSet
{

	private final Object value;
	private String url;
	private String tooltip;

	public ChartDataSet( Object value )
	{
		this.value = value;
	}

	/**
	 * @return Returns the value.
	 */
	public Object getValue( )
	{
		return value;
	}

	public double getDoubleValue( )
	{
		return value instanceof Number ? ( (Number) value ).doubleValue( ) : 0;
	}

	/**
	 * @param url
	 *            The url to set.
	 */
	public void setUrl( String url )
	{
		this.url = url;
	}

	/**
	 * @return Returns the url.
	 */
	public String getUrl( )
	{
		return url;
	}

	/**
	 * @param tooltip
	 *            The tooltip to set.
	 */
	public void setTooltip( String tooltip )
	{
		this.tooltip = tooltip;
	}

	/**
	 * @return Returns the tooltip.
	 */
	public String getTooltip( )
	{
		return tooltip;
	}
}
