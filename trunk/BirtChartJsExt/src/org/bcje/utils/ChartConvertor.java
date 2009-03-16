
package org.bcje.utils;

import java.util.List;

import org.bcje.model.ChartDataSet;
import org.bcje.model.ChartModel;
import org.bcje.model.ChartTypeDefinition;
import org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.ActionType;
import org.eclipse.birt.chart.model.attribute.Angle3D;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.TriggerCondition;
import org.eclipse.birt.chart.model.attribute.URLValue;
import org.eclipse.birt.chart.model.attribute.impl.Angle3DImpl;
import org.eclipse.birt.chart.model.attribute.impl.Rotation3DImpl;
import org.eclipse.birt.chart.model.attribute.impl.TooltipValueImpl;
import org.eclipse.birt.chart.model.attribute.impl.URLValueImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.AxisImpl;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.Trigger;
import org.eclipse.birt.chart.model.data.impl.ActionImpl;
import org.eclipse.birt.chart.model.data.impl.NumberDataElementImpl;
import org.eclipse.birt.chart.model.data.impl.QueryImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.data.impl.TriggerImpl;
import org.eclipse.birt.chart.model.impl.ChartWithAxesImpl;
import org.eclipse.birt.chart.model.impl.ChartWithoutAxesImpl;

/**
 * 
 */

public class ChartConvertor
{

	private static final String CATEGORY_EXPR = "category"; //$NON-NLS-1$
	private static final String VALUE_EXPR = "value"; //$NON-NLS-1$
	private static final String TOOLTIP_EXPR = "tooltip"; //$NON-NLS-1$
	private static final String URL_EXPR = "url"; //$NON-NLS-1$

	public static Chart convertToBIRTChart( ChartModel chart )
	{
		ChartTypeDefinition type = ChartTypeDefinition.getByName( chart.getType( ) );
		if ( type == null )
		{
			return null;
		}

		Chart cm = type.isChartWithAxes( ) ? ChartWithAxesImpl.create( )
				: ChartWithoutAxesImpl.create( );

		cm.getLegend( ).setItemType( LegendItemType.CATEGORIES_LITERAL );

		if ( chart.getDimension( ).equals( ChartModel.DIMENSION_2D_DEPTH ) )
		{
			cm.setDimension( ChartDimension.TWO_DIMENSIONAL_WITH_DEPTH_LITERAL );
		}
		else if ( chart.getDimension( ).equals( ChartModel.DIMENSION_3D ) )
		{
			cm.setDimension( ChartDimension.THREE_DIMENSIONAL_LITERAL );
		}

		if ( chart.getTitle( ) != null )
		{
			cm.getTitle( )
					.getLabel( )
					.getCaption( )
					.setValue( chart.getTitle( ) );
		}
		else
		{
			cm.getTitle( ).getLabel( ).setVisible( false );
		}

		if ( chart.getScript( ) != null )
		{
			cm.setScript( chart.getScript( ) );
		}

		cm.getLegend( ).setVisible( chart.isShowLegend( ) );
		cm.getLegend( )
				.setItemType( chart.isColorByCategory( ) ? LegendItemType.CATEGORIES_LITERAL
						: LegendItemType.SERIES_LITERAL );

		// Add X series
		Series seCategory = SeriesImpl.create( );
		seCategory.getDataDefinition( ).add( QueryImpl.create( CATEGORY_EXPR ) );
		SeriesDefinition categorySD = SeriesDefinitionImpl.create( );
		categorySD.getSeries( ).add( seCategory );
		if ( cm instanceof ChartWithAxes )
		{
			Axis xAxis = ( (ChartWithAxes) cm ).getAxes( ).get( 0 );
			xAxis.getSeriesDefinitions( ).add( categorySD );
		}
		else
		{
			( (ChartWithoutAxes) cm ).getSeriesDefinitions( ).add( categorySD );
		}
		categorySD.getSeriesPalette( ).shift( 0 );

		// Add Y series
		final int seriesCount = chart.getDatasets( ).size( )
				/ chart.getCategories( ).size( );
		for ( int i = 0; i < seriesCount; i++ )
		{
			SeriesDefinition orthSD = SeriesDefinitionImpl.create( );
			Series seValue;
			if ( cm instanceof ChartWithAxes )
			{
				seValue = type.createSeries( );
				Axis xAxis = ( (ChartWithAxes) cm ).getAxes( ).get( 0 );
				xAxis.getAssociatedAxes( )
						.get( 0 )
						.getSeriesDefinitions( )
						.add( orthSD );
			}
			else
			{
				seValue = type.createSeries( );
				categorySD.getSeriesDefinitions( ).add( orthSD );
			}
			orthSD.getSeries( ).add( seValue );
			orthSD.getSeriesPalette( ).shift( -i );
			seValue.getDataDefinition( )
					.add( QueryImpl.create( VALUE_EXPR + i ) );
			seValue.setStacked( chart.isStacked( ) );
			seValue.getLabel( ).setVisible( chart.isShowLabel( ) );

			List<Trigger> triggers = orthSD.getSeries( ).get( 0 ).getTriggers( );
			if ( chart.getDatasets( ).size( ) > 0 )
			{
				ChartDataSet dataset = chart.getDatasets( ).get( 0 );
				if ( dataset.getUrl( ) != null )
				{
					URLValue uv = URLValueImpl.create( URL_EXPR + i,
							"_blank",
							null,
							null,
							null );
					triggers.add( TriggerImpl.create( TriggerCondition.ONCLICK_LITERAL,
							ActionImpl.create( ActionType.URL_REDIRECT_LITERAL,
									uv ) ) );
				}
				// if ( dataset.getTooltip( ) != null )
				{
					triggers.add( TriggerImpl.create( TriggerCondition.ONMOUSEOVER_LITERAL,
							ActionImpl.create( ActionType.SHOW_TOOLTIP_LITERAL,
									TooltipValueImpl.create( 0, TOOLTIP_EXPR
											+ i ) ) ) );
				}
			}
		}

		// Add Z axis for 3d chart
		if ( cm instanceof ChartWithAxes
				&& cm.getDimension( ) == ChartDimension.THREE_DIMENSIONAL_LITERAL )
		{
			( (ChartWithAxes) cm ).setRotation( Rotation3DImpl.create( new Angle3D[]{
				Angle3DImpl.create( -20, 45, 0 )
			} ) );

			( (ChartWithAxes) cm ).getPrimaryBaseAxes( )[0].getAncillaryAxes( )
					.clear( );

			Axis zAxisAncillary = AxisImpl.create( Axis.ANCILLARY_BASE );
			zAxisAncillary.setTitlePosition( Position.BELOW_LITERAL );
			zAxisAncillary.getTitle( ).setVisible( true );
			zAxisAncillary.setPrimaryAxis( true );
			zAxisAncillary.setLabelPosition( Position.BELOW_LITERAL );
			zAxisAncillary.setOrientation( Orientation.HORIZONTAL_LITERAL );
			zAxisAncillary.getOrigin( ).setType( IntersectionType.MIN_LITERAL );
			zAxisAncillary.getOrigin( )
					.setValue( NumberDataElementImpl.create( 0 ) );
			zAxisAncillary.getTitle( ).setVisible( false );
			zAxisAncillary.setType( AxisType.TEXT_LITERAL );
			( (ChartWithAxes) cm ).getPrimaryBaseAxes( )[0].getAncillaryAxes( )
					.add( zAxisAncillary );
		}
		return cm;
	}

	public static IDataRowExpressionEvaluator convertToEvaluator(
			final ChartModel cm )
	{
		final int count = cm.getCategories( ).size( );
		IDataRowExpressionEvaluator evaluator = new IDataRowExpressionEvaluator( ) {

			private int index = 0;

			public void close( )
			{
				// TODO Auto-generated method stub

			}

			public Object evaluate( String arg0 )
			{
				if ( CATEGORY_EXPR.equals( arg0 ) )
				{
					return cm.getCategories( ).get( index - 1 ).getLabel( );
				}
				if ( arg0.startsWith( VALUE_EXPR ) )
				{
					return cm.getDatasets( ).get( getIndex( arg0,
							VALUE_EXPR,
							index - 1 ) ).getValue( );
				}
				if ( arg0.startsWith( TOOLTIP_EXPR ) )
				{
					String tooltip = cm.getDatasets( ).get( getIndex( arg0,
							TOOLTIP_EXPR,
							index - 1 ) ).getTooltip( );
					return tooltip == null ? "" : tooltip;
				}
				if ( arg0.startsWith( URL_EXPR ) )
				{
					return cm.getDatasets( ).get( getIndex( arg0,
							URL_EXPR,
							index - 1 ) ).getUrl( );
				}
				return null;
			}

			private int getIndex( String expr, String prefix, int index )
			{
				return Integer.parseInt( expr.substring( prefix.length( ) ) )
						* count
						+ index;
			}

			public Object evaluateGlobal( String arg0 )
			{
				return evaluate( arg0 );
			}

			public boolean first( )
			{
				return next( );
			}

			public boolean next( )
			{
				return index++ < count;
			}
		};
		return evaluator;
	}
}
