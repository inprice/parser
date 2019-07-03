package io.inprice.scrapper.worker.websites.it;

import com.mashape.unirest.http.HttpResponse;
import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.helpers.Constants;
import io.inprice.scrapper.worker.helpers.HttpClient;
import io.inprice.scrapper.worker.websites.AbstractWebsite;
import org.json.JSONObject;
import org.jsoup.nodes.Element;

import java.math.BigDecimal;
import java.util.List;

/**
 * Parser for vidaXL Italy
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class VidaXL extends AbstractWebsite {

    private JSONObject current;

    public VidaXL(Link link) {
        super(link);
    }

    public String getAuctionId() {
        Element auctionId = doc.getElementById("auctionId");
        if (auctionId != null) {
            return auctionId.val();
        }
        return null;
    }

    @Override
    protected JSONObject getJsonData() {
        String auctionId = getAuctionId();
        if (auctionId != null) {
            HttpResponse<String> response = HttpClient.get("https://www.vidaxl.it/platform/index.php?m=auction&a=getAuctionsList&id=" + auctionId);
            if (response.getStatus() == 200 && ! response.getBody().isEmpty()) {
                JSONObject auction = new JSONObject(response.getBody());
                if (auction.has("current")) {
                    current = auction.getJSONObject("current");
                }
            }
        }

        return null;
    }

    @Override
    public boolean isAvailable() {
        Element inStock = doc.selectFirst("div#not-available div.false");
        return  (inStock != null);
    }

    @Override
    public String getSku() {
        Element code = doc.selectFirst("meta[itemprop='sku']");
        if (code != null) {
            return code.attr("content").trim();
        }

        code = doc.selectFirst("input[name='hidden_sku']");
        if (code != null) {
            return code.attr("value").trim();
        }

        return Constants.NOT_AVAILABLE;
    }

    @Override
    public String getName() {
        Element title = doc.selectFirst("h1[itemprop='name']");
        if (title != null) {
            return title.text().trim();
        }

        title = doc.selectFirst("meta[property='og:title']");
        if (title != null) {
            return title.attr("content");
        }

        return Constants.NOT_AVAILABLE;
    }

    @Override
    public BigDecimal getPrice() {
        if (current != null && current.has("price_num")) {
            return new BigDecimal(current.getString("price_num"));
        }

        Element price = doc.selectFirst("meta[itemprop='price']");
        if (price != null) {
            return new BigDecimal(price.attr("content").trim());
        }
        return BigDecimal.ZERO;
    }

    @Override
    public String getSeller() {
        Element seller = doc.selectFirst("meta[itemprop='seller']");
        if (seller != null) {
            return seller.attr("content");
        }
        return "VidaXL";
    }

    @Override
    public String getShipment() {
        StringBuilder sb = new StringBuilder();

        Element shipment = doc.selectFirst("div.delivery-name");
        if (shipment == null) shipment = doc.selectFirst("div.delivery-info");

        if (shipment != null) {
            sb.append(shipment.text());
            sb.append(". ");
        }

        shipment = doc.selectFirst("div.shipping-from");
        if (shipment != null) {
            sb.append(shipment.text());
            sb.append(". ");
        }

        shipment = doc.selectFirst("div.delivery-seller");
        if (shipment != null) {
            sb.append(shipment.text());
        }

        if (sb.length() == 0) sb.append("NA");

        return sb.toString().replaceAll(" Disponibile Non disponibile", "").trim();
    }

    @Override
    public String getBrand() {
        final String indicator = "\"brand\":\"";

        int start = doc.html().indexOf(indicator) + indicator.length();
        int end   = doc.html().indexOf(",\"", start);

        if (start > indicator.length() && end > start) {
            return doc.html().substring(start, end);
        }

        return "VidaXL";
    }

    @Override
    public List<LinkSpec> getSpecList() {
        return getValueOnlySpecList(doc.select("ul.specs li"));
    }
}
