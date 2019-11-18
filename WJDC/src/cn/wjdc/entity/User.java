package cn.wjdc.entity;

public class User {
	private Long id;
	private String nickName;
	private int sex;
	private String city;
	private String province;
	private String country;
	private String avatarUrl;
	private String openid;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickname) {
		this.nickName = nickname;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getAvatarUrl() {
		return avatarUrl;
	}
	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", nickname=" + nickName + ", sex=" + sex + ", city=" + city + ", province="
				+ province + ", country=" + country + ", avatarUrl=" + avatarUrl + ", openid=" + openid + "]";
	}
	
	
	
	

}
