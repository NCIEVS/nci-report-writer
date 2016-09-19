package gov.nih.nci.evs.app.neopl;


import java.io.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import org.apache.poi.hpsf.DocumentSummaryInformation;
import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.POIXMLProperties.*;
import org.apache.poi.POIXMLProperties;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.openxmlformats.schemas.officeDocument.x2006.extendedProperties.CTProperties;


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


public class XLSMetadataUtils {

     public static void setAuthor(String filename, String author) {
		HSSFWorkbook workbook = null;
		FileInputStream fis = null;
		try {
			System.out.println(filename);
			fis = new FileInputStream(filename);
			workbook = new HSSFWorkbook(fis);
            workbook.createInformationProperties();
			SummaryInformation summaryInfo = workbook.getSummaryInformation();
			summaryInfo.setAuthor(author);

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				fis.close();
			} catch (Exception ex) {

			}
		}
		FileOutputStream fos = null;

		try {
			fos = new FileOutputStream(new File(filename));
			workbook.write(fos);
			fos.flush();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				fos.close();
				System.out.println("Outputfile " + filename + " generated.");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

    public static void main(String[] args) {
		String xlsfile = args[0];
        try {
            XLSMetadataUtils.setAuthor(xlsfile, "NCI/EVS");
        }
        catch(Exception ex) {
            System.out.println(ex.getClass().getName());
            System.out.println(ex.getMessage());
            ex.printStackTrace(System.out);
        }
    }


}
