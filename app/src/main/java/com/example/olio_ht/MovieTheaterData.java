/*Created by: Aino Räkköläinen
 * Date: 3.5.2022 */
// Here is parsed the data from wanted Finnkino's XML file, those are saved to noteList and then ArrayList as
// MovieTheater objects. Resource is the lecture videos

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

//In this class is parsed the data for the movietheatres
public class MovieTheaterData {
    private Document doc;
    ArrayList<MovieTheater> movieTheaters = new ArrayList<MovieTheater>();
    HashMap <String, String> movieTheaterHashMap = new HashMap<>();

    public MovieTheaterData(Document d) {
        this.doc = d;
        doc.getDocumentElement().normalize();
        NodeList nList = doc.getDocumentElement().getElementsByTagName("TheatreArea");
        for (int i = 0; i < nList.getLength(); i++) {
            Node node = nList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String ID = element.getElementsByTagName("ID").item(0).getTextContent();
                String Name = element.getElementsByTagName("Name").item(0).getTextContent();
                MovieTheater movietheater = new MovieTheater(ID, Name);
                movieTheaterHashMap.put(Name, ID);
                movieTheaters.add(movietheater);
            }
        }
    }

    public ArrayList<MovieTheater> getMovieTheaters () {
        return movieTheaters;
    }
}