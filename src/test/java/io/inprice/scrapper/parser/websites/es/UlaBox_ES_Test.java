package io.inprice.scrapper.parser.websites.es;

import io.inprice.scrapper.common.meta.CompetitorStatus;
import io.inprice.scrapper.common.models.Competitor;
import io.inprice.scrapper.parser.websites.Helpers;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class UlaBox_ES_Test {

    private final String SITE_NAME = "ulabox";
    private final String COUNTRY_CODE = "es";

    private final UlaBox site = new UlaBox(new Competitor());

    @Test
    public void test_product_1() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("48295", competitor.getSku());
        assertEquals("Pack Durex Massage Aloe Vera + Preservativos Sensitivo Suave", competitor.getName());
        assertEquals("36.54", competitor.getPrice().toString());
        assertEquals("Tienda Reckitt Benckiser", competitor.getBrand());
        assertEquals("UlaBox", competitor.getSeller());
        assertEquals("Envío GRATIS a partir de 59€ de compra", competitor.getShipment());
        assertNull(competitor.getSpecList());
    }

    @Test
    public void test_product_2() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("34725", competitor.getSku());
        assertEquals("Absolut Vodka 50 cl", competitor.getName());
        assertEquals("10.99", competitor.getPrice().toString());
        assertEquals("Pernod Ricard", competitor.getBrand());
        assertEquals("UlaBox", competitor.getSeller());
        assertEquals("Envío GRATIS a partir de 59€ de compra", competitor.getShipment());
        assertNull(competitor.getSpecList());
    }

    @Test
    public void test_product_3() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("8983", competitor.getSku());
        assertEquals("Aceite Corporal Coco Addiction Natural Honey 300ml", competitor.getName());
        assertEquals("4.09", competitor.getPrice().toString());
        assertEquals("Tienda Revlon", competitor.getBrand());
        assertEquals("UlaBox", competitor.getSeller());
        assertEquals("Envío GRATIS a partir de 59€ de compra", competitor.getShipment());
        assertNull(competitor.getSpecList());
    }

    @Test
    public void test_product_4() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("3743", competitor.getSku());
        assertEquals("Olay Essentials Leche Limpiadora", competitor.getName());
        assertEquals("3.69", competitor.getPrice().toString());
        assertEquals("P&G", competitor.getBrand());
        assertEquals("UlaBox", competitor.getSeller());
        assertEquals("Envío GRATIS a partir de 59€ de compra", competitor.getShipment());
        assertNull(competitor.getSpecList());
    }

}
