package io.inprice.scrapper.worker.websites.de;

import io.inprice.scrapper.common.meta.Status;
import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.worker.websites.Helpers;
import io.inprice.scrapper.worker.websites.Website;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class Rakuten_DE_Test {

    private final String SITE_NAME = "rakuten";
    private final String COUNTRY_CODE = "de";

    private final Website site = new io.inprice.scrapper.worker.websites.xx.Rakuten(new Link());

    @Test
    public void test_product_1() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("56477395", link.getSku());
        assertEquals("Western Stars", link.getName());
        assertEquals("20.99", link.getPrice().toString());
        assertEquals("Sony Music Entertainment Germ", link.getBrand());
        assertEquals("buecher.de", link.getSeller());
        assertEquals("Free shipping", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("217988201", link.getSku());
        assertEquals("Xiaomi N4M340 Ninebot Plus 11 Zoll Electric Scooter Self Balancing Selbstbalancierendes Doppelräder", link.getName());
        assertEquals("707.77", link.getPrice().toString());
        assertEquals("Xiaomi", link.getBrand());
        assertEquals("Shenzhen Shanghua E-Commerce Co", link.getSeller());
        assertEquals("Free shipping", link.getShipment());
        assertNull(link.getSpecList());
    }

    @Test
    public void test_product_3() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("1353202652-6613-1937", link.getSku());
        assertEquals("Energiespar Deckenventilator Eco Genuino Chrom Flügel Holz Natur", link.getName());
        assertEquals("489.00", link.getPrice().toString());
        assertEquals("CasaFan", link.getBrand());
        assertEquals("Tobias Krist", link.getSeller());
        assertEquals("Free shipping", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("00000182-500", link.getSku());
        assertEquals("Eisformen aus Silikon / Stieleisformen / Eisformen Eis am Stiel, Stieleisformen Silikon, rot", link.getName());
        assertEquals("16.99", link.getPrice().toString());
        assertEquals("Zollner24", link.getBrand());
        assertEquals("Zollner24", link.getSeller());
        assertEquals("Free shipping", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

}
