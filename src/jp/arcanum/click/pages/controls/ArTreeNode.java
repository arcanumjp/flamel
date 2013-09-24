package jp.arcanum.click.pages.controls;

import java.util.Random;

import net.sf.click.extras.tree.TreeNode;

/**
 * �c���[�m�[�h
 * @author shinya
 *
 */
public class ArTreeNode extends TreeNode {
	
    /** Used internally to generate unique id's for tree nodes where id's are
     * not explicitly provided. */
    protected final static Random RANDOM = new Random();
	
	/**
	 * DESC�m�[�h���ǂ���
	 */
	private boolean desc = false;
	public void setIsDesc(boolean value){
		desc = value;
	}
	public boolean isDesc(){
		return desc;
	}
	
	/**
	 * �\������m�[�h���ǂ���
	 */
	private boolean visible = false;
	public void setIsVisible(boolean value){
		visible = value;
	}
	
	/**
	 * �����N����m�[�h���ǂ���
	 */
	private boolean link = false;
	public void setIsLink(boolean value){
		link = value;
	}
	
	/**
	 * �q���t�H���_��`�悷�邩�ǂ���
	 */
	private boolean renderchild = false;
	public void setIsRenderChild(boolean value){
		renderchild = value;
	}
	public boolean isRenderChild(){
		return renderchild;
	}
	
	/**
	 * �R���X�g���N�^
	 *
	 */
	public ArTreeNode(){
		super();
	}
	
	/**
	 * �R���X�g���N�^
	 *
	 */
    public ArTreeNode(Object value, String id) {
    	super(value,id);
    }


	/**
	 * �R���X�g���N�^
	 *
	 */
    public ArTreeNode(Object value, String id, TreeNode parent) {
    	super(value, id, parent);
    }

	/**
	 * �R���X�g���N�^
	 *
	 */
    public ArTreeNode(Object value, String id, TreeNode parent, boolean childrenSupport) {
    	super(value, id, parent, childrenSupport);
    }

	/**
	 * ID����
	 * ���̃��\�b�h�́A�I���W�i���̃N���X�̃��\�b�h������J�ł��邽��
	 * ���J���邽�߂ɍ����
	 *
	 */
    public String generateId() {
        return Long.toString(Math.abs(RANDOM.nextLong()));
    }

    
}
