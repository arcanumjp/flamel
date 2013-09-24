package jp.arcanum.click.pages;

import jp.arcanum.click.ArUtil;
import jp.arcanum.click.UserInfo;

/**
 * ツールページの抽象クラス
 * @author shinya
 *
 */
public class AbstractToolPage extends AbstractPage {


	/**
	 * セキュリティチェック
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
