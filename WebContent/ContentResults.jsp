<%@page import="com.retrieval.Retrieval"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Intelligent Crawling and Indexing using Lucene</title>
<meta content="text/html; charset=iso-8859-1" http-equiv="Content-Type">
<link rel="stylesheet" type="text/css" href="base.css">
<%@ page import="java.io.File"%> 
<%@ page import="java.util.List"%> 

<%@ page import="com.searchEngine.SearcEngine"%>
<%@ page import = "com.retrieval.*"%>

<link rel="stylesheet" type="text/css" href="ml.css">
</head>
<body>
<div class="headerTXT">Intelligent Crawling and Indexing using
Lucene</div>
<table width="100%" cellpadding="0" cellspacing="0" border="0"
	class="Header">
	<tr with="995px" valign="middle" align="left">
		<td height="25px" width="995px"></td>
		<td width="995px">
		<div style="width: 800px;"></div>
		</td>
	</tr>
	<tr with="995px" valign="middle" align="left">
		<td height="25px" width="995px"></td>
		<td width="995px">
		<div style="width: 800px;"></div>
		</td>
	</tr>

</table>
<table width="100%" cellpadding="0" cellspacing="0" border="0">
	<tr with="995px">
		<td width="995px" height="2px" colspan="2" class="HeaderSeparator">
		<div style="width: 800px"></div>
		</td>
	</tr>
</table>
<table width="100%" cellpadding="0" cellspacing="0" border="0"
	height="50%" class="outerTable">
	<tr>
		<td width="995px" height="450px">
		<table height="100%" width="100%" cellpadding="0" cellspacing="0"
			border="0" class="tblBorder">
			<tr>
				<td width="210" valign="top" class="NavPanel"><br>
				<table cellspacing="0" width="100%" border="0">
					<tr>
						<td valign="top" class="InactiveNavTopic">&nbsp;</td>
						<td width="100%" class="InactiveNavTopic"></td>
					</tr>
					<tr>
						<td valign="top" class="InactiveNavTopic"></td>
						<td width="100%" class="InactiveNavTopic"></td>
					</tr>
					<tr>
						<td valign="top" class="InactiveNavTopic"></td>
						<td width="100%" class="InactiveNavTopic"></td>
					</tr>
					<tr>
						<td valign="top" class="InactiveNavTopic"></td>
						<td width="100%" class="InactiveNavTopic"><a
							href="mailto:sthatipe@cs.odu.edu">Email Administrator</a></td>
					</tr>
					<tr>
						<td width="100%" height="1px" colspan="2" class="HeaderSeparator"></td>
					</tr>
					<tr>
						<td align="right" colspan="2"></td>
					</tr>
					<tr>
						<td valign="top" class="InactiveNavTopic"></td>
						<td width="100%" class="InactiveNavTopic"></td>
					</tr>
					<tr>
						<td valign="top" class="InactiveNavTopic"></td>
						<td width="100%" class="InactiveNavTopic"></td>
					</tr>
					<tr>
						<td valign="top" class="InactiveNavTopic"></td>
						<td width="100%" class="InactiveNavTopic"></td>
					</tr>
					<tr>
						<td valign="top" class="InactiveNavTopic"></td>
						<td width="100%" class="InactiveNavTopic"></td>
					</tr>
					<tr>
						<td width="100%" height="1px" colspan="2" class="HeaderSeparator"></td>
					</tr>
				</table>
				</td>
				<td colspan="2" valign="top" class="pgContent">
				<table width="100%" cellpadding="0" cellspacing="2" border="0">

					<br>
					<br>
					<h4>
					

					<%
					
						String query = request.getParameter("q");
						System.out.println("query:"+query);
						System.out.println("2");
						QueryResult[] result;
						System.out.println("isCrawlenabled:"+SearcEngine.isCrawlEnabled);
					    result = SearcEngine.search(query);
					    System.out.println(result);
						System.out.println("3");
						for(int i=0;i<Retrieval.hitListCounter;i++){
							
							int index = i+1;
							String title = result[i].getTitle();
							System.out.println(i+":"+title);
							String url = result[i].getUrl();%>
							
							<p><%=index%> <a href="<%=url %>"><%=title %></a></p>
							<p>
							
						<%}
					%>
					
				</table>
				</div>
				</td>
			</tr>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td height="1px" width="995px" class="NavPanel">
		<div style="width: 800px; height: 1px;"></div>
		</td>
	</tr>
</table>
<table width="100%" cellpadding="0" cellspacing="0" border="0">
	<tr valign="middle" align="left">
		<td class="Footer" height="18"></td>
		<td class="Footer" width="100%" height="18"></td>
		<td class="Footer" height="18"></td>
	</tr>
	<tr>
		<td colspan="3" width="995px">
		<div style="width: 800px;"></div>
		</td>
	</tr>
</table>
</body>
</html>