package cn.wjdc.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.wjdc.dao.QuestionDao;
import cn.wjdc.entity.Question;
import cn.wjdc.service.QuestionService;

@Service
public class QuestionServiceImpl implements QuestionService {
	@Autowired
	QuestionDao questionDao;
	
	@Override
	public void addQuestion(Question question) {
		questionDao.addQuestion(question);
		
	}

	@Override
	public List<Question> findQuestionByQid(Long quesNaireId) {
		// TODO Auto-generated method stub
		return questionDao.findQuestionByQid(quesNaireId);
	}

	@Override
	public void delQuestionByQid(Long quesNaireId) {
		// TODO Auto-generated method stub
		questionDao.delQuestionByQid(quesNaireId);
	}

	@Override
	public void updateQuestion(Question question) {
		questionDao.updateQuestion(question);
		
	}

	@Override
	public void delQuestionByTid(String tid) {
		questionDao.delQuestionByTid(tid);
	}

	@Override
	public void answersAdd(String tid) {
		questionDao.answersAdd(tid);
		
	}


}
