/*
 * Copyright  2019 - present. MEDIA.NET ADVERTISING FZ-LLC
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

package net.media.converters.response30toresponse25;

import net.media.config.Config;
import net.media.converters.Converter;
import net.media.driver.Conversion;
import net.media.exceptions.OpenRtbConverterException;
import net.media.openrtb25.response.nativeresponse.AssetResponse;
import net.media.openrtb25.response.nativeresponse.Link;
import net.media.openrtb25.response.nativeresponse.NativeResponse;
import net.media.openrtb25.response.nativeresponse.NativeResponseBody;
import net.media.openrtb3.Asset;
import net.media.openrtb3.LinkAsset;
import net.media.openrtb3.Native;
import net.media.utils.CommonConstants;
import net.media.utils.Provider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static net.media.utils.ExtUtils.removeFromExt;

public class Native30ToNative10Converter implements Converter<Native, NativeResponse> {

  static List<String> extraFieldsInNativeResponseBodyExt = new ArrayList<>();
  static {
    extraFieldsInNativeResponseBodyExt.add("jsTracker");
    extraFieldsInNativeResponseBodyExt.add("impTrackers");
  }

  public NativeResponse map(Native source, Config config, Provider converterProvider)
      throws OpenRtbConverterException {
    if (isNull(source) || isNull(config)) return null;
    NativeResponse nativeResponse = new NativeResponse();
    NativeResponseBody nativeResponseBody = new NativeResponseBody();
    nativeResponse.setNativeResponseBody(nativeResponseBody);
    enhance(source, nativeResponse, config, converterProvider);
    return nativeResponse;
  }

  public void enhance(
      Native source, NativeResponse target, Config config, Provider converterProvider)
      throws OpenRtbConverterException {

    Converter<Asset, AssetResponse> assetAssetResponseConverter =
        converterProvider.fetch(new Conversion<>(Asset.class, AssetResponse.class));
    Converter<LinkAsset, Link> linkAssetLinkConverter =
        converterProvider.fetch(new Conversion<>(LinkAsset.class, Link.class));

    if (isNull(source) || isNull(target) || isNull(config)) return;

    NativeResponseBody nativeResponseBody = target.getNativeResponseBody();
    List<AssetResponse> assetResponseList = new ArrayList<>();
    if (nonNull(source.getAsset())) {
      for (Asset asset : source.getAsset()) {
        assetResponseList.add(assetAssetResponseConverter.map(asset, config, converterProvider));
      }
    }
    nativeResponseBody.setAsset(assetResponseList);
    nativeResponseBody.setLink(
        linkAssetLinkConverter.map(source.getLink(), config, converterProvider));
    if(isNull(source.getExt()))
      nativeResponseBody.setExt(new HashMap<>());
    else
      nativeResponseBody.setExt(new HashMap<>(source.getExt()));
    try {
      if (nonNull(source.getExt())) {
        nativeResponseBody.setJstracker((String) source.getExt().get(CommonConstants.JS_TRACKER));
        nativeResponseBody.setImptrackers((Collection<String>) source.getExt().get(CommonConstants.IMP_TRACKERS));
      }
    } catch (Exception e) {
      throw new OpenRtbConverterException("error while type casting ext objects in native", e);
    }
    removeFromExt(nativeResponseBody.getExt(), extraFieldsInNativeResponseBodyExt);
    target.setNativeResponseBody(nativeResponseBody);
  }
}
