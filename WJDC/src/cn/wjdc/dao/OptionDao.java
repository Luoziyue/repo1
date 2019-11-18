package cn.wjdc.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.wjdc.entity.Option;

public interface OptionDao {

	void addOptions(List<Option> optionList);

	List<Option> findOptionByTid(String tid);

	void delOptionsWithNotQues();

	void delOptionByTid(String tid);

	void answersAdd(Long oid);

	void addShortAnswer(@Param("tid")String tid, @Param("value")String value);

	List<String> finValueByTid(String tid);

}
