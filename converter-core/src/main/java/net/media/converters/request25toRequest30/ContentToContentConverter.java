package net.media.converters.request25toRequest30;

import net.media.exceptions.OpenRtbConverterException;
import net.media.config.Config;
import net.media.converters.Converter;
import net.media.openrtb25.request.Content;
import net.media.openrtb25.request.Producer;
import net.media.openrtb3.Data;
import net.media.utils.CollectionToCollectionConverter;
import net.media.utils.Utils;

import java.util.HashMap;
import java.util.Map;

import static net.media.utils.CommonConstants.DEFAULT_CATTAX_TWODOTX;

public class ContentToContentConverter implements Converter<Content, net.media.openrtb3.Content> {

  private Converter<Producer, net.media.openrtb3.Producer> producerProducerConverter;
  private Converter<net.media.openrtb25.request.Data, Data> dataDataConverter;

  @java.beans.ConstructorProperties({"producerProducerConverter", "dataDataConverter"})
  public ContentToContentConverter(Converter<Producer, net.media.openrtb3.Producer> producerProducerConverter, Converter<net.media.openrtb25.request.Data, Data> dataDataConverter) {
    this.producerProducerConverter = producerProducerConverter;
    this.dataDataConverter = dataDataConverter;
  }

  @Override
  public net.media.openrtb3.Content map(Content source, Config config) throws OpenRtbConverterException {
    if ( source == null ) {
      return null;
    }

    net.media.openrtb3.Content content1 = new net.media.openrtb3.Content();

    enhance( source, content1, config );

    return content1;
  }

  @Override
  public void enhance(Content source, net.media.openrtb3.Content target, Config config) throws OpenRtbConverterException {
    if(source == null)
      return;
    target.setMrating( source.getQagmediarating() );
    target.setSrcrel( source.getSourcerelationship() );
    target.setRating( source.getContentrating() );
    target.setEmbed( source.getEmbeddable() );
    target.setUrating( source.getUserrating() );
    target.setLang( source.getLanguage() );
    target.setLive( source.getLivestream() );
    target.setId( source.getId() );
    target.setEpisode( source.getEpisode() );
    target.setTitle( source.getTitle() );
    target.setSeries( source.getSeries() );
    target.setSeason( source.getSeason() );
    target.setArtist( source.getArtist() );
    target.setGenre( source.getGenre() );
    target.setAlbum( source.getAlbum() );
    target.setIsrc( source.getIsrc() );
    target.setUrl( source.getUrl() );
    target.setCat( Utils.copyCollection(source.getCat(), config) );
    target.setProdq( source.getProdq() );
    target.setContext( source.getContext() );
    target.setKeywords( source.getKeywords() );
    target.setLen( source.getLen() );
    target.setProducer( producerProducerConverter.map( source.getProducer(), config ) );
    target.setData( CollectionToCollectionConverter.convert( source.getData(), dataDataConverter, config ) );
    Map<String, Object> map = source.getExt();
    if ( map != null ) {
      target.setExt( Utils.copyMap(map, config) );
    }

    if(source.getExt() == null)
      return;
    try {
      if (source.getExt().containsKey("cattax")) {
        target.setCattax((Integer) source.getExt().get("cattax"));
      } else {
        target.setCattax(DEFAULT_CATTAX_TWODOTX);
      }
      target.getExt().remove("cattax");
    } catch (ClassCastException e) {
      throw new OpenRtbConverterException("error while typecasting ext for Content", e);
    }
    if(source.getVideoquality() != null) {
      if(target.getExt() == null)
        target.setExt(new HashMap<>());
      target.getExt().put("videoquality", source.getVideoquality());
    }
  }
}
