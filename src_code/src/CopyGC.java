
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Scanner;

public class CopyGC {

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




    /////////////////////////////////// Copy GC implementation//////////////////////////////////
    public void Re_arrange(LinkedHashMap<Integer, node> new_nodeInfo,LinkedHashMap<Integer, node> map,int root,int i)
    {
        i++;
        Object[] list = new_nodeInfo.keySet().toArray();
        for(node node : map.get(root).neighbors){
        new_nodeInfo.putIfAbsent(node.getKey(), map.get(node.getKey()));}
        if(i==list.length)
            return;
        Re_arrange(new_nodeInfo,map, (int) list[i],i);

    }
   public void Re_allocation(LinkedHashMap<Integer, node> Info){
       Object[] list = Info.keySet().toArray();
       int j = 0 ;
       for(Object i : list){
           int s = Info.get((int)i).get_StartByte();
           int e = Info.get((int)i).end_Byte();
           Info.get((int)i).set_StartByte(j);
           Info.get((int)i).set_endByte(j+(e-s));
           j = j +(e-s)+1;
       }
   }


    public void Write_csv(LinkedHashMap<Integer,node> map,String path)
    {
        try
        {
            FileWriter csvWriter = new FileWriter(path+"\\Copy GC.csv");
            for (node Node : map.values())
            {
                ArrayList<String> rows = new ArrayList<String>();
                rows.add(Integer.toString(Node.getKey()));
                rows.add(Integer.toString(Node.get_StartByte()));
                rows.add(Integer.toString(Node.end_Byte()));
                csvWriter.append(String.join(",", rows));
                csvWriter.append("\n");

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

        CopyGC obj=new CopyGC();
        LinkedHashMap<Integer, node> new_nodeInfo =new LinkedHashMap<>();
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



        ////////////////////Copy GC calls////////////////////////////

        for(int root : roots){
            new_nodeInfo.put(root,nodeInfo.get(root));
        }
        int i= 0 ;
        if(!roots.isEmpty()) {
        obj.Re_arrange(new_nodeInfo,nodeInfo,roots.get(0),i);
        obj.Re_allocation(new_nodeInfo);
        }
        /////////////////////////////////////add in csv file////////////////////////////////
        obj.Write_csv(new_nodeInfo,pathDestination);
        sc.close();

    }

}