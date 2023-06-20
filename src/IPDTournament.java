import java.util.Random;
import java.util.*;

public class IPDTournament {
	
	private static int popSize = 50;
	private static int noOfGen = 500;
	private static double probCrossover = 0.8;
	private static double probMutation = 0.1;
	private static int N = 4; // chromosome length = 4^memory size
	private static Member[] newPop = new Member[popSize];
	private static Member[] oldPop = new Member[popSize];
	private static int countCC = 0;
	private static int countDD = 0;
	private static int countC = 0;
	private static int bestIndex = -1; // index of the best population member in oldPop
	private static int bestFitness = 0;
	private static double avgFitness = 0.0;
	private static Random rand = new Random();
	
	// 0 - CC, 1 - CD, 2 - DC, 3 - DD
	private static int[] payoff = new int[ 4 ];
	
	public static void playGame( Member member1, Member member2 )
	{
		int play1, play2, outcome1, outcome2;
		
		// play the PD 100 times
		for ( int i = 0; i < 100; i++ )
		{
			// get the current play
			play1 = member1.getStrategy()[ member1.getMemory() ];
			play2 = member2.getStrategy()[ member2.getMemory() ];
			
			// get the current payoff
			outcome1 = 2 * play1 + play2; // 0-CC, 1-CD, 2-DC, 3-DD
			outcome2 = 2 * play2 + play1;
			member1.setFitness( member1.getFitness() + payoff[ outcome1 ]);
			member2.setFitness( member2.getFitness() + payoff[ outcome2 ]);
			
			// update the counts
			if ( outcome1 == 0 )
					countCC++;
			else if ( outcome1 == 3 )
					countDD++;
				else
					countC++;
			
			
			// update the memory
			member1.setMemory( outcome1 + 4 * ( member1.getMemory() % ( member1.getSize() / 4 )));
			member2.setMemory( outcome2 + 4 * ( member2.getMemory() % ( member2.getSize() / 4 )));
		}
	}
	
	// update a population's fitness by playing games between every pair
	public static void updatePopFitness( Member[] pop )
	{
		// set the fitness of all members to 0
		for( int i = 0; i < pop.length; i++ )
			pop[ i ].setFitness( 0 );
		
		// play between every pair 
		for( int i = 0; i < pop.length - 1; i++ )
			for( int j = i + 1; j < pop.length; j++ )
				playGame( pop[ i ], pop[ j ] );
	}
	
	// update the population statistics
	public static void updateStats( Member[] pop )
	{
		bestIndex = 0;
		bestFitness = pop[ 0 ].getFitness();
		avgFitness = 0.0;
		
		for( int i = 0; i < popSize; i++ )
		{
			if ( pop[ i ].getFitness() > bestFitness )
			{
				bestIndex = i;
				bestFitness = pop[ i ].getFitness();
			}
			
			avgFitness += pop[ i ].getFitness();
		}
		avgFitness /= popSize;
		
	}
	
	public static void shufflePopIndex( int[] index ) 
	{
	    int index1, index2, temp;
	    for (int i = 0; i < index.length - 1; i++) 
	    {
	        index1 = i;
	        //index2 = (int) Math.floor(i + 1 + Math.random() * (index.length - i - 1));
	        index2 = i + 1 + rand.nextInt( index.length - i - 1 ); // i+1 to length-1
	        
	        temp = index[ index1 ];
	        index[ index1 ] = index[ index2 ];
	        index[ index2 ] = temp;
	    }

	}
	
	// Tournament selection - Will create newpop from oldpop
	public static void selection()
	{
		int[] index = new int[ popSize ];
		
		for( int i = 0; i < popSize; i++ )
			index[ i ] = i;
		
		shufflePopIndex( index );
		
		// create the first half of newpop
		for( int i = 0;  i < popSize-1; i = i + 2 )
		{
			if( oldPop[ index[ i ] ].getFitness() > oldPop[ index[ i+1 ] ].getFitness() )
				newPop[ i/2 ] = oldPop[ index[ i ] ].clone();
			else
				newPop[ i/2 ] = oldPop[ index[ i+1 ] ].clone();
		}
		
		shufflePopIndex( index );
		
		// create the second half of newpop
		for( int i = 0;  i < popSize-1; i = i + 2 )
		{
			if( oldPop[ index[ i ] ].getFitness() > oldPop[ index[ i+1 ] ].getFitness() )
				newPop[ (i+popSize)/2 ] = oldPop[ index[ i ] ].clone();
			else
				newPop[ (i+popSize)/2 ] = oldPop[ index[ i+1 ] ].clone();
		}
	}
	
	// performs 1-point crossover on two members
	public static void onePointCrossover( int[] sol1, int[] sol2 )
	{
		int point = rand.nextInt( sol1.length - 1 );
		int temp;
		
		for( int i = 0; i <= point; i++ )
		{
			// exchange the genes from 0 to point
			temp = sol2[ i ];
			sol2[ i ] = sol1[ i ];
			sol1[ i ] = temp;
		}
		
	}
	
	public static void uniformCrossover (int[] sol1, int[] sol2) 
	{
		int temp;
		for ( int i = 0; i<sol1.length ; i++)
		{
			if (Math.random() < .5) 
			{
				temp = sol1[i];
				sol1[i] = sol2[i];
				sol2[i] = temp;
			}
		}
	}
	
	public static void mutation( int[] sol ) 
	{
		for ( int i = 0; i < sol.length; i++ )
		{
			if ( Math.random() < probMutation ) 
				sol[i] = 1 - sol[i];
		}
	}
	
	public static void main( String args[] )
	{
		// initialize the payoff matrix
		payoff[ 0 ] = 3;
		payoff[ 1 ] = 0;
		payoff[ 2 ] = 5;
		payoff[ 3 ] = 1;
		
		// initialize the old population
		for( int i = 0; i < popSize; i++ )
			oldPop[ i ] = new Member( N );
		
		// evaluate the oldpop
		updatePopFitness( oldPop );
		updateStats( oldPop );
		
		// print the results - oldpop
		
					
		// repeat generations
		for( int gen = 0; gen < noOfGen; gen++ )
		{
			
			// tournament selection - create newpop from oldpop
			selection();
			
			// crossover on newpop
			for( int i = 0; i < popSize-1; i = i + 2)
			{
				if ( ( newPop[ i ].getFitness() != bestFitness ) &&
					 ( newPop[ i+1 ].getFitness() != bestFitness ) &&
					 ( rand.nextDouble() <= probCrossover ))
					onePointCrossover( newPop[ i ].getStrategy(), newPop[ i+1 ].getStrategy());
					
			}
			
			// mutation on newpop
			for( int i = 0; i < popSize; i++)
				mutation( newPop[ i ].getStrategy() );
			
			// copy newPop to oldPop
			for( int i = 0; i < popSize; i++ )
				oldPop[ i ] = newPop[ i ].clone();
			
			// evaluate the oldpop
			updatePopFitness( oldPop );
			updateStats( oldPop );
			
			// print the results - oldpop
			// print count of CC, DD, C
			System.out.println(countCC);
		}
			
	}

}
