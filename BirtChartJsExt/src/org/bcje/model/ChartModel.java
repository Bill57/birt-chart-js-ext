package org.bcje.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 */

public class ChartModel {

	public static final String DIMENSION_2D = "2D"; //$NON-NLS-1$
	public static final String DIMENSION_2D_DEPTH = "2.5D"; //$NON-NLS-1$
	public static final String DIMENSION_3D = "3D"; //$NON-NLS-1$

	private final String type, dimension, format;
	private final double width, height;
	private final List<ChartCategory> categories = new ArrayList<ChartCategory>(
			4);
	private final List<ChartDataSet> datasets = new ArrayList<ChartDataSet>(4);
	private final List<String> seriesNames = new ArrayList<String>(1);
	private String title;
	private String script;
	private boolean stacked = false;
	private boolean colorByCategory = true;
	private boolean showLegend = true;
	private boolean showLabel = false;

	public ChartModel(String type, String dimension, String format,
			double width, double height) {
		this.type = type.toUpperCase();
		if (dimension == null) {
			this.dimension = DIMENSION_2D;
		} else {
			this.dimension = dimension.toUpperCase();
		}
		if (format == null) {
			this.format = "PNG";
		} else {
			this.format = format.toUpperCase();
		}
		this.width = width;
		this.height = height;
	}

	/**
	 * @param title
	 *            The title to set.
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return Returns the title.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return Returns the categories.
	 */
	public List<ChartCategory> getCategories() {
		return categories;
	}

	/**
	 * @return Returns the datasets.
	 */
	public List<ChartDataSet> getDatasets() {
		return datasets;
	}

	/**
	 * @return the seriesNames
	 */
	public List<String> getSeriesNames() {
		return seriesNames;
	}

	/**
	 * @return Returns the type.
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return Returns the dimension.
	 */
	public String getDimension() {
		return dimension;
	}

	/**
	 * @return Returns the width.
	 */
	public double getWidth() {
		return width;
	}

	/**
	 * @return Returns the height.
	 */
	public double getHeight() {
		return height;
	}

	/**
	 * @return Returns the format.
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * @param script
	 *            The script to set.
	 */
	public void setScript(String script) {
		this.script = script;
	}

	/**
	 * @return Returns the script.
	 */
	public String getScript() {
		return script;
	}

	/**
	 * Stacks the multiple data if possible. The charts supporting the stacked
	 * state include: Bar, Tube, Pyramid, Cone, Line, Area.
	 * 
	 * @param stacked
	 *            The stacked to set.
	 */
	public void setStacked(boolean stacked) {
		this.stacked = stacked;
	}

	/**
	 * @return Returns the stacked.
	 */
	public boolean isStacked() {
		return stacked;
	}

	/**
	 * @param colorByCategory
	 *            The colorByCategory to set.
	 */
	public void setColorByCategory(boolean colorByCategory) {
		this.colorByCategory = colorByCategory;
	}

	/**
	 * @return Returns the colorByCategory.
	 */
	public boolean isColorByCategory() {
		return colorByCategory;
	}

	/**
	 * @param showLegend
	 *            The showLegend to set.
	 */
	public void setShowLegend(boolean showLegend) {
		this.showLegend = showLegend;
	}

	/**
	 * @return Returns the showLegend.
	 */
	public boolean isShowLegend() {
		return showLegend;
	}

	/**
	 * @param showLabel
	 *            The showLabel to set.
	 */
	public void setShowLabel(boolean showLabel) {
		this.showLabel = showLabel;
	}

	/**
	 * @return Returns the showLabel.
	 */
	public boolean isShowLabel() {
		return showLabel;
	}
}
