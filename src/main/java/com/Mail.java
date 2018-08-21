package com;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 */
public final class Mail {

	public final static String MAILKEY = "mail.address";
	public static final String MAIL_ADDRESS_TO = "mail.addressTo";
	public static final String MAIL_SUBJECT = "mail.subject";
	public static final String MAIL_CONTENT = "mail.content";

	public final static void sendMail(Properties properties, String password) {
		//Establishing a session with required user details
		Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(properties.getProperty(MAILKEY), password);
			}
		});
		try {
			//Creating a Message object to set the email content
			MimeMessage msg = new MimeMessage(session);
			//Storing the comma seperated values to email addresses
			String to = properties.getProperty(MAIL_ADDRESS_TO);
            /*Parsing the String with defualt delimiter as a comma by marking the boolean as true and storing the email
            addresses in an array of InternetAddress objects*/
			InternetAddress[] address = InternetAddress.parse(to, true);
			//Setting the recepients from the address variable
			msg.setRecipients(Message.RecipientType.TO, address);
			String timeStamp = new SimpleDateFormat("yyyymmdd_hh-mm-ss").format(new Date());
			msg.setSubject(properties.getProperty(MAIL_SUBJECT) + timeStamp);
			msg.setSentDate(new Date());
			msg.setText(properties.getProperty(MAIL_CONTENT) );
			msg.setHeader("XPriority", "1");
			Transport.send(msg);
			System.out.println("Mail has been sent successfully");
		} catch (MessagingException mex) {
			System.out.println("Unable to send an email" + mex);
		}
	}
}
