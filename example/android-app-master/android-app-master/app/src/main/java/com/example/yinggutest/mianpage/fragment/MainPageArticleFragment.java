package com.example.yinggutest.mianpage.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.yinggutest.R;
import com.example.yinggutest.Util.GlideImageLoader;
import com.example.yinggutest.Util.HandlerUtil;
import com.example.yinggutest.Util.OkHttpUtil;
import com.example.yinggutest.Util.ToastUtil;
import com.example.yinggutest.constant.TemporaryApiInterface;
import com.example.yinggutest.mianpage.fragment.everypractice.AnalogyExaminationActivity;
import com.example.yinggutest.mianpage.fragment.featureicon.FeatureAdapter;
import com.example.yinggutest.mianpage.fragment.featureicon.FeatureIconData;
import com.example.yinggutest.mianpage.fragment.featureicon.FeatureIconEntity;
import com.example.yinggutest.mianpage.fragment.featureicon.SaveFeatureIconData;
import com.example.yinggutest.mianpage.fragment.simulation.SimulationActivity;
import com.example.yinggutest.mianpage.fragment.truetitle.TrueTitleActivity;
import com.example.yinggutest.mianpage.fragment.wrongtopic.WrongTopicBook;
import com.example.yinggutest.temporaryentity.TemporaryBannerEntity;
import com.example.yinggutest.temporaryentity.WXDataEntity;
import com.google.gson.Gson;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.scwang.smartrefresh.layout.util.DensityUtil.dp2px;

//题库内容部分的fragment
public class MainPageArticleFragment extends Fragment {

    public static final String ID = "id";
    public static final String NAME = "name";

    //页面刷新控件
    private SmartRefreshLayout mRefreshLayout;
    //内容的recyclerView
    private RecyclerView mRecyclerView;
    private Context mContext;
    //根据id匹配数据
    private int id;
    //默认当前页面位置为1
    private int mCurrentPage = 1;
    //默认当前刷新状态
    private boolean isRefresh = true;
    //recyclerView内容页面适配器
    private MainPageArticleAdapter mAdapter;
    //顶部广告轮播图
    private Banner mBanner;
    //用来存储广告图片地址
    private List<String> list_path = new ArrayList<>();
    //用来存储广告标题
    private List<String> list_title = new ArrayList<>();
    //用来存储banner的url
    private List<String> list_url = new ArrayList<>();
    //题库页面用于存放功能按钮的recyclerView
    private RecyclerView mHeadRecycler;
    //用于存放功能图标的数据
    List<FeatureIconEntity> featureList = new ArrayList<>();

    public static MainPageArticleFragment newInstance(int id,String name){
        MainPageArticleFragment mainPageArticleFragment = new MainPageArticleFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ID,id);
        bundle.putString(NAME,name);
        mainPageArticleFragment.setArguments(bundle);
        return mainPageArticleFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_main_page_article, container, false);
        mRefreshLayout = inflate.findViewById(R.id.srl_fragment_main_page_article);
        mRecyclerView = inflate.findViewById(R.id.rv_article_fragment_main_page_article);
        return inflate;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getContext();
        //上拉加载，下拉刷新
        setRefresh();
        Bundle arguments = getArguments();
        if (arguments != null) {
            id = arguments.getInt(ID);
        }
        //初始化recyclerView
        initRecyclerView();
        mCurrentPage = 1;

        //根据id，currentPage获取文章内容
        getWxDetailData(id, mCurrentPage);
        //添加功能图标
        SaveFeatureIconData.sFeatureIconDataInfo.clear();
        for (int i = 0;i < FeatureIconData.featureName.length;i++){
            FeatureIconEntity featureIconEntity = new FeatureIconEntity();
            featureIconEntity.setFeatureIcon(FeatureIconData.featureIcon[i]);
            featureIconEntity.setFeatureName(FeatureIconData.featureName[i]);
            SaveFeatureIconData.sFeatureIconDataInfo.add(featureIconEntity);
        }
    }

    private void setRefresh() {
        mRefreshLayout.setPrimaryColorsId(R.color.course_background,R.color.white);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mCurrentPage = 1;
                if (id != 0) {
                    isRefresh = true;
                    getWxDetailData(id,0);
                }
                refreshLayout.finishRefresh(1000);
            }
        });

        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mCurrentPage++;
                if (id != 0) {
                    isRefresh = false;
                    getWxDetailData(id,mCurrentPage);
                }
                refreshLayout.finishLoadMore(1000);
            }
        });
    }

    private void initRecyclerView() {
        mAdapter = new MainPageArticleAdapter(R.layout.item_activity_sub_page, null);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);

        View mBannerView = getLayoutInflater().inflate(R.layout.item_head_banner, mRecyclerView, false);
        mBanner = mBannerView.findViewById(R.id.head_banner);
        mHeadRecycler = mBannerView.findViewById(R.id.head_recyclerview);
        mHeadRecycler.setLayoutManager(new GridLayoutManager(mContext,3));
        mHeadRecycler.setHasFixedSize(true);
        FeatureAdapter featureAdapter = new FeatureAdapter(R.layout.item_feature, SaveFeatureIconData.sFeatureIconDataInfo);
        mHeadRecycler.setAdapter(featureAdapter);
        featureAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (position){
                    case 0:
                        startActivity(new Intent(mContext, AnalogyExaminationActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(mContext, SimulationActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(mContext, TrueTitleActivity.class));
                        break;
                    case 3:
                        startActivity(new Intent(mContext, WrongTopicBook.class));
                        break;
                }
            }
        });
        initBannerView();

        mAdapter.setHeaderView(mBannerView);
    }

    private void initBannerView() {
        OkHttpUtil.enqueue(TemporaryApiInterface.BANNER_URL, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                TemporaryBannerEntity bannerEntity = new Gson().fromJson(string, TemporaryBannerEntity.class);
                if (bannerEntity.getErrorCode() == 0) {
                    list_path.clear();
                    list_title.clear();
                    list_url.clear();
                    List<TemporaryBannerEntity.DataBean> data = bannerEntity.getData();
                    for (int i =0;i<data.size();i++){
                        TemporaryBannerEntity.DataBean dataBean = data.get(i);
                        String imagePath = dataBean.getImagePath();
                        String title = dataBean.getTitle();
                        String url = dataBean.getUrl();
                        list_path.add(imagePath);
                        list_title.add(title);
                        list_url.add(url);
                    }
                    HandlerUtil.post(new Runnable() {
                        @Override
                        public void run() {
                            //设置banner样式
                            mBanner.setBannerStyle(BannerConfig.NUM_INDICATOR_TITLE);
                            //设置图片加载器
                            mBanner.setImageLoader(new GlideImageLoader());
                            //设置图片地址
                            mBanner.setImages(list_path);
                            //设置轮播的动画效果
                            mBanner.setBannerAnimation(Transformer.Default);
                            //设置轮播标题
                            mBanner.setBannerTitles(list_title);
                            //设置轮播间隔
                            mBanner.setDelayTime(2000);
                            //设置是否自动轮播
                            mBanner.isAutoPlay(true);
                            //设置轮播底部指示器样式
                            mBanner.setIndicatorGravity(BannerConfig.CENTER);
                            //设置轮播点击事件
                            mBanner.setOnBannerListener(new OnBannerListener() {
                                @Override
                                public void OnBannerClick(int position) {
                                    String format = String.format("标题：%s，跳转的url：%s", list_title.get(position), list_url.get(position));
                                    ToastUtil.showToast(mContext,format);
                                }
                            });
                            //开始轮播
                            mBanner.start();
                        }
                    });
                }
            }
        });
    }

    private void getWxDetailData(int id, int currentPage) {
        String url = String.format(Locale.CHINA, TemporaryApiInterface.PUBLIC_ARTICLE_URL, id, currentPage);
        Log.e("aa",url);
        OkHttpUtil.enqueue(url, new OkHttpUtil.MainThreadCallBack() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("aa","这里");
            }

            @Override
            public void onResponse(Call call, String result) {
                Gson gson = new Gson();
                WXDataEntity wxDataEntity = gson.fromJson(result, WXDataEntity.class);
                if (wxDataEntity.getErrorCode() == 0) {
                    WXDataEntity.DataBean wxSumData = wxDataEntity.getData();
                    if (isRefresh) {
                        mAdapter.replaceData(wxSumData.getDatas());
                    } else {
                        if (wxSumData.getDatas().size() > 0) {
                            mAdapter.addData(wxSumData.getDatas());
                        } else {
                            ToastUtil.showToast(getContext(), getString(R.string.load_more_no_data));
                        }
                    }
                }
            }
        });
    }
}
