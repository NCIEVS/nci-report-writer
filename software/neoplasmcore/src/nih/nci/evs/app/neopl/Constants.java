package gov.nih.nci.evs.app.neopl;


import java.util.Arrays;
import java.util.List;
import java.util.Vector;


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


public class Constants {
		public static final String _serviceInfo = "EvsServiceInfo";
		public static final String ANCHOR = "a";
		public static final String ASSOCIATION = "has_sub";
		public static final String ASSOCIATION_NAME = "has_child";
		public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

        public static int DIRECTION_INBOUND = 0;
        public static int DIRECTION_OUTBOUND = 1;

		public static final String H1 = "h1";
		public static final String HEADING_LINE = "\"NCIt Code\",\"NCIt Preferred Term\",\"NCIm CUI\",\"NCIm Preferred Name\",\"NCIm Source\",\"Term Type\",\"Source Code\",\"Source Term\"";
	    public static final String HEADING_STR = "\"Code\",\"Preferred Term\",\"Relationship\",\"Code\",\"Preferred Term\"";

		public static final String HIERARCHY_FILE_PREFIX = "Neoplasm_Core_Hierarchy_";
		public static final String HIERARCHY_FILE_PREFIX_2 = "Neoplasm_Core_Hierarchy_By_Neoplastic_Status_";
		public static final String HIERARCHY_FILE_PREFIX_2_PLUS = "Neoplasm_Core_Hierarchy_By_Neoplastic_Status_Plus_";
		public static final String HIERARCHY_FILE_PREFIX_PLUS = "Neoplasm_Core_Hierarchy_Plus_";
		public static final String IMAGE_SRC = "<img src=";
		public static final String INVERSE_IS_A = "inverse_is_a";
		public static final String INVERSE_ROLE_FILE = "inverse_roles.txt";
		public static final String MAPPING_FILE_PREFIX = "Neoplasm_Core_Mappings_NCIm_Terms_";
		public static int MAX_VERSION_LENGTH = 30;
		public static final String MODE_COLLAPSE = "2";
		public static final String NCI_THESAURUS = "NCI_Thesaurus";
        public static final String NCI_METATHESAURUS = "NCI Metathesaurus";
		public static final String NCIT_CS_NAME = "NCI_Thesaurus";
		public static final String OWL_VERSION_INFO = "owl:versionInfo";
		public static final String RELATIONSHIP_FILE_PREFIX = "Neoplasm_Core_Rels_NCIt_Molecular_";
		public static final String ROOT_CODE = "@";
		public static final String SCRIPT = "script";
		public static final String SELECTED_ROLE_FILE = "roles.txt";
		public static final String SHEET_LABEL = "Neoplasm Core Mapping";
		public static final String SUMMARY_DATA_AUTHOR = "SUMMARY_DATA_AUTHOR";
		public static final String SUMMARY_DATA_KEYWORDS = "SUMMARY_DATA_KEYWORDS";
		public static final String SUMMARY_DATA_SUBJECT = "SUMMARY_DATA_SUBJECT";
		public static final String SUMMARY_DATA_TITLE = "SUMMARY_DATA_TITLE";
		public static final String TAB = "";
		public static final String TITLE = "title";
		public static final String TYPE_LEAF = "TYPE_LEAF";
		public static final String TYPE_ROOT = "TYPE_ROOT";
		public static final String[] DIRECTION = new String[] {"inbound", "outbound"};
    /**
     * Constructor
     */
    private Constants() {
        // Prevent class from being explicitly instantiated
    }
    /*
    public static void main(String[] args) {
		Vector v = FileUtils.readFile("constant.txt");
		System.out.println(v.size());
		v = SortUtils.quickSort(v);
		for (int i=0; i<v.size(); i++) {
			String t = (String) v.elementAt(i);
			System.out.println(t);
		}
	}
	*/

} // Class Constants
