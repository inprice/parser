package io.inprice.parser.consumer;

import io.inprice.common.config.SysProps;

/**
 * Handles TOBE_CLASSIFIED and RENEWED competitors
 */
public class TobeClassifiedConsumer extends BaseCompetitorConsumer {

  public TobeClassifiedConsumer() {
    super("TOBE_CLASSIFIED competitors consumer", SysProps.MQ_TOBE_CLASSIFIED_COMPETITORS_QUEUE());
  }

}
