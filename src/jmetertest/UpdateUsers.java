package jmetertest;

import java.io.Serializable;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

import com.o2.registration.adapter.CompanionAdapter;

public class UpdateUsers extends AbstractJavaSamplerClient implements Serializable {
	private static final long serialVersionUID = 1L;
	public void setupTest(JavaSamplerContext context) {}
	public Arguments getDefaultParameters() {
		Arguments defaultParameters = new Arguments();
		defaultParameters.clear();
		defaultParameters.addArgument("UserNameUpdateUsers", "Ptest1");
		defaultParameters.addArgument("custNumUpdateUsers", "11660516");
		defaultParameters.addArgument("msisdnUpdateUsers", "07777777778");

		defaultParameters.addArgument("UserNameUpdateUsers_1", "test_cog11");
		defaultParameters.addArgument("custNumUpdateUsers_1", "11628186");
		defaultParameters.addArgument("msisdnUpdateUsers_1", "07521111124");

		defaultParameters.addArgument("counterUpdateUsers", "${__counter(FALSE,user)}");
		defaultParameters.addArgument("domainUpdateUsers", "stf.ref.o2.co.uk");

		return defaultParameters;
	}
	
	public SampleResult runTest(JavaSamplerContext context) {
		SampleResult results = new SampleResult();
		try{                     
			results.sampleStart();
			String UserName = context.getParameter("UserNameUpdateUsers");
			String custNum = context.getParameter("custNumUpdateUsers");
			String msisdn = context.getParameter("msisdnUpdateUsers");

			String UserName1 = context.getParameter("UserNameUpdateUsers_1");
			String custNum1 = context.getParameter("custNumUpdateUsers_1");
			String msisdn1 = context.getParameter("msisdnUpdateUsers_1");

			String counter = context.getParameter("counterUpdateUsers");
			String domain = context.getParameter("domainUpdateUsers");

			int newCounter = Integer.parseInt(counter);

			CompanionAdapter bean = EJBfactory.callEJB();

			if((newCounter & 1) != 0)
			{
				UserName  = UserName1;
				custNum = custNum1;
				msisdn= msisdn1;
			}

			String createOrUpdateUserXML = "<users><userDetails action=\"Update\">"
					+ "<user custnum=\"" + custNum + "\" id=\""+UserName+"\" domain=\""+domain +"\" "
					+ "password=\"Password123\" forename=\"Perf\" lastname=\"Test\" "
					+ "title=\"MR\" alternativeContactNumber=\"" + msisdn + "\" "
					+ "alternativeEmail=\"t_" + counter +"@gmail.com\" dateOfBirth=\"1980-08-11\""
					+ " partner=\"CPW\" MSISDN=\"" + msisdn + "\" wantsGenieMarketting=\"No\""
					+ " wantsOtherMarketting=\"No\" segmentation=\"none\" customerType=\"CON\""
					+ " contactMedium=\"Email\" saleDate=\"2010-04-06\" "
					+ "owningBusinessDivision=\"O2UK\"><Address PTCABS=\"MATCHED\">"
					+ "<houseName>75</houseName><addressLine1>Hampstead Road</addressLine1>"
					+ "<townCity>LONDON</townCity><postcode>NW1 2PL</postcode><country>UK</country></Address></user></userDetails></users>";

			int returnValueUser  = bean.createOrUpdateUser(createOrUpdateUserXML);

			results.setSuccessful(true);
			results.setResponseCode(Integer.toString(returnValueUser));
			results.setResponseMessage("alternativeEmail: " + "t_" + counter +"@gmail.com" + " User " + UserName);
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