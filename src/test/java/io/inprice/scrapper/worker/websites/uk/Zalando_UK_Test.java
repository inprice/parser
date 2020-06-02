package io.inprice.scrapper.worker.websites.uk;

import io.inprice.scrapper.common.meta.CompetitorStatus;
import io.inprice.scrapper.common.models.Competitor;
import io.inprice.scrapper.worker.websites.Helpers;
import io.inprice.scrapper.worker.websites.Website;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Zalando_UK_Test {

    private final String SITE_NAME = "zalando";
    private final String COUNTRY_CODE = "uk";

    private final Website site = new io.inprice.scrapper.worker.websites.xx.Zalando(new Competitor());

    @Test
    public void test_product_1() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("AD121J0K8-I11", competitor.getSku());
        assertEquals("TREFOIL HOODIE - Hoodie", competitor.getName());
        assertEquals("41.90", competitor.getPrice().toString());
        assertEquals("adidas Originals", competitor.getBrand());
        assertEquals("Zalando", competitor.getSeller());
        assertEquals("Standard delivery Free 4-6 working days Next day delivery £5.95 order before 2pm", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("TO851L01L-F11", competitor.getSku());
        assertEquals("EARRINGS - Earrings - gold", competitor.getName());
        assertEquals("36.99", competitor.getPrice().toString());
        assertEquals("TomShot", competitor.getBrand());
        assertEquals("Zalando", competitor.getSeller());
        assertEquals("Standard delivery Free 4-6 working days Next day delivery £5.95 order before 2pm", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("M9121U0HC-K11", competitor.getSku());
        assertEquals("STAR - Classic coat", competitor.getName());
        assertEquals("69.99", competitor.getPrice().toString());
        assertEquals("Mango", competitor.getBrand());
        assertEquals("Zalando", competitor.getSeller());
        assertEquals("Standard delivery Free 4-6 working days Next day delivery £5.95 order before 2pm", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("RA251K00I-O11", competitor.getSku());
        assertEquals("Sunglasses", competitor.getName());
        assertEquals("109.99", competitor.getPrice().toString());
        assertEquals("Ray-Ban", competitor.getBrand());
        assertEquals("Zalando", competitor.getSeller());
        assertEquals("Standard delivery Free 4-6 working days Next day delivery £5.95 order before 2pm", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

}
