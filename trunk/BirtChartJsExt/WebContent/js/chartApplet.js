BirtChartApplet = function(birtChart) {
	this.jars = new Array();
	this.appletClass = "ChartApplet.class";
	this.codeBase = ".";
	// this.addJar("chartapplet.jar");
	this.addJar("js.jar");
	this.addJar("com.ibm.icu_3.8.1.v20080530.jar");
	this.addJar("org.eclipse.birt.chart.device.extension_2.5.0.v20090312.jar");
	this.addJar("org.eclipse.birt.chart.engine.extension_2.5.0.v20090312.jar");
	this.addJar("org.eclipse.birt.chart.engine_2.5.0.v20090312.jar");
	this.addJar("org.eclipse.birt.core_2.5.0.v20090311.jar");
	this.addJar("org.eclipse.emf.common_2.5.0.v200902031500.jar");
	this.addJar("org.eclipse.emf.ecore.xmi_2.5.0.v200902031500.jar");
	this.addJar("org.eclipse.emf.ecore_2.5.0.v200902031500.jar");

	this.chart = birtChart;
}

BirtChartApplet.prototype = {
	addJar : function(jar) {
		this.jars[this.jars.length] = jar;
	},

	render : function(elementId, width, height) {
		drawChartApplet(elementId, this.appletClass, width, height,
				this.codeBase, this.getArchiveJars(), "bar");
	},

	getArchiveJars : function() {
		var archiveString = "";

		for ( var i = 0; i < this.jars.length; i++) {
			if (i > 0) {
				archiveString += ", ";
			}
			archiveString += this.jars[i];
		}
		return archiveString;
	}

}

BirtChart = function(chartType, title) {
	this.chartType = chartType;
	this.title = title;
}

BirtChart.prototype = {
	getXML : function() {
		return "<chart title='" + this.title + "' type='" + this.chartType
				+ "'></chart>";
	}
}

function drawChartApplet(elementId, appletClass, width, height, codeBase,
		archive, dataXML) {
	var _app = navigator.appName;
	var html;
	if (_app == 'Netscape') {
		html = '<embed code=' + appletClass + ' width=' + width + ' height='
				+ height + ' codeBase=\"' + codeBase + '\"' + ' archive=\"'
				+ archive + '\"' + ' dataXML=\"' + dataXML + '\"'
				+ ' type="application/x-java-applet;version=1.6">';
	} else if (_app == 'Microsoft Internet Explorer') {
		html = '<OBJECT '
				+ ' classid="clsid:8AD9C840-044E-11D1-B3E9-00805F499D93"'
				+ ' width=' + width + ' height=' + height + '>'
				+ '<PARAM name="code" value=\"' + appletClass + '\">'
				+ '<PARAM name="codeBase" value=\"' + codeBase + '\">'
				+ '<PARAM name="archive" value=\"' + archive + '\">'
				+ '<PARAM name="dataXML" value=\"' + dataXML + '\">'
				+ '</OBJECT>';
	} else {
		html = '<p>Sorry, unsupported browser.</p>';
	}
	document.getElementById(elementId).innerHTML = html;
}