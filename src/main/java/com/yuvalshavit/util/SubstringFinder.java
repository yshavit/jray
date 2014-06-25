package com.yuvalshavit.util;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;

public class SubstringFinder implements Consumer<String> {

  public static <T> String get(Collection<? extends T> strings, Function<? super T, String> toString) {
    SubstringFinder finder = new SubstringFinder();
    strings.stream().map(toString).forEach(finder);
    return finder.get();

  }

  private String s;

  @Override
  public void accept(String string) {
    if (string == null) {
      return;
    }
    if (s == null) {
      s = string;
    } else {
      int oldStrLen = s.length();
      int incomingStringLen = string.length();
      int end = 0;
      for (int max = Math.min(incomingStringLen, oldStrLen); end < max; ++end) {
        if (string.charAt(end) != s.charAt(end)) {
          break;
        }
      }
      if (oldStrLen != end) {
        if (incomingStringLen == end) {
          s = string;
        } else {
          s = string.substring(0, end);
        }
      }
    }
  }

  public String get() {
    return s;
  }
}
