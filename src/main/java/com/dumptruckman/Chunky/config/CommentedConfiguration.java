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

import org.bukkit.util.config.Configuration;

import java.io.*;
import java.util.HashMap;

/**
 * @author dumptruckman
 */
public class CommentedConfiguration extends Configuration {

    private HashMap<String, String> comments;
    private File file;

    public CommentedConfiguration(File file) {
        super(file);
        comments = new HashMap<String, String>();
        this.file = file;
    }

    @Override
    public boolean save() {
        // Save the config just like normal
        boolean saved = super.save();

        // if there's comments to add and it saved fine, we need to add comments
        if (!comments.isEmpty() && saved) {
            // String array of each line in the config file
            String[] yamlContents =
                    convertFileToString(file).split("[" + System.getProperty("line.separator") + "]");

            // This will hold the newly formatted line
            String newContents = "";
            // This holds the current path the lines are at in the config
            String currentPath = "";
            // This tells if the specified path has already been commented
            boolean commentedPath = false;
            // The depth of the path. (number of words separated by periods - 1)
            int depth = 0;

            // Loop through the config lines
            for (String line : yamlContents) {
                // If the line is a node (and not something like a list value)
                if (line.contains(": ") || (line.length() > 1 && line.charAt(line.length() - 1) == ':')) {
                    // Grab the index of the end of the node name
                    int index = 0;
                    index = line.indexOf(": ");
                    if (index < 0) {
                        index = line.length() - 1;
                    }
                    // If currentPath is empty, store the node name as the currentPath. (this is only on the first iteration, i think)
                    if (currentPath.isEmpty()) {
                        currentPath = line.substring(0, index);
                    } else {
                        // Calculate the whitespace preceding the node name
                        int whiteSpace = 0;
                        for (int n = 0; n < line.length(); n++) {
                            if (line.charAt(n) == ' ') {
                                whiteSpace++;
                            } else {
                                break;
                            }
                        }
                        // Find out if the current depth (whitespace * 4) is greater/lesser/equal to the previous depth
                        if (whiteSpace / 4 > depth) {
                            // Path is deeper.  Add a . and the node name
                            currentPath += "." + line.substring(whiteSpace, index);
                            depth++;
                        } else if (whiteSpace / 4 < depth) {
                            // Path is shallower, calculate current depth from whitespace (whitespace / 4) and subtract that many levels from the currentPath
                            int newDepth = whiteSpace / 4;
                            for (int i = 0; i < depth - newDepth; i++) {
                                currentPath = currentPath.replace(currentPath.substring(currentPath.lastIndexOf(".")), "");
                            }
                            // Grab the index of the final period
                            int lastIndex = currentPath.lastIndexOf(".");
                            if (lastIndex < 0) {
                                // if there isn't a final period, set the current path to nothing because we're at root
                                currentPath = "";
                            } else {
                                // If there is a final period, replace everything after it with nothing
                                currentPath = currentPath.replace(currentPath.substring(currentPath.lastIndexOf(".")), "");
                                currentPath += ".";
                            }
                            // Add the new node name to the path
                            currentPath += line.substring(whiteSpace, index);
                            // Reset the depth
                            depth = newDepth;
                        } else {
                            // Path is same depth, replace the last path node name to the current node name
                            int lastIndex = currentPath.lastIndexOf(".");
                            if (lastIndex < 0) {
                                // if there isn't a final period, set the current path to nothing because we're at root
                                currentPath = "";
                            } else {
                                // If there is a final period, replace everything after it with nothing
                                currentPath = currentPath.replace(currentPath.substring(currentPath.lastIndexOf(".")), "");
                                currentPath += ".";
                            }
                            //currentPath = currentPath.replace(currentPath.substring(currentPath.lastIndexOf(".")), "");
                            currentPath += line.substring(whiteSpace, index);

                        }
                        // This is a new node so we need to mark it for commenting (if there are comments)
                        commentedPath = false;
                    }
                }

                String comment = "";
                if (!commentedPath) {
                    // If there's a comment for the current path, retrieve it and flag that path as already commented
                    comment = comments.get(currentPath);
                    commentedPath = true;
                }
                if (comment != null) {
                    // Add the comment to the beginning of the current line
                    line = comment + System.getProperty("line.separator") + line;
                }
                if (comment != null || (line.length() > 1 && line.charAt(line.length() - 1) == ':')) {
                    // Add the (modified) line to the total config String
                    // This modified version will not write the config if a comment is not present
                    newContents += line + System.getProperty("line.separator");
                }
            }
            try {
                // Write the string to the config file
                stringToFile(newContents, file);
            } catch (IOException e) {
                saved = false;
            }
        }

        return saved;
    }

    /**
     * Adds a comment just before the specified path.  The comment can be multiple lines.  An empty string will indicate a blank line.
     * @param path Configuration path to add comment.
     * @param commentLines Comments to add.  One String per line.
     */
    public void addComment(String path, String...commentLines) {
        StringBuilder commentstring = new StringBuilder();
        String leadingSpaces = "";
        for (int n = 0; n < path.length(); n++) {
            if (path.charAt(n) == '.') {
                leadingSpaces += "    ";
            }
        }
        for (String line : commentLines) {
            if (!line.isEmpty()) {
                line = leadingSpaces + line;
            } else {
                line = " ";
            }
            if (commentstring.length() > 0) {
                commentstring.append("\r\n");
            }
            commentstring.append(line);
        }
        comments.put(path, commentstring.toString());
    }

    /**
     * Pass a file and it will return it's contents as a string.
     * @param file File to read.
     * @return Contents of file.  String will be empty in case of any errors.
     */
    private String convertFileToString(File file) {
        if (file != null && file.exists() && file.canRead() && !file.isDirectory()) {
            Writer writer = new StringWriter();
            InputStream is = null;

            char[] buffer = new char[1024];
            try {
                is = new FileInputStream(file);
                Reader reader = new BufferedReader(
                        new InputStreamReader(is, "UTF-8"));
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            } catch (IOException e) {
                System.out.println("Exception ");
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException ignore) {}
                }
            }
            return writer.toString();
        } else {
            return "";
        }
    }

    /**
     * Writes the contents of a string to a file.
     * @param source String to write.
     * @param file File to write to.
     * @return True on success.
     * @throws IOException
     */
    private boolean stringToFile(String source, File file) throws IOException {
        try {
            OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(file),"UTF-8");

            source.replaceAll("\n", System.getProperty("line.separator"));

            out.write(source);
            out.close();
            return true;
        } catch (IOException e) {
            System.out.println("Exception ");
            return false;
        }
    }
}
