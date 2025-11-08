// File: RoundRobin.java
import java.util.*;
public class RoundRobin {
    static class P{String id;int at,bt,rt,ct,wt,tat;P(String i,int a,int b){id=i;at=a;bt=b;rt=b;}}
    public static void main(String[] args){
        int tq=2;
        List<P> list = new ArrayList<>();
        list.add(new P("P1",0,6)); list.add(new P("P2",1,4)); list.add(new P("P3",4,8)); list.add(new P("P4",3,3));
        int time=0; Queue<Integer> q=new LinkedList<>();
        boolean[] inQueue=new boolean[list.size()];
        int completed=0, n=list.size();
        // push arrived at time 0
        for(int i=0;i<n;i++) if(list.get(i).at<=time){ q.add(i); inQueue[i]=true; }
        System.out.println("Gantt:");
        while(completed<n){
            if(q.isEmpty()){
                time++;
                for(int i=0;i<n;i++) if(!inQueue[i] && list.get(i).at<=time){ q.add(i); inQueue[i]=true; }
                continue;
            }
            int idx=q.poll();
            P p=list.get(idx);
            int exec = Math.min(tq, p.rt);
            System.out.print(time + ":" + p.id + " ");
            p.rt -= exec; time += exec;
            // add newly arrived
            for(int i=0;i<n;i++) if(!inQueue[i] && list.get(i).at<=time){ q.add(i); inQueue[i]=true; }
            if(p.rt>0) q.add(idx);
            else { p.ct=time; p.tat = p.ct - p.at; p.wt = p.tat - p.bt; completed++; }
        }
        System.out.println("\n\nResults:");
        double wt=0,tat=0;
        for(P p:list){ System.out.printf("%s %d %d %d %d\n",p.id,p.at,p.bt,p.ct,p.wt); wt+=p.wt; tat+=p.tat;}
        System.out.printf("Avg TAT=%.2f Avg WT=%.2f\n",tat/n,wt/n);
    }
}
