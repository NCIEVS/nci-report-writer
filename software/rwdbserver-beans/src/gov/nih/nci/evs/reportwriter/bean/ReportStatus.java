package gov.nih.nci.evs.reportwriter.bean;


import java.io.Serializable;
/**
	* 
	**/

public class ReportStatus  implements Serializable
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
	
	private String description;
	/**
	* Retrieves the value of the description attribute
	* @return description
	**/

	public String getDescription(){
		return description;
	}

	/**
	* Sets the value of description attribute
	**/

	public void setDescription(String description){
		this.description = description;
	}
	
	/**
	* 
	**/
	
	private Boolean active;
	/**
	* Retrieves the value of the active attribute
	* @return active
	**/

	public Boolean getActive(){
		return active;
	}

	/**
	* Sets the value of active attribute
	**/

	public void setActive(Boolean active){
		this.active = active;
	}
	
	/**
	* Compares <code>obj</code> to it self and returns true if they both are same
	*
	* @param obj
	**/
	public boolean equals(Object obj)
	{
		if(obj instanceof ReportStatus) 
		{
			ReportStatus c =(ReportStatus)obj; 			 
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