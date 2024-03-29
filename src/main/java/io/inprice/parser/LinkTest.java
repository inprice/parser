package io.inprice.parser;

import java.net.Authenticator;
import java.net.Authenticator.RequestorType;
import java.net.PasswordAuthentication;

import org.apache.commons.collections4.CollectionUtils;

import io.inprice.common.models.Link;
import io.inprice.common.models.Platform;
import io.inprice.parser.info.ParseStatus;
import io.inprice.parser.websites.AbstractWebsite;
import io.inprice.parser.websites.fr.LaredouteFR;
import io.inprice.parser.websites.zz.IfConfigZZ;

public class LinkTest {
	
	//singly   --> walmart.ca
	//blocked  --> eprice.it, mediamarkt.es

	// Australia --> appliancesonline, bigw, harveynorman, kogan, thegoodguys
	// Canada    --> bestbuy, canadiantire, walmart
	// Germany   --> euronics, lidl, mediamarkt, notebooksbillinger, otto
	// Spain     --> electroking, euronics, 101gigas, ulabox
	// France    --> auchan, cdiscount, fnac, laredoute
	// Italy     --> euronics, mediaworld
	// Netherland--> bol, coolblue, debijenkorf, wehkamp
	// Turkey    --> gittigidiyor, hepsiburada, n11, teknosa, trendyol
	// U.Kingdom --> argos, asdadirect, asdagrocery, asos, currys, debenhams, euronics, newlook, zavvi
	// U.States  --> bestbuy, bonanza, etsy, walmart, target
	// Global    --> amazon, apple, bonprix, ebay, lidl, mediamarkt, vidaxl, zalando

	// U.States  --> AmazonXX, EbayXX, AppleXX, BestBuyUS, BonanzaUS, EtsyUS, LidlUS, TargetUS, VidaXLXX, WalmartXX
	// U.Kingdom --> AmazonXX, EbayXX, AppleXX, ArgosUK, AsdaDirectUK, AsosUK, BonprixXX, CurrysUK, DebenhamsUK, EuronicsUK, LidlXX, NewLookUK, ZavviUK, ZalandoXX
	// Canada    --> AmazonXX, EbayXX, AppleXX, BestBuyCA, CanadianTireCA, WalmartXX_ALT
	// Australia --> AmazonXX, EbayXX, AppleXX, AppliancesOnlineAU, BigW, HarveyNormanAU, KoganAU, TheGoodGuysAU, VidaXLXX
	// Germany   --> AmazonXX, EbayXX, AppleXX, BonprixXX, EuronicsDE, LidlDE, MediaMarktXX_1, NotebooksBilligerDE, OttoDE, ZalandoXX
	// Netherlands-> AmazonXX, EbayXX, AppleXX, BolNL, BonprixXX, CoolBlueNL, DeBijenkorfNL, LidlNL, MediaMarktXX_2, VidaXLXX, WehkampNL, ZalandoXX
	// France    --> AmazonXX, EbayXX, AppleXX, AuchanFR, BonprixXX, LidlXX, ZalandoXX
	
	/*
  private static final Map<String, AlternativeParser> alternativeParserMap = Map.of(
  		"xx.WalmartXX", new AlternativeParser("/PRD", "alt.WalmartALT")
  	);
	*/

	private static String[] urls = {
		"https://www.laredoute.fr/ppdp/prod-542343290.aspx"
	};
	
	private static final AbstractWebsite website = new LaredouteFR();

	public static void main(String[] args) throws InterruptedException {
		Platform platform = new Platform();
		platform.setDomain("Solo Test");
		platform.setCountry("France");
		
		applyProxy("France");

		for (String url: urls) {
			new Thread(new Runnable() {

				@Override
				public void run() {
		  		Link link = new Link(url);
		  		link.setId(1l);
		  		link.setRetry(1);
		  		link.setPlatform(platform);
		  		link.setPlatformId(platform.getId());

		  		/*
		    	//checks class name and a word in the url to determine if an alternative class must be used!
		    	AlternativeParser altPar = alternativeParserMap.get("xx.WalmartXX");
		    	if (altPar != null && urls[0].indexOf(altPar.getWordInUrl()) > 0) {
		    		website = new WalmartXX_ALT();
		    		System.out.println("Alternative parser is in use!!!");
		    	}
		    	*/

		  		ParseStatus status = website.check(link);
		  		printout(link, status);
				}
			}).start();
		}
	}

	private static void printout(Link link, ParseStatus status) {
		System.out.println("Sku: " + link.getSku());
		System.out.println("Name: " + link.getName());
		System.out.println("Price: " + link.getPrice());
		System.out.println("Brand: " + link.getBrand());
		System.out.println("Seller: " + link.getSeller());
		System.out.println("Shipment: " + link.getShipment());

		System.out.println("--------------------------");
		System.out.println("Status: " + link.getStatus());
		System.out.println("Parse Code: " + link.getParseCode());
		System.out.println("Parse Problem: " + link.getParseProblem());
		System.out.println("--------------------------");

		if (CollectionUtils.isNotEmpty(link.getSpecList())) {
			System.out.println("Spec List");
			link.getSpecList().forEach(s -> System.out.println(" - Key: " + s.getKey() + ", Value: " + s.getValue()));
		}
	}
	
	private static void applyProxy(String country) {
    final String authUser = "mdumlupinar";
    final String authPassword = "ZLLKIoebz0Xi1WVk";
    final String proxyHost = "proxy.packetstream.io";
    final int proxyPort = 31112;
    final String destURL = "https://ipv4.icanhazip.com";

    System.setProperty("jdk.http.auth.tunneling.disabledSchemes", "");
    System.setProperty("jdk.http.auth.proxying.disabledSchemes", "");

    Authenticator.setDefault(new Authenticator() {@Override
      protected PasswordAuthentication getPasswordAuthentication() {
        if (getRequestorType().equals(RequestorType.PROXY)) {
          return new PasswordAuthentication(authUser, authPassword.toCharArray());
        }
        return super.getPasswordAuthentication();
      }
    });
		
	}

	//United States
	//"https://www.amazon.com/Samsung-970-EVO-Plus-MZ-V7S2T0B/dp/B07MFZXR1B/?pf_rd_r=5C4RSM4KE4NS6R9J75SQ&pf_rd_p=a424a707-7b70-4cc8-8e86-14651aa9290f&pd_rd_r=d06e9a1b-b1f1-4787-98d9-b5b27f5fb647&pd_rd_w=rDSld&pd_rd_wg=qjTj9&ref_=pd_gw_exports_top_sellers_unrec"
	//"https://www.apple.com/shop/buy-mac/macbook-pro/14-inch-space-gray-10-core-cpu-16-core-gpu-1tb"
	//"https://www.bestbuy.com/site/samsung-galaxy-s22-ultra-128gb-unlocked-burgundy/6494426.p?skuId=6494426&intl=nosplash"
	//"https://www.bonanza.com/items/like/599307266/nikon-digital-camera"
	//"https://www.etsy.com/listing/624453534/ram-horn-shofar-trumpet-from-israel?ref=hp_prn-2&bes=1"
	//"https://www.lidl.com/products/1070120"
	//"https://www.target.com/p/hp-pavilion-15-6-34-gaming-laptop-intel-core-i5-10300h-qc-8gb-ram-256gb-ssd-nvidia-gtx-1650-backlit-kb-black-15-dk1045tg/-/A-83180151#lnk=sametab"
	//"https://www.vidaxl.com/e/vidaxl-speaker-stands-2-pcs-tempered-glass-2-pillars-design-silver/8718475592884.html"
	//"https://www.walmart.com/ip/Xiaomi-MI-Band-6-Smart-Watch-Fitness-Tracker-SpO2-Monitor-30-Sports-Modes-1-56-In-AMOLED-Screen-5ATM-Waterproof-Wristband-Bracelet-Black/575900423?athbdg=L1700"
	//"https://www.walmart.com/ip/Star-Wars-Grogu-Plush-Toy-11-in-The-Child-Character-from-The-Mandalorian/863451787?athcpid=863451787&athpgid=AthenaHomepageDesktop__gm__-1.0&athcgid=null&athznid=BestSellers_BestsellinginToysandGames&athieid=v0&athstid=CS020&athguid=_ZQMfaTZrZAxyLCfx9GaMlKGdOn3dMD21Lgk&athancid=null&athena=true"

	//United Kingdom
	//"https://www.amazon.co.uk/WindGallop-Inflator-Compressor-Automatic-Inflation/dp/B083R15ZDV/ref=zg-bs_automotive_6/258-7384418-8315467?pd_rd_w=xFHWv&pf_rd_p=c3077bff-a471-42bf-a406-b93ec8e1a044&pf_rd_r=8ZEHPR675BACB9BNQ8HP&pd_rd_r=4abfa005-0565-4f8b-a6e7-57515def932c&pd_rd_wg=bz1QV&pd_rd_i=B083R15ZDV&psc=1"
	//"https://www.argos.co.uk/product/2069953?istCompanyId=a74d8886-5df9-4baa-b776-166b3bf9111c&istFeedId=f44915b0-b246-4f76-a3b4-91c4ff266428&istItemId=ixltwximm&istBid=t"
	//"https://direct.asda.com/george/toys-character/kids-bikes-ride-ons/mookie-red-and-black-scuttlebug-beetle/050729693,default,pd.html?cgid=D30M3G1C1"
	//"https://www.bonprix.co.uk/products/pack-of-2-non-wired-bras/_/A-961981_48?PFM_rsn=browse&PFM_ref=false&PFM_psp=own&PFM_pge=1&PFM_lpn=2"
	//"https://www.asos.com/asos-design/asos-design-recycled-frame-oversized-rectangle-sunglasses-in-black-with-black-lens/prd/23133000?colourWayId=60453643&cid=16773"
	//"https://www.currys.co.uk/products/hotpoint-class-2-sa2-544-c-ix-electric-single-oven-stainless-steel-10148725.html"
	//"https://www.debenhams.com/product/armani-exchange-stainless-steel-fashion-analogue-quartz-watch---ax4326_p-6d5045d1-0bc8-4c7d-b2ed-680a4c55a23e?colour=Rose"
	//"https://euronics.co.uk/catalogue/laundry/tumble-dryers/freestanding-tumble-dryers/condenser-tumble-dryers/hoover-hlec9lg-9kg-condenser-tumble-dryer-white/p/094HLEC9LG"
	//"https://www.lidl.co.uk/p/diy-tools/parkside-plasma-cutter-with-compressor/p49910"
	//"https://www.newlook.com/uk/womens/clothing/hoodies-sweatshirts/tall-grey-acid-wash-chicago-logo-sweatshirt/p/805364308"
	//"https://www.zalando.co.uk/jack-wolfskin-seven-seas-3-walking-sandals-limeblue-ja443b02a-m11.html"
	//"https://www.zavvi.com/blu-ray/man-of-steel-4k-ultra-hd/11284534.html"

	//Canada
	//"https://www.amazon.ca/TheFitLife-Exercise-Resistance-Bands-Set/dp/B07BK14R97/ref=pd_rhf_dp_s_pop_multi_srecs_sabr_13/131-2567945-0653402?pd_rd_w=UVGYR&pf_rd_p=792836e4-edf4-471d-96b9-ec43f2e735c5&pf_rd_r=KWXJ129EPEHV5A5Z9WN4&pd_rd_r=c698e4cd-8dbc-410e-a27b-81f3c2a4cdde&pd_rd_wg=Lezx5&pd_rd_i=B07BK14R97&psc=1"
	//"https://www.ebay.ca/itm/255333058959?_trkparms=amclksrc%3DITM%26aid%3D1110006%26algo%3DHOMESPLICE.SIM%26ao%3D1%26asc%3D236703%26meid%3D5d59def7fd9e443b835e52e87726d4ee%26pid%3D101195%26rk%3D4%26rkt%3D12%26sd%3D334311127797%26itm%3D255333058959%26pmt%3D1%26noa%3D0%26pg%3D2047675%26algv%3DSimplAMLv9PairwiseWebMskuAspectsV202110NoVariantSeedWithWeightedSampleMeHot%26brand%3DLenovo&_trksid=p2047675.c101195.m1851&amdata=cksum%3A2553330589595d59def7fd9e443b835e52e87726d4ee%7Cenc%3AAQAGAAACMDWgS1YaeJW%252BHNc%252Bx7I26iGePtttkiS7R7VTrkOyu5I1W8yxRfCxGMlvcL3fjsprBYH5ElUFN5SGhYx8JrkYa0xPRqFwH11aDTUqSBDvqzVD1NIUraxheCcXesU4YXOyKnAtGKyJ6PCIbecfgmDuJlzWRipLvXUNUL0Qqp1xmKF8Q%252BhqaOD6Qjspr89sCLHbEYyX6JxDi9s3DyrW%252B1O1ChJKSAu2wOUhDHIpnLTW9LaL%252B1LsPW1oX%252Fbb%252Bs8ySI8JCxl8xkCulkb1%252F5ZF84n7tfJ%252FTvJ6zFpfsKHnjBn%252FzWhSsmbC0C08VHAGYj6F%252FJ%252BV1RpZ4W9hqTVe2%252BMOusSfKoTvqrKj7z13Y1sxEYgSv1d1ClxFM2eTYLkFrPZCwY2bEctWh1a8pTkLf4W2baCkUaAj%252B1rkFk270vFYB3Qe5wcUJNlqo%252Bg0EWgHmmnp3lq7woluHNbX7trXhULnNbNYwHixH%252FqGRLcW4KObjGD3hlTl%252Fwo6KG97JxWxdG7ADmlJdUItJBBDa%252FOkPCUNWmQ7f3yg%252FLnj1DbBNadG8CSNvip%252BrkbMFYXyiI2McmJ6cdFuoOH17zlTTo8boMO48hshcpmDTpVZbEAomeizukmt7fp%252BPxi6%252BjKD77puKu3cg2xl7FVnrDSlHwT56vCpS4GV0IKt4jbgpFafk3WQ1EBKHYuT8XXQGfv9BMcQlK3KstNwiRlj2bF3rMgUkjkkKYYWH2dmzdbKHr%252F2RmTvadVsxDuG%7Campid%3APL_CLK%7Cclp%3A2047675"
	//"https://www.apple.com/ca/shop/buy-tv/apple-tv-4k/32gb"
	//"https://www.bestbuy.ca/en-ca/product/nordictrack-c-1100i-smart-folding-treadmill/15618578"
	//"https://www.canadiantire.ca/en/pdp/petco-orthopedic-peaceful-nester-dog-bed-grey-40-in-x-30-in-1423900p.html#srp"
	//"https://www.walmart.ca/en/ip/mass-effect-legendary-edition-xbox-one/6000202521168"
	//"https://www.walmart.ca/en/ip/Costway-12-Amp-14-Inch-Electric-Push-Lawn-Corded-Mower-With-Grass-Bag-Green/PRD2QUNYMN8LBHS"

	//Australia
	//"https://www.amazon.com.au/Nylabone-Dura-Chew-Bone-Large/dp/B01H508536?ref_=Oct_DLandingS_D_20fba2bc_62&smid=A3JCEYBC5L8UJ8"
	//"https://www.ebay.com.au/itm/302643647485?_trkparms=pageci%3A7dbdcf02-9246-11ec-90a5-6648f2ef027f%7Cparentrq%3A170cf55317f0a6466e033f1ffffec78a%7Ciid%3A1"
	//"https://www.apple.com/au/shop/buy-mac/macbook-pro/14-inch-space-grey-10-core-cpu-16-core-gpu-1tb"
	//"https://www.appliancesonline.com.au/andoo/product/ostro-tickera-accent-chair-dark-greyu5041320bcsxdyx"
	//"https://www.appliancesonline.com.au/andoo/product/remington-ipl6500au-ilight-pro-face-and-body-ipl-hair-removal"
	//"https://www.bigw.com.au/product/dymples-easy-stroller-lite-black/p/123523"
	//"https://www.harveynorman.com.au/lg-83-inch-c1-cinema-series-4k-uhd-oled-ai-thinq-smart-tv.html"
	//"https://www.kogan.com/au/buy/kogan-smarterhometm-5w-cool-warm-white-smart-bulb-e14-wifi-pack-of-2-kogan/?ssid=201.0a3a1c44-3e00-4e62-af1a-09f75941ed2e"
	//"https://www.vidaxl.com.au/e/vidaxl-outdoor-dog-kennel-253x133x116-cm/8718475724858.html"
	//"https://www.thegoodguys.com.au/lg-65-inches-a1-4k-uhd-self-lit-oled-smart-tv-oled65a1pta"

	//Germany
	//"https://www.amazon.de/Amazon-Basics-cable-stereo-audio-black/dp/B00NO73MUQ?ref_=ast_sto_dp&th=1&psc=1"
	//"https://www.ebay.de/itm/114146333948?_trkparms=pageci%3A51c08294-92fb-11ec-b596-f274680d3ab1%7Cparentrq%3A1bae08ad17f0aaecf1553305fffb4a0c%7Ciid%3A1"
	//"https://www.apple.com/de/shop/buy-watch/apple-watch/41mm-gps-gr%C3%BCn-aluminium-klee-solo-loop-size-1"
	//"https://www.bonprix.de/produkt/langarmshirt-schwarz-974795/?shopId=16437&assetId=22007718"
	//"https://www.euronics.de/tv-und-audio/dvd-blu-ray-video/dvd-player/dvd-s500eg-k-dvd-player-schwarz-4051168877689"
	//"https://www.mediamarkt.de/de/product/_apple-airpods-pro-in-ear-kopfhorer-bluetooth-weiss-2771284.html"
	//"https://www.notebooksbilliger.de/haushaltsgeraete/raumklima/trotec+luftreiniger+airgoclean+10+e+371571"
	//"https://www.otto.de/p/nintendo-switch-oled-modell-inkl-pokemon-legenden-arceus-1581067389/#variationId=1581067390"
	//"https://www.saturn.de/de/product/_apple-pencil-2-generation-2495773.html"
	//"https://www.lidl.de/p/crivit-campinglampe/p100342613"
	//"https://www.zalando.de/polo-ralph-lauren-short-sleeve-poloshirt-elite-blueliberty-po222p0if-k11.html"

	//Netherlands
	//"https://www.amazon.nl/Braun-Elektrisch-Scheerapparaat-Vervangende-scheerkop/dp/B008LQZP5K/?_encoding=UTF8&pd_rd_w=DVXHA&pf_rd_p=55c2ce67-4925-4bd3-9246-79532184b67e&pf_rd_r=BF2G0KNKY1DJW1B5S878&pd_rd_r=25acd4f3-f41a-49d2-82ea-965eaafa0f21&pd_rd_wg=uCfoP&ref_=pd_gw_trq_ed_l79jikzo"
	//"https://www.ebay.nl/itm/262367265830?hash=item3d164ec426%3Ag%3A1nUAAOSw10NZxpPH&LH_BIN=1&LH_ItemCondition=1000"
	//"https://www.apple.com/nl/shop/product/ML5Q3ZM/A/gevlochten-solobandje-mais-45-mm-maat-7?fnode=1e70d946fd712999bed2e09c0c48243e919165f54ce5b1cd9e85e5d9e617d9b64e30504c04741c4df1f738b39f53e8d250df1ac4b230674ecf998dd8f4b154fad90515d05cc57a3cbaa200d5f0b2d08d61aa561d05981c8f3921a237c7f23ad7"
	//"https://www.bol.com/nl/nl/p/zelftest-covid-19-corona-test-corona-covid-sneltest-flow-flex-20-stuks/9300000034540398/?promo=main_802_POPE_B3_product_5_&bltgh=vHTi-WgQlhyMeFxBJDhAKQ.49_lbYhTPuCZMLZ10QQprBFHw_0_1_2.8.ProductImage"
	//"https://www.bonprix.nl/product/zelfophoudende-kousen-zwart-936780/"
	//"https://www.coolblue.nl/product/761432/tp-link-deco-m5-mesh-wifi-3-pack.html"
	//"https://www.debijenkorf.nl/donsje-amsterdam-wadudu-lining-donkey-laarsje-van-leer-9393060009-939306000959240"
	//"https://www.lidl.nl/p/parkside-montage-rolplank-en-werkplaatskruk/p100338177"
	//"https://www.mediamarkt.nl/nl/product/_samsung-rb36t600dsa-1672474.html"
	//"https://www.vidaxl.nl/e/vidaxl-32-st-zwenkwielen-75-mm/8719883716329.html"
	//"https://www.wehkamp.nl/zwitsal-original-deodorant-6x100-ml-zacht-voor-de-huid-compressed-16375413/?ref=%7B%22slug%22%3A%22verzorging-lichaamsverzorging%22%7D"
	//"https://www.zalando.nl/nike-performance-blade-sportshirt-obsidianwhite-n1242d3rh-k12.html"

	//France
	//"https://www.amazon.fr/Pack-Cartouches-Compatible-Officejet-Expedition/dp/B0869RSVC6/ref=sr_1_1?pf_rd_i=21766165031&pf_rd_m=A1X6FK5RDHNB96&pf_rd_p=af7fddaa-9ddb-412a-8206-e82a6ba06e83&pf_rd_r=XP3RDDKA3MZBRFB35HN2&pf_rd_s=merchandised-search-5&pf_rd_t=101&qid=1645520895&s=officeproduct&sr=1-1&srs=21766165031"
	//"https://www.ebay.fr/itm/151577745628?_trkparms=%26rpp_cid%3D61c9d7007e4cb8d76b5e35ad%26rpp_icid%3D61c9d7007e4cb8d76b5e35ac&_trkparms=pageci%3Adcccc08a-93c1-11ec-9eda-fe5ecaf6dcf8%7Cparentrq%3A20c334b717f0aaec86887ba6fff97146%7Ciid%3A1"
	//"https://www.auchan.fr/mattel-uno-deluxe/pr-C801598"
	//"https://www.bonprix.fr/produit/t-shirt-a-dentelle-bleu-fonce-935860/"
	//"https://www.zalando.fr/adidas-originals-continental-80-stripes-unisex-baskets-basses-footwear-whitepulsa-aqua-ad116d14q-a11.html"

}
