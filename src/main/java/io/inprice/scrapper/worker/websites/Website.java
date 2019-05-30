package io.inprice.scrapper.worker.websites;

import io.inprice.scrapper.common.models.LinkSpec;

import java.math.BigDecimal;
import java.util.List;

public interface Website {

    void check();

    boolean isAvailable();

    String getSku();

    String getName();

    BigDecimal getPrice();

    String getSeller();

    String getShipment();

    String getBrand();

    List<LinkSpec> getSpecList();

    String getMainUrl();

    String getSubUrl();

    boolean willHtmlBeDownloaded();

}
