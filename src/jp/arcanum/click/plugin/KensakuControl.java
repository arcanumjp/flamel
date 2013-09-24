package jp.arcanum.click.plugin;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import au.id.jericho.lib.html.Source;

import jp.arcanum.click.ArUtil;
import jp.arcanum.click.PluginInterface;
import net.sf.click.Page;
import net.sf.click.control.Form;
import net.sf.click.control.Submit;
import net.sf.click.control.TextField;

public class KensakuControl implements PluginInterface{

	/**
	 * 検索文字入力エリア
	 */
	private TextField inputfld = new TextField("inputfld");
	
	/**
	 * 検索ボタン
	 */
	private Submit kensaku = new Submit("kensaku","検索");

	/**
	 * 検索結果
	 */
	private String kensakukekka = "";

	
	public void onDestroy(Map params, Page page) {
		
	}

	public void onGet(Map params, Page page) {
		
	}

	public void onInit(Map params, Page page) {
		
		//検索コントロール関係
		//page.addModel("inputfld",inputfld);
		kensaku.setListener(this, "clickKensaku");
		//form.add(kensaku);
		
		Form form =  (Form)page.getModel().get("form");
		form.add(inputfld);
		form.add(kensaku);
		
		
		page.addModel("kensakukekka", this);
		
		_page = page;
		
	}

	public void onPost(Map params, Page page) {
	}

	public void onRender(Map params, Page page) {
	}

	public boolean onSecurityCheck(Map params, Page page) {
		return true;
	}
	
	
	private Page _page = null;
	
	
	/**
	 * 検索ボタン押下
	 * @return
	 */
	public boolean clickKensaku(){

		String starttag = ArUtil.getProperty(_page, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_SRCH_RES_START_TAG);
		String endtag   = ArUtil.getProperty(_page, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_SRCH_RES_END_TAG);
		
		//　検索文字列がない場合
		String cond = this.inputfld.getValue();
		if(cond.equals("")){
			
			String nocond = ArUtil.getProperty(_page, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_SRCH_ERR_NOCOND);

			this.kensakukekka = starttag + nocond + endtag;
			
			return true;
		
		}
		
		//　全ファイルを取得
		String doc_root = ArUtil.getProperty(_page, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_DOCROOT);
		String path = _page.getContext().getServletContext().getRealPath(doc_root);
		List list = getFiles(path);
		
		//　全ファイルをなめて、条件に一致するものについて処理
		for(int i = 0 ; i < list.size();i++){

			String fname = (String)list.get(i);

			//　ファイルを読み込んで改行付きの文字列にする
			List lines = ArUtil.readFile(fname);
			String alllines = "";
			for(int j = 0 ; j < lines.size(); j++){
				alllines = alllines + lines.get(j) + "\n";
			}
			
			//　Jerichoに解析させて文章だけ取得
			Source src = new Source(alllines);
			String htmldocs = src.getRenderer().toString();
			
			//　文章を改行で分けて配列化する
			lines = new ArrayList(); //　一旦初期化
			StringTokenizer tokens = new StringTokenizer(htmldocs, "\n");
			while(tokens.hasMoreTokens()){
				lines.add(tokens.nextToken());
			}
			
			
			//　ファイル内に検索文字列があるかチェック
	        List existlist = new ArrayList();
			for(int j = 0 ; j < lines.size(); j++){
				String line = (String)lines.get(j);
		        if(line.indexOf(cond) != -1){
		        	existlist.add(line);
		        }
				
			}
			//　ファイル内に検索文字列があった場合のみの処理
	        if(existlist.size() != 0){
	        	
	        	//　URLTOPの編集
	        	HttpServletRequest req = _page.getContext().getRequest();
	        	//String protocol     = req.getProtocol();  //  これで取得できるのは"HTTP"なので駄目
	        	String servername   = req.getServerName();
	        	String exactportno  = Integer.toString(req.getServerPort());
	        	String propportno   = ArUtil.getProperty(_page, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_PORTNO);
	        	String appname      = req.getContextPath();
	        	if(exactportno.equals("80")){
	        		exactportno = "";
	        	}
	        	else{
	        		exactportno = ":" + exactportno;
	        	}
	        	String urltop = "http://" + servername + exactportno + appname + "/";
	        	
	        	//  URLTOPに対応するドキュメントパスを取得
	        	String docpath = _page.getContext().getServletContext().getRealPath("/");
	        	
	        	//　検索されたファイルパスをURLベースのパスに変換
	        	fname = urltop + fname.substring( docpath.length() );
	        	
	        	//　￥を／にする(サーバがWindowsの場合の対策)
				StringBuffer buff = new StringBuffer() ;
				for( int j = 0 ; j < fname.length() ; j ++ ){
					String s = fname.substring( j , j + 1 ) ;
					if( s.equals( "\\" ) ){
						buff.append( "/" ) ;
					}
					else{
						buff.append( s ) ;
					}
				}
				fname = buff.toString() ;
				
				// <tr>始まり　↓↓↓ここから
	    		this.kensakukekka = this.kensakukekka +
	    		  starttag +
                  		"<a href=\"" + fname +  "\">" + fname  +"</a>"+  "<br><br>" ;
	    		
	    		for(int j=0;j<existlist.size();j++){
	    			
	    			this.kensakukekka = this.kensakukekka + "<i>ヒットした行 " + (j + 1) + "</i><br>"; 
	    			
	    			String line = (String)existlist.get(j);
	    			line = ArUtil.changeString(line);
	    			
	    			String showlength_ = ArUtil.getProperty(_page, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_SRCH_RES_SHOWLONGTH);
	    			int showlength = Integer.parseInt(showlength_);
	    			
	    			int st = line.indexOf(cond);
	    			if(st <showlength){
	    			}
	    			else{
	    				line = "・・・  " + line.substring(st-showlength);
	    			}
	    			
	    			int end = line.lastIndexOf(cond);
	    			if(line.substring(end).length() <showlength){
	    			}
	    			else{
	    				line = line.substring(0, end + showlength) + "・・・";
	    			}
	    			
	    			String empstart = ArUtil.getProperty(_page, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_SRCH_RES_EMPH_START);
	    			String empend   = ArUtil.getProperty(_page, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_SRCH_RES_EMPH_END);
	    			
	    			
	    			line = line.replaceAll(cond, empstart + cond + empend);
	    			
	    			this.kensakukekka  = this.kensakukekka + line + "<br>";
	    			
	    		}
	    		
	    		this.kensakukekka = this.kensakukekka +  endtag ;	
				// <tr>終わり　↑↑↑ここまで

	        }
		}
		if(this.kensakukekka.equals("")){
			
			String noresult = ArUtil.getProperty(_page, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_SRCH_ERR_NORES);
			
    		this.kensakukekka = starttag + noresult + endtag;
		}
			
		return true;

	}
	
	/**
	 * ファイル取得<br>
	 * 入力されたパス（ディレクトリ）以下のファイルのうち
	 * index.htmファイルをすべて取得し返す。
	 * @param path
	 * @return
	 */
	private List getFiles(String path){
		
		List ret = new ArrayList();
		
		File f = new File(path);
		
		if(f.isDirectory()){
			
			File[] files = f.listFiles();

			for(int i=0;i<files.length;i++){
				ret.addAll(getFiles(files[i].getAbsolutePath()));
			}
			
		}
		else{
			String filename = f.getName();
			if(filename.equalsIgnoreCase("index.htm")){
				ret.add(f.getAbsolutePath());
			}
			
		}
		
		return ret;
	}
	
	private List parseHtml(final String filepath){
		List ret = new ArrayList();
		
		String _filepath = filepath;
		if(filepath.indexOf(":")==-1){
			_filepath = "file:" + filepath;
		}
		
		Source src = null;
		try {
			src = new Source(new URL(filepath));
			
			
		} catch (Exception e) {
			throw new RuntimeException("HTMLの解析に失敗");
		}
		
		String html = src.getRenderer().toString();
		//html = html.replaceAll("\\r", "");
		StringTokenizer tokens = new StringTokenizer(html,"\n");
		while(tokens.hasMoreTokens()){
			ret.add(tokens.nextToken());
		}
		
		
		return ret;
	}

	public String toString(){
		return kensakukekka;
	}
	
	
}
