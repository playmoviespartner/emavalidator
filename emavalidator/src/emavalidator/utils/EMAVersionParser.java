package emavalidator.utils;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * XML Parser that simply parses one single tag in an XML to determine the 
 * avails version number being used.
 * @author ckha
 *
 */
public class EMAVersionParser
{
    // Filename of the XML to be validated.
    String fileName;
    InputStream is;
    
    public EMAVersionParser(String fileName, InputStream is) 
    {
        this.fileName = fileName;
        this.is = is;
    }
    
    class MyXMLHandler extends DefaultHandler 
    {
        // Version Number of the avails XML.
        String version;
        
        public MyXMLHandler() 
        {
            this.version = "v1.6"; // Default to v1.6
        }
        
        public void startElement(String uri, String localName, String name,
                Attributes attributes) throws SAXException 
        {
            if ( name.equals("AvailList") ) {
                int length = attributes.getLength();
                for( int i = 0; i < length; i++ ) {
                    String n = attributes.getQName(i);
                    String v = attributes.getValue(i);
                    if ( n.equals("xmlns") ) {
                        /** 
                         * Tokenize the URL: http://www.movielabs.com/schema/avails/v1.6/avails
                         * Take the 5th token (v1.6) as the version number.
                         */
                        String[] tokens = v.split("/");
                        
                        this.version = tokens[5];
                    }
                }
            }
        } // end startElement()
        
        public void endElement(String uri, String localName, String name)
                throws SAXException 
        {

        } // end endElement()
        
        public void characters(char ch[], int start, int length) throws SAXException 
        {
 
        } // end characters()

    }
    
    public String process() throws ParserConfigurationException, SAXException 
    {
        
        SAXParserFactory factory = SAXParserFactory.newInstance();
        MyXMLHandler xmlHandler = new MyXMLHandler();
        
        try 
        {
            SAXParser parser = factory.newSAXParser();
            parser.parse(this.is, xmlHandler);
        } 
        catch (SAXException se) 
        {
            se.printStackTrace();
        } 
        catch (IOException ioe) 
        {
            ioe.printStackTrace();
        }

        return xmlHandler.version;
    }
}
