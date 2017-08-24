package com.salesmanager.business.marketing.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.inject.Inject;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import com.salesmanager.business.marketing.repositories.EmailMarketingRepository;
import com.salesmanager.business.marketing.repositories.util.JpaUtils;
import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.business.modules.email.EmailConfig;
import com.salesmanager.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.salesmanager.core.business.services.system.EmailService;
import com.salesmanager.marketing.model.EmailMarketing;

@Service("emailMarketingService")
public class EmailMarketingServiceImpl extends SalesManagerEntityServiceImpl<Long, EmailMarketing>
		implements EmailMarketingService {
	private EmailMarketingRepository emailMarketingRepository;
	@Inject
	private JavaMailSender mailSender;
	@Inject
	private EmailService emailService;
	@PersistenceContext
	private EntityManager em;

	@Inject
	public EmailMarketingServiceImpl(EmailMarketingRepository emailMarketingRepository) {
		super(emailMarketingRepository);
		this.emailMarketingRepository = emailMarketingRepository;
	}

	@Override
	public List<EmailMarketing> findByMerchant(Integer storeId) {
		return emailMarketingRepository.findByMerchant(storeId);
	}

	@Override
	public EmailMarketing getById(Long id, String collectionName) {
		EmailMarketing emailMarketing = this.getById(id);
		JpaUtils.initializeRelation(em, emailMarketing, 2, "products");
		return emailMarketing;
	}

	@Override
	public EmailMarketing getByIdLoaded(Long id) {
		EmailMarketing emailMarketing = this.getById(id);
		JpaUtils.initialize(em, emailMarketing, 2);
		return emailMarketing;
	}

	@Override
	public List<String> getEmailCustomersByCriteria(EmailMarketing emailMarketing) {

		return emailMarketingRepository.getCustomerEmailsByCriteria(emailMarketing);
	}

	@Override
	public void sendMarketingEmail(EmailMarketing email) throws Exception {

		List<String> listEmails = this.getEmailCustomersByCriteria(email);

		if (listEmails.isEmpty()) {
			throw new Exception(new Throwable("No emails found in criteria"));
		}
		else {
			EmailConfig emailConfig = emailService.getEmailConfiguration(email.getMerchant());

			final String eml = email.getMerchant().getStorename();
			final String from = email.getMerchant().getStoreEmailAddress();
			// final List<String> to = listEmails;
			final List<String> to = new ArrayList<String>();
			to.add("jmarcosjm@gmail.com");
			to.add("jmarcosjm@gmail.com");
			final String subject = email.getTitle();
			final String cont = email.getContent();
			MimeMessagePreparator preparator = new MimeMessagePreparator() {
				public void prepare(MimeMessage mimeMessage) throws MessagingException, IOException {

					JavaMailSenderImpl impl = (JavaMailSenderImpl) mailSender;
					// if email configuration is present in Database, use the
					// same
					if (emailConfig != null) {
						impl.setProtocol(emailConfig.getProtocol());
						impl.setHost(emailConfig.getHost());
						impl.setPort(Integer.parseInt(emailConfig.getPort()));
						impl.setUsername(emailConfig.getUsername());
						impl.setPassword(emailConfig.getPassword());

						Properties prop = new Properties();
						prop.put("mail.smtp.auth", emailConfig.isSmtpAuth());
						prop.put("mail.smtp.starttls.enable", emailConfig.isStarttls());
						impl.setJavaMailProperties(prop);
					}
					InternetAddress inetAddress[] = new InternetAddress[to.size()];
					int c = 0;
					for (String emailTo : to) {
						inetAddress[c] = new InternetAddress(emailTo);
						c++;
					}
					mimeMessage.setRecipients(Message.RecipientType.TO, inetAddress);

					InternetAddress inetAddressFrom = new InternetAddress();

					inetAddressFrom.setPersonal(eml);
					inetAddressFrom.setAddress(from);

					mimeMessage.setFrom(inetAddressFrom);
					mimeMessage.setSubject(subject);
					Multipart multipart = new MimeMultipart("alternative");

					MimeBodyPart htmlPart = new MimeBodyPart();
					htmlPart.setContent(cont, "text/html; charset=utf-8");
					multipart.addBodyPart(htmlPart);
					mimeMessage.setContent(multipart);

					mimeMessage.saveChanges();

				}
			};

			mailSender.send(preparator);
		}
	}

}
