package cn.wjdc.test;


import cn.wjdc.service.OptionService;
import cn.wjdc.service.QuesNaireService;
import cn.wjdc.service.QuestionService;
import cn.wjdc.service.Impl.OptionServiceImpl;
import cn.wjdc.service.Impl.QuesNaireServiceImpl;
import cn.wjdc.service.Impl.QuestionServiceImpl;

public class Test {
	public static void main(String[] args){
		
		QuesNaireService quesNaireService = new QuesNaireServiceImpl();
		
		QuestionService questionService = new QuestionServiceImpl();
		
		OptionService optionService = new OptionServiceImpl();
		
		Long id = 5L;
		quesNaireService.delQuesNaireById(id);
	}
}
