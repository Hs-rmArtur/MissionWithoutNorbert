package missionWithoutNorbert;

import de.hsrm.mi.prog.util.StaticScanner;

/*
Erstes Stegreifprojekt "MissionWithoutNorbert"
Implementiert von Mykhailo Fakliier, Fouad Ahsayni und Artur Konkel	
	
*/

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		final int MAX_MINION_QUEUE = 11;
		final int MAX_SELECTED_MINIONS_ALLOWED = 3;

		boolean playersTurn;
		int norbertsPosition;
		String minionQueue;
		int numOfChosenMinions = 0;
		boolean selectedLeftSide = true;
		boolean turnPossible = false;
		boolean inputCorrect = false;
		
		
		int minionsLeftOfNorbert;
		int minionsRightOfNorbert;
		
		int playerInputNumMinions;
		char playerInputSide;

		int teamPC = 0;
		int teamPlayer = 0;

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
				System.out.println("Auf welcher Seite möchten Sie ihre Minions wählen? Geben Sie rechts(r) oder links(l) ein.");
				while (!inputCorrect) {
					playerInputSide = StaticScanner.nextChar();
					
					if(playerInputSide != 'r' || playerInputSide != 'l') {
						System.out.println("Ihre Eingabe ist leider ungültig. Versuchen Sie es bitte erneut.");
					} else {
						if(playerInputSide == 'l') {
							selectedLeftSide = true;
						} else {
							selectedLeftSide = false;
						}
					}
				}
				
				System.out.println("Wieviele Minions möchten Sie wählen? Wählen Sie zwischen 1 und 3 Minions.");
				
				//Check if input are correct	
				inputCorrect = false;
				while (!inputCorrect) {
					playerInputNumMinions = StaticScanner.nextInt();
					
					if(playerInputNumMinions < 1 || playerInputNumMinions > 3) {
						System.out.println("Ihre Eingabe ist nicht korrekt. Bitte versuchen Sie es erneut");
					} else {
						numOfChosenMinions = playerInputNumMinions;
						inputCorrect = true;
					}
				}
				
				
				
			} else {
				// PC turn

				while (!turnPossible) {
					numOfChosenMinions = getRandomNumber(1, MAX_SELECTED_MINIONS_ALLOWED);
					selectedLeftSide = coinFlip();
					// Check if it's possible to select the chosen number of Minions on the chosen
					// side
					turnPossible = checkIfTurnPossible(minionsLeftOfNorbert, minionsRightOfNorbert, numOfChosenMinions,
							selectedLeftSide);
				}
				
				if (selectedLeftSide) {
					minionsLeftOfNorbert -= numOfChosenMinions;
				} else {
					minionsRightOfNorbert -= numOfChosenMinions;
				}

				teamPC = addSelectedMinionsToTeam(teamPC, numOfChosenMinions);
				
				minionQueue = adjustMinionQueue(selectedLeftSide, numOfChosenMinions, minionsLeftOfNorbert,
						minionsRightOfNorbert);
				
				
				System.out.println("PC has selected " + numOfChosenMinions + " Minions. ");
				System.out.println(minionQueue);
				
				//Finished with Turn
				playersTurn = !playersTurn;
				

			}

			minionQueue = "__________";
		}

	}

	public static String adjustMinionQueue(boolean selectedLeftSide, int numOfChosenMinions, int minionsLeftOfNorbert,
			int minionsRightOfNorbert) {
		String minionQueue = "";

		if (selectedLeftSide) {
			for (int i = 0; i < numOfChosenMinions; i++) {
				minionQueue += "_";
			}
		}
		for (int i = 0; i < minionsLeftOfNorbert; i++) {
			minionQueue += "X";
		}

		// Add Norbert to Queue
		minionQueue += "O";

		for (int i = 0; i < minionsRightOfNorbert; i++) {
			minionQueue += "X";
		}
		if (!selectedLeftSide)
			for (int i = 0; i < numOfChosenMinions; i++) {
				minionQueue += "_";

			}

		return minionQueue;

	}

	public static int addSelectedMinionsToTeam(int team, int numOfChosenMinions) {
		team += numOfChosenMinions;

		return team;
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
