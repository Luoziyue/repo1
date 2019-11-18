package cn.wjdc.entity;

public class Option {
	private Long oid;
	private String tid;
	private char tnumber;
	private String optionname;
	private int answers;
	
	public Long getOid() {
		return oid;
	}

	public void setOid(Long oid) {
		this.oid = oid;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public char getTnumber() {
		return tnumber;
	}

	public void setTnumber(char tnumber) {
		this.tnumber = tnumber;
	}

	public String getOptionname() {
		return optionname;
	}

	public void setOptionname(String optionname) {
		this.optionname = optionname;
	}

	public int getAnswers() {
		return answers;
	}

	public void setAnswers(int answers) {
		this.answers = answers;
	}

	@Override
	public String toString() {
		return "Option [oid=" + oid + ", tid=" + tid + ", tnumber=" + tnumber + ", optionname=" + optionname
				+ ", answers=" + answers + "]";
	}
	
}
