package io.inprice.scrapper.worker.websites.es;

import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.websites.AbstractWebsite;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.List;

public class Pixmania extends AbstractWebsite {

    @Override
    public String getJSBasedPageCaller() {
        return "Pixmania";
    }

    @Override
    public JSONObject getJsonData() {
        System.out.println(doc.html());
        return super.getJsonData();
    }

    @Override
    public boolean isAvailable() {
        return super.isAvailable();
    }

    @Override
    public String getSku() {
        return "NA";
    }

    @Override
    public String getName() {
        return "NA";
    }

    @Override
    public BigDecimal getPrice() {
        return BigDecimal.ZERO;
    }

    @Override
    public String getSeller() {
        return "Pixmaina";
    }

    @Override
    public String getShipment() {
        return "NA";
    }

    @Override
    public String getBrand() {
        return "NA";
    }

    @Override
    public List<LinkSpec> getSpecList() {
        return null;
    }

}
