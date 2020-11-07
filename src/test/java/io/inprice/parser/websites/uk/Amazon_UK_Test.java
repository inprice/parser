package io.inprice.parser.websites.uk;

import io.inprice.common.meta.LinkStatus;
import io.inprice.common.models.Link;
import io.inprice.parser.websites.Helpers;
import io.inprice.parser.websites.Website;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Amazon_UK_Test {

    private final String SITE_NAME = "amazon";
    private final String COUNTRY_CODE = "uk";

    private final Website site = new io.inprice.parser.websites.xx.Amazon();

    @Test
    public void test_product_1() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 1));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("B071HZV1H7", link.getSku());
        assertEquals("Waterpik WP-560UK Cordless Advanced Water Flosser - White Edition (UK 2-Pin Bathroom Plug)", link.getName());
        assertEquals("64.99", link.getPrice().toString());
        assertEquals("Waterpik", link.getBrand());
        assertEquals("Amazon", link.getSeller());
        assertEquals("& FREE Delivery in the UK. Delivery Details", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_2() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 2));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("B07L4F8LJW", link.getSku());
        assertEquals("Crash Team Racing Nitro-Fueled (PS4)", link.getName());
        assertEquals("34.99", link.getPrice().toString());
        assertEquals("by ACTIVISION", link.getBrand());
        assertEquals("Amazon", link.getSeller());
        assertEquals("& FREE Delivery in the UK. Delivery Details", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_3() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 3));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("B00QB7G10Y", link.getSku());
        assertEquals("NINJA BL480UK Nutri 1000W Blender with Auto-iQ-BL480UK-Silver, Silver", link.getName());
        assertEquals("69.00", link.getPrice().toString());
        assertEquals("Ninja", link.getBrand());
        assertEquals("Amazon", link.getSeller());
        assertEquals("& FREE Delivery in the UK. Delivery Details", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_4() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 4));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("B07FP33MLK", link.getSku());
        assertEquals("OMERIL LED Head Torch, [2 Pack] Super Bright Headlamps with 3 Modes, 150 Lumens, Lightweight COB Head Lights for Kids Running Walking Camping Fishing, Car Repair, DIY- 6*Batteries Included", link.getName());
        assertEquals("9.99", link.getPrice().toString());
        assertEquals("OMERIL", link.getBrand());
        assertEquals("BLOOM Store", link.getSeller());
        assertEquals("& FREE UK Delivery on orders dispatched by Amazon over £20. Delivery Details", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

    @Test
    public void test_product_5() {
        Link link = site.test(Helpers.getHtmlPath(SITE_NAME, COUNTRY_CODE, 5));

        assertEquals(LinkStatus.AVAILABLE, link.getStatus());
        assertEquals("B07RDNVZ65", link.getSku());
        assertEquals("NULAXY [Upgraded Version Phone Stand, Adjustable Mobile Phone Desk Holder, Cradle, Dock, Cell Phone Stand compatible with iPhone Xs Xr 8 X 7 6 6s Plus SE 5 5s 5c, All Smartphones-Black", link.getName());
        assertEquals("11.99", link.getPrice().toString());
        assertEquals("NULAXY", link.getBrand());
        assertEquals("WONEW-EU", link.getSeller());
        assertEquals("This item does not ship to Turkey.", link.getShipment());
        assertTrue(link.getSpecList().size() > 0);
    }

}
