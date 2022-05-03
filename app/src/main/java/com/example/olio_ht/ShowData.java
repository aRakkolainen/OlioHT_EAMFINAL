/*Created by: Aino Räkköläinen
 * Date: 3.5.2022 */
package com.example.olio_ht;

import android.os.StrictMode;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
//In this class is parsed the data for shows from Finnkino's xml file with method readScheduleXML

public class ShowData {
    private ArrayList<Show> shows = new ArrayList<Show>();

    public ShowData() {

    }
    public void readScheduleXML(String url) {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(url);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getDocumentElement().getElementsByTagName("Show");
            for (int i = 0; i < nList.getLength(); i++) {
                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String theatreID = element.getElementsByTagName("TheatreID").item(0).getTextContent();
                    String theatre = element.getElementsByTagName("Theatre").item(0).getTextContent();
                    //String startingTime = element.getElementsByTagName("dttmShowStart").item(0).getTextContent();
                    String title = element.getElementsByTagName("Title").item(0).getTextContent();
                    String genres = element.getElementsByTagName("Genres").item(0).getTextContent();
                    Show show = new Show(theatreID, theatre, title, genres);
                    shows.add(show);
                }
            }

            } catch(IOException e) {
                e.printStackTrace();
            } catch(SAXException e){
                e.printStackTrace();
            } catch(ParserConfigurationException e){
                e.printStackTrace();
            }

    }

    public ArrayList<Show> getShows() {
        return shows;
    }
}
