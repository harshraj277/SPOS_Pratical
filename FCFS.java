// File: FCFS.java
import java.util.*;
public class FCFS {
    static class Proc { String id; int at, bt; int ct, tat, wt; }
    public static void main(String[] args){
        List<Proc> procs = new ArrayList<>();
        procs.add(make("P1",0,3));
        procs.add(make("P2",2,6));
        procs.add(make("P3",4,4));
        procs.add(make("P4",6,5));
        procs.add(make("P5",8,2));
        // sort by arrival
        procs.sort(Comparator.comparingInt(p->p.at));
        int time=0;
        System.out.println("Gantt:");
        for(Proc p: procs){
            if(time < p.at) time = p.at;
            System.out.print("[" + time + "]->" + p.id + " ");
            time += p.bt;
            p.ct = time; p.tat = p.ct - p.at; p.wt = p.tat - p.bt;
        }
        System.out.println("\n\nResults:");
        System.out.printf("ID\tAT\tBT\tCT\tTAT\tWT\n");
        double wtSum=0, tatSum=0;
        for(Proc p: procs){
            System.out.printf("%s\t%d\t%d\t%d\t%d\t%d\n", p.id, p.at, p.bt, p.ct, p.tat, p.wt);
            wtSum+=p.wt; tatSum+=p.tat;
        }
        System.out.printf("Average TAT= %.2f, Average WT= %.2f\n", tatSum/procs.size(), wtSum/procs.size());
    }
    static Proc make(String id,int at,int bt){ Proc p=new Proc(); p.id=id; p.at=at; p.bt=bt; return p; }
}
