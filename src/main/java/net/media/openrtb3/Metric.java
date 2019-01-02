package net.media.openrtb3;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by shiva.b on 14/12/18.
 */

@Getter
@Setter
public class Metric {
  private String type;
  private Double value;
  private String vendor;
  private Map<String, Object> ext;
}
