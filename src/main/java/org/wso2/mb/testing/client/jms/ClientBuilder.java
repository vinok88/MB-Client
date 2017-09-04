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

import org.wso2.andes.jms.TopicSubscriber;

import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;
import javax.jms.Topic;
import javax.jms.TopicConnectionFactory;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

public class ClientBuilder {
    private static final String connectionFactory = "andesConnectionfactory";

    // Required information to access broker
    private final String username;
    private final String password;
    private final String destinationName;
    private final String type;

    // Initialized to default parameters
    private String brokerHost = "localhost";
    private int port = AndesClientUtils.ANDES_DEFAULT_PORT;
    private InitialContext initialContext;

    public  ClientBuilder(String username, String password, String destinationName, String type) {
        this.username = username;
        this.password = password;
        this.destinationName = destinationName;
        this.type = type;
    }

    public ClientBuilder brokerHost(String hostname) {
        this.brokerHost = hostname;
        return this;
    }

    public ClientBuilder port(int port) {
        this.port = port;
        return this;
    }

    public AndesQueueSubscriber buildSubscriber() throws NamingException {
        createInitialContext();
        return new AndesQueueSubscriber(this);
    }

    public AndesQueuePublisher buildPublisher() throws NamingException {
        createInitialContext();
        return new AndesQueuePublisher(this);
    }

    public AndesTopicSubscriber buildTopicSubscriber() throws NamingException {
        createInitialContext();
        return new AndesTopicSubscriber(this);
    }

    public AndesTopicPublisher buildTopicPublisher() throws NamingException {
        createInitialContext();
        return new AndesTopicPublisher(this);
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
        contextProperties.put("connectionfactory." + connectionFactory,
                              connectionString);
        contextProperties.put(this.type + "." + this.destinationName, this.destinationName);
        return contextProperties;
    }

    QueueConnectionFactory getConnectionFactory(ClientBuilder builder) throws NamingException {
        return (QueueConnectionFactory) builder.initialContext.lookup(
                ClientBuilder.connectionFactory);
    }

    TopicConnectionFactory getTopicConnectionFactory(ClientBuilder builder) throws NamingException {
        return (TopicConnectionFactory) builder.initialContext.lookup(
                ClientBuilder.connectionFactory);
    }

    Queue getDestination(ClientBuilder builder) throws NamingException {
        return (Queue) builder.initialContext.lookup(builder.destinationName);
    }

    Topic getTopicDestination(ClientBuilder builder) throws NamingException {
        return (Topic) builder.initialContext.lookup(builder.destinationName);
    }
}
