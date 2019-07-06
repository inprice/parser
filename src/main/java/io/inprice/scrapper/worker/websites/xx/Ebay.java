package io.inprice.scrapper.worker.websites.xx;

import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.helpers.Constants;
import io.inprice.scrapper.worker.websites.AbstractWebsite;
import org.json.JSONObject;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Parser for Ebay Global
 *
 * Contains standard data. Nothing special, all is extracted by css selectors
 *
 * finding by item-id : https://www.ebay.com/itm/372661939240
 *
 * @author mdpinar
 */
public class Ebay extends AbstractWebsite {

    private String brand = "NA";
    private List<LinkSpec> specList;

    public Ebay(Link link) {
        super(link);
    }

    @Override
    protected JSONObject getJsonData() {
        //for handling brand name at first hand
        buildSpecList();
        return super.getJsonData();
    }

    @Override
    public boolean isAvailable() {
        Element stock = doc.getElementById("vi-quantity__select-box");
        if (stock != null) return true;

        stock = doc.selectFirst("#qtySubTxt span");
        if (stock != null) return true;

        stock = doc.selectFirst("a[data-action-name='BUY_IT_NOW']");
        if (stock != null) return true;

        stock = doc.selectFirst("span[itemprop='availableAtOrFrom']");
        return  (stock != null);
    }

    @Override
    public String getSku() {
        Element sku = doc.getElementById("descItemNumber");
        if (sku != null) {
            return sku.text();
        }

        sku = doc.selectFirst("a[data-itemid]");
        if (sku != null) {
            return sku.attr("data-itemid");
        }
        return Constants.NOT_AVAILABLE;
    }

    @Override
    public String getName() {
        Element name = doc.selectFirst("span#vi-lkhdr-itmTitl");
        if (name == null) name = doc.selectFirst("title");
        if (name != null) return name.text();

        name = doc.selectFirst("h1.product-title");
        if (name != null) return name.text();

        name = doc.selectFirst("a[data-itemid]");
        if (name != null) {
            return name.attr("etafsharetitle");
        }
        return Constants.NOT_AVAILABLE;
    }

    @Override
    public BigDecimal getPrice() {
        String strPrice = null;

        Element price = doc.getElementById("convbinPrice");
        if (price == null) price = doc.getElementById("convbidPrice");

        if (price != null) {
            strPrice = price.text();
        } else {
            price = doc.getElementById("prcIsum");
            if (price == null) price = doc.getElementById("prcIsum_bidPrice");

            if (price != null) {
                strPrice = price.attr("content");
            } else {
                price = doc.getElementById("mm-saleDscPrc");
                if (price != null) strPrice = price.text();
            }

            if (price == null) {
                price = doc.selectFirst("div.price");
                if (price != null) strPrice = price.text();
            }
        }

        if (strPrice == null)
            return BigDecimal.ZERO;
        else
            return new BigDecimal(cleanDigits(strPrice));
    }

    @Override
    public String getSeller() {
        String value = null;
        Element seller = doc.getElementById("mbgLink");

        if (seller != null) {
            String[] sellerChunks = seller.attr("aria-label").split(":");
            if (sellerChunks.length > 1) {
                value = sellerChunks[1];
            }
        } else {
            seller = doc.selectFirst("div.seller-persona a");
            if (seller == null) seller = doc.selectFirst("span.mbg-nw");

            if (seller != null) {
                value = seller.text();
            }
        }
        return value;
    }

    @Override
    public String getShipment() {
        String value = null;

        Element shipment = doc.getElementById("fshippingCost");
        if (shipment != null) {
            String left = shipment.text();
            String right = "";

            shipment = doc.getElementById("fShippingSvc");
            if (shipment != null) {
                right = shipment.text();
            }
            return left + " " + right;
        }

        shipment = doc.selectFirst("#shSummary span");
        if (shipment == null) shipment = doc.selectFirst("span.logistics-cost");

        if (shipment != null && shipment.text().trim().length() > 1) {
            value = shipment.text();
        } else {
            shipment = doc.getElementById("shSummary");
            if (shipment != null) {
                value = shipment.text();
            }
        }

        return value;
    }

    @Override
    public String getBrand() {
        return brand;
    }

    @Override
    public List<LinkSpec> getSpecList() {
        return specList;
    }

    private final String BRAND_WORDS = "(Brand|Marca|Marke|Marque).";

    private void buildSpecList() {
        Elements specs = doc.select("table[role='presentation']:not(#itmSellerDesc) tr");
        if (specs != null && specs.size() > 0) {
            specList = new ArrayList<>();
            for (Element row: specs) {
                Elements tds = row.select("td");
                if (tds != null && tds.size() > 0) {
                    String key = "";
                    String value = "";
                    for (int i = 0; i < tds.size(); i++) {
                        if (i % 2 == 0) {
                            key = tds.get(i).text();
                        } else {
                            value = tds.get(i).text();
                            specList.add(new LinkSpec(key, value));
                            if (key.matches(BRAND_WORDS)) {
                                brand = value;
                            }
                            key = "";
                            value = "";
                        }
                    }
                }
            }
        } else {
            specs = doc.select("#ProductDetails li div");
            if (specs != null && specs.size() > 0) {
                specList = new ArrayList<>();
                for (int i = 0; i < specs.size(); i++) {
                    String key = specs.get(i).text();
                    String value = "";
                    if (i < specs.size() - 1) {
                        value = specs.get(++i).text();
                    }
                    if (key.matches(BRAND_WORDS)) {
                        brand = value;
                    }
                    specList.add(new LinkSpec(key, value));
                }
            }
        }
    }
}
