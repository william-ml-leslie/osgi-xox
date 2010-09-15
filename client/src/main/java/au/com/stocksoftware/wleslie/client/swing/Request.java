package au.com.stocksoftware.wleslie.client.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

public class Request
  extends JDialog
{
  private JPanel contentPane;
  private JButton buttonOK;
  private JButton buttonCancel;

  public Request()
  {
    setContentPane( contentPane );
    setModal( true );
    getRootPane().setDefaultButton( buttonOK );

    buttonOK.addActionListener( new ActionListener()
    {
      public void actionPerformed( ActionEvent e )
      {
        onOK();
      }
    } );
  }

  private void onOK()
  {
    // add your code here
    dispose();
  }

  public static void main( String[] args )
  {
    Request dialog = new Request();
    dialog.pack();
    dialog.setVisible( true );
    System.exit( 0 );
  }
}
