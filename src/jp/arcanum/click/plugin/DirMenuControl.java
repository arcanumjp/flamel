package jp.arcanum.click.plugin;

import java.io.File;
import java.io.FilenameFilter;
import java.util.List;
import java.util.Map;

import jp.arcanum.click.ArUtil;
import jp.arcanum.click.PageProperties;
import jp.arcanum.click.PluginInterface;
import jp.arcanum.click.pages.controls.ArTreeNode;
import net.sf.click.Page;
import net.sf.click.extras.tree.Tree;
import net.sf.click.extras.tree.TreeNode;
import net.sf.click.util.HtmlStringBuffer;


/**
 * �f�B���N�g�����̋L�������炸����ׂ�@�\�B
 * Yahoo�̃g�b�v�y�[�W�̂悤�ȕ����C���[�W����
 * ��������ǁE�E�E���񂺂������I
 * @author shinya
 *
 */
public class DirMenuControl implements PluginInterface{

	/** 
	 * �f�B���N�g���c���[���̂������[�g�m�[�h���Z�b�V�����Ɋi�[����ۂ̃L�[ 
	 */
	private static final String TREE_NODE_SESS_KEY = "TREE_NODE_SESS_KEY";

	
	/** �c���[�m�[�h */
	private Tree dirtree = new Tree("dirtree"){
		
		
	    protected void renderTreeNodeStart(HtmlStringBuffer buffer, TreeNode treeNode,
	            int indentation) {
	    	

	        buffer.append("<li><span");
	        StringBuffer sb = new StringBuffer();
	        sb.append(getExpandClass(treeNode));
	        buffer.appendAttribute("class", sb.toString());
	        if (isJavascriptEnabled()) {
	            //hook to insert javascript specific code
	            //javascriptHandler.getJavascriptRenderer().renderTreeNodeStart(buffer); // �R�����g�A�E�g
	        }
	        buffer.appendAttribute("style", "display:block;");
	        buffer.closeTag();

	        //Render the node's expand/collapse functionality.
	        //This includes adding a css class for the current expand/collapse state.
	        //In the tree.css file, the css classes are mapped to icons by default.
	        if (treeNode.hasChildren()) {
	            //renderExpandAndCollapseAction(buffer, treeNode);  // �R�����g�A�E�g
	            buffer.append("<span class=\"spacer\"></span>");    //�@�ǉ�
	        } else {
	            buffer.append("<span class=\"spacer\"></span>");
	        }
	        buffer.append("\n");

	    
	    }

		
		
		
	    protected void renderIcon(HtmlStringBuffer buffer, TreeNode treeNode) {

	    	ArTreeNode node = (ArTreeNode)treeNode;
	    	
	    	//render the icon to display
	        buffer.elementStart("span");

	        if(node.isDesc()){
	        	//�@�A�C�R���͕`�悵�Ȃ�
	    	}
	        else{
		        buffer.appendAttribute("class", getIconClass(treeNode));
	        }
	    	

	        buffer.append(">");
	    }		
		
		
	    /**
	     * �m�[�h�̕\���������ҏW����B<br>
	     * �m�[�h��ID�����ɑΉ����郊���N���쐬����
	     */
		protected void renderValue(HtmlStringBuffer buffer, TreeNode treeNode) {

			ArTreeNode node = (ArTreeNode)treeNode;
			
	    	// TODO�@�ݒ�t�@�C���ɂ��A��\���A�\�������A�����N�����\��
	    	

			//�@TODO ���͂Ƃ肠�����̃R�[�h�AFireFox��/�̕ς���\���t���Ă���̂�
			String id = node.getId();
			if(id.startsWith("\\")){
				id = "/" + id.substring(1);
			}
			
			String title = (String)node.getValue();
			if(title.length()>10){
				title = title.substring(0,10) + "...";
			}
			
			buffer.append("<small>");
			
	    	if(node.isDesc()){
	    		buffer.append("<label title=\"" + node.getValue() + "\">");
		        buffer.append(title);
		        buffer.append("</label>");
	    	}
	    	else{
		    	buffer.elementStart("a");
		        buffer.append(" href=\"");
		        buffer.append(id);
		        buffer.append("\"");
		        buffer.append("title=\"");
		        buffer.append(node.getValue());
		        buffer.append("\"");
		        
		        
		        buffer.closeTag();
		        if (node.getValue() != null) {
		            buffer.append(title);
		        }
		        buffer.elementEnd("a");
	    	}
	    	
			buffer.append("</small>");
	    	
	        buffer.append("\n");

	        
	    }

	};

	
	
	
	
	public void onDestroy(Map params, Page page) {
		
	}

	public void onGet(Map params, Page page) {
		
	}

	public void onInit(Map params, Page page) {
		
		page.addControl(dirtree);
		
		
		//�@���j���[�c���[��ݒ�
		this.buildTree(page);
		
	}

	public void onPost(Map params, Page page) {
		
	}

	public void onRender(Map params, Page page) {

		
	}

	public boolean onSecurityCheck(Map params, Page page) {
		return true;
	}

	
	
	
	
	public String toString(){
		return this.dirtree.toString();
	}
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * ���j���[���\�z
	 *
	 */
	private void buildTree(Page page){

		//�@�X�V�t�@�C�����̎��Ԃ��擾�@�E�E�E�@
        String lastupdatedtime = "";
        String lastpath = page.getContext().getServletContext().getRealPath("/");
        lastpath = lastpath + ArUtil.DIR_CONF + "lastupdated.txt";
        List lastlines = ArUtil.readFile(lastpath);
        if(lastlines.size()!=0){
        	lastupdatedtime = (String)lastlines.get(0);
        }

        //�@�Z�b�V�����ɕۑ������X�V�t�@�C���̎��Ԃ��擾�@�E�E�E�A
        String sesslastupdated = 
        	(String)page.getContext().getSession().getAttribute("LASTUPDATED");
        
        //�@�@�ƇA���قȂ����狭���\�z
        boolean forcebuild = true;
        //if(!lastupdatedtime.equals(sesslastupdated)){
        	forcebuild = false;
        //}
        
		//�@�Z�b�V�����Ƀ��[�g�m�[�h����������A�����ݒ�
        TreeNode existingRootNode = 
        	(TreeNode)page.getContext().getSession().getAttribute(TREE_NODE_SESS_KEY);
        if( forcebuild 	&& existingRootNode != null ){

			dirtree.setRootNode(existingRootNode);
	        //dirtree.setRootNodeDisplayed(true);
            return;
        	
        }
		
        //�@���[�g�p�X���擾
        String docroot = ArUtil.getProperty(page, ArUtil.PROP_SITE, "document_root");
        String rootpath = page.getContext().getServletContext().getRealPath(docroot);
        String absrootpath = rootpath.substring(0,rootpath.length() - docroot.length());
        
        //�@�y�[�W�̏����擾
        PageProperties prop = ArUtil.getPageProperties(rootpath);

        //�@���[�g�m�[�h�̍쐬�ƒl�̐ݒ�
        ArTreeNode rootnode = new ArTreeNode(prop.getTitle(), ArUtil.APPNAME + docroot);
        rootnode.setIsVisible(prop.isVisible());
        rootnode.setIsLink(prop.isIslink());
        rootnode.setIsRenderChild(prop.isRenderChild());
		
		//�@DESC������ꍇ
		List descs = prop.getDescs();
		if(!descs.isEmpty()){
			for(int j = 0 ; j < descs.size(); j++){
				String desc = (String)descs.get(j);
				ArTreeNode descchild = new ArTreeNode(desc,rootnode.generateId(),rootnode,false);
				descchild.setIsDesc(true);
				
			}
			
		}

		//�@���[�g�m�[�h�̐ݒ�
        dirtree.setRootNode(rootnode);
    	rootnode.setIsRenderChild(prop.isPublic());
        
        //�ċA�I�Ƀf�B���N�g���\�����Z�b�g����
        this.setTree(page, rootnode, rootpath, absrootpath);
        dirtree.expandAll();
        dirtree.setRootNodeDisplayed(prop.isVisible());
    	
        
        page.getContext().getSession().setAttribute("LASTUPDATED", lastupdatedtime);
        page.getContext().getSession().setAttribute(TREE_NODE_SESS_KEY, rootnode);

	
	}
	
	private Page _page;
	
	/**
	 * �ċA�I�Ƀh�L�������g���[�g���猟�����s���A�f�B���N�g���c���[�ɕ\��
	 * @param node
	 * @param relativepath
	 * @param rootpath
	 */
	private void setTree(Page page, ArTreeNode node, String relativepath, String rootpath){
		
		_page = page;
		
		//�@�f�B���N�g���݂̂̃f�B���N�g�������X�g���擾
		// �i[00][pageclass][event]�Ȃǂ̃p�X���܂܂Ȃ��f�B���N�g�����j
		File file = new File(relativepath);
		String[] _files = file.list(
				new FilenameFilter(){
					public boolean accept(File dir, String name) {

						String contextpath = _page.getContext().getServletContext().getRealPath("");
						File confile = new File(contextpath);
						if(dir.getAbsolutePath().equals(confile.getAbsolutePath())){
							
							if(name.equals(".settings")){
								return false;
							}
							if(name.equals("work")){
								return false;
							}
							if(name.equals("WEB-INF")){
								return false;
							}
							if(name.equals("click")){
								return false;
							}
							if(name.equals("tools")){
								return false;
							}
						
						}
						
						
						File f = new File(dir.getAbsolutePath() + "/" + name);
						if(f.exists() && f.isDirectory()){
							return true;
						}
						return false;
					}
				}
				
		);
		
		//�@�y�[�W�v���p�e�B���擾
		PageProperties dirprop = ArUtil.getPageProperties(relativepath);
		
		//�@�y�[�W�v���p�e�B�ɂ��������ĕ��ёւ����s��
		String[] files = new String[_files.length];
		int filecnt = 0;
		List childorder = dirprop.getChildorder();
		for(int i = 0 ; i < childorder.size(); i++){
			String childname = (String)childorder.get(i);
			boolean endflg=false;
			for(int j = 0 ; j < _files.length; j++){
				String filename = _files[j];
				//filename = filename.substring(filename.lastIndexOf(ArUtil.SV_FILE_SEPARATOR));
				if(childname.equals(filename)){
					files[filecnt] = _files[j];
					filecnt++;
					_files[j] = null;
				}
				
			}
			
		}
		
		//�@�y�[�W�v���p�e�B�ɋL�q����Ă��Ȃ����̂��Ō�ɒǉ�
		for(int i = 0 ; i < _files.length; i++){
			if(_files[i] !=null){
				files[filecnt]= _files[i];
				filecnt++;
			}
		}
		
		
		//�@�m�[�h���쐬
		for(int i = 0 ; i < files.length; i++){
			String abspath = "";
			if(relativepath.endsWith(ArUtil.SV_FILE_SEPARATOR)){
				abspath = relativepath + files[i];
			}
			else{
				abspath = relativepath + "/" + files[i];
			}
			
			File wkfile = new File(abspath);
			PageProperties prop = ArUtil.getPageProperties( abspath);
			
			if(prop.isVisible()){
				String title = prop.getTitle();
				
				//�@���̃t�H���_���m�[�h������
				String id =  abspath.substring(rootpath.length());
				if(id.startsWith("\\")){
					id = "/" + id.substring(1);
				}

				ArTreeNode child = new ArTreeNode(title, ArUtil.APPNAME + id,node);
				child.setIsVisible(prop.isVisible());
				child.setIsLink(prop.isIslink());
				child.setIsRenderChild(prop.isRenderChild());
				
				//�@DESC������ꍇ
				List descs = prop.getDescs();
				if(!descs.isEmpty()){
					for(int j = 0 ; j < descs.size(); j++){
						String desc = (String)descs.get(j);
						ArTreeNode descchild = new ArTreeNode(desc,child.generateId(),child,false);
						descchild.setIsDesc(true);
						
					}
					
				}
				
				if(child.isRenderChild()){
					setTree(page, child, abspath, rootpath);
				}
					
				
			}

		}
		
	}


	
	
	
}
