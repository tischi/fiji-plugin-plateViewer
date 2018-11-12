import de.embl.cba.gridviewer.commands.MultiPositionViewerCommand;
import net.imagej.ImageJ;

public class MultiPositionViewerCommandTest
{

	public static void main(final String... args) throws Exception
	{
		final ImageJ ij = new ImageJ();
		ij.ui().showUI();

		// invoke the plugin
		ij.command().run( MultiPositionViewerCommand.class, true );

		// test data
		// /Volumes/almfscreen/Gbekor/ATAT1/Nikon/PlateATAT1_pilot_4000_cells
		// /Users/tischer/Documents/fiji-plugin-plateViewer/src/test/resources/Eugene
	}

}
