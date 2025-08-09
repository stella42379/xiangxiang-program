package com.xiangjia.locallife.util;

import com.xiangjia.locallife.model.ForumPost;
import com.xiangjia.locallife.model.ForumMessage;
import com.xiangjia.locallife.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 论坛数据生成器
 * 用于生成满足课程要求的2500+条测试数据
 * 实现Task2设计要求和6442课程要求的DataFiles特性
 */
public class ForumDataGenerator {
    
    private static final Random random = new Random();
    
    // 用户名列表
    private static final String[] USERNAMES = {
        "张三", "李四", "王五", "赵六", "钱七", "孙八", "周九", "吴十",
        "陈小明", "刘小红", "黄小华", "林小美", "郑小强", "何小丽", "朱小军",
        "社区新人", "热心市民", "邻居老王", "楼下阿姨", "隔壁老张", "社区管家",
        "爱心志愿者", "资深住户", "新手妈妈", "退休大爷", "大学生小李", "上班族小王",
        "community_lover", "neighborhood_friend", "helpful_neighbor", "kind_resident"
    };
    
    // 帖子标题模板
    private static final String[] POST_TITLES = {
        "求助：小区停车位问题如何解决？",
        "分享：今天在小区里发现的美景",
        "讨论：关于小区垃圾分类的建议",
        "交友：新搬来的邻居想认识大家",
        "求助：家里水管漏水，有推荐的师傅吗？",
        "分享：小区周边美食推荐",
        "讨论：小区健身器材维护问题",
        "交友：寻找一起晨练的伙伴",
        "公告：小区物业通知停水停电",
        "求助：孩子找不到了，请大家帮忙留意",
        "分享：小区里的可爱小动物",
        "讨论：关于小区绿化改造的意见",
        "交友：组建小区读书会",
        "求助：急需借用梯子一用",
        "分享：今日份的好心情",
        "讨论：小区安全问题讨论",
        "交友：寻找志同道合的朋友",
        "分享：邻里互助温暖瞬间",
        "求助：寻找走失的小猫",
        "讨论：如何让社区更和谐"
    };
    
    // 帖子内容模板
    private static final String[] POST_CONTENTS = {
        "最近小区的停车位越来越紧张了，很多时候回家都找不到地方停车。大家有什么好的建议吗？是不是应该向物业反映一下这个问题？",
        "今天早上出门的时候，看到小区花园里樱花开得正盛，真的很美。春天的气息扑面而来，心情瞬间变好了。分享几张照片给大家看看。",
        "关于垃圾分类，我觉得我们小区做得还不够好。很多人还是习惯把所有垃圾都扔在一个桶里。不如我们组织一个垃圾分类学习活动？",
        "我是新搬来的住户，住在8号楼，希望能认识更多的邻居朋友。大家平时都有什么业余爱好吗？可以一起交流交流。",
        "家里厨房的水管突然漏水了，情况比较紧急。有没有靠谱的维修师傅推荐？最好是就近的，价格合理的那种。谢谢大家！",
        "给大家推荐几家小区周边的美食店：楼下的兰州拉面很正宗，对面的川菜馆麻婆豆腐一绝，还有街角的甜品店蛋糕很棒。",
        "小区里的健身器材有些已经老化了，特别是单杠有点松动，大家使用的时候要注意安全。建议物业尽快维修更换。",
        "想找几个一起晨练的伙伴，我通常6点钟在小区花园开始锻炼，主要是太极拳和慢跑。有兴趣的朋友可以一起来。",
        "接到物业通知，本周六上午9点到下午3点会停水停电，请大家提前做好准备。停电期间电梯也会停运，请注意安全。",
        "我家5岁的小朋友在小区里玩耍时走失了，穿着红色外套，黑色裤子。如果有人看到请及时联系我，万分感谢！",
        "今天在小区里遇到一只超级可爱的小橘猫，毛茸茸的特别乖。它好像是流浪猫，有没有好心人愿意收养或者一起照顾它？",
        "关于小区绿化改造，我觉得可以多种一些花草，特别是香花植物。还可以设置一些休息的座椅，让大家有更多交流的场所。",
        "想在小区里组建一个读书会，定期分享好书，交流读书心得。有兴趣的朋友请报名，我们可以每周聚会一次。",
        "家里需要修一下窗户，急需借用一把梯子，用完就还。有热心邻居可以帮忙吗？我住在6号楼3单元。",
        "今天心情特别好，想和大家分享一下。邻居阿姨给我送了自己包的饺子，真的很感动。这就是邻里之间的温暖啊！",
        "最近小区的安全问题需要大家重视，建议加强门禁管理，陌生人进入要登记。大家也要提高警惕，相互照应。",
        "新搬来不久，希望能找到一些志同道合的朋友。我喜欢摄影、旅行、美食，如果有共同爱好的朋友欢迎交流。",
        "今天看到楼下大爷主动帮助搬家的新邻居搬东西，真的很暖心。这种邻里互助的精神值得我们学习和传承。",
        "我家的小猫昨天晚上跑出去后就没回来，它是一只白色的波斯猫，很亲人。如果有人看到请联系我，重金酬谢！",
        "如何让我们的社区更加和谐？我觉得需要大家都积极参与，互相理解，多一些包容和耐心。欢迎大家畅所欲言。"
    };
    
    // 回复内容模板
    private static final String[] REPLY_CONTENTS = {
        "支持楼主的观点！",
        "这个建议很不错，我赞同。",
        "我也遇到过类似的问题。",
        "感谢分享，很有用的信息。",
        "楼主辛苦了，给你点赞！",
        "我觉得还可以这样处理...",
        "有道理，学习了。",
        "楼主说得对，我们应该这样做。",
        "这个方法我试过，确实有效。",
        "谢谢楼主的提醒！",
        "我也来分享一下我的经验。",
        "这个问题确实需要重视。",
        "楼主人真好，为你点赞！",
        "我来帮顶一下这个帖子。",
        "希望更多人能看到这个帖子。",
        "楼主的照片拍得真好！",
        "我也想参加这个活动。",
        "有同样想法的可以联系我。",
        "这个想法很有创意。",
        "我们小区就是需要这样的人。",
        "谢谢楼主的无私分享。",
        "我也来说说我的看法。",
        "楼主考虑得很周到。",
        "这确实是个值得讨论的话题。",
        "我举双手赞成！",
        "楼主真是热心肠。",
        "这种正能量需要传递下去。",
        "我们都要向楼主学习。",
        "这个提议我支持。",
        "希望物业能够重视这个问题。"
    };
    
    // 分类列表
    private static final String[] CATEGORIES = {
        "discussion", "friends", "help", "share", "announcement"
    };
    
    /**
     * 生成测试用户数据
     * @param count 用户数量
     * @return 用户列表
     */
    public static List<User> generateUsers(int count) {
        List<User> users = new ArrayList<>();

        
        
        // 添加课程要求的测试账户
        User testUser1 = new User("comp2100@anu.edu.au", "comp2100@anu.edu.au", "comp2100");
        testUser1.setNickname("COMP2100测试用户");
        testUser1.setUserRole("admin");
        users.add(testUser1);
        
        User testUser2 = new User("comp6442@anu.edu.au", "comp6442@anu.edu.au", "comp6442");
        testUser2.setNickname("COMP6442测试用户");
        testUser2.setUserRole("admin");
        users.add(testUser2);
        
        // 生成其他用户
        for (int i = 2; i < count; i++) {
            String username = USERNAMES[random.nextInt(USERNAMES.length)] + i;
            String email = "user" + i + "@example.com";
            String password = "password123";
            
            User user = new User(username, email, password);
            user.setNickname(username);
            user.setBio("这是用户" + username + "的个人简介");
            user.setLocation("社区" + (random.nextInt(10) + 1) + "号楼");
            
            // 随机设置用户统计数据
            user.setPostCount(random.nextInt(50));
            user.setMessageCount(random.nextInt(200));
            user.setFriendCount(random.nextInt(30));
            
            // 随机设置在线状态
            user.setOnline(random.nextBoolean());
            
            // 随机设置用户角色
            if (random.nextDouble() < 0.05) { // 5%概率为版主
                user.setUserRole("moderator");
            }
            
            users.add(user);
        }
        
        return users;
    }
    
    /**
     * 生成测试帖子数据
     * @param users 用户列表
     * @param count 帖子数量
     * @return 帖子列表
     */
    public static List<ForumPost> generatePosts(List<User> users, int count) {
        List<ForumPost> posts = new ArrayList<>();
        
        for (int i = 0; i < count; i++) {
            User author = users.get(random.nextInt(users.size()));
            String title = POST_TITLES[random.nextInt(POST_TITLES.length)];
            String content = POST_CONTENTS[random.nextInt(POST_CONTENTS.length)];
            String category = CATEGORIES[random.nextInt(CATEGORIES.length)];
            
            // 为标题添加序号使其唯一
            if (i > 0) {
                title = title + " (" + (i + 1) + ")";
            }
            
            ForumPost post = new ForumPost(
                author.getUserId(),
                author.getNickname(),
                title,
                content,
                category
            );
            
            // 随机设置发布时间（过去30天内）
            long now = System.currentTimeMillis();
            long randomTime = now - random.nextInt(30 * 24 * 60 * 60 * 1000); // 30天内随机时间
            post.setTimestamp(randomTime);
            post.setLastActivityTime(randomTime + random.nextInt(24 * 60 * 60 * 1000)); // 最后活动时间稍晚一些
            
            // 随机设置统计数据
            post.setLikeCount(random.nextInt(100));
            post.setReplyCount(random.nextInt(50));
            post.setViewCount(random.nextInt(500) + post.getLikeCount() + post.getReplyCount());
            
            // 随机设置置顶状态（5%概率）
            if (random.nextDouble() < 0.05) {
                post.setPinned(true);
            }
            
            // 随机添加标签
            if (random.nextBoolean()) {
                String[] tags = {"热门", "精华", "新手", "求助", "分享", "讨论"};
                post.setTags("[\"" + tags[random.nextInt(tags.length)] + "\"]");
            }
            
            posts.add(post);
        }
        
        return posts;
    }
    
    /**
     * 生成测试消息数据
     * @param users 用户列表
     * @param posts 帖子列表
     * @param avgMessagesPerPost 每个帖子平均消息数
     * @return 消息列表
     */
    public static List<ForumMessage> generateMessages(List<User> users, List<ForumPost> posts, int avgMessagesPerPost) {
        List<ForumMessage> messages = new ArrayList<>();
        
        for (ForumPost post : posts) {
            // 为每个帖子生成随机数量的回复
            int messageCount = Math.max(1, random.nextInt(avgMessagesPerPost * 2));
            
            for (int i = 0; i < messageCount; i++) {
                User author = users.get(random.nextInt(users.size()));
                String content = REPLY_CONTENTS[random.nextInt(REPLY_CONTENTS.length)];
                
                // 为回复内容添加一些变化
                if (random.nextBoolean()) {
                    content = content + " 我觉得楼主说得很有道理。";
                }
                if (random.nextBoolean() && i > 0) {
                    content = "@" + users.get(random.nextInt(users.size())).getNickname() + " " + content;
                }
                
                ForumMessage message = new ForumMessage(
                    post.getPostId(),
                    author.getUserId(),
                    author.getNickname(),
                    content
                );
                
                // 设置回复时间（在帖子发布时间之后）
                long postTime = post.getTimestamp();
                long replyTime = postTime + random.nextInt(7 * 24 * 60 * 60 * 1000); // 7天内随机时间
                message.setTimestamp(replyTime);
                
                // 随机设置点赞数
                message.setLikeCount(random.nextInt(20));
                
                // 检查是否为楼主回复
                if (author.getUserId().equals(post.getAuthorId())) {
                    message.setAuthorReply(true);
                }
                
                // 随机设置父消息ID（回复其他回复，20%概率）
                if (i > 0 && random.nextDouble() < 0.2 && !messages.isEmpty()) {
                    // 随机选择一个之前的消息作为父消息
                    List<ForumMessage> postMessages = new ArrayList<>();
                    for (ForumMessage msg : messages) {
                        if (msg.getPostId().equals(post.getPostId())) {
                            postMessages.add(msg);
                        }
                    }
                    if (!postMessages.isEmpty()) {
                        ForumMessage parentMessage = postMessages.get(random.nextInt(postMessages.size()));
                        message.setParentMessageId(parentMessage.getMessageId());
                    }
                }
                
                messages.add(message);
            }
        }
        
        return messages;
    }
    
    /**
     * 生成完整的论坛测试数据
     * @return 包含用户、帖子、消息的数据集合
     */
    public static ForumDataSet generateCompleteForumData() {
        // 生成500个用户
        List<User> users = generateUsers(500);
        
        // 生成1000个帖子
        List<ForumPost> posts = generatePosts(users, 1000);
        
        // 生成2000条回复消息
        List<ForumMessage> messages = generateMessages(users, posts, 2);
        
        return new ForumDataSet(users, posts, messages);
    }
    
    /**
     * 论坛数据集合类
     */
    public static class ForumDataSet {
        private List<User> users;
        private List<ForumPost> posts;
        private List<ForumMessage> messages;
        
        public ForumDataSet(List<User> users, List<ForumPost> posts, List<ForumMessage> messages) {
            this.users = users;
            this.posts = posts;
            this.messages = messages;
        }
        
        public List<User> getUsers() { return users; }
        public List<ForumPost> getPosts() { return posts; }
        public List<ForumMessage> getMessages() { return messages; }
        
        public int getTotalDataCount() {
            return users.size() + posts.size() + messages.size();
        }
        
        public String getDataSummary() {
            return String.format("用户: %d, 帖子: %d, 消息: %d, 总计: %d", 
                users.size(), posts.size(), messages.size(), getTotalDataCount());
        }
    }
    
    /**
     * 生成随机用户名
     */
    public static String generateRandomUsername() {
        return USERNAMES[random.nextInt(USERNAMES.length)] + random.nextInt(1000);
    }
    
    /**
     * 生成随机帖子标题
     */
    public static String generateRandomPostTitle() {
        return POST_TITLES[random.nextInt(POST_TITLES.length)];
    }
    
    /**
     * 生成随机帖子内容
     */
    public static String generateRandomPostContent() {
        return POST_CONTENTS[random.nextInt(POST_CONTENTS.length)];
    }
    
    /**
     * 生成随机回复内容
     */
    public static String generateRandomReplyContent() {
        return REPLY_CONTENTS[random.nextInt(REPLY_CONTENTS.length)];
    }
}