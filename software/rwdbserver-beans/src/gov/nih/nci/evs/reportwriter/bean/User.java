package gov.nih.nci.evs.reportwriter.bean;

import java.util.Collection;

import java.io.Serializable;
/**
	* 
	**/

public class User  implements Serializable
{
	/**
	* An attribute to allow serialization of the domain objects
	*/
	private static final long serialVersionUID = 1234567890L;

	
	/**
	* 
	**/
	
	private Long id;
	/**
	* Retrieves the value of the id attribute
	* @return id
	**/

	public Long getId(){
		return id;
	}

	/**
	* Sets the value of id attribute
	**/

	public void setId(Long id){
		this.id = id;
	}
	
	/**
	* 
	**/
	
	private String loginName;
	/**
	* Retrieves the value of the loginName attribute
	* @return loginName
	**/

	public String getLoginName(){
		return loginName;
	}

	/**
	* Sets the value of loginName attribute
	**/

	public void setLoginName(String loginName){
		this.loginName = loginName;
	}
	
	/**
	* An associated gov.nih.nci.evs.reportwriter.bean.CustomizedQuery object's collection 
	**/
			
	private Collection<CustomizedQuery> queryCollection;
	/**
	* Retrieves the value of the queryCollection attribute
	* @return queryCollection
	**/

	public Collection<CustomizedQuery> getQueryCollection(){
		return queryCollection;
	}

	/**
	* Sets the value of queryCollection attribute
	**/

	public void setQueryCollection(Collection<CustomizedQuery> queryCollection){
		this.queryCollection = queryCollection;
	}
		
	/**
	* An associated gov.nih.nci.evs.reportwriter.bean.Report object's collection 
	**/
			
	private Collection<Report> createdReportCollection;
	/**
	* Retrieves the value of the createdReportCollection attribute
	* @return createdReportCollection
	**/

	public Collection<Report> getCreatedReportCollection(){
		return createdReportCollection;
	}

	/**
	* Sets the value of createdReportCollection attribute
	**/

	public void setCreatedReportCollection(Collection<Report> createdReportCollection){
		this.createdReportCollection = createdReportCollection;
	}
		
	/**
	* An associated gov.nih.nci.evs.reportwriter.bean.Report object's collection 
	**/
			
	private Collection<Report> modifiedReportCollection;
	/**
	* Retrieves the value of the modifiedReportCollection attribute
	* @return modifiedReportCollection
	**/

	public Collection<Report> getModifiedReportCollection(){
		return modifiedReportCollection;
	}

	/**
	* Sets the value of modifiedReportCollection attribute
	**/

	public void setModifiedReportCollection(Collection<Report> modifiedReportCollection){
		this.modifiedReportCollection = modifiedReportCollection;
	}
		
	/**
	* Compares <code>obj</code> to it self and returns true if they both are same
	*
	* @param obj
	**/
	public boolean equals(Object obj)
	{
		if(obj instanceof User) 
		{
			User c =(User)obj; 			 
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