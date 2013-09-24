/**
 * 
 */
package jp.arcanum.click.pages;

import java.util.List;

import jp.arcanum.click.ArUtil;
import jp.arcanum.click.PageProperties;
import jp.arcanum.click.wiki.HikiLikeFormatter;




/**
 *
 */
public class Index extends TemplatePage {

	public String wikicontents = "";
	
	public void onRender(){
	
		super.onRender();
		
		String abspath = super.context.getServletContext().getRealPath("");
		abspath = abspath + this.getPath();
		PageProperties prop = ArUtil.getPageProperties(abspath);
		
		if(prop.getMarkup().equals(PageProperties.MARKUP_WIKI)){

			abspath = abspath.substring(0,abspath.length()-9);
			abspath = abspath + "wiki.txt";
			
			List lines = ArUtil.readFile(abspath);
			HikiLikeFormatter formatter = new HikiLikeFormatter();
			
			for(int i = 0 ; i < lines.size(); i++){
				wikicontents = wikicontents + formatter.format((String)lines.get(i));
			}
			
		}
		
	}
	
}
