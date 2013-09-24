/**
 * 
 */
package jp.arcanum.click.pages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import jp.arcanum.click.ArUtil;
import net.sf.click.Page;
import net.sf.click.control.HiddenField;
import net.sf.click.control.Submit;
import net.sf.click.control.TextArea;


/**
 *
 */
public class EditFilePage extends AbstractToolPage{

	
	/**
	 * ファイル内容
	 */
	private TextArea filecontents = new TextArea("filecontents");
	
	/**
	 * OKボタン
	 */
	private Submit ok      = new Submit("ok", "O.K", this, "onClickOk");
	
	/**
	 * 戻るボタン
	 */
	private Submit cancel  = new Submit("cancel", "戻る", this, "onClickBack");
	
	
	/**
	 * 引数パス<br>
	 * この変数はsetParameter()により一時的に使用される
	 */
	private String _abspath = "";
	
	/**
	 * 引数ページ<br>
	 * この変数はsetParameter()により一時的に使用される
	 */
	private Page _caller = null;

	/**
	 * 編集ファイルへのパス。
	 * 例：/WEB-INF/hoge.txt
	 */
	private HiddenField abspath = new HiddenField("abspath", String.class);

	/**
	 * 呼び出し元画面のクラス情報
	 */
	private HiddenField caller = new HiddenField("caller", Class.class);
	
	/**
	 * パラメータ設定
	 * @param abspath 
	 * @param caller
	 */
	public void setParameter(
			String abspath,
			Page caller
	){
		_abspath = abspath;
		_caller = caller;
	}
	
	/**
	 * index.htm用
	 * @param abspath
	 */
	public void setParameter(
			String abspath
	){
		_abspath = abspath;
	}
	

	/**
	 * コンストラクタ
	 *
	 */
	public EditFilePage(){
		
		//テキストエリア
		filecontents.setAttribute("wrap", "off");
		filecontents.setCols(100);
		filecontents.setRows(25);
		form.add(filecontents);
		//OK
		form.add(ok);
		//cancel
		form.add(cancel);
		//編集パス（隠し項目）
		form.add(abspath);
		//呼び出し元ページ（隠し項目）
		form.add(caller);
		
	}
	
	
	
	
	
	/**
	 * 初期設定
	 */
	public void onInit(){
		super.onInit();

		//　初期画面表示の場合
        if (getContext().isForward() && _abspath != null) {
			//引数の設定
			abspath.setValueObject(_abspath);
			if(_caller != null){
				caller.setValueObject(_caller.getClass());
			}
        }

	}

	/**
	 * POST処理
	 */
	public void onPost(){
		
		if(ok.isClicked()){
			return;
		}
		
		//　画面に引数で渡されたファイルの内容を設定する
		setFile();
		
	}

	/**
	 * GET処理
	 */
	public void onGet(){
		
		if(ok.isClicked()){
			return;
		}
		
		//　画面に引数で渡されたファイルの内容を設定する
		setFile();
		
	}

	/**
	 * 拡張子の変換テーブル
	 */
	private static final Map EXT_TABLE = new HashMap(){
		{
			put("TXT", "ptext");
			put("HTML", "html");
			put("HTM", "html");
			put("CSS", "css");
			put("JS", "js");
			put("XML", "xml");
		}
	};
	
	
	/**
	 * 画面にファイル内容を設定する
	 *
	 */
	private void setFile(){
		
		//　ファイルの実パスを取得
		String filepath = super.context.getServletContext().getRealPath("");
		filepath = filepath + abspath.getValue();
		
		//　ファイル内容を読み込み画面にセット
		List lines = ArUtil.readFile(filepath);
		String _lines = "";
		for(int i = 0 ; i < lines.size(); i++){
			_lines = _lines + lines.get(i) + ArUtil.getClientChangingLine(this);
		}
		filecontents.setValue(_lines);
		

	}
	
	/**
	 * 描画前処理
	 */
	public void onRender(){
		super.onRender();
		
		//　編集ファイルのパスを表示
		String editpath = abspath.getValue();
		if(editpath.startsWith("//")){
			editpath = editpath.substring(1);
		}
		addModel("editpath", editpath);
		
		//　拡張子によりEditAreaを設定（デフォルトはptext）
		String filepath = super.context.getServletContext().getRealPath("");
		filepath = filepath + abspath.getValue();
		String ext = filepath.substring(filepath.lastIndexOf(".")+1).toUpperCase();
		String fileext = "ptext";
		if(EXT_TABLE.containsKey(ext)){
			fileext = (String)EXT_TABLE.get(ext);
		}
		addModel("fileext", fileext);
		
	}
	
	/**
	 * OKボタン処理
	 * @return
	 */
    public boolean onClickOk(){
    	
    	//　テキストエリアに入力された内容から保存する情報に編集
        String text = this.filecontents.getValue();
        
        text = ArUtil.sanitize(text);
        
        System.out.println(text);
        filecontents.setValue(text);
        
        StringTokenizer tokens = new StringTokenizer(text, "\n");
        List wklines = new ArrayList();
        while(tokens.hasMoreTokens()){
            wklines.add(tokens.nextToken()+"\n");
        }
        
        //　保存ファイルを確定＆保存
        String path = super.context.getServletContext().getRealPath("");
        path = path + abspath.getValue();
        ArUtil.writeFile(path, wklines);
        
        //　後処理
    	addMessage("ファイルを更新しました。");
        return true;

    }
    
    /**
     * 戻る処理
     * @return
     */
    public boolean onClickBack(){
        
    	
    	Class clazz = (Class)caller.getValueObject();
    	if(clazz != null){
        	super.setForward(clazz);
    	}
    	else{
    		super.setForward(abspath.getValue());
    	}
    	
        return false;
        
    }
    
    
}
