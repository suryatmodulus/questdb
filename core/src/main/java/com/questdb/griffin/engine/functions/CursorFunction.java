/*******************************************************************************
 *    ___                  _   ____  ____
 *   / _ \ _   _  ___  ___| |_|  _ \| __ )
 *  | | | | | | |/ _ \/ __| __| | | |  _ \
 *  | |_| | |_| |  __/\__ \ |_| |_| | |_) |
 *   \__\_\\__,_|\___||___/\__|____/|____/
 *
 * Copyright (C) 2014-2018 Appsicle
 *
 * This program is free software: you can redistribute it and/or  modify
 * it under the terms of the GNU Affero General Public License, version 3,
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 ******************************************************************************/

package com.questdb.griffin.engine.functions;

import com.questdb.cairo.AbstractRecordCursorFactory;
import com.questdb.cairo.sql.*;
import com.questdb.griffin.TypeEx;
import com.questdb.std.BinarySequence;
import com.questdb.std.str.CharSink;

public class CursorFunction implements Function {
    private final int position;
    private final GenericRecordCursorFactory factory;

    public CursorFunction(int position, RecordMetadata metadata, RecordCursor cursor) {
        this.position = position;
        this.factory = new GenericRecordCursorFactory(metadata, cursor);
    }

    @Override
    public final BinarySequence getBin(Record rec) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final boolean getBool(Record rec) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final byte getByte(Record rec) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final long getDate(Record rec) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final double getDouble(Record rec) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final float getFloat(Record rec) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final int getInt(Record rec) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final long getLong(Record rec) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final int getPosition() {
        return position;
    }

    @Override
    public final short getShort(Record rec) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final CharSequence getStr(Record rec) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final void getStr(Record rec, CharSink sink) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final CharSequence getStrB(Record rec) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final int getStrLen(Record rec) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final CharSequence getSymbol(Record rec) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final long getTimestamp(Record rec) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final int getType() {
        return TypeEx.CURSOR;
    }

    @Override
    public RecordMetadata getMetadata() {
        return factory.getMetadata();
    }

    @Override
    public RecordCursorFactory getRecordCursorFactory(Record record) {
        return factory;
    }

    private static class GenericRecordCursorFactory extends AbstractRecordCursorFactory {
        private final RecordCursor cursor;

        public GenericRecordCursorFactory(RecordMetadata metadata, RecordCursor cursor) {
            super(metadata);
            this.cursor = cursor;
        }

        @Override
        public RecordCursor getCursor() {
            cursor.toTop();
            return cursor;
        }
    }

}