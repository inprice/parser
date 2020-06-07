package io.inprice.scrapper.worker.websites.uk;

import io.inprice.scrapper.common.meta.CompetitorStatus;
import io.inprice.scrapper.common.models.Competitor;
import io.inprice.scrapper.worker.websites.Helpers;
import io.inprice.scrapper.worker.websites.Website;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Amazon_UK_Test {

    private final String SITE_NAME = "amazon";
    private final String COUNTRY_CODE = "uk";

    private final Website site = new io.inprice.scrapper.worker.websites.xx.Amazon(new Competitor());

    @Test
    public void test_product_1() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("B071HZV1H7", competitor.getSku());
        assertEquals("Waterpik WP-560UK Cordless Advanced Water Flosser - White Edition (UK 2-Pin Bathroom Plug)", competitor.getName());
        assertEquals("64.99", competitor.getPrice().toString());
        assertEquals("Waterpik", competitor.getBrand());
        assertEquals("Amazon", competitor.getSeller());
        assertEquals("& FREE Delivery in the UK. Delivery Details", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("B07L4F8LJW", competitor.getSku());
        assertEquals("Crash™ Team Racing Nitro-Fueled (PS4)", competitor.getName());
        assertEquals("34.99", competitor.getPrice().toString());
        assertEquals("by ACTIVISION", competitor.getBrand());
        assertEquals("Amazon", competitor.getSeller());
        assertEquals("& FREE Delivery in the UK. Delivery Details", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("B00QB7G10Y", competitor.getSku());
        assertEquals("NINJA BL480UK Nutri 1000W Blender with Auto-iQ-BL480UK-Silver, Silver", competitor.getName());
        assertEquals("69.00", competitor.getPrice().toString());
        assertEquals("Ninja", competitor.getBrand());
        assertEquals("Amazon", competitor.getSeller());
        assertEquals("& FREE Delivery in the UK. Delivery Details", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("B07FP33MLK", competitor.getSku());
        assertEquals("OMERIL LED Head Torch, [2 Pack] Super Bright Headlamps with 3 Modes, 150 Lumens, Lightweight COB Head Lights for Kids Running Walking Camping Fishing, Car Repair, DIY- 6*Batteries Included", competitor.getName());
        assertEquals("9.99", competitor.getPrice().toString());
        assertEquals("OMERIL", competitor.getBrand());
        assertEquals("BLOOM Store", competitor.getSeller());
        assertEquals("& FREE UK Delivery on orders dispatched by Amazon over £20. Delivery Details", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

    @Test
    public void test_product_5() {
        Competitor competitor = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 5));

        assertEquals(CompetitorStatus.AVAILABLE, competitor.getStatus());
        assertEquals("B07RDNVZ65", competitor.getSku());
        assertEquals("NULAXY [Upgraded Version Phone Stand, Adjustable Mobile Phone Desk Holder, Cradle, Dock, Cell Phone Stand compatible with iPhone Xs Xr 8 X 7 6 6s Plus SE 5 5s 5c, All Smartphones-Black", competitor.getName());
        assertEquals("11.99", competitor.getPrice().toString());
        assertEquals("NULAXY", competitor.getBrand());
        assertEquals("WONEW-EU", competitor.getSeller());
        assertEquals("This item does not ship to Turkey.", competitor.getShipment());
        assertTrue(competitor.getSpecList().size() > 0);
    }

}
