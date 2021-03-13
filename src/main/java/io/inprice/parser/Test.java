package io.inprice.parser;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;

import io.inprice.parser.pool.WebClientPool;

public class Test {
	
	public static void main(String[] args) throws IOException {
		WebClientPool pool = new WebClientPool();
		pool.setup();

		WebClient webClient = pool.acquire();
		
  	WebRequest req = new WebRequest(new URL("https://www.bestbuy.com/site/compex-ayre-wireless-rapid-recovery-compression-boots-black/6413832.p?skuId=6413832"));

		WebResponse res = webClient.loadWebResponse(req);
		System.out.println(res.getContentAsString());
		
		pool.release(webClient);
		pool.shutdown();
	}
	
	private static String referrer;
	
	public static void main5(String[] args) {
		System.out.println(getAlternativeUrl("https://www.bestbuy.com/site/compex-ayre-wireless-rapid-recovery-compression-boots-black/6413832.p?skuId=6413832&intl=nosplash"));
		System.out.println(referrer);
		
		System.out.println(getAlternativeUrl("https://www.bestbuy.com/site/compex-ayre-wireless-rapid-recovery-compression-boots-black/6413832.p"));
		System.out.println(referrer);
	}
	
	private static String getAlternativeUrl(String zurl) {
		String url = zurl;
		boolean hasIntl = url.indexOf("intl=nosplash") > 0;
		
		if (hasIntl) {
			referrer = url.replaceAll(".intl=nosplash", "");
		} else {
			referrer = url;
			url = url + (url.indexOf("?") > 0 ? "&" : "?") + "intl=nosplash";
		}
		return url;
	}
	
	public static void main4(String[] args) {
		String str = "\"{\"fsa\":\"L5V\",\"products\":[{\"productId\":\"6BQE42ZH54OW\",\"skuIds\":[\"6M7ZDWKVHO3T\"]}],\"lang\":\"en\",\"pricingStoreId\":\"1061\",\"fulfillmentStoreId\":\"1061\",\"experience\":\"whiteGM\"}\"";
		
    String expInd = "\"skuIds\":[\"";
    int expPos = str.indexOf(expInd)+expInd.length();
    
    System.out.println(expPos + " - " + str.indexOf("\"", expPos));
    
    String experience = str.substring(expPos, str.indexOf("\"", expPos));
		System.out.println(experience);
		
		System.out.println(experience.substring(0, 3));
	}
	
	public static void main3(String[] args) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		//String url1 = "https://www.walmart.ca/en/ip/koolatron-retro-portable-6-can-thermoelectric-mini-fridge-cooler-4-l42-quarts-capacity-12v-dc110v-ac-aqua-for-home-dorm-car-boat-beverages-snacks-skin/6000199652305";
		String url = "https://www.walmart.ca/en/ip/Pool-Skimmer-Net-with-Telescopic-Pole-Removal-Leaf-Rake-Swimming-Pool-Ponds-Fast-Cleaning-Tool/6BQE42ZH54OW?rrid=richrelevance";

		WebClient webClient = new WebClient();
    webClient.getOptions().setUseInsecureSSL(true); //ignore ssl certificate
    webClient.getOptions().setJavaScriptEnabled(false);
    webClient.getOptions().setThrowExceptionOnScriptError(false);
    webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);

		WebResponse res = null;

		res = webClient.loadWebResponse(new WebRequest(new URL(url)));
    System.out.println(res.getContentAsString());

    System.out.println("------------------------------------------------------------------------------------");

		WebRequest req = new WebRequest(new URL("https://www.walmart.ca/api/product-page/v2/price-offer"));
		req.setHttpMethod(HttpMethod.POST);
		req.setAdditionalHeader("User-Agent", "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:86.0) Gecko/20100101 Firefox/86.0");
		req.setAdditionalHeader("Accept", "application/json");
		req.setAdditionalHeader("content-type", "application/json");
		req.setRequestBody("{\"fsa\":\"L5V\",\"products\":[{\"productId\":\"6BQE42ZH54OW\",\"skuIds\":[\"6M7ZDWKVHO3T\"]}],\"lang\":\"en\",\"pricingStoreId\":\"1061\",\"fulfillmentStoreId\":\"1061\",\"experience\":\"whiteGM\"}");

    res = webClient.loadWebResponse(req);
    System.out.println(res.getContentAsString());

    webClient.close();
	}
	
	
	public static void main2(String[] args) {
		String url = "https://www.canadiantire.ca/en/pdp/kids-reversible-foam-tiles-24-in-0686044p.html#srp";

		String[] urlParts = url.split("-");
		String lastOne = urlParts[urlParts.length-1].split("\\.")[0];
		System.out.println(lastOne.replaceAll("[^0-9]", "").trim());
		
		int pPoint = url.indexOf("p.");
		String sku = url.substring(pPoint-7, pPoint);
		System.out.println(sku);
	}
  
  public static void main1(String[] args) throws IOException {
  	String doc = new String(Files.readAllBytes( Paths.get("/home/mdpinar/tmp/apple-test.html")));

  	String rawJson = findAPart(doc, "\"product\":", ",\"productSellers\":{", 0, 0);
  	System.out.println(rawJson);
  	/*
  	JSONObject info = null;
  	JSONObject price = null;
  	JSONArray details = null;
  	
		if (StringUtils.isNotBlank(rawJson)) {
			JSONObject json = new JSONObject(rawJson);
			if (json != null && json.has("model")) {
				JSONObject model = json.getJSONObject("model");
				if (model != null) {
					if (model.has("articleInfo")) {
						info = model.getJSONObject("articleInfo");
					}
					if (model.has("displayPrice")) {
						price = model.getJSONObject("displayPrice");
					}
					if (model.has("productDetailsCluster")) {
						details = model.getJSONArray("productDetailsCluster");
					}
				}
			}
		}
  	System.out.println("");
  	*/
  }

  private static String findAPart(String html, String starting, String ending, int plus, int startPointOffset) {
		int start = html.indexOf(starting) + (startPointOffset <= 0 ? starting.length() : 0);
		int end = html.indexOf(ending, start) + plus;

		if (start > starting.length() && end > start) {
			return html.substring(start+startPointOffset, end);
		}

		return null;
	}

}