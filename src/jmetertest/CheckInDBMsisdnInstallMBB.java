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

public class CheckInDBMsisdnInstallMBB extends AbstractJavaSamplerClient implements Serializable {
	private static final long serialVersionUID = 1L;
	private static String DB_URL = "jdbc:oracle:thin:@//216.239.197.76:1521/REGFR1";
	private static final String DB_USERNAME = "O2_STF_USER";
	private static final String DB_PASSWORD = "zL9bG2KTKo_tCo_C_2";
	private static final String OID_URL = "ldap://82.132.150.41:13060";
	private static final String OID_USERNAME = "cn=orcladmin";
	private static final String OID_PASSWORD = "password_1";


	private static final String MAIN_QUERY = "select msisdn, status, SERVICESTATUS, MSISDNTYPE from msisdn msisdn = XXX and custnum = XXXX ";

	public void setupTest(JavaSamplerContext context) {}

	public Arguments getDefaultParameters() {
		Arguments defaultParameters = new Arguments();
		defaultParameters.clear();
		defaultParameters.addArgument("DBUrl", "jdbc:oracle:thin:@//216.239.197.76:1521/REGFR1");
		defaultParameters.addArgument("UserName", "O2_STF_USER");
		defaultParameters.addArgument("custnum", "${custnum}");
		defaultParameters.addArgument("msisdn_csv", "${msisdn_csv}");

		return defaultParameters;
	}
	public SampleResult runTest(JavaSamplerContext context) {
		SampleResult results = new SampleResult();
		try{                     
			results.sampleStart();
			results.setSuccessful(false);
			DB_URL = context.getParameter("DBUrl");
			String custnum = context.getParameter("custnum");
			String msisdn_csv = context.getParameter("msisdn_csv");

			Connection con = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

			Statement myStatement = con.createStatement();

			try {
				
				String query = MAIN_QUERY.replace("XXX", msisdn_csv) ;
				query = query.replace("XXXX", custnum);

				ResultSet rs = myStatement.executeQuery(query);
				while(rs.next()){
			         //Retrieve by column name
			         String MSISDN = rs.getString("MSISDN");
			         String status = rs.getString("status");
			         String SERVICESTATUS = rs.getString("SERVICESTATUS");
			         String MSISDNTYPE = rs.getString("MSISDNTYPE");

			         //Display values
			         System.out.print(", MSISDN: " + MSISDN);
			         System.out.print(", MSISDN: " + status);
			         System.out.print(", MSISDN: " + SERVICESTATUS);
			         System.out.println(", status: " + MSISDNTYPE);
			      }
				rs.close();
				myStatement.close();
				con.close();
				results.setSuccessful(true);
				results.setResponseMessage("Select done properly..");
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
