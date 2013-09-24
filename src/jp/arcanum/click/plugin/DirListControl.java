package jp.arcanum.click.plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jp.arcanum.click.ArUtil;
import jp.arcanum.click.PageProperties;
import jp.arcanum.click.PluginInterface;
import net.sf.click.Page;
import net.sf.click.util.HtmlStringBuffer;

/**
 * パンくずリストを表示する
 * @author shinya
 *
 */
public class DirListControl implements PluginInterface{

	public void onDestroy(Map params, Page page) {
		
	}

	public void onGet(Map params, Page page) {
		
	}

	public void onInit(Map params, Page page) {
		page.addModel("linklist", linklist);
		setLink(page);
	}

	public void onPost(Map params, Page page) {
	}

	public void onRender(Map params, Page page) {
		
	}

	public boolean onSecurityCheck(Map params, Page page) {
		return true;
	}
	
	
	
	
	
	private List linklist = new ArrayList();

	
	private void setLink(Page page){
		
		if(page.getPath().equals("/click/not-found.htm")){
			return;
		}
		
		
		//　現在のパスとルートパスを取得
		List filelist   = new ArrayList();
		String curpath  = page.getContext().getServletContext().getRealPath(page.getPath());
		String rootpath = page.getContext().getServletContext().getRealPath("");
		//String rootpath = ArUtil.getProperty(this, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_DOCROOT);
		
		//　ルートパスまでのパスのファイルリストを取得
		while(true){
			boolean endflg = false;
			
			File file = new File(curpath);
			curpath = file.getParent();
			if(curpath.equals(rootpath)){
				endflg = true;
			}
			else{
				filelist.add(new File(curpath + "/page.xml"));
			}
			
			if(endflg){
				break;
			}
				
		}
		
		//　リンクの作成
		String href="";
		for(int i = 0 ; i < filelist.size(); i++){

			File file = (File)filelist.get(i);
			String pagepath = file.getAbsolutePath();
			pagepath = pagepath.substring(0,pagepath.indexOf(ArUtil.SV_FILE_SEPARATOR + "page.xml"));
			PageProperties prop = ArUtil.getPageProperties(pagepath);

			String astring = prop.getTitle();
			if(!href.equals("")){
				HtmlStringBuffer atag = new HtmlStringBuffer();
				atag.elementStart("a");
				atag.appendAttribute("href", href);
				atag.closeTag();
				atag.append(astring);
				atag.elementEnd("a");
				astring = atag.toString();
			}
			this.linklist.add(0,astring);

			href = href + "../";
			
		}
	
	}

	
	
}
