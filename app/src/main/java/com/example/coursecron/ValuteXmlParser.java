package com.example.coursecron;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

public class ValuteXmlParser {
    private ArrayList<Valute> valutes;

    public ValuteXmlParser(){
        valutes = new ArrayList<>();
    }

    public ArrayList<Valute> getValutes(){
        return valutes;
    }

    public boolean parse(String xmlData){
        boolean status = true;
        Valute currentValute = null;
        boolean inEntry = false;
        String textValue = "";

        try{
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();

            xpp.setInput(new StringReader(xmlData));
            int eventType = xpp.getEventType();
            while(eventType != XmlPullParser.END_DOCUMENT){

                String tagName = xpp.getName();
                switch (eventType){
                    case XmlPullParser.START_TAG:
                        if("Valute".equalsIgnoreCase(tagName)){
                            inEntry = true;
                            currentValute = new Valute();
                        }
                        break;
                    case XmlPullParser.TEXT:
                        textValue = xpp.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if(inEntry){
                            if("Valute".equalsIgnoreCase(tagName)){
                                valutes.add(currentValute);
                                inEntry = false;
                            } else if("Nominal".equalsIgnoreCase(tagName)){
                                currentValute.setNominal(textValue);
                            } else if("Value".equalsIgnoreCase(tagName)){
                                currentValute.setValue(textValue);
                            }
                            else if("NumCode".equalsIgnoreCase(tagName)){
                                currentValute.setNumCode(textValue);
                            }
                        }
                        break;
                    default:
                }
                eventType = xpp.next();
            }
        }
        catch (Exception e){
            status = false;
            e.printStackTrace();
        }
        return  status;
    }
}