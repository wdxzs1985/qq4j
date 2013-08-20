package org.qq4j.web;

import java.util.List;

import org.qq4j.core.QQRobot;
import org.qq4j.domain.QQUser;
import org.qq4j.service.RobotService;
import org.qq4j.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class UserController {

    @Autowired
    private UserService userService = null;
    @Autowired
    private RobotService robotService = null;

    @RequestMapping(value = "/{account}/users", method = RequestMethod.GET)
    public String users(@PathVariable final long account, final Model model) {
        final List<QQUser> userList = this.userService.getUsers(account);
        model.addAttribute("account", account);
        model.addAttribute("userList", userList);
        return "users";
    }

    @RequestMapping(value = "/{account}/user/{userAccount}/black", method = RequestMethod.GET)
    public String doGetUserBlack(@PathVariable final long account,
                                 @PathVariable final long userAccount,
                                 final Model model) {
        this.userService.black(account, userAccount);

        final QQRobot robot = this.robotService.getRobot(account);
        robot.getContext().getFriendManager().initFriendsInfo();

        return "redirect:/"
               + account
               + "/users";
    }
}
