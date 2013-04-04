package model;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;


/**
 * @author Arisa C. Ochavez
 *
 */
@ManagedBean
public class UserDataModel extends ListDataModel<User> implements SelectableDataModel<User>, Serializable {

	/**
	 * Global Variables
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 */
	public UserDataModel () {
		
	}
  
	/**
	 * Constructor
	 * 
	 * @param data - list of users
	 */
	public UserDataModel(List<User> data) {  
        super(data);  
    }  
      
    @SuppressWarnings("unchecked")
	@Override  
	/*
	 * (non-Javadoc)
	 * @see org.primefaces.model.SelectableDataModel#getRowData(java.lang.String)
	 */
	public User getRowData(String rowKey) {  
        List<User> user = (List<User>) getWrappedData();  
          
        for(User i : user) {  
	        if(i.getUsId().equals(rowKey))  
	            return i;  
        }  
          
        return null;  
    }  
  
    @Override  
    /*
     * (non-Javadoc)
     * @see org.primefaces.model.SelectableDataModel#getRowKey(java.lang.Object)
     */
    public Object getRowKey(User rf) {  
        return rf.getUsId();  
    }  
}
