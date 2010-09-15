package au.com.stocksoftware.wleslie.commons;

import au.com.stocksoftware.wleslie.InvalidMoveException;
import java.util.List;

public interface GameFacet
{
  enum GameState
  {
    WAIT, WON, DRAW, LOST, READY
  }

  void makeMove( int column, int row )
    throws InvalidMoveException;

  GameState getState();

  List<GameFacet> getBoard();

  String getName();

  String getName( int row, int column );
}
