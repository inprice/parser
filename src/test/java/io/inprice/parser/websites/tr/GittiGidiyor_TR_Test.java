package io.inprice.parser.websites.tr;

import io.inprice.common.meta.LinkStatus;
import io.inprice.common.models.Link;
import io.inprice.parser.helpers.Consts;
import io.inprice.parser.websites.Helpers;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GittiGidiyor_TR_Test {

	private final GittiGidiyor site = new GittiGidiyor();

	@Test
	public void test_product_1() {
		Link link = site.test(Helpers.getHtmlPath(site, 1));

		assertEquals(LinkStatus.AVAILABLE, link.getStatus());
		assertEquals("445833337", link.getSku());
		assertEquals("Huawei Mate 20 Lite 64 GB Duos Cep Telefonu", link.getName());
		assertEquals("2029.00", link.getPrice().toString());
		assertEquals("Huawei", link.getBrand());
		assertEquals("ceppaket", link.getSeller());
		assertEquals("Alıcı Öder - 2-3 gün içinde kargolanır Tüm Türkiye: 9,00 TL", link.getShipment());
		assertTrue(link.getSpecList().size() > 0);
	}

	@Test
	public void test_product_2() {
		Link link = site.test(Helpers.getHtmlPath(site, 2));

		assertEquals(LinkStatus.AVAILABLE, link.getStatus());
		assertEquals("450770874", link.getSku());
		assertEquals("GÜNEŞ ENERJİLİ SOLAR DIŞ MEKAN AYDINLATMASI DEKORATİF 30 LED 5 METRE RENKLİ ANİMASYONLU",
		    link.getName());
		assertEquals("99.99", link.getPrice().toString());
		assertEquals(Consts.Words.NOT_AVAILABLE, link.getBrand());
		assertEquals("cocukbebekevofis", link.getSeller());
		assertEquals("Ücretsiz - Aynı Gün Kargolanır", link.getShipment());
		assertTrue(link.getSpecList().size() > 0);
	}

	@Test
	public void test_product_3() {
		Link link = site.test(Helpers.getHtmlPath(site, 3));

		assertEquals(LinkStatus.AVAILABLE, link.getStatus());
		assertEquals("426398848", link.getSku());
		assertEquals("TÜP ŞEKLİNDE,50 adet, MANTAR TIPALI CAM ŞİŞE, ÜCRETSİZ KARGO", link.getName());
		assertEquals("71.99", link.getPrice().toString());
		assertEquals(Consts.Words.NOT_AVAILABLE, link.getBrand());
		assertEquals("birkanz", link.getSeller());
		assertEquals("Ücretsiz - Aynı Gün Kargolanır", link.getShipment());
		assertTrue(link.getSpecList().size() > 0);
	}

	@Test
	public void test_product_4() {
		Link link = site.test(Helpers.getHtmlPath(site, 4));

		assertEquals(LinkStatus.AVAILABLE, link.getStatus());
		assertEquals("409029831", link.getSku());
		assertEquals("Aden X67 Fly More Combo 2K (3 Bataryalı Set)", link.getName());
		assertEquals("1249.00", link.getPrice().toString());
		assertEquals(Consts.Words.NOT_AVAILABLE, link.getBrand());
		assertEquals("hubsan", link.getSeller());
		assertEquals("Ücretsiz - Aynı Gün Kargolanır", link.getShipment());
		assertTrue(link.getSpecList().size() > 0);
	}

	@Test
	public void test_product_5() {
		Link link = site.test(Helpers.getHtmlPath(site, 5));

		assertEquals(LinkStatus.AVAILABLE, link.getStatus());
		assertEquals("453332669", link.getSku());
		assertEquals("Bosch TDA3024010 Ütü", link.getName());
		assertEquals("258.99", link.getPrice().toString());
		assertEquals("Bosch", link.getBrand());
		assertEquals("sepet-indirimi", link.getSeller());
		assertEquals("Ücretsiz", link.getShipment());
		assertTrue(link.getSpecList().size() > 0);
	}

}
