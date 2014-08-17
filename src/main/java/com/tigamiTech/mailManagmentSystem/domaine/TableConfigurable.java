package com.tigamiTech.mailManagmentSystem.domaine;

import java.util.List;

public interface TableConfigurable {
	public List<String> getNestedProperties();
    public String[] getVisibleColumns();
    public String[] getColumnHeaders();
    public String getCaption();
    public String[] getColumnsInEditorView();
    public String getDetail();
}
