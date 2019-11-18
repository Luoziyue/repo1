package cn.wjdc.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.wjdc.dao.OptionDao;
import cn.wjdc.entity.Option;
import cn.wjdc.service.OptionService;

@Service
public class OptionServiceImpl implements OptionService {
	@Autowired
	OptionDao optionDao;
	
	@Override
	public void addOptions(List<Option> optionList) {
		optionDao.addOptions(optionList);
		
	}

	@Override
	public List<Option> findOptionByTid(String tid) {
		// TODO Auto-generated method stub
		return optionDao.findOptionByTid(tid);
	}

	@Override
	public void delOptionsWithNotQues() {
		// TODO Auto-generated method stub
		optionDao.delOptionsWithNotQues();
	}

	@Override
	public void delOptionByTid(String tid) {
		optionDao.delOptionByTid(tid);
		
	}

	@Override
	public void answerAdd(Long oid) {
		optionDao.answersAdd(oid);
		
	}

	@Override
	public List<String> finValueByTid(String tid) {
		
		return optionDao.finValueByTid(tid);
	}
	
}
