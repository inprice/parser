package io.inprice.parser.websites.it;

import io.inprice.common.meta.LinkStatus;
import io.inprice.common.models.Link;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.websites.Helpers;
import io.inprice.parser.websites.Website;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Amazon_IT_Test {

    private final String SITE_NAME = "amazon";
    private final String COUNTRY_CODE = "it";

    private final Website site = new io.inprice.parser.websites.xx.Amazon();

    @Test
    public void test_product_1() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("B07H1BZY5R", link.getSku());
        assertEquals("KollyKolla Bottiglia Acqua in Acciaio Inox - 350/500/650/750ml, Senza BPA, Borraccia Termica Isolamento Sottovuoto a Doppia Parete, Borracce per Bambini, Scuola, Sport, All'aperto, Palestra, Yoga", link.getName());
        assertEquals("16.89", link.getPrice().toString());
        assertEquals("KollyKolla", link.getBrand());
        assertEquals("Amazon", link.getSeller());
        assertEquals(Consts.Words.NOT_AVAILABLE, link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("B075WZ38TX", link.getSku());
        assertEquals("Michael Kors Access MKT5026 Orologio Da Uomo", link.getName());
        assertEquals("225.00", link.getPrice().toString());
        assertEquals("Michael Kors", link.getBrand());
        assertEquals("CUOMOWJ", link.getSeller());
        assertEquals("Nessun venditore spedisce attualmente questo prodotto in Turchia. Maggiori informazioni", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("B01ESD5ACW", link.getSku());
        assertEquals("FOREO LUNA play dispositivo per la pulizia del viso sonico Aquamarine , ultra portatile ed impermeabile", link.getName());
        assertEquals("33.86", link.getPrice().toString());
        assertEquals("Foreo", link.getBrand());
        assertEquals("Amazon", link.getSeller());
        assertEquals("Spedizione GRATUITA. Maggiori informazioni", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("B075QLCNMG", link.getSku());
        assertEquals("Cuffie Bluetooth Paww Wavesound 3 con Cancellazione del Rumore Adattiva, adattatore per aereo, cavo di ricarica e custodia", link.getName());
        assertEquals("79.99", link.getPrice().toString());
        assertEquals("Paww", link.getBrand());
        assertEquals("Paww Europe", link.getSeller());
        assertEquals("Spedizione GRATUITA. Maggiori informazioni", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

}
