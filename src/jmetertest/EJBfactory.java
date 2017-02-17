package jmetertest;


import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.ejb.EJBHome;
import javax.ejb.EJBLocalHome;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;

import com.o2.registration.adapter.CompanionAdapter;
import com.o2.registration.adapter.CompanionAdapterHome;
import com.o2.registration.phase2.ejb.entity.Customer;
import com.o2.registration.phase2.ejb.entity.CustomerHome;
import com.o2.registration.phase2.ejb.valueobjects.CustomerVO;
import com.o2.registration.phase2.util.EmailHelper;

public class EJBfactory {
	private static Map<String, EJBHome> homeCache;
	private static Map<String, EJBLocalHome> localHomeCache;
	private static Map<String,String> testDataMap = new HashMap<String, String>();
	
//	private static String tunnelURL = "t3://216.239.197.244:80";
//	private static String tunnelURL = "t3://orangeperformance.ref.o2.co.uk:80";
	
	private static String tunnelURL = "t3://localhost:8080";

	/*public EJBfactory() {
		try {
			loadPropValues();
			System.out.println("Property Loaded");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}*/

	public static void initialise()
	{
		homeCache = Collections.synchronizedMap(new HashMap<String, EJBHome>());
		localHomeCache = Collections.synchronizedMap(new HashMap<String, EJBLocalHome>());
	}

	public static CompanionAdapter callEJB() throws Exception{
		Hashtable<String, String> env = new Hashtable<String,String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY,"weblogic.jndi.WLInitialContextFactory");
		env.put(Context.PROVIDER_URL,tunnelURL);
		Context xx=new InitialContext(env);
		//		System.out.println("CompanionAdapter Initialized "+xx);
		CompanionAdapterHome home = (CompanionAdapterHome) PortableRemoteObject.narrow( xx.lookup("CompanionAdapterBean"), CompanionAdapterHome.class);
		CompanionAdapter bean = (CompanionAdapter) home.create();

		return bean;
	}
	
	
	public static CompanionAdapter callEJB(String tunnelURL) throws Exception{
		Hashtable<String, String> env = new Hashtable<String,String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY,"weblogic.jndi.WLInitialContextFactory");
		env.put(Context.PROVIDER_URL,tunnelURL);
		Context xx=new InitialContext(env);
		//		System.out.println("CompanionAdapter Initialized "+xx);
		CompanionAdapterHome home = (CompanionAdapterHome) PortableRemoteObject.narrow( xx.lookup("CompanionAdapterBean"), CompanionAdapterHome.class);
		CompanionAdapter bean = (CompanionAdapter) home.create();

		return bean;
	}

	public static CustomerHome getLocalHomeInterface() throws Exception {
		Hashtable<String, String> env = new Hashtable<String,String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY,"weblogic.jndi.WLInitialContextFactory");
		env.put(Context.PROVIDER_URL,tunnelURL);
		Context xx=new InitialContext(env);
				System.out.println("CustomerHome Initialized "+xx);
		CustomerHome home = (CustomerHome) PortableRemoteObject.narrow( xx.lookup("customerCMPBean"), CustomerHome.class);
//		CompanionAdapter bean = (CompanionAdapter) home.create(null);

		return home;
	}


	public static String getCustNum(String id, String msisdn) throws Exception{

		System.out.println("before wait");
		Thread.sleep(100);
		System.out.println("after wait");
		String custnum = null;
		try {

			CustomerVO cvo=new CustomerVO();

			CustomerHome custLH = getLocalHomeInterface();
			Customer custL = null;

			if (EmailHelper.isValidEmailAddress(id+"@gmail.com"))
			{
				System.out.println("EmailHelper.isValidEmailAddress  " + id+"@gmail.com");
				System.out.println("id  " + id + "   msisdn    "+ msisdn);
				custL = custLH.findByIdAndMsisdn(id, msisdn);

				System.out.println("custnum found by id and msisdn");
			}
			if (custL != null) {
				cvo = custL.getCustomerVO();
				custnum = Integer.toString(cvo.getCustomerNumber());
				System.out.println("custno : " + custnum);
			}
			return custnum;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return custnum;
	}

	/*public static void loadPropValues() throws IOException {

		Properties properties = new Properties();

		File jarPath = new File(EJBfactory.class.getProtectionDomain().getCodeSource().getLocation().getPath());
		String propertiesPath=jarPath.getParentFile().getAbsolutePath();
		System.out.println(" propertiesPath-"+propertiesPath);
		properties.load(new FileInputStream(propertiesPath+"/Resources/testData.properties"));

		for (String key : properties.stringPropertyNames()) {
			String value = properties.getProperty(key);
			testDataMap.put(key, value);
		}

		for(Entry<String, String> entry:testDataMap.entrySet()){
			String val=entry.getValue();
			String key=entry.getKey();
			System.out.println(key + " = " + val);
		}
	}*/
}
