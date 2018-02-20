package com.crawler;

import java.io.File;
import java.net.URL;
import java.security.cert.CRLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

import com.Indexer.Indexer;
import com.database.DBHelper;

import websphinx.CrawlListener;
import websphinx.Crawler;
import websphinx.DownloadParameters;
import websphinx.HTMLParser;
import websphinx.Link;
import websphinx.LinkListener;
import websphinx.Mirror;
import websphinx.Page;
import websphinx.PagePredicate;
import websphinx.workbench.LinkViewEvent;
import websphinx.workbench.LinkViewListener;
import websphinx.workbench.URLPredicate;
import websphinx.workbench.WebGraph;

public class WebCrawler extends Crawler{

	/**
	 * @param args
	 */
	public static int fileName=0;
	HashMap fileMap = new HashMap();
	Link[] visitedLinks = new Link[5000];
	public static PageFile[] pageFile = new PageFile[5000];
	HashMap graphMap = new HashMap();
	String tableName="web_graph";
	
	Indexer indexer = new Indexer();
	public WebCrawler() {
		// TODO Auto-generated method stub
		//WebCrawler crawl = new WebCrawler();
		
		try{
		Link[] link = new Link[2];
		link[0] = new Link("http://www.careercup.com/");
		link[1] = new Link("http://www.glassdoor.com/");
		//WebGraph web = new WebGraph();
		//addCrawlListener(web);
		//web.getg
		setRoots(link);
		setDepthFirst(true);
		setMaxDepth(2);
		
		//crawl.setIgnoreVisitedLinks(true);
		System.out.println("Crawler started...!");
		this.setSynchronous(true);
        this.setDomain(Crawler.SERVER);
		run();
		int pagesVisited=getPagesVisited();
		System.out.println("No of pages visited: "+pagesVisited);
		System.out.println("Number of roots: "+getRoots().length);
		//Page page;
		System.out.println("Number of visited Pages: "+(fileName-1));
		
		/*DownloadParameters dp = new DownloadParameters();
		LinkListener listen;
		WebGraph webG = new WebGraph();
		LinkViewListener listener;
		listener.viewLink(new LinkViewEvent("", link).getLink());
		webG.addLinkViewListener(listener);
		crawl.addLinkListener(listen);
		listen.
		dp.*/
		//Mirror mirror = new Mirror("E:\\Data\\Education\\workspaces\\InfoRet\\SEO\\VisitedPages\\");
		Link [] links = new Link[pagesVisited];
		
		links = getCrawledRoots();
		
		/*for(int i=0;i<1;i++){
			
			page = new Page(links[i]);
			mirror.writePage(page);
		}*/
		System.out.println("Pages written to directory...!");
		System.out.println("Crawled...!");
		constructChildMap();
		displayMap();
		//this.clearVisited();
		//this.clear();
		//System.out.println("FileMap: "+crawl.fileMap);
		}catch(Exception e){
			System.out.println("Exception occured. Happy debugging...! :-) ");
			e.printStackTrace();
		}

	}
	
	protected void doVisit(Page page) {
	    URL url = page.getURL();
	    try {
	    	String targetDir  = "E:\\Data\\Education\\workspaces\\InfoRet\\SearchEngine\\VisitedPages\\";
	      //String path = url.getPath().replaceFirst("/", "");
	    	String path = ((Integer)fileName).toString();
	    	fileMap.put(path, url);
	    	System.out.println("------");
	    	System.out.println("Visiting : "+fileName);
	    	System.out.println("page:"+url);
	    	//System.out.println("Source Page:"+page.getSource().getURL());
	    	//System.out.println("depth:"+page.getDepth());
	    	//System.out.println("No of links:"+page.getLinks().length);
	    	//System.out.println("------");
	    	int noOfLinks = page.getLinks().length;
	    	
	    	//Link[] links = page.getLinks();
	    	//page.isHTML();
	    	//PagePredicate pagePredicate = this.getPagePredicate();
	    	//int count=0;
	    	/*for(int i=0;i<noOfLinks;i++){
	    		System.out.println("Link"+i+": "+links[i].getURL());
	    	}*/
	    	//System.out.println("count of act links:"+count);
	    	//System.out.println("Origin:"+page.getOrigin().getParentURL());
	    	

	      //System.out.println("path:"+path);
	      if (StringUtils.isNotEmpty(path)) {
	        String targetPathName = FilenameUtils.concat(targetDir, path);
	        //System.out.println("targetPathName:"+targetPathName);
	        File targetFile = new File(targetPathName);
	        //System.out.println("targetFile:"+targetFile);
	        File targetPath = new File(FilenameUtils.getPath(targetPathName));
	        //System.out.println("targetPath:"+targetPath);
	        if (! targetPath.exists()) {
	          FileUtils.forceMkdir(targetPath);
	        }
	        FileUtils.writeByteArrayToFile(targetFile, page.getContentBytes());
	        pageFile[fileName] = new PageFile();
	        //pageFile[fileName].page = page;
	        indexer.index(page);
	        pageFile[fileName].fileName=path;
	    	pageFile[fileName].url=url;
	    	pageFile[fileName].childLinks=page.getLinks();
	    	fileName++;
	    	//page.discardContent();
	      }
	    } catch (Exception e) {
	      System.out.println("Could not download url:" + url.toString()+e);
	    }
	}

	  public void visit(Page page) {
		    doVisit(page);
		    page.getOrigin().setPage(null);
		    page.discardContent();
		    if(fileName>1500){
		    	stop();
		    }
		    //System.out.println("Url:"+page.getURL());
		    try {
		      Thread.sleep(1000L);
		    } catch (InterruptedException e) {;}
		    
		  }
	  
	  public void constructChildMap(){

		  for(int i=fileName-1;i>=0;i--){
			  for(int j=0;j<fileName;j++){
				if(isChild(pageFile[i].url,pageFile[j].childLinks)){
					pageFile[i].sourceNodes.add(pageFile[j].fileName);
					pageFile[j].outgoingNodes.add(pageFile[i].fileName);
					graphMap.put(pageFile[j].fileName.toString(), pageFile[i].fileName.toString());
					pageFile[j].noOfOutLinks++;
				}
			  }
		  }
	  }
	  
	  public boolean isChild(URL url,Link[] childLinks){
		  boolean isChild=false;
		  for(int i=0;i<childLinks.length;i++){
			  if(url.equals(childLinks[i].getURL())){
				  isChild = true;
				  break;
			  }
		  }
		  return isChild;
	  }
	  
	  public void displayMap(){
		  System.out.println("Node Info:");
		  System.out.println("----------");
		  for(int i=0;i<fileName;i++){
			  System.out.println(pageFile[i].fileName+", Source:"
					  +pageFile[i].sourceNodes+", OutNodes:"+pageFile[i].outgoingNodes);
			  
		  }
	  }
	  
	  public void storeWebGraph(){
		  DBHelper db = new DBHelper();
		  String[] fields = new String[2];
		  String[] data = new String[2];
		  fields[0] = "Source_node";
		  fields[1] = "Dest_node";
		  db.deleteTable(tableName);
		  Iterator iter = graphMap.keySet().iterator();
		  while(iter.hasNext()){
			  data[0] = (String)iter.next();
			  data[1] = (String)graphMap.get(data[0]);
			  db.insertTable(tableName, fields, data);
		  }
	  }
}


