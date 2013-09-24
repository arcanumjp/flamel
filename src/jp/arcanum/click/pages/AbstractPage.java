package jp.arcanum.click.pages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.click.Page;
import net.sf.click.control.Form;

/**
 * 抽象ページ<br>
 * このアプリケーション中を通じてのすべてのページ
 * が継承すべきクラス
 * @author shinya
 *
 */
public class AbstractPage extends Page {

	/**
	 * メッセージエリア
	 */
	private String _message = "";
	protected void addMessage(String message){
		_message = "\n" + message;
	}

	
	/**
	 * フォーム
	 */
	public Form form = new Form(){
	    public String getActionURL() {
	        HttpServletRequest request = getContext().getRequest();
	        HttpServletResponse response = getContext().getResponse();
	        
	        String ret = response.encodeURL(request.getRequestURI());
	        if(ret.startsWith("//")){
	        	ret = ret.substring(1);
	        }
	        
	        return ret;
	    }

		
	};
	
	
	public void onRender(){
		addModel("message", _message);
	}
	
	
}
