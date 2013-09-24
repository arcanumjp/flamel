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
	 * �t�@�C�����e
	 */
	private TextArea filecontents = new TextArea("filecontents");
	
	/**
	 * OK�{�^��
	 */
	private Submit ok      = new Submit("ok", "O.K", this, "onClickOk");
	
	/**
	 * �߂�{�^��
	 */
	private Submit cancel  = new Submit("cancel", "�߂�", this, "onClickBack");
	
	
	/**
	 * �����p�X<br>
	 * ���̕ϐ���setParameter()�ɂ��ꎞ�I�Ɏg�p�����
	 */
	private String _abspath = "";
	
	/**
	 * �����y�[�W<br>
	 * ���̕ϐ���setParameter()�ɂ��ꎞ�I�Ɏg�p�����
	 */
	private Page _caller = null;

	/**
	 * �ҏW�t�@�C���ւ̃p�X�B
	 * ��F/WEB-INF/hoge.txt
	 */
	private HiddenField abspath = new HiddenField("abspath", String.class);

	/**
	 * �Ăяo������ʂ̃N���X���
	 */
	private HiddenField caller = new HiddenField("caller", Class.class);
	
	/**
	 * �p�����[�^�ݒ�
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
	 * index.htm�p
	 * @param abspath
	 */
	public void setParameter(
			String abspath
	){
		_abspath = abspath;
	}
	

	/**
	 * �R���X�g���N�^
	 *
	 */
	public EditFilePage(){
		
		//�e�L�X�g�G���A
		filecontents.setAttribute("wrap", "off");
		filecontents.setCols(100);
		filecontents.setRows(25);
		form.add(filecontents);
		//OK
		form.add(ok);
		//cancel
		form.add(cancel);
		//�ҏW�p�X�i�B�����ځj
		form.add(abspath);
		//�Ăяo�����y�[�W�i�B�����ځj
		form.add(caller);
		
	}
	
	
	
	
	
	/**
	 * �����ݒ�
	 */
	public void onInit(){
		super.onInit();

		//�@������ʕ\���̏ꍇ
        if (getContext().isForward() && _abspath != null) {
			//�����̐ݒ�
			abspath.setValueObject(_abspath);
			if(_caller != null){
				caller.setValueObject(_caller.getClass());
			}
        }

	}

	/**
	 * POST����
	 */
	public void onPost(){
		
		if(ok.isClicked()){
			return;
		}
		
		//�@��ʂɈ����œn���ꂽ�t�@�C���̓��e��ݒ肷��
		setFile();
		
	}

	/**
	 * GET����
	 */
	public void onGet(){
		
		if(ok.isClicked()){
			return;
		}
		
		//�@��ʂɈ����œn���ꂽ�t�@�C���̓��e��ݒ肷��
		setFile();
		
	}

	/**
	 * �g���q�̕ϊ��e�[�u��
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
	 * ��ʂɃt�@�C�����e��ݒ肷��
	 *
	 */
	private void setFile(){
		
		//�@�t�@�C���̎��p�X���擾
		String filepath = super.context.getServletContext().getRealPath("");
		filepath = filepath + abspath.getValue();
		
		//�@�t�@�C�����e��ǂݍ��݉�ʂɃZ�b�g
		List lines = ArUtil.readFile(filepath);
		String _lines = "";
		for(int i = 0 ; i < lines.size(); i++){
			_lines = _lines + lines.get(i) + ArUtil.getClientChangingLine(this);
		}
		filecontents.setValue(_lines);
		

	}
	
	/**
	 * �`��O����
	 */
	public void onRender(){
		super.onRender();
		
		//�@�ҏW�t�@�C���̃p�X��\��
		String editpath = abspath.getValue();
		if(editpath.startsWith("//")){
			editpath = editpath.substring(1);
		}
		addModel("editpath", editpath);
		
		//�@�g���q�ɂ��EditArea��ݒ�i�f�t�H���g��ptext�j
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
	 * OK�{�^������
	 * @return
	 */
    public boolean onClickOk(){
    	
    	//�@�e�L�X�g�G���A�ɓ��͂��ꂽ���e����ۑ�������ɕҏW
        String text = this.filecontents.getValue();
        
        text = ArUtil.sanitize(text);
        
        System.out.println(text);
        filecontents.setValue(text);
        
        StringTokenizer tokens = new StringTokenizer(text, "\n");
        List wklines = new ArrayList();
        while(tokens.hasMoreTokens()){
            wklines.add(tokens.nextToken()+"\n");
        }
        
        //�@�ۑ��t�@�C�����m�聕�ۑ�
        String path = super.context.getServletContext().getRealPath("");
        path = path + abspath.getValue();
        ArUtil.writeFile(path, wklines);
        
        //�@�㏈��
    	addMessage("�t�@�C�����X�V���܂����B");
        return true;

    }
    
    /**
     * �߂鏈��
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
