package io.inprice.parser.websites.de;

import java.math.BigDecimal;
import java.util.Set;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import io.inprice.common.models.LinkSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.helpers.StringHelpers;
import io.inprice.parser.info.HttpStatus;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * Parser for MediaMarkt and Saturn Deutschland (protected by cloudflare!!!)
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class MediaMarktDE extends AbstractWebsite {

	//used by Euronics as well
	protected Document dom;

	private JSONObject json;
  private JSONObject offers;

  protected boolean isPageFound() {
  	Element notFoundImg = dom.selectFirst("[data-test='error-page-image-not-found']");
		return (notFoundImg == null);
  }

	@Override
	protected HttpStatus setHtml(String html) {
		dom = Jsoup.parse(html);

		if (isPageFound()) {
      Elements dataEL = dom.select("script[type='application/ld+json']");
      if (dataEL != null && dataEL.size() > 0) {
      	for (DataNode dNode : dataEL.dataNodes()) {
          JSONObject data = new JSONObject(StringHelpers.escapeJSON(dNode.getWholeData()));
          if (data.has("@type")) {
            String type = data.getString("@type");
            if (type.equals("Product")) {
            	json = data;
              if (json.has("offers")) {
            		offers = json.getJSONObject("offers");
            		return HttpStatus.OK;
              }
            }
          }
        }
      }
			return HttpStatus.OK;
		}
		return HttpStatus.NOT_FOUND;
	}

  @Override
  public boolean isAvailable() {
    if (offers != null && offers.has("availability")) {
      String availability = offers.getString("availability").toLowerCase();
      return availability.contains("instock") || availability.contains("preorder");
    }
    return false;
  }

  @Override
  public String getSku() {
    if (json != null && json.has("sku")) {
      return json.getString("sku");
    }
    return Consts.Words.NOT_AVAILABLE;
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
    if (offers != null && offers.has("price")) {
      return offers.getBigDecimal("price");
    }
    return BigDecimal.ZERO;
  }

  @Override
  public String getBrand() {
    if (json != null && json.has("brand")) {
      return json.getJSONObject("brand").getString("name");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getShipment() {
    return "Siehe Lieferbedingungen";
  }

  @Override
  public Set<LinkSpec> getSpecs() {
  	return getKeyValueSpecs(dom.select("[data-test=mms-accordion-features] tr"), "td:nth-child(1)", "td:nth-child(2)");
  }

}
