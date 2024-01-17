package ru.oleaghue.file_distributor.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import ru.oleaghue.file_distributor.exceptions.ConfigReadException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class ConfigReader {

    public static Map<String, String> readConfig(String dir, String studioName)  {
        Map<String, String> result = new HashMap<>();
        String filePath = dir + File.separator + "distributor_config.xml";
        File file = new File(filePath);
        if (!file.exists()) {
            throw new RuntimeException();
        }
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document document = builder.parse(file);
            document.getDocumentElement().normalize();

            NodeList nodeList = document.getElementsByTagName(studioName);

            for (int i = 0; i < nodeList.getLength(); i++) {
                Setting setting = getSetting(nodeList.item(i));
                result.put(setting.getName(), setting.getValue());
            }

        } catch (ParserConfigurationException | IOException | SAXException e) {
            LocalDateTime date = LocalDateTime.now();
            System.out.println(date + e.getMessage());
            throw new ConfigReadException("Ошибка чтения конфигурации ", e);
        }
        return result;
    }
    private static Setting getSetting(Node node) {
        Setting lang = new Setting();
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            lang.setName(getTagValue("name", element));
            lang.setValue(getTagValue("value", element));
        }

        return lang;
    }
    private static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = nodeList.item(0);
        return node.getNodeValue();
    }
}
