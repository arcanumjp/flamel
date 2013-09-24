package jp.arcanum.click.plugin;

import java.io.File;
import java.io.FilenameFilter;
import java.util.List;
import java.util.Map;

import jp.arcanum.click.ArUtil;
import jp.arcanum.click.PageProperties;
import jp.arcanum.click.PluginInterface;
import jp.arcanum.click.pages.controls.ArTreeNode;
import net.sf.click.Page;
import net.sf.click.extras.tree.Tree;
import net.sf.click.extras.tree.TreeNode;
import net.sf.click.util.HtmlStringBuffer;


/**
 * ディレクトリ内の記事をずらずら並べる機能。
 * Yahooのトップページのような物をイメージして
 * 作ったけど・・・ぜんぜん違った！
 * @author shinya
 *
 */
public class DirMenuControl implements PluginInterface{

	/** 
	 * ディレクトリツリー情報のうちルートノードをセッションに格納する際のキー 
	 */
	private static final String TREE_NODE_SESS_KEY = "TREE_NODE_SESS_KEY";

	
	/** ツリーノード */
	private Tree dirtree = new Tree("dirtree"){
		
		
	    protected void renderTreeNodeStart(HtmlStringBuffer buffer, TreeNode treeNode,
	            int indentation) {
	    	

	        buffer.append("<li><span");
	        StringBuffer sb = new StringBuffer();
	        sb.append(getExpandClass(treeNode));
	        buffer.appendAttribute("class", sb.toString());
	        if (isJavascriptEnabled()) {
	            //hook to insert javascript specific code
	            //javascriptHandler.getJavascriptRenderer().renderTreeNodeStart(buffer); // コメントアウト
	        }
	        buffer.appendAttribute("style", "display:block;");
	        buffer.closeTag();

	        //Render the node's expand/collapse functionality.
	        //This includes adding a css class for the current expand/collapse state.
	        //In the tree.css file, the css classes are mapped to icons by default.
	        if (treeNode.hasChildren()) {
	            //renderExpandAndCollapseAction(buffer, treeNode);  // コメントアウト
	            buffer.append("<span class=\"spacer\"></span>");    //　追加
	        } else {
	            buffer.append("<span class=\"spacer\"></span>");
	        }
	        buffer.append("\n");

	    
	    }

		
		
		
	    protected void renderIcon(HtmlStringBuffer buffer, TreeNode treeNode) {

	    	ArTreeNode node = (ArTreeNode)treeNode;
	    	
	    	//render the icon to display
	        buffer.elementStart("span");

	        if(node.isDesc()){
	        	//　アイコンは描画しない
	    	}
	        else{
		        buffer.appendAttribute("class", getIconClass(treeNode));
	        }
	    	

	        buffer.append(">");
	    }		
		
		
	    /**
	     * ノードの表示文字列を編集する。<br>
	     * ノードのID属性に対応するリンクを作成する
	     */
		protected void renderValue(HtmlStringBuffer buffer, TreeNode treeNode) {

			ArTreeNode node = (ArTreeNode)treeNode;
			
	    	// TODO　設定ファイルにより、非表示、表示だけ、リンクありを表現
	    	

			//　TODO ↓はとりあえずのコード、FireFoxは/の変わりに\が付いてくるので
			String id = node.getId();
			if(id.startsWith("\\")){
				id = "/" + id.substring(1);
			}
			
			String title = (String)node.getValue();
			if(title.length()>10){
				title = title.substring(0,10) + "...";
			}
			
			buffer.append("<small>");
			
	    	if(node.isDesc()){
	    		buffer.append("<label title=\"" + node.getValue() + "\">");
		        buffer.append(title);
		        buffer.append("</label>");
	    	}
	    	else{
		    	buffer.elementStart("a");
		        buffer.append(" href=\"");
		        buffer.append(id);
		        buffer.append("\"");
		        buffer.append("title=\"");
		        buffer.append(node.getValue());
		        buffer.append("\"");
		        
		        
		        buffer.closeTag();
		        if (node.getValue() != null) {
		            buffer.append(title);
		        }
		        buffer.elementEnd("a");
	    	}
	    	
			buffer.append("</small>");
	    	
	        buffer.append("\n");

	        
	    }

	};

	
	
	
	
	public void onDestroy(Map params, Page page) {
		
	}

	public void onGet(Map params, Page page) {
		
	}

	public void onInit(Map params, Page page) {
		
		page.addControl(dirtree);
		
		
		//　メニューツリーを設定
		this.buildTree(page);
		
	}

	public void onPost(Map params, Page page) {
		
	}

	public void onRender(Map params, Page page) {

		
	}

	public boolean onSecurityCheck(Map params, Page page) {
		return true;
	}

	
	
	
	
	public String toString(){
		return this.dirtree.toString();
	}
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * メニューを構築
	 *
	 */
	private void buildTree(Page page){

		//　更新ファイル側の時間を取得　・・・①
        String lastupdatedtime = "";
        String lastpath = page.getContext().getServletContext().getRealPath("/");
        lastpath = lastpath + ArUtil.DIR_CONF + "lastupdated.txt";
        List lastlines = ArUtil.readFile(lastpath);
        if(lastlines.size()!=0){
        	lastupdatedtime = (String)lastlines.get(0);
        }

        //　セッションに保存した更新ファイルの時間を取得　・・・②
        String sesslastupdated = 
        	(String)page.getContext().getSession().getAttribute("LASTUPDATED");
        
        //　①と②が異なったら強制構築
        boolean forcebuild = true;
        //if(!lastupdatedtime.equals(sesslastupdated)){
        	forcebuild = false;
        //}
        
		//　セッションにルートノードがあったら、それを設定
        TreeNode existingRootNode = 
        	(TreeNode)page.getContext().getSession().getAttribute(TREE_NODE_SESS_KEY);
        if( forcebuild 	&& existingRootNode != null ){

			dirtree.setRootNode(existingRootNode);
	        //dirtree.setRootNodeDisplayed(true);
            return;
        	
        }
		
        //　ルートパスを取得
        String docroot = ArUtil.getProperty(page, ArUtil.PROP_SITE, "document_root");
        String rootpath = page.getContext().getServletContext().getRealPath(docroot);
        String absrootpath = rootpath.substring(0,rootpath.length() - docroot.length());
        
        //　ページの情報を取得
        PageProperties prop = ArUtil.getPageProperties(rootpath);

        //　ルートノードの作成と値の設定
        ArTreeNode rootnode = new ArTreeNode(prop.getTitle(), ArUtil.APPNAME + docroot);
        rootnode.setIsVisible(prop.isVisible());
        rootnode.setIsLink(prop.isIslink());
        rootnode.setIsRenderChild(prop.isRenderChild());
		
		//　DESCがある場合
		List descs = prop.getDescs();
		if(!descs.isEmpty()){
			for(int j = 0 ; j < descs.size(); j++){
				String desc = (String)descs.get(j);
				ArTreeNode descchild = new ArTreeNode(desc,rootnode.generateId(),rootnode,false);
				descchild.setIsDesc(true);
				
			}
			
		}

		//　ルートノードの設定
        dirtree.setRootNode(rootnode);
    	rootnode.setIsRenderChild(prop.isPublic());
        
        //再帰的にディレクトリ構造をセットする
        this.setTree(page, rootnode, rootpath, absrootpath);
        dirtree.expandAll();
        dirtree.setRootNodeDisplayed(prop.isVisible());
    	
        
        page.getContext().getSession().setAttribute("LASTUPDATED", lastupdatedtime);
        page.getContext().getSession().setAttribute(TREE_NODE_SESS_KEY, rootnode);

	
	}
	
	private Page _page;
	
	/**
	 * 再帰的にドキュメントルートから検索を行い、ディレクトリツリーに表示
	 * @param node
	 * @param relativepath
	 * @param rootpath
	 */
	private void setTree(Page page, ArTreeNode node, String relativepath, String rootpath){
		
		_page = page;
		
		//　ディレクトリのみのディレクトリ名リストを取得
		// （[00][pageclass][event]などのパスを含まないディレクトリ名）
		File file = new File(relativepath);
		String[] _files = file.list(
				new FilenameFilter(){
					public boolean accept(File dir, String name) {

						String contextpath = _page.getContext().getServletContext().getRealPath("");
						File confile = new File(contextpath);
						if(dir.getAbsolutePath().equals(confile.getAbsolutePath())){
							
							if(name.equals(".settings")){
								return false;
							}
							if(name.equals("work")){
								return false;
							}
							if(name.equals("WEB-INF")){
								return false;
							}
							if(name.equals("click")){
								return false;
							}
							if(name.equals("tools")){
								return false;
							}
						
						}
						
						
						File f = new File(dir.getAbsolutePath() + "/" + name);
						if(f.exists() && f.isDirectory()){
							return true;
						}
						return false;
					}
				}
				
		);
		
		//　ページプロパティを取得
		PageProperties dirprop = ArUtil.getPageProperties(relativepath);
		
		//　ページプロパティにしたがって並び替えを行う
		String[] files = new String[_files.length];
		int filecnt = 0;
		List childorder = dirprop.getChildorder();
		for(int i = 0 ; i < childorder.size(); i++){
			String childname = (String)childorder.get(i);
			boolean endflg=false;
			for(int j = 0 ; j < _files.length; j++){
				String filename = _files[j];
				//filename = filename.substring(filename.lastIndexOf(ArUtil.SV_FILE_SEPARATOR));
				if(childname.equals(filename)){
					files[filecnt] = _files[j];
					filecnt++;
					_files[j] = null;
				}
				
			}
			
		}
		
		//　ページプロパティに記述されていないものを最後に追加
		for(int i = 0 ; i < _files.length; i++){
			if(_files[i] !=null){
				files[filecnt]= _files[i];
				filecnt++;
			}
		}
		
		
		//　ノードを作成
		for(int i = 0 ; i < files.length; i++){
			String abspath = "";
			if(relativepath.endsWith(ArUtil.SV_FILE_SEPARATOR)){
				abspath = relativepath + files[i];
			}
			else{
				abspath = relativepath + "/" + files[i];
			}
			
			File wkfile = new File(abspath);
			PageProperties prop = ArUtil.getPageProperties( abspath);
			
			if(prop.isVisible()){
				String title = prop.getTitle();
				
				//　このフォルダをノード化する
				String id =  abspath.substring(rootpath.length());
				if(id.startsWith("\\")){
					id = "/" + id.substring(1);
				}

				ArTreeNode child = new ArTreeNode(title, ArUtil.APPNAME + id,node);
				child.setIsVisible(prop.isVisible());
				child.setIsLink(prop.isIslink());
				child.setIsRenderChild(prop.isRenderChild());
				
				//　DESCがある場合
				List descs = prop.getDescs();
				if(!descs.isEmpty()){
					for(int j = 0 ; j < descs.size(); j++){
						String desc = (String)descs.get(j);
						ArTreeNode descchild = new ArTreeNode(desc,child.generateId(),child,false);
						descchild.setIsDesc(true);
						
					}
					
				}
				
				if(child.isRenderChild()){
					setTree(page, child, abspath, rootpath);
				}
					
				
			}

		}
		
	}


	
	
	
}
