package com.techinfocom.nvis.agendartftoxml.utils;

import com.techinfocom.nvis.agendartftoxml.utils.model.agenda.Agenda;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.*;
import java.net.URISyntaxException;

/**
 * Created by volkov_kv on 22.10.2015.
 */
public class JaxbXmlCodec {
    private final static org.slf4j.Logger LOGGER = Logger.LOGGER;

    private final XMLOutputFactory xmlOutputFactory;
    private final Marshaller marshaller;

    private JaxbXmlCodec() throws InitException {
        try {
            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            File schemeFile = new File(JaxbXmlCodec.class.getClassLoader().getResource("agenda-internal-new.xsd").toURI());
            Schema schema = sf.newSchema(schemeFile);
            JAXBContext jaxbContextReq = JAXBContext.newInstance(Agenda.class);

            Marshaller m = jaxbContextReq.createMarshaller();
            //m.setSchema(schema);
            m.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            m.setProperty(Marshaller.JAXB_FRAGMENT, true);
            marshaller = m;

            xmlOutputFactory = XMLOutputFactory.newFactory();
        } catch (URISyntaxException | SAXException | JAXBException e) {
            throw new InitException("XML marshaller initialization error", e);
        }
    }

    public static JaxbXmlCodec getInstance() throws InitException {
        return new JaxbXmlCodec();
    }


    public byte[] marshalData(Object request) throws MarshalingException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            XMLStreamWriter xmlStreamWriter = xmlOutputFactory.createXMLStreamWriter(baos, (String) marshaller.getProperty(Marshaller.JAXB_ENCODING));
            xmlStreamWriter.writeStartDocument((String) marshaller.getProperty(Marshaller.JAXB_ENCODING), "1.0");
            marshaller.marshal(request, xmlStreamWriter);
            xmlStreamWriter.writeEndDocument();
            xmlStreamWriter.close();
        } catch (XMLStreamException | JAXBException e) {
            LOGGER.error("Ошибка преобразования в XML, {}", e.getStackTrace());
            throw new MarshalingException("Agenda marshalling error", e);
        }
        return baos.toByteArray();
    }
}