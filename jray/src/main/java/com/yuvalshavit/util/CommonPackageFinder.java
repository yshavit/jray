package com.yuvalshavit.util;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;

public class CommonPackageFinder implements Consumer<String> {

  public static <T> String get(Collection<? extends T> strings, Function<? super T, String> toString) {
    CommonPackageFinder finder = new CommonPackageFinder();
    strings.stream().map(toString).forEach(finder);
    return finder.get();

  }

  private String s;

  @Override
  public void accept(String string) {
    if (string == null) {
      return;
    }
    int lastDotIncoming = string.lastIndexOf('.');
    if (lastDotIncoming < 0) {
      s = "";
      return;
    }
    string = string.substring(0, lastDotIncoming + 1);
    if (s == null) {
      s = string;
    } else {
      int oldStrLen = s.length();
      int incomingStringLen = string.length();
      int lastDot = 0;
      for (int i = 0, max = Math.min(incomingStringLen, oldStrLen); i < max; ++i) {
        if (string.charAt(i) != s.charAt(i)) {
          break;
        }
        if (string.charAt(i) == '.') {
          lastDot = i;
        }
      }
      int lastDotIdx = lastDot + 1;
      if (oldStrLen != lastDotIdx) {

        if (incomingStringLen == lastDotIdx) {
          s = string;
        } else {
          s = string.substring(0, lastDotIdx);
        }
      }
    }
  }

  public String get() {
    return s;
  }
}
