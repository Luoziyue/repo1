package cn.wjdc.dao;

import java.util.List;

import cn.wjdc.entity.QuestionNaire;

public interface QuesNaireDao {
	public List<QuestionNaire> findQuesNaireByOpenid(String openid);
	
	public void addQuesNaire(QuestionNaire quesNaire);

	public void delQuesNaireById(Long quesNaireId);

	public void updateQuesNaire(QuestionNaire quesNaire);

	public void answersAdd(String tid);
}
