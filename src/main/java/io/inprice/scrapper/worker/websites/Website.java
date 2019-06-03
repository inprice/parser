package io.inprice.scrapper.worker.websites;

import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.common.models.LinkSpec;

import java.math.BigDecimal;
import java.util.List;

/**
 * A common interface for websites. So, all of them must implement this interface.
 *
 * @author mdpinar
 */
public interface Website {

    //checks link status and sets data. is implemented in AbstractWebsite
    void check();

    //indicates the availability of the page
    boolean isAvailable();

    String getBrand();

    String getSeller();

    //can be used as SKU, CODE, ASIN, PRODUCT-ID, ITEM-ID...
    String getSku();

    String getName();

    BigDecimal getPrice();

    String getShipment();

    List<LinkSpec> getSpecList();

    //main url
    String getUrl();

    //alternative url for spa
    String getAlternativeUrl();

    //for test purposes
    Link test(String fileName);

}
