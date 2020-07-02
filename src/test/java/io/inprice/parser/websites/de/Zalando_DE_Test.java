package io.inprice.parser.websites.de;

import io.inprice.common.meta.CompetitorStatus;
import io.inprice.common.models.Competitor;
import io.inprice.parser.websites.Helpers;
import io.inprice.parser.websites.Website;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Zalando_DE_Test {

    private final String SITE_NAME = "zalando";
    private final String COUNTRY_CODE = "de";

    private final Website site = new io.inprice.parser.websites.xx.Zalando(new Competitor());

    @Test
    public void test_product_1() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("TIB31H01D-S11", competitor.getSku());
        assertEquals("BED HEAD AFTER PARTY 100ML - Styling", competitor.getName());
        assertEquals("16.05", competitor.getPrice().toString());
        assertEquals("Tigi", competitor.getBrand());
        assertEquals("Zalando", competitor.getSeller());
        assertEquals("Standard-Lieferung kostenlos 3-5 Werktage Express 7,90 € Lieferung verfügbar", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("ES183C02R-K11", competitor.getSku());
        assertEquals("BRAVA BEACH AMERICAN NECKHOLDER HIPSTER - Bikini", competitor.getName());
        assertEquals("27.95", competitor.getPrice().toString());
        assertEquals("Esprit", competitor.getBrand());
        assertEquals("Zalando", competitor.getSeller());
        assertEquals("Standard-Lieferung kostenlos 3-5 Werktage Express 7,90 € Lieferung verfügbar", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("GU152M00V-D11", competitor.getSku());
        assertEquals("MENS SPORT - Uhr - silver", competitor.getName());
        assertEquals("189.95", competitor.getPrice().toString());
        assertEquals("Guess", competitor.getBrand());
        assertEquals("Zalando", competitor.getSeller());
        assertEquals("Standard-Lieferung kostenlos 3-5 Werktage Express 7,90 € Lieferung verfügbar", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("K4451K00T-Q11", competitor.getSku());
        assertEquals("Sonnenbrille", competitor.getName());
        assertEquals("12.75", competitor.getPrice().toString());
        assertEquals("KIOMI", competitor.getBrand());
        assertEquals("Zalando", competitor.getSeller());
        assertEquals("Standard-Lieferung kostenlos 3-5 Werktage Express 7,90 € Lieferung verfügbar", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

}
