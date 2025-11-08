// File: SJFPreemptive.java
import java.util.*;
public class SJFPreemptive {
    static class P{String id;int at,bt,rt,ct,wt,tat;P(String i,int a,int b){id=i;at=a;bt=b;rt=b;}}
    public static void main(String[] args){
        List<P> list = new ArrayList<>();
        list.add(new P("P1",0,6));
        list.add(new P("P2",1,4));
        list.add(new P("P3",4,8));
        list.add(new P("P4",3,3));
        int time=0, completed=0, n=list.size();
        P current=null;
        System.out.println("Gantt (time:process):");
        while(completed < n){
            // pick process with min remaining time among arrived
            P best=null;
            for(P p: list) if(p.at <= time && p.rt>0){
                if(best==null || p.rt < best.rt) best=p;
            }
            if(best==null){ time++; continue; }
            System.out.print(time + ":" + best.id + " ");
            best.rt--; time++;
            if(best.rt==0){ best.ct=time; best.tat=best.ct-best.at; best.wt=best.tat-best.bt; completed++; }
        }
        System.out.println("\n\nResults:");
        double wt=0,tat=0;
        System.out.println("ID AT BT CT TAT WT");
        for(P p: list){ System.out.printf("%s %d %d %d %d %d\n",p.id,p.at,p.bt,p.ct,p.tat,p.wt); wt+=p.wt; tat+=p.tat;}
        System.out.printf("Avg TAT=%.2f Avg WT=%.2f\n",tat/n,wt/n);
    }
}
