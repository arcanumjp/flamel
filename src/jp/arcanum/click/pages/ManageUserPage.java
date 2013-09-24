package jp.arcanum.click.pages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.click.control.Column;
import net.sf.click.control.PasswordField;
import net.sf.click.control.Submit;
import net.sf.click.control.Table;
import net.sf.click.control.TextField;
import net.sf.click.extras.control.FieldColumn;
import net.sf.click.extras.control.FormTable;

public class ManageUserPage extends AbstractToolPage {

	
	public FormTable table = new FormTable();
	
	private Submit ok = new Submit("ok", "更新", this, "onClickOk");
	private Submit back = new Submit("back", "戻る", this, "onClickBack");
	
	public ManageUserPage(){
		
        table.setClass(Table.CLASS_SIMPLE);
        table.setWidth("700px");

		table.addColumn(new Column("id","ID"));

        FieldColumn colpass = new FieldColumn("pass", "パスワード", new PasswordField());
        colpass.getField().setRequired(true);
        table.addColumn(colpass);

		
        FieldColumn colgroup = new FieldColumn("group", "グループ", new TextField());
        colgroup.getField().setRequired(true);
        table.addColumn(colgroup);
		
		table.getForm().add(ok);
		table.getForm().add(back);
		
		
		
	}
	
	public void onPost(){
		
		List list = new ArrayList();

		
		Map r1 = new HashMap();
		r1.put("id", "hogehoge");
		r1.put("pass", "5963");
		r1.put("group", "group1,group2");
		list.add(r1);
		
		Map r2 = new HashMap();
		r2.put("id", "hogehoge");
		r2.put("pass", "5963");
		r2.put("group", "group1,group2");
		table.setRowList(list);
		list.add(r2);
		
		
	}
	
	
	public boolean onClickOk(){
		
		List list = table.getRowList();
		
		
		return true;
	}

	
	public boolean onClickBack(){
		
		super.setForward(ToolMainPage.class);
		
		return true;
	}
	
	public void onRender(){
		super.onRender();

	}
	
}
