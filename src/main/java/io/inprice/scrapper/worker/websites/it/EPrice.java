package io.inprice.scrapper.worker.websites.it;

import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.websites.AbstractWebsite;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Parser for EPrice Italy
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class EPrice extends AbstractWebsite {

    public EPrice(Link link) {
        super(link);
    }

    @Override
    public boolean isAvailable() {
        Element inStock = doc.selectFirst("meta[itemprop='availability']");
        if (inStock != null) {
            return inStock.attr("content").contains("InStock");
        }
        return false;
    }

    @Override
    public String getSku() {
        Element sku = doc.selectFirst("meta[itemprop='sku']");
        if (sku != null) {
            return sku.attr("content").trim();
        }
        return "NA";
    }

    @Override
    public String getName() {
        Element name = doc.selectFirst("h1[itemprop='name']");
        if (name != null) {
            return name.text();
        }
        return "NA";
    }

    @Override
    public BigDecimal getPrice() {
        Element price = doc.selectFirst("span[itemprop='price']");
        if (price != null) {
            return new BigDecimal(cleanPrice(price.text()));
        }
        return BigDecimal.ZERO;
    }

    @Override
    public String getSeller() {
        Element shipment = doc.selectFirst("p.infoSeller a strong");
        if (shipment != null) {
            return shipment.text().trim();
        }
        return "ePrice";
    }

    @Override
    public String getShipment() {
        return "Venduto e spedito da " + getSeller();
    }

    @Override
    public String getBrand() {
        Element brand = doc.selectFirst("meta[itemprop='brand']");
        if (brand != null) {
            return brand.attr("content").trim();
        }
        return "NA";
    }

    @Override
    public List<LinkSpec> getSpecList() {
        List<LinkSpec> specList = getKeyValueSpecList(doc.select("#anchorCar li"), "span", "a");
        if (specList == null) {
            Elements specs = doc.select("#anchorTech li");
            if (specs != null && specs.size() > 0) {
                specList = new ArrayList<>();
                for (Element spec : specs) {
                    Elements pair = spec.select("span");
                    if (pair.size() == 1) {
                        specList.add(new LinkSpec("", pair.get(0).text()));
                    } else if (pair.size() > 1) {
                        specList.add(new LinkSpec(pair.get(0).text(), pair.get(1).text()));
                    }
                }
            }
        }

        if (specList == null) {
            Elements specs = doc.select("#anchorDesc p");
            if (specs != null && specs.size() > 0) {
                specList = new ArrayList<>();
                for (Element spec : specs) {
                    String[] specChunks = spec.text().split("\\.");
                    for (String sp: specChunks) {
                        specList.add(new LinkSpec("", sp.trim()));
                    }
                }
            }
        }

        return specList;
    }
}
