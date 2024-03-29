package org.idch.util.xml;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import org.idch.util.LogService;

/**
 * A few utilities to help with XML type things
 * 
 * @author Neal Audenaert
 */
public abstract class XMLUtil {
    private static final String logger = XMLUtil.class.getName();
    
    /**
     * Writes the XML document to standard out.
     * @param doc An XML Document.
     */
    public static void print(Document doc) {
        try {
            TransformerFactory tFactory =
                TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer();
            
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(System.out);
            transformer.transform(source, result);
            
        } catch (TransformerConfigurationException tce) {
            // Error generated by the parser
            String msg = "Transformer Factory error: " + tce.getMessage();
            Throwable x = tce;
            if (tce.getException() != null)
                x = tce.getException();
            LogService.logError(msg, logger, x);
        } catch (TransformerException te) {
            // Error generated by the parser
            String msg = "Transformer error: " + te.getMessage();
            Throwable x = te;
            if (te.getException() != null)
                x = te.getException();
            LogService.logError(msg, logger, x);
         }
    }
    
    /**
     * Writes the XML document to a String.
     * @param doc An XML Document.
     * @return String representation of Document.
     */
    public static String toString(Document doc) {
    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
    	try {
        	TransformerFactory tFactory =
                TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer();
            
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(baos);
            transformer.transform(source, result);
        } catch (TransformerConfigurationException tce) {
            // Error generated by the parser
            String msg = "Transformer Factory error: " + tce.getMessage();
            Throwable x = tce;
            if (tce.getException() != null)
                x = tce.getException();
            LogService.logError(msg, logger, x);
        } catch (TransformerException te) {
            // Error generated by the parser
            String msg = "Transformer error: " + te.getMessage();
            Throwable x = te;
            if (te.getException() != null)
                x = te.getException();
            LogService.logError(msg, logger, x);
         }
        return baos.toString();
    }

    
    /**
     * Writes the XML document to the specified file.
     * @param doc An XML document.
     * @param file The file the XML document should be written to.
     */
    public static void print(Document doc, File file) {
        try {
            TransformerFactory tFactory =
                TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer();
            
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new FileOutputStream(file));
            transformer.transform(source, result);
            
        } catch (TransformerConfigurationException tce) {
            // Error generated by the parser
            String msg = "Transformer Factory error: " + tce.getMessage();
            Throwable x = tce;
            if (tce.getException() != null)
                x = tce.getException();
            LogService.logError(msg, logger, x);
        } catch (TransformerException te) {
            // Error generated by the parser
            String msg = "Transformer error: " + te.getMessage();
            Throwable x = te;
            if (te.getException() != null)
                x = te.getException();
            LogService.logError(msg, logger, x);
        } catch (FileNotFoundException fnfe) {
            // Could not open a file to write to
            String msg = "FileNotFoundException error";
            LogService.logError(msg, logger, fnfe);
        }
    }
    
    /**
     * Formats an XML document and returns it as a <code>String</code>.
     * @param doc An XML document
     * @param format A string containing an XSLT stylesheet.
     * @return The XML document, as a <code>String</code> formatted by the 
     *      stylesheet.
     */
    public static String format(Document doc, String format) {
        // get the xslt stylesource and the DOMSource 
        InputStream is = new ByteArrayInputStream(format.getBytes());
        StreamSource stylesource = new StreamSource(is);
        DOMSource source = new DOMSource(doc);
        
        return format(source, stylesource);
    }
    
    /**
     * Formats an XML document and returns it as a <code>String</code>.
     * @param doc An XML document
     * @param format A file containing an XSLT stylesheet.
     * @return The XML document, as a <code>String</code> formatted by the 
     *      stylesheet.
     */
    public static String format(Document doc, File format) {
            // get the xslt stylesource and the DOMSource 
            StreamSource stylesource = new StreamSource(format);
            DOMSource source = new DOMSource(doc);
            
            return format(source, stylesource);
    }
    
    /**
     * Formats an XML document and returns it as a <code>String</code>.
     * @param doc An XML document
     * @param format A file containing an XSLT stylesheet.
     * @return The XML document, as a <code>String</code> formatted by the 
     *      stylesheet.
     */
    private static String format(DOMSource source, StreamSource stylesource) {
        try {
            // construct the result
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            StreamResult transformResult = 
                new StreamResult(new BufferedOutputStream(result));
            
            if ((stylesource != null) && (source != null)) { 
                // create the transformer
                TransformerFactory tFactory =
                    TransformerFactory.newInstance();
                Transformer transformer = tFactory.newTransformer(stylesource);
                
                transformer.transform(source, transformResult);
                return result.toString();
            } else {
                String msg = "Could not obtain style or XML source object";
                LogService.logWarn(msg, logger);
            }
            
        } catch (TransformerConfigurationException tce) {
            // Error generated by the parser
            String msg = "Transformer Factory error: " + tce.getMessage();
            Throwable x = tce;
            if (tce.getException() != null)
                x = tce.getException();
            LogService.logError(msg, logger, x);
        } catch (TransformerException te) {
            // Error generated by the parser
            String msg = "Transformer error: " + te.getMessage();
            Throwable x = te;
            if (te.getException() != null)
                x = te.getException();
            LogService.logError(msg, logger, x);
        } 
        return "";
    }
    
    /**
     * Formats an XML document and returns it as a <code>String</code>.
     * @param doc An XML document
     * @param format A file containing an XSLT stylesheet.
     * @return The XML document, as a <code>String</code> formatted by the 
     *      stylesheet.
     */
    public static void format(Document doc, File format, File output) {
        BufferedOutputStream os = null;
        try {
            // get the xslt stylesource and the DOMSource 
            StreamSource stylesource = new StreamSource(format);
            DOMSource source = new DOMSource(doc);
            
            os = new BufferedOutputStream(new FileOutputStream(output));
            StreamResult transformResult = new StreamResult(os);
            
            if ((stylesource != null) && (source != null)) { 
                // create the transformer
                TransformerFactory tFactory =
                    TransformerFactory.newInstance();
                Transformer transformer = tFactory.newTransformer(stylesource);
                
                transformer.transform(source, transformResult);
            } else {
                String msg = "Could not obtain style or XML source object";
                LogService.logWarn(msg, logger);
            }
            
        } catch (TransformerConfigurationException tce) {
            // Error generated by the parser
            String msg = "Transformer Factory error: " + tce.getMessage();
            Throwable x = tce;
            if (tce.getException() != null)
                x = tce.getException();
            LogService.logError(msg, logger, x);
        } catch (TransformerException te) {
            // Error generated by the parser
            String msg = "Transformer error: " + te.getMessage();
            Throwable x = te;
            if (te.getException() != null)
                x = te.getException();
            LogService.logError(msg, logger, x);
        } catch (FileNotFoundException fnfe) {
            // Could not open a file to write to
            String msg = "FileNotFoundException error";
            LogService.logError(msg, logger, fnfe);
        } finally {
            try {os.close();}
            catch (IOException ioe) {}
        }
    }
    
    public static Document getXmlDocument(File file) {
        String errorMsg = "Could not construct XML from input stream";
        Document doc = null;
        
        try {
            InputStream is = new FileInputStream(file);
            doc = getXmlDocument(is);
        } catch (IOException ioe) { 
            LogService.logError(errorMsg, logger, ioe);
            throw new XMLException(ioe);
        }
        
        return doc;
    }
 
    public static Document getXmlDocument(InputStream is) { 
        String errorMsg = "Could not construct XML from input stream";
        Document doc = null;
        try {
            DocumentBuilderFactory factory = 
                DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.parse(is);
        } catch (IOException ioe) {
            LogService.logError(errorMsg, logger, ioe);
            throw new XMLException(ioe);
        } catch (ParserConfigurationException pce) {
            LogService.logError(errorMsg, logger, pce);
            throw new XMLException(pce);
        } catch (SAXException se) {
            LogService.logError(errorMsg, logger, se);
            throw new XMLException(se);
        }
        
        return doc;
    }

    public static Document getXmlDocument(URL url) { 
        try {
            InputStream iStream = url.openStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int nextByte;
            while((nextByte = iStream.read()) != -1)
                baos.write(nextByte);
            iStream.close();

            return getXmlDocument(new ByteArrayInputStream(baos.toByteArray()));
        } catch (IOException e) {
            e.printStackTrace();
            throw new XMLException(e);
        }
    }

    public static org.w3c.dom.Document getXmlDocument() {
        org.w3c.dom.Document doc = null;
        try {
            DocumentBuilderFactory factory = 
                DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.newDocument();
        } catch (ParserConfigurationException pce) {
            LogService.logError("Could not construct DOM.", logger, pce);
            doc = null;
            throw new XMLException(pce);
        }
        
        return doc;
    }
}
