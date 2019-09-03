package io.inprice.scrapper.worker.websites.xx;

import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.helpers.Consts;
import io.inprice.scrapper.worker.websites.AbstractWebsite;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Parser for Zalando Global
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class Zalando extends AbstractWebsite {

    public Zalando(Link link) {
        super(link);
    }

    @Override
    public boolean isAvailable() {
        boolean isAvailable = doc.html().indexOf("\"available\":true") > 0;
        if (isAvailable) return true;

        Element addToCartButton = doc.getElementById("z-pdp-topSection-addToCartButton");
        return (addToCartButton != null);
    }

    @Override
    public String getSku() {
        Element sku = doc.selectFirst("meta[property='og:url']");
        if (sku != null) {
            String url = sku.attr("content");
            if (! url.isEmpty()) {
                String[] urlChunks = url.split("-");
                if (urlChunks.length > 1) {
                    String pure = urlChunks[urlChunks.length-2] + "-" + urlChunks[urlChunks.length-1].replaceAll(".html", "");
                    return pure.toUpperCase();
                }
            }
        }
        return Consts.Words.NOT_AVAILABLE;
    }

    @Override
    public String getName() {
        Element name = doc.selectFirst("h1[title]");
        if (name != null) {
            return name.attr("title");
        }

        name = doc.selectFirst("meta[name='twitter:title']");
        if (name != null) {
            return name.attr("content");
        }
        return Consts.Words.NOT_AVAILABLE;
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
        return "Zalando";
    }

    @Override
    public String getShipment() {
        final String html = doc.html();

        StringBuilder delivery = new StringBuilder();

        String standard1 = findAPart(html, "\"zalando.prodpres.delivery.available.title\":\"", "\",\"");
        String standard2 = findAPart(html, "\"zalando.prodpres.delivery.standard.free\":\"", "\",\"");
        String standard3 = findAPart(html, "\"zalando.prodpres.delivery.available.time\":\"", "\",\"");

        if (standard1 != null) {
            delivery.append(standard1);
            delivery.append(" ");
        }
        if (standard2 != null) {
            delivery.append(standard2);
            delivery.append(" ");
        }
        if (standard3 != null) {
            delivery.append(standard3);
            delivery.append(" ");
        }

        String express1 = findAPart(html, "\"zalando.prodpres.delivery.express.title\":\"", "\",\"");
        String express2 = findAPart(html, "\"zalando.prodpres.delivery.express.cost\":\"", "\",\"");
        String express3 = findAPart(html, "\"zalando.prodpres.delivery.express.time\":\"", "\",\"");

        if (express1 != null) {
            delivery.append(express1);
            delivery.append(" ");
        }
        if (express2 != null) {
            delivery.append(express2);
            delivery.append(" ");
        }
        if (express3 != null) delivery.append(express3);

        return delivery.toString(); //"Standard shipment";
    }

    @Override
    public String getBrand() {
        Element brand = doc.selectFirst("a[title] h2");
        if (brand != null) {
            return brand.text();
        }
        return Consts.Words.NOT_AVAILABLE;
    }

    @Override
    public List<LinkSpec> getSpecList() {
        List<LinkSpec> specList = null;
        Elements specs = doc.select("div#z-pdp-detailsSection p.h-text.h-color-black.body.h-m-bottom-xs span");
        if (specs != null && specs.size() > 0) {
            specList = new ArrayList<>();
            int i = 0;
            while (i < specs.size()) {
                String key = specs.get(i++).text().replaceAll(":", "");
                String value = "";
                if (i < specs.size())  value = specs.get(i++).text();
                if (! key.isEmpty() && ! value.isEmpty()) specList.add(new LinkSpec(key, value));
            }
        }
        return specList;
    }

}
