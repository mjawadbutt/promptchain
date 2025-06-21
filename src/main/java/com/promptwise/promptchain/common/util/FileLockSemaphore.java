package com.promptwise.promptchain.common.util;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

public class FileLockSemaphore {

  private final Path lockDir;
  private final int maxSlots;

  public FileLockSemaphore(Path lockDir, int maxSlots) throws IOException {
    this.lockDir = lockDir;
    this.maxSlots = maxSlots;
    Files.createDirectories(lockDir);
  }

  /**
   * Throwing version. Uses tryAcquire internally.
   */
  public LockHandle acquireOrThrow(Duration timeout) throws IOException, InterruptedException, TimeoutException {
    Optional<LockHandle> optionalLock = tryAcquire(timeout);
    if (optionalLock.isPresent()) {
      return optionalLock.get();
    } else {
      throw new TimeoutException("Failed to acquire lock within " + timeout.toMillis() + " ms.");
    }
  }

  /**
   * Non-throwing version. Try to acquire a lock, retrying until timeout.
   */
  public Optional<LockHandle> tryAcquire(Duration timeout) throws IOException, InterruptedException {
    Instant deadline = Instant.now().plus(timeout);
    while (Instant.now().isBefore(deadline)) {
      for (int i = 0; i < getMaxSlots(); i++) {
        Path lockFile = getLockDir().resolve("im-process-lock-" + i + ".lock");
        //-- Ensure file exists for locking
        if (Files.notExists(lockFile)) {
          try {
            Files.createFile(lockFile);
          } catch (FileAlreadyExistsException ignored) {
            //-- Someone else created it, ignore
          }
        }
        try {
          RandomAccessFile raf = new RandomAccessFile(lockFile.toFile(), "rw");
          FileChannel channel = raf.getChannel();
          try {
            FileLock fileLock = channel.tryLock();
            if (fileLock != null) {
              return Optional.of(new LockHandle(channel, fileLock, lockFile));
            } else {
              raf.close();
            }
          } catch (OverlappingFileLockException e) {
            //-- Possibly locked by this JVM process; skip and try next slot
            raf.close();
          }
        } catch (IOException e) {
          //-- Possibly locked by another process; skip and try next slot
        }
      }
      //-- No lock acquired; wait a bit before retrying
      Thread.sleep(100);
    }
    return Optional.empty();
  }

  public Path getLockDir() {
    return lockDir;
  }

  public int getMaxSlots() {
    return maxSlots;
  }

  public static class LockHandle implements AutoCloseable {
    private final FileChannel channel;
    private final FileLock lock;
    private final Path filePath;

    LockHandle(FileChannel channel, FileLock lock, Path filePath) {
      this.channel = channel;
      this.lock = lock;
      this.filePath = filePath;
    }

    @Override
    public void close() throws IOException {
      if (lock != null && lock.isValid()) {
        lock.release();
      }
      if (channel != null && channel.isOpen()) {
        channel.close();
      }
    }

    public Path getFilePath() {
      return filePath;
    }
  }

}
