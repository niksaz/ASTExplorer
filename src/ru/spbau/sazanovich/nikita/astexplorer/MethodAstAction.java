package ru.spbau.sazanovich.nikita.astexplorer;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiElement;

public class MethodAstAction extends AnAction {

  public void actionPerformed(AnActionEvent event) {
    Project project = event.getData(PlatformDataKeys.PROJECT);
    PsiElement psiElement = event.getData(LangDataKeys.PSI_ELEMENT);
    StringBuilder stringBuilder = new StringBuilder();
    while (psiElement != null) {
      stringBuilder.append(psiElement.toString());
      stringBuilder.append('\n');
      psiElement = psiElement.getParent();
    }
    Messages.showMessageDialog(
        project,
        stringBuilder.toString(),
        "Information",
        Messages.getInformationIcon());
  }
}