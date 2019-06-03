package io.inprice.scrapper.worker.websites.fr;

import com.mashape.unirest.http.HttpResponse;
import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.helpers.HttpClient;
import io.inprice.scrapper.worker.websites.AbstractWebsite;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.nodes.Element;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Parser for Fnac France
 *
 * Contains standard data, all is extracted from html body and via json data in getJsonData()
 *
 * @author mdpinar
 */
public class Fnac extends AbstractWebsite {

    private String sku;

    public Fnac(Link link) {
        super(link);
    }

    @Override
    protected boolean willHtmlBePulled() {
        return false;
    }

    private void findProductId() {
        final String[] urlChunks = getUrl().split("\\|");
        if (urlChunks.length > 0) {
            for (String u: urlChunks) {
                if (u.matches("\\d+") && u.length() > 5) {
                    sku = u;
                    break;
                }
            }
        }
    }

    @Override
    public String getAlternativeUrl() {
        findProductId();
        if (sku == null) return null;

        return String.format("https://www.fnac.com/Nav/API/Article/GetStrate" +
                "?prid=%s" +
                "&catalogRef=1" +
                "&strateType=DoNotMiss", sku);
    }

    @Override
    public JSONObject getJsonData() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept-Language", "en-US,en;q=0.5");

        HttpResponse<String> response = HttpClient.get(getAlternativeUrl(), headers);

        if (response != null && response.getStatus() < 400) {
            JSONObject data = new JSONObject(response.getBody());
            if (data.has("ArticleThumbnailList")) {
                JSONArray list = data.getJSONArray("ArticleThumbnailList");
                for (int i = 0; i < list.length(); i++) {
                    JSONObject ref = list.getJSONObject(i);
                    if (ref.has("ArticleReference")) {
                        JSONObject prod = ref.getJSONObject("ArticleReference");
                        if (sku.equals(""+prod.getInt("PRID"))) {
                            return ref; //be aware ---> not prod but ref!!!
                        }
                    }

                }
            }
        }
        return super.getJsonData();
    }

    @Override
    public boolean isAvailable() {
        if (json != null && json.has("Availability")) {
            JSONObject availability = json.getJSONObject("Availability");
            if (availability.has("IsAvailable")) {
                return availability.getBoolean("IsAvailable");
            }
        }
        return false;
    }

    @Override
    public String getSku() {
        return sku;
    }

    @Override
    public String getName() {
        if (json != null && json.has("Title")) {
            JSONObject title = json.getJSONObject("Title");
            return title.getString("Title");
        }
        return "NA";
    }

    @Override
    public BigDecimal getPrice() {
        if (json != null && json.has("Offer")) {
            JSONObject offer = json.getJSONObject("Offer");
            return offer.getBigDecimal("Price");
        }
        return BigDecimal.ZERO;
    }

    @Override
    public String getSeller() {
        return "Fnac";
    }

    @Override
    public String getShipment() {
        StringBuilder sb = new StringBuilder();

        if (json != null && json.has("ShippingInfos")) {
            JSONObject ship = json.getJSONObject("ShippingInfos");
            if (ship.has("Title")) sb.append(ship.getString("Title"));
            if (ship.has("Price")) {
                sb.append(": ");
                sb.append(ship.getBigDecimal("Price"));
            }
        } else {
            sb.append("NA");
        }

        return sb.toString();
    }

    @Override
    public String getBrand() {
        if (json != null && json.has("SubTitle")) {
            JSONObject title = json.getJSONObject("SubTitle");
            return title.getString("FamilyName");
        }
        return "NA";
    }

    @Override
    public List<LinkSpec> getSpecList() {
        List<LinkSpec> specList = null;

        if (json != null && json.has("Properties")) {
            JSONArray props = json.getJSONArray("Properties");
            if (props.length() > 0) {
                specList = new ArrayList<>(props.length());
                for (int i = 0; i < props.length(); i++) {
                    JSONObject prop = props.getJSONObject(i);
                    specList.add(new LinkSpec(prop.getString("Key"), prop.getString("Value")));
                }
            }
        }

        return specList;
    }
}
