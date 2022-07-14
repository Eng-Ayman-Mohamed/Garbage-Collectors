import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Scanner;

public class MarkCompact {
	public void store_Heapcsv( LinkedHashMap<Integer,node> map,String path) 
	{
		String line="";
		try   
		{  
		BufferedReader b_reader = new BufferedReader(new FileReader(path));  
		while ((line = b_reader.readLine()) != null)   //returns a Boolean value  
		{  
		  String[] arr = line.replace("﻿","").split(",");    // use comma as separator 
		  map.put(Integer.parseInt(arr[0]), new node(Integer.parseInt(arr[0]),Integer.parseInt(arr[1]),Integer.parseInt(arr[2])));
		} 
		b_reader.close();
		}   
		catch (IOException e)   
		{  
		e.printStackTrace();  
		} 
	}
	
	public void store_pointercsv(LinkedHashMap<Integer,node> map,String path)
	{
		String line="";
		try   
		{  
		BufferedReader b_reader = new BufferedReader(new FileReader(path));  
		while ((line = b_reader.readLine()) != null)   //returns a Boolean value  
		{  
		  String[] arr = line.replace("﻿","").split(",");    // use comma as separator  
		  map.get(Integer.parseInt(arr[0])).neighbors.add(map.get(Integer.parseInt(arr[1])));
		} 
		b_reader.close();
		}   
		catch (IOException e)   
		{  
		e.printStackTrace();  
		} 
	}
	
	public void DFS(node Node)
	{
		if(Node==null || Node.getMark())
			return;
		Node.markN();
		for(int i=0;i<Node.neighbors.size();i++)
		{
		    DFS(Node.neighbors.get(i));
		}
	
	}
	public void Compact(LinkedHashMap<Integer,node> map)
	{
		int lastInd=0;
		for(node Node :map.values())
		{
			if(Node.getMark())
			{
				
				int temp=Node.end_Byte()-Node.get_StartByte();
				Node.set_StartByte(lastInd);
				Node.set_endByte(temp+lastInd);
				lastInd=Node.end_Byte()+1;
				
			}
		}
	}
	
	public void Write_csv(LinkedHashMap<Integer,node> map,String path)
	{
		try 
		{
		  FileWriter csvWriter = new FileWriter(path+"\\Mark_Compact.csv");
		  for (node Node : map.values())
		  {
			  ArrayList<String> rows = new ArrayList<String>();
			 if(Node.getMark())
			 {
			  rows.add(Integer.toString(Node.getKey()));
			  rows.add(Integer.toString(Node.get_StartByte()));
			  rows.add(Integer.toString(Node.end_Byte()));
			  csvWriter.append(String.join(",", rows));
			  csvWriter.append("\n");
			 }
		  }
		  csvWriter.close();
		 
		}
		catch(Exception E)
		{
			System.out.println("Error"+E.getMessage());
		}
	}

	public static void main(String[] args) throws IOException {
       
		Scanner sc=new Scanner(System.in);
		String pathHeap,pathRoot,pathPointer,pathDestination="";
		System.out.print("Enter path of heap.csv file: ");
		pathHeap=sc.nextLine();
		System.out.print("Enter path of pointers.csv: file: ");
	    pathPointer=sc.nextLine();
	   
		System.out.print("Enter path of roots.txt file: ");
	    pathRoot=sc.nextLine();
		
	    System.out.print("Enter path of destination: ");
	    pathDestination=sc.nextLine();
		
	    MarkCompact obj=new MarkCompact();
	    
	    LinkedHashMap<Integer, node> nodeInfo = new LinkedHashMap<>();
	    
	    ////////////// storage area of roots//////////////////////*
	    
        ArrayList<Integer> roots=new ArrayList<>();
        String line="";
		try   
		{  
		BufferedReader b_reader = new BufferedReader(new FileReader(pathRoot));  
		while ((line = b_reader.readLine()) != null)   //returns a Boolean value  
		{  
		  roots.add(Integer.parseInt(line));
		} 
		b_reader.close();
		}   
		catch (IOException e)   
		{  
		e.printStackTrace();  
		} 
			
	    obj.store_Heapcsv(nodeInfo,pathHeap);
	    
	    obj.store_pointercsv(nodeInfo, pathPointer);
	    
	  
	    	    
	    ////////////////////check if node is in roots and not visited////////////////////////////
	    for (int root : roots)
	    {
	    	obj.DFS(nodeInfo.get(root));
	    	
	    }
	    /////////////////////////////////////add in csv file////////////////////////////////
	    obj.Compact(nodeInfo);
	    obj.Write_csv(nodeInfo,pathDestination);
	    
	    
	    
	    sc.close();
	    
	}
}
