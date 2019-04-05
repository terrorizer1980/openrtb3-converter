package net.media.converters.request25toRequest30;

import net.media.driver.Conversion;
import net.media.exceptions.OpenRtbConverterException;
import net.media.config.Config;
import net.media.converters.Converter;
import net.media.openrtb25.request.BidRequest;
import net.media.openrtb25.request.Imp;
import net.media.openrtb25.request.Source;
import net.media.openrtb25.request.User;
import net.media.openrtb3.Context;
import net.media.openrtb3.Dooh;
import net.media.openrtb3.Item;
import net.media.openrtb3.Placement;
import net.media.openrtb3.Request;
import net.media.openrtb3.Spec;
import net.media.utils.CollectionUtils;
import net.media.utils.CollectionToCollectionConverter;
import net.media.utils.Provider;
import net.media.utils.Utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rajat.go on 03/01/19.
 */
public class BidRequestToRequestConverter implements Converter<BidRequest, Request> {

  private String bidRequestUserCustomdata(BidRequest bidRequest) {
    if ( bidRequest == null ) {
      return null;
    }
    User user = bidRequest.getUser();
    if ( user == null ) {
      return null;
    }
    String customdata = user.getCustomdata();
    if ( customdata == null ) {
      return null;
    }
    return customdata;
  }

  @Override
  public Request map(BidRequest source, Config config, Provider converterProvider) throws OpenRtbConverterException {
    if ( source == null ) {
      return null;
    }

    Request request = new Request();

    enhance( source, request, config, converterProvider );

    return request;
  }

  @Override
  public void enhance(BidRequest source, Request target, Config config, Provider converterProvider) throws OpenRtbConverterException {
    if(source == null || target == null) {
      return;
    }
    Converter<BidRequest, Context> bidRequestContextConverter = converterProvider.fetch(new Conversion<>
            (BidRequest.class, Context.class));
    Converter<Imp, Item> impItemConverter = converterProvider.fetch(new Conversion<>
            (Imp.class, Item.class));
    target.setContext( bidRequestContextConverter.map( source, config, converterProvider ) );
    target.setItem( CollectionToCollectionConverter.convert( source.getImp(), impItemConverter,
      config, converterProvider ) );
    target.setPack( source.getAllimps() );
    String customdata = bidRequestUserCustomdata( source );
    if ( customdata != null ) {
      target.setCdata( customdata );
    }
    target.setId( source.getId() );
    target.setTest( source.getTest() );
    target.setTmax( source.getTmax() );
    target.setAt( source.getAt() );
    target.setCur(Utils.copyCollection(source.getCur(), config));
    Converter<Source, net.media.openrtb3.Source> source25Source3Converter = converterProvider.fetch(new Conversion<>
            (Source.class, net.media.openrtb3.Source.class));
    target.setSource( source25Source3Converter.map(source.source, config, converterProvider ));
    Map<String, Object> map = source.getExt();
    if ( map != null ) {
      target.setExt( new HashMap<String, Object>( map ) );
    }

    if (!CollectionUtils.isEmpty(target.getItem())) {
      for (Item item : target.getItem()) {
        if ( item.getSpec() == null ) {
          item.setSpec( new Spec() );
        }
        bidRequestToSpec( source, item.getSpec(), config );
      }
    }
    if (source.getWseat()!=null && source.getWseat().size()>0){
      target.setWseat(1);
      target.setSeat(Utils.copyCollection(source.getWseat(), config));
    } else {
      target.setWseat(0);
      target.setSeat(Utils.copyCollection(source.getBseat(), config));
    }
    if(target.getExt() == null)
      return;
    target.getExt().remove("cattax");
    target.getExt().remove("restrictions");
    if(source.getExt() == null)
      return;
    if(source.getExt().containsKey("dooh")) {
      if(target.getContext() == null)
        target.setContext(new Context());
      try {
        Dooh dooh = (Dooh) source.getExt().get("dooh");
        target.getContext().setDooh(dooh);
      } catch (ClassCastException e) {
        throw new OpenRtbConverterException("error while typecasting ext for BidRequest", e);
      }
    }
  }

  private void bidRequestToSpec(BidRequest bidRequest, Spec mappingTarget, Config config) {
    if ( bidRequest == null ) {
      return;
    }

    if ( mappingTarget.getPlacement() == null ) {
      mappingTarget.setPlacement( new Placement() );
    }
    bidRequestToPlacement( bidRequest, mappingTarget.getPlacement(), config );
  }

  private void bidRequestToPlacement(BidRequest bidRequest, Placement mappingTarget, Config config) {
    if ( bidRequest == null ) {
      return;
    }

    mappingTarget.setWlang( Utils.copyCollection(bidRequest.getWlang(), config) );
  }
}