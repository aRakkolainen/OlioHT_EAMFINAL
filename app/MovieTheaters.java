//Tekijä: Aino Räkköläinen ohjelma: MovieTheaterData.java Tarkoitus: Täällä parsitaan
// XML-tiedostosta halutut tiedot, tallennetaan ensin noteListaan ja sitten ArrayListaan jokainen
// elokuvateatteri luomalla sen omasta luokasta olio, jolla on attribuutteina nimi ja ID. Luokalla
// on metodi, jolla voidaan palauttaa tämä ArrayList pääaktiviteettiin. Lähteenä luentovideo Android ja XML
package com.example.viikko9;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

public class MovieTheaters {
    private Document doc;
    ArrayList<MovieTheater> movieTheaters = new ArrayList<MovieTheater>();

    public MovieTheaters(Document d) {
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
                movieTheaters.add(movietheater);
            }
        }
    }

    public ArrayList<MovieTheater> getMovieTheaters () {
        return movieTheaters;
    }
}