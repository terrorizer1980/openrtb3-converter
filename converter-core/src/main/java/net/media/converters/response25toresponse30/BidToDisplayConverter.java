package net.media.converters.response25toresponse30;

import net.media.OpenRtbConverterException;
import net.media.config.Config;
import net.media.converters.Converter;
import net.media.enums.AdType;
import net.media.openrtb25.response.Bid;
import net.media.openrtb25.response.nativeresponse.NativeResponse;
import net.media.openrtb3.Banner;
import net.media.openrtb3.Display;
import net.media.openrtb3.Event;
import net.media.openrtb3.Native;
import net.media.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Objects.nonNull;

/**
 * @author shiva.b
 */
public class BidToDisplayConverter implements Converter<Bid, Display> {

  private Converter<NativeResponse, Native> nativeResponseNativeConverter;

  public BidToDisplayConverter(Converter<NativeResponse, Native> nativeResponseNativeConverter) {
    this.nativeResponseNativeConverter = nativeResponseNativeConverter;
  }

  @Override
  public Display map(Bid source, Config config)throws OpenRtbConverterException {
    if (source == null) {
      return null;
    }
    Display display = new Display();
    enhance(source, display, config);
    return display;
  }

  @Override
  public void enhance(Bid source, Display target, Config config) throws OpenRtbConverterException{
    if (source == null || target == null) {
      return;
    }
    target.setH( source.getH() );
    target.setWratio( source.getWratio() );
    target.setW( source.getW() );
    target.setHratio( source.getHratio() );
    if(nonNull(source.getApi()))
      target.setApi(new ArrayList<>(source.getApi()));
    target.setCurl(source.getNurl());
    if (config.getAdType() == AdType.NATIVE) {
      if (source.getAdm() instanceof String) {
        try {
          NativeResponse nativeResponse = Utils.getMapper().readValue((String) source.getAdm(),
            NativeResponse.class);
          Native _native = nativeResponseNativeConverter.map(nativeResponse, config);
          target.setAdm(_native);
        } catch (IOException e) {
          throw new OpenRtbConverterException("error while deserializing native response object", e);
        }
      } else {
        try {
          NativeResponse nativeResponse = Utils.getMapper().convertValue(source.getAdm(),NativeResponse.class);
          Native _native = nativeResponseNativeConverter.map((NativeResponse) source.getAdm(), config);
          target.setAdm(_native);
        }
        catch (Exception e) {
          throw new OpenRtbConverterException("error while casting adm to native response", e);
        }
      }
    }
    else if (config.getAdType() == AdType.BANNER) {
      target.setAdm(source.getAdm());
    }
    if (nonNull(source.getExt())) {
      try {
        Map<String, Object> ext = source.getExt();
        target.setCtype((Integer) ext.get("ctype"));
        target.setPriv((String) ext.get("priv"));
        target.setMime((String) ext.get("mime"));

        if (config.getAdType() == AdType.BANNER) {
          target.setBanner(Banner.HashMapToBanner((Map<String, Object>) ext.get("banner")));
        } else if (config.getAdType() == AdType.NATIVE) {
          target.set_native((net.media.openrtb3.Native) ext.get("native"));
        }
        target.setEvent((List<Event>) ext.get(ext.get("event")));
      }
      catch (Exception e) {
        throw new OpenRtbConverterException("error while casting contents of bid.ext", e);
      }
    }
  }
}
