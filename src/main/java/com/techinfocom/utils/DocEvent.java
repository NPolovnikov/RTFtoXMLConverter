package com.techinfocom.utils;

/**
 * Только важные с точки зрения преобразования события возникающие при чтении RTF-документа
 */
public enum DocEvent {
    DOC_BEGIN,
    DOC_END,
    TABLE_BEGIN,
    TABLE_END,
    ROW_BEGIN,
    ROW_END,
    CELL_END,
    ITALIC_BEGIN,
    ITALIC_END,
    BOLD_BEGIN
}
