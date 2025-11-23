package com.promptwise.promptchain.common.util.fileio;

import com.promptwise.promptchain.common.util.LongRange;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class TextFileChunkingUtil {

  public static final char NEW_LINE_CHAR = '\n';
  public static final ByteOrder NATIVE_BYTE_ORDER = ByteOrder.nativeOrder();

  private TextFileChunkingUtil() {
  }

  //TODO: We can maybe make an impl that does not need maxLineLengthInBytes and just cacls line lengths when needed.
  //TODO: (we have to bc this is impl is for the case when user wants the chunks to end at a delimiter (new line
  //TODO: in this case). However this calc should be very fast, also we have to make use of the reusableLineBuffer
  //TODO: bc the call that makes it is very heavy. Only if these can be addressed then we should get rid of
  //TODO: maxLineLengthInBytes. The benefit we would get is that the todos below regarding robustness can be addressed
  //TODO: in a better way.

  //TODO: maxChunkSizeInBytes should have a reasonable limit e.g. 5 MB
  public static SortedSet<LongRange> createNewlineBoundedChunksOfTextualDataFileChannel(File textualDataFile,
                                                                                        int maxLineLengthInBytes,
                                                                                        int maxChunkSizeInBytes)
          throws IOException {
    return createNewlineBoundedChunksOfTextualDataFileChannel(textualDataFile.getAbsolutePath(),
            maxLineLengthInBytes, maxChunkSizeInBytes);
  }

  public static SortedSet<LongRange> createNewlineBoundedChunksOfTextualDataFileChannel(String textualDataFilepath,
                                                                                        int maxLineLengthInBytes,
                                                                                        int maxChunkSizeInBytes)
          throws IOException {
    try (FileChannel fileChannel = FileChannel.open(Paths.get(textualDataFilepath), StandardOpenOption.READ)) {
      System.out.println("Creating chunks started at " + LocalDateTime.now());

      long fileSizeInBytes = fileChannel.size();
      boolean hasPartialTailChunk = (fileSizeInBytes % maxChunkSizeInBytes) > 0;
      int numberOfChunksInFile = (int) (fileSizeInBytes / maxChunkSizeInBytes) + (hasPartialTailChunk ? 1 : 0);

      List<LongRange> longRanges = new ArrayList<>(numberOfChunksInFile);
      ByteBuffer reusableLineBuffer = createDirectByteBufferWithNativeByteOrder(maxLineLengthInBytes);
      long fcPositionOfChunkStart = 0;
      fileChannel.position(fcPositionOfChunkStart);
      //TODO-Robustness: dependency on numberOfChunksInFile should be removed and fileSizeInBytes and
      //TODO-Robustness: maxChunkSizeInBytes should directly be used if possible.
      for (int i = 0; i < numberOfChunksInFile; i++) {
        long fcPositionOfNextChunkStart = Math.min(fileSizeInBytes, fcPositionOfChunkStart + maxChunkSizeInBytes);
        //-- Except in case of last chunk, the lines might split between two neighbouring chunks so we need to adjust
        //-- the chunk size such that it always ends at a newline byte.
        long fcPositionOfLastNewlineByteInChunk = (i < numberOfChunksInFile - 1)
                ? findPositionOfLastNewlineByteInChunk(fileChannel, maxLineLengthInBytes, fcPositionOfNextChunkStart,
                reusableLineBuffer) : fileSizeInBytes - 1;
        LongRange longRange = new LongRange(fcPositionOfChunkStart, fcPositionOfLastNewlineByteInChunk);
        longRanges.add(longRange);
        //-- The next chunk will begin at the position right after the last fcPositionOfLastNewlineByteInChunk.
        fcPositionOfChunkStart = fcPositionOfLastNewlineByteInChunk + 1;
      }
      validateOrderedContiguousFiniteIntegerRanges(longRanges, maxChunkSizeInBytes, fileSizeInBytes);
      System.out.println("Creating chunks ended at " + LocalDateTime.now());
      return new TreeSet<>(longRanges);
    }
  }

  private static ByteBuffer createDirectByteBufferWithNativeByteOrder(int size) {
    ByteBuffer bb = ByteBuffer.allocateDirect(size);
    bb.order(NATIVE_BYTE_ORDER);
    return bb;
  }

  public static void validateOrderedContiguousFiniteIntegerRanges(List<LongRange> longRanges,
                                                                  long maxRangeSizeAllowed,
                                                                  long endValueOfLastRange) {
    long prevRangeEnd = -1;
    int chunkNumber = 0;
    for (LongRange longRange : longRanges) {
      if (longRange.getStart() != prevRangeEnd + 1) {
        throw new RuntimeException(String.format(
                "'start': %d of chunk number: '%d' is not equal to 'end': '%d' of previous chunk.",
                longRange.getStart(), chunkNumber, prevRangeEnd));
      } else if (longRange.rangeSize() > maxRangeSizeAllowed) {
        throw new RuntimeException(String.format(
                "Size of chunk number '%d': '%d' is more than maxChunkSizeInBytes: '%d'",
                chunkNumber, longRange.rangeSize(), maxRangeSizeAllowed));
      } else if (chunkNumber == longRanges.size() - 1 && longRange.getEnd() != endValueOfLastRange - 1) {
        throw new RuntimeException(String.format(
                "'end': '%d' of last chunk number: '%d' is not equal to fileSizeInBytes: '%d' - 1",
                longRange.getEnd(), chunkNumber, endValueOfLastRange));
      }
      prevRangeEnd = longRange.getEnd();
    }
  }

  private static long findPositionOfLastNewlineByteInChunk(FileChannel fileChannel, int maxLineLengthInBytes,
                                                           long fcPositionOfNextChunkStart, ByteBuffer reusableLineBuffer)
          throws IOException {
    long minimumPossibleFcPositionOfStartOfLastLineInChunk = Math.max(0, fcPositionOfNextChunkStart - maxLineLengthInBytes);
    reusableLineBuffer.rewind();
    //-- Read the last line of maximum possible length which guarantees that we will find at least one newline byte
    //-- (does not matter if actual last line is shorter because we only want to find the last newline byte).
    //TODO-Robustness: Should handle the case if last line of the currently processed chunk does not contain
    //TODO-Robustness: a newline (bad data etc.)
    fileChannel.read(reusableLineBuffer, minimumPossibleFcPositionOfStartOfLastLineInChunk);
    int i = maxLineLengthInBytes - 1;
    while (reusableLineBuffer.get(i) != NEW_LINE_CHAR) {
      i--;
    }
    return minimumPossibleFcPositionOfStartOfLastLineInChunk + i;
  }

}
