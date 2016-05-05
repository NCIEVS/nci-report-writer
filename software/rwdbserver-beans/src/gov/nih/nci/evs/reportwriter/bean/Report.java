package gov.nih.nci.evs.reportwriter.bean;


import java.io.Serializable;
/**
	* 
	**/

public class Report  implements Serializable
{
	/**
	* An attribute to allow serialization of the domain objects
	*/
	private static final long serialVersionUID = 1234567890L;

	
	/**
	* 
	**/
	
	private Integer id;
	/**
	* Retrieves the value of the id attribute
	* @return id
	**/

	public Integer getId(){
		return id;
	}

	/**
	* Sets the value of id attribute
	**/

	public void setId(Integer id){
		this.id = id;
	}
	
	/**
	* 
	**/
	
	private String label;
	/**
	* Retrieves the value of the label attribute
	* @return label
	**/

	public String getLabel(){
		return label;
	}

	/**
	* Sets the value of label attribute
	**/

	public void setLabel(String label){
		this.label = label;
	}
	
	/**
	* 
	**/
	
	private java.util.Date lastModified;
	/**
	* Retrieves the value of the lastModified attribute
	* @return lastModified
	**/

	public java.util.Date getLastModified(){
		return lastModified;
	}

	/**
	* Sets the value of lastModified attribute
	**/

	public void setLastModified(java.util.Date lastModified){
		this.lastModified = lastModified;
	}
	
	/**
	* 
	**/
	
	private String pathName;
	/**
	* Retrieves the value of the pathName attribute
	* @return pathName
	**/

	public String getPathName(){
		return pathName;
	}

	/**
	* Sets the value of pathName attribute
	**/

	public void setPathName(String pathName){
		this.pathName = pathName;
	}
	
	/**
	* An associated gov.nih.nci.evs.reportwriter.bean.ReportFormat object
	**/
			
	private ReportFormat format;
	/**
	* Retrieves the value of the format attribute
	* @return format
	**/
	
	public ReportFormat getFormat(){
		return format;
	}
	/**
	* Sets the value of format attribute
	**/

	public void setFormat(ReportFormat format){
		this.format = format;
	}
			
	/**
	* An associated gov.nih.nci.evs.reportwriter.bean.User object
	**/
			
	private User createdBy;
	/**
	* Retrieves the value of the createdBy attribute
	* @return createdBy
	**/
	
	public User getCreatedBy(){
		return createdBy;
	}
	/**
	* Sets the value of createdBy attribute
	**/

	public void setCreatedBy(User createdBy){
		this.createdBy = createdBy;
	}
			
	/**
	* An associated gov.nih.nci.evs.reportwriter.bean.ReportStatus object
	**/
			
	private ReportStatus status;
	/**
	* Retrieves the value of the status attribute
	* @return status
	**/
	
	public ReportStatus getStatus(){
		return status;
	}
	/**
	* Sets the value of status attribute
	**/

	public void setStatus(ReportStatus status){
		this.status = status;
	}
			
	/**
	* An associated gov.nih.nci.evs.reportwriter.bean.User object
	**/
			
	private User modifiedBy;
	/**
	* Retrieves the value of the modifiedBy attribute
	* @return modifiedBy
	**/
	
	public User getModifiedBy(){
		return modifiedBy;
	}
	/**
	* Sets the value of modifiedBy attribute
	**/

	public void setModifiedBy(User modifiedBy){
		this.modifiedBy = modifiedBy;
	}
			
	/**
	* Compares <code>obj</code> to it self and returns true if they both are same
	*
	* @param obj
	**/
	public boolean equals(Object obj)
	{
		if(obj instanceof Report) 
		{
			Report c =(Report)obj; 			 
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