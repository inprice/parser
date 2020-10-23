package io.inprice.parser.websites.exceptions;

import io.inprice.common.meta.LinkStatus;
import io.inprice.common.models.Link;
import io.inprice.common.models.LinkSpec;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.websites.AbstractWebsite;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.spy;

public class AbstractWebsite_Test {

  private final Link link = new Link();

  @Test
  public void should_set_NO_DATA_for_available_products_with_NA_name() {
    AbstractWebsite site = new FakeSite(true, Consts.Words.NOT_AVAILABLE, BigDecimal.ZERO, false);

    Link link = site.test(null);

    assertEquals(LinkStatus.NO_DATA, link.getStatus());
  }

  @Test
  public void should_set_NOT_DATA_for_unavailable_products_with_NULL_name() {
    AbstractWebsite site = new FakeSite(false, null, BigDecimal.ZERO, false);

    Link link = site.test(null);

    assertEquals(LinkStatus.NO_DATA, link.getStatus());
  }

  @Test
  public void should_set_SOCKET_ERROR() {
    FakeSite site = spy(new FakeSite(false, null, BigDecimal.ZERO));
    site.check(link);

    assertEquals(LinkStatus.NETWORK_ERROR, site.getLink().getStatus());
  }

  @Test
  public void should_set_NETWORK_ERROR() {
    FakeSite site = spy(new FakeSite(false, null, BigDecimal.ZERO));
    site.check(link);

    assertEquals(LinkStatus.NETWORK_ERROR, site.getLink().getStatus());
  }

  private class FakeSite extends AbstractWebsite {

    private boolean isAvailable;
    private String name;
    private BigDecimal price;

    private boolean willHtmlBePulled;

    private FakeSite(boolean isAvailable, String name, BigDecimal price) {
      this(isAvailable, name, price, true);
    }

    private FakeSite(boolean isAvailable, String name, BigDecimal price, boolean willHtmlBePulled) {
      this.isAvailable = isAvailable;
      this.name = name;
      this.price = price;
      this.willHtmlBePulled = willHtmlBePulled;
      check(link);
    }

    public Link getLink() {
      return link;
    }

    @Override
    public boolean willHtmlBePulled() {
      return willHtmlBePulled;
    }

    @Override
    public boolean isAvailable() {
      return isAvailable;
    }

    @Override
    public String getBrand() {
      return Consts.Words.NOT_AVAILABLE;
    }

    @Override
    public String getSeller() {
      return Consts.Words.NOT_AVAILABLE;
    }

    @Override
    public String getSku() {
      return Consts.Words.NOT_AVAILABLE;
    }

    @Override
    public String getName() {
      return name;
    }

    @Override
    public BigDecimal getPrice() {
      return price;
    }

    @Override
    public String getShipment() {
      return Consts.Words.NOT_AVAILABLE;
    }

    @Override
    public List<LinkSpec> getSpecList() {
      return null;
    }
  }

}
