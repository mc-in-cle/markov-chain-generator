package world.info.minorcline;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Map.Entry;

public class TrieTest{
    private Trie<Character, String> sut;
    private List<Character> listABCD;
    private List<Character> listABCE;
    private List<Character> listABC;
    private List<Character> listAB;
    private List<Character> listZZ;
    boolean pass = true;

    public TrieTest(){
        sut = new Trie<>();
        makeLists();
    }

    private void makeLists(){
        listABCD = new ArrayList<>();
        listABCD.add('a');
        listABCD.add('b');
        listABCD.add('c');
        listABCD.add('d');

        listABCE = new ArrayList<>();
        listABCE.add('a');
        listABCE.add('b');
        listABCE.add('c');
        listABCE.add('d');
        listABCE.add('e');

        listABC = new ArrayList<>();
        listABC.add('a');
        listABC.add('b');
        listABC.add('c');

        listAB = new ArrayList<>();
        listAB.add('a');
        listAB.add('b');

        listZZ = new ArrayList<>();
        listZZ.add('z');
        listZZ.add('z');
    }

    public void runTests(){
        Map<String, Boolean> testResults = new HashMap<>();
        
        testResults.put("testPut1", testPut1());
        testResults.put("testPut2", testPut2());
        testResults.put("testGet1", testGet1());
        testResults.put("testGetNull1", testGetNull1());
        testResults.put("testGetNull1", testGetNull2());
        testResults.put("testContains1", testContains1());
        testResults.put("testDoesNotContain1", testDoesNotContain1());
        
        testResults.put("testRemoveContains", testRemoveContains());
        
        testResults.put("testRemoveDoesNotRemoveParent", testRemoveDoesNotRemoveParent());
        testResults.put("testRemoveDoesNotRemoveSiblin", testRemoveDoesNotRemoveSibling());
        testResults.put("testSizeEmpty", testSizeEmpty());
        testResults.put("testSizeAddOne", testSizeAddOne());
        testResults.put("testSizeAddSeveral", testSizeAddSeveral());
        testResults.put("testSizeRemoveOne", testSizeRemoveOne());
        

        for (Entry<String, Boolean> e : testResults.entrySet()){
            if (e.getValue() == false){
                System.out.println("Test failure: " + e.getKey());
                pass = false;
            }
        }
        if (pass)
            System.out.println("Passed all tests.");
    }
    public static void main(String[] args){
        TrieTest test = new TrieTest();
        test.runTests();
    }

    public boolean testPut1(){
        sut.clear();
        sut.put(listABCD, "TestABCD");
        String result = sut.get(listABCD);
        return "TestABCD".equals(result);
    }
    
    public boolean testPut2(){
        sut.clear();
        sut.put(listABCD, "TestABCD");
        sut.put(listABC, "TestABC");
        String result = (String)(sut.get(listABC));
        return "TestABC".equals(result);
    }

    public boolean testGet1(){
        sut.clear();
        sut.put(listABCD, "TestABCD");
        String result = (String)(sut.get(listABCD));
        return "TestABCD".equals(result);
    }

    public boolean testGetNull1(){
        sut.clear();
        sut.put(listABC, "TestABC");
        String result = (String)(sut.get(listAB));
        return result == null;
    }

    public boolean testGetNull2(){
        sut.clear();
        sut.put(listABC, "Go!ABC");
        String result = (String)(sut.get(listZZ));
        return result == null;
    }

    public boolean testContains1(){
        sut.clear();
        sut.put(listABCD, "TestABCD");
        boolean result = sut.containsKey(listABCD);
        return result == true;
    }

    public boolean testDoesNotContain1(){
        sut.clear();
        sut.put(listABC, "YE");
        boolean result = sut.containsKey(listAB);
        return result == false;
    }

    public boolean testDoesNotContain2(){
        sut.clear();
        sut.put(listABC, "YE");
        boolean result = sut.containsKey(listZZ);
        return result == false;
    }

    public boolean testRemoveContains(){
        sut.clear();
        sut.put(listAB, "YE");
        sut.put(listABCD, "GO-ABCD!");
        sut.remove(listABCD);
        boolean result = sut.containsKey(listABCD);
        return result == false;
    }

    public boolean testRemoveDoesNotRemoveParent(){
        sut.clear();
        sut.put(listABCD, "GO-ABCD!");
        sut.put(listABC, "YESABC");
        sut.remove(listABCD);
        boolean result = sut.containsKey(listABC);
        return result == true;
    }

    public boolean testRemoveDoesNotRemoveSibling(){
        sut.clear();
        sut.put(listABCD, "GO-ABCD!");
        sut.put(listABCE, "YESABCE");
        sut.remove(listABCD);
        boolean result = sut.containsKey(listABCE);
        return result == true;
    }

    public boolean testSizeEmpty(){
        sut.clear();
        return sut.size() == 0;
    }

    public boolean testSizeAddOne(){
        sut.clear();
        sut.put(listZZ, "OOOO");
        return sut.size() == 1;
    }

    public boolean testSizeAddSeveral(){
        sut.clear();
        sut.put(listABCD, "B");
        sut.put(listABCE, "OO");
        sut.put(listAB, "FSFS");
        return sut.size() == 3;
    }

    public boolean testSizeRemoveOne(){
        sut.clear();
        sut.put(listABCD, "B");
        sut.put(listABCE, "OO");
        sut.remove(listABCD);
        return sut.size() == 1;
    }
}