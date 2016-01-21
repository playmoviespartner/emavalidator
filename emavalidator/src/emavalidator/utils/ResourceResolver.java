package emavalidator.utils;

import java.io.InputStream;

import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

public class ResourceResolver implements LSResourceResolver
{

    public ResourceResolver() 
    {
        super();
    }
    
    public LSInput resolveResource(String type, String namespaceURI,
            String publicId, String systemId, String baseURI) 
    {
        InputStream resourceAsStream;
        System.out.println("Resolving: " + type + ", " + namespaceURI + ", " +  publicId  + ", " + systemId + ", " + baseURI);

         // note: in this sample, the XSD's are expected to be in the root of the classpath
        resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(systemId);

        return new Input(publicId, systemId, resourceAsStream);
    }
    
}

