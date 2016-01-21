package com.emavalidator.appengine.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreInputStream;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.channel.ChannelMessage;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import com.google.appengine.api.taskqueue.DeferredTask;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.RetryOptions;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.apphosting.api.DeadlineExceededException;

import emavalidator.EMAResourceValidator;
import emavalidator.ErrorLog;
import emavalidator.ValidatorResults;

public class Upload extends HttpServlet
{
    private static final long serialVersionUID = -5765746662457153466L;

    /**
     * Number of ms long we will arbitrarily delay.
     */
    static final int DELAY_MS = 5000;
    public static class ExpensiveOperation implements DeferredTask {
    	/**
		 * 
		 */
		private static final long serialVersionUID = 7380972860221869462L;
		private BlobKey blobKey = null;
		private String channelIDKey = "";
        public ExpensiveOperation(BlobKey blobKey, String channelIDKey) {
        	this.blobKey = blobKey;
        	this.channelIDKey = channelIDKey;
        }
    	
    	@Override
        public void run() {
    		try {
	            // expensive operation to be backgrounded goes here
	        	ChannelService channelService = ChannelServiceFactory.getChannelService();
				
//	            BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
	            BlobstoreInputStream blobInputStream = new BlobstoreInputStream(blobKey);
	            BlobInfoFactory blobInfoFactory = new BlobInfoFactory();
	            BlobInfo blobFileInfo = blobInfoFactory.loadBlobInfo(blobKey);
	            String fileName = blobFileInfo.getFilename().toLowerCase();
        		ValidatorResults validatorResults;
	            
	            if(fileName.endsWith(".txt") || fileName.endsWith(".csv")) // if the input file is text formatted. either CSV or txt file is fine since both would have the same encoding scheme
	            {
	                try
	                {
	                    BufferedReader bufferedCSVReader = new BufferedReader(new InputStreamReader(blobInputStream, "UTF-8"));
	                    validatorResults = EMAResourceValidator.validateEMACSVFile(fileName, bufferedCSVReader, "log");
	                }
	                catch (IOException IOE) // catch text related exceptions here only. throw them to the surrounding catch clause with context.
	                {
                        blobInputStream.close(); // close the input file to prevent resource leak
	                    IOE.printStackTrace();
	                    throw new IllegalArgumentException("ERROR PARSING CSV: " + IOE.getMessage());
	                }
	            }
	            else if(fileName.endsWith(".xls") || fileName.endsWith("xlsx")) // if the input file is a workbook. either XLS or XLSX is fine
	            {
	                try
	                {
//	                    formattedOutput = EMAResourceValidator.validateEMAWorkbook(WorkbookFactory.create(blobInputStream), "log");
	                    validatorResults = EMAResourceValidator.validateEMAXLSXAsCSV(blobInputStream, "log");
	                }
	                catch (Exception e) // catch Apache POI or spreadsheet-based exceptions here only. throw them to the surrounding catch clause with context.
	                {
	                    blobInputStream.close(); // close the input file to prevent resource leak
	                    throw new IllegalArgumentException("Input workbook was corrupted. Try using Google Sheets or Excel instead. This has only been experienced with Libre Office 4.2.7.2 on Ubuntu 14.04");
	                }
	            }
	            else if(fileName.endsWith(".xml")) {
	            	try
	            	{
	            		validatorResults = EMAResourceValidator.validateEMAXML(fileName, blobInputStream, "log");
	            	}
	            	catch (Exception e) 
	            	{
	                    blobInputStream.close(); // close the input file to prevent resource leak
	                    throw new IllegalArgumentException("ERROR VALIDATING XML: " + e.getMessage());
	            	}
	            }
	            else // any other file ending or extension should be caught here. this should never get executed as front-end javascript validation should prevent any other file types / mimes / extensions from making it this far
	            {
	                blobInputStream.close(); // close the input file to prevent resource leak
	                throw new IllegalArgumentException("Unsupported file type. Please choose a supported file type and try again.");
	            }

	            int totalErrorColumnsCount = ErrorLog.getTotalErrorColumnsCount();
	            String resultsToSend;
	            
	            if (validatorResults.getFileType().equals("xml"))
	            {
	            	resultsToSend = validatorResults.getFileType() + "`" + validatorResults.isXmlValid() + "`" + validatorResults.getResults();
	            }
	            else
	            {
	            	resultsToSend = validatorResults.getFileType() + "`" + totalErrorColumnsCount + "`" + validatorResults.getResults();
	            }
	            
	            channelService.sendMessage(new ChannelMessage(channelIDKey, resultsToSend));
    		} 
    		catch (Exception e) { 
    			e.printStackTrace();
    			String resultsToSend = "error`Error.`Sorry. An Error has occured: " + e.getMessage();
	        	ChannelService channelService = ChannelServiceFactory.getChannelService();

	            channelService.sendMessage(new ChannelMessage(channelIDKey, resultsToSend));
    		}
        }
    }
    
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
    {
        try
        {
            boolean badFileName = false;
            BlobKey blobKey = null;
            String channelIDKey = "";
            Queue queue = QueueFactory.getDefaultQueue();
            RetryOptions retry = RetryOptions.Builder.withTaskRetryLimit(1); 

            Map<String, List<BlobKey>> uploadedBlobs = BlobstoreServiceFactory.getBlobstoreService().getUploads(req);

            try
            {
                blobKey = uploadedBlobs.get("inputFile").get(0);
                channelIDKey = req.getParameter("channelID");

                // Build a DeferredTask to run, with retry options set to 1 retry
                TaskOptions task = TaskOptions.Builder.withPayload(new ExpensiveOperation(blobKey, channelIDKey)).retryOptions(retry);
                
                // Add the task to the queue.
                queue.add(task);
            }
            catch (NullPointerException NPE) { badFileName = true; }

            if (badFileName)
                throw new IllegalArgumentException("File retrieval failed. Please make sure there are no symbols in the file name and try again.");

            if (blobKey == null)
                throw new IllegalArgumentException("File upload failed. Double check your connection and try again.");
        }
        catch (NullPointerException | IndexOutOfBoundsException EXCEPTION)
        {
            EXCEPTION.printStackTrace();
            res.getWriter().print("Please verify that no extra worksheets are submitted in the workbook and that they are using correctly formatted EMA structure. Then submit again.");
        }
        catch (IllegalArgumentException IAE)
        {
            IAE.printStackTrace();
            res.getWriter().println(IAE.getMessage());
        }
        catch (DeadlineExceededException DEE)
        {
        	DEE.printStackTrace();
        	res.getWriter().print("Timeout");
        }
        catch (Exception E)
        {
        	E.printStackTrace();
        	res.getWriter().print(Upload.getErrorOutputString(E, "Please give feedback to the appropriate channels with the information below. To validate immediately, please revert to the exact EMA template(s) as linked to on the Movie Labs page."));
        }
    }

    private static String getErrorOutputString(Exception e, String description)
    {
        StringBuilder returnThis = new StringBuilder();
        returnThis.append(e.getClass().getName());
        returnThis.append(": That's an error. ");
        if(description != null)
            returnThis.append(description);
        returnThis.append(System.lineSeparator());
        returnThis.append(Upload.getPrintableStacktraceString(e.getStackTrace()));
        return returnThis.toString();
    }

    private static String getPrintableStacktraceString(StackTraceElement[] stackTraceElements)
    {
        StringBuilder returnThis = new StringBuilder();
        for(StackTraceElement ste : stackTraceElements)
            returnThis.append(ste.toString() + System.lineSeparator());
        return returnThis.toString();
    }
}