/*
 * Copyright 2013-2014 Sergey Ignatov, Alexander Zolotov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.goide;

import com.goide.highlighting.GoSyntaxHighlightingColors;
import com.goide.psi.*;
import com.goide.psi.impl.GoPsiImplUtil;
import com.goide.psi.impl.GoReference;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.containers.ContainerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GoAnnotator implements Annotator {
  @Override
  public void annotate(@NotNull PsiElement o, @NotNull AnnotationHolder holder) {
    if (o instanceof GoImportSpec) {
      if (((GoImportSpec)o).getDot() != null) {
        o.putUserData(GoReference.IMPORT_USERS, ContainerUtil.<PsiElement>newArrayListWithCapacity(0));
      }
    }
    else if (o instanceof GoReferenceExpression) {
      PsiElement resolve = ((GoReferenceExpression)o).getReference().resolve();
      highlightRefIfNeeded((GoReferenceExpression)o, resolve, holder);      
    }
    else if (o instanceof GoTypeReferenceExpression) {
      PsiElement resolve = ((GoTypeReferenceExpression)o).getReference().resolve();
      highlightRefIfNeeded((GoTypeReferenceExpression)o, resolve, holder);
    }
    else if (o instanceof GoTypeSpec) {
      setHighlighting(((GoTypeSpec)o).getIdentifier(), holder, GoSyntaxHighlightingColors.TYPE_SPECIFICATION);
    }
    else if (o instanceof GoConstDefinition) {
      setHighlighting(((GoConstDefinition)o).getIdentifier(), holder, GoSyntaxHighlightingColors.CONSTANT);
    }
    else if (o instanceof GoVarDefinition && isGlobal((GoVarDefinition)o)) {
      setHighlighting(((GoVarDefinition)o).getIdentifier(), holder, GoSyntaxHighlightingColors.GLOBAL_VARIABLE);
    }
    else if (o instanceof GoNamedSignatureOwner) {
      PsiElement identifier = ((GoNamedSignatureOwner)o).getIdentifier();
      if (identifier != null) {
        setHighlighting(identifier, holder, GoSyntaxHighlightingColors.FUNCTION_DECLARATION);
      }
    }
  }

  private static boolean isGlobal(@NotNull GoVarDefinition o) {
    GoVarDeclaration declaration = PsiTreeUtil.getParentOfType(o, GoVarDeclaration.class);
    return declaration != null && declaration.getParent() instanceof GoFile;
  }

  private static void highlightRefIfNeeded(@NotNull GoReferenceExpressionBase o,
                                           @Nullable PsiElement resolve,
                                           @NotNull AnnotationHolder holder) {
    if (resolve instanceof GoTypeSpec) {
      TextAttributesKey key = GoPsiImplUtil.builtin(resolve)
                              ? GoSyntaxHighlightingColors.BUILTIN_TYPE_REFERENCE
                              : GoSyntaxHighlightingColors.TYPE_REFERENCE;
      setHighlighting(o.getIdentifier(), holder, key);
    }
    else if (resolve instanceof GoConstDefinition) {
      setHighlighting(o.getIdentifier(), holder, GoSyntaxHighlightingColors.CONSTANT);
    }
    else if (resolve instanceof GoVarDefinition && isGlobal((GoVarDefinition)resolve)) {
      setHighlighting(o.getIdentifier(), holder, GoSyntaxHighlightingColors.GLOBAL_VARIABLE);
    }
  }

  private static void setHighlighting(@NotNull PsiElement element, @NotNull AnnotationHolder holder, @NotNull TextAttributesKey key) {
    holder.createInfoAnnotation(element, null).setEnforcedTextAttributes(TextAttributes.ERASE_MARKER);
    TextAttributes attributes = EditorColorsManager.getInstance().getGlobalScheme().getAttributes(key);
    holder.createInfoAnnotation(element, null).setEnforcedTextAttributes(attributes);
  }
}
