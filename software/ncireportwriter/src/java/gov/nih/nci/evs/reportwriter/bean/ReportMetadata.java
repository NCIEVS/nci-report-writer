package gov.nih.nci.evs.reportwriter.bean;

import java.io.*;
import java.util.*;
import java.net.*;

public class ReportMetadata
{

// Variable declaration
	private int id;
	private String label;
	private int templateId;
	private String templateLabel;
	private int formatId;
	private String format;
	private String codingScheme;
	private String version;
	private String createdBy;
	private String lastModified;
	private String status;
	private String pathName;

// Default constructor
	public ReportMetadata() {
	}

// Constructor
	public ReportMetadata(
		int id,
		String label,
		int templateId,
		String templateLabel,
		int formatId,
		String format,
		String codingScheme,
		String version,
		String createdBy,
		String lastModified,
		String status,
		String pathName) {

		this.id = id;
		this.label = label;
		this.templateId = templateId;
		this.templateLabel = templateLabel;
		this.formatId = formatId;
		this.format = format;
		this.codingScheme = codingScheme;
		this.version = version;
		this.createdBy = createdBy;
		this.lastModified = lastModified;
		this.status = status;
		this.pathName = pathName;
	}

// Set methods
	public void setId(int id) {
		this.id = id;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}

	public void setTemplateLabel(String templateLabel) {
		this.templateLabel = templateLabel;
	}

	public void setFormatId(int formatId) {
		this.formatId = formatId;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public void setCodingScheme(String codingScheme) {
		this.codingScheme = codingScheme;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setPathName(String pathName) {
		this.pathName = pathName;
	}


// Get methods
	public int getId() {
		return this.id;
	}

	public String getLabel() {
		return this.label;
	}

	public int getTemplateId() {
		return this.templateId;
	}

	public String getTemplateLabel() {
		return this.templateLabel;
	}

	public int getFormatId() {
		return this.formatId;
	}

	public String getFormat() {
		return this.format;
	}

	public String getCodingScheme() {
		return this.codingScheme;
	}

	public String getVersion() {
		return this.version;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public String getLastModified() {
		return this.lastModified;
	}

	public String getStatus() {
		return this.status;
	}

	public String getPathName() {
		return this.pathName;
	}

	public String toString() {
	    StringBuffer buf = new StringBuffer();
	    buf.append("id: " + id);
	    buf.append("\n" + "label: " + label);
	    buf.append("\n" + "templateId: " + templateId);
	    buf.append("\n" + "templateLabel: " + templateLabel);
	    buf.append("\n" + "formatId: " + formatId);
	    buf.append("\n" + "format: " + format);
	    buf.append("\n" + "codingScheme: " + codingScheme);
	    buf.append("\n" + "version: " + version);
	    buf.append("\n" + "createdBy: " + createdBy);
	    buf.append("\n" + "lastModified: " + lastModified);
	    buf.append("\n" + "status: " + status);
	    buf.append("\n" + "pathName: " + pathName);
        return buf.toString();
	}

}
