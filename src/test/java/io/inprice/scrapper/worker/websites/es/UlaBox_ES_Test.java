package io.inprice.scrapper.worker.websites.es;

import io.inprice.scrapper.common.meta.Status;
import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.worker.websites.Helpers;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class UlaBox_ES_Test {

    private final String SITE_NAME = "ulabox";
    private final String COUNTRY_CODE = "es";

    private final UlaBox site = new UlaBox(new Link());

    @Test
    public void test_product_1() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("48295", link.getSku());
        assertEquals("Pack Durex Massage Aloe Vera + Preservativos Sensitivo Suave", link.getName());
        assertEquals("36.54", link.getPrice().toString());
        assertEquals("Tienda Reckitt Benckiser", link.getBrand());
        assertEquals("UlaBox", link.getSeller());
        assertEquals("Envío GRATIS a partir de 59€ de compra", link.getShipment());
        assertNull(link.getSpecList());
    }

    @Test
    public void test_product_2() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("34725", link.getSku());
        assertEquals("Absolut Vodka 50 cl", link.getName());
        assertEquals("10.99", link.getPrice().toString());
        assertEquals("Pernod Ricard", link.getBrand());
        assertEquals("UlaBox", link.getSeller());
        assertEquals("Envío GRATIS a partir de 59€ de compra", link.getShipment());
        assertNull(link.getSpecList());
    }

    @Test
    public void test_product_3() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("8983", link.getSku());
        assertEquals("Aceite Corporal Coco Addiction Natural Honey 300ml", link.getName());
        assertEquals("4.09", link.getPrice().toString());
        assertEquals("Tienda Revlon", link.getBrand());
        assertEquals("UlaBox", link.getSeller());
        assertEquals("Envío GRATIS a partir de 59€ de compra", link.getShipment());
        assertNull(link.getSpecList());
    }

    @Test
    public void test_product_4() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(Status.AVAILABLE, link.getStatus());
        assertEquals("3743", link.getSku());
        assertEquals("Olay Essentials Leche Limpiadora", link.getName());
        assertEquals("3.69", link.getPrice().toString());
        assertEquals("P&G", link.getBrand());
        assertEquals("UlaBox", link.getSeller());
        assertEquals("Envío GRATIS a partir de 59€ de compra", link.getShipment());
        assertNull(link.getSpecList());
    }

}
