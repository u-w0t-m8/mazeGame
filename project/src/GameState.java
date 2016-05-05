
/**
 * Enum describing the state of the whole game, used by Engine to keep track of
 * things in a compact way. What gets rendered/updated depends on the value of
 * Engine's GameState value.
 *
 */
public enum GameState {
	// STARTUP, //very unlikely to be needed
	MAIN_MENU,
	// LOADING, //might not be needed
	IN_GAME, IN_GAME_PAUSED
}
