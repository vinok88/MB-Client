/*
 * Copyright (c) WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License, 
 * Version 2.0 (the "License"); you may not use this file except 
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wso2.mb.testing.client.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.naming.NamingException;

public class AndesTopicPublisher {

    private final TopicConnectionFactory connectionFactory;
    private final Topic destination;
    private TopicConnection connection;
    private TopicPublisher publisher;
    private TopicSession session;

    AndesTopicPublisher(ClientBuilder builder) throws NamingException {
        connectionFactory = builder.getTopicConnectionFactory(builder);
        destination = builder.getTopicDestination(builder);
    }

    /**
     * Connects to the configured broker.
     *
     * @throws javax.jms.JMSException
     */
    public void connect() throws JMSException {
        connection = connectionFactory.createTopicConnection();
        connection.start();
        session = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
        publisher = session.createPublisher(destination);
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

    public void create(String queue) throws JMSException {
        connect();
        session.createTopic(queue);
    }
}