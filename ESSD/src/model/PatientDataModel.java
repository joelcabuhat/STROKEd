package model;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

@ManagedBean
public class PatientDataModel extends ListDataModel<Patient> implements SelectableDataModel<Patient>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*
	 * Constructor
	 */
	public PatientDataModel () {
		
	}
  
	/*
	 * Constructor with parameter data
	 */
    public PatientDataModel(List<Patient> data) {  
        super(data);  
    }  
      
    @SuppressWarnings("unchecked")
	@Override  
	/*
	 * (non-Javadoc)
	 * @see org.primefaces.model.SelectableDataModel#getRowData(java.lang.String)
	 */
    public Patient getRowData(String rowKey) {  
        List<Patient> p = (List<Patient>) getWrappedData();  
          
        for(Patient i : p) {  
	        if(i.getCaseNum()==Integer.parseInt(rowKey))  
	            return i;  
        }  
          
        return null;  
    }  
  
    @Override  
    /*
     * (non-Javadoc)
     * @see org.primefaces.model.SelectableDataModel#getRowKey(java.lang.Object)
     */
    public Object getRowKey(Patient p) {  
        return p.getCaseNum();
    }  
}
