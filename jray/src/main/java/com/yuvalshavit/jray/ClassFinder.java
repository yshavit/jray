package com.yuvalshavit.jray;

import org.objectweb.asm.ClassReader;

import java.io.IOException;
import java.util.function.Consumer;

public interface ClassFinder {
  void forEach(Consumer<ClassReader> consumer) throws IOException;
}
