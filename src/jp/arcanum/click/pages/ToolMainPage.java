/**
 * 
 */
package jp.arcanum.click.pages;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import jp.arcanum.click.ArUtil;
import jp.arcanum.click.PageProperties;
import jp.arcanum.click.UserInfo;
import jp.arcanum.click.pages.controls.ArTreeNode;
import jp.arcanum.click.pages.controls.LoginSubmit;
import net.sf.click.Context;
import net.sf.click.control.Checkbox;
import net.sf.click.control.FileField;
import net.sf.click.control.HiddenField;
import net.sf.click.control.Option;
import net.sf.click.control.PasswordField;
import net.sf.click.control.Select;
import net.sf.click.control.Submit;
import net.sf.click.control.TextArea;
import net.sf.click.control.TextField;
import net.sf.click.extras.control.PickList;
import net.sf.click.extras.tree.Tree;
import net.sf.click.extras.tree.TreeListener;
import net.sf.click.extras.tree.TreeNode;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.FileUtils;


/**
 * ツールページクラス。
 */
public class ToolMainPage extends AbstractToolPage implements TreeListener{

	/** ディレクトリツリー情報のうちルートノードをセッションに格納する際のキー */
	private static final String TOOL_TREE = "TOOL_TREE";

	/**
	 * 新規ディレクトリ作成ボタン
	 */
	private Submit newdir = new Submit("newdir", "新規",this,"onClickNewDir");
	
	/**
	 * 新規ディレクトリ名入力フィールド
	 */
	private TextField newdirname = new TextField("newdirname");

	/**
	 * ディレクトリ削除ボタン
	 */
	private Submit deldir = new Submit("deldir", "削除", this, "onClickDeleteDir");
	
	/**
	 * グループのパスワード変更フィールド
	 */
	private PasswordField password = new PasswordField("password"){
		
		public String toString(){
			UserInfo user = (UserInfo)super.context.getSession().getAttribute(ArUtil.USER);
			if(user == null){
				return "";
			}
			if(user.isAdmin()){
				return "";
			}
			return super.toString();
		}
	};
	
	/**
	 * 更新前のマークアップ方法
	 */
	private HiddenField exmarkup = new HiddenField("exmarkup", String.class);
	
	/**
	 * グループのパスワード変更ボタン
	 */
	private LoginSubmit changepass = 
		new LoginSubmit(
				"changepass", 
				"パスワード変更",
				this,
				"onClickChangePass",
				false);
		
	/**
	 * スーパーユーザのメニュー
	 */
	private LoginSubmit manageuser = 
		new LoginSubmit(
				"manageuser", 
				"ユーザ管理", 
				this, 
				"onClickManageUser",
				true);
	
	/**
	 * 
	 */
	private LoginSubmit managesite = 
		new LoginSubmit(
				"managesite", 
				"サイト設定", 
				this, 
				"onClickManageSite",
				true);
	
	
	/**
	 * ログアウトボタン
	 */
	private Submit logout = new Submit("logout", "ログアウト", this, "onClickLogout");
	
	
	//---------------------------------------------------------------------------
	
	/**
	 * ディレクトリを選択しているかどうか<br>
	 * trueの場合、詳細を表示する。
	 */
	public boolean isselect = false;

	/**
	 * ディレクトリツリーが生成できたかどうか
	 */
	public boolean istreeok = false;
	
	/**
	 * ディレクトリツリー
	 */
	public Tree dirtree = new Tree("dirtree");
	
	
	//------------------------------------------------------------------------
	//　詳細のフィールド
	//------------------------------------------------------------------------
	
	/**
	 * ディレクトリの物理名
	 */
	private TextField dirpname = new TextField("dirpname");
	/**
	 * ディレクトリの論理名
	 */
	private TextField dirlname = new TextField("dirlname");
	/**
	 * ディレクトリのオーナー
	 */
	private TextField dirowner = new TextField("dirowner");
	/**
	 * ディレクトリの編集者
	 */
	private PickList  direditor = new PickList("direditor");
	
	/**
	 * ディレクトリを表示するかどうかのチェックボックス
	 */
	private Checkbox publication     = new Checkbox("publication");
	/**
	 * ディレクトリを表示するかどうかのチェックボックス
	 */
	private Checkbox isshow     = new Checkbox("isshow");
	/**
	 * ディレクトリの編集方法<br>
	 * html、wikiの二つを表示
	 */
	private Select   editmeth   = new Select("editmeth");
	/**
	 * 配下ディレクトリを表示するかどうか
	 */
	private Checkbox rendierchildren = new Checkbox("rendierchildren");
	/**
	 * ディレクトリの説明
	 */
	private TextArea description = new TextArea("description");
	/**
	 * 詳細の更新ボタン
	 */
	private Submit okdetail = new Submit("okdetail", "更新", this, "onClickOkDetail");
	/**
	 * index.htmのアップロード（ファイルフィールド）
	 */
	private FileField filefld = new FileField("filefld");
	/**
	 * index.htmのアップロード（ボタン）
	 */
	private Submit uploadindex = new Submit("uploadindex", "アップロード", this, "onClickUpload");
	/**
	 * index.htmの編集ボタン
	 */
	private Submit editindex = new Submit("editindex", "内容編集", this, "onClickEditIndex");
	
	/**
	 * ファイルリストボックス
	 */
	private Select filelist = new Select("filelist");
	
	/**
	 * ファイルコピーボタン
	 */
	private Submit filecopy = new Submit("filecopy", "コピー", 				this, "onClickFileCopy");
	/**
	 * ディレクトリコピーボタン
	 */
	private Submit dircopy = new Submit("dircopy", "ディレクトリコピー", 		this, "onClickDirCopy");
	/**
	 * コピー時のメッセージエリア
	 */
	public String copymessage = "";
	/**
	 * 貼り付けボタン
	 */
	private Submit pasteobj = new Submit("pasteobj", "貼り付け", 				this, "onClickPaste");
	/**
	 * ファイル削除ボタン
	 */
	private Submit filedelete = new Submit("filedelete", "削除", 				this, "onClickFileDelete");
	/**
	 * ファイル編集ボタン
	 */
	private Submit fileedit = new Submit("fileedit", "編集", 					this, "onClickFileEdit");
	/**
	 * ファイルダウンロードボタン
	 */
	private Submit filedownload = new Submit("filedownload", "ダウンロード", 	this, "onClickFileDownload");
	/**
	 * ファイルセレクトボックス
	 */
	private HiddenField targetfile = new HiddenField("selfile", String.class);
	/**
	 * リネームボタン
	 */
	private Submit filerename = new Submit("filerename", "リネーム", this, "onClickFilrRename");
	/**
	 * 新規ファイル作成ボタン
	 */
	private Submit filemake   = new Submit("filemake", "新規ファイル", this, "onClickFileMake");
	/**
	 * リネーム名、新規ファイル名入力エリア
	 */
	private TextField inputfilename = new TextField("inputfilename"); 
	
	/**
	 * コンストラクタ
	 *
	 */
	public ToolMainPage(){
		
		//　新規ボタン		
		form.add(newdir);
		//　新規ディレクトリ名
		form.add(newdirname);
		//　ユーザ管理
		form.add(manageuser);
		//　パスワードテキスト
		password.setSize(20);
		form.add(password);
		//　サイト設定ボタン
		form.add(managesite);
		//　パスワード変更ボタン
		form.add(changepass);
		//　削除ボタン
		deldir.setAttribute("style", "color:blue;");
		deldir.setAttribute("onclick", "return confirm('選択されたディレクトリを含む、配下のディレクトリも削除します')");
		form.add(deldir);
		
		//　ディレクトリツリー
		dirtree.addListener(this);
		
		//　詳細の更新ボタン
		form.add(okdetail);
		
		//　ログアウト
		form.add(logout);
		
		//------------------------------------------------
		//　詳細の設定
		//------------------------------------------------
		
		//　物理名
		dirpname.setAttribute("disabled", "true");
		dirpname.setSize(40);
		form.add(dirpname);
		//　論理名
		dirlname.setSize(40);
		form.add(dirlname);
		//　ディレクトリの所有者
		dirowner.setSize(40);
		form.add(dirowner);
		//　ディレクトリの編集者
		form.add(direditor);
		//　ディレクトリを表示するか
		form.add(isshow);
		//　公開するか
		form.add(publication);
		//　編集方法
		editmeth.add(new Option("html"));
		editmeth.add(new Option("wiki"));
		form.add(editmeth);
		form.add(exmarkup);
		//　配下を描画するか
		form.add(rendierchildren);
		//　ディレクトリの説明
		description.setCols(40);
		description.setRows(5);
		form.add(description);
		//　indexアップロード（フィールド）
		filefld.setAttribute("style", "width:400px;");
		form.add(filefld);
		//　indexアップロード（ボタン）
		form.add(uploadindex);
		//  index編集
		form.add(editindex);
		//　ファイルリストボックス
		filelist.setSize(7);
		filelist.setAttribute("style", "width:200px;");
		form.add(filelist);
		
		//　コピー
		filecopy.setAttribute("style", "width:80px");
		filecopy.setOnClick("form.selfile.value=form.filelist.value");
		form.add(filecopy);
		//　ディレクトリコピー
		dircopy.setAttribute("style", "width:80px");
		dircopy.setOnClick("form.selfile.value=form.filelist.value");
		form.add(dircopy);
		//　貼り付け
		pasteobj.setAttribute("style", "width:80px");
		pasteobj.setOnClick("form.selfile.value=form.filelist.value");
		form.add(pasteobj);
		//　ダウンロード
		filedownload.setAttribute("style", "width:80px");
		filedownload.setOnClick("form.selfile.value=form.filelist.value");
		form.add(filedownload);
		//　削除
		filedelete.setAttribute("style", "width:80px");
		filedelete.setOnClick("form.selfile.value=form.filelist.value");
		form.add(filedelete);
		//　編集
		fileedit.setAttribute("style", "width:80px");
		fileedit.setOnClick("form.selfile.value=form.filelist.value");
		form.add(fileedit);
		//　オペレーション対象ファイル
		form.add(targetfile);

		//　リネームボタン
		filerename.setAttribute("style", "width:80px");
		filerename.setOnClick("form.selfile.value=form.filelist.value");
		form.add(filerename);
		//　新規ファイル作成ボタン
		filemake.setAttribute("style", "width:80px");
		filemake.setOnClick("form.selfile.value=form.filelist.value");
		form.add(filemake);
		//　ファイル名称
		form.add(inputfilename);
		
	}

	
	/**
	 * 初期化処理
	 */
	public void onInit(){
	
		buildTree();
		
		
	}
	

	/**
	 * 描画前処理
	 */
	public void onRender(){
		
		super.onRender();
		
		//　ディレクトリが選択されていない場合は、ボタンを使用不可とする
		List selectednodes = dirtree.getSelectedNodes(true);
		if(selectednodes.isEmpty()){
			this.newdir.setAttribute("disabled", "true");
			this.deldir.setAttribute("disabled", "true");
			
		}
		//　なにかしら選択状態の場合は、ディレクトリの設定を表示
		else{
			
			UserInfo user = (UserInfo)super.context.getSession().getAttribute(ArUtil.USER);
			
			isselect = true;
			TreeNode selected = (TreeNode)selectednodes.get(0);
			
			//　ページプロパティを取得
			String path = super.context.getServletContext().getRealPath("");
			path = path + "/" + selected.getId();
			PageProperties prop = ArUtil.getPageProperties(path);
			
			//　手動で消している場合がある
			if(!new File(path).exists()){
				addMessage("選択されたディレクトリは削除されています。");
				super.context.getSession().setAttribute("FORCE_BUILD_TREE", "FORCE_BUILD_TREE");
				buildTree();
				return;
			}
			
			//　ディレクトリ物理名
			this.dirpname.setValue((String)selected.getValue());
			//　ディレクトリ論理名
			this.dirlname.setValue(prop.getTitle());
			//　メニュー表示
			this.isshow.setChecked(prop.isVisible());
			//　公開
			this.publication.setChecked(prop.isPublic());
			//　マークアップ
			this.editmeth.setValue(prop.getMarkup());
			this.exmarkup.setValue(prop.getMarkup());
			//　配下を描画するか
			this.rendierchildren.setChecked(prop.isRenderChild());
			//　説明
			String desc = "";
			for(int i = 0 ; i < prop.getDescs().size(); i++){
				desc = desc + prop.getDescs().get(i) + "\n";
			}
			this.description.setValue(desc);
			
			//　ファイルリスト
			File dir = new File(path);
			File[] files = dir.listFiles();
			for(int i = 0 ; i < files.length; i++){
				if(files[i].isFile()) filelist.add(files[i].getName());
			}
			
			//　コピー状態を表示
			String copytype   = (String)super.context.getSession().getAttribute("COPY_TYPE");
			String copytarget = (String)super.context.getSession().getAttribute("COPY_TARGET");
			if(copytype == null){
				pasteobj.setAttribute("disabled", "true");
				copymessage = "";
			}
			else{
				copymessage = "[" + copytarget + "]が貼り付けできます。";
			}

			if(!user.isAdmin()){
				okdetail.setAttribute("disabled", "true");
			}

		
		}

	
	
	}
	
	
	/**
	 * ディレクトリツリーを構成する<br>
	 * このメソッドは、リスナを設定しているため、必ずonInit()以前で
	 * 使用すること
	 *
	 */
	private void buildTree(){

        TreeNode existingRootNode = 
        	(TreeNode)super.context.getSession().getAttribute(TOOL_TREE);
        if(super.context.getSession().getAttribute("FORCE_BUILD_TREE")== null 
        		&& existingRootNode != null ) {
        	
    			dirtree.setRootNode(existingRootNode);
    	        dirtree.setRootNodeDisplayed(true);
    	        istreeok = true;
                return;
        	
        }

		super.context.getSession().removeAttribute("FORCE_BUILD_TREE");
		
        //　ルートパスを取得
        String docroot = ArUtil.getProperty(this, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_DOCROOT);
        if(!docroot.startsWith("/")){
        	docroot = "/" + docroot;
        }
        String rootpath = context.getServletContext().getRealPath(docroot);
        String absrootpath = rootpath.substring(0,rootpath.length() - docroot.length());
        
        //　ルートパスが存在しなければ終了
        File rootpathfile = new File(rootpath);
        if(!rootpathfile.exists()){
        	return;
        }
        
        //　ツリーを生成
    	istreeok = true;
        
        PageProperties prop = ArUtil.getPageProperties(rootpath);
        String title = rootpath.substring(absrootpath.length());
        if(title.startsWith("\\")){
            title = "/" + title.substring(1);
        }
        
        if(prop.isPublic()){
        	title = title + " [+";
        }
        else{
        	title = title + " [-";
        }
        if(prop.isVisible()){
        	title = title + "+";
        }
        else{
        	title = title + "-";
        }
        if(prop.getMarkup().equals(PageProperties.MARKUP_HTML)){
        	title = title + "h";
        }
        else{
        	title = title + "w";
        }
        if(prop.isRenderChild()){
        	title = title + "+]";
        }
        else{
        	title = title + "-]";
        }
        
        
        
        
        ArTreeNode rootnode = new ArTreeNode(title,docroot);
        rootnode.setIsVisible(prop.isVisible());
        rootnode.setIsLink(prop.isIslink());
        rootnode.setIsRenderChild(prop.isRenderChild());
        
        //再帰的にディレクトリ構造をセットする
        this.setTree(rootnode, rootpath, absrootpath);

        //　ツリー自体の設定
        dirtree.setRootNode(rootnode);
        dirtree.expandAll();
        dirtree.setRootNodeDisplayed(true);
        if(existingRootNode != null){
        	
        	List selectedlist = dirtree.getSelectedNodes(true);
        	if(!selectedlist.isEmpty()){
        		
        		TreeNode selected = (TreeNode)selectedlist.get(0);
        		dirtree.select(selected.getId());
        		
        	}
        	
        }
        
        
        //　このツリーをセッションに保存
        getContext().getSession().setAttribute(TOOL_TREE, rootnode);

	
	}
	
	/**
	 * 再帰的にドキュメントルートから検索を行い、ディレクトリツリーに表示
	 * @param node
	 * @param path
	 * @param rootpath
	 */
	private void setTree(ArTreeNode node, String path, String rootpath){
		
		
		File file = new File(path);
		String[] files = file.list();
		
		for(int i = 0 ; i < files.length; i++){
			
			String abspath = path + "/" + files[i];
			File wkfile = new File(abspath);
			if(wkfile.isDirectory()){
				
				PageProperties prop = ArUtil.getPageProperties( abspath);
				
				String dirname =  abspath.substring(abspath.lastIndexOf("/"));
				
				//　このフォルダをノード化する
				String id = abspath.substring(rootpath.length()+1);
				if(id.startsWith("\\")){
					id = "/" + id.substring(1);
				}
				
				if(id.equals("/.settings")){
					continue;
				}
				if(id.equals("/work")){
					continue;
				}
				if(id.equals("/WEB-INF")){
					continue;
				}
				if(id.equals("/click")){
					continue;
				}
				if(id.equals("/tools")){
					continue;
				}

		        if(prop.isPublic()){
		        	dirname = dirname + " [+";
		        }
		        else{
		        	dirname = dirname + " [-";
		        }
		        if(prop.isVisible()){
		        	dirname = dirname + "+";
		        }
		        else{
		        	dirname = dirname + "-";
		        }
		        if(prop.getMarkup().equals(PageProperties.MARKUP_HTML)){
		        	dirname = dirname + "h";
		        }
		        else{
		        	dirname = dirname + "w";
		        }
		        if(prop.isRenderChild()){
		        	dirname = dirname + "+]";
		        }
		        else{
		        	dirname = dirname + "-]";
		        }
		        
				
				
				ArTreeNode child = 
					new ArTreeNode(dirname, id, node);

				child.setIsVisible(prop.isVisible());
				child.setIsLink(prop.isIslink());
				child.setIsRenderChild(prop.isRenderChild());
				
				setTree(child, abspath, rootpath);
						
			}
		}
		
	}

	/**
	 * 新規ディレクトリ作成
	 * @return 
	 */
	public boolean onClickNewDir(){
		
		String dirname = newdirname.getValue();
		dirname = dirname.trim();
		if(dirname.equals("")){
			addMessage("ディレクトリ名を入力してください。");
			return true;
		}
		
		//　ファイル禁則文字のチェック
		//　　・Windows　\/:;*?"<>
		//　　・Unix　　 /以外はOK
		//  補足：
		//　　　OSの判断をしてしまうと、Webサーバの載っているOS
		//　　　がバレてしまうので、Windowsにあわせる。
		String checkstr = "\\/:;*?\"<>";
		for(int i = 0 ; i < checkstr.length(); i++){
			if(dirname.indexOf(checkstr.substring(i,i+1))!=-1){
				addMessage("ディレクトリに使用できない文字があります。");
				return true;
			}
		}
		
	
		//　ファイル禁則文字のチェック
		//　　・漢字などは作れるけど、Tree上で選択できなくなるので
        byte[] bytes = dirname.getBytes();
        int beams = dirname.length();
        if (beams != bytes.length) {
        	addMessage("全角は使用できません。");
        	return true;
        }
	
		
		//　ディレクトリが選択されていない場合があるので
		List selected = dirtree.getSelectedNodes(true);
		if(selected.isEmpty()){
			addMessage("親となるディレクトリが選択されていません。");
			return true;
		}
		
		
		//　作成する実ディレクトリパスを編集
		TreeNode node = (TreeNode)selected.get(0);
		String abspath = super.context.getServletContext().getRealPath("");
		String makedir = abspath + "/" + node.getId() + "/" + dirname;
		 
		
		//　既に存在するディレクトリの場合エラー
		File file = new File(makedir);
		if(file.exists()){
			addMessage("既にディレクトリが作成されています。");
			return true;
		}
		
		//　強制的にディレクトリを作成
		try {
			FileUtils.forceMkdir(file);
		} catch (IOException e) {
			addMessage("ディレクトリの作成に失敗しました。" + e.getMessage());
			return true;
		}
		
		//　補足：ここでindex.htm,wiki.txtを作るべきか？
		//　　　当然シーケンスは新規ディレクトリを作ってから
		//　　　マークアップを変更して内容の編集となるので
		//　　　ココで作られるのはhtmlのみになる。但し、ユーザ
		//　　　によっては、ディレクトリを作ってすぐにページを
		//　　　表示する場合があるので、一応ここでも作る
		
		//　index.htm,wiki.txtを作る
		if(editmeth.getValue().equals("html")){
			List lines = new ArrayList();
			lines.add("<br>");
			lines.add("<hr>");
			lines.add("<h1>この内容を変更してください。</h1>");
			lines.add("<hr>");
			ArUtil.writeFile(makedir + "/index.htm", lines);
		}
		else{
			List lines = new ArrayList();
			lines.add("$wikicontents");
			ArUtil.writeFile(makedir + "/wiki.txt", lines);
			
			List wikilines = new ArrayList();
			wikilines.add("--");
			wikilines.add("この内容を変更してください。");
			wikilines.add("--");
			ArUtil.writeFile(makedir + "/index.htm", wikilines);
			
		}
		
		//　セッション経由でディレクトリツリーを再作成させる。
		super.context.getSession().setAttribute("FORCE_BUILD_TREE", "FORCE_BUILD_TREE");
		setLastUpdatedTime();
		
		buildTree();
		
		return true;
		
	}
	
	/**
	 * ユーザ管理処理
	 * @return
	 */
	public boolean onClickManageUser(){
		
		super.setForward(ManageUserPage.class);
		return false;

	}
	
	/**
	 * 削除ボタン処理
	 * @return
	 */
	public boolean onClickDeleteDir(){
		
		//　ディレクトリが選択されていない場合
		List selected = dirtree.getSelectedNodes(true);
		if(selected.isEmpty()){
			addMessage("削除するディレクトリが選択されていません。");
			return true;
		}
		
		//　削除するディレクトリの取得
		TreeNode node = (TreeNode)selected.get(0);
		String abspath = super.context.getServletContext().getRealPath("");
		String deldir = abspath + node.getId() ;
		System.out.println( deldir + "　を削除しまっせ");
		
		//　削除対象ディレクトリの存在チェック
		File file = new File(deldir);
		if(!file.exists()){
			addMessage("既にディレクトリが削除されていました。");
			//　削除したいディレクトリを誰かが削除したんだから良いって言えば良いんだけど・・・
			return true;
		}

		try {
			//　強制的にディレクトリを削除（再帰的に削除します）
			FileUtils.forceDelete(file);
		} catch (IOException e) {
			addMessage("ディレクトリの削除に失敗しました。" + e.getMessage());
			return true;
		}

		super.context.getSession().setAttribute("FORCE_BUILD_TREE", "FORCE_BUILD_TREE");
		setLastUpdatedTime();
		buildTree();
		
		return true;
	}

	/**
	 * ログアウトボタン処理
	 * @return
	 */
	public boolean onClickLogout(){
		
		super.context.getSession().removeAttribute(ArUtil.USER);
		super.context.getSession().invalidate();
		
		super.setRedirect(LoginPage.class);
		return false;
		
	}

	
	//-----------------------------------------------------------------------------
	
	
	public void nodeCollapsed(Tree tree, TreeNode node, Context context, boolean oldValue) {
	}


	public void nodeDeselected(Tree tree, TreeNode node, Context context, boolean oldValue) {
	}


	public void nodeExpanded(Tree tree, TreeNode node, Context context, boolean oldValue) {
	}

	/**
	 * ディレクトリツリーのノードがクリックされたとき<br>
	 * ディレクトリツリーのうち１つのノードのみが選択されるように制御します。
	 */
	public void nodeSelected(Tree tree, TreeNode node, Context context, boolean oldValue) {

		System.out.println("--------------------------------");
		System.out.println(node);
		System.out.println(oldValue);
		
		
        List selected = tree.getSelectedNodes(false);
        for(int i = 0 ; i < selected.size(); i++){
            TreeNode selnode = (TreeNode)selected.get(i);
            if(selnode != node){
                tree.deselect(selnode);
            }
        }
        
        isselect = true;
	
	}

	
	/**
	 * 内容更新ボタン処理
	 * @return
	 */
	public boolean onClickOkDetail() {

		if(dirlname.getValue().trim().equals("")){
			addMessage("ディレクトリの論理名が入力されていません。");
			return true;
		}
		
		// TODO DOMで保存するようにする
		// TODO ユーティリティ化する
		
		//　保存する内容の編集
		List lines = new ArrayList();
		lines.add("<?xml version=\"1.0\" encoding=\"Shift_JIS\" ?>" + "\n");
		String page = "<page ";
		page = page + "visible=\"" + this.isshow.isChecked() + "\" ";
		page = page + "markup=\"" + this.editmeth.getValue() + "\" ";
		page = page + "renderchild=\"" + this.rendierchildren.isChecked() + "\" ";
		page = page + "public=\"" + this.publication.isChecked() + "\" ";
		page = page + ">";
		lines.add(page + "\n");
		
		String title = "<title>" + this.dirlname.getValue() + "</title>";
		lines.add(title + "\n");
		
		String descs = description.getValue();
		descs = descs.replaceAll("\r", "");
		StringTokenizer tokens = new StringTokenizer(descs,"\n");
		while(tokens.hasMoreTokens()){
			String token = tokens.nextToken();
			String desc = "<desc>" + token + "</desc>";
			lines.add(desc + "\n");
		}
		lines.add("</page>");
		
		//　保存するファイルの特定
        List selected = dirtree.getSelectedNodes(false);
        if(selected.isEmpty()){
        	addMessage("更新すべきディレクトリが分からなくなった！");
        	return true;
        }
        TreeNode node = (TreeNode)selected.get(0);
 
        String path = super.context.getServletContext().getRealPath("");
        String abspath = path + "/" + node.getId();
        String xmlpath = abspath + "/page.xml";
        
        //　次のようなイメージで更新される
		//<//page visible="true" link="true" markup="html" renderchild="true" >
	    //	<title>トップページ</title>
	    //	<desc>はじめに</desc>
	    //	<desc>至極のクリックリンク集</desc>
	    //	<desc>更新履歴</desc>
	    //	<desc>その他</desc>
		//</page>
        ArUtil.writeFile(xmlpath, lines);
		
        //　履歴をつくるようにする
        if(!this.exmarkup.getValue().equals(this.editmeth.getValue())){

        	//マークアップの更新
        	this.exmarkup.setValue(this.editmeth.getValue());
        	
			Date date = new Date();
			SimpleDateFormat f = new SimpleDateFormat("yyyyMMddkkmmssSSS");
			String ymd = f.format(date);

			//　index.htm,wiki.txtを作る
    		if(editmeth.getValue().equals("html")){

    			// バックアップを取る yyyymmddhhmmssSSS_wiki.txt
    			//                    yyyymmddhhmmssSSS_index.htm
    			File exindexwiki = new File(abspath + "/wiki.txt");
    			//File exindexhtml = new File(abspath + "/index.htm");
    			if(exindexwiki.exists()){
    				
        			try {
    					FileUtils.copyFile(exindexwiki,	new File(abspath + "/" + ymd + "_wiki.txt"));
    					//FileUtils.copyFile(exindexhtml,	new File(abspath + "/" + ymd + "_index.htm"));
    							
    				} catch (IOException e) {
    					throw new RuntimeException("ファイルコピー失敗");
    				}
    				
    			}
    			

    			List lineshtml = new ArrayList();
    			lineshtml.add("<hr>");
    			lineshtml.add("<h1>この内容を変更してください。</h1>");
    			lineshtml.add("<h2>マークアップはhtmlです</h2>");
    			lineshtml.add("<hr>");
    			
    			ArUtil.writeFile(abspath + "/index.htm", lineshtml);

    		}
    		else{
    			

    			// バックアップを取る yyyymmddhhmmssSSS_index.htm
    			File exindex = new File(abspath + "/index.htm");
    			if(exindex.exists()){
    				
        			try {
    					FileUtils.copyFile(exindex,	new File(abspath + "/" + ymd + "_index.htm")
    							);
    				} catch (IOException e) {
    					throw new RuntimeException("ファイルコピー失敗");
    				}
    				
    			}

    			
    			List lineshtml = new ArrayList();
    			lineshtml.add("$wikicontents");
    			ArUtil.writeFile(abspath + "/index.htm", lineshtml);
    			
    			List lineswiki = new ArrayList();
    			lineswiki.add("----\n");
    			lineswiki.add("!この内容を変更してください。\n");
    			lineswiki.add("!!マークアップはwikiです\n");
    			lineswiki.add("----\n");
    			
    			ArUtil.writeFile(abspath + "/wiki.txt", lineswiki);
    			
    		}

        }
        
        addMessage("設定を更新しました。");
		isselect = true;
		super.context.getSession().setAttribute("FORCE_BUILD_TREE", "FORCE_BUILD_TREE");
		buildTree();
		setLastUpdatedTime();

		return true;
	
	}

	/**
	 * 内容編集ボタン
	 * @return
	 */
	public boolean onClickEditIndex(){
		
        List selectednode = dirtree.getSelectedNodes(true);
        TreeNode node = (TreeNode)selectednode.get(0);
        
        super.context.setRequestAttribute("EDIT_PATH", node.getId());
		
		super.setForward(EditIndexPage.class);
		return false;
		
	}
	
	/**
	 * サイト管理ボタン
	 * @return
	 */
	public boolean onClickManageSite(){
		
		super.setForward(ManageSitePage.class);
		return false;
		
	}
	
	/**
	 * アップロードボタン
	 * @return
	 */
	public boolean onClickUpload(){
		
		FileItem item = filefld.getFileItem();
		if(item.getSize() == 0){
			addMessage("アップロードするファイルが選択されていません。");
			return true;
		}
		
		
		InputStream inst = null;
		try {
			inst = item.getInputStream();
		} catch (IOException e) {
			addMessage("ファイルのアップロード中にエラーがおきました。" + e.getMessage());
			return true;
		}

		//　どのディレクトリにアップロードするか
        List selected = dirtree.getSelectedNodes(false);
        TreeNode node = (TreeNode)selected.get(0);
        
        //　ディレクトリへの絶対パスを編集
        String path = super.context.getServletContext().getRealPath("");
        path = path + "/" + node.getId() + "/";
        
        //　ファイル名称の編集
        String filename = item.getName();
        String filesep = ArUtil.getClientFileSeparator(this);
        filename = filename.substring(filename.lastIndexOf(filesep)+1);
        path = path + filename;
        
        //　アップロード処理
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(path);
			byte[] b = new byte[2048];
			while((inst.read(b,0,2048)) != -1){
				fos.write(b,0 ,2048);
			}
			
		} catch (Exception e) {
			addMessage("ファイルのアップロード中にエラーがおきました。" + e.getMessage());
			return true;
		}
		finally{
			try {
				if(fos != null)fos.close();
			} catch (IOException e) {
				addMessage("ファイルのクローズ中にエラーがおきました。" + e.getMessage());
				return true;
			}
		}
		
		addMessage("ファイル「"+ item.getName() +"」をアップロードしました。");
		return true;
		
	}

	/**
	 * 最終更新時間<br>
	 * この処理は最終的に使用されなくなる予定
	 *
	 */
	private void setLastUpdatedTime(){
		
        String lastpath = super.context.getServletContext().getRealPath("/");
        lastpath = lastpath + ArUtil.DIR_CONF + "lastupdated.txt";

        Date now = new Date();
        List lines = new ArrayList();
        lines.add(now.toString());
        
        ArUtil.writeFile(lastpath, lines);
        
	}
	
	/**
	 * ファイル編集ボタン
	 * @return
	 */
	public boolean onClickFileEdit(){
		
		String selfile = targetfile.getValue();
		if(selfile.equals("")){
			addMessage("ファイルが選択されていません。");
			return true;
		}
		if(selfile.endsWith("_index.htm")||
		   selfile.endsWith("_wiki.txt")){
			addMessage("履歴ファイルは編集できません。");
			return true;
		}
		if(selfile.endsWith("index.htm")){
			return onClickEditIndex();
		}
		
		//　ディレクトリが選択されている場合のみ
		List selectednodes = dirtree.getSelectedNodes(true);
		if(!selectednodes.isEmpty()){
			
			TreeNode node = (TreeNode)selectednodes.get(0);
			
			//　編集画面に遷移
			EditFilePage page = (EditFilePage)super.context.createPage(EditFilePage.class);
			page.setParameter(node.getId() + "/" + selfile, this);
			super.setForward(page);
		}
		else{
			addMessage("編集すべきファイルが分からなくなった！！");
		}
		
		return true;
	}


	/**
	 * ファイル削除処理
	 * @return
	 */
	public boolean onClickFileDelete(){
		
		String selfile = targetfile.getValue();
		if(selfile.equals("")){
			addMessage("ファイルが選択されていません。");
			return true;
		}
		
		//　ディレクトリが選択されている場合のみ
		List selectednodes = dirtree.getSelectedNodes(true);
		if(!selectednodes.isEmpty()){
			
			TreeNode node = (TreeNode)selectednodes.get(0);
			String abspath = super.context.getServletContext().getRealPath("");
			abspath = abspath + node.getId() + "/" + selfile;

			try {
				FileUtils.forceDelete(new File(abspath));
			} catch (IOException e) {
				throw new RuntimeException("ファイル削除に失敗 " + abspath , e);
			}
			
			addMessage("[" + node.getId() + "/" + selfile + "]の削除を行いました。");
		}
		else{
			addMessage("編集すべきファイルが分からなくなった！！");
		}
		
		return true;
	}


	/**
	 * ファイルダウンロード処理
	 * @return
	 */
	public boolean onClickFileDownload(){

		//　ファイルが選択されているか
		String selfile = targetfile.getValue();
		if(selfile.equals("")){
			addMessage("ファイルが選択されていません。");
			return true;
		}
		
		//　ディレクトリが選択されている場合のみ
		List selectednodes = dirtree.getSelectedNodes(true);
		if(selectednodes.isEmpty()){
			addMessage("編集すべきファイルが分からなくなった！！");
			
		}
		
		//　ダウンロードパスの編集
		TreeNode node = (TreeNode)selectednodes.get(0);
		String abspath = super.context.getServletContext().getRealPath("");
		abspath = abspath + node.getId() + "/" + selfile;

		//　ダウンロード画面に処理を委譲
		super.context.getRequest().setAttribute("DLPATH", abspath);
		super.setForward(DownloadPage.class);

		return true;
	
	}

	

	/**
	 * ファイルコピー処理
	 * @return
	 */
	public boolean onClickFileCopy(){
		
		String selfile = targetfile.getValue();
		if(selfile.equals("")){
			addMessage("ファイルが選択されていません。");
			return true;
		}
		
		//　ディレクトリが選択されている場合のみ
		List selectednodes = dirtree.getSelectedNodes(true);
		if(!selectednodes.isEmpty()){
			
			TreeNode node = (TreeNode)selectednodes.get(0);

			super.context.getSession().setAttribute("COPY_TYPE", "FILE");
			super.context.getSession().setAttribute("COPY_TARGET", node.getId() + "/" + selfile);
		
		
		}
		else{
			addMessage("コピーすべきファイルが分からなくなった！！");
		}
		
		return true;
	}
	
	/**
	 * ディレクトリコピー処理
	 * @return
	 */
	public boolean onClickDirCopy(){
		
		//　ディレクトリが選択されている場合のみ
		List selectednodes = dirtree.getSelectedNodes(true);
		if(!selectednodes.isEmpty()){
			
			TreeNode node = (TreeNode)selectednodes.get(0);

			super.context.getSession().setAttribute("COPY_TYPE", "DIR");
			super.context.getSession().setAttribute("COPY_TARGET", node.getId() );
		
		}
		else{
			addMessage("コピーすべきディレクトリがわからなくなった！！");
		}
		
		return true;
	}
	
	/**
	 * 貼り付け処理
	 * @return
	 */
	public boolean onClickPaste(){
		
		String type   = (String)super.context.getSession().getAttribute("COPY_TYPE");
		String target = (String)super.context.getSession().getAttribute("COPY_TARGET");
	
		if(type == null){
			addMessage("コピーする種別が分かりません　" + type);
			return true;
		}
		
		if(type.equals("FILE")){
			return pasteFile();
		}
		else if(type.equals("DIR")){
			
			boolean ret = pasteDir();
			
			super.context.getSession().setAttribute("FORCE_BUILD_TREE", "FORCE_BUILD_TREE");
			buildTree();
			setLastUpdatedTime();
				
			addMessage(target + " を貼り付けました。");
			
			return ret;
		}
		else{
			addMessage("コピーする対象が分かりません　" + target);
			return true;
		}
		
	}
	
	/**
	 * 貼り付け（ファイル）
	 * @return
	 */
	private boolean pasteFile(){
		
		String expath = (String)super.context.getSession().getAttribute("COPY_TARGET");
		expath = super.context.getServletContext().getRealPath("") + expath;
		
		File extarget = new File(expath);
		if(!extarget.exists()){
			addMessage("コピー元ファイルは既に削除されたようです。" + expath);
			return true;
		}
		
		List selectednodes = dirtree.getSelectedNodes(true);
		if(selectednodes.isEmpty()){
			addMessage("対象ディレクトリが分からなくなった。");
			return true;
		}
		
		TreeNode node = (TreeNode)selectednodes.get(0);
		
		String topath = super.context.getServletContext().getRealPath("");
		topath = topath + node.getId();
		
		File todir = new File(topath);
		if(!todir.exists()){
			addMessage("コピー先のディレクトリは既にありません " + topath);
			return true;
		}
		
		String exabspath = extarget.getAbsolutePath();
		exabspath = exabspath.substring(0, exabspath.lastIndexOf(ArUtil.SV_FILE_SEPARATOR));
		
		if(exabspath.equals(todir.getAbsolutePath())){
			addMessage("コピー先がコピー元ファイルと同じものです。");
			return true;
		}
		
		try {
			FileUtils.copyFileToDirectory(extarget, todir);
		} catch (IOException e) {
			throw new RuntimeException("ファイルコピーに失敗", e);
		}
		
		addMessage("ファイルがコピーされました。");
		return true;

	}
	
	/**
	 * 貼り付け（ディレクトリ）
	 * @return
	 */
	private boolean pasteDir(){
	
		String expath = (String)super.context.getSession().getAttribute("COPY_TARGET");
		expath = super.context.getServletContext().getRealPath("") + expath;
		
		File extarget = new File(expath);
		if(!extarget.exists()){
			addMessage("コピー元ディレクトリは既に削除されたようです。" + expath);
			return true;
		}
		
		List selectednodes = dirtree.getSelectedNodes(true);
		if(selectednodes.isEmpty()){
			addMessage("対象ディレクトリが分からなくなった。");
			return true;
		}
		
		TreeNode node = (TreeNode)selectednodes.get(0);
		
		String topath = super.context.getServletContext().getRealPath("");
		topath = topath + node.getId();
		
		File todir = new File(topath);
		if(!todir.exists()){
			addMessage("コピー先のディレクトリは既にありません " + topath);
			return true;
		}
		
		if(extarget.equals(todir)){
			addMessage("コピー先が同一ディレクトリです。");
			return true;
		}
		
		
		try {
			FileUtils.copyDirectoryToDirectory(extarget, todir);
		} catch (IOException e) {
			throw new RuntimeException("ディレクトリごとコピーに失敗", e);
		}
		
		addMessage("ディレクトリがコピーされました。");
		return true;

	}
	
	

	public boolean onClickFilrRename(){
		
		String selfile = targetfile.getValue();
		if(selfile.equals("")){
			addMessage("ファイルが選択されていません。");
			return true;
		}

		String filename = inputfilename.getValue();
		if(filename.equals("")){
			addMessage("ファイル名が入力されていませｎ。");
			return true;
		}
		
		//　ディレクトリが選択されていない場合があるので
		List selected = dirtree.getSelectedNodes(true);
		if(selected.isEmpty()){
			addMessage("ディレクトリがわからなくなった。");
			return true;
		}
		
		
		//　変更する実ディレクトリパスを編集
		TreeNode node = (TreeNode)selected.get(0);
		String abspath = super.context.getServletContext().getRealPath("");
		String renamepath = abspath + "/" + node.getId() + "/";

		//　既にある名前なら駄目
		if(new File(renamepath + filename).exists()){
			addMessage("既に存在するディレクトリまたはファイル名です。");
			return true;
		}
		
		File file = new File(renamepath + selfile); 
		file.renameTo(new File(renamepath + filename));
		
		addMessage(node.getId() + "/" + selfile +" を " + node.getId() + "/" + filename + " に変更しました。");
		
		return true;
		
	}
	

	public boolean onClickFileMake(){
		
		String filename = inputfilename.getValue();
		if(filename.equals("")){
			addMessage("ファイル名が入力されていませｎ。");
			return true;
		}
		
		//　ディレクトリが選択されていない場合があるので
		List selected = dirtree.getSelectedNodes(true);
		if(selected.isEmpty()){
			addMessage("ディレクトリがわからなくなった。");
			return true;
		}
		
		
		//　変更する実ディレクトリパスを編集
		TreeNode node = (TreeNode)selected.get(0);
		String abspath = super.context.getServletContext().getRealPath("");
		String renamepath = abspath + "/" + node.getId() + "/";

		//　既にある名前なら駄目
		if(new File(renamepath + filename).exists()){
			addMessage("既に存在するディレクトリまたはファイル名です。");
			return true;
		}
		
		File file = new File(renamepath + filename); 
		try {
			FileUtils.writeStringToFile(file, "");
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("ファイル新規作成に失敗",e);
		}
		
		addMessage(node.getId() + "/" + filename + " を作成しました。");

		return true;
		
	}



}
