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


package quickfix;

import quickfix.ext.IFIXContext;

import java.io.IOException;

/**
 * Creates a message store that stores all data in memory.
 * 
 * @see MessageStore
 */
public class MemoryStoreFactory implements MessageStoreFactory {
    private final IFIXContext context;

    /**
     * c-tor
     *
     * @param context
     */
    public MemoryStoreFactory(IFIXContext context) {
        this.context = context;
    }

    @Override
	public MessageStore create(SessionID sessionID) {
		try {
            return new MemoryStore(context);
        } catch (IOException e) {
            throw new RuntimeError(e);
        }
	}
}
