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
	 * �J�E���^�[��
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
		
    	
        //�@�J�E���^�[�t�@�C��������e�ǂݍ���
    	String filepath = page.getContext().getServletContext().getRealPath(ArUtil.PATH_COUNTER);
    	File file = new File(filepath);
    	if(!file.exists()){
    		return;
    	}
    	List list = ArUtil.readFile(filepath);
    	String wk = (String)list.get(0);

		//�g�b�v�f�B���N�g���̏ꍇ�Ō����{�^�������ł͂Ȃ��ꍇ�̂݃J�E���^�[���A�b�v
        //String urltop = ArUtil.getProperty(this, ArUtil.PROP_SITE, "document_root");
        //if(urltop.equals("/")){
        //	urltop="";	//  �A�v���P�[�V�����g�b�v�̏ꍇ��//index.htm�ɂȂ邽�ߍ폜
        //}
		
		// TODO ���̃p�X�̏����́A�v���O�C�������ꂽ�Ƃ��A�Ɩ��v���ƂȂ�̂�
		//�@���̂܂܂ɂ���Bcms�Ƃ��Ĕėp���ł��Ȃ������B
        //if( ("/pages/index.htm").equals(page.getPath()) && ! this.kensaku.isClicked()){
        if( ("/pages/index.htm").equals(page.getPath()) ){
			
			//�@�Z�b�V�����ɃJ�E���^�A�b�v�t���O������ꍇ�̓X�f�ɃA�b�v����Ă���
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
	 * �J�E���^�A�b�v�t���O�B�R�����Z�b�V�����ɂ���ƃJ�E���^�[��
	 * ���ɃA�b�v���ꂽ���ɂȂ�炵���B
	 */
	public static final String COUNTER_FLG = "COUNTER_FLG";

}
