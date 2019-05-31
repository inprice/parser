package io.inprice.scrapper.worker.websites.de;

import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.websites.AbstractWebsite;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Parser for MediaMarkt Deutschland
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class MediaMarkt extends AbstractWebsite {

    public MediaMarkt(Link link) {
        super(link);
    }

    @Override
    public boolean isAvailable() {
        Element inStock = doc.selectFirst("div.mms-availability__description--headline p span");
        if (inStock != null) {
            return inStock.text().trim().contains("Online auf Lager");
        }
        return false;
    }

    @Override
    public String getSku() {
        String[] urlChunks = getUrl().split("-");
        String raw = urlChunks[urlChunks.length-1];
        return raw.substring(0, raw.indexOf('.'));
    }

    @Override
    public String getName() {
        Element name = doc.selectFirst("h1[itemprop='name']");
        if (name != null) {
            return name.attr("content").trim();
        }
        return "NA";
    }

    @Override
    public BigDecimal getPrice() {
        Element name = doc.selectFirst("meta[itemProp='price']");
        if (name != null) {
            return new BigDecimal(name.attr("content").trim());
        }
        return BigDecimal.ZERO;
    }

    @Override
    public String getSeller() {
        return "Media Markt";
    }

    @Override
    public String getShipment() {
        Element shipment = doc.selectFirst("div.mms-availability__description--price");
        if (shipment != null) {
            return shipment.text();
        }
        return "NA";
    }

    @Override
    public String getBrand() {
        Element brand = doc.selectFirst("img[title]");
        if (brand != null) {
            return brand.attr("title").trim();
        }
        return "NA";
    }

    @Override
    public List<LinkSpec> getSpecList() {
        List<LinkSpec> specList = null;

        Elements specKeys = doc.select("tr.mms-feature-list__row th");
        if (specKeys != null && specKeys.size() > 0) {
            specList = new ArrayList<>();
            for (Element key : specKeys) {
                specList.add(new LinkSpec(key.text().replaceAll(":","").trim(), ""));
            }
        }

        Elements specValues = doc.select("tr.mms-feature-list__row td");
        if (specValues != null && specValues.size() > 0) {
            boolean isEmpty = false;
            if (specList == null) {
                isEmpty = true;
                specList = new ArrayList<>();
            }
            for (int i = 0; i < specValues.size(); i++) {
                Element value = specValues.get(i);
                if (isEmpty) {
                    specList.add(new LinkSpec("", value.text().trim()));
                } else {
                    specList.get(i).setValue(value.text().trim());
                }
            }
        }

        return specList;
    }
}
