package informed.images.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.ContentType;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.SearchTerm;

import org.testng.Reporter;

public class MailAPIUtils {

	private final String userName;
	private final String password;

	public MailAPIUtils(String userName, String password) {
		this.userName = userName;
		this.password = password;
	}

	public String getLearnMoreLinkFromEmail(final String emailContains) {
		String learnMore = null;
		Properties props = new Properties();
		try {
			String pathTosmtp = new File(this.getClass().getResource("/smtp.properties").toURI()).toString();
			props.load(new FileInputStream(new File(pathTosmtp)));
			Session session = Session.getDefaultInstance(props, null);

			Reporter.log("<br>Connecting to Gmail...", true);
			Store store = session.getStore("imaps");
			store.connect("smtp.gmail.com", userName, password);

			Reporter.log("<br>Loading inbox...", true);
			Folder inbox = store.getFolder("inbox");
			inbox.open(Folder.READ_ONLY);

			Reporter.log("<br>search criteria::if mail content contains " + emailContains, true);

			Message[] messages = null;

			for (int i = 0; i < WaitTime.TOO_LONG_TO; i++) {
				SearchTerm term = new SearchTerm() {
					@Override
					public boolean match(Message message) {
						 String result = "";
						 try{
						    if (message.isMimeType("text/plain")) {
						        result = message.getContent().toString();
						    } else if (message.isMimeType("multipart/*")) {
						        MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
						        result = getTextFromMimeMultipart(mimeMultipart);
						    }
						 }catch(Exception e) {
							 
						 }
						try {
							if (result.contains(emailContains)) {
								return true;
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						return false;
					}
				};
				Reporter.log("<br>Searching for mail...", true);
				messages = inbox.search(term);
				if (messages.length < 1) {
					Reporter.log("<br>Wating for Mail to arrive::" + (i + 1) + " sec", true);
					Thread.sleep(1000);
				} else {
					Reporter.log("<br>Mail received::" + messages.length, true);
					Reporter.log("<br>Subject::" + messages[0].getSubject(), true);
					break;
				}

			}
			String htmlPart=getContentInActalFormatMimeMultipart((MimeMultipart) messages[0].getContent());
			learnMore=htmlPart.split("a href=\"")[1].split("\">Learn More")[0].split("\"")[0];
			
			Reporter.log("<br>Mail link - learnMore::" + learnMore, true);
			// closing connection
			Reporter.log("<br>Closing connection...", true);
			inbox.close(true);
			store.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return learnMore;
	}
	
	private String getTextFromMimeMultipart(
	        MimeMultipart mimeMultipart) throws IOException, MessagingException {

	    int count = mimeMultipart.getCount();
	    if (count == 0)
	        throw new MessagingException("Multipart with no body parts not supported.");
	    boolean multipartAlt = new ContentType(mimeMultipart.getContentType()).match("multipart/alternative");
	    if (multipartAlt)
	        // alternatives appear in an order of increasing 
	        // faithfulness to the original content. Customize as req'd.
	        return getTextFromBodyPart(mimeMultipart.getBodyPart(count - 1));
	    String result = "";
	    for (int i = 0; i < count; i++) {
	        BodyPart bodyPart = mimeMultipart.getBodyPart(i);
	        result += getTextFromBodyPart(bodyPart);
	    }
	    return result;
	}

	private String getTextFromBodyPart(
	        BodyPart bodyPart) throws IOException, MessagingException {
	    
	    String result = "";
	    if (bodyPart.isMimeType("text/plain")) {
	        result = (String) bodyPart.getContent();
	    } else if (bodyPart.isMimeType("text/html")) {
	        String html = (String) bodyPart.getContent();
	        result = org.jsoup.Jsoup.parse(html).text();
	    } else if (bodyPart.getContent() instanceof MimeMultipart){
	        result = getTextFromMimeMultipart((MimeMultipart)bodyPart.getContent());
	    }
	    return result;
	}

	private String getContentInActalFormatMimeMultipart(
	        MimeMultipart mimeMultipart) throws IOException, MessagingException {

	    int count = mimeMultipart.getCount();
	    if (count == 0)
	        throw new MessagingException("Multipart with no body parts not supported.");
	    boolean multipartAlt = new ContentType(mimeMultipart.getContentType()).match("multipart/alternative");
	    if (multipartAlt)
	        // alternatives appear in an order of increasing 
	        // faithfulness to the original content. Customize as req'd.
	        return getContentFromBodyPart(mimeMultipart.getBodyPart(count - 1));
	    String result = "";
	    for (int i = 0; i < count; i++) {
	        BodyPart bodyPart = mimeMultipart.getBodyPart(i);
	        result += getContentFromBodyPart(bodyPart);
	    }
	    return result;
	}
	
	private String getContentFromBodyPart(
	        BodyPart bodyPart) throws IOException, MessagingException {
	    
	    String result = "";
	    if (bodyPart.isMimeType("text/plain")) {
	        result = (String) bodyPart.getContent();
	    } else if (bodyPart.isMimeType("text/html")) {
	        result = (String) bodyPart.getContent();
	    } else if (bodyPart.getContent() instanceof MimeMultipart){
	        result = getContentInActalFormatMimeMultipart((MimeMultipart)bodyPart.getContent());
	    }
	    return result;
	}
	
	
	public String inboxCleanUp() {
		String clickHere = null;
		Properties props = new Properties();
		try {
			String pathTosmtp = new File(this.getClass().getResource("/smtp.properties").toURI()).toString();
			props.load(new FileInputStream(new File(pathTosmtp)));
			Session session = Session.getDefaultInstance(props, null);

			Reporter.log("<br>Connecting to Gmail...", true);
			Store store = session.getStore("imaps");
			store.connect("smtp.gmail.com", userName, password);

			Reporter.log("<br>Loading inbox...", true);
			Folder inbox = store.getFolder("inbox");
			inbox.open(Folder.READ_WRITE);

			Message[] messages = inbox.getMessages();

			for (int i = 0; i < messages.length; i++) {
				// setting delete flag for mail.
				Reporter.log("<br>Setting delete flag for::" + messages[i].getSubject(), true);
				messages[i].setFlag(Flags.Flag.DELETED, true);
			}

			// closing connection
			Reporter.log("<br>Closing connection...", true);
			inbox.close(true);
			store.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return clickHere;
	}
}
