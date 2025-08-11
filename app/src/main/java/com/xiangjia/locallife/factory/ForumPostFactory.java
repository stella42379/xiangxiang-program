package com.xiangjia.locallife.factory;

import com.xiangjia.locallife.model.ForumPost;
import com.xiangjia.locallife.model.User;

/**
 * Factory Pattern - 帖子工厂
 * 根据不同类型创建不同的帖子
 */
public class ForumPostFactory {
    
    public enum PostType {
        DISCUSSION,     // 讨论贴
        HELP,           // 求助贴
        SHARE,          // 分享贴
        ANNOUNCEMENT,   // 公告贴
        FRIENDS         // 交友贴
    }
    
    /**
     * 创建帖子
     * @param type 帖子类型
     * @param author 作者
     * @param title 标题
     * @param content 内容
     * @return 创建的帖子
     */
    public static ForumPost createPost(PostType type, User author, String title, String content) {
        if (type == null || author == null || title == null || content == null) {
            throw new IllegalArgumentException("参数不能为null");
        }
        
        ForumPost post = new ForumPost(
            author.getUserId(), 
            author.getNickname(), 
            title, 
            content, 
            getCategory(type)
        );
        
        // 根据类型设置特殊属性
        switch (type) {
            case ANNOUNCEMENT:
                post.setPinned(true);
                post.setTags("[\"公告\",\"重要\"]");
                break;
            case HELP:
                post.setTags("[\"求助\",\"需要帮助\"]");
                break;
            case SHARE:
                post.setTags("[\"分享\",\"经验\"]");
                break;
            case FRIENDS:
                post.setTags("[\"交友\",\"认识朋友\"]");
                break;
            case DISCUSSION:
            default:
                post.setTags("[\"讨论\",\"话题\"]");
                break;
        }
        
        return post;
    }
    
    /**
     * 创建预设模板帖子
     * @param type 帖子类型
     * @param author 作者
     * @return 创建的模板帖子
     */
    public static ForumPost createTemplatePost(PostType type, User author) {
        if (type == null || author == null) {
            throw new IllegalArgumentException("参数不能为null");
        }
        
        switch (type) {
            case HELP:
                return createPost(type, author, 
                    "求助：" + getHelpTitle(), 
                    getHelpContent());
            case SHARE:
                return createPost(type, author, 
                    "分享：" + getShareTitle(), 
                    getShareContent());
            case FRIENDS:
                return createPost(type, author, 
                    "交友：" + getFriendsTitle(), 
                    getFriendsContent());
            case ANNOUNCEMENT:
                return createPost(type, author, 
                    "公告：" + getAnnouncementTitle(), 
                    getAnnouncementContent());
            case DISCUSSION:
            default:
                return createPost(type, author, 
                    "讨论：" + getDiscussionTitle(), 
                    getDiscussionContent());
        }
    }
    
    /**
     * 批量创建帖子
     * @param type 帖子类型
     * @param author 作者
     * @param count 数量
     * @return 帖子列表
     */
    public static java.util.List<ForumPost> createBatchPosts(PostType type, User author, int count) {
        java.util.List<ForumPost> posts = new java.util.ArrayList<>();
        
        for (int i = 0; i < count; i++) {
            ForumPost post = createTemplatePost(type, author);
            // 为每个帖子添加序号以区分
            post.setTitle(post.getTitle() + " (" + (i + 1) + ")");
            posts.add(post);
        }
        
        return posts;
    }
    
    /**
     * 创建快速帖子 - 使用最少参数
     * @param author 作者
     * @param title 标题
     * @param content 内容
     * @return 讨论类型的帖子
     */
    public static ForumPost createQuickPost(User author, String title, String content) {
        return createPost(PostType.DISCUSSION, author, title, content);
    }
    
    /**
     * 根据关键词自动判断帖子类型
     * @param title 标题
     * @param content 内容
     * @return 推荐的帖子类型
     */
    public static PostType suggestPostType(String title, String content) {
        String text = (title + " " + content).toLowerCase();
        
        if (text.contains("求助") || text.contains("帮忙") || text.contains("急需") || text.contains("请问")) {
            return PostType.HELP;
        } else if (text.contains("分享") || text.contains("推荐") || text.contains("经验")) {
            return PostType.SHARE;
        } else if (text.contains("交友") || text.contains("认识") || text.contains("朋友") || text.contains("一起")) {
            return PostType.FRIENDS;
        } else if (text.contains("公告") || text.contains("通知") || text.contains("重要")) {
            return PostType.ANNOUNCEMENT;
        } else {
            return PostType.DISCUSSION;
        }
    }
    
    /**
     * 创建智能帖子 - 自动判断类型
     * @param author 作者
     * @param title 标题
     * @param content 内容
     * @return 自动判断类型的帖子
     */
    public static ForumPost createSmartPost(User author, String title, String content) {
        PostType suggestedType = suggestPostType(title, content);
        return createPost(suggestedType, author, title, content);
    }
    
    private static String getCategory(PostType type) {
        switch (type) {
            case DISCUSSION: return "discussion";
            case HELP: return "help";
            case SHARE: return "share";
            case ANNOUNCEMENT: return "announcement";
            case FRIENDS: return "friends";
            default: return "discussion";
        }
    }
    
    // 模板内容生成方法
    private static String getHelpTitle() {
        String[] titles = {
            "家里水管漏水，求推荐师傅",
            "寻找靠谱的家政服务",
            "小区停车位问题如何解决",
            "急需借用工具一用",
            "电器坏了，求维修推荐",
            "孩子丢失物品，请大家帮忙寻找"
        };
        return titles[(int)(Math.random() * titles.length)];
    }
    
    private static String getHelpContent() {
        return "遇到了一些问题，希望大家能帮忙解决或者提供一些建议。万分感谢！";
    }
    
    private static String getShareTitle() {
        String[] titles = {
            "小区周边美食推荐",
            "今天发现的美景分享",
            "生活小贴士分享",
            "好用的APP推荐",
            "育儿经验分享",
            "节能环保小妙招"
        };
        return titles[(int)(Math.random() * titles.length)];
    }
    
    private static String getShareContent() {
        return "今天想和大家分享一些有用的信息，希望对大家有帮助，欢迎交流讨论。";
    }
    
    private static String getFriendsTitle() {
        String[] titles = {
            "新搬来的邻居想认识大家",
            "寻找一起晨练的伙伴",
            "组建小区读书会",
            "寻找志同道合的朋友",
            "周末一起遛娃的家长",
            "组织小区篮球队"
        };
        return titles[(int)(Math.random() * titles.length)];
    }
    
    private static String getFriendsContent() {
        return "希望能认识更多的邻居朋友，大家一起交流，让社区更加和谐温暖。";
    }
    
    private static String getAnnouncementTitle() {
        String[] titles = {
            "小区物业重要通知",
            "停水停电通知",
            "小区活动公告",
            "安全提醒通知",
            "设施维护通知",
            "社区规定更新"
        };
        return titles[(int)(Math.random() * titles.length)];
    }
    
    private static String getAnnouncementContent() {
        return "这是一条重要的社区公告，请大家仔细阅读并遵守相关规定。如有疑问请及时联系物业。";
    }
    
    private static String getDiscussionTitle() {
        String[] titles = {
            "关于小区垃圾分类的建议",
            "小区绿化改造意见征集",
            "如何让社区更和谐",
            "小区安全问题讨论",
            "业委会选举讨论",
            "小区文化活动策划"
        };
        return titles[(int)(Math.random() * titles.length)];
    }
    
    private static String getDiscussionContent() {
        return "想和大家讨论一个话题，希望大家能积极参与，发表自己的看法和建议。";
    }
}