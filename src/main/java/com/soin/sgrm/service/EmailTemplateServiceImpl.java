package com.soin.sgrm.service;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.PreencodedMimeBodyPart;

import org.springframework.mail.javamail.MimeMessageHelper;

import com.soin.sgrm.dao.EmailTemplateDao;
import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.Ambient;
import com.soin.sgrm.model.Dependency;
import com.soin.sgrm.model.EmailTemplate;
import com.soin.sgrm.model.Release;
import com.soin.sgrm.model.ReleaseObject;
import com.soin.sgrm.model.UserInfo;
import com.soin.sgrm.model.wf.Node;
import com.soin.sgrm.model.wf.WFRelease;
import com.soin.sgrm.model.wf.WFUser;
import com.soin.sgrm.utils.Constant;
import com.soin.sgrm.utils.EnviromentConfig;

@Transactional("transactionManager")
@Service("EmailTemplateService")
public class EmailTemplateServiceImpl implements EmailTemplateService {

	private static final Pattern imgRegExp = Pattern.compile("<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>");

	@Autowired
	EmailTemplateDao dao;

	@Autowired
	private Environment env;

	@Autowired
	private JavaMailSender mailSender;

	EnviromentConfig envConfig = new EnviromentConfig();

	@Override
	public List<EmailTemplate> listAll() {
		return dao.listAll();
	}

	@Override
	public EmailTemplate findById(Integer id) {
		return dao.findById(id);
	}

	@Override
	public void sendMail(String to, String cc, String subject, String html) throws Exception {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		mimeMessage.setHeader("Content-Type", "text/plain; charset=UTF-8");
		try {
			Map<String, String> inlineImage = new HashMap<String, String>();
			String body = html;
			body = Constant.getCharacterEmail(body);
			final Matcher matcher = imgRegExp.matcher(body);
			int i = 0;
			while (matcher.find()) {
				String src = matcher.group();
				if (body.indexOf(src) != -1) {
					String srcToken = "src=\"";
					int x = src.indexOf(srcToken);
					int y = src.indexOf("\"", x + srcToken.length());
					String srcText = src.substring(x + srcToken.length(), y);
					String cid = "image" + i;
					String newSrc = src.replace(srcText, "cid:" + cid);
					inlineImage.put(cid, srcText.split(",")[1]);
					body = body.replace(src, newSrc);
					i++;
				}
			}
			mimeMessage.setSubject(subject);
			mimeMessage.setSender(new InternetAddress(envConfig.getEntry("mailUser")));
			mimeMessage.setFrom(new InternetAddress(envConfig.getEntry("mailUser")));
			BodyPart bp = new MimeBodyPart();
			bp.setContent(body, "text/html");
			MimeMultipart mmp = new MimeMultipart();
			mmp.addBodyPart(bp);
			Iterator<Entry<String, String>> it = inlineImage.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, String> pairs = it.next();
				PreencodedMimeBodyPart pmp = new PreencodedMimeBodyPart("base64");
				pmp.setHeader("Content-ID", "<" + pairs.getKey() + ">");
				pmp.setDisposition(MimeBodyPart.INLINE);
				pmp.setContent(pairs.getValue(), "image/png");
				mmp.addBodyPart(pmp);
			}
			mimeMessage.setContent(mmp, "UTF-8");
			for (String toUser : to.split(",")) {
				mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(toUser));
			}
			if (!((cc != null) ? cc : "").trim().equals("")) {
				for (String ccUser : cc.split(",")) {
					mimeMessage.addRecipient(Message.RecipientType.CC, new InternetAddress(ccUser));
				}
			}

			mailSender.send(mimeMessage);
		} catch (Exception e) {
			Sentry.capture(e, "email");
			throw e;
		}
	}

	@Override
	public void updateEmail(EmailTemplate email) {
		dao.updateEmail(email);
	}

	@Override
	public void saveEmail(EmailTemplate email) {
		dao.saveEmail(email);
	}

	@Override
	public void sendMail(Release release, EmailTemplate email) throws Exception {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		mimeMessage.setHeader("Content-Type", "text/plain; charset=UTF-8");
		email = fillEmail(email, release);
		String body = email.getHtml();
		body = Constant.getCharacterEmail(body);
		MimeMultipart mmp = MimeMultipart(body);
		mimeMessage.setContent(mmp);
		mimeMessage.setSubject(email.getSubject());
		mimeMessage.setSender(new InternetAddress(envConfig.getEntry("mailUser")));
		mimeMessage.setFrom(new InternetAddress(envConfig.getEntry("mailUser")));
		for (String toUser : email.getTo().split(",")) {
			mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(toUser));
		}
		mimeMessage.addRecipient(Message.RecipientType.CC, new InternetAddress(release.getUser().getEmail()));
		if (!((email.getCc() != null) ? email.getCc() : "").trim().equals("")) {
			for (String ccUser : email.getCc().split(",")) {
				mimeMessage.addRecipient(Message.RecipientType.CC, new InternetAddress(ccUser));
			}
		}
		mimeMessage.addRecipient(Message.RecipientType.CC, new InternetAddress(release.getUser().getEmail()));

		mailSender.send(mimeMessage);
	}

	@SuppressWarnings("unused")
	private MimeMultipart MimeMultipart(String body) throws MessagingException {
		MimeMultipart mmp = new MimeMultipart();
		Map<String, String> inlineImage = new HashMap<String, String>();
		final Matcher matcher = imgRegExp.matcher(body);
		int i = 0;
		while (matcher.find()) {
			String src = matcher.group();
			if (body.indexOf(src) != -1) {
				String srcToken = "src=\"";
				int x = src.indexOf(srcToken);
				int y = src.indexOf("\"", x + srcToken.length());
				String srcText = src.substring(x + srcToken.length(), y);
				String cid = "image" + i;
				String newSrc = src.replace(srcText, "cid:" + cid);
				inlineImage.put(cid, srcText.split(",")[1]);
				body = body.replace(src, newSrc);
				i++;
			}
		}
		BodyPart bp = new MimeBodyPart();
		bp.setContent(body, "text/html");
		mmp.addBodyPart(bp);
		Iterator<Entry<String, String>> it = inlineImage.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, String> pairs = it.next();
			PreencodedMimeBodyPart pmp = new PreencodedMimeBodyPart("base64");
			pmp.setHeader("Content-ID", "<" + pairs.getKey() + ">");
			pmp.setDisposition(MimeBodyPart.INLINE);
			pmp.setContent(pairs.getValue(), "image/png");
			mmp.addBodyPart(pmp);
		}
		return mmp;
	}

	public EmailTemplate fillEmail(EmailTemplate email, Release release) {
		String temp = "";
		/* ------ body ------ */
		if (email.getHtml().contains("{{userName}}")) {
			email.setHtml(email.getHtml().replace("{{userName}}",
					(release.getUser().getFullName() != null ? release.getUser().getFullName() : "")));
		}

		if (email.getHtml().contains("{{releaseNumber}}")) {
			email.setHtml(email.getHtml().replace("{{releaseNumber}}",
					(release.getReleaseNumber() != null ? release.getReleaseNumber() : "")));
		}

		if (email.getHtml().contains("{{description}}")) {
			String description = release.getDescription() != null ? release.getDescription() : "";
			description = description.replace("\n", "<br>");
			email.setHtml(email.getHtml().replace("{{description}}", description));
		}

		if (email.getHtml().contains("{{observation}}")) {
			String observation = release.getObservations() != null ? release.getObservations() : "";
			observation = observation.replace("\n", "<br>");
			email.setHtml(email.getHtml().replace("{{observation}}", observation));
		}

		if (email.getHtml().contains("{{functionalSolution}}")) {
			String functionalSolution = release.getFunctionalSolution() != null ? release.getFunctionalSolution() : "";
			functionalSolution = functionalSolution.replace("\n", "<br>");
			email.setHtml(email.getHtml().replace("{{functionalSolution}}", functionalSolution));
		}

		if (email.getHtml().contains("{{minimalEvidence}}")) {
			String minimalEvidence = release.getMinimal_evidence() != null ? release.getMinimal_evidence() : "";
			minimalEvidence = minimalEvidence.replace("\n", "<br>");
			email.setHtml(email.getHtml().replace("{{minimalEvidence}}", minimalEvidence));
		}

		if (email.getHtml().contains("{{technicalSolution}}")) {
			String technicalSolution = release.getTechnicalSolution() != null ? release.getTechnicalSolution() : "";
			technicalSolution = technicalSolution.replace("\n", "<br>");
			email.setHtml(email.getHtml().replace("{{technicalSolution}}", technicalSolution));
		}

		if (email.getHtml().contains("{{ambient}}")) {
			temp = "";
			for (Ambient amb : release.getAmbients()) {
				temp += amb.getName() + "<br>";
			}
			email.setHtml(email.getHtml().replace("{{ambient}}", (temp.equals("") ? "Sin ambientes definidos" : temp)));
		}

		if (email.getHtml().contains("{{dependencies}}")) {
			temp = "";
			int i = 1;
			for (Dependency dep : release.getDependencies()) {
				temp += i + ": " + dep.getTo_release().getReleaseNumber() + "<br>";
				i++;
			}
			email.setHtml(email.getHtml().replace("{{dependencies}}",
					(temp.equals("") ? "Sin dependencias definidos" : temp)));
		}

		if (email.getHtml().contains("{{objects}}")) {
			temp = "<ul>";

			for (ReleaseObject obj : release.getObjects()) {
				temp += "<li> " + obj.getName() + "</li>";
			}
			temp += "</ul>";
			email.setHtml(email.getHtml().replace("{{objects}}", (temp.equals("") ? "Sin objetos definidos" : temp)));
		}

		if (email.getHtml().contains("{{version}}")) {
			email.setHtml(email.getHtml().replace("{{version}}",
					(release.getVersionNumber() != null ? release.getVersionNumber() : "")));
		}

		/* ------ Subject ------ */
		if (email.getSubject().contains("{{releaseNumber}}")) {
			email.setSubject(email.getSubject().replace("{{releaseNumber}}",
					(release.getReleaseNumber() != null ? release.getReleaseNumber() : "")));
		}
		if (email.getSubject().contains("{{version}}")) {
			email.setSubject(email.getSubject().replace("{{version}}",
					(release.getVersionNumber() != null ? release.getVersionNumber() : "")));
		}
		if (email.getSubject().contains("{{priority}}")) {
			email.setSubject(email.getSubject().replace("{{priority}}",
					(release.getPriority().getName() != null ? release.getPriority().getName() : "")));
		}

		if (email.getSubject().contains("{{impact}}")) {
			email.setSubject(email.getSubject().replace("{{impact}}",
					(release.getImpact().getName() != null ? release.getImpact().getName() : "")));
		}

		if (email.getSubject().contains("{{risk}}")) {
			email.setSubject(email.getSubject().replace("{{risk}}",
					(release.getRisk().getName() != null ? release.getRisk().getName() : "")));
		}

		return email;
	}

	@Override
	public boolean existEmailTemplate(String name) {
		return dao.existEmailTemplate(name);
	}

	@Override
	public boolean existEmailTemplate(String name, Integer id) {
		return dao.existEmailTemplate(name, id);
	}

	@Override
	public void deleteEmail(Integer id) {
		dao.deleteEmail(id);
	}

	@Override
	public void sendMail(UserInfo user, String password, EmailTemplate email) throws Exception {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		mimeMessage.setHeader("Content-Type", "text/plain; charset=UTF-8");
		try {
			Map<String, String> inlineImage = new HashMap<String, String>();
			String body = email.getHtml();
			body = body.replace("{{date}}", new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
			body = body.replace("{{username}}", user.getUsername());
			body = body.replace("{{password}}", password);
			body = Constant.getCharacterEmail(body);
			final Matcher matcher = imgRegExp.matcher(body);
			int i = 0;
			while (matcher.find()) {
				String src = matcher.group();
				if (body.indexOf(src) != -1) {
					String srcToken = "src=\"";
					int x = src.indexOf(srcToken);
					int y = src.indexOf("\"", x + srcToken.length());
					String srcText = src.substring(x + srcToken.length(), y);
					String cid = "image" + i;
					String newSrc = src.replace(srcText, "cid:" + cid);
					inlineImage.put(cid, srcText.split(",")[1]);
					body = body.replace(src, newSrc);
					i++;
				}
			}
			mimeMessage.setSubject(email.getSubject());
			mimeMessage.setSender(new InternetAddress(envConfig.getEntry("mailUser")));
			mimeMessage.setFrom(new InternetAddress(envConfig.getEntry("mailUser")));
			BodyPart bp = new MimeBodyPart();
			bp.setContent(body, "text/html");
			MimeMultipart mmp = new MimeMultipart();
			mmp.addBodyPart(bp);
			Iterator<Entry<String, String>> it = inlineImage.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, String> pairs = it.next();
				PreencodedMimeBodyPart pmp = new PreencodedMimeBodyPart("base64");
				pmp.setHeader("Content-ID", "<" + pairs.getKey() + ">");
				pmp.setDisposition(MimeBodyPart.INLINE);
				pmp.setContent(pairs.getValue(), "image/png");
				mmp.addBodyPart(pmp);
			}
			mimeMessage.setContent(mmp);
			mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmailAddress()));
			mailSender.send(mimeMessage);
		} catch (Exception e) {
			Sentry.capture(e, "email");
			throw e;
		}
	}

	@Override
	public void sendMail(WFRelease releaseEmail, EmailTemplate email, String motive) {
		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			mimeMessage.setHeader("Content-Type", "text/plain; charset=UTF-8");
			// ------------------Seccion del asunto del correo -------------------------- //
			// Se agrega el nombre del sistema
			if (email.getSubject().contains("{{systemName}}")) {
				email.setSubject(email.getSubject().replace("{{systemName}}",
						(releaseEmail.getSystem() != null ? releaseEmail.getSystem().getName() : "")));
			}
			// Se agrega el numero de release
			if (email.getSubject().contains("{{releaseNumber}}")) {
				email.setSubject(email.getSubject().replace("{{releaseNumber}}",
						(releaseEmail.getReleaseNumber() != null ? releaseEmail.getReleaseNumber() : "")));
			}
			// ------------------Seccion del cuerpo del correo -------------------------- //
			if (email.getHtml().contains("{{releaseNumber}}")) {
				email.setHtml(email.getHtml().replace("{{releaseNumber}}",
						(releaseEmail.getReleaseNumber() != null ? releaseEmail.getReleaseNumber() : "")));
			}

			if (email.getHtml().contains("{{releaseStatus}}")) {
				email.setHtml(email.getHtml().replace("{{releaseStatus}}",
						(releaseEmail.getStatus() != null ? releaseEmail.getStatus().getName() : "")));
			}
			if (email.getHtml().contains("{{userName}}")) {
				email.setHtml(email.getHtml().replace("{{userName}}",
						(releaseEmail.getUser().getFullName() != null ? releaseEmail.getUser().getFullName() : "")));
			}
			if (email.getHtml().contains("{{operator}}")) {
				email.setHtml(email.getHtml().replace("{{operator}}",
						(releaseEmail.getOperator() != null ? releaseEmail.getOperator() : "")));
			}

			if (email.getHtml().contains("{{updateAt}}")) {
				DateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy hh:mm a");
				String strDate = dateFormat.format(releaseEmail.getCreateDate());
				email.setHtml(email.getHtml().replace("{{updateAt}}", strDate));
			}

			if (email.getHtml().contains("{{motive}}")) {
				email.setHtml(email.getHtml().replace("{{motive}}", (motive != null ? motive : "")));
			}

			String body = email.getHtml();
			body = Constant.getCharacterEmail(body);
			MimeMultipart mmp = MimeMultipart(body);
			mimeMessage.setContent(mmp);
			mimeMessage.setSubject(email.getSubject());
			mimeMessage.setSender(new InternetAddress(envConfig.getEntry("mailUser")));
			mimeMessage.setFrom(new InternetAddress(envConfig.getEntry("mailUser")));
			for (WFUser toUser : releaseEmail.getNode().getUsers()) {
				mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(toUser.getEmail()));
			}
			// Se notifica el usuario que lo solicito
			mimeMessage.addRecipient(Message.RecipientType.CC, new InternetAddress(releaseEmail.getUser().getEmail()));

			mailSender.send(mimeMessage);
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void sendMailActor(WFRelease releaseEmail, EmailTemplate email) {
		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			mimeMessage.setHeader("Content-Type", "text/plain; charset=UTF-8");
			// ------------------Seccion del asunto del correo -------------------------- //
			// Se agrega el nombre del sistema
			if (email.getSubject().contains("{{systemName}}")) {
				email.setSubject(email.getSubject().replace("{{systemName}}",
						(releaseEmail.getSystem() != null ? releaseEmail.getSystem().getName() : "")));
			}
			// Se agrega el numero de release
			if (email.getSubject().contains("{{releaseNumber}}")) {
				email.setSubject(email.getSubject().replace("{{releaseNumber}}",
						(releaseEmail.getReleaseNumber() != null ? releaseEmail.getReleaseNumber() : "")));
			}
			// ------------------Seccion del cuerpo del correo -------------------------- //
			if (email.getHtml().contains("{{releaseNumber}}")) {
				email.setHtml(email.getHtml().replace("{{releaseNumber}}",
						(releaseEmail.getReleaseNumber() != null ? releaseEmail.getReleaseNumber() : "")));
			}

			if (email.getHtml().contains("{{releaseStatus}}")) {
				email.setHtml(email.getHtml().replace("{{releaseStatus}}",
						(releaseEmail.getStatus() != null ? releaseEmail.getStatus().getName() : "")));
			}
			if (email.getHtml().contains("{{userName}}")) {
				email.setHtml(email.getHtml().replace("{{userName}}",
						(releaseEmail.getUser().getFullName() != null ? releaseEmail.getUser().getFullName() : "")));
			}
			String body = email.getHtml();
			body = Constant.getCharacterEmail(body);
			MimeMultipart mmp = MimeMultipart(body);
			mimeMessage.setContent(mmp);
			mimeMessage.setSubject(email.getSubject());
			mimeMessage.setSender(new InternetAddress(envConfig.getEntry("mailUser")));
			mimeMessage.setFrom(new InternetAddress(envConfig.getEntry("mailUser")));
			for (WFUser toUser : releaseEmail.getNode().getActors()) {
				mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(toUser.getEmail()));
			}
			mailSender.send(mimeMessage);
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
