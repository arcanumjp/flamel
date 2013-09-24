package jp.arcanum.click.pages.controls;

import jp.arcanum.click.ArUtil;
import jp.arcanum.click.UserInfo;
import net.sf.click.control.Submit;

/**
 * ログイン中のサブミットボタン
 * @author shinya
 *
 */
public class LoginSubmit extends Submit {

	/**
	 * 管理者かどうか
	 */
	private boolean _isadmin = false;
	
	/**
	 * コンストラクタ
	 * @param name name属性
	 * @param label 表示名
	 * @param listener イベントリスナー
	 * @param method イベント名
	 * @param isadmin 管理者かどうか
	 */
	public LoginSubmit(
			String name, 
			String label, 
			Object listener, 
			String method, 
			boolean isadmin){
		super(name, label, listener, method);
		_isadmin = isadmin;
	}
	
	/**
	 * 文字列化<br>
	 * ログイン中でなければ""を返す。また、ログインちゅうであっても
	 * _isadminフラグにより表示または非表示を行う
	 */
	public String toString(){
		UserInfo user = (UserInfo)super.context.getSession().getAttribute(ArUtil.USER);
		if(user == null){
			return "";
		}
		if(user.isAdmin() != _isadmin){
			return "";
		}
		return super.toString();
	}

}
