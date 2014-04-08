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
package ch.thn.gedcom.creator;

import java.util.Arrays;

import ch.thn.gedcom.data.GedcomNode;
import ch.thn.gedcom.data.GedcomNode.PathStepPieces;
import ch.thn.gedcom.data.GedcomTree;
import ch.thn.gedcom.store.GedcomStore;
import ch.thn.gedcom.store.GedcomStoreStructure;

/**
 * @author Thomas Naeff (github.com/thnaeff)
 *
 */
public abstract class GedcomCreatorStructure {
		
	private GedcomTree tree = null;
	protected GedcomNode baseNode = null;

	private boolean v55 = false;
	
	/**
	 * 
	 * 
	 * @param store
	 * @param structureName
	 * @param basePath
	 */
	public GedcomCreatorStructure(GedcomStore store, String structureName, String... basePath) {
		
		v55 = store.getFileVersion().equals("5.5");
		
		tree = store.getGedcomTree(structureName);
		//Do not add mandatory lines. Just create them when needed
		//t.addMandatoryChildLines(true);
		
		baseNode = tree.followPathCreate(basePath);
	}
	
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
	 * @param store
	 * @param structureName
	 * @param baseNode
	 */
	public GedcomCreatorStructure(GedcomStore store, String structureName, 
			GedcomNode baseNode, String... basePath) {
		this.baseNode = baseNode.followPathCreate(basePath);
		
		GedcomStoreStructure storeStructure = null;
		
		if (baseNode.getStoreBlock() != null) {
			storeStructure = baseNode.getStoreBlock().getStoreStructure();
		} else {
			storeStructure = baseNode.getStoreLine().getStoreStructure();
		}
		
		if (!storeStructure.getStructureName().equals(structureName)) {
			throw new GedcomCreatorError("Invalid creation of " + getClass().getSimpleName() + ". " + 
					"The " + baseNode.getClass().getSimpleName() + 
					" has to match the given structure name " + structureName);
		}
		
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public GedcomTree getTree() {
		return tree;
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
	 * removed. In addition, any unused and empty parent nodes are removed too.
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
