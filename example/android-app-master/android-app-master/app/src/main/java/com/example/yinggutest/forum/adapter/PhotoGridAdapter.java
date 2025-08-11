package com.example.yinggutest.forum.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.yinggutest.R;
import com.example.yinggutest.forum.util.TakePhotosActivity;

import java.util.ArrayList;

public class PhotoGridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "PhotoGridAdapter";
    private Activity context;
    private ArrayList<String> mList;
    private final LayoutInflater inflater;
    private static final int ITEM_TYPE_PICTURE = 0x00001;
    private static final int ITEM_TYPE_DEFAULT = 0x00002;

    private Dialog mShareDialog;
    /**
     *这里之所以用多行视图，因为我们默认的有一张图片的（那个带+的图片，用户点击它才会才会让你去选择图片）
     *集合url为空的时候，默认显示它，当它达到集合9时，这个图片会自动隐藏。
     */
    public PhotoGridAdapter(Activity context, ArrayList<String> mList) {
        this.context = context;
        this.mList = mList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType(int position) {
        if (position +1 == getItemCount()) {
            return ITEM_TYPE_DEFAULT;
        } else {
            return ITEM_TYPE_PICTURE;
        }
    }

    @Override
    public int getItemCount() {
        //Log.e(TAG, "getItemCount: "+mList.get(0).toString() );
        return mList.size() +1 ;
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        parent.setPadding(20, 0, 20, 0);
        switch (viewType) {
            case ITEM_TYPE_PICTURE:
                return new PictureHolder(inflater.inflate(R.layout.item_release_picture, parent, false));
            case ITEM_TYPE_DEFAULT:
                return new DefaultHolder(inflater.inflate(R.layout.item_release_defauit, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PictureHolder) {
            bindItemPictureHolder((PictureHolder) holder, position);
        } else if (holder instanceof DefaultHolder) {
            bindItemDefaultHolder((DefaultHolder) holder, position);
        }
    }
    private void bindItemPictureHolder(PictureHolder holder, int position) {
        Glide.with(context)
                .load(mList.get(position))
               // .centerCrop()
                .into(holder.ivPicture);
    }
    private void bindItemDefaultHolder(final DefaultHolder holder, int position) {
        Log.e(TAG, "bindItemDefaultHolder: "+ mList.size());
        if (mList.size() >= 9) {//集合长度大于等于9张时，隐藏 图片
            holder.ivDefault.setVisibility(View.GONE);
        }
        holder.ivDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //从底部弹出对话框，选择是拍摄还是从相册获取
                Log.e(TAG, "onClick: 添加照片按钮被点击！" );
                //底部弹出对话框
                showDialog();
            }
        });
    }
    private void showDialog() {
        if (mShareDialog == null) {
            mShareDialog = new Dialog(context, R.style.dialog_bottom_full);
            mShareDialog.setCanceledOnTouchOutside(true); //手指触碰到外界取消
            mShareDialog.setCancelable(true);             //可取消 为true
            Window window = mShareDialog.getWindow();      // 得到dialog的窗体
            window.setGravity(Gravity.BOTTOM);
            //window.setWindowAnimations(R.style.share_animation);

            View view = View.inflate(context, R.layout.lay_share, null); //获取布局视图
            view.findViewById(R.id.tv_select_pictures).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mShareDialog != null && mShareDialog.isShowing()) {
                        mShareDialog.dismiss();
                        Intent intent = new Intent(context, TakePhotosActivity.class);
                        intent.putExtra(TakePhotosActivity.TAKE_PHOTO_TYPE,TakePhotosActivity.ON_SELECT_PICTURES);
                        context.startActivityForResult(intent, 1);
                    }
                }
            });
            view.findViewById(R.id.tv_take_photos).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mShareDialog != null && mShareDialog.isShowing()) {
                        mShareDialog.dismiss();
                        Intent intent = new Intent(context, TakePhotosActivity.class);
                        intent.putExtra(TakePhotosActivity.TAKE_PHOTO_TYPE,TakePhotosActivity.ON_TAKE_PHOTOS);
                        context.startActivityForResult(intent, 1);
                    }
                }

            });
            window.setContentView(view);
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);//设置横向全屏
        }
        mShareDialog.show();
    }





    class PictureHolder extends RecyclerView.ViewHolder {
        private final ImageView ivPicture;
        public PictureHolder(View item) {
            super(item);
            ivPicture = item.findViewById(R.id.iv_picture_image);
        }
    }

    class DefaultHolder extends RecyclerView.ViewHolder {
        private final ImageView ivDefault;
        public DefaultHolder(View itemView) {
            super(itemView);
            ivDefault = (ImageView) itemView.findViewById(R.id.iv_default_image);
        }
    }
    //对外暴露方法  。点击添加图片（类似于上啦加载数据）
    public void addMoreItem(ArrayList<String> loarMoreDatas) {
        mList.addAll(loarMoreDatas);
        notifyDataSetChanged();
    }

}
