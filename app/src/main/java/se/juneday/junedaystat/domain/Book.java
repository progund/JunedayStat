package se.juneday.junedaystat.domain;

import android.util.Log;
import java.security.acl.LastOwnerException;
import java.util.ArrayList;
import java.util.List;

public class Book {

  private List<Chapter> chapters;
  private String name;

  public Book(String name, List<Chapter> chapters) {
    this.name = name;
    this.chapters = chapters;
  }

  public List<Chapter> chapters() {
    return chapters;
  }

  public String name() {
    return name;
  }

  public int pages() {
    int sum=0;
    for (Chapter c : chapters) {
      sum += c.pages();
    }
    return sum;
  }


}
