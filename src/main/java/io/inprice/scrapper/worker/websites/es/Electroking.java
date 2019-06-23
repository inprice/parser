package io.inprice.scrapper.worker.websites.es;

import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.websites.AbstractWebsite;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.math.BigDecimal;
import java.util.List;

/**
 * Parser for Electroking Spain
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class Electroking extends AbstractWebsite {

    public Electroking(Link link) {
        super(link);
    }

    @Override
    public boolean isAvailable() {
        Element stock = doc.selectFirst("span[data-stock]");
        if (stock != null) {
            try {
                int amount = new Integer(stock.attr("data-stock"));
                return (amount > 0);
            } catch (Exception e) {
                //
            }
        }
        return false;
    }

    @Override
    public String getSku() {
        Element sku = doc.selectFirst("span[itemprop='sku']");
        if (sku != null) {
            return sku.text().trim();
        }
        return "NA";
    }

    @Override
    public String getName() {
        Element name = doc.selectFirst("meta[property='og:title']");
        if (name != null) {
            return name.attr("content");
        }

        name = doc.selectFirst("p.product_name strong");
        if (name != null) {
            return name.text().trim();
        }
        return "NA";
    }

    @Override
    public BigDecimal getPrice() {
        Element price = doc.selectFirst("meta[property='product:price:amount']");
        if (price != null) {
            return new BigDecimal(cleanPrice(price.attr("content")));
        }
        return BigDecimal.ZERO;
    }

    @Override
    public String getSeller() {
        return "Electroking";
    }

    @Override
    public String getShipment() {
        return "Envío por Agencia de Transporte. Ver detalles";
    }

    @Override
    public String getBrand() {
        Element brand = doc.selectFirst("div.product-manufacturer a");
        if (brand != null) {
            String brnd = brand.text().trim();
            if (! brnd.isEmpty()) return brnd;
        }

        brand = doc.selectFirst("img.manufacturer-logo");
        if (brand != null) {
            return brand.attr("alt").trim();
        }

        return getSeller();
    }

    @Override
    public List<LinkSpec> getSpecList() {
        return getValueOnlySpecList(doc.select("div.product-description li"));
    }
}
