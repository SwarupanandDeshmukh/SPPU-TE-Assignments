import java.io.*;
import java.util.*;

public class Pass1 {
    static Map<String, Integer> optab = new HashMap<>();
    static Map<String, Integer> regtab = new HashMap<>();
    static Map<String, Integer> condtab = new HashMap<>();
    static Map<String, Integer> symtab = new LinkedHashMap<>();
    static List<String> littab = new ArrayList<>();
    static List<Integer> litaddr = new ArrayList<>();
    static List<Integer> pooltab = new ArrayList<>();
    static Set<String> poolLits = new LinkedHashSet<>();

    static int lc = 0;
    static PrintWriter out;

    public static void main(String[] args) throws IOException {
        initTables();

        BufferedReader in = new BufferedReader(new FileReader("source.txt"));
        out = new PrintWriter("ic.txt");

        String line;
        while ((line = in.readLine()) != null) {
            if (!line.trim().isEmpty())
                processLine(line.trim());
        }

        in.close();
        out.close();
        saveTables();
        System.out.println("Pass 1 Completed!");
    }

    static void initTables() {
        optab.put("STOP", 0);
        optab.put("ADD", 1);
        optab.put("SUB", 2);
        optab.put("MULT", 3);
        optab.put("MOVER", 4);
        optab.put("MOVEM", 5);
        optab.put("COMP", 6);
        optab.put("BC", 7);
        optab.put("DIV", 8);
        optab.put("READ", 9);
        optab.put("PRINT", 10);

        regtab.put("AREG", 1);
        regtab.put("BREG", 2);
        regtab.put("CREG", 3);
        regtab.put("DREG", 4);

        condtab.put("LT", 1);
        condtab.put("LE", 2);
        condtab.put("EQ", 3);
        condtab.put("GT", 4);
        condtab.put("GE", 5);
        condtab.put("ANY", 6);
    }

    static void processLine(String line) throws IOException {
        String[] parts = line.split("[\\s,]+");
        int i = 0;

        // Handle START before label processing
        if (parts[i].equals("START")) {
            lc = Integer.parseInt(parts[i + 1]);
            out.println("-x- (AD,01) (C," + parts[i + 1] + ")");
            return;
        }

        // Label handling (only non-empty and non-keyword)
        if (!parts[0].isEmpty() && !isKeyword(parts[0])) {
            symtab.put(parts[0], lc);
            i++;
        }

        if (i >= parts.length)
            return;

        String op = parts[i];

        if (op.equals("END")) {
            processLTORG();
            out.println("-x- (AD,02)");
            return;
        } else if (op.equals("LTORG")) {
            processLTORG();
            out.println("-x- (AD,05)");
            return;
        } else if (op.equals("ORIGIN")) {
            String expr = parts[i + 1]; // e.g. "NEXT+2" or "LOOP-3"
            lc = eval(expr); // Update LC correctly

            // Determine the operation symbol and split
            String sign = "";
            String symbol = "";
            String value = "";

            if (expr.contains("+")) {
                String[] p = expr.split("\\+");
                symbol = p[0];
                value = p[1];
                sign = "+";
            } else if (expr.contains("-")) {
                String[] p = expr.split("\\-");
                symbol = p[0];
                value = p[1];
                sign = "-";
            } else {
                symbol = expr;
                value = "";
            }

            // Print proper intermediate code
            if (!value.isEmpty()) {
                out.println("-x- (AD,05) (S," + getSymIndex(symbol) + ")" + sign + value);
            } else {
                out.println("-x- (AD,05) (S," + getSymIndex(symbol) + ")");
            }

            return;
        } else if (op.equals("DC")) {
            out.println(lc + " (DL,01) (C," + parts[i + 1].replace("'", "") + ")");
            lc++;
            return;
        } else if (op.equals("DS")) {
            int size = Integer.parseInt(parts[i + 1]);
            out.println(lc + " (DL,02) (C," + size + ")");
            lc += size;
            return;
        }

        // Instruction statements
        if (optab.containsKey(op)) {
            out.print(lc + " (IS," + String.format("%02d", optab.get(op)) + ")");

            if (!op.equals("STOP")) {
                if (op.equals("READ") || op.equals("PRINT")) {
                    out.print(" (S," + getSymIndex(parts[i + 1]) + ")");
                } else {
                    String op1 = parts[i + 1];
                    String op2 = parts[i + 2];

                    out.print(" (" + (op.equals("BC") ? condtab.get(op1) : regtab.get(op1)) + ")");
                    if (op2.startsWith("=")) {
                        String litVal = op2.substring(2, op2.length() - 1);
                        out.print(" (L," + getLitIndex(litVal) + ")");
                    } else {
                        out.print(" (S," + getSymIndex(op2) + ")");
                    }
                }
            }

            out.println();
            lc++;
        }
    }

    static boolean isKeyword(String s) {
        return optab.containsKey(s) || regtab.containsKey(s) ||
                s.matches("START|END|ORIGIN|EQU|LTORG|DC|DS");
    }

    static int getSymIndex(String sym) {
        if (!symtab.containsKey(sym))
            symtab.put(sym, -1);
        int idx = 1;
        for (String k : symtab.keySet()) {
            if (k.equals(sym))
                return idx;
            idx++;
        }
        return idx;
    }

    static int getLitIndex(String lit) {
        // Check only in the current pool (poolLits) to avoid reusing old pool literals
        if (!poolLits.contains(lit)) {
            poolLits.add(lit);
            littab.add(lit);
            litaddr.add(-1);
        }

        // Return the actual position in littab (1-indexed)
        // Find the last occurrence of this literal (in current pool)
        for (int i = littab.size() - 1; i >= 0; i--) {
            if (littab.get(i).equals(lit) && litaddr.get(i) == -1) {
                return i + 1;
            }
        }

        return littab.size(); // fallback
    }

    static void processLTORG() throws IOException {
        // Start a new literal pool
        if (!poolLits.isEmpty()) {
            pooltab.add(littab.size() - poolLits.size() + 1);
        }

        for (int i = 0; i < littab.size(); i++) {
            if (litaddr.get(i) == -1) {
                litaddr.set(i, lc);
                out.println(lc + " (DL,01) (C," + littab.get(i) + ")");
                lc++;
            }
        }
        poolLits.clear(); // clear after processing this pool
    }

    static int eval(String expr) {
        if (expr.contains("+")) {
            String[] p = expr.split("\\+");
            return symtab.getOrDefault(p[0], 0) + Integer.parseInt(p[1]);
        }
        if (expr.contains("-")) {
            String[] p = expr.split("-");
            return symtab.getOrDefault(p[0], 0) - Integer.parseInt(p[1]);
        }
        return symtab.getOrDefault(expr, 0);
    }

    static void saveTables() throws IOException {
        PrintWriter w = new PrintWriter("symtab.txt");
        w.println("Sr.No\tSymbol\t\tAddress");
        int n = 1;
        for (Map.Entry<String, Integer> e : symtab.entrySet())
            w.println(n++ + "\t" + e.getKey() + "\t\t" + e.getValue());
        w.close();

        w = new PrintWriter("littab.txt");
        w.println("Sr.No\tLiteral\t\tAddress");
        for (int i = 0; i < littab.size(); i++)
            w.println((i + 1) + "\t" + littab.get(i) + "\t\t" + litaddr.get(i));
        w.close();

        w = new PrintWriter("pooltab.txt");
        w.println("Pool\tStartsAtLiteralNo");
        for (int i = 0; i < pooltab.size(); i++)
            w.println((i + 1) + "\t\t" + pooltab.get(i));
        w.close();
    }
}
