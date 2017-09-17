package ru.spbau.sazanovich.nikita.astexplorer;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;

import static com.google.common.truth.Truth.assertThat;

public class MethodAstActionTest extends LightCodeInsightFixtureTestCase {

  private static final String TEST_CLASS = "Hello";

  @Override
  protected String getTestDataPath() {
    return TestUtil.TEST_DATA_DIR_PATH.toString();
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    myFixture.configureByFiles(TEST_CLASS + ".java");
  }

  public void testTraversePsiElement() throws Exception {
    PsiFile file = getFile();
    StringBuilder astSummaryBuilder = new StringBuilder();
    MethodAstAction.traversePsiElement(file, astSummaryBuilder);
    String astSummary = astSummaryBuilder.toString();

    String expectedSummary = TestUtil.readExpectedSummary(TEST_CLASS);
    assertThat(astSummary).isEqualTo(expectedSummary);
  }

  public void testFindParentMethod() throws Exception {
    PsiFile file = getFile();
    // Reference to the PSI element for println at "testdata/Hello.java".
    PsiElement printlnPsiElement = file.findElementAt(79 /* offset */);
    PsiElement methodPsiElement = MethodAstAction.findParentMethod(printlnPsiElement);

    // Reference to the PSI element for main method at "testdata/Hello.java".
    PsiElement mainPsiElement = file.findElementAt(42 /* offset */).getParent();
    assertThat(methodPsiElement).isEqualTo(mainPsiElement);
  }
}