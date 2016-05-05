package gov.nih.nci.evs.reportwriter.bean;

import java.util.Collection;

import java.io.Serializable;
/**
	* 
	**/

public class StandardReportTemplate  implements Serializable
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
	
	private String rootConceptCode;
	/**
	* Retrieves the value of the rootConceptCode attribute
	* @return rootConceptCode
	**/

	public String getRootConceptCode(){
		return rootConceptCode;
	}

	/**
	* Sets the value of rootConceptCode attribute
	**/

	public void setRootConceptCode(String rootConceptCode){
		this.rootConceptCode = rootConceptCode;
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
	
	private String associationName;
	/**
	* Retrieves the value of the associationName attribute
	* @return associationName
	**/

	public String getAssociationName(){
		return associationName;
	}

	/**
	* Sets the value of associationName attribute
	**/

	public void setAssociationName(String associationName){
		this.associationName = associationName;
	}
	
	/**
	* 
	**/
	
	private Boolean direction;
	/**
	* Retrieves the value of the direction attribute
	* @return direction
	**/

	public Boolean getDirection(){
		return direction;
	}

	/**
	* Sets the value of direction attribute
	**/

	public void setDirection(Boolean direction){
		this.direction = direction;
	}
	
	/**
	* 
	**/
	
	private Integer level;
	/**
	* Retrieves the value of the level attribute
	* @return level
	**/

	public Integer getLevel(){
		return level;
	}

	/**
	* Sets the value of level attribute
	**/

	public void setLevel(Integer level){
		this.level = level;
	}
	
	/**
	* 
	**/
	
	private Character delimiter;
	/**
	* Retrieves the value of the delimiter attribute
	* @return delimiter
	**/

	public Character getDelimiter(){
		return delimiter;
	}

	/**
	* Sets the value of delimiter attribute
	**/

	public void setDelimiter(Character delimiter){
		this.delimiter = delimiter;
	}
	
	/**
	* An associated gov.nih.nci.evs.reportwriter.bean.StandardReport object's collection 
	**/
			
	private Collection<StandardReport> reportCollection;
	/**
	* Retrieves the value of the reportCollection attribute
	* @return reportCollection
	**/

	public Collection<StandardReport> getReportCollection(){
		return reportCollection;
	}

	/**
	* Sets the value of reportCollection attribute
	**/

	public void setReportCollection(Collection<StandardReport> reportCollection){
		this.reportCollection = reportCollection;
	}
		
	/**
	* An associated gov.nih.nci.evs.reportwriter.bean.ReportColumn object's collection 
	**/
			
	private Collection<ReportColumn> columnCollection;
	/**
	* Retrieves the value of the columnCollection attribute
	* @return columnCollection
	**/

	public Collection<ReportColumn> getColumnCollection(){
		return columnCollection;
	}

	/**
	* Sets the value of columnCollection attribute
	**/

	public void setColumnCollection(Collection<ReportColumn> columnCollection){
		this.columnCollection = columnCollection;
	}
		
	/**
	* Compares <code>obj</code> to it self and returns true if they both are same
	*
	* @param obj
	**/
	public boolean equals(Object obj)
	{
		if(obj instanceof StandardReportTemplate) 
		{
			StandardReportTemplate c =(StandardReportTemplate)obj; 			 
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