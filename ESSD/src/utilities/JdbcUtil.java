package utilities;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/* Utility for establishing a connection to the database */
public class JdbcUtil implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*
	 * Method that establishes a connection to the database
	 */
	public static Connection startConnection () {
		Connection conn = null;		
		DocumentBuilder dBuilder = null;
		Document doc = null;
		
		//Load XML file containing details of the Database
		File details = new File("ESSD_Files/JDBC.cfg.xml");
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		XPath xp = XPathFactory.newInstance().newXPath();
		
		try {
			dBuilder = dbFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
				
		try {
			doc = dBuilder.parse(details);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		doc.getDocumentElement().normalize();
		
		//Load MySQL Driver
	    try {
	    	Class.forName(xp.evaluate("/jdbc-connection-details/mysql-driver/text()", doc.getDocumentElement()));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
	    
	    //Setup the Connection
	    try {
			conn = DriverManager
					.getConnection(xp.evaluate("/jdbc-connection-details/url/text()", doc.getDocumentElement()),
							xp.evaluate("/jdbc-connection-details/user/text()", doc.getDocumentElement()),
							xp.evaluate("/jdbc-connection-details/password/text()", doc.getDocumentElement()));
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
	    
		return conn; //Return the Connection
	}
}
