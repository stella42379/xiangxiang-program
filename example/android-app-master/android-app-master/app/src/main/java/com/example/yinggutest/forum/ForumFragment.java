package com.example.yinggutest.forum;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.yinggutest.R;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import com.example.yinggutest.forum.adapter.ForumFragmentAdapter;
import com.example.yinggutest.forum.data.ForumData;
import com.example.yinggutest.forum.data.sSaveForumData;
import com.example.yinggutest.forum.entity.AnForumDataInfo;

public class ForumFragment extends Fragment {

    private static final String TAG = "ForumFragment";
    private static final String USERNAME = "username";

    //中间内容部分
    private RecyclerView rvContent;
    //用来存储论坛内容的数据
    private List<AnForumDataInfo> dataItems = new ArrayList<>();
    private ImageView ivAdd;

    private NewsReleaseEntity newsReleaseEntity;
    private SmartRefreshLayout srlRelease;
    private ForumFragmentAdapter forumFragmentAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_forum_page, container, false);
        srlRelease = inflate.findViewById(R.id.srl_news_release);
        rvContent = inflate.findViewById(R.id.rv_content_fragment_forum_page);
        ivAdd = inflate.findViewById(R.id.iv_add_fragment_forum_page);
        return inflate;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        srlRelease.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {


                Toast.makeText(getContext(),"刷新成功！",Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onRefresh: 刷新成功！" );
                refreshLayout.finishRefresh(1000);
            }
        });
        //“发动态”按钮点击事件
        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"跳转到添加界面",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), NewsReleaseActivity.class);
//                startActivityForResult(intent,1);
                startActivity(intent);


            }
        });

        dataItems.clear();

        for(int i = 0;i<ForumData.userName.length;i++){
            AnForumDataInfo anForumDataInfo = new AnForumDataInfo();
            anForumDataInfo.setUserName(ForumData.userName[i]);
            anForumDataInfo.setUserContent(ForumData.userContent[i]);
            anForumDataInfo.setUserImageUrl(ForumData.userImageUrl[i]);
            anForumDataInfo.setUserTime(ForumData.userTime[i]);
            anForumDataInfo.setUserNumber(ForumData.userNumber[i]);
            dataItems.add(anForumDataInfo);
        }
        sSaveForumData.sAnforumdataInfo.clear();
        //添加论坛内容到集合中
        sSaveForumData.sAnforumdataInfo.addAll(dataItems);



        rvContent.setLayoutManager(new LinearLayoutManager(getContext()));
        forumFragmentAdapter = new ForumFragmentAdapter(R.layout.item_fragment_forum_page, sSaveForumData.sAnforumdataInfo);
        rvContent.setAdapter(forumFragmentAdapter);

        //设置条目点击事件
        forumFragmentAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                AnForumDataInfo anForumDataInfo = sSaveForumData.sAnforumdataInfo.get(position);
                boolean like = anForumDataInfo.isLike();

                if (view.getId() == R.id.tv_content_item_fragment_forum_page) {  //文章内容的点击事件
                    //跳转到详情页面
                    Intent intent = new Intent(getContext(),ForumFragmentSencond.class);
                    intent.putExtra(USERNAME,position);
                    startActivity(intent);
                }else if(view.getId() == R.id.iv_like_item_fragment_forum_page){  //“点赞”的点击事件
                    if (like){
                        ImageView ivLike = view.findViewById(R.id.iv_like_item_fragment_forum_page);
                        Glide.with(getContext()).load(R.drawable.ic_unlike).placeholder(R.drawable.ic_unlike).into(ivLike);
                        int  i = sSaveForumData.sAnforumdataInfo.get(position).getUserNumber();
                        int number = i - 1;
                        sSaveForumData.sAnforumdataInfo.get(position)
                                .setUserNumber(number);
                        forumFragmentAdapter.notifyDataSetChanged();
                    }else {
                        ImageView ivLike = view.findViewById(R.id.iv_like_item_fragment_forum_page);
                        Glide.with(getContext()).load(R.drawable.ic_like).placeholder(R.drawable.ic_like).into(ivLike);
                        int  i = sSaveForumData.sAnforumdataInfo.get(position).getUserNumber();
                        int number = i + 1;
                        sSaveForumData.sAnforumdataInfo.get(position)
                                .setUserNumber(number);
                        forumFragmentAdapter.notifyDataSetChanged();

                    }
                    anForumDataInfo.setLike(!anForumDataInfo.isLike());

                }
            }
        });
    }


//重写Fragment的onResume()方法，更新数据
    @Override
    public void onResume() {
        super.onResume();
        if(NewsReleaseEntity.article != null){
            AnForumDataInfo dataInfo = new AnForumDataInfo();
            dataInfo.setUserName(NewsReleaseEntity.userName);
            dataInfo.setUserContent(NewsReleaseEntity.article);
            dataInfo.setUserImageUrl(NewsReleaseEntity.pictureUrl);
            dataInfo.setUserTime(NewsReleaseEntity.time);
            dataInfo.setUserNumber(NewsReleaseEntity.hits);
            sSaveForumData.sAnforumdataInfo.clear();
            sSaveForumData.sAnforumdataInfo.add(dataInfo);
            sSaveForumData.sAnforumdataInfo.addAll(dataItems);
            dataItems = sSaveForumData.sAnforumdataInfo;
            Log.e(TAG, "onResume: 更新数据成功 " + sSaveForumData.sAnforumdataInfo.size());
            forumFragmentAdapter.notifyDataSetChanged();
            Toast.makeText(getContext(),"已刷新数据",Toast.LENGTH_SHORT).show();
        }

    }
}
