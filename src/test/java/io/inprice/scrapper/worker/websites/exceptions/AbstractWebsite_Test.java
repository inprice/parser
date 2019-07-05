package io.inprice.scrapper.worker.websites.exceptions;

import io.inprice.scrapper.common.meta.Status;
import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.common.models.LinkSpec;
import io.inprice.scrapper.worker.helpers.Constants;
import io.inprice.scrapper.worker.websites.AbstractWebsite;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.spy;

@RunWith(PowerMockRunner.class)
@PrepareForTest(AbstractWebsite.class)
public class AbstractWebsite_Test {

    @Test
    public void should_set_NOT_SPECIFIC_for_available_products_with_NA_name() {
        AbstractWebsite site = new FakeSite(true, Constants.NOT_AVAILABLE, BigDecimal.ZERO, false);

        Link link = site.test(null);

        assertEquals(Status.NOT_SPECIFIC, link.getStatus());
    }

    @Test
    public void should_set_NOT_SPECIFIC_for_unavailable_products_with_NULL_name() {
        AbstractWebsite site = new FakeSite(false, null, BigDecimal.ZERO, false);

        Link link = site.test(null);

        assertEquals(Status.NOT_SPECIFIC, link.getStatus());
    }

    @Test
    public void should_set_SOCKET_ERROR() throws Exception {
        FakeSite site = spy(new FakeSite(false, null, BigDecimal.ZERO));

        doReturn(0).when(site, "openDocument");

        site.check();

        assertEquals(Status.SOCKET_ERROR, site.getLink().getStatus());
    }

    @Test
    public void should_set_NETWORK_ERROR() throws Exception {
        FakeSite site = spy(new FakeSite(false, null, BigDecimal.ZERO));

        doReturn(400).when(site, "openDocument");

        site.check();

        assertEquals(Status.NETWORK_ERROR, site.getLink().getStatus());
    }

    private final Link link = new Link();

    private class FakeSite extends AbstractWebsite {

        private boolean isAvailable;
        private String name;
        private BigDecimal price;

        private boolean willHtmlBePulled;

        private FakeSite(boolean isAvailable, String name, BigDecimal price) {
            this(isAvailable, name, price, true);
        }

        private FakeSite(boolean isAvailable, String name, BigDecimal price, boolean willHtmlBePulled) {
            super(link);
            this.isAvailable = isAvailable;
            this.name = name;
            this.price = price;
            this.willHtmlBePulled = willHtmlBePulled;
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
            return Constants.NOT_AVAILABLE;
        }

        @Override
        public String getSeller() {
            return Constants.NOT_AVAILABLE;
        }

        @Override
        public String getSku() {
            return Constants.NOT_AVAILABLE;
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
            return Constants.NOT_AVAILABLE;
        }

        @Override
        public List<LinkSpec> getSpecList() {
            return null;
        }
    }

}
