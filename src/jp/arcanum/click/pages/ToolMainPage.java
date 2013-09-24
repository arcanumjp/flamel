/**
 * 
 */
package jp.arcanum.click.pages;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import jp.arcanum.click.ArUtil;
import jp.arcanum.click.PageProperties;
import jp.arcanum.click.UserInfo;
import jp.arcanum.click.pages.controls.ArTreeNode;
import jp.arcanum.click.pages.controls.LoginSubmit;
import net.sf.click.Context;
import net.sf.click.control.Checkbox;
import net.sf.click.control.FileField;
import net.sf.click.control.HiddenField;
import net.sf.click.control.Option;
import net.sf.click.control.PasswordField;
import net.sf.click.control.Select;
import net.sf.click.control.Submit;
import net.sf.click.control.TextArea;
import net.sf.click.control.TextField;
import net.sf.click.extras.control.PickList;
import net.sf.click.extras.tree.Tree;
import net.sf.click.extras.tree.TreeListener;
import net.sf.click.extras.tree.TreeNode;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.FileUtils;


/**
 * �c�[���y�[�W�N���X�B
 */
public class ToolMainPage extends AbstractToolPage implements TreeListener{

	/** �f�B���N�g���c���[���̂������[�g�m�[�h���Z�b�V�����Ɋi�[����ۂ̃L�[ */
	private static final String TOOL_TREE = "TOOL_TREE";

	/**
	 * �V�K�f�B���N�g���쐬�{�^��
	 */
	private Submit newdir = new Submit("newdir", "�V�K",this,"onClickNewDir");
	
	/**
	 * �V�K�f�B���N�g�������̓t�B�[���h
	 */
	private TextField newdirname = new TextField("newdirname");

	/**
	 * �f�B���N�g���폜�{�^��
	 */
	private Submit deldir = new Submit("deldir", "�폜", this, "onClickDeleteDir");
	
	/**
	 * �O���[�v�̃p�X���[�h�ύX�t�B�[���h
	 */
	private PasswordField password = new PasswordField("password"){
		
		public String toString(){
			UserInfo user = (UserInfo)super.context.getSession().getAttribute(ArUtil.USER);
			if(user == null){
				return "";
			}
			if(user.isAdmin()){
				return "";
			}
			return super.toString();
		}
	};
	
	/**
	 * �X�V�O�̃}�[�N�A�b�v���@
	 */
	private HiddenField exmarkup = new HiddenField("exmarkup", String.class);
	
	/**
	 * �O���[�v�̃p�X���[�h�ύX�{�^��
	 */
	private LoginSubmit changepass = 
		new LoginSubmit(
				"changepass", 
				"�p�X���[�h�ύX",
				this,
				"onClickChangePass",
				false);
		
	/**
	 * �X�[�p�[���[�U�̃��j���[
	 */
	private LoginSubmit manageuser = 
		new LoginSubmit(
				"manageuser", 
				"���[�U�Ǘ�", 
				this, 
				"onClickManageUser",
				true);
	
	/**
	 * 
	 */
	private LoginSubmit managesite = 
		new LoginSubmit(
				"managesite", 
				"�T�C�g�ݒ�", 
				this, 
				"onClickManageSite",
				true);
	
	
	/**
	 * ���O�A�E�g�{�^��
	 */
	private Submit logout = new Submit("logout", "���O�A�E�g", this, "onClickLogout");
	
	
	//---------------------------------------------------------------------------
	
	/**
	 * �f�B���N�g����I�����Ă��邩�ǂ���<br>
	 * true�̏ꍇ�A�ڍׂ�\������B
	 */
	public boolean isselect = false;

	/**
	 * �f�B���N�g���c���[�������ł������ǂ���
	 */
	public boolean istreeok = false;
	
	/**
	 * �f�B���N�g���c���[
	 */
	public Tree dirtree = new Tree("dirtree");
	
	
	//------------------------------------------------------------------------
	//�@�ڍׂ̃t�B�[���h
	//------------------------------------------------------------------------
	
	/**
	 * �f�B���N�g���̕�����
	 */
	private TextField dirpname = new TextField("dirpname");
	/**
	 * �f�B���N�g���̘_����
	 */
	private TextField dirlname = new TextField("dirlname");
	/**
	 * �f�B���N�g���̃I�[�i�[
	 */
	private TextField dirowner = new TextField("dirowner");
	/**
	 * �f�B���N�g���̕ҏW��
	 */
	private PickList  direditor = new PickList("direditor");
	
	/**
	 * �f�B���N�g����\�����邩�ǂ����̃`�F�b�N�{�b�N�X
	 */
	private Checkbox publication     = new Checkbox("publication");
	/**
	 * �f�B���N�g����\�����邩�ǂ����̃`�F�b�N�{�b�N�X
	 */
	private Checkbox isshow     = new Checkbox("isshow");
	/**
	 * �f�B���N�g���̕ҏW���@<br>
	 * html�Awiki�̓��\��
	 */
	private Select   editmeth   = new Select("editmeth");
	/**
	 * �z���f�B���N�g����\�����邩�ǂ���
	 */
	private Checkbox rendierchildren = new Checkbox("rendierchildren");
	/**
	 * �f�B���N�g���̐���
	 */
	private TextArea description = new TextArea("description");
	/**
	 * �ڍׂ̍X�V�{�^��
	 */
	private Submit okdetail = new Submit("okdetail", "�X�V", this, "onClickOkDetail");
	/**
	 * index.htm�̃A�b�v���[�h�i�t�@�C���t�B�[���h�j
	 */
	private FileField filefld = new FileField("filefld");
	/**
	 * index.htm�̃A�b�v���[�h�i�{�^���j
	 */
	private Submit uploadindex = new Submit("uploadindex", "�A�b�v���[�h", this, "onClickUpload");
	/**
	 * index.htm�̕ҏW�{�^��
	 */
	private Submit editindex = new Submit("editindex", "���e�ҏW", this, "onClickEditIndex");
	
	/**
	 * �t�@�C�����X�g�{�b�N�X
	 */
	private Select filelist = new Select("filelist");
	
	/**
	 * �t�@�C���R�s�[�{�^��
	 */
	private Submit filecopy = new Submit("filecopy", "�R�s�[", 				this, "onClickFileCopy");
	/**
	 * �f�B���N�g���R�s�[�{�^��
	 */
	private Submit dircopy = new Submit("dircopy", "�f�B���N�g���R�s�[", 		this, "onClickDirCopy");
	/**
	 * �R�s�[���̃��b�Z�[�W�G���A
	 */
	public String copymessage = "";
	/**
	 * �\��t���{�^��
	 */
	private Submit pasteobj = new Submit("pasteobj", "�\��t��", 				this, "onClickPaste");
	/**
	 * �t�@�C���폜�{�^��
	 */
	private Submit filedelete = new Submit("filedelete", "�폜", 				this, "onClickFileDelete");
	/**
	 * �t�@�C���ҏW�{�^��
	 */
	private Submit fileedit = new Submit("fileedit", "�ҏW", 					this, "onClickFileEdit");
	/**
	 * �t�@�C���_�E�����[�h�{�^��
	 */
	private Submit filedownload = new Submit("filedownload", "�_�E�����[�h", 	this, "onClickFileDownload");
	/**
	 * �t�@�C���Z���N�g�{�b�N�X
	 */
	private HiddenField targetfile = new HiddenField("selfile", String.class);
	/**
	 * ���l�[���{�^��
	 */
	private Submit filerename = new Submit("filerename", "���l�[��", this, "onClickFilrRename");
	/**
	 * �V�K�t�@�C���쐬�{�^��
	 */
	private Submit filemake   = new Submit("filemake", "�V�K�t�@�C��", this, "onClickFileMake");
	/**
	 * ���l�[�����A�V�K�t�@�C�������̓G���A
	 */
	private TextField inputfilename = new TextField("inputfilename"); 
	
	/**
	 * �R���X�g���N�^
	 *
	 */
	public ToolMainPage(){
		
		//�@�V�K�{�^��		
		form.add(newdir);
		//�@�V�K�f�B���N�g����
		form.add(newdirname);
		//�@���[�U�Ǘ�
		form.add(manageuser);
		//�@�p�X���[�h�e�L�X�g
		password.setSize(20);
		form.add(password);
		//�@�T�C�g�ݒ�{�^��
		form.add(managesite);
		//�@�p�X���[�h�ύX�{�^��
		form.add(changepass);
		//�@�폜�{�^��
		deldir.setAttribute("style", "color:blue;");
		deldir.setAttribute("onclick", "return confirm('�I�����ꂽ�f�B���N�g�����܂ށA�z���̃f�B���N�g�����폜���܂�')");
		form.add(deldir);
		
		//�@�f�B���N�g���c���[
		dirtree.addListener(this);
		
		//�@�ڍׂ̍X�V�{�^��
		form.add(okdetail);
		
		//�@���O�A�E�g
		form.add(logout);
		
		//------------------------------------------------
		//�@�ڍׂ̐ݒ�
		//------------------------------------------------
		
		//�@������
		dirpname.setAttribute("disabled", "true");
		dirpname.setSize(40);
		form.add(dirpname);
		//�@�_����
		dirlname.setSize(40);
		form.add(dirlname);
		//�@�f�B���N�g���̏��L��
		dirowner.setSize(40);
		form.add(dirowner);
		//�@�f�B���N�g���̕ҏW��
		form.add(direditor);
		//�@�f�B���N�g����\�����邩
		form.add(isshow);
		//�@���J���邩
		form.add(publication);
		//�@�ҏW���@
		editmeth.add(new Option("html"));
		editmeth.add(new Option("wiki"));
		form.add(editmeth);
		form.add(exmarkup);
		//�@�z����`�悷�邩
		form.add(rendierchildren);
		//�@�f�B���N�g���̐���
		description.setCols(40);
		description.setRows(5);
		form.add(description);
		//�@index�A�b�v���[�h�i�t�B�[���h�j
		filefld.setAttribute("style", "width:400px;");
		form.add(filefld);
		//�@index�A�b�v���[�h�i�{�^���j
		form.add(uploadindex);
		//  index�ҏW
		form.add(editindex);
		//�@�t�@�C�����X�g�{�b�N�X
		filelist.setSize(7);
		filelist.setAttribute("style", "width:200px;");
		form.add(filelist);
		
		//�@�R�s�[
		filecopy.setAttribute("style", "width:80px");
		filecopy.setOnClick("form.selfile.value=form.filelist.value");
		form.add(filecopy);
		//�@�f�B���N�g���R�s�[
		dircopy.setAttribute("style", "width:80px");
		dircopy.setOnClick("form.selfile.value=form.filelist.value");
		form.add(dircopy);
		//�@�\��t��
		pasteobj.setAttribute("style", "width:80px");
		pasteobj.setOnClick("form.selfile.value=form.filelist.value");
		form.add(pasteobj);
		//�@�_�E�����[�h
		filedownload.setAttribute("style", "width:80px");
		filedownload.setOnClick("form.selfile.value=form.filelist.value");
		form.add(filedownload);
		//�@�폜
		filedelete.setAttribute("style", "width:80px");
		filedelete.setOnClick("form.selfile.value=form.filelist.value");
		form.add(filedelete);
		//�@�ҏW
		fileedit.setAttribute("style", "width:80px");
		fileedit.setOnClick("form.selfile.value=form.filelist.value");
		form.add(fileedit);
		//�@�I�y���[�V�����Ώۃt�@�C��
		form.add(targetfile);

		//�@���l�[���{�^��
		filerename.setAttribute("style", "width:80px");
		filerename.setOnClick("form.selfile.value=form.filelist.value");
		form.add(filerename);
		//�@�V�K�t�@�C���쐬�{�^��
		filemake.setAttribute("style", "width:80px");
		filemake.setOnClick("form.selfile.value=form.filelist.value");
		form.add(filemake);
		//�@�t�@�C������
		form.add(inputfilename);
		
	}

	
	/**
	 * ����������
	 */
	public void onInit(){
	
		buildTree();
		
		
	}
	

	/**
	 * �`��O����
	 */
	public void onRender(){
		
		super.onRender();
		
		//�@�f�B���N�g�����I������Ă��Ȃ��ꍇ�́A�{�^�����g�p�s�Ƃ���
		List selectednodes = dirtree.getSelectedNodes(true);
		if(selectednodes.isEmpty()){
			this.newdir.setAttribute("disabled", "true");
			this.deldir.setAttribute("disabled", "true");
			
		}
		//�@�Ȃɂ�����I����Ԃ̏ꍇ�́A�f�B���N�g���̐ݒ��\��
		else{
			
			UserInfo user = (UserInfo)super.context.getSession().getAttribute(ArUtil.USER);
			
			isselect = true;
			TreeNode selected = (TreeNode)selectednodes.get(0);
			
			//�@�y�[�W�v���p�e�B���擾
			String path = super.context.getServletContext().getRealPath("");
			path = path + "/" + selected.getId();
			PageProperties prop = ArUtil.getPageProperties(path);
			
			//�@�蓮�ŏ����Ă���ꍇ������
			if(!new File(path).exists()){
				addMessage("�I�����ꂽ�f�B���N�g���͍폜����Ă��܂��B");
				super.context.getSession().setAttribute("FORCE_BUILD_TREE", "FORCE_BUILD_TREE");
				buildTree();
				return;
			}
			
			//�@�f�B���N�g��������
			this.dirpname.setValue((String)selected.getValue());
			//�@�f�B���N�g���_����
			this.dirlname.setValue(prop.getTitle());
			//�@���j���[�\��
			this.isshow.setChecked(prop.isVisible());
			//�@���J
			this.publication.setChecked(prop.isPublic());
			//�@�}�[�N�A�b�v
			this.editmeth.setValue(prop.getMarkup());
			this.exmarkup.setValue(prop.getMarkup());
			//�@�z����`�悷�邩
			this.rendierchildren.setChecked(prop.isRenderChild());
			//�@����
			String desc = "";
			for(int i = 0 ; i < prop.getDescs().size(); i++){
				desc = desc + prop.getDescs().get(i) + "\n";
			}
			this.description.setValue(desc);
			
			//�@�t�@�C�����X�g
			File dir = new File(path);
			File[] files = dir.listFiles();
			for(int i = 0 ; i < files.length; i++){
				if(files[i].isFile()) filelist.add(files[i].getName());
			}
			
			//�@�R�s�[��Ԃ�\��
			String copytype   = (String)super.context.getSession().getAttribute("COPY_TYPE");
			String copytarget = (String)super.context.getSession().getAttribute("COPY_TARGET");
			if(copytype == null){
				pasteobj.setAttribute("disabled", "true");
				copymessage = "";
			}
			else{
				copymessage = "[" + copytarget + "]���\��t���ł��܂��B";
			}

			if(!user.isAdmin()){
				okdetail.setAttribute("disabled", "true");
			}

		
		}

	
	
	}
	
	
	/**
	 * �f�B���N�g���c���[���\������<br>
	 * ���̃��\�b�h�́A���X�i��ݒ肵�Ă��邽�߁A�K��onInit()�ȑO��
	 * �g�p���邱��
	 *
	 */
	private void buildTree(){

        TreeNode existingRootNode = 
        	(TreeNode)super.context.getSession().getAttribute(TOOL_TREE);
        if(super.context.getSession().getAttribute("FORCE_BUILD_TREE")== null 
        		&& existingRootNode != null ) {
        	
    			dirtree.setRootNode(existingRootNode);
    	        dirtree.setRootNodeDisplayed(true);
    	        istreeok = true;
                return;
        	
        }

		super.context.getSession().removeAttribute("FORCE_BUILD_TREE");
		
        //�@���[�g�p�X���擾
        String docroot = ArUtil.getProperty(this, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_DOCROOT);
        if(!docroot.startsWith("/")){
        	docroot = "/" + docroot;
        }
        String rootpath = context.getServletContext().getRealPath(docroot);
        String absrootpath = rootpath.substring(0,rootpath.length() - docroot.length());
        
        //�@���[�g�p�X�����݂��Ȃ���ΏI��
        File rootpathfile = new File(rootpath);
        if(!rootpathfile.exists()){
        	return;
        }
        
        //�@�c���[�𐶐�
    	istreeok = true;
        
        PageProperties prop = ArUtil.getPageProperties(rootpath);
        String title = rootpath.substring(absrootpath.length());
        if(title.startsWith("\\")){
            title = "/" + title.substring(1);
        }
        
        if(prop.isPublic()){
        	title = title + " [+";
        }
        else{
        	title = title + " [-";
        }
        if(prop.isVisible()){
        	title = title + "+";
        }
        else{
        	title = title + "-";
        }
        if(prop.getMarkup().equals(PageProperties.MARKUP_HTML)){
        	title = title + "h";
        }
        else{
        	title = title + "w";
        }
        if(prop.isRenderChild()){
        	title = title + "+]";
        }
        else{
        	title = title + "-]";
        }
        
        
        
        
        ArTreeNode rootnode = new ArTreeNode(title,docroot);
        rootnode.setIsVisible(prop.isVisible());
        rootnode.setIsLink(prop.isIslink());
        rootnode.setIsRenderChild(prop.isRenderChild());
        
        //�ċA�I�Ƀf�B���N�g���\�����Z�b�g����
        this.setTree(rootnode, rootpath, absrootpath);

        //�@�c���[���̂̐ݒ�
        dirtree.setRootNode(rootnode);
        dirtree.expandAll();
        dirtree.setRootNodeDisplayed(true);
        if(existingRootNode != null){
        	
        	List selectedlist = dirtree.getSelectedNodes(true);
        	if(!selectedlist.isEmpty()){
        		
        		TreeNode selected = (TreeNode)selectedlist.get(0);
        		dirtree.select(selected.getId());
        		
        	}
        	
        }
        
        
        //�@���̃c���[���Z�b�V�����ɕۑ�
        getContext().getSession().setAttribute(TOOL_TREE, rootnode);

	
	}
	
	/**
	 * �ċA�I�Ƀh�L�������g���[�g���猟�����s���A�f�B���N�g���c���[�ɕ\��
	 * @param node
	 * @param path
	 * @param rootpath
	 */
	private void setTree(ArTreeNode node, String path, String rootpath){
		
		
		File file = new File(path);
		String[] files = file.list();
		
		for(int i = 0 ; i < files.length; i++){
			
			String abspath = path + "/" + files[i];
			File wkfile = new File(abspath);
			if(wkfile.isDirectory()){
				
				PageProperties prop = ArUtil.getPageProperties( abspath);
				
				String dirname =  abspath.substring(abspath.lastIndexOf("/"));
				
				//�@���̃t�H���_���m�[�h������
				String id = abspath.substring(rootpath.length()+1);
				if(id.startsWith("\\")){
					id = "/" + id.substring(1);
				}
				
				if(id.equals("/.settings")){
					continue;
				}
				if(id.equals("/work")){
					continue;
				}
				if(id.equals("/WEB-INF")){
					continue;
				}
				if(id.equals("/click")){
					continue;
				}
				if(id.equals("/tools")){
					continue;
				}

		        if(prop.isPublic()){
		        	dirname = dirname + " [+";
		        }
		        else{
		        	dirname = dirname + " [-";
		        }
		        if(prop.isVisible()){
		        	dirname = dirname + "+";
		        }
		        else{
		        	dirname = dirname + "-";
		        }
		        if(prop.getMarkup().equals(PageProperties.MARKUP_HTML)){
		        	dirname = dirname + "h";
		        }
		        else{
		        	dirname = dirname + "w";
		        }
		        if(prop.isRenderChild()){
		        	dirname = dirname + "+]";
		        }
		        else{
		        	dirname = dirname + "-]";
		        }
		        
				
				
				ArTreeNode child = 
					new ArTreeNode(dirname, id, node);

				child.setIsVisible(prop.isVisible());
				child.setIsLink(prop.isIslink());
				child.setIsRenderChild(prop.isRenderChild());
				
				setTree(child, abspath, rootpath);
						
			}
		}
		
	}

	/**
	 * �V�K�f�B���N�g���쐬
	 * @return 
	 */
	public boolean onClickNewDir(){
		
		String dirname = newdirname.getValue();
		dirname = dirname.trim();
		if(dirname.equals("")){
			addMessage("�f�B���N�g��������͂��Ă��������B");
			return true;
		}
		
		//�@�t�@�C���֑������̃`�F�b�N
		//�@�@�EWindows�@\/:;*?"<>
		//�@�@�EUnix�@�@ /�ȊO��OK
		//  �⑫�F
		//�@�@�@OS�̔��f�����Ă��܂��ƁAWeb�T�[�o�̍ڂ��Ă���OS
		//�@�@�@���o���Ă��܂��̂ŁAWindows�ɂ��킹��B
		String checkstr = "\\/:;*?\"<>";
		for(int i = 0 ; i < checkstr.length(); i++){
			if(dirname.indexOf(checkstr.substring(i,i+1))!=-1){
				addMessage("�f�B���N�g���Ɏg�p�ł��Ȃ�����������܂��B");
				return true;
			}
		}
		
	
		//�@�t�@�C���֑������̃`�F�b�N
		//�@�@�E�����Ȃǂ͍��邯�ǁATree��őI���ł��Ȃ��Ȃ�̂�
        byte[] bytes = dirname.getBytes();
        int beams = dirname.length();
        if (beams != bytes.length) {
        	addMessage("�S�p�͎g�p�ł��܂���B");
        	return true;
        }
	
		
		//�@�f�B���N�g�����I������Ă��Ȃ��ꍇ������̂�
		List selected = dirtree.getSelectedNodes(true);
		if(selected.isEmpty()){
			addMessage("�e�ƂȂ�f�B���N�g�����I������Ă��܂���B");
			return true;
		}
		
		
		//�@�쐬������f�B���N�g���p�X��ҏW
		TreeNode node = (TreeNode)selected.get(0);
		String abspath = super.context.getServletContext().getRealPath("");
		String makedir = abspath + "/" + node.getId() + "/" + dirname;
		 
		
		//�@���ɑ��݂���f�B���N�g���̏ꍇ�G���[
		File file = new File(makedir);
		if(file.exists()){
			addMessage("���Ƀf�B���N�g�����쐬����Ă��܂��B");
			return true;
		}
		
		//�@�����I�Ƀf�B���N�g�����쐬
		try {
			FileUtils.forceMkdir(file);
		} catch (IOException e) {
			addMessage("�f�B���N�g���̍쐬�Ɏ��s���܂����B" + e.getMessage());
			return true;
		}
		
		//�@�⑫�F������index.htm,wiki.txt�����ׂ����H
		//�@�@�@���R�V�[�P���X�͐V�K�f�B���N�g��������Ă���
		//�@�@�@�}�[�N�A�b�v��ύX���ē��e�̕ҏW�ƂȂ�̂�
		//�@�@�@�R�R�ō����̂�html�݂̂ɂȂ�B�A���A���[�U
		//�@�@�@�ɂ���ẮA�f�B���N�g��������Ă����Ƀy�[�W��
		//�@�@�@�\������ꍇ������̂ŁA�ꉞ�����ł����
		
		//�@index.htm,wiki.txt�����
		if(editmeth.getValue().equals("html")){
			List lines = new ArrayList();
			lines.add("<br>");
			lines.add("<hr>");
			lines.add("<h1>���̓��e��ύX���Ă��������B</h1>");
			lines.add("<hr>");
			ArUtil.writeFile(makedir + "/index.htm", lines);
		}
		else{
			List lines = new ArrayList();
			lines.add("$wikicontents");
			ArUtil.writeFile(makedir + "/wiki.txt", lines);
			
			List wikilines = new ArrayList();
			wikilines.add("--");
			wikilines.add("���̓��e��ύX���Ă��������B");
			wikilines.add("--");
			ArUtil.writeFile(makedir + "/index.htm", wikilines);
			
		}
		
		//�@�Z�b�V�����o�R�Ńf�B���N�g���c���[���č쐬������B
		super.context.getSession().setAttribute("FORCE_BUILD_TREE", "FORCE_BUILD_TREE");
		setLastUpdatedTime();
		
		buildTree();
		
		return true;
		
	}
	
	/**
	 * ���[�U�Ǘ�����
	 * @return
	 */
	public boolean onClickManageUser(){
		
		super.setForward(ManageUserPage.class);
		return false;

	}
	
	/**
	 * �폜�{�^������
	 * @return
	 */
	public boolean onClickDeleteDir(){
		
		//�@�f�B���N�g�����I������Ă��Ȃ��ꍇ
		List selected = dirtree.getSelectedNodes(true);
		if(selected.isEmpty()){
			addMessage("�폜����f�B���N�g�����I������Ă��܂���B");
			return true;
		}
		
		//�@�폜����f�B���N�g���̎擾
		TreeNode node = (TreeNode)selected.get(0);
		String abspath = super.context.getServletContext().getRealPath("");
		String deldir = abspath + node.getId() ;
		System.out.println( deldir + "�@���폜���܂���");
		
		//�@�폜�Ώۃf�B���N�g���̑��݃`�F�b�N
		File file = new File(deldir);
		if(!file.exists()){
			addMessage("���Ƀf�B���N�g�����폜����Ă��܂����B");
			//�@�폜�������f�B���N�g����N�����폜�����񂾂���ǂ����Č����Ηǂ��񂾂��ǁE�E�E
			return true;
		}

		try {
			//�@�����I�Ƀf�B���N�g�����폜�i�ċA�I�ɍ폜���܂��j
			FileUtils.forceDelete(file);
		} catch (IOException e) {
			addMessage("�f�B���N�g���̍폜�Ɏ��s���܂����B" + e.getMessage());
			return true;
		}

		super.context.getSession().setAttribute("FORCE_BUILD_TREE", "FORCE_BUILD_TREE");
		setLastUpdatedTime();
		buildTree();
		
		return true;
	}

	/**
	 * ���O�A�E�g�{�^������
	 * @return
	 */
	public boolean onClickLogout(){
		
		super.context.getSession().removeAttribute(ArUtil.USER);
		super.context.getSession().invalidate();
		
		super.setRedirect(LoginPage.class);
		return false;
		
	}

	
	//-----------------------------------------------------------------------------
	
	
	public void nodeCollapsed(Tree tree, TreeNode node, Context context, boolean oldValue) {
	}


	public void nodeDeselected(Tree tree, TreeNode node, Context context, boolean oldValue) {
	}


	public void nodeExpanded(Tree tree, TreeNode node, Context context, boolean oldValue) {
	}

	/**
	 * �f�B���N�g���c���[�̃m�[�h���N���b�N���ꂽ�Ƃ�<br>
	 * �f�B���N�g���c���[�̂����P�̃m�[�h�݂̂��I�������悤�ɐ��䂵�܂��B
	 */
	public void nodeSelected(Tree tree, TreeNode node, Context context, boolean oldValue) {

		System.out.println("--------------------------------");
		System.out.println(node);
		System.out.println(oldValue);
		
		
        List selected = tree.getSelectedNodes(false);
        for(int i = 0 ; i < selected.size(); i++){
            TreeNode selnode = (TreeNode)selected.get(i);
            if(selnode != node){
                tree.deselect(selnode);
            }
        }
        
        isselect = true;
	
	}

	
	/**
	 * ���e�X�V�{�^������
	 * @return
	 */
	public boolean onClickOkDetail() {

		if(dirlname.getValue().trim().equals("")){
			addMessage("�f�B���N�g���̘_���������͂���Ă��܂���B");
			return true;
		}
		
		// TODO DOM�ŕۑ�����悤�ɂ���
		// TODO ���[�e�B���e�B������
		
		//�@�ۑ�������e�̕ҏW
		List lines = new ArrayList();
		lines.add("<?xml version=\"1.0\" encoding=\"Shift_JIS\" ?>" + "\n");
		String page = "<page ";
		page = page + "visible=\"" + this.isshow.isChecked() + "\" ";
		page = page + "markup=\"" + this.editmeth.getValue() + "\" ";
		page = page + "renderchild=\"" + this.rendierchildren.isChecked() + "\" ";
		page = page + "public=\"" + this.publication.isChecked() + "\" ";
		page = page + ">";
		lines.add(page + "\n");
		
		String title = "<title>" + this.dirlname.getValue() + "</title>";
		lines.add(title + "\n");
		
		String descs = description.getValue();
		descs = descs.replaceAll("\r", "");
		StringTokenizer tokens = new StringTokenizer(descs,"\n");
		while(tokens.hasMoreTokens()){
			String token = tokens.nextToken();
			String desc = "<desc>" + token + "</desc>";
			lines.add(desc + "\n");
		}
		lines.add("</page>");
		
		//�@�ۑ�����t�@�C���̓���
        List selected = dirtree.getSelectedNodes(false);
        if(selected.isEmpty()){
        	addMessage("�X�V���ׂ��f�B���N�g����������Ȃ��Ȃ����I");
        	return true;
        }
        TreeNode node = (TreeNode)selected.get(0);
 
        String path = super.context.getServletContext().getRealPath("");
        String abspath = path + "/" + node.getId();
        String xmlpath = abspath + "/page.xml";
        
        //�@���̂悤�ȃC���[�W�ōX�V�����
		//<//page visible="true" link="true" markup="html" renderchild="true" >
	    //	<title>�g�b�v�y�[�W</title>
	    //	<desc>�͂��߂�</desc>
	    //	<desc>���ɂ̃N���b�N�����N�W</desc>
	    //	<desc>�X�V����</desc>
	    //	<desc>���̑�</desc>
		//</page>
        ArUtil.writeFile(xmlpath, lines);
		
        //�@����������悤�ɂ���
        if(!this.exmarkup.getValue().equals(this.editmeth.getValue())){

        	//�}�[�N�A�b�v�̍X�V
        	this.exmarkup.setValue(this.editmeth.getValue());
        	
			Date date = new Date();
			SimpleDateFormat f = new SimpleDateFormat("yyyyMMddkkmmssSSS");
			String ymd = f.format(date);

			//�@index.htm,wiki.txt�����
    		if(editmeth.getValue().equals("html")){

    			// �o�b�N�A�b�v����� yyyymmddhhmmssSSS_wiki.txt
    			//                    yyyymmddhhmmssSSS_index.htm
    			File exindexwiki = new File(abspath + "/wiki.txt");
    			//File exindexhtml = new File(abspath + "/index.htm");
    			if(exindexwiki.exists()){
    				
        			try {
    					FileUtils.copyFile(exindexwiki,	new File(abspath + "/" + ymd + "_wiki.txt"));
    					//FileUtils.copyFile(exindexhtml,	new File(abspath + "/" + ymd + "_index.htm"));
    							
    				} catch (IOException e) {
    					throw new RuntimeException("�t�@�C���R�s�[���s");
    				}
    				
    			}
    			

    			List lineshtml = new ArrayList();
    			lineshtml.add("<hr>");
    			lineshtml.add("<h1>���̓��e��ύX���Ă��������B</h1>");
    			lineshtml.add("<h2>�}�[�N�A�b�v��html�ł�</h2>");
    			lineshtml.add("<hr>");
    			
    			ArUtil.writeFile(abspath + "/index.htm", lineshtml);

    		}
    		else{
    			

    			// �o�b�N�A�b�v����� yyyymmddhhmmssSSS_index.htm
    			File exindex = new File(abspath + "/index.htm");
    			if(exindex.exists()){
    				
        			try {
    					FileUtils.copyFile(exindex,	new File(abspath + "/" + ymd + "_index.htm")
    							);
    				} catch (IOException e) {
    					throw new RuntimeException("�t�@�C���R�s�[���s");
    				}
    				
    			}

    			
    			List lineshtml = new ArrayList();
    			lineshtml.add("$wikicontents");
    			ArUtil.writeFile(abspath + "/index.htm", lineshtml);
    			
    			List lineswiki = new ArrayList();
    			lineswiki.add("----\n");
    			lineswiki.add("!���̓��e��ύX���Ă��������B\n");
    			lineswiki.add("!!�}�[�N�A�b�v��wiki�ł�\n");
    			lineswiki.add("----\n");
    			
    			ArUtil.writeFile(abspath + "/wiki.txt", lineswiki);
    			
    		}

        }
        
        addMessage("�ݒ���X�V���܂����B");
		isselect = true;
		super.context.getSession().setAttribute("FORCE_BUILD_TREE", "FORCE_BUILD_TREE");
		buildTree();
		setLastUpdatedTime();

		return true;
	
	}

	/**
	 * ���e�ҏW�{�^��
	 * @return
	 */
	public boolean onClickEditIndex(){
		
        List selectednode = dirtree.getSelectedNodes(true);
        TreeNode node = (TreeNode)selectednode.get(0);
        
        super.context.setRequestAttribute("EDIT_PATH", node.getId());
		
		super.setForward(EditIndexPage.class);
		return false;
		
	}
	
	/**
	 * �T�C�g�Ǘ��{�^��
	 * @return
	 */
	public boolean onClickManageSite(){
		
		super.setForward(ManageSitePage.class);
		return false;
		
	}
	
	/**
	 * �A�b�v���[�h�{�^��
	 * @return
	 */
	public boolean onClickUpload(){
		
		FileItem item = filefld.getFileItem();
		if(item.getSize() == 0){
			addMessage("�A�b�v���[�h����t�@�C�����I������Ă��܂���B");
			return true;
		}
		
		
		InputStream inst = null;
		try {
			inst = item.getInputStream();
		} catch (IOException e) {
			addMessage("�t�@�C���̃A�b�v���[�h���ɃG���[�������܂����B" + e.getMessage());
			return true;
		}

		//�@�ǂ̃f�B���N�g���ɃA�b�v���[�h���邩
        List selected = dirtree.getSelectedNodes(false);
        TreeNode node = (TreeNode)selected.get(0);
        
        //�@�f�B���N�g���ւ̐�΃p�X��ҏW
        String path = super.context.getServletContext().getRealPath("");
        path = path + "/" + node.getId() + "/";
        
        //�@�t�@�C�����̂̕ҏW
        String filename = item.getName();
        String filesep = ArUtil.getClientFileSeparator(this);
        filename = filename.substring(filename.lastIndexOf(filesep)+1);
        path = path + filename;
        
        //�@�A�b�v���[�h����
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(path);
			byte[] b = new byte[2048];
			while((inst.read(b,0,2048)) != -1){
				fos.write(b,0 ,2048);
			}
			
		} catch (Exception e) {
			addMessage("�t�@�C���̃A�b�v���[�h���ɃG���[�������܂����B" + e.getMessage());
			return true;
		}
		finally{
			try {
				if(fos != null)fos.close();
			} catch (IOException e) {
				addMessage("�t�@�C���̃N���[�Y���ɃG���[�������܂����B" + e.getMessage());
				return true;
			}
		}
		
		addMessage("�t�@�C���u"+ item.getName() +"�v���A�b�v���[�h���܂����B");
		return true;
		
	}

	/**
	 * �ŏI�X�V����<br>
	 * ���̏����͍ŏI�I�Ɏg�p����Ȃ��Ȃ�\��
	 *
	 */
	private void setLastUpdatedTime(){
		
        String lastpath = super.context.getServletContext().getRealPath("/");
        lastpath = lastpath + ArUtil.DIR_CONF + "lastupdated.txt";

        Date now = new Date();
        List lines = new ArrayList();
        lines.add(now.toString());
        
        ArUtil.writeFile(lastpath, lines);
        
	}
	
	/**
	 * �t�@�C���ҏW�{�^��
	 * @return
	 */
	public boolean onClickFileEdit(){
		
		String selfile = targetfile.getValue();
		if(selfile.equals("")){
			addMessage("�t�@�C�����I������Ă��܂���B");
			return true;
		}
		if(selfile.endsWith("_index.htm")||
		   selfile.endsWith("_wiki.txt")){
			addMessage("�����t�@�C���͕ҏW�ł��܂���B");
			return true;
		}
		if(selfile.endsWith("index.htm")){
			return onClickEditIndex();
		}
		
		//�@�f�B���N�g�����I������Ă���ꍇ�̂�
		List selectednodes = dirtree.getSelectedNodes(true);
		if(!selectednodes.isEmpty()){
			
			TreeNode node = (TreeNode)selectednodes.get(0);
			
			//�@�ҏW��ʂɑJ��
			EditFilePage page = (EditFilePage)super.context.createPage(EditFilePage.class);
			page.setParameter(node.getId() + "/" + selfile, this);
			super.setForward(page);
		}
		else{
			addMessage("�ҏW���ׂ��t�@�C����������Ȃ��Ȃ����I�I");
		}
		
		return true;
	}


	/**
	 * �t�@�C���폜����
	 * @return
	 */
	public boolean onClickFileDelete(){
		
		String selfile = targetfile.getValue();
		if(selfile.equals("")){
			addMessage("�t�@�C�����I������Ă��܂���B");
			return true;
		}
		
		//�@�f�B���N�g�����I������Ă���ꍇ�̂�
		List selectednodes = dirtree.getSelectedNodes(true);
		if(!selectednodes.isEmpty()){
			
			TreeNode node = (TreeNode)selectednodes.get(0);
			String abspath = super.context.getServletContext().getRealPath("");
			abspath = abspath + node.getId() + "/" + selfile;

			try {
				FileUtils.forceDelete(new File(abspath));
			} catch (IOException e) {
				throw new RuntimeException("�t�@�C���폜�Ɏ��s " + abspath , e);
			}
			
			addMessage("[" + node.getId() + "/" + selfile + "]�̍폜���s���܂����B");
		}
		else{
			addMessage("�ҏW���ׂ��t�@�C����������Ȃ��Ȃ����I�I");
		}
		
		return true;
	}


	/**
	 * �t�@�C���_�E�����[�h����
	 * @return
	 */
	public boolean onClickFileDownload(){

		//�@�t�@�C�����I������Ă��邩
		String selfile = targetfile.getValue();
		if(selfile.equals("")){
			addMessage("�t�@�C�����I������Ă��܂���B");
			return true;
		}
		
		//�@�f�B���N�g�����I������Ă���ꍇ�̂�
		List selectednodes = dirtree.getSelectedNodes(true);
		if(selectednodes.isEmpty()){
			addMessage("�ҏW���ׂ��t�@�C����������Ȃ��Ȃ����I�I");
			
		}
		
		//�@�_�E�����[�h�p�X�̕ҏW
		TreeNode node = (TreeNode)selectednodes.get(0);
		String abspath = super.context.getServletContext().getRealPath("");
		abspath = abspath + node.getId() + "/" + selfile;

		//�@�_�E�����[�h��ʂɏ������Ϗ�
		super.context.getRequest().setAttribute("DLPATH", abspath);
		super.setForward(DownloadPage.class);

		return true;
	
	}

	

	/**
	 * �t�@�C���R�s�[����
	 * @return
	 */
	public boolean onClickFileCopy(){
		
		String selfile = targetfile.getValue();
		if(selfile.equals("")){
			addMessage("�t�@�C�����I������Ă��܂���B");
			return true;
		}
		
		//�@�f�B���N�g�����I������Ă���ꍇ�̂�
		List selectednodes = dirtree.getSelectedNodes(true);
		if(!selectednodes.isEmpty()){
			
			TreeNode node = (TreeNode)selectednodes.get(0);

			super.context.getSession().setAttribute("COPY_TYPE", "FILE");
			super.context.getSession().setAttribute("COPY_TARGET", node.getId() + "/" + selfile);
		
		
		}
		else{
			addMessage("�R�s�[���ׂ��t�@�C����������Ȃ��Ȃ����I�I");
		}
		
		return true;
	}
	
	/**
	 * �f�B���N�g���R�s�[����
	 * @return
	 */
	public boolean onClickDirCopy(){
		
		//�@�f�B���N�g�����I������Ă���ꍇ�̂�
		List selectednodes = dirtree.getSelectedNodes(true);
		if(!selectednodes.isEmpty()){
			
			TreeNode node = (TreeNode)selectednodes.get(0);

			super.context.getSession().setAttribute("COPY_TYPE", "DIR");
			super.context.getSession().setAttribute("COPY_TARGET", node.getId() );
		
		}
		else{
			addMessage("�R�s�[���ׂ��f�B���N�g�����킩��Ȃ��Ȃ����I�I");
		}
		
		return true;
	}
	
	/**
	 * �\��t������
	 * @return
	 */
	public boolean onClickPaste(){
		
		String type   = (String)super.context.getSession().getAttribute("COPY_TYPE");
		String target = (String)super.context.getSession().getAttribute("COPY_TARGET");
	
		if(type == null){
			addMessage("�R�s�[�����ʂ�������܂���@" + type);
			return true;
		}
		
		if(type.equals("FILE")){
			return pasteFile();
		}
		else if(type.equals("DIR")){
			
			boolean ret = pasteDir();
			
			super.context.getSession().setAttribute("FORCE_BUILD_TREE", "FORCE_BUILD_TREE");
			buildTree();
			setLastUpdatedTime();
				
			addMessage(target + " ��\��t���܂����B");
			
			return ret;
		}
		else{
			addMessage("�R�s�[����Ώۂ�������܂���@" + target);
			return true;
		}
		
	}
	
	/**
	 * �\��t���i�t�@�C���j
	 * @return
	 */
	private boolean pasteFile(){
		
		String expath = (String)super.context.getSession().getAttribute("COPY_TARGET");
		expath = super.context.getServletContext().getRealPath("") + expath;
		
		File extarget = new File(expath);
		if(!extarget.exists()){
			addMessage("�R�s�[���t�@�C���͊��ɍ폜���ꂽ�悤�ł��B" + expath);
			return true;
		}
		
		List selectednodes = dirtree.getSelectedNodes(true);
		if(selectednodes.isEmpty()){
			addMessage("�Ώۃf�B���N�g����������Ȃ��Ȃ����B");
			return true;
		}
		
		TreeNode node = (TreeNode)selectednodes.get(0);
		
		String topath = super.context.getServletContext().getRealPath("");
		topath = topath + node.getId();
		
		File todir = new File(topath);
		if(!todir.exists()){
			addMessage("�R�s�[��̃f�B���N�g���͊��ɂ���܂��� " + topath);
			return true;
		}
		
		String exabspath = extarget.getAbsolutePath();
		exabspath = exabspath.substring(0, exabspath.lastIndexOf(ArUtil.SV_FILE_SEPARATOR));
		
		if(exabspath.equals(todir.getAbsolutePath())){
			addMessage("�R�s�[�悪�R�s�[���t�@�C���Ɠ������̂ł��B");
			return true;
		}
		
		try {
			FileUtils.copyFileToDirectory(extarget, todir);
		} catch (IOException e) {
			throw new RuntimeException("�t�@�C���R�s�[�Ɏ��s", e);
		}
		
		addMessage("�t�@�C�����R�s�[����܂����B");
		return true;

	}
	
	/**
	 * �\��t���i�f�B���N�g���j
	 * @return
	 */
	private boolean pasteDir(){
	
		String expath = (String)super.context.getSession().getAttribute("COPY_TARGET");
		expath = super.context.getServletContext().getRealPath("") + expath;
		
		File extarget = new File(expath);
		if(!extarget.exists()){
			addMessage("�R�s�[���f�B���N�g���͊��ɍ폜���ꂽ�悤�ł��B" + expath);
			return true;
		}
		
		List selectednodes = dirtree.getSelectedNodes(true);
		if(selectednodes.isEmpty()){
			addMessage("�Ώۃf�B���N�g����������Ȃ��Ȃ����B");
			return true;
		}
		
		TreeNode node = (TreeNode)selectednodes.get(0);
		
		String topath = super.context.getServletContext().getRealPath("");
		topath = topath + node.getId();
		
		File todir = new File(topath);
		if(!todir.exists()){
			addMessage("�R�s�[��̃f�B���N�g���͊��ɂ���܂��� " + topath);
			return true;
		}
		
		if(extarget.equals(todir)){
			addMessage("�R�s�[�悪����f�B���N�g���ł��B");
			return true;
		}
		
		
		try {
			FileUtils.copyDirectoryToDirectory(extarget, todir);
		} catch (IOException e) {
			throw new RuntimeException("�f�B���N�g�����ƃR�s�[�Ɏ��s", e);
		}
		
		addMessage("�f�B���N�g�����R�s�[����܂����B");
		return true;

	}
	
	

	public boolean onClickFilrRename(){
		
		String selfile = targetfile.getValue();
		if(selfile.equals("")){
			addMessage("�t�@�C�����I������Ă��܂���B");
			return true;
		}

		String filename = inputfilename.getValue();
		if(filename.equals("")){
			addMessage("�t�@�C���������͂���Ă��܂����B");
			return true;
		}
		
		//�@�f�B���N�g�����I������Ă��Ȃ��ꍇ������̂�
		List selected = dirtree.getSelectedNodes(true);
		if(selected.isEmpty()){
			addMessage("�f�B���N�g�����킩��Ȃ��Ȃ����B");
			return true;
		}
		
		
		//�@�ύX������f�B���N�g���p�X��ҏW
		TreeNode node = (TreeNode)selected.get(0);
		String abspath = super.context.getServletContext().getRealPath("");
		String renamepath = abspath + "/" + node.getId() + "/";

		//�@���ɂ��閼�O�Ȃ�ʖ�
		if(new File(renamepath + filename).exists()){
			addMessage("���ɑ��݂���f�B���N�g���܂��̓t�@�C�����ł��B");
			return true;
		}
		
		File file = new File(renamepath + selfile); 
		file.renameTo(new File(renamepath + filename));
		
		addMessage(node.getId() + "/" + selfile +" �� " + node.getId() + "/" + filename + " �ɕύX���܂����B");
		
		return true;
		
	}
	

	public boolean onClickFileMake(){
		
		String filename = inputfilename.getValue();
		if(filename.equals("")){
			addMessage("�t�@�C���������͂���Ă��܂����B");
			return true;
		}
		
		//�@�f�B���N�g�����I������Ă��Ȃ��ꍇ������̂�
		List selected = dirtree.getSelectedNodes(true);
		if(selected.isEmpty()){
			addMessage("�f�B���N�g�����킩��Ȃ��Ȃ����B");
			return true;
		}
		
		
		//�@�ύX������f�B���N�g���p�X��ҏW
		TreeNode node = (TreeNode)selected.get(0);
		String abspath = super.context.getServletContext().getRealPath("");
		String renamepath = abspath + "/" + node.getId() + "/";

		//�@���ɂ��閼�O�Ȃ�ʖ�
		if(new File(renamepath + filename).exists()){
			addMessage("���ɑ��݂���f�B���N�g���܂��̓t�@�C�����ł��B");
			return true;
		}
		
		File file = new File(renamepath + filename); 
		try {
			FileUtils.writeStringToFile(file, "");
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("�t�@�C���V�K�쐬�Ɏ��s",e);
		}
		
		addMessage(node.getId() + "/" + filename + " ���쐬���܂����B");

		return true;
		
	}



}
