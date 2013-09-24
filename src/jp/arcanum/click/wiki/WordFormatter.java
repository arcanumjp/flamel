package jp.arcanum.click.wiki;



import java.util.regex.Matcher;



/**
 * Wiki単語をある形式（HTMLなど）に整形するインターフェース。
 */
public abstract class WordFormatter {
	


//    /**
//     * HTMLの特殊文字（<,>,&,"）をエスケープする。
//     * @param s エスケープする文字列。
//     * @return エスケープした文字列。
//     */
//    public static String escape(String s) {
//        StringBuffer sb = new StringBuffer();
//
//        for (int i = 0; i < s.length(); i++) {
//            char c = s.charAt(i);
//
//            switch (c) {
//                case '<' :
//                    sb.append("&lt;");
//                    break;
//                case '>' :
//                    sb.append("&gt;");
//                    break;
//                case '&' :
//                    sb.append("&amp;");
//                    break;
//                case '\"' :
//                    sb.append("&quot;");
//                    break;
//                default :
//                    sb.append(c);
//                    break;
//            }
//        }
//
//        return sb.toString();
//    }
    
//    /**
//     * HTMLの特殊文字（<,>,&,"）をエスケープ解除する。
//     * @param s エスケープ解除する文字列。
//     * @return エスケープ解除した文字列。
//     */
//    public static String unescape(String s) {
//        StringBuffer sb = new StringBuffer(s);
//
//        for (int i = 0; i < ESCAPED_STRINGS.length; i++) {
//            int p;
//
//            while ((p = sb.indexOf(ESCAPED_STRINGS[i])) != -1) {
//                sb.replace(
//                    p,
//                    p + ESCAPED_STRINGS[i].length(),
//                    ESCAPE_CHARS[i]);
//            }
//        }
//
//        return sb.toString();
//    }

    
    /**
     * ある文字列のなかの正規表現を、順に検索し、置換クラスを用いて取得した
     * 置換後の文字列で置き換える。
     * @param   s   置換する文字列。
     * @param   rep 使用する置換クラス。
     * @return  置換後の文字列。
     */
    public static String replaceRegExp(String s, StringReplacer rep) {
        StringBuffer dst;
        Matcher m;
        boolean matched;
        int lastIndex;

        m = rep.getPattern().matcher(s);

        //  ひとつも見つからなければ、そのまま返す
        matched = m.find();
        if (!matched) {
            return s;
        }

        //  順に置換する
        dst = new StringBuffer();
        lastIndex = 0;
        while (matched) {
            dst.append(s.substring(lastIndex, m.start(0)));
            dst.append(rep.replace(m));
            lastIndex = m.end(0);

            //  次を検索
            matched = m.find();
        }
        dst.append(s.substring(lastIndex));

        return dst.toString();
    }
    
    
    
	/**
	 * Wiki単語をある形式に整形する。
	 * 1行ごとにformatLineメソッドを呼び出す。
	 * 1行ごとの処理が必要なければ、サブクラスでオーバーライドする。
	 * @param org 整形元の文字列。
	 * @return 整形した文字列。
	 */
	public String format(String org) {
		StringBuffer sb;
		String s;
		char[] pga = org.toCharArray();
		
		sb = new StringBuffer();
		
		s = start(org);
		if (s != null) {
			sb.append(s);
		}

		//	1行ずつ処理する
		for (int index = 0; index < pga.length;) {
			int i;
			int len;
			
			//	改行を探す
			for (i = index; i < pga.length && pga[i] != '\n'; i++) {
				//	nop
			}
			len = i - index;

			//	1行切り出して処理
			if (len == 0) {
				sb.append(formatLine(""));
			} else {
				char[] la = new char[len];
				System.arraycopy(pga, index, la, 0, len);
				
				sb.append(formatLine(new String(la)));
			}

			if (i < pga.length) {
				sb.append("\n");
			}

			index = i + 1;
		}
		
		s = end(sb.toString());
		if (s != null) {
			sb.append(s);
		}

		return sb.toString();
	}
	
	/**
	 * Wiki単語の1行を、ある形式に整形する。
	 * @param line	整形元の文字列。
	 * @return 整形した文字列。
	 */
	protected abstract String formatLine(String line);
	
	/**
	 * 整形を開始する。formatメソッドから、最初に一度だけ呼ばれる。
	 * @param org 整形元の文字列。
	 * @return 整形後の文字列の最初に設定する文字列。
	 */
	protected abstract String start(String org);
	
	/**
	 * 整形を終了する。整形を終わる前に、一度だけ呼ばれる。
	 * @param formatted 整形後の文字列。
	 * @return 整形後の文字列の最後に追加する文字列。
	 */
	protected abstract String end(String formatted);
	
}
