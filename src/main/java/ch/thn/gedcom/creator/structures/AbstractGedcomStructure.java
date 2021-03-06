/**
 *    Copyright 2013 Thomas Naeff (github.com/thnaeff)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package ch.thn.gedcom.creator.structures;

import java.util.Arrays;

import ch.thn.datatree.TreeIterator;
import ch.thn.gedcom.creator.GedcomCreatorError;
import ch.thn.gedcom.data.GedcomLine;
import ch.thn.gedcom.data.GedcomNode;
import ch.thn.gedcom.data.GedcomNode.PathStepPieces;
import ch.thn.gedcom.data.GedcomTagLine;
import ch.thn.gedcom.data.GedcomTree;
import ch.thn.gedcom.store.GedcomStore;

/**
 * @author Thomas Naeff (github.com/thnaeff)
 *
 */
public abstract class AbstractGedcomStructure {

  public static final String END_OF_FILE = "END_OF_FILE";
  public static final String FAM_RECORD = "FAM_RECORD";
  public static final String HEADER = "HEADER";
  public static final String INDIVIDUAL_RECORD = "INDIVIDUAL_RECORD";
  public static final String SUBMITTER_RECORD = "SUBMITTER_RECORD";

  private GedcomNode treeHead = null;
  protected GedcomNode baseNode = null;

  private boolean v55 = false;

  /**
   * Creates a new gedcom node from the store using the given structure name
   *
   * @param store
   * @param structureName
   * @param basePath
   */
  public AbstractGedcomStructure(GedcomStore store, String structureName, String... basePath) {

    v55 = store.getFileVersion().equals("5.5");

    treeHead = store.getGedcomTree(structureName);
    //Do not add mandatory lines. Just create them when needed
    //t.addMandatoryChildLines(true);

    baseNode = treeHead.followPathCreate(basePath);
  }

  /**
   * Uses the given gedcom base node and verifies it if it matches the given
   * structure name
   *
   * @param store
   * @param structureName
   * @param gedcomHeadNode The head node of a gedcom tree is a {@link GedcomTree}
   * object
   * @param basePath
   */
  public AbstractGedcomStructure(GedcomStore store, String structureName,
      GedcomTree gedcomHeadNode, String... basePath) {
    this.baseNode = gedcomHeadNode.followPathCreate(basePath);

    String structureName2 = gedcomHeadNode.getStoreBlock().getStoreStructure().getStructureName();

    if (!structureName2.equals(structureName)) {
      throw new GedcomCreatorError("Invalid creation of " + getClass().getSimpleName() + ". " +
          "The " + gedcomHeadNode.getClass().getSimpleName() + " '" + structureName2 +
          "' does not match the given structure name '" + structureName + "'");
    }

    treeHead = gedcomHeadNode.getHeadNode();

  }

  /**
   * Returns the structure name which matches the used structure
   *
   * @return
   */
  public abstract String getStructureName();

  /**
   * Returns <code>true</code> if the loaded gedcom grammar file is version 5.5
   *
   * @return
   */
  protected boolean isV55() {
    return v55;
  }

  /**
   *
   *
   * @return
   */
  public GedcomNode getTree() {
    return treeHead;
  }

  /**
   *
   *
   * @return
   */
  protected GedcomNode getBaseNode() {
    return baseNode;
  }

  /**
   *
   *
   * @param path
   * @return
   */
  protected GedcomNode followPath(String... path) {
    return followPath(baseNode, path);
  }

  /**
   *
   *
   * @param node
   * @param path
   * @return
   */
  protected GedcomNode followPath(GedcomNode node, String... path) {
    return node.followPath(path);
  }

  /**
   * Retrieves the value at the given path, starting at the given node.
   *
   * @param path
   * @return
   */
  protected String getValue(String... path) {
    return getValue(baseNode, path);
  }

  /**
   * Retrieves the value at the given path, starting at the given node.
   *
   * @param node
   * @param path
   * @return
   */
  protected String getValue(GedcomNode node, String... path) {
    GedcomNode n = node.followPath(path);

    if (n == null) {
      return null;
    }

    return n.getTagLineValue();
  }

  /**
   * Retrieves the xref at the given path, starting at the base node.
   *
   * @param path
   * @return
   */
  protected String getXRef(String... path) {
    return getXRef(baseNode, path);
  }

  /**
   * Retrieves the xref at the given path, starting at the given node.
   *
   * @param node
   * @param path
   * @return
   */
  protected String getXRef(GedcomNode node, String... path) {
    GedcomNode n = node.followPath(path);

    if (n == null) {
      return null;
    }

    return n.getTagLineXRef();
  }


  /**
   * Creates all the paths and, if successful, sets all the values/xrefs. If one
   * path can not be created, it returns <code>false</code> and no values/xrefs
   * are set (the paths are still created in the order of the given data array
   * until the first path creation fails).
   *
   * @param data
   * @return <code>true</code> if all the paths have been created successfully and
   * the values/xrefs have been set. <code>false</code> if creating a path failed
   */
  protected boolean createAndSet(GedcomData... data) {

    //Try to create all the paths
    for (int i = 0; i < data.length; i++) {
      GedcomData d = data[i];

      if (d == null) {
        //Skip null data
        continue;
      }

      GedcomNode node = baseNode;

      if (d.followPrevious != null) {
        if (d.followPrevious.followedNode == null) {
          throw new GedcomCreatorError("Can not follow previous data " +
              Arrays.toString(d.followPrevious.path) + " with continuing path " +
              Arrays.toString(d.path) + ". Previous data has not been " +
              "processed yet or processing failed.");
        }

        node = d.followPrevious.followedNode;
      }

      if (d.add) {
        //Add
        d.followedNode = node.createPathEnd(d.path);
      } else {
        //Set
        d.followedNode = node.followPathCreate(d.path);
      }

      if (d.followedNode == null) {
        return false;
      }

    }

    //Set the values since all the paths have been created
    for (int i = 0; i < data.length; i++) {
      GedcomData d = data[i];

      if (d == null) {
        //Skip null data
        continue;
      }

      if (d.followedNode != null) {
        if (d instanceof GedcomValue) {
          d.followedNode.setTagLineValue(d.value);
        } else if (d instanceof GedcomXRef) {
          d.followedNode.setTagLineXRef(d.xref);
        }
        //Nothing to set for structures
      }
    }

    return true;
  }

  /**
   * Removes the line at the end of the given path. If one
   * path can not be removed, it returns <code>false</code> and no paths are
   * removed.
   *
   * @param path
   * @return
   */
  protected boolean remove(String... path) {
    return remove(baseNode, path);
  }

  /**
   * Removes the line at the end of the given path. If one
   * path can not be removed, it returns <code>false</code> and no paths are
   * removed.
   *
   * @param node
   * @param path
   * @return
   */
  protected boolean remove(GedcomNode node, String... path) {
    node = node.removePath(path);

    if (node == null) {
      return false;
    }

    return true;
  }

  /**
   * Returns the index of the given value among the child lines with the given
   * tag. Only tag lines are considered.
   *
   * @param value The value to look for
   * @param tag The lines to look for the values have to be of the tag line
   * with this tag
   * @param path The path to look for the child value
   * @return The index if the value has been found, or <code>-1</code> if the
   * value has not been found
   */
  protected int indexOfChildValue(String value, String tag, String... path) {
    return indexOfChildValue(baseNode, value, tag, 0, path);
  }

  /**
   * Returns the index of the given value among the child lines with the given
   * tag. Only tag lines are considered.
   *
   * @param node The node to follow the path from
   * @param value The value to look for
   * @param tag The lines to look for the values have to be of the tag line
   * with this tag
   * @param offset The offset of the line to look for the value. Since it uses
   * a iterator for searching, setting an offset still starts at the head of the
   * sub tree but skips all occurrences until the index is greater or equal the offset.
   * @param path The path to look for the child value
   * @return The index if the value has been found, or <code>-1</code> if the
   * value has not been found
   */
  protected int indexOfChildValue(GedcomNode node, String value, String tag,
      int offset, String... path) {
    GedcomNode n = node.followPath(path);

    //Use the iterator for the sub-tree of the current node since values could
    //be nested under some structure lines. For example:
    //├─ SPOUSE_TO_FAMILY_LINK
    //│  └─ FAMS @spouse1@
    //├─ SPOUSE_TO_FAMILY_LINK
    //│  └─ FAMS @spouse2@
    TreeIterator<GedcomNode> iterator = n.iterator(true);
    int index = 0;
    while (iterator.hasNext()) {
      GedcomLine line = iterator.next().getNodeValue();
      if (line.isTagLine()) {
        GedcomTagLine tagLine = line.getAsTagLine();

        //Only count the tag lines with the given tag
        if (tagLine.getTag().equals(tag)) {
          if (offset <= index && tagLine.getValue().equals(value)) {
            return index;
          }

          index++;
        }
      }
    }

    return -1;
  }

  /**
   * Returns the index of the given XRef among the child lines with the given
   * tag. Only tag lines are considered.
   *
   * @param xref The XRef to look for
   * @param tag The lines to look for the XRef have to be of the tag line
   * with this tag
   * @param path The path to look for the child XRef
   * @return The index if the XRef has been found, or <code>-1</code> if the
   * XRef has not been found
   */
  protected int indexOfChildXRef(String xref, String tag, String... path) {
    return indexOfChildXRef(baseNode, xref, tag, 0, path);
  }

  /**
   * Returns the index of the given XRef among the child lines with the given
   * tag. Only tag lines are considered.
   *
   * @param node The node to follow the path from
   * @param xref The XRef to look for
   * @param tag The lines to look for the XRef have to be of the tag line
   * with this tag
   * @param offset The offset of the line to look for the XRef. Since it uses
   * a iterator for searching, setting an offset still starts at the head of the
   * sub tree but skips all occurrences until the index is greater or equal the offset.
   * @param path The path to look for the child XRef
   * @return The index if the XRef has been found, or <code>-1</code> if the
   * XRef has not been found
   */
  protected int indexOfChildXRef(GedcomNode node, String xref, String tag,
      int offset, String... path) {
    GedcomNode n = node.followPath(path);

    //Use the iterator for the sub-tree of the current node since xrefs could
    //be nested under some structure lines. For example:
    //├─ SPOUSE_TO_FAMILY_LINK
    //│  └─ FAMS @spouse1@
    //├─ SPOUSE_TO_FAMILY_LINK
    //│  └─ FAMS @spouse2@
    TreeIterator<GedcomNode> iterator = n.iterator(true);
    int index = 0;
    while (iterator.hasNext()) {
      GedcomLine line = iterator.next().getNodeValue();
      if (line.isTagLine()) {
        GedcomTagLine tagLine = line.getAsTagLine();

        //Only count the tag lines with the given tag
        if (tagLine.getTag().equals(tag)) {
          if (offset <= index && tagLine.getXRef().equals(xref)) {
            return index;
          }

          index++;
        }

      }
    }

    return -1;
  }

  /**
   *
   *
   * @param changeDate
   * @param changeTime
   * @return
   */
  public boolean setChangeDate(String changeDate, String changeTime) {
    GedcomValue date = new GedcomValue(false, changeDate,
        "CHANGE_DATE", "CHAN", "DATE");

    GedcomValue time = new GedcomValue(false, changeTime, date,
        "TIME");

    return createAndSet(date, time);
  }

  /**
   *
   *
   * @return
   */
  public String getChangeDate() {
    return getValue("CHANGE_DATE", "CHAN", "DATE");
  }

  /**
   *
   *
   * @return
   */
  public boolean removeChangeDate() {
    return remove("CHANGE_DATE", "CHAN", "DATE");
  }

  /**
   *
   *
   * @return
   */
  public String getChangeTime() {
    return getValue("CHANGE_DATE", "CHAN", "DATE", "TIME");
  }

  /**
   *
   *
   * @return
   */
  public boolean removeChangeTime() {
    return remove("CHANGE_DATE", "CHAN", "DATE", "TIME");
  }

  /**
   *
   *
   * @param note
   * @return
   */
  public boolean addNote(String note) {
    return createAndSet(new GedcomValue(true, note,
        "NOTE_STRUCTURE;NOTE;false;true", "NOTE"));
  }

  /**
   *
   *
   * @param index
   * @param note
   * @return
   */
  public boolean setNote(int index, String note) {
    return createAndSet(new GedcomValue(false, note,
        "NOTE_STRUCTURE;NOTE;false;true" + GedcomNode.PATH_OPTION_DELIMITER + index, "NOTE"));
  }

  /**
   *
   *
   * @param index
   * @return
   */
  public String getNote(int index) {
    return getValue("NOTE_STRUCTURE;NOTE;false;true" + GedcomNode.PATH_OPTION_DELIMITER + index, "NOTE");
  }

  /**
   *
   *
   * @param index
   * @return
   */
  public boolean removeNote(int index) {
    return remove("NOTE_STRUCTURE;NOTE;false;true" + GedcomNode.PATH_OPTION_DELIMITER + index, "NOTE");
  }

  /**
   *
   *
   * @return
   */
  public int getNumberOfNotes() {
    return getNumberOfLines("NOTE_STRUCTURE");
  }

  /**
   * Returns the number of lines which exist at the given path. This method
   * follows the given path until just before the last path part and then counts
   * the number of child nodes which have the last path part (tag or structure
   * name and variation).
   *
   * @param keyCountPath
   * @return
   */
  protected int getNumberOfLines(String... keyCountPath) {
    return getNumberOfLines(baseNode, keyCountPath);
  }
  /**
   * Returns the number of lines which exist at the given path. This method
   * follows the given path until just before the last path part and then counts
   * the number of child nodes which have the last path part (tag or structure
   * name and variation).
   *
   * @param n
   * @param keyCountPath
   * @return
   */
  protected int getNumberOfLines(GedcomNode n, String... keyCountPath) {
    GedcomNode node = null;

    if (keyCountPath.length > 1) {
      String[] path1 = new String[keyCountPath.length - 1];
      System.arraycopy(keyCountPath, 0, path1, 0, keyCountPath.length - 2);

      node = n.followPath(path1);
    } else if (keyCountPath.length == 1) {
      node = this.baseNode;
    }

    if (node == null) {
      return 0;
    } else {
      PathStepPieces p = node.new PathStepPieces();
      p.parse(keyCountPath[keyCountPath.length - 1]);
      if (p.tag == null) {
        return node.getNumberOfChildLines(p.tagOrStructureName);
      } else if (p.lookForXRefAndValueVariation) {
        return node.getNumberOfChildLines(p.tagOrStructureName, p.tag, p.withXRef, p.withValue);
      } else {
        return node.getNumberOfChildLines(p.tagOrStructureName, p.tag);
      }
    }
  }



  /*************************************************************************
   *
   *
   * @author Thomas Naeff (github.com/thnaeff)
   *
   */
  public abstract class GedcomData {

    private boolean add = false;

    private String value = null;
    private String xref = null;
    private String[] path = null;

    private GedcomData followPrevious = null;

    private GedcomNode followedNode = null;

    /**
     *
     * @param add
     * @param value
     * @param xref
     * @param path
     */
    protected GedcomData(boolean add, String value, String xref,
        GedcomData followPrevious, String... path) {
      this.add = add;
      this.value = value;
      this.xref = xref;
      this.followPrevious = followPrevious;
      this.path = path;
    }


  }


  /*************************************************************************
   *
   *
   * @author Thomas Naeff (github.com/thnaeff)
   *
   */
  public class GedcomValue extends GedcomData {

    /**
     *
     *
     * @param add
     * @param value
     * @param path
     */
    protected GedcomValue(boolean add, String value, String... path) {
      super(add, value, null, null, path);
    }

    /**
     *
     *
     * @param add
     * @param value
     * @param followPrevious
     * @param path
     */
    protected GedcomValue(boolean add, String value, GedcomData followPrevious, String... path) {
      super(add, value, null, followPrevious, path);
    }


  }


  /*************************************************************************
   *
   *
   * @author Thomas Naeff (github.com/thnaeff)
   *
   */
  public class GedcomXRef extends GedcomData {

    /**
     *
     *
     * @param add
     * @param xref
     * @param path
     */
    protected GedcomXRef(boolean add, String xref, String... path) {
      super(add, null, xref, null, path);

    }

    /**
     *
     *
     * @param add
     * @param xref
     * @param followPrevious
     * @param path
     */
    protected GedcomXRef(boolean add, String xref, GedcomData followPrevious, String... path) {
      super(add, null, xref, followPrevious, path);

    }


  }


  /*************************************************************************
   *
   *
   * @author Thomas Naeff (github.com/thnaeff)
   *
   */
  public class GedcomDataEmpty extends GedcomData {

    /**
     *
     *
     * @param add
     * @param path
     */
    protected GedcomDataEmpty(boolean add, String... path) {
      super(add, null, null, null, path);

    }

    /**
     *
     *
     * @param add
     * @param followPrevious
     * @param path
     */
    protected GedcomDataEmpty(boolean add, GedcomData followPrevious, String... path) {
      super(add, null, null, followPrevious, path);

    }


  }


}
