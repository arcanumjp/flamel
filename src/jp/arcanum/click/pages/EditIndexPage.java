/**
 * 
 */
package jp.arcanum.click.pages;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import jp.arcanum.click.ArUtil;
import jp.arcanum.click.PageProperties;
import net.sf.click.control.HiddenField;
import net.sf.click.control.Submit;
import net.sf.click.control.TextArea;

import org.apache.commons.io.FileUtils;

import au.id.jericho.lib.html.Source;
import au.id.jericho.lib.html.SourceFormatter;


/**
 * コンテンツ編集ページ
 */
public class EditIndexPage extends AbstractToolPage{

	
	/**
	 * 編集パス  "/foo/bar/"形式
	 */
	private HiddenField editpathhdn = new HiddenField("editpathhdn", String.class);
	
	/**
	 * 編集年月日<br>
	 * "yyyyMMddhhmmssSSS_"形式。最新版を表示の際は""
	 */
	private HiddenField editymd = new HiddenField("editymd", String.class);

	/**
	 * ディレクトリのマークアップ（html/wiki）表示用
	 * TODO この変数は将来的に消す
	 */
	public String markup = "";
	
	/**
	 * ファイル内容
	 */
	private TextArea contents = new TextArea("contents");
	
	
	
	/**
	 * OKボタン
	 */
	private Submit savebtn      = new Submit("savebtn", "保存", this, "onClickSave");
	/**
	 * 戻るボタン
	 */
	private Submit cancel  = new Submit("cancel", "戻る", this, "onClickBack");
    /**
     * プレビューボタン
     */
	private Submit preview = new Submit("preview", "プレビュー", "onClickPreview");
	
	/**
	 * 前の履歴
	 */
	private Submit prevrireki = new Submit("prevrireki","＜＜ 前の履歴", this, "onClickPrev");
	
	/**
	 * HTML表示のON/OFFボタン
	 */
	private Submit onoff = new Submit("onoff", "***", this, "onClickOnOff");
	
	/**
	 * HTML表示のON/OFFフラグ
	 */
	private HiddenField onoffflg = new HiddenField("onoffflg", String.class);
	
	/**
	 * 次の履歴
	 */
	private Submit nextrireki = new Submit("nextrireki","次の履歴 ＞＞", this, "onClickNext");
	
	
	
	/**
	 * コンストラクタ
	 *
	 */
	public EditIndexPage(){
		
		//テキストエリア
		contents.setAttribute("wrap", "off");
		contents.setCols(100);
		contents.setRows(25);
		form.add(contents);
		//OK
		form.add(savebtn);
		//cancel
		form.add(cancel);
        //プレビュー
        form.add(preview);
        
        //　前の履歴
        form.add(prevrireki);
        //　次の履歴
        form.add(nextrireki);
		
        //ON/OFFボタン
        form.add(onoff);
        form.add(onoffflg);
        
		form.add(editpathhdn);
		form.add(editymd);
		
		
	}
	
	/**
	 * 初期設定
	 */
	public void onInit(){
		super.onInit();
		
		//　初期画面表示の場合
		String _path = (String)super.context.getRequestAttribute("EDIT_PATH");
        if (getContext().isForward() && _path != null) {
    		if(_path.startsWith("\\")){
    			_path = "/" + _path.substring(1);
    		}
    		if(_path.startsWith("//")){
    			_path = _path.substring(1);
    		}
        	editpathhdn.setValue(_path);
        	
        }

	}
	
	/**
	 * POST処理
	 */
	public void onPost(){
		
		
		//　履歴のプレフィクス確定
		String _editymd = (String)editymd.getValue();
		
		//　ディレクトリの絶対パス取得
		String _path = editpathhdn.getValue();
		_path = super.context.getServletContext().getRealPath("") + _path;

		//　編集ファイルを確定
		PageProperties prop = ArUtil.getPageProperties(_path);
		this.markup = prop.getMarkup();
		
		if(savebtn.isClicked()){
			return;
		}
		if(onoff.isClicked()){
			return;
		}
		
		//　以下、画面がはじめて表示されるときの処理
		
		onoffflg.setValue("1");
		
		if(prop.getMarkup().equals("html")){
			_path = _path + "/" + _editymd + "index.htm";
		}
		else{
			_path = _path + "/" + _editymd + "wiki.txt";
		}
		
		//　ファイルを読み込み＆画面に設定
        List filelines = ArUtil.readFile(_path);
        String wk = "";
        for(int i = 0 ; i < filelines.size(); i++){
            wk = wk + filelines.get(i) + "\n";
        }
        this.contents.setValue(wk);
		
	}
    
	/**
	 * OK処理
	 * @return
	 */
    public boolean onClickSave(){

        //　内容を取得し、行に分ける
        String text = this.contents.getValue();
        
        text = ArUtil.sanitize(text);
        contents.setValue(text);
        
        
        StringTokenizer tokens = new StringTokenizer(text, "\n");
        List wklines = new ArrayList();
        while(tokens.hasMoreTokens()){
            wklines.add(tokens.nextToken()+"\n");
        }
        
        //　保存するファイルの絶対パスを取得
        String _path = editpathhdn.getValue();
        _path = super.context.getServletContext().getRealPath("") + _path;
        
        //htmlかwikiのマークアップにより、保存するファイル名を決定
        PageProperties prop = ArUtil.getPageProperties(_path);
        this.markup = prop.getMarkup();
        String filename = "";
        String exfilename = "";
        if(prop.getMarkup().equals("html")){
            filename = "_index.htm";
            exfilename = "/index.htm";
        }
        else{
            filename = "_wiki.txt";
            exfilename = "/wiki.txt";
        }
        
		// バックアップを取る yyyymmddhhmmssSSS_wiki.txt / index.htm
		Date date = new Date();
		SimpleDateFormat f = new SimpleDateFormat("yyyyMMddkkmmssSSS");
		String ymd = f.format(date);
		File exindex = new File(_path + exfilename);
		if(exindex.exists()){
			try {
				FileUtils.copyFile(exindex,	new File(_path + "/" + ymd + filename));
						
			} catch (IOException e) {
				throw new RuntimeException("ファイルコピー失敗");
			}
			
		}
        
		//　ファイルを更新する
        ArUtil.writeFile(_path + exfilename, wklines);
        
        
        addMessage("コンテンツを更新しました。");
        editymd.setValue("");
        return true;        

    }
    

    /**
     * 戻る処理
     * @return
     */
    public boolean onClickBack(){
        
        super.setForward(ToolMainPage.class);
        return false;
        
    }

    /**
     * プレビュー処理
     * @return
     */
    public boolean onClickPreview(){
        
        //　テキストエリアの値を取得
        //　取得した値を別画面になるようにResponseに出力
        //　JavaScript側で別画面（_blank）になるようにする
        
        super.setForward(ToolMainPage.class);
        return false;
        
    }
    
    /**
     * 次の履歴
     * @return
     */
    public boolean onClickNext(){

    	String _editymd = editymd.getValue();
    	
    	File[] files = getRirekiFileList(editpathhdn.getValue());
    	String rireki = "";
    	for(int i = 0 ; i < files.length; i++){
    		String filename = files[i].getAbsolutePath();
    		filename = filename.substring(filename.lastIndexOf(ArUtil.SV_FILE_SEPARATOR)+1);
    		if(filename.startsWith(_editymd)){
    			if(i!=files.length-1){
            		filename = files[i+1].getAbsolutePath();
            		filename = filename.substring(filename.lastIndexOf(ArUtil.SV_FILE_SEPARATOR)+1);
            		filename = filename.substring(0, filename.lastIndexOf("_")+1);
            		rireki = filename;
    				
    			}
    			
    		}
    	}
    	
    	editymd.setValue(rireki);
    	
    	return true;
    }
    
    /**
     * 前の履歴
     * @return
     */
    public boolean onClickPrev(){

    	String _editymd = editymd.getValue();
    	
    	File[] files = getRirekiFileList(editpathhdn.getValue());
    	String rireki = "";
    	if(_editymd.equals("")){
    		String filename = files[files.length-1].getAbsolutePath();
    		filename = filename.substring(filename.lastIndexOf(ArUtil.SV_FILE_SEPARATOR)+1);
    		filename = filename.substring(0, filename.lastIndexOf("_")+1);
    		rireki = filename;
    	}
    	else{
    		
        	for(int i = 0 ; i < files.length; i++){
        		String filename = files[i].getAbsolutePath();
        		filename = filename.substring(filename.lastIndexOf(ArUtil.SV_FILE_SEPARATOR)+1);
        		if(filename.startsWith(_editymd)){
        			
            		filename = files[i-1].getAbsolutePath();
            		filename = filename.substring(filename.lastIndexOf(ArUtil.SV_FILE_SEPARATOR)+1);
            		filename = filename.substring(0, filename.lastIndexOf("_")+1);
            		rireki = filename;
        		}
        	}
    	}
    	
    	editymd.setValue(rireki);
    	
    	return true;
    }
    
 
    private File[] getRirekiFileList(String dirpath){
    	
    	File[] ret;
    	
    	dirpath = super.context.getServletContext().getRealPath("") + dirpath;
    	
    	File file = new File(dirpath);
    	ret = file.listFiles(
    			new FileFilter(){

					public boolean accept(File pathname) {
						if(pathname.getAbsolutePath().endsWith("_index.htm")){
							return true;
						}
						return false;
					}
    				
    			}
    	);
    	
    	return ret;
    	
    }
    
    
    public void onRender(){
    	
		super.onRender();

		//　前へ、次への使用可否を更新
    	updatePrevNextDisabled();
    	
    	
		//　編集パス（表示用）
		//this.editpath = editpathhdn.getValue();
    	addModel("editpath", editpathhdn.getValue());

		
    	String val = onoffflg.getValue();
    	String file = "";
    	if(val.equals("1")){
    		file = "click/tinymce.js";
    		onoff.setLabel("HTMLを表示");
    	}
    	else{
    		file = "click/editarea.js";
    		onoff.setLabel("WYSIWYG編集");
    	}
        List list = ArUtil.readFile(getContext().getServletContext(), file);
        String editorjs = "";
        for(int i = 0 ; i < list.size(); i++){
        	editorjs = editorjs + list.get(i) + "\n";
        }
        addModel("editorjs", editorjs);

   		final Source htmlSource = new Source(contents.getValue());
        final SourceFormatter formatter = htmlSource.getSourceFormatter();
        formatter.setIndentString("    ");
        formatter.setTidyTags(true);
        contents.setValue(formatter.toString());

    }
    
    
    private void updatePrevNextDisabled(){
    	
    	String _editymd = editymd.getValue();
    	
    	//　次への使用可否判断
    	if(_editymd.equals("")){
    		nextrireki.setAttribute("disabled", "true");
    	
    	}
    	
    	//　前への使用可否判断
    	File[] files = getRirekiFileList(editpathhdn.getValue());
		if(files.length==0){
			prevrireki.setAttribute("disabled", "true");
		}
		else{
        	String filename = files[0].getAbsolutePath();
        	filename = filename.substring(filename.lastIndexOf(ArUtil.SV_FILE_SEPARATOR)+1);
        	if(!_editymd.equals("") && filename.startsWith(_editymd)){
    			prevrireki.setAttribute("disabled", "true");
        	}
			
		}
		
		//　履歴のステータス表示
		String rirekistatus = "";
		if(_editymd.equals("")){
			rirekistatus = "最新版";
		}
		else{
			// TODO フォーマッタを使いなさい！
			rirekistatus = _editymd.substring( 0, 4) + "年" +
			               _editymd.substring( 4, 6) + "月" +
			               _editymd.substring( 6, 8) + "日" +
			               _editymd.substring( 8,10) + "時" +
			               _editymd.substring(10,12) + "分" +
			               _editymd.substring(12,14) + "." +
			               _editymd.substring(14,17) + "秒";
			
		}
		addModel("rirekistatus", rirekistatus);

		
    }
    
    public boolean onClickOnOff(){
    	

    	String val = onoffflg.getValue();
    	if(val.equals("1")){
    		onoffflg.setValue("2");
    	}
    	else{
    		onoffflg.setValue("1");
    	}
        
        return true;

    }
    
    
    
}
