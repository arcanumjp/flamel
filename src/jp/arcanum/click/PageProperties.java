package jp.arcanum.click;

import java.util.ArrayList;
import java.util.List;

public class PageProperties {
	
	/**
	 * マークアップ方法・ＨＴＭＬ
	 */
	public static final String MARKUP_HTML = "html";
	/**
	 * マークアップ方法・ＷＩＫＩ
	 */
	public static final String MARKUP_WIKI = "wiki";
	
	/**
	 * メニューに表示するかどうか
	 */
	private boolean isvisible = false;
	public boolean isVisible(){
		return isvisible;
	}
	public void setVisivle(boolean value){
		isvisible = value;
	}
	
	/**
	 * リンクをするかどうか
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
	 * マークアップ
	 */
	private String markup = MARKUP_HTML;
	public String getMarkup() {
		return markup;
	}
	public void setMarkup(String markup) {
		this.markup = markup;
	}
	
	/**
	 * ディレクトリタイトル
	 */
	private String title = "";
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * 説明
	 */
	private List descs = new ArrayList();
	public List getDescs() {
		return descs;
	}
	public void setDescs(List descs) {
		this.descs = descs;
	}

	/**
	 * 配下の表示順
	 */
	private List childorder = new ArrayList();
	public List getChildorder() {
		return childorder;
	}
	public void setChildorder(List childorder) {
		this.childorder = childorder;
	}

	/**
	 * 配下を描画するかどうか
	 */
	private boolean renderchild = false;
	public boolean isRenderChild() {
		return renderchild;
	}
	public void setRenderChild(boolean renderchild) {
		this.renderchild = renderchild;
	}
	
	/**
	 * 公開するかどうか
	 */
	private boolean ispublic = false;
	public boolean isPublic(){
		return ispublic;
	}
	public void setPublic(boolean value){
		ispublic = value;
	}
	
}
