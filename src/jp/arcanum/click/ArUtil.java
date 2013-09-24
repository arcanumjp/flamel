package jp.arcanum.click;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.servlet.ServletContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import net.sf.click.Page;
import net.sf.click.extras.tree.TreeNode;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ArUtil {

	
	public static final String WEB_INF  		= "WEB-INF/";
	public static final String DIR_CONF 		= WEB_INF 	+ "conf/";
	public static final String DIR_WORK 		= DIR_CONF 	+ "work/";
	public static final String PATH_COUNTER 	= WEB_INF 	+ "counter.txt";
	public static final String PATH_UPDATE  	= WEB_INF 	+ DIR_WORK + "update.txt";
	public static final String PROP_SITE  	= DIR_CONF 	+ "site";
	public static final String PROP_ADMIN 	= DIR_CONF 	+ "admin";
	
	/**
	 * ログインユーザをセッションに保存するときのキー
	 */
	public static final String USER = "USER";
	
	
	/**
	 * Servlet#onGet,onPostにて設定されるよ
	 */
	public static String APPNAME = "";
	
	/**
	 * サーバのシステムプロパティ
	 */
	public static final Properties SV_SYSPROPS = System.getProperties();
	
	/**
	 * サーバのファイルセパレータ
	 */
	public static final String SV_FILE_SEPARATOR = SV_SYSPROPS.getProperty("file.separator");
	
	/**
	 * サーバのＯＳ
	 */
	public static final String SV_OS_TYPE = SV_SYSPROPS.getProperty("os.name");
	
	public static final String KEY_ADMIN_CONF_NAME = "admin.name";
	public static final String KEY_ADMIN_CONF_PASS = "admin.password";
	
	public static final String KEY_SITECONF_HEADER = "header.title";
	public static final String KEY_SITECONF_TITLE     = "sitetitle.title";
	public static final String KEY_SITECONF_TITLELINK = "sitetitle.link";
	public static final String KEY_SITECONF_MAILTO = "mail.address";
	public static final String KEY_SITECONF_MAILLINK = "mail.link";
	public static final String KEY_SITECONF_MAILTITLE = "mail.title";
	
	
	public static final String KEY_SITECONF_PORTNO = "port";
	public static final String KEY_SITECONF_DOCROOT = "document_root";
	public static final String KEY_SITECONF_TEMPLATE = "template_thm";
	public static final String KEY_SITECONF_SRCH_ERR_NOCOND = "search.error.nocondition";
	public static final String KEY_SITECONF_SRCH_ERR_NORES = "search.error.noresult";
	public static final String KEY_SITECONF_SRCH_RES_START_TAG = "search.result.starttag";
	public static final String KEY_SITECONF_SRCH_RES_END_TAG = "search.result.endtag";
	public static final String KEY_SITECONF_SRCH_RES_SHOWLONGTH = "search.result.showlength";
	public static final String KEY_SITECONF_SRCH_RES_EMPH_START = "search.result.emphasis.starttag";
	public static final String KEY_SITECONF_SRCH_RES_EMPH_END = "search.result.emphasis.endtag";
	
	
	public static void setProperty(Page page, String path, String key, String value){
		setProperty(page.getContext().getServletContext(), path, key, value);
	}

	public static void setProperty(ServletContext con , String path, String key, String value){
		
		
		//　プロパティファイルの読み込み
        String abspath = con.getRealPath("/");
        List lines = ArUtil.readFile(abspath + path + ".properties");
        
        
        
        List writelines = new ArrayList();
        boolean exist = false;
        for(int i = 0 ; i < lines.size(); i++){
        	
        	String line = (String)lines.get(i);
        	
        	if(!line.startsWith("#")){

        		StringTokenizer tokens = new StringTokenizer(line, "=");
        		
        		//  aaa=bbbまたはaa=に適合する場合
        		if(tokens.countTokens()>=1){
        			
        			if(tokens.nextToken().equals(key)){
            			line = key + "=" + value;
            			exist = true;
        			}
 
        		}
        		
        	}
        	
        	writelines.add(line + "\r\n");
        	
        }
        if(!exist){
        	throw new RuntimeException("[" + path + "] に [" + key + "]がありません。");
        }
		
        //　結果をファイルに書き出す
        ArUtil.writeFile(abspath + path + ".properties", writelines);
        
        
	}
	
	
	/**
	 * プロパティ取得
	 * @param page
	 * @param path
	 * @param key
	 * @return
	 */
	public static String getProperty(Page page, String path, String key){
		return getProperty(page.getContext().getServletContext(), path, key);
	}
	
	
	
	
	/**
	 * 
	 * @param con
	 * @param path
	 * @param key
	 * @return
	 */
    public static String getProperty(ServletContext con, String path, String key){
        
    	// 補足：
    	//　　　Propertiesを使っていたけど、日本語が使えないのでやめる
    	//      native2asciiいつも使うのは面倒だし・・・
    	//      　→国際化は考えていない
    	
        String ret = "";
        
//        Properties prop = new Properties();
//        try {
//            prop.load(con.getResourceAsStream(path + ".properties"));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        
//        ret = prop.getProperty(key);

        
        String abspath = con.getRealPath("/");
        
        
        //System.out.println("----------------------------->" + abspath + path + ".properties");

        
        
        List lines = ArUtil.readFile(abspath + path + ".properties");
        boolean exist = false;
        for(int i = 0 ; i < lines.size() ; i++){
        	String line = (String)lines.get(i);
        	
        	StringTokenizer tokens = new StringTokenizer(line, "=");
        	if(tokens.countTokens()<1){
        		continue;
        	}
        	
        	String propkey = tokens.nextToken();
        	if(key.equals(propkey)){
        		ret = line.substring(line.indexOf("=")+1);
        		exist = true;
        	}
        	
        }
        if(!exist){
        	throw new RuntimeException("[" + path + "] に [" + key + "]がありません。");
        }
        
        
        return ret;
        
    }
    

    
	/**
	 * ファイル読み込み
	 * @param context
	 * @param path
	 * @return
	 */
	public static List readFile(ServletContext context, String path){
		
		String fullpath = context.getRealPath(path);
		return readFile(fullpath);
		
	}
	
	/**
	 * ファイル読み込み
	 * @param path
	 * @return
	 */
	public static List readFile(String path){
		return readFile(new File(path));
	}
	
	/**
	 * ファイル読み込み
	 * @param file
	 * @return
	 */
    public static List readFile(File file){
        
        //　戻り値の初期化
        List ret = new ArrayList();
        if(!file.exists()){
        	System.out.println("ファイルなし！");
        	return ret;
        }
        
        if(file.getAbsolutePath().indexOf(".."+ArUtil.SV_FILE_SEPARATOR) !=-1){
        	throw new RuntimeException("入力されたファイルパスが不正");
        }
        
        
        FileInputStream fis = null;
        InputStreamReader ir = null;
        BufferedReader br = null;
        try {
            fis = new FileInputStream(file);
            ir  = new InputStreamReader(fis);
            br  = new BufferedReader(ir);
            
            //　ファイルの全行を読み込み、戻り値に追加
            ret = new ArrayList();
            while(br.ready()){
                String line = br.readLine();
                ret.add(line);
            }
            
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        finally{
            try {
                br.close();
                ir.close();
                fis.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
              
        }
        
        //　戻り値を返し、処理を終了する
        return ret;
        
    }

	
    public static final void writeFile(String path, List list){
    	writeFile(new File(path), list);
    }
    
    
    public static final void writeFile(File file, List list){
    	
    	FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        BufferedWriter bw = null;
	    try {
			fos = new FileOutputStream(file);
			osw = new OutputStreamWriter(fos );
			bw = new BufferedWriter(osw);
	        
			for(int i = 0 ; i < list.size(); i++){
				bw.write((String)list.get(i));
	        }
	          
        } catch (Exception e) {
			new RuntimeException("ファイル出力に失敗 " + file);
        }
        finally{
	        try {
				bw.close();
				osw.close();
				fos.close();
			} catch (IOException e) {
				new RuntimeException("ファイルクローズに失敗 " + file);
			}
        	
        }
    	
    }
    
	/**
	 * クロススプリクティング対策用メソッド
	 * @param orgstr
	 * @return
	 */
	public static  String changeString( String orgstr ){

		char charletter ;
		StringBuffer buff = new StringBuffer( orgstr.length() ) ;
		for( int i = 0 ; i < orgstr.length() ; i++ ){
			charletter = orgstr.charAt( i ) ;
			if( charletter == '<' ){
				buff.append( "&lt" ) ;
			}
			else if( charletter == '>' ){
				buff.append( "&gt" ) ;
			}
			else if( charletter == '&' ){
				buff.append( "&amp" ) ;
			}
			else if( charletter == '"' ){
				buff.append( "&quot" ) ;
			}
			else{
				buff.append( charletter ) ;
			}

		}
		return  buff.toString() ;

	}
	
	
    //  unescape時のみ使用
    private static final String[] ESCAPE_CHARS = { "<", ">", "&", "\"" };
    private static final String[] ESCAPED_STRINGS = { "&lt;", "&gt;", "&amp;", "&quot;" };

	
    /**
     * HTMLの特殊文字（<,>,&,"）をエスケープ解除する。
     * @param s エスケープ解除する文字列。
     * @return エスケープ解除した文字列。
     */
    public static String unescape(String s) {
        StringBuffer sb = new StringBuffer(s);

        for (int i = 0; i < ESCAPED_STRINGS.length; i++) {
            int p;

            while ((p = sb.indexOf(ESCAPED_STRINGS[i])) != -1) {
                sb.replace(
                    p,
                    p + ESCAPED_STRINGS[i].length(),
                    ESCAPE_CHARS[i]);
            }
        }

        return sb.toString();
    }
	
	
	public static String getClientFileSeparator(Page page){
		String ret = "/";
		
		String agent = page.getContext().getRequest().getHeader("User-Agent");
		if(agent.indexOf("Windows")!=-1){
			ret = "\\";
		}
		
		return ret;
	}
	
	public static String getClientChangingLine(Page page){
		String ret = "\n";
		
		String agent = page.getContext().getRequest().getHeader("User-Agent");
		if(agent.indexOf("Windows")!=-1){
			ret = "\r\n";
		}
		
		return ret;
	}

	public static Map getUser(Page page, String userid){
		return getUser(page.getContext().getServletContext(), userid);
	}
	
	
	public static Map getUser(ServletContext con,  String userid){
		
		Map ret = null;
		
		List list = getUserList(con);
		for(int i = 0 ; i < list.size(); i++){
			Map user = (Map)list.get(i);
			String id = (String)user.get("id"); 
			if( id != null || id.equals(userid)){
				
				ret = user;
				
			}
			
		}
		
		return ret;
		
	}
	
	
	/**
	 * 
	 * @param userid
	 * @return Mapのリスト
	 */
	public static List getUserList(ServletContext con){
		
		List ret = new ArrayList();
		
		String xmlpath = con.getRealPath(ArUtil.DIR_CONF + "/user.xml");
		File xmlfile = new File(xmlpath);
		if(!xmlfile.exists()){
			return ret;
		}
		
		
		try {
			
			
			//　XMLを読み込んで、ルートエレメント<users>を取得
			DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = dbfactory.newDocumentBuilder();
			Document doc = builder.parse(xmlfile);
			Element root = doc.getDocumentElement();

		
			//  <users>の要素で<user>のリストを取得
			NodeList userlist = root.getElementsByTagName("user");
			for(int i = 0 ; i < userlist.getLength(); i++){
				Element usertag = (Element)userlist.item(i);
				
				NodeList idlist    = usertag.getElementsByTagName("id");
				Element idelem = (Element)idlist.item(0);
				String id = idelem.getFirstChild().getNodeValue();
				
				NodeList passlist  = usertag.getElementsByTagName("pass");
				Element passelem = (Element)passlist.item(0);
				String pass = passelem.getFirstChild().getNodeValue();
				
				String group ="";
				NodeList grouplist = usertag.getElementsByTagName("group");
				for(int j = 0 ; j < grouplist.getLength(); j++){
					Element groupelem = (Element)grouplist.item(j);
					group = group + groupelem.getFirstChild().getNodeValue() + ",";
					
				}
				
				Map user = new HashMap();
				user.put("id", id);
				user.put("pass", pass);
				user.put("group", group);
				
				ret.add(user);
				
			}
		
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return ret;
		
	}
	
	
	public static PageProperties getPageProperties(String path){
		
		PageProperties ret = new PageProperties();
		
		if(path.endsWith("/index.htm")){
			path = path.substring(0,path.length()-10);
		}
		
		
		try {

			String xmlpath = path;
			File xmlfile = new File(xmlpath + "/page.xml");
		
			if(xmlfile.getAbsolutePath().indexOf(".." + ArUtil.SV_FILE_SEPARATOR)!=-1){
				throw new RuntimeException("パスが不正です");
			}
			
			
			if(xmlfile.exists()){
				
				//　XMLを読み込んで、ルートエレメント<page>を取得
				DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = dbfactory.newDocumentBuilder();
				Document doc = builder.parse(xmlfile);
				Element root = doc.getDocumentElement();
				
				//　<page>の属性を設定
				String visible = root.getAttribute("visible");
				if(visible.equals("")){
					visible="false";
				}
				ret.setVisivle(Boolean.valueOf(visible).booleanValue());
				//　<page>の属性を設定
				String link = root.getAttribute("link");
				if(link.equals("")){
					link = "false";
				}	
				ret.setIslink(Boolean.valueOf(link).booleanValue());
				//　<page>の属性を設定
				String markup = root.getAttribute("markup");
				if(markup.equals("")){
					markup = PageProperties.MARKUP_HTML;
				}
				ret.setMarkup(markup);
				//　<page>の属性を設定
				String renderchild = root.getAttribute("renderchild");
				if(renderchild.equals("")){
					renderchild = "false";
				}
				//　<page>の属性を設定
				ret.setRenderChild(Boolean.valueOf(renderchild).booleanValue());
				String publication = root.getAttribute("public");
				if(publication.equals("")){
					publication = "false";
				}
				ret.setPublic(Boolean.valueOf(publication).booleanValue());
				
				//  <page>の要素で<title>を取得
				NodeList titlelist = root.getElementsByTagName("title");
				Element titleelem = (Element)titlelist.item(0);
				String title = titleelem.getFirstChild().getNodeValue();
				ret.setTitle(title);
				
				
				//  <page>の要素で<descs>を取得
				NodeList descslist = root.getElementsByTagName("desc");
				for(int i = 0 ; i < descslist.getLength(); i++){
					Element descselem = (Element)descslist.item(i);
					String descs = descselem.getFirstChild().getNodeValue();
					ret.getDescs().add(descs);
					
				}
				
				//  <page>の要素で<childorder>を取得
				NodeList childlist = root.getElementsByTagName("childorder");
				for(int i = 0 ; i < childlist.getLength(); i++){
					Element childelem = (Element)childlist.item(i);
					String child = childelem.getFirstChild().getNodeValue();
					ret.getChildorder().add(child);
					
				}
				
				
			}
			else{
				//　無い場合はデフォルト値
				ret.setMarkup("html");
				ret.setRenderChild(true);
				ret.setTitle(xmlpath.substring(xmlpath.lastIndexOf(ArUtil.SV_FILE_SEPARATOR)+1));
				if(ret.getTitle().indexOf("/")!=-1){
					ret.setTitle(xmlpath.substring(xmlpath.lastIndexOf("/")+1));
				}
				ret.setVisivle(true);
				ret.setPublic(false);
				
				
			}
			
			
		} catch (Exception e) {
			
			throw new RuntimeException(e);
		}
		

		
		return ret;
		
	}
	
//	public static void setPageProperties(Page owner, String path, PageProperties prop){
//		
//		// TODO DOMで更新する
//		
//		//　保存する内容の編集
//		List lines = new ArrayList();
//		lines.add("<?xml version=\"1.0\" encoding=\"Shift_JIS\" ?>" + "\n");
//		String page = "<page ";
//		page = page + "visible=\"" 		+ prop.isVisible() + "\" ";
//		page = page + "markup=\"" 		+ prop.getMarkup() + "\" ";
//		page = page + "renderchild=\"" 	+ prop.isRenderChild() + "\" ";
//		page = page + "public=\"" 		+ prop.isPublic() + "\" ";
//		page = page + ">";
//		lines.add(page + "\n");
//		
//		String title = "<title>" + prop.getTitle() + "</title>";
//		lines.add(title + "\n");
//		
//		String descs = prop.getDescs();
//		descs = descs.replaceAll("\r", "");
//		StringTokenizer tokens = new StringTokenizer(descs,"\n");
//		while(tokens.hasMoreTokens()){
//			String token = tokens.nextToken();
//			String desc = "<desc>" + token + "</desc>";
//			lines.add(desc + "\n");
//		}
//		lines.add("</page>");
//		
//		//　保存するファイルの特定
//        path = owner.getContext().getServletContext().getRealPath("") + path;
//        String xmlpath = path + "/page.xml";
//        
//        //　次のようなイメージで更新される
//		//<//page visible="true" link="true" markup="html" renderchild="true" >
//	    //	<title>トップページ</title>
//	    //	<desc>はじめに</desc>
//	    //	<desc>至極のクリックリンク集</desc>
//	    //	<desc>更新履歴</desc>
//	    //	<desc>その他</desc>
//		//</page>
//        ArUtil.writeFile(xmlpath, lines);
//
//	}
    
    
	
	
	public static String sanitize(String line){
		
		
//		//onclick
//		line = line.replaceAll("[o|O][n|N][c|C][l|L][i|I][c|C][k|K]", "on<!-- -->Click");
//		
//		//onBlur
//		line = line.replaceAll("[o|O][n|N][b|B][l|L][u|U][r|R]", "on<!-- -->Blur");
//		
//		//onFocus 
//		line = line.replaceAll("[o|O][n|N][f|F][o|O][c|C][u|U][s|S]", "on<!-- -->Focus");
//		
//		//onChange  
//		line = line.replaceAll("[o|O][n|N][c|C][h|H][a|A][n|N][g|G][e|E]", "on<!-- -->Change");
//		
//		//onSelect  
//		line = line.replaceAll("[o|O][n|N][s|S][e|E][l|L][e|E][c|C][t|T]", "on<!-- -->Select");
//		
//		//onSelectStart  
//		line = line.replaceAll("[o|O][n|N][s|S][e|E][l|L][e|E][c|C][t|T][s|S][t|T][a|A][r|R][t|T]", "on<!-- -->SelectStart");
//		
//		//onSubmit  
//		line = line.replaceAll("[o|O][n|N][s|S][u|U][b|B][m|M][i|I][t|T]", "on<!-- -->Submit");
//		
//		//onReset  
//		line = line.replaceAll("[o|O][n|N][r|R][e|E][s|S][e|E][t|T]", "on<!-- -->Reset");
//		
//		//onAbort  
//		line = line.replaceAll("[o|O][n|N][a|A][b|B][o|O][r|R][t|T]", "on<!-- -->Abort");
//		
//		//onError  
//		line = line.replaceAll("[o|O][n|N][e|E][r|R][r|R][o|O][r|R]", "on<!-- -->Error");
//		
//		//onLoad  
//		line = line.replaceAll("[o|O][n|N][l|L][o|O][a|A][d|D]", "on<!-- -->Load");
//		
//		//onUnload 
//		line = line.replaceAll("[o|O][n|N][u|U][n|N][l|L][o|O][a|A][d|D]", "on<!-- -->Unload");
//		
//		//onDblClick  
//		line = line.replaceAll("[o|O][n|N][d|D][b|B][l|L][c|C][l|L][i|I][c|C][k|K]", "on<!-- -->DblClick");
//		
//		//onKeyUp  
//		line = line.replaceAll("[o|O][n|N][k|K][e|E][y|Y][u|U][p|P]", "on<!-- -->KeyUp");
//		
//		//onKeyDown  
//		line = line.replaceAll("[o|O][n|N][k|K][e|E][y|Y][d|D][o|O][w|W][n|N]", "on<!-- -->KeyDown");
//		
//		//onKeyPress  
//		line = line.replaceAll("[o|O][n|N][k|K][e|E][y|Y][p|P][r|R][e|E][s|S][s|S]", "on<!-- -->KeyPress");
//		
//		//onMouseOut  
//		line = line.replaceAll("[o|O][n|N][m|M][o|O][u|U][s|S][e|E][o|O][u|U][t|T]", "on<!-- -->MouseOut");
//		
//		//onMouseOver  
//		line = line.replaceAll("[o|O][n|N][m|M][o|O][u|U][s|S][e|E][o|O][v|V][e|E][r|R]", "on<!-- -->MouseOver");
//		
//		//onMouseUp  
//		line = line.replaceAll("[o|O][n|N][m|M][o|O][u|U][s|S][e|E][u|U][p|P]", "on<!-- -->MouseUp");
//		
//		//onMouseDown  
//		line = line.replaceAll("[o|O][n|N][m|M][o|O][u|U][s|S][e|E][d|D][o|O][w|W][n|N]", "on<!-- -->MouseDown");
//		
//		//onMouseMove  
//		line = line.replaceAll("[o|O][n|N][m|M][o|O][u|U][s|S][e|E][m|M][o|O][v|V][e|E]", "on<!-- -->MouseMove");
//		
//		//onDragDrop 
//		line = line.replaceAll("[o|O][n|N][d|D][r|R][a|A][g|G][d|D][r|R][o|O][p|P]", "on<!-- -->DragDrop");
		
		
		return line;
		
	}
	
	
}
