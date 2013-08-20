package org.qq4j.web;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.qq4j.core.QQAiManager;
import org.qq4j.domain.QQMessage;
import org.qq4j.service.MessageService;
import org.qq4j.service.RobotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MessageController {

    @Autowired
    private MessageService messageService = null;
    @Autowired
    private RobotService robotService = null;

    @RequestMapping(value = "/{account}/messages", method = RequestMethod.GET)
    public String messages(@PathVariable final long account,
                           @RequestParam(required = false) final Integer page,
                           @RequestParam(required = false) final Integer filter,
                           final Model model) {
        final List<QQMessage> messageList = this.messageService.getMessages(account,
                                                                            page);
        model.addAttribute("account", account);
        model.addAttribute("page", page);
        model.addAttribute("filter", filter);
        model.addAttribute("messageList", messageList);
        return "messages";
    }

    @RequestMapping(value = "/message/{messageId}", method = RequestMethod.GET)
    public String message(@PathVariable final String messageId,
                          final Model model) {
        final QQMessage message = this.messageService.getMessage(messageId);
        if (message == null) {
            return "redirect:/";
        }
        model.addAttribute("message", message);

        // this.robotService.getAiManager().getReplyAnswerSmart(message, user);

        return "message";
    }

    @RequestMapping(value = "/message/{messageId}", method = RequestMethod.POST)
    public String doPostMessage(@PathVariable final String messageId,
                                final String answer,
                                final Model model) {
        final QQMessage message = this.messageService.getMessage(messageId);
        if (message == null) {
            return "redirect:/";
        }
        if (StringUtils.isBlank(answer)) {
            model.addAttribute("message", message);
            return "message";
        }
        message.setAnswer(answer);
        message.setUnknown(0);
        this.messageService.updateAnswer(message);

        return "redirect:/"
               + message.getQq()
               + "/messages";
    }

    @RequestMapping(value = "/message/{messageId}/public", method = RequestMethod.GET)
    public String doGetMessagePublic(@PathVariable final String messageId,
                                     final Model model) {
        final QQMessage message = this.messageService.getMessage(messageId);
        if (message == null) {
            return "redirect:/";
        }
        message.setPrivatable(0);
        this.messageService.updatePrivatable(message);

        return "redirect:/"
               + message.getQq()
               + "/messages";
    }

    @RequestMapping(value = "/message/{messageId}/private", method = RequestMethod.GET)
    public String doGetMessagePrivate(@PathVariable final String messageId,
                                      final Model model) {
        final QQMessage message = this.messageService.getMessage(messageId);
        if (message == null) {
            return "redirect:/";
        }
        message.setPrivatable(1);
        this.messageService.updatePrivatable(message);

        return "redirect:/"
               + message.getQq()
               + "/messages";
    }

    @RequestMapping(value = "/message/{messageId}/delete", method = RequestMethod.GET)
    public String doGetMessageDelete(@PathVariable final String messageId,
                                     final Model model) {
        final QQMessage message = this.messageService.getMessage(messageId);
        if (message == null) {
            return "redirect:/";
        }
        this.messageService.deleteAnswer(message);
        return "redirect:/"
               + message.getQq()
               + "/messages";
    }

    @RequestMapping(value = "/{account}/study", method = RequestMethod.GET)
    public String study(@PathVariable final long account, final Model model)
                                                                            throws UnsupportedEncodingException {
        model.addAttribute("account", account);
        return "study";
    }

    @RequestMapping(value = "/{account}/study", method = RequestMethod.POST)
    public String doPostStudy(@PathVariable final long account,
                              final String message,
                              final String answer,
                              final Model model) {
        if (StringUtils.isNotBlank(message)
            && StringUtils.isNotBlank(answer)) {
            final QQAiManager aiManager = this.robotService.getAiManager();
            final String source = StringUtils.lowerCase(message);
            final List<String> wordList = aiManager.analystString(source);
            this.messageService.addAnswer(account, message, answer, wordList);
        }
        return "redirect:/"
               + account
               + "/messages";
    }
}
