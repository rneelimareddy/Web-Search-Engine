package com.queryProcessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import com.searchEngine.SearcEngine;

public class QueryProcessor {

	
		public static List hitList = new ArrayList();
		public static HashMap vectorScore = new HashMap();
	
	   //IndexSearcher searcher;
	 
	  // private static StandardAnalyzer analyzer;
	   
	   public QueryProcessor() {
		  
	}
	   
	public void processQuery(String query){
		
		
		SearcEngine.analyzer = new StandardAnalyzer(Version.LUCENE_36);
		 try {
			   // Query the created Index
			   String querystr = query;
			   // The "content" arg specifies the default field to use
			   // when no field is explicitly specified in the query.
			   Query q = new QueryParser(Version.LUCENE_36, "contents", SearcEngine.analyzer).parse(querystr);
			    
			   System.out.println("Inside Query Processor. Searching for :"+querystr);
			   // Do the actual search
			   int hitsPerPage = 200;
			    
			   //System.out.println("before search..");
			   TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
			   //System.out.println("before search1..");
			   //System.out.println("s:"+SearcEngine.searcher.doc(1).getFields());
			   //System.out.println("s:"+SearcEngine.searcher.doc(2).getFields());
			   SearcEngine.searcher.search(q, collector);
			   //System.out.println("before search2..");
			   ScoreDoc[] hits = collector.topDocs().scoreDocs;
			   //System.out.println("after search..");
			   // Display the results
			   System.out.println("\nFound " + hits.length + " hits.");
			   for (int i = 0; i < hits.length; ++i) {
			    int docId = hits[i].doc;
			    vectorScore.put(docId,hits[i].score);
			    hitList.add(docId);
			    Document d = SearcEngine.searcher.doc(docId);
			    System.out.println("DocumentId:"+docId);
			    System.out.println((i + 1) + ". " + d.get("title"));
			    System.out.println( "\t"+ d.get("path"));
			   }
			    
			   // Searcher can only be closed when there
			   // is no need to access the documents any more.
			   SearcEngine.searcher.close();
			  } catch (Exception e) {
			   System.out.println("Exception occured while searching..!");
			   e.printStackTrace();
			  }
			    }
	}
	

