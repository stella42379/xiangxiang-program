package com.example.yinggutest.mianpage.fragment.simulation;

//数据实体类
public class SimulationEntity {

	public int id; //每套模拟题的id
	public String title; //每套模拟题的标题
	public String content; //每套模拟题的题目数量
	public String questionId; // 试题主键
	public String questionName; // 试题题目
	public String QuestionFor; // （0模拟试题，1竞赛试题）
	public String questionType; // 试题类型
	public String analysis; // 试题分析
	public String correctAnswer; // 正确答案
	public String optionA; // 正确答案A
	public String optionB; // 正确答案B
	public String optionC; // 正确答案C
	public String optionD; // 正确答案D
	public String optionE; // 正确答案E
	public String score; // 分值
	public String option_type; // 是否是图片题0是1否
	public String imgServerUrl; //图片地址
	public String isSelect; // 是否选择0是1否
	public String mySelect;
	public String testNo;
	public String videoName;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getVideoName() {
		return videoName;
	}

	public void setVideoName(String videoName) {
		this.videoName = videoName;
	}

	public String getTestNo() {
		return testNo;
	}

	public void setTestNo(String testNo) {
		this.testNo = testNo;
	}

	public String getMySelect() {
		return mySelect;
	}

	public void setMySelect(String mySelect) {
		this.mySelect = mySelect;
	}

	public String getQuestionId() {
		return questionId;
	}
	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}
	public String getQuestionName() {
		return questionName;
	}
	public void setQuestionName(String questionName) {
		this.questionName = questionName;
	}
	public String getQuestionFor() {
		return QuestionFor;
	}
	public void setQuestionFor(String questionFor) {
		QuestionFor = questionFor;
	}
	public String getQuestionType() {
		return questionType;
	}
	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}
	public String getAnalysis() {
		return analysis;
	}
	public void setAnalysis(String analysis) {
		this.analysis = analysis;
	}
	public String getCorrectAnswer() {
		return correctAnswer;
	}
	public void setCorrectAnswer(String correctAnswer) {
		this.correctAnswer = correctAnswer;
	}
	public String getOptionA() {
		return optionA;
	}
	public void setOptionA(String optionA) {
		this.optionA = optionA;
	}
	public String getOptionB() {
		return optionB;
	}
	public void setOptionB(String optionB) {
		this.optionB = optionB;
	}
	public String getOptionC() {
		return optionC;
	}
	public void setOptionC(String optionC) {
		this.optionC = optionC;
	}
	public String getOptionD() {
		return optionD;
	}
	public void setOptionD(String optionD) {
		this.optionD = optionD;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public String getImgServerUrl() {
		return imgServerUrl;
	}
	public void setImgServerUrl(String imgServerUrl) {
		this.imgServerUrl = imgServerUrl;
	}
	public String getIsSelect() {
		return isSelect;
	}
	public void setIsSelect(String isSelect) {
		this.isSelect = isSelect;
	}
	public String getOptionE() {
		return optionE;
	}
	public void setOptionE(String optionE) {
		this.optionE = optionE;
	}
	public String getOption_type() {
		return option_type;
	}
	public void setOption_type(String option_type) {
		this.option_type = option_type;
	}




}
