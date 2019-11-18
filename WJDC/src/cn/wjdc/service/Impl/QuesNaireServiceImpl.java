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
	 * answer�е�����Ϊһ��map���б�ÿ��map�а���type��tid��value
	 * type=1Ϊ��ѡ�⣬valueΪѡ��oid
	 * type=2Ϊ��ѡ�⣬valueΪѡ��oid������
	 * type=3Ϊ����⣬value��һ���ַ���
	 */
	public void answersAdd(List<HashMap> answers) {
		boolean flag = true; // �ʾ�ش������һ��־
		for (HashMap map : answers) { // ����ÿ��map���ж�type��ʲô����
			Integer type = (Integer) map.get("type");
			String tid = (String) map.get("tid");
			if(flag){
				//����Ӧ���ʾ�Ļش�������һ�����flag=false  ����ÿ���ύֻ��һ��
				quesNaireDao.answersAdd(tid);
				flag = false;
			}
			//����tid��������Ļش�������һ
			questionDao.answersAdd(tid);
			
			//ѡ��ش�������һ�����򱣴������
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
				System.out.println("�ʴ�" + value);
			}

		}
	}
	
}
