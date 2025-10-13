import java.util.Scanner;

class Semaphore
{
    volatile int value;

    Semaphore(int v)
    {
        value = v;
    }

    void Wait()
    {
        while(value ==0);
        value--;
    }

    void Signal()
    {
        value ++;
    }
}

class Buffer
{
    int[] buff;
    int n;

    int front,rear;

    Semaphore E;
    Semaphore F;
    Semaphore M;

   Buffer(int n)
    {
        this.n = n;
        buff = new int[n];
        front = -1;
        rear = -1;
         E = new Semaphore(n);
         F = new Semaphore(0);
         M = new Semaphore(1);
    }

    void produce(int i)
    {
        E.Wait();
        M.Wait();


        rear = (rear + 1) % n;
        buff[rear] = i;
        if(front == -1)
            front =0;

        System.out.println("Item Produced: " + i);
        showBuffer();
        System.out.println();
        
        F.Signal();
        M.Signal();
    }

    void consume()
    {
        F.Wait();
        M.Wait();

        int i = buff[front];
        if(front == rear)
            front=rear=-1;
        else
        front = (front + 1) % n;

         System.out.println("Item consumed: "+i);
        showBuffer();
        System.out.println();

        E.Signal();
        M.Signal();
    }

    void showBuffer()
    {
        System.out.print("Buffer : | ");
        for(int i = 0; i<n; i++)
        {
            if(front <=rear)
            {
                if(i>=front && i<=rear)
                    System.out.print(buff[i] + " | ");
                else
                    System.out.print(" | ");
            }
            else
            {
                if(i>rear && i<front)
                    System.out.print(" | ");
                else   
                    System.out.print(buff[i] + " | ");
            }
        }

        System.out.println();
    }
}

class Producer extends Thread
{
    Buffer buf;

    Producer(Buffer b)
    {
        buf = b;
    }

    public void run()
    {
        for(int i = 1;i<=15; i++)
        {
            buf.produce(i);
            try{
                Thread.sleep(100);
            }
            catch(Exception e)
            {
                System.out.println("Producer Interrupted");
            }
        }
    }
}

class Consumer extends Thread
{
    Buffer buf;
    Consumer(Buffer b)
    {
        buf = b;
    }

    public void run()
    {
        for(int i=1;i<=15;i++)
        {
            buf.consume();
            try{
                Thread.sleep(150);
            }
            catch(Exception e)
            {
                System.out.println("Consumer interrupted");
            }
        }
    }
}

public class ProducerConsumer
{
    public static void main(String args[])
    {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Buffer size: ");
        int n = sc.nextInt();

        Buffer buffer = new Buffer(n);
        Producer p = new Producer(buffer);
        Consumer c = new Consumer(buffer);

        p.start();
        c.start();
    }
}




// import java.util.Scanner;

// class Semaphore{
//     private volatile int val;

//     public Semaphore(int val){
//         this.val = val;
//     }

//     public void waitS(){
//         while(val == 0);
//         val--;
//     }

//     public void signalS(){
//         val++;
//     }
// }

// class Buffer{
//     private int[] buf;
//     private int n;

//     private int front, rear;

//     Semaphore E;
//     Semaphore F;
//     Semaphore M;
    

//     public Buffer(int n){
//         this.n = n;
//         buf = new int[n];
//         front = rear = -1;

//         E = new Semaphore(n);
//         F = new Semaphore(0);
//         M = new Semaphore(1);
//     }

//     public void produce(int i){
//         E.waitS();
//         M.waitS();


//         rear = (rear+1) % n;
//         buf[rear] = i;
//         if(front==-1) front = 0;

//         System.out.println("Item Produced: "+i);
//         this.showBuffer();
//         System.out.println();

//         F.signalS();
//         M.signalS();
//     }

//     public void consume(){
//         F.waitS();
//         M.waitS();

//         int res = buf[front];
//         if(front==rear) front = rear = -1;
//         else front = (front+1)%n;

//         System.out.println("Item Consumed: " + res);
//         this.showBuffer();
//         System.out.println();

//         E.signalS();
//         M.signalS();
//     }

//     public void showBuffer(){
//         System.out.print("Buffer: | ");
//         int i;
//         for(i=0; i<n; i++){
//             if(front<=rear){
//                 if(i>=front && i<=rear) System.out.print(buf[i]+" | ");
//                 else System.out.print(" | ");
//             }else{
//                 if(i>rear && i<front) System.out.print(" | ");
//                 else System.out.print(buf[i]+" | ");
//             }
//         }

//         System.out.println();
//     }
// }

// class Producer extends Thread{
//     Buffer buf;
//     public Producer(Buffer buff){
//         buf = buff;
//     }

//     public void run(){
//         for(int i=1; i<=15; i++){
//             buf.produce(i);
//             try{
//                 Thread.sleep(100);
//             }catch(Exception e){
//                 System.out.println("\nProducer Interrupted");
//             }
//         }
//     }
// }

// class Consumer extends Thread{
//     Buffer buf;
//     public Consumer(Buffer buff){
//         buf = buff;
//     }
//     public void run(){
//         for(int i=1; i<=15; i++){
//             buf.consume();
//             try{
//                 Thread.sleep(150);
//             }catch(Exception e){
//                 System.out.println("\nConsumer Interrupted");
//             }
//         }
//     }
// }

// public class ProducerConsumer{
//     public static void main(String[] args){
//         Scanner s = new Scanner(System.in);

//         System.out.print("Enter Buffer Size: ");
//         int n = s.nextInt();
//         s.close();

//         Buffer buffer = new Buffer(n);
//         Producer p = new Producer(buffer);
//         Consumer c = new Consumer(buffer);

//         p.start();
//         c.start();
//     }
// }