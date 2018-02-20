package com.Indexer;

import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexFileNames;

import websphinx.Element;
import websphinx.Page;
import websphinx.Text;

import org.apache.lucene.document.*;
import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import com.crawler.PageFile;
import com.crawler.WebCrawler;
import com.searchEngine.SearcEngine;

public class Indexer {

	    // IndexWriter writer;
	     String filename="";
	public Indexer(){
		
		
		
	}
	
	public void index(){
		for(int i=0;i<WebCrawler.fileName;i++){
			filename= WebCrawler.pageFile[i].fileName;
			index(WebCrawler.pageFile[i].page);
			WebCrawler.pageFile[i].page.discardContent();
			WebCrawler.pageFile[i].page.getOrigin().setPage(null);
		}
	}
	
	public void index(Page p) {
        StringBuffer contents = new StringBuffer();
        p.removeLabel("sidebar");
        Document doc = new Document();
        doc.add(new Field("path", p.getURL().toString(), Field.Store.YES, Field.Index.ANALYZED));
        
        //doc.add(Field.Keyword("modified",DateField.timeToString(p.getLastModified())));
 
        if (p.getTitle() != null) {
        	doc.add(new Field("title", p.getTitle(), Field.Store.YES, Field.Index.ANALYZED));

        }
 
        /*System.out.println("    Indexing...");
        System.out.println("        filename:"+filename);
        System.out.println("        depth [" + p.getDepth() + "]");
        System.out.println("        title [" + p.getTitle() + "]");
        System.out.println("        modified [" + p.getLastModified() + "]");*/
        Element[] elements = p.getElements();
        for (int i = 0; i < elements.length; i++) {
            if (elements[i].getTagName().equalsIgnoreCase("meta")) {
                String name = elements[i].getHTMLAttribute("name", "");
                String content = elements[i].getHTMLAttribute("content", "");
                if (!name.equals("")) {
                	doc.add(new Field(name, content, Field.Store.YES, Field.Index.ANALYZED));
                    System.out.println("        meta [" + name + ":" + content + "]");
                }
            }
        }
        Text[] texts = p.getWords();
        for (int i = 0; i < texts.length; i++) {
            contents.append(texts[i].toText());
            contents.append(" ");
        }
        //System.out.println(contents.toString());
        doc.add(new Field("contents", contents.toString(), Field.Store.YES, Field.Index.ANALYZED));
        try {
        	System.out.println("no of fields in file"+doc.getFields().size());
        	SearcEngine.writer.addDocument(doc);
        } catch (IOException e) {
            throw new RuntimeException(e.toString());
        }
    }

}
