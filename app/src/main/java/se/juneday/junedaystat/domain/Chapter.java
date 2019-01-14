package se.juneday.junedaystat.domain;

import java.util.ArrayList;
import java.util.List;

public class Chapter {

  private String name;
  private int pages;
  private List<String> channelUrls;
  private List<String> videoUrls;
  private List<Presentation> presentations;

  public Chapter(String name, int pages, List<String> channelUrls,
      List<String> videoUrls,
      List<Presentation> presentations) {
    this.name = name;
    this.pages = pages;
    this.channelUrls = channelUrls;
    this.videoUrls = videoUrls;
    this.presentations = presentations;
  }

  public Chapter(String name, int pages) {
    this.name = name;
    this.pages = pages;
    channelUrls = new ArrayList<>();
    videoUrls = new ArrayList<>();
    presentations = new ArrayList<>();
  }

  public void addChannelUrl(String channel) {
    channelUrls.add(channel);
  }

  public void addVideoUrl(String video) {
    videoUrls.add(video);
  }

  public void addPresentation(Presentation presentation) {
    presentations.add(presentation);
  }

  public int pages() {
    return pages;
  }

  public int presentationPages() {
    int sum = 0;
    for (Presentation p : presentations) {
      sum += p.pages();
    }
    return sum;
  }

  @Override
  public String toString() {
    return "Chapter{" +
        "name='" + name + '\'' +
        ", pages=" + pages +
        ", channelUrls=" + channelUrls +
        ", videoUrls=" + videoUrls +
        ", presentations=" + presentations +
        '}';
  }


  public String name() {
    return name;
  }


}