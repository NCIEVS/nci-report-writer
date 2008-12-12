package gov.nih.nci.evs.reportwriter.utils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import gov.nih.nci.security.util.StringEncrypter;
import gov.nih.nci.security.util.StringUtilities;
import gov.nih.nci.security.util.StringEncrypter.EncryptionException;

/**
  * <!-- LICENSE_TEXT_START -->
* Copyright 2007 NGIT. This software was developed in conjunction with the National Cancer Institute,
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
 */

   public class LoginUtil
   {
	   Connection conn = null;
	   static String username = "root";
	   static String password = "password";

	   public LoginUtil(String username, String password)
	   {
		   this.username = username;
		   this.password = password;
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
               String url = "jdbc:mysql://localhost:3306/csmupt";
               Class.forName ("com.mysql.jdbc.Driver").newInstance ();
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


	   public boolean connectToDB(String ip, String port, String db,
	                                  String username, String password)
	   {
          conn = null;

           try
           {
               String url = "jdbc:mysql://" + ip + ":" + port + "/" + db ;
               Class.forName ("com.mysql.jdbc.Driver").newInstance ();
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

	   public void getUsers()
	   {
			StringEncrypter stringEncrypter = null;
			try {
				stringEncrypter = new StringEncrypter();
			} catch (EncryptionException e1) {
				e1.printStackTrace();
			}

            try {
				Statement stmt = conn.createStatement();
				ResultSet rs=stmt.executeQuery("select * from csm_user");// where userid='"+uName+"'");

				while (rs.next()) {
					String pwd = stringEncrypter.decrypt((String) rs.getString(10));
					System.out.println("login name: " + rs.getString(2) + " password: " + rs.getString(10) + " (decrypt: " + pwd + ")");
				}

				rs.close();
				stmt.close();
		    } catch (Exception e) {
				e.printStackTrace();
			}
	   }


	   public static int getUserId(String loginName, String ip, int port, String db)
	   {
		   Connection connection = null;
		   int uid = -1;
           try
           {
               String url = "jdbc:mysql://" + ip + ":" + port + "/" + db ;
               Class.forName ("com.mysql.jdbc.Driver").newInstance ();
               connection = DriverManager.getConnection (url, username, password);

               if (connection == null)
               {
                   return uid;
               }

			   try {
					Statement stmt = connection.createStatement();
					ResultSet rs=stmt.executeQuery("select USER_ID from csm_user where LOGIN_NAME='"+loginName+"'");
					while (rs.next()) {
						uid = (int) rs.getInt(1);
					}
					rs.close();
					stmt.close();
				} catch (Exception e) {
					e.printStackTrace();
				}

		   } catch (Exception e) {
			   e.printStackTrace();
			   System.out.println ("Database connection CANNOT BE established");
		   }
		   return uid;
	   }

	   public static int getUserRoleGroupId(int userId, String ip, int port, String db)
	   {
		   Connection connection = null;
		   int groupId = -1;
           try
           {
               String url = "jdbc:mysql://" + ip + ":" + port + "/" + db ;
               Class.forName ("com.mysql.jdbc.Driver").newInstance ();
               connection = DriverManager.getConnection (url, username, password);

               if (connection == null)
               {
                   return groupId;
               }

			   try {
					Statement stmt = connection.createStatement();
					ResultSet rs=stmt.executeQuery("select GROUP_ID from CSM_USER_GROUP where USER_ID=" + userId);
					while (rs.next()) {
						groupId = (int) rs.getInt(1);
					}
					rs.close();
					stmt.close();
				} catch (Exception e) {
					e.printStackTrace();
				}

		   } catch (Exception e) {
			   e.printStackTrace();
			   System.out.println ("Database connection CANNOT BE established");
		   }
		   return groupId;
	   }


	   public static boolean authenticateUser(String loginName, String passwd, String ip, int port, String db)
	   {
		   Connection connection = null;
           try
           {
               String url = "jdbc:mysql://" + ip + ":" + port + "/" + db ;
               Class.forName ("com.mysql.jdbc.Driver").newInstance ();
               connection = DriverManager.getConnection (url, username, password);

               if (connection == null)
               {
                   return false;
               }

				StringEncrypter stringEncrypter = null;
				try {
					stringEncrypter = new StringEncrypter();
				} catch (EncryptionException e1) {
					e1.printStackTrace();
				}

				try {
					Statement stmt = connection.createStatement();
					ResultSet rs=stmt.executeQuery("select * from csm_user");// where userid='"+uName+"'");
                    boolean authenticated = false;
					while (rs.next()) {
						String uid = (String) rs.getString(2);
						String pwd = stringEncrypter.decrypt((String) rs.getString(10));
						System.out.println("login name: " + rs.getString(2) + " password: " + rs.getString(10) + " (decrypt: " + pwd + ")");
						if (uid.compareTo(loginName) == 0 && passwd.compareTo(pwd) == 0)
						{
							authenticated= true;
							break;
						}
					}

					rs.close();
					stmt.close();
					return authenticated;
				} catch (Exception e) {
					e.printStackTrace();
				}

		   } catch (Exception e) {
			   e.printStackTrace();
			   System.out.println ("Database connection CANNOT BE established");
		   }
		   return false;

	   }


	   public static void main (String[] args)
       {
		    LoginUtil test = new LoginUtil("root", "password");
		    boolean retval = test.connect();
		    if (!retval) System.exit(1);
		    test.getUsers();
		    test.disconnect();

		    String ip = "localhost";
		    int port = 3306;
		    String db = "csmupt";

		    String loginName = "kimong";
		    String password = "changeme";

		    boolean authenticated = LoginUtil.authenticateUser(loginName, password, ip, port, db);
		    System.out.println("User " + loginName + " authenticated");

		    loginName = "kim ong";
		    authenticated = LoginUtil.authenticateUser(loginName, password, ip, port, db);
		    System.out.println("User " + loginName + " NOT authenticated");

	   }


   }

