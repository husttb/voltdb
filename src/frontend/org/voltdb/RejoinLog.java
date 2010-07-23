/* This file is part of VoltDB.
 * Copyright (C) 2008-2010 VoltDB L.L.C.
 *
 * VoltDB is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * VoltDB is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with VoltDB.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.voltdb;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * This file isn't long for this world. It's just something I've been using
 * to debug multi-process rejoin stuff.
 *
 */
public class RejoinLog {
    static File m_logfile = new File("rejoinlog.txt");

    public synchronized static void setPortNo(int portNo) {
        m_logfile = new File(String.format("rejoinlog-%d.txt", portNo));
    }

    public synchronized static void log(String str) {
        try {
            FileWriter log = new FileWriter(m_logfile, true);
            log.write(str + "\n");
            log.flush();
            log.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void log(String format, Object... args) {
        log(String.format(format, args));
    }
}
