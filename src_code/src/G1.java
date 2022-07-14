import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Scanner;

public class G1 {
    public void store_Heapcsv(LinkedHashMap<Integer, node> map, String path) {
        String line = "";
        try {
            BufferedReader b_reader = new BufferedReader(new FileReader(path));
            while ((line = b_reader.readLine()) != null) // returns a Boolean value
            {
                String[] arr = line.replace("﻿", "").split(","); // use comma as separator
                System.out.println(arr[0]);
                map.put(Integer.parseInt(arr[0]),
                        new node(Integer.parseInt(arr[0]), Integer.parseInt(arr[1]), Integer.parseInt(arr[2])));
            }
            b_reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void store_pointercsv(LinkedHashMap<Integer, node> map, String path) {
        String line = "";
        try {
            BufferedReader b_reader = new BufferedReader(new FileReader(path));
            while ((line = b_reader.readLine()) != null) // returns a Boolean value
            {
                String[] arr = line.replace("﻿", "").split(","); // use comma as separator
                map.get(Integer.parseInt(arr[0])).neighbors.add(map.get(Integer.parseInt(arr[1])));
            }
            b_reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void DFS(node Node) {
        if (Node == null || Node.getMark())
            return;
        Node.markN();
        for (int i = 0; i < Node.neighbors.size(); i++) {
            DFS(Node.neighbors.get(i));
        }

    }

    public void Write_csv(LinkedHashMap<Integer, node> map, String path) {
        try {
            FileWriter csvWriter = new FileWriter(path + "\\G1.csv");
            for (node Node : map.values()) {
                if (Node.getMark()) {
                    ArrayList<String> rows = new ArrayList<String>();
                    rows.add(Integer.toString(Node.getKey()));
                    rows.add(Integer.toString(Node.get_StartByte()));
                    rows.add(Integer.toString(Node.end_Byte()));
                    csvWriter.append(String.join(",", rows));
                    csvWriter.append("\n");
                }
            }
            csvWriter.close();

        } catch (Exception E) {
            System.out.println("Error" + E.getMessage());
        }
    }

    public int[] GetFreeSections(int[] Sections, LinkedHashMap<Integer, node> map, int section_Size) {
        for (node Node : map.values()) {
            if (Node.getMark()) {
                //// we marking busy sections by one in its index.
                for (int i = 1; i < Sections.length + 1; i++) {
                    if ((Node.get_StartByte() < section_Size * i && Node.get_StartByte() > section_Size * (i - 1))
                            || (Node.end_Byte() < section_Size * i && Node.end_Byte() > section_Size * (i - 1))) {
                        Sections[i - 1] = 0;
                    }
                }
            } // if (Sections[i]==0){Sections[i] is empty}
        }
        return Sections;
    }

    public void G1Calc(int size, LinkedHashMap<Integer, node> map) {
        int section_Size = (size / 16); // 0--6, 7--13, 14--20, 21--27,28--34| 35--41
        int Sections[] = { section_Size, section_Size, section_Size, section_Size, section_Size, section_Size,
                section_Size, section_Size, section_Size, section_Size, section_Size, section_Size, section_Size,
                section_Size, section_Size, section_Size }; // IF SEC==0 THEN busy, ELSE ==SECTION SIZE.
        Sections = GetFreeSections(Sections, map, section_Size);
        for (node Node : map.values()) {
            if (Node.getMark()) {
                for (int i = 0; i < Sections.length; i++) {
                    if (Sections[i] != 0) {
                        int Start_Index = (section_Size * i) + (section_Size - Sections[i]);
                        int Node_size = Node.end_Byte() - Node.get_StartByte();
                        if (Node_size <= Sections[i]) {
                            Node.set_StartByte(Start_Index);
                            Node.set_endByte(Start_Index + Node_size);
                            Sections[i] -= Node_size;
                            break;
                        }
                    }
                }
            }

        }
    }

    public static void main(String[] args) {
        G1 obj = new G1();
        Scanner sc = new Scanner(System.in);
        String pathHeap, pathRoot, pathPointer, pathDestination = "";
        int Size = 0;
        System.out.print("Enter path of heap.csv file: ");
        pathHeap = sc.nextLine();
        System.out.print("Enter path of pointers.csv: file: ");
        pathPointer = sc.nextLine();
        System.out.print("Enter path of roots.txt file: ");
        pathRoot = sc.nextLine();
        System.out.print("Enter path of destination: ");
        pathDestination = sc.nextLine();
        System.out.print("Enter size of heap: ");
        Size = sc.nextInt();
        LinkedHashMap<Integer, node> nodeInfo = new LinkedHashMap<>();
        ////////////// storage area of roots//////////////////////*
        ArrayList<Integer> roots = new ArrayList<>();
        String line = "";
        try {
            BufferedReader b_reader = new BufferedReader(new FileReader(pathRoot));
            while ((line = b_reader.readLine()) != null) // returns a Boolean value
            {
                roots.add(Integer.parseInt(line));
            }
            b_reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        obj.store_Heapcsv(nodeInfo, pathHeap);
        obj.store_pointercsv(nodeInfo, pathPointer);
        /////////// check if node is in roots and not visited//////////////
        for (int root : roots) {
            obj.DFS(nodeInfo.get(root));
        }
        obj.G1Calc(Size, nodeInfo);
        obj.Write_csv(nodeInfo, pathDestination);
        sc.close();
    }

}
/*
 * Testing:
 * pathHeap =
 * "D:\\studying\\Level 2\\2nd Semester\\Paradigm\\Project\\Carpage collector 2\\sample1\\heap.csv"
 * ;
 * pathPointer
 * ="D:\\studying\\Level 2\\2nd Semester\\Paradigm\\Project\\Carpage collector 2\\sample1\\pointers.csv"
 * ;
 * pathRoot =
 * "D:\\studying\\Level 2\\2nd Semester\\Paradigm\\Project\\Carpage collector 2\\sample1\\roots.txt"
 * ;
 * pathDestination =
 * "D:\\studying\\Level 2\\2nd Semester\\Paradigm\\Project\\Carpage collector 2\\sample1"
 * ;
 */