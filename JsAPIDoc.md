# Introduction #

This page lists the samples and documentations of all available js APIs and XML file.


# Samples #
There are two approaches to generate chart. They are API approach and URL approach. No matter which approach is used, there must be a div object in HTML body to display the chart image. In sample code, we give the name "chartDiv" here.
## API approach ##
This approach uses pure javascript to generate chart.
### Javascript code ###
```
var cm = new ChartModel (400,300);
cm.setType('tube');
cm.setFormat('svg');
cm.setDimension('2.5d');
cm.setStacked(false);
cm.setColorByCategory(true);
cm.setShowLabel(true);
cm.setShowLegend(true);
cm.setTitle('World Population');
cm.setCategories('China,EU,USA');
cm.setValues('1300000000,728000000,300000000');
	
var chart = new BirtChart();
chart.setDataXML(cm);
chart.render("chartDiv");
```
## URL approach ##
In URL approach, there must be a XML file to store the chart properties.
### Javascript code ###
```
var chart = new BirtChart();
chart.setDataURL("xmls/WorldPopulation.xml");
chart.render("chartDiv");
```
### XML content ###
```
<chart type='bar' title='3D Bar Chart' dimension='3d' format='png' width='500' height='400' showLegend="true" >
<categories>
<category label='Australia'/>
<category label='China'/>
<category label='USA'/>
</categories>
<dataset name="Population">
<set value='250' />
<set value='800' />
<set value='500' />
</dataset>
<dataset name="Area">
<set value='300' />
<set value='400' />
<set value='600' />
</dataset>
</chart>
```
# JS API Documentation #
## ChartModel ##
This class stores the chart properties, and is only useful for API approach.
### Constructor ###
|**Attribute name**|**Type**|**Description**|
|:-----------------|:-------|:--------------|
|Width             |double  |width of chart image in pixels|
|Height            |double  |height of chart image in pixels|

### Methods ###
|**Method name**|**Attribute type**|Default value|**Range**|**Description**|
|:--------------|:-----------------|:------------|:--------|:--------------|
|setFormat      |string            |PNG          |SVG,PNG,JPG,BMP,GIF|Set the output format of chart image|
|setType        |string            |BAR          |BAR,PYRAMID,CONE,TUBE,LINE,AREA,SCATTER,PIE|Set the chart type|
|setDimension   |string            |2D           |2D,2.5D,3D|Set the chart dimension|
|setStacked     |boolean           |false        |         |Set the stacked state of multiple series|
|setColorByCategory|boolean           |true         |         |Set whether series color varies by category|
|setShowLegend  |boolean           |true         |         |Set whether legend is visible|
|setShowLabel   |boolean           |false        |         |Set whether series label is visible|
|setTitle       |string            |_optional_   |         |Set the chart title|
|setScript      |string            |_optional_   |         |Set the script executed in runtime|
|setCategories  |string or string array|             |         |Set the chart categories. If it's a string, it should be separated by comma.|
|setValues      |string or string array|             |         |Set the chart values. If it's a string, it should be separated by comma. The length of values can be equal or multiple to the length of categories.|
|setTooltips    |string or string array|_optional_   |         |Set the tooltips of data point. If it's a string, it should be separated by comma. The length of array must be equal with values.|
|setURLs        |string or string array|_optional_   |         |Set the hyperlink of data point. If it's a string, it should be separated by comma. The length of array must be equal with values.|
|setSeriesNames |string or string array|_optional_   |         |Set the series name of each series. If it's a string, it should be separated by comma. This only works when colorByCategory is false, otherwise the series name is category name. The length of array should be equal with values length divided by categories length.|
|getXML         |string            |             |         |Get the XML content of current chart model.|

## BirtChart ##
This class is used to generate chart image.

### Constructor ###
|**Attribute name**|**Type**|**Description**|
|:-----------------|:-------|:--------------|
|contextPath       |string  |optional. Defines the context path if web page is in another server.|

### Methods ###
|**Method name**|**Attribute type**|**Description**|
|:--------------|:-----------------|:--------------|
|setDataXML     |string or ChartModel|Set the data XML. If setDataURL is called later, this will do nothing.|
|setDataURL     |string            |Set the path of XML file. If setDataXML is called later, this will do nothing.|
|setStartTime   |long              |Optional. Set the milliseconds of time. If this is set, will popup a dialog to indicate how long the chart image generation takes.|
|render         |string            |Set the name of div object which will display chart image.|

# XML Documentation #
## chart element ##
This element can contain categories, dataset and script elements. It also has following attributes:
|**Attribute name**|**Type**|Default value|**Range**|**Description**|
|:-----------------|:-------|:------------|:--------|:--------------|
|format            |string  |PNG          |SVG,PNG,JPG,BMP,GIF|Set the output format of chart image|
|type              |string  |BAR          |BAR,PYRAMID,CONE,TUBE,LINE,AREA,SCATTER,PIE|Set the chart type|
|dimension         |string  |2D           |2D,2.5D,3D|Set the chart dimension|
|stacked           |boolean |false        |         |Set the stacked state of multiple series|
|colorByCategory   |boolean |true         |         |Set whether series color varies by category|
|showLegend        |boolean |true         |         |Set whether legend is visible|
|showLabel         |boolean |false        |         |Set whether series label is visible|
|title             |string  |_optional_   |         |Set the chart title|

## categories element ##
This element can contain category element. It doesn't have attribute.

## category element ##
This element has following attributes:
|**Attribute name**|**Type**|Default value|**Range**|**Description**|
|:-----------------|:-------|:------------|:--------|:--------------|
|label             |string  |             |         |Set the label of category|

## dataset element ##
This element can contain set element. It has following attributes:
|**Attribute name**|**Type**|Default value|**Range**|**Description**|
|:-----------------|:-------|:------------|:--------|:--------------|
|name              |string  |_optional_   |         |Set the series name|

## set element ##
This element has following attributes:
|**Attribute name**|**Type**|Default value|**Range**|**Description**|
|:-----------------|:-------|:------------|:--------|:--------------|
|value             |double or datetime|             |         |Set the value of current data point|
|tooltip           |string  |_optional_   |         |Set the tooltip of current data point|
|url               |string  |_optional_   |         |Set the url of current data point|

## script element ##
This element can contain script content directly.