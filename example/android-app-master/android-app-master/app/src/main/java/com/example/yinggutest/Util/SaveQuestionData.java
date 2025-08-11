package com.example.yinggutest.Util;

import com.example.yinggutest.mianpage.fragment.bean.ErrorQuestionInfo;
import com.example.yinggutest.mianpage.fragment.bean.SaveQuestionInfo;
import com.example.yinggutest.mianpage.fragment.bean.TruetitleInfo;

import java.util.ArrayList;
import java.util.List;

public class SaveQuestionData {
    //用来存储真题
    public static List<TruetitleInfo> sTrueQuestionList = new ArrayList<>();
    //用来 存储提交之后所有题目的别数据
    public static List<SaveQuestionInfo> sSaveQuestionList = new ArrayList<>();
    //用来 存储提交之后答错题目的数据
    public static List<ErrorQuestionInfo> sSaveErrorQuestionList = new ArrayList<>();
    //用来存储 所有答过的错题，用于错题本
    public static List<ErrorQuestionInfo> sSaveAllErrorQuestionList = new ArrayList<>();

}
