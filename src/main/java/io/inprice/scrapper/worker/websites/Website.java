package io.inprice.scrapper.worker.websites;

import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.common.models.LinkSpec;

import java.math.BigDecimal;
import java.util.List;

public interface Website {

    //void test(String fileName, Link link);

    void check(Link link);

    boolean isAvailable();

    String getSku();

    String getName();

    BigDecimal getPrice();

    String getSeller();

    String getShipment();

    String getBrand();

    List<LinkSpec> getSpecList();

}
