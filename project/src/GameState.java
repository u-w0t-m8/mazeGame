
/**
 * Enum describing the state of the whole game, used by Engine to keep track of
 * things in a compact way. What gets rendered/updated depends on the value of
 * Engine's GameState value.
 *
 */
public enum GameState {
	MAIN_MENU,
	// LOADING, // may be needed later
	IN_GAME,
	// IN_GAME_PAUSED // will definitely be needed later
}
