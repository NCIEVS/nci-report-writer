package gov.nih.nci.evs.reportwriter.bean;

import java.util.Collection;

import java.io.Serializable;
/**
	* 
	**/

public class CustomizedQuery  implements Serializable
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
	
	private String queryExpression;
	/**
	* Retrieves the value of the queryExpression attribute
	* @return queryExpression
	**/

	public String getQueryExpression(){
		return queryExpression;
	}

	/**
	* Sets the value of queryExpression attribute
	**/

	public void setQueryExpression(String queryExpression){
		this.queryExpression = queryExpression;
	}
	
	/**
	* 
	**/
	
	private String codingSchemeName;
	/**
	* Retrieves the value of the codingSchemeName attribute
	* @return codingSchemeName
	**/

	public String getCodingSchemeName(){
		return codingSchemeName;
	}

	/**
	* Sets the value of codingSchemeName attribute
	**/

	public void setCodingSchemeName(String codingSchemeName){
		this.codingSchemeName = codingSchemeName;
	}
	
	/**
	* 
	**/
	
	private String codingSchemeVersion;
	/**
	* Retrieves the value of the codingSchemeVersion attribute
	* @return codingSchemeVersion
	**/

	public String getCodingSchemeVersion(){
		return codingSchemeVersion;
	}

	/**
	* Sets the value of codingSchemeVersion attribute
	**/

	public void setCodingSchemeVersion(String codingSchemeVersion){
		this.codingSchemeVersion = codingSchemeVersion;
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
	* An associated gov.nih.nci.evs.reportwriter.bean.User object
	**/
			
	private User user;
	/**
	* Retrieves the value of the user attribute
	* @return user
	**/
	
	public User getUser(){
		return user;
	}
	/**
	* Sets the value of user attribute
	**/

	public void setUser(User user){
		this.user = user;
	}
			
	/**
	* An associated gov.nih.nci.evs.reportwriter.bean.CustomizedReport object's collection 
	**/
			
	private Collection<CustomizedReport> reportCollection;
	/**
	* Retrieves the value of the reportCollection attribute
	* @return reportCollection
	**/

	public Collection<CustomizedReport> getReportCollection(){
		return reportCollection;
	}

	/**
	* Sets the value of reportCollection attribute
	**/

	public void setReportCollection(Collection<CustomizedReport> reportCollection){
		this.reportCollection = reportCollection;
	}
		
	/**
	* Compares <code>obj</code> to it self and returns true if they both are same
	*
	* @param obj
	**/
	public boolean equals(Object obj)
	{
		if(obj instanceof CustomizedQuery) 
		{
			CustomizedQuery c =(CustomizedQuery)obj; 			 
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