package jp.arcanum.click.plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jp.arcanum.click.ArUtil;
import jp.arcanum.click.PluginInterface;
import net.sf.click.Page;

public class SetSitePropControl implements PluginInterface{

	
	/**
	 * �w�b�_�̃^�C�g��
	 */
	private String headertitle = "";
	
	/**
	 * �w�b�_�[�C���|�[�g
	 * VTL��ŕ\������̂��߂�ǂ��������AHTML�̃X�^�C��
	 * �ɂȂ��֌W�Ȃ���ł����ŕ����񉻂���B
	 */
	private List headerimports = new ArrayList(){
		
		public String toString(){
			String ret = "";
			for(int i = 0 ; i < size(); i++){
				ret = ret + get(i);
				
			}
			return ret;
		}
	};

	/**
	 * �T�C�g�̃^�C�g��
	 */
	private String sitetitle = "";
	
	/**
	 * ���[��
	 */
	private String mailto = "";

	
	public void onDestroy(Map params, Page page) {
		
	}

	public void onGet(Map params, Page page) {
		
	}

	public void onInit(Map params, Page page) {
	}

	public void onPost(Map params, Page page) {
	}

	public void onRender(Map params, Page page) {
	}

	public boolean onSecurityCheck(Map params, Page page) {
		return true;
	}

}
