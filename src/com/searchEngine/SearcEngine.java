package com.searchEngine;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.FieldSelector;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.FieldInfos;
import org.apache.lucene.index.FilterIndexReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriter.IndexReaderWarmer;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermDocs;
import org.apache.lucene.index.TermEnum;
import org.apache.lucene.index.TermFreqVector;
import org.apache.lucene.index.TermPositions;
import org.apache.lucene.index.TermVectorMapper;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import com.Indexer.Indexer;
import com.crawler.WebCrawler;
import com.linkAnalysis.LinkAnalysis;
import com.queryProcessor.QueryProcessor;
import com.retrieval.QueryResult;
import com.retrieval.Retrieval;

public class SearcEngine {

	/**
	 * @param args
	 */
	
	public static StandardAnalyzer analyzer;
	public static Directory index;
	public static IndexWriter writer;
	public static IndexSearcher searcher;
	public static boolean isCrawlEnabled = true;

	
	public static void main(String args[]){
		String query = "Amazon";
		QueryResult[] result = SearcEngine.search(query);
		System.out.println("3");
		for(int i=0;i<Retrieval.hitListCounter;i++){
			
			int index = i+1;
			String title = result[i].getTitle();
			System.out.println(i+":"+title);
			String url = result[i].getUrl();
			System.out.println(i+":"+url);
		}
	}
	
	
	public static QueryResult[] search(String query) {
		// TODO Auto-generated method stub

		//String query = "riverbed";
		File path = new File("E:\\Data\\Education\\workspaces\\InfoRet\\SearchEngine\\index\\");
		System.out.println("hi");
		if(SearcEngine.isCrawlEnabled){
			
			SearcEngine.analyzer = new StandardAnalyzer(Version.LUCENE_36);
		     System.out.println("Index1:"+SearcEngine.index);
		     //SearcEngine seo = new SearcEngine();
		     try {
		    	 
		    	 //SearcEngine.index = new RAMDirectory();
		    	 deleteDir(path);
		    	 SearcEngine.index = FSDirectory.open(path);
		    	 
		    	 System.out.println("Index2:"+SearcEngine.index);
		    	 SearcEngine.writer = new IndexWriter(SearcEngine.index, new IndexWriterConfig(Version.LUCENE_36, SearcEngine.analyzer));
		    	 SearcEngine.writer.commit();
		    	 //seo.writer.setMergeFactor(20);
		    	 
			System.out.println("Index3:"+SearcEngine.index);
			System.out.println("I:"+SearcEngine.writer);
			WebCrawler crawler = new WebCrawler();
			crawler.storeWebGraph();
			//Indexer indexer = new Indexer();
			//indexer.index();
			System.out.println("Indexing done....!");
			SearcEngine.writer.commit();
			SearcEngine.writer.close();
			LinkAnalysis link = new LinkAnalysis();
			link.constructRankMap();
			//System.out.println(SearcEngine.writer);
			//System.out.println("no of docs in writer :"+SearcEngine.writer.maxDoc());
			//System.out.println("index in writer:"+SearcEngine.writer.getDirectory().listAll().length);
			
		     } catch (Exception e) {
					
					e.printStackTrace();
				}
	    }else{
	    	//construct rankMap
	    	LinkAnalysis link = new LinkAnalysis();
	    	link.getPageRankfromDB();
	    }
		try{
			Directory index1 = FSDirectory.open(path);
			SearcEngine.searcher = new IndexSearcher(IndexReader.open(index1));
			//System.out.println("s:"+SearcEngine.searcher.getIndexReader().maxDoc());
			//System.out.println("no of docs in searcher :"+SearcEngine.searcher.maxDoc());
			//System.out.println("1 doc in searcher :"+SearcEngine.searcher.doc(0));
			QueryProcessor queryProc = new QueryProcessor();
			queryProc.processQuery(query);
			Retrieval ret = new Retrieval();
			ret.showResults();
			return ret.rankedHitList;
	     } catch (Exception e) {
				
				e.printStackTrace();
			}
		return null;
	}

	public static boolean deleteDir(File dir) {
	    if (dir.isDirectory()) {
	        String[] children = dir.list();
	        for (int i=0; i<children.length; i++) {
	            boolean success = deleteDir(new File(dir, children[i]));
	            if (!success) {
	                return false;
	            }
	        }
	    }

	    // The directory is now empty so delete it
	    return dir.delete();
	}
	
}
