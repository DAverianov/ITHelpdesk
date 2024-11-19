package de.lewens_markisen.web.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import de.lewens_markisen.domain.local_db.email.EmailAcсount;
import de.lewens_markisen.email.EmailAccountService;
import de.lewens_markisen.email.model.EmailAccounts;
import de.lewens_markisen.security.perms.EmailAccountCreatePermission;
import de.lewens_markisen.security.perms.EmailAccountReadPermission;
import de.lewens_markisen.security.perms.EmailAccountUpdatePermission;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/email_acсounts")
@Controller
public class EmailAccountController {

	private final EmailAccountService emailAccountService;

	@EmailAccountReadPermission
	@GetMapping(path = "/list")
	public String list(@RequestParam(defaultValue = "1") int page, Model model) {
		EmailAccounts accounts = new EmailAccounts();
		Page<EmailAcсount> paginated = findPaginated(page);
		accounts.getAccountList().addAll(paginated.toList());
		return addPaginationModel(page, paginated, model);
	}

	private String addPaginationModel(int page, Page<EmailAcсount> paginated, Model model) {
		List<EmailAcсount> accounts = paginated.getContent();
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", paginated.getTotalPages());
		model.addAttribute("totalItems", paginated.getTotalElements());
		model.addAttribute("accounts", accounts);
		return "email_acсounts/emailAccountList";
	}

	private Page<EmailAcсount> findPaginated(int page) {
		int pageSize = 12;
		Sort sort = Sort.by("email").ascending();
		Pageable pageable = PageRequest.of(page - 1, pageSize, sort);
		return emailAccountService.findAll(pageable);
	}

	@EmailAccountReadPermission
	@GetMapping(value = "/{id}")
	public ModelAndView showEditAccountForm(@PathVariable(name = "id") Long id) {
		ModelAndView modelAndView = new ModelAndView("email_acсounts/emailAccountEdit");
		Optional<EmailAcсount> accountOpt = emailAccountService.findById(id);
		if (accountOpt.isPresent()) {
			modelAndView.addObject("account", accountOpt.get());
		} else {
			modelAndView.addObject("message", "Account mit id wurde nicht gefunden!");
			modelAndView.setViewName("error");
		}
		return modelAndView;
	}

	@EmailAccountCreatePermission
	@GetMapping("/new")
	public String initCreationForm(Model model) {
		model.addAttribute("account", EmailAcсount.builder().build());
		return "email_acсounts/createEmailAccount";
	}

	@EmailAccountCreatePermission
	@PostMapping("/new")
	public String processCreationForm(EmailAcсount emailAccount) {
		//@formatter:off
		EmailAcсount newAccount = EmailAcсount.builder()
				.email(emailAccount.getEmail())
				.host(emailAccount.getHost())
				.port(emailAccount.getPort())
				.username(emailAccount.getUsername())
				.access(emailAccount.getAccess())
				.outgoingProtocol(emailAccount.getOutgoingProtocol())
				.smtpAuth(emailAccount.getSmtpAuth())
				.smtpStarttlsEnable(emailAccount.getSmtpStarttlsEnable())
				.build();
		//@formatter:on
		EmailAcсount savedAccess = emailAccountService.save(newAccount);
		return "redirect:/email_acсounts/list";
	}

	@EmailAccountUpdatePermission
	@PostMapping(value = "/update")
    public String update(@ModelAttribute("access") EmailAcсount access, @RequestParam(value="action", required=true) String action) {
		System.out.println("ich bin bei Update");
     	if (action.equals("update")) {
     		emailAccountService.update(access);
        }
        return "redirect:/email_acсounts/list";
    }

}
