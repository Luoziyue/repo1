package cn.wjdc.service.Impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.wjdc.dao.OptionDao;
import cn.wjdc.dao.QuesNaireDao;
import cn.wjdc.dao.QuestionDao;
import cn.wjdc.entity.QuestionNaire;
import cn.wjdc.service.QuesNaireService;

@Service
public class QuesNaireServiceImpl implements QuesNaireService {
	@Autowired
	QuesNaireDao quesNaireDao;
	@Autowired
	QuestionDao questionDao;
	@Autowired
	OptionDao optionDao;
	
	@Override
	public List<QuestionNaire> findQuesNaireByOpenid(String openid) {
		// TODO Auto-generated method stub
		return quesNaireDao.findQuesNaireByOpenid(openid);
	}

	@Override
	public void addQuesNaire(QuestionNaire quesNaire) {
		// TODO Auto-generated method stub
		quesNaireDao.addQuesNaire(quesNaire);
	}

	@Override
	public void delQuesNaireById(Long quesNaireId) {
		// TODO Auto-generated method stub
		quesNaireDao.delQuesNaireById(quesNaireId);
	}

	@Override
	public void updateQuesNaire(QuestionNaire quesNaire) {
		
		quesNaireDao.updateQuesNaire(quesNaire);
		
	}

	@Override
	@Transactional
	/**
	 * answer中的数据为一个map的列表，每个map中包含type，tid，value
	 * type=1为单选题，value为选项oid
	 * type=2为多选题，value为选项oid的数组
	 * type=3为简答题，value是一个字符串
	 */
	public void answersAdd(List<HashMap> answers) {
		boolean flag = true; // 问卷回答次数加一标志
		for (HashMap map : answers) { // 遍历每个map，判断type是什么题型
			Integer type = (Integer) map.get("type");
			String tid = (String) map.get("tid");
			if(flag){
				//将相应的问卷的回答人数加一，随后flag=false  所以每次提交只加一次
				quesNaireDao.answersAdd(tid);
				flag = false;
			}
			//根据tid将该问题的回答人数加一
			questionDao.answersAdd(tid);
			
			//选项回答人数加一，，或保存简答题答案
			if (type.intValue() == 1) {
				Long value = Long.parseLong((String) map.get("value"));
				optionDao.answersAdd(value);
			} else if (type.intValue() == 2) {
				List<String> values = (List<String>) map.get("value");
				for (String str : values) {
					optionDao.answersAdd(Long.parseLong(str));
				}
				
			} else {
				String value = (String) map.get("value");
				optionDao.addShortAnswer(tid,value);
				System.out.println("问答：" + value);
			}

		}
	}
	
}
