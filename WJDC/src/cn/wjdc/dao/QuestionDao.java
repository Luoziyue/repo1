package cn.wjdc.dao;

import java.util.List;

import cn.wjdc.entity.Question;

public interface QuestionDao {

	void addQuestion(Question question);

	List<Question> findQuestionByQid(Long quesNaireId);

	void delQuestionByQid(Long quesNaireId);

	void updateQuestion(Question question);

	void delQuestionByTid(String tid);

	void answersAdd(String tid);


}
