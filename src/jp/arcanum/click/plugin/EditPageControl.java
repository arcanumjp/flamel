package jp.arcanum.click.plugin;

import java.util.Map;

import jp.arcanum.click.ArUtil;
import jp.arcanum.click.PluginInterface;
import jp.arcanum.click.UserInfo;
import jp.arcanum.click.pages.EditFilePage;
import net.sf.click.Page;
import net.sf.click.control.ActionLink;

/**
 * �ҏW�����N��\������R���g���[���B<br>
 * ��ʂ̋������ɒu�����肵�āA���΂₭�ҏW��ʂɓ��B�ł���悤�ɂ��邽�߂�
 * �R���g���[���B���O�C�����Ă��Ȃ��ƌ����Ȃ��d�l
 * @author shinya
 *
 */
public class EditPageControl implements PluginInterface{

	
	/**
	 * �ҏW�����N�i���O�C�����Ă��Ȃ��ƌ����Ȃ��j
	 */
	public ActionLink editpage = new ActionLink("editpage", "�ҏW",this, "onClickEditThisPage"){
		public String toString(){
			if(super.context.getSession().getAttribute(ArUtil.USER)==null){
				return "";
			}
			return super.toString();
		}
	};

	
	public void onDestroy(Map params, Page page) {
		
	}

	public void onGet(Map params, Page page) {
		
	}

	public void onInit(Map params, Page page) {
		page.addControl(editpage);
		_page = page;
	}

	public void onPost(Map params, Page page) {
	}

	public void onRender(Map params, Page page) {
	}

	public boolean onSecurityCheck(Map params, Page page) {
		return true;
	}

	private Page _page = null;
	
	
	/**
	 * 
	 * @return
	 */
	public boolean onClickEditThisPage(){
		
		UserInfo user = (UserInfo)_page.getContext().getSession().getAttribute(ArUtil.USER);
		if(user == null){
			return true;
		}
		
		EditFilePage page = (EditFilePage)_page.getContext().createPage(EditFilePage.class);
		page.setParameter(_page.getPath());
		_page.setForward(page);
		
		return true;
		
	}

}
