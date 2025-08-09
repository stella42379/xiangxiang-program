package com.xiangjia.locallife.utils;

import com.xiangjia.locallife.model.NewsItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class NewsDataGenerator {
    
    private static final List<String> TECHNOLOGY_NEWS = Arrays.asList(
        "智能城市建设取得新进展",
        "5G网络覆盖范围持续扩大", 
        "人工智能助力社区管理",
        "数字化服务平台正式上线"
    );
    
    private static final List<String> TRAFFIC_NEWS = Arrays.asList(
        "新增公交线路优化市民出行",
        "智能交通信号系统投入使用",
        "地铁建设项目进展顺利", 
        "共享单车服务区域扩展"
    );
    
    private static final List<String> ENVIRONMENT_NEWS = Arrays.asList(
        "垃圾分类工作成效显著",
        "绿化工程美化城市环境",
        "空气质量持续改善",
        "节能减排措施深入推进"
    );
    
    private static final List<String> MEDICAL_NEWS = Arrays.asList(
        "社区医院推出便民挂号服务",
        "健康体检活动惠及居民",
        "医疗设备更新提升服务质量",
        "专家义诊活动定期举办"
    );
    
    private static final List<String> SAFETY_NEWS = Arrays.asList(
        "消防安全演练进社区",
        "安全隐患排查工作加强",
        "应急预案体系不断完善",
        "安全教育宣传活动开展"
    );
    
    private static final List<String> SOURCES = Arrays.asList(
        "本地新闻", "社区快报", "城市日报", "生活周刊", "民生新闻"
    );
    
    private static final List<String> TIME_OPTIONS = Arrays.asList(
        "刚刚", "30分钟前", "1小时前", "3小时前", "5小时前", "1天前"
    );
    
    private static final List<String> CATEGORIES = Arrays.asList(
        "technology", "traffic", "environment", "medical", "safety"
    );
    
    /**
     * 生成新闻数据
     */
    public static List<NewsItem> generateNewsData() {
        List<NewsItem> newsList = new ArrayList<>();
        Random random = new Random();
        
        List<List<String>> allNewsCategories = Arrays.asList(
            TECHNOLOGY_NEWS, TRAFFIC_NEWS, ENVIRONMENT_NEWS, MEDICAL_NEWS, SAFETY_NEWS
        );
        
        int newsId = 0;
        for (int categoryIndex = 0; categoryIndex < allNewsCategories.size(); categoryIndex++) {
            List<String> categoryNews = allNewsCategories.get(categoryIndex);
            String category = CATEGORIES.get(categoryIndex);
            
            for (int i = 0; i < categoryNews.size(); i++) {
                String title = categoryNews.get(i);
                String source = SOURCES.get(newsId % SOURCES.size());
                String time = TIME_OPTIONS.get(newsId % TIME_OPTIONS.size());
                String thumbnail = generateThumbnailUrl(newsId + 100);
                
                NewsItem newsItem = new NewsItem(
                    newsId,
                    title,
                    source,
                    "#",
                    thumbnail,
                    time,
                    category
                );
                
                // 生成新闻内容
                newsItem.setContent(generateNewsContent(title));
                
                newsList.add(newsItem);
                newsId++;
            }
        }
        
        return newsList;
    }
    
    /**
     * 生成更多新闻数据
     */
    public static List<NewsItem> generateMoreNews(int startId) {
        List<NewsItem> moreNews = new ArrayList<>();
        Random random = new Random();
        
        // 随机生成5条新闻
        for (int i = 0; i < 5; i++) {
            int categoryIndex = random.nextInt(CATEGORIES.size());
            String category = CATEGORIES.get(categoryIndex);
            
            List<String> categoryNewsList;
            switch (category) {
                case "technology": categoryNewsList = TECHNOLOGY_NEWS; break;
                case "traffic": categoryNewsList = TRAFFIC_NEWS; break;
                case "environment": categoryNewsList = ENVIRONMENT_NEWS; break;
                case "medical": categoryNewsList = MEDICAL_NEWS; break;
                default: categoryNewsList = SAFETY_NEWS; break;
            }
            
            String title = categoryNewsList.get(random.nextInt(categoryNewsList.size()));
            String source = SOURCES.get(random.nextInt(SOURCES.size()));
            String time = TIME_OPTIONS.get(random.nextInt(TIME_OPTIONS.size()));
            String thumbnail = generateThumbnailUrl(startId + i + 200);
            
            NewsItem newsItem = new NewsItem(
                startId + i,
                title + " (更多)",
                source,
                "#",
                thumbnail,
                time,
                category
            );
            
            newsItem.setContent(generateNewsContent(title));
            moreNews.add(newsItem);
        }
        
        return moreNews;
    }
    
    /**
     * 生成缩略图URL
     */
    private static String generateThumbnailUrl(int seed) {
        return "https://picsum.photos/400/300?random=" + seed;
    }
    
    /**
     * 生成新闻内容
     */
    public static String generateNewsContent(String title) {
        if (title.contains("台风") || title.contains("天气")) {
            return "根据最新气象信息，" + title + "。\n\n气象部门提醒广大市民：\n1. 及时关注天气预报和预警信息\n2. 做好防范准备，减少外出\n3. 加固门窗，清理阳台物品\n4. 储备必要的生活用品\n\n相关部门已启动应急预案，各项防范措施正在有序进行中。市民如遇紧急情况，请及时拨打相关部门电话求助。\n\n我们将持续关注天气变化，为您带来最新报道。";
        } else if (title.contains("交通") || title.contains("出行")) {
            return title + "，将为市民出行带来便利。\n\n主要内容包括：\n1. 优化公交线路，提高运行效率\n2. 完善交通标识，方便市民出行\n3. 加强交通管理，确保道路畅通\n4. 推广智能交通系统\n\n交通部门表示，此次调整旨在更好地服务市民，提升出行体验。相关措施将分阶段实施，确保平稳过渡。\n\n市民如有意见建议，可通过官方渠道反馈。";
        } else if (title.contains("智能") || title.contains("科技") || title.contains("数字")) {
            return title + "，标志着我市在科技创新方面取得新突破。\n\n项目特点：\n1. 运用先进技术，提升服务效率\n2. 注重用户体验，操作简单便捷\n3. 数据安全可靠，保护用户隐私\n4. 可持续发展，绿色环保\n\n相关负责人表示，这一创新成果将为市民生活带来更多便利，推动城市数字化转型。\n\n未来还将继续加大科技投入，为市民提供更多智能化服务。";
        } else if (title.contains("环保") || title.contains("绿色") || title.contains("垃圾")) {
            return title + "，体现了我市对环境保护的重视。\n\n主要措施：\n1. 加强环境监测，确保空气质量\n2. 推广绿色出行，减少碳排放\n3. 完善垃圾分类，提高回收利用率\n4. 保护生态环境，维护生物多样性\n\n环保部门呼吁广大市民积极参与环保行动，共同建设美丽家园。每个人的小行动都能汇聚成保护环境的大力量。\n\n相关环保政策将持续完善，为可持续发展提供有力保障。";
        } else if (title.contains("医院") || title.contains("医疗") || title.contains("健康")) {
            return title + "，为市民健康保驾护航。\n\n服务内容：\n1. 提供便民挂号服务，减少排队时间\n2. 优化就医流程，提升医疗效率\n3. 加强医护培训，提高服务质量\n4. 完善医疗设备，保障诊疗效果\n\n医院方面表示，将继续以患者为中心，不断改善医疗服务质量，让市民看病更方便、更安心。\n\n如有就医需求，建议提前预约，合理安排时间。";
        } else if (title.contains("安全") || title.contains("消防") || title.contains("应急")) {
            return title + "，确保市民生命财产安全。\n\n安全要点：\n1. 定期检查安全设施，消除隐患\n2. 加强安全教育，提高防范意识\n3. 完善应急预案，快速响应处置\n4. 强化监督检查，确保措施落实\n\n安全部门提醒：安全无小事，预防最重要。市民在日常生活中要时刻注意安全，发现隐患及时报告。\n\n相关部门将持续加强安全监管，为市民创造安全稳定的生活环境。";
        } else {
            return title + "，引起了广泛关注。\n\n据了解，此次事件涉及多个方面，相关部门正在积极处理。具体情况如下：\n\n1. 相关负责人高度重视，第一时间作出部署\n2. 各部门密切配合，形成工作合力\n3. 及时向公众通报进展，保持信息透明\n4. 持续跟进处理，确保问题得到妥善解决\n\n市民朋友如有相关问题，可通过官方渠道进行咨询。我们将持续关注此事，并为您带来最新报道。";
        }
    }
}