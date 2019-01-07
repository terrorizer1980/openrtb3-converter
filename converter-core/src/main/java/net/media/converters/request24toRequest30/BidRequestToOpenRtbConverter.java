package net.media.converters.request24toRequest30;

import net.media.OpenRtbConverterException;
import net.media.config.Config;
import net.media.converters.Converter;
import net.media.openrtb24.request.BidRequest;
import net.media.openrtb3.OpenRTB;
import net.media.openrtb3.Request;

import static java.util.Objects.isNull;

/**
 * Created by rajat.go on 03/01/19.
 */
public class BidRequestToOpenRtbConverter implements Converter<BidRequest, OpenRTB> {

  Converter<BidRequest, Request> bidRequestRequestConverter;

  public BidRequestToOpenRtbConverter(Converter<BidRequest, Request> bidRequestRequestConverter) {
    this.bidRequestRequestConverter = bidRequestRequestConverter;
  }

  @Override
  public OpenRTB map(BidRequest source, Config config) throws OpenRtbConverterException {
    if (isNull(source)) {
      return null;
    }

    OpenRTB openRTB = new OpenRTB();
    inhance(source, openRTB, config);

    return openRTB;
  }

  @Override
  public void inhance(BidRequest source, OpenRTB target, Config config) throws OpenRtbConverterException {
    if (isNull(source) || isNull(target)) {
      return;
    }
    target.setRequest(bidRequestRequestConverter.map(source, config));
  }
}
