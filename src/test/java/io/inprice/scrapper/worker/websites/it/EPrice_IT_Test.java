package io.inprice.scrapper.worker.websites.it;

import io.inprice.scrapper.common.meta.Status;
import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.worker.websites.Helpers;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EPrice_IT_Test {

    private final String SITE_NAME = "eprice";
    private final String COUNTRY_CODE = "it";

    private final EPrice site = new EPrice(new Link());

    @Test
    public void test_product_1() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("4374277", link.getSku());
        assertEquals("D-LINK DWR-730 Mini Router 3G con tecnologia HSPA+ 21 Mbps Slot Sim Card Micro SD", link.getName());
        assertEquals("42.99", link.getPrice().toString());
        assertEquals("D-LINK", link.getBrand());
        assertEquals("ePrice", link.getSeller());
        assertEquals("Venduto e spedito da ePrice", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("13085006", link.getSku());
        assertEquals("SEAGATE SSD 2 TB Serie FireCuda 510 M. 2 Interfaccia PCI Express 3.0", link.getName());
        assertEquals("459.99", link.getPrice().toString());
        assertEquals("SEAGATE", link.getBrand());
        assertEquals("ePrice", link.getSeller());
        assertEquals("Venduto e spedito da ePrice", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("55859104", link.getSku());
        assertEquals("INTEX Piscina Fuoriterra Intex Frame Prisma 400x200x100 Pompa Filtro Scaletta #26788", link.getName());
        assertEquals("315.00", link.getPrice().toString());
        assertEquals("INTEX", link.getBrand());
        assertEquals("ePrice", link.getSeller());
        assertEquals("Venduto e spedito da ePrice", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("13032637", link.getSku());
        assertEquals("APPLE AirPods 2 con Custodia di Ricarica", link.getName());
        assertEquals("169.99", link.getPrice().toString());
        assertEquals("APPLE", link.getBrand());
        assertEquals("ePrice", link.getSeller());
        assertEquals("Venduto e spedito da ePrice", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

}
