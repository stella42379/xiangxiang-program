package com.example.yinggutest.mianpage.fragment.bean;

public class SaveQuestionInfo {
	
	private String questionId;//题目id
	private String questionType;//题目类型
	private String realAnswer;//题目答案
	private String is_correct;//是否正确
	private String score;//分值

	public String questionName;//题目内容
	public String optionA;//选项A内容
	public String optionB;//选项B内容
	public String optionC;//选项C内容
	public String optionD;//选项D内容
	public String optionE;//选项E内容
	public String Analysis;//解析内容
	public String url;//图片地址
	public String option_type; // 是否是图片题0是1否

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getOption_type() {
		return option_type;
	}

	public void setOption_type(String option_type) {
		this.option_type = option_type;
	}

	public String getQuestionName() {
		return questionName;
	}

	public void setQuestionName(String questionName) {
		this.questionName = questionName;
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

	public String getOptionE() {
		return optionE;
	}

	public void setOptionE(String optionE) {
		this.optionE = optionE;
	}

	public String getAnalysis() {
		return Analysis;
	}

	public void setAnalysis(String analysis) {
		Analysis = analysis;
	}



	public String getQuestionId() {
		return questionId;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}
	
	public String getQuestionType() {
		return questionType;
	}

	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}

	public String getRealAnswer() {
		return realAnswer;
	}

	public void setRealAnswer(String realAnswer) {
		this.realAnswer = realAnswer;
	}

	public String getIs_correct() {
		return is_correct;
	}

	public void setIs_correct(String is_correct) {
		this.is_correct = is_correct;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}






	public String toString() {
		return "{'question_id':'"+getQuestionId()+"','question_type':'"+getQuestionType()+"','realAnswer':'"+getRealAnswer()+"','is_correct':'"+getIs_correct()+"','score':'"+getScore()+"'}";
	}
	
}
