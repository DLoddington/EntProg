package Utils;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import model.*;

public class XmlUtils {
	
	public static String serializeXML(FilmList inputList){
		String xmlToReturn = "";
		
		try{
			JAXBContext context = JAXBContext.newInstance(FilmList.class);
	        Marshaller m = context.createMarshaller();
	        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
	        StringWriter sWrite = new StringWriter();
	        m.marshal(inputList, sWrite);
	        xmlToReturn = sWrite.toString();
		} catch(JAXBException je) { System.out.print(je);}
  
        return xmlToReturn;
	}
	
	public static String serializeXMLMessage(Message inputMessage){
		String xmlToReturn = "";
		
		try{
			JAXBContext context = JAXBContext.newInstance(Message.class);
	        Marshaller m = context.createMarshaller();
	        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
	        StringWriter sWrite = new StringWriter();
	        m.marshal(inputMessage, sWrite);
	        xmlToReturn = sWrite.toString();
		} catch(JAXBException je) { System.out.print(je);}
  
        return xmlToReturn;
	}
	
	public static Film desearializeXML(String xmlInput) {
		Film filmToReturn = null;

		try {
			JAXBContext context = JAXBContext.newInstance(Film.class);
			Unmarshaller um = context.createUnmarshaller();
			StringReader sr = new StringReader(xmlInput);
			filmToReturn = (Film) um.unmarshal(sr);
		} catch (JAXBException je) {System.out.print(je);}

		return filmToReturn;
	}

}
