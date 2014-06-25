package com.yuvalshavit.util;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class CommonPackageFinderTest {
  @Test
  public void basic() {
    CommonPackageFinder finder = new CommonPackageFinder();
    assertEquals(finder.get(), null);

    finder.accept("one.two.three.four.ClassA");
    assertEquals(finder.get(), "one.two.three.four.");

    finder.accept("one.two.three.four.ClassB");
    assertEquals(finder.get(), "one.two.three.four.");

    finder.accept(null);
    assertEquals(finder.get(), "one.two.three.four.");

    finder.accept("one.two.three.four.five.ClassC");
    assertEquals(finder.get(), "one.two.three.four.");

    finder.accept("one.two.three.ClassD");
    assertEquals(finder.get(), "one.two.three.");

    finder.accept("one.two.trice.ClassE");
    assertEquals(finder.get(), "one.two.");

    finder.accept("six");
    assertEquals(finder.get(), "");
  }
}