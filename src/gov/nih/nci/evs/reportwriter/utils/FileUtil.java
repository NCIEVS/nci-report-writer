package gov.nih.nci.evs.reportwriter.utils;

import java.io.*;
import java.util.*;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.DocumentEntry;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;

/**
  * <!-- LICENSE_TEXT_START -->
* Copyright 2008,2009 NGIT. This software was developed in conjunction with the National Cancer Institute,
* and so to the extent government employees are co-authors, any rights in such works shall be subject to Title 17 of the United States Code, section 105.
* Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
* 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the disclaimer of Article 3, below. Redistributions
* in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other
* materials provided with the distribution.
* 2. The end-user documentation included with the redistribution, if any, must include the following acknowledgment:
* "This product includes software developed by NGIT and the National Cancer Institute."
* If no such end-user documentation is to be included, this acknowledgment shall appear in the software itself,
* wherever such third-party acknowledgments normally appear.
* 3. The names "The National Cancer Institute", "NCI" and "NGIT" must not be used to endorse or promote products derived from this software.
* 4. This license does not authorize the incorporation of this software into any third party proprietary programs. This license does not authorize
* the recipient to use any trademarks owned by either NCI or NGIT
* 5. THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESSED OR IMPLIED WARRANTIES, (INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
* MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE) ARE DISCLAIMED. IN NO EVENT SHALL THE NATIONAL CANCER INSTITUTE,
* NGIT, OR THEIR AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
* PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
* WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
  * <!-- LICENSE_TEXT_END -->
  */

/**
  * @author EVS Team
  * @version 1.0
  *
  * Modification history
  *     Initial implementation kim.ong@ngc.com
  *
 */

public class FileUtil {

	static final int MAX_WIDTH = 15;

    public FileUtil() {};

    private static Vector<String> parseData(String line, String tab)
    {
		Vector data_vec = new Vector();
	    StringTokenizer st = new StringTokenizer(line, tab);
		while (st.hasMoreTokens()) {
			String value = st.nextToken();
			data_vec.add(value);
		}
		return data_vec;
	}

    public static Boolean[] findWrappedColumns(String textfile, String delimiter, int maxLength) {
		File file = new File(textfile);

		FileInputStream fis = null;
		BufferedInputStream bis = null;
		DataInputStream dis = null;

		Boolean[] a = null;

		String heading = null;

		try {
		   fis = new FileInputStream(file);
		   bis = new BufferedInputStream(fis);
		   dis = new DataInputStream(bis);

           int rownum = 0;
		   while (dis.available() != 0) {
			  String line = dis.readLine();
			  line = line.trim();
			  if (line.length() > 0)
			  {
				  Vector<String> v = parseData(line, delimiter);
				  if (rownum == 0)
				  {
					  heading = line;
					  a = new Boolean[v.size()];
					  for (int i=0; i<v.size(); i++) {
						  a[i] = Boolean.FALSE;
					  }
				  }
				  else
				  {
					  for (int i=0; i<v.size(); i++) {
						 String s = (String) v.elementAt(i);
						 if (s.length() > maxLength && a[i].equals(Boolean.FALSE))
						 {
							 //System.out.println("\n" + line);
							 //System.out.println("i: " + i + " " + s);
							 a[i] = Boolean.TRUE;
						 }
					  }
			      }
				  rownum++;
			  }
		   }


		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return a;

	}


    public static Boolean convertToExcel(String textfile, String delimiter) {
		int k = textfile.indexOf(".txt");
		String excelfile = textfile.substring(0, k) + ".xls";
		return convertToExcel(textfile, delimiter, excelfile);

	}


    public static Boolean convertToExcel(String textfile, String delimiter, String excelfile) {
        Boolean[] a = findWrappedColumns(textfile, delimiter, MAX_WIDTH);
		File file = new File(textfile);

		String absolutePath = file.getAbsolutePath();
		System.out.println("Absolute Path: " + absolutePath);

		String filename = file.getName();
		System.out.println("filename: " + filename);

		int m = filename.indexOf(".");
		String workSheetLabel = filename.substring(0, m);
		int n = workSheetLabel.indexOf("__");
		workSheetLabel = workSheetLabel.substring(0, n);
		System.out.println("workSheetLabel: " + workSheetLabel);

		if (workSheetLabel.compareTo("") == 0) return Boolean.FALSE;

		String pathName = file.getPath();
		System.out.println("Path: " + pathName);

		FileInputStream fis = null;
		BufferedInputStream bis = null;
		DataInputStream dis = null;
		try {
		   fis = new FileInputStream(file);
		   bis = new BufferedInputStream(fis);
		   dis = new DataInputStream(bis);

		   FileOutputStream fout = new FileOutputStream(excelfile);
		   HSSFWorkbook wb = new HSSFWorkbook();
		   HSSFSheet ws = wb.createSheet(workSheetLabel);

		   HSSFCellStyle cs = wb.createCellStyle();
		   cs.setWrapText(true);
		   cs.setAlignment(HSSFCellStyle.ALIGN_JUSTIFY);

		   HSSFRow wr=null;
           int rownum = 0;
		   while (dis.available() != 0) {
			  String line = dis.readLine();
			  line = line.trim();
			  if (line.length() > 0)
			  {
				  Vector<String> v = parseData(line, delimiter);
				  wr = ws.createRow(rownum);

				  wr.setHeightInPoints(60);

				  for (int i=0; i<v.size();i++) {
				     HSSFCell wc = wr.createCell(i);
				     if(a[i].equals(Boolean.TRUE))
				     {
						 wc.setCellStyle(cs);
						 wc.setCellType(HSSFCell.CELL_TYPE_STRING);
					 }

				     String s = (String) v.elementAt(i);
				   	 wc.setCellValue(s);
				  }
				  rownum++;
			  }
		   }

		   wb.write(fout);
		   fout.close();
		   return Boolean.TRUE;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
   }


   public static void main(String[] args) {
      try {
         FileUtil test = new FileUtil();
   	     String delimiter = "\t";
		 test.convertToExcel(args[0], delimiter, args[1]);

      } catch (Exception e) {
         e.printStackTrace();
      }
   }
}

