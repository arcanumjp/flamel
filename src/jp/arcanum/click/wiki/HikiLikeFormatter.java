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
 * Hikiに似た形式でHTML形式に変換する。
 */
public class HikiLikeFormatter extends WordFormatter {
	protected static final String[] URL_EXTS =
		{ ".jpg", ".jpeg", ".gif", ".png" };

	//	置換のための正規表現パターン

	//	WikiNameのパターン
	protected static Pattern wikiNamePattern =
		Pattern.compile(
			"((\\W|^)(([A-Z][0-9a-z]+){2,})|"
				+ "(\\[\\[([^\\|\\:\\[\\]\\s]+)\\]\\]))");

	//	URLなど、リンクのパターン
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

	//	この中にWikiのパターンが含まれていても無視するパターン
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

	//	検索のための正規表現パターン
	protected static Pattern blankLinePattern = Pattern.compile("^\\s*$");

	protected static Logger log =
		Logger.getLogger(HikiLikeFormatter.class.getName());

	//	InterWikiはWikiManagerに依存しており、WikiManagerはContextManagerに
	//	依存しており、ContextManagerはServletの初期化に依存している。
	//	FormatterはServlet初期化時にインスタンス化されるので、
	//	その時点ではInterWikiは取得できない。
	//	また、途中で変更があった場合には、再読み込みをしなくてはならない。
	//	そのため、startのたびに初期化するものとする。

	protected Map interWiki;

	private Mode mode; // 現在の状態

	/** デフォルトコンストラクタ。 */
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
	 * ページの内容で、HTML変換時に必要な変更を行なう。
	 * @param	line	変換元の行。
	 * @return	変換後の行。
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
			 * エスケープ不要の置換。
			 */
			if (line.equals("----")) {
				html = "<hr>";
				break;
			} else if (line.startsWith("\"\"")) {
				//	引用
				toMode.setQuote();

				line = line.substring(2);

				toMode.setInParagraph(line.length() > 0);
			}

			//	エスケープ
			html = ArUtil.changeString(line);

			/*
			 * 空行とPREの処理。
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
			 * モードの処理。
			 */
			if ((m = listPattern.matcher(html)).matches()) {
				//	リスト
				String heading = m.group(1);
				String body = m.group(2);

				toMode.setUlList(heading.length());

				html = body;
			} else if ((m = numberedListPattern.matcher(html)).matches()) {
				//	数字リスト
				String heading = m.group(1);
				String body = m.group(2);

				toMode.setOlList(heading.length());

				html = body;
			} else if ((m = headerPattern.matcher(html)).matches()) {
				//	ヘッダ
				html = super.replaceRegExp(html, new HeaderReplacer());
			} else if ((m = definePattern.matcher(html)).matches()) {
				//	定義リスト
				toMode.setDlList();

				html =
                    super.replaceRegExp(html, new DefListReplacer());
			} else if ((m = tablePattern.matcher(html)).matches()) {
				//	テーブル
				toMode.setTable();

				html = super.replaceRegExp(html, new TableReplacer());
			} else {
				//	通常の行
				toMode.enterParagraph();
			}

			/*
			 * URLとWikiNameの置換。
			 */
			//	単独で出現するURLを[[説明|URL]]の形に整形する
			html = super.replaceRegExp(html, new UrlReplacer());
			html = super.replaceRegExp(html, new MailtoReplacer());

			//	WikiNameを置換する
			html = replaceWiki(html);

			//	URL
			html = super.replaceRegExp(html, new DescUrlReplacer());

			//	InterWiki
			html = super.replaceRegExp(html, new InterWikiReplacer());

			/*
			 * 正規表現の置換。
			 */
			//	文字列の一部
			html = super.replaceRegExp(html, new BoldReplacer());
			html = super.replaceRegExp(html, new ItalicReplacer());
			html = super.replaceRegExp(html, new StrikeReplacer());

			break;
		}

		/*
		 * モード変異の処理。
		 */
		//	モード解除
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

	//	WikiName置換
	private String replaceWiki(String line) {
		List startIdxs;
		List endIdxs;
		Matcher m;
		boolean matched;
		StringBuffer dst;
		int lastIndex;

		//	WikiNameが出現しても無視する部分を検索
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
		 * 置換
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

			//	置換部分の抽出
			if (m.start(3) >= 0) {
				//	WikiNameのパターン
				name = m.group(3);
				s = m.start(3);
				e = m.end(3);
			} else {
				//	[[Wiki-Name]]のパターン
				name = m.group(6);
				s = m.start(5);
				e = m.end(5);
			}
			name = ArUtil.unescape(name);
			log.finest("Matched wiki " + name + "(" + s + "," + e + ")");

			//	無視するか判定
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
			 *	置換の実行
			 */
			if (!ignore) {
				log.finest("Replace.");

				//	置換部分の前までの文字列を追加
				dst.append(line.substring(lastIndex, s));

				//	定義済みか否か確認
				//defined = WikiWordManager.getInstance().isDefined(name);

				//	URLの取得
//				url =
//					defined
//						? getReadUrlForName(name)
//						: getEditUrlForName(name);
//
//				//	置換結果の追加
//				if (defined) {
//					dst.append("<a href=\"").append(url).append("\">");
//					dst.append(HtmlUtility.escape(name)).append("</a>");
//				} else {
//					dst.append(HtmlUtility.escape(name));
//					dst.append("<a href=\"").append(url).append("\">?</a>");
//				}

				lastIndex = e;
			}

			//	次を検索
			matched = m.find();
		}

		//	残り部分追加
		dst.append(line.substring(lastIndex));

		return dst.toString();
	}

//	//	WikiNameを表示するためのURLを取得する
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

//	//	WikiNameを編集するためのURLを取得する
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
	 * 処理モードを表現するクラス。
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
		 * モードとレベル、段落状態が同じならtrue。
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
		 * ある状態からある状態へ遷移するためのタグを返す。
		 * @param toMode 遷移先の状態。
		 * @return タグ。
		 */
		public String getTagTo(Mode toMode) {
			StringBuffer tag;
			int ul = 0;
			int ol = 0;
			int toUl = 0;
			int toOl = 0;

			//	変化なし?
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
			 * 閉じる。
			 */
			if (this.inParagraph
				&& (this.mode != toMode.mode
					|| this.inParagraph != toMode.inParagraph)) {
				//	<p>の解除
				tag.append("</p>\n");
			}

			if (this.mode != toMode.mode) {
				//	モードが変わる
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

			//	インデント浅く
			for (int l = ul; l > toUl; l--) {
				tag.append("</ul>\n");
			}
			for (int l = ol; l > toOl; l--) {
				tag.append("</ol>\n");
			}

			/*
			 * 開く。
			 */
			if (this.mode != toMode.mode) {
				//	モードが変わる
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

			//	インデント深く
			for (int l = ul; l < toUl; l++) {
				tag.append("<ul>\n");
			}
			for (int l = ol; l < toOl; l++) {
				tag.append("<ol>\n");
			}

			if (toMode.inParagraph
				&& (this.mode != toMode.mode
					|| this.inParagraph != toMode.inParagraph)) {
				//	<p>の設定
				tag.append("<p>\n");
			}

			return tag.toString();
		}

	}

	/**
	 * ''italic''（強調）を処理する。
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
	 * '''bold'''（さらに強調）を処理する。
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
	 * ==strike==を処理する。
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
	 * mailtoを処理する。
	 */
	private class MailtoReplacer implements StringReplacer {
		public MailtoReplacer() {}

		public Pattern getPattern() {
			return mailtoPattern;
		}

		public String replace(Matcher m) {
			//	[[desc|url]]]に変換する
			return m.group(1)
				+ "[["
				+ m.group(2)
				+ "|mailto:"
				+ m.group(2)
				+ "]]";
		}
	}

	/**
	 * URL（[以外の文字+URL）を処理する（mailtoを除く）。
	 */
	private class UrlReplacer implements StringReplacer {
		public UrlReplacer() {}

		public Pattern getPattern() {
			return urlPattern;
		}

		public String replace(Matcher m) {
			//	[[desc|url]]]に変換する
			log.finest("group1 : '" + m.group(1) + "'");
			log.finest("group2 : '" + m.group(2) + "'");
			return m.group(1) + "[[" + m.group(2) + "|" + m.group(2) + "]]";
		}
	}

	/**
	 * [[desc|url]]を処理する。
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
	 * [[desc|url]]を処理する。
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
				//	InterWiki未定義なら、そのまま返す
				return m.group(0);
			}

			//	置換結果の生成
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
	 * !headerを処理する。
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
	 * :title:descを処理する。
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
	 * ||table-column||table-columnを処理する。
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
// * ある正規表現がマッチする文字列を、別の文字列に置き換えるクラスを
// * 示すインターフェース。
// */
//interface StringReplacer {
//    /**
//     * このクラスが検索に使う正規表現のパターンを返す。
//     * @return 正規表現。
//     */
//    public Pattern getPattern();
//    
//    /**
//     * ある正規表現のマッチを別の文字列に置き換える。
//     * @param   m   置換元の正規表現のマッチ。
//     * @return  置換先の文字列。
//     */
//    public String replace(Matcher m);
//
//}





/**
 * InterWikiを表現するためのクラス。
 */
class InterWiki {
    private static final String[] ENCODING_ALIASES = {
        "euc", "sjis"
    };
    private static final String[] ENCODINGS = {
        "EUC_JP", "Shift_JIS"
    };
    private static final String DEFAULT_ENCODING = "Windows-31J";
    
    /** サイト名。InterWikiNameの最初に用いる。 */
    public String siteName;
    
    /** サイトのURL。 */
    public String url;
    
    /**
     * 引数の漢字コード。Javaで定義されているエンコーディング名、または
     * "euc"、"sjis"が使用できる。
     */
    public String encoding;
    
    /** デフォルトコンストラクタ。 */
    public InterWiki() {}
    
    /**
     * 属性を初期化するコンストラクタ。
     * @param siteName
     * @param url
     * @param encoding nullならデフォルト（Windows-31J）。
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
