package com.xiangjia.locallife.search;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 论坛搜索词法分析器
 * 支持语法：
 * - 基本关键词: 停车位 水管
 * - 分类搜索: category:discussion
 * - 作者搜索: author:张三
 * - 日期搜索: date:2024-01-01
 * - 标签搜索: tag:热门
 * - 引用字符串: "停车位问题"
 * - 逻辑操作符: AND OR NOT
 * - 括号分组: (停车位 OR 水管) AND category:help
 */
public class ForumSearchTokenizer {
    
    private static final Pattern TOKEN_PATTERN = Pattern.compile(
        "(?<CATEGORY>category:\\w+)|" +
        "(?<AUTHOR>author:[\\u4e00-\\u9fa5\\w]+)|" +
        "(?<DATE>date:\\d{4}-\\d{2}-\\d{2})|" +
        "(?<TAG>tag:[\\u4e00-\\u9fa5\\w]+)|" +
        "(?<OPERATOR>\\b(?:AND|OR|NOT)\\b)|" +
        "(?<QUOTED>\"[^\"]*\")|" +
        "(?<LPAREN>\\()|" +
        "(?<RPAREN>\\))|" +
        "(?<KEYWORD>[\\u4e00-\\u9fa5\\w]+)|" +
        "(?<WHITESPACE>\\s+)"
    );
    
    /**
     * 将输入字符串分词为Token列表
     * @param input 搜索查询字符串
     * @return Token列表
     */
    public List<Token> tokenize(String input) {
        List<Token> tokens = new ArrayList<>();
        
        if (input == null || input.trim().isEmpty()) {
            tokens.add(new Token(Token.Type.EOF, "", 0));
            return tokens;
        }
        
        Matcher matcher = TOKEN_PATTERN.matcher(input);
        
        while (matcher.find()) {
            int start = matcher.start();
            
            if (matcher.group("CATEGORY") != null) {
                String value = matcher.group("CATEGORY").substring(9); // 去掉 "category:"
                tokens.add(new Token(Token.Type.CATEGORY, value, start));
            } else if (matcher.group("AUTHOR") != null) {
                String value = matcher.group("AUTHOR").substring(7); // 去掉 "author:"
                tokens.add(new Token(Token.Type.AUTHOR, value, start));
            } else if (matcher.group("DATE") != null) {
                String value = matcher.group("DATE").substring(5); // 去掉 "date:"
                tokens.add(new Token(Token.Type.DATE, value, start));
            } else if (matcher.group("TAG") != null) {
                String value = matcher.group("TAG").substring(4); // 去掉 "tag:"
                tokens.add(new Token(Token.Type.TAG, value, start));
            } else if (matcher.group("OPERATOR") != null) {
                tokens.add(new Token(Token.Type.OPERATOR, matcher.group("OPERATOR"), start));
            } else if (matcher.group("QUOTED") != null) {
                String value = matcher.group("QUOTED");
                // 去掉引号
                value = value.substring(1, value.length() - 1);
                tokens.add(new Token(Token.Type.QUOTED_STRING, value, start));
            } else if (matcher.group("LPAREN") != null) {
                tokens.add(new Token(Token.Type.LPAREN, "(", start));
            } else if (matcher.group("RPAREN") != null) {
                tokens.add(new Token(Token.Type.RPAREN, ")", start));
            } else if (matcher.group("KEYWORD") != null) {
                tokens.add(new Token(Token.Type.KEYWORD, matcher.group("KEYWORD"), start));
            }
            // 忽略空白字符 WHITESPACE
        }
        
        tokens.add(new Token(Token.Type.EOF, "", input.length()));
        return tokens;
    }
    
    /**
     * 验证查询字符串的基本语法
     * @param input 查询字符串
     * @return 是否语法正确
     */
    public boolean validateSyntax(String input) {
        try {
            List<Token> tokens = tokenize(input);
            
            // 检查基本的括号匹配
            int parenCount = 0;
            for (Token token : tokens) {
                if (token.getType() == Token.Type.LPAREN) {
                    parenCount++;
                } else if (token.getType() == Token.Type.RPAREN) {
                    parenCount--;
                    if (parenCount < 0) {
                        return false; // 右括号过多
                    }
                }
            }
            
            return parenCount == 0; // 括号必须匹配
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 获取查询字符串的统计信息
     * @param input 查询字符串
     * @return 统计信息字符串
     */
    public String getTokenStatistics(String input) {
        List<Token> tokens = tokenize(input);
        
        int keywords = 0, operators = 0, categories = 0, authors = 0;
        
        for (Token token : tokens) {
            switch (token.getType()) {
                case KEYWORD:
                case QUOTED_STRING:
                    keywords++;
                    break;
                case OPERATOR:
                    operators++;
                    break;
                case CATEGORY:
                    categories++;
                    break;
                case AUTHOR:
                    authors++;
                    break;
            }
        }
        
        return String.format("关键词:%d, 操作符:%d, 分类:%d, 作者:%d", 
                           keywords, operators, categories, authors);
    }
}