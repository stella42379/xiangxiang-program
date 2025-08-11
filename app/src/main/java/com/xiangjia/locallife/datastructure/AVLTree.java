package com.xiangjia.locallife.datastructure;

import java.util.ArrayList;
import java.util.List;

/**
 * AVL树实现 - 用于高效的论坛数据搜索和排序
 * 支持按时间戳、点赞数等字段进行快速查找
 * 
 * AVL树是一种自平衡二叉搜索树，保证所有操作的时间复杂度为O(log n)
 */
public class AVLTree<T extends Comparable<T>> {
    
    private AVLNode<T> root;
    private int size;
    
    /**
     * AVL树节点
     */
    private static class AVLNode<T> {
        T data;
        AVLNode<T> left;
        AVLNode<T> right;
        int height;
        
        AVLNode(T data) {
            this.data = data;
            this.height = 1;
        }
    }
    
    public AVLTree() {
        this.root = null;
        this.size = 0;
    }
    
    /**
     * 插入元素
     * @param data 要插入的数据
     */
    public void insert(T data) {
        if (data == null) {
            throw new IllegalArgumentException("数据不能为null");
        }
        root = insert(root, data);
        size++;
    }
    
    private AVLNode<T> insert(AVLNode<T> node, T data) {
        // 1. 标准BST插入
        if (node == null) {
            return new AVLNode<>(data);
        }
        
        int comparison = data.compareTo(node.data);
        if (comparison < 0) {
            node.left = insert(node.left, data);
        } else if (comparison > 0) {
            node.right = insert(node.right, data);
        } else {
            // 相等的值，不插入重复
            size--; // 回退size增加
            return node;
        }
        
        // 2. 更新高度
        updateHeight(node);
        
        // 3. 获取平衡因子
        int balance = getBalance(node);
        
        // 4. 如果不平衡，进行旋转
        // Left Left Case
        if (balance > 1 && data.compareTo(node.left.data) < 0) {
            return rotateRight(node);
        }
        
        // Right Right Case
        if (balance < -1 && data.compareTo(node.right.data) > 0) {
            return rotateLeft(node);
        }
        
        // Left Right Case
        if (balance > 1 && data.compareTo(node.left.data) > 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }
        
        // Right Left Case
        if (balance < -1 && data.compareTo(node.right.data) < 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }
        
        return node;
    }
    
    /**
     * 删除元素
     * @param data 要删除的数据
     * @return 是否删除成功
     */
    public boolean delete(T data) {
        if (data == null || search(data) == null) {
            return false;
        }
        root = delete(root, data);
        size--;
        return true;
    }
    
    private AVLNode<T> delete(AVLNode<T> node, T data) {
        // 1. 标准BST删除
        if (node == null) {
            return node;
        }
        
        int comparison = data.compareTo(node.data);
        if (comparison < 0) {
            node.left = delete(node.left, data);
        } else if (comparison > 0) {
            node.right = delete(node.right, data);
        } else {
            // 找到要删除的节点
            if (node.left == null || node.right == null) {
                AVLNode<T> temp = (node.left != null) ? node.left : node.right;
                
                if (temp == null) {
                    // 没有子节点
                    temp = node;
                    node = null;
                } else {
                    // 一个子节点
                    node = temp;
                }
            } else {
                // 两个子节点：找到中序后继
                AVLNode<T> temp = findMin(node.right);
                node.data = temp.data;
                node.right = delete(node.right, temp.data);
            }
        }
        
        if (node == null) {
            return node;
        }
        
        // 2. 更新高度
        updateHeight(node);
        
        // 3. 获取平衡因子
        int balance = getBalance(node);
        
        // 4. 如果不平衡，进行旋转
        // Left Left Case
        if (balance > 1 && getBalance(node.left) >= 0) {
            return rotateRight(node);
        }
        
        // Left Right Case
        if (balance > 1 && getBalance(node.left) < 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }
        
        // Right Right Case
        if (balance < -1 && getBalance(node.right) <= 0) {
            return rotateLeft(node);
        }
        
        // Right Left Case
        if (balance < -1 && getBalance(node.right) > 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }
        
        return node;
    }
    
    /**
     * 搜索元素
     * @param data 要搜索的数据
     * @return 找到的数据，如果不存在则返回null
     */
    public T search(T data) {
        if (data == null) return null;
        AVLNode<T> node = search(root, data);
        return node != null ? node.data : null;
    }
    
    private AVLNode<T> search(AVLNode<T> node, T data) {
        if (node == null || data.compareTo(node.data) == 0) {
            return node;
        }
        
        if (data.compareTo(node.data) < 0) {
            return search(node.left, data);
        } else {
            return search(node.right, data);
        }
    }
    
    /**
     * 中序遍历 - 返回排序后的列表
     * @return 按升序排列的元素列表
     */
    public List<T> inorderTraversal() {
        List<T> result = new ArrayList<>();
        inorderTraversal(root, result);
        return result;
    }
    
    private void inorderTraversal(AVLNode<T> node, List<T> result) {
        if (node != null) {
            inorderTraversal(node.left, result);
            result.add(node.data);
            inorderTraversal(node.right, result);
        }
    }
    
    /**
     * 范围查询 - 查找在指定范围内的所有元素
     * @param min 最小值（包含）
     * @param max 最大值（包含）
     * @return 范围内的元素列表
     */
    public List<T> rangeQuery(T min, T max) {
        List<T> result = new ArrayList<>();
        if (min != null && max != null) {
            rangeQuery(root, min, max, result);
        }
        return result;
    }
    
    private void rangeQuery(AVLNode<T> node, T min, T max, List<T> result) {
        if (node == null) return;
        
        if (node.data.compareTo(min) > 0) {
            rangeQuery(node.left, min, max, result);
        }
        
        if (node.data.compareTo(min) >= 0 && node.data.compareTo(max) <= 0) {
            result.add(node.data);
        }
        
        if (node.data.compareTo(max) < 0) {
            rangeQuery(node.right, min, max, result);
        }
    }
    
    /**
     * 获取树的高度
     * @return 树的高度
     */
    public int getTreeHeight() {
        return getHeight(root);
    }
    
    /**
     * 检查树是否平衡
     * @return 是否平衡
     */
    public boolean isBalanced() {
        return isBalanced(root);
    }
    
    private boolean isBalanced(AVLNode<T> node) {
        if (node == null) return true;
        
        int balance = getBalance(node);
        return Math.abs(balance) <= 1 && 
               isBalanced(node.left) && 
               isBalanced(node.right);
    }
    
    // 辅助方法
    private int getHeight(AVLNode<T> node) {
        return node == null ? 0 : node.height;
    }
    
    private void updateHeight(AVLNode<T> node) {
        if (node != null) {
            node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));
        }
    }
    
    private int getBalance(AVLNode<T> node) {
        return node == null ? 0 : getHeight(node.left) - getHeight(node.right);
    }
    
    private AVLNode<T> rotateRight(AVLNode<T> y) {
        AVLNode<T> x = y.left;
        AVLNode<T> T2 = x.right;
        
        x.right = y;
        y.left = T2;
        
        updateHeight(y);
        updateHeight(x);
        
        return x;
    }
    
    private AVLNode<T> rotateLeft(AVLNode<T> x) {
        AVLNode<T> y = x.right;
        AVLNode<T> T2 = y.left;
        
        y.left = x;
        x.right = T2;
        
        updateHeight(x);
        updateHeight(y);
        
        return y;
    }
    
    private AVLNode<T> findMin(AVLNode<T> node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }
    
    /**
     * 获取树中元素数量
     * @return 元素数量
     */
    public int size() {
        return size;
    }
    
    /**
     * 检查树是否为空
     * @return 是否为空
     */
    public boolean isEmpty() {
        return size == 0;
    }
    
    /**
     * 清空树
     */
    public void clear() {
        root = null;
        size = 0;
    }
    
    /**
     * 获取树的字符串表示（用于调试）
     * @return 树的结构字符串
     */
    @Override
    public String toString() {
        if (root == null) return "Empty AVL Tree";
        return "AVL Tree (size=" + size + ", height=" + getTreeHeight() + ")";
    }
}