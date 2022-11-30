package missionWithoutNorbert;

import java.util.Scanner;

/*
Erstes Stegreifprojekt "MissionWithoutNorbert"
Implementiert von Mykhailo Fakliier, Fouad und Artur Konkel	
	
*/

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scan = new Scanner(System.in);

		final int MAX_MINION_QUEUE = 10;
		final int MAX_SELECTED_MINIONS_ALLOWED = 3;

		boolean playersTurn;
		int norbertsPosition;
		String minionQueue;
		int numOfChosenMinions = 0;
		boolean selectedLeftSide;
		boolean turnPossible = false;

		int minionsLeftOfNorbert;
		int minionsRightOfNorbert;

		// Choose who will start
		playersTurn = coinFlip();

		// Determine Norberts position in Queue
		norbertsPosition = determineNorbertsPosition(MAX_MINION_QUEUE);

		// Build MinionQueue
		minionQueue = buildMinionQueue(norbertsPosition, MAX_MINION_QUEUE);

		minionsLeftOfNorbert = determineMinionsLeftOfNorbert(norbertsPosition);
		minionsRightOfNorbert = determineMinionsRightOfNorbert(norbertsPosition, MAX_MINION_QUEUE);

		// Start Playing
		while (minionQueue != "__________") {

			if (playersTurn) {
				// Players turn
			} else {
				// PC turn

				while (!turnPossible) {
					numOfChosenMinions = getRandomNumber(1, MAX_SELECTED_MINIONS_ALLOWED);
					selectedLeftSide = coinFlip();

					// Check if it's possible to select the chosen number of Minions on the chosen
					// side
					turnPossible = checkIfTurnPossible(minionsLeftOfNorbert, minionsRightOfNorbert, numOfChosenMinions,
							selectedLeftSide);
					System.out.println(turnPossible);
				}

			}

			minionQueue = "__________";
		}

	}

	public static int determineMinionsLeftOfNorbert(int norbertsPosition) {
		int runner = 1;
		int minionsLeftOfNorbert = 0;

		while (runner < norbertsPosition) {
			minionsLeftOfNorbert++;
			runner++;
		}

		return minionsLeftOfNorbert;
	}

	public static int determineMinionsRightOfNorbert(int norbertsPosition, int maxMinionQueue) {
		int minionsLeftOfNorbert;
		int minionsRightOfNorbert;
		minionsLeftOfNorbert = determineMinionsLeftOfNorbert(norbertsPosition);

		minionsRightOfNorbert = maxMinionQueue - minionsLeftOfNorbert - 1;
		// Es muss -1 abgezogen werden, da Norbert nicht mit einbegriffen wird.

		return minionsRightOfNorbert;

	}

	public static boolean checkIfTurnPossible(int minionsLeftOfNorbert, int minionsRightOfNorbert,
			int numOfChosenMinions, boolean selectedLeftSide) {
		boolean turnPossible;

		if (selectedLeftSide) {
			// Check if selection on left side is possible
			if (minionsLeftOfNorbert < numOfChosenMinions) {
				turnPossible = false;
			} else {
				turnPossible = true;
			}

		} else {
			// Check if selection on right side is possible
			if (minionsRightOfNorbert < numOfChosenMinions) {
				turnPossible = false;
			} else {
				turnPossible = true;
			}
		}

		return turnPossible;
	}

	public static int getRandomNumber(int min, int max) {
		return (int) Math.round(Math.random() * (max - min) + min);
	}

	public static boolean coinFlip() {
		int ranNum = getRandomNumber(0, 1);
		if (ranNum != 0) {
			// Head
			return true;
		} else {
			// Tail
			return false;
		}
	}

	public static int determineNorbertsPosition(int maxQueueLength) {
		return getRandomNumber(1, maxQueueLength);
	}

	public static String buildMinionQueue(int norbertsPosition, int maxQueueLength) {
		String queue = "";

		for (int i = 1; i <= maxQueueLength; i++) {
			if (i != norbertsPosition) {
				queue += "X";
			} else {
				queue += "O";
			}
		}
		return queue;
	}

}
