package tests;

import static org.junit.Assert.*;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

import org.junit.*;
import chord.*;
import static chord.Configuration.*;
import static chord.ChordSimulationUtil.*;


public class ChordNodeTest {
	
	public static final int TEST_NODES = 5;

	@Test
	public void inIntervalTest(){
		assertTrue(FunctionTest.inInterval(151, 176, false, true, 155));
		
		assertFalse(FunctionTest.inInterval(151, 176, false, false,15));

		assertTrue(FunctionTest.inInterval(176, 151, true, false, 250));

		assertTrue(FunctionTest.inInterval(176, 151, false, true, 1));
		
		assertFalse(FunctionTest.inInterval(175, 150, false, false, 151));

		assertFalse(FunctionTest.inInterval(175, 150, false, true, 175));

		assertTrue(FunctionTest.inInterval(175, 150, true, true, 150));

		assertFalse(FunctionTest.inInterval(175, 150, true, false, 150));

	}
	
	@Test
	public void addNodeTest() {
		Scanner myObj = new Scanner(System.in);  // Create a Scanner object
	    System.out.println("Enter 1 to run test");
	    System.out.println("Enter any other to end test");
	    String input = myObj.nextLine();  // Read user input
	    while(input.equals("1")) {
			//generate test nodes
			ArrayList<ChordNode> nodes = new ArrayList<>();
			ArrayList<Integer> hashes = new ArrayList<>();
			
			byte[] randomArray = new byte[10];
			for(int i = 0; i < TEST_NODES; i++) {
				new Random().nextBytes(randomArray);
				ChordNode testNode = new ChordNode(new String(randomArray, Charset.forName("UTF-8")));
			
				while(hashes.contains(testNode.getHash())) {
					new Random().nextBytes(randomArray);
					testNode = new ChordNode(new String(randomArray, Charset.forName("UTF-8")));
				}
				
				nodes.add(testNode);
				hashes.add(testNode.getHash());
			}
			
			Collections.sort(hashes);
			
			//adding all nodes to the system
			System.out.println("adding node 0 with hash "+ nodes.get(0).getHash());
			nodes.get(0).add(null);
			
			for(int i = 1; i< nodes.size();i++) {
				System.out.println("adding node "+ i +" with hash "+ nodes.get(i).getHash());
				nodes.get(i).add(nodes.get(i-1));
			}
			
			
			for(ChordNode n : nodes) {
				for(int i = 0; i<HASH_LENGTH; i++) {
					assertEquals("finger "+ i + " of node "+ n.getHash() + " is " + n.getFingerTableEntry(i).getHash() + " instead of " + getNextResponsibleHash(hashes,addPow(n.getHash(),i)),
							n.getFingerTableEntry(i).getHash(),
							getNextResponsibleHash(hashes,addPow(n.getHash(),i)));
				}
				assertEquals("predecessor of " + n.getHash() + " is " + n.getPredecessor().getHash(),
						n.getPredecessor().getFingerTableEntry(0),n);
			}
			
			nodes.get(4).remove();
			hashes.remove((Integer) nodes.get(4).getHash());
			
			nodes.remove(4);
			
			for(int i = 0;i<hashes.size();i++) {
				System.out.println(hashes.get(i));
			}
			
			for(ChordNode n : nodes) {
				for(int i = 0; i<HASH_LENGTH; i++) {
					assertEquals("finger "+ i + " of node "+ n.getHash() + " is " + n.getFingerTableEntry(i).getHash() + " instead of " + getNextResponsibleHash(hashes,addPow(n.getHash(),i)),
							n.getFingerTableEntry(i).getHash(),
							getNextResponsibleHash(hashes,addPow(n.getHash(),i)));
				}
				assertEquals("predcessor of " + n.getHash() + " is " + n.getPredecessor().getHash(),
						n.getPredecessor().getFingerTableEntry(0),n);
			}
			System.out.println();
			printAll(nodes.get(0));
			System.out.println("Enter 1 to run test");
		    System.out.println("Enter 2 to end test");
		    input = myObj.nextLine();  
	    }
	    System.out.println("Program Ends");
	}
	
	private int getNextResponsibleHash(ArrayList<Integer> hashes, int target) {
		int index = Collections.binarySearch(hashes, target);
		if(index < 0) {
			index = -index-1;
			if(index == hashes.size()) {
				index = 0;
			}
		}
		return hashes.get(index);
	}
	
}
