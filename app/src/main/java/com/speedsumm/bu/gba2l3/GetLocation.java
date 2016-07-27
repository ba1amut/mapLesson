package com.speedsumm.bu.gba2l3;

import android.os.AsyncTask;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by bu on 26.07.2016.
 */
public class GetLocation extends AsyncTask<String, Void, String> {
    NodeList nodeList;
    String lat;
    String lon;


    @Override
    protected String doInBackground(String... Url) {

        try {
            URL url = new URL(Url[0]);


            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document doc = documentBuilder.parse(new InputSource(url.openStream()));
            doc.getDocumentElement().normalize();
            nodeList = doc.getElementsByTagName("coordinates");
            Element node = (Element) nodeList.item(0);
            lat = node.getAttribute("nlatitude");
            lon = node.getAttribute("nlongitude");
            MainActivity.cellLatitude = Double.valueOf(lat);
            MainActivity.cellLongitude = Double.valueOf(lon);
            String s = lat+lon;

            Log.d(".....", s);


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        return lat;

    }

    @Override
    protected void onPostExecute(String s) {

        super.onPostExecute(s);
    }

}