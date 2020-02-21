
public class BinomialBowling {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		
		int N = 10;
		double p1 = .5;
		double p2 = 1 - p1;
		int M = 10;
	
		
		int[] game = play110RandomBowlingGame(M, N, p1, p2);
		int score = 0;
		double avg = 0;
		for(int j = 1; j < 100000; j++)
		{
			score = 0;
			game = play110RandomBowlingGame(M, N, p1, p2);
			for(int i = 0; i < M; i++)
			{
				//System.out.println(game[i]);
				score = score + game[i];
				
			}
			avg = avg + score;
			System.out.println(score);
			System.out.println(avg/j);
		}
		
		
		
	}
	
	private static double getAverageScoreOverMFrames(int M, int N, double p1, double p2)
	{
		double toReturn;
		toReturn = M * getAverageScoreForAFrame(N, p1, p2);
		return toReturn;
	}
	private static double getAverageScoreForAFrame(int N, double p1, double p2)
	{
		double toReturn = 0;
		toReturn = getAverageSumOfTwoRolls(N, p1, p2) + getAverageAdditionPerFrameSpares(N, p1, p2) + getAverageAdditionPerFrameOneStrike(N, p1, p2) + getAverageAdditionPerFrameTwoStrikes(N, p1, p2); 
		return toReturn;
	}
	
	private static double getAverageAdditionPerFrameSpares(int N, double p1, double p2)
	{
		double toReturn;
		toReturn = N * p1 * (Math.pow((p1 + p2 - p1 * p2), (double)N) - Math.pow(p1,  (double) N));
		return toReturn;
	}
	
	private static double getAverageSumOfTwoRolls(int N, double p1, double p2)
	{
		double toReturn;
		toReturn = N * (p1 + p2 - p1 * p2)	;
		return toReturn;
	}
	
	private static double getAverageAdditionPerFrameOneStrike(int N, double p1, double p2)
	{
		double toReturn;
		toReturn = Math.pow(p1, (double)N) * (N * (p1 + p2 - p1*p2) - N * Math.pow(p1, (double)N));
		return toReturn;
	}
	
	private static double getAverageAdditionPerFrameTwoStrikes(int N, double p1, double p2)
	{
		double toReturn;
		toReturn = N * (1 + p1) * Math.pow(p1, (double)N);
		return toReturn;
	}
	
	private static int[] play5050RandomBowlingGame(int M, int N, double p1, double p2) throws InterruptedException
	{
		int[] scoreByFrame = new int[M];
		int[][] rolls = new int[M][2];
		double score = 0;
		int lastFrame3 = 0;
		//Getting Score for Each Frame except last frame
		for(int i = 0; i < M; i++)
		{	
			if(i < M-1)
			{
				int[] frameBalls = roll25050BallFrame(N);
				//Setting score of first and second rolls in the array
				rolls[i][0] = frameBalls[0];
				rolls[i][1] = frameBalls[1];
				//Adds together two balls and puts into array
				scoreByFrame[i] = rolls[i][0] + rolls[i][1];
			}
			//Last frame has a potential 3 balls
			else if(i == M-1)
			{
				int firstRoll;
				int secondRoll = 0;
				int thirdRoll = 0;
				//Rolls first ball
				firstRoll = roll15050Ball(N);
				//Checks if first ball was strike
				if(firstRoll == N)
				{
					secondRoll = roll15050Ball(N);
					//if second ball is also strike
					if(secondRoll == N)
					{
						thirdRoll = roll15050Ball(N);
					}
					//if second ball is not a strike
					else if(secondRoll < N)
					{
						thirdRoll = roll15050Ball(N-secondRoll);
					}
				}
				//Checks if first ball was not a strike
				else if(firstRoll < N)
				{
					secondRoll = roll15050Ball(N-firstRoll);
					//if second ball was a spare
					if(secondRoll + firstRoll == N)
					{
						thirdRoll = roll15050Ball(N);
					}
				}
				rolls[i][0] = firstRoll;
				rolls[i][1] = secondRoll;
				lastFrame3 = thirdRoll;
				scoreByFrame[i] = 0;
				
				
			}
		}
	
		//Checking and accounting for spares and strikes
		for(int i = 0; i < M; i++)
		{
			//if all pins are knocked down and it is not the last frame
			if(rolls[i][0] + rolls[i][1] == N && i < M-1)
			{
				//if strike
				if(rolls[i][0] == N)
				{
					//if next ball is a strike
					if(i+1 < M && rolls[i+1][0] == N)
					{
						scoreByFrame[i] = scoreByFrame[i] + N;
						//if next frame is there, adds next ball
						if(i+2 < M)
						{
							scoreByFrame[i] = scoreByFrame[i] + rolls[i+2][0];
						}
					}
					//if next ball is not a strike, adds next 2 balls
					else if(i+1 < M && rolls[i+1][0] < N)
					{
						scoreByFrame[i] = scoreByFrame[i] + rolls[i+1][0] + rolls[i+1][1];
					}
				}
				//if spare
				else if(rolls[i][0] + rolls[i][1] == N)
				{
					//if next frame is there, adds next ball
					if(i+1 < M)
					{
						scoreByFrame[i] = scoreByFrame[i] + rolls[i+1][0];
					}
				}
			}
			//if it is the last frame
			else if(i == M-1)
			{
				//if first ball is a strike
				if(rolls[i][0] == N)
				{
					//adds N and next two rolls
					scoreByFrame[i] = scoreByFrame[i] + N + rolls[i][1] + lastFrame3; 
					//if second ball is a strike
					if(rolls[i][1] == N)
					{
						//adds N and next roll
						scoreByFrame[i] = scoreByFrame[i] + N + lastFrame3;
					}
					//if second ball is not a strike adds second and third ball
					else if(rolls[i][1] < N)
					{
						scoreByFrame[i] = scoreByFrame[i] + rolls[i][1] + lastFrame3;
					}
				}
				//if first ball is not a strike
				else if(rolls[i][0] < N)
				{
					//if spare on second ball
					if(rolls[i][0] + rolls[i][1] == N)
					{
						scoreByFrame[i] = scoreByFrame[i] + 10 + lastFrame3;
					}
					//if not spare on second ball
					else if(rolls[i][0] + rolls[i][1] < N)
					{
						scoreByFrame[i] = scoreByFrame[i] + rolls[i][0] + rolls[i][1];
					}
				}
			}
		}
	/*	if(rolls[M-1][0] == 10 && rolls[M-1][1] == 10)
		{
			for(int i = 0; i < M; i++)
			{
				for(int j = 0; j < 2; j++)
				{
					System.out.print(rolls[i][j]);
				}
				System.out.print("");
				if(i == M-1)
					System.out.print(" " + lastFrame3);
				System.out.println("\t" + scoreByFrame[i]);
			}
			Thread.sleep(3000);
		}
		System.out.println("");*/
		return scoreByFrame;
	}
	
	public static int[] roll25050BallFrame(int N)
	{
		int[] toReturn = new int[2];
		int firstRoll = 0;
		for(int j = 0; j < N; j++)
		{
			if((int)(Math.random()*(2)) == 1)
			{
				firstRoll++;
			}
			
		}
		int secondRoll = 0;
		//Getting score for second ball
		for(int j = 0; j < N - firstRoll; j++)
		{
			if((int)(Math.random()*(2)) == 1)
			{
				secondRoll++;
			}
		}
		toReturn[0] = firstRoll;
		toReturn[1] = secondRoll;
		return toReturn;
		
	}
	public static int roll15050Ball(int N)
	{
		int firstRoll = 0;
		for(int j = 0; j < N; j++)
		{
			if((int)(Math.random()*(2)) == 1)
			{
				firstRoll++;
			}
			
		}
		return firstRoll;
		
	}
	
	private static int[] play110RandomBowlingGame(int M, int N, double p1, double p2) throws InterruptedException
	{
		int[] scoreByFrame = new int[M];
		int[][] rolls = new int[M][2];
		double score = 0;
		int lastFrame3 = 0;
		//Getting Score for Each Frame
		for(int i = 0; i < M; i++)
		{	//if it is not the last frame
			if(i < M-1)
			{
				int[] frameBalls = roll2110BallFrame(N);
				//Setting score of first and second rolls in the array
				rolls[i][0] = frameBalls[0];
				rolls[i][1] = frameBalls[1];
				//Adds together two balls and puts into array
				scoreByFrame[i] = rolls[i][0] + rolls[i][1];
			}
			//Last frame has a potential 3 balls
			else if(i == M-1)
			{
				int firstRoll;
				int secondRoll = 0;
				int thirdRoll = 0;
				//Rolls first ball
				firstRoll = roll1110Ball(N);
				//Checks if first ball was strike
				if(firstRoll == N)
				{
					secondRoll = roll1110Ball(N);
					//if second ball is also strike
					if(secondRoll == N)
					{
						thirdRoll = roll1110Ball(N);
					}
					//if second ball is not a strike
					else if(secondRoll < N)
					{
						thirdRoll = roll1110Ball(N-secondRoll);
					}
				}
				//Checks if first ball was not a strike
				else if(firstRoll < N)
				{
					secondRoll = roll1110Ball(N-firstRoll);
					//if second ball was a spare
					if(secondRoll + firstRoll == N)
					{
						thirdRoll = roll1110Ball(N);
					}
				}
				rolls[i][0] = firstRoll;
				rolls[i][1] = secondRoll;
				lastFrame3 = thirdRoll;
				//will handle score of final frame later
				scoreByFrame[i] = 0;
			}
		}
	
		//Checking and accounting for spares and strikes in the score
		for(int i = 0; i < M; i++)
		{
			//if all pins are knocked down and it is not the last frame
			if(scoreByFrame[i] == N && i < M-1)
			{
				//if strike
				if(rolls[i][0] == N)
				{
					//if next ball is a strike and not the last frame
					if(i+1 < M-1 && rolls[i+1][0] == N)
					{
						scoreByFrame[i] = scoreByFrame[i] + N;
						//if next frame is there, adds next ball
						if(i+2 < M)
						{
							scoreByFrame[i] = scoreByFrame[i] + rolls[i+2][0];
						}
					}
					//if next ball is not a strike, adds next 2 balls
					else if(i+1 < M && rolls[i+1][0] < N)
					{
						scoreByFrame[i] = scoreByFrame[i] + rolls[i+1][0] + rolls[i+1][1];
					}
					//if next ball is the last frame and a strike
					else if(i+1 == M-1)
					{
						if(rolls[i+1][0] == N)
						{
							scoreByFrame[i] = scoreByFrame[i] + rolls[i+1][0] + rolls[i+1][1];
						}
					}
				}
				//if spare
				else
				{
					//if next frame is there, adds next ball
					if(i+1 < M)
					{
						scoreByFrame[i] = scoreByFrame[i] + rolls[i+1][0];
					}
				}
			}
			//if it is the last frame
			else if(i == M-1)
			{
				scoreByFrame[i] = scoreByFrame[i] + rolls[i][0] + rolls[i][1] + lastFrame3;
			}
		}
	//	if(rolls[M-1][0] == 10 && rolls[M-1][1] == 10)
		//{
			for(int i = 0; i < M; i++)
			{
				for(int j = 0; j < 2; j++)
				{
					System.out.print(rolls[i][j]);
				}
				System.out.print("");
				if(i == M-1)
					System.out.print(" " + lastFrame3);
				System.out.println("\t" + scoreByFrame[i]);
			}
			//Thread.sleep(3000);
		//}
		System.out.println("");
		return scoreByFrame;
	}
	
	public static int[] roll2110BallFrame(int N)
	{
		int[] toReturn = new int[2];
		int firstRoll = (int)(Math.random()*(N+1));
		int secondRoll;
		if(firstRoll == N)
			secondRoll = 0;
		else if(firstRoll == N-1)
		{
			if((int)(Math.random()*(2)) == 1)
			{
				secondRoll = 0;;
			}
			else
				secondRoll = 1;
		}
		else
			secondRoll = (int)(Math.random()*(N-firstRoll+1));
		toReturn[0] = firstRoll;
		toReturn[1] = secondRoll;
		return toReturn;
		
	}
	public static int roll1110Ball(int N)
	{
		int firstRoll = (int)(Math.random()*(N+1));
		return firstRoll;
		
	}
	
}
