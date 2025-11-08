// File: MemoryPlacement.java
import java.util.*;
public class MemoryPlacement {
    static void run(String name, int[] parts, int[] procs){
        System.out.println("\n--- " + name + " ---");
        int m = parts.length;
        int n = procs.length;
        int[] part = Arrays.copyOf(parts,m);
        int[] assign = new int[n]; Arrays.fill(assign, -1);
        if(name.equals("First Fit")){
            for(int i=0;i<n;i++){
                for(int j=0;j<m;j++) if(part[j] >= procs[i]){ assign[i]=j; part[j]-=procs[i]; break; }
            }
        } else if(name.equals("Best Fit")){
            for(int i=0;i<n;i++){
                int best=-1, bestSize=Integer.MAX_VALUE;
                for(int j=0;j<m;j++) if(part[j] >= procs[i] && part[j] < bestSize){ best=j; bestSize=part[j]; }
                if(best!=-1){ assign[i]=best; part[best]-=procs[i]; }
            }
        } else if(name.equals("Worst Fit")){
            for(int i=0;i<n;i++){
                int worst=-1, worstSize=-1;
                for(int j=0;j<m;j++) if(part[j] >= procs[i] && part[j] > worstSize){ worst=j; worstSize=part[j]; }
                if(worst!=-1){ assign[i]=worst; part[worst]-=procs[i]; }
            }
        } else if(name.equals("Next Fit")){
            int last=0;
            for(int i=0;i<n;i++){
                boolean placed=false;
                for(int k=0;k<m;k++){
                    int j = (last + k) % m;
                    if(part[j] >= procs[i]){ assign[i]=j; part[j]-=procs[i]; last=j; placed=true; break; }
                }
                if(!placed) assign[i]=-1;
            }
        }
        System.out.println("Process\tSize\tPartitionAssigned");
        for(int i=0;i<n;i++){
            System.out.println("P"+(i+1)+"\t"+procs[i]+"\t"+(assign[i]==-1 ? "Not Allocated" : ("Partition"+(assign[i]+1))));
        }
        System.out.println("Remaining partition sizes: " + Arrays.toString(part));
    }
    public static void main(String[] args){
        int[] parts = {100,500,200,300,600};
        int[] procs = {212,417,112,426};
        run("First Fit", parts, procs);
        run("Best Fit", parts, procs);
        run("Next Fit", parts, procs);
        run("Worst Fit", parts, procs);
    }
}
