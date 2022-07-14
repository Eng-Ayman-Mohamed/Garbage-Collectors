import java.util.ArrayList;

public class node {
   private int key;
   private int startByte;
   private int endByte;
   private boolean mark;
   public ArrayList<node> neighbors=new ArrayList<node>();
   node(int name,int start,int end)
   {
	   key=name;
	   startByte=start;
	   endByte=end;
	   mark=false;
   }
   public int getKey()
   {
	   return key;
   }
   public int get_StartByte()
   {
	   return startByte;
   }
   public int end_Byte()
   {
	   return endByte;
   }
   public boolean getMark()
   {
	   return mark;
   }
   public void setKey(int name)
   {
	    key=name;
   }
   public void set_StartByte(int start)
   {
	    startByte=start;
   }
   public void set_endByte(int end)
   {
	    endByte=end;
   }
 
   public void markN()
   {
	   mark=true;
   }
   
}
