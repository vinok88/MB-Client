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

package org.wso2.mb.testing.client.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.naming.NamingException;

/**
 * Client used to subscribe to queues in Broker
 */
public class AndesQueuePublisher {
    private final QueueConnectionFactory connectionFactory;
    private final Queue destination;
    private QueueConnection connection;
    private QueueSender publisher;
    private QueueSession session;

    AndesQueuePublisher(ClientBuilder builder) throws NamingException {
        connectionFactory = builder.getConnectionFactory(builder);
        destination = builder.getDestination(builder);
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
}
