package jmetertest;


import java.io.Serializable;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

import com.o2.registration.adapter.CompanionAdapter;

public class CreateUser extends AbstractJavaSamplerClient implements Serializable {
	private static final long serialVersionUID = 1L;

	public void setupTest(JavaSamplerContext context) {}

	public Arguments getDefaultParameters() {
		Arguments defaultParameters = new Arguments();
		defaultParameters.clear();
		defaultParameters.addArgument("userIdCreateUser", "${__counter(FALSE,user)}");
		defaultParameters.addArgument("UserNameCreateUser", "spa20");
		defaultParameters.addArgument("msisdnCreateUser", "07000000050");
		defaultParameters.addArgument("domainCreateUser", "stf.ref.o2.co.uk");

		return defaultParameters;
	}
	public SampleResult runTest(JavaSamplerContext context) {
		SampleResult results = new SampleResult();
		String custnum = null;

		try{                     
			results.sampleStart();
			results.setSuccessful(false);

			String counterID = context.getParameter("userIdCreateUser");
			String userName = context.getParameter("UserNameCreateUser");
			String msisdn = context.getParameter("msisdnCreateUser");
			String domain = context.getParameter("domainCreateUser");

			CompanionAdapter bean = EJBfactory.callEJB();

			long newmsisdn = Long.parseLong(msisdn);
			long newcounterID = Long.parseLong(counterID);

			newmsisdn = newmsisdn + newcounterID ;

			msisdn = "0"+ Long.toString(newmsisdn);
			String msisdnInternational = "44"+ Long.toString(newmsisdn);
			
			System.out.println("msisdn  " + msisdn);
			
			String fullUserName = userName + counterID ; 

			String createOrUpdateUserXML = "<users><userDetails action=\"Create\">"
					+ "<user custnum=\"0\" id=\"" + fullUserName +"\""
					+ " domain=\""+domain+"\""
					+ " password=\"Password123\" forename=\"Perf\" lastname=\"Test\""
					+ " title=\"MR\" alternativeContactNumber=\""+msisdn+"\""
					+ " alternativeEmail=\"" + fullUserName +"@gmail.com\" dateOfBirth=\"1980-08-11\""
					+ " partner=\"CPW\" MSISDN=\""+msisdn+"\" wantsGenieMarketting=\"No\""
					+ " wantsOtherMarketting=\"No\" segmentation=\"none\" customerType=\"CON\""
					+ " contactMedium=\"Email\" saleDate=\"2010-04-06\""
					+ " SecurityQuestion=\"Memorable name\" SecurityAnswer=\"prasad\""
					+ " owningBusinessDivision=\"O2UK\"><Address PTCABS=\"MATCHED\">"
					+ "<houseName>75</houseName><addressLine1>Hampstead Road</addressLine1>"
					+ "<townCity>LONDON</townCity><postcode>NW1 2PL</postcode><country>UK</country></Address></user></userDetails></users>";
			
			int returnValueUser = bean.createOrUpdateUser(createOrUpdateUserXML);
			System.out.println("createOrUpdateUserXML       "+createOrUpdateUserXML);
			System.out.println("returnValueUser :   "+ returnValueUser);
			results.setResponseMessage(Integer.toString(returnValueUser));
			
			if (returnValueUser == 0 || returnValueUser == 1 ) {
				
				

				String createOrUpdateMsisdnProfileXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
						+ "<msisdnProfile xmlns=\"http://reg.eai.o2c.ibm.com\">"
						+ "<userid>"+fullUserName+"</userid><domain>"+domain+"</domain>"
						+ "<msisdn>"+msisdn+"</msisdn><msisdntype>POST-PAY</msisdntype><msisdnstatus>ACTIVE</msisdnstatus>"
						+ "<msisdnservicestatus>INSTALL</msisdnservicestatus><genevaaccountid>TESTCOG"+ counterID +"</genevaaccountid></msisdnProfile>";

				int returnValueMsisdnProfile = bean.createOrUpdateMsisdnProfile(createOrUpdateMsisdnProfileXML);
				
				System.out.println("createOrUpdateMsisdnProfileXML    " + createOrUpdateMsisdnProfileXML);

				System.out.println("returnValueMsisdnProfile :   "+ returnValueMsisdnProfile);
				
				if (returnValueMsisdnProfile == 0 || returnValueMsisdnProfile == 1 ) {
					
					custnum = EJBfactory.getCustNum(fullUserName, msisdnInternational);
					
					System.out.println("custNum :   "+ custnum);

					String createOrUpdateRoleSetXML = "<roles xmlns=\"http://reg.eai.o2c.ibm.com\" custnum=\""+custnum+"\" id=\""+ fullUserName +"\""
							+ " domain=\""+domain+"\""
							+ " action=\"Create\"><roleSet><chooserRole><GenevaAccountId>TESTCOG"+counterID+"</GenevaAccountId>"
							+ "<GenevaAccountName>MM TESTCOG"+counterID+"</GenevaAccountName><GenevaCustomerId>TESTCOG"+counterID+"</GenevaCustomerId>"
							+ "<GenevaCustomerName>MM TESTCOG"+counterID+"</GenevaCustomerName><CompanyCustomerType>CON</CompanyCustomerType>"
							+ "<HorizonCompanyCustnum>"+custnum+"</HorizonCompanyCustnum><AccountType>Consumer</AccountType>"
							+ "</chooserRole><chooserRole><GenevaAccountId>TESTCOG"+counterID+"-2</GenevaAccountId><GenevaAccountName>MM TESTCOG"+counterID+"-2</GenevaAccountName>"
							+ "<GenevaCustomerId>TESTCOG"+counterID+"</GenevaCustomerId><GenevaCustomerName>MM TESTCOG"+counterID+"</GenevaCustomerName>"
							+ "<CompanyCustomerType>CON</CompanyCustomerType><HorizonCompanyCustnum>"+custnum+"</HorizonCompanyCustnum>"
							+ "<AccountType>Consumer</AccountType></chooserRole><superchooserRole><GenevaCustomerId>TESTCOG"+counterID+"</GenevaCustomerId>"
							+ "<GenevaCustomerName>MM TESTCOG"+counterID+"</GenevaCustomerName><HorizonCompanyCustnum>"+custnum+"</HorizonCompanyCustnum>"
							+ "<CompanyCustomerType>CON</CompanyCustomerType></superchooserRole></roleSet></roles>";

					int returnValueRole = bean.createOrUpdateRoleSet(createOrUpdateRoleSetXML);
					System.out.println("returnValueRole  : " + returnValueRole);
					if (returnValueMsisdnProfile == 0 || returnValueMsisdnProfile == 1 ) {
						results.setSuccessful(true);
					}

//					results.setResponseCode(Integer.toString(returnValueUser +returnValueMsisdnProfile +returnValueRole ));
					results.setResponseCode(custnum);
					results.setResponseMessage("myResult: " + results + " returnValueUser:" +returnValueUser 
							+ " returnValueRole:"+returnValueRole+ " returnValueMsisdnProfile:" +returnValueMsisdnProfile);
					results.setResponseHeaders(custnum);
				}

			}

		} catch (Exception ce){
			//System.out.println("in exception "+ce.getMessage()+"  "+ce.getCause());
			results.setResponseMessage(ce.getMessage()+"  "+ce.getCause());
			results.setSuccessful(false);
		}
		results.sampleEnd();
		return results;
	}
	public void teardownTest(JavaSamplerContext context) {}
}
