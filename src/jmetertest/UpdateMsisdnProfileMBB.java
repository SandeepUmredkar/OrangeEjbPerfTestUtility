package jmetertest;

import java.io.Serializable;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

import com.o2.registration.adapter.CompanionAdapter;

public class UpdateMsisdnProfileMBB extends AbstractJavaSamplerClient implements Serializable {
	private static final long serialVersionUID = 1L;
	Arguments defaultParameters = new Arguments();
	public void setupTest(JavaSamplerContext context) {}
	public Arguments getDefaultParameters() {

		defaultParameters.clear();
		defaultParameters.addArgument("UserNameUpdateMsisdnProfile", "${User}");
		defaultParameters.addArgument("msisdnUpdateMsisdnProfile", "${msisdn_csv}");

		defaultParameters.addArgument("domainUpdateMsisdnProfile", "stf.ref.o2.co.uk");
		defaultParameters.addArgument("tunnelURL", "t3://localhost:8080");
		
		defaultParameters.addArgument("msisdnStatus", "ACTIVE");
		defaultParameters.addArgument("msisdnservicestatus", "INSTALL");
		
		defaultParameters.addArgument("msisdntype", "PRE-PAY");
		
//		defaultParameters.addArgument("counterUpdateMsisdnProfile", "${__counter(FALSE,user)}");
//		defaultParameters.addArgument("ThreadNoUpdateMsisdnProfile", "${__threadNum}");
		
		return defaultParameters;
	}
	public SampleResult runTest(JavaSamplerContext context) {
		SampleResult results = new SampleResult();
		results.setSuccessful(false);
		try{                     
			results.sampleStart();
			
			String userName = context.getParameter("UserNameUpdateMsisdnProfile");
			String msisdn = context.getParameter("msisdnUpdateMsisdnProfile");

			String domain = context.getParameter("domainUpdateMsisdnProfile");
			String tunnelURL = context.getParameter("tunnelURL");
			

			String msisdnStatus = context.getParameter("msisdnStatus");
			String msisdnservicestatus =context.getParameter("msisdnservicestatus");
			
			String msisdntype =context.getParameter("msisdntype");
			
			CompanionAdapter bean = EJBfactory.callEJB(tunnelURL);
			
			String createOrUpdateMsisdnProfileXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
					+ "<msisdnProfile xmlns=\"http://reg.eai.o2c.ibm.com\">"
					+ "<userid>"+userName+"</userid><domain>"+domain+"</domain>"
					+ "<msisdn>"+msisdn+"</msisdn><msisdntype>"+msisdntype+"</msisdntype><msisdnstatus>"+msisdnStatus+"</msisdnstatus>"
					+ "<msisdnservicestatus>"+msisdnservicestatus+"</msisdnservicestatus><genevaaccountid>"+userName+"</genevaaccountid></msisdnProfile>";

			int returnValueMsisdnProfile = bean.createOrUpdateMsisdnProfile(createOrUpdateMsisdnProfileXML);
			
			if (returnValueMsisdnProfile == 0 || returnValueMsisdnProfile == 1 ){
				results.setSuccessful(true);
			}
			results.setResponseCode(Integer.toString(returnValueMsisdnProfile));
			results.setResponseMessage("MSISDN : "+ msisdn+ "  msisdnStatus: " + msisdnStatus +"  msisdnservicestatus  " 
					+msisdnservicestatus + " User :  " + userName );
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
