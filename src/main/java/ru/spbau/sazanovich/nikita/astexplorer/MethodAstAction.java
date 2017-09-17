package ru.spbau.sazanovich.nikita.astexplorer;

import com.google.common.base.Strings;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.WindowWrapper;
import com.intellij.openapi.ui.WindowWrapperBuilder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.PsiMethodImpl;
import com.intellij.ui.components.JBScrollPane;
import org.jetbrains.annotations.Nullable;

import javax.swing.JComponent;
import javax.swing.JTextPane;

public class MethodAstAction extends AnAction {

  private static final int INDENTION_SPACES_PER_LEVEL = 2;

  public void actionPerformed(AnActionEvent event) {
    Project project = ActionEventUtil.getProjectFor(event);
    Editor editor = ActionEventUtil.getEditorFor(event);
    if (editor == null) {
      showInfoMessage("Editor is not selected", project);
      return;
    }
    PsiFile psiFile = ActionEventUtil.getPsiFileFor(event);
    if (psiFile == null) {
      showInfoMessage("No enclosing file found", project);
      return;
    }
    PsiElement psiElement = psiFile.findElementAt(editor.getCaretModel().getOffset());
    if (psiElement == null) {
      showInfoMessage("No PSI for the element found", project);
      return;
    }
    PsiElement methodPsiElement = findParentMethod(psiElement);
    if (methodPsiElement == null) {
      showInfoMessage("Current PSI element is not a part of any method", project);
      return;
    }
    StringBuilder astSummaryBuilder = new StringBuilder();
    traversePsiElement(methodPsiElement, astSummaryBuilder, 0);
    showDialogWithAst(astSummaryBuilder.toString(), project);
  }

  /** Finds a PSI element that represents a method by checking element's parents. */
  @Nullable
  private static PsiElement findParentMethod(PsiElement psiElement) {
    if (psiElement == null) {
      return null;
    }
    return psiElement instanceof PsiMethodImpl
        ? psiElement
        : findParentMethod(psiElement.getParent());
  }

  /**
   * Recursively iterates over element's children and populating {@link StringBuilder} with info
   * about traversed elements.
   */
  private static void traversePsiElement(
      PsiElement psiElement,
      StringBuilder astSummaryBuilder,
      int indentionLevel) {
    astSummaryBuilder
        .append(Strings.repeat("-", indentionLevel * INDENTION_SPACES_PER_LEVEL));
    astSummaryBuilder.append(psiElement.toString());
    astSummaryBuilder.append('\n');
    for (PsiElement element : psiElement.getChildren()) {
      traversePsiElement(element, astSummaryBuilder, indentionLevel + 1);
    }
  }

  private static void showInfoMessage(String message, Project project) {
    Messages.showMessageDialog(
        project,
        message,
        "Information",
        Messages.getInformationIcon());
  }

  private static void showDialogWithAst(String astSummary, Project project) {
    WindowWrapper wrapperDialog =
        new WindowWrapperBuilder(WindowWrapper.Mode.MODAL, packTextIntoJComponent(astSummary))
            .setProject(project)
            .setTitle("AST of the current method")
            .build();
    wrapperDialog.show();
  }

  private static JComponent packTextIntoJComponent(String text) {
    JTextPane textPane = new JTextPane();
    textPane.setText(text);
    textPane.setCaretPosition(0);
    return new JBScrollPane(textPane);
  }
}
