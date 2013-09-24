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
 * �R���e���c�ҏW�y�[�W
 */
public class EditIndexPage extends AbstractToolPage{

	
	/**
	 * �ҏW�p�X  "/foo/bar/"�`��
	 */
	private HiddenField editpathhdn = new HiddenField("editpathhdn", String.class);
	
	/**
	 * �ҏW�N����<br>
	 * "yyyyMMddhhmmssSSS_"�`���B�ŐV�ł�\���̍ۂ�""
	 */
	private HiddenField editymd = new HiddenField("editymd", String.class);

	/**
	 * �f�B���N�g���̃}�[�N�A�b�v�ihtml/wiki�j�\���p
	 * TODO ���̕ϐ��͏����I�ɏ���
	 */
	public String markup = "";
	
	/**
	 * �t�@�C�����e
	 */
	private TextArea contents = new TextArea("contents");
	
	
	
	/**
	 * OK�{�^��
	 */
	private Submit savebtn      = new Submit("savebtn", "�ۑ�", this, "onClickSave");
	/**
	 * �߂�{�^��
	 */
	private Submit cancel  = new Submit("cancel", "�߂�", this, "onClickBack");
    /**
     * �v���r���[�{�^��
     */
	private Submit preview = new Submit("preview", "�v���r���[", "onClickPreview");
	
	/**
	 * �O�̗���
	 */
	private Submit prevrireki = new Submit("prevrireki","���� �O�̗���", this, "onClickPrev");
	
	/**
	 * HTML�\����ON/OFF�{�^��
	 */
	private Submit onoff = new Submit("onoff", "***", this, "onClickOnOff");
	
	/**
	 * HTML�\����ON/OFF�t���O
	 */
	private HiddenField onoffflg = new HiddenField("onoffflg", String.class);
	
	/**
	 * ���̗���
	 */
	private Submit nextrireki = new Submit("nextrireki","���̗��� ����", this, "onClickNext");
	
	
	
	/**
	 * �R���X�g���N�^
	 *
	 */
	public EditIndexPage(){
		
		//�e�L�X�g�G���A
		contents.setAttribute("wrap", "off");
		contents.setCols(100);
		contents.setRows(25);
		form.add(contents);
		//OK
		form.add(savebtn);
		//cancel
		form.add(cancel);
        //�v���r���[
        form.add(preview);
        
        //�@�O�̗���
        form.add(prevrireki);
        //�@���̗���
        form.add(nextrireki);
		
        //ON/OFF�{�^��
        form.add(onoff);
        form.add(onoffflg);
        
		form.add(editpathhdn);
		form.add(editymd);
		
		
	}
	
	/**
	 * �����ݒ�
	 */
	public void onInit(){
		super.onInit();
		
		//�@������ʕ\���̏ꍇ
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
	 * POST����
	 */
	public void onPost(){
		
		
		//�@�����̃v���t�B�N�X�m��
		String _editymd = (String)editymd.getValue();
		
		//�@�f�B���N�g���̐�΃p�X�擾
		String _path = editpathhdn.getValue();
		_path = super.context.getServletContext().getRealPath("") + _path;

		//�@�ҏW�t�@�C�����m��
		PageProperties prop = ArUtil.getPageProperties(_path);
		this.markup = prop.getMarkup();
		
		if(savebtn.isClicked()){
			return;
		}
		if(onoff.isClicked()){
			return;
		}
		
		//�@�ȉ��A��ʂ��͂��߂ĕ\�������Ƃ��̏���
		
		onoffflg.setValue("1");
		
		if(prop.getMarkup().equals("html")){
			_path = _path + "/" + _editymd + "index.htm";
		}
		else{
			_path = _path + "/" + _editymd + "wiki.txt";
		}
		
		//�@�t�@�C����ǂݍ��݁���ʂɐݒ�
        List filelines = ArUtil.readFile(_path);
        String wk = "";
        for(int i = 0 ; i < filelines.size(); i++){
            wk = wk + filelines.get(i) + "\n";
        }
        this.contents.setValue(wk);
		
	}
    
	/**
	 * OK����
	 * @return
	 */
    public boolean onClickSave(){

        //�@���e���擾���A�s�ɕ�����
        String text = this.contents.getValue();
        
        text = ArUtil.sanitize(text);
        contents.setValue(text);
        
        
        StringTokenizer tokens = new StringTokenizer(text, "\n");
        List wklines = new ArrayList();
        while(tokens.hasMoreTokens()){
            wklines.add(tokens.nextToken()+"\n");
        }
        
        //�@�ۑ�����t�@�C���̐�΃p�X���擾
        String _path = editpathhdn.getValue();
        _path = super.context.getServletContext().getRealPath("") + _path;
        
        //html��wiki�̃}�[�N�A�b�v�ɂ��A�ۑ�����t�@�C����������
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
        
		// �o�b�N�A�b�v����� yyyymmddhhmmssSSS_wiki.txt / index.htm
		Date date = new Date();
		SimpleDateFormat f = new SimpleDateFormat("yyyyMMddkkmmssSSS");
		String ymd = f.format(date);
		File exindex = new File(_path + exfilename);
		if(exindex.exists()){
			try {
				FileUtils.copyFile(exindex,	new File(_path + "/" + ymd + filename));
						
			} catch (IOException e) {
				throw new RuntimeException("�t�@�C���R�s�[���s");
			}
			
		}
        
		//�@�t�@�C�����X�V����
        ArUtil.writeFile(_path + exfilename, wklines);
        
        
        addMessage("�R���e���c���X�V���܂����B");
        editymd.setValue("");
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

    /**
     * �v���r���[����
     * @return
     */
    public boolean onClickPreview(){
        
        //�@�e�L�X�g�G���A�̒l���擾
        //�@�擾�����l��ʉ�ʂɂȂ�悤��Response�ɏo��
        //�@JavaScript���ŕʉ�ʁi_blank�j�ɂȂ�悤�ɂ���
        
        super.setForward(ToolMainPage.class);
        return false;
        
    }
    
    /**
     * ���̗���
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
     * �O�̗���
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

		//�@�O�ցA���ւ̎g�p�ۂ��X�V
    	updatePrevNextDisabled();
    	
    	
		//�@�ҏW�p�X�i�\���p�j
		//this.editpath = editpathhdn.getValue();
    	addModel("editpath", editpathhdn.getValue());

		
    	String val = onoffflg.getValue();
    	String file = "";
    	if(val.equals("1")){
    		file = "click/tinymce.js";
    		onoff.setLabel("HTML��\��");
    	}
    	else{
    		file = "click/editarea.js";
    		onoff.setLabel("WYSIWYG�ҏW");
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
    	
    	//�@���ւ̎g�p�۔��f
    	if(_editymd.equals("")){
    		nextrireki.setAttribute("disabled", "true");
    	
    	}
    	
    	//�@�O�ւ̎g�p�۔��f
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
		
		//�@�����̃X�e�[�^�X�\��
		String rirekistatus = "";
		if(_editymd.equals("")){
			rirekistatus = "�ŐV��";
		}
		else{
			// TODO �t�H�[�}�b�^���g���Ȃ����I
			rirekistatus = _editymd.substring( 0, 4) + "�N" +
			               _editymd.substring( 4, 6) + "��" +
			               _editymd.substring( 6, 8) + "��" +
			               _editymd.substring( 8,10) + "��" +
			               _editymd.substring(10,12) + "��" +
			               _editymd.substring(12,14) + "." +
			               _editymd.substring(14,17) + "�b";
			
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
