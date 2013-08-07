package org.qq4j.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.commons.lang.StringUtils;
import org.qq4j.core.QQRobot;
import org.qq4j.domain.QQFont;
import org.qq4j.domain.QQUser;
import org.qq4j.service.RobotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private RobotService robotService = null;

    /**
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(final Model model) {
        model.addAttribute("qqRobotList", this.robotService.getRobotList());
        return "home";
    }

    /**
     */
    @RequestMapping(value = "/{account}", method = RequestMethod.GET)
    public String account(@PathVariable final long account, final Model model) {
        final QQRobot robot = this.robotService.getRobot(account);
        model.addAttribute("account", account);
        model.addAttribute("qqRobot", robot);
        return "account";
    }

    /**
     */
    @RequestMapping(value = "/{account}/login", method = RequestMethod.GET)
    public String doGetlogin(@PathVariable final long account, final Model model) {
        model.addAttribute("account", account);
        final QQRobot robot = this.robotService.getRobot(account);
        if (robot.getContext().isRun()) {
            return "redirect:/";
        }
        robot.getContext().getUserManager().getVerifyCode();
        model.addAttribute("qqRobot", robot);
        return "login";
    }

    @RequestMapping(value = "/{account}/login", method = RequestMethod.POST)
    public String doPostlogin(@PathVariable final long account,
                              final String password,
                              final String verifyCode,
                              final Model model)
                                                throws UnsupportedEncodingException {
        final QQRobot robot = this.robotService.getRobot(account);
        final QQUser user = robot.getContext()
                                 .getUserManager()
                                 .login(password, verifyCode);
        if (user != null) {
            robot.startup(user);
        }
        return "redirect:/"
               + account;
    }

    @RequestMapping(value = "/{account}/verify.jpg", method = RequestMethod.GET)
    @ResponseBody
    public byte[] verify(@PathVariable final long account, final Model model)
                                                                             throws IOException {
        final QQRobot robot = this.robotService.getRobot(account);
        return robot.getContext().getUserManager().downloadVerifyImage();
    }

    @RequestMapping(value = "/{account}/offline", method = RequestMethod.GET)
    public String login(@PathVariable final long account, final Model model) {
        final QQRobot robot = this.robotService.getRobot(account);
        robot.shutdown();
        return "redirect:/"
               + account;
    }

    @RequestMapping(value = "/{account}/nlk", method = RequestMethod.POST)
    public String setLongNick(@PathVariable final long account, final String nlk) {
        final QQRobot robot = this.robotService.getRobot(account);
        if (StringUtils.isNotBlank(nlk)) {
            robot.getContext().getUserManager().setLongNick(nlk);
        }
        return "redirect:/"
               + account;
    }

    @RequestMapping(value = "/{account}/font", method = RequestMethod.POST)
    public String setFont(@PathVariable final long account,
                          final String name,
                          final int size,
                          final String color,
                          final String bold,
                          final String italic) {
        final QQRobot robot = this.robotService.getRobot(account);
        final QQFont font = robot.getContext().getFont();
        font.setName(name);
        font.setSize(size);
        font.setColor(color);
        font.setBold(StringUtils.isNotBlank(bold));
        font.setItalic(StringUtils.isNotBlank(italic));
        return "redirect:/"
               + account;
    }

    /**
     */
    @RequestMapping(value = "/study", method = RequestMethod.GET)
    public String study(final Model model) {
        model.addAttribute("qqRobotList", this.robotService.getRobotList());
        return "study";
    }

}
