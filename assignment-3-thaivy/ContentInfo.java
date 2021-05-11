import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import javax.sound.sampled.*;

/**
 * ContentInfo.java
 */

/**
 * A class representing content URL information and relevant methods
 *
 *
 * @author Vy Thai
 * @since 2020-09-18
 */
public class ContentInfo {
    private URL url;

    /**
     * Create an instance with the content URL.
     *
     * @param url the content location
     * @throws NullPointerException if url is null
     */
    public ContentInfo(URL url) {
        if (url == null) throw new NullPointerException("Url is null");
        this.url = url;
    }

    /**
     * Create an instance with a string representing the content URL.
     *
     * @param urlString the content location
     * @throws MalformedURLException if invalid url string
     */
    public ContentInfo(String urlString) throws MalformedURLException {
        this.url = new URL(urlString);
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public URL getUrl() {
        return url;
    }

    /**
     * Establish and return the appropriate URL connection of the URL instance
     *
     * @param contentRequired true if url's full content required, false if not
     * @return URL Connection of the URL instance
     * @throws java.io.IOException if the content is not available
     */
    private URLConnection getConnection(boolean contentRequired) throws IOException {
        // establish URL connection
        URLConnection connection = url.openConnection();

        // if URLConnection is a HttpURLConnection protocol
        if (connection instanceof HttpURLConnection) {
            // establish HttpURLConnection
            HttpURLConnection httpURLConnection = (HttpURLConnection) connection;

            // fetch with "GET" if content is required, otherwise optimize with "HEAD"
            httpURLConnection.setRequestMethod(contentRequired ? "GET" : "HEAD");

            // return HttpUrlConnection is connection's successful
            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                return httpURLConnection;
            } else {
                // throw IOException if connection's unsuccessful
                throw new IOException("Content unavailable.");
            }
        } else {
            connection.connect();
        }
        return connection;
    }

    /**
     * Get the length of this content in bytes.
     *
     * @return length in bytes
     * @throws IOException if content unavailable
     */
    public int getContentLength() throws IOException {
        return getConnection(true).getContentLength();
    }

    public String getContentType() throws IOException {
        return getConnection(false).getContentType();
    }

    /**
     * Gets the dimension for image content.
     *
     * @return the dimensions of the image (width and height)
     * @throws IOException if content unavailable
     * @throws IllegalStateException if not image content
     */
    public Dimension getImageSize() throws IOException {
        if (!isImage()) throw new IllegalStateException("Content is not an image.");

        // read image
        InputStream in = getConnection(true).getInputStream();
        BufferedImage image = ImageIO.read(in);
        in.close();
        return new Dimension(image.getWidth(), image.getHeight());
    }

    /**
     * Gets the date that this content was last modified.
     *
     * @return date last modified
     * @throws IOException if content unavailable
     */
    public Date getLastModified() throws IOException  {
        return new Date(getConnection(false).getLastModified());

    }

    /**
     * Gets the line count for text content.
     *
     * @return line count
     * @throws IOException if content unavailable
     * @throws IllegalStateException if not text content
     */
    public int getLineCount() throws IOException {
        if (!isText()) throw new IllegalStateException("Content is not a text.");

        // read lines
        InputStreamReader in = new InputStreamReader(getConnection(true).getInputStream());
        BufferedReader reader = new BufferedReader(in);
        // count total number of lines
        int count = (int)reader.lines().count();
        in.close();
        return count;
    }

    /**
     * Get the location for this content.
     *
     * @return content location represented as a String
     */
    public String getLocation() {
        return url.toString();
    }

    /**
     * Determines whether the content is available.
     *
     * @return true if the content is available, false otherwise
     */
    public boolean isAvailable() {
        try {
            // test connection
            getConnection(true);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Determines whether the content is an image.
     *
     * @return true if the content is an image, false otherwise
     * @throws IOException if content unavailable
     */
    public boolean isImage() throws IOException {
        return getConnection(false).getContentType().startsWith("image/");
    }

    /**
     * Determines whether the content is text.
     *
     * @return true if the content is a text, false otherwise
     * @throws IOException if content unavailable
     */
    public boolean isText() throws IOException {
        return getConnection(false).getContentType().startsWith("text/");
    }

    /**
     * Determines whether the content is audio.
     *
     * @return true if the content is an audio, false otherwise
     * @throws IOException if content unavailable
     */
    public boolean isAudio() throws IOException {
        return getConnection(false).getContentType().startsWith("audio/");
    }

    /**
     * Gets the audio duration for audio content.
     *
     * @return duration in seconds
     * @throws IOException if content unavailable or unsupported
     * @throws IllegalStateException if not audio content
     */
    public double getAudioDuration() throws IOException {
        if (!isAudio()) throw new IllegalStateException("Content is not an audio.");

        // Establish connection read file using BufferedInputStream
        InputStream in = getConnection(true).getInputStream();
        BufferedInputStream file = new BufferedInputStream(in);
        try {
            // Extract audioInputStream and format to get the audio's frameLength and frameRate
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
            AudioFormat format = audioInputStream.getFormat();
            double frameLength = audioInputStream.getFrameLength();
            double frameRate = format.getFrameRate();

            // duration in seconds
            return frameLength / frameRate;

        } catch (UnsupportedAudioFileException e) {
            throw new IOException("UnsupportedAudioFileException");
        }
    }
}