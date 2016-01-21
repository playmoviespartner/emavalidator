package com.emavalidator.appengine.server;

import java.io.FileReader;
import java.io.IOException;
import java.nio.CharBuffer;
import java.util.UUID;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;

public class Init extends HttpServlet {
	private static final long serialVersionUID = -1591554032328557188L;

	@Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String uniqueID = UUID.randomUUID().toString();
        System.out.println("doGet");
        ChannelService channelService = ChannelServiceFactory.getChannelService();
        String token = channelService.createChannel(uniqueID);
        
        BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

        String url = blobstoreService.createUploadUrl("/emavalidatorappengine/upload");
        
    	FileReader reader = new FileReader("index.html");
        CharBuffer buffer = CharBuffer.allocate(16384);
        reader.read(buffer);
        String index = new String(buffer.array());
        index = index.replaceAll("\\{\\{ token \\}\\}", token);
        index = index.replaceAll("\\{\\{ form_action_url \\}\\}", url);
        reader.close();
        resp.setContentType("text/html");
        resp.getWriter().print(index);
    }
}
