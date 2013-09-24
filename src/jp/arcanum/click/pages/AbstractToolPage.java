package jp.arcanum.click.pages;

import jp.arcanum.click.ArUtil;
import jp.arcanum.click.UserInfo;

/**
 * �c�[���y�[�W�̒��ۃN���X
 * @author shinya
 *
 */
public class AbstractToolPage extends AbstractPage {


	/**
	 * �Z�L�����e�B�`�F�b�N
	 */
	public boolean onSecurityCheck(){
		
		UserInfo user = (UserInfo)super.context.getSession().getAttribute(ArUtil.USER);
		if(user == null){
			super.setRedirect(LoginPage.class);
			return false;
		}
		
		return true;
	
	}
	

	
}
