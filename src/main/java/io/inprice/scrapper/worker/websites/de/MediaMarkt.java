package io.inprice.scrapper.worker.websites.de;

import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.websites.AbstractWebsite;
import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Parser for MediaMarkt Deutschland
 *
 * The parsing steps:
 *
 *  - the html body of link's url contains data (in json format) we need
 *  - in getJsonData(), we get that json data by using substring() method of String class
 *  - this data is named as product which is hold on a class-level variable
 *  - each data (except for availability and specList) can be gathered using product variable
 *
 * @author mdpinar
 */
public class MediaMarkt extends AbstractWebsite {

    private JSONObject article;

    public MediaMarkt(Link link) {
        super(link);
    }

    @Override
    protected JSONObject getJsonData() {
        final String indicator = "__PRELOADED_STATE__ = ";
        final String html = doc.html();

        int start = html.indexOf(indicator) + indicator.length();
        int end   = html.indexOf("};", start) + 1;

        if (start > indicator.length() && end > start) {
            JSONObject data = new JSONObject(html.substring(start, end));
            if (data.has("reduxInitialStore")) {
                JSONObject store = data.getJSONObject("reduxInitialStore");
                if (store.has("select")) {
                    JSONObject prod = store.getJSONObject("select");

                    if (prod.has("article")) {
                        article = prod.getJSONObject("article");
                    }

                    return prod;
                }
            }
        }
        return null;
    }

    @Override
    public boolean isAvailable() {
        if (json != null && json.has("availability")) {
            JSONObject availability = json.getJSONObject("availability");
            if (availability.has("online")) {
                JSONObject online = availability.getJSONObject("online");
                if (online.has("quantity") && ! online.isNull("quantity")) {
                    return online.getInt("quantity") > 0;
                }
            }
        }
        return false;
    }

    @Override
    public String getSku() {
        if (article != null && article.has("articleId")) {
            return article.getString("articleId");
        }
        return "NA";
    }

    @Override
    public String getName() {
        if (article != null && article.has("title")) {
            return article.getString("title");
        }
        return "NA";
    }

    @Override
    public BigDecimal getPrice() {
        if (json != null && json.has("price")) {
            JSONObject price = json.getJSONObject("price");
            if (price.has("price")) {
                return price.getBigDecimal("price");
            }
        }
        return BigDecimal.ZERO;
    }

    @Override
    public String getSeller() {
        return "Media Markt";
    }

    @Override
    public String getShipment() {
        if (json != null && json.has("availability")) {
            JSONObject availability = json.getJSONObject("availability");
            if (availability.has("online")) {
                JSONObject online = availability.getJSONObject("online");
                if (online.has("shipping")) {
                    JSONObject shipping = online.getJSONObject("shipping");
                    if (shipping.has("shippingCosts")) {
                        BigDecimal cost = shipping.getBigDecimal("shippingCosts");
                        if (cost.compareTo(BigDecimal.ZERO) == 0)
                            return "Kostenloser Versand";
                        else
                            return "Versandkosten " + shipping.getBigDecimal("shippingCosts");
                    }
                }
            }
        }

        return "NA";
    }

    @Override
    public String getBrand() {
        if (article != null && article.has("manufacturer")) {
            return article.getString("manufacturer");
        }
        return "NA";
    }

    @Override
    public List<LinkSpec> getSpecList() {
        List<LinkSpec> specList = null;

        if (article != null && article.has("mainFeatures")) {
            specList = new ArrayList<>();
            JSONArray features = article.getJSONArray("mainFeatures");
            if (features.length() > 0) {
                for (int i = 0; i < features.length(); i++) {
                    JSONObject pair = features.getJSONObject(i);
                    specList.add(new LinkSpec(pair.getString("name"), pair.getString("value")));
                }
            }
        }

        return specList;
    }
}
