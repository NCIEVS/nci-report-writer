(1) Check out ncireportwriter from github:
    git clone https://github.com/NCIEVS/nci-report-writer.git
(2) Drop the inferred version of the NCIt and the neoplasm core value set xls file (for example, ThesaurusInferred_16.08e.owl and Neoplasm_Core_16.08e.xls) to the neoplasmcore folder.
(3) Check out nci-term-browser from github:
    git clone https://github.com/NCIEVS/nci-term-browser.git 
(4) Modify run.bat to match with your java environment and the two input file names above.
    Note: Include the ncibrowser/lib and ncibrowser/extlib in your classpath.
(5) Click on run.bat and the neoplasm core package will be created in the output folder with a timestamp yyyy-MM-dd, (for example, output_2016-09-19).    

