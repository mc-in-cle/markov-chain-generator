package world.info.minorcline;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class HashPileTest {
	private HashPile<String> sut;
	private boolean pass = true;

    public HashPileTest(){
        sut = new HashPile<>();
    }
    
    public static void main(String[] args){
        HashPileTest test = new HashPileTest();
        test.runTests();
    }
    
    public void runTests(){
        Map<String, Boolean> testResults = new HashMap<>();
        
        testResults.put("testAdd1CheckSize", testAdd1CheckSize());
        testResults.put("testAdd1removeExactlySameCheckSize", testAdd1removeExactlySameCheckSize());
        testResults.put("testAdd1removeExactlyEqualCheckSize", testAdd1removeExactlyEqualCheckSize());
        testResults.put("testremoveExactlyNonMemberElement", testremoveExactlyNonMemberElement());
        testResults.put("testAddTwoCheckSize", testAddTwoCheckSize());
        testResults.put("testAddTworemoveExactly1CheckSize", testAddTworemoveExactly1CheckSize());
        testResults.put("testAddTwoEqualNotSame", testAddTwoEqualNotSame());

        for (Entry<String, Boolean> e : testResults.entrySet()){
            if (e.getValue() == false){
                System.out.println("Test failure: " + e.getKey());
                pass = false;
            }
        }
        if (pass)
            System.out.println("Passed all tests.");
    }
    
    public boolean testAdd1CheckSize(){
        sut.clear();
        sut.add("abcd");
        return sut.size() == 1;
    }
    
    public boolean testAdd1removeExactlySameCheckSize() {
    	String s = "abcd";
    	sut.clear();
    	sut.add(s);
    	sut.removeExactly(s);
    	return sut.size() == 0;
    }
    
    public boolean testAdd1removeExactlyEqualCheckSize() {
    	String s = "abcd";
    	String t = "ab";
    	t = t + "cd";
    	sut.clear();
    	sut.add(s);
    	sut.removeExactly(t);
    	return sut.size() == 1;
    }
    
    public boolean testremoveExactlyNonMemberElement() {
    	sut.clear();
    	sut.add("aa");
    	sut.removeExactly("zz");
    	return sut.size() == 1;
    }
    
    public boolean testAddTwoCheckSize() {
    	sut.clear();
    	sut.add("aa");
    	sut.add("bb");
    	return sut.size() == 2;
    }
    
    public boolean testAddTworemoveExactly1CheckSize() {
    	sut.clear();
    	sut.add("aa");
    	sut.add("bb");
    	sut.removeExactly("aa");
    	return sut.size() == 1;
    }
    
    public boolean testAddTwoEqualNotSame() {
	    String s = "abcd";
		String t = "ab";
		t = t + "cd";
		System.out.println(s == t);
		sut.clear();
		sut.add(s);
		sut.add(t);
		return sut.size() == 2;
    }
    

}
