package jp.arcanum.click.pages;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import jp.arcanum.click.ArUtil;
import jp.arcanum.click.PageProperties;
import jp.arcanum.click.PluginInterface;
import jp.arcanum.click.UserInfo;
import net.sf.click.Page;
import net.sf.click.control.Form;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


public class TemplatePage extends Page {
	
	/**
	 * プラグインリスト
	 */
	private List plugins = new ArrayList();
	
	/**
	 * ヘッダのタイトル
	 */
	public String headertitle = "";
	
	/**
	 * ヘッダーインポート
	 * VTL上で表現するのがめんどくさいし、HTMLのスタイル
	 * になんら関係ないんでここで文字列化する。
	 */
	public List headerimports = new ArrayList(){
		
		public String toString(){
			String ret = "";
			for(int i = 0 ; i < size(); i++){
				ret = ret + get(i);
				
			}
			return ret;
		}
	};

	/**
	 * サイトのタイトル
	 */
	public String sitetitle = "";
	
	/**
	 * メール
	 */
	public String mailto = "";
	
	/**
	 * FORM
	 */
	public Form form = new Form();
	
	
	/**
	 * コンストラクタ
	 *
	 */
	public TemplatePage() {
		
		
	}
	
	/**
	 * セキュリティチェック
	 */
	public boolean onSecurityCheck(){

		
		//　表示しようとするページのプロパティを取得
		String abspath = super.context.getServletContext().getRealPath(this.getPath());
		abspath = abspath.substring(0,abspath.indexOf(ArUtil.SV_FILE_SEPARATOR + "index.htm"));
		PageProperties prop = ArUtil.getPageProperties(abspath);
		if(!prop.isPublic()){

			//　公開しないページでもログインしていれば見る事は可能
			UserInfo user = (UserInfo)super.context.getSession().getAttribute(ArUtil.USER);
			if(user == null){
				super.setRedirect("/click/not-found.htm");
				return false;
				
			}
		}

			
		//　プラグインに委譲
		for(int i = 0 ; i < plugins.size(); i++){
			Plugin plugin = (Plugin)plugins.get(i);
			PluginInterface inter = plugin.plugin;
			plugin.issecurityok = inter.onSecurityCheck(plugin.parameters, this);
		}
		
		return true;
	}
	

	/**
	 * 初期処理
	 */
	public void onInit(){

		this.setPlugins();
		
		for(int i = 0 ; i < plugins.size(); i++){
			Plugin plugin = (Plugin)plugins.get(i);
			PluginInterface inter = plugin.plugin;
			//if(!plugin.issecurityok)continue;
			inter.onInit(plugin.parameters, this);
		}
		
		
	}
	
	/**
	 * HTTP-GET
	 */
	public void onGet(){
		super.onGet();
		
		//　プラグインに処理を委譲
		for(int i = 0 ; i < plugins.size(); i++){
			Plugin plugin = (Plugin)plugins.get(i);
			PluginInterface inter = plugin.plugin;
			if(!plugin.issecurityok)continue;
			inter.onGet(plugin.parameters, this);
		}

	}
	
	/**
	 * HTTP-POST
	 */
	public void onPost(){
		super.onPost();
		
		//　プラグインに処理を委譲
		for(int i = 0 ; i < plugins.size(); i++){
			Plugin plugin = (Plugin)plugins.get(i);
			PluginInterface inter = plugin.plugin;
			if(!plugin.issecurityok)continue;
			inter.onPost(plugin.parameters, this);
		}

	}
	
	
	
	/**
	 * 描画前処理
	 */
	public void onRender(){
		
		//　プラグインに委譲
		for(int i = 0 ; i < plugins.size(); i++){
			Plugin plugin = (Plugin)plugins.get(i);
			PluginInterface inter = plugin.plugin;
			if(!plugin.issecurityok)continue;
			inter.onRender(plugin.parameters, this);
		}
		
		//　サイト設定
		this.setSiteProperties();

	}
	
	/**
	 * ページ破棄前処理
	 */
	public void onDestroy(){
		
		super.onDestroy();
		
		for(int i = 0 ; i < plugins.size(); i++){
			Plugin plugin = (Plugin)plugins.get(i);
			PluginInterface inter = plugin.plugin;
			inter.onDestroy(plugin.parameters, this);
		}

		
	}
	


	/**
	 * サイトのプロパティを設定
	 *
	 */
	public void setSiteProperties(){
		
		//　ページトップの設定を取得しておく
		String pagetop = ArUtil.getProperty(this, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_DOCROOT);

		//　ヘッダータイトル
		this.headertitle = ArUtil.getProperty(this, ArUtil.PROP_SITE, "header.title");
		
		// サイトタイトル
		this.sitetitle = ArUtil.getProperty(this, ArUtil.PROP_SITE, "sitetitle.title");
		String titlelink = ArUtil.getProperty(this, ArUtil.PROP_SITE, "sitetitle.link");
		if(Boolean.valueOf(titlelink).booleanValue()){
			this.sitetitle = "<a href=\"" + ArUtil.APPNAME + pagetop + "\">" + "<font color=white class=titlebar >"  + this.sitetitle + "</font></a>";
		}
		
		//　メールTO
		this.mailto = ArUtil.getProperty(this, ArUtil.PROP_SITE, "mail.address");
		String maillink = ArUtil.getProperty(this, ArUtil.PROP_SITE, "mail.link");
		String mailtext = ArUtil.getProperty(this, ArUtil.PROP_SITE, "mail.title");
		if(Boolean.valueOf(maillink).booleanValue() && !mailtext.equals("") ){
			
			this.mailto = "<a href=\"mailto:" + this.mailto + "\">" + "<font color=white class=titlebar >"  + mailtext + "</font></a>";
		}
		
		// TODO　/WEB-INF/conf/link.xmlなんかで設定できるといいかも
		headerimports.add("<link rel=\"stylesheet\" href=\"" + ArUtil.APPNAME + "/default.css\" type=\"text/css\" />");

	}
	
	
	/**
	 * テンプレートファイル取得
	 */
	public String getTemplate(){
		//String ret = ArUtil.APPNAME +  ArUtil.getProperty(this, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_TEMPLATE);
		String ret = ArUtil.getProperty(this, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_TEMPLATE);
		return ret;
		//return "/indextemplate.htm";
	}
	 
	
	/**
	 * プラグインを読み込む
	 *
	 */
	private void setPlugins(){

		String xmlfile = super.context.getServletContext().getRealPath("");
		xmlfile = xmlfile + "/WEB-INF/plugin.xml";
		
		if(!new File(xmlfile).exists()){
			return;
		}
		
		try {
			//　XMLを読み込んで、ルートエレメント<page>を取得
			DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = dbfactory.newDocumentBuilder();
			Document doc = builder.parse("file:" + xmlfile);
			Element root = doc.getDocumentElement();
			
			//  <plugin>の要素で<class>を取得
			NodeList classlist = root.getElementsByTagName("class");
			for(int i = 0 ; i < classlist.getLength(); i++){
				Element classelem = (Element)classlist.item(i);
				
				String classname = classelem.getAttribute("classname");

				Class clazz = Class.forName(classname);
				PluginInterface plugininstance = (PluginInterface)clazz.newInstance();
				
				Map params = new HashMap();
				
				// <parameter>タグを取得
				NodeList paramlist = classelem.getElementsByTagName("parameter");
				if(paramlist.getLength()!=0){
					
					for(int j = 0 ; j < paramlist.getLength(); j++){
						
						//<param-name>
						Element nameelem = (Element)paramlist.item(0);
						String name = nameelem.getFirstChild().getNodeValue();
						//<param-value>
						Element valueelem =(Element)paramlist.item(1);
						String value = valueelem.getFirstChild().getNodeValue();
						
						params.put(name, value);
						
					}

				}
					
				
				Plugin plugin = new Plugin();
				plugin.plugin = plugininstance;
				plugin.parameters = params;
				plugins.add(plugin);
				
			}

			
			
		} 
		catch (Exception e) {
			throw new RuntimeException("plugin.xmlの解析に失敗　　" + xmlfile, e);
		}
		
	}

	
	
	
	/**
	 * プラグインを表現する内部クラス
	 * @author shinya
	 *
	 */
	class Plugin{
		public PluginInterface plugin = null;
		public Map parameters = new HashMap();
		boolean issecurityok = false; 
	}
	
}
