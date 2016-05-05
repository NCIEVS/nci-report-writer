package gov.nih.nci.evs.reportwriter.bean;


import java.io.Serializable;
/**
	* 
	**/

public class ReportColumn  implements Serializable
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
	
	private Integer columnNumber;
	/**
	* Retrieves the value of the columnNumber attribute
	* @return columnNumber
	**/

	public Integer getColumnNumber(){
		return columnNumber;
	}

	/**
	* Sets the value of columnNumber attribute
	**/

	public void setColumnNumber(Integer columnNumber){
		this.columnNumber = columnNumber;
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
	
	private String fieldId;
	/**
	* Retrieves the value of the fieldId attribute
	* @return fieldId
	**/

	public String getFieldId(){
		return fieldId;
	}

	/**
	* Sets the value of fieldId attribute
	**/

	public void setFieldId(String fieldId){
		this.fieldId = fieldId;
	}
	
	/**
	* 
	**/
	
	private String propertyType;
	/**
	* Retrieves the value of the propertyType attribute
	* @return propertyType
	**/

	public String getPropertyType(){
		return propertyType;
	}

	/**
	* Sets the value of propertyType attribute
	**/

	public void setPropertyType(String propertyType){
		this.propertyType = propertyType;
	}
	
	/**
	* 
	**/
	
	private String propertyName;
	/**
	* Retrieves the value of the propertyName attribute
	* @return propertyName
	**/

	public String getPropertyName(){
		return propertyName;
	}

	/**
	* Sets the value of propertyName attribute
	**/

	public void setPropertyName(String propertyName){
		this.propertyName = propertyName;
	}
	
	/**
	* 
	**/
	
	private Boolean isPreferred;
	/**
	* Retrieves the value of the isPreferred attribute
	* @return isPreferred
	**/

	public Boolean getIsPreferred(){
		return isPreferred;
	}

	/**
	* Sets the value of isPreferred attribute
	**/

	public void setIsPreferred(Boolean isPreferred){
		this.isPreferred = isPreferred;
	}
	
	/**
	* 
	**/
	
	private String representationalForm;
	/**
	* Retrieves the value of the representationalForm attribute
	* @return representationalForm
	**/

	public String getRepresentationalForm(){
		return representationalForm;
	}

	/**
	* Sets the value of representationalForm attribute
	**/

	public void setRepresentationalForm(String representationalForm){
		this.representationalForm = representationalForm;
	}
	
	/**
	* 
	**/
	
	private String source;
	/**
	* Retrieves the value of the source attribute
	* @return source
	**/

	public String getSource(){
		return source;
	}

	/**
	* Sets the value of source attribute
	**/

	public void setSource(String source){
		this.source = source;
	}
	
	/**
	* 
	**/
	
	private String qualifierName;
	/**
	* Retrieves the value of the qualifierName attribute
	* @return qualifierName
	**/

	public String getQualifierName(){
		return qualifierName;
	}

	/**
	* Sets the value of qualifierName attribute
	**/

	public void setQualifierName(String qualifierName){
		this.qualifierName = qualifierName;
	}
	
	/**
	* 
	**/
	
	private String qualifierValue;
	/**
	* Retrieves the value of the qualifierValue attribute
	* @return qualifierValue
	**/

	public String getQualifierValue(){
		return qualifierValue;
	}

	/**
	* Sets the value of qualifierValue attribute
	**/

	public void setQualifierValue(String qualifierValue){
		this.qualifierValue = qualifierValue;
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
	* 
	**/
	
	private Integer conditionalColumnId;
	/**
	* Retrieves the value of the conditionalColumnId attribute
	* @return conditionalColumnId
	**/

	public Integer getConditionalColumnId(){
		return conditionalColumnId;
	}

	/**
	* Sets the value of conditionalColumnId attribute
	**/

	public void setConditionalColumnId(Integer conditionalColumnId){
		this.conditionalColumnId = conditionalColumnId;
	}
	
	/**
	* An associated gov.nih.nci.evs.reportwriter.bean.StandardReportTemplate object
	**/
			
	private StandardReportTemplate reportTemplate;
	/**
	* Retrieves the value of the reportTemplate attribute
	* @return reportTemplate
	**/
	
	public StandardReportTemplate getReportTemplate(){
		return reportTemplate;
	}
	/**
	* Sets the value of reportTemplate attribute
	**/

	public void setReportTemplate(StandardReportTemplate reportTemplate){
		this.reportTemplate = reportTemplate;
	}
			
	/**
	* Compares <code>obj</code> to it self and returns true if they both are same
	*
	* @param obj
	**/
	public boolean equals(Object obj)
	{
		if(obj instanceof ReportColumn) 
		{
			ReportColumn c =(ReportColumn)obj; 			 
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