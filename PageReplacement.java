// File: PageReplacement.java
import java.util.*;
public class PageReplacement {
    static int[] ref = {2,3,2,1,5,2,4,5,3,2,5,2};
    static int frames = 3;
    public static void main(String[] args){
        System.out.println("FIFO:");
        simulateFIFO();
        System.out.println("\nOptimal:");
        simulateOptimal();
        System.out.println("\nLRU:");
        simulateLRU();
    }
    static void simulateFIFO(){
        Set<Integer> s = new HashSet<>();
        Queue<Integer> q = new LinkedList<>();
        int faults=0;
        for(int r: ref){
            if(!s.contains(r)){
                if(q.size()==frames){
                    int rem=q.poll(); s.remove(rem);
                }
                q.add(r); s.add(r); faults++;
            }
            System.out.println("Ref "+r+" Frames: "+q);
        }
        System.out.println("Total faults: " + faults);
    }
    static void simulateOptimal(){
        Set<Integer> s = new HashSet<>();
        List<Integer> framesList = new ArrayList<>();
        int faults=0;
        for(int i=0;i<ref.length;i++){
            int r=ref[i];
            if(s.contains(r)){ System.out.println("Ref "+r+" Frames: "+framesList); continue; }
            if(framesList.size() < frames){
                framesList.add(r); s.add(r); faults++;
            } else {
                // find page with farthest next use
                int indexToReplace = -1; int farthest = -1;
                for(int j=0;j<frames;j++){
                    int page = framesList.get(j);
                    int nextUse = Integer.MAX_VALUE;
                    for(int k=i+1;k<ref.length;k++){ if(ref[k]==page){ nextUse=k; break; } }
                    if(nextUse > farthest){ farthest = nextUse; indexToReplace=j; }
                }
                s.remove(framesList.get(indexToReplace));
                framesList.set(indexToReplace, r); s.add(r); faults++;
            }
            System.out.println("Ref "+r+" Frames: "+framesList);
        }
        System.out.println("Total faults: " + faults);
    }
    static void simulateLRU(){
        Map<Integer,Integer> map = new LinkedHashMap<>();
        int faults=0;
        for(int i=0;i<ref.length;i++){
            int r=ref[i];
            if(map.containsKey(r)){
                map.remove(r); map.put(r,i);
            } else {
                if(map.size()==frames){
                    // remove least recently used (first entry)
                    Integer lru = map.keySet().iterator().next();
                    map.remove(lru);
                }
                map.put(r,i); faults++;
            }
            System.out.println("Ref "+r+" Frames: "+map.keySet());
        }
        System.out.println("Total faults: " + faults);
    }
}
