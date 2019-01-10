/**   
 * 特别声明：本技术材料受《中华人民共和国着作权法》、《计算机软件保护条例》
 * 等法律、法规、行政规章以及有关国际条约的保护，武汉中地数码科技有限公
 * 司享有知识产权、保留一切权利并视其为技术秘密。未经本公司书面许可，任何人
 * 不得擅自（包括但不限于：以非法的方式复制、传播、展示、镜像、上载、下载）使
 * 用，不得向第三方泄露、透露、披露。否则，本公司将依法追究侵权者的法律责任。
 * 特此声明！
 * 
   Copyright (c) 2014,武汉华信联创技术工程有限公司
 */

package com.hxlc.backstageapp.util;

import org.w3c.dom.*;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.util.*;

/**
 * Few simple utils to read DOM. This is originally from the Jakarta Commons
 * Modeler.
 * 
 * @author Costin Manolache
 */
public final class DOMUtils {
	private static final DocumentBuilderFactory FACTORY = DocumentBuilderFactory.newInstance();
	private static DocumentBuilder builder;
	private static final String XMLNAMESPACE = "xmlns";

	private DOMUtils() {
	}

	private static synchronized DocumentBuilder getBuilder() throws ParserConfigurationException {
		if (builder == null) {
			FACTORY.setNamespaceAware(true);
			builder = FACTORY.newDocumentBuilder();
		}
		return builder;
	}

	/**
	 * This function is much like getAttribute, but returns null, not "", for a
	 * nonexistent attribute.
	 * 
	 * @param e
	 * @param attributeName
	 * @return
	 */
	public static String getAttributeValueEmptyNull(Element e, String attributeName) {
		Attr node = e.getAttributeNode(attributeName);
		if (node == null) {
			return null;
		}
		return node.getValue();
	}

	/**
	 * Get the trimmed text content of a node or null if there is no text
	 */
	public static String getContent(Node n) {
		String s = getRawContent(n);
		if (s != null) {
			s = s.trim();
		}
		return s;
	}

	/**
	 * Get the raw text content of a node or null if there is no text
	 */
	public static String getRawContent(Node n) {
		if (n == null) {
			return null;
		}
		StringBuilder b = null;
		String s = null;
		Node n1 = n.getFirstChild();
		while (n1 != null) {
			if (n1.getNodeType() == Node.TEXT_NODE) {
				if (b != null) {
					b.append(((Text) n1).getNodeValue());
				} else if (s == null) {
					s = ((Text) n1).getNodeValue();
				} else {
					b = new StringBuilder(s).append(((Text) n1).getNodeValue());
					s = null;
				}
			}
			n1 = n1.getNextSibling();
		}
		if (b != null) {
			return b.toString();
		}
		return s;
	}

	/**
	 * Get the first element child.
	 * 
	 * @param parent
	 *            lookup direct childs
	 * @param name
	 *            name of the element. If null return the first element.
	 */
	public static Node getChild(Node parent, String name) {
		if (parent == null) {
			return null;
		}

		Node first = parent.getFirstChild();
		if (first == null) {
			return null;
		}

		for (Node node = first; node != null; node = node.getNextSibling()) {
			// system.out.println("getNode: " + name + " " +
			// node.getNodeName());
			if (node.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			if (name != null && name.equals(node.getNodeName())) {
				return node;
			}
			if (name == null) {
				return node;
			}
		}
		return null;
	}

	public static String getAttribute(Node element, String attName) {
		NamedNodeMap attrs = element.getAttributes();
		if (attrs == null) {
			return null;
		}
		Node attN = attrs.getNamedItem(attName);
		if (attN == null) {
			return null;
		}
		return attN.getNodeValue();
	}

	public static String getAttribute(Element element, QName attName) {
		Attr attr;
		if (isEmpty(attName.getNamespaceURI())) {
			attr = element.getAttributeNode(attName.getLocalPart());
		} else {
			attr = element.getAttributeNodeNS(attName.getNamespaceURI(), attName.getLocalPart());
		}
		return attr == null ? null : attr.getValue();
	}

	public static void setAttribute(Node node, String attName, String val) {
		NamedNodeMap attributes = node.getAttributes();
		Node attNode = node.getOwnerDocument().createAttribute(attName);
		attNode.setNodeValue(val);
		attributes.setNamedItem(attNode);
	}

	public static void removeAttribute(Node node, String attName) {
		NamedNodeMap attributes = node.getAttributes();
		attributes.removeNamedItem(attName);
	}

	/**
	 * Set or replace the text value
	 */
	public static void setText(Node node, String val) {
		Node chld = DOMUtils.getChild(node, Node.TEXT_NODE);
		if (chld == null) {
			Node textN = node.getOwnerDocument().createTextNode(val);
			node.appendChild(textN);
			return;
		}
		// change the value
		chld.setNodeValue(val);
	}

	/**
	 * Find the first direct child with a given attribute.
	 * 
	 * @param parent
	 * @param elemName
	 *            name of the element, or null for any
	 * @param attName
	 *            attribute we're looking for
	 * @param attVal
	 *            attribute value or null if we just want any
	 */
	public static Node findChildWithAtt(Node parent, String elemName, String attName, String attVal) {

		Node child = DOMUtils.getChild(parent, Node.ELEMENT_NODE);
		if (attVal == null) {
			while (child != null && (elemName == null || elemName.equals(child.getNodeName()))
					&& DOMUtils.getAttribute(child, attName) != null) {
				child = getNext(child, elemName, Node.ELEMENT_NODE);
			}
		} else {
			while (child != null && (elemName == null || elemName.equals(child.getNodeName()))
					&& !attVal.equals(DOMUtils.getAttribute(child, attName))) {
				child = getNext(child, elemName, Node.ELEMENT_NODE);
			}
		}
		return child;
	}

	/**
	 * Get the first child's content ( ie it's included TEXT node ).
	 */
	public static String getChildContent(Node parent, String name) {
		Node first = parent.getFirstChild();
		if (first == null) {
			return null;
		}
		for (Node node = first; node != null; node = node.getNextSibling()) {
			// system.out.println("getNode: " + name + " " +
			// node.getNodeName());
			if (name.equals(node.getNodeName())) {
				return getRawContent(node);
			}
		}
		return null;
	}

	public static QName getElementQName(Element el) {
		return new QName(el.getNamespaceURI(), el.getLocalName());
	}

	/**
	 * Get the first direct child with a given type
	 */
	public static Element getFirstElement(Node parent) {
		Node n = parent.getFirstChild();
		while (n != null && Node.ELEMENT_NODE != n.getNodeType()) {
			n = n.getNextSibling();
		}
		if (n == null) {
			return null;
		}
		return (Element) n;
	}

	public static Element getNextElement(Element el) {
		Node nd = el.getNextSibling();
		while (nd != null) {
			if (nd.getNodeType() == Node.ELEMENT_NODE) {
				return (Element) nd;
			}
			nd = nd.getNextSibling();
		}
		return null;
	}

	/**
	 * Return the first element child with the specified qualified name.
	 * 
	 * @param parent
	 * @param q
	 * @return
	 */
	public static Element getFirstChildWithName(Element parent, QName q) {
		String ns = q.getNamespaceURI();
		String lp = q.getLocalPart();
		return getFirstChildWithName(parent, ns, lp);
	}

	/**
	 * Return the first element child with the specified qualified name.
	 * 
	 * @param parent
	 * @param ns
	 * @param lp
	 * @return
	 */
	public static Element getFirstChildWithName(Element parent, String ns, String lp) {
		for (Node n = parent.getFirstChild(); n != null; n = n.getNextSibling()) {
			if (n instanceof Element) {
				Element e = (Element) n;
				String ens = (e.getNamespaceURI() == null) ? "" : e.getNamespaceURI();
				if (ns.equals(ens) && lp.equals(e.getLocalName())) {
					return e;
				}
			}
		}
		return null;
	}

	/**
	 * Return child elements with specified name.
	 * 
	 * @param parent
	 * @param ns
	 * @param localName
	 * @return
	 */
	public static List<Element> getChildrenWithName(Element parent, String ns, String localName) {
		List<Element> r = new ArrayList<Element>();
		for (Node n = parent.getFirstChild(); n != null; n = n.getNextSibling()) {
			if (n instanceof Element) {
				Element e = (Element) n;
				String eNs = (e.getNamespaceURI() == null) ? "" : e.getNamespaceURI();
				if (ns.equals(eNs) && localName.equals(e.getLocalName())) {
					r.add(e);
				}
			}
		}
		return r;
	}

	/**
	 * Get the first child of the specified type.
	 * 
	 * @param parent
	 * @param type
	 * @return
	 */
	public static Node getChild(Node parent, int type) {
		Node n = parent.getFirstChild();
		while (n != null && type != n.getNodeType()) {
			n = n.getNextSibling();
		}
		if (n == null) {
			return null;
		}
		return n;
	}

	/**
	 * Get the next sibling with the same name and type
	 */
	public static Node getNext(Node current) {
		String name = current.getNodeName();
		int type = current.getNodeType();
		return getNext(current, name, type);
	}

	/**
	 * Return the next sibling with a given name and type
	 */
	public static Node getNext(Node current, String name, int type) {
		Node first = current.getNextSibling();
		if (first == null) {
			return null;
		}

		for (Node node = first; node != null; node = node.getNextSibling()) {

			if (type >= 0 && node.getNodeType() != type) {
				continue;
			}

			if (name == null) {
				return node;
			}
			if (name.equals(node.getNodeName())) {
				return node;
			}
		}
		return null;
	}

	public static class NullResolver implements EntityResolver {
		public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
			return new InputSource(new StringReader(""));
		}
	}

	/**
	 * Read XML as DOM.
	 */
	public static Document readXml(InputStream is) throws SAXException, IOException, ParserConfigurationException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		dbf.setValidating(false);
		dbf.setIgnoringComments(false);
		dbf.setIgnoringElementContentWhitespace(true);
		dbf.setNamespaceAware(true);
		// dbf.setCoalescing(true);
		// dbf.setExpandEntityReferences(true);

		DocumentBuilder db = null;
		db = dbf.newDocumentBuilder();
		db.setEntityResolver(new NullResolver());

		// db.setErrorHandler( new MyErrorHandler());

		return db.parse(is);
	}

	public static Document readXml(Reader is) throws SAXException, IOException, ParserConfigurationException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		dbf.setValidating(false);
		dbf.setIgnoringComments(false);
		dbf.setIgnoringElementContentWhitespace(true);
		dbf.setNamespaceAware(true);
		// dbf.setCoalescing(true);
		// dbf.setExpandEntityReferences(true);

		DocumentBuilder db = null;
		db = dbf.newDocumentBuilder();
		db.setEntityResolver(new NullResolver());

		// db.setErrorHandler( new MyErrorHandler());
		InputSource ips = new InputSource(is);
		return db.parse(ips);
	}

	public static Document readXml(File xmlF) throws SAXException, IOException, ParserConfigurationException {
		if (!xmlF.exists()) {
			return null;
		}
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		dbf.setValidating(false);
		dbf.setIgnoringComments(false);
		dbf.setIgnoringElementContentWhitespace(true);
		// dbf.setCoalescing(true);
		// dbf.setExpandEntityReferences(true);

		DocumentBuilder db = null;
		db = dbf.newDocumentBuilder();
		db.setEntityResolver(new NullResolver());

		// db.setErrorHandler( new MyErrorHandler());

		Document doc = db.parse(xmlF);
		return doc;
	}

	public static Document readXml(StreamSource is) throws SAXException, IOException, ParserConfigurationException {

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		dbf.setValidating(false);
		dbf.setIgnoringComments(false);
		dbf.setIgnoringElementContentWhitespace(true);
		dbf.setNamespaceAware(true);
		// dbf.setCoalescing(true);
		// dbf.setExpandEntityReferences(true);

		DocumentBuilder db = null;
		db = dbf.newDocumentBuilder();
		db.setEntityResolver(new NullResolver());

		// db.setErrorHandler( new MyErrorHandler());
		InputSource is2 = new InputSource();
		is2.setSystemId(is.getSystemId());
		is2.setByteStream(is.getInputStream());
		is2.setCharacterStream(is.getReader());

		return db.parse(is2);
	}

	public static void writeXml(Node n, OutputStream os) throws TransformerException {
		TransformerFactory tf = TransformerFactory.newInstance();
		// identity
		Transformer t = tf.newTransformer();
		t.setOutputProperty(OutputKeys.INDENT, "yes");
		t.transform(new DOMSource(n), new StreamResult(os));
	}

	public static DocumentBuilder createDocumentBuilder() {
		try {
			return FACTORY.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			throw new RuntimeException("Couldn't find a DOM parser.", e);
		}
	}

	public static Document createDocument() {
		try {
			return getBuilder().newDocument();
		} catch (ParserConfigurationException e) {
			throw new RuntimeException("Couldn't find a DOM parser.", e);
		}
	}

	public static String getPrefixRecursive(Element el, String ns) {
		String prefix = getPrefix(el, ns);
		if (prefix == null && el.getParentNode() instanceof Element) {
			prefix = getPrefixRecursive((Element) el.getParentNode(), ns);
		}
		return prefix;
	}

	public static String getPrefix(Element el, String ns) {
		NamedNodeMap atts = el.getAttributes();
		for (int i = 0; i < atts.getLength(); i++) {
			Node node = atts.item(i);
			String name = node.getNodeName();
			if (ns.equals(node.getNodeValue())
					&& (name != null && (XMLNAMESPACE.equals(name) || name.startsWith(XMLNAMESPACE + ":")))) {
				return node.getPrefix();
			}
		}
		return null;
	}

	/**
	 * Get all prefixes defined, up to the root, for a namespace URI.
	 * 
	 * @param element
	 * @param namespaceUri
	 * @param prefixes
	 */
	public static void getPrefixesRecursive(Element element, String namespaceUri, List<String> prefixes) {
		getPrefixes(element, namespaceUri, prefixes);
		Node parent = element.getParentNode();
		if (parent instanceof Element) {
			getPrefixesRecursive((Element) parent, namespaceUri, prefixes);
		}
	}

	/**
	 * Get all prefixes defined on this element for the specified namespace.
	 * 
	 * @param element
	 * @param namespaceUri
	 * @param prefixes
	 */
	public static void getPrefixes(Element element, String namespaceUri, List<String> prefixes) {
		NamedNodeMap atts = element.getAttributes();
		for (int i = 0; i < atts.getLength(); i++) {
			Node node = atts.item(i);
			String name = node.getNodeName();
			if (namespaceUri.equals(node.getNodeValue())
					&& (name != null && (XMLNAMESPACE.equals(name) || name.startsWith(XMLNAMESPACE + ":")))) {
				prefixes.add(node.getPrefix());
			}
		}
	}

	public static String createNamespace(Element el, String ns) {
		String p = "ns1";
		int i = 1;
		while (getPrefix(el, ns) != null) {
			p = "ns" + i;
			i++;
		}
		addNamespacePrefix(el, ns, p);
		return p;
	}

	/**
	 * Starting from a node, find the namespace declaration for a prefix. for a
	 * matching namespace declaration.
	 * 
	 * @param node
	 *            search up from here to search for namespace definitions
	 * @param searchPrefix
	 *            the prefix we are searching for
	 * @return the namespace if found.
	 */
	public static String getNamespace(Node node, String searchPrefix) {

		Element el;
		while (!(node instanceof Element)) {
			node = node.getParentNode();
		}
		el = (Element) node;

		NamedNodeMap atts = el.getAttributes();
		for (int i = 0; i < atts.getLength(); i++) {
			Node currentAttribute = atts.item(i);
			String currentLocalName = currentAttribute.getLocalName();
			String currentPrefix = currentAttribute.getPrefix();
			if (searchPrefix.equals(currentLocalName) && XMLNAMESPACE.equals(currentPrefix)) {
				return currentAttribute.getNodeValue();
			} else if (isEmpty(searchPrefix) && XMLNAMESPACE.equals(currentLocalName) && isEmpty(currentPrefix)) {
				return currentAttribute.getNodeValue();
			}
		}

		Node parent = el.getParentNode();
		if (parent instanceof Element) {
			return getNamespace((Element) parent, searchPrefix);
		}

		return null;
	}

	public static List<Element> findAllElementsByTagNameNS(Element elem, String nameSpaceURI, String localName) {
		List<Element> ret = new LinkedList<Element>();
		findAllElementsByTagNameNS(elem, nameSpaceURI, localName, ret);
		return ret;
	}

	private static void findAllElementsByTagNameNS(Element el, String nameSpaceURI, String localName,
			List<Element> elementList) {

		if (localName.equals(el.getLocalName()) && nameSpaceURI.contains(el.getNamespaceURI())) {
			elementList.add(el);
		}
		Element elem = getFirstElement(el);
		while (elem != null) {
			findAllElementsByTagNameNS(elem, nameSpaceURI, localName, elementList);
			elem = getNextElement(elem);
		}
	}

	public static List<Element> findAllElementsByTagName(Element elem, String tagName) {
		List<Element> ret = new LinkedList<Element>();
		findAllElementsByTagName(elem, tagName, ret);
		return ret;
	}

	/**
	 * 功能描述：读取数据表格XML格式的节点属性集合<br>
	 * 创建作者：雷志强<br>
	 * 创建时间：2013-8-21 上午11:32:56<br>
	 * 
	 * @param nodeList
	 * @return
	 * @return List<HashMap<String,String>>
	 */
	public static List<HashMap<String, String>> getXmlNodeAttList(NodeList nodeList) {
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		int length = nodeList.getLength();
		NamedNodeMap atts = null;
		String key = null, value = null;
		int attLength = 0;
		HashMap<String, String> row = null;
		for (int i = 0; i < length; i++) {
			atts = nodeList.item(i).getAttributes();
			attLength = atts.getLength();
			row = new LinkedHashMap<String, String>();
			for (int j = 0; j < attLength; j++) {
				key = atts.item(j).getNodeName();
				value = atts.item(j).getNodeValue();
				row.put(key, value);
			}
			list.add(row);
		}
		return list;
	}

	private static void findAllElementsByTagName(Element el, String tagName, List<Element> elementList) {

		if (tagName.equals(el.getTagName())) {
			elementList.add(el);
		}
		Element elem = getFirstElement(el);
		while (elem != null) {
			findAllElementsByTagName(elem, tagName, elementList);
			elem = getNextElement(elem);
		}
	}

	/**
	 * Set a namespace/prefix on an element if it is not set already. First off,
	 * it searches for the element for the prefix associated with the specified
	 * namespace. If the prefix isn't null, then this is returned. Otherwise, it
	 * creates a new attribute using the namespace/prefix passed as parameters.
	 * 
	 * @param element
	 * @param namespace
	 * @param prefix
	 * @return the prefix associated with the set namespace
	 */
	public static String setNamespace(Element element, String namespace, String prefix) {
		String pre = getPrefixRecursive(element, namespace);
		if (pre != null) {
			return pre;
		}
		element.setAttributeNS(XMLConstants.XMLNS_ATTRIBUTE_NS_URI, "xmlns:" + prefix, namespace);
		return prefix;
	}

	/**
	 * Add a namespace prefix definition to an element.
	 * 
	 * @param element
	 * @param namespaceUri
	 * @param prefix
	 */
	public static void addNamespacePrefix(Element element, String namespaceUri, String prefix) {
		element.setAttributeNS(XMLConstants.XMLNS_ATTRIBUTE_NS_URI, "xmlns:" + prefix, namespaceUri);
	}

	protected static boolean isEmpty(String str) {
		if (str != null) {
			int len = str.length();
			for (int x = 0; x < len; ++x) {
				if (str.charAt(x) > ' ') {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * @Description: 获取XML文件的指定结点
	 * 
	 * @author 王明生
	 * @date Aug 14, 2013 4:15:50 PM
	 * @param filePath
	 *            XML文件路径
	 * @param tagName
	 *            结点名称
	 * @return 结点列表
	 * @throws Exception
	 */
	public static NodeList getElementsByTagName(String filePath, String tagName) throws Exception {
		File configFile = new File(filePath);
		if (configFile.exists()) {
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(configFile);
				Document doc = readXml(fis);
				return doc.getElementsByTagName(tagName);
			} catch (Exception e) {
				throw e;
			} finally {
				if (fis != null) {
					try {
						fis.close();
					} catch (Exception e) {
						throw e;
					}
				}
			}
		} else {
			throw new Exception("文件:" + filePath + "不存在");
		}
	}

	/**
	 * @Description: 获取子结点属性集合
	 * 
	 * @author 王明生
	 * @date Aug 14, 2013 4:30:43 PM
	 * @param parentNode
	 *            父节点
	 * @return
	 */
	/**
	 * 功能描述：将指定结点的孩子结点属性转化为List<Map> <br>
	 * 创建作者：王明生<br>
	 * 创建时间：2013-8-15 上午11:08:00<br>
	 * 
	 * @param parentNode
	 *            父节点
	 * @return
	 * @return LinkedList<LinkedHashMap<String,String>>
	 */
	public static LinkedList<LinkedHashMap<String, String>> getChildNodesAttr(Node parentNode) {
		LinkedList<LinkedHashMap<String, String>> childNodeAttrList = new LinkedList<LinkedHashMap<String, String>>();
		NodeList childNodeList = parentNode.getChildNodes();
		for (int i = 0; i < childNodeList.getLength(); i++) {
			Node node = childNodeList.item(i);
			if (node instanceof Element) {
				LinkedHashMap<String, String> nodeAttrMap = getNodeAttr(node);
				childNodeAttrList.add(nodeAttrMap);
			}
		}
		return childNodeAttrList;
	}

	/**
	 * 功能描述：将指定结点的孩子结点属性转化为Map<key,Map> <br>
	 * 创建作者：王明生<br>
	 * 创建时间：2013-8-15 上午11:08:00<br>
	 * 
	 * @param parentNode
	 *            父节点
	 * @return
	 * @return LinkedList<LinkedHashMap<String,String>>
	 */
	public static LinkedHashMap<String, LinkedHashMap<String, String>> getChildNodesMap(Node parentNode) {
		LinkedHashMap<String, LinkedHashMap<String, String>> childNodeAttrMap = new LinkedHashMap<String, LinkedHashMap<String, String>>();
		NodeList childNodeList = parentNode.getChildNodes();
		for (int i = 0; i < childNodeList.getLength(); i++) {
			Node node = childNodeList.item(i);
			if (node instanceof Element) {
				String nodeName = node.getNodeName();
				LinkedHashMap<String, String> nodeAttrMap = getNodeAttr(node);
				childNodeAttrMap.put(nodeName, nodeAttrMap);
			}
		}
		return childNodeAttrMap;
	}

	/**
	 * 
	 * 功能描述：将结点属性信息转化为Map<br>
	 * 创建作者：王明生<br>
	 * 创建时间：2013-8-15 上午11:05:13<br>
	 * 
	 * @param node
	 *            结点
	 * @return
	 * @return LinkedHashMap<String,String> 结点属性Map
	 */
	public static LinkedHashMap<String, String> getNodeAttr(Node node) {
		LinkedHashMap<String, String> nodeAttrMap = new LinkedHashMap<String, String>();
		NamedNodeMap nodeMap = node.getAttributes();
		int attLength = nodeMap.getLength();
		for (int j = 0; j < attLength; j++) {
			String attributeName = nodeMap.item(j).getNodeName();
			String attributeValue = nodeMap.item(j).getNodeValue();
			nodeAttrMap.put(attributeName, attributeValue);
		}
		return nodeAttrMap;
	}

	/**
	 * 功能描述：创建结点及其结点属性<br>
	 * 创建作者：王明生<br>
	 * 创建时间：2013-9-13 下午01:48:56<br>
	 * 
	 * @param doc
	 *            文档结构对象
	 * @param nodeName
	 *            结点名
	 * @param nodeAttrMap
	 *            结点属性
	 * @return Node 创建的结点
	 */
	public static Node createNode(Document doc, String nodeName, Map<String, String> nodeAttrMap) {
		Element childElement = doc.createElement(nodeName);
		Iterator<String> iterator = nodeAttrMap.keySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			String value = nodeAttrMap.get(key);
			if (value == null || value.equals("null")) {
				value = "";
			}
			DOMUtils.setAttribute(childElement, key, value);
		}
		return childElement;
	}

	/**
	 * 功能描述：创建父节点与孩子结点的属性<br>
	 * 创建作者：王明生<br>
	 * 创建时间：2013-8-15 下午02:18:49<br>
	 * 
	 * @param doc
	 *            文档对象
	 * @param nodeName
	 *            结点名
	 * @param childNodeName
	 *            孩子结点名
	 * @param childNodeAttrList
	 *            孩子结点属性列表
	 * @return
	 * @return Node 创建的结点
	 */
	public static Node createNode(Document doc, String nodeName, String childNodeName,
			List<LinkedHashMap<String, String>> childNodeAttrList) {
		Element element = doc.createElement(nodeName);
		for (int i = 0; i < childNodeAttrList.size(); i++) {
			Node lineNode = doc.createTextNode("\n  \t");
			Element childElement = doc.createElement(childNodeName);
			LinkedHashMap<String, String> attributeMap = childNodeAttrList.get(i);
			Iterator<String> iterator = attributeMap.keySet().iterator();
			while (iterator.hasNext()) {
				String key = iterator.next();
				String value = attributeMap.get(key);
				if (value == null || value.equals("null")) {
					value = "";
				}
				DOMUtils.setAttribute(childElement, key, value);
			}
			element.appendChild(lineNode);
			element.appendChild(childElement);
		}
		Node lineNode = doc.createTextNode("\n");
		element.appendChild(lineNode);
		return element;
	}

	/**
	 * 
	 * 功能描述：将属性Map中的属性插入到指定结点中<br>
	 * 创建作者：王明生<br>
	 * 创建时间：2013-8-15 上午11:27:33<br>
	 * 
	 * @param node
	 *            被插入结点
	 * @param attributeMap
	 *            属性集合
	 * @return void
	 */
	public static void setNodeAttribute(Node node, HashMap<String, String> attributeMap) {
		setNodeAttribute(node, null, attributeMap);
	}

	/**
	 * 
	 * 功能描述：将Map中键值位于列表中的属性插入到结点中<br>
	 * 创建作者：王明生<br>
	 * 创建时间：2013-8-15 上午11:25:02<br>
	 * 
	 * @param node
	 *            被插入结点
	 * @param attributeList
	 *            属性集合
	 * @param attributeMap
	 *            属性列表
	 * @return void
	 */
	public static void setNodeAttribute(Node node, List<String> attributeList, HashMap<String, String> attributeMap) {
		Iterator<String> iterator = attributeMap.keySet().iterator();
		while (iterator.hasNext()) {
			String attriName = iterator.next();
			String attriValue = attributeMap.get(attriName);
			if (attributeList == null || attributeList.contains(attriName)) {
				setAttribute(node, attriName, attriValue);
			}
		}
	}

	/**
	 * 
	 * 功能描述：保存修改后的XML文件<br>
	 * 创建作者：王明生<br>
	 * 创建时间：2013-8-15 上午11:30:33<br>
	 * 
	 * @param doc
	 *            xml文档对象
	 * @param xmlFile
	 *            xml文件对象
	 * @throws Exception
	 * @return void
	 */
	public static void saveXml(Document doc, File xmlFile) throws Exception {
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.transform(new DOMSource(doc), new StreamResult(xmlFile));
	}

	/**
	 * 功能描述：获取结点的Text值<br>
	 * 创建作者：王明生<br>
	 * 创建时间：2013-9-27 上午10:03:33<br>
	 * 
	 * @param doc
	 *            文档对象
	 * @param nodeName
	 *            结点名
	 * @return String
	 */
	public static String getText(Document doc, String nodeName) throws Exception {
		NodeList nodeList = doc.getElementsByTagName(nodeName);
		int length = nodeList.getLength();
		if (length == 0 || length > 1) {
			throw new Exception("结点：" + nodeName + "不存在或者不唯一");
		}
		Node node = nodeList.item(0);
		String nodeTextContent = node.getTextContent();
		return nodeTextContent;
	}

	/**
	 * 功能描述：获取指定结点所有直接子节点的Text值<br>
	 * 创建作者：王明生<br>
	 * 创建时间：2013-10-17 上午10:08:19<br>
	 * 
	 * @param parentNode
	 *            父节点
	 * @return Map<String,String>
	 */
	public static void getChildNodeText(Node parentNode, Map<String, String> nodeConfigMap) {
		NodeList childList = parentNode.getChildNodes();
		if (childList != null && childList.getLength() > 0) {
			for (int i = 0; i < childList.getLength(); i++) {
				Node node = childList.item(i);
				if (node instanceof Element) {
					String nodeName = node.getNodeName();
					String nodeTextContent = node.getTextContent();
					nodeConfigMap.put(nodeName, nodeTextContent);
				}
			}
		}
	}

	/**
	 * 
	 * 功能描述：请用一句话描述这个方法实现的功能<br>
	 * 创建作者：王明生<br>
	 * 创建时间：2014-4-18 下午03:24:35<br>
	 * 
	 * @param doc
	 *            文档对象
	 * @param nodeName
	 *            结点名字
	 * @param childNode2TextMap
	 *            子结点的结点名与Text的映射集合
	 * @return Node
	 */
	public static Node createNodeByText(Document doc, String nodeName, Map<String, String> childNode2TextMap) {
		Element element = doc.createElement(nodeName);
		Node lineNode = doc.createTextNode("\n");
		element.appendChild(lineNode);
		Iterator<String> iterator = childNode2TextMap.keySet().iterator();
		while (iterator.hasNext()) {
			String childNodeName = iterator.next();
			String childNodeText = childNode2TextMap.get(childNodeName);
			Element childElement = doc.createElement(childNodeName);
			childElement.setTextContent(childNodeText);
			element.appendChild(childElement);
			lineNode = doc.createTextNode("\n");
			element.appendChild(lineNode);
		}
		return element;
	}

	/**
	 * 功能描述：修改父节点的所有子节点<br>
	 * 创建作者：王明生<br>
	 * 创建时间：2013-10-17 上午11:41:18<br>
	 * 
	 * @param parentNode
	 *            父节点
	 * @param childNoList
	 *            子节点列表
	 * @return void
	 */
	public static void modifyChildNode(Node parentNode, List<Node> newChildNodeList) {
		NodeList childNodeList = parentNode.getChildNodes();
		List<Node> oldChildNodeList = new ArrayList<Node>();
		if (childNodeList != null) {
			for (int i = 0; i < childNodeList.getLength(); i++) {
				Node childNode = childNodeList.item(i);
				oldChildNodeList.add(childNode);
			}
		}
		for (int i = 0; i < oldChildNodeList.size(); i++) {
			Node oldChild = oldChildNodeList.get(i);
			parentNode.removeChild(oldChild);
		}
		if (newChildNodeList != null) {
			for (int i = 0; i < newChildNodeList.size(); i++) {
				Node newChild = newChildNodeList.get(i);
				parentNode.appendChild(newChild);
			}
		}
	}

	/**
	 * 
	 * 功能描述：导出站点数据为XML格式<br>
	 * 创建作者：雷志强<br>
	 * 创建时间：2013-8-19 下午02:10:14<br>
	 * 
	 * @param dataHead
	 * @param featureText
	 * @param featureTime
	 * @param list
	 * @param outFilePath
	 * @throws Exception
	 * @return void
	 */
	public static void exportDataToXml(String[] dataHead, String normalHeader, List<List<String>> list,
			String outFilePath) throws Exception {
		Document doc = DOMUtils.createDocument();
		Node rootNode = doc.createElement("stations");
		DOMUtils.setAttribute(rootNode, "title", normalHeader);
		doc.appendChild(rootNode);
		int stationNum = list.size();
		List<String> rowList = null;
		Node stationNode = null;
		Node attNode = null;
		int nodeAttLength = dataHead.length;
		String[] strArr = null;
		String[] nodeArr = new String[nodeAttLength];
		String[] nodeTextArr = new String[nodeAttLength];
		for (int i = 0; i < nodeAttLength; i++) {
			strArr = dataHead[i].split("\\:");
			nodeArr[i] = strArr[0];
			nodeTextArr[i] = strArr[1];
		}
		for (int i = 0; i < stationNum; i++) {
			rowList = list.get(i);
			stationNode = doc.createElement("station");
			for (int j = 0; j < nodeAttLength; j++) {
				attNode = doc.createElement(nodeArr[j]);
				DOMUtils.setAttribute(attNode, "title", nodeTextArr[j]);
				DOMUtils.setText(attNode, rowList.get(j));
				stationNode.appendChild(attNode);
			}
			rootNode.appendChild(stationNode);
		}
		FileOutputStream fos = new FileOutputStream(outFilePath);
		DOMUtils.writeXml(doc, fos);
		fos.close();
	}
}
