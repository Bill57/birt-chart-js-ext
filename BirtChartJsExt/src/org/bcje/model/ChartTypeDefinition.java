
package org.bcje.model;

import org.eclipse.birt.chart.model.attribute.RiserType;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.type.BarSeries;
import org.eclipse.birt.chart.model.type.PieSeries;
import org.eclipse.birt.chart.model.type.impl.AreaSeriesImpl;
import org.eclipse.birt.chart.model.type.impl.BarSeriesImpl;
import org.eclipse.birt.chart.model.type.impl.LineSeriesImpl;
import org.eclipse.birt.chart.model.type.impl.PieSeriesImpl;
import org.eclipse.birt.chart.model.type.impl.ScatterSeriesImpl;

/**
 * 
 */

public enum ChartTypeDefinition {
	BAR {

		@Override
		public String getName( )
		{
			return "BAR";
		}

		@Override
		public Series createSeries( )
		{
			return BarSeriesImpl.create( );
		}

	},

	PYRAMID {

		@Override
		public String getName( )
		{
			return "PYRAMID";
		}

		@Override
		public Series createSeries( )
		{
			BarSeries series = (BarSeries) BarSeriesImpl.create( );
			series.setRiser( RiserType.TRIANGLE_LITERAL );
			return series;
		}

	},

	TUBE {

		@Override
		public String getName( )
		{
			return "TUBE";
		}

		@Override
		public Series createSeries( )
		{
			BarSeries series = (BarSeries) BarSeriesImpl.create( );
			series.setRiser( RiserType.TUBE_LITERAL );
			return series;
		}

	},

	CONE {

		@Override
		public String getName( )
		{
			return "CONE";
		}

		@Override
		public Series createSeries( )
		{
			BarSeries series = (BarSeries) BarSeriesImpl.create( );
			series.setRiser( RiserType.CONE_LITERAL );
			return series;
		}

	},

	LINE {

		@Override
		public String getName( )
		{
			return "LINE";
		}

		@Override
		public Series createSeries( )
		{
			return LineSeriesImpl.create( );
		}

	},

	AREA {

		@Override
		public String getName( )
		{
			return "AREA";
		}

		@Override
		public Series createSeries( )
		{
			return AreaSeriesImpl.create( );
		}

	},

	SCATTER {

		@Override
		public String getName( )
		{
			return "SCATTER";
		}

		@Override
		public Series createSeries( )
		{
			return ScatterSeriesImpl.create( );
		}

	},

	PIE {

		@Override
		public String getName( )
		{
			return "PIE";
		}

		@Override
		public Series createSeries( )
		{
			PieSeries series = (PieSeries) PieSeriesImpl.create( );
			series.setExplosion( 5 );
			return series;
		}

		@Override
		public boolean isChartWithAxes( )
		{
			return false;
		}

	};

	public abstract String getName( );

	public abstract Series createSeries( );

	public boolean isChartWithAxes( )
	{
		return true;
	}

	public static ChartTypeDefinition getByName( String name )
	{
		for ( ChartTypeDefinition typeDefinition : ChartTypeDefinition.values( ) )
		{
			if ( typeDefinition.getName( ).equals( name ) )
			{
				return typeDefinition;
			}
		}
		return null;
	}
}
