package cn.wjdc.service;

import java.util.List;

import cn.wjdc.entity.Question;

public interface QuestionService {

	void addQuestion(Question question);

	List<Question> findQuestionByQid(Long quesNaireId);

	void delQuestionByQid(Long quesNaireId);

	void updateQuestion(Question question);

	void delQuestionByTid(String tid);

	void answersAdd(String tid);


}
