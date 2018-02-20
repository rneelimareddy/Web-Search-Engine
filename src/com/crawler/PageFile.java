package com.crawler;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import websphinx.Link;
import websphinx.Page;

public class PageFile {
	
		public Page page ;
		public String fileName="";
		public URL url;
		public Link[] childLinks;
		public List sourceNodes = new ArrayList();
		public List outgoingNodes = new ArrayList();
		public int noOfOutLinks=0;
	
}
