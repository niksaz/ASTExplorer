package ru.spbau.sazanovich.nikita.astexplorer;

import com.google.common.base.Strings;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.PsiMethodImpl;
import org.jetbrains.annotations.Nullable;

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
    showInfoMessage(astSummaryBuilder.toString(), project);
  }

  @Nullable
  private static PsiElement findParentMethod(PsiElement psiElement) {
    if (psiElement == null) {
      return null;
    }
    return psiElement instanceof PsiMethodImpl
        ? psiElement
        : findParentMethod(psiElement.getParent());
  }

  private static void traversePsiElement(
      PsiElement psiElement,
      StringBuilder astSummaryBuilder,
      int indentionLevel) {
    if (psiElement == null) {
      return;
    }
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
}
