import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

class MNTEntry {
    String name;
    int pp, kp, mdtp, kpdtp;

    public MNTEntry(String name, int pp, int kp, int mdtp, int kpdtp) {
        this.name = name;
        this.pp = pp;
        this.kp = kp;
        this.mdtp = mdtp;
        this.kpdtp = kpdtp;
    }

    public String getName() { return name; }
    public int getPp() { return pp; }
    public int getKp() { return kp; }
    public int getMdtp() { return mdtp; }
    public int getKpdtp() { return kpdtp; }
}

public class MacroPass2 {
    public static void main(String[] args) {
        BufferedReader irb = null, mdtb = null, kpdtb = null, mntb = null;
        FileWriter fr = null, ala = null;

        try {
            // Input files
            irb = new BufferedReader(new FileReader("intermediate.txt"));
            mdtb = new BufferedReader(new FileReader("mdt.txt"));
            kpdtb = new BufferedReader(new FileReader("kpdt.txt"));
            mntb = new BufferedReader(new FileReader("mnt.txt"));

            // Output files
            fr = new FileWriter("pass2.txt");
            ala = new FileWriter("ala.txt");

            // Data structures
            HashMap<String, MNTEntry> mnt = new HashMap<>();
            HashMap<Integer, String> aptab = new HashMap<>();
            HashMap<String, Integer> aptabInverse = new HashMap<>();
            Vector<String> mdt = new Vector<>();
            Vector<String> kpdt = new Vector<>();

            // Read MDT
            String line;
            while ((line = mdtb.readLine()) != null)
                mdt.add(line);

            // Read KPDT
            while ((line = kpdtb.readLine()) != null)
                kpdt.add(line);

            // Read MNT
            while ((line = mntb.readLine()) != null) {
                String[] parts = line.trim().split("\\s+");
                if (parts.length >= 5) {
                    mnt.put(parts[0], new MNTEntry(
                            parts[0],
                            Integer.parseInt(parts[1]),
                            Integer.parseInt(parts[2]),
                            Integer.parseInt(parts[3]),
                            Integer.parseInt(parts[4])
                    ));
                }
            }

            // Process Intermediate Code
            while ((line = irb.readLine()) != null) {
                String[] parts = line.trim().split("\\s+");

                if (mnt.containsKey(parts[0])) {
                    // Found a macro call
                    aptab.clear();
                    aptabInverse.clear();

                    MNTEntry entry = mnt.get(parts[0]);
                    int pp = entry.getPp();
                    int kp = entry.getKp();
                    int kpdtp = entry.getKpdtp();
                    int mdtp = entry.getMdtp();

                    int paramNo = 1;

                    // --- Step 1: Positional parameters ---
                    for (int i = 1; i <= pp && i < parts.length; i++) {
                        String param = parts[i].replace(",", "");
                        aptab.put(paramNo, param);
                        aptabInverse.put("P" + paramNo, paramNo);
                        paramNo++;
                    }

                    // --- Step 2: Keyword parameters (default values) ---
                    int j = kpdtp - 1;
                    for (int i = 0; i < kp && j < kpdt.size(); i++, j++) {
                        String[] temp = kpdt.get(j).split("\t");
                        if (temp.length >= 2) {
                            aptab.put(paramNo, temp[1]);
                            aptabInverse.put(temp[0].replaceAll("&", ""), paramNo);
                            paramNo++;
                        }
                    }


                    // --- Step 3: Overwrite keyword parameters from call ---
                    for (int i = pp + 1; i < parts.length; i++) {
                        String param = parts[i].replace(",", "");
                        String[] splits = param.split("=");
                        if (splits.length == 2) {
                            String name = splits[0].replaceAll("&", "");
                            if (aptabInverse.containsKey(name)) {
                                int index = aptabInverse.get(name);
                                aptab.put(index, splits[1]); // update value
                            }
                        }
                    }

                    // --- Step 4: Write final ALA after processing ---
                    ala.write("Final ALA for macro: " + parts[0] + "\n");
                    for (int key : aptab.keySet()) {
                        ala.write(key + "\t" + aptab.get(key) + "\n");
                    }
                    ala.write("\n");

                    // --- Step 5: Process MDT ---
                    int i = mdtp - 1;
                    while (i < mdt.size() && !mdt.get(i).trim().equalsIgnoreCase("MEND")) {
                        String[] splits = mdt.get(i).trim().split("\\s+");
                        fr.write("+ ");
                        for (String split : splits) {
                            if (split.contains("(P,")) {
                                String paramIndex = split.replaceAll("[^0-9]", "");
                                try {
                                    int index = Integer.parseInt(paramIndex);
                                    String value = aptab.get(index);
                                    fr.write((value != null ? value : "null") + "\t");
                                } catch (NumberFormatException e) {
                                    fr.write(split + "\t");
                                }
                            } else {
                                fr.write(split + "\t");
                            }
                        }
                        fr.write("\n");
                        i++;
                    }
                } else {
                    // Not a macro call
                    fr.write(line + "\n");
                }
            }

        } catch (IOException e) {
            System.err.println("IO Error: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Number Format Error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected Error: " + e.getMessage());
        } finally {
            try {
                if (irb != null) irb.close();
                if (mdtb != null) mdtb.close();
                if (kpdtb != null) kpdtb.close();
                if (mntb != null) mntb.close();
                if (fr != null) fr.close();
                if (ala != null) ala.close();
            } catch (IOException e) {
                System.err.println("Error closing files: " + e.getMessage());
            }
        }
    }
}
