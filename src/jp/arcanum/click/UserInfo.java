package jp.arcanum.click;

public class UserInfo {
	
	/**
	 * ���[�UID
	 */
	private String username = "";
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	/**
	 * �O���[�v��
	 */
	private String group = "";
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	

	
	
	/**
	 * �A�h�~�����ǂ���
	 * @return
	 */
	private boolean isadmin = false;
	public boolean isAdmin(){
		return isadmin;
	}
	public void setAdmin(boolean value){
		this.isadmin = value;
	}
	
	
}
