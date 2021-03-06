package jmetertest;


import java.io.File;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

public class DeleteUser extends AbstractJavaSamplerClient implements Serializable {
	private static final long serialVersionUID = 1L;
	private static String DB_URL = "jdbc:oracle:thin:@//216.239.197.76:1521/REGFR1";
	private static final String DB_USERNAME = "O2_STF_USER";
	private static final String DB_PASSWORD = "zL9bG2KTKo_tCo_C_2";
	private static final String OID_URL = "ldap://82.132.150.41:13060";
	private static final String OID_USERNAME = "cn=orcladmin";
	private static final String OID_PASSWORD = "password_1";

	private String TEXTFILEPATH = "D:\\Support\\apache-jmeter-2.9\\bin\\testResults.txt";

	private static final String MAIN_QUERY = "delete from xxx where custnum = ";
	private static final String[] TABLE_ARRAY = {"customerrole", "genevaaccount", "msisdn", "user_identity_link", "user_identity", "customer"};
	private Scanner fileScanner;

	public void setupTest(JavaSamplerContext context) {}

	public Arguments getDefaultParameters() {
		Arguments defaultParameters = new Arguments();
		defaultParameters.clear();
		defaultParameters.addArgument("DBUrl", "jdbc:oracle:thin:@//216.239.197.76:1521/REGFR1");
		defaultParameters.addArgument("UserName", "O2_STF_USER");
		defaultParameters.addArgument("TEXTFILEPATH", TEXTFILEPATH);

		return defaultParameters;
	}
	public SampleResult runTest(JavaSamplerContext context) {
		SampleResult results = new SampleResult();
		try{                     
			results.sampleStart();
			results.setSuccessful(false);
			TEXTFILEPATH = context.getParameter("TEXTFILEPATH");
			DB_URL = context.getParameter("DBUrl");

			Connection con = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

			fileScanner = new Scanner( new File(TEXTFILEPATH), "UTF-8" );
			String custnumString = fileScanner.useDelimiter("\\A").next();
			String[] custnumStringArray = custnumString.replaceAll("[\\s\\u00A0]+", ",").split(",");

			Statement myStatement = con.createStatement();
			//String query =null;
			
			
		/*	System.out.println("array start ******");
			for (int i = 0; i < custnumStringArray.length; i++) {
				System.out.println(custnumStringArray[i]);
			}

			System.out.println("array END ******");*/
			
			
			
			try {
				for (int i = 0; i < custnumStringArray.length; i++) {
					if(! custnumStringArray[i].isEmpty()){
						
						String newCustNum = custnumStringArray[i].trim();
						
						System.out.println("Custnum to delete   " + newCustNum);
						
						myStatement.executeUpdate("delete from customerrole where custnum = " + newCustNum);
						myStatement.executeUpdate("delete from genevaaccount where custnum = " + newCustNum);
						myStatement.executeUpdate("delete from msisdn where custnum = " + newCustNum);
						myStatement.executeUpdate("delete from user_identity_link where custnum = " + newCustNum);
						myStatement.executeUpdate("delete from user_identity where default_account = " + newCustNum);
						myStatement.executeUpdate("delete from customer where custnum = " + newCustNum);
						
						/*for (int j = 0; j < TABLE_ARRAY.length; j++) {
							query = MAIN_QUERY.replace("xxx", TABLE_ARRAY[j]) + custnumStringArray[i].trim() + " ;";
							myStatement.executeUpdate(query);
						}*/
					}
				}
				myStatement.close();
				con.close();
				results.setSuccessful(true);
				results.setResponseMessage("Deletion of users is done properly..");
			} catch (Exception ex) {
				myStatement.close();
				con.close();
				results.setResponseMessage("in exception "+ex.getMessage()+"  "+ex.getCause());
			}

		} 
		catch (Exception ce){
			results.setResponseMessage("in exception "+ce.getMessage()+"  "+ce.getCause());
		}
		results.sampleEnd();
		return results;
	}
	public void teardownTest(JavaSamplerContext context) {}
}
