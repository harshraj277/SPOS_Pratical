// File: MacroPass2.java
import java.util.*;
/*
 Simplified Pass-II example: expand macros given MNT/MDT/ALA (from provided sample).
*/
public class MacroPass2 {
    public static void main(String[] args){
        // Sample MDT (as per doc)
        String[] MDT = {
            "INCR &ARG1,&ARG2",
            "MOVER AREG,&ARG1",
            "ADD AREG,&ARG2",
            "MEND",
            "DECR &ARG3,&ARG4",
            "MOVER AREG,&ARG3",
            "SUB AREG,&ARG4",
            "MEND"
        };
        Map<String,Integer> MNT = new HashMap<>();
        MNT.put("INCR",0);
        MNT.put("DECR",4);
        Map<String,String[]> ALA = new HashMap<>();
        ALA.put("INCR", new String[]{"&ARG1","&ARG2"});
        ALA.put("DECR", new String[]{"&ARG3","&ARG4"});
        // Intermediate code / calls
        List<String> calls = Arrays.asList("INCR N1,N2", "DECR N3,N4");
        List<String> output = new ArrayList<>();
        for(String call: calls){
            String[] p = call.split("\\s+");
            String name = p[0];
            String args1 = p.length>1 ? p[1] : "";
            String[] actuals = args1.split(",");
            int mdtIndex = MNT.get(name);
            String[] formal = ALA.get(name);
            // build substitution map
            Map<String,String> sub = new HashMap<>();
            for(int i=0;i<formal.length;i++){
                sub.put(formal[i], actuals[i]);
            }
            // expand MDT starting at mdtIndex until MEND
            for(int i=mdtIndex;i<MDT.length;i++){
                String line = MDT[i];
                if(line.equals("MEND")) break;
                // replace formal args in the line
                for(String f: sub.keySet()){
                    line = line.replace(f, sub.get(f));
                }
                output.add(line);
            }
        }
        System.out.println("Output after macro expansion:");
        output.forEach(System.out::println);
    }
}
