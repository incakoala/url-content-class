import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

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
            // establish HttpURLConnectio
            HttpURLConnection httpURLConnection = (HttpURLConnection) connection;

            // fetch content if required
            if (contentRequired) {
                httpURLConnection.setRequestMethod("GET");
            } else {
                // if content not required
                httpURLConnection.setRequestMethod("HEAD");
            }

            // return HttpUrlConnection is connection's successful
            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                return httpURLConnection;
            } else {
                // throw IOException if connection's unsuccessful
                throw new IOException("Content unavailable.");
            }
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
        } catch (IOException e) {
            return false;
        }
        return true;
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
}
