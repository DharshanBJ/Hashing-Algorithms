import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.ThreadLocalRandom;

public class DoubleHashing {
    /** Get user time in nanoseconds. */
    public static long getUserTime( ) {
        ThreadMXBean bean = ManagementFactory.getThreadMXBean( );
        return bean.isCurrentThreadCpuTimeSupported( ) ?
                bean.getCurrentThreadUserTime( ) : 0L;
    }

    private static int  size=97;
    public static int currsize=0;
  //  public static int[] table=new int[size];
  public static Cells[] table=new Cells[size];
    public static int  prime=7;

    public static void initTable(Cells[] entry){
        for(int i=0;i<size;i++){
            entry[i]=new Cells();
            entry[i].key=Integer.MAX_VALUE;
            entry[i].value=Integer.MAX_VALUE;
        }
    }

    public static void insert(int key,int val){

        currsize++;
        if(currsize/size<=0.7) {
            int index = key % size;

            //if there is a collision
            if (table[index].value != Integer.MAX_VALUE) {
                int hash2 = prime - (key % prime);
                int i = 1;
                while (true) {

                    int newIndex = (key % size + i * hash2)/2 % size;
                    if (table[newIndex].value != Integer.MAX_VALUE) {
                        i++;
                    } else {
                        table[newIndex].key = key;//if no collision
                        table[newIndex].value = val;
                        break;
                    }

                }
            } else {

                table[index].key = key;//if no collision
                table[index].value = val;
            }
        }else{
            //resizing
            size=2*size;
            currsize=0;
            Cells[] temp=table;
            table=new Cells[size];
            initTable(table);

            for(Cells dummy:temp){
                insert(dummy.key,dummy.value);
            }

            insert(key,val);
        }
    }

    private static void delete(int key){
        int HK1=key%size;
        if(table[HK1].key==key){
            //tombstone
            table[HK1].key=Integer.MIN_VALUE;
            table[HK1].value=Integer.MIN_VALUE;
            return;
        }else{
            int HK2= prime - (key % prime);

            for(int i=1;i<size;i++){
                int newIndex = (key % size + i * HK2) % size;
                if (table[newIndex].value == Integer.MAX_VALUE || table[newIndex].value == Integer.MIN_VALUE) {
                    i++;
                } else if(table[newIndex].key==key){
                    table[newIndex].key=Integer.MIN_VALUE;
                    table[newIndex].value=Integer.MIN_VALUE;
                    return;
                }
            }
            System.out.println("Key dosen't exist");
        }
    }

    public static int search(int key){

        int hashKey=key%size;
        int hashKey2 = prime - (key % prime);

        if(table[hashKey].key==key){
            return table[hashKey].value;
        }else {
            for(int i=1;i<size;i++){
                int NI = (key % size + i * hashKey2) % size;
                if(table[NI].key==key){
                    return table[NI].value;
                }
            }
            System.out.println("Key dosen't exist");
            return -1;
        }

    }
    public static void printTable(Cells[] table){
        for(int i=0;i<table.length;i++){
            if( table[i].value==Integer.MAX_VALUE || table[i].value==Integer.MIN_VALUE){
                System.out.println(i+"->"+" - ");
            }else{
                System.out.println(i+"->"+"key "+table[i].key+" Value"+"->"+table[i].value);
            }
        }
    }

    public static void main(String[] args){
       // int[]keys={19, 27, 36, 10, 64};
        initTable(table);
//        for(int i=0;i<keys.length;i++){
//            insert(keys[i]);
//        }
      // double startUserTimeNano = getUserTime();
        int insert=10;

        for(int i=0;i<insert;i++) {

            insert(i*5,ThreadLocalRandom.current().nextInt(1, 999 + 1));

        }
//
       System.out.println("Double Hashing");
//        printTable(table);
//
//        System.out.println(search(442));
//        double taskUserTimeNano = getUserTime() - startUserTimeNano;
//        System.out.println("Time it takes to insert "+insert+" elements is "+"User time: "+taskUserTimeNano/1000000+ " milli seconds");

        double startUserTimeNano = getUserTime();
        for(int i=0;i<insert;i++){
            search(i*5000);
        }

        double taskUserTimeNano = getUserTime() - startUserTimeNano;
        System.out.println("Time it takes to search "+insert+" elements is "+"User time: "+taskUserTimeNano/1000000+ " milli seconds");
        //printTable(table);
       
    }
}
