/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-report-writer/LICENSE.txt for details.
 */

package gov.nih.nci.evs.reportwriter.properties;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Text;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParsePosition;
import java.util.List;
import java.util.ArrayList;
import java.util.Vector;
import java.util.HashMap;

import java.io.*;
import java.util.*;


/**
 * Application ConfigurationFileParser class
 *
 * @since 1.0
 * @author kim.ong@ngc.com
 * @version 1.0
 */


public class ConfigurationFileParser {
    protected Document doc = null;
    protected String xmlfile = null;
    private NodeList root_nodes = null;
    private Vector path_vec = null;
    private Vector node_vec = null;

    public ConfigurationFileParser() {
		this.doc = null;
		this.xmlfile = null;
	}

    public ConfigurationFileParser(String xmlfile) {
		this.xmlfile = xmlfile;
		this.doc = loadDocument(xmlfile);
		String root_node_name = this.doc.getDocumentElement().getNodeName();

		if (root_node_name != null) {
			this.root_nodes = doc.getElementsByTagName(root_node_name);
	    }
	}

    public Document getDocument() {
		return this.doc;
	}

    public String getXMLFile() {
		return this.xmlfile;
	}

	public NodeList getRootNodes() {
		return this.root_nodes;
	}

	protected Node getNode(String tagName, NodeList nodes) {
		for ( int x = 0; x < nodes.getLength(); x++ ) {
			Node node = nodes.item(x);
			if (node.getNodeName().equalsIgnoreCase(tagName)) {
				return node;
			}
		}
		return null;
	}

	protected String getNodeValue( Node node ) {
		NodeList childNodes = node.getChildNodes();
		for (int x = 0; x < childNodes.getLength(); x++ ) {
			Node data = childNodes.item(x);
			if ( data.getNodeType() == Node.TEXT_NODE )
				return data.getNodeValue();
		}
		return "";
	}

	protected String getNodeValue(String tagName, NodeList nodes ) {
		for ( int x = 0; x < nodes.getLength(); x++ ) {
			Node node = nodes.item(x);
			if (node.getNodeName().equalsIgnoreCase(tagName)) {
				NodeList childNodes = node.getChildNodes();
				for (int y = 0; y < childNodes.getLength(); y++ ) {
					Node data = childNodes.item(y);
					if ( data.getNodeType() == Node.TEXT_NODE )
						return data.getNodeValue();
				}
			}
		}
		return "";
	}

	protected String getNodeAttr(String attrName, Node node ) {
		NamedNodeMap attrs = node.getAttributes();
		if (attrs == null) return "";
		for (int y = 0; y < attrs.getLength(); y++ ) {
			Node attr = attrs.item(y);
			if (attr.getNodeName().equalsIgnoreCase(attrName)) {
				return attr.getNodeValue();
			}
		}
		return "";
	}

	protected String getNodeAttr(String tagName, String attrName, NodeList nodes ) {
		for ( int x = 0; x < nodes.getLength(); x++ ) {
			Node node = nodes.item(x);
			if (node.getNodeName().equalsIgnoreCase(tagName)) {
				NodeList childNodes = node.getChildNodes();
				for (int y = 0; y < childNodes.getLength(); y++ ) {
					Node data = childNodes.item(y);
					if ( data.getNodeType() == Node.ATTRIBUTE_NODE ) {
						if ( data.getNodeName().equalsIgnoreCase(attrName) )
							return data.getNodeValue();
					}
				}
			}
		}

		return "";
	}

    public Document loadDocument(String filename) {
		Document doc = null;
        try {
			File file = new File(filename);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(file);
			doc.getDocumentElement().normalize();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return doc;
	}


	public List searchDocument(NodeList nodes, String nodeName, short type) {
        List list = new ArrayList();
		if (nodes == null) return list;
		for (int i=0; i<nodes.getLength(); i++) {
			Node node = nodes.item(i);
			if (node.getNodeName().compareTo(nodeName) == 0 && node.getNodeType() == type) {
				list.add(node);
			}

		    if (node.hasChildNodes()) {
				NodeList childNodes = node.getChildNodes();
				List list2 = searchDocument(childNodes, nodeName, type);
                if (list2 != null && list2.size() > 0) {
					list.addAll(list2);
				}
			}
		}
        return list;
	}

	public List searchDocument(String nodeName, short type) {
		String root_node_name = doc.getDocumentElement().getNodeName();
		NodeList nodes = doc.getElementsByTagName(root_node_name);
		List list = searchDocument(nodes, nodeName, type);
        return list;
	}

	public List searchDocument(Document doc, String nodeName, short type) {
		String root_node_name = doc.getDocumentElement().getNodeName();
		NodeList nodes = doc.getElementsByTagName(root_node_name);
		List list = searchDocument(nodes, nodeName, type);
        return list;
	}


	public NodeList getRootNodes(String filename) {
		try {
			Document doc = loadDocument(filename);
			String root_node_name = doc.getDocumentElement().getNodeName();
			NodeList nodes = doc.getElementsByTagName(root_node_name);
			return nodes;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public NodeList getRootNodeList(String filename) {
		try {
			Document doc = loadDocument(filename);
			String root_node_name = doc.getDocumentElement().getNodeName();
			NodeList nodes = doc.getElementsByTagName(root_node_name);
			return nodes;

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

    public HashMap getNodeAttributes(Node node) {
		if (node == null) return null;
		HashMap map = new HashMap();
		if (!node.hasAttributes()) {
			return map;
		}

		NamedNodeMap nnm = node.getAttributes();
		int len = nnm.getLength();
		for (int k=0; k<len; k++) {
			Node nd = (Node) nnm.item(k);
			String value = nd.getNodeValue();
			map.put(nd.getNodeName(), value);
		}
		return map;
	}

	public Vector extractElementNodesByName(String name) {
		Vector code_vec = new Vector();
		for (int i=0; i<node_vec.size(); i++) {
			Node node = (Node) node_vec.elementAt(i);
			if (node.getNodeName().compareTo(name) == 0) {
                code_vec.add(node);
			}
		}
		return code_vec;
	}

    public String extractTextContent(Node node) {
        if (node.getNodeType() != Node.ELEMENT_NODE) return null;
        org.w3c.dom.Element e = (org.w3c.dom.Element) node;
		String value = e.getTextContent();
		if (value != null) {
			value = value.trim();
		}
		return value;
	}


    public String getElementNodeValue(Node node) {
		NodeList childNodes = node.getChildNodes();
		if (childNodes != null) {
			for (int x = 0; x < childNodes.getLength(); x++ ) {
				Node childNode = childNodes.item(x);
				if (childNode.getNodeName().compareTo("#text") == 0) {
					String t = childNode.getNodeValue();
					return t;
				}
			}
		}
		return null;
	}

    public HashMap getAttributeToValueHashMap(String nodeName, String attrName) {
		HashMap map = new HashMap();
		List list = searchDocument(root_nodes, nodeName, Node.ELEMENT_NODE);
		for (int i=0; i<list.size(); i++) {
			Node node = (Node) list.get(i);
			map.put(getNodeAttr(attrName, node), extractTextContent(node));
		}
		return map;
	}
}

