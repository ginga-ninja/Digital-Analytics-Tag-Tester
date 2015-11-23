import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class DATester {
	
	//private static String expClientID = "90397055";
	private static String currentDC;
	private static ExtentReports extentReport;
	private static ExtentTest testRun;
	private static int stepCounter;
	
	public final static HashMap<String, String> tagTypes = new HashMap<String, String>();
    static
    {
    	tagTypes.put("1", "Page View");
    	tagTypes.put("2", "Registration");
    	tagTypes.put("3", "Order");
    	tagTypes.put("4", "Shop");
    	tagTypes.put("5", "Product View");
        tagTypes.put("6", "Page View + Tech Props");
        tagTypes.put("8", "Element");
        tagTypes.put("14", "Conversion Event");
        tagTypes.put("15", "Element");
    }
    
    public DATester(String testName, String desc, String reportLoc){
    	extentReport = new ExtentReports(reportLoc, true);
    	testRun = extentReport.startTest(testName, desc);
    	stepCounter = 0;
    }
    
    //public static void init(String reportName){
    	//init the extent report here
    //	extentReport = new ExtentReports("/Users/jamielockhart/PetshopOrder.html", true);
    //	testRun = extentReport.startTest(reportName, "IBM Digital Analytics Tag Test Report");
    	//stepCounter = 0;
    //}
    
    public void checkForLib(WebDriver driver){
    	Boolean b = driver.getPageSource().contains("eluminate.js");
		if (b){
			System.out.println("Coremetrics library - eluminate.js - is on page");
		} else {
			System.out.println("Coremetrics library - eluminate.js - not found on page");
		}
    }
    
    public void getTags(WebDriver driver, JavascriptExecutor js){
    	//  determine if any tags fired.  If yes then parse and examine
    	//testRun.log(LogStatus.INFO, "Step", driver.getCurrentUrl());
    	
    	stepCounter = stepCounter+1;
    	System.out.println("STEP " + stepCounter);
    	testRun.log(LogStatus.INFO, "Step "+ stepCounter, driver.getCurrentUrl());
    	
    	String tagRequest="";
		Long numTags=(long) 0;
		// attempt to get the number of tags fired.  If not possible exit
		try {
			numTags = (Long)js.executeScript("return document.cmTagCtl.cTI.length");
			System.out.println("Num of tags fired = " + numTags.intValue());
			testRun.log(LogStatus.INFO, "", "Num of tags fired = " + numTags.intValue());
		} catch (Exception e){
			System.out.println("No tags found!!!");
			testRun.log(LogStatus.INFO, "", "No tags found on this page.");
			//driver.quit();
			//System.exit(1);
			return;
		}
		
		for (int i=0;i<numTags.intValue();i++){
			tagRequest=(String)js.executeScript("return document.cmTagCtl.cTI["+ i + "].src");
			//System.out.println("Tag request: " + tagRequest);
			parseTag(tagRequest);
			//System.out.println("jamie");
		}
    	
    }
    
    private static void getPageviewTagDetails(Map<String, String> m){
    	//parse out the page view tag details
    	//get ci, pi, cg, attrs (p_a1 etc)
    	
    	//System.out.println("TAG: "+ tagTypes.get(m.get("tid")));
    	testRun.log(LogStatus.INFO, "", tagTypes.get(m.get("tid")));
    	//System.out.println("- Data Collection Destination: "+ currentDC);
    	testRun.log(LogStatus.INFO, "", " Data Collection Destination: "+ currentDC);
    	
    	String clientid = m.get("ci");       //get client id
    	//System.out.println("- Client id: "+ clientid);
    	testRun.log(LogStatus.INFO, "", " Client ID: " + clientid);
    	
    	//get page id
    	try{
			//System.out.println("- Page id: " + URLDecoder.decode(m.get("pi"),"UTF-8"));
			testRun.log(LogStatus.INFO, "", " Page ID: " + URLDecoder.decode(m.get("pi"),"UTF-8"));
			//System.out.println("- Page URL: " + URLDecoder.decode(m.get("ul"),"UTF-8"));
		}
		catch (Exception e){
			System.out.println("Decoding problem");
		}
    	
    	//get page category id
    	String category = m.get("cg");       //get category id
    	//System.out.println("- Category: "+ category);
    	testRun.log(LogStatus.INFO, "", " Category ID: " + category);
    	
    	//get explore attributes
    	for (int i=1;i<=50;i++){
        	if (m.containsKey("pv_a"+i)){
        		//System.out.println("Attribute " + i + ": " + m.get("pv_a" + i));
        		testRun.log(LogStatus.INFO, "", " Attribute " + i + ": " + m.get("pv_a" + i));
        	}
        }
    }
    	
    	private static void getProductviewTagDetails(Map<String, String> m){
        	//parse out the page view tag details
        	//get ci, pi, cg, attrs (p_a1 etc)
    		
    		System.out.println("TAG: "+ tagTypes.get(m.get("tid")));
    		System.out.println("- Data Collection Dest: "+ currentDC);
        	
        	String clientid = m.get("ci");       //get client id
        	System.out.println("- Client id: "+ clientid);
        	
        	try{
    			System.out.println("- Product id: " + URLDecoder.decode(m.get("pr"),"UTF-8"));
    			System.out.println("- Product Name: " + URLDecoder.decode(m.get("pm"),"UTF-8"));
    			//System.out.println("- Page URL: " + URLDecoder.decode(m.get("ul"),"UTF-8"));
    		}
    		catch (Exception e){
    			System.out.println("decoding problem!");
    		}
        	
        	//String productid = m.get("pr");
        	//System.out.println("- Product id: "+ productid);
        	
        	//String productname = m.get("pm");
        	//System.out.println("- Product name: "+ productname);
        	
        	String category = m.get("cg");       //get category id
        	System.out.println("- Category: "+ category);
        	
        	//try{
    			//System.out.println("Page id = " + URLDecoder.decode(m.get("pi"),"UTF-8"));
    		//}
    		//catch (Exception e){
    		//	System.out.println("problem!");
    		//}
        	
        	//get explore attributes
        	for (int i=1;i<=50;i++){
            	if (m.containsKey("pr_a"+i)){
            		System.out.println("Attribute " + i + ": " + m.get("pr_a" + i));
            	}
            }
    	
    }
	
	private static void parseTag(String t) {
		Map<String, String> tagMap = new HashMap<String, String>();
		String[] parts = t.split("\\?");
		
		//System.out.println(parts.length);
		String dc_url = parts[0];      //data collection url
		String qs = parts[1];
		//System.out.println("Tag params: " + qs);
		
		String[] params = qs.split("&");
		//System.out.println(params.length);
		
		//build map of tag params
		for (int i=0;i<params.length;i++){
			//System.out.println(params[i]);
			String[] tmp = params[i].split("=");
			if (tmp.length > 1){
				tagMap.put(tmp[0], tmp[1]);
			}
		}
		
		//System.out.println("Client id = " + tagMap.get("ci"));
		//System.out.println("Tag type id = " + tagMap.get("tid"));
		//System.out.println("Tag type fired = " + tagTypes.get(tagMap.get("tid")));
		
		//record the data collection domain so that it can be referenced in tag methods
		currentDC = dc_url;
		
		int tid = Integer.parseInt(tagMap.get("tid"));
		switch (tid) {
			case 1: getPageviewTagDetails(tagMap);
					break;
			case 2: getRegistrationTagDetails(tagMap);
					break;
			case 3: getOrderTagDetails(tagMap);
					break;
			case 4: getShopTagDetails(tagMap);
					break;
			case 5: getProductviewTagDetails(tagMap);
					break;
			case 6: getPageviewTagDetails(tagMap);
                 	break;
			case 14: getConversionEventTagDetails(tagMap);
         			break;
			case 15: getElementTagDetails(tagMap);
					break;
			
		}
		
	}
	
	private static void getElementTagDetails(Map<String, String> m) {
		// TODO Auto-generated method stub
		System.out.println("TAG: "+ tagTypes.get(m.get("tid")));
    	testRun.log(LogStatus.INFO, "", tagTypes.get(m.get("tid")));
    	
    	String eid = m.get("eid");       
    	//System.out.println("- Client id: "+ clientid);
    	testRun.log(LogStatus.INFO, "", "Element ID: " + eid);
    	
    	String ecat = m.get("ecat");      
    	//System.out.println("- Client id: "+ clientid);
    	testRun.log(LogStatus.INFO, "", "Element Category: " + ecat);
    	
    	//get explore attributes
    	for (int i=1;i<=50;i++){
        	if (m.containsKey("e_a"+i)){
        		//System.out.println("Attribute " + i + ": " + m.get("pv_a" + i));
        		testRun.log(LogStatus.INFO, "", " Attribute " + i + ": " + m.get("e_a" + i));
        	}
        }
		
	}

	public void completeReport(){
		// called at end of test - flushes the report
		testRun.log(LogStatus.PASS, "Test complete. Check your tag output.");
	    extentReport.endTest(testRun);
		extentReport.flush();
	}

	private static void getRegistrationTagDetails(Map<String, String> m) {
		// TODO Auto-generated method stub
		//cd = regid, cm = registrants email address
		System.out.println("TAG: "+ tagTypes.get(m.get("tid")));
		testRun.log(LogStatus.INFO, "", tagTypes.get(m.get("tid")));
		
		String cd = m.get("cd");       //get cust id
    	System.out.println("- Customer ID: "+ cd);
    	testRun.log(LogStatus.INFO, "", "Customer ID: " + cd);
    	
    	//get explore attributes
    	for (int i=1;i<=50;i++){
        	if (m.containsKey("rg_a"+i)){
        		//System.out.println("Attribute " + i + ": " + m.get("pv_a" + i));
        		testRun.log(LogStatus.INFO, "", " Attribute " + i + ": " + m.get("rg_a" + i));
        	}
        }
    	
		
	}

	private static void getShopTagDetails(Map<String, String> m) {
		// TODO Auto-generated method stub
		String shopTag;
		
		if (!m.containsKey("on")) {
			System.out.println("TAG: " + tagTypes.get(m.get("tid"))+"5");
			shopTag = tagTypes.get(m.get("tid"))+"5";
		} else {
			System.out.println("TAG: " + tagTypes.get(m.get("tid"))+"9");
			shopTag = tagTypes.get(m.get("tid"))+"9";
		}
		testRun.log(LogStatus.INFO, "", shopTag);
		
    	//System.out.println("- Data Collection Destination: "+ currentDC);
    	
    	String clientid = m.get("ci");       //get client id
    	System.out.println("- Client id: "+ clientid);
    	testRun.log(LogStatus.INFO, "", " Client ID: " + clientid);
    	
    	String pr = m.get("pr");       //get product number
    	System.out.println("- Product Number: "+ pr);
    	testRun.log(LogStatus.INFO, "", "Product Number: " + pr);
    	
    	String pm = m.get("pm");       //get product name
    	System.out.println("- Product Name: "+ pm);
    	testRun.log(LogStatus.INFO, "", "Product Name: " + pm);
    	
    	String category = m.get("cg");       //get category id
    	System.out.println("- Prod Category: "+ category);
    	testRun.log(LogStatus.INFO, "", " Product Category ID: " + category);
    	
    	String qt = m.get("qt");       //get quantity
    	System.out.println("- Quantity: "+ qt);
    	testRun.log(LogStatus.INFO, "", "Quantity: " + qt);
    	
    	String bp = m.get("bp");       //get base price
    	System.out.println("- Base Price: "+ qt);
    	testRun.log(LogStatus.INFO, "", "Base Price: " + qt);
    	
    	if (shopTag.equals("Shop9")){
    		String on = m.get("on");       //get quantity
        	System.out.println("- Order Number: "+ on);
        	testRun.log(LogStatus.INFO, "", "Order Number: " + on);
        	
        	String tr = m.get("tr");       //get base price
        	System.out.println("- Total Revenue: "+ tr);
        	testRun.log(LogStatus.INFO, "", "Total Revenue: " + tr);
        	
        	String cd = m.get("cd");       //get cust id
        	System.out.println("- Customer ID: "+ cd);
        	testRun.log(LogStatus.INFO, "", "Customer ID: " + cd);
    	}
    	
    	//get explore attributes
    	for (int i=1;i<=50;i++){
        	if (m.containsKey("s_a"+i)){
        		//System.out.println("Attribute " + i + ": " + m.get("pv_a" + i));
        		testRun.log(LogStatus.INFO, "", " Attribute " + i + ": " + m.get("s_a" + i));
        	}
        }
				
		
	}

	private static void getConversionEventTagDetails(Map<String, String> m) {
		// TODO Auto-generated method stub
		
		System.out.println("TAG: "+ tagTypes.get(m.get("tid")));
    	testRun.log(LogStatus.INFO, "", tagTypes.get(m.get("tid")));
    	
    	String cid = m.get("cid");       
    	//System.out.println("- Client id: "+ clientid);
    	testRun.log(LogStatus.INFO, "", "Conversion Event ID: " + cid);
    	
    	String ccid = m.get("ccid");      
    	//System.out.println("- Client id: "+ clientid);
    	testRun.log(LogStatus.INFO, "", "Conversion Event Category: " + ccid);
    	
    	String cat = m.get("cat");       
    	//System.out.println("- Client id: "+ clientid);
    	testRun.log(LogStatus.INFO, "", "Conversion Event Action Type: " + cat);
    	
    	String cpt = m.get("cpt");       
    	//System.out.println("- Client id: "+ clientid);
    	testRun.log(LogStatus.INFO, "", "Conversion Event Points: " + cpt);
    	
    	//get explore attributes
    	for (int i=1;i<=50;i++){
        	if (m.containsKey("c_a"+i)){
        		//System.out.println("Attribute " + i + ": " + m.get("pv_a" + i));
        		testRun.log(LogStatus.INFO, "", " Attribute " + i + ": " + m.get("c_a" + i));
        	}
        }
	}

	private static void getOrderTagDetails(Map<String, String> m) {
		// TODO Auto-generated method stub
		//parse out the page view tag details
    	//get on, cd, tr, attrs (p_a1 etc)
    	
    	//System.out.println("TAG: "+ tagTypes.get(m.get("tid")));
    	testRun.log(LogStatus.INFO, "", tagTypes.get(m.get("tid")));
    	
    	//System.out.println("- Data Collection Destination: "+ currentDC);
    	
    	String clientid = m.get("ci");       //get client id
    	//System.out.println("Client id: "+ clientid);
    	testRun.log(LogStatus.INFO, "", "Client id: "+ clientid);
    	
    	try{
			//System.out.println("- Page id: " + URLDecoder.decode(m.get("pi"),"UTF-8"));
			System.out.println("- Page URL: " + URLDecoder.decode(m.get("ul"),"UTF-8"));
		}
		catch (Exception e){
			System.out.println("decoding problem!");
		}
    	
    	String orderNumber = m.get("on");       //get category id
    	//System.out.println("- Order Number: "+ orderNumber);
    	testRun.log(LogStatus.INFO, "", "Order Number: "+ orderNumber);
    	
    	String orderTotal = m.get("tr");       //get category id
    	//System.out.println("- Order Total: "+ orderTotal);
    	testRun.log(LogStatus.INFO, "", "Order Total: "+ orderTotal);
    	
    	String custId = m.get("cd");       //get category id
    	//System.out.println("- Customer ID: "+ custId);
    	testRun.log(LogStatus.INFO, "", "Customer ID: "+ custId);
    	
    	//get explore attributes
    	for (int i=1;i<=50;i++){
        	if (m.containsKey("o_a"+i)){
        		//System.out.println("- Attribute " + i + ": " + m.get("o_a" + i));
        		testRun.log(LogStatus.INFO, "", "Attribute " + i + ": " + m.get("o_a" + i));
        	}
        }
		
	}
	
	
}
