package cn.wjdc.service;

import java.util.List;

import cn.wjdc.entity.Option;

public interface OptionService {

	void addOptions(List<Option> optionList);

	List<Option> findOptionByTid(String tid);

	void delOptionsWithNotQues();

	void delOptionByTid(String tid);

	void answerAdd(Long oid);

	List<String> finValueByTid(String tid);

}
