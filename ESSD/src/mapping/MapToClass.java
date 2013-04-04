package mapping;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import model.Patient;
import model.RiskFactor;
import model.StrokeType;
import model.User;
import beans.InferenceBean;

/**
 * @author Arisa C. Ochavez
 *
 */
public class MapToClass {

	/**
	 * Constructor
	 */
	public MapToClass () {
		
	}
	
	/**
	 * Map information retrieved from the database to the User class
	 * 
	 * @param user - variable where the information will be mapped
	 * @param rs - pointer to the result of database query
	 * @return user - variable containing the user information from the database
	 * @throws SQLException
	 */
	public static User mapUser (User user, ResultSet rs) throws SQLException {
		user.setUsId(Integer.toString(rs.getInt("user_id")));
		user.setUserId(rs.getInt("user_id"));
		user.setUsername(rs.getString("username"));
		user.setPassword(rs.getString("password"));
		user.setName(rs.getString("name"));
		user.setLicense(rs.getString("license"));
		user.setType(rs.getString("type"));
		user.setUser_type(rs.getString("user_type"));
		
		return user;
	}
	
<<<<<<< HEAD
	/**
	 * Maps the patient information retrieved from the database to the Patient class for learning
	 * 
	 * @param p - variable where the information will be mapped
	 * @param rs - pointer to the result of the database query
	 * @return p - variable containing the patient information from the database
	 * @throws SQLException
=======

	/**
	 * Maps the patient to be fed to network for learning retrieved from the database to the Patient class.
	 * @param p Patient model.
	 * @param rs Result set retreived from database.
	 * @return Mapped patient.
	 * @throws SQLException Access to mysql database.
>>>>>>> 66344b793f54b1ddd4903dfbf630566b6b242634
	 */
	public static Patient mapPLearnNet(Patient p, ResultSet rs) throws SQLException {
		p.setCaseNum(rs.getInt("case_num"));
		p.setCaseNumStr(""+rs.getInt("case_num"));
		return p;
	}
	
	/**
	 * Maps the risk factors retrieved from the database to the RiskFactor class
	 * 
	 * @param rf - variable where the information will be mapped
	 * @param rs - pointer to the result of the database query
	 * @return rf - variable containing the risk factor information from the database
	 * @throws SQLException
	 */
	public static RiskFactor mapRf (RiskFactor rf, ResultSet rs) throws SQLException {
		rf.setRf_id(Integer.toString(rs.getInt("rf_id")));
		rf.setName(rs.getString("name"));
		rf.setRange(rs.getString("range"));
		rf.setDescription(rs.getString("description"));
		rf.setHistory(rs.getString("history"));
		rf.setSigns_symptoms(rs.getString("signs_symptoms"));
		rf.setPhysical_exams(rs.getString("physical_exams"));
		rf.setLaboratory_exams(rs.getString("laboratory_exams"));
		rf.setGeneral_measures(rs.getString("general_measures"));
		rf.setSpecific_measures(rs.getString("specific_measures"));
		rf.setCreated_date(rs.getTimestamp("created_date"));
		rf.setLast_update_date(rs.getTimestamp("last_update_date"));
		rf.setCreated_by(rs.getInt("created_by"));
		rf.setLast_update_by(rs.getInt("last_update_by"));
		rf.setRangeValues();
		rf.setDisplay(true);
		rf.setToEdit((rs.getString("to_edit").equals("Yes")) ? true : false);
		
		/*set initial probability
		 *if the states are only Present or Absent, set it to probability of Absent
		 *else show the probability of the first in its range values*/
			if (rf.getRangeValues().isEmpty())
				rf.setProbability((int)((InferenceBean.getProbValue(rf.getName(), "Present")*100)));
			else 
				rf.setProbability((int)((InferenceBean.getProbValue(rf.getName(), rf.getRangeValues().get(0))*100)));
			
		return rf;
	}
	
	/**
	 * Maps the stroke types retrieved from the database to the StrokeType class
	 * 
	 * @param type - variable where the information will be mapped
	 * @param rs - pointer for the result of the database query
	 * @return type - variable containing the stroke type information from the database
	 * @throws SQLException
	 */
	public static StrokeType mapSt (StrokeType type, ResultSet rs) throws SQLException {
		type.setName(rs.getString("name"));
		type.setDescription(rs.getString("description"));
		type.setProbability(InferenceBean.getProbValue("Stroke", rs.getString("name")));
		
		return type;
	}
<<<<<<< HEAD

	/**
	 * Maps the patient information retrieved from the database to the Patient class
	 * 
	 * @param p - variable where the information will be mapped
	 * @param rs - pointer to the result of the database query
	 * @return p - variable containing the patient information from the database
	 * @throws SQLException
=======
	
	/**
	 * Maps the patient for patient information tab from the database to the Patient class.
	 * @param p Patient.
	 * @param rs Result set queried from database;
	 * @return Patient.
	 * @throws SQLException Access to mysql database.
>>>>>>> 66344b793f54b1ddd4903dfbf630566b6b242634
	 */
	public static Patient mapP(Patient p, ResultSet rs) throws SQLException {
		p.setCaseNum(rs.getInt("case_num"));
		DocumentBuilder dBuilder = null;
		Document doc = null;
		File details = new File("ESSD_Files/Patient_DB/"+rs.getInt("case_num")+".xml");
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
		
		try {
			p.setCaseNumStr(p.getCaseNum()+"");
			p.setDoctor(Integer.parseInt(xp.evaluate("/data/others/doctor/text()", doc.getDocumentElement())));
			p.setComplaint(xp.evaluate("/data/others/complaint/text()", doc.getDocumentElement()));		
			String tempQ=xp.evaluate("/data/rosier/questions/text()", doc.getDocumentElement());
			String [] rosQArray=tempQ.split("#");
			List<String> rosQList=new ArrayList<String>();
			for(String i:rosQArray){
				rosQList.add(i);
			}
			
			p.setRosQuestions(rosQList);		
			p.setRosDiagnosis(xp.evaluate("/data/rosier/diagnosis/text()", doc.getDocumentElement()));
			p.setRosScore(Integer.parseInt(xp.evaluate("/data/rosier/score/text()", doc.getDocumentElement())));
<<<<<<< HEAD
			
			String tempRf=xp.evaluate("/data/spt/risk-factors/text()", doc.getDocumentElement());
			String[] rfArray=tempRf.split("@");
			
			List<String> rfList=new ArrayList<String>();
			List<String> nameRfs=new ArrayList<String>();
			List<String> rangeRfs=new ArrayList<String>();
			
=======
			String tempRf=xp.evaluate("/data/spt/risk-factors/text()", doc.getDocumentElement());
			String[] rfArray=tempRf.split("@");
			List<String> rfList=new ArrayList<String>();
			List<String> nameRfs=new ArrayList<String>();
			List<String> rangeRfs=new ArrayList<String>();
>>>>>>> 66344b793f54b1ddd4903dfbf630566b6b242634
			if(rfArray.length>0 && !rfArray[0].equals("")){
				for(String i:rfArray){
					rfList.add(i);
					String[] arraySplit=i.split("#");
<<<<<<< HEAD
					nameRfs.add(arraySplit[8]);
					rangeRfs.add(arraySplit[14]);
=======
						nameRfs.add(arraySplit[8]);
						rangeRfs.add(arraySplit[14]);
>>>>>>> 66344b793f54b1ddd4903dfbf630566b6b242634
				}
			}
			
			p.setListRf(rfList);
			p.setNameRfs(nameRfs);
			p.setRangeRfs(rangeRfs);
			p.setProbHemorrhagic(Double.parseDouble(xp.evaluate("/data/spt/hemorrhagic-stroke/text()", doc.getDocumentElement())));
			p.setProbIschemic(Double.parseDouble(xp.evaluate("/data/spt/ischemic-stroke/text()", doc.getDocumentElement())));
			p.setProbNone(Double.parseDouble(xp.evaluate("/data/spt/none/text()", doc.getDocumentElement())));
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
	
		return p;
	}	
}
