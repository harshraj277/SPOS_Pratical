// File: AssemblerPass1.java
import java.util.*;
import java.io.*;
/*
 Simplified Pass I: reads lines until END, supports START, ORIGIN, EQU, LTORG, DS, DC, labels, simple IS/AD/DL detection by mnemonics map.
 Outputs: symbol table and intermediate representation (addressed lines).
*/
public class AssemblerPass1 {
    static Map<String,Integer> optab = new HashMap<>();
    static Set<String> ad = new HashSet<>(Arrays.asList("START","END","ORIGIN","LTORG","EQU"));
    static Set<String> dl = new HashSet<>(Arrays.asList("DS","DC"));
    public static void main(String[] args) throws Exception{
        // sample: read lines from stdin until END
        System.out.println("Enter assembly lines (END to stop):");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line; int lc=0;
        Map<String,Integer> symtab = new LinkedHashMap<>();
        List<String> intermediate = new ArrayList<>();
        List<String> literaltab = new ArrayList<>();
        while((line = br.readLine())!=null){
            line=line.trim();
            if(line.isEmpty()) continue;
            String[] parts = line.split("\\s+");
            // check START
            if(parts[0].equalsIgnoreCase("START")){
                lc = Integer.parseInt(parts[1]);
                intermediate.add("(AD,01) (C,"+lc+")\t; "+line);
                continue;
            }
            if(parts[0].equalsIgnoreCase("END")){
                intermediate.add("(AD,02)\t; END");
                break;
            }
            // label?
            String label=null; String instr; List<String> operands = new ArrayList<>();
            if(parts[0].endsWith(":") || (!isMnemonic(parts[0]) && parts.length>1 && (isMnemonic(parts[1])||ad.contains(parts[1])))){
                label = parts[0].replace(":", "");
                if(!symtab.containsKey(label)) symtab.put(label, lc);
                else symtab.put(label, lc); // update
                // shift
                parts = Arrays.copyOfRange(parts,1,parts.length);
            }
            instr = parts[0];
            if(ad.contains(instr.toUpperCase())){
                if(instr.equalsIgnoreCase("ORIGIN")){
                    // format ORIGIN L1+3 etc.
                    String expr = parts[1];
                    int newLc = evalOrigin(expr, symtab);
                    intermediate.add("(AD,03) (S,"+expr+")\t; ORIGIN "+expr);
                    lc = newLc;
                    continue;
                } else if(instr.equalsIgnoreCase("LTORG")){
                    intermediate.add("(AD,05)\t; LTORG");
                    // assign literals - simplified
                    for(String lit: literaltab){
                        intermediate.add("(L,"+lit+") -> (ADDED AT "+lc+")");
                        lc++;
                    }
                    literaltab.clear();
                    continue;
                } else if(instr.equalsIgnoreCase("EQU")){
                    // LABEL EQU expr
                    if(label!=null){
                        int val = evalOrigin(parts[1], symtab);
                        symtab.put(label, val);
                        intermediate.add("(AD,04) (S,"+parts[1]+")\t; EQU "+parts[1]);
                    }
                    continue;
                }
            } else if(dl.contains(instr.toUpperCase())){
                if(instr.equalsIgnoreCase("DS")){
                    int size = Integer.parseInt(parts[1]);
                    intermediate.add("(DL,02) (C,"+size+")\t; DS");
                    if(label!=null) symtab.put(label, lc);
                    lc += size;
                    continue;
                } else if(instr.equalsIgnoreCase("DC")){
                    intermediate.add("(DL,01) (C,"+parts[1]+")\t; DC");
                    if(label!=null) symtab.put(label, lc);
                    lc++;
                    continue;
                }
            } else {
                // Imperative statements - simple: MOVER, MOVEM, ADD, SUB, MULT, BC etc.
                // Check operands for literals
                for(int i=1;i<parts.length;i++){
                    String op=parts[i].replaceAll(",","");
                    if(op.startsWith("='")||op.startsWith("=\"")){
                        literaltab.add(op);
                    } else if(!op.matches("\\d+") && !op.matches("AREG|BREG|CREG|DREG") && !op.matches("[A-Za-z_]\\w*")){
                        // nothing
                    }
                }
                intermediate.add("(IS,??) "+line);
                if(label!=null) symtab.put(label, lc);
                lc++;
            }
        }
        System.out.println("\n--- Intermediate Code ---");
        intermediate.forEach(System.out::println);
        System.out.println("\n--- Symbol Table ---");
        symtab.forEach((k,v)->System.out.println(k + " -> " + v));
        System.out.println("\n--- Literal Table (unassigned in this simplified pass) ---");
        literaltab.forEach(System.out::println);
    }
    static boolean isMnemonic(String s){
        String up=s.toUpperCase();
        return up.equals("MOVER")||up.equals("MOVEM")||up.equals("ADD")||up.equals("SUB")||up.equals("MULT")||up.equals("BC")||up.equals("STOP");
    }
    static int evalOrigin(String expr, Map<String,Integer> sym){
        // supports forms like L1+3 or NEXT+5 or numeric constant
        if(expr.matches("\\d+")) return Integer.parseInt(expr);
        String e = expr;
        int add = 0;
        if(e.contains("+")){
            String[] parts = e.split("\\+");
            e = parts[0];
            add = Integer.parseInt(parts[1]);
        } else if(e.contains("-")){
            String[] parts = e.split("-");
            e = parts[0];
            add = -Integer.parseInt(parts[1]);
        }
        return sym.getOrDefault(e,0) + add;
    }
}
