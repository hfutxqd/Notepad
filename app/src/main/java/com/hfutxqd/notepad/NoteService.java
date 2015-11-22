package com.hfutxqd.notepad;

import java.util.Map;
import java.util.List;
public interface NoteService {
	
	public boolean add(String title, String content);
	public boolean update(String id, String title, String content);
	public boolean delete(String id);
	public List<Map<String, Object>>  getList();
	public Map<String, Object> getItem(String id);
}
