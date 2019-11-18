package cn.wjdc.entity;

import java.util.List;

public class Question {
	private String tid;
	private String tname;
	private Long qid;
	private int type;
	private boolean must_do;
	private int answers;//�ش�����
	private List<Option> options; //��Ŀ����Ϊѡ����ʱ������ѡ���б�
	private List<String> values;  //��ĿΪ����ʱ�����лش��ߵĴ�
	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getTname() {
		return tname;
	}

	public void setTname(String tname) {
		this.tname = tname;
	}

	public Long getQid() {
		return qid;
	}

	public void setQid(Long qid) {
		this.qid = qid;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public boolean getMust_do() {
		return must_do;
	}

	public void setMust_do(boolean must_do) {
		this.must_do = must_do;
	}

	public int getAnswers() {
		return answers;
	}

	public void setAnswers(int answers) {
		this.answers = answers;
	}

	public List<Option> getOptions() {
		return options;
	}

	public void setOptions(List<Option> options) {
		this.options = options;
	}
	
	public List<String> getValues() {
		return values;
	}

	public void setValues(List<String> values) {
		this.values = values;
	}

	@Override
	public String toString() {
		return "Question [tid=" + tid + ", tname=" + tname + ", qid=" + qid + ", type=" + type + ", must_do=" + must_do
				+ ", answers=" + answers + ", options=" + options + "]";
	}

}
