package jp.arcanum.click.plugin;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import au.id.jericho.lib.html.Source;

import jp.arcanum.click.ArUtil;
import jp.arcanum.click.PluginInterface;
import net.sf.click.Page;

public class CounterControl implements PluginInterface{

	
	/**
	 * カウンター数
	 */
	private String counter = "";

	
	
	public void onDestroy(Map params, Page page) {
		
	}

	public void onGet(Map params, Page page) {
		
	}

	public void onInit(Map params, Page page) {
		page.addModel("counter", this);
	}

	public void onPost(Map params, Page page) {
	}

	public void onRender(Map params, Page page) {
		addCounter(page);
	}

	public boolean onSecurityCheck(Map params, Page page) {
		return true;
	}
	
	
	public String toString(){
		return counter;
	}
	
	
	
	
	private void addCounter(Page page){
		
    	
        //　カウンターファイルから内容読み込み
    	String filepath = page.getContext().getServletContext().getRealPath(ArUtil.PATH_COUNTER);
    	File file = new File(filepath);
    	if(!file.exists()){
    		return;
    	}
    	List list = ArUtil.readFile(filepath);
    	String wk = (String)list.get(0);

		//トップディレクトリの場合で検索ボタン処理ではない場合のみカウンターをアップ
        //String urltop = ArUtil.getProperty(this, ArUtil.PROP_SITE, "document_root");
        //if(urltop.equals("/")){
        //	urltop="";	//  アプリケーショントップの場合は//index.htmになるため削除
        //}
		
		// TODO このパスの条件は、プラグイン化されたとき、業務要件となるので
		//　このままにする。cmsとして汎用化できない部分。
        //if( ("/pages/index.htm").equals(page.getPath()) && ! this.kensaku.isClicked()){
        if( ("/pages/index.htm").equals(page.getPath()) ){
			
			//　セッションにカウンタアップフラグがある場合はスデにアップされている
			if(page.getContext().getSession().getAttribute(COUNTER_FLG)==null){
				wk = Integer.toString(Integer.parseInt(wk) + 1);
				List wklist = new ArrayList();
				wklist.add(wk);
		    	ArUtil.writeFile(filepath, wklist);
			}
			
		}
        
        wk = "00000" + wk;
        this.counter = wk.substring(wk.length()-5,wk.length()) ;

		page.getContext().getSession().setAttribute(COUNTER_FLG, COUNTER_FLG);
		
	}

	/**
	 * カウンタアップフラグ。コレがセッションにあるとカウンターは
	 * 既にアップされた事になるらしい。
	 */
	public static final String COUNTER_FLG = "COUNTER_FLG";

}
