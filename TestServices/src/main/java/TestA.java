import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

import org.apache.commons.lang.SystemUtils;
import org.apache.commons.lang.math.RandomUtils;

import com.oomoqu.rest.util.CommonUtility;



class A{
	A(){
		System.out.println("a");
	}
	public float test(){
		System.out.println("A's test");
		return 1f;
	}
}

class B extends A{
	B(){
		System.out.println("b");
	}
	public float test(){
		System.out.println("B's test");
		return 2;
	}
}

public class TestA {
	public static void main(String...args){
		
		Random random = new Random();
		List<Integer> longs = new ArrayList<Integer>();
		
		for(int i=0; i<100000; i++){
			System.out.println(i);
			Integer newLong = RandomUtils.nextInt();
			System.out.println(newLong);
			longs.add(newLong);
		}
		
		List<Integer> duplicates = new ArrayList<Integer>();
		System.out.println("Count down begins");
		int count = 0;
		
		for(Integer l: longs){
			int duplicate = 0;
			System.out.print(count);
			for(Integer n:longs){
				
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
		for(Integer l : duplicates){
			count++;
			System.out.print(l);
		}
		
		
	}
	public List<Integer> get4RandomDigits(){
		List<Integer> randomNumbers = new ArrayList<Integer>();
		for(int i=0; i<4; i++){
			int pos = CommonUtility.randInt(0, 11);
			randomNumbers.add(pos);
		}
		Collections.sort(randomNumbers);
		return randomNumbers;
	}
}
