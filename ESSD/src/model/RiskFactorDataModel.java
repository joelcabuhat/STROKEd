package model;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

@ManagedBean
public class RiskFactorDataModel extends ListDataModel<RiskFactor> implements SelectableDataModel<RiskFactor>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*
	 * Constructor
	 */
	public RiskFactorDataModel () {
		
	}
  
	/*
	 * Constructor with parameter data
	 */
    public RiskFactorDataModel(List<RiskFactor> data) {  
        super(data);  
    }  
      
    @SuppressWarnings("unchecked")
	@Override  
	/*
	 * (non-Javadoc)
	 * @see org.primefaces.model.SelectableDataModel#getRowData(java.lang.String)
	 */
    public RiskFactor getRowData(String rowKey) {  
        List<RiskFactor> rf = (List<RiskFactor>) getWrappedData();  
          
        for(RiskFactor i : rf) {  
	        if(i.getRf_id().equals(rowKey))  
	            return i;  
        }  
          
        return null;  
    }  
  
    @Override  
    /*
     * (non-Javadoc)
     * @see org.primefaces.model.SelectableDataModel#getRowKey(java.lang.Object)
     */
    public Object getRowKey(RiskFactor rf) {  
        return rf.getRf_id();  
    }  
}
