package io.inprice.scrapper.worker.websites.au;

import io.inprice.scrapper.common.meta.Status;
import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.worker.websites.Helpers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class HarveyNorman_AU_Test {

    private final String SITE_NAME = "harveynorman";
    private final String COUNTRY_CODE = "au";

    private final HarveyNorman site = new HarveyNorman(new Link());

    @Test
    public void test_product_1() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(Status.ACTIVE, link.getStatus());
        assertEquals("ROYAL/NK/8", link.getSku());
        assertEquals("Nickel 8 Light Chandelier", link.getName());
        assertEquals("1039.00", link.getPrice().toString());
        assertEquals("Powerlight", link.getBrand());
        assertEquals("Harvey Norman Australia", link.getSeller());
        assertEquals("See delivery details", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(Status.ACTIVE, link.getStatus());
        assertEquals("65UM7600PTA", link.getSku());
        assertEquals("LG 65-inch UM76 4K UHD LED LCD AI ThinQ Smart TV", link.getName());
        assertEquals("1595.00", link.getPrice().toString());
        assertEquals("LG", link.getBrand());
        assertEquals("Harvey Norman Australia", link.getSeller());
        assertEquals("See delivery details", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(Status.ACTIVE, link.getStatus());
        assertEquals("HWFM8012", link.getSku());
        assertEquals("Hisense 8kg Front Loading Washing Machine", link.getName());
        assertEquals("495.00", link.getPrice().toString());
        assertEquals("Hisense", link.getBrand());
        assertEquals("Harvey Norman Australia", link.getSeller());
        assertEquals("See delivery details", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(Status.ACTIVE, link.getStatus());
        assertEquals("PT-0190", link.getSku());
        assertEquals("Plantronics RIG 300 Stereo Gaming Headset for PC", link.getName());
        assertEquals("47.00", link.getPrice().toString());
        assertEquals("Plantronics", link.getBrand());
        assertEquals("Harvey Norman Australia", link.getSeller());
        assertEquals("See delivery details", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

}