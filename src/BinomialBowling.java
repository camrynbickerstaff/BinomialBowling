
public class BinomialBowling {

	public static void main(String[] args) throws InterruptedException {
		
		int N = 10;
		double p1 = .9;
		double p2 = .7;
		int M = 10;
		
		
		int[] game = playBinomialBowling2Game(M, N,p1,p2);
		int score = 0;
		double avg = 0;
		for(int j = 1; j < 10000; j++)
		{
			score = 0;
			game = playBinomialBowling2Game(M, N,p1,p2);
			for(int i = 0; i < M; i++)
			{
				//System.out.println(game[i]);
				score = score + game[i];
				
			}
			avg = avg + score;
			System.out.println("Score: " + score + "\n");
			System.out.println(avg/j + "\n");
		}
		
		System.out.println(calculateAverageScorePerGameBinomialBowling2(M,N,p1,p2));
		
		
	}
	
	
	
	//Binomial Bowling
	//Binomial Bowling Math
	private static double getAverageScoreOverMFrames(int M, int N, double p1, double p2)
	{
		double toReturn;
		toReturn = M * getAverageScoreForAFrame(N, p1, p2);
		return toReturn;
	}
	private static double getAverageScoreForAFrame(int N, double p1, double p2)
	{
		double toReturn = 0;
		toReturn = getAverageSumOfTwoRolls(N, p1, p2) + getAverageAdditionPerFrameSpares(N, p1, p2) + getAverageAdditionPerFrameOneStrike(N, p1, p2) + getAverageAdditionPerFrameTwoStrikes(N, p1); 
		return toReturn;
	}
	
	private static double getAverageAdditionPerFrameSpares(int N, double p1, double p2)
	{
		double toReturn;
		toReturn = N * (p1 * (Math.pow((p1 + p2 - (p1 * p2)), (double)N) - Math.pow(p1,  (double) N)));
		return toReturn;
	}
	
	private static double getAverageSumOfTwoRolls(int N, double p1, double p2)
	{
		double toReturn;
		toReturn = N * (p1 + p2 - (p1 * p2))	;
		return toReturn;
	}
	
	private static double getAverageAdditionPerFrameOneStrike(int N, double p1, double p2)
	{
		double toReturn;
		toReturn = Math.pow(p1, (double)N) * (N * (p1 + p2 - (p1*p2)) - (N * Math.pow(p1, (double)N)));
		return toReturn;
	}
	
	private static double getAverageAdditionPerFrameTwoStrikes(int N, double p1)
	{
		double toReturn;
		toReturn = N * (1 + p1) * Math.pow(p1, (double)N);
		return toReturn;
	}
	
	//Simulate Binomial Bowling Game
	private static int[] playBinomialBowling2Game(int M, int N, double p1, double p2) throws InterruptedException
	{
		int[] scoreByFrame = new int[M];
		int[][] rolls = new int[M][2];
		int lastFrame3 = 0;
		int lastFrame4 = 0;
		//Getting Score for Each Frame
		for(int i = 0; i < M; i++)
		{	//if it is not the last frame
			if(i < M-1)
			{
				int[] frameBalls = roll2BinomialBallFrame(N, p1, p2);
				//int[] frameBalls = roll25050BallFrame(N);
				//Setting score of first and second rolls in the array
				rolls[i][0] = frameBalls[0];
				rolls[i][1] = frameBalls[1];
				//Adds together two balls and puts into array
				scoreByFrame[i] = rolls[i][0] + rolls[i][1];
			}
			//Last frame has a potential 4 balls
			else if(i == M-1)
			{
				int firstRoll = 0;
				int secondRoll = 0;
				int thirdRoll = 0;
				int fourthRoll = 0;
				//Rolls first ball
				firstRoll = roll1BinomialBall(N, p1);
				//firstRoll = roll15050Ball(N);
				//Checks if first ball was strike
				if(firstRoll == N)
				{
					secondRoll = roll1BinomialBall(N, p1);
					//if second ball is also strike
					if(secondRoll == N)
					{
						thirdRoll = roll1BinomialBall(N, p1);
						//if third ball is also a strike
						if(thirdRoll == N)
						{
							fourthRoll = roll1BinomialBall(N, p1);
						}
						else if(thirdRoll < N)
						{
							fourthRoll = roll1BinomialBall(N-thirdRoll, p2);
						}
					}
					//if second ball is not a strike
					else if(secondRoll < N)
					{
						thirdRoll = roll1BinomialBall(N-secondRoll, p2);
						if(secondRoll + thirdRoll == N)
						{
							fourthRoll = roll1BinomialBall(N,p1);
						}
					}
				}
				//Checks if first ball was not a strike
				else if(firstRoll < N)
				{
					secondRoll = roll1BinomialBall(N-firstRoll, p2);
					//if second ball was a spare
					if(secondRoll + firstRoll == N)
					{
						thirdRoll = roll1BinomialBall(N, p1);
						if(thirdRoll == N)
						{
							fourthRoll = roll1BinomialBall(N, p1);
						}
						else if(thirdRoll < N)
						{
							fourthRoll = roll1BinomialBall(N-thirdRoll,p2);
						}
					}
				}
				rolls[i][0] = firstRoll;
				rolls[i][1] = secondRoll;
				lastFrame3 = thirdRoll;
				lastFrame4 = fourthRoll;
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
						//if next frame is there, and not a strike, adds next 2 balls
						if(i+2 < M && rolls[i+2][0] < N)
						{
							scoreByFrame[i] = scoreByFrame[i] + rolls[i+2][0] + rolls[i+2][1];
						}
						//if next frame is a strike and that frame is there, adds next 2 balls
						else if(i+3 < M && rolls[i+2][0] == N)
						{
							scoreByFrame[i] = scoreByFrame[i] + N + rolls[i+3][0];
						}
						//if next frame is the tenth frame, and a strike, adds next 2 balls
						else if(i+3 == M && rolls[i+2][0] == N)
						{
							scoreByFrame[i] = scoreByFrame[i] + rolls[i+2][0] + rolls[i+2][1];
						}
						
					}
					//if next ball is not a strike, and the next 2 frames are there, adds next 3 balls
					else if(i+2 < M && rolls[i+1][0] < N)
					{
						scoreByFrame[i] = scoreByFrame[i] + rolls[i+1][0] + rolls[i+1][1] + rolls[i+2][0];
					}
					//if next ball is the last frame and a strike
					else if(i+1 == M-1)
					{
						
						scoreByFrame[i] = scoreByFrame[i] + rolls[i+1][0] + rolls[i+1][1] + lastFrame3;
						
					}
				}
				//if spare
				if(rolls[i][0] < N && rolls[i][0] + rolls[i][1] == N)
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
			}
			//if it is the last frame
			else if(i == M-1)
			{
				scoreByFrame[i] = scoreByFrame[i] + rolls[i][0] + rolls[i][1] + lastFrame3 + lastFrame4;
			}
		}
		System.out.println("Frame\t1st\t2nd\t3rd\t4th\tTotal");
		System.out.println("-----|-------------------------------------");
		for(int i = 0; i < M; i++)
		{
			if(i < 9)
				System.out.print(i+1 + "    |\t");
			if(i >= 9)
				System.out.print(i+1 + "   |\t");
			
			for(int j = 0; j < 2; j++)
			{
				System.out.print(" " + rolls[i][j]);
				if(j == 0)
					System.out.print("\t");
			}
			System.out.print("");
			if(i >= M-1)
			{	System.out.print("\t" + lastFrame3 + "\t" + lastFrame4);
				System.out.println("\t" + scoreByFrame[i]); 
			}
			else
				System.out.println("\t\t\t" + scoreByFrame[i]); 
		}
	
		return scoreByFrame;
	}
	
	//Roll entire Binomial Bowling frame
	public static int[] roll2BinomialBallFrame(int N, double p1, double p2) throws InterruptedException
	{
		int[] toReturn = new int[2];
		p1 = p1*10000000;
		p2 = p2*10000000;
		int firstRoll = 0;
		//Getting score for first ball
		//Loop 1
		for(int j = 0; j < N; j++)
		{
			//if statement
			if((int)(Math.random()*(10000001)) <= p1)
			{
				firstRoll = firstRoll + 1;
			}
			
		}
		//End Loop 1
		int secondRoll = 0;
		//Getting score for second ball
		//Loop 2
		for(int j = 0; j < N - firstRoll; j++)
		{
			//if statement
			if((int)(Math.random()*(10000001)) <= p2)
			{
				secondRoll = secondRoll + 1;
			}
		}
		//End Loop 2
		toReturn[0] = firstRoll;
		toReturn[1] = secondRoll;
		return toReturn;
		
	}
	
	//Roll just one Binomial Bowling ball
	public static int roll1BinomialBall(int N, double p)
	{
		p = p*10000000;
		int firstRoll = 0;
		//getting score for first ball
		for(int j = 0; j < N; j++)
		{
			if((int)(Math.random()*(10000001)) <= p)
			{
				firstRoll++;
			}
			
		}
		return firstRoll;
		
	}
	
	public static double calculateAverageScorePerGameBinomialBowling2(int M, int N, double p1, double p2)
	{
		double result = 0;
		result = M * calculateAverageScorePerFrameBinomialBowling2(N,p1,p2);
		return result;
	}
	public static double calculateAverageScorePerFrameBinomialBowling2(int N, double p1, double p2)
	{
		double result = 0;
		result = getAverageSumOfTwoRolls(N,p1,p2) + (probabilityOfSpare2(N,p1,p2)*probabilityOfStrike2(N,p1,p2))*(N + N*p1) + (probabilityOfSpare2(N,p1,p2)*probabilityOfNotStrike2(N,p1,p2))*(((N*(p1 + p2 - (p1*p2)) - N*(Math.pow(p1, (double)N))))) + (probabilityOfStrike2(N,p1,p2)*probabilityOfStrike2(N,p1,p2)*probabilityOfNotStrike2(N,p1,p2))*(N + ((N*(p1 + p2 - (p1*p2)) - N*(Math.pow(p1, (double)N))))) + (probabilityOfStrike2(N,p1,p2)*probabilityOfNotStrike2(N,p1,p2))*(((N*(p1 + p2 - (p1*p2)) - N*Math.pow(p1, (double)N))) + N*p1) + (probabilityOfStrike2(N,p1,p2)*probabilityOfStrike2(N,p1,p2)*probabilityOfStrike2(N,p1,p2))*(2*N + N*p1);
		//System.out.println("Expectation of frame given not strike: " + ((N*(p1 + p2 - (p1*p2)) - N*(Math.pow(p1, (double)N)))));
		//System.out.println("Expectation of Strike: " + N );
		//System.out.println("Expectation of Single ball: " + N*p1 );
		//System.out.println("Average Score per frame: " + result);
		return result;
	}
	
	public static double probabilityOfStrike2(int N, double p1, double p2)
	{
		double result = 0;
		result = Math.pow(p1, (double)N);
		//System.out.println("Probability of strike: " + result);
		return result;
	}
	
	public static double probabilityOfSpare2(int N, double p1, double p2)
	{
		double result = 0;
		result = Math.pow(((p1 + p2) - (p1*p2)), (double)N) - (Math.pow(p1, (double)N));
		//System.out.println("Probability of spare: " + result);
		return result;
	}
	
	public static double probabilityOfNotStrike2(int N, double p1, double p2)
	{
		double result = 0;
		result = (1 - probabilityOfStrike2(N, p1, p2));
		//System.out.println("Probability of not strike: " + result);
		return result;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//Simulate Binomial Bowling Game
	private static int[] playBinomialBowlingGame(int M, int N, double p1, double p2) throws InterruptedException
	{
		int[] scoreByFrame = new int[M];
		int[][] rolls = new int[M][2];
		int lastFrame3 = 0;
		//Getting Score for Each Frame
		for(int i = 0; i < M; i++)
		{	//if it is not the last frame
			if(i < M-1)
			{
				int[] frameBalls = roll2BinomialBallFrame(N, p1, p2);
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
				firstRoll = roll1BinomialBall(N, p1);
				//Checks if first ball was strike
				if(firstRoll == N)
				{
					secondRoll = roll1BinomialBall(N, p1);
					//if second ball is also strike
					if(secondRoll == N)
					{
						thirdRoll = roll1BinomialBall(N, p1);
					}
					//if second ball is not a strike
					else if(secondRoll < N)
					{
						thirdRoll = roll1BinomialBall(N-secondRoll, p2);
					}
				}
				//Checks if first ball was not a strike
				else if(firstRoll < N)
				{
					secondRoll = roll1BinomialBall(N-firstRoll, p2);
					//if second ball was a spare
					if(secondRoll + firstRoll == N)
					{
						thirdRoll = roll1BinomialBall(N, p1);
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
	
		return scoreByFrame;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//Binomial Bowling but p1 = .5. Another Way of calculating each frame
	
	//Each pin has 50/50 chance of falling over
	private static int[] play5050BinomialBowlingGame(int M, int N, double p1) throws InterruptedException
	{
		int[] scoreByFrame = new int[M];
		int[][] rolls = new int[M][2];
		int lastFrame3 = 0;
		//Getting Score for Each Frame
		for(int i = 0; i < M; i++)
		{	//if it is not the last frame
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
		/*	for(int i = 0; i < M; i++)
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
			*/
		System.out.println("");
		return scoreByFrame;
	}
	
	//Calculates each frame by going through every pin and doing 5050
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
	
	//Calculates one ball using 5050 chance for each pin
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
	
	
	
	
	
	
	
	
	//Number of pins knocked down is chosen randomly from 1-n (n number of pins)
	private static int[] play110RandomBowlingGame(int M, int N) throws InterruptedException
	{
		int[] scoreByFrame = new int[M];
		int[][] rolls = new int[M][2];
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
						//if next frame is there, and not a strike, adds next 2 balls
						if(i+2 < M && rolls[i+2][0] < N)
						{
							scoreByFrame[i] = scoreByFrame[i] + rolls[i+2][0] + rolls[i+2][1];
						}
						//if next frame is a strike and that frame is there, adds next 2 balls
						else if(i+3 < M && rolls[i+2][0] == N)
						{
							scoreByFrame[i] = scoreByFrame[i] + N + rolls[i+3][0];
						}
					}
					//if next ball is not a strike, and the next 2 frames are there, adds next 3 balls
					else if(i+2 < M && rolls[i+1][0] < N)
					{
						scoreByFrame[i] = scoreByFrame[i] + rolls[i+1][0] + rolls[i+1][1] + rolls[i+2][0];
					}
					//if next ball is the last frame and a strike
					else if(i+1 == M-1)
					{
						if(rolls[i+1][0] == N)
						{
							scoreByFrame[i] = scoreByFrame[i] + rolls[i+1][0] + rolls[i+1][1] + lastFrame3;
						}
					}
				}
				//if spare
				if(rolls[i][0] < N && rolls[i][0] + rolls[i][1] == N)
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
			}
			//if it is the last frame
			else if(i == M-1)
			{
				scoreByFrame[i] = scoreByFrame[i] + rolls[i][0] + rolls[i][1] + lastFrame3;
			}
		}
		
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
