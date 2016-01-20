package emavalidator;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;

import emavalidator.utils.ResourceResolver;

public class XSDValidator
{   
    // If there is an error, save the error into this variable to be printed.
    String errorMessage = "";
    
    public boolean validateXMLSchema(String xsdFilePath, InputStream xmlInputStream) 
    {
        try 
        {
            // Create factory with default XML Schema.
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            
            // Associate the schema factory with the resource resolver, which is responsible for resolving the imported XSD's
            factory.setResourceResolver(new ResourceResolver());
            
            // Array of schema sources.
//            Source[] schemaSources = new Source[] {
//                                        new StreamSource(new File("third_party/ema_xsd_template/xmldsig-core-schema.xsd")),
//                                        new StreamSource(new File("third_party/ema_xsd_template/avails-v2.0.xsd"))
//                                     };
            Source[] schemaSources = new Source[] {
              new StreamSource(this.getClass().getClassLoader().getResourceAsStream(xsdFilePath)),
//              new StreamSource(this.getClass().getClassLoader().getResourceAsStream("third_party/ema_xsd_template/xmldsig-core-schema.xsd")),
            };

            // Create the schema using the XSD Schema source.
            Schema schema = factory.newSchema(schemaSources);
            
            // Create a validator using the schema.
            Validator validator = schema.newValidator();
            
            // Validate the XML of the avail against the XSD.
            validator.validate(new StreamSource(xmlInputStream));
        } 
        catch (IOException e)
        {
            this.errorMessage = "Exception: " + e.getMessage();
            System.out.println(this.errorMessage);
            return false;
        }
        catch(SAXException e1) 
        {
            this.errorMessage = "SAX Exception: "+e1.getMessage();
            System.out.println(this.errorMessage);
            return false;
        }
        return true;
    }
    
    public String getErrorMessage() {
        return this.errorMessage;
    }
}
