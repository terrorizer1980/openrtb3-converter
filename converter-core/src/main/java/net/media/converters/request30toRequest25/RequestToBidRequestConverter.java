package net.media.converters.request30toRequest25;

import net.media.exceptions.OpenRtbConverterException;
import net.media.config.Config;
import net.media.converters.Converter;
import net.media.openrtb25.request.BidRequest;
import net.media.openrtb25.request.Imp;
import net.media.openrtb25.request.User;
import net.media.openrtb3.App;
import net.media.openrtb3.Device;
import net.media.openrtb3.Item;
import net.media.openrtb3.Regs;
import net.media.openrtb3.Request;
import net.media.openrtb3.Site;
import net.media.openrtb3.Source;
import net.media.utils.CollectionUtils;
import net.media.utils.CollectionToCollectionConverter;
import net.media.utils.Utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Objects.nonNull;

public class RequestToBidRequestConverter implements Converter<Request, BidRequest> {

  private Converter<net.media.openrtb3.User, User> userUserConverter;
  private Converter<Request, User> requestUserConverter;
  private Converter<App, net.media.openrtb25.request.App> appAppConverter;
  private Converter<Regs, net.media.openrtb25.request.Regs> regsRegsConverter;
  private Converter<Site, net.media.openrtb25.request.Site> siteSiteConverter;
  private Converter<Device, net.media.openrtb25.request.Device> deviceDeviceConverter;
  private Converter<Source, net.media.openrtb25.request.Source> sourceSourceConverter;
  private Converter<Item, net.media.openrtb25.request.Imp> itemImpConverter;

  @java.beans.ConstructorProperties({"userUserConverter", "requestUserConverter", "appAppConverter", "regsRegsConverter", "siteSiteConverter", "deviceDeviceConverter", "sourceSourceConverter", "itemImpConverter"})
  public RequestToBidRequestConverter(Converter<net.media.openrtb3.User, User> userUserConverter, Converter<Request, User> requestUserConverter, Converter<App, net.media.openrtb25.request.App> appAppConverter, Converter<Regs, net.media.openrtb25.request.Regs> regsRegsConverter, Converter<Site, net.media.openrtb25.request.Site> siteSiteConverter, Converter<Device, net.media.openrtb25.request.Device> deviceDeviceConverter, Converter<Source, net.media.openrtb25.request.Source> sourceSourceConverter, Converter<Item, Imp> itemImpConverter) {
    this.userUserConverter = userUserConverter;
    this.requestUserConverter = requestUserConverter;
    this.appAppConverter = appAppConverter;
    this.regsRegsConverter = regsRegsConverter;
    this.siteSiteConverter = siteSiteConverter;
    this.deviceDeviceConverter = deviceDeviceConverter;
    this.sourceSourceConverter = sourceSourceConverter;
    this.itemImpConverter = itemImpConverter;
  }

  @Override
  public BidRequest map(Request source, Config config) throws OpenRtbConverterException {
    if ( source == null ) {
      return null;
    }

    BidRequest bidRequest = new BidRequest();

    enhance( source, bidRequest, config );

    return bidRequest;
  }

  @Override
  public void enhance(Request source, BidRequest target, Config config) throws OpenRtbConverterException {
    if(source == null)
      return;

    if(source.getContext() != null) {

      if (source.getContext().getUser() != null) {
        if (target.getUser() == null) {
          target.setUser(userUserConverter.map(source.getContext().getUser(), config));
        }
      } else {
        target.setUser(null);
      }
      if (source.getCdata() != null) {
        if (target.getUser() == null) {
          target.setUser(new User());
        }
        requestUserConverter.enhance(source, target.getUser(), config);
      }

      App app = source.getContext().getApp();
      if ( app != null ) {
        target.setApp( appAppConverter.map( app, config ) );
      }

      Regs regs = source.getContext().getRegs();
      if ( regs != null ) {
        target.setRegs( regsRegsConverter.map( regs, config ) );
      }

      Site site = source.getContext().getSite();
      if ( site != null ) {
        target.setSite( siteSiteConverter.map( site, config ) );
      }

      if(source.getContext().getRestrictions() != null) {
        target.setBapp( Utils.copyCollection(source.getContext().getRestrictions().getBapp(), config) );
        target.setBcat( Utils.copyCollection(source.getContext().getRestrictions().getBcat(), config) );
        target.setBadv( Utils.copyCollection(source.getContext().getRestrictions().getBadv(), config) );
        if (source.getContext().getRestrictions().getCattax() != null) {
          if (target.getExt() == null)
            target.setExt(new HashMap<>());
          target.getExt().put("cattax", source.getContext().getRestrictions().getCattax());
        }

        if (source.getContext().getRestrictions().getExt() != null) {
          if (target.getExt() == null)
            target.setExt(new HashMap<>());
          target.getExt().put("restrictionsExt", source.getContext().getRestrictions().getExt());
        }
      }

      Device device = source.getContext().getDevice();
      if ( device != null ) {
        target.setDevice( deviceDeviceConverter.map( device, config ) );
      }
    }
    Map<String, Object> map = source.getExt();
    if ( map != null ) {
      target.setExt( new HashMap<String, Object>( map ) );
    }
    target.setAllimps( source.getPack() );
    target.setImp( CollectionToCollectionConverter.convert( source.getItem(), itemImpConverter, config ) );
    if (!CollectionUtils.isEmpty(target.getImp())) {
      if (nonNull(source.getContext()) && nonNull(source.getContext().getRestrictions())) {
        for (Imp imp : target.getImp()) {
          if (nonNull(imp.getBanner())) {
            if (nonNull(source.getContext().getRestrictions().getBattr())) {
              imp.getBanner().setBattr(Utils.copyCollection(source.getContext().getRestrictions().getBattr
                (), config));
            }
          }
          if (nonNull(imp.getVideo())) {
            if (nonNull(source.getContext().getRestrictions().getBattr())) {
              imp.getVideo().setBattr(Utils.copyCollection(source.getContext().getRestrictions().getBattr
                (), config));
            }
          }
          if (nonNull(imp.getNat())) {
            if (nonNull(source.getContext().getRestrictions().getBattr())) {
              imp.getNat().setBattr(Utils.copyCollection(source.getContext().getRestrictions().getBattr
                (), config));
            }
          }
        }
      }
    }
    target.setId( source.getId() );
    target.setAt( source.getAt() );
    target.setTest( source.getTest() );
    target.setTmax( source.getTmax() );
    target.setSource( sourceSourceConverter.map( source.getSource(), config ) );
    Collection<String> list1 = source.getCur();
    if ( list1 != null ) {
      target.setCur( Utils.copyCollection( list1, config ) );
    }

    if(source.getWseat() != null) {

      if (source.getWseat() == 0) {
        target.setBseat(Utils.copyCollection(source.getSeat(), config));
        target.setBseat(Utils.copyCollection(source.getSeat(), config));
      } else {
        target.setWseat(Utils.copyCollection(source.getSeat(), config));
      }
    }

    if(source.getItem() != null && source.getItem().size() > 0) {
      Collection<String> wlang = new HashSet<>();
      for(Item item : source.getItem()) {
        if(item.getSpec() != null && item.getSpec().getPlacement() != null && item.getSpec()
          .getPlacement().getWlang() != null) {
          wlang.addAll(item.getSpec().getPlacement().getWlang());
        }
      }
      target.setWlang(Utils.copyCollection(wlang, config));
    }

    if(target.getImp() != null) {
      for (Imp imp : target.getImp()) {
        if (imp.getBanner() != null)
          imp.getBanner().setBattr(source.getContext().getRestrictions().getBattr());
        if (imp.getVideo() != null)
          imp.getVideo().setBattr(source.getContext().getRestrictions().getBattr());
        if (imp.getNat() != null)
          imp.getNat().setBattr(source.getContext().getRestrictions().getBattr());
      }
    }

    if(source.getContext().getDooh() == null)
      return;

    if(target.getExt() == null)
      target.setExt(new HashMap<>());
    target.getExt().put("dooh", source.getContext().getDooh());
  }

}
