/**
 * 
 */
package jp.arcanum.click.pages;

import jp.arcanum.click.ArUtil;
import jp.arcanum.click.UserInfo;
import net.sf.click.control.Checkbox;
import net.sf.click.control.PasswordField;
import net.sf.click.control.Submit;
import net.sf.click.control.TextField;


/**
 *
 */
public class ManageSitePage extends ToolMainPage{

	/**
	 * ユーザ名
	 */
	private TextField username = new TextField("username");
	/**
	 * パスワード
	 */
	private PasswordField password1 = new PasswordField("password1");
	/**
	 * パスワード（確認用）
	 */
	private PasswordField password2 = new PasswordField("password2");
	
	//-------------------------------------------------------------
	
	private TextField headertitle = new TextField("headertitle");
	/**
	 * サイトタイトル
	 */
	private TextField sitetitle = new TextField("sitetitle");
	/**
	 * サイトタイトルをリンクするか
	 */
	private Checkbox sitelinkable = new Checkbox("sitelinkable");
	/**
	 * メール
	 */
	private TextField mailto = new TextField("mailto");
	/**
	 * メールをリンクするか
	 */
	private Checkbox maillinkable = new Checkbox("maillinkable");
	/**
	 * メールのリンクテキスト
	 */
	private TextField mailtext = new TextField("mailtext");
	/**
	 * ポート番号
	 */
	private TextField portno = new TextField("portno");
	/**
	 * ドキュメントルート
	 */
	private TextField docroot = new TextField("docroot");
	/**
	 * テンプレートパス
	 */
	private TextField templatepath = new TextField("templatepath");
	/**
	 * テンプレート編集ボタン
	 */
	private Submit edittemp = new Submit("edittemp", "編集", this, "onClickEditTemplate");
	/**
	 * 検索・エラー・条件なしメッセージ
	 */
	private TextField errnocond = new TextField("errnocond");
	/**
	 * 検索・エラー・結果なしメッセージ
	 */
	private TextField errnores = new TextField("errnores");
	/**
	 * 検索・結果・表示開始タグ
	 */
	private TextField resstarttag = new TextField("resstarttag");
	/**
	 * 検索・結果・表示開始タグ
	 */
	private TextField resendtag = new TextField("resendtag");
	/**
	 * 検索・結果・前後文字数
	 */
	private TextField resshowlength = new TextField("resshowlength");
	/**
	 * 検索・結果・表示開始タグ
	 */
	private TextField resemphstarttag = new TextField("resemphstarttag");
	/**
	 * 検索・結果・表示開始タグ
	 */
	private TextField resemphendtag = new TextField("resemphendtag");
	
	//-------------------------------------------------------------

	/**
	 * O.Kボタン
	 */
	private Submit ok = new Submit("ok", "O.K", this, "onClickOk");
	/**
	 * 戻るボタン
	 */
	private Submit back = new Submit("back", "戻る", this, "onClickBack");
	
	
	
	/**
	 * コンストラクタ
	 *
	 */
	public ManageSitePage(){
		
		//　ユーザ名
		username.setSize(50);
		form.add(username);
		//　パスワード
		password1.setSize(50);
		form.add(password1);
		//　パスワード（確認用）
		password2.setSize(50);
		form.add(password2);
		
		
		//　ヘッダータイトル
		headertitle.setSize(50);
		form.add(headertitle);
		//　サイトタイトル
		sitetitle.setSize(50);
		form.add(sitetitle);
		//　サイトリンク
		form.add(sitelinkable);
		//　メールアドレス
		mailto.setSize(50);
		form.add(mailto);
		//　メールリンク
		form.add(maillinkable);
		//　メールテキスト
		mailtext.setSize(50);
		form.add(mailtext);
		//　ポート番号
		portno.setMaxLength(5);
		portno.setSize(50);
		form.add(portno);
		//　ドキュメントルート
		docroot.setSize(50);
		form.add(docroot);
		//　テンプレートパス
		templatepath.setSize(50);
		form.add(templatepath);
		//　テンプレート編集ボタン
		form.add(edittemp);
		//　検索・エラー・条件なしメッセージ
		errnocond.setSize(50);
		form.add(errnocond);
		//　検索・エラー・結果なしメッセージ
		errnores.setSize(50);
		form.add(errnores);
		//　検索・結果・開始タグ
		resstarttag.setSize(50);
		form.add(resstarttag);
		//　検索・結果・終了タグ
		resendtag.setSize(50);
		form.add(resendtag);
		//　検索・前後表示文字数
		resshowlength.setMaxLength(2);
		resshowlength.setSize(50);
		form.add(resshowlength);
		//　検索・結果・強調開始タグ
		resemphstarttag.setSize(50);
		form.add(resemphstarttag);
		//　検索・結果・強調終了タグ
		resemphendtag.setSize(50);
		form.add(resemphendtag);
		//　ＯＫボタン
		form.add(ok);
		//　キャンセルボタン
		form.add(back);

	}
	
	/**
	 * 初期化処理
	 */
	public void onInit(){
		super.onInit();
		
		
	}
	
	/**
	 * POST処理
	 */
	public void onPost(){
		
		if(ok.isClicked()){
			return;
		}
		
		
		String wkstr = "";
		
		//-------------------------------------------------------------------
		
		wkstr = ArUtil.getProperty(this, ArUtil.PROP_ADMIN, ArUtil.KEY_ADMIN_CONF_NAME);
		username.setValue(wkstr);

		wkstr = ArUtil.getProperty(this, ArUtil.PROP_ADMIN, ArUtil.KEY_ADMIN_CONF_PASS);
		password1.setValue(wkstr);
		password2.setValue(wkstr);
		
		
		//-------------------------------------------------------------------
		//ヘッダータイトル
		wkstr = ArUtil.getProperty(this, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_HEADER);
		headertitle.setValue(wkstr);
		//サイトタイトル
		wkstr = ArUtil.getProperty(this, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_TITLE);
		sitetitle.setValue(wkstr);
		//サイトリンク
		wkstr = ArUtil.getProperty(this, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_TITLELINK);
		sitelinkable.setValue(wkstr);
		//メール
		wkstr = ArUtil.getProperty(this, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_MAILTO);
		mailto.setValue(wkstr);
		//メールリンク
		wkstr = ArUtil.getProperty(this, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_MAILLINK);
		maillinkable.setValue(wkstr);
		//メールタイトル
		wkstr = ArUtil.getProperty(this, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_MAILLINK);
		//ポート番号  
		wkstr = ArUtil.getProperty(this, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_PORTNO);
		portno.setValue(wkstr);
		//ドキュメントルート  
		wkstr = ArUtil.getProperty(this, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_DOCROOT);
		docroot.setValue(wkstr);
		//テンプレートhtmパス  
		wkstr = ArUtil.getProperty(this, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_TEMPLATE);
		templatepath.setValue(wkstr);
		//検索 エラー 条件なしメッセージ  
		wkstr = ArUtil.getProperty(this, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_SRCH_ERR_NOCOND);
		errnocond.setValue(wkstr);
		//検索 エラー 結果なしメッセージ  
		wkstr = ArUtil.getProperty(this, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_SRCH_ERR_NORES);
		errnores.setValue(wkstr);
		//検索 結果 表示開始タグ  
		wkstr = ArUtil.getProperty(this, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_SRCH_RES_START_TAG);
		resstarttag.setValue(wkstr);
		//検索 結果 表示終了タグ  
		wkstr = ArUtil.getProperty(this, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_SRCH_RES_END_TAG);
		resendtag.setValue(wkstr);
		//検索 結果 前後文字数  
		wkstr = ArUtil.getProperty(this, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_SRCH_RES_SHOWLONGTH);
		resshowlength.setValue(wkstr);
		//検索 結果 強調開始  
		wkstr = ArUtil.getProperty(this, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_SRCH_RES_EMPH_START);
		resemphstarttag.setValue(wkstr);
		//検索 結果 強調終了 
		wkstr = ArUtil.getProperty(this, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_SRCH_RES_EMPH_END);
		resemphendtag.setValue(wkstr);
		
		
		
	}
    
    /**
     * テンプレート編集処理
     * @return
     */
	public boolean onClickEditTemplate(){
		
    	String abspath = ArUtil.getProperty(this, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_TEMPLATE);
    	EditFilePage page = (EditFilePage)super.context.createPage(EditFilePage.class);
    	page.setParameter(abspath, this);
        super.setForward(page);
        return false;

	}
	
	/**
	 * OKボタン処理
	 * @return
	 */
	public boolean onClickOk(){

    	
		//　フィールド単体チェック -----------------------------------------------------
        
		//　ユーザ名
		String _username = username.getValue();
		if(_username.equals("")){
			addMessage("ユーザ名が入力されていません。");
			return false;
		}
		//　パスワード
		String _password1 = password1.getValue();
		if(_password1.equals("")){
			addMessage("パスワードが入力されていません。");
			return false;
		}
		//　パスワード（確認用）
		String _password2 = password2.getValue();
		if(_password2.equals("")){
			addMessage("パスワード（確認用）が入力されていません。");
			return false;
		}
		
		
		//　ヘッダー
		String _headertitle = headertitle.getValue();
		if(_headertitle.equals("")){
			addMessage("ヘッダーテキストが入力されていません。");
			return false;
		}
		//　サイトタイトル
		String _sitetitle = sitetitle.getValue();
		if(_sitetitle.equals("")){
			addMessage("サイトタイトルが入力されていません。");
			return false;
		}
		
		//　サイトリンク
		//　単体チェックなし
		
		//　メール
		String _mailto = mailto.getValue();
		if(_mailto.equals("")){
			addMessage("メールアドレスが入力されていません。");
			return false;
		}
		//　メールリンク
		//　単体チェックなし
		
		//　メールテキスト
		String _mailtext = mailtext.getValue();
		//　単体チェックなし
		
		//　ポート番号
		String _portno = portno.getValue();
		if(_portno.equals("")){
			addMessage("ポート番号は必須です。");
			return false;
		}
		
		//　ドキュメントルート
		String _docroot = docroot.getValue();
		if(_docroot.equals("")){
			addMessage("ドキュメントルートは必須です。");
			return false;
		}
		
		//　テンプレートパス
		String _templatepath = templatepath.getValue();
		if(_templatepath.equals("")){
			addMessage("テンプレートパスは必須です。");
			return false;
		}
		
		//　検索・エラー・条件なしメッセージ
		String _errnocond = errnocond.getValue();
		if(_errnocond.equals("")){
			addMessage("検索・エラー・条件なしは必須です。");
			return false;
		}
		
		//　検索・エラー・結果なしメッセージ
		String _errnores = errnores.getValue();
		if(_errnores.equals("")){
			addMessage("検索・エラー・結果なしは必須です。");
			return false;
		}
		
		//　検索・結果・開始タグ
		String _resstarttag = resstarttag.getValue();
		
		//　検索・結果・終了タグ
		String _resendtag = resendtag.getValue();
		
		//　検索・前後表示文字数
		String _resshowlength = resshowlength.getValue();
		
		//　検索・結果・強調開始タグ
		String _resemphstarttag = resemphstarttag.getValue();
		
		//　検索・結果・強調終了タグ
		String _resemphendtag = resemphendtag.getValue();
        
		
		//　相関チェック -----------------------------------------------------
		
		//　パスワードが確認用と異なる場合エラー
		if(!_password1.equals(_password2)){
			addMessage("パスワードが確認用と異なります。");
			return false;
		}
		
		
		//　admin.properties更新 -----------------------------------------------------
		
		ArUtil.setProperty(this, ArUtil.PROP_ADMIN, ArUtil.KEY_ADMIN_CONF_NAME, _username);
		ArUtil.setProperty(this, ArUtil.PROP_ADMIN, ArUtil.KEY_ADMIN_CONF_PASS, _password1);
		
		UserInfo user = (UserInfo)super.context.getSession().getAttribute(ArUtil.USER);
		user.setUsername(_username);
		
		//　site.properties更新 -----------------------------------------------------
		
		//ヘッダータイトル
		ArUtil.setProperty(this, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_HEADER, _headertitle);
		//サイトタイトル
		ArUtil.setProperty(this, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_TITLE, _sitetitle);
		//サイトリンク
		ArUtil.setProperty(this, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_TITLELINK, sitelinkable.getValue());
		//メール
		ArUtil.setProperty(this, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_MAILTO, _mailto);
		//メールリンク
		ArUtil.setProperty(this, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_MAILLINK, maillinkable.getValue());
		//メールリンクテキスト
		ArUtil.setProperty(this, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_MAILTITLE, _mailtext);
		//ポート番号  
		ArUtil.setProperty(this, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_PORTNO, _portno);
		//ドキュメントルート  
		ArUtil.setProperty(this, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_DOCROOT, _docroot);
		//テンプレートhtmパス  
		ArUtil.setProperty(this, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_TEMPLATE, _templatepath);
		//検索 エラー 条件なしメッセージ  
		ArUtil.setProperty(this, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_SRCH_ERR_NOCOND, _errnocond);
		//検索 エラー 結果なしメッセージ  
		ArUtil.setProperty(this, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_SRCH_ERR_NORES, _errnores);
		//検索 結果 表示開始タグ  
		ArUtil.setProperty(this, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_SRCH_RES_START_TAG, _resstarttag);
		//検索 結果 表示終了タグ  
		ArUtil.setProperty(this, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_SRCH_RES_END_TAG, _resendtag);
		//検索 結果 前後文字数  
		ArUtil.setProperty(this, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_SRCH_RES_SHOWLONGTH, _resshowlength);
		//検索 結果 強調開始  
		ArUtil.setProperty(this, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_SRCH_RES_EMPH_START, _resemphstarttag);
		//検索 結果 強調終了 
		ArUtil.setProperty(this, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_SRCH_RES_EMPH_END, _resemphendtag);
		
		
		addMessage("設定を更新しました。");
		
        return true;        
    }
    
	/**
	 * 戻る処理
	 * @return
	 */
    public boolean onClickBack(){
        
        super.setForward(ToolMainPage.class);
        return false;
        
    }
    
    
    public void onRender(){
		super.onRender();

    }
    
}
