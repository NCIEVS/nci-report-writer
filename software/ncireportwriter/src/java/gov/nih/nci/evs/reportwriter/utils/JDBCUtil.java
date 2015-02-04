/*
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-report-writer/LICENSE.txt for details.
 */

package gov.nih.nci.evs.reportwriter.utils;

import gov.nih.nci.evs.reportwriter.bean.*;
import gov.nih.nci.evs.reportwriter.properties.*;

import java.io.*;
import java.util.*;
import java.text.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

//import java.sql.DatabaseMetaData;
import java.sql.*;

import gov.nih.nci.security.util.StringEncrypter;
import gov.nih.nci.security.util.StringUtilities;
import gov.nih.nci.security.util.StringEncrypter.EncryptionException;

import org.apache.log4j.*;


   public class JDBCUtil
   {
	   private static Logger _logger = Logger.getLogger(JDBCUtil.class);

	   public static String HIBERNATE_CFG_PATH = "/WEB-INF/classes/hibernate.cfg.xml";
	   private Connection conn = null;
	   private String username = null;
	   private String password = null;
	   private String database = "reportwriter";
	   private String url = null;
	   private String driver = null;
	   private String configuration_file = null;

	   public JDBCUtil(String configuration_file) {
		   this.configuration_file = configuration_file;
		   _logger.debug("configuration_file: " + configuration_file);
		  System.out.println("configuration_file: " + configuration_file);

		   initialize();
	   }

	   public String getUsername() {
		   return username;
	   }

	   public String getPassword() {
		   return password;
	   }

	   public String getUrl() {
		   return url;
	   }

	   public String getDriver() {
		   return driver;
	   }

	   public String getConfigurationFile() {
		   return configuration_file;
	   }

	   public void initialize() {
           ConfigurationFileParser parser = new ConfigurationFileParser(this.configuration_file);
		   HashMap map = parser.getAttributeToValueHashMap("property", "name");
		   Iterator it = map.keySet().iterator();
		   while (it.hasNext()) {
				String key = (String) it.next();
				String value = (String) map.get(key);
				if (key.compareTo("connection.url") == 0) {
					url = value;
					System.out.println("url: " + url);
					_logger.debug("url: " + url);
				} else if (key.compareTo("connection.driver_class") == 0) {
					driver = value;
					_logger.debug("driver_class: " + driver);
					System.out.println("driver: " + driver);
				} else if (key.compareTo("connection.username") == 0) {
					username = value;
					_logger.debug("username: " + username);
					System.out.println("username: " + username);
				} else if (key.compareTo("connection.password") == 0) {
					password = value;
				}
			}
		}

	   public JDBCUtil(String username, String password)
	   {
		   this.username = username;
		   this.password = password;
	   }

	   public JDBCUtil(String username, String password, String database)
	   {
		   this.username = username;
		   this.password = password;
		   this.database = database;
	   }

	   public void setDatabase(String database) {
		   this.database = database;
	   }


	   public boolean disconnect()
	   {
		   if (conn != null)
		   {
			   try {
				   conn.close ();
				   System.out.println ("Database connection terminated");
				   return true;
			   } catch (Exception e) {
				   e.printStackTrace();
			   }
		   }
		   return false;
	   }

	   public boolean connect()
	   {
           conn = null;
           try
           {
               Class.forName(driver).newInstance();
               conn = DriverManager.getConnection (url, username, password);

               if (conn != null)
               {
                   System.out.println ("Database connection established");
                   return true;
               }
		   } catch (Exception e) {
			   e.printStackTrace();
			   System.out.println ("Database connection CANNOT BE established");
		   }
		   return false;
	   }

	   public void getUsers() {
			StringEncrypter stringEncrypter = null;
			try {
				stringEncrypter = new StringEncrypter();
			} catch (EncryptionException e1) {
				e1.printStackTrace();
			}

		    Connection conn = null;
		    Statement stmt = null;
		    ResultSet rs = null;

            try {
				Class.forName (driver).newInstance ();
				conn = DriverManager.getConnection (url, username, password);
				stmt = conn.createStatement();
				////////////////////////////////////
				rs = stmt.executeQuery("select * from csm_user");// where userid='"+uName+"'");
				while (rs.next()) {
					String pwd = stringEncrypter.decrypt((String) rs.getString(10));
				}
				////////////////////////////////////

				rs.close();
				stmt.close();
			    conn.close();

		    } catch (SQLException se) {
			    se.printStackTrace();
		    } catch(Exception e){
			    e.printStackTrace();
		    } finally {
			    try{
				   if(stmt!=null) {
					   stmt.close();
				   }
			    } catch (SQLException se2) {
			       se2.printStackTrace();
			    }
			    try {
				   if (conn!=null) {
					   conn.close();
				   }
			    } catch (SQLException se) {
				   se.printStackTrace();
			    }
		    }
	   }


	   public boolean authenticateUser(String loginName, String passwd) {
			StringEncrypter stringEncrypter = null;
			try {
				stringEncrypter = new StringEncrypter();
			} catch (EncryptionException e1) {
				e1.printStackTrace();
			}

		    Connection conn = null;
		    Statement stmt = null;
		    ResultSet rs = null;
            boolean authenticated = false;
            try {
				Class.forName (driver).newInstance ();
				conn = DriverManager.getConnection (url, username, password);
				stmt = conn.createStatement();
				////////////////////////////////////
				rs = stmt.executeQuery("select * from csm_user");// where userid='"+uName+"'");

				while (rs.next()) {
					String uid = (String) rs.getString(2);
					String pwd = stringEncrypter.decrypt((String) rs.getString(10));
					if (uid.compareTo(loginName) == 0 && passwd.compareTo(pwd) == 0)
					{
						authenticated = true;
						int id = rs.getInt(1);
						break;
					}
				}
				////////////////////////////////////

				rs.close();
				stmt.close();
			    conn.close();

		    } catch (SQLException se) {
			    se.printStackTrace();
		    } catch(Exception e){
			    e.printStackTrace();
		    } finally {
			    try{
				   if(stmt!=null) {
					   stmt.close();
				   }
			    } catch (SQLException se2) {
			       se2.printStackTrace();
			    }
			    try {
				   if (conn!=null) {
					   conn.close();
				   }
			    } catch (SQLException se) {
				   se.printStackTrace();
			    }
		    }
		    return authenticated;
	   }


	   public int getUserId(String loginName) {
		    Connection conn = null;
		    Statement stmt = null;
		    ResultSet rs = null;
		    int uid = -1;

            try {
				Class.forName (driver).newInstance ();
				conn = DriverManager.getConnection (url, username, password);
				stmt = conn.createStatement();
				////////////////////////////////////
				rs = stmt.executeQuery("select USER_ID from csm_user where LOGIN_NAME='"+loginName+"'");
				while (rs.next()) {
					uid = (int) rs.getInt(1);
				}
				////////////////////////////////////

				rs.close();
				stmt.close();
			    conn.close();

		    } catch (SQLException se) {
			    se.printStackTrace();
		    } catch(Exception e){
			    e.printStackTrace();
		    } finally {
			    try{
				   if(stmt!=null) {
					   stmt.close();
				   }
			    } catch (SQLException se2) {
			       se2.printStackTrace();
			    }
			    try {
				   if (conn!=null) {
					   conn.close();
				   }
			    } catch (SQLException se) {
				   se.printStackTrace();
			    }
		    }
		    return uid;
	   }

	   public String getLoginName(int userid) {
		    Connection conn = null;
		    Statement stmt = null;
		    ResultSet rs = null;
		    String login_name = null;

            try {
				Class.forName (driver).newInstance ();
				conn = DriverManager.getConnection (url, username, password);
				stmt = conn.createStatement();
				////////////////////////////////////
				rs = stmt.executeQuery("select LOGIN_NAME from user where ID=" + userid);
				while (rs.next()) {
					login_name = (String) rs.getString("LOGIN_NAME");
					break;
				}
				////////////////////////////////////

				rs.close();
				stmt.close();
			    conn.close();

		    } catch (SQLException se) {
			    se.printStackTrace();
		    } catch(Exception e){
			    e.printStackTrace();
		    } finally {
			    try{
				   if(stmt!=null) {
					   stmt.close();
				   }
			    } catch (SQLException se2) {
			       se2.printStackTrace();
			    }
			    try {
				   if (conn!=null) {
					   conn.close();
				   }
			    } catch (SQLException se) {
				   se.printStackTrace();
			    }
		    }
		    return login_name;
	   }

	   public int getUserRoleGroupId(int userId) {
		    Connection conn = null;
		    Statement stmt = null;
		    ResultSet rs = null;
		    int groupId = -1;

            try {
				Class.forName (driver).newInstance ();
				conn = DriverManager.getConnection (url, username, password);
				stmt = conn.createStatement();
				////////////////////////////////////
				rs = stmt.executeQuery("select GROUP_ID from CSM_USER_GROUP where USER_ID=" + userId);
				while (rs.next()) {
					groupId = (int) rs.getInt(1);
				}
				////////////////////////////////////

				rs.close();
				stmt.close();
			    conn.close();

		    } catch (SQLException se) {
			    se.printStackTrace();
		    } catch(Exception e){
			    e.printStackTrace();
		    } finally {
			    try{
				   if(stmt!=null) {
					   stmt.close();
				   }
			    } catch (SQLException se2) {
			       se2.printStackTrace();
			    }
			    try {
				   if (conn!=null) {
					   conn.close();
				   }
			    } catch (SQLException se) {
				   se.printStackTrace();
			    }
		    }
		    return groupId;
	   }


		public Vector AllTableName() {
		    Vector v = new Vector();
		    Connection conn = null;
		    Statement stmt = null;
		    ResultSet rs = null;

            try {
				Class.forName (driver).newInstance ();
				conn = DriverManager.getConnection (url, username, password);
				stmt = conn.createStatement();
				////////////////////////////////////
				DatabaseMetaData dbm = conn.getMetaData();
			    String[] types = {"TABLE"};
			    rs = dbm.getTables(null,null,"%",types);
			    while (rs.next()){
				    String table = rs.getString("TABLE_NAME");
				    v.add(table);
			    }
				////////////////////////////////////
				rs.close();
				stmt.close();
			    conn.close();

		    } catch (SQLException se) {
			    se.printStackTrace();
		    } catch(Exception e){
			    e.printStackTrace();
		    } finally {
			    try{
				   if(stmt!=null) {
					   stmt.close();
				   }
			    } catch (SQLException se2) {
			       se2.printStackTrace();
			    }
			    try {
				   if (conn!=null) {
					   conn.close();
				   }
			    } catch (SQLException se) {
				   se.printStackTrace();
			    }
		    }
		    return v;
	   }


	   public void describeTable (String table) {
		    Connection conn = null;
		    Statement stmt = null;
		    ResultSet rs = null;

            try {
				Class.forName (driver).newInstance ();
				conn = DriverManager.getConnection (url, username, password);
				stmt = conn.createStatement();
				////////////////////////////////////
			    rs = stmt.executeQuery("DESCRIBE "+table);
			    ResultSetMetaData md = rs.getMetaData();
			    int col = md.getColumnCount();
			    for (int i = 1; i <= col; i++){
				    String col_name = md.getColumnName(i);
				    System.out.print(col_name+"\t");
			    }
			    System.out.println();
			    DatabaseMetaData dbm = conn.getMetaData();
			    ResultSet rs1 = dbm.getColumns(null,"%",table,"%");
			    while (rs1.next()) {
				    String col_name = rs1.getString("COLUMN_NAME");
				    String data_type = rs1.getString("TYPE_NAME");
				    int data_size = rs1.getInt("COLUMN_SIZE");
				    int nullable = rs1.getInt("NULLABLE");
				    System.out.print(col_name+"\t"+data_type+"("+data_size+")"+"\t");
				    if(nullable == 1){
					    System.out.print("YES\t");
				    } else {
					    System.out.print("NO\t");
				    }
				    System.out.println();
			    }
				////////////////////////////////////
				rs.close();
				stmt.close();
			    conn.close();

		    } catch (SQLException se) {
			    se.printStackTrace();
		    } catch(Exception e){
			    e.printStackTrace();
		    } finally {
			    try{
				   if(stmt!=null) {
					   stmt.close();
				   }
			    } catch (SQLException se2) {
			       se2.printStackTrace();
			    }
			    try {
				   if (conn!=null) {
					   conn.close();
				   }
			    } catch (SQLException se) {
				   se.printStackTrace();
			    }
		    }
	   }


       public Vector getDataType (String table) {
	        Vector v = new Vector();
	        Connection conn = null;
		    Statement stmt = null;
		    ResultSet rs = null;

            try {
				Class.forName (driver).newInstance ();
				conn = DriverManager.getConnection (url, username, password);
				stmt = conn.createStatement();
				////////////////////////////////////
			    rs = stmt.executeQuery("DESCRIBE "+table);
			    ResultSetMetaData md = rs.getMetaData();
			    int col = md.getColumnCount();
			    for (int i = 1; i <= col; i++){
				    String col_name = md.getColumnName(i);
			    }
			    System.out.println();
			    DatabaseMetaData dbm = conn.getMetaData();
			    ResultSet rs1 = dbm.getColumns(null,"%",table,"%");
			    while (rs1.next()) {
				    String col_name = rs1.getString("COLUMN_NAME");
				    String data_type = rs1.getString("TYPE_NAME");
				    v.add(col_name + "|" + data_type);
			    }
				////////////////////////////////////
				rs.close();
				stmt.close();
			    conn.close();

		    } catch (SQLException se) {
			    se.printStackTrace();
		    } catch(Exception e){
			    e.printStackTrace();
		    } finally {
			    try{
				   if(stmt!=null) {
					   stmt.close();
				   }
			    } catch (SQLException se2) {
			       se2.printStackTrace();
			    }
			    try {
				   if (conn!=null) {
					   conn.close();
				   }
			    } catch (SQLException se) {
				   se.printStackTrace();
			    }
		    }
		    return v;
	   }


	   public int getTableSize(String table) {
	        Connection conn = null;
		    Statement stmt = null;
		    ResultSet rs = null;
		    int rowCount = 0;

            try {
				Class.forName (driver).newInstance ();
				conn = DriverManager.getConnection (url, username, password);
				stmt = conn.createStatement();
				////////////////////////////////////
			    rs = stmt.executeQuery("select count(*) from " + table);
			    while(rs.next()){
			        rowCount = rs.getInt("COUNT(*)");
			    }
				////////////////////////////////////
				rs.close();
				stmt.close();
			    conn.close();

		    } catch (SQLException se) {
			    se.printStackTrace();
		    } catch(Exception e){
			    e.printStackTrace();
		    } finally {
			    try{
				   if(stmt!=null) {
					   stmt.close();
				   }
			    } catch (SQLException se2) {
			       se2.printStackTrace();
			    }
			    try {
				   if (conn!=null) {
					   conn.close();
				   }
			    } catch (SQLException se) {
				   se.printStackTrace();
			    }
		    }
		    return rowCount;
	   }

	   public Object[][] retrieveTableData(String table) {
            Vector datatype_vec = getDataType(table);
            int numColumns = datatype_vec.size();
            int numRows = getTableSize(table);
            Object[][] obj_mx = new String[numRows][numColumns];
	        Connection conn = null;
		    Statement stmt = null;
		    ResultSet rs = null;
            try {
				Class.forName (driver).newInstance ();
				conn = DriverManager.getConnection (url, username, password);
				stmt = conn.createStatement();
				////////////////////////////////////
				rs = stmt.executeQuery("select * from " + table);
				//Print the data to the console
				int max = 100;
				//int lcv = 0;
				int rowNum = 0;
				//int colNum = 0;
				while(rs.next()) {
					for (int i=0; i<datatype_vec.size(); i++) {
					    String datatype = (String) datatype_vec.elementAt(i);
					    int j = i+1;
					    if (datatype.indexOf("varchar") != -1) {
						     obj_mx[rowNum][i] = rs.getString(j);
  					    } else {
							 int k = rs.getInt(j);
							 obj_mx[rowNum][i] = new Integer(rs.getString(j)).toString();
						}
					}
					rowNum++;
					if (rowNum == max) break;
				}
				////////////////////////////////////
				rs.close();
				stmt.close();
			    conn.close();

		    } catch (SQLException se) {
			    se.printStackTrace();
		    } catch(Exception e){
			    e.printStackTrace();
		    } finally {
			    try{
				   if(stmt!=null) {
					   stmt.close();
				   }
			    } catch (SQLException se2) {
			       se2.printStackTrace();
			    }
			    try {
				   if (conn!=null) {
					   conn.close();
				   }
			    } catch (SQLException se) {
				   se.printStackTrace();
			    }
		    }
		    return obj_mx;
	   }


	   public String getReportFormat(int formatId) {
		    Connection conn = null;
		    Statement stmt = null;
		    ResultSet rs = null;
		    String format = null;

            try {
				Class.forName (driver).newInstance ();
				conn = DriverManager.getConnection (url, username, password);
				stmt = conn.createStatement();
				////////////////////////////////////
				rs = stmt.executeQuery("select DESCRIPTION from report_format where ID=" + formatId);
				while (rs.next()) {
					format = (String) rs.getString("description");
					break;
				}
				////////////////////////////////////

				rs.close();
				stmt.close();
			    conn.close();

		    } catch (SQLException se) {
			    se.printStackTrace();
		    } catch(Exception e){
			    e.printStackTrace();
		    } finally {
			    try{
				   if(stmt!=null) {
					   stmt.close();
				   }
			    } catch (SQLException se2) {
			       se2.printStackTrace();
			    }
			    try {
				   if (conn!=null) {
					   conn.close();
				   }
			    } catch (SQLException se) {
				   se.printStackTrace();
			    }
		    }
		    return format;
	   }


	   public String getReportStatus(int statusId) {
		    Connection conn = null;
		    Statement stmt = null;
		    ResultSet rs = null;
		    String status = null;

            try {
				Class.forName (driver).newInstance ();
				conn = DriverManager.getConnection (url, username, password);
				stmt = conn.createStatement();
				////////////////////////////////////
				rs = stmt.executeQuery("select LABEL from report_status where ID=" + statusId);
				while (rs.next()) {
					status = (String) rs.getString("label");
					break;
				}
				////////////////////////////////////

				rs.close();
				stmt.close();
			    conn.close();

		    } catch (SQLException se) {
			    se.printStackTrace();
		    } catch(Exception e){
			    e.printStackTrace();
		    } finally {
			    try{
				   if(stmt!=null) {
					   stmt.close();
				   }
			    } catch (SQLException se2) {
			       se2.printStackTrace();
			    }
			    try {
				   if (conn!=null) {
					   conn.close();
				   }
			    } catch (SQLException se) {
				   se.printStackTrace();
			    }
		    }
		    return status;
	   }

	   public int getTemplateId(int report_id) {
		    Connection conn = null;
		    Statement stmt = null;
		    ResultSet rs = null;
            int template_id = -1;

            try {
				Class.forName (driver).newInstance ();
				conn = DriverManager.getConnection (url, username, password);
				stmt = conn.createStatement();
				////////////////////////////////////
				rs = stmt.executeQuery("SELECT BASED_ON_TEMPLATE FROM standard_report where REPORT_ID=" + report_id);
				while (rs.next()) {
					template_id = rs.getInt("based_on_template");
					break;
				}
				////////////////////////////////////

				rs.close();
				stmt.close();
			    conn.close();

		    } catch (SQLException se) {
			    se.printStackTrace();
		    } catch(Exception e){
			    e.printStackTrace();
		    } finally {
			    try{
				   if(stmt!=null) {
					   stmt.close();
				   }
			    } catch (SQLException se2) {
			       se2.printStackTrace();
			    }
			    try {
				   if (conn!=null) {
					   conn.close();
				   }
			    } catch (SQLException se) {
				   se.printStackTrace();
			    }
		    }
		    return template_id;
	   }

	   public String getCodingSchemeNameAndVersionByReportId(int report_id) {
		    int standardReportTemplateId = getTemplateId(report_id);
		    if (standardReportTemplateId == -1) return null;
		    return getCodingSchemeNameAndVersion(standardReportTemplateId);
	   }


	   public String getCodingSchemeNameAndVersion(int standardReportTemplateId) {
		    Connection conn = null;
		    Statement stmt = null;
		    ResultSet rs = null;
		    String template_label = null;
		    String codingSchemeName = null;
		    String codingSchemeVersion = null;
		    String template_data = null;

            try {
				Class.forName (driver).newInstance ();
				conn = DriverManager.getConnection (url, username, password);
				stmt = conn.createStatement();
				////////////////////////////////////
				rs = stmt.executeQuery("SELECT * FROM standard_report_template where ID=" + standardReportTemplateId);
				while (rs.next()) {
					template_label = (String) rs.getString("label");
					codingSchemeName = (String) rs.getString("coding_scheme_name");
					codingSchemeVersion = (String) rs.getString("coding_scheme_version");
					template_data = "" + standardReportTemplateId + "|" + template_label + "|" + codingSchemeName + "|" + codingSchemeVersion;
					break;
				}
				////////////////////////////////////

				rs.close();
				stmt.close();
			    conn.close();

		    } catch (SQLException se) {
			    se.printStackTrace();
		    } catch(Exception e){
			    e.printStackTrace();
		    } finally {
			    try{
				   if(stmt!=null) {
					   stmt.close();
				   }
			    } catch (SQLException se2) {
			       se2.printStackTrace();
			    }
			    try {
				   if (conn!=null) {
					   conn.close();
				   }
			    } catch (SQLException se) {
				   se.printStackTrace();
			    }
		    }
		    return template_data;
	   }


	   public int getTemplateId(String standardReportTemplateLabel) {
		    Connection conn = null;
		    Statement stmt = null;
		    ResultSet rs = null;
		    int template_id = -1;

            try {
				Class.forName (driver).newInstance ();
				conn = DriverManager.getConnection (url, username, password);
				stmt = conn.createStatement();
				////////////////////////////////////
				rs = stmt.executeQuery("SELECT * FROM standard_report_template where LABEL=" + "'" + standardReportTemplateLabel + "'");
				while (rs.next()) {
					template_id = (int) rs.getInt("id");
					break;
				}
				////////////////////////////////////
				rs.close();
				stmt.close();
			    conn.close();

		    } catch (SQLException se) {
			    se.printStackTrace();
		    } catch(Exception e){
			    e.printStackTrace();
		    } finally {
			    try{
				   if(stmt!=null) {
					   stmt.close();
				   }
			    } catch (SQLException se2) {
			       se2.printStackTrace();
			    }
			    try {
				   if (conn!=null) {
					   conn.close();
				   }
			    } catch (SQLException se) {
				   se.printStackTrace();
			    }
		    }
		    return template_id;
	   }

	   public Vector<Integer> getReportIds(int templateId) {
		    Connection conn = null;
		    Statement stmt = null;
		    ResultSet rs = null;
		    Vector v = new Vector();
		    int report_id;

            try {
				Class.forName (driver).newInstance ();
				conn = DriverManager.getConnection (url, username, password);
				stmt = conn.createStatement();
				////////////////////////////////////
				rs = stmt.executeQuery("SELECT * FROM standard_report where BASED_ON_TEMPLATE=" + templateId);
				while (rs.next()) {
					report_id = (int) rs.getInt("REPORT_ID");
					v.add(new Integer(report_id));
				}
				////////////////////////////////////
				rs.close();
				stmt.close();
			    conn.close();

		    } catch (SQLException se) {
			    se.printStackTrace();
		    } catch(Exception e){
			    e.printStackTrace();
		    } finally {
			    try{
				   if(stmt!=null) {
					   stmt.close();
				   }
			    } catch (SQLException se2) {
			       se2.printStackTrace();
			    }
			    try {
				   if (conn!=null) {
					   conn.close();
				   }
			    } catch (SQLException se) {
				   se.printStackTrace();
			    }
		    }
		    return v;
	   }


	   public void updateReportStatus(int report_id, String status) {
		    // 505: DRAFT
		    // 506: APPROVED
		    int status_id = 505;
		    if (status.compareTo("APPROVED") == 0) status_id = 506;
		    Connection conn = null;
		    Statement stmt = null;
		    ResultSet rs = null;
		    Vector v = new Vector();

            try {
				Class.forName (driver).newInstance ();
				conn = DriverManager.getConnection (url, username, password);
				stmt = conn.createStatement();
				////////////////////////////////////
				String sql = "UPDATE report set HAS_STATUS=" + status_id + " where ID=" +  report_id;
				stmt.executeUpdate(sql);
				////////////////////////////////////
				//rs.close();
				stmt.close();
			    conn.close();

		    } catch (SQLException se) {
			    se.printStackTrace();
		    } catch(Exception e){
			    e.printStackTrace();
		    } finally {
			    try{
				   if(stmt!=null) {
					   stmt.close();
				   }
			    } catch (SQLException se2) {
			       se2.printStackTrace();
			    }
			    try {
				   if (conn!=null) {
					   conn.close();
				   }
			    } catch (SQLException se) {
				   se.printStackTrace();
			    }
		    }
	   }

	    public void updateStatus(String standardReportTemplateLabel, String status) {
			int templateId = getTemplateId(standardReportTemplateLabel);
			Vector<Integer> v = getReportIds(templateId);
			for (int i=0; i<v.size(); i++) {
				Integer int_obj = (Integer) v.elementAt(i);
				int report_id = int_obj.intValue();
				updateReportStatus(report_id, status);
				updateReportLastModified(report_id);
			}
		}


       public String getTimeStampString() {
			DateFormat df = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
			java.util.Date today = Calendar.getInstance().getTime();
			String timestampstr = df.format(today);
			return timestampstr;
	   }

	   public void updateReportLastModified(int report_id) {
		    Connection conn = null;
		    PreparedStatement stmt = null;
		    ResultSet rs = null;
		    String timestampstr = getTimeStampString();
            try {
				Class.forName (driver).newInstance ();
				conn = DriverManager.getConnection (url, username, password);
				////////////////////////////////////
				String sql = "UPDATE report SET LAST_MODIFIED='" + getTimeStampString() + "' where ID=" +  report_id;
				stmt = conn.prepareStatement(sql);
				stmt.executeUpdate(sql);
				////////////////////////////////////
				//rs.close();
				stmt.close();
			    conn.close();

		    } catch (SQLException se) {
			    se.printStackTrace();
		    } catch(Exception e){
			    e.printStackTrace();
		    } finally {
			    try{
				   if(stmt!=null) {
					   stmt.close();
				   }
			    } catch (SQLException se2) {
			       se2.printStackTrace();
			    }
			    try {
				   if (conn!=null) {
					   conn.close();
				   }
			    } catch (SQLException se) {
				   se.printStackTrace();
			    }
		    }
	   }



		public static Vector<String> parseData(String line) {
			if (line == null) return null;
			String tab = "|";
			return parseData(line, tab);
		}

		public static Vector<String> parseData(String line, String tab) {
			if (line == null) return null;
			Vector data_vec = new Vector();
			StringTokenizer st = new StringTokenizer(line, tab);
			while (st.hasMoreTokens()) {
				String value = st.nextToken();
				if (value.compareTo("null") == 0)
					value = " ";
				data_vec.add(value);
			}
			return data_vec;
		}


	   public Vector sortReportData(Vector v) {
		    if (v == null || v.size() <= 1) return v;
		    Vector key_vec = new Vector();
		    HashMap map = new HashMap();
		    for (int i=0; i<v.size(); i++) {
				ReportMetadata rmd = (ReportMetadata) v.elementAt(i);
				String templateLabel = rmd.getTemplateLabel();
				String formatId_str = new Integer(rmd.getFormatId()).toString();
				String key = templateLabel + "|" + formatId_str;
				key_vec.add(key);
				map.put(key, rmd);
			}
			key_vec = SortUtils.quickSort(key_vec);
			Vector w = new Vector();
			for (int i=0; i<key_vec.size(); i++) {
				String key = (String) key_vec.elementAt(i);
				w.add((ReportMetadata)map.get(key));
			}
			return w;
	   }

	   public Vector getReportData() {
		    _logger.debug("getReportData... ");
		    Vector v = new Vector();
		    Connection conn = null;
		    Statement stmt = null;
		    ResultSet rs = null;
            try {
				Class.forName (driver).newInstance();
				//Class.forName(driver);
				conn = DriverManager.getConnection (url, username, password);
				stmt = conn.createStatement();
				////////////////////////////////////
				rs = stmt.executeQuery("SELECT * FROM report");
				while (rs.next()) {
					 int id  = rs.getInt("id");
					 String label = rs.getString("label");
					 int formatId = rs.getInt("has_format");
					 String format = getReportFormat(formatId);
					 int has_status = rs.getInt("has_status");
					 String status = getReportStatus(has_status);
					 int created_by = rs.getInt("created_by");
					 String last_modified = rs.getString("last_modified");
					 String login_name = getLoginName(created_by);
                     String pathName = rs.getString("path_name");
                     int templateId = -1;

                     String template_data = getCodingSchemeNameAndVersionByReportId(id);
                     String template_label = null;
                     String codingSchemeName = null;
                     String codingSchemeVersion = null;
                     if (template_data != null) {
						 Vector u = parseData(template_data);
						 String t = (String) u.elementAt(0);
						 templateId = Integer.parseInt(t);
						 template_label = (String) u.elementAt(1);
						 codingSchemeName = (String) u.elementAt(2);
						 codingSchemeVersion = (String) u.elementAt(3);
					 }
					 ReportMetadata mrd = new ReportMetadata(id, label, templateId, template_label, formatId, format, codingSchemeName, codingSchemeVersion,
					     login_name, last_modified, status, pathName);

                     v.add(mrd);
				}
				////////////////////////////////////

				rs.close();
				stmt.close();
			    conn.close();

		    } catch (SQLException se) {
			    se.printStackTrace();

		    } catch(Exception e){
			    e.printStackTrace();
		    } finally {
			    try{
				   if(stmt!=null) {
					   stmt.close();
				   }
			    } catch (SQLException se2) {
			       se2.printStackTrace();
			    }
			    try {
				   if (conn!=null) {
					   conn.close();
				   }
			    } catch (SQLException se) {
				   se.printStackTrace();
			    }
		    }
		    _logger.debug("getReportData - number of reports: " + v.size());
		    return sortReportData(v);
	   }
   }

