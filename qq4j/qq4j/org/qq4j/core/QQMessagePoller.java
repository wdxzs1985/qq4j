package org.qq4j.core;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.qq4j.core.handler.QQMessageHandlerMapping;
import org.qq4j.net.SystemConstants;


public class QQMessagePoller implements Runnable {

    private static Log LOG = LogFactory.getLog(QQMessagePoller.class);

    private static final Executor threadExecutor = Executors.newFixedThreadPool(10,
                                                                                new QQThreadFactory("poller"));

    public static final void shutdown() {
        final ThreadPoolExecutor executor = (ThreadPoolExecutor) QQMessagePoller.threadExecutor;
        executor.shutdownNow();
    }

    private QQContext context = null;
    private QQMessageHandlerMapping handlers = null;

    @Override
    public void run() {
        final QQContext context = this.getContext();
        final String pollUrl = "http://d.web2.qq.com/channel/poll2?clientid=" + context.getClientid() + "&psessionid=" + context.getPsessionid();
        while (context.isRun()) {
            try {
                final String ret = context.getHttpClient().getData(pollUrl);
                if (StringUtils.isNotBlank(ret)) {
                    final JSONObject retJson = JSONObject.fromObject(ret);
                    final int retcode = retJson.getInt("retcode");
                    switch (retcode) {
                    case 0:
                        final JSONArray result = retJson.getJSONArray("result");
                        for (int i = 0; i < result.size(); i++) {
                            QQMessagePoller.threadExecutor.execute(new HandleMessageWorker(context,
                                                                                           this.handlers,
                                                                                           result.getJSONObject(i)));
                        }
                        break;
                    case 102:
                        if (QQMessagePoller.LOG.isDebugEnabled()) {
                            final String message = String.format("[%s]%s >> 没有收到消息，重试。。。",
                                                                 DateFormatUtils.format(System.currentTimeMillis(),
                                                                                        SystemConstants.DATETIME_FORMAT),
                                                                 context.getSelf());
                            QQMessagePoller.LOG.debug(message);
                        }
                        break;
                    case 116:
                        final String p = retJson.getString("p");
                        if (QQMessagePoller.LOG.isDebugEnabled()) {
                            final String message = String.format("[%s]%s >> 更新Ptwebqq：%s",
                                                                 DateFormatUtils.format(System.currentTimeMillis(),
                                                                                        SystemConstants.DATETIME_FORMAT),
                                                                 context.getSelf(),
                                                                 p);
                            QQMessagePoller.LOG.debug(message);
                        }
                        context.setPtwebqq(p);
                        break;
                    case 100:
                    case 120:
                    case 121:
                    default:
                        final String message = String.format("[%s]%s >> QQ出现未知错误：%s",
                                                             DateFormatUtils.format(System.currentTimeMillis(),
                                                                                    SystemConstants.DATETIME_FORMAT),
                                                             context.getSelf(),
                                                             ret);
                        throw new RuntimeException(message);
                        // context.setRun(false);
                        // QQMessagePoller.LOG.warn("QQ出现未知错误。error=" +
                        // retcode);
                        // QQMessagePoller.LOG.warn(ret);
                        // break;
                    }
                }
            } catch (final JSONException e) {
                QQMessagePoller.LOG.error(e.getMessage());
            } catch (final Exception e) {
                context.setRun(false);
                QQMessagePoller.LOG.error("遇到致命错误！", e);
                throw new RuntimeException(e);
            }
        }
    }

    public QQContext getContext() {
        return this.context;
    }

    public QQMessageHandlerMapping getHandlers() {
        return this.handlers;
    }

    public void setContext(final QQContext context) {
        this.context = context;
    }

    public void setHandlers(final QQMessageHandlerMapping handlers) {
        this.handlers = handlers;
    }

    static class HandleMessageWorker implements Runnable {

        private final QQContext context;
        private final QQMessageHandlerMapping handlers;
        private final JSONObject obj;

        HandleMessageWorker(final QQContext context, final QQMessageHandlerMapping handlers, final JSONObject obj) {
            this.context = context;
            this.handlers = handlers;
            this.obj = obj;
        }

        @Override
        public void run() {
            try {
                this.handlers.handle(this.context, this.obj);
            } catch (final JSONException e) {
                QQMessagePoller.LOG.error(e.getMessage());
            }
        }

    }
}