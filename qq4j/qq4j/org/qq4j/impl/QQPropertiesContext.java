package org.qq4j.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.qq4j.core.QQContext;

public class QQPropertiesContext extends QQContext {

    private static final Log LOG = LogFactory.getLog(QQPropertiesContext.class);

    public void loadProperties(final String propertiesName) {
        final Properties configation = new Properties();
        InputStream is = null;
        try {
            is = this.getClass()
                     .getClassLoader()
                     .getResourceAsStream(propertiesName);
            configation.load(is);

            this.setConfigation(configation);
        } catch (final IOException e) {
            QQPropertiesContext.LOG.error(e);
        } finally {
            IOUtils.closeQuietly(is);
        }
    }

}
