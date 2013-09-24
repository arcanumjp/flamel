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
	 * �����������̓G���A
	 */
	private TextField inputfld = new TextField("inputfld");
	
	/**
	 * �����{�^��
	 */
	private Submit kensaku = new Submit("kensaku","����");

	/**
	 * ��������
	 */
	private String kensakukekka = "";

	
	public void onDestroy(Map params, Page page) {
		
	}

	public void onGet(Map params, Page page) {
		
	}

	public void onInit(Map params, Page page) {
		
		//�����R���g���[���֌W
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
	 * �����{�^������
	 * @return
	 */
	public boolean clickKensaku(){

		String starttag = ArUtil.getProperty(_page, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_SRCH_RES_START_TAG);
		String endtag   = ArUtil.getProperty(_page, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_SRCH_RES_END_TAG);
		
		//�@���������񂪂Ȃ��ꍇ
		String cond = this.inputfld.getValue();
		if(cond.equals("")){
			
			String nocond = ArUtil.getProperty(_page, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_SRCH_ERR_NOCOND);

			this.kensakukekka = starttag + nocond + endtag;
			
			return true;
		
		}
		
		//�@�S�t�@�C�����擾
		String doc_root = ArUtil.getProperty(_page, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_DOCROOT);
		String path = _page.getContext().getServletContext().getRealPath(doc_root);
		List list = getFiles(path);
		
		//�@�S�t�@�C�����Ȃ߂āA�����Ɉ�v������̂ɂ��ď���
		for(int i = 0 ; i < list.size();i++){

			String fname = (String)list.get(i);

			//�@�t�@�C����ǂݍ���ŉ��s�t���̕�����ɂ���
			List lines = ArUtil.readFile(fname);
			String alllines = "";
			for(int j = 0 ; j < lines.size(); j++){
				alllines = alllines + lines.get(j) + "\n";
			}
			
			//�@Jericho�ɉ�͂����ĕ��͂����擾
			Source src = new Source(alllines);
			String htmldocs = src.getRenderer().toString();
			
			//�@���͂����s�ŕ����Ĕz�񉻂���
			lines = new ArrayList(); //�@��U������
			StringTokenizer tokens = new StringTokenizer(htmldocs, "\n");
			while(tokens.hasMoreTokens()){
				lines.add(tokens.nextToken());
			}
			
			
			//�@�t�@�C�����Ɍ��������񂪂��邩�`�F�b�N
	        List existlist = new ArrayList();
			for(int j = 0 ; j < lines.size(); j++){
				String line = (String)lines.get(j);
		        if(line.indexOf(cond) != -1){
		        	existlist.add(line);
		        }
				
			}
			//�@�t�@�C�����Ɍ��������񂪂������ꍇ�݂̂̏���
	        if(existlist.size() != 0){
	        	
	        	//�@URLTOP�̕ҏW
	        	HttpServletRequest req = _page.getContext().getRequest();
	        	//String protocol     = req.getProtocol();  //  ����Ŏ擾�ł���̂�"HTTP"�Ȃ̂őʖ�
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
	        	
	        	//  URLTOP�ɑΉ�����h�L�������g�p�X���擾
	        	String docpath = _page.getContext().getServletContext().getRealPath("/");
	        	
	        	//�@�������ꂽ�t�@�C���p�X��URL�x�[�X�̃p�X�ɕϊ�
	        	fname = urltop + fname.substring( docpath.length() );
	        	
	        	//�@�����^�ɂ���(�T�[�o��Windows�̏ꍇ�̑΍�)
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
				
				// <tr>�n�܂�@��������������
	    		this.kensakukekka = this.kensakukekka +
	    		  starttag +
                  		"<a href=\"" + fname +  "\">" + fname  +"</a>"+  "<br><br>" ;
	    		
	    		for(int j=0;j<existlist.size();j++){
	    			
	    			this.kensakukekka = this.kensakukekka + "<i>�q�b�g�����s " + (j + 1) + "</i><br>"; 
	    			
	    			String line = (String)existlist.get(j);
	    			line = ArUtil.changeString(line);
	    			
	    			String showlength_ = ArUtil.getProperty(_page, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_SRCH_RES_SHOWLONGTH);
	    			int showlength = Integer.parseInt(showlength_);
	    			
	    			int st = line.indexOf(cond);
	    			if(st <showlength){
	    			}
	    			else{
	    				line = "�E�E�E  " + line.substring(st-showlength);
	    			}
	    			
	    			int end = line.lastIndexOf(cond);
	    			if(line.substring(end).length() <showlength){
	    			}
	    			else{
	    				line = line.substring(0, end + showlength) + "�E�E�E";
	    			}
	    			
	    			String empstart = ArUtil.getProperty(_page, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_SRCH_RES_EMPH_START);
	    			String empend   = ArUtil.getProperty(_page, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_SRCH_RES_EMPH_END);
	    			
	    			
	    			line = line.replaceAll(cond, empstart + cond + empend);
	    			
	    			this.kensakukekka  = this.kensakukekka + line + "<br>";
	    			
	    		}
	    		
	    		this.kensakukekka = this.kensakukekka +  endtag ;	
				// <tr>�I���@�����������܂�

	        }
		}
		if(this.kensakukekka.equals("")){
			
			String noresult = ArUtil.getProperty(_page, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_SRCH_ERR_NORES);
			
    		this.kensakukekka = starttag + noresult + endtag;
		}
			
		return true;

	}
	
	/**
	 * �t�@�C���擾<br>
	 * ���͂��ꂽ�p�X�i�f�B���N�g���j�ȉ��̃t�@�C���̂���
	 * index.htm�t�@�C�������ׂĎ擾���Ԃ��B
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
			throw new RuntimeException("HTML�̉�͂Ɏ��s");
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
