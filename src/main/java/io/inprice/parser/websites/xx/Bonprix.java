package io.inprice.parser.websites.xx;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.HttpHeader;
import com.gargoylesoftware.htmlunit.WebRequest;

import io.inprice.common.models.LinkSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.websites.AbstractWebsite;

/**
 * Parser for Bonprix Global
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class Bonprix extends AbstractWebsite {

	private Document dom;
	
	@Override
	protected void setHtml(String html) {
		super.setHtml(html);
		dom = Jsoup.parse(html);
	}

	@Override
	protected void beforeRequest(WebRequest req) {
		//TODO: zamanasimi ihtimaline karsin bir zaman sonra bu bir daha test edilmeli. 2021-03-14
		//normalde ilk once anasayfsnin yuklenmesi ile SID cookie yukleniyor. SID isinde sorun olmasi durumunda, once anasayfa yuklettirilecek!
		req.setAdditionalHeader(HttpHeader.COOKIE, "SID=%7B42ED5934%2DB8C8%2D4B21%2DBCFF%2D4FE33209A7F9%7D");
	}

  @Override
  public boolean isAvailable() {
    Element val = dom.selectFirst("meta[property='og:availability']");
    if (val == null) val = dom.selectFirst("meta[content='https://schema.org/InStock']");

    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return val.attr("content").toLowerCase().contains("instock");
    }

    val = dom.selectFirst("div.product-availability-box_wrapper div");
    if (val == null) dom.selectFirst("div#product-detail-availibility-container noscript");

    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text().contains("erfügbar");
    }

    return false;
  }

  @Override
  public String getSku() {
    Element val = dom.selectFirst("meta[itemprop='sku']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return val.attr("content");
    }
    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getName() {
    Element val = dom.selectFirst("h1.product-name span[itemprop='name']");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }

    val = dom.selectFirst("meta[property='og:title']");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return val.attr("content");
    }

    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public BigDecimal getPrice() {
    Element val = dom.selectFirst("span.price");
    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return new BigDecimal(cleanDigits(val.attr("content")));
    }

    val = dom.selectFirst("span[itemprop='price']");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return new BigDecimal(cleanDigits(val.text()));
    }

    val = dom.selectFirst("span.clearfix.price");
    if (val == null) val = dom.selectFirst("meta[property='og:price:amount']");

    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return new BigDecimal(cleanDigits(val.attr("content")));
    }

    return BigDecimal.ZERO;
  }

  @Override
  public String getBrand() {
    Element val = dom.selectFirst("meta[itemprop='brand']");
    if (val == null) val = dom.selectFirst("meta[property='og:brand']");

    if (val != null && StringUtils.isNotBlank(val.attr("content"))) {
      return val.attr("content");
    }

    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public String getSeller() {
    return "Bonprix";
  }

  @Override
  public String getShipment() {
    Element val = dom.selectFirst("div[data-controller='infoicon'] div.info-text");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text().replaceAll(" ada12_info-icon-bigsize-additional-text", "");
    }

    val = dom.selectFirst("div.product-availability-box_wrapper div");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }

    val = dom.getElementById("aiDelChargeSame");
    if (val != null && StringUtils.isNotBlank(val.text())) {
      return val.text();
    }

    return Consts.Words.NOT_AVAILABLE;
  }

  @Override
  public List<LinkSpec> getSpecList() {
    List<LinkSpec> specList = null;

    Elements specKeys = dom.select("div.product-attributes strong");
    if (specKeys != null && specKeys.size() > 0) {
      specList = new ArrayList<>();
      Elements specValues = dom.select("div.product-attributes span");
      for (int i = 0; i < specKeys.size(); i++) {
        Element key = specKeys.get(i);
        Element val = null;
        if (i < specValues.size() - 1) {
          val = specValues.get(i);
        }
        specList.add(new LinkSpec(key.text().replaceAll(":", ""), (val != null ? val.text() : "")));
      }
      return specList;
    }

    specKeys = dom.select("div.productFeaturesContainer span.productFeatureName");
    if (specKeys == null)
      specKeys = dom.select("div.product-attributes strong");

    if (specKeys != null && specKeys.size() > 0) {
      specList = new ArrayList<>();
      for (Element key : specKeys) {
        specList.add(new LinkSpec(key.text().replaceAll(":", ""), ""));
      }
    }

    Elements specValues = dom.select("div.productFeaturesContainer span.productFeatureValue");

    if (specValues != null && specValues.size() > 0) {
      boolean isEmpty = false;
      if (specList == null) {
        isEmpty = true;
        specList = new ArrayList<>();
      }
      for (int i = 0; i < specValues.size(); i++) {
        Element value = specValues.get(i);
        if (isEmpty) {
          specList.add(new LinkSpec("", value.text()));
        } else {
          specList.get(i).setValue(value.text());
        }
      }

      return specList;
    }

    specValues = dom.select("div.productDescription");

    if (specValues != null) {
      specList = new ArrayList<>();
      String desc = specValues.text();
      String[] descChunks = desc.split("\\.");
      if (descChunks.length > 0) {
        for (String dsc : descChunks) {
          specList.add(new LinkSpec("", dsc));
        }
      }
    }

    return specList;
  }

}
