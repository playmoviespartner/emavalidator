package emavalidator;

public class ValidatorResults
{
    private String results;
    private String fileType;
    private boolean isXmlValid;
    
    public ValidatorResults() 
    {
        results = "";
        fileType = "";
        isXmlValid = false;
    }
    public ValidatorResults(String results, String fileType, boolean isXmlValid)
    {
        this.results = results;
        this.fileType = fileType;
        this.isXmlValid = isXmlValid;
    }
    
    public void setResults(String results)
    {
        this.results = results;
    }
    public void setFileType(String fileType)
    {
        this.fileType = fileType;
    }
    public void setIsXmlValid(boolean isXmlValid)
    {
        this.isXmlValid = isXmlValid;
    }
    
    public String getResults()
    {
        return this.results;
    }
    public String getFileType()
    {
        return this.fileType;
    }
    public boolean isXmlValid()
    {
        return this.isXmlValid;
    }
}
