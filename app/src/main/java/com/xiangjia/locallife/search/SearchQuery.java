package com.xiangjia.locallife.search;

/**
 * 搜索查询抽象语法树 (AST)
 * 表示解析后的搜索查询结构
 */
public abstract class SearchQuery {
    
    /**
     * 关键词查询
     */
    public static class KeywordQuery extends SearchQuery {
        private final String keyword;
        
        public KeywordQuery(String keyword) {
            this.keyword = keyword;
        }
        
        public String getKeyword() { 
            return keyword; 
        }
        
        @Override
        public String toString() {
            return "Keyword(" + keyword + ")";
        }
    }
    
    /**
     * 分类查询
     */
    public static class CategoryQuery extends SearchQuery {
        private final String category;
        
        public CategoryQuery(String category) {
            this.category = category;
        }
        
        public String getCategory() { 
            return category; 
        }
        
        @Override
        public String toString() {
            return "Category(" + category + ")";
        }
    }
    
    /**
     * 作者查询
     */
    public static class AuthorQuery extends SearchQuery {
        private final String author;
        
        public AuthorQuery(String author) {
            this.author = author;
        }
        
        public String getAuthor() { 
            return author; 
        }
        
        @Override
        public String toString() {
            return "Author(" + author + ")";
        }
    }
    
    /**
     * 日期查询
     */
    public static class DateQuery extends SearchQuery {
        private final String date;
        
        public DateQuery(String date) {
            this.date = date;
        }
        
        public String getDate() { 
            return date; 
        }
        
        @Override
        public String toString() {
            return "Date(" + date + ")";
        }
    }
    
    /**
     * 标签查询
     */
    public static class TagQuery extends SearchQuery {
        private final String tag;
        
        public TagQuery(String tag) {
            this.tag = tag;
        }
        
        public String getTag() { 
            return tag; 
        }
        
        @Override
        public String toString() {
            return "Tag(" + tag + ")";
        }
    }
    
    /**
     * 二元操作查询 (AND, OR)
     */
    public static class BinaryQuery extends SearchQuery {
        public enum Operator { 
            AND, OR 
        }
        
        private final SearchQuery left;
        private final Operator operator;
        private final SearchQuery right;
        
        public BinaryQuery(SearchQuery left, Operator operator, SearchQuery right) {
            this.left = left;
            this.operator = operator;
            this.right = right;
        }
        
        public SearchQuery getLeft() { 
            return left; 
        }
        
        public Operator getOperator() { 
            return operator; 
        }
        
        public SearchQuery getRight() { 
            return right; 
        }
        
        @Override
        public String toString() {
            return "(" + left + " " + operator + " " + right + ")";
        }
    }
    
    /**
     * 否定查询 (NOT)
     */
    public static class NotQuery extends SearchQuery {
        private final SearchQuery query;
        
        public NotQuery(SearchQuery query) {
            this.query = query;
        }
        
        public SearchQuery getQuery() { 
            return query; 
        }
        
        @Override
        public String toString() {
            return "NOT(" + query + ")";
        }
    }
}