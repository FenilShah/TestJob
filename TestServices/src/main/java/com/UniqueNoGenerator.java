package com;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.math.RandomUtils;

import com.oomoqu.rest.util.CommonUtility;

public class UniqueNoGenerator {
	public static void main(String[] args){
		UniqueNoGenerator uniqueNoGenerator = new UniqueNoGenerator();
		List<String> longs = new ArrayList<String>();
		Set<String> longsNew = new HashSet<String>();
 		for(int i=0; i<1000000 ; i++){
			String s = uniqueNoGenerator.generateUniqueNumber();
			System.out.println(s);
			longs.add(s);
			longsNew.add(s);
		}
 		//uniqueNoGenerator.checkDuplicateNumbers(longs);
 		System.out.println("List size " + longs.size());
 		System.out.println("Set size " + longsNew.size());
	}
	
	public String generateUniqueNumber(){
		Integer randomNo = RandomUtils.nextInt();
		StringBuilder str = new StringBuilder(randomNo.toString());
		System.out.println("No length old " + str.length());
		paddingRemainingNumber(str);
		System.out.println("No length new " + str.length());
		return str.toString();
	}
	
	public void paddingRemainingNumber(StringBuilder str){
		int paddingLength = 11 - str.length();
		for(int i=0; i<paddingLength ; i++){
			int pos = CommonUtility.randInt(1, str.length());
			int no = CommonUtility.randInt(0, 9);
			str.insert(pos, no);
		}
	}
	
	public void checkDuplicateNumbers(List<String> longs){
		List<String> duplicates = new ArrayList<String>();
		System.out.println("Count down begins");
		int count = 0;
		
		for(String l: longs){
			int duplicate = 0;
			System.out.println(count);
			for(String n:longs){
				
				if(l.equals(n)){
					duplicate++;
				}
			}
			if(duplicate > 1){
				duplicates.add(l);
			}
			count++;
		}
		
		System.out.println("Duplicate numbers.");
		count = 0;
		for(String l : duplicates){
			count++;
			System.out.println(l);
		}
	}
}
