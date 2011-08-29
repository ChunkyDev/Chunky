/*
 * Copyright (c) 2011. dumptruckman
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the Software
 * without restriction, including without limitation the rights to use, copy, modify,
 * merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies
 * or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.dumptruckman.chunky.config;

/**
 * @author dumptruckman
 */
public enum ConfigPath {
    LANGUAGE("settings.language_file", "english.yml", "# This is the language file you wish to use."),
    DATA_SAVE_PERIOD("settings.data.save_every", 30, "# This is often plugin data is written to the disk."),
    ;

    private String path;
    private Object def;
    private String[] comments;

    ConfigPath(String path, Object def, String...comments) {
        this.path = path;
        this.def = def;
        this.comments = comments;
    }

    /**
     * Retrieves the path for a config option
     * @return The path for a config option
     */
    public String getPath() {
        return path;
    }

    /**
     * Retrieves the default value for a config path
     * @return The default value for a config path
     */
    public Object getDefault() {
        return def;
    }

    /**
     * Retrieves the comment for a config path
     * @return The comments for a config path
     */
    public String[] getComments() {
        if (comments != null) {
            return comments;
        }

        String[] comments = new String[1];
        comments[0] = "";
        return comments;
    }
}
