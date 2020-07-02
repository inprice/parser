package io.inprice.parser.websites.it;

import io.inprice.common.meta.CompetitorStatus;
import io.inprice.common.models.Competitor;
import io.inprice.parser.websites.Helpers;
import io.inprice.parser.websites.Website;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Amazon_IT_Test {

    private final String SITE_NAME = "amazon";
    private final String COUNTRY_CODE = "it";

    private final Website site = new io.inprice.parser.websites.xx.Amazon(new Competitor());

    @Test
    public void test_product_1() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("B07H1BZY5R", competitor.getSku());
        assertEquals("KollyKolla Bottiglia Acqua in Acciaio Inox - 350/500/650/750ml, Senza BPA, Borraccia Termica Isolamento Sottovuoto a Doppia Parete, Borracce per Bambini, Scuola, Sport, All'aperto, Palestra, Yoga", competitor.getName());
        assertEquals("16.89", competitor.getPrice().toString());
        assertEquals("KollyKolla", competitor.getBrand());
        assertEquals("Amazon", competitor.getSeller());
        assertEquals("NA", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("B075WZ38TX", competitor.getSku());
        assertEquals("Michael Kors Access MKT5026 Orologio Da Uomo", competitor.getName());
        assertEquals("225.00", competitor.getPrice().toString());
        assertEquals("Michael Kors", competitor.getBrand());
        assertEquals("CUOMOWJ", competitor.getSeller());
        assertEquals("Nessun venditore spedisce attualmente questo prodotto in Turchia. Maggiori informazioni", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("B01ESD5ACW", competitor.getSku());
        assertEquals("FOREO LUNA play dispositivo per la pulizia del viso sonico Aquamarine , ultra portatile ed impermeabile", competitor.getName());
        assertEquals("33.86", competitor.getPrice().toString());
        assertEquals("Foreo", competitor.getBrand());
        assertEquals("Amazon", competitor.getSeller());
        assertEquals("Spedizione GRATUITA. Maggiori informazioni", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("B075QLCNMG", competitor.getSku());
        assertEquals("Cuffie Bluetooth Paww Wavesound 3 con Cancellazione del Rumore Adattiva, adattatore per aereo, cavo di ricarica e custodia", competitor.getName());
        assertEquals("79.99", competitor.getPrice().toString());
        assertEquals("Paww", competitor.getBrand());
        assertEquals("Paww Europe", competitor.getSeller());
        assertEquals("Spedizione GRATUITA. Maggiori informazioni", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

}
