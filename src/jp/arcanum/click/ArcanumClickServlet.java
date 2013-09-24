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
 * �T�[�u���b�g
 * @author shinya
 *
 */
public class ArcanumClickServlet extends ClickServlet {
	
	
	
	
	
	

//	public void init(ServletConfig config) throws ServletException {
//		super.init(config);
//		
//	}

	/**
	 * �I�[�o�[���C�h���\�b�h
	 */
	protected void service(
			HttpServletRequest request, 
			HttpServletResponse response
	) throws ServletException, IOException {
		
		// TODO init(ServletConfig)�ŏo����Ȃ�ړ�����
		ArUtil.APPNAME = request.getContextPath();
		super.service(request, response);
	
	}

	/**
	 * �y�[�W�쐬����
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

			//�@�����Ŗ{����index.htm�����邩���f���K�v�E�E�E
			//�@�̂͂������ǁA�G���[�y�[�W�ɑJ�ڂ��Ă���̂łn�D�j�Ƃ���
			
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
//	//�@����showLog���\�b�h�͕K��100�s�ڈȍ~�ɓ���Ă�������
//	
//	/**
//     * ���O�Ƀ��N�G�X�g�����o��
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
//        //�@�L�[���̃��X�g����p�����[�^�����ׂă��O�ɓf���o��
//        Enumeration enumParam = request.getParameterNames() ;
//        while( enumParam.hasMoreElements() ){
//            String str = ( String )enumParam.nextElement() ;
//            logger.debug(" | " + str + " = '"   + request.getParameter( str ) +"'") ;
//
//        }
//
//        //�@�Z�b�V�����̒��g��������
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
