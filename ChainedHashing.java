import java.util.ArrayList;
import java.util.List;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.ThreadLocalRandom;

class CellCollection{
    int key;
    int value;

    CellCollection next;

   public CellCollection(){
        this.key=Integer.MAX_VALUE;
        this.value=Integer.MAX_VALUE;
    }
}

public class ChainedHashing {
    /** Get user time in nanoseconds. */
    public static long getUserTime( ) {
        ThreadMXBean bean = ManagementFactory.getThreadMXBean( );
        return bean.isCurrentThreadCpuTimeSupported( ) ?
                bean.getCurrentThreadUserTime( ) : 0L;
    }

    public static  List<CellCollection> inputL=new ArrayList<>();
    public static int size=97;
    public static int currSize=0;
   // public static int numBuckets;

    public ChainedHashing(){

    }

    public static void initTable(List<CellCollection> tbl){
        for(int i=0;i<size;i++){
            inputL.add(null);
       }
    }

    public static void insert(int key,int val){
        int HK=key%size;
        CellCollection head=inputL.get(HK);


        //check if key is already present
//        while(head != null){
//            if(head.key==key){
//                //replace the value
//                head.value=val;
//                return;
//            }
//            head=head.next;
//     }

        currSize++;
        CellCollection newNode=new CellCollection();
        newNode.key=key;
        newNode.value=val;
        newNode.next=head;
        inputL.set(HK,newNode);//update index HK with newNode

        //resizing-if the load factor exceeds a certain threshold,then search wont be O(1) time
        if((currSize/size)>=0.7){
            List<CellCollection> temp=inputL;
            inputL=new ArrayList<>();
            size=2*size;
            currSize=0;
            for(int i=0;i<size;i++){
                inputL.add(null);
            }
            for(CellCollection headNode:temp){
                while(headNode!=null){
                    insert(headNode.key, headNode.value);
                    headNode = headNode.next;
                }
            }
        }
    }

    public static int search(int key){
        int HK=key%size;
        CellCollection head=inputL.get(HK);
        while(head!=null){
            if(head.key==key){
                return head.value;
            }
            head=head.next;
        }
        System.out.println("No entry for the given key");
        return 0;
    }

    private static void delete(int key){
        int HK=key%size;
        CellCollection head=inputL.get(HK);
        CellCollection prev=new CellCollection();


        while(head!=null){
            //key found
            if(head.key==key){
                break;
            }else{
                //keep moving in chain
                prev=head;
                head=head.next;
            }

        }
        //if no key found
        if(head.value==Integer.MAX_VALUE){
            System.out.println("Key not found");
            return;
        }

        //remove key
        if(prev.value!=Integer.MAX_VALUE){
            prev.next=head.next;
        }else{
            inputL.set(HK,head.next);
        }
    }



    public static void printTable(List<CellCollection> input){

        for(int i=0;i<size;i++){//looping through each bucket
          //  System.out.print("Index "+i);
            CellCollection head=inputL.get(i);
            while(head != null){
                System.out.println("Index "+i+"-->"+"Key "+head.key+" Value "+head.value);
                head=head.next;
            }
        }
    }

    public static void main(String[] args){

        initTable(inputL);
        System.out.println("Chained Hashing");

     //   double startUserTimeNano = getUserTime();
        int insert=10;

        for(int i=0;i<insert;i++) {

            insert(5*i,ThreadLocalRandom.current().nextInt(1, 999 + 1));

        }

//        double taskUserTimeNano = getUserTime() - startUserTimeNano;
//        System.out.println("Time it takes to insert "+insert+" elements is "+"User time: "+taskUserTimeNano/1000000+ " milli seconds");

        double startUserTimeNano = getUserTime();
        for(int i=0;i<insert;i++) {

            search(i*5000);

        }

        double taskUserTimeNano = getUserTime() - startUserTimeNano;
         System.out.println("Time it takes to search "+insert+" elements is "+"User time: "+taskUserTimeNano/1000000+ " milli seconds");
         //printTable(inputL);




    }
}
