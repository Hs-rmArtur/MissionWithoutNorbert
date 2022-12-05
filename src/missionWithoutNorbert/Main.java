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
		boolean emptyMinionQueue = false;
		int numOfChosenMinions = 0;
		boolean selectedLeftSide = true;
		boolean turnPossible = false;
		boolean inputCorrect = false;

		int minionsLeftOfNorbert;
		int minionsRightOfNorbert;
		int numChosenLeft = 0;
		int numChosenRight = 0;

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
		while (!emptyMinionQueue) {
			turnPossible = false;

			if (playersTurn) {
				// Players turn
				System.out.println("Aktuelle Minionreihe: " + minionQueue);
				System.out.println(
						"Sie sind an der Reihe. Auf welcher Seite möchten Sie ihre Minions wählen? Geben Sie rechts(r) oder links(l) ein.");

				while (!turnPossible) {
					inputCorrect = false;

					while (!inputCorrect) {
						playerInputSide = StaticScanner.nextChar();

						if (playerInputSide != 'r' && playerInputSide != 'l') {
							System.out.println("Falsche Richtung. Versuchen Sie es bitte erneut.");
						} else {
							if (playerInputSide == 'l') {
								selectedLeftSide = true;
							} else {
								selectedLeftSide = false;
							}
							inputCorrect = true;
						}

					}

					System.out.println("Wieviele Minions möchten Sie wählen? Wählen Sie zwischen 1 und 3 Minions.");

					// Check if input is correct
					inputCorrect = false;
					while (!inputCorrect) {
						playerInputNumMinions = StaticScanner.nextInt();

						if (playerInputNumMinions < 1 || playerInputNumMinions > 3) {
							System.out.println("Es sind nur Zahlen zwischen 1 und 3 erlaubt. Bitte versuchen Sie es erneut");
						} else {
							numOfChosenMinions = playerInputNumMinions;
							inputCorrect = true;
						}
					}
					turnPossible = checkIfTurnPossible(minionsLeftOfNorbert, minionsRightOfNorbert, numOfChosenMinions,
							selectedLeftSide);

					if (!turnPossible) {
						System.out
								.println("Ihre Auswahlskombination ist leider nicht möglich. Bitte wählen Sie erneut.");
						System.out.println("Aktuelle Minionreihe: " + minionQueue);
					}

				}

				if (selectedLeftSide) {
					minionsLeftOfNorbert -= numOfChosenMinions;
					numChosenLeft += numOfChosenMinions;
				} else {
					minionsRightOfNorbert -= numOfChosenMinions;
					numChosenRight += numOfChosenMinions;
				}

				teamPlayer = addSelectedMinionsToTeam(teamPlayer, numOfChosenMinions);

				minionQueue = adjustMinionQueue(numChosenLeft, numChosenRight, minionsLeftOfNorbert,
						minionsRightOfNorbert);

				if (selectedLeftSide) {
					if (numOfChosenMinions != 1) {
						System.out.println("Spieler hat " + numOfChosenMinions + " Minion(s) links ausgewählt. ");
					}

				} else {
					System.out.println("Spieler hat " + numOfChosenMinions + " Minion(s) rechts ausgewählt. ");
				}

				System.out.println("Aktuelle Minionreihe: " + minionQueue);

				playersTurn = !playersTurn;

				if (determineLose(minionsLeftOfNorbert, minionsRightOfNorbert)) {
					emptyMinionQueue = true;
					minionQueue = createEmptyMinionQueue(MAX_MINION_QUEUE);
					System.out.println("Alle Minions wurden gewählt: " + minionQueue);
					System.out.println("- - - - - - - - - - - - - - -");
					System.out.println("Gewonnen! Der PC hat Norbert! Sie haben " + teamPlayer
							+ " Minions im Team. Retten Sie mit Ihnen die Welt!");
				}
			} else {
				// PC turn
				turnPossible = false;
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
					numChosenLeft += numOfChosenMinions;
				} else {
					minionsRightOfNorbert -= numOfChosenMinions;
					numChosenRight += numOfChosenMinions;
				}
				teamPC = addSelectedMinionsToTeam(teamPC, numOfChosenMinions);

				minionQueue = adjustMinionQueue(numChosenLeft, numChosenRight, minionsLeftOfNorbert,
						minionsRightOfNorbert);

				if (selectedLeftSide) {
					System.out.println("PC hat " + numOfChosenMinions + " Minion(s) links ausgewählt. ");
				} else {
					System.out.println("PC hat " + numOfChosenMinions + " Minion(s) rechts ausgewählt. ");
				}

				// Finished with Turn
				playersTurn = !playersTurn;

				if (determineLose(minionsLeftOfNorbert, minionsRightOfNorbert)) {
					emptyMinionQueue = true;
					minionQueue = createEmptyMinionQueue(MAX_MINION_QUEUE);
					System.out.println("Alle Minions wurden gewählt: " + minionQueue);
					System.out.println("- - - - - - - - - - - - - - -");
					System.out.println("Sie haben Norbert und " + teamPlayer
							+ " Minions im Team. Versuchen Sie mit Ihm die Welt zu retten!");

				}

			}

		}

	}

	public static String createEmptyMinionQueue(int maxQueue) {
		String queue = "";
		for (int i = 0; i < maxQueue; i++) {
			queue += "_";
		}
		return queue;
	}

	public static boolean determineLose(int minionsLeftOfNorbert, int minionsRightOfNorbert) {
		if (minionsLeftOfNorbert + minionsRightOfNorbert == 0) {
			return true;
		}
		return false;
	}

	public static String adjustMinionQueue(int numChosenLeft, int numChosenRight, int minionsLeftOfNorbert,
			int minionsRightOfNorbert) {
		String minionQueue = "";

		for (int i = 0; i < numChosenLeft; i++) {
			minionQueue += "_";
		}

		for (int i = 0; i < minionsLeftOfNorbert; i++) {
			minionQueue += "X";
		}

		// Add Norbert to Queue
		minionQueue += "O";

		for (int i = 0; i < minionsRightOfNorbert; i++) {
			minionQueue += "X";
		}
		for (int i = 0; i < numChosenRight; i++) {
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
