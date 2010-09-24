package gov.nih.nci.evs.reportwriter.utils;

public class TabFormatterBase {
    private static final String TAB = "  ";
    private String _tab = TAB;
    private String _indentation = "";

    public void setTab(String tab) {
        _tab = tab;
    }

    public String write(String text) throws Exception {
        if (text == null)
            text = "";
        text = _indentation + text;
        //text = String.format("%4d: ", _indentation.length()) + text;
        return text;
    }

    public void writeln(String text) throws Exception {
        write(text + "\n");
    }
    
    public void writeln() throws Exception {
        writeln("");
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
        indent();
        writeln(text);
    }

    public void writeln_undent(String text) throws Exception {
        writeln(text);
        undent();
    }

    public void writeln_normal(String text) throws Exception {
        writeln(text);
    }
    
    public void writeln_inden1(String text) throws Exception {
        indent();
        writeln(text);
        undent();
    }
}
