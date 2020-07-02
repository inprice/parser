package io.inprice.parser.websites.au;

import io.inprice.common.meta.CompetitorStatus;
import io.inprice.common.models.Competitor;
import io.inprice.parser.websites.Helpers;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HarveyNorman_AU_Test {

    private final String SITE_NAME = "harveynorman";
    private final String COUNTRY_CODE = "au";

    private final HarveyNorman site = new HarveyNorman(new Competitor());

    @Test
    public void test_product_1() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("ROYAL/NK/8", competitor.getSku());
        assertEquals("Nickel 8 Light Chandelier", competitor.getName());
        assertEquals("1039.00", competitor.getPrice().toString());
        assertEquals("Powerlight", competitor.getBrand());
        assertEquals("Harvey Norman Australia", competitor.getSeller());
        assertEquals("See delivery details", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("65UM7600PTA", competitor.getSku());
        assertEquals("LG 65-inch UM76 4K UHD LED LCD AI ThinQ Smart TV", competitor.getName());
        assertEquals("1595.00", competitor.getPrice().toString());
        assertEquals("LG", competitor.getBrand());
        assertEquals("Harvey Norman Australia", competitor.getSeller());
        assertEquals("See delivery details", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("HWFM8012", competitor.getSku());
        assertEquals("Hisense 8kg Front Loading Washing Machine", competitor.getName());
        assertEquals("495.00", competitor.getPrice().toString());
        assertEquals("Hisense", competitor.getBrand());
        assertEquals("Harvey Norman Australia", competitor.getSeller());
        assertEquals("See delivery details", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("PT-0190", competitor.getSku());
        assertEquals("Plantronics RIG 300 Stereo Gaming Headset for PC", competitor.getName());
        assertEquals("47.00", competitor.getPrice().toString());
        assertEquals("Plantronics", competitor.getBrand());
        assertEquals("Harvey Norman Australia", competitor.getSeller());
        assertEquals("See delivery details", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

}
