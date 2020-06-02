package io.inprice.scrapper.worker.websites.it;

import io.inprice.scrapper.common.meta.CompetitorStatus;
import io.inprice.scrapper.common.models.Competitor;
import io.inprice.scrapper.worker.websites.Helpers;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EPrice_IT_Test {

    private final String SITE_NAME = "eprice";
    private final String COUNTRY_CODE = "it";

    private final EPrice site = new EPrice(new Competitor());

    @Test
    public void test_product_1() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("4374277", competitor.getSku());
        assertEquals("D-COMPETITOR DWR-730 Mini Router 3G con tecnologia HSPA+ 21 Mbps Slot Sim Card Micro SD", competitor.getName());
        assertEquals("42.99", competitor.getPrice().toString());
        assertEquals("D-COMPETITOR", competitor.getBrand());
        assertEquals("ePrice", competitor.getSeller());
        assertEquals("Venduto e spedito da ePrice", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("13085006", competitor.getSku());
        assertEquals("SEAGATE SSD 2 TB Serie FireCuda 510 M. 2 Interfaccia PCI Express 3.0", competitor.getName());
        assertEquals("459.99", competitor.getPrice().toString());
        assertEquals("SEAGATE", competitor.getBrand());
        assertEquals("ePrice", competitor.getSeller());
        assertEquals("Venduto e spedito da ePrice", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("55859104", competitor.getSku());
        assertEquals("INTEX Piscina Fuoriterra Intex Frame Prisma 400x200x100 Pompa Filtro Scaletta #26788", competitor.getName());
        assertEquals("315.00", competitor.getPrice().toString());
        assertEquals("INTEX", competitor.getBrand());
        assertEquals("ePrice", competitor.getSeller());
        assertEquals("Venduto e spedito da ePrice", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("13032637", competitor.getSku());
        assertEquals("APPLE AirPods 2 con Custodia di Ricarica", competitor.getName());
        assertEquals("169.99", competitor.getPrice().toString());
        assertEquals("APPLE", competitor.getBrand());
        assertEquals("ePrice", competitor.getSeller());
        assertEquals("Venduto e spedito da ePrice", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

}
