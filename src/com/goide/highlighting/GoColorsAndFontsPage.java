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

package com.goide.highlighting;

import com.goide.GoFileType;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;
import com.intellij.util.containers.ContainerUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.Map;

import static com.goide.highlighting.GoSyntaxHighlightingColors.*;

public class GoColorsAndFontsPage implements ColorSettingsPage {
  private static final AttributesDescriptor[] DESCRIPTORS = new AttributesDescriptor[]{
    new AttributesDescriptor("Line comment", LINE_COMMENT),
    new AttributesDescriptor("Block comment", BLOCK_COMMENT),
    new AttributesDescriptor("Keyword", KEYWORD),
    new AttributesDescriptor("Identifier", IDENTIFIER),
    new AttributesDescriptor("String", STRING),
    new AttributesDescriptor("Number", NUMBER),
    new AttributesDescriptor("Semicolon", SEMICOLON),
    new AttributesDescriptor("Colon", COLON),
    new AttributesDescriptor("Comma", COMMA),
    new AttributesDescriptor("Dot", DOT),
    new AttributesDescriptor("Operator", OPERATOR),
    new AttributesDescriptor("Brackets", BRACKETS),
    new AttributesDescriptor("Braces", BRACES),
    new AttributesDescriptor("Parentheses", PARENTHESES),
    new AttributesDescriptor("Bad character", BAD_CHARACTER),
    new AttributesDescriptor("Type specification", TYPE_SPECIFICATION),
    new AttributesDescriptor("Type reference", TYPE_REFERENCE),
    new AttributesDescriptor("Builtin type", BUILTIN_TYPE_REFERENCE),
    new AttributesDescriptor("Constant", CONSTANT),
    new AttributesDescriptor("Function declaration", FUNCTION_DECLARATION),
    new AttributesDescriptor("Global variable", GLOBAL_VARIABLE),
  };
  private static final Map<String, TextAttributesKey> ATTRIBUTES_KEY_MAP = ContainerUtil.newTroveMap();
  static {
    ATTRIBUTES_KEY_MAP.put("tr", TYPE_REFERENCE);
    ATTRIBUTES_KEY_MAP.put("ts", TYPE_SPECIFICATION);
    ATTRIBUTES_KEY_MAP.put("bt", BUILTIN_TYPE_REFERENCE);
    ATTRIBUTES_KEY_MAP.put("c", CONSTANT);
    ATTRIBUTES_KEY_MAP.put("f", FUNCTION_DECLARATION);
    ATTRIBUTES_KEY_MAP.put("g", GLOBAL_VARIABLE);
  }

  @NotNull
  public String getDisplayName() {
    return GoFileType.INSTANCE.getName();
  }

  public Icon getIcon() {
    return GoFileType.INSTANCE.getIcon();
  }

  @NotNull
  public AttributesDescriptor[] getAttributeDescriptors() {
    return DESCRIPTORS;
  }

  @NotNull
  public ColorDescriptor[] getColorDescriptors() {
    return ColorDescriptor.EMPTY_ARRAY;
  }

  @NotNull
  public SyntaxHighlighter getHighlighter() {
    return new GoSyntaxHighlighter();
  }

  @NotNull
  public String getDemoText() {
    return "package main\n" +
           "\n" +
           "import (\n" +
           "    \"fmt\"\n" +
           "    \"math\"\n" +
           ")\n" +
           "\n" +
           "type <ts>Abser</ts> interface {\n" +
           "    Abs() <bt>float64</bt>\n" +
           "}\n" +
           "\n" +
           "const <c>Constance</c> = 1\n" +
           "\n" +
           "var <g>GlobalVar</g> = 1\n" +
           "\n" +
           "func <f>main</f>() {\n" +
           "    var a <tr>Abser</tr>;\n" +
           "    f := <tr>MyFloat</tr>(-math.<c>Sqrt2</c>);\n" +
           "    v := <tr>Vertex</tr>{3, 4};\n" +
           "\n" +
           "    a = f  // a MyFloat implements Abser\n" +
           "    a = &v // a *Vertex implements Abser\n" +
           "    a = v  // a Vertex, does NOT\n" +
           "    // implement Abser\n" +
           "\n" +
           "    fmt.Println(a.Abs())\n" +
           "}\n" +
           "\n" +
           "type <ts>MyFloat</ts> <bt>float64</bt>\n" +
           "\n" +
           "func (f <tr>MyFloat</tr>) <f>Abs</f>() <bt>float64</bt> {\n" +
           "    if f < 0 {\n" +
           "        return <bt>float64</bt>(-f)\n" +
           "    }\n" +
           "    return <bt>float64</bt>(f)\n" +
           "}\n" +
           "\n" +
           "type <ts>Vertex</ts> struct {\n" +
           "    X, Y <bt>float64</bt>\n" +
           "}\n" +
           "\n" +
           "func (v *<tr>Vertex</tr>) <f>Abs</f>() <bt>float64</bt> {\n" +
           "    return math.Sqrt(v.X*v.X + v.Y*v.Y)\n" +
           "}";
  }

  @NotNull
  public Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap() {
    return ATTRIBUTES_KEY_MAP;
  }
}
