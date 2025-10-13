import java.util.*;

public class MemoryManagement
{
    static Scanner sc = new Scanner(System.in);
    static int blocks[];
    static int processes[];
    static int allocation[];

    public static void main(String args[])
    {
        System.out.println("Enter no.of blocks: ");
        int bno = sc.nextInt();
        blocks = new int[bno];
        System.out.println("Enter block sizes:");
        for(int i=0; i<bno; i++)
        {
            System.out.print("Enter Block " + (i+1) +":");
            blocks[i] = sc.nextInt();
        }
        System.out.println();
        System.out.println("Enter no.of processes: ");
        int pno = sc.nextInt();
        processes = new int[pno];
        System.out.println("Enter process sizes:");
        for(int i=0; i<pno; i++)
        {
            System.out.print("Enter Process " + (i+1) +":");
            processes[i] = sc.nextInt();
        }
        System.out.println();

        boolean f = false;
        while(!f)
        {
            System.out.println("-------- MEMORY ALLOCATION STRATEGIES --------");
            System.out.println("1. FIRST FIT");
            System.out.println("2. BEST FIT");
            System.out.println("3. WORST FIT");
            System.out.println("4. NEXT FIT");
            System.out.println("5. EXIT");
            System.out.print("Enter Choice: ");
            int ch = sc.nextInt();
            System.out.println();

            switch(ch)
            {
                case 1: firstFit();break;
                case 2: bestFit();break;
                case 3: worstFit();break;
                case 4: nextFit();break;
                case 5: f = true; break;
            }
        }
    }


    static void firstFit()
    {
        allocation = new int[processes.length];
        Arrays.fill(allocation,-1);
        int[] tempBlock = Arrays.copyOf(blocks,blocks.length);

        for(int i=0; i< processes.length; i++)
        {
            for(int j=0; j<tempBlock.length; j++)
            {
                if(tempBlock[j] >= processes[i])
                {
                    allocation[i] = j;
                    tempBlock[j] = tempBlock[j] - processes[i];
                    break;
                }
            }
            
        }

        display(tempBlock);
    }

    static void bestFit()
    {
        allocation = new int[processes.length];
        Arrays.fill(allocation,-1);
        int tempBlock[] = Arrays.copyOf(blocks,blocks.length);

        for(int i=0;i<processes.length; i++)
        {
            int bestIdx = -1;
            for(int j = 0;j<tempBlock.length; j++)
            {
                if(tempBlock[j] >= processes[i])
                {
                    if(bestIdx == -1 || tempBlock[j] < tempBlock[bestIdx])
                        bestIdx = j;
                }
            }

            if(bestIdx != -1)
            {
                allocation[i] = bestIdx;
                tempBlock[bestIdx] = tempBlock[bestIdx] - processes[i]; 
            }
        }

        display(tempBlock);
    }

     static void worstFit()
    {
        allocation = new int[processes.length];
        Arrays.fill(allocation,-1);
        int tempBlock[] = Arrays.copyOf(blocks,blocks.length);

        for(int i=0;i<processes.length; i++)
        {
            int worstIdx = -1;
            for(int j = 0;j<tempBlock.length; j++)
            {
                if(tempBlock[j] >= processes[i])
                {
                    if(worstIdx == -1 || tempBlock[j] > tempBlock[worstIdx])
                        worstIdx = j;
                }
            }

            if(worstIdx != -1)
            {
                allocation[i] = worstIdx;
                tempBlock[worstIdx] = tempBlock[worstIdx] - processes[i]; 
            }
        }

        display(tempBlock);
    }

    static void nextFit()
    {
        allocation = new int[processes.length];
        Arrays.fill(allocation,-1);
        int tempBlock[] = Arrays.copyOf(blocks,blocks.length);
        int lastpos = 0;
        for(int i = 0; i<processes.length; i++)
        {
            int j = lastpos;
            int count = 0;
            
            while(count < tempBlock.length)
            {
                if(tempBlock[j] >= processes[i])
                {
                    allocation[i] = j;
                    tempBlock[j] = tempBlock[j] - processes[i];
                    lastpos = (j+1) % tempBlock.length;
                    break;
                }
                j = (j+1) % tempBlock.length;
                count++;
            } 
        }

        display(tempBlock);
    }

    static void display(int[] rem)
    {
        System.out.println("ProcessNo\tProcess Size\tBlock Size");
        for(int i=0;i<processes.length;i++)
        {
            System.out.print((i+1) + "\t\t" + processes[i] + "\t\t");
            if(allocation[i] != -1)
                System.out.println((allocation[i] + 1));
            else
                System.out.println("Not Allocated");
        }

        System.out.println("Remaining blocks: ");
        for(int i = 0;i<rem.length; i++)
        {
            System.out.println("Block " + (i+1) + ": " + rem[i]);
        }
    }

}





