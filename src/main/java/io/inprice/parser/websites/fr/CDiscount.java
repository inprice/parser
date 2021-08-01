package io.inprice.parser.websites.fr;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import com.gargoylesoftware.htmlunit.HttpHeader;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;

import io.inprice.common.models.LinkSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * Parser for CDiscount France
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class CDiscount extends AbstractWebsite {

	private String sku;
	private JSONObject json;

	protected WebResponse makeRequest(WebClient webClient) throws MalformedURLException, IOException {
		String referer = getUrl();
		sku = referer.substring(referer.lastIndexOf("-")+1, referer.length()-5);

		WebRequest req = new WebRequest(new URL("https://www.cdiscount.com/GetAlgoProducts/0"), HttpMethod.POST);
		req.setAdditionalHeader(HttpHeader.ACCEPT, "application/json, text/javascript, */*; q=0.01");
    req.setAdditionalHeader(HttpHeader.CONTENT_TYPE, "application/x-www-form-urlencoded; charset=UTF-8");
		req.setAdditionalHeader(HttpHeader.REFERER, referer);
    
		req.setRequestBody(
				"ControlId=7583423&ProductId="+sku+"&SkuList=&Category=&SellerId=&Algorithm=SimilarProducts&FirstProductIndex=0&SitemapNodeId=13148&Name=&PaginationMode=0" +
				"&PreloadedSkuList=&Hash=&ViewedKey=&ForcedVersion=&Context=&PageType=Product&CarrouselType=&DepartementId=12103050401&TrackingPixel=&EbWidgetId=&EbContexte=" +
				"&IsAboveWaterLine=false&DepartmentId=12103050401&SwordVersion=&BrandName=&ProductName=&SearchId=&Prefix=&ErrorOnPostDataInit=false&ErrorsListOnPostDataInit=&CommonId="
			);
		return webClient.loadWebResponse(req);
	}
	
	@Override
	protected void setHtml(String html) {
		super.setHtml(html);

		JSONObject root = new JSONObject(html.replaceAll("\\p{C}", "")); //can have non-printable chars, lets clean them up!
		if (root != null && root.has("products")) {
			JSONArray prods = root.getJSONArray("products");
			if (prods != null && prods.length() > 0) {
				for (int i = 0; i < prods.length(); i++) {
					JSONObject prod = prods.getJSONObject(i);
					if (prod != null && prod.has("sku") && prod.getString("sku").equalsIgnoreCase(sku)) {
						json = prod;
						break;
					}
				}
			}
		}
	}

  @Override
  public boolean isAvailable() {
  	if (json != null && json.has("stock")) {
  		return json.getInt("stock") > 0;
  	}
    return false;
  }

  @Override
  public String getSku() {
    return sku;
  }

  @Override
  public String getName() {
  	if (json != null && json.has("name")) {
  		return json.getString("name");
  	}
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
  	if (json != null && json.has("prx")) {
  		JSONObject prx = json.getJSONObject("prx");
  		if (prx != null && prx.has("val")) {
  			return new BigDecimal(cleanDigits(Jsoup.parse(prx.getString("val").replace("&euro;", ",")).text()));
  		}
  	}
  	return BigDecimal.ZERO;
  }

  @Override
  public String getBrand() {
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getShipment() {
    return "Vendu et expédié par " + getSeller();
  }

  @Override
  public Set<LinkSpec> getSpecs() {
    return null;
  }

}
