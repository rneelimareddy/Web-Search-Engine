package com.linkAnalysis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import websphinx.Crawler;

import com.crawler.PageFile;
import com.crawler.WebCrawler;
import com.database.DBHelper;
import com.searchEngine.SearcEngine;

public class LinkAnalysis {

	int noOfNodes=0;
	PageFile[] nodes = WebCrawler.pageFile;
	public static HashMap pageRank = new HashMap();
	public static LinkedHashMap nodeSort = new LinkedHashMap();
	public final double damp = 0.85;
	public final double epsilon = 0.01;
	public int iterationCount = 0;
	public List previousRank = new ArrayList();
	public final String tableName= "Page_Rank"; 
	
	public LinkAnalysis() {
		if(SearcEngine.isCrawlEnabled){
			noOfNodes = WebCrawler.fileName;
			System.out.println("noofnodes:"+noOfNodes);
			System.out.println("noofnodes:"+1.0/noOfNodes);
			System.out.println("noofnodes:"+1/noOfNodes);
			double initialPageRank = 1.0/noOfNodes;
			for(int i=0;i<noOfNodes;i++){
				pageRank.put(i,initialPageRank);
			}
			System.out.println("initialPageRank :"+initialPageRank);
			System.out.println("Iteration "+iterationCount+": "+pageRank);
		}
	}
	
	
	public HashMap constructRankMap(){
		double pageRankScore=0.0;
		
		while(true){
			if(iterationCount==0 || !isRankConverging()){
				addRankToList();
				for(int i=0;i<noOfNodes;i++){
					pageRankScore = calcPageRank(i);
					pageRank.put(i, pageRankScore);
				}
				iterationCount++;
				System.out.println("Iteration "+iterationCount+": "+pageRank);
			}else{
				break;
			}
		}
		
		sortNodeOnRank();
		storePageRank();
		return pageRank;
	}
	
	public void addRankToList(){
		previousRank = new ArrayList();
		for(int i=0;i<noOfNodes;i++){
			previousRank.add(pageRank.get(i));
		}
	}
	
	public boolean isRankConverging(){
		boolean isConvergent=true;
		for(int i=0;i<noOfNodes;i++){
			if(Math.abs(((Double)pageRank.get(i))-((Double)previousRank.get(i)))>=epsilon){
				isConvergent = false;
				break;
			}
		}
		
		return isConvergent;
		
	}
	
	public double calcPageRank(int node){
	
		double rankScore=0.0;
		double prByC=0.0;
		PageFile nodeDetails = nodes[node];
		int noOfInLinks = nodeDetails.sourceNodes.size();
		for(int i=0;i<noOfInLinks;i++){
			 int inNode = Integer.parseInt((String)nodeDetails.sourceNodes.get(i));
			 int c = nodeDetails.noOfOutLinks;
			prByC = prByC+(Double)previousRank.get(inNode)/c; 
		}
		rankScore = (1-damp) + damp*(prByC);
		return rankScore;
	}
	
	public void sortNodeOnRank(){
		
			//LinkedHashMap<Integer,Double> sortedMap = new LinkedHashMap();
			List mapValues = new ArrayList(pageRank.values());
			Set s = pageRank.keySet();
			Collections.sort(mapValues,Collections.reverseOrder());
			Iterator valueIter = mapValues.iterator();
			Iterator keyIter = s.iterator();
			int count=1;
			double tmp=0.0;
			while(valueIter.hasNext()){
				
				double value = (Double)valueIter.next();
				if(value!=tmp){
					keyIter = s.iterator();
				}
				tmp = value;
				while(keyIter.hasNext()){
					int key = (Integer)keyIter.next();
					if((Double)pageRank.get(key)==value){
						nodeSort.put(key, value);
						count++;
						break;
					}
				}
			}
			System.out.println("Sorted map: "+nodeSort);
	}
	
	public void storePageRank(){
		DBHelper db = new DBHelper();
		String[] fields = new String[2];
		  String[] data = new String[2];
		  fields[0] = "Node";
		  fields[1] = "Page_Rank";
		db.deleteTable(tableName);
		Iterator iter = pageRank.keySet().iterator();
		  while(iter.hasNext()){
			  int key = (Integer)iter.next();
			  data[0] = ((Integer)key).toString();
			  System.out.println("data:"+data[0]);
			  System.out.println("rank : "+(Double)pageRank.get(key));
			  data[1] = ((Double)pageRank.get(key)).toString();
			  db.insertTable(tableName, fields, data);
		  }	
	}
	
	public void getPageRankfromDB(){
		DBHelper db = new DBHelper();
		HashMap map = new HashMap();
		map = db.getTableData(tableName);
		Iterator iter = map.keySet().iterator();
		while(iter.hasNext()){
			String key = (String)iter.next();
			double value = Double.parseDouble((String)map.get(key));
			pageRank.put(Integer.parseInt(key), value) ;
		}
		sortNodeOnRank();
		
	}
	
}
