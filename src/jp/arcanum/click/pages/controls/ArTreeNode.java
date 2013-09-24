package jp.arcanum.click.pages.controls;

import java.util.Random;

import net.sf.click.extras.tree.TreeNode;

/**
 * ツリーノード
 * @author shinya
 *
 */
public class ArTreeNode extends TreeNode {
	
    /** Used internally to generate unique id's for tree nodes where id's are
     * not explicitly provided. */
    protected final static Random RANDOM = new Random();
	
	/**
	 * DESCノードかどうか
	 */
	private boolean desc = false;
	public void setIsDesc(boolean value){
		desc = value;
	}
	public boolean isDesc(){
		return desc;
	}
	
	/**
	 * 表示するノードかどうか
	 */
	private boolean visible = false;
	public void setIsVisible(boolean value){
		visible = value;
	}
	
	/**
	 * リンクするノードかどうか
	 */
	private boolean link = false;
	public void setIsLink(boolean value){
		link = value;
	}
	
	/**
	 * 子供フォルダを描画するかどうか
	 */
	private boolean renderchild = false;
	public void setIsRenderChild(boolean value){
		renderchild = value;
	}
	public boolean isRenderChild(){
		return renderchild;
	}
	
	/**
	 * コンストラクタ
	 *
	 */
	public ArTreeNode(){
		super();
	}
	
	/**
	 * コンストラクタ
	 *
	 */
    public ArTreeNode(Object value, String id) {
    	super(value,id);
    }


	/**
	 * コンストラクタ
	 *
	 */
    public ArTreeNode(Object value, String id, TreeNode parent) {
    	super(value, id, parent);
    }

	/**
	 * コンストラクタ
	 *
	 */
    public ArTreeNode(Object value, String id, TreeNode parent, boolean childrenSupport) {
    	super(value, id, parent, childrenSupport);
    }

	/**
	 * ID生成
	 * このメソッドは、オリジナルのクラスのメソッドが非公開であるため
	 * 公開するために作った
	 *
	 */
    public String generateId() {
        return Long.toString(Math.abs(RANDOM.nextLong()));
    }

    
}
