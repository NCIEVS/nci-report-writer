/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-report--writer/LICENSE.txt for details.
 */

package gov.nih.nci.evs.reportwriter.utils;

import gov.nih.nci.evs.utils.*;

import java.io.*;
import java.util.*;

import org.apache.log4j.*;

/**
 * 
 */

/**
 * @author EVS Team (David Yee)
 * @version 1.0
 */

public class AsciiToTextFormatter extends BaseFileFormatter {
	private static Logger _logger = Logger
			.getLogger(AsciiToTextFormatter.class);

	public Boolean convert(String textfile, String delimiter) throws Exception {
		return convert2(textfile, "edt", delimiter);
	}

	public Boolean convert(String textfile, String delimiter, String outfile)
			throws Exception {
		BufferedReader br = getBufferReader(textfile);
		MyFileOutputStream out = new MyFileOutputStream(outfile);
		Vector<String> headings = getColumnHeadings(textfile, delimiter);

		int row = 0;
		while (true) {
			String line = br.readLine();
			if (line == null)
				break;
			if (line.length() <= 0)
				continue;

			if (row > 0) {
				Vector<String> v = parseData(line, delimiter);
				out.writeln(StringUtils.SEPARATOR);
				for (int col = 0; col < v.size(); col++) {
					out.writeln("[" + row + "," + col + "] "
							+ headings.get(col) + ": " + v.get(col));
				}
				out.writeln();
			}
			row++;
		}
		br.close();
		out.close();
		return Boolean.TRUE;
	}

	public static void main(String[] args) {
		try {
			String dir = "C:/apps/evs/ncireportwriter-webapp/downloads";
			String infile = dir + "/FDA-SPL_Country_Code_REPORT__10.06e.txt";
			String delimiter = "\t";

			new AsciiToTextFormatter().convert(infile, delimiter);
			_logger.debug("Done");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
