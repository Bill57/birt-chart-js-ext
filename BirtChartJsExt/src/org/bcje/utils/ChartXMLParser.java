package org.bcje.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.bcje.model.ChartCategory;
import org.bcje.model.ChartDataSet;
import org.bcje.model.ChartModel;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ChartXMLParser {

	private final String xml;

	public ChartXMLParser(String xml) {
		this.xml = xml;
	}

	public ChartModel createChart() {
		ChartModel chart = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder parser = factory.newDocumentBuilder();
			InputStream is = new ByteArrayInputStream(xml.getBytes());
			Document doc = parser.parse(is);

			Node root = doc.getFirstChild();
			NamedNodeMap chartAttr = root.getAttributes();
			String type = chartAttr.getNamedItem("type").getNodeValue();
			String dimension = chartAttr.getNamedItem("dimension")
					.getNodeValue();
			String format = chartAttr.getNamedItem("format").getNodeValue();
			String width = chartAttr.getNamedItem("width").getNodeValue();
			String height = chartAttr.getNamedItem("height").getNodeValue();
			chart = new ChartModel(type, dimension, format, Double
					.parseDouble(width), Double.parseDouble(height));

			String title = chartAttr.getNamedItem("title").getNodeValue();
			chart.setTitle(title);

			chart.setStacked(getBooleanProperty(chartAttr, "stacked"));
			chart.setColorByCategory(getBooleanProperty(chartAttr,
					"colorByCategory"));
			chart.setShowLabel(getBooleanProperty(chartAttr, "showLabel"));
			chart.setShowLegend(getBooleanProperty(chartAttr, "showLegend"));

			NodeList nodes = root.getChildNodes();
			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);
				if (node.getNodeName().equals("categories")) {
					handleCategories(chart, node);
				} else if (node.getNodeName().equals("dataset")) {
					handleValues(chart, node);
				} else if (node.getNodeName().equals("script")) {
					if (node.getFirstChild() != null) {
						chart.setScript(node.getFirstChild().getNodeValue());
					}
				}
			}

			is.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return chart;
	}

	private void handleCategories(ChartModel cm, Node categories) {
		NodeList categoryList = categories.getChildNodes();
		for (int i = 0; i < categoryList.getLength(); i++) {
			NamedNodeMap namedNode = categoryList.item(i).getAttributes();
			if (namedNode != null) {
				String label = namedNode.getNamedItem("label").getNodeValue();
				cm.getCategories().add(new ChartCategory(label));
			}
		}
	}

	private void handleValues(ChartModel cm, Node values) {
		cm.getSeriesNames().add(getNodeValue(values.getAttributes(), "name"));

		NodeList valueList = values.getChildNodes();
		for (int i = 0; i < valueList.getLength(); i++) {
			NamedNodeMap namedNode = valueList.item(i).getAttributes();
			if (namedNode != null) {
				String label = getNodeValue(namedNode, "value");
				if (label != null) {
					ChartDataSet dataset = new ChartDataSet(Double
							.parseDouble(label));
					dataset.setTooltip(getNodeValue(namedNode, "tooltip"));
					dataset.setUrl(getNodeValue(namedNode, "url"));
					cm.getDatasets().add(dataset);
				}
			}
		}

	}

	private String getNodeValue(NamedNodeMap namedNode, String nodeName) {
		Node node = namedNode.getNamedItem(nodeName);
		if (node != null) {
			return node.getNodeValue();
		}
		return null;
	}

	private boolean getBooleanProperty(NamedNodeMap chartAttr,
			String propertyName) {
		Node node = chartAttr.getNamedItem(propertyName);
		return node != null && node.getNodeValue() != null
				&& Boolean.parseBoolean(node.getNodeValue());
	}
}
