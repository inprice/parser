import io.inprice.scrapper.common.logging.Logger;
import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.worker.websites.Website;

public class deneme {

    private static final Logger log = new Logger(deneme.class);

    public static void main(String... args) {
        String[] hepsiburadaURLs = {
            "https://www.hepsiburada.com/emporio-armani-she-edp-kadin-parfumu-30ml-p-SGARMANSHE30?magaza=Hepsiburada",
            "https://www.hepsiburada.com/dede-multi-blocks-92-parca-p-AILEFEN01254",
            "https://www.hepsiburada.com/yenilenmis-samsung-galaxy-j7-prime-12-ay-garantili-p-HBV00000CQAHR",
            "https://www.hepsiburada.com/kontorland-nostalji-8-bit-atari-p-HBV000007RFZP"
        };

        String[] gittigidiyorURLs = {
            "https://urun.gittigidiyor.com/bilgisayar-tablet/hometech-alfa-110a-intel-atom-z3735f-2gb-32gb-emmc-windows-10-home-11-6-416381866",
            "https://urun.gittigidiyor.com/supermarket/caykur-rize-turist-cayi-1000-gr-6-adet-328113242",
            "https://urun.gittigidiyor.com/bebek-anne/britax-romer-affinity-baby-safe-plus-shr-2-travel-set-chili-pepper-chrome-sasi-ile-413834434"
        };

        String[] n11URLs = {
            "https://urun.n11.com/deri-mont-ve-ceket/only-bayan-deri-mont-15119515-new-start-faux-leather-jacket-P123831498",
            "https://urun.n11.com/spa-masaj/taksim-pera-rose-hotel-spada-masaj-keyfi-ve-spa-kullanimi-P283898904",
            "https://urun.n11.com/oto-lastik/bridgestone-blizzak-lm001-20555-r16-91h-kis-lastigi-P240385904",
            "https://urun.n11.com/yuruyus-ve-kosu/nike-t-lite-xi-erkek-kosu-ayakkabisi-616544-007-P274641565"
        };

        String[] argosURLs = {
            "https://www.argos.co.uk/product/8370842",
            "https://www.argos.co.uk/product/1516593",
            "https://www.argos.co.uk/product/8086538?tag=ar:events:babyshower:carousel:8086538"
        };

        String[] ebayURLs = {
            "https://www.ebay.co.uk/itm/SET-AVENGERS-SHIELD-CAPTAIN-AMERICA-HELMET-IRON-MAN-HAMMER-THOR/233211971362?hash=item364c844722&enc=AQADAAAC8FjVrDbVsZ8oH%2F8PNHtt9VX4%2Fw7FZcmMuqsX8uaFEduVXBEL5f0aqS0b%2BHW5MTgloGVWX%2BTWh2Ke9HIUgDWuzfv7IRyodxosi6A8vf7e6votklBYEkI2kFcn7s%2B04jknfOmGJk71%2FHyyzUY%2F7576f0zer9%2BwO%2FMsY%2FGbrWyWpX%2FLnHo4rKMkTw3nlBo4Scn1gdPXyqX6lZAtBQIbPgfJAA98DZ1BlFHAAn6KCRw%2F9AGeUDfCyVmBn13ySsxPnEtlw2l9IJe7ytQwuwwtDX%2B76kjb1XRCOpL%2F33xCNn9q%2B%2BP%2BUsxrIS9CREQG2Hsr7wPkZEe8Fplo4XV%2B8feStdbyBsIs4%2FnguH%2FWG%2BQJFiECDP6vB52aubzxtGduVZKuFfP6k2gJmWfF4J9UICTUyTpKTFlpRvUmX5WQTEsaq%2BTIqxRPtLqvjZvggQe%2FNnVB4wsdoT9mNvoggwTaQZYnKaykn%2FYocFdH%2F9ZKfCuWQb7hzr%2BTUMoaEGFuSijg16n7QUfzo2SZ7XPx%2FZcQatviIGRCiBchuUWxi%2B5EOf8PTmeKpgcZLEIMqXtjHeVxsrppoiJJHB69G1vqP9vVRm%2BZ7ogmHmq%2FLtnRnRyIYw4Nfbvl1dt6xmP%2F63u9zyB2PjWL3ws%2F86kWQf%2Bd3P%2BmpNEuxIfNBUcwwKzEVzY7AFfSJOw0jy4s%2F74uDH5LdytUtZPFppQPsDU5y7mpVt8U2ZulJ6esXgGFposYLc%2Bj1RZnzQgJLAPV2pV3DuqoT3TpQiNv2fUcHStkNOSSu%2BGwWWojf%2BfTtZje7eF4z4H9rb08bsejfPYsyVqJlG1v3rcV4Ot9yWCL84Tu8yDJDtmZRbk%2FTbQoY6L%2FY3ccaMtwpikJkXlq%2FcrXN7%2FtMQocDl2amsmojQkFmdhKGo6W9AIoQx2fC6FcBJ7yRyxGDFw3chKUPfhRHeh%2F5chqkTAdIoD23KwkDGvYluWfV5JWnOApNh3BaGghYDVmRHv5GMtXQX2SxYs4RKoF&checksum=2332119713625a42c862be834450bc37e0d3414bb1e3",
            "https://www.ebay.co.uk/itm/Draper-G1806G-HER-FSC-Heritage-Range-230mm-Garden-Shears/231524390041?epid=1627676644&hash=item35e7edd499",
            "https://www.ebay.co.uk/itm/TYSON-FURY-KLITSCHKO-GERMANY-Framed-Canvas-Print-Signed-Great-Gift-Souvenir/113449957449",
            "https://www.ebay.co.uk/itm/Floating-Buoyancy-Aid-Foam-Vest-Life-Jacket-Rescue-Vest-Automatic-Inflatable/263670768637?var=&hash=item3d6400a3fd&enc=AQADAAADAFjVrDbVsZ8oH%2F8PNHtt9VX4%2Fw7FZcmMuqsX8uaFEduVmuiyxqCPDp7Cwnuro%2F%2BTejMY0bngunuNZSx7xXrnJtAvpc7iKXJxxLJCsHmPbm8r%2By2mUULf%2BIYIZBD%2FRBilACZud%2F7%2FidXMF5Qz5fyGITU25aKfgpSHk5B7WgKEJQWZlqGfsc2Sq0BMPZuAea%2B2TeULS6nkaqM0VA7l7q7%2Fc2XxPAD661gesBtYo1BtiZDBOceyeCMVuYXjoXvWkzi4xV8JvynxcTWhn%2F0AJjChtC3YVlclGKArUnT%2BHB4weywlvkVu6%2BM9F8L1JgSZn5AcRSrQ8k3Dn9aZxBZ386C7dGuJnC0NMP7D3g4WV9jVS%2BWlsFB9zsm6NLoXjZVoasrN%2BX6Z5mCoRGw3UEBG8V3IqYfFCTrnnzpGnaVyjBwg5mq30fJvJYI7h2v4IGFlWF2c%2BYerk7e7%2BD43UjViUH9I%2B%2FuFLNSD60l8RJ4D9z2rXaLgVAAHNYIs9ueqANa7EyYy9SdjEPqfuFn%2B%2Fa41F%2B4OR%2FrjLoGLgRTknePz6P%2BesjXdBV1vg5%2Bpe0vs86hL0133aM80OEzj%2BbLzOHiwOblr9%2Fh2L6yty19KTaxuUyEKiuwMiVQxqsyNPLF4vBurq3gcYyVTAbOyb5aACpD6tsFIEUZpXShGcFnKDAyxZjAUH%2FyGKXqQaZ4bejtE7pnL7tG7R25ShNnKHDSaERPEhje0Ii5Zx77goiyg0Z04iZAj%2BciWHzoM%2FWeqs33T2arpRiyZ2l%2BF%2BjCR%2FEGfnGlchWoI8OEjz8HKkNVbhmYiUOlmLT7Smbw1bGWrpDL18p0QHA77YWsm1I2vZfEavpuDxsShfmbLpZN9Znh1XUejlf8Ig0vMRFDn8Hr6BcJcyDMK2IpP%2B7K455bszwgZhAOulH6AiOWEz91iRyVTzyVKsEHufOL1xEO7%2BSs2oUWmUJNmrfENJvg0l9q3n7Wiix0mMtECHecRIXA%2B8nWK3%2FyfRvWzvr1%2BcMZKt8ByFrQnNJ5mAvTdWQ%3D%3D&checksum=263670768637b49936d52cb84df3bd8c87d696a28d9a"
        };

        String[] amazonURLs = {
            "https://www.amazon.de/Schildkr%C3%B6ten-Sortierung-Sepiaschalen-Mineralbomben-Gesunderhaltung/dp/B07HDSP11B/ref=lp_470881031_1_2_sspa?s=pet-supplies&ie=UTF8&qid=1557042844&sr=1-2-spons&psc=1",
            "https://www.amazon.co.uk/Samsung-SIM-Free-Smartphone-Certified-Refurbished-Black/dp/B07HRKZFKY?pd_rd_w=7kmtT&pf_rd_p=0331ee17-1a3c-47df-90cc-487c0ff33fea&pf_rd_r=V0F5JKYD6FFWDA8ZZ4XN&pd_rd_r=cb6dae65-f822-43d4-bce0-c69b7841dce4&pd_rd_wg=r3KFY&ref_=pd_gw_cr_simh",
            "https://www.amazon.de/dp/B06ZXQV6P8/ref=gw_de_desk_h1_aucc_rd_dntpr15_rdtpr11_shlv_alx?pf_rd_p=511c77f5-4944-4af2-b365-f3c9e60fac9f&pf_rd_r=WRKSQCH44DENSVR2FDVK",
            "https://www.amazon.co.uk/Golden-Swan-White-Vinegar-Litre/dp/B00TZT6GQE?pd_rd_wg=r3KFY&pd_rd_r=cb6dae65-f822-43d4-bce0-c69b7841dce4&pd_rd_w=0JK2s&ref_=pd_gw_ri&pf_rd_r=V0F5JKYD6FFWDA8ZZ4XN&pf_rd_p=c85344a3-5ec3-5d96-b72f-07ff4e82b502",
            "https://www.amazon.de/Victrola-Pro-Automatischer-Plattenspieler-Vinyl-zu-MP3-Aufnahme/dp/B07JFVSJNW?pd_rd_w=m9HYC&pf_rd_p=85b271c5-b536-4dbe-8834-14e1a3991d65&pf_rd_r=WRKSQCH44DENSVR2FDVK&pd_rd_r=e5f88f78-fc27-448a-ace0-84abfef3a174&pd_rd_wg=aC7Tp&ref_=pd_gw_unk",
            "https://www.amazon.co.uk/Super-Smash-Bros-Ultimate-Nintendo/dp/B07BHGGHX1?pd_rd_wg=r3KFY&pd_rd_r=cb6dae65-f822-43d4-bce0-c69b7841dce4&pd_rd_w=0JvDG&ref_=pd_gw_ri&pf_rd_r=V0F5JKYD6FFWDA8ZZ4XN&pf_rd_p=269ec5b6-6f20-5c71-913e-8ce78b2d6799&th=1",
            "https://www.amazon.de/Tassimo-Jacobs-Classico-Kaffee-Getr%C3%A4nke/dp/B0095FMJE6?pd_rd_wg=aC7Tp&pd_rd_r=e5f88f78-fc27-448a-ace0-84abfef3a174&pd_rd_w=kchaO&ref_=pd_gw_ri&pf_rd_r=WRKSQCH44DENSVR2FDVK&pf_rd_p=9dbb654c-708a-5b76-98fc-bdd043d90222",
            "https://www.amazon.co.uk/Spider-man-Into-Spider-Verse-Blu-ray-Region/dp/B07L3LL6F5?pd_rd_wg=r3KFY&pd_rd_r=cb6dae65-f822-43d4-bce0-c69b7841dce4&pd_rd_w=e2Z13&ref_=pd_gw_ri&pf_rd_r=V0F5JKYD6FFWDA8ZZ4XN&pf_rd_p=5df011d0-bcb5-5669-a493-47753caf4ace"
        };

        String[] amazon_fr_URLs = {
            "https://www.amazon.fr/Gritin-dextension-%C3%89cologique-Porte-Cl%C3%A9s-Carte-Touch/dp/B07H4K2QPP?ref_=Oct_BSellerC_1456155031_2&pf_rd_p=2bbad10e-a823-5711-88f0-2d55fe5ba61f&pf_rd_s=merchandised-search-6&pf_rd_t=101&pf_rd_i=1456155031&pf_rd_m=A1X6FK5RDHNB96&pf_rd_r=VQPEQ9W3HH24AQAQC06C&pf_rd_r=VQPEQ9W3HH24AQAQC06C&pf_rd_p=2bbad10e-a823-5711-88f0-2d55fe5ba61f",
            "https://www.amazon.fr/dp/B001NJ179U?ref_=Oct_DotdC_210992031_3&pf_rd_p=8c79e101-094d-57da-a8b9-c8331847982c&pf_rd_s=merchandised-search-5&pf_rd_t=101&pf_rd_i=210992031&pf_rd_m=A1X6FK5RDHNB96&pf_rd_r=C35RQYZ0DQFTX8MB5VXE&pf_rd_r=C35RQYZ0DQFTX8MB5VXE&pf_rd_p=8c79e101-094d-57da-a8b9-c8331847982c",
            "https://www.amazon.fr/Anker-PowerDrive-Delivery-Chargeur-Voiture/dp/B07H4M4N5V?ref_=Oct_DLandingS_PC_2736b6ad_NA&smid=A2PGPJL0BBLHLX",
            "https://www.amazon.fr/dp/B07GTJ1XFQ?ref_=Oct_DotdC_530490_5&pf_rd_p=16831d37-566a-5378-981e-c65d7718dc62&pf_rd_s=merchandised-search-5&pf_rd_t=101&pf_rd_i=530490&pf_rd_m=A1X6FK5RDHNB96&pf_rd_r=PDJM9CD6M355VCWCSHJV&pf_rd_r=PDJM9CD6M355VCWCSHJV&pf_rd_p=16831d37-566a-5378-981e-c65d7718dc62"
        };

        for (String url: amazon_fr_URLs) {
            Link link = new Link(url);
            link.setWebsiteClassName("io.inprice.scrapper.worker.websites.xx.Amazon");
            try {
                Class<Website> resolverClass = (Class<Website>) Class.forName(link.getWebsiteClassName());
                Website website = resolverClass.newInstance();
                website.check(link);
                printOut(link);
            } catch (Exception e) {
                log.error("Error in converting message from byte array to Link", e);
            }
        }
    }

    private static void extractDomain(String url) {
        String newForm = url.replaceAll("^(https?)://|www.", "");
        String[] chunks = newForm.split("/");
        System.out.println(chunks[0]);
    }

    private static boolean isValidURL(String url) {
        if (url == null
        || url.trim().isEmpty()
        || url.trim().length() < 20
        || url.trim().length() > 1000) return false;
        return url.matches("^(http|https)://.*$");
    }

    private static void printOut(Link link) {
        log.debug("--------------------------------------------------------------------------------------------------");
        log.debug("Code  : " + link.getCode());
        log.debug("Title : " + link.getTitle());
        log.debug("Price : %f, Seller: %s, Shipment: %s, Brand: %s", link.getPrice(), link.getSeller(), link.getShipment(), link.getBrand());
        if (link.getSpecList() != null && link.getSpecList().size() > 0) {
            link.getSpecList().forEach(spec -> {
                log.debug("  > " + spec.getKey() + " - " + spec.getValue());
            });
        }
    }

}