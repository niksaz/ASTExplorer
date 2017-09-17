package ru.spbau.sazanovich.nikita.astexplorer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestUtil {

  /**
   * Since Intellij IDEA CE's path should be specified as working directory, we construct relative
   * path from there. We assume that Intellij IDEA CE and ASTExplorer projects have the same parent
   * directory.
   */
  public static final Path TEST_DATA_DIR_PATH =
      Paths.get(System.getProperty("user.dir"))
          .getParent()
          .resolve("ASTExplorer")
          .resolve("testdata");

  public static String readExpectedSummary(String className) throws IOException {
    Path summaryPath = TEST_DATA_DIR_PATH.resolve(className + ".txt");
    return new String(Files.readAllBytes(summaryPath));
  }

  private TestUtil() {}
}
