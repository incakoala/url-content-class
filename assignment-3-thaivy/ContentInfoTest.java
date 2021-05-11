import static org.junit.Assert.*;
import org.junit.Test;
import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * ContentInfoTest.java
 */

/**
 * JUnit tests for ContentInfo class
 *
 *
 * @author Vy Thai
 * @since 2020-09-18
 */
public class ContentInfoTest {
    ContentInfo pdf;
    ContentInfo txt;
    ContentInfo png;
    ContentInfo wav;
    String pdfStr = "https://www.ccis.northeastern.edu/home/pgust/classes/cs5500/2020/Fall/resources/assignment-1/12lines.pdf";
    String txtStr = "https://www.ccis.northeastern.edu/home/pgust/classes/cs5500/2020/Fall/resources/assignment-1/12lines.txt";
    String pngStr = "https://www.ccis.northeastern.edu/home/pgust/classes/cs5500/2020/Fall/resources/assignment-1/500x200.png";
    String wavStr = "https://www.ccis.northeastern.edu/home/pgust/classes/cs5500/2020/Fall/resources/assignment-3/3.258start.wav";

    public ContentInfoTest() throws MalformedURLException {
        pdf = new ContentInfo(pdfStr);
        txt = new ContentInfo(txtStr);
        png = new ContentInfo(pngStr);
        wav = new ContentInfo(wavStr);
    }

    /**
     * Test method for {@link ContentInfo#ContentInfo(java.net.URL)}
     */
    @Test
    public void testContentInfo() {
        try {
            // Non-null URL -- HTTP
            new ContentInfo(new URL("https://www.khoury.northeastern.edu/"));
        } catch (NullPointerException | MalformedURLException e){
            fail("Unexpected NullPointerException");
        }

        try {
            // Non-null URL -- FTP
            new ContentInfo(new URL("ftp://ftp.funet.fi/pub/standards/RFC/rfc959.txt"));
        } catch (NullPointerException | MalformedURLException e){
            fail("Unexpected NullPointerException");
        }

        try {
            // Null URL
            new ContentInfo(new URL(null));
            fail("expected NullPointerException");
        } catch (NullPointerException | MalformedURLException e) {
            // NullPointerException caught
        }
    }

    /**
     * Test method for {@link ContentInfo#ContentInfo(java.lang.String)}
     */
    @Test
    public void testContentInfoString() {
        try {
            // Valid URL string
            new ContentInfo("https://google.com");
        } catch (MalformedURLException e){
            fail("Unexpected MalformedURLException");
        }

        try {
            // Valid URL string -- FTP
            new ContentInfo(new URL("ftp://ftp.funet.fi/pub/standards/RFC/rfc959.txt"));
        } catch (MalformedURLException e){
            fail("Unexpected MalformedURLException");
        }

        try {
            // Invalid URL string
            new ContentInfo("abc");
            fail("Expected MalformedURLException");
        } catch (MalformedURLException e) {
            // MalformedURLException caught
        }

        try {
            // Invalid URL string -- invalid protocol
            new ContentInfo("htp://google.com");
            fail("Expected MalformedURLException");
        } catch (MalformedURLException e) {
            // MalformedURLException caught
        }
    }

    /**
     * Test method for {@link ContentInfo#getContentLength()}
     */
    @Test
    public void testGetContentLength() {
        try {
            // pdf
            assertEquals("getContentLength - 12lines.pdf", 12811, pdf.getContentLength());
            // txt
            assertEquals("getContentLength - 12lines.txt", 649, txt.getContentLength());
            // png
            assertEquals("getContentLength - 500x200.png", 68643, png.getContentLength());
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }

    /**
     * Test method for {@link ContentInfo#getContentType()}
     */
    @Test
    public void testGetContentType() {
        try {
            // pdf
            assertEquals("getContentType - 12lines.pdf", "application/pdf", pdf.getContentType());
            // txt
            assertEquals("getContentType - 12lines.txt", "text/plain", txt.getContentType().substring(0, txt.getContentType().indexOf(';')));
            // png
            assertEquals("getContentType - 500x200.png", "image/png", png.getContentType());
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }

    /**
     * Test method for {@link ContentInfo#getImageSize()}
     */
    @Test
    public void testGetImageSize() {
        try {
            // image
            assertEquals("getImageSize - 500x200.png", new Dimension(500, 200), png.getImageSize());
        } catch (IOException e) {
            fail("Unexpected IOException");
        } catch (IllegalStateException e) {
            fail("Unexpected non-image");
        }

        try {
            // non-image
            txt.getImageSize();
            fail("Expected non-image");
        } catch (IOException e) {
            fail("Unexpected IOException");
        } catch (IllegalStateException e) {
            // IllegalStateException caught
        }
    }

    /**
     * Test method for {@link ContentInfo#getLastModified()}
     */
    @Test
    public void testGetLastModified() {
        try {
            // pdf
            Date date = pdf.getLastModified();
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            int year = calendar.get(Calendar.YEAR);
            assertEquals("getLastModified - 12lines.pdf", 2020, year);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }

        try {
            // txt
            Date date = txt.getLastModified();
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            int year = calendar.get(Calendar.YEAR);
            assertEquals("getLastModified - 12lines.txt", 2020, year);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }

        try {
            // png
            Date date = png.getLastModified();
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            int year = calendar.get(Calendar.YEAR);
            assertEquals("getLastModified - 500x200.png", 2020, year);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }

    /**
     * Test method for {@link ContentInfo#getLineCount()}
     */
    @Test
    public void testGeLineCount() {
        try {
            // txt
            assertEquals("geLineCount - 12lines.txt", 12, txt.getLineCount());
        } catch (IOException e) {
            fail("Unexpected IOException");
        } catch (IllegalStateException e) {
            fail("Unexpected non-text");
        }

        try {
            // txt
            assertEquals("geLineCount - 12lines.txt", 104, new ContentInfo("https://www.w3.org/TR/PNG/iso_8859-1.txt").getLineCount());
        } catch (IOException e) {
            fail("Unexpected IOException");
        } catch (IllegalStateException e) {
            fail("Unexpected non-text");
        }

        try {
            // non-txt
            pdf.getLineCount();
            fail("Expected non-txt");
        } catch (IOException e) {
            fail("Unexpected IOException");
        } catch (IllegalStateException e) {
            // IllegalStateException caught
        }
    }

    /**
     * Test method for {@link ContentInfo#getLocation()}
     */
    @Test
    public void testGetLocation() {
        // pdf
        assertEquals("geLineCount - 12lines.pdf", pdfStr, pdf.getLocation());
        // txt
        assertEquals("geLineCount - 12lines.txt", txtStr, txt.getLocation());
        // png
        assertEquals("geLineCount - 500x200.png", pngStr, png.getLocation());
    }

    /**
     * Test method for {@link ContentInfo#isAvailable()}
     *
     * @throws MalformedURLException if invalid url string
     */
    @Test
    public void testIsAvailable() throws MalformedURLException {
        // pdf
        assertTrue("isAvailable - 12lines.pdf", pdf.isAvailable());
        // txt
        assertTrue("isAvailable - 12lines.txt", txt.isAvailable());
        // png
        assertTrue("isAvailable - 500x200.png", png.isAvailable());
        // unavailable urls
        assertFalse("isAvailable - unknown host", new ContentInfo("http://blahbabbaah.com").isAvailable());
        assertFalse("isAvailable - file not found", new ContentInfo("https://www.tutorialspoint.com/junit/index.html").isAvailable());
    }

    /**
     * Test method for {@link ContentInfo#isImage()}
     */
    @Test
    public void tesIsImage() {
        try {
        // pdf
        assertFalse("isImage - 12lines.pdf", pdf.isImage());
        // txt
        assertFalse("isImage - 12lines.txt", txt.isImage());
        // png
        assertTrue("isImage - 500x200.png", png.isImage());
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }

    /**
     * Test method for {@link ContentInfo#isText()}
     */
    @Test
    public void testIsText() {
        try {
            // pdf
            assertFalse("isText - 12lines.pdf", pdf.isText());
            // txt
            assertTrue("isText - 12lines.txt", txt.isText());
            // png
            assertFalse("isText - 500x200.png", png.isText());
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }

    /**
     * Test method for {@link ContentInfo#isAudio()}
     */
    @Test
    public void tesIsAudio() {
        try {
            // wav
            assertTrue("isImage - 3.258start.wav", wav.isAudio());
            // png
            assertFalse("isImage - 500x200.png", png.isAudio());
            // random mp3
            assertTrue("isImage - bear.mp3", new ContentInfo("http://commondatastorage.googleapis.com/chromiumos-test-assets/bear/bear.mp3").isAudio());
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }

    /**
     * Test method for {@link ContentInfo#getAudioDuration()}
     */
    @Test
    public void testGetAudioDuration() {
        try {
            // audio
            assertEquals("getAudioDuration - 3.258start.wav", 3.258, wav.getAudioDuration(), 0.1);
        } catch (IOException e) {
            fail("Unexpected IOException");
        } catch (IllegalStateException e) {
            fail("Unexpected non-audio");
        }

        try {
            // non-audio
            ContentInfo mp4 = new ContentInfo("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4");
            mp4.getAudioDuration();
            fail("Expected non-audio");
        } catch (IOException e) {
            fail("Unexpected IOException");
        } catch (IllegalStateException e) {
            // IllegalStateException caught
        }

        try {
            // corrupted/unsupported audio
            ContentInfo corruptedAudio = new ContentInfo("https://srv-store3.gofile.io/download/5VcghO/sample.mp3");
            corruptedAudio.getAudioDuration();
            fail("Expected unsupported audio");
        } catch (IllegalStateException e) {
            fail("Unexpected IllegalStateException");
        } catch (IOException e) {
            // IOException caught
        }
    }
}