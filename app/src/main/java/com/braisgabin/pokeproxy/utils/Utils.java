package com.braisgabin.pokeproxy.utils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public abstract class Utils {
  public static byte[] file2byteArray(File file) {
    try {
      final RandomAccessFile f = new RandomAccessFile(file, "r");
      final byte[] bytes = new byte[(int) f.length()];
      f.readFully(bytes);
      return bytes;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
