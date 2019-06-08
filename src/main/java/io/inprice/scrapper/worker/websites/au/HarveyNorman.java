package io.inprice.scrapper.worker.websites.au;

import com.mashape.unirest.http.HttpResponse;
import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.helpers.Global;
import io.inprice.scrapper.worker.helpers.HttpClient;
import io.inprice.scrapper.worker.websites.AbstractWebsite;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * DOESN'T WORK!!! instant session ids are useless!!!
 *
 * Please note that:
 *    This site is guarded by incapsula that is a scrapper blocking service and checks every request for a set of cookies starting with visid_incap_ and incap_ses_
 *    For this reason, we have to add those cookies in header (please refer to getHeader() method)
 *
 * If you need to refresh those cookie values, here is the todo list:
 *    1) open your browser
 *    2) clear cache (especially cookies)
 *    3) open developer tools section
 *    4) paste the url into the address bar of the browser
 *    5) click on to Network Tab in developer tools
 *    4) find GET request of the url
 *    5) "Cookie" value in "Request Headers" is what we are looking for
 *    6) copy the value of incap_ses_XXXX_XXXXX (I mean Cookie value)
 *    7) use it as a value in getHeader()
 *
 * Parser for HarveyNorman Australia
 *
 * Contains standard data, all is extracted by css selectors
 *
 * @author mdpinar
 */
public class HarveyNorman extends AbstractWebsite {

    /*
     * instant session ids
     */
    private final String[] INCAP_SESSIONS = {
        "incap_ses_1193_39856=PIrqSV8PnwKV47puY2OOEBKQ+lwAAAAA2lfOySTdgWDap6cEnyuNJA==;",
        "incap_ses_1193_39856=sOb2DXsjIB3ZpbpuY2OOEIeO+lwAAAAAypE0/nLxWa6fQEaZ+clo+Q==;",
        "incap_ses_1193_39856=rfWmUnnbtik9ZbpuY2OOEKuM+lwAAAAApd1vq340qzurxipcJaVh/w==;",
        "incap_ses_1193_39856=S5UEXsLLfipGIbtuY2OOEIqR+lwAAAAA+aR8Qh3cUWfvno/XWefW4g==;",
        "incap_ses_1193_39856=4Q7xHupe5GhQOLtuY2OOEB2S+lwAAAAA3fOV6RP2+7rSuwjEJaGA0A==;",
        "incap_ses_1193_39856=Sn40VqAjElWZXr1uY2OOEHWf+lwAAAAAyCyAkmarfaZp06hPJswJMA==;"
    };

    /*
     * holds price info set in getJsonData()
     */
    private JSONObject offer;

    public HarveyNorman(Link link) {
        super(link);
    }

    @Override
    protected Map<String, String> getHeaders() {
        Map<String, String> headers = Global.standardHeaders;
        headers.put("Cookie", INCAP_SESSIONS[new Random().nextInt(INCAP_SESSIONS.length)]);
        return headers;
    }

    @Override
    protected int openDocument() {
        HttpResponse<String> response = HttpClient.get(getUrl(), getHeaders());
        System.out.println(response.getBody());
        if (response.getStatus() == 200) {
            doc = Jsoup.parse(response.getBody());
        }
        return response.getStatus();
    }

    @Override
    public JSONObject getJsonData() {
        Elements dataEL = doc.select("script[type='application/ld+json']");
        if (dataEL != null && dataEL.size() > 0) {
            for (int i = 0; i < dataEL.size(); i++) {
                if (dataEL.get(i).dataNodes().get(0).getWholeData().indexOf("aggregateRating") > 0) {
                    JSONObject data = new JSONObject(dataEL.get(i).dataNodes().get(0).getWholeData().replace("\r\n"," ").trim());
                    if (data.has("offers")) {
                        JSONArray offers = data.getJSONArray("offers");
                        if (! offers.isEmpty() && offers.length() > 0) {
                            offer = offers.getJSONObject(0);
                        }
                    }
                    return data;
                }
            }
        }
        return super.getJsonData();
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public String getSku() {
        if (json != null && json.has("sku")) {
            return json.getString("sku");
        }
        return "NA";
    }

    @Override
    public String getName() {
        if (json != null && json.has("name")) {
            return json.getString("name");
        }
        return "NA";
    }

    @Override
    public BigDecimal getPrice() {
        if (offer != null && offer.has("price")) {
            return offer.getBigDecimal("price");
        }
        return BigDecimal.ZERO;
    }

    @Override
    public String getSeller() {
        if (offer != null && offer.has("seller")) {
            JSONObject seller = offer.getJSONObject("seller");
            if (seller.has("name")) {
                return seller.getString("name");
            }
        }
        return "Harvey Norman";
    }

    @Override
    public String getBrand() {
        if (json != null && json.has("brand")) {
            return json.getJSONObject("brand").getString("name");
        }
        return "NA";
    }

    @Override
    public String getShipment() {
        Element shipment = doc.selectFirst("span.margin-vertical-xsmall.font-weight-light");
        if (shipment != null) {
            return shipment.text().trim();
        }
        return "NA";
    }

    @Override
    public List<LinkSpec> getSpecList() {
        return getValueOnlySpecList(doc.select("div.product-long-description li"));
    }

}
