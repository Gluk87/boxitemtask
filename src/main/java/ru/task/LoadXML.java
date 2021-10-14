package ru.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.MalformedURLException;

class LoadXML {
    static final Logger log =
            LoggerFactory.getLogger(LoadXML.class);

    static void loadXML(String link) throws MalformedURLException {
        Resource urlResource = new UrlResource(link);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document document;
       try {
            builder = factory.newDocumentBuilder();
            document = builder.parse(urlResource.getInputStream());
            document.getDocumentElement().normalize();

            NodeList nodeList = document.getElementsByTagName("Box");
            log.info("Table Box:");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Integer box_id;
                Integer box_id_parent = null;
                Node storage = nodeList.item(i);
                NamedNodeMap attributes = storage.getAttributes();
                box_id = Integer.parseInt(attributes.getNamedItem("id").getNodeValue());
                if (storage.getParentNode().getAttributes().getLength()>0) {
                    box_id_parent = Integer.parseInt(storage.getParentNode().getAttributes().getNamedItem("id").getNodeValue());
                }
                DataBase.insertIntoBox(box_id, box_id_parent);
                log.info(box_id+" "+box_id_parent);
            }

            nodeList = document.getElementsByTagName("Item");
            log.info("Table Item:");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Integer item_id;
                Integer box_id = null;
                String color = "";
                Node storage = nodeList.item(i);
                NamedNodeMap attributes = storage.getAttributes();
                item_id = Integer.parseInt(attributes.getNamedItem("id").getNodeValue());
                if (attributes.getLength()>1)
                    color = attributes.getNamedItem("color").getNodeValue();
                if (storage.getParentNode().getAttributes().getLength()>0) {
                    box_id = Integer.parseInt(storage.getParentNode().getAttributes().getNamedItem("id").getNodeValue());
                }
                DataBase.insertIntoItem(item_id, box_id, color);
                log.info(item_id+" "+box_id+" "+color);
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }
}