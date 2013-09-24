package jp.arcanum.click;

import java.util.ArrayList;
import java.util.List;

public class PageProperties {
	
	/**
	 * �}�[�N�A�b�v���@�E�g�s�l�k
	 */
	public static final String MARKUP_HTML = "html";
	/**
	 * �}�[�N�A�b�v���@�E�v�h�j�h
	 */
	public static final String MARKUP_WIKI = "wiki";
	
	/**
	 * ���j���[�ɕ\�����邩�ǂ���
	 */
	private boolean isvisible = false;
	public boolean isVisible(){
		return isvisible;
	}
	public void setVisivle(boolean value){
		isvisible = value;
	}
	
	/**
	 * �����N�����邩�ǂ���
	 * @deprecated
	 */
	private boolean islink = false;
	public boolean isIslink() {
		return islink;
	}
	public void setIslink(boolean islink) {
		this.islink = islink;
	}

	/**
	 * �}�[�N�A�b�v
	 */
	private String markup = MARKUP_HTML;
	public String getMarkup() {
		return markup;
	}
	public void setMarkup(String markup) {
		this.markup = markup;
	}
	
	/**
	 * �f�B���N�g���^�C�g��
	 */
	private String title = "";
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * ����
	 */
	private List descs = new ArrayList();
	public List getDescs() {
		return descs;
	}
	public void setDescs(List descs) {
		this.descs = descs;
	}

	/**
	 * �z���̕\����
	 */
	private List childorder = new ArrayList();
	public List getChildorder() {
		return childorder;
	}
	public void setChildorder(List childorder) {
		this.childorder = childorder;
	}

	/**
	 * �z����`�悷�邩�ǂ���
	 */
	private boolean renderchild = false;
	public boolean isRenderChild() {
		return renderchild;
	}
	public void setRenderChild(boolean renderchild) {
		this.renderchild = renderchild;
	}
	
	/**
	 * ���J���邩�ǂ���
	 */
	private boolean ispublic = false;
	public boolean isPublic(){
		return ispublic;
	}
	public void setPublic(boolean value){
		ispublic = value;
	}
	
}
