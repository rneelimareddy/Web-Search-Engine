package com.database;

import java.sql.*;
import java.util.HashMap;

import javax.sql.*;


public class DBHelper {

	String insert = "insert into ";
	String values=" values (";
	String end=")";
	String and = ", ";

	String create = "create table ";
	String start = " ( ";
	String type_int =" int ";
	String type_var_start = "varchar(15)";
	
	String strQuery = "Select * from ";
	
	public static Connection con;
	
	public DBHelper(){
		
			//For MySql
			String dbUrl = "jdbc:mysql://localhost:3306/SEO";
			String dbClass = "com.mysql.jdbc.Driver";
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection (dbUrl,"root","avinash");
				
			} //end try
			catch(ClassNotFoundException e) {
				e.printStackTrace();
			}
			catch(SQLException e) {
				e.printStackTrace();
			}catch(Exception e){
				System.out.println("Exception occured while connecting to database..!");
				e.printStackTrace();
			}
		
	}
	
	
	/*
	public void dropTable(String tableName){
		
		String strQuery = "drop table "+tableName;
		try{
			Statement stmt = con.createStatement();
			ResultSet rs = null;
			
			System.out.println("query :"+strQuery);
			stmt.executeQuery(strQuery);
			System.out.println("update done: "+rs);
				
			}catch(Exception e){
				System.out.println("Exception occured in updateTable():"+e);
			}
	}
	
	public void createWebGraphTable(String tablename,String[] fields){
		
		dropTable(tablename);
		//String tablename = "web_graph";
		String strQuery = create+tablename+start;
		for(int i=0;i<fields.length;i++){
			if(i!=0){
				strQuery = strQuery+and;
			}
			strQuery = strQuery+fields[i]+type_int;
		}
		strQuery = strQuery+end;
		System.out.println("query :"+strQuery);
		try{
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		
		System.out.println("query :"+strQuery);
		stmt.executeQuery(strQuery);
		System.out.println("update done: "+rs);
			
		}catch(Exception e){
			System.out.println("Exception occured in updateTable():"+e);
		}
		
	}*/
	
	public void deleteTable(String tableName){
		
		String strQuery = "delete from "+tableName;
		try{
			Statement stmt = con.  createStatement();
			ResultSet rs = null;
			
			System.out.println("query :"+strQuery);
			stmt.executeUpdate(strQuery);
			System.out.println("update done: "+rs);
				
			}catch(Exception e){
				System.out.println("Exception occured in deleteTable():"+e);
			}
	}
	
	public void insertTable(String tablename,String[] fields, String[] data){
		
		//String tablename = "web_graph";
		String strQuery = insert+tablename+values;
		for(int i=0;i<fields.length;i++){
			if(i!=0){
				strQuery=strQuery+and;
			}
			strQuery = strQuery+"'"+data[i]+"'";
		}
		strQuery = strQuery+end;
		//System.out.println("query :"+strQuery);
		try{
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		System.out.println("query :"+strQuery);
		stmt.executeUpdate(strQuery);
		//System.out.println("update done: "+rs);
			
		}catch(Exception e){
			System.out.println("Exception occured in insertTable():"+e);
		}

	}
	
	public HashMap getTableData(String tableName){
		HashMap map = new HashMap();
		String strquery = "Select * from "+tableName;
		try {
		Statement stmt = con.createStatement();
		ResultSet rs=null;
		String snode;
		String dnode;
		
			rs = stmt.executeQuery(strquery);
			// table.populateData(rs);
			while (rs.next()) {
				snode = rs.getString(1);
				dnode = rs.getString(2);
				//System.out.print(col1+"\t\t");
				//System.out.println("snode :"+snode);
				//System.out.println("dnode :"+dnode);
				map.put(snode,dnode);
			} //end while
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return map;
	}
	
	/*public void createPageRankTable(){
		
	}
	
	public void insertRank(){
		
	}*/
	
	
		
	
}
