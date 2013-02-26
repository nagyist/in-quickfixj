/*******************************************************************************
 * Copyright (c) quickfixengine.org  All rights reserved.
 *
 * This file is part of the QuickFIX FIX Engine
 *
 * This file may be distributed under the terms of the quickfixengine.org
 * license as defined by quickfixengine.org and appearing in the file
 * LICENSE included in the packaging of this file.
 *
 * This file is provided AS IS with NO WARRANTY OF ANY KIND, INCLUDING
 * THE WARRANTY OF DESIGN, MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE.
 *
 * See http://www.quickfixengine.org/LICENSE for licensing information.
 *
 * Contact ask@quickfixengine.org if any conditions of this licensing
 * are not clear to you.
 ******************************************************************************/

package quickfix.netty;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import quickfix.Session;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 *
 */
public class FIXSocketInitiator extends AbstractTransportSupport {
    private static Logger LOGGER =
        LoggerFactory.getLogger(FIXSocketInitiator.class);

    private String m_host;
    private int m_port;

    /**
     * c-tor
     *
     * @param runtime
     * @param session
     * @param host
     * @param port
     */
    public FIXSocketInitiator(FIXRuntime runtime,Session session,String host, int port) {
        super(runtime,session);
        m_host = host;
        m_port = port;
    }

    /**
     *
     */
    public void stop() {
        setRunning(false);
        disconnect();
    }

    /**
     *
     */
    @Override
    public void run() {
        ChannelFactory factory =
            new NioClientSocketChannelFactory(
                Executors.newCachedThreadPool(),
                Executors.newCachedThreadPool());

        ClientBootstrap bootstrap = new ClientBootstrap(factory);
        bootstrap.setPipelineFactory(new FIXProtocolPipelineFactory(getRuntime(),getSession(),FIXSessionType.INITIATOR));
        bootstrap.setOption("tcpNoDelay", true);
        bootstrap.setOption("keepAlive", true);

        ChannelFuture future  = bootstrap.connect(new InetSocketAddress(m_host,m_port));
        Channel       channel = future.awaitUninterruptibly().getChannel();

        setRunning(true);

        if (!future.isSuccess()) {
            LOGGER.warn("Error", future.getCause());
        } else {
            try {
                setChanngel(channel);
                setRunning(true);
                while(isRunning()) {
                    try{ Thread.sleep(5000); } catch(Exception e) {}
                }
            } catch(Exception e) {
                LOGGER.warn("Error", e);
            }

            disconnect();
        }

        bootstrap.releaseExternalResources();
    }
}
