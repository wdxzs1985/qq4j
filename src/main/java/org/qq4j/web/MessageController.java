package org.qq4j.web;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
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
        final List<String> wordList = this.messageService.getIndexes(messageId);
        model.addAttribute("wordList", wordList);
        return "message";
    }

    @RequestMapping(value = "/{account}/study", method = RequestMethod.GET)
    public String study(@PathVariable final long account,
                        @RequestParam(required = false) final String message,
                        final Model model) throws UnsupportedEncodingException {
        model.addAttribute("account", account);
        if (StringUtils.isNotBlank(message)) {
            final String messageUtf8 = new String(message.getBytes("ISO-8859-1"));
            model.addAttribute("message", messageUtf8);
            final String source = StringUtils.lowerCase(messageUtf8);
            final QQAiManager aiManager = this.robotService.getAiManager();
            final List<String> wordList = aiManager.analystString(source);
            model.addAttribute("wordList", wordList);
            List<QQMessage> answerList = null;
            if (CollectionUtils.isEmpty(wordList)) {
                answerList = this.messageService.searchAnswer(source, account);
            } else {
                answerList = this.messageService.searchAnswersByIndex(wordList,
                                                                      account);
            }
            model.addAttribute("answerList", answerList);
        }
        return "study";
    }

    @RequestMapping(value = "/{account}/answer", method = RequestMethod.POST)
    public String answer(@PathVariable final long account,
                         final String message,
                         final String answer,
                         @RequestParam(value = "word[]") final String[] word,
                         final Model model) {
        if (StringUtils.isNotBlank(message)
            && StringUtils.isNotBlank(answer)
            && !ArrayUtils.isEmpty(word)) {
            this.messageService.addAnswer(account, message, answer, word);
        }
        model.addAttribute("message", message);
        return "redirect:/"
               + account
               + "/study";
    }
}
