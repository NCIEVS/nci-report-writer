package gov.nih.nci.evs.reportwriter.utils;

import java.io.*;

public class MyFileOutputStream {
	private static final String TAB = "  ";
	private FileOutputStream _out = null;
	private String _tab = TAB;
	private String _indentation = "";
	
	public MyFileOutputStream(String filename) throws Exception {
		_out = new FileOutputStream(filename);
	}

    public void close() throws Exception {
    	_out.close();
    }
    
    public void setTab(String tab) {
    	_tab = tab;
    }

    public void write(String text) throws Exception {
    	_out.write(text.getBytes());
    }
    
    public void writeln(String text) throws Exception {
    	write(text + "\n");
    }
    
    public String indent() {
    	_indentation += _tab;
    	return _indentation;
    }
    
    public String undent() {
    	if (_indentation.length() > 0)
    		_indentation = _indentation.substring(_tab.length());
    	return _indentation;
    }

    public void writeln_indent(String text) throws Exception {
    	writeln(indent() + text);
    }
    
    public void writeln_undent(String text) throws Exception {
    	writeln(undent() + text);
    }
    
    public void writeln_normal(String text) throws Exception {
    	writeln(_indentation + text);
    }
}
