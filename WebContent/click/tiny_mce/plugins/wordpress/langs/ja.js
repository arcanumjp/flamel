// EN lang variables

if (navigator.userAgent.indexOf('Mac OS') != -1) {
// Mac OS browsers use Ctrl to hit accesskeys
	var metaKey = 'Ctrl';
}
else if (navigator.userAgent.indexOf('Firefox/2') != -1) {
// Firefox 2.x uses Alt+Shift to hit accesskeys
	var metaKey = 'Alt+Shift';
}
else {
	var metaKey = 'Alt';
}

tinyMCE.addToLang('',{
wordpress_more_button : '投稿をMoreタグで分割 (' + metaKey + '+t)',
wordpress_page_button : '投稿をPageタグで分割',
wordpress_adv_button : 'アドバンストツールバーの表示/非表示 (' + metaKey + '+v)',
wordpress_more_alt : 'さらに表示...',
wordpress_page_alt : '...ページ...',
help_button_title : 'ヘルプ (' + metaKey + '+h)',
bold_desc : '太字 (Ctrl+B)',
italic_desc : '斜体 (Ctrl+I)',
underline_desc : '下線 (Ctrl+U)',
link_desc : 'ハイパーリンクの挿入/編集 (' + metaKey + '+a)',
unlink_desc : 'ハイパーリンクの削除 (' + metaKey + '+s)',
image_desc : '画像の挿入/編集 (' + metaKey + '+m)',
striketrough_desc : '打ち消し線 (' + metaKey + '+k)',
justifyleft_desc : '左揃え (' + metaKey + '+f)',
justifycenter_desc : '中央揃え (' + metaKey + '+c)',
justifyright_desc : '右揃え (' + metaKey + '+r)',
justifyfull_desc : '均等割り付け (' + metaKey + '+j)',
bullist_desc : '番号なしリスト (' + metaKey + '+l)',
numlist_desc : '番号付きリスト (' + metaKey + '+o)',
outdent_desc : 'インデント解除 (' + metaKey + '+w)',
indent_desc : 'インデント リスト/引用 (' + metaKey + '+q)'
});
