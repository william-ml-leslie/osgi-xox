package au.com.stocksoftware.wleslie.client;

import au.com.stocksoftware.wleslie.InvalidMoveException;
import au.com.stocksoftware.wleslie.server.Game;
import au.com.stocksoftware.wleslie.server.GameFacet;
import au.com.stocksoftware.wleslie.server.GameFacet.GameState;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class TextPlayerSession
  implements Player
{
  private final GameFacet _facet;
  private final BufferedReader _in;

  public TextPlayerSession( final GameFacet facet )
  {
    _facet = facet;
    _in = new BufferedReader( new InputStreamReader( System.in ) );
  }

  private void requestMove( )
  {
    while (true )
    {
      final List<GameFacet> board = _facet.getBoard();
      for ( int i = 0; i < Game.BOARD_SIZE; i++ )
      {
        System.out.print( " | " + _facet.getName( 0, i ) +
                          " | " + _facet.getName( 1, i ) +
                          " | " + _facet.getName( 2, i ) + " |" );
      }

      try {
        _facet.makeMove( readInt( "type row number" ), readInt( "type column number" ) );
        return;
      } catch ( InvalidMoveException e )
      {
        write( "Invalid move.");
      }
    }
  }

  private void write( final String line )
  {
    System.out.println( line );
  }

  private int readInt( final String request )
  {
    write( request );
    while ( true )
    {
      try {
        String line;
        line = _in.readLine();
        return Integer.parseInt( line );
      } catch ( IOException e ) {
        throw new RuntimeException( e );
      } catch ( NumberFormatException e ) {
        // try again
      }
    }
  }

  public void onStateChange()
  {
    final GameState state = _facet.getState();
    switch ( state )
    {
      case WON:
        write( "You won!" );
        break;
      case DRAW:
        write( "Game Over" );
        break;
      case LOST:
        write( "Try harder next time" );
        break;
      case READY:
        new Thread() {
          public void run()
          {
            requestMove();
          }
        }.start();
    }
  }
}
