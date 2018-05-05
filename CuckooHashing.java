import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.ThreadLocalRandom;

class Cells2D{
    int key;
    int value;

    //CellCollection next;

    public Cells2D(){
        this.key=Integer.MAX_VALUE;
        this.value=Integer.MAX_VALUE;
    }
}

public class cuckooHashing2 {
    /** Get user time in nanoseconds. */
    public static long getUserTime( ) {
        ThreadMXBean bean = ManagementFactory.getThreadMXBean( );
        return bean.isCurrentThreadCpuTimeSupported( ) ?
                bean.getCurrentThreadUserTime( ) : 0L;
    }


    public static int size=97;
    public static int currsize=0;

    public static Cells2D[][] tables=new Cells2D[2][size];
    public static int hashValue=Integer.MAX_VALUE;
    public static boolean flag=false;

    public static void initTable(Cells2D[][] tables){
        for(int i=0;i<2;i++){
            for(int j=0;j<size;j++){
                tables[i][j]=new Cells2D();
                tables[i][j].key=Integer.MAX_VALUE;
                tables[i][j].value=Integer.MAX_VALUE;
            }
        }
    }
    public static void insert(int key,int tableNumber,int maxCount,int val){

        if(maxCount==11){
            System.out.println("Cycle present - Rehashing needed ");

            //resizing
            size=2*size;
            currsize=0;
            maxCount=0;

            Cells2D[][] temp=tables;
            tables=new Cells2D[2][size];
            initTable(tables);

            for(int i=0;i<temp.length;i++){
                for(int j=0;j<temp[0].length;j++){
                    if(temp[i][j].key != Integer.MAX_VALUE){
                        insert(temp[i][j].key,0,0,temp[i][j].value);
                    }
                }
            }

            insert(key,tableNumber,maxCount,val);

           // flag=true;
            return;
        }

        //current number of elements
        int count=0;
        for (int i=0;i<2;i++){
            for(int j=0;j<tables[0].length;j++){
                if(tables[i][j].value!=Integer.MAX_VALUE){
                    count++;
                }
            }
        }

       if(count/size<=0.99) {

            //generate the hashkey based on the table you want to insert
            if (tableNumber == 0) {
                hashValue = key % size;
            } else {
                hashValue = (key / size) % size;
            }

            //check if the key is already present in the table
            if (tables[0][key % size].key == key) {
                return;
            }
            if (tables[1][(key / size) % size].key == key) {
                return;
            }

            //check if another key is present in the position of the new key
            if (tables[tableNumber][hashValue].value != Integer.MAX_VALUE) {
                int shiftKey = tables[tableNumber][hashValue].key;
                int shiftValue = tables[tableNumber][hashValue].value;
                tables[tableNumber][hashValue].key = key;
                tables[tableNumber][hashValue].value = val;
                insert(shiftKey, (tableNumber + 1) % 2, maxCount + 1, shiftValue);//trick for toggling between the two tables
            } else {
                tables[tableNumber][hashValue].key = key;
                tables[tableNumber][hashValue].value = val;
            }
        }else{
            //resizing
            size=2*size;
            //currsize=0;
            maxCount=0;

            Cells2D[][] temp=tables;
            tables=new Cells2D[2][size];
            initTable(tables);

            for(int i=0;i<temp.length;i++){
                for(int j=0;j<temp[0].length;j++){
                    if(temp[i][j].key != Integer.MAX_VALUE){
                        insert(temp[i][j].key,0,0,temp[i][j].value);
                    }
                }
            }

            insert(key,tableNumber,maxCount,val);
        }

    }
    public static int search(int key){

        int hashKey=key%size;
        int hashKey2=(key / size) % size;
        if(tables[0][hashKey].key==key){
            return tables[0][hashKey].value;
        }else if(tables[1][hashKey2].key==key){
            return tables[1][hashKey2].value;
        }else{
            System.out.println("key dosen't exist");
            return -1;
        }

    }

    private static void delete(int key){
        int hashKeyD=key%size;
        int hashKey2D=(key / size) % size;
        if(tables[0][hashKeyD].key==key){
            tables[0][hashKeyD].value=Integer.MAX_VALUE;
            tables[0][hashKeyD].key=Integer.MAX_VALUE;

        }else if(tables[1][hashKey2D].key==key){
            tables[1][hashKey2D].value=Integer.MAX_VALUE;
            tables[1][hashKey2D].key=Integer.MAX_VALUE;
        }else{
            System.out.println("key dosen't exist");
        }
    }

    //function to print contents of the hashTable
    public static void printKeys(Cells2D[][] tbls){
        for(int i=0;i<2;i++){
            for(int j=0;j<size;j++){
                if(tbls[i][j].value==Integer.MAX_VALUE){
                    System.out.print("-");
                }
                else {
                    System.out.println("Table number "+i+" Index "+j+"->"+"key "+tbls[i][j].key+" Value"+"->"+tbls[i][j].value);
                }
            }
        }
    }
    public static void main(String[]args){
        //initialize tables to dummy values
       initTable(tables);

      // double startUserTimeNano = getUserTime();
        int insert=10;

        for(int i=0;i<insert;i++) {

            insert(i*5,0,0,ThreadLocalRandom.current().nextInt(1, 999 + 1));

        }


//        double taskUserTimeNano = getUserTime() - startUserTimeNano;
//        System.out.println("Time it takes to insert "+insert+" elements is "+"User time: "+taskUserTimeNano/1000000+ " milli seconds");

        double startUserTimeNano = getUserTime();
        for(int i=0;i<insert;i++){
            search(i*50000);
        }

        double taskUserTimeNano = getUserTime() - startUserTimeNano;
        System.out.println("Time it takes to search "+insert+" elements is "+"User time: "+taskUserTimeNano/1000000+ " milli seconds");

//        delete(6);
//       printKeys(tables);
    }
}
