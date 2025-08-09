package com.xiangjia.locallife.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.xiangjia.locallife.R;
import com.xiangjia.locallife.utils.NewsDataGenerator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 新闻详情页面 - 完全按照小程序设计实现
 */
public class NewsDetailActivity extends AppCompatActivity {
    
    private ImageView backButton;
    private TextView titleText;
    private ImageView shareButton;
    
    private TextView newsTitle;
    private TextView newsSource;
    private TextView newsTime;
    private ImageView newsImage;
    private TextView newsContent;
    
    private FloatingActionButton collectFab;
    private FloatingActionButton shareFab;
    private FloatingActionButton reportFab;
    
    private String currentNewsTitle;
    private String currentNewsSource;
    private String currentNewsUrl;
    private String currentNewsThumbnail;
    private String currentNewsTime;
    private String currentNewsCategory;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        
        initViews();
        setupClickListeners();
        loadNewsData();
    }
    
    /**
     * 初始化视图
     */
    private void initViews() {
        // 顶部导航栏
        backButton = findViewById(R.id.back_button);
        titleText = findViewById(R.id.title_text);
        shareButton = findViewById(R.id.share_button);
        titleText.setText("新闻详情");
        
        // 新闻内容
        newsTitle = findViewById(R.id.news_title);
        newsSource = findViewById(R.id.news_source);
        newsTime = findViewById(R.id.news_time);
        newsImage = findViewById(R.id.news_image);
        newsContent = findViewById(R.id.news_content);
        
        // 操作按钮
        collectFab = findViewById(R.id.collect_fab);
        shareFab = findViewById(R.id.share_fab);
        reportFab = findViewById(R.id.report_fab);
        
        // 应用毛玻璃效果
        setupGlassEffect();
    }
    
    /**
     * 设置毛玻璃效果
     */
    private void setupGlassEffect() {
        // 为各个卡片视图应用毛玻璃背景
        View headerCard = findViewById(R.id.news_header_card);
        View imageCard = findViewById(R.id.news_image_card);
        View contentCard = findViewById(R.id.news_content_card);
        View actionsCard = findViewById(R.id.news_actions_card);
        
        if (headerCard != null) {
            headerCard.setBackground(getDrawable(R.drawable.bg_glass_card));
        }
        if (imageCard != null) {
            imageCard.setBackground(getDrawable(R.drawable.bg_glass_card));
        }
        if (contentCard != null) {
            contentCard.setBackground(getDrawable(R.drawable.bg_glass_card));
        }
        if (actionsCard != null) {
            actionsCard.setBackground(getDrawable(R.drawable.bg_glass_card));
        }
    }
    
    /**
     * 设置点击监听
     */
    private void setupClickListeners() {
        backButton.setOnClickListener(v -> finish());
        
        shareButton.setOnClickListener(v -> shareNews());
        
        collectFab.setOnClickListener(v -> collectNews());
        
        shareFab.setOnClickListener(v -> shareNews());
        
        reportFab.setOnClickListener(v -> reportNews());
    }
    
    /**
     * 加载新闻数据
     */
    private void loadNewsData() {
        Intent intent = getIntent();
        currentNewsTitle = intent.getStringExtra("news_title");
        currentNewsSource = intent.getStringExtra("news_source");
        currentNewsUrl = intent.getStringExtra("news_url");
        currentNewsThumbnail = intent.getStringExtra("news_thumbnail");
        currentNewsTime = intent.getStringExtra("news_time");
        currentNewsCategory = intent.getStringExtra("news_category");
        
        // 设置新闻信息
        if (currentNewsTitle != null) {
            newsTitle.setText(currentNewsTitle);
        }
        
        if (currentNewsSource != null) {
            newsSource.setText(currentNewsSource);
        } else {
            newsSource.setText("未知来源");
        }
        
        if (currentNewsTime != null) {
            newsTime.setText(currentNewsTime);
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm", Locale.getDefault());
            newsTime.setText(sdf.format(new Date()));
        }
        
        // 加载新闻图片
        if (currentNewsThumbnail != null && !currentNewsThumbnail.isEmpty()) {
            RequestOptions requestOptions = new RequestOptions()
                    .transform(new RoundedCorners(24))
                    .placeholder(R.drawable.placeholder_news)
                    .error(R.drawable.placeholder_news);
            
            Glide.with(this)
                    .load(currentNewsThumbnail)
                    .apply(requestOptions)
                    .into(newsImage);
            newsImage.setVisibility(View.VISIBLE);
        } else {
            newsImage.setVisibility(View.GONE);
        }
        
        // 生成新闻内容
        if (currentNewsTitle != null) {
            String content = NewsDataGenerator.generateNewsContent(currentNewsTitle);
            newsContent.setText(content);
        } else {
            newsContent.setText("暂无新闻内容。");
        }
        
        // 检查收藏状态
        updateCollectButton();
    }
    
    /**
     * 收藏新闻
     */
    private void collectNews() {
        if (currentNewsTitle == null) return;
        
        SharedPreferences prefs = getSharedPreferences("news_collect", MODE_PRIVATE);
        String collectKey = "collect_" + currentNewsTitle.hashCode();
        
        boolean isCollected = prefs.getBoolean(collectKey, false);
        
        if (!isCollected) {
            // 添加收藏
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(collectKey, true);
            editor.putString(collectKey + "_title", currentNewsTitle);
            editor.putString(collectKey + "_source", currentNewsSource);
            editor.putString(collectKey + "_url", currentNewsUrl);
            editor.putString(collectKey + "_thumbnail", currentNewsThumbnail);
            editor.putString(collectKey + "_time", currentNewsTime);
            editor.putLong(collectKey + "_collect_time", System.currentTimeMillis());
            editor.apply();
            
            Toast.makeText(this, "收藏成功！", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "已经收藏过了！", Toast.LENGTH_SHORT).show();
        }
        
        updateCollectButton();
    }
    
    /**
     * 更新收藏按钮状态
     */
    private void updateCollectButton() {
        if (currentNewsTitle == null) return;
        
        SharedPreferences prefs = getSharedPreferences("news_collect", MODE_PRIVATE);
        String collectKey = "collect_" + currentNewsTitle.hashCode();
        boolean isCollected = prefs.getBoolean(collectKey, false);
        
        if (isCollected) {
            collectFab.setImageResource(R.drawable.ic_favorite_filled);
            collectFab.setColorFilter(getColor(R.color.accent_color));
        } else {
            collectFab.setImageResource(R.drawable.ic_favorite_border);
            collectFab.setColorFilter(getColor(R.color.text_secondary));
        }
    }
    
    /**
     * 分享新闻
     */
    private void shareNews() {
        if (currentNewsTitle == null) return;
        
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "分享新闻");
        shareIntent.putExtra(Intent.EXTRA_TEXT, 
            currentNewsTitle + "\n来自：" + (currentNewsSource != null ? currentNewsSource : "本地新闻"));
        
        startActivity(Intent.createChooser(shareIntent, "分享到"));
    }
    
    /**
     * 举报新闻
     */
    private void reportNews() {
        String[] reasons = {"内容不实", "涉及违法", "垃圾信息", "其他原因"};
        
        new AlertDialog.Builder(this)
                .setTitle("举报原因")
                .setItems(reasons, (dialog, which) -> {
                    Toast.makeText(this, "举报成功！感谢您的举报，我们会及时处理。", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("取消", null)
                .show();
    }
}