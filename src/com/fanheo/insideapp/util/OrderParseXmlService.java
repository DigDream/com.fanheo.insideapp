package com.fanheo.insideapp.util;

import java.io.InputStream;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *解析xml文件。
 */
public class OrderParseXmlService
{
	public HashMap<String, String> parseXml(InputStream inStream) throws Exception
	{
		HashMap<String, String> hashMap = new HashMap<String, String>();
		
		// 实例化一个文档构建器工厂
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		// 通过文档构建器工厂获取一个文档构建器
		DocumentBuilder builder = factory.newDocumentBuilder();
		// 通过文档通过文档构建器构建一个文档实例
		Document document = builder.parse(inStream);
		//获取XML文件根节点
		Element root = document.getDocumentElement();
		//获得所有子节点
		NodeList childNodes = root.getChildNodes();
		for (int j = 0; j < childNodes.getLength(); j++)
		{
			//遍历子节点
			Node childNode = (Node) childNodes.item(j);
			if (childNode.getNodeType() == Node.ELEMENT_NODE)
			{
				Element childElement = (Element) childNode;
				//版本号
				if ("image".equals(childElement.getNodeName()))
				{
					hashMap.put("image",childElement.getFirstChild().getNodeValue());
				}
				//软件名称
				else if (("title".equals(childElement.getNodeName())))
				{
					hashMap.put("title",childElement.getFirstChild().getNodeValue());
				}
				//下载地址
				else if (("info".equals(childElement.getNodeName())))
				{
					hashMap.put("info",childElement.getFirstChild().getNodeValue());
				}
				//下载地址
				else if (("detail".equals(childElement.getNodeName())))
				{
					hashMap.put("detail",childElement.getFirstChild().getNodeValue());
				}
			}
		}
		return hashMap;
	}
}
