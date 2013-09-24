package jp.arcanum.click.pages;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

/**
 * ダウンロードページ。
 * 
 * @author arcanum
 *
 */
public class DownloadPage extends AbstractToolPage{

	
	/**
	 * POST処理
	 * setForward()でここに来る
	 */
	public void onPost(){
		
		
		String path = (String)super.context.getRequest().getAttribute("DLPATH");
		String filename = path.substring(path.lastIndexOf("/")+1);
		
        //　HTTPヘッダの指定
		HttpServletResponse response = super.context.getResponse();
        response.setHeader("Content-Disposition", "attachment;filename=\"" + filename + "\"");
        
        FileInputStream in = null;
        OutputStream os = null;
        try {
			// ファイルを読み込こんでレスポンスに返す
			File outputFile = new File(path);
			in = new FileInputStream(outputFile);
			os = response.getOutputStream();
			byte[] buffer = new byte[2048];
			int bytes;
			while ((bytes = in.read(buffer, 0, 2048)) != -1) {
			    os.write(buffer, 0, bytes);
			}
		} catch (Exception e) {
			throw new RuntimeException("ダウンロード中に失敗", e);
		}
		finally{
			try {
				if(os!=null)os.close();
				if(in!=null)in.close();
			} catch (IOException e) {
				throw new RuntimeException("ダウンロード中のファイルクローズに失敗", e);
			}
		}
		
	}
	


    public String getPath() {
        return null;
    }


}
