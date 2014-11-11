/*
 * Copyright (c) 2005-2014, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.mb.testing.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

/**
 * Client used to subscribe to queues in Broker
 */
public class AndesQueuePublisher {
    private final QueueConnectionFactory connectionFactory;
    private final Queue destination;
    private QueueConnection connection;
    private QueueSender publisher;
    private QueueSession session;

    private AndesQueuePublisher(Builder builder) throws NamingException {
        connectionFactory = (QueueConnectionFactory) builder.initialContext
                .lookup(Builder.connectionFactory);
        destination = (Queue) builder.initialContext.lookup(builder.destinationName);
    }

    /**
     * Connects to the configured broker.
     *
     * @throws javax.jms.JMSException
     */
    public void connect() throws JMSException {
        connection = connectionFactory.createQueueConnection();
        connection.start();
        session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        publisher = session.createSender(destination);
    }

    /**
     * Disconnects from the broker.
     *
     * @throws javax.jms.JMSException
     */
    public void disconnect() throws JMSException {
        connection.close();
    }

    /**
     * Send the next message produced for this message publisher.
     *
     * @return
     * @throws javax.jms.JMSException
     */
    public void send(String message) throws JMSException {
        Message jmsMessage = session.createTextMessage(message);
        publisher.send(jmsMessage);
    }

    public static class Builder {
        public enum DestinationType {QUEUE, TOPIC}

        ;

        private static final String connectionFactory = "andesConnectionfactory";

        // Required information to access broker
        private final String username;
        private final String password;
        private final String destinationName;

        // Initialized to default parameters
        String brokerHost = "localhost";
        int port = AndesClientUtils.ANDES_DEFAULT_PORT;
        private InitialContext initialContext;

        public Builder(String username, String password, String destinationName) {
            this.username = username;
            this.password = password;
            this.destinationName = destinationName;
        }

        public Builder brokerHost(String hostname) {
            this.brokerHost = hostname;
            return this;
        }

        public Builder port(int port) {
            this.port = port;
            return this;
        }

        public AndesQueuePublisher build() throws NamingException {
            createInitialContext();
            return new AndesQueuePublisher(this);
        }

        private void createInitialContext() throws NamingException {
            Properties contextProperties = createContextProperties();
            this.initialContext = new InitialContext(contextProperties);
        }

        private Properties createContextProperties() {
            Properties contextProperties = new Properties();
            contextProperties.put(Context.INITIAL_CONTEXT_FACTORY,
                                  AndesClientUtils.ANDES_INITIAL_CONTEXT_FACTORY);
            String connectionString = AndesClientUtils.getBrokerConnectionString(this.username,
                                                                                 this.password,
                                                                                 this.brokerHost,
                                                                                 this.port);
            // TODO: Check if this is actually required
            contextProperties.put("connectionfactory." + connectionFactory,
                                  connectionString);
            contextProperties.put("queue." + this.destinationName, this.destinationName);
            return contextProperties;
        }
    }
}
