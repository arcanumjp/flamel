		<!-- 
			‘‘‘‘‘‘‘‘‘‘‘‘‘
			TinyMCEΜέθ
			‘‘‘‘‘‘‘‘‘‘‘‘‘
		-->
		<script type="text/javascript" src="../click/tiny_mce/tiny_mce.js"></script>
		<script type="text/javascript">
			
			
						
			tinyMCE.init({
				// General options
				mode : "exact", 
				elements : "form_contents",				
				theme : "advanced",
				language : "ja",
				plugins : "safari,pagebreak,style,layer,table,save,advhr,advimage,advlink,emotions,iespell,inlinepopups,insertdatetime,preview,media,searchreplace,print,contextmenu,paste,directionality,fullscreen,noneditable,visualchars,nonbreaking,xhtmlxtras,template",
				
				// Theme options
				theme_advanced_buttons1 : "bold,italic,underline,strikethrough,|,justifyleft,justifycenter,justifyright,justifyfull,styleselect,formatselect,fontselect,fontsizeselect",
				theme_advanced_buttons2 : "cut,copy,paste,pastetext,pasteword,|,search,replace,|,bullist,numlist,|,outdent,indent,blockquote,|,undo,redo,|,link,unlink,anchor,image,cleanup,help|,insertdate,inserttime,preview,|,forecolor,backcolor",
				theme_advanced_buttons3 : "tablecontrols,|,hr,removeformat,visualaid,|,sub,sup,|,charmap,emotions,iespell,media,advhr,|,print,|,ltr,rtl,|,fullscreen",
				theme_advanced_buttons4 : "insertlayer,moveforward,movebackward,absolute,|,styleprops,|,cite,abbr,acronym,del,ins,attribs,|,visualchars",
				theme_advanced_toolbar_location : "top",
				theme_advanced_toolbar_align : "left",
				theme_advanced_statusbar_location : "bottom",
				theme_advanced_resizing : true,


				// Drop lists for link/image/media/template dialogs
				template_external_list_url : "lists/template_list.js",
				external_link_list_url : "lists/link_list.js",
				external_image_list_url : "lists/image_list.js",
				media_external_list_url : "lists/media_list.js",

				apply_source_formatting : true,
				fix_content_duplication  : true,
				fix_list_elements  : true,
				fix_table_elements  : true,
				fix_nesting  : true,
				content_css : "../default.css",
				body_class  : "flamel_content_editor",
				nonbreaking_force_tab : true,
				
				// Replace values for the template plugin
				template_replace_values : {
					username : "Some User",
					staffid : "991234"
				}
			});
			

		</script>
