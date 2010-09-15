package au.com.stocksoftware.wleslie.server;

import au.com.stocksoftware.wleslie.InvalidMoveException;
import au.com.stocksoftware.wleslie.client.Player;
import au.com.stocksoftware.wleslie.server.GameFacet.GameState;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class GameImpl
  implements Game
{
  enum Position
  {
    TOP_LEFT(0, 0), TOP_CENTER(0, 1), TOP_RIGHT(0, 2),
    MIDDLE_LEFT(1, 0), MIDDLE_CENTER(1, 1), MIDDLE_RIGHT(1, 2),
    BOTTOM_LEFT(2, 0), BOTTOM_CENTER(2, 1), BOTTOM_RIGHT(2,2);

    final int _row;
    final int _column;

    private Position( final int row, final int column )
    {
      _row = row;
      _column = column;
    }

    public int getRow()
    {
      return _row;
    }

    public int getColumn()
    {
      return _column;
    }
  }

  private static final String[] PLAYER_SYMBOLS = {"X", "O"};

  private static final int MAX_PLAYERS = PLAYER_SYMBOLS.length;

  private final GameFacet _board[] = new GameFacet[BOARD_SIZE * BOARD_SIZE];
  private final Map<Player, GameFacet> _connectedPlayers = new HashMap<Player, GameFacet>();
  private GameFacet _winner = null;
  private Player _currentPlayer = null;

  /** Request that the player join the game.
   *
   * @return a facet which can be used to interact with the game.
   */
  public synchronized GameFacet join( final Player player )
  {
    if ( _connectedPlayers.size() >= MAX_PLAYERS ||
         _connectedPlayers.containsKey( player ) )
    {
      return null;
    }
    if ( _connectedPlayers.size() == MAX_PLAYERS )
    {
      _currentPlayer = _connectedPlayers.keySet().iterator().next();
      notifyPlayers();
    }

    final String symbol = PLAYER_SYMBOLS[_connectedPlayers.size()];
    final GameFacet facet = new GameFacetImpl( player, this, symbol );
    _connectedPlayers.put( player, facet );
    return facet;
  }

  public List<GameFacet> getBoard()
  {
    return Arrays.asList( _board );
  }

  public synchronized GameState getState( final GameFacet facet )
  {
    if ( _connectedPlayers.size() < MAX_PLAYERS )
    {
      return GameState.WAIT;
    }
    if ( null != _winner )
    {
      return _winner == facet ? GameState.WON : GameState.LOST;
    }
    if ( isGameOver() )
    {
      return GameState.DRAW;
    }
    return _connectedPlayers.get( _currentPlayer ).equals( facet ) ? GameState.READY : GameState.WAIT;
  }

  public Collection<Player> getPlayers()
  {
    return _connectedPlayers.keySet();
  }

  public synchronized void makeMove( final GameFacet facet,
                                     final int column,
                                     final int row )
    throws InvalidMoveException
  {
    if ( null != _winner )
    {
      throw new InvalidMoveException();
    }

    final int position = column * BOARD_SIZE + row;
    if ( position > _board.length )
    {
      throw new InvalidMoveException();
    }
    if ( null != _board[position] )
    {
      throw new InvalidMoveException();
    }
    _board[position] = facet;
    if ( isGameWon() )
    {
      _winner = facet;
    }
  }

  public GameFacet getWinner()
  {
    return _winner;
  }

  public boolean isGameWon()
  {

      for ( int i = 0; i < BOARD_SIZE; i++ )
      {
        if ( isLineWinner( get( i, 0 ), get( i, 1 ), get( i, 2 ) ) ||
             isLineWinner( get( 0, i ), get( 1, i ), get( 2, i ) ) )
        {
          return true;
        }
      }
      return isLineWinner( get( Position.TOP_LEFT ),
                       get( Position.MIDDLE_CENTER ),
                       get( Position.BOTTOM_RIGHT ) ) ||
             isLineWinner( get( Position.TOP_RIGHT ),
                       get( Position.MIDDLE_CENTER ),
                       get( Position.BOTTOM_LEFT ) );
  }

  public boolean isGameOver()
  {
    for ( GameFacet square : _board )
    {
      if ( null == square )
      {
        return false;
      }
    }
    return true;
  }

  private GameFacet get( final int column, final int row )
  {
    return _board[ column * BOARD_SIZE + row ];
  }

  private GameFacet get( final Position position )
  {
    return get( position.getColumn(), position.getRow() );
  }

  private boolean isLineWinner( final GameFacet x,
                                final GameFacet y,
                                final GameFacet z )
  {
    // every box on this line has been filled, with the same value.
    return ( null != x && null != y && null != z && x == y && x == z );
  }

  private void notifyPlayers()
  {
    for ( final Player player : _connectedPlayers.keySet() )
    {
      player.onStateChange();
    }
  }
}
