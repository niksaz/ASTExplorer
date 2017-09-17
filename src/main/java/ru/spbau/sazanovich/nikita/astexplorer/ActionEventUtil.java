package ru.spbau.sazanovich.nikita.astexplorer;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;

class ActionEventUtil {

  static Project getProjectFor(AnActionEvent event) {
    return event.getData(PlatformDataKeys.PROJECT);
  }

  static Editor getEditorFor(AnActionEvent event) {
    return event.getData(PlatformDataKeys.EDITOR);
  }

  static PsiFile getPsiFileFor(AnActionEvent event) {
    return event.getData(LangDataKeys.PSI_FILE);
  }

  private ActionEventUtil() {}
}
