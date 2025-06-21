package com.promptwise.promptchain.common.util.fileio;

import java.io.IOException;
import java.nio.file.Path;

public class TempDirectory implements AutoCloseable {
  private final Path dir;

  TempDirectory(Path dir) {
    this.dir = dir;
  }


  public Path getDir() {
    return dir;
  }

  @Override
  public void close() throws IOException {
    FileIoUtil.deleteDirRecursivelyIfItExists(getDir());
  }
}
