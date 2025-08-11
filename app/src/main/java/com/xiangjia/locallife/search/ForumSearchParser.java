package com.xiangjia.locallife.search;

import java.util.List;

/**
 * 论坛搜索语法分析器
 * 语法规则:
 * query ::= or_expr
 * or_expr ::= and_expr ('OR' and_expr)*
 * and_expr ::= not_expr ('AND' not_expr)*
 * not_expr ::= 'NOT' primary | primary
 * primary ::= '(' query ')' | term
 * term ::= KEYWORD | CATEGORY | AUTHOR | DATE | TAG | QUOTED_STRING
 */
public class ForumSearchParser {
    
    private List<Token> tokens;
    private int current = 0;
    
    /**
     * 解析Token列表为SearchQuery AST
     * @param tokens Token列表
     * @return SearchQuery AST
     * @throws SearchParseException 解析异常
     */
    public SearchQuery parse(List<Token> tokens) throws SearchParseException {
        this.tokens = tokens;
        this.current = 0;
        
        if (tokens.isEmpty() || (tokens.size() == 1 && tokens.get(0).getType() == Token.Type.EOF)) {
            throw new SearchParseException("空查询");
        }
        
        SearchQuery result = parseOrExpression();
        
        if (!isAtEnd()) {
            throw new SearchParseException("意外的标记: " + peek().getValue());
        }
        
        return result;
    }
    
    /**
     * 解析OR表达式 (最低优先级)
     */
    private SearchQuery parseOrExpression() throws SearchParseException {
        SearchQuery expr = parseAndExpression();
        
        while (match(Token.Type.OPERATOR) && previous().getValue().equals("OR")) {
            SearchQuery right = parseAndExpression();
            expr = new SearchQuery.BinaryQuery(expr, SearchQuery.BinaryQuery.Operator.OR, right);
        }
        
        return expr;
    }
    
    /**
     * 解析AND表达式 (中等优先级)
     */
    private SearchQuery parseAndExpression() throws SearchParseException {
        SearchQuery expr = parseNotExpression();
        
        while (match(Token.Type.OPERATOR) && previous().getValue().equals("AND")) {
            SearchQuery right = parseNotExpression();
            expr = new SearchQuery.BinaryQuery(expr, SearchQuery.BinaryQuery.Operator.AND, right);
        }
        
        // 隐式AND：相邻的term默认是AND关系
        while (!isAtEnd() && peek().getType() != Token.Type.RPAREN && 
               peek().getType() != Token.Type.EOF && 
               !(peek().getType() == Token.Type.OPERATOR && 
                 (peek().getValue().equals("OR") || peek().getValue().equals("AND")))) {
            SearchQuery right = parseNotExpression();
            expr = new SearchQuery.BinaryQuery(expr, SearchQuery.BinaryQuery.Operator.AND, right);
        }
        
        return expr;
    }
    
    /**
     * 解析NOT表达式 (高优先级)
     */
    private SearchQuery parseNotExpression() throws SearchParseException {
        if (match(Token.Type.OPERATOR) && previous().getValue().equals("NOT")) {
            SearchQuery expr = parsePrimary();
            return new SearchQuery.NotQuery(expr);
        }
        
        return parsePrimary();
    }
    
    /**
     * 解析基本表达式 (最高优先级)
     */
    private SearchQuery parsePrimary() throws SearchParseException {
        if (match(Token.Type.KEYWORD)) {
            return new SearchQuery.KeywordQuery(previous().getValue());
        }
        
        if (match(Token.Type.CATEGORY)) {
            return new SearchQuery.CategoryQuery(previous().getValue());
        }
        
        if (match(Token.Type.AUTHOR)) {
            return new SearchQuery.AuthorQuery(previous().getValue());
        }
        
        if (match(Token.Type.DATE)) {
            return new SearchQuery.DateQuery(previous().getValue());
        }
        
        if (match(Token.Type.TAG)) {
            return new SearchQuery.TagQuery(previous().getValue());
        }
        
        if (match(Token.Type.QUOTED_STRING)) {
            return new SearchQuery.KeywordQuery(previous().getValue());
        }
        
        if (match(Token.Type.LPAREN)) {
            SearchQuery expr = parseOrExpression();
            consume(Token.Type.RPAREN, "期望 ')'");
            return expr;
        }
        
        throw new SearchParseException("意外的标记: " + peek().getValue());
    }
    
    /**
     * 匹配指定类型的Token
     */
    private boolean match(Token.Type... types) {
        for (Token.Type type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }
        return false;
    }
    
    /**
     * 检查当前Token是否为指定类型
     */
    private boolean check(Token.Type type) {
        if (isAtEnd()) return false;
        return peek().getType() == type;
    }
    
    /**
     * 前进到下一个Token
     */
    private Token advance() {
        if (!isAtEnd()) current++;
        return previous();
    }
    
    /**
     * 是否到达Token列表末尾
     */
    private boolean isAtEnd() {
        return peek().getType() == Token.Type.EOF;
    }
    
    /**
     * 获取当前Token
     */
    private Token peek() {
        return tokens.get(current);
    }
    
    /**
     * 获取前一个Token
     */
    private Token previous() {
        return tokens.get(current - 1);
    }
    
    /**
     * 消费指定类型的Token，如果不匹配则抛出异常
     */
    private Token consume(Token.Type type, String message) throws SearchParseException {
        if (check(type)) return advance();
        throw new SearchParseException(message + "，但得到: " + peek().getValue());
    }
    
    /**
     * 搜索解析异常
     */
    public static class SearchParseException extends Exception {
        public SearchParseException(String message) {
            super(message);
        }
        
        public SearchParseException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}