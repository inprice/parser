package io.inprice.scrapper.worker.websites.exceptions;

import io.inprice.scrapper.common.meta.CompetitorStatus;
import io.inprice.scrapper.common.models.Competitor;
import io.inprice.scrapper.common.models.CompetitorSpec;
import io.inprice.scrapper.worker.helpers.Consts;
import io.inprice.scrapper.worker.websites.AbstractWebsite;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

public class AbstractWebsite_Test {

    @Test
    public void should_set_NO_DATA_for_available_products_with_NA_name() {
        AbstractWebsite site = new FakeSite(true, Consts.Words.NOT_AVAILABLE, BigDecimal.ZERO, false);

        Competitor competitor = site.test(null);

        assertEquals(CompetitorStatus.NO_DATA, competitor.getStatus());
    }

    @Test
    public void should_set_NOT_DATA_for_unavailable_products_with_NULL_name() {
        AbstractWebsite site = new FakeSite(false, null, BigDecimal.ZERO, false);

        Competitor competitor = site.test(null);

        assertEquals(CompetitorStatus.NO_DATA, competitor.getStatus());
    }

    @Test
    public void should_set_SOCKET_ERROR() {
        FakeSite site = spy(new FakeSite(false, null, BigDecimal.ZERO));

        doReturn(0).when(site).openDocument();

        site.check();

        assertEquals(CompetitorStatus.SOCKET_ERROR, site.getCompetitor().getStatus());
    }

    @Test
    public void should_set_NETWORK_ERROR() {
        FakeSite site = spy(new FakeSite(false, null, BigDecimal.ZERO));

        doReturn(400).when(site).openDocument();

        site.check();

        assertEquals(CompetitorStatus.NETWORK_ERROR, site.getCompetitor().getStatus());
    }

    private final Competitor competitor = new Competitor();

    private class FakeSite extends AbstractWebsite {

        private boolean isAvailable;
        private String name;
        private BigDecimal price;

        private boolean willHtmlBePulled;

        private FakeSite(boolean isAvailable, String name, BigDecimal price) {
            this(isAvailable, name, price, true);
        }

        private FakeSite(boolean isAvailable, String name, BigDecimal price, boolean willHtmlBePulled) {
            super(competitor);
            this.isAvailable = isAvailable;
            this.name = name;
            this.price = price;
            this.willHtmlBePulled = willHtmlBePulled;
        }

        public Competitor getCompetitor() {
            return competitor;
        }

        @Override
        protected int openDocument() {
            return super.openDocument();
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
        public List<CompetitorSpec> getSpecList() {
            return null;
        }
    }

}
