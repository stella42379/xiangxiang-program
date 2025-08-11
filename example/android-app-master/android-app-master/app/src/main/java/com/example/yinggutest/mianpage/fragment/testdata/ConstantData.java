package com.example.yinggutest.mianpage.fragment.testdata;

import android.util.Log;

import com.example.yinggutest.mianpage.fragment.simulation.SimulationEntity;

import java.util.ArrayList;
import java.util.List;

//测试数据
public class ConstantData {
	private static final String TAG = "ConstantData";

	private static class Simulation{
		public static int id[] = {1,2,3,4};
		public static final String title[] ={
				"模拟试卷（一）",
				"模拟试卷（二）",
				"模拟试卷（三）",
				"模拟试卷（四）",
				"模拟试卷（五）",
				"模拟试卷（六）",
				"模拟试卷（七）"} ;
		public static final String content[] = {"4","3","5","4","6","5","4"};
	}


	public static final String answerId[]={"1","2","3","4"};
	public static final String answerOptionType[]={"0","0","0","1"};//是否是图片题0是1否
	public static final String answerOptionTypeImageUrl[]={"","","","https://dss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=2567670815,24101428&fm=26&gp=0.jpg"};//是否是图片题0是1否
	public static final String answerName[]={
			"随着气候变化，肥料和供水导致了极大的能源和环境成本，如何让作物更好地吸收营养和水就显得极其重要。因此不少人认为只要增加植物根毛的长度，就可以更有效地吸收水和养分，从而提高作物产量。下列哪项如果为真，最能削弱上述结论（    ）",
			"口感好、有营养、无污染……随着这些理念的____ ，有机产品迅速发展。然而，一些问题也开始____；有的生产或销售企业有机产品认证标志使用不规范；有的企业甚至以普通产品____有机产品。依次填入划横线部分最恰当的一项是（  ）",
			"将以下6个句子重新排列，语序正确的是（    ）①未开采的煤炭只是一种能源储备，只有开采出来，价值才能得到发挥②充分挖掘并应用大数据这座巨大而未知的宝藏，将成为企业转型升级的关键③有人把大数据比喻为蕴藏能量的煤矿④数据作为一种资源，在“沉睡”的时候是很难创造价值的，需要进行数据挖掘⑤大数据是一种在获取、存储、管理、分析方面规模大大超出传统数据库软件工具能力范围的数据集合⑥与此类似，大数据并不在“大”，而在于“用”",
			"填入图表空白处能够符合规律的一项是（  ）"};
	public static final String answerType[]={"0","0","0","0"};//0单选 1多选 2判断
	public static final String answerOptionA[]={
			"实践证明，合理控制光照时间，并保证同一块田地间隔播种作物能极大地提高产量",
			"推行 突现 替换",
			"③①②⑤④⑥",
			"-4"};
	public static final String answerOptionB[]={
			"根毛的寿命很短，仅能存活2~3周便自行脱落，由于更新很快，植物的根部总能保持一个数量相对稳定的根毛区",
			"推进 凸现 代替",
			"⑤④③①⑥②",
			"-2"};
	public static final String answerOptionC[]={
			"如果植株过于密集而养料不足，即使增加了根毛长度，也无法保证植物的营养供应",
			"推动 突显 冒充",
			"③⑤②①④⑥",
			"0"};
	public static final String answerOptionD[]={
			"根毛的长度与植物生长激素密切相关，当植物生长激素分泌旺盛时，根毛也变长；而生长激素分泌减少时，根毛也逐渐萎缩",
			"推广 凸显 假冒",
			"⑤③④⑥①②",
			"2",};
	public static final String answerOptionE[]={"","","",""};
	public static final String answerAnalysis[]={
			"解析：“因此”引出论点：只要增加职务根毛的长度，就可以更有效地吸收水和养分，从而提高作物产量。C项明确说明即使增加根毛长度也不能吸收养分的一种情况，举例否定论点。A项说合理控制光照时间可以提高产量，没有直接提到产量和根毛长度的关系，不如C项直接。B项说的是根毛寿命和根毛数量，与根毛长度无光，属于无光选项。排除。D项说根毛长度受生长激素的影响，没有直接提到其与吸收营养和产量的关系，属于不明确选项。故正确答案为C。",
			"“推行”指推广施行，对象为具体制定出的机制或政策。“突显”与“凸显”是近义词，主要区别在于“凸显”强调物体的轮廓凸出来，形体意思明显；“突显”强调时间上的快速出现。因此“凸显”更为合适。排除A、C。第三空替换、假冒、冒充、代替也为近义词，从感情色彩的角度来说，应填入感情色彩偏消极的词汇。排除A、B。“替换”强调由一个事物换掉另外一个事物。 “假冒”以假乱真，与“冒充”是同义词。故本题选D项。",
			"解析：对比③和⑤，⑤句下定义，更适合作首句，观察⑥句中“此”，对应的为①句，因此①⑥捆绑，答案选择B",
			"解析：14=2×6+2,5=1×3+2,8=1×4+4，即右上角数字等于左上角与右下角两个数字的乘积与左下角数字的和。10=3×4+？，即？=-2"};
	public static final String answerScore[]={"2","2","1","2"};   //TODO：这句是什么意思？？
	public static final String answerCorrect[]={
			"C",
			"D",
			"B",
			"B"};



	//用来存储真题部分
	//真题分类ID
	public static final int trueSortID [] = {1,2,3,4,5};
	//真题分类
	public static final String trueSort[]={"行测","综合","英语四级","公基","教编"};
	//每一分类的具体真题
	public static final String XingCe[] = {"2000行测","2011行测","2012行测","2013行测","2014行测"
			,"2015行测","2016行测","2017行测","2018行测","2019行测"};
	public static final String Zonghe[] = {"2020综合","2019综合"};
	public static final String Siji[] ={"2000四级真题","2020四级真题"};
	public static final String Gongji[] = {"公基1","公基2"};
	public static final String JiaoBian[] = {"2000教师编","2011教师编","2012教师编","2013教师编","2014教师编"
			,"2015教师编","2016教师编","2017教师编","2018教师编","2019教师编","2020教师编"};







	//数据转换方法 textData to Entity
	public static List<SimulationEntity> toEntityList() {

		List<SimulationEntity> testTitleList = new ArrayList<>();
		//collection 值传递和址传递？？？    Collection<?> var
		for (int i = 0; i < ConstantData.answerName.length; i++) { //TODO: 如果数组长度不一样 循环次数的问题
			SimulationEntity entity = new SimulationEntity(); //TODO: new 对象必须放在for循环里面
			entity.setId(Simulation.id[i]);
			entity.setTitle(Simulation.title[i]); //（每套）模拟题标题
			entity.setContent(Simulation.content[i]); ////（每套）模拟题数量

			entity.setQuestionId(ConstantData.answerId[i]);// 试题主键
			entity.setQuestionName(ConstantData.answerName[i]);// 试题题目
			entity.setQuestionType(ConstantData.answerType[i]);// 试题类型0单选1多选
			entity.setQuestionFor("0");// （0模拟试题，1竞赛试题）
			entity.setAnalysis(ConstantData.answerAnalysis[i]);// 试题分析
			entity.setCorrectAnswer(ConstantData.answerCorrect[i]);// 正确答案
			entity.setOptionA(ConstantData.answerOptionA[i]);// 试题选项A
			entity.setOptionB(ConstantData.answerOptionB[i]);// 试题选项B
			entity.setOptionC(ConstantData.answerOptionC[i]);// 试题选项C
			entity.setOptionD(ConstantData.answerOptionD[i]);// 试题选项D
			entity.setOptionE(ConstantData.answerOptionE[i]);// 试题选项E
			entity.setScore(ConstantData.answerScore[i]);// 分值
			if (i == 3) {
				entity.setOption_type("1");
			} else {
				entity.setOption_type("0");
			}
			//将数据存入实体类列表
			Log.e(TAG, "toEntityList: "+entity.title );
			testTitleList.add(entity);
		}
		Log.e(TAG, "toEntityList: List<SimulationEntity> create!"+testTitleList.get(1).title);
		return testTitleList;
	}
}
