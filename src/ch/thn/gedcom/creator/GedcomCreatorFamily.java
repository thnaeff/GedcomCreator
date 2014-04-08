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

import ch.thn.gedcom.creator.GedcomCreatorEnums.YesNo;
import ch.thn.gedcom.data.GedcomNode;
import ch.thn.gedcom.store.GedcomStore;

/**
 * @author Thomas Naeff (github.com/thnaeff)
 *
 */
public class GedcomCreatorFamily extends GedcomCreatorStructure {

	
	/**
	 * A FAM_RECORD
	 * 
	 * @param store
	 * @param id
	 */
	public GedcomCreatorFamily(GedcomStore store, String id) {
		super(store, "FAM_RECORD", "FAM");
		
		if (!setId(id)) {
			throw new GedcomCreatorError("Failed to create family with ID " + 
					id + ". Id could not be set.");
		}		
	}
	
	/**
	 * 
	 * 
	 * @param store
	 * @param node
	 */
	public GedcomCreatorFamily(GedcomStore store, GedcomNode node) {
		super(store, "FAM_RECORD", node, "FAM");
	}
	
	/**
	 * 
	 * 
	 * @param id
	 * @return
	 * @throws GedcomCreatorError
	 */
	public boolean setId(String id) {
		if (id == null || id.length() == 0) {
			throw new GedcomCreatorError("Setting an empty ID is not allowed");
		}
		
		return createAndSet(new GedcomXRef(false, id));
	}
	
	/**
	 * 
	 * 
	 * @param husbandId
	 * @return
	 */
	public boolean setHusbandLink(String husbandId) {
		return createAndSet(new GedcomXRef(false, husbandId, 
				"HUSB"));
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public String getHusbandLink() {
		return getXRef("HUSB");
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public boolean removeHusbandLink() {
		return remove("HUSB");
	}
	
	/**
	 * 
	 * 
	 * @param wifeId
	 * @return
	 */
	public boolean setWifeLink(String wifeId) {
		return createAndSet(new GedcomXRef(false, wifeId, 
				"WIFE"));
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public String getWifeLink() {
		return getXRef("WIFE");
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public boolean removeWifeLink() {
		return remove("WIFE");
	}
	
	/**
	 * 
	 * 
	 * @param childId
	 * @return
	 */
	public boolean addChildLink(String childId) {
		return createAndSet(new GedcomXRef(true, childId, 
				"CHIL"));
	}
	
	/**
	 * 
	 * 
	 * @param childId
	 * @return
	 */
	public boolean setChildLink(int index, String childId) {
		return createAndSet(new GedcomXRef(false, childId, 
				"CHIL" + GedcomNode.PATH_OPTION_DELIMITER + index));
	}
	
	/**
	 * 
	 * 
	 * @param index
	 * @return
	 */
	public String getChildLink(int index) {
		return getXRef("CHIL" + GedcomNode.PATH_OPTION_DELIMITER + index);
	}
	
	/**
	 * 
	 * 
	 * @param index
	 * @return
	 */
	public boolean removeChildLink(int index) {
		return remove("CHIL" + GedcomNode.PATH_OPTION_DELIMITER + index);
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public int getNumberOfChildren() {
		return getNumberOfLines("CHIL");
	}
	
	/**
	 * 
	 * 
	 * @param isMarried
	 * @param marriageDate
	 * @return
	 */
	public boolean setMarried(boolean isMarried, String marriageDate) {
		GedcomValue marr = new GedcomValue(false, (isMarried ? YesNo.YES.value : null), 
				"FAMILY_EVENT_STRUCTURE;MARR", "MARR");
		
		GedcomValue date = new GedcomValue(false, marriageDate, marr,  
				(!isV55() ? "FAMILY_EVENT_DETAIL" : null), "EVENT_DETAIL", "DATE");
		
		return createAndSet(marr, date);
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public boolean isMarried() {
		return YesNo.YES.value.equals(getValue("FAMILY_EVENT_STRUCTURE;MARR", "MARR"));
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public boolean removeMarried() {
		return remove("FAMILY_EVENT_STRUCTURE;MARR", "MARR");
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public String getMarriageDate() {
		return getValue("FAMILY_EVENT_STRUCTURE;MARR", "MARR", (!isV55() ? "FAMILY_EVENT_DETAIL" : null), "EVENT_DETAIL", "DATE");
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public boolean removeMarriageDate() {
		return remove("FAMILY_EVENT_STRUCTURE;MARR", "MARR", (!isV55() ? "FAMILY_EVENT_DETAIL" : null), "EVENT_DETAIL", "DATE");
	}
	
	/**
	 * 
	 * 
	 * @param isDivorced
	 * @param marriageDate
	 * @return
	 */
	public boolean setDivorced(boolean isDivorced, String divorcedDate) {
		GedcomDataEmpty div = new GedcomDataEmpty(false, 
				"FAMILY_EVENT_STRUCTURE;DIV", "DIV");
		
		GedcomValue date = new GedcomValue(false, divorcedDate, div,  
				(!isV55() ? "FAMILY_EVENT_DETAIL" : null), "EVENT_DETAIL", "DATE");
		
		return createAndSet(div, date);
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public boolean isDivorced() {
		//Since the DIV tag does not have a value field, just check if the tag is there
		return (getBaseNode().followPath("FAMILY_EVENT_STRUCTURE;DIV", "DIV") != null);
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public boolean removeDivorced() {
		return remove("FAMILY_EVENT_STRUCTURE;DIV", "DIV");
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public String getDivorceDate() {
		return getValue("FAMILY_EVENT_STRUCTURE;DIV", "DIV", (!isV55() ? "FAMILY_EVENT_DETAIL" : null), "EVENT_DETAIL", "DATE");
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public boolean removeDivorceDate() {
		return remove("FAMILY_EVENT_STRUCTURE;DIV", "DIV", (!isV55() ? "FAMILY_EVENT_DETAIL" : null), "EVENT_DETAIL", "DATE");
	}
	

}
