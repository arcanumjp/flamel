/**
 * 
 */
package jp.arcanum.click.pages;

import java.util.Map;

import jp.arcanum.click.ArUtil;
import jp.arcanum.click.UserInfo;
import net.sf.click.control.PasswordField;
import net.sf.click.control.Submit;
import net.sf.click.control.TextField;


/**
 *
 */
public class LoginPage extends AbstractPage{

	/**
	 * ユーザID
	 */
	private TextField userid = new TextField("userid", "ユーザID");
	/**
	 * パスワードフィールド
	 */
	private PasswordField password = new PasswordField("password", "パスワード");
	/**
	 * ログインボタン
	 */
	private Submit login = new Submit("login", "ログイン",this,"onClickLogin");

	
	/**
	 * コンストラクタ
	 *
	 */
	public LoginPage(){
		
		form.add(userid);
		form.add(password);
		form.add(login);
		
		userid.setSize(20);
		password.setSize(20);
		
	}

	/**
	 * 初期化処理
	 */
	public void onInit(){
		
		//　既にログイン中であれば、ツールメインページに遷移する
		if(super.context.getSession().getAttribute(ArUtil.USER) != null){
			super.setRedirect(ToolMainPage.class);
			return;
		}
		
	}
	
	/**
	 * ログイン処理
	 * @return
	 */
	public boolean onClickLogin(){
		
		String id   = userid.getValue();
		String pass = password.getValue();
		
		if(id.length() == 0){
			addMessage("ユーザIDが入力されていません。");
			return true;
		}
		if(pass.equals("")){
			addMessage("パスワードが入力されていません。");
			return true;
		}
		
		//ユーザ情報を作っとく
		UserInfo user = new UserInfo();

		//　スーパーユーザかどうか
		String adminid   = ArUtil.getProperty(this, ArUtil.PROP_ADMIN, ArUtil.KEY_ADMIN_CONF_NAME);
		String adminpass = ArUtil.getProperty(this, ArUtil.PROP_ADMIN, ArUtil.KEY_ADMIN_CONF_PASS);
		if(id.equals(adminid) && pass.equals(adminpass)){

			user.setUsername(id);
			user.setGroup("");  // スーパーユーザなので不要
			user.setAdmin(true);
			super.context.getSession().setAttribute(ArUtil.USER, user);
			super.setRedirect(ToolMainPage.class);
			return false;

		}

		// ユーザファイルから読み込む
		Map xmluser = ArUtil.getUser(this, id);
		if(xmluser.get("pass").equals(pass)){
			
			user.setUsername(id);
			user.setGroup((String)xmluser.get("group"));
			user.setAdmin(false);
			super.context.getSession().setAttribute(ArUtil.USER, user);
			super.setRedirect(ToolMainPage.class);
			return false;
			
		}
		
		addMessage("ユーザIDまたはパスワードが間違っています。");
		return true;

	}

	
	
}
