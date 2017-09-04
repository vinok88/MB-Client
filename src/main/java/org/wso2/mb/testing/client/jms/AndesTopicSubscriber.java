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
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicRequestor;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.NamingException;

public class AndesTopicSubscriber {

    private final TopicConnectionFactory connectionFactory;
    private final Topic destination;
    private TopicConnection connection;
    private TopicSubscriber consumer;

    AndesTopicSubscriber(ClientBuilder builder) throws NamingException {
        connectionFactory = builder.getTopicConnectionFactory(builder);
        destination = builder.getTopicDestination(builder);
    }

    /**
     * Connects to the configured broker.
     *
     * @throws JMSException
     */
    public void connect() throws JMSException {
        connection = connectionFactory.createTopicConnection();
        connection.start();
        TopicSession session = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
        consumer = session.createSubscriber(destination);
    }

    /**
     * Disconnects from the broker.
     *
     * @throws JMSException
     */
    public void disconnect() throws JMSException {
        connection.close();
    }

    /**
     * Receives the next message produced for this message consumer.
     *
     * @return
     * @throws JMSException
     */
    public String receive() throws JMSException {
        return ((TextMessage) consumer.receive()).getText();
    }
}