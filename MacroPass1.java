// File: MacroPass1.java
import java.util.*;
import java.io.*;
/*
 Pass-I: Build MNT (macro name table), MDT (macro definition table), ALA (argument list array).
*/
public class MacroPass1 {
    static class Macro {
        String name; int mdtIndex; List<String> ala = new ArrayList<>();
        Macro(String n,int i){name=n;mdtIndex=i;}
    }
    public static void main(String[] args) throws Exception{
        System.out.println("Enter macro program lines. Type END to stop:");
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
        String line; Map<String,Macro> MNT = new LinkedHashMap<>();
        List<String> MDT = new ArrayList<>();
        int mdtPtr=0;
        while((line=br.readLine())!=null){
            line=line.trim();
            if(line.equalsIgnoreCase("END")) break;
            if(line.startsWith("MACRO")){
                // next line contains macro header
                String header = br.readLine().trim();
                String[] parts = header.split("\\s+");
                String mname = parts[0];
                String argsPart = header.substring(mname.length()).trim();
                Macro macro = new Macro(mname, mdtPtr);
                if(!argsPart.isEmpty()){
                    String[] argsList = argsPart.split(",");
                    for(String a: argsList){ macro.ala.add(a.trim()); }
                }
                MNT.put(mname, macro);
                MDT.add(header); mdtPtr++;
                // read body until MEND
                while((line=br.readLine())!=null){
                    line=line.trim();
                    MDT.add(line); mdtPtr++;
                    if(line.equalsIgnoreCase("MEND")) break;
                }
            } else {
                // invocation or other
            }
        }
        System.out.println("\nMacro Name Table (MNT):");
        int idx=0;
        for(Macro m: MNT.values()){
            System.out.println(idx++ + " -> " + m.name + " MDT index: " + m.mdtIndex);
        }
        System.out.println("\nMacro Definition Table (MDT):");
        for(int i=0;i<MDT.size();i++) System.out.println(i + ": " + MDT.get(i));
        System.out.println("\nArgument List Arrays (ALA):");
        for(Macro m: MNT.values()){
            System.out.println("MACRO: " + m.name);
            for(String a: m.ala) System.out.println("  " + a);
        }
    }
}
