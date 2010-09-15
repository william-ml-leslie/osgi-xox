package au.com.stocksoftware.wleslie.server;

import au.com.stocksoftware.wleslie.InvalidMoveException;
import au.com.stocksoftware.wleslie.client.Player;
import au.com.stocksoftware.wleslie.server.GameFacet.GameState;
import java.util.Collection;
import java.util.List;

/** The interface used to locate and join games
 *
 */
public interface Game
{
  public static final int BOARD_SIZE = 3;
  Collection<Player> getPlayers();

  void makeMove( GameFacet facet, int column, int row )
    throws InvalidMoveException;

  GameFacet join( Player player );

  List<GameFacet> getBoard();
  GameState getState( GameFacet facet );
}
