package cn.wjdc.entity;

public class QuestionNaire {
	private Long id;
	private String qnname;
	private String useropenid;
	private Integer anwers;
	private String createtime;
	private String explains;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getQnname() {
		return qnname;
	}
	public void setQnname(String qnname) {
		this.qnname = qnname;
	}
	public String getUseropenid() {
		return useropenid;
	}
	public void setUseropenid(String useropenid) {
		this.useropenid = useropenid;
	}
	public Integer getAnwers() {
		return anwers;
	}
	public void setAnwers(Integer anwers) {
		this.anwers = anwers;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getExplains() {
		return explains;
	}
	public void setExplains(String explain) {
		this.explains = explain;
	}
	@Override
	public String toString() {
		return "QuestionNaire [id=" + id + ", qnname=" + qnname + ", useropenid=" + useropenid + ", anwers=" + anwers
				+ ", createtime=" + createtime + ", explain=" + explains + "]";
	}
	
}
