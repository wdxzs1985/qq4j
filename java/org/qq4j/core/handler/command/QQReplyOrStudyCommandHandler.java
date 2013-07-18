package org.qq4j.core.handler.command;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.qq4j.core.QQAiManager;
import org.qq4j.core.QQContext;
import org.qq4j.core.QQSession;
import org.qq4j.core.handler.QQCommandHandler;
import org.qq4j.core.handler.QQSessionHandler;
import org.qq4j.domain.QQGroup;
import org.qq4j.domain.QQUser;
import org.springframework.beans.factory.annotation.Autowired;

public class QQReplyOrStudyCommandHandler implements QQCommandHandler {

    @Autowired
    private QQAiManager aiManager = null;

    public static final String STUDY_STEP = "QQReplyOrStudyCommandHandler.step";
    public static final String STUDY_QUESTION = "QQReplyOrStudyCommandHandler.question";
    public static final int STEP_0 = 0;
    public static final int STEP_1 = 1;
    public static final int STEP_2 = 2;

    private final static Log LOG = LogFactory.getLog(QQReplyOrStudyCommandHandler.class);

    private String command1;// 不对
    private String command3;// 放弃
    //
    private String answer1;
    private String answer2;
    private String answer3;
    private String answer4;
    // private String answer5;
    private String answer6;
    private String answer7;
    private String answer8;
    //
    private int faith = 0;

    @Override
    public void handle(final QQContext context,
                       final QQUser user,
                       final String message) {
        final QQSession session = context.getSessionManager().getSession(user);
        Integer step = (Integer) session.get(QQReplyOrStudyCommandHandler.STUDY_STEP);
        if (step == null) {
            step = QQReplyOrStudyCommandHandler.STEP_0;
        }
        if (QQReplyOrStudyCommandHandler.LOG.isDebugEnabled()) {
            QQReplyOrStudyCommandHandler.LOG.debug(String.format("%s进入自动学习模式(step: %d)",
                                                                 user,
                                                                 step));
        }

        switch (step) {
        case STEP_0:
            this.doStep0(context, user, session);
            if (StringUtils.isBlank(message)) {
                break;
            }
        case STEP_1:
            this.doStep1(context, user, session, message);
            break;
        case STEP_2:
            this.doStep2(context, user, session, message);
            break;
        }
    }

    private void doStep0(final QQContext context,
                         final QQUser user,
                         final QQSession session) {
        context.getSender().sendToUser(user, this.getAnswer1());
        session.put(QQSessionHandler.SESSION_HANDLER, this);
        session.put(QQReplyOrStudyCommandHandler.STUDY_STEP,
                    QQReplyOrStudyCommandHandler.STEP_1);
    }

    private void doStep1(final QQContext context,
                         final QQUser user,
                         final QQSession session,
                         final String message) {
        String answer = null;
        if (StringUtils.isBlank(message)) {
            answer = this.getAnswer2();
        } else {
            final String question = (String) session.get(QQReplyOrStudyCommandHandler.STUDY_QUESTION);
            if (StringUtils.length(message) > 200) {
                answer = this.getAnswer4();
            } else {
                if (StringUtils.isNotBlank(question)
                    && StringUtils.equals(this.getCommand1(), message)) {
                    // 不对
                    answer = this.getAnswer3();
                    session.put(QQSessionHandler.SESSION_HANDLER, this);
                    session.put(QQReplyOrStudyCommandHandler.STUDY_STEP,
                                QQReplyOrStudyCommandHandler.STEP_2);
                } else {
                    session.put(QQSessionHandler.SESSION_HANDLER, this);
                    session.put(QQReplyOrStudyCommandHandler.STUDY_QUESTION,
                                message);
                    answer = this.aiManager.getReplyAnswerSmart(message, user);
                    if (StringUtils.isBlank(answer)) {
                        answer = this.getAnswer6();
                        session.put(QQReplyOrStudyCommandHandler.STUDY_STEP,
                                    QQReplyOrStudyCommandHandler.STEP_2);
                    } else if (StringUtils.equals(answer, "[屏蔽]")) {
                        answer = null;
                    }
                }
            }
        }
        context.getSender().sendToUser(user, answer);
    }

    private void doStep2(final QQContext context,
                         final QQUser user,
                         final QQSession session,
                         final String message) {
        String answer = null;
        if (StringUtils.isBlank(message)) {
            answer = this.getAnswer2();
        } else if (StringUtils.length(message) > 200) {
            answer = this.getAnswer4();
        } else {
            if (StringUtils.equals(this.getCommand3(), message)) {
                answer = this.getAnswer7();
            } else {
                final String question = (String) session.get(QQReplyOrStudyCommandHandler.STUDY_QUESTION);
                final long account = context.getSelf().getAccount();
                this.aiManager.addAnswer(question,
                                         message,
                                         account,
                                         user.getAccount());
                this.aiManager.increaseFaith(user, this.getFaith());
                answer = this.getAnswer8();
            }
            session.remove(QQReplyOrStudyCommandHandler.STUDY_QUESTION);
            session.put(QQSessionHandler.SESSION_HANDLER, this);
            session.put(QQReplyOrStudyCommandHandler.STUDY_STEP,
                        QQReplyOrStudyCommandHandler.STEP_1);
        }
        context.getSender().sendToUser(user, answer);
    }

    @Override
    public void handleGroup(final QQContext context,
                            final QQGroup group,
                            final QQUser user,
                            final String message) {
    }

    public String getCommand1() {
        return this.command1;
    }

    public void setCommand1(final String command1) {
        this.command1 = command1;
    }

    public String getCommand3() {
        return this.command3;
    }

    public void setCommand3(final String command3) {
        this.command3 = command3;
    }

    public String getAnswer1() {
        return this.answer1;
    }

    public void setAnswer1(final String answer1) {
        this.answer1 = answer1;
    }

    public String getAnswer2() {
        return this.answer2;
    }

    public void setAnswer2(final String answer2) {
        this.answer2 = answer2;
    }

    public String getAnswer3() {
        return this.answer3;
    }

    public void setAnswer3(final String answer3) {
        this.answer3 = answer3;
    }

    public String getAnswer6() {
        return this.answer6;
    }

    public void setAnswer6(final String answer6) {
        this.answer6 = answer6;
    }

    public String getAnswer7() {
        return this.answer7;
    }

    public void setAnswer7(final String answer7) {
        this.answer7 = answer7;
    }

    public String getAnswer8() {
        return this.answer8;
    }

    public void setAnswer8(final String answer8) {
        this.answer8 = answer8;
    }

    public int getFaith() {
        return this.faith;
    }

    public void setFaith(final int faith) {
        this.faith = faith;
    }

    public String getAnswer4() {
        return this.answer4;
    }

    public void setAnswer4(final String answer4) {
        this.answer4 = answer4;
    }

}
