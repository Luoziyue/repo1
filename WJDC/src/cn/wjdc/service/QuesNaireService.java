package cn.wjdc.service;

import java.util.HashMap;
import java.util.List;

import cn.wjdc.entity.QuestionNaire;

public interface QuesNaireService {
	public List<QuestionNaire> findQuesNaireByOpenid(String openid);
	
	public void addQuesNaire(QuestionNaire quesNaire);

	public void delQuesNaireById(Long quesNaireId);

	public void updateQuesNaire(QuestionNaire quesNaire);

	public void answersAdd(List<HashMap> answers);
}
