package com.promptwise.promptchain.common.util.fileio;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public final class FileIoUtil {

  public static final Path SYSTEM_PROPERTY__JAVA_HOME = Path.of(System.getProperty("java.home"));
  public static final Path SYSTEM_PROPERTY__USER_HOME = Path.of(System.getProperty("user.home"));
  public static final Path SYSTEM_PROPERTY__USER_DIR = Path.of(System.getProperty("user.dir"));
  public static final Path SYSTEM_PROPERTY__USER_NAME = Path.of(System.getProperty("user.name"));
  public static final Path SYSTEM_PROPERTY__JAVA_IO_TMPDIR = Path.of(System.getProperty("java.io.tmpdir"));

  /**
   * The default character-set of the JVM. Used to convert text-files to binary streams and vice versa.
   */
  public static final Charset DEFAULT_CHARSET = Charset.defaultCharset();

  public static final String NEWLINE_CHARACTER = System.lineSeparator();
  public static final String FILE_SEPARATOR = FileSystems.getDefault().getSeparator();
  public static final String LINE_SEPARATOR = System.lineSeparator();
  public static final String TAB_CHARACTER = "\t";


  private static final Logger LOGGER = LoggerFactory.getLogger(FileIoUtil.class);


  private FileIoUtil() {
  }

  public static void unzipFileToDirectory(File zipFile, File destinationDirectory) throws IOException {
    try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile))) {
      byte[] buffer = new byte[1024 * 1024 * 1024];
      ZipEntry zipEntry = zis.getNextEntry();
      while (zipEntry != null) {
        if (zipEntry.isDirectory()) {
          File file = new File(destinationDirectory, zipEntry.getName());
          file.mkdir();
        } else {
          File destinationFile = new File(destinationDirectory, zipEntry.getName());
          try (FileOutputStream fos = new FileOutputStream(destinationFile)) {
            int len = zis.read(buffer);
            while (len > 0) {
              fos.write(buffer, 0, len);
              len = zis.read(buffer);
            }
          }
        }
        zipEntry = zis.getNextEntry();
      }
    }
  }

  public static Path getPathRelativeToAnAncestorDirectory(File file, int numberOfAncestorDirectoriesToIncludeInTheResult) {
    Assert.notNull(file, "The parameter 'file' cannot be null!");
    return getPathRelativeToAnAncestorDirectory(file.toPath(), numberOfAncestorDirectoriesToIncludeInTheResult);
  }

  public static Path getPathRelativeToAnAncestorDirectory(Path path, int numberOfAncestorDirectoriesToIncludeInTheResult) {
    Assert.notNull(path, "The parameter 'path' cannot be null!");
    Assert.isTrue(numberOfAncestorDirectoriesToIncludeInTheResult >= 0
                    && numberOfAncestorDirectoriesToIncludeInTheResult <= Integer.MAX_VALUE,
            "The parameter 'file' cannot be null!");
    //-- Subtracting one so as not to include the file itself but also the ancestor directories till root directory.
    int totalNumberOfDirectoriesInTheAncestorChain = path.getNameCount() - 1;
    if (numberOfAncestorDirectoriesToIncludeInTheResult > totalNumberOfDirectoriesInTheAncestorChain) {
      throw new IllegalArgumentException("Unable to relativize the path: '" + path + "' such that it includes '" +
              numberOfAncestorDirectoriesToIncludeInTheResult + "' directories from its ancestor chain! " +
              "The ancestor chain of the path has only '" + totalNumberOfDirectoriesInTheAncestorChain + " directories.");
    } else {
      Path ancestorPathToRelativizeAgainst = path.getParent();
      for (int i = 0; i < numberOfAncestorDirectoriesToIncludeInTheResult; i++) {
        ancestorPathToRelativizeAgainst = ancestorPathToRelativizeAgainst.getParent();
      }
      Path relativePath = ancestorPathToRelativizeAgainst.relativize(path);
      return relativePath;
    }
  }

  public static String removeCharactersThatAreIncompatibleWithFilenames(String str) {
    String strThatCanBeUsedAsFilename = str.replaceAll("[/:*?\"<>|]", "");
    return strThatCanBeUsedAsFilename;
  }

  public static Map<String, String> loadOptionalPropertiesFromTextFileResource(String textFileResourcePath) throws IOException {
    return loadOptionalPropertiesFromTextFileResource(textFileResourcePath, DEFAULT_CHARSET);
  }

  public static Map<String, String> loadOptionalPropertiesFromTextFileResource(String textFileResourcePath, Charset charset)
          throws IOException {
    try {
      return loadPropertiesFromTextFileResource(textFileResourcePath, charset);
    } catch (FileNotFoundException fnfe) {
      LOGGER.warn("", fnfe);
      return null;
    }
  }

  public static Map<String, String> loadPropertiesFromTextFileResource(String textFileResourcePath) throws IOException {
    return loadPropertiesFromTextFileResource(textFileResourcePath, DEFAULT_CHARSET);
  }

  public static Map<String, String> loadPropertiesFromTextFileResource(String textFileResourcePath, Charset charset) throws IOException {
    String propertyFileContents = loadTextFileResourceToString(textFileResourcePath, charset);
    return loadPropertiesFromString(propertyFileContents);
  }

  public static Map<String, String> loadOptionalPropertiesFromTextFile(File propertyFile) throws IOException {
    return loadOptionalPropertiesFromTextFile(propertyFile, DEFAULT_CHARSET);
  }

  public static Map<String, String> loadOptionalPropertiesFromTextFile(File propertyFile, Charset charset) throws IOException {
    try {
      return loadPropertiesFromTextFile(propertyFile, charset);
    } catch (FileNotFoundException fnfe) {
      LOGGER.warn("", fnfe);
      return null;
    }
  }

  public static Map<String, String> loadPropertiesFromTextFile(File propertiesFile) throws IOException {
    return loadPropertiesFromTextFile(propertiesFile, DEFAULT_CHARSET);
  }

  public static Map<String, String> loadPropertiesFromTextFile(File propertiesFile, Charset charset) throws IOException {
    String propertiesAsString = loadTextFileToString(propertiesFile, charset);
    return loadPropertiesFromString(propertiesAsString);
  }

  public static String loadOptionalTextFileResourceToString(String textFileResourcePath) throws IOException {
    return loadOptionalTextFileResourceToString(textFileResourcePath, DEFAULT_CHARSET);
  }

  public static String loadOptionalTextFileResourceToString(String textFileResourcePath, Charset charset) throws IOException {
    try {
      return loadTextFileResourceToString(textFileResourcePath, charset);
    } catch (FileNotFoundException fnfe) {
      //-- Ignore exception.
      return null;
    }
  }

  public static String loadTextFileResourceToString(String textFileResourcePath) throws IOException {
    return loadTextFileResourceToString(textFileResourcePath, DEFAULT_CHARSET);
  }

  public static String loadTextFileResourceToString(String textFileResourcePath, Charset charset) throws IOException {
    InputStream inputStream = ClassLoader.getSystemResourceAsStream(textFileResourcePath);
    if (inputStream == null) {
      throw new FileNotFoundException(textFileResourcePath);
    }
    try {
      return loadInputStreamToString(inputStream, charset);
    } finally {
      try {
        inputStream.close();
      } catch (Exception e) {
        //-- Ignore exception.
      }
    }
  }

  public static String loadOptionalTextFileToString(File textFile) throws IOException {
    return loadOptionalTextFileToString(textFile, DEFAULT_CHARSET);
  }

  public static String loadOptionalTextFileToString(File textFile, Charset charset) throws IOException {
    try {
      return loadTextFileToString(textFile, charset);
    } catch (FileNotFoundException fnfe) {
      //-- Ignore exception.
      return null;
    }
  }

  public static String loadTextFileToString(File textFile) throws IOException {
    return loadTextFileToString(textFile, DEFAULT_CHARSET);
  }

  public static String loadTextFileToString(File textFile, Charset charset) throws IOException {
    try (InputStream inputStream = new FileInputStream(textFile)) {
      return loadInputStreamToString(inputStream, charset);
    }
  }

  public static void writeStringToTextFile(String fileContentsToWrite, File outputFile) throws IOException {
    writeStringToTextFile(fileContentsToWrite, outputFile, Charset.defaultCharset());
  }

  public static void writeStringToTextFile(String fileContentsToWrite, File outputFile, Charset charset) throws IOException {
    try (FileOutputStream fos = new FileOutputStream(outputFile)) {
      IOUtils.write(fileContentsToWrite, fos, charset);
    }
  }

  private static Map<String, String> loadPropertiesFromString(String propertiesAsString) throws IOException {
    Properties properties = new Properties();
    properties.load(new StringReader(propertiesAsString));
    Map<String, String> propertiesMap = new HashMap<>();
    properties.forEach((key, value) -> {
      propertiesMap.put((String) key, (String) value);
    });
    return propertiesMap;
  }

  private static String loadInputStreamToString(InputStream inputStream, Charset charset) throws IOException {
    return IOUtils.toString(inputStream, charset);
  }

  public static void recreateDir(Path directory) throws IOException {
    Assert.notNull(directory, "The parameter 'directory' cannot be null!");
    LOGGER.info("Recreating dir: {} by deleting it recursively and creating it again", directory.normalize());
    if (Files.exists(directory)) {
      LOGGER.info("Dir exists so deleting it recursively");
      deleteDirRecursivelyIfItExists(directory);
      LOGGER.info("Deleted the dir");
    }
    Files.createDirectory(directory);
    LOGGER.info("Successfully recreated the dir");
  }

  public static TempDirectory createRealUniqueTempDirectory(Path parentDir, String prefix) throws IOException {
    Path realUniqueDir = FileIoUtil.createRealUniqueTempDir(parentDir, prefix);
    return new TempDirectory(realUniqueDir);
  }

  public static Path createRealUniqueTempDir(Path parentDir, String prefix) throws IOException {
    Path rawParentDirWithExistenceGuarantee = Files.createDirectories(parentDir);
    Path tempDir = Files.createTempDirectory(rawParentDirWithExistenceGuarantee, prefix);
    Path realTempDir = tempDir.toRealPath();
    return realTempDir;
  }

  public static void deleteDirRecursivelyIfItExists(Path dirToDelete) throws IOException {
    if (Files.exists(dirToDelete)) {
      List<Path> pathsToDelete = getMatchingPathsInDir(dirToDelete, true, null,
              FILE_OR_DIRECTORY_MATCHING_OPTION.MATCH_BOTH, true);
      for (Path path : pathsToDelete) {
        try {
          Files.delete(path);
        } catch (NoSuchFileException e) {
          //-- This catch and ignore addresses an edge case where:
          //-- 1. A temp-file is created somewhere inside dirToDelete by an async process invoked by this thread
          //-- 2. The invoked process ends and this thread tries to delete the dirToDelete.
          //-- 3. The temp-file created in step 1 is listed in pathsToDelete
          //-- 4. The temp-file is removed by the OS because it was a temp file and the owning process has ended.
          //-- 5. This method (deleteDirRecursivelyIfItExists) tries to delete it because it appeared in the pathsToDelete
          //-- 6. This method fails with java.nio.file.NoSuchFileException
          //-- File already gone (NFS .nfs* cleanup or race condition) → ignore
        }
      }
      try {
        Files.delete(dirToDelete);
      } catch (NoSuchFileException e) {
        //-- See above. Same logic for dir itself
      }
    }
  }

  public static List<Path> getMatchingPathsInDir(
          Path dirToSearch,
          boolean searchRecursively,
          Pattern matchPattern,
          FILE_OR_DIRECTORY_MATCHING_OPTION fileOrDirectoryMatchingOption,
          boolean reverseSortResult) throws IOException {

    Assert.notNull(dirToSearch, "The parameter 'dirToSearch' cannot be null!");
    if (!Files.exists(dirToSearch)) {
      throw new FileNotFoundException("The directory: '" + dirToSearch + "' does not exist!");
    }

    Path realDirToSearch = dirToSearch.toRealPath();
    List<Path> matchingPaths = new ArrayList<>();
    List<RuntimeException> suppressedExceptions = new ArrayList<>();

    Files.walkFileTree(realDirToSearch, EnumSet.noneOf(FileVisitOption.class),
            searchRecursively ? Integer.MAX_VALUE : 1,
            new SimpleFileVisitor<>() {

              @Override
              public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                try {
                  String fileName = file.getFileName() != null ? file.getFileName().toString() : "";
                  if (!fileName.startsWith(".nfs") &&
                          (matchPattern == null || matchPattern.matcher(fileName).matches()) &&
                          fileOrDirectoryMatchingOption.isApplicableTo(file)) {
                    matchingPaths.add(file);
                  }
                } catch (RuntimeException r) {
                  suppressedExceptions.add(r);
                }
                return FileVisitResult.CONTINUE;
              }

              @Override
              public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                try {
                  if (!dir.equals(realDirToSearch)) { // skip root dir if desired
                    String dirName = dir.getFileName() != null ? dir.getFileName().toString() : "";
                    if (!dirName.startsWith(".nfs") &&
                            (matchPattern == null || matchPattern.matcher(dirName).matches()) &&
                            fileOrDirectoryMatchingOption.isApplicableTo(dir)) {
                      matchingPaths.add(dir);
                    }
                  }
                } catch (RuntimeException r) {
                  suppressedExceptions.add(r);
                }
                return FileVisitResult.CONTINUE;
              }

              @Override
              public FileVisitResult visitFileFailed(Path file, IOException exc) {
                // Ignore transient .nfs files, otherwise wrap in RuntimeException
                if (file.getFileName() != null && file.getFileName().toString().startsWith(".nfs")) {
                  return FileVisitResult.CONTINUE;
                }
                suppressedExceptions.add(new RuntimeException(exc));
                return FileVisitResult.CONTINUE;
              }
            });

    if (suppressedExceptions.isEmpty()) {
      if (reverseSortResult) {
        matchingPaths.sort(Comparator.reverseOrder());
      }
      return matchingPaths;
    } else {
      Throwable lastThrowable = suppressedExceptions.remove(suppressedExceptions.size() - 1);
      suppressedExceptions.forEach(lastThrowable::addSuppressed);
      throw new IOException(
              "An exception occurred while searching for matching files in the specified dir",
              lastThrowable);
    }
  }

  public enum FILE_OR_DIRECTORY_MATCHING_OPTION {
    MATCH_FILES_ONLY, MATCH_DIRECTORIES_ONLY, MATCH_BOTH;

    public boolean isApplicableTo(Path path) {
      return switch (this) {
        case MATCH_FILES_ONLY -> Files.isRegularFile(path);
        case MATCH_DIRECTORIES_ONLY -> Files.isDirectory(path);
        case MATCH_BOTH -> true;
      };
    }
  }

  public static Path createDirectories(Path path) throws IOException {
    Path createdPath = Files.createDirectories(path);
    Path realPath = toRealPath(createdPath, true);
    return realPath;
  }

  public static Path toRealPath(Path path, boolean checkForWriteAccess) throws IOException {
    if (Files.exists(path)) {
      if (checkForWriteAccess && !Files.isWritable(path)) {
        throw new IOException(String.format("The current process does not have write access to the path: %s!", path));
      } else {
        Path realPath = path.toRealPath();
        return realPath;
      }
    } else {
      throw new IOException(String.format("The path: %s does not exist!", path));
    }
  }

}
