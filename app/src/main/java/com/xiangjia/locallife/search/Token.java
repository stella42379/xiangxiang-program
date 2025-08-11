package com.xiangjia.locallife.search;

/**
 * 搜索词法单元
 * 用于表示搜索查询中的各种词法元素
 */
public class Token {
    public enum Type {
        KEYWORD,        // 关键词
        CATEGORY,       // 分类: category:discussion
        AUTHOR,         // 作者: author:张三
        DATE,           // 日期: date:2024-01-01
        TAG,            // 标签: tag:热门
        OPERATOR,       // 操作符: AND, OR, NOT
        QUOTED_STRING,  // 引用字符串: "停车位问题"
        LPAREN,         // 左括号
        RPAREN,         // 右括号
        EOF             // 结束符
    }
    
    private final Type type;
    private final String value;
    private final int position;
    
    public Token(Type type, String value, int position) {
        this.type = type;
        this.value = value;
        this.position = position;
    }
    
    // Getters
    public Type getType() { 
        return type; 
    }
    
    public String getValue() { 
        return value; 
    }
    
    public int getPosition() { 
        return position; 
    }
    
    @Override
    public String toString() {
        return String.format("Token{type=%s, value='%s', pos=%d}", type, value, position);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Token token = (Token) obj;
        return position == token.position &&
               type == token.type &&
               value.equals(token.value);
    }
    
    @Override
    public int hashCode() {
        return type.hashCode() * 31 + value.hashCode() * 7 + position;
    }
}