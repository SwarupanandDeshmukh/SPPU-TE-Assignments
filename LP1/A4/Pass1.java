import java.io.*;
import java.util.*;

public class Pass1 {
    static class MNTEntry {
        String name;
        int pp, kp, mdtp, kpdtp;

        MNTEntry(String name, int pp, int kp, int mdtp, int kpdtp) {
            this.name = name;
            this.pp = pp;
            this.kp = kp;
            this.mdtp = mdtp;
            this.kpdtp = kpdtp;
        }
    }

    static List<MNTEntry> MNT = new ArrayList<>();
    static List<String> MDT = new ArrayList<>();
    static List<String[]> KPDT = new ArrayList<>();
    static List<List<String>> PNTAB = new ArrayList<>();  // multiple PNTs
    static List<List<String>> sourceCode = new ArrayList<>();
    static List<String> outputLines = new ArrayList<>();
    static String sourceLocation = "";

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Source input file: ");
        sourceLocation = reader.readLine();
        readLines();
        processLines();
        printOutput();

        // Write to files
        writeMNTToFile("MNT.txt");
        writePNTToFile();   // separate PNT files per macro
        writeKPDTToFile("KPDT.txt");
        writeMDTToFile("MDT.txt");
    }

    static void readLines() throws IOException {
        BufferedReader fileReader = new BufferedReader(new FileReader(sourceLocation));
        String line;
        while ((line = fileReader.readLine()) != null) {
            List<String> tokens = Arrays.asList(line.trim().split("\\s+"));
            sourceCode.add(tokens);
        }
        fileReader.close();
    }

    static void processLines() {
        int lineNumber = 0;

        while (lineNumber < sourceCode.size()) {
            List<String> lineTokens = sourceCode.get(lineNumber);

            if (lineTokens.contains("MACRO")) {
                lineNumber++;
                List<String> macroDefTokens = sourceCode.get(lineNumber);
                analyzeMacroDefinition(macroDefTokens);
                lineNumber++;

                while (true) {
                    List<String> macroLineTokens = sourceCode.get(lineNumber);
                    substituteIndexNotationsForArgument(macroLineTokens);

                    if (macroLineTokens.contains("MEND")) {
                        break;
                    }
                    lineNumber++;
                }
                lineNumber++;
            } else {
                outputLines.add(String.join(" ", lineTokens));
                lineNumber++;
            }
        }
    }

    static void analyzeMacroDefinition(List<String> mdTokens) {
        String macroName = mdTokens.get(0);
        int pp = 0, kp = 0;

        // record starting pointers
        int mdtp = MDT.size() + 1;   // MDT line number starts here
        int kpdtp = KPDT.size() + 1; // KPDT index starts here

        List<String> currentPNT = new ArrayList<>();  // New PNT for this macro

        for (int i = 1; i < mdTokens.size(); i++) {
            String token = mdTokens.get(i);
            if (token.contains("=")) {
                String[] paramTokens = token.split("=");
                String parameter = paramTokens[0];
                String defaultArg = (paramTokens.length > 1 && !paramTokens[1].isEmpty()) ? paramTokens[1] : "_";
                kp++;
                currentPNT.add(parameter);
                KPDT.add(new String[]{parameter, defaultArg});
            } else {
                pp++;
                currentPNT.add(token);
            }
        }

        // Save this macro’s PNT
        PNTAB.add(currentPNT);

        // Add entry in MNT
        MNT.add(new MNTEntry(macroName, pp, kp, mdtp, kp > 0 ? kpdtp : -1));
    }

    static void substituteIndexNotationsForArgument(List<String> lineTokens) {
        MNTEntry lastEntry = MNT.get(MNT.size() - 1);
        List<String> currentPNT = PNTAB.get(PNTAB.size() - 1);
        int totalParams = lastEntry.pp + lastEntry.kp;

        StringBuilder outputLine = new StringBuilder();

        for (String token : lineTokens) {
            if (token.startsWith("&")) {
                int index = -1;
                for (int j = 0; j < totalParams; j++) {
                    if (token.equals(currentPNT.get(j))) {
                        index = j + 1;
                        break;
                    }
                }
                outputLine.append("(P,").append(index).append(") ");
            } else {
                outputLine.append(token).append(" ");
            }
        }

        MDT.add(outputLine.toString().trim());
    }

    static void printOutput() {
        System.out.println("PASS1 COMPLETED..");
    }

    // --- File Writers ---

    static void writeMNTToFile(String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("Name\tpp\tkp\tMDTP\tKPDT");
            for (MNTEntry entry : MNT) {
                writer.printf("%s\t%d\t%d\t%d\t%s%n",
                        entry.name, entry.pp, entry.kp, entry.mdtp,
                        (entry.kp > 0 ? entry.kpdtp : "-"));
            }
        } catch (IOException e) {
            System.err.println("Error writing MNT to file: " + e.getMessage());
        }
    }

    static void writePNTToFile() {
        for (int i = 0; i < PNTAB.size(); i++) {
            String macroName = MNT.get(i).name;
            String filename = "PNT_" + macroName + ".txt";

            try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
                writer.println(macroName);
                for (String param : PNTAB.get(i)) {
                    writer.println(param);
                }
            } catch (IOException e) {
                System.err.println("Error writing PNT file for " + macroName + ": " + e.getMessage());
            }
        }
    }

    static void writeKPDTToFile(String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("Name\tDefault");
            for (String[] pair : KPDT) {
                writer.printf("%s\t%s%n", pair[0], pair[1]);
            }
        } catch (IOException e) {
            System.err.println("Error writing KPDT to file: " + e.getMessage());
        }
    }

    static void writeMDTToFile(String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (String line : MDT) {
                writer.println(line);
            }
        } catch (IOException e) {
            System.err.println("Error writing MDT to file: " + e.getMessage());
        }
    }
}


