/**
 * 
 */
package jp.arcanum.click.pages;

import jp.arcanum.click.ArUtil;
import jp.arcanum.click.UserInfo;
import net.sf.click.control.Checkbox;
import net.sf.click.control.PasswordField;
import net.sf.click.control.Submit;
import net.sf.click.control.TextField;


/**
 *
 */
public class ManageSitePage extends ToolMainPage{

	/**
	 * ���[�U��
	 */
	private TextField username = new TextField("username");
	/**
	 * �p�X���[�h
	 */
	private PasswordField password1 = new PasswordField("password1");
	/**
	 * �p�X���[�h�i�m�F�p�j
	 */
	private PasswordField password2 = new PasswordField("password2");
	
	//-------------------------------------------------------------
	
	private TextField headertitle = new TextField("headertitle");
	/**
	 * �T�C�g�^�C�g��
	 */
	private TextField sitetitle = new TextField("sitetitle");
	/**
	 * �T�C�g�^�C�g���������N���邩
	 */
	private Checkbox sitelinkable = new Checkbox("sitelinkable");
	/**
	 * ���[��
	 */
	private TextField mailto = new TextField("mailto");
	/**
	 * ���[���������N���邩
	 */
	private Checkbox maillinkable = new Checkbox("maillinkable");
	/**
	 * ���[���̃����N�e�L�X�g
	 */
	private TextField mailtext = new TextField("mailtext");
	/**
	 * �|�[�g�ԍ�
	 */
	private TextField portno = new TextField("portno");
	/**
	 * �h�L�������g���[�g
	 */
	private TextField docroot = new TextField("docroot");
	/**
	 * �e���v���[�g�p�X
	 */
	private TextField templatepath = new TextField("templatepath");
	/**
	 * �e���v���[�g�ҏW�{�^��
	 */
	private Submit edittemp = new Submit("edittemp", "�ҏW", this, "onClickEditTemplate");
	/**
	 * �����E�G���[�E�����Ȃ����b�Z�[�W
	 */
	private TextField errnocond = new TextField("errnocond");
	/**
	 * �����E�G���[�E���ʂȂ����b�Z�[�W
	 */
	private TextField errnores = new TextField("errnores");
	/**
	 * �����E���ʁE�\���J�n�^�O
	 */
	private TextField resstarttag = new TextField("resstarttag");
	/**
	 * �����E���ʁE�\���J�n�^�O
	 */
	private TextField resendtag = new TextField("resendtag");
	/**
	 * �����E���ʁE�O�㕶����
	 */
	private TextField resshowlength = new TextField("resshowlength");
	/**
	 * �����E���ʁE�\���J�n�^�O
	 */
	private TextField resemphstarttag = new TextField("resemphstarttag");
	/**
	 * �����E���ʁE�\���J�n�^�O
	 */
	private TextField resemphendtag = new TextField("resemphendtag");
	
	//-------------------------------------------------------------

	/**
	 * O.K�{�^��
	 */
	private Submit ok = new Submit("ok", "O.K", this, "onClickOk");
	/**
	 * �߂�{�^��
	 */
	private Submit back = new Submit("back", "�߂�", this, "onClickBack");
	
	
	
	/**
	 * �R���X�g���N�^
	 *
	 */
	public ManageSitePage(){
		
		//�@���[�U��
		username.setSize(50);
		form.add(username);
		//�@�p�X���[�h
		password1.setSize(50);
		form.add(password1);
		//�@�p�X���[�h�i�m�F�p�j
		password2.setSize(50);
		form.add(password2);
		
		
		//�@�w�b�_�[�^�C�g��
		headertitle.setSize(50);
		form.add(headertitle);
		//�@�T�C�g�^�C�g��
		sitetitle.setSize(50);
		form.add(sitetitle);
		//�@�T�C�g�����N
		form.add(sitelinkable);
		//�@���[���A�h���X
		mailto.setSize(50);
		form.add(mailto);
		//�@���[�������N
		form.add(maillinkable);
		//�@���[���e�L�X�g
		mailtext.setSize(50);
		form.add(mailtext);
		//�@�|�[�g�ԍ�
		portno.setMaxLength(5);
		portno.setSize(50);
		form.add(portno);
		//�@�h�L�������g���[�g
		docroot.setSize(50);
		form.add(docroot);
		//�@�e���v���[�g�p�X
		templatepath.setSize(50);
		form.add(templatepath);
		//�@�e���v���[�g�ҏW�{�^��
		form.add(edittemp);
		//�@�����E�G���[�E�����Ȃ����b�Z�[�W
		errnocond.setSize(50);
		form.add(errnocond);
		//�@�����E�G���[�E���ʂȂ����b�Z�[�W
		errnores.setSize(50);
		form.add(errnores);
		//�@�����E���ʁE�J�n�^�O
		resstarttag.setSize(50);
		form.add(resstarttag);
		//�@�����E���ʁE�I���^�O
		resendtag.setSize(50);
		form.add(resendtag);
		//�@�����E�O��\��������
		resshowlength.setMaxLength(2);
		resshowlength.setSize(50);
		form.add(resshowlength);
		//�@�����E���ʁE�����J�n�^�O
		resemphstarttag.setSize(50);
		form.add(resemphstarttag);
		//�@�����E���ʁE�����I���^�O
		resemphendtag.setSize(50);
		form.add(resemphendtag);
		//�@�n�j�{�^��
		form.add(ok);
		//�@�L�����Z���{�^��
		form.add(back);

	}
	
	/**
	 * ����������
	 */
	public void onInit(){
		super.onInit();
		
		
	}
	
	/**
	 * POST����
	 */
	public void onPost(){
		
		if(ok.isClicked()){
			return;
		}
		
		
		String wkstr = "";
		
		//-------------------------------------------------------------------
		
		wkstr = ArUtil.getProperty(this, ArUtil.PROP_ADMIN, ArUtil.KEY_ADMIN_CONF_NAME);
		username.setValue(wkstr);

		wkstr = ArUtil.getProperty(this, ArUtil.PROP_ADMIN, ArUtil.KEY_ADMIN_CONF_PASS);
		password1.setValue(wkstr);
		password2.setValue(wkstr);
		
		
		//-------------------------------------------------------------------
		//�w�b�_�[�^�C�g��
		wkstr = ArUtil.getProperty(this, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_HEADER);
		headertitle.setValue(wkstr);
		//�T�C�g�^�C�g��
		wkstr = ArUtil.getProperty(this, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_TITLE);
		sitetitle.setValue(wkstr);
		//�T�C�g�����N
		wkstr = ArUtil.getProperty(this, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_TITLELINK);
		sitelinkable.setValue(wkstr);
		//���[��
		wkstr = ArUtil.getProperty(this, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_MAILTO);
		mailto.setValue(wkstr);
		//���[�������N
		wkstr = ArUtil.getProperty(this, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_MAILLINK);
		maillinkable.setValue(wkstr);
		//���[���^�C�g��
		wkstr = ArUtil.getProperty(this, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_MAILLINK);
		//�|�[�g�ԍ�  
		wkstr = ArUtil.getProperty(this, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_PORTNO);
		portno.setValue(wkstr);
		//�h�L�������g���[�g  
		wkstr = ArUtil.getProperty(this, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_DOCROOT);
		docroot.setValue(wkstr);
		//�e���v���[�ghtm�p�X  
		wkstr = ArUtil.getProperty(this, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_TEMPLATE);
		templatepath.setValue(wkstr);
		//���� �G���[ �����Ȃ����b�Z�[�W  
		wkstr = ArUtil.getProperty(this, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_SRCH_ERR_NOCOND);
		errnocond.setValue(wkstr);
		//���� �G���[ ���ʂȂ����b�Z�[�W  
		wkstr = ArUtil.getProperty(this, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_SRCH_ERR_NORES);
		errnores.setValue(wkstr);
		//���� ���� �\���J�n�^�O  
		wkstr = ArUtil.getProperty(this, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_SRCH_RES_START_TAG);
		resstarttag.setValue(wkstr);
		//���� ���� �\���I���^�O  
		wkstr = ArUtil.getProperty(this, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_SRCH_RES_END_TAG);
		resendtag.setValue(wkstr);
		//���� ���� �O�㕶����  
		wkstr = ArUtil.getProperty(this, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_SRCH_RES_SHOWLONGTH);
		resshowlength.setValue(wkstr);
		//���� ���� �����J�n  
		wkstr = ArUtil.getProperty(this, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_SRCH_RES_EMPH_START);
		resemphstarttag.setValue(wkstr);
		//���� ���� �����I�� 
		wkstr = ArUtil.getProperty(this, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_SRCH_RES_EMPH_END);
		resemphendtag.setValue(wkstr);
		
		
		
	}
    
    /**
     * �e���v���[�g�ҏW����
     * @return
     */
	public boolean onClickEditTemplate(){
		
    	String abspath = ArUtil.getProperty(this, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_TEMPLATE);
    	EditFilePage page = (EditFilePage)super.context.createPage(EditFilePage.class);
    	page.setParameter(abspath, this);
        super.setForward(page);
        return false;

	}
	
	/**
	 * OK�{�^������
	 * @return
	 */
	public boolean onClickOk(){

    	
		//�@�t�B�[���h�P�̃`�F�b�N -----------------------------------------------------
        
		//�@���[�U��
		String _username = username.getValue();
		if(_username.equals("")){
			addMessage("���[�U�������͂���Ă��܂���B");
			return false;
		}
		//�@�p�X���[�h
		String _password1 = password1.getValue();
		if(_password1.equals("")){
			addMessage("�p�X���[�h�����͂���Ă��܂���B");
			return false;
		}
		//�@�p�X���[�h�i�m�F�p�j
		String _password2 = password2.getValue();
		if(_password2.equals("")){
			addMessage("�p�X���[�h�i�m�F�p�j�����͂���Ă��܂���B");
			return false;
		}
		
		
		//�@�w�b�_�[
		String _headertitle = headertitle.getValue();
		if(_headertitle.equals("")){
			addMessage("�w�b�_�[�e�L�X�g�����͂���Ă��܂���B");
			return false;
		}
		//�@�T�C�g�^�C�g��
		String _sitetitle = sitetitle.getValue();
		if(_sitetitle.equals("")){
			addMessage("�T�C�g�^�C�g�������͂���Ă��܂���B");
			return false;
		}
		
		//�@�T�C�g�����N
		//�@�P�̃`�F�b�N�Ȃ�
		
		//�@���[��
		String _mailto = mailto.getValue();
		if(_mailto.equals("")){
			addMessage("���[���A�h���X�����͂���Ă��܂���B");
			return false;
		}
		//�@���[�������N
		//�@�P�̃`�F�b�N�Ȃ�
		
		//�@���[���e�L�X�g
		String _mailtext = mailtext.getValue();
		//�@�P�̃`�F�b�N�Ȃ�
		
		//�@�|�[�g�ԍ�
		String _portno = portno.getValue();
		if(_portno.equals("")){
			addMessage("�|�[�g�ԍ��͕K�{�ł��B");
			return false;
		}
		
		//�@�h�L�������g���[�g
		String _docroot = docroot.getValue();
		if(_docroot.equals("")){
			addMessage("�h�L�������g���[�g�͕K�{�ł��B");
			return false;
		}
		
		//�@�e���v���[�g�p�X
		String _templatepath = templatepath.getValue();
		if(_templatepath.equals("")){
			addMessage("�e���v���[�g�p�X�͕K�{�ł��B");
			return false;
		}
		
		//�@�����E�G���[�E�����Ȃ����b�Z�[�W
		String _errnocond = errnocond.getValue();
		if(_errnocond.equals("")){
			addMessage("�����E�G���[�E�����Ȃ��͕K�{�ł��B");
			return false;
		}
		
		//�@�����E�G���[�E���ʂȂ����b�Z�[�W
		String _errnores = errnores.getValue();
		if(_errnores.equals("")){
			addMessage("�����E�G���[�E���ʂȂ��͕K�{�ł��B");
			return false;
		}
		
		//�@�����E���ʁE�J�n�^�O
		String _resstarttag = resstarttag.getValue();
		
		//�@�����E���ʁE�I���^�O
		String _resendtag = resendtag.getValue();
		
		//�@�����E�O��\��������
		String _resshowlength = resshowlength.getValue();
		
		//�@�����E���ʁE�����J�n�^�O
		String _resemphstarttag = resemphstarttag.getValue();
		
		//�@�����E���ʁE�����I���^�O
		String _resemphendtag = resemphendtag.getValue();
        
		
		//�@���փ`�F�b�N -----------------------------------------------------
		
		//�@�p�X���[�h���m�F�p�ƈقȂ�ꍇ�G���[
		if(!_password1.equals(_password2)){
			addMessage("�p�X���[�h���m�F�p�ƈقȂ�܂��B");
			return false;
		}
		
		
		//�@admin.properties�X�V -----------------------------------------------------
		
		ArUtil.setProperty(this, ArUtil.PROP_ADMIN, ArUtil.KEY_ADMIN_CONF_NAME, _username);
		ArUtil.setProperty(this, ArUtil.PROP_ADMIN, ArUtil.KEY_ADMIN_CONF_PASS, _password1);
		
		UserInfo user = (UserInfo)super.context.getSession().getAttribute(ArUtil.USER);
		user.setUsername(_username);
		
		//�@site.properties�X�V -----------------------------------------------------
		
		//�w�b�_�[�^�C�g��
		ArUtil.setProperty(this, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_HEADER, _headertitle);
		//�T�C�g�^�C�g��
		ArUtil.setProperty(this, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_TITLE, _sitetitle);
		//�T�C�g�����N
		ArUtil.setProperty(this, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_TITLELINK, sitelinkable.getValue());
		//���[��
		ArUtil.setProperty(this, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_MAILTO, _mailto);
		//���[�������N
		ArUtil.setProperty(this, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_MAILLINK, maillinkable.getValue());
		//���[�������N�e�L�X�g
		ArUtil.setProperty(this, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_MAILTITLE, _mailtext);
		//�|�[�g�ԍ�  
		ArUtil.setProperty(this, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_PORTNO, _portno);
		//�h�L�������g���[�g  
		ArUtil.setProperty(this, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_DOCROOT, _docroot);
		//�e���v���[�ghtm�p�X  
		ArUtil.setProperty(this, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_TEMPLATE, _templatepath);
		//���� �G���[ �����Ȃ����b�Z�[�W  
		ArUtil.setProperty(this, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_SRCH_ERR_NOCOND, _errnocond);
		//���� �G���[ ���ʂȂ����b�Z�[�W  
		ArUtil.setProperty(this, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_SRCH_ERR_NORES, _errnores);
		//���� ���� �\���J�n�^�O  
		ArUtil.setProperty(this, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_SRCH_RES_START_TAG, _resstarttag);
		//���� ���� �\���I���^�O  
		ArUtil.setProperty(this, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_SRCH_RES_END_TAG, _resendtag);
		//���� ���� �O�㕶����  
		ArUtil.setProperty(this, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_SRCH_RES_SHOWLONGTH, _resshowlength);
		//���� ���� �����J�n  
		ArUtil.setProperty(this, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_SRCH_RES_EMPH_START, _resemphstarttag);
		//���� ���� �����I�� 
		ArUtil.setProperty(this, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_SRCH_RES_EMPH_END, _resemphendtag);
		
		
		addMessage("�ݒ���X�V���܂����B");
		
        return true;        
    }
    
	/**
	 * �߂鏈��
	 * @return
	 */
    public boolean onClickBack(){
        
        super.setForward(ToolMainPage.class);
        return false;
        
    }
    
    
    public void onRender(){
		super.onRender();

    }
    
}
