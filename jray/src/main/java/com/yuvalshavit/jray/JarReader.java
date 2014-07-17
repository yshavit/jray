package com.yuvalshavit.jray;

import org.objectweb.asm.ClassReader;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.function.Consumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class JarReader implements ClassFinder {
  private final File zipFile;

  public JarReader(File zipFile) {
    this.zipFile = zipFile;
  }

  @Override
  public void forEach(Consumer<ClassReader> classReaderConsumer) throws IOException {
    try (FileInputStream fileStreamRaw = new FileInputStream(zipFile);
         BufferedInputStream fileStream = new BufferedInputStream(fileStreamRaw);
         ZipInputStream zipStream = new ZipInputStream(fileStream))
    {
      for (ZipEntry entry = zipStream.getNextEntry(); entry != null; entry = zipStream.getNextEntry()) {
        if (entry.getName().endsWith(".class")) {
          ClassReader classReader = new ClassReader(zipStream);
          classReaderConsumer.accept(classReader);
        }
      }
    }
  }
}
