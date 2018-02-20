package com.retrieval;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import com.linkAnalysis.LinkAnalysis;
import com.queryProcessor.QueryProcessor;
import com.searchEngine.SearcEngine;

public class Retrieval {


	public static QueryResult[] rankedHitList = new QueryResult[100];
	public static int hitListCounter=0;
	public static List pageRankList = new ArrayList();
	public static HashMap finalScore = new HashMap();
	public static final double w=0.7;
	
	public void showResults(){
		
		combineHitListWithPageRank();
		rankHitListWithPageRank();
		displayHitlist();
	}
	
	public void displayHitlist(){
		
		System.out.println("Doc ids of Vector Model based ranking: "+QueryProcessor.hitList);
		System.out.println("Doc ids of Connectivity based ranking: "+pageRankList);
		System.out.println("Doc ids of combined ranking: ");
		for(int i=0;i<hitListCounter;i++){
			System.out.print(rankedHitList[i].docId+", ");
		}
		
	}
	
	public void combineScore(){
		List hitList = QueryProcessor.hitList;
		Iterator iter = hitList.iterator();
		double combinedScore=0.0;
		while(iter.hasNext()){
			int docId = (Integer)iter.next();
			double vectorScore = (Float)QueryProcessor.vectorScore.get(docId);
			double pageRank = (Double)LinkAnalysis.pageRank.get(docId);
			combinedScore = w*vectorScore + (1.0-w)*pageRank;
			finalScore.put(docId, combinedScore);
		}
	}
	
	public void combineHitListWithPageRank(){
		combineScore();
		//LinkedHashMap<Integer,Double> sortedMap = new LinkedHashMap();
		List mapValues = new ArrayList(finalScore.values());
		Set s = finalScore.keySet();
		Collections.sort(mapValues,Collections.reverseOrder());
		Iterator valueIter = mapValues.iterator();
		Iterator keyIter = s.iterator();
		
		double tmp=0.0;
		try{
		while(valueIter.hasNext()){
			
			double value = (Double)valueIter.next();
			if(value!=tmp){
				keyIter = s.iterator();
			}
			tmp = value;
			while(keyIter.hasNext()){
				int docId = (Integer)keyIter.next();
				if((Double)finalScore.get(docId)==value){
					rankedHitList[hitListCounter] = new QueryResult();
					rankedHitList[hitListCounter].setDocId(docId);
					rankedHitList[hitListCounter].setTitle(SearcEngine.searcher.doc(docId).get("title"));
					rankedHitList[hitListCounter].setUrl(SearcEngine.searcher.doc(docId).get("path"));
					hitListCounter++;
					//nodeSort.put(key, value);
					
					break;
				}
			}
		}
		}catch(Exception e){
			System.out.println("Exception occured while combinig score of vector model and page rank..!");
			e.printStackTrace();
		}
		//System.out.println("Sorted map: "+nodeSort);
}
	
	
	public void rankHitListWithPageRank(){
		List hitList = QueryProcessor.hitList;
		Set s = LinkAnalysis.nodeSort.keySet();
		Iterator iter = s.iterator();
		int docId;
		try{
			
			
			
		//System.out.println("HitList:"+hitList);
		while(iter.hasNext()){
			docId = (Integer)iter.next();
			//System.out.println("Doc id:"+docId);
			//System.out.println("present in hit list:"+hitList.contains(docId));
			if(hitList.contains(docId)){
				pageRankList.add(docId);
			}
			
		}
		/*System.out.println("Doc ids of Connectivity based ranking: ");
		for(int i=0;i<hitListCounter;i++){
			System.out.print(rankedHitList[i].docId+", ");
		}*/
		}catch(Exception e){
			System.out.println("Exception occured while construting hitlist..!");
		}
	}
}
