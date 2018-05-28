package at.ltd.adds.superuser.google;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * This class provides helper methods to create a QR code containing the
 * provided credential.  The generated QR code can be fed to the Google
 * Authenticator application so that it can configure itself with the data
 * contained therein.
 */
public final class GoogleAuthenticatorQRGenerator {
    
    /**
     * The format string to generate the Google Chart HTTP API call.
     */
    private static final String TOTP_URI_FORMAT =
            "https://chart.googleapis.com/chart?chs=200x200&chld=M%%7C0&cht=qr&chl=%s";

    /**
     * This method wraps the invocation of <code>URLEncoder##encode</code>
     * method using the "UTF-8" encoding.  This call also wraps the
     * <code>UnsupportedEncodingException</code> thrown by
     * <code>URLEncoder##encode</code> into a <code>RuntimeException</code>.
     * Such an exception should never be thrown.
     *
     * @param s The string to URL-encode.
     * @return the URL-encoded string.
     */
    private static String internalURLEncode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UTF-8 encoding is not supported by URLEncoder.", e);
        }
    }

    /**
     * The label is used to identify which account a key is associated with.
     * It contains an account name, which is a URI-encoded string, optionally
     * prefixed by an issuer string identifying the provider or service managing
     * that account.  This issuer prefix can be used to prevent collisions
     * between different accounts with different providers that might be
     * identified using the same account name, e.g. the user's email address.
     * The issuer prefix and account name should be separated by a literal or
     * url-encoded colon, and optional spaces may precede the account name.
     * Neither issuer nor account name may themselves contain a colon.
     * Represented in ABNF according to RFC 5234:
     * <player/>
     * label = accountname / issuer (â€œ:â€� / â€œ%3Aâ€�) *â€�%20â€� accountname
     *
     * @see <a href="https://code.google.com/player/google-authenticator/wiki/KeyUriFormat">Google Authenticator - KeyUriFormat</a>
     */
    private static String formatLabel(String issuer, String accountName) {
        if (accountName == null || accountName.trim().length() == 0) {
            throw new IllegalArgumentException("Account name must not be empty.");
        }
        
        StringBuilder sb = new StringBuilder();
        if (issuer != null) {
            if (issuer.contains(":")) {
                throw new IllegalArgumentException("Issuer cannot contain the \':\' character.");
            }

            sb.append(issuer);
            sb.append(":");
        }

        sb.append(accountName);

        return sb.toString();
    }




    
}
