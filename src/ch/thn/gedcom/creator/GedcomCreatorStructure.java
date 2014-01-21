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

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;

import ch.thn.gedcom.GedcomFormatter;
import ch.thn.gedcom.data.GedcomError;
import ch.thn.gedcom.data.GedcomNode;
import ch.thn.gedcom.data.GedcomTagLine;
import ch.thn.gedcom.data.GedcomTree;
import ch.thn.gedcom.store.GedcomStore;

/**
 * @author Thomas Naeff (github.com/thnaeff)
 *
 */
public abstract class GedcomCreatorStructure {
	
	public static final int FALSE = -1;
	
	protected GedcomNode node = null;
	
	private HashMap<String, LinkedHashSet<GedcomNode>> nodesHash = null;
	private HashMap<String, LinkedList<GedcomNode>> nodesList = null;

	
	/**
	 * 
	 * 
	 * @param store
	 * @param structureName
	 * @param basePath
	 */
	public GedcomCreatorStructure(GedcomStore store, String structureName, String... basePath) {
		
		GedcomTree t = store.getGedcomTree(structureName);
		t.addMandatoryChildLines(true);
		
		node = t.followPath(basePath);
				
		nodesHash = new HashMap<String, LinkedHashSet<GedcomNode>>();
		nodesList = new HashMap<String, LinkedList<GedcomNode>>();
	}
	
	/**
	 * 
	 * 
	 * @param lines
	 * @return
	 */
	protected boolean addLines(Line[]... lines) {
		
		for (int i = 0; i < lines.length; i++) {
			if (lines[i] != null) {
				for (int j = 0; j < lines[i].length; j++) {
					if (!addLine(lines[i][j])) {
						return false;
					}
					
					setValue(lines[i][j]);
				}
			}
		}
		
		return true;
		
	}
	
	/**
	 * Adds all the given lines and also sets all the values 
	 * for the lines.
	 * 
	 * @param lines
	 * @return
	 */
	protected boolean addLines(Line... lines) {
		
		for (int i = 0; i < lines.length; i++) {
			if (!addLine(lines[i])) {
				return false;
			}
			
			setValue(lines[i]);
		}
		
		return true;
	}
	
	/**
	 * Adds only lines which have not been added yet and also sets all the values 
	 * for the lines.
	 * 
	 * @param lines
	 * @return
	 */
	protected boolean setLines(Line[]... lines) {
		
		for (int i = 0; i < lines.length; i++) {
			for (int j = 0; j < lines[i].length; j++) {
				if (!nodesHash.containsKey(lines[i][j].key) 
						|| !nodesHash.get(lines[i][j].key).contains(lines[i][j].node)) {
					if (!addLine(lines[i][j])) {
						return false;
					}
				}
				
				setValue(lines[i][j]);
			}
		}
		
		return true;
	}
	
	/**
	 * Adds only lines which have not been added yet and also sets all the values 
	 * for the lines.
	 * 
	 * @param lines
	 * @return
	 */
	protected boolean setLines(Line... lines) {
		
		for (int i = 0; i < lines.length; i++) {			
			if (!nodesHash.containsKey(lines[i].key) 
					|| !nodesHash.get(lines[i].key).contains(lines[i].node)) {
				if (!addLine(lines[i])) {
					return false;
				}
			}
			
			setValue(lines[i]);
		}
		
		return true;
	}
	
	/**
	 * 
	 * 
	 * @param line
	 */
	private void setValue(Line line) {
		if (line.node != null) {
			if (line instanceof ValueLine) {
				line.node.setTagLineValue(line.value);
			} else if (line instanceof XRefLine) {
				line.node.setTagLineXRef(line.xref);
			}
		}
	}
	
	/**
	 * 
	 * 
	 * @param line
	 * @return
	 */
	private boolean addLine(Line line) {
		if (line.node == null) {
			return true;
		}
		
		if (!nodesHash.containsKey(line.key)) {
			nodesHash.put(line.key, new LinkedHashSet<GedcomNode>());
			nodesList.put(line.key, new LinkedList<GedcomNode>());
		}
		
		nodesHash.get(line.key).add(line.node);
		nodesList.get(line.key).add(line.node);
		
		return true;
	}
	
	/**
	 * 
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	protected boolean setValue(String key, String value) {
		return setValue(key, 0, value);
	}
	
	/**
	 * 
	 * 
	 * @param key
	 * @param index
	 * @param value
	 */
	protected boolean setValue(String key, int index, String value) {
		if (nodesList.containsKey(key) && nodesList.get(key).size() > index && index >= 0) {
			nodesList.get(key).get(index).setTagLineValue(value);
			return true;
		}
		
		return false;
	}
	
	/**
	 * Tries to set all the given values. If there are more values than existing 
	 * lines, <code>false</code> is returned. If there are less values than 
	 * existing lines, the rest of the existing lines are not changed and 
	 * <code>true</code> is returned.
	 * 
	 * @param key
	 * @param index
	 * @param values
	 * @return
	 */
	protected boolean setValues(String key, int index, String... values) {
		if (nodesList.containsKey(key + index)) {
			
			for (int i = 0; i < values.length; i++) {
				if (i >= nodesList.get(key + index).size()) {
					//Can not set all the values
					return false;
				}
				
				nodesList.get(key + index).get(i).setTagLineValue(values[i]);
			}
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * 
	 * 
	 * @param key
	 * @param xref
	 * @return
	 */
	protected boolean setXRef(String key, String xref) {
		return setXRef(key, 0, xref);
	}
		
		
	/**
	 * 
	 * 
	 * @param key
	 * @param index
	 * @param xref
	 */
	protected boolean setXRef(String key, int index, String xref) {
		if (nodesList.containsKey(key) && nodesList.get(key).size() > index && index >= 0) {
			nodesList.get(key).get(index).setTagLineXRef(xref);
			return true;
		}
		
		return false;
	}
	
	/**
	 * Tries to set all the given xrefs. If there are more xrefs than existing 
	 * lines, <code>false</code> is returned. If there are less xrefs than 
	 * existing lines, the rest of the existing lines are not changed and 
	 * <code>true</code> is returned.
	 * 
	 * @param key
	 * @param index
	 * @param xrefs
	 * @return
	 */
	protected boolean setXRefs(String key, int index, String... xrefs) {
		if (nodesList.containsKey(key + index)) {
			
			for (int i = 0; i < xrefs.length; i++) {
				if (i >= nodesList.get(key + index).size()) {
					//Can not set all the xrefs
					return false;
				}
				
				nodesList.get(key + index).get(i).setTagLineXRef(xrefs[i]);
			}
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * 
	 * 
	 * @param key
	 * @param index
	 * @return
	 */
	protected String getValue(String key, int index) {
		if (!nodesList.containsKey(key) || nodesList.get(key).size() <= index || index < 0) {
			return null;
		}
		
		return nodesList.get(key).get(index).getTagLineValue();
	}
	
	/**
	 * 
	 * 
	 * @param key
	 * @param index
	 * @return
	 */
	protected String getXRef(String key, int index) {
		if (!nodesList.containsKey(key) || nodesList.get(key).size() <= index || index < 0) {
			return null;
		}
		
		return nodesList.get(key).get(index).getTagLineXRef();
	}
	
	/**
	 * 
	 * 
	 * @param key
	 * @param index
	 * @return
	 */
	protected GedcomTagLine getLine(String key, int index) {
		return nodesList.get(key).get(index).getNodeValue().getAsTagLine();
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public GedcomNode getNode() {
		return node;
	}
	
	/**
	 * Creates the path if it does not exist
	 * 
	 * @param path
	 * @return
	 */
	protected GedcomNode followPathCreate(String... path) {
		return followPathCreate(node, path);
	}
	
	/**
	 * Creates the path if it does not exist
	 * 
	 * @param block
	 * @param createNew
	 * @param path
	 * @return
	 */
	protected GedcomNode followPathCreate(GedcomNode o, String... path) {
		try {
			return o.followPathCreate(path);
		} catch (GedcomError e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Creates a new path if there is already such a path
	 * 
	 * @param path
	 * @return
	 */
	protected GedcomNode createPathEnd(String... path) {
		return createPathEnd(node, path);
	}
	
	/**
	 * Creates a new path if there is already such a path
	 * 
	 * @param block
	 * @param createNew
	 * @param path
	 * @return
	 */
	protected GedcomNode createPathEnd(GedcomNode o, String... path) {
		try {
			return o.createPathEnd(path);
		} catch (GedcomError e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * 
	 * 
	 * @param key
	 * @return
	 */
	protected int getNumberOfLines(String key) {
		if (!nodesHash.containsKey(key)) {
			return 0;
		}
		
		return nodesHash.get(key).size();
	}
	

	/**
	 * 
	 * 
	 * @param changeDate
	 * @return
	 */
	public boolean setChangeDate(Date changeDate) {
		if (setValue("CHANGE-DATE", GedcomFormatter.getDate(changeDate)) 
				&& setValue("CHANGE-TIME", GedcomFormatter.getTime(changeDate))) {
			return true;
		}
		
		try {
			Line line1 = new ValueLine(
					"CHANGE-DATE", 
					GedcomFormatter.getDate(changeDate), 
					followPathCreate("CHANGE_DATE", "CHAN", "DATE"));
			Line line2 = new ValueLine(
					"CHANGE-TIME", 
					GedcomFormatter.getTime(changeDate), 
					followPathCreate(line1.node, "TIME"));
			
			return setLines(line1, line2);
		} catch (GedcomError e) {
			throw e;
		}
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public String getChangeDate() {
		return getValue("CHANGE-DATE", 0);
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public String getChangeTime() {
		return getValue("CHANGE-TIME", 0);
	}
	
	/**
	 * 
	 * 
	 * @param note
	 * @return
	 */
	public boolean addNote(String note) {
		try {
			Line line1 = new ValueLine(
					"NOTE", 
					note, 
					createPathEnd("NOTE_STRUCTURE;NOTE;false;true", "NOTE"));
			
			return setLines(line1);
		} catch (GedcomError e) {
			throw e;
		}
	}
	
	/**
	 * 
	 * @param index
	 * @param note
	 * @return
	 */
	public boolean setNote(int index, String note) {
		if (setValue("NOTE", index, note)) {
			return true;
		}
		
		return addNote(note);
	}
	
	/**
	 * 
	 * 
	 * @param index
	 * @return
	 */
	public String getNote(int index) {
		return getValue("NOTE", index);
	}
	
	/**
	 * 
	 * 
	 * @param values
	 * @param key
	 * @param parentStructureCount
	 * @param path
	 * @return
	 */
	protected Line[] createMultiValueLine(String[] values, String key, int parentStructureCount, 
			String... path) {
		return createMultiValueLine(values, key, parentStructureCount, node, path);
	}
	
	/**
	 * 
	 * 
	 * @param values
	 * @param key
	 * @param parentStructureCount
	 * @param startObject
	 * @param path
	 * @return
	 */
	protected Line[] createMultiValueLine(String[] values, String key, int parentStructureCount, 
			GedcomNode startObject, String... path) {
		if (values == null) {
			return null;
		}
		
		Line[] lines = new Line[values.length];
		
		for (int i = 0; i < values.length; i++) {
			lines[i] = new ValueLine(
					key + parentStructureCount, 
					values[i], 
					createPathEnd(startObject, path));
		}
		
		return lines;
	}
	
	
	
	/*************************************************************************
	 * 
	 * 
	 * @author Thomas Naeff (github.com/thnaeff)
	 *
	 */
	public abstract class Line {
		
		protected String key = null;
		protected String value = null;
		protected String xref = null;
		protected GedcomNode node = null;
		
		/**
		 * 
		 * 
		 * @param key
		 * @param value
		 * @param xref
		 * @param node
		 */
		protected Line(String key, String value, String xref, GedcomNode node) {
			this.key = key;
			this.value = value;
			this.xref = xref;
			this.node = node;
			
		}
		

	}
	
	
	/*************************************************************************
	 * 
	 * 
	 * @author Thomas Naeff (github.com/thnaeff)
	 *
	 */
	public class ValueLine extends Line {

		/**
		 * 
		 * 
		 * @param key
		 * @param value
		 * @param node
		 */
		protected ValueLine(String key, String value, GedcomNode node) {
			super(key, value, null, node);
			
		}
		
	}
	
	
	/*************************************************************************
	 * 
	 * 
	 * @author Thomas Naeff (github.com/thnaeff)
	 *
	 */
	public class XRefLine extends Line {

		/**
		 * 
		 * 
		 * @param key
		 * @param xref
		 * @param node
		 */
		protected XRefLine(String key, String xref, GedcomNode node) {
			super(key, null, xref, node);
			
		}
		
		
	}
	
	
	/*************************************************************************
	 * 
	 * 
	 * @author Thomas Naeff (github.com/thnaeff)
	 *
	 */
	public class EmptyLine extends Line {

		/**
		 * 
		 * 
		 * @param key
		 * @param node
		 */
		protected EmptyLine(String key, GedcomNode node) {
			super(key, null, null, node);
			
		}
		
	}

}
