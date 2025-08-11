package com.example.yinggutest.mianpage.fragment.everypractice.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.yinggutest.AnalusisQuestion;
import com.example.yinggutest.R;
import com.example.yinggutest.Util.ConstantUtil;
import com.example.yinggutest.mianpage.fragment.bean.SaveQuestionInfo;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnalusisQuestionAdapter extends PagerAdapter {

    AnalusisQuestion mContext;
    // 传递过来的页面view的集合
    List<View> viewItems;
    // 每个item的页面view
    View convertView;
    // 传递过来的所有数据
    List<SaveQuestionInfo> dataItems;

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

    public AnalusisQuestionAdapter(AnalusisQuestion context, List<View> viewItems, List<SaveQuestionInfo> dataItems, String imgServerUrl) {
        mContext = context;
        this.viewItems = viewItems;
        this.dataItems = dataItems;
        this.imgServerUrl = imgServerUrl;
	/*	dbManager = new DBManager(context);
		dbManager.openDB();*/
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

        holder.totalText.setText(position+1+"/"+dataItems.size());



        if(dataItems.get(position).getOptionA().equals("")){
            holder.layoutA.setVisibility(View.GONE);
        }if(dataItems.get(position).getOptionB().equals("")){
            holder.layoutB.setVisibility(View.GONE);
        }if(dataItems.get(position).getOptionC().equals("")){
            holder.layoutC.setVisibility(View.GONE);
        }if(dataItems.get(position).getOptionD().equals("")){
            holder.layoutD.setVisibility(View.GONE);
        }if(dataItems.get(position).getOptionE().equals("")){
            holder.layoutE.setVisibility(View.GONE);
        }

        String option_type = dataItems.get(position).getOption_type();
        if ("1".equals(option_type)) {
            //加载图片、
            //holder.ivTimu.setVisibility(View.VISIBLE);
            String url = dataItems.get(position).getUrl();
            Glide.with(mContext).load(url).into(holder.ivTimu);
        }
        if(dataItems.get(position).getQuestionType().equals("0")){
        holder.question.setText("(单选题)"+dataItems.get(position).getQuestionName());
        }
        holder.tvA.setText("A." + dataItems.get(position).getOptionA());
        holder.tvB.setText("B." + dataItems.get(position).getOptionB());
        holder.tvC.setText("C." + dataItems.get(position).getOptionC());
        holder.tvD.setText("D." + dataItems.get(position).getOptionD());
        holder.tvE.setText("E." + dataItems.get(position).getOptionE());
        holder.explaindetailTv.setText(dataItems.get(position).getAnalysis());

        String correctAnswer = dataItems.get(position).getRealAnswer();
        if (correctAnswer.equals("A")) {
            holder.ivA.setImageResource(R.mipmap.ic_practice_test_right);
        }else if(correctAnswer.equals("B")){
            holder.ivB.setImageResource(R.mipmap.ic_practice_test_right);
        }else if(correctAnswer.equals("C")){
            holder.ivC.setImageResource(R.mipmap.ic_practice_test_right);
        }else if(correctAnswer.equals("D")){
            holder.ivD.setImageResource(R.mipmap.ic_practice_test_right);
        }


        // 最后一页修改"下一步"按钮文字
        if (position == viewItems.size() - 1) {

            holder.nextText.setText("尾页");
            holder.nextImage.setImageResource(R.mipmap.vote_submit_finish);

            holder.nextText.setText("尾页");
            holder.nextImage.setImageResource(R.mipmap.vote_submit_finish);

        }
        holder.previousBtn.setOnClickListener(new LinearOnClickListener(position - 1, false,position,holder));
        holder.nextBtn.setOnClickListener(new LinearOnClickListener(position + 1, true,position,holder));
        container.addView(viewItems.get(position));
        return viewItems.get(position);
    }

    /**
     * @author  设置上一步和下一步按钮监听
     *
     */
    class LinearOnClickListener implements View.OnClickListener {

        private int mPosition;
        private int mPosition1;
        private boolean mIsNext;
        private AnalusisQuestionAdapter.ViewHolder viewHolder;

        public LinearOnClickListener(int position, boolean mIsNext, int position1, AnalusisQuestionAdapter.ViewHolder viewHolder) {
            mPosition = position;
            mPosition1 = position1;
            this.viewHolder = viewHolder;
            this.mIsNext = mIsNext;
        }

        @Override
        public void onClick(View v) {
                        isNext = mIsNext;
                        mContext.setCurrentView(mPosition);
        }
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

