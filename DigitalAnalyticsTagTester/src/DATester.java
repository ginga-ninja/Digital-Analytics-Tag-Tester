import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class DATester {
	
	private static String expClientID = "90397055";
	private static String currentDC;
	
	public final static HashMap<String, String> tagTypes = new HashMap<String, String>();
    static
    {
    	tagTypes.put("1", "Page View");
    	tagTypes.put("3", "Order");
    	tagTypes.put("4", "Shop");
    	tagTypes.put("5", "Product View");
        tagTypes.put("6", "Page View + Tech Props");
        tagTypes.put("8", "Element");
        tagTypes.put("14", "Conversion Event");
    }
    
    public static void checkForLib(WebDriver driver){
    	Boolean b = driver.getPageSource().contains("eluminate.js");
		if (b){
			System.out.println("Coremetrics library - eluminate.js - is on page");
		} else {
			System.out.println("Coremetrics library - eluminate.js - not found on page");
		}
    }
    
    public static void getTags(WebDriver driver, JavascriptExecutor js){
    	//  determine if any tags fired.  If yes then parse and examine
    	String tagRequest="";
		Long numTags=(long) 0;
		// attempt to get the number of tags fired.  If not possible exit
		try {
			numTags = (Long)js.executeScript("return document.cmTagCtl.cTI.length");
			System.out.println("Num of tags fired = " + numTags.intValue());
		} catch (Exception e){
			System.out.println("No tags found!!!");
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
    	
    	System.out.println("TAG: "+ tagTypes.get(m.get("tid")));
    	System.out.println("- Data Collection Destination: "+ currentDC);
    	
    	String clientid = m.get("ci");       //get client id
    	System.out.println("- Client id: "+ clientid);
    	
    	try{
			System.out.println("- Page id: " + URLDecoder.decode(m.get("pi"),"UTF-8"));
			System.out.println("- Page URL: " + URLDecoder.decode(m.get("ul"),"UTF-8"));
		}
		catch (Exception e){
			System.out.println("decoding problem!");
		}
    	
    	String category = m.get("cg");       //get category id
    	System.out.println("- Category: "+ category);
    	
    	//get explore attributes
    	for (int i=1;i<=50;i++){
        	if (m.containsKey("pv_a"+i)){
        		System.out.println("Attribute " + i + ": " + m.get("pv_a" + i));
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
    }
		
	}

	private static void getShopTagDetails(Map<String, String> m) {
		// TODO Auto-generated method stub
		
		if (!m.containsKey("on")) {
			System.out.println("TAG: " + tagTypes.get(m.get("tid"))+"5");
		} else {
			System.out.println("TAG: " + tagTypes.get(m.get("tid"))+"9");
		}
				
		
	}

	private static void getConversionEventTagDetails(Map<String, String> m) {
		// TODO Auto-generated method stub
		System.out.println("in conversion event tag");
		
	}

	private static void getOrderTagDetails(Map<String, String> m) {
		// TODO Auto-generated method stub
		//parse out the page view tag details
    	//get on, cd, tr, attrs (p_a1 etc)
    	
    	System.out.println("TAG: "+ tagTypes.get(m.get("tid")));
    	System.out.println("- Data Collection Destination: "+ currentDC);
    	
    	String clientid = m.get("ci");       //get client id
    	System.out.println("- Client id: "+ clientid);
    	
    	try{
			//System.out.println("- Page id: " + URLDecoder.decode(m.get("pi"),"UTF-8"));
			System.out.println("- Page URL: " + URLDecoder.decode(m.get("ul"),"UTF-8"));
		}
		catch (Exception e){
			System.out.println("decoding problem!");
		}
    	
    	String orderNumber = m.get("on");       //get category id
    	System.out.println("- Order Number: "+ orderNumber);
    	
    	String orderTotal = m.get("tr");       //get category id
    	System.out.println("- Order Total: "+ orderTotal);
    	
    	String custId = m.get("cd");       //get category id
    	System.out.println("- Customer ID: "+ custId);
    	
    	//get explore attributes
    	for (int i=1;i<=50;i++){
        	if (m.containsKey("o_a"+i)){
        		System.out.println("Attribute " + i + ": " + m.get("o_a" + i));
        	}
        }
		
	}
	
	
}
