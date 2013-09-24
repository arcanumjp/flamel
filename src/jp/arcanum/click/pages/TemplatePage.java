package jp.arcanum.click.pages;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import jp.arcanum.click.ArUtil;
import jp.arcanum.click.PageProperties;
import jp.arcanum.click.PluginInterface;
import jp.arcanum.click.UserInfo;
import net.sf.click.Page;
import net.sf.click.control.Form;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


public class TemplatePage extends Page {
	
	/**
	 * �v���O�C�����X�g
	 */
	private List plugins = new ArrayList();
	
	/**
	 * �w�b�_�̃^�C�g��
	 */
	public String headertitle = "";
	
	/**
	 * �w�b�_�[�C���|�[�g
	 * VTL��ŕ\������̂��߂�ǂ��������AHTML�̃X�^�C��
	 * �ɂȂ��֌W�Ȃ���ł����ŕ����񉻂���B
	 */
	public List headerimports = new ArrayList(){
		
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
	public String sitetitle = "";
	
	/**
	 * ���[��
	 */
	public String mailto = "";
	
	/**
	 * FORM
	 */
	public Form form = new Form();
	
	
	/**
	 * �R���X�g���N�^
	 *
	 */
	public TemplatePage() {
		
		
	}
	
	/**
	 * �Z�L�����e�B�`�F�b�N
	 */
	public boolean onSecurityCheck(){

		
		//�@�\�����悤�Ƃ���y�[�W�̃v���p�e�B���擾
		String abspath = super.context.getServletContext().getRealPath(this.getPath());
		abspath = abspath.substring(0,abspath.indexOf(ArUtil.SV_FILE_SEPARATOR + "index.htm"));
		PageProperties prop = ArUtil.getPageProperties(abspath);
		if(!prop.isPublic()){

			//�@���J���Ȃ��y�[�W�ł����O�C�����Ă���Ό��鎖�͉\
			UserInfo user = (UserInfo)super.context.getSession().getAttribute(ArUtil.USER);
			if(user == null){
				super.setRedirect("/click/not-found.htm");
				return false;
				
			}
		}

			
		//�@�v���O�C���ɈϏ�
		for(int i = 0 ; i < plugins.size(); i++){
			Plugin plugin = (Plugin)plugins.get(i);
			PluginInterface inter = plugin.plugin;
			plugin.issecurityok = inter.onSecurityCheck(plugin.parameters, this);
		}
		
		return true;
	}
	

	/**
	 * ��������
	 */
	public void onInit(){

		this.setPlugins();
		
		for(int i = 0 ; i < plugins.size(); i++){
			Plugin plugin = (Plugin)plugins.get(i);
			PluginInterface inter = plugin.plugin;
			//if(!plugin.issecurityok)continue;
			inter.onInit(plugin.parameters, this);
		}
		
		
	}
	
	/**
	 * HTTP-GET
	 */
	public void onGet(){
		super.onGet();
		
		//�@�v���O�C���ɏ������Ϗ�
		for(int i = 0 ; i < plugins.size(); i++){
			Plugin plugin = (Plugin)plugins.get(i);
			PluginInterface inter = plugin.plugin;
			if(!plugin.issecurityok)continue;
			inter.onGet(plugin.parameters, this);
		}

	}
	
	/**
	 * HTTP-POST
	 */
	public void onPost(){
		super.onPost();
		
		//�@�v���O�C���ɏ������Ϗ�
		for(int i = 0 ; i < plugins.size(); i++){
			Plugin plugin = (Plugin)plugins.get(i);
			PluginInterface inter = plugin.plugin;
			if(!plugin.issecurityok)continue;
			inter.onPost(plugin.parameters, this);
		}

	}
	
	
	
	/**
	 * �`��O����
	 */
	public void onRender(){
		
		//�@�v���O�C���ɈϏ�
		for(int i = 0 ; i < plugins.size(); i++){
			Plugin plugin = (Plugin)plugins.get(i);
			PluginInterface inter = plugin.plugin;
			if(!plugin.issecurityok)continue;
			inter.onRender(plugin.parameters, this);
		}
		
		//�@�T�C�g�ݒ�
		this.setSiteProperties();

	}
	
	/**
	 * �y�[�W�j���O����
	 */
	public void onDestroy(){
		
		super.onDestroy();
		
		for(int i = 0 ; i < plugins.size(); i++){
			Plugin plugin = (Plugin)plugins.get(i);
			PluginInterface inter = plugin.plugin;
			inter.onDestroy(plugin.parameters, this);
		}

		
	}
	


	/**
	 * �T�C�g�̃v���p�e�B��ݒ�
	 *
	 */
	public void setSiteProperties(){
		
		//�@�y�[�W�g�b�v�̐ݒ���擾���Ă���
		String pagetop = ArUtil.getProperty(this, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_DOCROOT);

		//�@�w�b�_�[�^�C�g��
		this.headertitle = ArUtil.getProperty(this, ArUtil.PROP_SITE, "header.title");
		
		// �T�C�g�^�C�g��
		this.sitetitle = ArUtil.getProperty(this, ArUtil.PROP_SITE, "sitetitle.title");
		String titlelink = ArUtil.getProperty(this, ArUtil.PROP_SITE, "sitetitle.link");
		if(Boolean.valueOf(titlelink).booleanValue()){
			this.sitetitle = "<a href=\"" + ArUtil.APPNAME + pagetop + "\">" + "<font color=white class=titlebar >"  + this.sitetitle + "</font></a>";
		}
		
		//�@���[��TO
		this.mailto = ArUtil.getProperty(this, ArUtil.PROP_SITE, "mail.address");
		String maillink = ArUtil.getProperty(this, ArUtil.PROP_SITE, "mail.link");
		String mailtext = ArUtil.getProperty(this, ArUtil.PROP_SITE, "mail.title");
		if(Boolean.valueOf(maillink).booleanValue() && !mailtext.equals("") ){
			
			this.mailto = "<a href=\"mailto:" + this.mailto + "\">" + "<font color=white class=titlebar >"  + mailtext + "</font></a>";
		}
		
		// TODO�@/WEB-INF/conf/link.xml�Ȃ񂩂Őݒ�ł���Ƃ�������
		headerimports.add("<link rel=\"stylesheet\" href=\"" + ArUtil.APPNAME + "/default.css\" type=\"text/css\" />");

	}
	
	
	/**
	 * �e���v���[�g�t�@�C���擾
	 */
	public String getTemplate(){
		//String ret = ArUtil.APPNAME +  ArUtil.getProperty(this, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_TEMPLATE);
		String ret = ArUtil.getProperty(this, ArUtil.PROP_SITE, ArUtil.KEY_SITECONF_TEMPLATE);
		return ret;
		//return "/indextemplate.htm";
	}
	 
	
	/**
	 * �v���O�C����ǂݍ���
	 *
	 */
	private void setPlugins(){

		String xmlfile = super.context.getServletContext().getRealPath("");
		xmlfile = xmlfile + "/WEB-INF/plugin.xml";
		
		if(!new File(xmlfile).exists()){
			return;
		}
		
		try {
			//�@XML��ǂݍ���ŁA���[�g�G�������g<page>���擾
			DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = dbfactory.newDocumentBuilder();
			Document doc = builder.parse("file:" + xmlfile);
			Element root = doc.getDocumentElement();
			
			//  <plugin>�̗v�f��<class>���擾
			NodeList classlist = root.getElementsByTagName("class");
			for(int i = 0 ; i < classlist.getLength(); i++){
				Element classelem = (Element)classlist.item(i);
				
				String classname = classelem.getAttribute("classname");

				Class clazz = Class.forName(classname);
				PluginInterface plugininstance = (PluginInterface)clazz.newInstance();
				
				Map params = new HashMap();
				
				// <parameter>�^�O���擾
				NodeList paramlist = classelem.getElementsByTagName("parameter");
				if(paramlist.getLength()!=0){
					
					for(int j = 0 ; j < paramlist.getLength(); j++){
						
						//<param-name>
						Element nameelem = (Element)paramlist.item(0);
						String name = nameelem.getFirstChild().getNodeValue();
						//<param-value>
						Element valueelem =(Element)paramlist.item(1);
						String value = valueelem.getFirstChild().getNodeValue();
						
						params.put(name, value);
						
					}

				}
					
				
				Plugin plugin = new Plugin();
				plugin.plugin = plugininstance;
				plugin.parameters = params;
				plugins.add(plugin);
				
			}

			
			
		} 
		catch (Exception e) {
			throw new RuntimeException("plugin.xml�̉�͂Ɏ��s�@�@" + xmlfile, e);
		}
		
	}

	
	
	
	/**
	 * �v���O�C����\����������N���X
	 * @author shinya
	 *
	 */
	class Plugin{
		public PluginInterface plugin = null;
		public Map parameters = new HashMap();
		boolean issecurityok = false; 
	}
	
}
