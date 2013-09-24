package jp.arcanum.click.pages;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

/**
 * �_�E�����[�h�y�[�W�B
 * 
 * @author arcanum
 *
 */
public class DownloadPage extends AbstractToolPage{

	
	/**
	 * POST����
	 * setForward()�ł����ɗ���
	 */
	public void onPost(){
		
		
		String path = (String)super.context.getRequest().getAttribute("DLPATH");
		String filename = path.substring(path.lastIndexOf("/")+1);
		
        //�@HTTP�w�b�_�̎w��
		HttpServletResponse response = super.context.getResponse();
        response.setHeader("Content-Disposition", "attachment;filename=\"" + filename + "\"");
        
        FileInputStream in = null;
        OutputStream os = null;
        try {
			// �t�@�C����ǂݍ�����Ń��X�|���X�ɕԂ�
			File outputFile = new File(path);
			in = new FileInputStream(outputFile);
			os = response.getOutputStream();
			byte[] buffer = new byte[2048];
			int bytes;
			while ((bytes = in.read(buffer, 0, 2048)) != -1) {
			    os.write(buffer, 0, bytes);
			}
		} catch (Exception e) {
			throw new RuntimeException("�_�E�����[�h���Ɏ��s", e);
		}
		finally{
			try {
				if(os!=null)os.close();
				if(in!=null)in.close();
			} catch (IOException e) {
				throw new RuntimeException("�_�E�����[�h���̃t�@�C���N���[�Y�Ɏ��s", e);
			}
		}
		
	}
	


    public String getPath() {
        return null;
    }


}
