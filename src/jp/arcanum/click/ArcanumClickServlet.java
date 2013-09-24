package jp.arcanum.click;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.arcanum.click.pages.Index;
import net.sf.click.ClickServlet;
import net.sf.click.Context;
import net.sf.click.Page;
import net.sf.click.util.Format;

/**
 * サーブレット
 * @author shinya
 *
 */
public class ArcanumClickServlet extends ClickServlet {
	
	
	
	
	
	

//	public void init(ServletConfig config) throws ServletException {
//		super.init(config);
//		
//	}

	/**
	 * オーバーライドメソッド
	 */
	protected void service(
			HttpServletRequest request, 
			HttpServletResponse response
	) throws ServletException, IOException {
		
		// TODO init(ServletConfig)で出来るなら移動する
		ArUtil.APPNAME = request.getContextPath();
		super.service(request, response);
	
	}

	/**
	 * ページ作成部分
	 */
    protected Page createPage(HttpServletRequest request,
            HttpServletResponse response, boolean isPost) {

        Context context = new Context(getServletContext(),
                getServletConfig(),
                request,
                response,
                isPost,
                pageMaker);

		String path = context.getResourcePath();
		if(path.endsWith("/index.htm")){

			//　ここで本当にindex.htmがあるか判断が必要・・・
			//　のはずだけど、エラーページに遷移しているのでＯ．Ｋとする
			
			final Page page = initPage(path, Index.class, request);
			page.setContext(context);
			page.setFormat(new Format(context.getLocale()));
			return page;

		}
		
		return super.createPage(request, response, isPost);

    }

    
//    
//    
//    /**
//     * 
//     */
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//    	request.setCharacterEncoding("Shift_JIS");
//    	this.showLog(request, response, "GET");
//		super.doGet(request, response);
//	}
//    
//    /**
//     * 
//     */
//	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//    	request.setCharacterEncoding("Shift_JIS");
//    	this.showLog(request, response, "POST");
//		super.doPost(request, response);
//	}
//
//	
//	
//	
//	
//	
	
	
	
	
	
	
	
	
//	//--------------------------------------------------------------
//	//　次のshowLogメソッドは必ず100行目以降に入れておくこと
//	
//	/**
//     * ログにリクエスト情報を出力
//     * @param request
//     * @param response
//     * @param postget POST / GET
//     */
//    private void showLog(
//            final HttpServletRequest request, 
//            final HttpServletResponse response , 
//            final String postget){
//        
//        
//        
//        logger.debug(request.getRequestURL());
//        logger.debug( " -- " + postget );
//        logger.debug( "-+-----------------------------------------------------" ) ;
//        logger.debug( " | SessionId = " + request.getSession().getId()  ) ;
//
//        //　キー名称リストからパラメータをすべてログに吐き出す
//        Enumeration enumParam = request.getParameterNames() ;
//        while( enumParam.hasMoreElements() ){
//            String str = ( String )enumParam.nextElement() ;
//            logger.debug(" | " + str + " = '"   + request.getParameter( str ) +"'") ;
//
//        }
//
//        //　セッションの中身を見せる
//        Enumeration enumattr = request.getSession().getAttributeNames() ;
//        while( enumattr.hasMoreElements() ){
//            String elem = (String)enumattr.nextElement();
//            logger.debug( " | A Object in this Session ... " + elem ) ;
//        }
//
//
//        logger.debug(request.getContextPath());
//        
//        logger.debug( "-+-----------------------------------------------------" ) ;
//    }
	
    
}
