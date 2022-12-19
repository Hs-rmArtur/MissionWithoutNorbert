package missionWithoutNorbert;

import de.hsrm.mi.prog.util.StaticScanner;

/*

Erstes Stegreifprojekt: "Mission Without Norbert"
Implementiert von Mykhailo Fakliier, Fouad Ahsayni und Artur Konkel	

*/

public class Main {

	public static void main(String[] args) {

		// Creating variables and constants
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

		// Choosing who will start
		playersTurn = coinFlip();

		// Determining Norbert's position in the queue
		norbertsPosition = determineNorbertsPosition(MAX_MINION_QUEUE);

		// Building the queue of minions
		minionQueue = buildMinionQueue(norbertsPosition, MAX_MINION_QUEUE);

		// Determine minions around Norbert
		minionsLeftOfNorbert = determineMinionsLeftOfNorbert(norbertsPosition);
		minionsRightOfNorbert = determineMinionsRightOfNorbert(norbertsPosition, MAX_MINION_QUEUE);

		// Starting the game by welcoming the player
		welcomingPlayer();
		
		if(playersTurn) {
			System.out.println("Sie beginnen das Spiel! Viel Erfolg!");
		} else {
			System.out.println("Ihr Gegner, der PC, beginnt!");
		}
		
		// The turns will be repeated until the queue of minions is empty
		while (!emptyMinionQueue) {
			turnPossible = false;

			if (playersTurn) {
				// Starting player's turn
				System.out.println("Aktuelle Minionreihe: " + minionQueue);
				System.out.println(
						"Sie sind an der Reihe. Auf welcher Seite möchten Sie ihre Minions wählen? Geben Sie rechts(r) oder links(l) ein.");

				while (!turnPossible) {

					// Checking is input is correct
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
					System.out.println("Wieviele Minions möchten Sie wählen? Wählen Sie zwischen 12312312135423534 Minions.");

					// Checking if input is correct
					inputCorrect = false;
					while (!inputCorrect) {
						playerInputNumMinions = StaticScanner.nextInt();

						if (playerInputNumMinions < 1 || playerInputNumMinions > 3) {
							System.out.println(
									"Es sind nur Zahlen zwischen 1 und 3 erlaubt. Bitte versuchen Sie es erneut.");
						} else {
							numOfChosenMinions = playerInputNumMinions;
							inputCorrect = true;
						}
					}

					turnPossible = checkIfTurnPossible(minionsLeftOfNorbert, minionsRightOfNorbert, numOfChosenMinions,
							selectedLeftSide);

					// Asking player to repeat input if intended turn is not possible
					if (!turnPossible) {
						System.out
								.println("Ihre Auswahlskombination ist leider nicht möglich. Bitte wählen Sie ihre Richtung erneut.");
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

				// Adjusting the queue of minions based on the minions that were chosen
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

				// Finishing turn
				playersTurn = !playersTurn;

				// Determining if PC lost and player won
				if (determineLose(minionsLeftOfNorbert, minionsRightOfNorbert)) {
					emptyMinionQueue = true;
					minionQueue = createEmptyMinionQueue(MAX_MINION_QUEUE);
					System.out.println("Alle Minions wurden gewählt: " + minionQueue);
					System.out.println("- - - - - - - - - - - - - - -");
					System.out.println("Gewonnen! Der PC hat Norbert! Sie haben " + teamPlayer
							+ " Minions im Team. Retten Sie mit Ihnen die Welt!");
				}
			} else {
				// PC's turn
				turnPossible = false;
				while (!turnPossible) {
					numOfChosenMinions = getRandomNumber(1, MAX_SELECTED_MINIONS_ALLOWED);
					selectedLeftSide = coinFlip();
					// Checking if it's possible to select the chosen number of minions on the
					// chosen side
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

				// Adjusting the queue of minions based on the minions that were chosen
				minionQueue = adjustMinionQueue(numChosenLeft, numChosenRight, minionsLeftOfNorbert,
						minionsRightOfNorbert);

				if (selectedLeftSide) {
					System.out.println("PC hat " + numOfChosenMinions + " Minion(s) links ausgewählt. ");
				} else {
					System.out.println("PC hat " + numOfChosenMinions + " Minion(s) rechts ausgewählt. ");
				}

				// Finishing turn
				playersTurn = !playersTurn;

				// Determining if player lost and PC won
				if (determineLose(minionsLeftOfNorbert, minionsRightOfNorbert)) {
					emptyMinionQueue = true;
					minionQueue = createEmptyMinionQueue(MAX_MINION_QUEUE);
					System.out.println("Alle Minions wurden gewählt: " + minionQueue);
					System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - -");
					System.out.println("Sie haben Norbert und " + teamPlayer
							+ " Minions im Team. Versuchen Sie mit Ihm die Welt zu retten!");

				}

			}

		}

	}

	// Method to create the empty queue (as soon as all minions have been chosen)
	public static String createEmptyMinionQueue(int maxQueue) {
		String queue = "";
		for (int i = 0; i < maxQueue; i++) {
			queue += "_";
		}
		return queue;
	}

	// Method to determine if player or PC lost (used in every turn)
	public static boolean determineLose(int minionsLeftOfNorbert, int minionsRightOfNorbert) {
		if (minionsLeftOfNorbert + minionsRightOfNorbert == 0) {
			return true;
		}
		return false;
	}

	// Method to adjust the queue of minions (used in every turn)
	public static String adjustMinionQueue(int numChosenLeft, int numChosenRight, int minionsLeftOfNorbert,
			int minionsRightOfNorbert) {
		String minionQueue = "";

		for (int i = 0; i < numChosenLeft; i++) {
			minionQueue += "_";
		}

		for (int i = 0; i < minionsLeftOfNorbert; i++) {
			minionQueue += "X";
		}

		// Adding Norbert to the queue
		minionQueue += "O";

		for (int i = 0; i < minionsRightOfNorbert; i++) {
			minionQueue += "X";
		}
		for (int i = 0; i < numChosenRight; i++) {
			minionQueue += "_";

		}

		return minionQueue;

	}

	// Method to add the number of selected minions to player's or PC's team
	public static int addSelectedMinionsToTeam(int team, int numOfChosenMinions) {
		team += numOfChosenMinions;

		return team;
	}

	// Method to determine number of minions left of Norbert
	public static int determineMinionsLeftOfNorbert(int norbertsPosition) {
		int runner = 1;
		int minionsLeftOfNorbert = 0;

		while (runner < norbertsPosition) {
			minionsLeftOfNorbert++;
			runner++;
		}

		return minionsLeftOfNorbert;
	}

	// Method to determine number of minions right of Norbert
	public static int determineMinionsRightOfNorbert(int norbertsPosition, int maxMinionQueue) {
		int minionsLeftOfNorbert;
		int minionsRightOfNorbert;
		minionsLeftOfNorbert = determineMinionsLeftOfNorbert(norbertsPosition);

		minionsRightOfNorbert = maxMinionQueue - minionsLeftOfNorbert - 1;
		// We have to subtract -1, as Norbert is not included

		return minionsRightOfNorbert;

	}

	// Method to determine if the intended selection is possible
	public static boolean checkIfTurnPossible(int minionsLeftOfNorbert, int minionsRightOfNorbert,
			int numOfChosenMinions, boolean selectedLeftSide) {
		boolean turnPossible;

		if (selectedLeftSide) {
			// Checking if selection on the left side is possible
			if (minionsLeftOfNorbert < numOfChosenMinions) {
				turnPossible = false;
			} else {
				turnPossible = true;
			}

		} else {
			// Checking if selection on the right side is possible
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

	// Method to randomly determine Norbert's position in the queue
	public static int determineNorbertsPosition(int maxQueueLength) {
		return getRandomNumber(1, maxQueueLength);
	}

	// Method to build the queue of minions for the first time
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

	// Welcoming the player in the beginning of the game
	public static void welcomingPlayer() {
		System.out.println(
				"Willkommen bei -Mission without Norbert-. Norbert ist ein anstrengeder kleiner Minion, der gerne die Pläne anderer durchkreuzt.");
		System.out.println(
				"Ihre Aufgabe ist es aus einer Schar von Minions Ihr Team zusammenzustellen. Doch passen Sie auf, unter Ihnen befindet sich auch ");
		System.out.println(
				"Norbert und Ihn möchten Sie auf keinen Fall im Team haben! Wählen Sie also weise. Sie treten gegen ihren Rivalen PC an.");
		System.out.println("Möge das Spiel beginnen!");
		System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - -");
	}
}
