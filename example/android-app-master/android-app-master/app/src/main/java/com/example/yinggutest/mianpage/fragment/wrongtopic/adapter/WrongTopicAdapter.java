package com.example.yinggutest.mianpage.fragment.wrongtopic.adapter;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.yinggutest.R;
import com.example.yinggutest.Util.ConstantUtil;
import com.example.yinggutest.Util.SaveQuestionData;
import com.example.yinggutest.mianpage.fragment.wrongtopic.WrongTopic;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WrongTopicAdapter extends PagerAdapter {

    private static final String TAG = "AnalusisQuestionErrorAd";
    WrongTopic mContext;
    // 传递过来的页面view的集合
    List<View> viewItems;
    // 每个item的页面view
    View convertView;
    // 传递过来的第几条数据
    int dataItems;

    String imgServerUrl="";

    private Map<Integer, Boolean> map = new HashMap<Integer, Boolean>();
    private Map<Integer, Boolean> mapClick = new HashMap<Integer, Boolean>();
    private Map<Integer, String> mapMultiSelect = new HashMap<Integer, String>();

    boolean isClick=false;

    boolean isNext = false;

    StringBuffer answer=new StringBuffer();
    StringBuffer answerLast=new StringBuffer();
    StringBuffer answer1=new StringBuffer();

    //DBManager dbManager;

    String isCorrect= ConstantUtil.isCorrect;//1对，0错

    int errortopicNum=0;

    String resultA="";
    String resultB="";
    String resultC="";
    String resultD="";
    String resultE="";

    public WrongTopicAdapter(WrongTopic context, List<View> viewItems, int dataItems, String imgServerUrl) {
        mContext = context;
        this.viewItems = viewItems;
        this.dataItems = dataItems;
        this.imgServerUrl = imgServerUrl;
	/*	dbManager = new DBManager(context);
		dbManager.openDB();*/
        Log.e(TAG, "AnalusisQuestionErrorAdapter: "+viewItems.size() );
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(viewItems.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        final ViewHolder holder = new ViewHolder();
        convertView = viewItems.get(position);

        holder.questionType = (TextView) convertView.findViewById(R.id.activity_prepare_test_no);
        holder.question = (TextView) convertView.findViewById(R.id.activity_prepare_test_question);
        holder.previousBtn = (LinearLayout) convertView.findViewById(R.id.activity_prepare_test_upLayout);
        holder.nextBtn = (LinearLayout) convertView.findViewById(R.id.activity_prepare_test_nextLayout);
        holder.nextText = (TextView) convertView.findViewById(R.id.menu_bottom_nextTV);
        holder.totalText = (TextView) convertView.findViewById(R.id.activity_prepare_test_totalTv);
        holder.nextImage = (ImageView) convertView.findViewById(R.id.menu_bottom_nextIV);
        holder.wrongLayout = (LinearLayout) convertView.findViewById(R.id.activity_prepare_test_wrongLayout);
        holder.explaindetailTv = (TextView) convertView.findViewById(R.id.activity_prepare_test_explaindetail);
        holder.layoutA=(LinearLayout) convertView.findViewById(R.id.activity_prepare_test_layout_a);
        holder.layoutB=(LinearLayout) convertView.findViewById(R.id.activity_prepare_test_layout_b);
        holder.layoutC=(LinearLayout) convertView.findViewById(R.id.activity_prepare_test_layout_c);
        holder.layoutD=(LinearLayout) convertView.findViewById(R.id.activity_prepare_test_layout_d);
        holder.layoutE=(LinearLayout) convertView.findViewById(R.id.activity_prepare_test_layout_e);
        holder.ivA=(ImageView) convertView.findViewById(R.id.vote_submit_select_image_a);
        holder.ivB=(ImageView) convertView.findViewById(R.id.vote_submit_select_image_b);
        holder.ivC=(ImageView) convertView.findViewById(R.id.vote_submit_select_image_c);
        holder.ivD=(ImageView) convertView.findViewById(R.id.vote_submit_select_image_d);
        holder.ivE=(ImageView) convertView.findViewById(R.id.vote_submit_select_image_e);
        holder.tvA=(TextView) convertView.findViewById(R.id.vote_submit_select_text_a);
        holder.tvB=(TextView) convertView.findViewById(R.id.vote_submit_select_text_b);
        holder.tvC=(TextView) convertView.findViewById(R.id.vote_submit_select_text_c);
        holder.tvD=(TextView) convertView.findViewById(R.id.vote_submit_select_text_d);
        holder.tvE=(TextView) convertView.findViewById(R.id.vote_submit_select_text_e);
        holder.ivA_=(ImageView) convertView.findViewById(R.id.vote_submit_select_image_a_);
        holder.ivB_=(ImageView) convertView.findViewById(R.id.vote_submit_select_image_b_);
        holder.ivC_=(ImageView) convertView.findViewById(R.id.vote_submit_select_image_c_);
        holder.ivD_=(ImageView) convertView.findViewById(R.id.vote_submit_select_image_d_);
        holder.ivE_=(ImageView) convertView.findViewById(R.id.vote_submit_select_image_e_);
        holder.ivTimu = (ImageView)convertView.findViewById(R.id.iv_timu);

        if (SaveQuestionData.sSaveAllErrorQuestionList.get(dataItems).getQuestionType().equals("0")) {
            holder.question.setText("(单选题) "+SaveQuestionData.sSaveAllErrorQuestionList.get(dataItems).getQuestionName());
        }
        holder.tvA.setText("A." + SaveQuestionData.sSaveAllErrorQuestionList.get(dataItems).getOptionA());
        holder.tvB.setText("B." + SaveQuestionData.sSaveAllErrorQuestionList.get(dataItems).getOptionB());
        holder.tvC.setText("C." + SaveQuestionData.sSaveAllErrorQuestionList.get(dataItems).getOptionC());
        holder.tvD.setText("D." + SaveQuestionData.sSaveAllErrorQuestionList.get(dataItems).getOptionD());

        if ("1".equals(SaveQuestionData.sSaveAllErrorQuestionList.get(dataItems).getOptionType())){
            //加载图片、
            //holder.ivTimu.setVisibility(View.VISIBLE);
            String url = SaveQuestionData.sSaveAllErrorQuestionList.get(dataItems).getUrl();
            Glide.with(mContext).load(url).into(holder.ivTimu);
        }

        String questionAnswer = SaveQuestionData.sSaveAllErrorQuestionList.get(dataItems).getQuestionAnswer();
        holder.layoutA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (questionAnswer.equals("A")) {
                    holder.ivA.setImageResource(R.mipmap.ic_practice_test_right);
                }else {
                    holder.ivA.setImageResource(R.mipmap.ic_practice_test_wrong);
                }
                holder.wrongLayout.setVisibility(View.VISIBLE);
                holder.layoutA.setEnabled(false);
                holder.layoutB.setEnabled(false);
                holder.layoutC.setEnabled(false);
                holder.layoutD.setEnabled(false);
            }
        });
        holder.layoutB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (questionAnswer.equals("B")) {
                    holder.ivB.setImageResource(R.mipmap.ic_practice_test_right);
                }else {
                    holder.ivB.setImageResource(R.mipmap.ic_practice_test_wrong);
                }
                holder.wrongLayout.setVisibility(View.VISIBLE);
                holder.layoutA.setEnabled(false);
                holder.layoutB.setEnabled(false);
                holder.layoutC.setEnabled(false);
                holder.layoutD.setEnabled(false);
            }
        });
        holder.layoutC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (questionAnswer.equals("C")) {
                    holder.ivC.setImageResource(R.mipmap.ic_practice_test_right);
                }else {
                    holder.ivC.setImageResource(R.mipmap.ic_practice_test_wrong);
                }
                holder.wrongLayout.setVisibility(View.VISIBLE);
                holder.layoutA.setEnabled(false);
                holder.layoutB.setEnabled(false);
                holder.layoutC.setEnabled(false);
                holder.layoutD.setEnabled(false);
            }
        });
        holder.layoutD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (questionAnswer.equals("D")) {
                    holder.ivD.setImageResource(R.mipmap.ic_practice_test_right);
                }else {
                    holder.ivD.setImageResource(R.mipmap.ic_practice_test_wrong);
                }
                holder.wrongLayout.setVisibility(View.VISIBLE);
                holder.layoutA.setEnabled(false);
                holder.layoutB.setEnabled(false);
                holder.layoutC.setEnabled(false);
                holder.layoutD.setEnabled(false);
            }
        });



        holder.explaindetailTv.setText(SaveQuestionData.sSaveAllErrorQuestionList.get(dataItems).getAnalysis());

        container.addView(viewItems.get(position));
        return viewItems.get(position);
    }



    @Override
    public int getCount() {
        if (viewItems == null)
            return 0;
        return viewItems.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }



    public class ViewHolder {
        TextView questionType;
        TextView question;
        LinearLayout previousBtn, nextBtn,errorBtn;
        TextView nextText;
        TextView totalText;
        ImageView nextImage;
        LinearLayout wrongLayout;
        TextView explaindetailTv;
        LinearLayout layoutA;
        LinearLayout layoutB;
        LinearLayout layoutC;
        LinearLayout layoutD;
        LinearLayout layoutE;
        ImageView ivA;
        ImageView ivB;
        ImageView ivC;
        ImageView ivD;
        ImageView ivE;
        TextView tvA;
        TextView tvB;
        TextView tvC;
        TextView tvD;
        TextView tvE;
        ImageView ivA_;
        ImageView ivB_;
        ImageView ivC_;
        ImageView ivD_;
        ImageView ivE_;
        ImageView ivTimu;

    }

}

