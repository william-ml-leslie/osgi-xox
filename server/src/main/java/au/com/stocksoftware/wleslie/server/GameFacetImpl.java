package au.com.stocksoftware.wleslie.server;

import au.com.stocksoftware.wleslie.InvalidMoveException;
import au.com.stocksoftware.wleslie.client.Player;
import java.util.List;

public class GameFacetImpl
  implements GameFacet
{
  final private Player _player;
  final private Game _game;
  final private String _name;

  public GameFacetImpl( final Player player, final Game game, final String name )
  {
    _player = player;
    _game = game;
    _name = name;
  }

  public void makeMove( final int column, final int row )
    throws InvalidMoveException
  {
    if ( GameState.READY != getState() )
    {
      throw new InvalidMoveException();
    }
    _game.makeMove( this, column, row );
  }

  public GameState getState()
  {
    return _game.getState( this );
  }

  public List<GameFacet> getBoard()
  {
    return _game.getBoard();
  }

  public String getName()
  {
    return _name;
  }

  public String getName( final int row, final int column )
  {
    return getBoard().get( row * Game.BOARD_SIZE + column ).getName();
  }
}
