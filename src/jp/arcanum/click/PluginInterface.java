package jp.arcanum.click;

import java.util.Map;

import net.sf.click.Page;

public interface PluginInterface {

	public void onInit(Map params, Page page);
	public boolean onSecurityCheck(Map params, Page page);
	public void onGet(Map params, Page page);
	public void onPost(Map params, Page page);
	public void onRender(Map params, Page page);
	public void onDestroy(Map params, Page page);
	
}
