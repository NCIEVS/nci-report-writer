package gov.nih.nci.evs.reportwriter.bean;


import java.io.Serializable;
/**
	* 
	**/

public class StandardReport extends Report implements Serializable
{
	/**
	* An attribute to allow serialization of the domain objects
	*/
	private static final long serialVersionUID = 1234567890L;

	
	/**
	* An associated gov.nih.nci.evs.reportwriter.bean.StandardReportTemplate object
	**/
			
	private StandardReportTemplate template;
	/**
	* Retrieves the value of the template attribute
	* @return template
	**/
	
	public StandardReportTemplate getTemplate(){
		return template;
	}
	/**
	* Sets the value of template attribute
	**/

	public void setTemplate(StandardReportTemplate template){
		this.template = template;
	}
			
	/**
	* Compares <code>obj</code> to it self and returns true if they both are same
	*
	* @param obj
	**/
	public boolean equals(Object obj)
	{
		if(obj instanceof StandardReport) 
		{
			StandardReport c =(StandardReport)obj; 			 
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