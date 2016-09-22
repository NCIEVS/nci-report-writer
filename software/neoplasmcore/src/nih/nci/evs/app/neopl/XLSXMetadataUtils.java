package gov.nih.nci.evs.app.neopl;


import java.io.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Random;
import org.apache.poi.hpsf.CustomProperties;
import org.apache.poi.hpsf.DocumentSummaryInformation;
import org.apache.poi.hpsf.MarkUnsupportedException;
import org.apache.poi.hpsf.NoPropertySetStreamException;
import org.apache.poi.hpsf.PropertySet;
import org.apache.poi.hpsf.PropertySetFactory;
import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.hpsf.UnexpectedPropertySetTypeException;
import org.apache.poi.hpsf.WritingNotSupportedException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.DocumentEntry;
import org.apache.poi.poifs.filesystem.DocumentInputStream;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.POIXMLProperties.CoreProperties;
import org.apache.poi.POIXMLProperties;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;


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


public final class XLSXMetadataUtils {

    private static String SUMMARY_DATA_AUTHOR = "SUMMARY_DATA_AUTHOR";
    private static String SUMMARY_DATA_KEYWORDS = "SUMMARY_DATA_KEYWORDS";
    private static String SUMMARY_DATA_TITLE = "SUMMARY_DATA_TITLE";
    private static String SUMMARY_DATA_SUBJECT = "SUMMARY_DATA_SUBJECT";

	public static boolean freezeRow(String filename, int sheetNumber, int rowNum) {
		FileOutputStream fileOut = null;
		boolean status = false;
        try {
			InputStream inp = new FileInputStream(filename);
			Workbook wb = WorkbookFactory.create(inp);
			Sheet sheet = wb.getSheetAt(sheetNumber);
			sheet.createFreezePane(0,rowNum); // this will freeze first rowNum rows
			fileOut = new FileOutputStream(filename);
			wb.write(fileOut);
			status = true;
			System.out.println("File modified " + filename);

		} catch (Exception ex) {
			//ex.printStackTrace();
			System.out.println("ERROR: freezeRow " + filename);

		} finally {
			try {
				fileOut.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return status;
	}

	public static void copyFile(String sourcefile, String targetfile) {
    	InputStream inStream = null;
	    OutputStream outStream = null;

    	try{
    	    File afile = new File(sourcefile);
    	    File bfile = new File(targetfile);

    	    inStream = new FileInputStream(afile);
    	    outStream = new FileOutputStream(bfile);

    	    byte[] buffer = new byte[1024];
     	    int length;

    	    while ((length = inStream.read(buffer)) > 0){
     	    	outStream.write(buffer, 0, length);
     	    }

    	    inStream.close();
    	    outStream.close();

    	}catch(IOException e){
    		e.printStackTrace();
    	}
    }



	public static String getCreator(File file) {
		String author = null;
        try {
			OPCPackage pkg = OPCPackage.open(file);
			POIXMLProperties props = new POIXMLProperties(pkg);
			POIXMLProperties.CoreProperties core_properties = props.getCoreProperties();
			author = core_properties.getCreator();
			pkg.close();

	    } catch (Exception ex) {
			ex.printStackTrace();
		}
		return author;
	}

	public static void setCreator(File file, String author) {
        try {
			OPCPackage pkg = OPCPackage.open(file);
			POIXMLProperties props = new POIXMLProperties(pkg);
			POIXMLProperties.CoreProperties core_properties = props.getCoreProperties();
			core_properties.setCreator(author);
			pkg.close();

	    } catch (Exception ex) {
			ex.printStackTrace();
		}
	}



	public static String getPOISummaryData(String filename, String key) {
		String value = null;
		OPCPackage pkg = null;
        try {
			pkg = OPCPackage.open(new File(filename));
			POIXMLProperties props = new POIXMLProperties(pkg);
			POIXMLProperties.CoreProperties core_properties = props.getCoreProperties();

			if (key.compareTo(SUMMARY_DATA_AUTHOR) == 0) {
				value = core_properties.getCreator();
			} else if (key.compareTo(SUMMARY_DATA_KEYWORDS) == 0) {
				value = core_properties.getKeywords();
			} else if (key.compareTo(SUMMARY_DATA_TITLE) == 0) {
				value = core_properties.getTitle();
			} else if (key.compareTo(SUMMARY_DATA_SUBJECT) == 0) {
				value = core_properties.getSubject();
			}

	    } catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				pkg.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		return value;
	}

	public static void setPOISummaryData(String filename, String key, String value) {
		OPCPackage pkg = null;
        try {
			pkg = OPCPackage.open(new File(filename));
			POIXMLProperties props = new POIXMLProperties(pkg);
			POIXMLProperties.CoreProperties core_properties = props.getCoreProperties();

			if (key.compareTo(SUMMARY_DATA_AUTHOR) == 0) {
				core_properties.setCreator(value);
			} else if (key.compareTo(SUMMARY_DATA_KEYWORDS) == 0) {
				core_properties.setKeywords(value);
			} else if (key.compareTo(SUMMARY_DATA_TITLE) == 0) {
				core_properties.setTitle(value);
			} else if (key.compareTo(SUMMARY_DATA_SUBJECT) == 0) {
				core_properties.setSubjectProperty(value);
			}

	    } catch (Exception ex) {
			//ex.printStackTrace();
		} finally {
			try {
				pkg.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}


	public static void setPOISummaryData(String filename, String[] keys, String[] values) {
		OPCPackage pkg = null;
        try {
			pkg = OPCPackage.open(new File(filename));
			POIXMLProperties props = new POIXMLProperties(pkg);
			POIXMLProperties.CoreProperties core_properties = props.getCoreProperties();

            for (int i=0; i<keys.length; i++) {
				String key = keys[i];
				String value = values[i];
				if (key.compareTo(SUMMARY_DATA_AUTHOR) == 0) {
					core_properties.setCreator(value);
				} else if (key.compareTo(SUMMARY_DATA_KEYWORDS) == 0) {
					core_properties.setKeywords(value);
				} else if (key.compareTo(SUMMARY_DATA_TITLE) == 0) {
					core_properties.setTitle(value);
				} else if (key.compareTo(SUMMARY_DATA_SUBJECT) == 0) {
					core_properties.setSubjectProperty(value);
				}
		    }

	    } catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				pkg.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}



	public static String getSummaryData(String filename, String key) {
		String value = null;
		FileInputStream stream = null;
		try {
			stream = new FileInputStream(new File(filename));
			POIFSFileSystem poifs = null;
			try {
				poifs = new POIFSFileSystem(stream);
			} catch (Exception e) {
				stream.close();
				return getPOISummaryData(filename, key);
			}
			DirectoryEntry dir = poifs.getRoot();
			DocumentEntry siEntry = (DocumentEntry)dir.getEntry(SummaryInformation.DEFAULT_STREAM_NAME);
			if (siEntry != null) {
				DocumentInputStream dis = new DocumentInputStream(siEntry);
				PropertySet ps = new PropertySet(dis);
				SummaryInformation si = new SummaryInformation(ps);

				if (key.compareTo(SUMMARY_DATA_AUTHOR) == 0) {
					value = si.getAuthor();
				} else if (key.compareTo(SUMMARY_DATA_KEYWORDS) == 0) {
					value = si.getKeywords();
				} else if (key.compareTo(SUMMARY_DATA_TITLE) == 0) {
					value = si.getTitle();
				} else if (key.compareTo(SUMMARY_DATA_SUBJECT) == 0) {
					value = si.getSubject();
				}
			}
		} catch (Exception ex) {
			ex.getStackTrace();
		} finally {
			try {
				stream.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return value;
	}


	public static void setSummaryData(String filename, String[] keys, String[] values) {
		String size = getFileSize(filename);
		FileInputStream stream = null;
		try {
			stream = new FileInputStream(new File(filename));
			POIFSFileSystem poifs = null;
			boolean passed = false;
			try {
				poifs = new POIFSFileSystem(stream);
				passed = true;
			} catch (Exception e) {
				passed = false;
			}
			if (!passed) {
				setPOISummaryData(filename, keys, values);
				stream.close();
				return;
			}

			HSSFWorkbook workbook = null;
			SummaryInformation summaryInfo = null;
			FileInputStream fis = null;
			try {
				System.out.println(filename);
				fis = new FileInputStream(filename);

				workbook = new HSSFWorkbook(fis);
				summaryInfo = workbook.getSummaryInformation();
				if (summaryInfo == null) {
					workbook.createInformationProperties();
					summaryInfo = workbook.getSummaryInformation();

					for (int i=0; i<keys.length; i++) {
						String key = keys[i];
						String value = values[i];

						System.out.println(key + " -> " + value);

						if (key.compareTo(SUMMARY_DATA_AUTHOR) == 0) {
							summaryInfo.setAuthor(value);
						} else if (key.compareTo(SUMMARY_DATA_KEYWORDS) == 0) {
							summaryInfo.setKeywords(value);
						} else if (key.compareTo(SUMMARY_DATA_TITLE) == 0) {
							summaryInfo.setTitle(value);
						} else if (key.compareTo(SUMMARY_DATA_SUBJECT) == 0) {
							summaryInfo.setSubject(value);
						}
					}


				}
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
				fos.close();
			}


		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}


	public static String getAuthor(File file) {
		String author=null;
		try {
			FileInputStream stream = new FileInputStream(file);
			POIFSFileSystem poifs = null;
			try {
				poifs = new POIFSFileSystem(stream);
			} catch (Exception e) {
				stream.close();
				return getCreator(file);
			}
			DirectoryEntry dir = null;
			try {
				dir = poifs.getRoot();
			} catch (Exception ex) {
				System.out.println("DirectoryEntry is NULL???");
				return null;
			}
			DocumentEntry siEntry = (DocumentEntry)dir.getEntry(SummaryInformation.DEFAULT_STREAM_NAME);
			if (siEntry != null) {
				DocumentInputStream dis = new DocumentInputStream(siEntry);
				PropertySet ps = new PropertySet(dis);
				SummaryInformation si = new SummaryInformation(ps);
				author = si.getAuthor();
			}
			stream.close();
		} catch (Exception ex) {
			ex.getStackTrace();
		}
		return author;
	}

	public static void setAuthor(String filename, String author) {
		try {
			FileInputStream stream = new FileInputStream(new File(filename));
			POIFSFileSystem poifs = new POIFSFileSystem(stream);
			DirectoryEntry dir = poifs.getRoot();

			System.out.println("SummaryInformation.DEFAULT_STREAM_NAME: " + SummaryInformation.DEFAULT_STREAM_NAME);

			DocumentEntry siEntry =      (DocumentEntry)dir.getEntry(SummaryInformation.DEFAULT_STREAM_NAME);
			DocumentInputStream dis = new DocumentInputStream(siEntry);
			PropertySet ps = new PropertySet(dis);
			SummaryInformation si = new SummaryInformation(ps);

			System.out.println("SummaryInformation setAuthor: " + author);

			si.setAuthor(author);

			OutputStream outStream = null;
			outStream = new FileOutputStream(new File(filename));
    	    byte[] buffer = new byte[1024];
     	    int length;

    	    while ((length = stream.read(buffer)) > 0){
     	    	outStream.write(buffer, 0, length);
     	    }
    	    outStream.close();
			stream.close();

		} catch (Exception ex) {
			ex.getStackTrace();
		}
	}


	public static void setAuthor(File file, String author) {
		try {
			FileInputStream stream = new FileInputStream(file);
			POIFSFileSystem poifs = null;
			try {
				poifs = new POIFSFileSystem(stream);
			} catch (Exception e) {
				stream.close();
				setCreator(file, author);
				return;
			}
			DirectoryEntry dir = poifs.getRoot();
			DocumentEntry siEntry = (DocumentEntry)dir.getEntry(SummaryInformation.DEFAULT_STREAM_NAME);
			if (siEntry != null) {
				DocumentInputStream dis = new DocumentInputStream(siEntry);
				PropertySet ps = new PropertySet(dis);
				SummaryInformation si = new SummaryInformation(ps);
				si.setAuthor(author);
			}
			stream.close();
		} catch (Exception ex) {
			ex.getStackTrace();
		}
	}


	public static String getReadableFileSize(long size) {
		if(size <= 0) return "0";
		final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
		int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
		return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
	}


	public static String getFileSize(String filename) {
		File file = new File(filename);
		return getReadableFileSize(file.length());
	}

	public static void dumpMetadata(String filename) {
        String author = getSummaryData(filename, SUMMARY_DATA_AUTHOR);
        System.out.println("SUMMARY_DATA_AUTHOR: " + author);

        String keywords = getSummaryData(filename, SUMMARY_DATA_KEYWORDS);
        System.out.println("SUMMARY_DATA_KEYWORDS: " + keywords);

        String title = getSummaryData(filename, SUMMARY_DATA_TITLE);
        System.out.println("SUMMARY_DATA_TITLE: " + title);

        String subject = getSummaryData(filename, SUMMARY_DATA_SUBJECT);
        System.out.println("SUMMARY_DATA_SUBJECT: " + subject);
		String size = getFileSize(filename);
		System.out.println("file size: " + size);
	}

	public static void updateMetadata(String filename, String author, String keywords, String title, String subject) {
		String[] keys = new String[4];
        keys[0] = SUMMARY_DATA_AUTHOR;
        keys[1] = SUMMARY_DATA_KEYWORDS;
        keys[2] = SUMMARY_DATA_TITLE;
        keys[3] = SUMMARY_DATA_SUBJECT;

        String[] values = new String[4];
        values[0] = author;
        values[1] = keywords;
        values[2] = title;
        values[3] = subject;
        setSummaryData(filename, keys, values);
	}

	public static void writeSummaryData(String filename) {
		System.out.println("Filename: " + filename);
        String author = getSummaryData(filename, SUMMARY_DATA_AUTHOR);
        System.out.println("SUMMARY_DATA_AUTHOR: " + author);

        String keywords = getSummaryData(filename, SUMMARY_DATA_KEYWORDS);
        System.out.println("SUMMARY_DATA_KEYWORDS: " + keywords);

        String title = getSummaryData(filename, SUMMARY_DATA_TITLE);
        System.out.println("SUMMARY_DATA_TITLE: " + title);

        String subject = getSummaryData(filename, SUMMARY_DATA_SUBJECT);
        System.out.println("SUMMARY_DATA_SUBJECT: " + subject);

		String size = getFileSize(filename);
		System.out.println("file size: " + size);
	}


	public static void main(String [ ] args)
	{
		String filename = "CDISC_Controlled_Terminology_Multiple_Term_Request_Spreadsheet.xlsx";
		filename = "20150115 Term Browser Functional Automation LOE.xlsx";
		//filename = "FDA-SPL_Country_Code_REPORT__14.12e.xls";
		filename = args[0];

		String size = getFileSize(filename);
		System.out.println("file size: " + size);

		//String backupfile = "new_" + filename;
		//String targetfile = "backup.xlsx";
		//copyFile(filename, backupfile);
		//freezeRow(targetfile, 1, 4);

		String[] keys = new String[4];
        keys[0] = SUMMARY_DATA_AUTHOR;
        keys[1] = SUMMARY_DATA_KEYWORDS;
        keys[2] = SUMMARY_DATA_TITLE;
        keys[3] = SUMMARY_DATA_SUBJECT;

        String[] values = new String[4];
        values[0] = "NCI/EVS";
        values[1] = "Cancer, Neoplasm";
        values[2] = "Neoplasm Core";
        values[3] = "Neoplasm_Core_16.08e";

		System.out.println("setSummaryData " + filename);
        setSummaryData(filename, keys, values);

        writeSummaryData(filename);
        //writeSummaryData(backupfile);
        /*

        String author = getSummaryData(filename, SUMMARY_DATA_AUTHOR);
        System.out.println("SUMMARY_DATA_AUTHOR: " + author);

        String keywords = getSummaryData(filename, SUMMARY_DATA_KEYWORDS);
        System.out.println("SUMMARY_DATA_KEYWORDS: " + keywords);

        String title = getSummaryData(filename, SUMMARY_DATA_TITLE);
        System.out.println("SUMMARY_DATA_TITLE: " + title);

        String subject = getSummaryData(filename, SUMMARY_DATA_SUBJECT);
        System.out.println("SUMMARY_DATA_SUBJECT: " + subject);

		size = getFileSize(filename);
		System.out.println("file size: " + size);
		*/
	}
}


