/*Created by: Aino Räkköläinen
 * Date: 3.5.2022
 * Sources: Sources are only lecture materials in this class
 *
 * */

package com.example.olio_ht;

import android.os.StrictMode;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

//In this class is parsed the data for movies from Finnkino's xml file with method readEventXML
public class MoviesData {
    private Document doc;
    private ArrayList<Movie> movies = new ArrayList<Movie>();

    public void readEventXML(String url) {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(url);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getDocumentElement().getElementsByTagName("Event");
            for (int i = 0; i < nList.getLength(); i++) {
                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String title = element.getElementsByTagName("Title").item(0).getTextContent();
                    String genres = element.getElementsByTagName("Genres").item(0).getTextContent();
                    Movie movie= new Movie(title, genres);
                    movies.add(movie);
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

    }

    public ArrayList<Movie> getMovies() {
        return movies;
    }
}

