package jp.arcanum.click.plugin;

import java.util.Map;

import jp.arcanum.click.PluginInterface;
import net.sf.click.Page;

public class LineAdsenseControl implements PluginInterface{

	
	public void onDestroy(Map params, Page page) {
		
	}

	public void onGet(Map params, Page page) {
		
	}

	private String adsense = "";
	
	public void onInit(Map params, Page page) {
		
		page.addModel("adsense", this);
		
	}

	public void onPost(Map params, Page page) {
		
	}


	public void onRender(Map params, Page page) {

		
		/**
		 * ここは、構成ファイルなんかから取得するようにする。
		 * もしくはplugn.xmlのパラメータ？？
		 * 下記は自分のAdsense。
		 * 
		 */
//		adsense = 
//			"<script type=\"text/javascript\"><!--" +
//			"google_ad_client = \"pub-5031109481190932\";"+
//			"google_ad_width = 468;"+
//			"google_ad_height = 15;"+
//			"google_ad_format = \"468x15_0ads_al\";"+
//			"google_ad_channel = \"\";"+
//			"google_color_border = \"e2e8fc\";"+
//			"google_color_bg = \"e2e8fc\";"+
//			"google_color_link = \"000066\";"+
//			"google_color_text = \"000000\";"+
//			"google_color_url = \"008000\";"+
//			"-->"+
//			"</script>"+
//			"<script type=\"text/javascript\""+
//			"  src=\"http://pagead2.googlesyndication.com/pagead/show_ads.js\">"+
//			"</script>";
		
	}

	public boolean onSecurityCheck(Map params, Page page) {
		return true;
	}
	
	
	public String toString(){
		return adsense;
	}
	
}
