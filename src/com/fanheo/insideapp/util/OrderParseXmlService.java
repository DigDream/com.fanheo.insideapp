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
 *����xml�ļ���
 */
public class OrderParseXmlService
{
	public HashMap<String, String> parseXml(InputStream inStream) throws Exception
	{
		HashMap<String, String> hashMap = new HashMap<String, String>();
		
		// ʵ����һ���ĵ�����������
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		// ͨ���ĵ�������������ȡһ���ĵ�������
		DocumentBuilder builder = factory.newDocumentBuilder();
		// ͨ���ĵ�ͨ���ĵ�����������һ���ĵ�ʵ��
		Document document = builder.parse(inStream);
		//��ȡXML�ļ����ڵ�
		Element root = document.getDocumentElement();
		//��������ӽڵ�
		NodeList childNodes = root.getChildNodes();
		for (int j = 0; j < childNodes.getLength(); j++)
		{
			//�����ӽڵ�
			Node childNode = (Node) childNodes.item(j);
			if (childNode.getNodeType() == Node.ELEMENT_NODE)
			{
				Element childElement = (Element) childNode;
				//�汾��
				if ("image".equals(childElement.getNodeName()))
				{
					hashMap.put("image",childElement.getFirstChild().getNodeValue());
				}
				//�������
				else if (("title".equals(childElement.getNodeName())))
				{
					hashMap.put("title",childElement.getFirstChild().getNodeValue());
				}
				//���ص�ַ
				else if (("info".equals(childElement.getNodeName())))
				{
					hashMap.put("info",childElement.getFirstChild().getNodeValue());
				}
				//���ص�ַ
				else if (("detail".equals(childElement.getNodeName())))
				{
					hashMap.put("detail",childElement.getFirstChild().getNodeValue());
				}
			}
		}
		return hashMap;
	}
}
