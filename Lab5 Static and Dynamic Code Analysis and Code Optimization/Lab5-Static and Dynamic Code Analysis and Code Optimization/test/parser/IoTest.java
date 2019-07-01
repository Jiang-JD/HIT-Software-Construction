package parser;

import org.junit.Test;

public class IoTest {

  @Test
  public void test() {
    new ReaderWriterIo().getText("src/txt/TrackGame_Giant.txt");
    new ScannerIo().getText("src/txt/TrackGame_Giant.txt");
    new FileStreamIo().getText("src/txt/TrackGame_Giant.txt");
    new ChannelIo().getText("src/txt/TrackGame_Giant.txt");
    new BufferIo().getText("src/txt/TrackGame_Giant.txt");
  }

}
