package io.inprice.scrapper.worker.websites;

import io.inprice.scrapper.common.models.Competitor;
import io.inprice.scrapper.common.models.CompetitorSpec;
import io.inprice.scrapper.worker.helpers.HttpClient;

import java.math.BigDecimal;
import java.util.List;

/**
 * A common interface for websites. So, all of them must implement this interface.
 *
 * @author mdpinar
 */
public interface Website {

    //checks competitor status and sets data. is implemented in AbstractWebsite
    void check();

    //indicates the availability of the page
    boolean isAvailable();

    //can be used as SKU, CODE, ASIN, PRODUCT-ID, ITEM-ID...
    String getSku();

    String getName();

    BigDecimal getPrice();

    String getBrand();

    String getSeller();

    String getShipment();

    List<CompetitorSpec> getSpecList();

    //main url
    String getUrl();

    //for test purposes
    Competitor test(String fileName);

    //for test purposes
    Competitor test(String fileName, HttpClient httpClient);

}
