package io.inprice.scrapper.worker.websites.tr;

import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.helpers.Constants;
import io.inprice.scrapper.worker.websites.AbstractWebsite;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Parser for Trendyol Turkiye
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class Trendyol extends AbstractWebsite {

    public Trendyol(Link link) {
        super(link);
    }

    @Override
    public boolean isAvailable() {
        Element soldoutButton = doc.selectFirst("button.add-to-bs.so");
        return (soldoutButton == null);
    }

    @Override
    public String getSku() {
        Element canonical = doc.selectFirst("link[rel='canonical']");
        if (canonical != null) {
            String[] linkChunks = canonical.attr("href").split("-");
            if (linkChunks.length > 0) {
                return linkChunks[linkChunks.length - 1];
            }
        }
        return Constants.NOT_AVAILABLE;
    }

    @Override
    public String getName() {
        Element name = doc.selectFirst("meta[name='twitter:title']");
        if (name != null) {
            return name.attr("content");
        }
        return Constants.NOT_AVAILABLE;
    }

    @Override
    public BigDecimal getPrice() {
        Element price = doc.selectFirst("meta[name='twitter:data1']");
        if (price != null) {
            return new BigDecimal(cleanDigits(price.attr("content")));
        }
        return BigDecimal.ZERO;
    }

    @Override
    public String getSeller() {
        Element seller = doc.selectFirst("span.pr-in-dt-spn");
        if (seller != null) {
            return seller.text();
        }

        seller = doc.selectFirst("meta[name='twitter:description']");
        if (seller != null) {
            String[] sellerChunks = seller.attr("content").split(":");
            if (sellerChunks.length > 0) {
                return sellerChunks[sellerChunks.length-1];
            }
        }

        return "Trendyol";
    }

    @Override
    public String getShipment() {
        Element shipment = doc.selectFirst("div.stamp.crg div");
        if (shipment != null) {
            return "Kargo Bedava";
        }

        shipment = doc.selectFirst("span.pr-in-dt-spn");
        if (shipment != null) {
            return shipment.text().trim() + " tarafından gönderilecektir.";
        }
        return Constants.NOT_AVAILABLE;
    }

    @Override
    public String getBrand() {
        Element brand = doc.selectFirst("div.pr-in-cn div.pr-in-br a");
        if (brand != null) {
            return brand.text();
        }

        return getSeller();
    }

    @Override
    public List<LinkSpec> getSpecList() {
        List<LinkSpec> specList = null;

        Elements specs = doc.select("div.pr-in-dt-cn ul span li");
        if (specs != null && specs.size() > 0) {
            specList = new ArrayList<>();
            for (Element spec : specs) {
                String[] specChunks = spec.text().split("\\.");
                for (String sp: specChunks) {
                    specList.add(new LinkSpec("", sp));
                }
            }
        }
        return specList;
    }

}
