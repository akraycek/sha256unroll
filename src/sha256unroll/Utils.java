package sha256unroll;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author chabapok
 */
public class Utils {
    
    static char not(char v){
        if (v=='*') return '*';
        if (v=='1') return '0';
        return '1';
    }
    
    //"a" может превращаться в "b"
    static boolean mayConverted(char a, char b) {
        return a==b || (a=='*');
    }
    
    static Collection<String> combineNotConflicted(Collection<String> aArr, Collection<String> bArr){
        ConsolidateSet result = new ConsolidateSet( aArr.size() + bArr.size() );
        
        for(String aVariant:aArr ){
            for(String bVariant:bArr ){
                String combined = combine(aVariant, bVariant);
                if (combined!=null) result.consolidate(combined);
            }    
        }
        
        return result;
    }

    /**
     * 
     * Уточнение.
     *  0*, *0, 00 -> 0
     *  1*, *1, 11 -> 1
     *  ** - >*
     * 01,10 - запрещено
     * 
     * @param aVariant
     * @param bVariant
     * @return 
     */
    static String combine(String aVariant, String bVariant) {
        if (aVariant.length()!=bVariant.length()) throw new RuntimeException("Length not same! "+aVariant+":"+bVariant);
        
        StringBuilder sb = new StringBuilder(aVariant.length());
        
        for(int i=0; i<aVariant.length(); i++){
            char a = aVariant.charAt(i);
            char b = bVariant.charAt(i);
            
            if (a=='*') {sb.append(b); continue;}
            if (b=='*') {sb.append(a); continue;}
            if (a==b) {sb.append(a); continue;}
            return null; //01 or 10
        }
        return sb.toString();
    }
    
    
    static Collection<String> removeDupes(Collection<String> aArr, Collection<String> bArr){
        ConsolidateSet result = new ConsolidateSet( aArr.size()+bArr.size() );
        result.consolidate(aArr);
        result.consolidate(bArr);        
        return result;
    }

    
    static Node op(char op, Node ... args){
        
        Node n2 = new Node(op, args[0], args[1]);
        switch(args.length){
            case 2: return n2;
            case 3: return new Node(op, n2, args[2]);
            default: {
                    Node a[] = Arrays.copyOfRange(args, 2, args.length);
                    Node otherNodes = op(op, a);
                    Node r = new Node(op, n2, otherNodes);
                    return r;
                }
        }
    }
    
    static Node or(Node ... args){  return op('+', args); }
    static Node and(Node ... args){ return op('*', args); }
    static Node xor(Node ... args){ return op('^', args); }
    static Node not(Node arg){      return new Node('!', arg); }
    
 
    static Bits32 and(Bits32 a, Bits32 b){return a.and(b);}
    static Bits32 or(Bits32 a, Bits32 b){return a.or(b);}
    static Bits32 xor(Bits32 a, Bits32 b){return a.xor(b);}
    static Bits32 not(Bits32 a){return a.not();}

    
    static Bits32 add(Bits32 v1, Bits32 v2){ return v1.add(v2);}

    
    
    private static Node xNodes[] = new Node[1024];
    static Node x(int i){
        if (xNodes[i]==null){
            xNodes[i] =new EndNode(i);
        }
        return xNodes[i];
    }
}
