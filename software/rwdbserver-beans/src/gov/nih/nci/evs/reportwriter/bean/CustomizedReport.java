package gov.nih.nci.evs.reportwriter.bean;


import java.io.Serializable;
/**
	* 
	**/

public class CustomizedReport extends Report implements Serializable
{
	/**
	* An attribute to allow serialization of the domain objects
	*/
	private static final long serialVersionUID = 1234567890L;

	
	/**
	* An associated gov.nih.nci.evs.reportwriter.bean.CustomizedQuery object
	**/
			
	private CustomizedQuery customizedQuery;
	/**
	* Retrieves the value of the customizedQuery attribute
	* @return customizedQuery
	**/
	
	public CustomizedQuery getCustomizedQuery(){
		return customizedQuery;
	}
	/**
	* Sets the value of customizedQuery attribute
	**/

	public void setCustomizedQuery(CustomizedQuery customizedQuery){
		this.customizedQuery = customizedQuery;
	}
			
	/**
	* Compares <code>obj</code> to it self and returns true if they both are same
	*
	* @param obj
	**/
	public boolean equals(Object obj)
	{
		if(obj instanceof CustomizedReport) 
		{
			CustomizedReport c =(CustomizedReport)obj; 			 
			if(getId() != null && getId().equals(c.getId()))
				return true;
		}
		return false;
	}
		
	/**
	* Returns hash code for the primary key of the object
	**/
	public int hashCode()
	{
		if(getId() != null)
			return getId().hashCode();
		return 0;
	}
	
}