package io.inprice.scrapper.worker.websites;

import io.inprice.scrapper.common.logging.Logger;
import io.inprice.scrapper.common.models.Link;

public abstract class AbstractSpasite extends AbstractSite {

    protected static final Logger log = new Logger(AbstractSpasite.class);

    protected AbstractSpasite(Link link) {
        super(link);
    }
}
