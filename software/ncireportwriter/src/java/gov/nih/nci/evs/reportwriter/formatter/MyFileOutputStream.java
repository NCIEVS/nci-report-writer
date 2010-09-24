package gov.nih.nci.evs.reportwriter.formatter;


import java.io.*;

public class MyFileOutputStream extends TabFormatterBase 
{
	private String _filename = null;
	private FileOutputStream _out = null;

	public MyFileOutputStream(String filename) throws Exception {
		_filename = filename;
		_out = new FileOutputStream(filename);
	}

	public String getFilename() {
		File file = new File(_filename);
		String name = file.getName();
		return name;
	}

	public void close() throws Exception {
		_out.close();
	}

    public String write(String text) throws Exception {
        text = super.write(text);
        _out.write(text.getBytes());
        return text;
    }

}
