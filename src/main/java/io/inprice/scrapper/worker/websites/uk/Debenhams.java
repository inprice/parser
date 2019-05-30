package io.inprice.scrapper.worker.websites.uk;

import io.inprice.scrapper.common.models.Link;
import io.inprice.scrapper.worker.websites.generic.GenericWebsiteT1;

/**
 * Parser for Debenhams UK
 *
 * Please refer GenericWebsiteT1 for extra information
 *
 * @author mdpinar
 */
public class Debenhams extends GenericWebsiteT1 {

    public Debenhams(Link link) {
        super(link, "Debenhams");
    }

}
