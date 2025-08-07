package com.xiangjia.locallife.util;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;

/**
 * 音频播放工具类
 * 提供音频播放功能
 */
public class AudioPlayerUtil {
    
    private static final String TAG = "AudioPlayerUtil";
    
    private MediaPlayer mediaPlayer;
    private Context context;
    private OnAudioPlayerListener onAudioPlayerListener;
    private boolean isPrepared = false;
    
    public AudioPlayerUtil(Context context) {
        this.context = context;
        initMediaPlayer();
    }
    
    /**
     * 初始化MediaPlayer
     */
    private void initMediaPlayer() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(mp -> {
            isPrepared = true;
            if (onAudioPlayerListener != null) {
                onAudioPlayerListener.onPrepared();
            }
        });
        
        mediaPlayer.setOnCompletionListener(mp -> {
            if (onAudioPlayerListener != null) {
                onAudioPlayerListener.onCompletion();
            }
        });
        
        mediaPlayer.setOnErrorListener((mp, what, extra) -> {
            Log.e(TAG, "MediaPlayer error: what=" + what + ", extra=" + extra);
            if (onAudioPlayerListener != null) {
                onAudioPlayerListener.onError("播放错误: " + what);
            }
            return true;
        });
    }
    
    /**
     * 播放音频文件
     */
    public void playAudio(String audioPath) {
        try {
            resetPlayer();
            mediaPlayer.setDataSource(audioPath);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            Log.e(TAG, "Error setting audio source", e);
            if (onAudioPlayerListener != null) {
                onAudioPlayerListener.onError("设置音频源失败: " + e.getMessage());
            }
        }
    }
    
    /**
     * 播放网络音频
     */
    public void playAudioFromUrl(String audioUrl) {
        try {
            resetPlayer();
            mediaPlayer.setDataSource(audioUrl);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            Log.e(TAG, "Error setting audio URL", e);
            if (onAudioPlayerListener != null) {
                onAudioPlayerListener.onError("设置音频URL失败: " + e.getMessage());
            }
        }
    }
    
    /**
     * 开始播放
     */
    public void start() {
        if (mediaPlayer != null && isPrepared && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            if (onAudioPlayerListener != null) {
                onAudioPlayerListener.onStart();
            }
        }
    }
    
    /**
     * 暂停播放
     */
    public void pause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            if (onAudioPlayerListener != null) {
                onAudioPlayerListener.onPause();
            }
        }
    }
    
    /**
     * 停止播放
     */
    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            isPrepared = false;
            if (onAudioPlayerListener != null) {
                onAudioPlayerListener.onStop();
            }
        }
    }
    
    /**
     * 重置播放器
     */
    public void reset() {
        if (mediaPlayer != null) {
            mediaPlayer.reset();
            isPrepared = false;
        }
    }
    
    /**
     * 释放资源
     */
    public void release() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
            isPrepared = false;
        }
    }
    
    /**
     * 重置播放器状态
     */
    private void resetPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.reset();
            isPrepared = false;
        }
    }
    
    /**
     * 是否正在播放
     */
    public boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.isPlaying();
    }
    
    /**
     * 获取当前播放位置
     */
    public int getCurrentPosition() {
        if (mediaPlayer != null) {
            return mediaPlayer.getCurrentPosition();
        }
        return 0;
    }
    
    /**
     * 获取总时长
     */
    public int getDuration() {
        if (mediaPlayer != null && isPrepared) {
            return mediaPlayer.getDuration();
        }
        return 0;
    }
    
    /**
     * 跳转到指定位置
     */
    public void seekTo(int position) {
        if (mediaPlayer != null && isPrepared) {
            mediaPlayer.seekTo(position);
        }
    }
    
    /**
     * 设置音量
     */
    public void setVolume(float leftVolume, float rightVolume) {
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(leftVolume, rightVolume);
        }
    }
    
    /**
     * 设置循环播放
     */
    public void setLooping(boolean looping) {
        if (mediaPlayer != null) {
            mediaPlayer.setLooping(looping);
        }
    }
    
    /**
     * 设置播放监听器
     */
    public void setOnAudioPlayerListener(OnAudioPlayerListener listener) {
        this.onAudioPlayerListener = listener;
    }
    
    /**
     * 音频播放监听器接口
     */
    public interface OnAudioPlayerListener {
        void onPrepared();
        void onStart();
        void onPause();
        void onStop();
        void onCompletion();
        void onError(String errorMessage);
    }
    
    /**
     * 格式化时间显示
     */
    public static String formatTime(int milliseconds) {
        int seconds = milliseconds / 1000;
        int minutes = seconds / 60;
        seconds = seconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
    
    /**
     * 获取播放进度百分比
     */
    public int getProgressPercentage() {
        if (mediaPlayer != null && isPrepared) {
            int duration = mediaPlayer.getDuration();
            int currentPosition = mediaPlayer.getCurrentPosition();
            if (duration > 0) {
                return (currentPosition * 100) / duration;
            }
        }
        return 0;
    }
}
