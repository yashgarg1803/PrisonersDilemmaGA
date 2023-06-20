
import java.util.*;

public class Member 
{
 private int size;
 private int[] strategy = new int[ size ];
 private int memory;
 private int fitness;
 private Random rand = new Random();
 
 public Member( int s )
 {
	 size = s;
	 
	 // create a random strategy
	 // 0 - C, 1 - D
	 for( int i = 0; i < size; i++ )
		 strategy[ i ] = rand.nextInt( 2 );
	 
	 // create a random memory
	 memory = rand.nextInt( size );
	 
	 fitness = 0;
 }
 
 public int getSize()
 {
	 return size;
 }
 
 public int[] getStrategy()
 {
	 return strategy; 
 }
 
 public int getMemory()
 {
	 return memory;
 }
 
 public int getFitness()
 {
	 return fitness;
 }
 
 public void setSize( int s )
 {
	 size = s;
 }
 
 public void setStrategy( int[] str )
 {
	for( int i = 0; i < str.length; i++ )
		strategy[ i ] = str[ i ];
 }
 
 public void setMemory( int m )
 {
	 memory = m;
 
 }
 
 public void setFitness( int f )
 {
	 fitness = f;
 }
 
 public Member clone()
 {
	 Member newMember = new Member( size );
	 
	 newMember.setStrategy ( strategy );
	 newMember.setMemory( memory );
	 newMember.setFitness( fitness );
	 
	 return newMember;
 }
 
}