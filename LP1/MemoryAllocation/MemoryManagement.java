import java.util.*;

public class MemoryManagement {
    static Scanner sc = new Scanner(System.in);
    static int processes[];
    static int blocks[];
    static int allocation[];

    public static void main(String args[]) {
        System.out.print("Enter no of blocks: ");
        int bno = sc.nextInt();
        blocks = new int[bno];
        System.out.println("\n Enter block sizes");
        for (int i = 0; i < bno; i++) {
            System.out.print("block " + (i + 1) + ": ");
            blocks[i] = sc.nextInt();
        }
        System.out.println();

        System.out.print("Enter no of processes: ");
        int pno = sc.nextInt();
        processes = new int[pno];
        System.out.println("\n Enter process sizes");
        for (int i = 0; i < pno; i++) {
            System.out.print("process " + (i + 1) + ": ");
            processes[i] = sc.nextInt();
        }
        System.out.println();

        boolean exit = false;
        while (!exit) {
            System.out.println("------------- MEMORY PLACEMENT STRATEGIES ------------");
            System.out.println("1. FIRST FIT");
            System.out.println("2. BEST FIT");
            System.out.println("3. WORST FIT");
            System.out.println("4. NEXT FIT");
            System.out.println("5. EXIT");
            System.out.print("Enter choice: ");
            int ch = sc.nextInt();
            System.out.println();

            switch (ch) {
                case 1:
                    firstFit();
                    break;

                case 2:
                    bestFit();
                    break;

                case 3:
                    worstFit();
                    break;

                case 4:
                    nextFit();
                    break;

                case 5:
                    exit = true;
                    break;

                default:
                    System.out.println("Invalid choice");
            }

        }
    }

    static void firstFit() {
        allocation = new int[processes.length];
        Arrays.fill(allocation, -1);
        int tempBlock[] = Arrays.copyOf(blocks, blocks.length);
        boolean[] blockUsed = new boolean[blocks.length];

        for (int i = 0; i < processes.length; i++) {
            for (int j = 0; j < tempBlock.length; j++) {
                if (!blockUsed[j] && tempBlock[j] >= processes[i]) {
                    allocation[i] = j;
                    blockUsed[j] = true;
                    break;
                }
            }
        }

        display();
    }

    static void bestFit() {
        allocation = new int[processes.length];
        Arrays.fill(allocation, -1);
        int tempBlock[] = Arrays.copyOf(blocks, blocks.length);
        boolean[] blockUsed = new boolean[blocks.length];

        for (int i = 0; i < processes.length; i++) {
            int bestIdx = -1;
            for (int j = 0; j < tempBlock.length; j++) {
                if (!blockUsed[j] && tempBlock[j] >= processes[i]) {
                    if (bestIdx == -1 || tempBlock[j] < tempBlock[bestIdx])
                        bestIdx = j;
                }
            }

            if (bestIdx != -1) {
                allocation[i] = bestIdx;
                blockUsed[bestIdx] = true;
            }
        }

        display();
    }

    static void worstFit() {
        allocation = new int[processes.length];
        Arrays.fill(allocation, -1);
        int tempBlock[] = Arrays.copyOf(blocks, blocks.length);
        boolean[] blockUsed = new boolean[blocks.length];

        for (int i = 0; i < processes.length; i++) {
            int worstIdx = -1;
            for (int j = 0; j < tempBlock.length; j++) {
                if (!blockUsed[j] && tempBlock[j] >= processes[i]) {
                    if (worstIdx == -1 || tempBlock[j] > tempBlock[worstIdx])
                        worstIdx = j;
                }
            }

            if (worstIdx != -1) {
                allocation[i] = worstIdx;
                blockUsed[worstIdx] = true;
            }
        }

        display();
    }

    static void nextFit() {
        allocation = new int[processes.length];
        Arrays.fill(allocation, -1);
        int tempBlock[] = Arrays.copyOf(blocks, blocks.length);
        boolean blockUsed[] = new boolean[blocks.length];

        int lastPos = 0;
        for (int i = 0; i < processes.length; i++) {
            int j = lastPos;
            int count = 0;
            boolean allocated = false;

            while (count < tempBlock.length) {
                if (!blockUsed[j] && tempBlock[j] >= processes[i]) {
                    allocation[i] = j;
                    blockUsed[j] = true;
                    lastPos = (j + 1) % tempBlock.length;
                    allocated = true;
                    break;
                }
                j = (j + 1) % tempBlock.length;
                count++;
            }

            if (!allocated)
                allocation[i] = -1;
        }

        display();
    }

    static void display() {
        System.out.println("ProcessNo\tProcessSize\tBlock No");
        for (int i = 0; i < processes.length; i++) {
            System.out.print((i + 1) + "\t\t" + processes[i] + "\t\t");
            if (allocation[i] != -1)
                System.out.println(allocation[i] + 1);
            else
                System.out.println("Not allocated");
        }
        System.out.println();
    }

}
