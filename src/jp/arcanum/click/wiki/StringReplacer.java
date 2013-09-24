package jp.arcanum.click.wiki;


import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * ���鐳�K�\�����}�b�`���镶������A�ʂ̕�����ɒu��������N���X��
 * �����C���^�[�t�F�[�X�B
 */
public interface StringReplacer {
    /**
     * ���̃N���X�������Ɏg�����K�\���̃p�^�[����Ԃ��B
     * @return ���K�\���B
     */
    public Pattern getPattern();
    
    /**
     * ���鐳�K�\���̃}�b�`��ʂ̕�����ɒu��������B
     * @param   m   �u�����̐��K�\���̃}�b�`�B
     * @return  �u����̕�����B
     */
    public String replace(Matcher m);

}