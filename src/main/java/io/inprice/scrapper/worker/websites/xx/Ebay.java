package io.inprice.scrapper.worker.websites.xx;

import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.websites.AbstractWebsite;
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

    public Ebay(Link link) {
        super(link);
    }

    @Override
    public boolean isAvailable() {
        Element stock = doc.getElementById("vi-quantity__select-box");
        if (stock != null) return true;

        stock = doc.selectFirst("#qtySubTxt span");
        if (stock != null) return true;

        stock = doc.selectFirst("a[data-action-name='BUY_IT_NOW']");
        return  (stock != null);
    }

    @Override
    public String getSku() {
        Element sku = doc.getElementById("descItemNumber");
        if (sku != null) {
            return sku.text().trim();
        }

        sku = doc.selectFirst("a[data-itemid]");
        if (sku != null) {
            return sku.attr("data-itemid").trim();
        }
        return "NA";
    }

    @Override
    public String getName() {
        Element name = doc.selectFirst("title");
        if (name == null) name = doc.getElementById("itemTitle");
        if (name == null) name = doc.selectFirst("h1.product-title");
        if (name != null) return name.text().trim();

        name = doc.selectFirst("a[data-itemid]");
        if (name != null) {
            return name.attr("etafsharetitle").trim();
        }
        return "NA";
    }

    @Override
    public BigDecimal getPrice() {
        String strPrice = null;

        Element price = doc.getElementById("prcIsum");
        if (price == null) price = doc.getElementById("prcIsum_bidPrice");

        if (price != null) {
            strPrice = price.attr("content").trim();
        } else {
            price = doc.getElementById("mm-saleDscPrc");
            if (price != null) strPrice = price.text().trim();
        }

        if (price == null) {
            price = doc.selectFirst("div.price");
            if (price != null) strPrice = price.text().trim();
        }

        if (strPrice == null)
            return BigDecimal.ZERO;
        else
            return new BigDecimal(cleanPrice(strPrice));
    }

    @Override
    public String getSeller() {
        String value = null;
        Element seller = doc.getElementById("mbgLink");

        if (seller != null) {
            String[] sellerChunks = seller.attr("aria-label").split(":");
            if (sellerChunks.length > 1) {
                value = sellerChunks[1].trim();
            }
        } else {
            seller = doc.selectFirst("div.seller-persona a");
            if (seller == null) seller = doc.selectFirst("span.mbg-nw");

            if (seller != null) {
                value = seller.text().trim();
            }
        }
        return value;
    }

    @Override
    public String getShipment() {
        String value = null;
        Element shipment = doc.selectFirst("#shSummary span");
        if (shipment == null) shipment = doc.selectFirst("span.logistics-cost");

        if (shipment != null) {
            value = shipment.text().trim();
        }

        return value;
    }

    @Override
    public String getBrand() {
        String[] titleChunks = getName().split("\\s");
        if (titleChunks.length > 1) return titleChunks[0].trim();
        return "NA";
    }

    @Override
    public List<LinkSpec> getSpecList() {
        List<LinkSpec> specList = null;
        Elements specs = doc.select("table[role='presentation']:not(#itmSellerDesc) td");
        if (specs != null && specs.size() > 0) {
            specList = new ArrayList<>();
            for (int i = 0; i < specs.size(); i++) {
                String key = specs.get(i).text().replaceAll(":", "").trim();
                String value = "";
                if (i < specs.size()-1) {
                    value = specs.get(++i).text().trim();
                }
                specList.add(new LinkSpec(key, value));
            }
        } else {
            specs = doc.select("#ProductDetails li div");
            if (specs != null && specs.size() > 0) {
                specList = new ArrayList<>();
                for (int i = 0; i < specs.size(); i++) {
                    String key = specs.get(i).text().trim();
                    String value = "";
                    if (i < specs.size() - 1) {
                        value = specs.get(++i).text().trim();
                    }
                    specList.add(new LinkSpec(key, value));
                }
            }
        }
        return specList;
    }
}
