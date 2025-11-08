// File: PriorityNonPreemptive.java
import java.util.*;
public class PriorityNonPreemptive {
    static class P{String id;int at,bt,pr,ct,wt,tat;P(String i,int a,int b,int p){id=i;at=a;bt=b;pr=p;}}
    public static void main(String[] args){
        List<P> list = Arrays.asList(new P("P1",0,8,1), new P("P2",0,6,2), new P("P3",2,1,3), new P("P4",3,2,0));
        int time=0, completed=0, n=list.size();
        boolean[] done = new boolean[n];
        System.out.println("Gantt:");
        while(completed<n){
            int idx=-1;
            for(int i=0;i<n;i++){
                P p=list.get(i);
                if(!done[i] && p.at<=time){
                    if(idx==-1 || p.pr < list.get(idx).pr || (p.pr==list.get(idx).pr && p.at < list.get(idx).at)) idx=i;
                }
            }
            if(idx==-1){ time++; continue; }
            P sel = list.get(idx);
            System.out.print("["+time+"]= " + sel.id + " ");
            time += sel.bt;
            sel.ct = time; sel.tat = sel.ct - sel.at; sel.wt = sel.tat - sel.bt;
            done[idx]=true; completed++;
        }
        System.out.println("\n\nResults:");
        System.out.println("ID AT BT PR CT TAT WT");
        double wt=0,tat=0;
        for(P p:list){ System.out.printf("%s %d %d %d %d %d %d\n",p.id,p.at,p.bt,p.pr,p.ct,p.tat,p.wt); wt+=p.wt; tat+=p.tat;}
        System.out.printf("Avg TAT=%.2f Avg WT=%.2f\n",tat/n,wt/n);
    }
}
