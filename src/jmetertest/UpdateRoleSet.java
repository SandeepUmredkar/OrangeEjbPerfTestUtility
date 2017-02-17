package jmetertest;


import java.io.Serializable;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

import com.o2.registration.adapter.CompanionAdapter;

public class UpdateRoleSet extends AbstractJavaSamplerClient implements Serializable {
	private static final long serialVersionUID = 1L;
	public void setupTest(JavaSamplerContext context) {}
	public Arguments getDefaultParameters() {
		Arguments defaultParameters = new Arguments();
		defaultParameters.clear();
		defaultParameters.addArgument("UserNameUpdateRoleSet", "Ptest1");
		defaultParameters.addArgument("custNumUpdateRoleSet", "11660516");
		defaultParameters.addArgument("msisdnUpdateRoleSet", "0777777777997");

		defaultParameters.addArgument("UserNameUpdateRoleSet_1", "test_cog11");
		defaultParameters.addArgument("custNumUpdateRoleSet_1", "11628186");
		defaultParameters.addArgument("msisdnUpdateRoleSet_1", "0777777777997");

		defaultParameters.addArgument("counterUpdateRoleSet", "${__counter(FALSE,user)}");
		defaultParameters.addArgument("domainUpdateRoleSet", "stf.ref.o2.co.uk");

		return defaultParameters;
	}
	public SampleResult runTest(JavaSamplerContext context) {
		SampleResult results = new SampleResult();
		try{                     
			results.sampleStart();
			String UserName = context.getParameter("UserNameUpdateRoleSet");
			String custNum = context.getParameter("custNumUpdateRoleSet");

			String UserName1 = context.getParameter("UserNameUpdateRoleSet_1");
			String custNum1 = context.getParameter("custNumUpdateRoleSet_1");

			String counter = context.getParameter("counterUpdateRoleSet");
			String domain = context.getParameter("domainUpdateRoleSet");

			int newCounter = Integer.parseInt(counter);

			CompanionAdapter bean = EJBfactory.callEJB();

			if((newCounter & 1) != 0) {
				UserName  = UserName1;
				custNum = custNum1;
			}

			String createOrUpdateRoleSetXML = "<roles xmlns=\"http://reg.eai.o2c.ibm.com\" custnum=\""+custNum+"\" id=\""+UserName+"\""
					+ " domain=\""+domain+"\""
					+ " action=\"Create\"><roleSet><chooserRole><GenevaAccountId>"+UserName+"</GenevaAccountId>"
					+ "<GenevaAccountName>MM "+UserName+ counter +"</GenevaAccountName><GenevaCustomerId>"+UserName+"</GenevaCustomerId>"
					+ "<GenevaCustomerName>MM "+UserName+"</GenevaCustomerName><CompanyCustomerType>CON</CompanyCustomerType>"
					+ "<HorizonCompanyCustnum>"+custNum+"</HorizonCompanyCustnum><AccountType>Consumer</AccountType>"
					+ "</chooserRole><chooserRole><GenevaAccountId>"+UserName+"-2</GenevaAccountId><GenevaAccountName>MM "+UserName+"-2</GenevaAccountName>"
					+ "<GenevaCustomerId>"+UserName+"</GenevaCustomerId><GenevaCustomerName>MM "+UserName+"</GenevaCustomerName>"
					+ "<CompanyCustomerType>CON</CompanyCustomerType><HorizonCompanyCustnum>"+custNum+"</HorizonCompanyCustnum>"
					+ "<AccountType>Consumer</AccountType></chooserRole><superchooserRole><GenevaCustomerId>"+UserName+"</GenevaCustomerId>"
					+ "<GenevaCustomerName>MM "+UserName+"</GenevaCustomerName><HorizonCompanyCustnum>"+custNum+"</HorizonCompanyCustnum>"
					+ "<CompanyCustomerType>CON</CompanyCustomerType></superchooserRole></roleSet></roles>";

			int returnValueRole = bean.createOrUpdateRoleSet(createOrUpdateRoleSetXML);

			results.setSuccessful(true);
			results.setResponseCode(Integer.toString(returnValueRole));
			results.setResponseMessage("GenevaAccountName: MM" + UserName+ counter  + "  User :  " + UserName);
		} 
		catch (Exception ce){
//			System.out.println("in exception "+ce.getMessage()+"  "+ce.getCause());
			results.setSuccessful(false);
		}
		results.sampleEnd();
		return results;
	}
	public void teardownTest(JavaSamplerContext context) {}
}