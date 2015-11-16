import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class sclTest_bak {
	
	private static String expClientID = "90397055";
	private static String currentDC;
	
	public final static HashMap<String, String> tagTypes = new HashMap<String, String>();
    static
    {
    	tagTypes.put("4", "Shop5");
    	tagTypes.put("5", "Product View");
        tagTypes.put("6", "Page View");
        tagTypes.put("8", "Element");
    }
    
    private static void getTags(){
    	//  determine if any tags fired.  If yes then parse and examine
    	
    }
    
    private static void getPageviewTagDetails(Map<String, String> m){
    	//parse out the page view tag details
    	//get ci, pi, cg, attrs (p_a1 etc)
    	
    	System.out.println("TAG: "+ tagTypes.get(m.get("tid")));
    	System.out.println("- Data Collection Dest: "+ currentDC);
    	
    	String clientid = m.get("ci");       //get client id
    	System.out.println("- Client id: "+ clientid);
    	
    	try{
			System.out.println("- Page id: " + URLDecoder.decode(m.get("pi"),"UTF-8"));
		}
		catch (Exception e){
			System.out.println("problem!");
		}
    	
    	String category = m.get("cg");       //get category id
    	System.out.println("- Category: "+ category);
    }
    	
    	private static void getProductviewTagDetails(Map<String, String> m){
        	//parse out the page view tag details
        	//get ci, pi, cg, attrs (p_a1 etc)
    		
    		System.out.println("TAG: "+ tagTypes.get(m.get("tid")));
    		System.out.println("- Data Collection Dest: "+ currentDC);
        	
        	String clientid = m.get("ci");       //get client id
        	System.out.println("- Client id: "+ clientid);
        	
        	String productid = m.get("pr");
        	System.out.println("- Product id: "+ productid);
        	
        	String productname = m.get("pm");
        	System.out.println("- Product name: "+ productname);
        	
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
			case 5: getProductviewTagDetails(tagMap);
					break;	
			case 6: getPageviewTagDetails(tagMap);
                 	break;
    }
		
	}
	
	public static void main(String[] args) throws InterruptedException{
		WebDriver driver = new FirefoxDriver();
		JavascriptExecutor js = (JavascriptExecutor) driver;
		
		//driver.get("http://www.scl.com/technology-services/coremetrics/");
		//driver.get("http://www.scl.com/");
		//driver.get("http://www.interflora.co.uk/product/heavenly-rose-hand-tied-red/?;category_id=2001703");
		driver.get("http://79.125.102.158/mspetshop/ItemDetails.aspx?itemId=EST-21");
		//Boolean b = driver.getPageSource().contains("eluminate.js");
		//if (b){
			//System.out.println("coremetrics library is on page");
		//}
		//get client id variable
		//String clientid = (String)js.executeScript("return cm_ClientID");
		//System.out.println("Client ID: " + clientid);
		
		//if (clientid == expClientID){
			//System.out.println("Client ID Test: Passed");
		//}
		
		//String pageid = (String)js.executeScript("return window.cG7.getPageID(" + clientid + ")");
		//instead of above could use document.cmTagCtl.getPageID(clientid)
		//System.out.println("Page ID: " + pageid);
		
		String tagRequest="";
		Long numTags=(long) 0;
		// attempt to get the number of tags fired.  If not possible exit
		try {
			numTags = (Long)js.executeScript("return document.cmTagCtl.cTI.length");
			System.out.println("Num of tags fired = " + numTags.intValue());
		} catch (Exception e){
			System.out.println("No tags found!!!");
			driver.quit();
			System.exit(1);
		}
		
		for (int i=0;i<numTags.intValue();i++){
			tagRequest=(String)js.executeScript("return document.cmTagCtl.cTI["+ i + "].src");
			System.out.println("Tag request: " + tagRequest);
			parseTag(tagRequest);
			//System.out.println("jamie");
		}
		
		//to get the visitor id cookie use window.cmJSFGetUserId()
		//to get the data collection host use window.cm_PRODUCTION_HOST
		//to get the image request url use window.cm_HOST
		//to get the number of tags fired on the page use window.cG6.cmTagCtl.cTI.length
		//to get each tag request use window.cG6.cmTagCtl.cTI[x].src
		//instead of using window.cG6 can use document.cmTagCtl
		
		Thread.sleep(6000);
		//now find the search box on the home page
		//WebElement searchBox;
		//searchBox = driver.findElement(By.id("tabsearchbutton"));
		//searchBox.sendKeys("lily");
		//searchBox.submit();
		
		driver.quit();
		
	
	}
}
