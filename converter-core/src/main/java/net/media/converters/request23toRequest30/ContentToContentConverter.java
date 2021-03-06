/*
 * Copyright  2019 - present. IAB Tech Lab
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.media.converters.request23toRequest30;

import com.fasterxml.jackson.databind.JavaType;
import net.media.config.Config;
import net.media.exceptions.OpenRtbConverterException;
import net.media.openrtb25.request.Content;
import net.media.openrtb25.request.Data;
import net.media.utils.CommonConstants;
import net.media.utils.JacksonObjectMapperUtils;
import net.media.utils.Provider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static net.media.utils.ExtUtils.fetchFromExt;
import static net.media.utils.ExtUtils.removeFromExt;

/** Created by rajat.go on 03/04/19. */
public class ContentToContentConverter
    extends net.media.converters.request25toRequest30.ContentToContentConverter {

  private static final List<String> extraFieldsInExt = new ArrayList<>();
  private static final JavaType javaTypeForDataCollection =
    JacksonObjectMapperUtils.getMapper()
      .getTypeFactory()
      .constructCollectionType(Collection.class, Data.class);

  static {
    extraFieldsInExt.add(CommonConstants.ARTIST);
    extraFieldsInExt.add(CommonConstants.GENRE);
    extraFieldsInExt.add(CommonConstants.ALBUM);
    extraFieldsInExt.add(CommonConstants.ISRC);
    extraFieldsInExt.add(CommonConstants.PRODQ);
    extraFieldsInExt.add(CommonConstants.DATA);
  }

  public void enhance(
      Content source, net.media.openrtb3.Content target, Config config, Provider converterProvider)
      throws OpenRtbConverterException {
    if (source == null || target == null) {
      return;
    }
    fetchFromExt(
      source::setArtist,
      source.getExt(),
      CommonConstants.ARTIST,
      "Error in setting artist from content.ext.artist");
    fetchFromExt(
      source::setGenre,
      source.getExt(),
      CommonConstants.GENRE,
      "Error in setting genre from content.ext.genre");
    fetchFromExt(
      source::setAlbum,
      source.getExt(),
      CommonConstants.ALBUM,
      "Error in setting album from content.ext.album");
    fetchFromExt(
      source::setIsrc,
      source.getExt(),
      CommonConstants.ISRC,
      "Error in setting isrc from content.ext.isrc");
    fetchFromExt(
      source::setProdq,
      source.getExt(),
      CommonConstants.PRODQ,
      "Error in setting prodq from content.ext.prodq");
    fetchFromExt(
      source::setData,
      source.getExt(),
      CommonConstants.DATA,
      "Error in setting data from content.ext.data",
      javaTypeForDataCollection);
    super.enhance(source, target, config, converterProvider);
    removeFromExt(target.getExt(), extraFieldsInExt);
  }
}
