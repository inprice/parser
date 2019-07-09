package io.inprice.scrapper.worker.websites;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import io.inprice.scrapper.common.logging.Logger;
import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.worker.helpers.UserAgents;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.net.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class deneme {

    private static final Logger log = new Logger(deneme.class);

    public static void main(String... args) {

        String[] teknosa_tr = {
            //"https://www.teknosa.com/goldmaster-enjoy-60-radyolu-turuncu-bluetooth-speaker-p-110300637",
            //"https://www.teknosa.com/philips-gc932420-perfectcare-aqua-pro-tionic-taban-buhar-kazanli-utu-p-120170416",
            //"https://www.teknosa.com/mickey-clubhouse-havlu-p-176000129",
            "https://www.teknosa.com/jwin-jf01-blok-flut-p-135040422"
        };

        String[] trendyol_tr = {
            "https://www.trendyol.com/eurostar/bio-filter-ring-beyaz-500-ml-p-3300773?boutiqueId=314378&merchantId=107634",
            "https://www.trendyol.com/natracare/intim-mendil-12-li-p-2429648?boutiqueId=314714&merchantId=106679",
            //"https://www.trendyol.com/samsung/samsung-860-evo-2-5-sata-3-0-ssd-disk-500gb-mz-76e500bw-p-2517007?boutiqueId=304398&merchantId=107174",
            //"https://www.trendyol.com/u-s-polo-assn/siyah-antrasit-lacivert-erkek-3-lu-boxer-us-01-80097-p-1783349?boutiqueId=314756&merchantId=1235",
        };

        String[] hepsiburada_tr = {
            "https://www.hepsiburada.com/emporio-armani-she-edp-kadin-parfumu-30ml-p-SGARMANSHE30?magaza=Hepsiburada",
            "https://www.hepsiburada.com/dede-multi-blocks-92-parca-p-AILEFEN01254",
            "https://www.hepsiburada.com/yenilenmis-samsung-galaxy-j7-prime-12-ay-garantili-p-HBV00000CQAHR",
            "https://www.hepsiburada.com/kontorland-nostalji-8-bit-atari-p-HBV000007RFZP"
        };

        String[] gittigidiyor_tr = {
            "https://urun.gittigidiyor.com/bilgisayar-tablet/hometech-alfa-110a-intel-atom-z3735f-2gb-32gb-emmc-windows-10-home-11-6-416381866",
            "https://urun.gittigidiyor.com/supermarket/caykur-rize-turist-cayi-1000-gr-6-adet-328113242",
            "https://urun.gittigidiyor.com/bebek-anne/britax-romer-affinity-baby-safe-plus-shr-2-travel-set-chili-pepper-chrome-sasi-ile-413834434"
        };

        String[] n11_tr = {
            "https://urun.n11.com/deri-mont-ve-ceket/only-bayan-deri-mont-15119515-new-start-faux-leather-jacket-P123831498",
            "https://urun.n11.com/spa-masaj/taksim-pera-rose-hotel-spada-masaj-keyfi-ve-spa-kullanimi-P283898904",
            "https://urun.n11.com/oto-lastik/bridgestone-blizzak-lm001-20555-r16-91h-kis-lastigi-P240385904",
            "https://urun.n11.com/yuruyus-ve-kosu/nike-t-lite-xi-erkek-kosu-ayakkabisi-616544-007-P274641565"
        };

        String[] amazon_uk = {
            "https://www.amazon.co.uk/Samsung-SIM-Free-Smartphone-Certified-Refurbished-Black/dp/B07HRKZFKY?pd_rd_w=7kmtT&pf_rd_p=0331ee17-1a3c-47df-90cc-487c0ff33fea&pf_rd_r=V0F5JKYD6FFWDA8ZZ4XN&pd_rd_r=cb6dae65-f822-43d4-bce0-c69b7841dce4&pd_rd_wg=r3KFY&ref_=pd_gw_cr_simh",
            "https://www.amazon.co.uk/Golden-Swan-White-Vinegar-Litre/dp/B00TZT6GQE?pd_rd_wg=r3KFY&pd_rd_r=cb6dae65-f822-43d4-bce0-c69b7841dce4&pd_rd_w=0JK2s&ref_=pd_gw_ri&pf_rd_r=V0F5JKYD6FFWDA8ZZ4XN&pf_rd_p=c85344a3-5ec3-5d96-b72f-07ff4e82b502",
            "https://www.amazon.co.uk/Super-Smash-Bros-Ultimate-Nintendo/dp/B07BHGGHX1?pd_rd_wg=r3KFY&pd_rd_r=cb6dae65-f822-43d4-bce0-c69b7841dce4&pd_rd_w=0JvDG&ref_=pd_gw_ri&pf_rd_r=V0F5JKYD6FFWDA8ZZ4XN&pf_rd_p=269ec5b6-6f20-5c71-913e-8ce78b2d6799&th=1",
            "https://www.amazon.co.uk/Spider-man-Into-Spider-Verse-Blu-ray-Region/dp/B07L3LL6F5?pd_rd_wg=r3KFY&pd_rd_r=cb6dae65-f822-43d4-bce0-c69b7841dce4&pd_rd_w=e2Z13&ref_=pd_gw_ri&pf_rd_r=V0F5JKYD6FFWDA8ZZ4XN&pf_rd_p=5df011d0-bcb5-5669-a493-47753caf4ace"
        };

        String[] amazon_de = {
            "https://www.amazon.de/Schildkr%C3%B6ten-Sortierung-Sepiaschalen-Mineralbomben-Gesunderhaltung/dp/B07HDSP11B/ref=lp_470881031_1_2_sspa?s=pet-supplies&ie=UTF8&qid=1557042844&sr=1-2-spons&psc=1",
            "https://www.amazon.de/dp/B06ZXQV6P8/ref=gw_de_desk_h1_aucc_rd_dntpr15_rdtpr11_shlv_alx?pf_rd_p=511c77f5-4944-4af2-b365-f3c9e60fac9f&pf_rd_r=WRKSQCH44DENSVR2FDVK",
            "https://www.amazon.de/Victrola-Pro-Automatischer-Plattenspieler-Vinyl-zu-MP3-Aufnahme/dp/B07JFVSJNW?pd_rd_w=m9HYC&pf_rd_p=85b271c5-b536-4dbe-8834-14e1a3991d65&pf_rd_r=WRKSQCH44DENSVR2FDVK&pd_rd_r=e5f88f78-fc27-448a-ace0-84abfef3a174&pd_rd_wg=aC7Tp&ref_=pd_gw_unk",
            "https://www.amazon.de/Tassimo-Jacobs-Classico-Kaffee-Getr%C3%A4nke/dp/B0095FMJE6?pd_rd_wg=aC7Tp&pd_rd_r=e5f88f78-fc27-448a-ace0-84abfef3a174&pd_rd_w=kchaO&ref_=pd_gw_ri&pf_rd_r=WRKSQCH44DENSVR2FDVK&pf_rd_p=9dbb654c-708a-5b76-98fc-bdd043d90222"
        };

        String[] amazon_es = {
            "https://www.amazon.es/Xiaomi-Inteligente-Rastreador-Puls%C3%B3metro-Pron%C3%B3stico/dp/B07R2LSHLR?pf_rd_p=3187c5a7-b24d-5d8f-ba5a-bd70f4f3b001&pf_rd_r=GP6Y2MNCHP9QPCDQNPVV&pd_rd_wg=mfsWq&ref_=pd_gw_ri&pd_rd_w=1vTe6&pd_rd_r=9a0a4f8d-0f3b-499e-bb99-1a3c88159cde",
            "https://www.amazon.es/Dodow-60-000-usuarios-quedan-dormidos/dp/B00ZFOB4BK?pf_rd_p=8aa0eef6-6cd2-46ac-81c9-33b6c7a885a0&pd_rd_wg=mfsWq&pf_rd_r=GP6Y2MNCHP9QPCDQNPVV&ref_=pd_gw_unk&pd_rd_w=PT4Q2&pd_rd_r=9a0a4f8d-0f3b-499e-bb99-1a3c88159cde",
            "https://www.amazon.es/Travis-Touch-Traductor-Inteligente-Inal%C3%A1mbrica/dp/B07HB1RYG7?pf_rd_p=8aa0eef6-6cd2-46ac-81c9-33b6c7a885a0&pd_rd_wg=mfsWq&pf_rd_r=GP6Y2MNCHP9QPCDQNPVV&ref_=pd_gw_unk&pd_rd_w=PT4Q2&pd_rd_r=9a0a4f8d-0f3b-499e-bb99-1a3c88159cde",
            "https://www.amazon.es/Apple-Espacial-Smartphone-Reacondicionado-Certificado/dp/B01L9KX5RY?pf_rd_p=f65deca5-51be-4e0d-81bc-558127d77030&pd_rd_wg=mfsWq&pf_rd_r=GP6Y2MNCHP9QPCDQNPVV&ref_=pd_gw_unk&pd_rd_w=VFCTt&pd_rd_r=9a0a4f8d-0f3b-499e-bb99-1a3c88159cde"
        };

        String[] amazon_fr = {
            "https://www.amazon.fr/Gritin-dextension-%C3%89cologique-Porte-Cl%C3%A9s-Carte-Touch/dp/B07H4K2QPP?ref_=Oct_BSellerC_1456155031_2&pf_rd_p=2bbad10e-a823-5711-88f0-2d55fe5ba61f&pf_rd_s=merchandised-search-6&pf_rd_t=101&pf_rd_i=1456155031&pf_rd_m=A1X6FK5RDHNB96&pf_rd_r=VQPEQ9W3HH24AQAQC06C&pf_rd_r=VQPEQ9W3HH24AQAQC06C&pf_rd_p=2bbad10e-a823-5711-88f0-2d55fe5ba61f",
            "https://www.amazon.fr/dp/B001NJ179U?ref_=Oct_DotdC_210992031_3&pf_rd_p=8c79e101-094d-57da-a8b9-c8331847982c&pf_rd_s=merchandised-search-5&pf_rd_t=101&pf_rd_i=210992031&pf_rd_m=A1X6FK5RDHNB96&pf_rd_r=C35RQYZ0DQFTX8MB5VXE&pf_rd_r=C35RQYZ0DQFTX8MB5VXE&pf_rd_p=8c79e101-094d-57da-a8b9-c8331847982c",
            "https://www.amazon.fr/Anker-PowerDrive-Delivery-Chargeur-Voiture/dp/B07H4M4N5V?ref_=Oct_DLandingS_PC_2736b6ad_NA&smid=A2PGPJL0BBLHLX",
            "https://www.amazon.fr/dp/B07GTJ1XFQ?ref_=Oct_DotdC_530490_5&pf_rd_p=16831d37-566a-5378-981e-c65d7718dc62&pf_rd_s=merchandised-search-5&pf_rd_t=101&pf_rd_i=530490&pf_rd_m=A1X6FK5RDHNB96&pf_rd_r=PDJM9CD6M355VCWCSHJV&pf_rd_r=PDJM9CD6M355VCWCSHJV&pf_rd_p=16831d37-566a-5378-981e-c65d7718dc62"
        };

        String[] amazon_tr = {
            "https://www.amazon.com.tr/DeFacto-Fermuarl%C4%B1-G%C3%B6zl%C3%BC-%C3%87antas%C4%B1-Beyaz/dp/B07RW4J8H7/ref=lp_13484625031_1_2?s=sports&ie=UTF8&qid=1558340604&sr=1-2",
            "https://www.amazon.com.tr/Arnica-Terra-Premium-Elektrikli-S%C3%BCp%C3%BCrge/dp/B011RPXN1O?pd_rd_w=oyxxB&pf_rd_p=694170e2-8866-4ac3-8958-d4c3c028e935&pf_rd_r=3H71QA3KRM8C01EH13Z3&pd_rd_r=2a558fbd-c510-4a16-b42e-5c5c2aa24f6b&pd_rd_wg=bovly&ref_=pd_gw_cr_simh",
            "https://www.amazon.com.tr/gp/product/B07R1NZS63/ref=s9_acsd_newrz_hd_bw_bEkNzUd_c_x_w?pf_rd_m=A1UNQM1SR2CHM&pf_rd_s=merchandised-search-11&pf_rd_r=RKPTXHAMPS3VV1CAAQ9F&pf_rd_t=101&pf_rd_p=59bce53d-a015-5a8d-95ad-e62bbe3349b8&pf_rd_i=13511289031",
            "https://www.amazon.com.tr/Kurban-Tuza%C4%9F%C4%B1ndan-Kurtulmak-Diane-Zimberoff-M/dp/605988363X?pd_rd_wg=bovly&pd_rd_r=2a558fbd-c510-4a16-b42e-5c5c2aa24f6b&pd_rd_w=ZfBdI&ref_=pd_gw_ri&pf_rd_r=3H71QA3KRM8C01EH13Z3&pf_rd_p=3229d5d9-6405-54b2-9044-efe0df427d0f"
        };

        String[] amazon_br = {
            "https://www.amazon.com.br/Sutil-Arte-Ligar-Se/dp/855100249X/ref=pd_rhf_gw_s_pd_crbs_0_5/144-2759219-9196109?_encoding=UTF8&pd_rd_i=855100249X&pd_rd_r=b71c8884-1ff2-433a-b9e6-a93f9d28339a&pd_rd_w=oknDA&pd_rd_wg=BDuk2&pf_rd_p=ef1a2638-bdaa-4980-8477-92b7b1882fbf&pf_rd_r=SZF5NC0QFRSN0RC3H4ZZ&psc=1&refRID=SZF5NC0QFRSN0RC3H4ZZ",
            "https://www.amazon.com.br/dp/B077TBS6ZV?smid=A1ZZFT5FULY4LN&pf_rd_p=218b3e38-ad08-4a31-8f95-c9007b4aab9e&pf_rd_r=0WXJNMTH5B4R09WMKDZB",
            "https://www.amazon.com.br/dp/B0797FGMDB/ref=br_msw_pdt-4/144-2759219-9196109?_encoding=UTF8&smid=A1ZZFT5FULY4LN&pf_rd_m=A1ZZFT5FULY4LN&pf_rd_s=&pf_rd_r=0WXJNMTH5B4R09WMKDZB&pf_rd_t=36701&pf_rd_p=5e678fa7-a330-4acb-b750-db681bb1a0a9&pf_rd_i=desktop",
            "https://www.amazon.com.br/dp/B07D3JT1XP/ref=br_msw_pdt-6/144-2759219-9196109?_encoding=UTF8&smid=A1ZZFT5FULY4LN&pf_rd_m=A1ZZFT5FULY4LN&pf_rd_s=&pf_rd_r=0WXJNMTH5B4R09WMKDZB&pf_rd_t=36701&pf_rd_p=7933c283-6061-4eff-99b7-b54cd49e667e&pf_rd_i=desktop"
        };

        String[] ebay_us = {
            "https://www.ebay.com/itm/Mens-Sport-Cycling-Bicycle-Sunglasses-Outdoor-Goggles-Driving-Eyewear-Glasses/302714386456?_trkparms=pageci%3Ae8379ee0-7afb-11e9-8541-74dbd18015f3%7Cparentrq%3Ad53e600f16a0a887dd0a8048fff6201a%7Ciid%3A1",
            "https://www.ebay.com/itm/1x-LAMBDASONDE-LAMDASONDE-REGELSONDE-VOR-KAT-POLO-6N-AUDI-A3-8L-A4-B6-OCTAVIA-1U/152050212135?_trkparms=aid%3D111001%26algo%3DREC.SEED%26ao%3D1%26asc%3D20180816085401%26meid%3Df91b079678a54818924c4a5fd126f29c%26pid%3D100970%26rk%3D2%26rkt%3D15%26sd%3D183796140739%26itm%3D152050212135&_trksid=p2481888.c100970.m5481&_trkparms=pageci%3Ae8379ee0-7afb-11e9-8541-74dbd18015f3%7Cparentrq%3Ad53e600f16a0a887dd0a8048fff6201a%7Ciid%3A1",
            "https://www.ebay.com/itm/Bear-Ted-Plush-Toy-Stuffed-Animals-Flying-Animals/173485761008?_trkparms=%26rpp_cid%3D5b73f0b26cbe743bd7d06fc2%26rpp_icid%3D5b73dfe5d6a6ba082f1a7ce6",
            "https://www.ebay.com/itm/Cleaning-Brush-Magic-Glove-Pet-Dog-Cat-Massage-Hair-Removal-Grooming-Groomer/272820105146?_trkparms=pageci%3Ae8379ee0-7afb-11e9-8541-74dbd18015f3%7Cparentrq%3Ad53e600f16a0a887dd0a8048fff6201a%7Ciid%3A1",
            "https://www.ebay.com/itm/E27-E14-E12-B22-LED-Corn-Bulb-5730-SMD-Light-Corn-Lamp-Incandescent-20W-160W/222532891266?_trkparms=pageci%3Ae8379ee0-7afb-11e9-8541-74dbd18015f3%7Cparentrq%3Ad53e600f16a0a887dd0a8048fff6201a%7Ciid%3A1"
        };

        String[] ebay_uk = {
            "https://www.ebay.co.uk/itm/SET-AVENGERS-SHIELD-CAPTAIN-AMERICA-HELMET-IRON-MAN-HAMMER-THOR/233211971362?hash=item364c844722&enc=AQADAAAC8FjVrDbVsZ8oH%2F8PNHtt9VX4%2Fw7FZcmMuqsX8uaFEduVXBEL5f0aqS0b%2BHW5MTgloGVWX%2BTWh2Ke9HIUgDWuzfv7IRyodxosi6A8vf7e6votklBYEkI2kFcn7s%2B04jknfOmGJk71%2FHyyzUY%2F7576f0zer9%2BwO%2FMsY%2FGbrWyWpX%2FLnHo4rKMkTw3nlBo4Scn1gdPXyqX6lZAtBQIbPgfJAA98DZ1BlFHAAn6KCRw%2F9AGeUDfCyVmBn13ySsxPnEtlw2l9IJe7ytQwuwwtDX%2B76kjb1XRCOpL%2F33xCNn9q%2B%2BP%2BUsxrIS9CREQG2Hsr7wPkZEe8Fplo4XV%2B8feStdbyBsIs4%2FnguH%2FWG%2BQJFiECDP6vB52aubzxtGduVZKuFfP6k2gJmWfF4J9UICTUyTpKTFlpRvUmX5WQTEsaq%2BTIqxRPtLqvjZvggQe%2FNnVB4wsdoT9mNvoggwTaQZYnKaykn%2FYocFdH%2F9ZKfCuWQb7hzr%2BTUMoaEGFuSijg16n7QUfzo2SZ7XPx%2FZcQatviIGRCiBchuUWxi%2B5EOf8PTmeKpgcZLEIMqXtjHeVxsrppoiJJHB69G1vqP9vVRm%2BZ7ogmHmq%2FLtnRnRyIYw4Nfbvl1dt6xmP%2F63u9zyB2PjWL3ws%2F86kWQf%2Bd3P%2BmpNEuxIfNBUcwwKzEVzY7AFfSJOw0jy4s%2F74uDH5LdytUtZPFppQPsDU5y7mpVt8U2ZulJ6esXgGFposYLc%2Bj1RZnzQgJLAPV2pV3DuqoT3TpQiNv2fUcHStkNOSSu%2BGwWWojf%2BfTtZje7eF4z4H9rb08bsejfPYsyVqJlG1v3rcV4Ot9yWCL84Tu8yDJDtmZRbk%2FTbQoY6L%2FY3ccaMtwpikJkXlq%2FcrXN7%2FtMQocDl2amsmojQkFmdhKGo6W9AIoQx2fC6FcBJ7yRyxGDFw3chKUPfhRHeh%2F5chqkTAdIoD23KwkDGvYluWfV5JWnOApNh3BaGghYDVmRHv5GMtXQX2SxYs4RKoF&checksum=2332119713625a42c862be834450bc37e0d3414bb1e3",
            "https://www.ebay.co.uk/itm/Draper-G1806G-HER-FSC-Heritage-Range-230mm-Garden-Shears/231524390041?epid=1627676644&hash=item35e7edd499",
            "https://www.ebay.co.uk/itm/TYSON-FURY-KLITSCHKO-GERMANY-Framed-Canvas-Print-Signed-Great-Gift-Souvenir/113449957449",
            "https://www.ebay.co.uk/itm/Floating-Buoyancy-Aid-Foam-Vest-Life-Jacket-Rescue-Vest-Automatic-Inflatable/263670768637?var=&hash=item3d6400a3fd&enc=AQADAAADAFjVrDbVsZ8oH%2F8PNHtt9VX4%2Fw7FZcmMuqsX8uaFEduVmuiyxqCPDp7Cwnuro%2F%2BTejMY0bngunuNZSx7xXrnJtAvpc7iKXJxxLJCsHmPbm8r%2By2mUULf%2BIYIZBD%2FRBilACZud%2F7%2FidXMF5Qz5fyGITU25aKfgpSHk5B7WgKEJQWZlqGfsc2Sq0BMPZuAea%2B2TeULS6nkaqM0VA7l7q7%2Fc2XxPAD661gesBtYo1BtiZDBOceyeCMVuYXjoXvWkzi4xV8JvynxcTWhn%2F0AJjChtC3YVlclGKArUnT%2BHB4weywlvkVu6%2BM9F8L1JgSZn5AcRSrQ8k3Dn9aZxBZ386C7dGuJnC0NMP7D3g4WV9jVS%2BWlsFB9zsm6NLoXjZVoasrN%2BX6Z5mCoRGw3UEBG8V3IqYfFCTrnnzpGnaVyjBwg5mq30fJvJYI7h2v4IGFlWF2c%2BYerk7e7%2BD43UjViUH9I%2B%2FuFLNSD60l8RJ4D9z2rXaLgVAAHNYIs9ueqANa7EyYy9SdjEPqfuFn%2B%2Fa41F%2B4OR%2FrjLoGLgRTknePz6P%2BesjXdBV1vg5%2Bpe0vs86hL0133aM80OEzj%2BbLzOHiwOblr9%2Fh2L6yty19KTaxuUyEKiuwMiVQxqsyNPLF4vBurq3gcYyVTAbOyb5aACpD6tsFIEUZpXShGcFnKDAyxZjAUH%2FyGKXqQaZ4bejtE7pnL7tG7R25ShNnKHDSaERPEhje0Ii5Zx77goiyg0Z04iZAj%2BciWHzoM%2FWeqs33T2arpRiyZ2l%2BF%2BjCR%2FEGfnGlchWoI8OEjz8HKkNVbhmYiUOlmLT7Smbw1bGWrpDL18p0QHA77YWsm1I2vZfEavpuDxsShfmbLpZN9Znh1XUejlf8Ig0vMRFDn8Hr6BcJcyDMK2IpP%2B7K455bszwgZhAOulH6AiOWEz91iRyVTzyVKsEHufOL1xEO7%2BSs2oUWmUJNmrfENJvg0l9q3n7Wiix0mMtECHecRIXA%2B8nWK3%2FyfRvWzvr1%2BcMZKt8ByFrQnNJ5mAvTdWQ%3D%3D&checksum=263670768637b49936d52cb84df3bd8c87d696a28d9a"
        };

        String[] ebay_es = {
            "https://www.ebay.es/itm/Bicicleta-estatica-plegable-con-freno-magnetico-regulable-y-pulsometro-FITFIU/311460342258?hash=item48847ba9f2%3Ag%3A4BIAAOSw7R9cSDSc&_trkparms=%2526rpp_cid%253D5c9a02bbb805c45c149ee008",
            "https://www.ebay.es/itm/Frigorifico-2-Puertas-KUNFT-KDD2529WH-144cm-A-Estatico-Refr-170-L-Cong-45-L/263767422336?_trkparms=pageci%3A4c69eca6-7aff-11e9-8687-74dbd180c897%7Cparentrq%3Ad554992416a0ad4a57fa8713ffb97a83%7Ciid%3A1",
            "https://www.ebay.es/itm/Cafetera-Multicapsula-y-Espresso-POTTS-dolce-gusto-nespresso-cafe-molido/352566398658?_trkparms=pageci%3A4c69eca6-7aff-11e9-8687-74dbd180c897%7Cparentrq%3Ad554992416a0ad4a57fa8713ffb97a83%7Ciid%3A1",
            "https://www.ebay.es/itm/Cortacesped-autopropulsado-de-gasolina-165cc-ancho-483mm-recogida-4-1-GREENCUT/362438467091?_trkparms=pageci%3A4c69eca6-7aff-11e9-8687-74dbd180c897%7Cparentrq%3Ad554992416a0ad4a57fa8713ffb97a83%7Ciid%3A1"
        };

        String[] argos_uk = {
            "https://www.argos.co.uk/product/8370842",
            "https://www.argos.co.uk/product/1516593",
            "https://www.argos.co.uk/product/8086538?tag=ar:events:babyshower:carousel:8086538"
        };

        String[] asos_uk = {
            "https://www.asos.com/ginger-ray/ginger-ray-pink-confetti-balloons-5pk/prd/9108594?clr=multi&colourWayId=15265656&SearchQuery=&cid=19872",
            //"https://www.asos.com/new-look/new-look-tube-flat-slider-sandal-in-black/prd/11873508?clr=black&colourWayId=16379931&SearchQuery=&cid=15872",
            //"https://www.asos.com/nike-training/nike-pro-training-leggings-in-black/prd/10587365?clr=black&colourWayId=15128132&SearchQuery=&cid=27163",
            //"https://www.asos.com/lime-crime/lime-crime-angel-velvetines-lip-liner-poppy/prd/11003593?clr=poppy&colourWayId=16277840&SearchQuery=&cid=25669",
            //"https://www.asos.com/new-balance/new-balance-373-trainers-in-black/prd/7350844?clr=black&colourWayId=15279827&SearchQuery=&cid=15892"
        };

        String[] newlook_uk = {
            "https://www.newlook.com/uk/mens/clothing/shirts/burgundy-cotton-short-sleeve-oxford-shirt/p/611256867?comp=Browse",
            "https://www.newlook.com/uk/womens/accessories/hair-accessories/2-pack-silver-diamant%C3%A9-hair-clips/p/630834290?comp=Browse",
            "https://www.newlook.com/uk/womens/accessories/bags/black-metal-trim-envelope-clutch-bag/p/579181101?comp=Browse",
            "https://www.newlook.com/uk/womens/accessories/womens-living/clear-glitter-girls-unite-slogan-1-litre-bottle/p/622940899?comp=Browse"
        };

        String[] currys_uk = {
            "https://www.currys.co.uk/gbuk/audio-and-headphones/audio/radios/jbl-tuner-portable-dab-fm-bluetooth-radio-black-10182838-pdt.html",
            "https://www.currys.co.uk/gbuk/tv-and-home-entertainment/gaming/virtual-reality/oculus-rift-s-vr-gaming-headset-10192914-pdt.html",
            "https://www.currys.co.uk/gbuk/computing/pc-monitors/pc-monitors/msi-optix-mag271c-full-hd-27-curved-led-gaming-monitor-black-10188685-pdt.html",
            "https://www.currys.co.uk/gbuk/household-appliances/laundry/washing-machines/hotpoint-fml-842-g-uk-8-kg-1400-spin-washing-machine-graphite-10184935-pdt.html"
        };

        String[] zavvi_uk = {
            "https://www.zavvi.com/blu-ray/x-men-trilogy-4k-ultra-hd-includes-blu-ray/11849516.html",
            "https://www.zavvi.com/merch-clothing/loungefly-disney-classic-mickey-mouse-aop-zip-around-wallet/11831621.html",
            "https://www.zavvi.com/home-accessories/avengers-infinity-war-infinite-power-within-doormat/12079962.html",
            "https://www.zavvi.com/merch-action-figures/neca-spyro-the-dragon-7-inch-action-figure/11939068.html"
        };

        String[] debenhams_uk = {
            "https://www.debenhams.com/webapp/wcs/stores/servlet/prod_10701_10001_321009114299MISC_-1",
            //"https://www.debenhamsplus.com/p/1335624/medion-erazer-p6689-core-i7-8550u-8gb-1tb-128gb-ssd-dvdrw-nvidia-gtx1050-4gb-15.6-inch-full-hd-gaming-laptop",
            //"https://www.debenhams.com/webapp/wcs/stores/servlet/prod_10701_10001_61131+DF72199_-1",
            //"https://www.debenhams.com/webapp/wcs/stores/servlet/prod_10701_10001_161010418163_-1"
        };

        String[] mediamarkt_tr = {
            "https://www.mediamarkt.com.tr/tr/product/_n%C4%B1ntendo-1-2-switch-nintende-switch-oyun-1181774.html#teknik-bilgiler",
            "https://www.mediamarkt.com.tr/tr/product/_sand%C4%B1sk-128gb-m%C4%B1cro-sd-extreme-sand%C4%B1sk-sdsqxa1-128g-gn6ma-adp-160mb-s-rescue-pro-deluxe-1192375.html",
            "https://www.mediamarkt.com.tr/tr/product/_sand%C4%B1sk-128gb-sd-kart-95mb-s-ext-pro-c10-sdsdxxg-064g-gn4%C4%B1n-1177291.html",
            "https://www.mediamarkt.com.tr/tr/product/_axen-ax40dab0937-40-102-ekran-uydu-al%C4%B1c%C4%B1l%C4%B1-led-tv-1180602.html",
            "https://www.mediamarkt.com.tr/tr/product/_ttec-2kmm11p-r%C4%B1o-kumandal%C4%B1-ve-mikrofonlu-kulaki%C3%A7i-kulakl%C4%B1k-pembe-1177578.html"
        };

        String[] mediamarkt_nl = {
            "https://www.mediamarkt.nl/nl/product/_samsung-dual-layer-cover-voor-samsung-galaxy-j7-2017-blauw-1518591.html",
            "https://www.mediamarkt.nl/nl/product/_lenco-tcd-2600-music-center-zwart-1601875.html",
            "https://www.mediamarkt.nl/nl/product/_vogels-wall-3205-1543523.html",
            "https://www.mediamarkt.nl/nl/product/_sonos-play-1-zwart-1289431.html",
            "https://www.mediamarkt.nl/nl/product/_koenic-keb350-rvs-1291396.html"
        };

        String[] mediamarkt_es = {
            "https://www.mediamarkt.es/es/product/_microscopio-celestron-digital-de-iniciaci%C3%B3n-1136733.html",
            "https://www.mediamarkt.es/es/product/_alejandro-fern%C3%A1ndez-15-a%C3%B1os-de-%C3%A9xitos-cd-dvd-9216526.html",
            "https://www.mediamarkt.es/es/product/_irrigador-solac-id7840-aqua-smile-dep%C3%B3sito-170-ml-3-modos-40-minutos-de-autonom%C3%ADa-1435039.html",
            "https://www.mediamarkt.es/es/product/_afeitadora-braun-mgk3980-estuche-9-en-1-para-barba-y-pelo-1430907.html"
        };

        String[] otto_de = {
            "https://www.otto.de/p/tom-tailor-sandalette-mit-farbigen-akzenten-716340078/#variationId=716342300",
            //"https://www.otto.de/p/jockenhoefer-gruppe-ecksofa-mit-2-separaten-liegeflaechen-766962708/#variationId=759331063",
            //"https://www.otto.de/p/lascana-7-8-jeggings-687489144/#variationId=631752997",
            //"https://www.otto.de/p/siemens-kaffeevollautomat-kaffeevollautomat-eq-9-s300-ti923509de-individualcoffee-system-persoenliches-getraenke-menue-fuer-bis-zu-6-profile-759657618/#variationId=759658888"
        };

        String[] zalando_uk = {
            "https://www.zalando.co.uk/adidas-performance-leggings-blackwhite-ad541e0ww-q11.html",
            "https://www.zalando.co.uk/g-h-bass-co-larkin-slip-ons-gh112c005-o11.html",
            "https://www.zalando.co.uk/erase-paisley-scarf-fedora-hat-brown-er752q00d-o11.html",
            "https://www.zalando.co.uk/adidas-performance-condivo-18-tracksuit-bottoms-grey-ad543e0lz-c11.html",
            "https://www.zalando.de/resteroeds-resort-shirt-terry-hemd-r6222d00k-a11.html"
        };


        String[] notebooksbilliger_de_ = {
            "https://www.notebooksbilliger.de/netzwerk/powerlines+netzwerk/devolo+magic+2+wifi+starter+kit",
            "https://www.notebooksbilliger.de/hp+mailights/pc+systeme+hp+mailights/omen/omen+by+hp+desktop+pc+880+172ng",
            "https://www.notebooksbilliger.de/beamer/alle+hersteller+beamer/panasonic/products_id/463911",
            "https://www.notebooksbilliger.de/pc+hardware/ssd+wd+black+sn750+nvme+ssd/wd+black+sn750+nvme+ssd+250gb+m2+2280+pcie+30+x4"
        };

        String[] lidl_de = {
            //"https://www.lidl.de/de/topmove-kameratasche/p255537",
            "https://www.lidl.de/de/schildkroet-fitness-springseil-speed-rope-pro/p296902",
            //"https://www.lidl.de/de/florabest-hochlehner-polsterauflage-120-x-50-x-4-cm/p269476?fromRecommendation=true&scenario=top_selling"
        };

        String[] lidl_uk = {
            "https://www.lidl.co.uk/en/Offers.htm?articleId=19337",
            //"https://www.lidl.co.uk/en/MiddleofLidl.htm?articleId=22936",
        };

        String[] bonprix_de = {
            "https://www.bonprix.de/produkt/stretchjeans-im-used-look-blue-bleached-used-975346/#image",
            //"https://www.bonprix.de/produkt/strandtunika-weiss-gold-metallic-956531/#image"
        };

        String[] bonprix_uk = {
            "https://www.bonprix.co.uk/products/brutting-rip-tape-trainers/_/A-920026_8?PFM_rsn=browse&PFM_ref=false&PFM_psp=own&PFM_pge=1&PFM_lpn=8",
            "https://www.bonprix.co.uk/products/super-stretchy-capri-jeans/_/A-905945_10?cs_ev=crossSell-_-recentlyViewed-_-carousel4-_-SuperStretchyCapriJeans&cs_pr=productlist-_-bottom-_-ListPage-_-RecentlyViewed-_-clicked-_-4"
        };

        String[] amazon_ca = {
            "https://www.amazon.ca/Brita-Longlast-Pitcher-Replacement-Filters/dp/B07H8CG6KX?pf_rd_p=4409d64c-9861-5e4f-aacf-c0dd13dbc25d&pf_rd_r=16S731EH61B9B98AYJ64&pd_rd_wg=R7zlA&ref_=pd_gw_ri&pd_rd_w=Wmh05&pd_rd_r=4c9a51f3-4a6d-47d7-a6d0-e743d72b70f8",
            "https://www.amazon.ca/AmazonBasics-Performance-Alkaline-Batteries-Count/dp/B00LH3DMUO?pf_rd_p=8422ed4f-029f-4a88-b4b3-04f0b9a65411&pd_rd_wg=bCLnX&pf_rd_r=334M3QH5X7GWMXHREATV&ref_=pd_gw_bia_d0&pd_rd_w=w2TtP&pd_rd_r=b12c0f5d-93b3-4f02-b8f1-3eb70c960a38"
        };

        String[] ebay_ca = {
            "https://www.ebay.ca/itm/ARCTIC-AIR-Portable-in-Home-Evaporative-Air-Cooler-As-Seen-on-TV-BRAND-NEW/323501089734?_trkparms=pageci%3A3d700d6d-7def-11e9-b0d3-74dbd180934f%7Cparentrq%3Ae894872116a0a9cc5b4bb6b2ffe7c410%7Ciid%3A1",
            "https://www.ebay.ca/itm/Gotham-Steel-8-Piece-Kitchen-and-Cookware-Set-with-Non-Stick-Copper-Coating/323502617412?_trkparms=pageci%3A3d700d6d-7def-11e9-b0d3-74dbd180934f%7Cparentrq%3Ae894872116a0a9cc5b4bb6b2ffe7c410%7Ciid%3A1"
        };

        String[] bestbuy_us = {
            "https://www.bestbuy.com/site/waterpik-ultra-water-flosser-white/9984673.p?skuId=9984673",
            "https://www.bestbuy.com/site/samsung-85-class-led-q900-series-4320p-smart-8k-uhd-tv-with-hdr/6295150.p?skuId=6295150",
            "https://www.bestbuy.com/site/mega-bloks-first-builders-big-building-bag-building-set/6257936.p?skuId=6257936",
            "https://www.bestbuy.com/site/insignia-fire-tv-stick-remote-cover-gray/6032803.p?skuId=6032803",
            "https://www.bestbuy.com/site/waterpik-water-pik-sonic-fusion-rechargeable-toothbrush-and-oral-irrigator-black-chrome/6321894.p?skuId=6321894"
        };

        String[] bestbuy_ca = {
            "https://www.bestbuy.ca/en-ca/product/dickinson-marine-19-100-10-10-ft-low-pressure-propane-hose/10744573",
            "https://www.bestbuy.ca/en-ca/product/anki-cozmo-carrying-case/11471285"
        };

        String[] walmart_ca = {
            //"https://www.walmart.ca/en/ip/scunci-no-slip-silicone-elastics/6000196486963",
            //"https://www.walmart.ca/en/ip/sharp-50-4k-smart-tv-n6003/6000198871514",
            "https://www.walmart.ca/en/ip/ViscoLogic-Series-THRILL-Gaming-Racing-Style-Swivel-Office-Chair/5P82E6OC9V42"
        };

        String[] walmart_us = {
            "https://www.walmart.com/ip/PAW-Patrol-PAW-Patroller-Rescue-Transport-Vehicle/45300865",
            "https://www.walmart.com/ip/Daisy-Youth-Line-1938-Red-Ryder-Air-Rifle/19341879",
            "https://www.walmart.com/ip/Oculus-Go-Standalone-Virtual-Reality-Headset-32GB-Oculus-VR/296029684",
        };

        String[] asda_uk = {
            "https://direct.asda.com/george/outdoor-garden/jakarta/jakarta-linen-corner-group-garden-dining-set/BUN636,default,pd.html?cgid=D33M09G01C05",
            "https://direct.asda.com/george/home/towels-bath-mats/grey-sausage-dog-cotton-towel-range/GEM658079,default,pd.html?cgid=D26M07G07C09",
            "https://direct.asda.com/george/women/shoes/light-brown-snake-print-heeled-chelsea-boots/GEM687876,default,pd.html?cgid=D1M1G20C20&cmpid=otc-good-_-geor-_-6-animal-print-accessories-weve-gone-wild-for-_-fashion-tips-_-section",
        };

        String[] pixmaina_es = {
            //"https://www.pixmania.es/p/knipex-slipjoint-gripping-pliers-125-mm-1054557?offerId=22687062",
            //"https://www.pixmania.es/p/navroad-x5-navegador-127-cm-5-pantalla-tactil-lcd-fijo-negro-184-g-7395496?offerId=22685200",
            "https://www.pixmania.es/p/air-rise-paquete-hoverboard-65-azul-hoverkart-negro-bluetooth-bolsa-y-su-mando-5962449?offerId=20221680",
        };

        String[] ulabox_es = {
            //"https://www.ulabox.com/en/product/refresco-sunny-delight-fresa-1-25l/14762",
            //"https://www.ulabox.com/en/product/dentifrico-elixir-non-stop-fresh/29509",
            //"https://www.ulabox.com/en/product/jabon-liquido-infantil-litsea-eco-yipsophilia/41750",
            "https://www.ulabox.com/en/product/comida-para-gatos-esterilizados-ultima-pollo-y-cebada/14502"
        };

        String[] gigas101_es = {
            //"https://101gigas.com/gigabyte/tarjeta-grafica-gigabyte-rtx-2060-mini-itx-oc-6gd-gv-n2060ixoc-6gd/",
            //"https://101gigas.com/msi/vga-msi-gtx-1050-2gt-ocv1-gddr5-912-v809-2634/",
            //"https://101gigas.com/onaji/onaji-teclado-gaming-fukei-igg315774/",
            "https://101gigas.com/xiaomi/xia-redmi-note-6-pro-32-a-15-9-bk-xiaomi-redmi-note-6-pro-4g-ds-eu-black-mzb6887eu/"
        };

        String[] electroking_es = {
            //"https://www.electroking.es/redes/24238-hub-switch-8-ptos-tp-link-tl-sg1008d-6935364092313.html",
            //"https://www.electroking.es/auriculares/24225-auricular-energy-sistem-style-6-ng-44731-8432426447312.html",
            //"https://www.electroking.es/inicio/24200-patin-electrico-xiaomi-electric-5-blanc-6970244526816.html",
            "https://www.electroking.es/lavadoras/16600-lavadora-balay-3ts873bc-4242006252250.html"
        };

        String[] canadiantire_ca = {
            //"https://www.canadiantire.ca/en/pdp/geeni-spot-smart-wi-fi-plug-0529700p.html#srp",
            //"https://www.canadiantire.ca/en/pdp/granite-rain-barrel-180l-0594161p.html#srp",
            //"https://www.canadiantire.ca/en/pdp/dewalt-7a-drill-driver-3-8-in-0542992p.html#srp",
            "https://www.canadiantire.ca/en/pdp/philips-satinshave-advanced-wet-dry-shaver-women-s-0438594p.html#srp"
        };

        String[] etsy_us = {
            "https://www.etsy.com/listing/477969731/the-spamp-guitar-practice-amplifier-and?ga_order=most_relevant&ga_search_type=all&ga_view_type=gallery&ga_search_query=&ref=sr_gallery-1-5&cns=1",
            //"https://www.etsy.com/listing/693195462/jack-daniels-vinyl-wall-clock-large-jack?ga_order=most_relevant&ga_search_type=all&ga_view_type=gallery&ga_search_query=&ref=sc_gallery-1-3&plkey=4f0b7348bf0c89cf89355a3471585dfe98ae5b77%3A693195462&pro=1&frs=1",
            //"https://www.etsy.com/listing/498796241/custom-skirt-circle-skirt-midi-skirt?ga_order=most_relevant&ga_search_type=all&ga_view_type=gallery&ga_search_query=&ref=sr_gallery-1-5&bes=1&col=1",
            //"https://www.etsy.com/listing/253465953/plug-in-wall-sconce-lamp?ga_order=most_relevant&ga_search_type=all&ga_view_type=gallery&ga_search_query=&ref=sc_gallery-1-4&plkey=b2e990a66028190194e6b8ae7e8455212c131ef0%3A253465953&pro=1&col=1"
        };

        String[] etsy_ca = {
            //"https://www.etsy.com/ca/listing/616087947/bridal-hair-barrette-pearl-crystal-hair?ga_order=most_relevant&ga_search_type=all&ga_view_type=gallery&ga_search_query=&ref=sc_gallery-1-4&plkey=297eae4ae00824f5bd1edd1aebf5e1e383eca42b%3A616087947&bes=1"
            //"https://www.etsy.com/ca/listing/627221358/antique-wooden-tobacco-cutter-distressed?ref=finds_l&pro=1&frs=1",
            //"https://www.etsy.com/ca/listing/456971012/room-decor-dorm-light-dorm-decor-lights?ref=hp_prn&bes=1",
            "https://www.etsy.com/ca/listing/507016675/vintage-metal-drum-drum-toy-eagle-toys?ga_order=most_relevant&ga_search_type=all&ga_view_type=gallery&ga_search_query=&ref=sr_gallery-1-1"
        };

        String[] target_us = {
            "https://www.target.com/p/armitron-174-sport-women-s-digital-chronograph-resin-strap-watch-black/-/A-16747358",
            //"https://www.target.com/p/wire-fan-tassel-necklace-universal-thread-153/-/A-54278968?preselect=54222438#lnk=sametab",
            //"https://www.target.com/p/full-wheaton-coil-spring-mattress-futon-dual-comfort/-/A-54278834?preselect=54242168#lnk=sametab",
            //"https://www.target.com/p/barbie-glam-pool-with-water-slide-pool-accessories/-/A-17305299"
        };

        String[] bonanza_us = {
            //"https://www.bonanza.com/listings/Women-Silk-Chemise-Sexy-V-Neck-Lingerie-Nightwear-Satin-Sleepwear-Lace-Nightgown/718176135?search_term_id=40273148",
            "https://www.bonanza.com/listings/Creative-Living-Solutions-Classic-White-Goose-Feather-Down-3-Piece-Comforter-and/722871269?featured=true&search_term_id=40295301",
            //"https://www.bonanza.com/items/like/87284491/alessandra-adelaide-needleworks-cross-stitch-pattern",
            //"https://www.bonanza.com/listings/MissNity-White-Simulated-Druzy-Stackable-Ring-Women-s-Silver-Plated-Hypoallergen/718348174?featured=true&search_term_id=40301281"
        };

        String[] rakuten_xx = {
            //"https://global.rakuten.com/en/store/maximum-japan/item/lens_case_l/",
            //"https://global.rakuten.com/en/store/jiggys-shop/item/fa162/",
            //"https://global.rakuten.com/en/store/cloudmoda/item/redwing-101/",
            "https://global.rakuten.com/en/store/lecollier/item/10000199/"
        };

        String[] ebay_it = {
            //"https://www.ebay.it/itm/Xiaomi-Redmi-Note-7-4GB-RAM-64GB-ROM-BLU-Snapdragon-660-Octa-Core-48MP-BANDA-20/333116119891?_trkparms=5373%3A0%7C5374%3AFeatured",
            //"https://www.ebay.it/itm/SHABBY-CHIC-PINK-VINTAGE-DISTRESSED-FLORAL-KNOB-HANDLE-DRAWER-DRESSER-CHILDREN/331565009011?hash=item4d32d08873:m:m8As4oOR012VV-wO9hBPg6Q&var=541447287554",
            "https://www.ebay.it/itm/Scarpa-Moto-Scarpette-Calzature-Scooter-Stivale-Tecnico-Pelle-impermianile-Rpro/263788550769?hash=item3d6b05da71:m:mDJwNhQoXN0vfv70xjNxvPg&var=563037879399",
            //"https://www.ebay.it/itm/Polo-Uomo-SLAZENGER-Cotone-Piquet-8-Colori-Art-045/283448150846?_trkparms=pageci%3A3006e80f-84fa-11e9-80f4-74dbd1802eb6%7Cparentrq%3A16bc534c16b0aa6fd65552d6ff91ec4b%7Ciid%3A1"
        };

        String[] amazon_it = {
            //"https://www.amazon.it/massaggiatore-contorno-occhiaie-Ricaricabile-garantita/dp/B01N26M6MV/ref=lp_14606354031_1_15?s=boost&srs=14606354031&ie=UTF8&qid=1559455192&sr=1-15",
            "https://www.amazon.it/Canon-fotocamera-obbiettivi-Ricondizionato-Certificato/dp/B01M5CG6SA?pd_rd_w=ZGnvT&pf_rd_p=54245443-aff5-4834-af54-d4a0f097d6fd&pf_rd_r=2QXNF9VQT443ZNVBCS2G&pd_rd_r=6c4b23a2-da9d-41d3-9c35-0cf5e3461d5f&pd_rd_wg=RKdZm&ref_=pd_gw_unk",
            //"https://www.amazon.it/dp/B07FDV6PKX?ref_=Oct_LDealsC_969755031_1&pf_rd_p=a306f344-7261-51d6-9914-c7727ca71cbe&pf_rd_s=merchandised-search-5&pf_rd_t=101&pf_rd_i=969755031&pf_rd_m=A11IL2PNWYJU7H&pf_rd_r=HRRKPMB97PSYFSQ2GETY&pf_rd_r=HRRKPMB97PSYFSQ2GETY&pf_rd_p=a306f344-7261-51d6-9914-c7727ca71cbe",
            //"https://www.amazon.it/SONGMICS-Poltrona-Direzionale-Ergonomica-Regolabile/dp/B01HNXGLFO/ref=lp_15855215031_1_6?s=kitchen&ie=UTF8&qid=1559455189&sr=1-6"
        };

        String[] mediaworld_it = {
            //"https://www.mediaworld.it/product/p-710937/sigma-150-600mmf5-63-sport-nikon",
            //"https://www.mediaworld.it/product/p-984752/samsung-galaxy-watch-46mm-silver",
            "https://www.mediaworld.it/product/p-994453/amazon-echo-dot-3-generazione-antracite",
            //"https://www.mediaworld.it/product/p-749095/coolermaster-masterset-ms120-combo"
        };

        String[] eprice_it = {
            //"https://www.eprice.it/fotocamere-a-sviluppo-istantaneo-POLAROID/d-7867039?metb=widget-prodotti-hp-sostitutivo-target",
            //"https://www.eprice.it/pneumatici-moto-METZELER/d-56598129",
            //"https://www.eprice.it/carta-HP/d-6896874?metb=widget-prodotti-n_uf_co-widget2",
            "https://www.eprice.it/toner-XEROX/d-9031240?shopid=0"
        };

        String[] euronics_it = {
            //"https://www.euronics.it/indossabili/apple/applewatch-s3-2018-cel-42mm-alluminio-space-grey/eProd182024114/",
            //"https://www.euronics.it/home-theatre/yamaha/yas108bl/eProd192003459/",
            "https://www.euronics.it/contenuti-digitali-games/sony-computer/playstation-network-card-10-/eProd152004387/",
            //"https://www.euronics.it/ventilatori/dyson/am06-dyson-cool/eProd142010733/"
        };

        String[] vidaxl_it = {
            "https://www.vidaxl.it/aste/index/viewProduct/id/961389/hash/4691327b055c78b5bdeb3dd1a4984b6f/",
            //"https://www.vidaxl.it/aste/index/viewProduct/id/961404/hash/d07a780ff0323c327a21cfc426a1ccb5/",
            //"https://www.vidaxl.it/aste/index/viewProduct/id/961379/hash/50ecd22467a943905e9374299dd38d87/",
            //"https://www.vidaxl.it/e/8718475052173/tenda-da-insetti-90-x-220-cm-marrone-beige"
        };

        String[] ebay_fr = {
            //"https://www.ebay.fr/itm/ADIDAS-ORIGINALS-STAN-SMITH-M2060-chaussures-femmes-sport-sneaker-blanc-loisir/183590929929",
            //"https://www.ebay.fr/itm/Trottinette-Electrique-Scooter-Motor-25km-h-20Km-250W-7-0Ah-S9-Tubeless/253928908275?_trkparms=pageci%3A07cc4642-854b-11e9-b23d-74dbd180e728%7Cparentrq%3A18ce233116b0aa46947513b6ff8e1df0%7Ciid%3A1",
            "https://www.ebay.fr/p/Trixie-Maison-de-Toilettes-Vico-Easy-Clean-Chat/1312569062?iid=254248396599",
            //"https://www.ebay.fr/itm/Velo-spinning-appartement-ergonomique-volant-dinertie-24kg-reglable-FITFIU/311796498963"
        };

        String[] apple_xx = {
            //"https://www.apple.com/shop/product/HJ162/steelseries-nimbus-wireless-gaming-controller?fnode=a4",
            "https://www.apple.com/shop/buy-mac/imac/21.5-inch-3.6ghz-quad-core-processor-1tb"
        };

        String[] fnac_fr = {
            //"https://www.fnac.com/LEGO-Star-Wars-75209-Le-Landspeeder-de-Han-Solo/a11562346/w-4#int=S:HookLogic|Home%20G%C3%A9n%C3%A9%7CNonApplicable|11562346|BL6|L1",
            "https://www.fnac.com/Pack-Fnac-PC-Ultra-Portable-Lenovo-Yoga-530-14IKB-81EK00LBFR-14-Souris-sans-fil-Noir-Tapis-de-souris-It-Works-Noir-Sacoche-d-ordinateur-Mobilis-TheOne-Basic-Noir-Microsoft-Office-365-Personnel-1-PC-MAC-1-an/a12909241/w-4#int=:NonApplicable|NonApplicable|NonApplicable|12909241|NonApplicable|NonApplicable",
            //"https://www.fnac.com/mp33469041/Salon-de-jardin-table-extensible-Chicago-210-Taupe-Alice-s-Garden/w-4#int=:NonApplicable|NonApplicable|NonApplicable|33469041|NonApplicable|NonApplicable",
            //"https://www.fnac.com/a13495268/Avengers-Endgame-Steelbook-Edition-Speciale-Fnac-Blu-ray-4K-Ultra-HD-Robert-Downey-Jr-Blu-ray-4K#int=S:Suggestion|Home%20G%C3%A9n%C3%A9%7CNonApplicable|13495268|BL2|L1"
        };

        String[] rakuten_fr = {
            //"https://fr.shopping.rakuten.com/offer?action=desc&aid=4731998207&xtatc=PUB-[pmad]-[KEYWORD1]-[super-hros]-[CPC]-[2927]-[2415162]-19662299[jayobone]",
            //"https://fr.shopping.rakuten.com/mfp/5693259/samsung-galaxy-note8-duos?pid=2270068266&xtatc=PUB-[PMC]-[H]-[Tel-PDA]-[PushProduit1]-[Pdts-2]-[]",
            //"https://fr.shopping.rakuten.com/offer?action=desc&aid=2880539926&productid=763543992",
            "https://fr.shopping.rakuten.com/offer?action=desc&aid=4542698602&productid=3622939970#xtatc=PUB-[PMC]-[H]-[HomePage]-[Carrousel]-[Marque]-[Boulanger]-[3622939970]-[]"
        };

        String[] cdiscount_fr = {
            "https://www.cdiscount.com/jardin/barbecue-plancha/barbecue-au-gaz-richelieu-marron-4-bruleurs-dont/f-1631901-ali3760247261363.html#mpos=2|mp",
            //"https://www.cdiscount.com/bricolage/outillage/mecafer-groupe-electrogene-essence-mf3800-3500-w/f-166010301-mec3283494501384.html?idOffre=139440285",
            //"https://www.cdiscount.com/bricolage/revetement-sol/ajtimber-revetement-stratifie-villa-2-22-m2-ch/f-166180201-ajt4042456978454.html",
            //"https://www.cdiscount.com/maison/achat-meuble-literie/confort-design-matelas-140-x-190cm-mousse-memoir/f-117552013-mrprince04ab.html"
        };

        String[] zalando_fr = {
            //"https://www.zalando.fr/anna-field-manteau-classique-maritime-blue-an621u00c-k11.html",
            //"https://www.zalando.fr/swarovski-luckily-pendant-collier-dark-multi-4sw51l08v-f11.html",
            //"https://www.zalando.fr/tory-burch-fleming-convertible-shoulder-bag-sac-bandouliere-t0751h003-j12.html",
            "https://www.zalando.fr/calvin-klein-jeans-padded-varsity-blouson-bomber-begonia-pink-c1821g01y-j11.html"
        };

        String[] auchan_fr = {
            //"https://www.auchan.fr/apple-iphone-6-reconditionne-grade-b-64-go-gris-slp/p-c1057432",
            //"https://www.auchan.fr/wickey-aire-de-jeux-freeflyer-portique-de-jeux-en-bois-cabane-avec-balancoire-et-toboggan-vert/p-m1733327",
            //"https://www.auchan.fr/yoyo-lumineux-star-wars-disney/p-m1184489",
            "https://www.auchan.fr/lit-cabaneen-bois-massif-90-x-190-cm-louis/p-ca1098579"
        };

        String[] laredoute_fr = {
            //"https://www.laredoute.fr/ppdp/prod-520662037.aspx",
            "https://www.laredoute.fr/ppdp/prod-508220242.aspx",
            //"https://www.laredoute.fr/ppdp/prod-511804188.aspx",
            //"https://www.laredoute.fr/ppdp/prod-526894420.aspx"
        };

        String[] bol_nl = {
            //"https://www.bol.com/nl/p/ses-soep-in-bad-waterspeelset/9200000075519637/?bltg=itm_event%3Dclick%26slt_owner%3DMCM%26itm_type%3Dproduct%26pg_nm%3Dmain%26slt_id%3D802%26slt_pos%3DB3%26itm_lp%3D0%26slt_ttd%3D5%26mcm_ccd%3DTRDO%26mmt_id%3DMMTUNKNOWN&promo=main_802_TRDO-_B3_product_0_",
            //"https://www.bol.com/nl/p/bobby-anti-diefstal-rugzak-zwart/9200000073006245/?bltg=itm_event%3Dclick%26slt_owner%3DMCM%26itm_type%3Dproduct%26pg_nm%3Dmain%26slt_id%3D802%26slt_pos%3DB3%26itm_lp%3D2%26slt_ttd%3D5%26mcm_ccd%3DTRDO%26mmt_id%3DMMTUNKNOWN&promo=main_802_TRDO-_B3_product_2_",
            //"https://www.bol.com/nl/p/walter-wallet-kunststof-creditcardhouder-way-black/9200000061709061/?suggestionType=browse&bltgh=mpfoymqB1-SXWXDXiblHSw.1.7.ProductImage#product_specifications",
            "https://www.bol.com/nl/p/hugo-boss-bottled-200-ml-eau-toilette-herenparfum/9200000016313339/?bltg=itm_event%3Dclick%26slt_owner%3DMCM%26itm_type%3Dproduct%26pg_nm%3Dmain%26slt_id%3D802%26slt_pos%3DB3%26itm_lp%3D4%26slt_ttd%3D5%26mcm_ccd%3DTRDO%26mmt_id%3DMMTUNKNOWN&promo=main_802_TRDO-_B3_product_4_"
        };

        String[] amazon_nl = {
            "https://www.amazon.de/Kindle-Paperwhite/dp/B07741S7Y8/ref=nl_de_rd_b",
            //"https://www.amazon.nl/Parel-zeven-zussen-Book-4-ebook/dp/B07GCYBZ4J?pd_rd_wg=lVua1&pd_rd_r=34d85d21-9be8-4fd1-ae21-795f6b5d5b6a&pd_rd_w=TMuex&ref_=pd_gw_ri&pf_rd_r=ZP9DB96AQ82Y9TNJ9VZW&pf_rd_p=69f19002-e722-56c6-b1e4-850aae3f9ccb",
            //"https://www.amazon.nl/Verras-me-Sophie-Kinsella-ebook/dp/B078Z5HWKW?pd_rd_wg=lVua1&pd_rd_r=34d85d21-9be8-4fd1-ae21-795f6b5d5b6a&pd_rd_w=VnmdA&ref_=pd_gw_ri&pf_rd_r=ZP9DB96AQ82Y9TNJ9VZW&pf_rd_p=3c14ef57-c926-55f7-b255-6997401dac51",
            //"https://www.amazon.nl/Kronieken-van-Onderwereld-Deel-Beenderen-ebook/dp/B00O7TZ43S?pd_rd_wg=lVua1&pd_rd_r=34d85d21-9be8-4fd1-ae21-795f6b5d5b6a&pd_rd_w=n2wRK&ref_=pd_gw_ri&pf_rd_r=ZP9DB96AQ82Y9TNJ9VZW&pf_rd_p=f13739b4-7e6c-567f-b602-0222329ab117"
        };

        String[] ebay_nl = {
            //"https://www.ebay.nl/itm/Nintendo-64-N64-System-Charcoal-Grey-Console-NTSC/192901018413?hash=item2ce9cbf32d:g:Z5gAAOSwOz5cxO1K",
            //"https://www.ebay.nl/itm/MADONNA-Madame-X-2019-Box-Set-2CD-Book-32-Pg-Poster-7-Picture-Disc-MC-14-6/382954726845?hash=item5929e165bd:g:ezwAAOSwNTFc29-2",
            //"https://www.ebay.nl/itm/Natural-Coir-Rubber-Non-Slip-Floor-Entrance-Door-Mat-Indoor-Outdoor-Doormat/192180733978?hash=item2cbedd481a:m:mPht7Tqrpy3VLTGdlPtX5dg",
            //"https://www.ebay.nl/itm/4-Sommerreifen-Dunlop-SP-Sport-Maxx-GT-RFT-RSC-DSST-MFS-245-50-R18-100Y-TOP/253101033141?hash=item3aedff46b5:g:9DAAAOSwSHZWgRuR"
        };

        String[] zalando_nl = {
            //"https://www.zalando.nl/new-balance-wsx90-sneakers-laag-white-ne211a07k-a11.html",
            //"https://www.zalando.nl/calvin-klein-underwear-string-zwart-c1181a00g-q11.html",
            //"https://www.zalando.nl/ecco-outdoorschoenen-black-ec141a05r-q11.html",
            "https://www.zalando.nl/coach-academy-pack-in-signature-rugzak-khaki-coh52n00a-b11.html"
        };

        String[] coolblue_nl = {
            //"https://www.coolblue.nl/product/670047/steelseries-qck-xxl.html",
            //"https://www.coolblue.nl/product/764243/epson-ecotank-et-2650.html",
            "https://www.coolblue.nl/product/831310/bose-quietcomfort-35-ii-limited-edition-rose-goud.html",
            //"https://www.coolblue.nl/product/828805/apple-airpods-2-met-oplaadcase.html#product-alternatives"
        };

        String[] debijenkorf_nl = {
            //"https://www.debijenkorf.nl/adidas-t-shirt-van-katoen-met-logo-opdruk-6859010010-685901001093128?ref=%2Fkindermode%2Fadidas",
            //"https://www.debijenkorf.nl/anna-nina-fijne-ketting-multi-ring-van-zilver-met-gouden-plating-3886090005-388609000500000?ref=%2Fsieraden%2Fanna-nina",
            //"https://www.debijenkorf.nl/rituals-tulip-japanese-yuzu-body-cream-limited-edition-bodycreme-7423090050-742309005000000?ref=%2Fnet-binnen%2Fdamesmode%2Fcosmetica",
            "https://www.debijenkorf.nl/sonos-play-1-wifi-speaker-1944090000-194409000000000?query=fh_location%3D%252F%252Fcatalog01%252Fnl_NL%252Fcadeau_attribuut%253E%257Bcadeaus%257D%252Fcadeau_moment%253E%257Bhousewarming%257D%26country%3DNL%26chl%3D1"
        };

        String[] wehkamp_nl = {
            //"https://www.wehkamp.nl/bottled-eau-de-toilette-50-ml-558907/?CC=C52&SC=VAD&PI=1",
            //"https://www.wehkamp.nl/babyliss-baardtrimmer-16219237/?CC=C29&SC=9IG&PI=1",
            "https://www.wehkamp.nl/lascal-kiddyguard-traphekje-avant-wit-16243750/?CC=C30&SC=8HE&PI=1",
            //"https://www.wehkamp.nl/denver-cau-438-1din-autoradio-zwart-16172809/?CC=C26&SC=6FH&PI=1"
        };

        String[] ebay_au = {
            //"https://www.ebay.com.au/itm/NEW-Avanti-Retro-Bread-Bin-Large-White-Keep-Bread-Pastries-Fresher/283280979556?hash=item41f4dcb664%3Ag%3A6TcAAOSwBZBb%7Eooi&_trkparms=%2526rpp_cid%253D5cec6d6b50d10434324388b5",
            //"https://www.ebay.com.au/itm/Milano-Deluxe-3pc-ABS-Luggage-Suitcase-Luxury-Hard-Case-Shockproof-Travel-Set/253639280508?_trkparms=pageci%3A14e8f2aa-88f0-11e9-a524-74dbd1801e4f%7Cparentrq%3A30b0fb1b16b0a4b7b6eca552fff592ec%7Ciid%3A1",
            "https://www.ebay.com.au/itm/Acclaimed-Exclusive-Yellow-Tail-All-The-Red-Mix-Wine-Case-12-x-750ml-RRP-129/202674359212?_trkparms=pageci%3A14e8f2aa-88f0-11e9-a524-74dbd1801e4f%7Cparentrq%3A30b0fb1b16b0a4b7b6eca552fff592ec%7Ciid%3A1",
            //"https://www.ebay.com.au/itm/NEW-Peter-Thomas-Roth-Max-Anti-Shine-Mattifying-Gel-30ml-Womens-Skin-Care/291875944966?_trkparms=pageci%3A14e8f2aa-88f0-11e9-a524-74dbd1801e4f%7Cparentrq%3A30b0fb1b16b0a4b7b6eca552fff592ec%7Ciid%3A1"
        };

        String[] amazon_au = {
            //"https://www.amazon.com.au/gp/product/B07FTHCPDP/ref=s9_acsd_al_bw_c_x_3_w?pf_rd_m=AMMK0LS9EDNM8&pf_rd_s=merchandised-search-4&pf_rd_r=WVWRBCQT4753FKR4TJQN&pf_rd_t=101&pf_rd_p=5233de58-9307-4731-ace0-52ac77689964&pf_rd_i=5130733051",
            //"https://www.amazon.com.au/dp/B072KMX18S?ref_=Oct_DotdC_5130767051_3&pf_rd_p=3d3d90e5-4547-5a84-a7ca-eeed2830215f&pf_rd_s=merchandised-search-3&pf_rd_t=101&pf_rd_i=5130767051&pf_rd_m=ANEGB3WVEVKZB&pf_rd_r=MC3AXM6FVJXZZ85HWGCC&pf_rd_r=MC3AXM6FVJXZZ85HWGCC&pf_rd_p=3d3d90e5-4547-5a84-a7ca-eeed2830215f",
            "https://www.amazon.com.au/GoPro-Handler-Version-Accessories-Orange/dp/B0755PDKCN?pf_rd_p=57122184-1c13-4448-aa3a-9048d997005b&pf_rd_r=HK1QPZMZQ1NE2FBS256Y",
            //"https://www.amazon.com.au/Milk-Chocolate-Party-Bucket-Packaging/dp/B07G9ZGL9X?pd_rd_w=iOSo7&pf_rd_p=6dd30046-d5ec-40cb-87a8-1ce2e00dae18&pf_rd_r=HK1QPZMZQ1NE2FBS256Y&pd_rd_r=7002a6d9-c146-4251-9fbb-4029d1a536c9&pd_rd_wg=qzFym&ref_=pd_gw_unk",
            //"https://www.amazon.com.au/Ice-Blu-ray-Digital-Copy-Discs/dp/B0776KXY4M?pd_rd_w=vvSyR&pf_rd_p=2d377477-2aa0-46e8-ad79-691cf3dcfbbc&pf_rd_r=HK1QPZMZQ1NE2FBS256Y&pd_rd_r=7002a6d9-c146-4251-9fbb-4029d1a536c9&pd_rd_wg=qzFym&ref_=pd_gw_unk"
        };

        String[] kogan_au = {
            //"https://www.kogan.com/au/buy/lavazza-amm-divinamente-64-pack/",
            //"https://www.kogan.com/au/buy/kogan-32-android-tv-smart-tv-chromecast-3-bundle/",
            "https://www.kogan.com/au/buy/kogan-hd-30-pro-headphone-black/",
            //"https://www.kogan.com/au/buy/exquisite-natural-cow-hide-brindle-170x180cm-premium-rugs/"
        };

        //DOESN'T WORK!!!
        String[] harveynorman_au = {
            //"https://www.harveynorman.com.au/nutri-ninja-pro-blender.html",
            //"https://www.harveynorman.com.au/lenovo-smart-clock.html",
            //"https://www.harveynorman.com.au/norton-security-premium-1-year-for-3-devices-1.html",
            "https://www.harveynorman.com.au/jbl-bar-5-1-soundbar-with-wireless-subwoofer.html"
        };

        String[] bigw_au = {
            //"https://www.bigw.com.au/product/toy-story-4-colour-pen/p/861741/",
            "https://www.bigw.com.au/product/fujifilm-instax-mini-film-10-pack-stained-glass/p/37834/",
            //"https://www.bigw.com.au/product/kambrook-upright-fan-heater-2400w-kfh600/p/886096/",
            //"https://www.bigw.com.au/product/philips-all-in-one-cooker-hd2237-72/p/867367/"
        };

        String[] thegoodguys_au = {
            "https://www.thegoodguys.com.au/asko-60cm-induction-cooktop--hi1611g",
            //"https://www.thegoodguys.com.au/dyson-am09-hotcool-fan-heater-blacknickel-302644-01",
            //"https://www.thegoodguys.com.au/fisher-and-paykel-75kg-front-load-washer-wh7560j3?rrec=true",
            //"https://www.thegoodguys.com.au/tom-tom-start-52-5-inches-gps-3278984"
        };

        String[] appliancesonline_au = {
            "https://www.appliancesonline.com.au/product/ionmax-humidifier-ion90",
            //"https://www.appliancesonline.com.au/product/armando-vicario-palais-kitchen-mixer",
            //"https://www.appliancesonline.com.au/product/philips-ironing-board-gc240",
            //"https://www.appliancesonline.com.au/product/marshall-150531-monitor-wireless-bluetooth-over-ear-headphones-black"
        };

        String[] mediamarkt_de = {
                //"https://www.mediamarkt.de/de/product/_beats-solo-3-wireless-2178424.html",
                "https://www.mediamarkt.de/de/product/_braun-silk%C2%B7expert-pro-5-pl5137-2514623.html",
                //"https://www.mediamarkt.de/de/product/_zhiyun-smooth-4-3-achsen-2487706.html",
                //"https://www.mediamarkt.de/de/product/_panasonic-kx-tg-6622-gb-1385157.html",
                //"https://www.mediamarkt.de/de/product/_iconbit-kick-scooter-ttv2-2438683.html"
        };

        String[] lidl_us = {
            //"https://www.lidl.com/products/285939_A",
            //"https://www.lidl.com/products/311073_A",
            //"https://www.lidl.com/products/310436_C",
            "https://www.lidl.com/products/1031629"
        };
/*
        for (String url: walmart_ca) {
            Link link = new Link(url);
            link.setWebsiteClassName("io.inprice.scrapper.worker.websites.ca.Walmart");
            try {
                Class<Website> clazz = (Class<Website>) Class.forName(link.getWebsiteClassName());
                Constructor<Website> ctor = clazz.getConstructor(Link.class);
                Website website = ctor.newInstance(link);
                website.check();

                printOut(link);
            } catch (Exception e) {
                log.error("Error in converting message from byte array to Link", e);
            }
        }
*/
        printArray(Arrays.asList("Selam", "Kelam", "Melam"));
    }

    private static void printArray(List<String> statuses) {
        System.out.println(statuses);
        System.out.println("'"+ String.join("', '", statuses) + "'");
    }

    private static String getRootDomain(String url) {
        return url.substring(0, url.indexOf("/shop/"));
    }

    private static String getThePage() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept-Language", "en-US,en;q=0.5");

        HttpResponse<String> response = null;
        try {
            response = Unirest.get("https://www.fnac.com/Nav/API/Article/GetStrate?prid=11562346&catalogRef=1&strateType=DoNotMiss")
                    .headers(headers)
                    .header("User-Agent", UserAgents.findARandomUA())
                    .header("Referrer", UserAgents.findARandomReferer())
                    .asString();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return response.getBody();
    }

    private static String findProductId(String url) {
        final String[] urlChunks = url.split("\\|");
        if (urlChunks.length > 0) {
            for (String u: urlChunks) {
                if (u.matches("\\d+") && u.length() > 5) {
                    return u;
                }
            }
        }
        return null;
    }

    public static void getSpecList(String body) {
            final String indicator = "maxOrderQuantity";

        int start = body.indexOf(indicator) + indicator.length() + 2;
        int end = body.indexOf(",", start);

        String maxOrder = body.substring(start, end);
        System.out.println(maxOrder.trim());
    }

    private static void printOut(Link link) {
        log.debug("--------------------------------------------------------------------------------------------------");
        log.debug("SKU   : " + link.getSku());
        log.debug("Name  : " + link.getName());
        log.debug("Status: " + link.getStatus() + (link.getHttpStatus() != null ? " ("+link.getHttpStatus()+")" : ""));
        log.debug("Price : %f, Seller: %s, Shipment: %s, Brand: %s", link.getPrice(), link.getSeller(), link.getShipment(), link.getBrand());
        log.debug("--------------------------------------------------------------------------------------------------");
        if (link.getSpecList() != null && link.getSpecList().size() > 0) {
            link.getSpecList().forEach(spec -> {
                log.debug("  > " + spec.getKey() + " - " + spec.getValue());
            });
        }
    }

    private static void download(String strUrl) {
        URL url;
        InputStream is = null;
        BufferedReader br;
        String line;

        try {
            url = new URL(strUrl);
            is = url.openStream();  // throws an IOException
            br = new BufferedReader(new InputStreamReader(is));

            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (MalformedURLException mue) {
            mue.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException ioe) {
                // nothing to see here
            }
        }
    }

}
