/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package musicgen.PatternGeneration;

import java.util.ArrayList;
import java.util.Random;
import musicgen.MusicGen;

/**
 *
 * @author FurEterinUmbrae
 */
public class Markov {
    
    public int currentNode;
    
    private ArrayList<Node> nodes = new ArrayList<>();
    public Random ran = new Random(MusicGen.seed);
    public int octShift = 0;
    
    
    public Markov(int[] keyscale){
        System.out.println("Num Nodes: " + keyscale.length);
        for(int i = 0; i < keyscale.length;i++){
            nodes.add(new Node(keyscale[i]));
        }
        for(int i = 0; i < nodes.size();i++){
            int numEdge = Math.abs(ran.nextInt()%nodes.size())+1;
            System.out.println("Node " + i + " Has " + numEdge + " Edges");
            for(int j = 0; j < numEdge;j++){
                int node = ran.nextInt(nodes.size());
                if(!nodes.get(i).hasEdge(nodes.get(node))){
                    nodes.get(i).addEdge(new Edge(ran.nextDouble(),nodes.get(node)));
                }
                
            }
        }
        
        currentNode = ran.nextInt(nodes.size());
        
        System.out.println("Markov Created");
        
    }
    
    public int[][] makeMusic(int iterations){
        int[][] i = new int[iterations][3];
        if(ran.nextDouble() < MusicGen.volitility){
            if(ran.nextDouble() <=.5){
                octShift-=1;
            }else{
                octShift +=1;
            }
        }
        if((Math.abs(octShift)/4) > (.5*ran.nextDouble()+.2)){
            octShift = 0;
        }
        for(int j = 0; j < iterations; j++){
            int time;
            int left = iterations - j;
            time = ran.nextInt(left) + 1;
            if(time > 4){
                time = 4;
            }
            if(ran.nextDouble() < MusicGen.lenVolt){
                time = 1;
            }
            move();
            i[j][0] = nodes.get(currentNode).key + octShift*12;
            i[j][1] = time;
            i[j][2] = j;
            j+=time-1;
        }
        return i;
    }
    
    public int[] makeMusic1D(int iterations){
        int[] i = new int[iterations];
        if(ran.nextDouble() < MusicGen.volitility){
            if(ran.nextDouble() <=.5){
                octShift-=1;
            }else{
                octShift +=1;
            }
        }
        if((Math.abs(octShift)/4) > (.5*ran.nextDouble()+.2)){
            octShift = 0;
        }
        for(int j = 0; j < iterations; j++){
            move();
            i[j] = nodes.get(currentNode).key + octShift*12;
        }
        return i;
    }
    
    public int[] makeSectionOrder(int iterations){
        int[] i = new int[iterations];
        for(int j = 0; j < iterations; j++){
            move();
            i[j] = nodes.get(currentNode).key;
        }
        return i;
    }
    
    public void move(){
        currentNode = nodes.indexOf(nodes.get(currentNode).nextNode(ran));
    }
    
}
