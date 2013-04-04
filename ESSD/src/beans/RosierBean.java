package beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.*;
import javax.faces.event.*;

import model.Question;
import model.RiskFactor;
import model.RiskFactorDataModel;
import model.StrokeType;

/**
 * Handles all the storing and manipulation of ROSIER entry for each patient.
 * 
 *
 */
@ManagedBean
@SessionScoped
public class RosierBean implements Serializable {
		
	private static final long serialVersionUID = 1L;
	public static List<Question> rq1;
	public static List<Question> rq2;
	public static Result result;
	public static List<Question> holderID;

	
	/**
	 * Gets the ROSIER diagnosis for each patient.
	 * @return The result of the ROSIER diagnosis.
	 */
	public Result getResult() {
		return result;
	}
	/**
	 * Sets the ROSIER diagnosis.
	 * @param result The output of ROSIER scoring system.
	 */
	public void setResult(Result result) {
		RosierBean.result = result;
	}	
	
	/**
	 * Gets the true or false value for the first 2 questions in ROSIER tab.
	 * @return List of true/false values. 
	 */
	public List<Question> getRq1() {
		return rq1;
	}
	/**
	 * Sets the list of true/false value for the first 2 questions in ROSIER tab.
	 * @param rq1 List of true/false values.
	 */
	public void setRq1(List<Question> rq1) {
		this.rq1 = rq1;
	}	
	/**
	 * Gets the true/false value for the last 5 questions in ROSIER tab.
	 * @return List of true/false values. 
	 */
	public List<Question> getRq2() {
		return rq2;
	}
	/**
	 * Sets the list of true/false value for the last 5 questions in ROSIER tab.
	 * @param rq2 List of true/false values. 
	 */
	public void setRq2(List<Question> rq2) {
		this.rq2 = rq2;
	}	
	
	/**
	 * Constructor, initializes the default values for the ROSIER part of the application.
	 */
	public RosierBean () {
		result = new Result();
		String desc="Patients with a score of 0, -1 or -2 have a low possibility of stroke but not completely excluded. Patient should be discussed with the stroke team and be admitted to the EAU at the RVI.";	
		result=new Result("Non-stroke",0,desc);
		initializeTable();
	}
	
	/**
	 * Resets the default values of the ROSIER part of the application.
	 */
	public static void start () {
		initializeTable();
	}
	/**
	 * Initializes the questions of the ROSIER table.
	 */
	public static void initializeTable(){
		rq1 = new ArrayList<Question>();
		rq2 = new ArrayList<Question>();
		holderID = new ArrayList<Question>();
		
		rq1.add(new Question("Has there been loss of consciousness or syncope?","false"));
		rq1.add(new Question("Has there been seizure activity?","false"));
		holderID.add(new Question("form:acc:bigTable:0:rques1:0:j_idt394","No"));
		holderID.add(new Question("form:acc:bigTable:0:rques1:1:j_idt394","No"));
			
		rq2.add(new Question("Asymmetric facial weakness","false"));
		rq2.add(new Question("Asymmetric arm weakness","false"));
		rq2.add(new Question("Asymmetric leg weakness","false"));
		rq2.add(new Question("Speech disturbance","false"));
		rq2.add(new Question("Visual field defect","false"));
		
		holderID.add(new Question("form:acc:bigTable:0:rques2:0:j_idt406","No"));
		holderID.add(new Question("form:acc:bigTable:0:rques2:1:j_idt406","No"));
		holderID.add(new Question("form:acc:bigTable:0:rques2:2:j_idt406","No"));
		holderID.add(new Question("form:acc:bigTable:0:rques2:3:j_idt406","No"));
		holderID.add(new Question("form:acc:bigTable:0:rques2:4:j_idt406","No"));
	}
	

	/**
	 * Decrements the score value whenever a checkbox of the first 2 questions in ROSIER tab is checked.
	 * @param event Select and unselect a checkbox.
	 */
	public void decrement(AjaxBehaviorEvent event) {     
		Boolean value = (Boolean) ((UIInput) event.getComponent()).getValue();
		String val = (String) ((UIInput) event.getComponent()).getClientId();		
		String temp="";
		Iterator<Question> it=holderID.iterator();
		int ctr=0;
		while(it.hasNext())
        {
          Question quest=(Question)it.next();
          if(quest.getQuestion().equals(val)){     	 
        	  if(value==true)
        		  temp="Yes";
        	  else
        		  temp="No";
        	  quest.setValue(temp);
        	  holderID.get(ctr).setValue(temp);
          }
          ctr++;
        }
		if (value) { 			
			result.setScore(result.getScore()-1);			
		} else {
			result.setScore(result.getScore()+1);
		}		
		whatDiagnosis();
	}
	
	/**
	 * Increments the score value whenever a checkbox of the last 5 questions in ROSIER tab is checked.
	 * @param event Select and unselect a checkbox.
	 */
	public void increment(AjaxBehaviorEvent event) {
	
		Boolean value = (Boolean) ((UIInput) event.getComponent()).getValue();
		String val = (String) ((UIInput) event.getComponent()).getClientId();
		String temp="";
		int ctr=0;
		Iterator<Question> it=holderID.iterator();
		while(it.hasNext())
        {
          Question quest=(Question)it.next();
          if(quest.getQuestion().equals(val)){
        	  if(value==true)
        		  temp="Yes";
        	  else
        		  temp="No";
        	  quest.setValue(temp);
        	  holderID.get(ctr).setValue(temp);
          }
          ctr++;
        }		
		if (value) { 			
			result.setScore(result.getScore()+1);			
		} else {
			result.setScore(result.getScore()-1);
		}		
		whatDiagnosis();
	}
	
	/**
	 * Determines the diagnosis of ROSIER scoring system given the score of the patient.
	 */
	public static void whatDiagnosis(){
		String desc1="Patients with a score of 0, -1 or -2 have a low possibility of stroke but not completely excluded. Patient should be discussed with the stroke team and be admitted to the EAU at the RVI.";
		String desc2="If total score > 0 (1 to 6) a diagnosis of acute stroke is likely. All patients admitted with a suspected stroke, irrespective of score should be admitted to the Emergency Admissions Unit (EAU) at the RVI.";
		if(result.getScore()>0){
			result.setDiagnosis("Stroke");
			result.setDescription(desc2);
		}
		else{
			result.setDiagnosis("Non-stroke");
			result.setDescription(desc1);
		}	
	}

	
	/**
	 * 
	 * Model and structure of the result of ROSIER diagnosis.
	 *
	 */
	public class Result {

		private int score;
		private String diagnosis;
		private String description;

		public Result () {		
		}
		public Result (String diagnosis,int score, String description) {
			this.score = score;
			this.diagnosis=diagnosis;
			this.description=description;
		}
		public int getScore() {
			return score;
		}
		public void setScore(int score) {
			this.score = score;
		}
		public String getDiagnosis() {
			return diagnosis;
		}
		public void setDiagnosis(String diagnosis) {
			this.diagnosis = diagnosis;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
	}

}