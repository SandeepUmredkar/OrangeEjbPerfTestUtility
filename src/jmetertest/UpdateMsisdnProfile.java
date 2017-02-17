package jmetertest;

import java.io.Serializable;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

import com.o2.registration.adapter.CompanionAdapter;

public class UpdateMsisdnProfile extends AbstractJavaSamplerClient implements Serializable {
	private static final long serialVersionUID = 1L;
	Arguments defaultParameters = new Arguments();
	public void setupTest(JavaSamplerContext context) {}
	public Arguments getDefaultParameters() {

		defaultParameters.clear();
		defaultParameters.addArgument("UserNameUpdateMsisdnProfile", "Ptest1");
		defaultParameters.addArgument("msisdnUpdateMsisdnProfile", "447777777777");

		defaultParameters.addArgument("UserNameUpdateMsisdnProfile_1", "test_cog11");
		defaultParameters.addArgument("msisdnUpdateMsisdnProfile_1", "07521111124");

		defaultParameters.addArgument("domainUpdateMsisdnProfile", "stf.ref.o2.co.uk");
		defaultParameters.addArgument("counterUpdateMsisdnProfile", "${__counter(FALSE,user)}");
		defaultParameters.addArgument("ThreadNoUpdateMsisdnProfile", "${__threadNum}");
		
		return defaultParameters;
	}
	public SampleResult runTest(JavaSamplerContext context) {
		SampleResult results = new SampleResult();
		results.setSuccessful(false);
		try{                     
			results.sampleStart();
			
			long threadNo = Long.parseLong(context.getParameter("ThreadNoUpdateMsisdnProfile"));
			
			String userName = context.getParameter("UserNameUpdateMsisdnProfile");
			String msisdn = context.getParameter("msisdnUpdateMsisdnProfile");

			String userName1 = context.getParameter("UserNameUpdateMsisdnProfile_1");
			String msisdn1 = context.getParameter("msisdnUpdateMsisdnProfile_1");

			String domain = context.getParameter("domainUpdateMsisdnProfile");
			int counter = Integer.parseInt(context.getParameter("counterUpdateMsisdnProfile"));

			String msisdnStatus = "INACTIVE";
			String msisdnservicestatus ="DISCONNECT";

			CompanionAdapter bean = EJBfactory.callEJB();
			
			System.out.println(threadNo);

			if((threadNo & 1) != 0)
			{
				msisdnStatus = "ACTIVE";
				msisdnservicestatus = "INSTALL";
				userName = userName1;
				msisdn = msisdn1;
			}

			String createOrUpdateMsisdnProfileXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
					+ "<msisdnProfile xmlns=\"http://reg.eai.o2c.ibm.com\">"
					+ "<userid>"+userName+"</userid><domain>"+domain+"</domain>"
					+ "<msisdn>"+msisdn+"</msisdn><msisdntype>POST-PAY</msisdntype><msisdnstatus>"+msisdnStatus+"</msisdnstatus>"
					+ "<msisdnservicestatus>"+msisdnservicestatus+"</msisdnservicestatus><genevaaccountid>"+userName+"</genevaaccountid></msisdnProfile>";

			int returnValueMsisdnProfile = bean.createOrUpdateMsisdnProfile(createOrUpdateMsisdnProfileXML);
			if (returnValueMsisdnProfile == 0 || returnValueMsisdnProfile == 1 ){
				results.setSuccessful(true);
			}
			results.setResponseCode(Integer.toString(returnValueMsisdnProfile));
			results.setResponseMessage("msisdnStatus: " + msisdnStatus +"  msisdnservicestatus  " 
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
