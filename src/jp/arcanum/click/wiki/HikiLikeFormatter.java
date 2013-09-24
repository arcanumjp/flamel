package jp.arcanum.click.wiki;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.arcanum.click.ArUtil;




/**
 * Hiki�Ɏ����`����HTML�`���ɕϊ�����B
 */
public class HikiLikeFormatter extends WordFormatter {
	protected static final String[] URL_EXTS =
		{ ".jpg", ".jpeg", ".gif", ".png" };

	//	�u���̂��߂̐��K�\���p�^�[��

	//	WikiName�̃p�^�[��
	protected static Pattern wikiNamePattern =
		Pattern.compile(
			"((\\W|^)(([A-Z][0-9a-z]+){2,})|"
				+ "(\\[\\[([^\\|\\:\\[\\]\\s]+)\\]\\]))");

	//	URL�ȂǁA�����N�̃p�^�[��
	protected static Pattern urlPattern =
		Pattern.compile(
			"([^\\|]|^)"
				+ "((http|ftp|news|https):"
				+ "\\/\\/[\\w\\-\\?\\#\\+\\.\\/~:&%=;,]{2,})");
	protected static Pattern mailtoPattern =
		Pattern.compile("([^\\|]|^)mailto:([\\w\\-\\.]+\\@[\\w\\.\\-]+)");
	protected static Pattern interWikiPattern =
		Pattern.compile("\\[\\[([^\\[\\:]+)\\:([^\\]]+)\\]\\]");
	protected static Pattern descUrlPattern =
		Pattern.compile("\\[\\[([^\\[\\|]+)\\|([^\\]]+)\\]\\]");

	//	���̒���Wiki�̃p�^�[�����܂܂�Ă��Ă���������p�^�[��
	protected static Pattern[] ignoreWikiPatterns =
		{ interWikiPattern, descUrlPattern };

	protected static Pattern listPattern =
		Pattern.compile("^(\\*{1,3})([^\\*].*)$");
	protected static Pattern numberedListPattern =
		Pattern.compile("^(\\#{1,3})([^\\#].*)$");
	protected static Pattern definePattern =
		Pattern.compile("^:([^:]+):(.+)$");

	protected static Pattern tablePattern =
		Pattern.compile("^\\|\\|[^\\|].*$");

	protected static Pattern boldPattern = Pattern.compile("'''([^']+)'''");
	protected static Pattern italicPattern = Pattern.compile("''([^']+)''");
	protected static Pattern strikePattern = Pattern.compile("==([^=]+)==");

	protected static Pattern headerPattern =
		Pattern.compile("^(!{1,5})([^!].*)$");

	//	�����̂��߂̐��K�\���p�^�[��
	protected static Pattern blankLinePattern = Pattern.compile("^\\s*$");

	protected static Logger log =
		Logger.getLogger(HikiLikeFormatter.class.getName());

	//	InterWiki��WikiManager�Ɉˑ����Ă���AWikiManager��ContextManager��
	//	�ˑ����Ă���AContextManager��Servlet�̏������Ɉˑ����Ă���B
	//	Formatter��Servlet���������ɃC���X�^���X�������̂ŁA
	//	���̎��_�ł�InterWiki�͎擾�ł��Ȃ��B
	//	�܂��A�r���ŕύX���������ꍇ�ɂ́A�ēǂݍ��݂����Ȃ��Ă͂Ȃ�Ȃ��B
	//	���̂��߁Astart�̂��тɏ�����������̂Ƃ���B

	protected Map interWiki;

	private Mode mode; // ���݂̏��

	/** �f�t�H���g�R���X�g���N�^�B */
	public HikiLikeFormatter() {}

	/**
	 * @see jp.co.tripod.javaballista.kjwiki.ctrl.format.WordFormatter#start(java.lang.String)
	 */
	public String start(String org) {
		mode = new Mode();
		interWiki = null;
		return null;
	}

	/**
	 * @see jp.co.tripod.javaballista.kjwiki.ctrl.format.WordFormatter#end(java.lang.String)
	 */
	public String end(String formatted) {
		return mode.getTagTo(new Mode());
	}

	/**
	 * �y�[�W�̓��e�ŁAHTML�ϊ����ɕK�v�ȕύX���s�Ȃ��B
	 * @param	line	�ϊ����̍s�B
	 * @return	�ϊ���̍s�B
	 */
	protected String formatLine(String line) {
		Matcher m;
		String html;
		StringBuffer prefix;
		StringBuffer suffix;
		Mode toMode;

		prefix = new StringBuffer();
		suffix = new StringBuffer();

		toMode = new Mode();

		while (true) {
			/*
			 * �G�X�P�[�v�s�v�̒u���B
			 */
			if (line.equals("----")) {
				html = "<hr>";
				break;
			} else if (line.startsWith("\"\"")) {
				//	���p
				toMode.setQuote();

				line = line.substring(2);

				toMode.setInParagraph(line.length() > 0);
			}

			//	�G�X�P�[�v
			html = ArUtil.changeString(line);

			/*
			 * ��s��PRE�̏����B
			 */
			if (html.startsWith(" ") || html.startsWith("\t")) {
				toMode.setPre();

				html = html.substring(1);
				break;
			} else if ((m = blankLinePattern.matcher(html)).matches()) {
				html = "";
				break;
			}

			/*
			 * ���[�h�̏����B
			 */
			if ((m = listPattern.matcher(html)).matches()) {
				//	���X�g
				String heading = m.group(1);
				String body = m.group(2);

				toMode.setUlList(heading.length());

				html = body;
			} else if ((m = numberedListPattern.matcher(html)).matches()) {
				//	�������X�g
				String heading = m.group(1);
				String body = m.group(2);

				toMode.setOlList(heading.length());

				html = body;
			} else if ((m = headerPattern.matcher(html)).matches()) {
				//	�w�b�_
				html = super.replaceRegExp(html, new HeaderReplacer());
			} else if ((m = definePattern.matcher(html)).matches()) {
				//	��`���X�g
				toMode.setDlList();

				html =
                    super.replaceRegExp(html, new DefListReplacer());
			} else if ((m = tablePattern.matcher(html)).matches()) {
				//	�e�[�u��
				toMode.setTable();

				html = super.replaceRegExp(html, new TableReplacer());
			} else {
				//	�ʏ�̍s
				toMode.enterParagraph();
			}

			/*
			 * URL��WikiName�̒u���B
			 */
			//	�P�Ƃŏo������URL��[[����|URL]]�̌`�ɐ��`����
			html = super.replaceRegExp(html, new UrlReplacer());
			html = super.replaceRegExp(html, new MailtoReplacer());

			//	WikiName��u������
			html = replaceWiki(html);

			//	URL
			html = super.replaceRegExp(html, new DescUrlReplacer());

			//	InterWiki
			html = super.replaceRegExp(html, new InterWikiReplacer());

			/*
			 * ���K�\���̒u���B
			 */
			//	������̈ꕔ
			html = super.replaceRegExp(html, new BoldReplacer());
			html = super.replaceRegExp(html, new ItalicReplacer());
			html = super.replaceRegExp(html, new StrikeReplacer());

			break;
		}

		/*
		 * ���[�h�ψق̏����B
		 */
		//	���[�h����
		if (!mode.equals(toMode)) {
			prefix.append(mode.getTagTo(toMode));
			mode = toMode;
		}
		if (mode.isList()) {
			prefix.append("<li>");
			suffix.append("</li>");
		}

		html = prefix + html + suffix;
		return html;
	}

	//	WikiName�u��
	private String replaceWiki(String line) {
		List startIdxs;
		List endIdxs;
		Matcher m;
		boolean matched;
		StringBuffer dst;
		int lastIndex;

		//	WikiName���o�����Ă��������镔��������
		startIdxs = new ArrayList();
		endIdxs = new ArrayList();
		for (int i = 0; i < ignoreWikiPatterns.length; i++) {
			Pattern ptn = ignoreWikiPatterns[i];

			m = ptn.matcher(line);
			while (m.find()) {
				startIdxs.add(new Integer(m.start(0)));
				endIdxs.add(new Integer(m.end(0)));
			}
		}

		/*
		 * �u��
		 */
		m = wikiNamePattern.matcher(line);
		matched = m.find();
		if (!matched) {
			return line;
		}

		dst = new StringBuffer();
		lastIndex = 0;
		while (matched) {
			String name;
			String url;
			int s;
			int e;
			boolean ignore;
			boolean defined;

			//	�u�������̒��o
			if (m.start(3) >= 0) {
				//	WikiName�̃p�^�[��
				name = m.group(3);
				s = m.start(3);
				e = m.end(3);
			} else {
				//	[[Wiki-Name]]�̃p�^�[��
				name = m.group(6);
				s = m.start(5);
				e = m.end(5);
			}
			name = ArUtil.unescape(name);
			log.finest("Matched wiki " + name + "(" + s + "," + e + ")");

			//	�������邩����
			ignore = false;
			for (int i = 0; i < startIdxs.size(); i++) {
				int sn = ((Integer) startIdxs.get(i)).intValue();
				int en = ((Integer) endIdxs.get(i)).intValue();

				if (sn <= s && e <= en) {
					ignore = true;
					break;
				}
			}

			/*
			 *	�u���̎��s
			 */
			if (!ignore) {
				log.finest("Replace.");

				//	�u�������̑O�܂ł̕������ǉ�
				dst.append(line.substring(lastIndex, s));

				//	��`�ς݂��ۂ��m�F
				//defined = WikiWordManager.getInstance().isDefined(name);

				//	URL�̎擾
//				url =
//					defined
//						? getReadUrlForName(name)
//						: getEditUrlForName(name);
//
//				//	�u�����ʂ̒ǉ�
//				if (defined) {
//					dst.append("<a href=\"").append(url).append("\">");
//					dst.append(HtmlUtility.escape(name)).append("</a>");
//				} else {
//					dst.append(HtmlUtility.escape(name));
//					dst.append("<a href=\"").append(url).append("\">?</a>");
//				}

				lastIndex = e;
			}

			//	��������
			matched = m.find();
		}

		//	�c�蕔���ǉ�
		dst.append(line.substring(lastIndex));

		return dst.toString();
	}

//	//	WikiName��\�����邽�߂�URL���擾����
//	private String getReadUrlForName(String name) {
//		String encName = null;
//
//		try {
//			encName = URLEncoder.encode(name, Constants.ENCODING);
//		} catch (UnsupportedEncodingException e) {
//			//	ignore
//			e.printStackTrace();
//			encName = name;
//		}
//
//		return Constants.READ_SERVLET_NAME + "?name=" + encName;
//	}

//	//	WikiName��ҏW���邽�߂�URL���擾����
//	private String getEditUrlForName(String name) {
//		String encName = null;
//
//		try {
//			encName = URLEncoder.encode(name, Constants.ENCODING);
//		} catch (UnsupportedEncodingException e) {
//			//	ignore
//			e.printStackTrace();
//			encName = name;
//		}
//
//		return Constants.EDIT_SERVLET_NAME + "?op=edit&amp;name=" + encName;
//	}

	/**
	 * �������[�h��\������N���X�B
	 */
	private class Mode {
		private static final int MODE_NORMAL = 0;
		private static final int MODE_PRE = 1;
		private static final int MODE_QUOTE = 2;
		private static final int MODE_LIST_UL = 3;
		private static final int MODE_LIST_OL = 4;
		private static final int MODE_LIST_DL = 5;
		private static final int MODE_TABLE = 6;

		private int mode = MODE_NORMAL;
		private int level = 0;
		private boolean inParagraph = false;

		public Mode() {}

		public void setNormal() {
			mode = MODE_NORMAL;
			inParagraph = false;
			level = 0;
		}

		public void setPre() {
			mode = MODE_PRE;
			inParagraph = false;
			level = 0;
		}

		public void setQuote() {
			mode = MODE_QUOTE;
			inParagraph = false;
			level = 0;
		}

		public void setUlList(int l) {
			mode = MODE_LIST_UL;
			inParagraph = false;
			level = l;
		}

		public void setOlList(int l) {
			mode = MODE_LIST_OL;
			inParagraph = false;
			level = l;
		}

		public void setDlList() {
			mode = MODE_LIST_DL;
			inParagraph = false;
			level = 0;
		}

		public void setTable() {
			mode = MODE_TABLE;
			inParagraph = false;
			level = 0;
		}

		public boolean isList() {
			return mode == MODE_LIST_UL || mode == MODE_LIST_OL;
		}

		public void setInParagraph(boolean p) {
			inParagraph = p;
		}

		public void enterParagraph() {
			inParagraph = true;
		}

		public void exitParagraph() {
			inParagraph = false;
		}

		public boolean isInParagraph() {
			return inParagraph;
		}

		/**
		 * ���[�h�ƃ��x���A�i����Ԃ������Ȃ�true�B
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		public boolean equals(Object o) {
			Mode m;

			if (!(o instanceof Mode)) {
				return false;
			}

			m = (Mode) o;
			return mode == m.mode
				&& level == m.level
				&& inParagraph == m.inParagraph;
		}

		/**
		 * �����Ԃ��炠���Ԃ֑J�ڂ��邽�߂̃^�O��Ԃ��B
		 * @param toMode �J�ڐ�̏�ԁB
		 * @return �^�O�B
		 */
		public String getTagTo(Mode toMode) {
			StringBuffer tag;
			int ul = 0;
			int ol = 0;
			int toUl = 0;
			int toOl = 0;

			//	�ω��Ȃ�?
			if (this.equals(toMode)) {
				return "";
			}

			tag = new StringBuffer();

			if (this.mode == MODE_LIST_UL) {
				ul = this.level;
			} else if (this.mode == MODE_LIST_OL) {
				ol = this.level;
			}

			if (toMode.mode == MODE_LIST_UL) {
				toUl = toMode.level;
			} else if (toMode.mode == MODE_LIST_OL) {
				toOl = toMode.level;
			}

			/*
			 * ����B
			 */
			if (this.inParagraph
				&& (this.mode != toMode.mode
					|| this.inParagraph != toMode.inParagraph)) {
				//	<p>�̉���
				tag.append("</p>\n");
			}

			if (this.mode != toMode.mode) {
				//	���[�h���ς��
				switch (mode) {
					case MODE_NORMAL :
						break;

					case MODE_PRE :
						tag.append("</pre>\n");
						break;

					case MODE_QUOTE :
						tag.append("</blockquote>\n");
						break;

					case MODE_LIST_DL :
						tag.append("</dl>\n");
						break;

					case MODE_TABLE :
						tag.append("</table>\n");
						break;

					default :
						break;
				}
			}

			//	�C���f���g��
			for (int l = ul; l > toUl; l--) {
				tag.append("</ul>\n");
			}
			for (int l = ol; l > toOl; l--) {
				tag.append("</ol>\n");
			}

			/*
			 * �J���B
			 */
			if (this.mode != toMode.mode) {
				//	���[�h���ς��
				switch (toMode.mode) {
					case MODE_NORMAL :
						break;

					case MODE_PRE :
						tag.append("<pre>\n");
						break;

					case MODE_QUOTE :
						tag.append("<blockquote>\n");
						break;

					case MODE_LIST_DL :
						tag.append("<dl>\n");
						break;

					case MODE_TABLE :
						tag.append("<table>\n");

					default :
						break;
				}
			}

			//	�C���f���g�[��
			for (int l = ul; l < toUl; l++) {
				tag.append("<ul>\n");
			}
			for (int l = ol; l < toOl; l++) {
				tag.append("<ol>\n");
			}

			if (toMode.inParagraph
				&& (this.mode != toMode.mode
					|| this.inParagraph != toMode.inParagraph)) {
				//	<p>�̐ݒ�
				tag.append("<p>\n");
			}

			return tag.toString();
		}

	}

	/**
	 * ''italic''�i�����j����������B
	 */
	private class ItalicReplacer implements StringReplacer {
		public ItalicReplacer() {}

		public Pattern getPattern() {
			return italicPattern;
		}

		public String replace(Matcher m) {
			return "<em>" + m.group(1) + "</em>";
		}
	}

	/**
	 * '''bold'''�i����ɋ����j����������B
	 */
	private class BoldReplacer implements StringReplacer {
		public BoldReplacer() {}

		public Pattern getPattern() {
			return boldPattern;
		}

		public String replace(Matcher m) {
			return "<strong>" + m.group(1) + "</strong>";
		}
	}

	/**
	 * ==strike==����������B
	 */
	private class StrikeReplacer implements StringReplacer {
		public StrikeReplacer() {}

		public Pattern getPattern() {
			return strikePattern;
		}

		public String replace(Matcher m) {
			return "<strike>" + m.group(1) + "</strike>";
		}
	}

	/**
	 * mailto����������B
	 */
	private class MailtoReplacer implements StringReplacer {
		public MailtoReplacer() {}

		public Pattern getPattern() {
			return mailtoPattern;
		}

		public String replace(Matcher m) {
			//	[[desc|url]]]�ɕϊ�����
			return m.group(1)
				+ "[["
				+ m.group(2)
				+ "|mailto:"
				+ m.group(2)
				+ "]]";
		}
	}

	/**
	 * URL�i[�ȊO�̕���+URL�j����������imailto�������j�B
	 */
	private class UrlReplacer implements StringReplacer {
		public UrlReplacer() {}

		public Pattern getPattern() {
			return urlPattern;
		}

		public String replace(Matcher m) {
			//	[[desc|url]]]�ɕϊ�����
			log.finest("group1 : '" + m.group(1) + "'");
			log.finest("group2 : '" + m.group(2) + "'");
			return m.group(1) + "[[" + m.group(2) + "|" + m.group(2) + "]]";
		}
	}

	/**
	 * [[desc|url]]����������B
	 */
	private class DescUrlReplacer implements StringReplacer {
		public DescUrlReplacer() {}

		public Pattern getPattern() {
			return descUrlPattern;
		}

		public String replace(Matcher m) {
			String desc = m.group(1);
			String url = m.group(2);

			return isImage(url)
				? "<img src=\"" + url + "\" alt=\"" + desc + "\">"
				: "<a href=\"" + url + "\">" + desc + "</a>";
		}

		private boolean isImage(String s) {
			for (int i = 0; i < URL_EXTS.length; i++) {
				if (s.endsWith(URL_EXTS[i])) {
					return true;
				}
			}
			return false;
		}
	}

	/**
	 * [[desc|url]]����������B
	 */
	private class InterWikiReplacer implements StringReplacer {
		public InterWikiReplacer() {}

		public Pattern getPattern() {
			return interWikiPattern;
		}

		public String replace(Matcher m) {
			String site = m.group(1);
			String name = m.group(2);
			StringBuffer sb;
			InterWiki iw;

			if (interWiki == null) {
				interWiki = null ; //WikiWordManager.getInstance().getInterWiki();
				if (interWiki == null) {
					return m.group(0);
				}
			}

			iw = (InterWiki) interWiki.get(site);
			if (iw == null) {
				//	InterWiki����`�Ȃ�A���̂܂ܕԂ�
				return m.group(0);
			}

			//	�u�����ʂ̐���
			sb = new StringBuffer();
			sb.append("<a href=\"");
			sb.append(iw.url);

			try {
				sb.append(URLEncoder.encode(name, iw.encoding));
			} catch (UnsupportedEncodingException e) {
				log.log(
					Level.WARNING,
					"Illegal encoding in InterWiki:" + iw.encoding,
					e);

				try {
                    //sb.append(URLEncoder.encode(name, Constants.ENCODING));
                    sb.append(URLEncoder.encode(name, "MS932"));
				} catch (UnsupportedEncodingException e2) {
					//	ignore
				}
			}

			sb.append("\">");
			sb.append(site).append(":").append(name).append("</a>");

			return sb.toString();
		}
	}

	/**
	 * !header����������B
	 */
	private class HeaderReplacer implements StringReplacer {
		public HeaderReplacer() {}

		public Pattern getPattern() {
			return headerPattern;
		}

		public String replace(Matcher m) {
			String head = m.group(1);
			String body = m.group(2);
			int level = head.length() + 1;

			return "<h" + level + ">" + body + "</h" + level + ">";
		}
	}

	/**
	 * :title:desc����������B
	 */
	private class DefListReplacer implements StringReplacer {
		public DefListReplacer() {}

		public Pattern getPattern() {
			return definePattern;
		}

		public String replace(Matcher m) {
			String title = m.group(1);
			String desc = m.group(2);

			return "<dt>" + title + "</dt><dd>" + desc + "</dd>";
		}
	}

	/**
	 * ||table-column||table-column����������B
	 */
	private class TableReplacer implements StringReplacer {
		public TableReplacer() {}

		public Pattern getPattern() {
			return tablePattern;
		}

		public String replace(Matcher m) {
			StringBuffer sb;
			String org = m.group(0);
			int pos;

			sb = new StringBuffer();
			sb.append("<tr>");
			
			pos = org.indexOf("||");
			while (pos != -1) {
				String cell;
				int nextPos = org.indexOf("||", pos + 2);

				cell =
					(nextPos == -1)
						? org.substring(pos + 2)
						: org.substring(pos + 2, nextPos);
				sb.append("<td>").append(cell).append("</td>");
				
				pos = nextPos;
			}
			sb.append("</tr>");
			
			return sb.toString();
		}
	}

}




//
///**
// * ���鐳�K�\�����}�b�`���镶������A�ʂ̕�����ɒu��������N���X��
// * �����C���^�[�t�F�[�X�B
// */
//interface StringReplacer {
//    /**
//     * ���̃N���X�������Ɏg�����K�\���̃p�^�[����Ԃ��B
//     * @return ���K�\���B
//     */
//    public Pattern getPattern();
//    
//    /**
//     * ���鐳�K�\���̃}�b�`��ʂ̕�����ɒu��������B
//     * @param   m   �u�����̐��K�\���̃}�b�`�B
//     * @return  �u����̕�����B
//     */
//    public String replace(Matcher m);
//
//}





/**
 * InterWiki��\�����邽�߂̃N���X�B
 */
class InterWiki {
    private static final String[] ENCODING_ALIASES = {
        "euc", "sjis"
    };
    private static final String[] ENCODINGS = {
        "EUC_JP", "Shift_JIS"
    };
    private static final String DEFAULT_ENCODING = "Windows-31J";
    
    /** �T�C�g���BInterWikiName�̍ŏ��ɗp����B */
    public String siteName;
    
    /** �T�C�g��URL�B */
    public String url;
    
    /**
     * �����̊����R�[�h�BJava�Œ�`����Ă���G���R�[�f�B���O���A�܂���
     * "euc"�A"sjis"���g�p�ł���B
     */
    public String encoding;
    
    /** �f�t�H���g�R���X�g���N�^�B */
    public InterWiki() {}
    
    /**
     * ����������������R���X�g���N�^�B
     * @param siteName
     * @param url
     * @param encoding null�Ȃ�f�t�H���g�iWindows-31J�j�B
     */
    public InterWiki(String siteName, String url, String encoding) {
        this.siteName = siteName;
        this.url = url;
        
        if (encoding == null) {
            encoding = DEFAULT_ENCODING;
        } else {
            for (int i = 0; i < ENCODING_ALIASES.length; i++) {
                if (encoding.equals(ENCODING_ALIASES[i])) {
                    encoding = ENCODINGS[i];
                    break;
                }
            }
        }
        this.encoding = encoding;
    }
    
}
