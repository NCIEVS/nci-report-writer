package gov.nih.nci.evs.app.neopl;


import java.io.*;
import java.net.*;
import java.util.*;


/**
 * <!-- LICENSE_TEXT_START -->
 * Copyright 2008-2016 NGIS. This software was developed in conjunction
 * with the National Cancer Institute, and so to the extent government
 * employees are co-authors, any rights in such works shall be subject
 * to Title 17 of the United States Code, section 105.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *   1. Redistributions of source code must retain the above copyright
 *      notice, this list of conditions and the disclaimer of Article 3,
 *      below. Redistributions in binary form must reproduce the above
 *      copyright notice, this list of conditions and the following
 *      disclaimer in the documentation and/or other materials provided
 *      with the distribution.
 *   2. The end-user documentation included with the redistribution,
 *      if any, must include the following acknowledgment:
 *      "This product includes software developed by NGIS and the National
 *      Cancer Institute."   If no such end-user documentation is to be
 *      included, this acknowledgment shall appear in the software itself,
 *      wherever such third-party acknowledgments normally appear.
 *   3. The names "The National Cancer Institute", "NCI" and "NGIS" must
 *      not be used to endorse or promote products derived from this software.
 *   4. This license does not authorize the incorporation of this software
 *      into any third party proprietary programs. This license does not
 *      authorize the recipient to use any trademarks owned by either NCI
 *      or NGIS
 *   5. THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESSED OR IMPLIED
 *      WARRANTIES, (INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *      OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE) ARE
 *      DISCLAIMED. IN NO EVENT SHALL THE NATIONAL CANCER INSTITUTE,
 *      NGIS, OR THEIR AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT,
 *      INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 *      BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *      LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 *      CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 *      LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
 *      ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 *      POSSIBILITY OF SUCH DAMAGE.
 * <!-- LICENSE_TEXT_END -->
 */

/**
 * @author EVS Team
 * @version 1.0
 *
 * Modification history:
 *     Initial implementation kim.ong@ngc.com
 *
 */


public class ConceptLink
{

// Variable declaration
	private String sourceConceptCode;
	private String sourceConceptName;
	private String targetConceptCode;
	private String targetConceptName;
	private String associationName;
	private String path;

// Default constructor
	public ConceptLink() {
	}

// Constructor
	public ConceptLink(
		String sourceConceptCode,
		String sourceConceptName,
		String targetConceptCode,
		String targetConceptName,
		String associationName) {

		this.sourceConceptCode = sourceConceptCode;
		this.sourceConceptName = sourceConceptName;
		this.targetConceptCode = targetConceptCode;
		this.targetConceptName = targetConceptName;
		this.associationName = associationName;
		this.path = sourceConceptCode;
	}

	public ConceptLink(
		String sourceConceptCode,
		String sourceConceptName,
		String targetConceptCode,
		String targetConceptName,
		String associationName,
		String path) {

		this.sourceConceptCode = sourceConceptCode;
		this.sourceConceptName = sourceConceptName;
		this.targetConceptCode = targetConceptCode;
		this.targetConceptName = targetConceptName;
		this.associationName = associationName;
		this.path = path;
	}

// Set methods
	public void setSourceConceptCode(String sourceConceptCode) {
		this.sourceConceptCode = sourceConceptCode;
	}

	public void setSourceConceptName(String sourceConceptName) {
		this.sourceConceptName = sourceConceptName;
	}

	public void setTargetConceptCode(String targetConceptCode) {
		this.targetConceptCode = targetConceptCode;
	}

	public void setTargetConceptName(String targetConceptName) {
		this.targetConceptName = targetConceptName;
	}

	public void setAssociationName(String associationName) {
		this.associationName = associationName;
	}

	public void setPath(String path) {
		this.path = path;
	}


// Get methods
	public String getSourceConceptCode() {
		return this.sourceConceptCode;
	}

	public String getSourceConceptName() {
		return this.sourceConceptName;
	}

	public String getTargetConceptCode() {
		return this.targetConceptCode;
	}

	public String getTargetConceptName() {
		return this.targetConceptName;
	}

	public String getAssociationName() {
		return this.associationName;
	}

	public String getPath() {
		return this.path;
	}


	public String toString() {
		return sourceConceptName + " (" + sourceConceptCode + ") --> [" + associationName + "] --> (" + targetConceptName + " (" + targetConceptCode + ")";
	}
}
