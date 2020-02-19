
public class BinomialBowling {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		int N = 10;
		double p1 = .5;
		double p2 = 1 - p1;
		int M = 10;
		
		
		int[] game = playBinomialBowlingGame(M, N, p1, p2);
		int score = 0;
		double avg = 0;
		for(int j = 0; j < 1000000; j++)
		{
			score = 0;
			for(int i = 0; i < M; i++)
			{
				System.out.println(game[i]);
				score = score + game[i];
				
			}
			avg = avg + score;
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
	
	private static int[] playBinomialBowlingGame(int M, int N, double p1, double p2)
	{
		int[] scoreByFrame = new int[M];
		int[][] rolls = new int[M][2];
		double score = 0;
		int lastFrame3 = 0;
		
		for(int i = 0; i < M; i++)
		{	
			int firstRoll = 0;
			for(int j = 0; j < 10; j++)
			{
				//System.out.println((int)(Math.random()*(2)));
				if((int)(Math.random()*(2)) == 1)
				{
					firstRoll++;
				}
				
			}
			//System.out.println(firstRoll);
			int secondRoll = 0;
			for(int j = 0; j < N - firstRoll; j++)
			{
				if((int)(Math.random()*(2)) == 1)
				{
					secondRoll++;
				}
			}
			rolls[i][0] = firstRoll;
			rolls[i][1] = secondRoll;
			if(i == M-1)
			{
				if(firstRoll + secondRoll == N)
				{
					for(int j = 0; j < N - firstRoll; j++)
					{
						if((int)Math.random()*(2) + 1 == 2)
						{
							lastFrame3++;
						}
					}
				}
			}
			scoreByFrame[i] = firstRoll + secondRoll;
		}
		for(int i = 0; i < M; i++)
		{
			for(int j = 0; j < 2; j++)
			{
				System.out.print(rolls[i][j]);
			}
			System.out.println();
		}
		for(int i = 0; i < M; i++)
		{
			if(rolls[i][0] + rolls[i][1] == N)
			{
				if(rolls[i][0] == N)
				{
					if(i+1 < M && rolls[i+1][0] == N)
					{
						scoreByFrame[i] = scoreByFrame[i] + N;
						if(i+2 < M)
						{
							scoreByFrame[i] = scoreByFrame[i] + rolls[i+2][0];
						}
					}
					else if(i+1 < M && rolls[i+1][0] < N)
					{
						scoreByFrame[i] = scoreByFrame[i] + rolls[i+1][0];
					}
				}
				else if(rolls[i][0] + rolls[i][1] == N)
				{
					if(i+1 < M)
					{
						scoreByFrame[i] = scoreByFrame[i] + rolls[i+1][0];
					}
				}
			}
		}
		return scoreByFrame;
	}
	
}
