package ru.spbau.sazanovich.nikita.astexplorer;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Strings;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiElement;

public class MethodAstAction extends AnAction {

  private static final int INDENTION_SPACES_PER_LEVEL = 2;

  public void actionPerformed(AnActionEvent event) {
    Project project = event.getData(PlatformDataKeys.PROJECT);
    PsiElement psiElement = event.getData(LangDataKeys.PSI_ELEMENT);
    StringBuilder stringBuilder = new StringBuilder();
    traversePsiElement(psiElement, stringBuilder, 0);
    Messages.showMessageDialog(
        project,
        stringBuilder.toString(),
        "Information",
        Messages.getInformationIcon());
  }

  private static void traversePsiElement(
      PsiElement psiElement,
      StringBuilder stringBuilder,
      int indentionLevel) {
    if (psiElement == null) {
      return;
    }
    if (indentionLevel > 0) {
      stringBuilder.append('\\');
      stringBuilder.append(
          Strings.repeat("-", indentionLevel * INDENTION_SPACES_PER_LEVEL - 1));
    }
    stringBuilder.append(psiElement.toString());
    stringBuilder.append('\n');
    for (PsiElement element : psiElement.getChildren()) {
      traversePsiElement(element, stringBuilder, indentionLevel + 1);
    }
  }
}