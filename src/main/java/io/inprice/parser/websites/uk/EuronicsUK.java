package io.inprice.parser.websites.uk;

import java.math.BigDecimal;
import java.util.Set;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import io.inprice.common.models.LinkSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.helpers.StringHelpers;
import io.inprice.parser.info.HttpStatus;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * Parser for Euronics United Kingdom
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class EuronicsUK extends AbstractWebsite {

	private Document dom;

	private JSONObject json;
  private JSONObject offers;

  @Override
	protected Renderer getRenderer() {
		return Renderer.HTMLUNIT;
	}

	@Override
	protected HttpStatus setHtml(String html) {
		dom = Jsoup.parse(html);

    Elements dataEL = dom.select("script[type='application/ld+json']");
    if (dataEL != null && dataEL.size() > 0) {
    	for (DataNode dNode : dataEL.dataNodes()) {
    		JSONObject data = new JSONObject(StringHelpers.escapeJSON(dNode.toString()).replaceAll("\"description\".*(\"offers\":)", "$1"));
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
		return HttpStatus.NOT_FOUND;
	}

  @Override
  public boolean isAvailable() {
    return true;
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
      return json.getString("brand");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getShipment() {
    return "Check delivery options";
  }

  @Override
  public Set<LinkSpec> getSpecs() {
  	return getKeyValueSpecs(dom.select("table.classifications tr"), "td:nth-child(1)", "td:nth-child(2)");
  }

}
