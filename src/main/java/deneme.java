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

        String[] amazon_uk_URLs = {
            "https://www.amazon.co.uk/Samsung-SIM-Free-Smartphone-Certified-Refurbished-Black/dp/B07HRKZFKY?pd_rd_w=7kmtT&pf_rd_p=0331ee17-1a3c-47df-90cc-487c0ff33fea&pf_rd_r=V0F5JKYD6FFWDA8ZZ4XN&pd_rd_r=cb6dae65-f822-43d4-bce0-c69b7841dce4&pd_rd_wg=r3KFY&ref_=pd_gw_cr_simh",
            "https://www.amazon.co.uk/Golden-Swan-White-Vinegar-Litre/dp/B00TZT6GQE?pd_rd_wg=r3KFY&pd_rd_r=cb6dae65-f822-43d4-bce0-c69b7841dce4&pd_rd_w=0JK2s&ref_=pd_gw_ri&pf_rd_r=V0F5JKYD6FFWDA8ZZ4XN&pf_rd_p=c85344a3-5ec3-5d96-b72f-07ff4e82b502",
            "https://www.amazon.co.uk/Super-Smash-Bros-Ultimate-Nintendo/dp/B07BHGGHX1?pd_rd_wg=r3KFY&pd_rd_r=cb6dae65-f822-43d4-bce0-c69b7841dce4&pd_rd_w=0JvDG&ref_=pd_gw_ri&pf_rd_r=V0F5JKYD6FFWDA8ZZ4XN&pf_rd_p=269ec5b6-6f20-5c71-913e-8ce78b2d6799&th=1",
            "https://www.amazon.co.uk/Spider-man-Into-Spider-Verse-Blu-ray-Region/dp/B07L3LL6F5?pd_rd_wg=r3KFY&pd_rd_r=cb6dae65-f822-43d4-bce0-c69b7841dce4&pd_rd_w=e2Z13&ref_=pd_gw_ri&pf_rd_r=V0F5JKYD6FFWDA8ZZ4XN&pf_rd_p=5df011d0-bcb5-5669-a493-47753caf4ace"
        };

        String[] amazon_de_URLs = {
            "https://www.amazon.de/Schildkr%C3%B6ten-Sortierung-Sepiaschalen-Mineralbomben-Gesunderhaltung/dp/B07HDSP11B/ref=lp_470881031_1_2_sspa?s=pet-supplies&ie=UTF8&qid=1557042844&sr=1-2-spons&psc=1",
            "https://www.amazon.de/dp/B06ZXQV6P8/ref=gw_de_desk_h1_aucc_rd_dntpr15_rdtpr11_shlv_alx?pf_rd_p=511c77f5-4944-4af2-b365-f3c9e60fac9f&pf_rd_r=WRKSQCH44DENSVR2FDVK",
            "https://www.amazon.de/Victrola-Pro-Automatischer-Plattenspieler-Vinyl-zu-MP3-Aufnahme/dp/B07JFVSJNW?pd_rd_w=m9HYC&pf_rd_p=85b271c5-b536-4dbe-8834-14e1a3991d65&pf_rd_r=WRKSQCH44DENSVR2FDVK&pd_rd_r=e5f88f78-fc27-448a-ace0-84abfef3a174&pd_rd_wg=aC7Tp&ref_=pd_gw_unk",
            "https://www.amazon.de/Tassimo-Jacobs-Classico-Kaffee-Getr%C3%A4nke/dp/B0095FMJE6?pd_rd_wg=aC7Tp&pd_rd_r=e5f88f78-fc27-448a-ace0-84abfef3a174&pd_rd_w=kchaO&ref_=pd_gw_ri&pf_rd_r=WRKSQCH44DENSVR2FDVK&pf_rd_p=9dbb654c-708a-5b76-98fc-bdd043d90222"
        };

        String[] amazon_fr_URLs = {
            "https://www.amazon.fr/Gritin-dextension-%C3%89cologique-Porte-Cl%C3%A9s-Carte-Touch/dp/B07H4K2QPP?ref_=Oct_BSellerC_1456155031_2&pf_rd_p=2bbad10e-a823-5711-88f0-2d55fe5ba61f&pf_rd_s=merchandised-search-6&pf_rd_t=101&pf_rd_i=1456155031&pf_rd_m=A1X6FK5RDHNB96&pf_rd_r=VQPEQ9W3HH24AQAQC06C&pf_rd_r=VQPEQ9W3HH24AQAQC06C&pf_rd_p=2bbad10e-a823-5711-88f0-2d55fe5ba61f",
            "https://www.amazon.fr/dp/B001NJ179U?ref_=Oct_DotdC_210992031_3&pf_rd_p=8c79e101-094d-57da-a8b9-c8331847982c&pf_rd_s=merchandised-search-5&pf_rd_t=101&pf_rd_i=210992031&pf_rd_m=A1X6FK5RDHNB96&pf_rd_r=C35RQYZ0DQFTX8MB5VXE&pf_rd_r=C35RQYZ0DQFTX8MB5VXE&pf_rd_p=8c79e101-094d-57da-a8b9-c8331847982c",
            "https://www.amazon.fr/Anker-PowerDrive-Delivery-Chargeur-Voiture/dp/B07H4M4N5V?ref_=Oct_DLandingS_PC_2736b6ad_NA&smid=A2PGPJL0BBLHLX",
            "https://www.amazon.fr/dp/B07GTJ1XFQ?ref_=Oct_DotdC_530490_5&pf_rd_p=16831d37-566a-5378-981e-c65d7718dc62&pf_rd_s=merchandised-search-5&pf_rd_t=101&pf_rd_i=530490&pf_rd_m=A1X6FK5RDHNB96&pf_rd_r=PDJM9CD6M355VCWCSHJV&pf_rd_r=PDJM9CD6M355VCWCSHJV&pf_rd_p=16831d37-566a-5378-981e-c65d7718dc62"
        };

        String[] amazon_tr_URLs = {
            "https://www.amazon.com.tr/DeFacto-Fermuarl%C4%B1-G%C3%B6zl%C3%BC-%C3%87antas%C4%B1-Beyaz/dp/B07RW4J8H7/ref=lp_13484625031_1_2?s=sports&ie=UTF8&qid=1558340604&sr=1-2",
            "https://www.amazon.com.tr/Arnica-Terra-Premium-Elektrikli-S%C3%BCp%C3%BCrge/dp/B011RPXN1O?pd_rd_w=oyxxB&pf_rd_p=694170e2-8866-4ac3-8958-d4c3c028e935&pf_rd_r=3H71QA3KRM8C01EH13Z3&pd_rd_r=2a558fbd-c510-4a16-b42e-5c5c2aa24f6b&pd_rd_wg=bovly&ref_=pd_gw_cr_simh",
            "https://www.amazon.com.tr/gp/product/B07R1NZS63/ref=s9_acsd_newrz_hd_bw_bEkNzUd_c_x_w?pf_rd_m=A1UNQM1SR2CHM&pf_rd_s=merchandised-search-11&pf_rd_r=RKPTXHAMPS3VV1CAAQ9F&pf_rd_t=101&pf_rd_p=59bce53d-a015-5a8d-95ad-e62bbe3349b8&pf_rd_i=13511289031",
            "https://www.amazon.com.tr/Kurban-Tuza%C4%9F%C4%B1ndan-Kurtulmak-Diane-Zimberoff-M/dp/605988363X?pd_rd_wg=bovly&pd_rd_r=2a558fbd-c510-4a16-b42e-5c5c2aa24f6b&pd_rd_w=ZfBdI&ref_=pd_gw_ri&pf_rd_r=3H71QA3KRM8C01EH13Z3&pf_rd_p=3229d5d9-6405-54b2-9044-efe0df427d0f"
        };

        String[] amazon_br_URLs = {
            "https://www.amazon.com.br/Sutil-Arte-Ligar-Se/dp/855100249X/ref=pd_rhf_gw_s_pd_crbs_0_5/144-2759219-9196109?_encoding=UTF8&pd_rd_i=855100249X&pd_rd_r=b71c8884-1ff2-433a-b9e6-a93f9d28339a&pd_rd_w=oknDA&pd_rd_wg=BDuk2&pf_rd_p=ef1a2638-bdaa-4980-8477-92b7b1882fbf&pf_rd_r=SZF5NC0QFRSN0RC3H4ZZ&psc=1&refRID=SZF5NC0QFRSN0RC3H4ZZ",
            "https://www.amazon.com.br/dp/B077TBS6ZV?smid=A1ZZFT5FULY4LN&pf_rd_p=218b3e38-ad08-4a31-8f95-c9007b4aab9e&pf_rd_r=0WXJNMTH5B4R09WMKDZB",
            "https://www.amazon.com.br/dp/B0797FGMDB/ref=br_msw_pdt-4/144-2759219-9196109?_encoding=UTF8&smid=A1ZZFT5FULY4LN&pf_rd_m=A1ZZFT5FULY4LN&pf_rd_s=&pf_rd_r=0WXJNMTH5B4R09WMKDZB&pf_rd_t=36701&pf_rd_p=5e678fa7-a330-4acb-b750-db681bb1a0a9&pf_rd_i=desktop",
            "https://www.amazon.com.br/dp/B07D3JT1XP/ref=br_msw_pdt-6/144-2759219-9196109?_encoding=UTF8&smid=A1ZZFT5FULY4LN&pf_rd_m=A1ZZFT5FULY4LN&pf_rd_s=&pf_rd_r=0WXJNMTH5B4R09WMKDZB&pf_rd_t=36701&pf_rd_p=7933c283-6061-4eff-99b7-b54cd49e667e&pf_rd_i=desktop"
        };

        String[] ebayURLs = {
            "https://www.ebay.com/itm/Mens-Sport-Cycling-Bicycle-Sunglasses-Outdoor-Goggles-Driving-Eyewear-Glasses/302714386456?_trkparms=pageci%3Ae8379ee0-7afb-11e9-8541-74dbd18015f3%7Cparentrq%3Ad53e600f16a0a887dd0a8048fff6201a%7Ciid%3A1",
            "https://www.ebay.com/itm/1x-LAMBDASONDE-LAMDASONDE-REGELSONDE-VOR-KAT-POLO-6N-AUDI-A3-8L-A4-B6-OCTAVIA-1U/152050212135?_trkparms=aid%3D111001%26algo%3DREC.SEED%26ao%3D1%26asc%3D20180816085401%26meid%3Df91b079678a54818924c4a5fd126f29c%26pid%3D100970%26rk%3D2%26rkt%3D15%26sd%3D183796140739%26itm%3D152050212135&_trksid=p2481888.c100970.m5481&_trkparms=pageci%3Ae8379ee0-7afb-11e9-8541-74dbd18015f3%7Cparentrq%3Ad53e600f16a0a887dd0a8048fff6201a%7Ciid%3A1",
            "https://www.ebay.com/itm/Bear-Ted-Plush-Toy-Stuffed-Animals-Flying-Animals/173485761008?_trkparms=%26rpp_cid%3D5b73f0b26cbe743bd7d06fc2%26rpp_icid%3D5b73dfe5d6a6ba082f1a7ce6",
            "https://www.ebay.com/itm/Cleaning-Brush-Magic-Glove-Pet-Dog-Cat-Massage-Hair-Removal-Grooming-Groomer/272820105146?_trkparms=pageci%3Ae8379ee0-7afb-11e9-8541-74dbd18015f3%7Cparentrq%3Ad53e600f16a0a887dd0a8048fff6201a%7Ciid%3A1",
            "https://www.ebay.com/itm/E27-E14-E12-B22-LED-Corn-Bulb-5730-SMD-Light-Corn-Lamp-Incandescent-20W-160W/222532891266?_trkparms=pageci%3Ae8379ee0-7afb-11e9-8541-74dbd18015f3%7Cparentrq%3Ad53e600f16a0a887dd0a8048fff6201a%7Ciid%3A1"
        };

        String[] ebay_uk_URLs = {
            "https://www.ebay.co.uk/itm/SET-AVENGERS-SHIELD-CAPTAIN-AMERICA-HELMET-IRON-MAN-HAMMER-THOR/233211971362?hash=item364c844722&enc=AQADAAAC8FjVrDbVsZ8oH%2F8PNHtt9VX4%2Fw7FZcmMuqsX8uaFEduVXBEL5f0aqS0b%2BHW5MTgloGVWX%2BTWh2Ke9HIUgDWuzfv7IRyodxosi6A8vf7e6votklBYEkI2kFcn7s%2B04jknfOmGJk71%2FHyyzUY%2F7576f0zer9%2BwO%2FMsY%2FGbrWyWpX%2FLnHo4rKMkTw3nlBo4Scn1gdPXyqX6lZAtBQIbPgfJAA98DZ1BlFHAAn6KCRw%2F9AGeUDfCyVmBn13ySsxPnEtlw2l9IJe7ytQwuwwtDX%2B76kjb1XRCOpL%2F33xCNn9q%2B%2BP%2BUsxrIS9CREQG2Hsr7wPkZEe8Fplo4XV%2B8feStdbyBsIs4%2FnguH%2FWG%2BQJFiECDP6vB52aubzxtGduVZKuFfP6k2gJmWfF4J9UICTUyTpKTFlpRvUmX5WQTEsaq%2BTIqxRPtLqvjZvggQe%2FNnVB4wsdoT9mNvoggwTaQZYnKaykn%2FYocFdH%2F9ZKfCuWQb7hzr%2BTUMoaEGFuSijg16n7QUfzo2SZ7XPx%2FZcQatviIGRCiBchuUWxi%2B5EOf8PTmeKpgcZLEIMqXtjHeVxsrppoiJJHB69G1vqP9vVRm%2BZ7ogmHmq%2FLtnRnRyIYw4Nfbvl1dt6xmP%2F63u9zyB2PjWL3ws%2F86kWQf%2Bd3P%2BmpNEuxIfNBUcwwKzEVzY7AFfSJOw0jy4s%2F74uDH5LdytUtZPFppQPsDU5y7mpVt8U2ZulJ6esXgGFposYLc%2Bj1RZnzQgJLAPV2pV3DuqoT3TpQiNv2fUcHStkNOSSu%2BGwWWojf%2BfTtZje7eF4z4H9rb08bsejfPYsyVqJlG1v3rcV4Ot9yWCL84Tu8yDJDtmZRbk%2FTbQoY6L%2FY3ccaMtwpikJkXlq%2FcrXN7%2FtMQocDl2amsmojQkFmdhKGo6W9AIoQx2fC6FcBJ7yRyxGDFw3chKUPfhRHeh%2F5chqkTAdIoD23KwkDGvYluWfV5JWnOApNh3BaGghYDVmRHv5GMtXQX2SxYs4RKoF&checksum=2332119713625a42c862be834450bc37e0d3414bb1e3",
            "https://www.ebay.co.uk/itm/Draper-G1806G-HER-FSC-Heritage-Range-230mm-Garden-Shears/231524390041?epid=1627676644&hash=item35e7edd499",
            "https://www.ebay.co.uk/itm/TYSON-FURY-KLITSCHKO-GERMANY-Framed-Canvas-Print-Signed-Great-Gift-Souvenir/113449957449",
            "https://www.ebay.co.uk/itm/Floating-Buoyancy-Aid-Foam-Vest-Life-Jacket-Rescue-Vest-Automatic-Inflatable/263670768637?var=&hash=item3d6400a3fd&enc=AQADAAADAFjVrDbVsZ8oH%2F8PNHtt9VX4%2Fw7FZcmMuqsX8uaFEduVmuiyxqCPDp7Cwnuro%2F%2BTejMY0bngunuNZSx7xXrnJtAvpc7iKXJxxLJCsHmPbm8r%2By2mUULf%2BIYIZBD%2FRBilACZud%2F7%2FidXMF5Qz5fyGITU25aKfgpSHk5B7WgKEJQWZlqGfsc2Sq0BMPZuAea%2B2TeULS6nkaqM0VA7l7q7%2Fc2XxPAD661gesBtYo1BtiZDBOceyeCMVuYXjoXvWkzi4xV8JvynxcTWhn%2F0AJjChtC3YVlclGKArUnT%2BHB4weywlvkVu6%2BM9F8L1JgSZn5AcRSrQ8k3Dn9aZxBZ386C7dGuJnC0NMP7D3g4WV9jVS%2BWlsFB9zsm6NLoXjZVoasrN%2BX6Z5mCoRGw3UEBG8V3IqYfFCTrnnzpGnaVyjBwg5mq30fJvJYI7h2v4IGFlWF2c%2BYerk7e7%2BD43UjViUH9I%2B%2FuFLNSD60l8RJ4D9z2rXaLgVAAHNYIs9ueqANa7EyYy9SdjEPqfuFn%2B%2Fa41F%2B4OR%2FrjLoGLgRTknePz6P%2BesjXdBV1vg5%2Bpe0vs86hL0133aM80OEzj%2BbLzOHiwOblr9%2Fh2L6yty19KTaxuUyEKiuwMiVQxqsyNPLF4vBurq3gcYyVTAbOyb5aACpD6tsFIEUZpXShGcFnKDAyxZjAUH%2FyGKXqQaZ4bejtE7pnL7tG7R25ShNnKHDSaERPEhje0Ii5Zx77goiyg0Z04iZAj%2BciWHzoM%2FWeqs33T2arpRiyZ2l%2BF%2BjCR%2FEGfnGlchWoI8OEjz8HKkNVbhmYiUOlmLT7Smbw1bGWrpDL18p0QHA77YWsm1I2vZfEavpuDxsShfmbLpZN9Znh1XUejlf8Ig0vMRFDn8Hr6BcJcyDMK2IpP%2B7K455bszwgZhAOulH6AiOWEz91iRyVTzyVKsEHufOL1xEO7%2BSs2oUWmUJNmrfENJvg0l9q3n7Wiix0mMtECHecRIXA%2B8nWK3%2FyfRvWzvr1%2BcMZKt8ByFrQnNJ5mAvTdWQ%3D%3D&checksum=263670768637b49936d52cb84df3bd8c87d696a28d9a"
        };

        String[] ebay_es_URLs = {
            "https://www.ebay.es/itm/Bicicleta-estatica-plegable-con-freno-magnetico-regulable-y-pulsometro-FITFIU/311460342258?hash=item48847ba9f2%3Ag%3A4BIAAOSw7R9cSDSc&_trkparms=%2526rpp_cid%253D5c9a02bbb805c45c149ee008",
            "https://www.ebay.es/itm/Frigorifico-2-Puertas-KUNFT-KDD2529WH-144cm-A-Estatico-Refr-170-L-Cong-45-L/263767422336?_trkparms=pageci%3A4c69eca6-7aff-11e9-8687-74dbd180c897%7Cparentrq%3Ad554992416a0ad4a57fa8713ffb97a83%7Ciid%3A1",
            "https://www.ebay.es/itm/Cafetera-Multicapsula-y-Espresso-POTTS-dolce-gusto-nespresso-cafe-molido/352566398658?_trkparms=pageci%3A4c69eca6-7aff-11e9-8687-74dbd180c897%7Cparentrq%3Ad554992416a0ad4a57fa8713ffb97a83%7Ciid%3A1",
            "https://www.ebay.es/itm/Cortacesped-autopropulsado-de-gasolina-165cc-ancho-483mm-recogida-4-1-GREENCUT/362438467091?_trkparms=pageci%3A4c69eca6-7aff-11e9-8687-74dbd180c897%7Cparentrq%3Ad554992416a0ad4a57fa8713ffb97a83%7Ciid%3A1"
        };

        String[] asos_uk_URLs = {
            "https://www.asos.com/ginger-ray/ginger-ray-pink-confetti-balloons-5pk/prd/9108594?clr=multi&colourWayId=15265656&SearchQuery=&cid=19872",
            "https://www.asos.com/new-look/new-look-tube-flat-slider-sandal-in-black/prd/11873508?clr=black&colourWayId=16379931&SearchQuery=&cid=15872",
            "https://www.asos.com/nike-training/nike-pro-training-leggings-in-black/prd/10587365?clr=black&colourWayId=15128132&SearchQuery=&cid=27163",
            "https://www.asos.com/lime-crime/lime-crime-angel-velvetines-lip-liner-poppy/prd/11003593?clr=poppy&colourWayId=16277840&SearchQuery=&cid=25669",
            "https://www.asos.com/new-balance/new-balance-373-trainers-in-black/prd/7350844?clr=black&colourWayId=15279827&SearchQuery=&cid=15892"
        };

        String[] newlook_uk_URLs = {
            "https://www.newlook.com/uk/mens/clothing/shirts/burgundy-cotton-short-sleeve-oxford-shirt/p/611256867?comp=Browse",
            "https://www.newlook.com/uk/womens/accessories/hair-accessories/2-pack-silver-diamant%C3%A9-hair-clips/p/630834290?comp=Browse",
            "https://www.newlook.com/uk/womens/accessories/bags/black-metal-trim-envelope-clutch-bag/p/579181101?comp=Browse",
            "https://www.newlook.com/uk/womens/accessories/womens-living/clear-glitter-girls-unite-slogan-1-litre-bottle/p/622940899?comp=Browse"
        };

        String[] currys_uk_URLs = {
            "https://www.currys.co.uk/gbuk/audio-and-headphones/audio/radios/jbl-tuner-portable-dab-fm-bluetooth-radio-black-10182838-pdt.html",
            "https://www.currys.co.uk/gbuk/tv-and-home-entertainment/gaming/virtual-reality/oculus-rift-s-vr-gaming-headset-10192914-pdt.html",
            "https://www.currys.co.uk/gbuk/computing/pc-monitors/pc-monitors/msi-optix-mag271c-full-hd-27-curved-led-gaming-monitor-black-10188685-pdt.html",
            "https://www.currys.co.uk/gbuk/household-appliances/laundry/washing-machines/hotpoint-fml-842-g-uk-8-kg-1400-spin-washing-machine-graphite-10184935-pdt.html"
        };

        String[] zavvi_uk_URLs = {
            "https://www.zavvi.com/blu-ray/x-men-trilogy-4k-ultra-hd-includes-blu-ray/11849516.html",
            "https://www.zavvi.com/merch-clothing/loungefly-disney-classic-mickey-mouse-aop-zip-around-wallet/11831621.html",
            "https://www.zavvi.com/home-accessories/avengers-infinity-war-infinite-power-within-doormat/12079962.html",
            "https://www.zavvi.com/merch-action-figures/neca-spyro-the-dragon-7-inch-action-figure/11939068.html"
        };

        String[] debenhams_uk_URLs = {
            "https://www.debenhams.com/webapp/wcs/stores/servlet/prod_10701_10001_321009114299MISC_-1",
            "https://www.debenhamsplus.com/p/1335624/medion-erazer-p6689-core-i7-8550u-8gb-1tb-128gb-ssd-dvdrw-nvidia-gtx1050-4gb-15.6-inch-full-hd-gaming-laptop",
            "https://www.debenhams.com/webapp/wcs/stores/servlet/prod_10701_10001_61131+DF72199_-1",
            "https://www.debenhams.com/webapp/wcs/stores/servlet/prod_10701_10001_161010418163_-1"
        };

        for (String url: zavvi_uk_URLs) {
            Link link = new Link(url);
            link.setWebsiteClassName("io.inprice.scrapper.worker.websites.uk.Zavvi");
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

    private static void printOut(Link link) {
        log.debug("--------------------------------------------------------------------------------------------------");
        log.debug("SKU   : " + link.getSku());
        log.debug("Name  : " + link.getName());
        log.debug("Price : %f, Seller: %s, Shipment: %s, Brand: %s", link.getPrice(), link.getSeller(), link.getShipment(), link.getBrand());
        if (link.getSpecList() != null && link.getSpecList().size() > 0) {
            link.getSpecList().forEach(spec -> {
                log.debug("  > " + spec.getKey() + " - " + spec.getValue());
            });
        }
    }

}
