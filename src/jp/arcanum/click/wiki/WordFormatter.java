package jp.arcanum.click.wiki;



import java.util.regex.Matcher;



/**
 * Wiki�P�������`���iHTML�Ȃǁj�ɐ��`����C���^�[�t�F�[�X�B
 */
public abstract class WordFormatter {
	


//    /**
//     * HTML�̓��ꕶ���i<,>,&,"�j���G�X�P�[�v����B
//     * @param s �G�X�P�[�v���镶����B
//     * @return �G�X�P�[�v����������B
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
//     * HTML�̓��ꕶ���i<,>,&,"�j���G�X�P�[�v��������B
//     * @param s �G�X�P�[�v�������镶����B
//     * @return �G�X�P�[�v��������������B
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
     * ���镶����̂Ȃ��̐��K�\�����A���Ɍ������A�u���N���X��p���Ď擾����
     * �u����̕�����Œu��������B
     * @param   s   �u�����镶����B
     * @param   rep �g�p����u���N���X�B
     * @return  �u����̕�����B
     */
    public static String replaceRegExp(String s, StringReplacer rep) {
        StringBuffer dst;
        Matcher m;
        boolean matched;
        int lastIndex;

        m = rep.getPattern().matcher(s);

        //  �ЂƂ�������Ȃ���΁A���̂܂ܕԂ�
        matched = m.find();
        if (!matched) {
            return s;
        }

        //  ���ɒu������
        dst = new StringBuffer();
        lastIndex = 0;
        while (matched) {
            dst.append(s.substring(lastIndex, m.start(0)));
            dst.append(rep.replace(m));
            lastIndex = m.end(0);

            //  ��������
            matched = m.find();
        }
        dst.append(s.substring(lastIndex));

        return dst.toString();
    }
    
    
    
	/**
	 * Wiki�P�������`���ɐ��`����B
	 * 1�s���Ƃ�formatLine���\�b�h���Ăяo���B
	 * 1�s���Ƃ̏������K�v�Ȃ���΁A�T�u�N���X�ŃI�[�o�[���C�h����B
	 * @param org ���`���̕�����B
	 * @return ���`����������B
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

		//	1�s����������
		for (int index = 0; index < pga.length;) {
			int i;
			int len;
			
			//	���s��T��
			for (i = index; i < pga.length && pga[i] != '\n'; i++) {
				//	nop
			}
			len = i - index;

			//	1�s�؂�o���ď���
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
	 * Wiki�P���1�s���A����`���ɐ��`����B
	 * @param line	���`���̕�����B
	 * @return ���`����������B
	 */
	protected abstract String formatLine(String line);
	
	/**
	 * ���`���J�n����Bformat���\�b�h����A�ŏ��Ɉ�x�����Ă΂��B
	 * @param org ���`���̕�����B
	 * @return ���`��̕�����̍ŏ��ɐݒ肷�镶����B
	 */
	protected abstract String start(String org);
	
	/**
	 * ���`���I������B���`���I���O�ɁA��x�����Ă΂��B
	 * @param formatted ���`��̕�����B
	 * @return ���`��̕�����̍Ō�ɒǉ����镶����B
	 */
	protected abstract String end(String formatted);
	
}
