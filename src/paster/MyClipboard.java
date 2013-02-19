package paster;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

public class MyClipboard implements ClipboardOwner {

	@Override
	public void lostOwnership(Clipboard clipboard, Transferable contents) {

	}

	public String getCb() {
		try {
			Clipboard clipboard = Toolkit.getDefaultToolkit()
					.getSystemClipboard();
			Transferable content = clipboard.getContents(this);
			return (String) content.getTransferData(DataFlavor.stringFlavor);

		} catch (Exception e) {
			return null;
		}
	}
}
