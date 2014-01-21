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

import ch.thn.gedcom.GedcomFormatter;
import ch.thn.gedcom.creator.GedcomCreatorEnums.YesNo;
import ch.thn.gedcom.data.GedcomError;
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
		
		addLines(new XRefLine("FAM", id, followPathCreate()));
		
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public String getId() {
		return getXRef("FAM", 0);
	}
	
	/**
	 * 
	 * 
	 * @param husbandId
	 * @return
	 */
	public boolean setHusbandLink(String husbandId) {
		if (setXRef("HUSB", husbandId)) {
			return true;
		}
		
		try {
			Line line1 = new XRefLine(
					"HUSB", 
					husbandId, 
					followPathCreate("HUSB"));
			return setLines(line1);
		} catch (GedcomError e) {
			throw e;
		}
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public String getHusbandLink() {
		return getXRef("HUSB", 0);
	}
	
	/**
	 * 
	 * 
	 * @param wifeId
	 * @return
	 */
	public boolean setWifeLink(String wifeId) {
		if (setXRef("WIFE", wifeId)) {
			return true;
		}
		
		try {
			Line line1 = new XRefLine(
					"WIFE", 
					wifeId, 
					followPathCreate("WIFE"));
			return setLines(line1);
		} catch (GedcomError e) {
			throw e;
		}
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public String getWifeLink() {
		return getXRef("WIFE", 0);
	}
	
	/**
	 * 
	 * 
	 * @param childId
	 * @return
	 */
	public boolean addChildLink(String childId) {
		try {
			Line line1 = new XRefLine(
					"CHIL", 
					childId, 
					createPathEnd("CHIL"));
			return setLines(line1);
		} catch (GedcomError e) {
			throw e;
		}
	}
	
	/**
	 * 
	 * 
	 * @param childId
	 * @return
	 */
	public boolean setChildLink(int index, String childId) {
		if (setXRef("CHIL", index, childId)) {
			return true;
		}
		
		return addChildLink(childId);
	}
	
	/**
	 * 
	 * 
	 * @param index
	 * @return
	 */
	public String getChildLink(int index) {
		return getXRef("CHIL", index);
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
	 * @param married
	 * @param marriageDate
	 * @return
	 */
	public boolean setMarried(boolean married, Date marriageDate) {
		if (setValue("MARR", (married ? YesNo.YES.value : null)) 
				&& setValue("MARR-DATE", GedcomFormatter.getDate(marriageDate))) {
			return true;
		}
		
		try {
			Line line1 = new ValueLine(
					"MARR", 
					(married ? YesNo.YES.value : null), 
					followPathCreate("FAMILY_EVENT_STRUCTURE;MARR", "MARR"));
			Line line2 = new ValueLine(
					"MARR-DATE", 
					GedcomFormatter.getDate(marriageDate), 
					followPathCreate(line1.node, "FAMILY_EVENT_DETAIL", "EVENT_DETAIL", "DATE"));
			
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
	public boolean getMarriage() {
		return getValue("MARR", 0).equals(YesNo.YES.value);
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public String getMarriageDate() {
		return getValue("MARR-DATE", 0);
	}
	
	/**
	 * 
	 * 
	 * @param divorced
	 * @return
	 */
	public boolean setDivorced(boolean divorced) {
		if (setValue("DIV", null)) {
			return true;
		}
		
		try {
			Line line1 = new EmptyLine(
					"DIV", 
					followPathCreate("FAMILY_EVENT_STRUCTURE;DIV", "DIV"));
			
			return setLines(line1);
		} catch (GedcomError e) {
			throw e;
		}
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public boolean getDivorced() {
		return getValue("DIV", 0).equals(YesNo.YES.value);
	}
	

}
