/**
 * Copyright (c) 2009--2010 Red Hat, Inc.
 *
 * This software is licensed to you under the GNU General Public License,
 * version 2 (GPLv2). There is NO WARRANTY for this software, express or
 * implied, including the implied warranties of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. You should have received a copy of GPLv2
 * along with this software; if not, see
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.txt.
 *
 * Red Hat trademarks are not licensed under GPLv2. No permission is
 * granted to use or replicate Red Hat trademarks that are incorporated
 * in this software or its documentation.
 */
package com.redhat.rhn.frontend.xmlrpc.serializer;

import com.redhat.rhn.domain.config.ConfigRevision;
import com.redhat.rhn.frontend.xmlrpc.serializer.util.SerializerHelper;

import java.io.IOException;
import java.io.Writer;
import java.text.DecimalFormat;

import redstone.xmlrpc.XmlRpcCustomSerializer;
import redstone.xmlrpc.XmlRpcException;
import redstone.xmlrpc.XmlRpcSerializer;


/**
 * ConfigRevisionSerializer
 * @version $Rev$
 *
 * @xmlrpc.doc
 * #struct("Configuration Revision information")
 *   #prop("string", "type")
 *              #options()
 *                  #item("file")
 *                  #item("directory")
 *                  #item("symlink")
 *              #options_end()
 *   #prop_desc("string", "path","File Path")
 *   #prop_desc("string", "target_path","Symbolic link Target File Path.
 *                              Present for Symbolic links only.")
 *   #prop_desc("string", "channel","Channel Name")
 *   #prop_desc("string", "contents","File contents for text files only.")
 *   #prop_desc("int", "revision","File Revision")
 *   #prop_desc($date, "creation","Creation Date")
 *   #prop_desc($date, "modified","Last Modified Date")
 *   #prop_desc("string", "owner","File Owner. Present for files or directories only.")
 *   #prop_desc("string", "group","File Group. Present for files or directories only.")
 *   #prop_desc("int", "permissions","File Permissions (Deprecated).
 *                                  Present for files or directories only.")
 *   #prop_desc("string", "permissions_mode", "File Permissions.
 *                                      Present for files or directories only.")
 *   #prop_desc("string", "selinux_ctx", "SE inux Context.")
 *   #prop_desc("boolean", "binary", "true/false , Present for files only.")
 *   #prop_desc("string", "md5", "File's md5 signature. Present for files only.")
 *   #prop_desc("string", "macro-start-delimiter",
 *          "Macro start delimiter for a config file. Present for text files only.")
 *   #prop_desc("string", "macro-end-delimiter",
 *          "Macro end delimiter for a config file. Present for text files only.")
 * #struct_end()
 */
public class ConfigRevisionSerializer implements XmlRpcCustomSerializer {

    public static final String CONTENTS = "contents";
    public static final String PATH = "path";
    public static final String TARGET_PATH = "target_path";
    public static final String OWNER = "owner";
    public static final String GROUP = "group";
    public static final String SELINUX_CTX = "selinux_ctx";
    public static final String PERMISSIONS = "permissions";
    public static final String PERMISSIONS_MODE = "permissions_mode";
    public static final String MACRO_START = "macro-start-delimiter";
    public static final String MACRO_END = "macro-end-delimiter";
    public static final String BINARY = "binary";
    public static final String TYPE = "type";
    public static final String REVISION = "revision";


    /**
     * {@inheritDoc}
     */
    public Class getSupportedClass() {
        return ConfigRevision.class;
    }

    /**
     * {@inheritDoc}
     */
    public void serialize(Object value, Writer output, XmlRpcSerializer builtInSerializer)
        throws XmlRpcException, IOException {
        ConfigRevision rev = (ConfigRevision) value;
        SerializerHelper helper = new SerializerHelper(builtInSerializer);

        if (rev.getConfigFileType() != null) {
            helper.add(TYPE, rev.getConfigFileType().getLabel());
        }

        helper.add(PATH, rev.getConfigFile().getConfigFileName().getPath());
        helper.add(REVISION, rev.getRevision());
        helper.add("creation", rev.getCreated());
        helper.add("modified", rev.getModified());
        helper.add(SELINUX_CTX, rev.getConfigInfo().getSelinuxCtx());
        if (!rev.isSymlink()) {
            helper.add(OWNER, rev.getConfigInfo().getUsername());
            helper.add(GROUP, rev.getConfigInfo().getGroupname());
            helper.add(PERMISSIONS, rev.getConfigInfo().getFilemode());
            helper.add(PERMISSIONS_MODE, new DecimalFormat("000").format(
                rev.getConfigInfo().getFilemode().longValue()));

        }

        if (rev.isFile()) {
            if (!rev.getConfigContent().isBinary()) {
                helper.add(BINARY, Boolean.FALSE);
                helper.add(CONTENTS, rev.getConfigContent().getContentsString());
            }
            else {
                helper.add(BINARY, Boolean.TRUE);
                helper.add(MACRO_START, rev.getConfigContent().getDelimStart());
                helper.add(MACRO_END, rev.getConfigContent().getDelimEnd());
            }
            helper.add("md5", rev.getConfigContent().getChecksum().getChecksum());
        }
        else if (rev.isSymlink()) {
            helper.add(TARGET_PATH, rev.getConfigInfo().getTargetFileName().getPath());
        }
        helper.add("channel", rev.getConfigFile().getConfigChannel().getName());
        helper.writeTo(output);
    }

}
