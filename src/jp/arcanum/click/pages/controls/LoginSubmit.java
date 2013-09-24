package jp.arcanum.click.pages.controls;

import jp.arcanum.click.ArUtil;
import jp.arcanum.click.UserInfo;
import net.sf.click.control.Submit;

/**
 * ���O�C�����̃T�u�~�b�g�{�^��
 * @author shinya
 *
 */
public class LoginSubmit extends Submit {

	/**
	 * �Ǘ��҂��ǂ���
	 */
	private boolean _isadmin = false;
	
	/**
	 * �R���X�g���N�^
	 * @param name name����
	 * @param label �\����
	 * @param listener �C�x���g���X�i�[
	 * @param method �C�x���g��
	 * @param isadmin �Ǘ��҂��ǂ���
	 */
	public LoginSubmit(
			String name, 
			String label, 
			Object listener, 
			String method, 
			boolean isadmin){
		super(name, label, listener, method);
		_isadmin = isadmin;
	}
	
	/**
	 * ������<br>
	 * ���O�C�����łȂ����""��Ԃ��B�܂��A���O�C�����イ�ł����Ă�
	 * _isadmin�t���O�ɂ��\���܂��͔�\�����s��
	 */
	public String toString(){
		UserInfo user = (UserInfo)super.context.getSession().getAttribute(ArUtil.USER);
		if(user == null){
			return "";
		}
		if(user.isAdmin() != _isadmin){
			return "";
		}
		return super.toString();
	}

}
