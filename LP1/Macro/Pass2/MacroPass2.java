// import java.util.*;
// import java.io.*;

// class MNTEntry
// {
//     String name;
//     int pp,kp,mdtp,kpdtp;

//     public MNTEntry(String name,int pp,int kp,int mdtp, int kpdtp)
//     {
//         this.name = name;
//         this.pp = pp;
//         this.kp = kp;
//         this.mdtp = mdtp;
//         this.kpdtp = kpdtp;
//     }

//     String getName()
//     { return name;}

//     int getPP()
//     {return pp;}

//     int getKP()
//     {return kp;}

//     int getMDTP()
//     {return mdtp;}

//     int getKPDTP()
//     {return kpdtp;}
// }


// public class MacroPass2
// {
//     public static void main(String args[])
//     {
//         BufferedReader irb = null, mdtb = null, mntb = null, kpdtb = null;
//         FileWriter ps2 = null, ala = null;

//         try{
//             irb = new BufferedReader(new FileReader("intermediate.txt"));
//             mdtb = new BufferedReader(new FileReader("mdt.txt"));
//             mntb = new BufferedReader(new FileReader("mnt.txt"));
//             kpdtb = new BufferedReader(new FileReader("kpdt.txt"));
//             ps2 = new FileWriter("pass2.txt");
//             ala = new FileWriter("ala.txt");

//             HashMap<String,MNTEntry> mnt = new HashMap<>();
//             HashMap<Integer,String> aptab = new HashMap<>();
//             HashMap<String,Integer> aptabInverse = new HashMap<>();
//             Vector<String> mdt = new Vector<>();
//             Vector<String> kpdt = new Vector<>();

//             String line;
//             while((line = mdtb.readLine()) != null)
//                 mdt.add(line);

//             while((line = kpdtb.readLine()) != null)
//                 kpdt.add(line);

//             while((line = mntb.readLine()) != null)
//             {
//                 String parts[] = line.trim().split("\\s+");
//                 if(parts.length == 5)
//                 {
//                     mnt.put(parts[0],new MNTEntry(
//                         parts[0],
//                         Integer.parseInt(parts[1]),
//                         Integer.parseInt(parts[2]),
//                         Integer.parseInt(parts[3]),
//                         Integer.parseInt(parts[4])
//                     ));
//                 }
//             }


//             while((line = irb.readLine()) != null)
//             {
//                 String parts[] = line.trim().split("\\s+");
//                 if(mnt.containsKey(parts[0]))
//                 {
//                     aptab.clear();
//                     aptabInverse.clear();

//                     MNTEntry entry = mnt.get(parts[0]);
//                     int pp = entry.getPP();
//                     int kp = entry.getKP();
//                     int mdtp = entry.getMDTP();
//                     int kpdtp = entry.getKPDTP();

//                     int paramNo =1;

//                     for(int i = 1; i<= pp && i<parts.length; i++)
//                     {
//                         String pparam = parts[i].replaceAll(",","");
//                         aptab.put(paramNo,pparam);
//                         aptabInverse.put("P" + paramNo, paramNo);
//                         paramNo++;
//                     }

//                     int j = kpdtp - 1;
//                     for(int i = 0; i< kp && j < kpdt.size(); i++,j++)
//                     {   
//                         String temp[] = kpdt.get(j).split("\t");
//                         if(temp.length == 2)
//                         {
//                             aptab.put(paramNo, temp[1]);
//                             aptabInverse.put(temp[0].replaceAll("&",""),paramNo);
//                             paramNo++;
//                         }
//                     }

//                     for(int i = pp + 1; i<parts.length; i++)
//                     {
//                         String param = parts[i].replaceAll(",","");
//                         String temp[] = param.split("=");
//                         if(temp.length == 2)
//                         {
//                             String name = temp[0].replaceAll("&","");
//                             if(aptabInverse.containsKey(name))
//                             {
//                                 int idx = aptabInverse.get(name);
//                                 aptab.put(idx,temp[1]);
//                             }
//                         }
//                     }

//                     ala.write("ALA FOR MACRO " + entry.name + "\n");
//                     for(int k : aptab.keySet())
//                     {
//                         ala.write(k + "\t" + aptab.get(k) + "\n");
//                     }
//                     ala.write("\n");


//                     int i = mdtp - 1;
//                     while(i < mdt.size() && !mdt.get(i).equalsIgnoreCase("MEND"))
//                     {
//                         String temp[] = mdt.get(i).split("\\s+");
//                         ps2.write("+ ");
//                         for(String t : temp)
//                         {
//                             if(t.contains("(P,"))
//                             {
//                                 int idx = Integer.parseInt(t.replaceAll("[^0-9]",""));
//                                 String value = aptab.get(idx);
//                                 ps2.write((value!=null ? value : "null") + "\t" );
//                             }
//                             else
//                             {
//                                 ps2.write(t + "\t");
//                             }
//                         }
//                         ps2.write("\n");
//                         i++;
//                     }
//                 }
//                 else
//                 {
//                     ps2.write(line + "\n");
//                 }
//             }  
//         }
//         catch(Exception e) { System.out.println(e);}
           
//         finally{
//             try{
//             if (irb != null) irb.close();
//             if (mdtb != null) mdtb.close();
//             if (kpdtb != null) kpdtb.close();
//             if (mntb != null) mntb.close();
//             if (ps2 != null) ps2.close();
//             if (ala != null) ala.close();
//             }
//             catch(Exception e){System.out.println(e);}
                
//         }
//     }
// }


import java.util.*;
import java.io.*;

class MNTEntry
{
    String name;
    int pp,kp,mdtp,kpdtp;

    MNTEntry(String name, int pp, int kp, int mdtp, int kpdtp)
    {
        this.name = name;
        this.pp = pp;
        this.kp = kp;
        this.mdtp = mdtp;
        this.kpdtp = kpdtp;
    }

    String getName()
    {return this.name;}

    int getPP()
    {return this.pp;}

    int getKP()
    {return this.kp;}

    int getMDTP()
    {return this.mdtp;}

    int getKPDTP()
    {return this.kpdtp;}
}

public class MacroPass2
{
    public static void main(String args[]) throws IOException
    {
        BufferedReader irb = null, mntb = null, mdtb = null, kpdtb = null;
        FileWriter ps2 = null, ala = null;

        try
        {
             irb = new BufferedReader(new FileReader("intermediate.txt"));
             mntb = new BufferedReader(new FileReader("mnt.txt"));
             mdtb = new BufferedReader(new FileReader("mdt.txt"));
             kpdtb = new BufferedReader(new FileReader("kpdt.txt"));
             ps2 = new FileWriter("pass2.txt");
            ala = new FileWriter("ala.txt");

            HashMap<String,MNTEntry> mnt = new HashMap<>();
            HashMap<Integer,String> aptab = new HashMap<>();
            HashMap<String,Integer> aptabInverse =  new HashMap<>();
            Vector<String> mdt = new Vector<>();
            Vector<String> kpdt = new Vector<>();

            String line;
            while((line = mdtb.readLine()) != null)
            {
                mdt.add(line);
            }

            while((line = kpdtb.readLine()) != null)
            {
                kpdt.add(line);
            }

            while((line = mntb.readLine()) != null)
            {
                String parts[] = line.trim().split("\\s+");
                if(parts.length == 5)
                {
                    mnt.put(parts[0],new MNTEntry(
                        parts[0],
                        Integer.parseInt(parts[1]),
                        Integer.parseInt(parts[2]),
                        Integer.parseInt(parts[3]),
                        Integer.parseInt(parts[4])
                    ));
                }
            }

            while((line = irb.readLine()) != null)
            {
                String parts[] = line.trim().split("\\s+");
                if(mnt.containsKey(parts[0]))
                {
                    aptab.clear();
                    aptabInverse.clear();

                    MNTEntry entry = mnt.get(parts[0]);
                    int pp = entry.pp;
                    int kp = entry.kp;
                    int mdtp = entry.mdtp;
                    int kpdtp = entry.kpdtp;

                    int paramNo = 1;

                    for(int i=1; i<=pp && i<parts.length; i++)
                    {
                        String pparam = parts[i].replaceAll(",","");
                        aptab.put(paramNo,pparam);
                        aptabInverse.put("P" + paramNo,paramNo);
                        paramNo++;
                    }


                    int j = kpdtp -1;
                    for(int i =0; i<kp && j < kpdt.size(); i++,j++)
                    {
                        String temp[] = kpdt.get(j).split("\t");
                        if(temp.length == 2)
                        {
                            aptab.put(paramNo,temp[1]);
                            aptabInverse.put(temp[0].replaceAll("&",""), paramNo);
                            paramNo++;
                        }
                    }


                    for(int i = pp+1; i<parts.length; i++)
                    {
                        String param = parts[i].replaceAll(",","");
                        String temp[] = param.split("=");
                        if(temp.length == 2)
                        {
                            String name = temp[0].replaceAll("&","");
                            if(aptabInverse.containsKey(name))
                            {
                                int idx = aptabInverse.get(name);
                                aptab.put(idx, temp[1]);
                            }
                        }
                    }


                    ala.write("ALA FOR MACRO" + entry.name + "\n");
                    for(int k : aptab.keySet())
                    {
                        ala.write(k + "\t" + aptab.get(k) + "\n");
                    }
                    ala.write("\n");

                    
                    int i = mdtp -1;
                    while(i<mdt.size() && !mdt.get(i).equalsIgnoreCase("MEND"))
                    {
                        String temp[] = mdt.get(i).split("\\s+");
                        ps2.write(" + ");
                        for(String t: temp)
                        {
                            if(t.contains("(P,"))
                            {
                                int idx = Integer.parseInt(t.replaceAll("[^0-9]",""));
                                String value = aptab.get(idx);
                                ps2.write((value !=null ? value : "null") + "\t" );
                            }
                            else
                            {
                                ps2.write(t + "\t");
                            }
                        }
                        ps2.write("\n");
                        i++;
                    }

                }
                else
                {
                    ps2.write(line + "\n");
                }
            }
        }
        catch(Exception e)
        {System.out.println(e);}

        finally{
            try{
                if(irb!=null) irb.close();
                if(mntb!=null) mntb.close();
                if(mdtb!=null) mdtb.close();
                if(kpdtb!=null) kpdtb.close();
                if(ps2!=null) ps2.close();
                if(ala!=null) ala.close();
            }
            catch(Exception e)
            {System.out.println(e);}
        }
    }
}