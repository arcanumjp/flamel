/**
 * 
 */
package jp.arcanum.click.pages;

import java.util.Map;

import jp.arcanum.click.ArUtil;
import jp.arcanum.click.UserInfo;
import net.sf.click.control.PasswordField;
import net.sf.click.control.Submit;
import net.sf.click.control.TextField;


/**
 *
 */
public class LoginPage extends AbstractPage{

	/**
	 * ���[�UID
	 */
	private TextField userid = new TextField("userid", "���[�UID");
	/**
	 * �p�X���[�h�t�B�[���h
	 */
	private PasswordField password = new PasswordField("password", "�p�X���[�h");
	/**
	 * ���O�C���{�^��
	 */
	private Submit login = new Submit("login", "���O�C��",this,"onClickLogin");

	
	/**
	 * �R���X�g���N�^
	 *
	 */
	public LoginPage(){
		
		form.add(userid);
		form.add(password);
		form.add(login);
		
		userid.setSize(20);
		password.setSize(20);
		
	}

	/**
	 * ����������
	 */
	public void onInit(){
		
		//�@���Ƀ��O�C�����ł���΁A�c�[�����C���y�[�W�ɑJ�ڂ���
		if(super.context.getSession().getAttribute(ArUtil.USER) != null){
			super.setRedirect(ToolMainPage.class);
			return;
		}
		
	}
	
	/**
	 * ���O�C������
	 * @return
	 */
	public boolean onClickLogin(){
		
		String id   = userid.getValue();
		String pass = password.getValue();
		
		if(id.length() == 0){
			addMessage("���[�UID�����͂���Ă��܂���B");
			return true;
		}
		if(pass.equals("")){
			addMessage("�p�X���[�h�����͂���Ă��܂���B");
			return true;
		}
		
		//���[�U��������Ƃ�
		UserInfo user = new UserInfo();

		//�@�X�[�p�[���[�U���ǂ���
		String adminid   = ArUtil.getProperty(this, ArUtil.PROP_ADMIN, ArUtil.KEY_ADMIN_CONF_NAME);
		String adminpass = ArUtil.getProperty(this, ArUtil.PROP_ADMIN, ArUtil.KEY_ADMIN_CONF_PASS);
		if(id.equals(adminid) && pass.equals(adminpass)){

			user.setUsername(id);
			user.setGroup("");  // �X�[�p�[���[�U�Ȃ̂ŕs�v
			user.setAdmin(true);
			super.context.getSession().setAttribute(ArUtil.USER, user);
			super.setRedirect(ToolMainPage.class);
			return false;

		}

		// ���[�U�t�@�C������ǂݍ���
		Map xmluser = ArUtil.getUser(this, id);
		if(xmluser.get("pass").equals(pass)){
			
			user.setUsername(id);
			user.setGroup((String)xmluser.get("group"));
			user.setAdmin(false);
			super.context.getSession().setAttribute(ArUtil.USER, user);
			super.setRedirect(ToolMainPage.class);
			return false;
			
		}
		
		addMessage("���[�UID�܂��̓p�X���[�h���Ԉ���Ă��܂��B");
		return true;

	}

	
	
}
