package net.media.converters.response30toresponse24;

import net.media.config.Config;
import net.media.converters.Converter;
import net.media.openrtb24.response.Bid;
import net.media.openrtb3.Audit;
import net.media.openrtb3.Display;

import java.util.HashMap;

import static java.util.Objects.isNull;

public class AuditToBidConverter implements Converter<Audit,Bid> {
  public AuditToBidConverter(){

  }
  public Bid map(Audit source, Config config){
    if(isNull(source) || isNull(config))
      return  null;
    Bid  bid = new Bid();
    inhance(source,bid,config);
    return bid;
  }

  public  void inhance(Audit source, Bid target, Config config){
    if(isNull(source) || isNull(target) || isNull(config))
      return ;
    if(isNull(target.getExt())){
      target.setExt(new HashMap<>());
    }
    target.getExt().put("status",source.getStatus());
    target.getExt().put("feedback",source.getFeedback());
    target.getExt().put("init",source.getInit());
    target.getExt().put("lastmod",source.getLastmod());
    target.getExt().put("corr",source.getCorr());
    target.getExt().putAll(source.getExt());
  }
}
