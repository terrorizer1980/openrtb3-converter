package net.media.openrtb25.request;

import net.media.utils.validator.CheckAtLeastOneNotNull;

import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@CheckAtLeastOneNotNull(fieldNames = {"title", "img", "video", "data"})
public class Asset {

  @NotNull
  private Integer id;

  private Integer required;

  @Valid
  private NativeTitle title;

  @Valid
  private NativeImage img;

  @Valid
  private NativeVideo video;

  @Valid
  private NativeData data;

  private Map<String, Object> ext;

  public Asset() {
  }

  public @NotNull Integer getId() {
    return this.id;
  }

  public Integer getRequired() {
    return this.required;
  }

  public @Valid NativeTitle getTitle() {
    return this.title;
  }

  public @Valid NativeImage getImg() {
    return this.img;
  }

  public @Valid NativeVideo getVideo() {
    return this.video;
  }

  public @Valid NativeData getData() {
    return this.data;
  }

  public Map<String, Object> getExt() {
    return this.ext;
  }

  public void setId(@NotNull Integer id) {
    this.id = id;
  }

  public void setRequired(Integer required) {
    this.required = required;
  }

  public void setTitle(@Valid NativeTitle title) {
    this.title = title;
  }

  public void setImg(@Valid NativeImage img) {
    this.img = img;
  }

  public void setVideo(@Valid NativeVideo video) {
    this.video = video;
  }

  public void setData(@Valid NativeData data) {
    this.data = data;
  }

  public void setExt(Map<String, Object> ext) {
    this.ext = ext;
  }

  public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof Asset)) return false;
    final Asset other = (Asset) o;
    if (!other.canEqual((Object) this)) return false;
    final Object this$id = this.getId();
    final Object other$id = other.getId();
    if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
    final Object this$required = this.getRequired();
    final Object other$required = other.getRequired();
    if (this$required == null ? other$required != null : !this$required.equals(other$required))
      return false;
    final Object this$title = this.getTitle();
    final Object other$title = other.getTitle();
    if (this$title == null ? other$title != null : !this$title.equals(other$title)) return false;
    final Object this$img = this.getImg();
    final Object other$img = other.getImg();
    if (this$img == null ? other$img != null : !this$img.equals(other$img)) return false;
    final Object this$video = this.getVideo();
    final Object other$video = other.getVideo();
    if (this$video == null ? other$video != null : !this$video.equals(other$video)) return false;
    final Object this$data = this.getData();
    final Object other$data = other.getData();
    if (this$data == null ? other$data != null : !this$data.equals(other$data)) return false;
    final Object this$ext = this.getExt();
    final Object other$ext = other.getExt();
    if (this$ext == null ? other$ext != null : !this$ext.equals(other$ext)) return false;
    return true;
  }

  public int hashCode() {
    final int PRIME = 59;
    int result = 1;
    final Object $id = this.getId();
    result = result * PRIME + ($id == null ? 43 : $id.hashCode());
    final Object $required = this.getRequired();
    result = result * PRIME + ($required == null ? 43 : $required.hashCode());
    final Object $title = this.getTitle();
    result = result * PRIME + ($title == null ? 43 : $title.hashCode());
    final Object $img = this.getImg();
    result = result * PRIME + ($img == null ? 43 : $img.hashCode());
    final Object $video = this.getVideo();
    result = result * PRIME + ($video == null ? 43 : $video.hashCode());
    final Object $data = this.getData();
    result = result * PRIME + ($data == null ? 43 : $data.hashCode());
    final Object $ext = this.getExt();
    result = result * PRIME + ($ext == null ? 43 : $ext.hashCode());
    return result;
  }

  protected boolean canEqual(Object other) {
    return other instanceof Asset;
  }

  public String toString() {
    return "net.media.openrtb25.request.Asset(id=" + this.getId() + ", required=" + this.getRequired() + ", title=" + this.getTitle() + ", img=" + this.getImg() + ", video=" + this.getVideo() + ", data=" + this.getData() + ", ext=" + this.getExt() + ")";
  }
}