import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.ThreadLocalRandom;

class Cells{
    int key;
    int value;

    Cells(){
        key=Integer.MAX_VALUE;
        value=Integer.MAX_VALUE;
    }
}

public class LH2 {

    /** Get user time in nanoseconds. */
    public static long getUserTime( ) {
        ThreadMXBean bean = ManagementFactory.getThreadMXBean( );
        return bean.isCurrentThreadCpuTimeSupported( ) ?
                bean.getCurrentThreadUserTime( ) : 0L;
    }


    public static int  size=97;
    public static int  currsize=0;
   // public static int[] table=new int[size];
   public static Cells[] entry=new Cells[size];

    public static void initTable(Cells[] entry){
        for(int i=0;i<size;i++){
            entry[i]=new Cells();
            entry[i].key=Integer.MAX_VALUE;
            entry[i].value=Integer.MAX_VALUE;
        }
    }

    public static void insert(int key,int value){
        currsize++;
        if(currsize/size<=0.7) { //checking load factor
            int hashKey = key % size;

            while (entry[hashKey].value != Integer.MAX_VALUE && entry[hashKey].value != Integer.MIN_VALUE) {
                hashKey = (hashKey + 1) % size;
            }
            entry[hashKey].key = key;
            entry[hashKey].value = value;
        }else{

            //resizing
            size=2*size;
            currsize=0;
            Cells[] temp=entry;
            entry=new Cells[size];
            initTable(entry);

            for(Cells dummy:temp){
                insert(dummy.key,dummy.value);
            }

            insert(key,value);
        }

    }

    public static int search(int key){

        int hashKey=key%size;
        int count=0;
        while((entry[hashKey].value != Integer.MAX_VALUE || entry[hashKey].value != Integer.MIN_VALUE) && entry[hashKey].key!= key && count <=size){
            hashKey=(hashKey+1)%size;
            count++;
        }

        if(entry[hashKey].value != Integer.MAX_VALUE){
            return entry[hashKey].value;
        }else{
            System.out.println("No entry for the given key");
            return -1;
        }

    }

    private static void delete(int key){ //lazy delete
        int hash=key%size;
         int count=0;
        while(entry[hash].value!=Integer.MAX_VALUE || entry[hash].value!=Integer.MIN_VALUE){

            if(count > size){
                break;
            }
            if(entry[hash].key==key){
                //update to a dummy value
                entry[hash].value=Integer.MIN_VALUE;
                entry[hash].key=Integer.MIN_VALUE;
                return;
            }
            count++;
            hash=(hash+1)%size;
        }
        System.out.println("Key not found");
    }

    public static void printTable(Cells[] table){
        for(int i=0;i<table.length;i++){
            if( entry[i].value==Integer.MAX_VALUE ||entry[i].value==Integer.MIN_VALUE){
                System.out.println(i+"->"+" - ");
            }else{
                System.out.println(i+"->"+"key "+entry[i].key+" Value"+"->"+entry[i].value);
            }
        }
    }

    public static void main(String[] args){
        //int[]keys={ 18, 41, 22, 44, 59, 32, 31, 73};

        System.out.println("Linear Hashing");
        initTable(entry);
       // double startUserTimeNano = getUserTime();
       int insert=10;
       //double startUserTimeNano = getUserTime();
        for(int i=0;i<insert;i++) {

        insert(i*5,ThreadLocalRandom.current().nextInt(1, 999 + 1));

        }
//        double taskUserTimeNano = getUserTime() - startUserTimeNano;
//        System.out.println("Time it takes to insert "+insert+" elements is "+"User time: "+taskUserTimeNano/1000000+ " milli seconds");

        double startUserTimeNano = getUserTime();
        for(int i=0;i<insert;i++){
           search(500*i);
        }

        double taskUserTimeNano = getUserTime() - startUserTimeNano;
        System.out.println("Time it takes to search "+insert+" elements is "+"User time: "+taskUserTimeNano/1000000+ " milli seconds");

        //System.out.println("Time it takes to insert "+insert+" elements is "+"User time: "+taskUserTimeNano/1000000+ " milli seconds");

    //   printTable(entry);




    }
}
