package gov.nih.nci.evs.reportwriter.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Random;

import java.io.*;
import java.text.DecimalFormat;

import junit.framework.TestCase;
import org.junit.Assert;

import org.apache.poi.hpsf.CustomProperties;
import org.apache.poi.hpsf.DocumentSummaryInformation;
import org.apache.poi.hpsf.PropertySet;
import org.apache.poi.hpsf.PropertySetFactory;
import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.hpsf.WritingNotSupportedException;
import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.DocumentEntry;
import org.apache.poi.poifs.filesystem.DocumentInputStream;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.hpsf.NoPropertySetStreamException;
import org.apache.poi.hpsf.MarkUnsupportedException;
import org.apache.poi.hpsf.UnexpectedPropertySetTypeException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.POIXMLProperties;
import org.apache.poi.POIXMLProperties.CoreProperties;


public final class ExcelMetadataUtils {

    private static String SUMMARY_DATA_AUTHOR = "SUMMARY_DATA_AUTHOR";
    private static String SUMMARY_DATA_KEYWORDS = "SUMMARY_DATA_KEYWORDS";
    private static String SUMMARY_DATA_TITLE = "SUMMARY_DATA_TITLE";
    private static String SUMMARY_DATA_SUBJECT = "SUMMARY_DATA_SUBJECT";

	public static void freezeRow(String filename, int sheetNumber, int rowNum) {
        try {
			InputStream inp = new FileInputStream(filename);
			Workbook wb = WorkbookFactory.create(inp);
			Sheet sheet = wb.getSheetAt(sheetNumber);
			sheet.createFreezePane(0,rowNum); // this will freeze first rowNum rows
			FileOutputStream fileOut = new FileOutputStream(filename);
			wb.write(fileOut);
			fileOut.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		System.out.println("File modified " + filename);
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
			ex.printStackTrace();
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
			try {
				poifs = new POIFSFileSystem(stream);
			} catch (Exception e) {
				stream.close();
				setPOISummaryData(filename, keys, values);
			}

			System.out.println("setSummaryData..#2.");
			DirectoryEntry dir = poifs.getRoot();
			DocumentEntry siEntry = (DocumentEntry)dir.getEntry(SummaryInformation.DEFAULT_STREAM_NAME);
			DocumentInputStream dis = new DocumentInputStream(siEntry);
			PropertySet ps = new PropertySet(dis);
			SummaryInformation si = new SummaryInformation(ps);

			for (int i=0; i<keys.length; i++) {
				String key = keys[i];
				String value = values[i];

				System.out.println(key + " -> " + value);

				if (key.compareTo(SUMMARY_DATA_AUTHOR) == 0) {
					si.setAuthor(value);
				} else if (key.compareTo(SUMMARY_DATA_KEYWORDS) == 0) {
					si.setKeywords(value);
				} else if (key.compareTo(SUMMARY_DATA_TITLE) == 0) {
					si.setTitle(value);
				} else if (key.compareTo(SUMMARY_DATA_SUBJECT) == 0) {
					si.setSubject(value);
				}

		    }

			OutputStream outStream = null;
			outStream = new FileOutputStream(new File("test2.xls"));
			try {
				stream.close();
				stream = new FileInputStream(new File(filename));
				int c;
				while ((c = stream.read()) != -1) {
				  outStream.write(c);
				}
				stream.close();
				outStream.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				stream.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
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
			DirectoryEntry dir = poifs.getRoot();
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



	public static void main(String [ ] args)
	{
		String filename = "CDISC_Controlled_Terminology_Multiple_Term_Request_Spreadsheet.xlsx";

		String size = getFileSize(filename);
		System.out.println("file size: " + size);

		String targetfile = "copyfile.xlsx";
		copyFile(filename, targetfile);
		freezeRow(targetfile, 1, 4);

		String[] keys = new String[4];
        keys[0] = SUMMARY_DATA_AUTHOR;
        keys[1] = SUMMARY_DATA_KEYWORDS;
        keys[2] = SUMMARY_DATA_TITLE;
        keys[3] = SUMMARY_DATA_SUBJECT;

        String[] values = new String[4];
        values[0] = "Kim Ong";
        values[1] = "Keyword_1, Keyword_2";
        values[2] = "This is the title";
        values[3] = "This is the subject";

        setSummaryData(filename, keys, values);

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
	}
}

