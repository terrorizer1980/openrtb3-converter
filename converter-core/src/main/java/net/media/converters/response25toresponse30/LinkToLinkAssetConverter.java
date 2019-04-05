package net.media.converters.response25toresponse30;

import net.media.driver.Conversion;
import net.media.exceptions.OpenRtbConverterException;
import net.media.config.Config;
import net.media.converters.Converter;
import net.media.openrtb25.response.nativeresponse.Link;
import net.media.openrtb3.LinkAsset;
import net.media.utils.Provider;

/**
 * @author shiva.b
 */
public class LinkToLinkAssetConverter implements Converter<Link, LinkAsset> {

  @Override
  public LinkAsset map(Link source, Config config, Provider converterProvider) throws OpenRtbConverterException {
    if (source == null) {
      return null;
    }
    LinkAsset linkAsset = new LinkAsset();
    enhance(source, linkAsset, config, converterProvider);
    return linkAsset;
  }

  @Override
  public void enhance(Link source, LinkAsset target, Config config, Provider converterProvider)throws OpenRtbConverterException {
    if (source == null || target == null) {
      return;
    }
    target.setUrl(source.getUrl());
    target.setUrlfb(source.getFallback());
    target.setTrkr(source.getClicktrackers());
    target.setExt(source.getExt());
  }
}