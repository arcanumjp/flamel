package jp.arcanum.click.pages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.click.Page;
import net.sf.click.control.Form;

/**
 * ���ۃy�[�W<br>
 * ���̃A�v���P�[�V��������ʂ��Ă̂��ׂẴy�[�W
 * ���p�����ׂ��N���X
 * @author shinya
 *
 */
public class AbstractPage extends Page {

	/**
	 * ���b�Z�[�W�G���A
	 */
	private String _message = "";
	protected void addMessage(String message){
		_message = "\n" + message;
	}

	
	/**
	 * �t�H�[��
	 */
	public Form form = new Form(){
	    public String getActionURL() {
	        HttpServletRequest request = getContext().getRequest();
	        HttpServletResponse response = getContext().getResponse();
	        
	        String ret = response.encodeURL(request.getRequestURI());
	        if(ret.startsWith("//")){
	        	ret = ret.substring(1);
	        }
	        
	        return ret;
	    }

		
	};
	
	
	public void onRender(){
		addModel("message", _message);
	}
	
	
}
