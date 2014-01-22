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
import ch.thn.gedcom.data.GedcomError;
import ch.thn.gedcom.data.GedcomNode;
import ch.thn.gedcom.store.GedcomStore;

/**
 * @author Thomas Naeff (github.com/thnaeff)
 *
 */
public class GedcomCreatorHeader extends GedcomCreatorStructure {

	
	/**
	 * A HEADER
	 * 
	 * @param store
	 */
	public GedcomCreatorHeader(GedcomStore store) {
		super(store, "HEADER", "HEAD");
				
	}
	
	/**
	 * 
	 * 
	 * @param store
	 * @param node
	 */
	public GedcomCreatorHeader(GedcomStore store, GedcomNode node) {
		super(store, "HEADER", node);
	}
	
	/**
	 * Not available for HEADER
	 * 
	 */
	@Override
	public boolean setChangeDate(Date changeDate) {
		throw new UnsupportedOperationException("No change date available in HEADER. Use transmission date instead.");
	}
	
	/**
	 * Not available for HEADER
	 * 
	 */
	@Override
	public String getChangeDate() {
		throw new UnsupportedOperationException("No change date available in HEADER. Use transmission date instead.");
	}
	
	
	/**
	 * 
	 * 
	 * @param systemId
	 * @param productName
	 * @param businessName
	 * @return
	 */
	public boolean setSource(String systemId, String productName, String businessName) {
		if (setValue("SOUR", systemId) 
				&& setValue("NAME", productName)
				&& setValue("CORP", businessName)) {
			return true;
		}
		
		try {
			Line line1 = new ValueLine(
					"SOUR", 
					systemId, 
					followPathCreate("SOUR"));
			Line line2 = new ValueLine(
					"NAME", 
					productName, 
					followPathCreate(line1.node, "NAME"));
			Line line3 = new ValueLine(
					"CORP", 
					businessName, 
					followPathCreate(line1.node, "CORP"));
			
			return setLines(line1, line2, line3);
		} catch (GedcomError e) {
			throw e;
		}
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public String getSource() {
		return getValue("SOUR", 0, "SOUR");
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public String getName() {
		return getValue("NAME", 0, "SOUR", "NAME");
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public String getCoorporation() {
		return getValue("CORP", 0, "SOUR", "CORP");
	}
	
	/**
	 * 
	 * 
	 * @param transmissionDate
	 * @return
	 */
	public boolean setTransmissionDate(Date transmissionDate) {
		if (setValue("DATE", GedcomFormatter.getDate(transmissionDate)) 
				&& setValue("TIME", GedcomFormatter.getTime(transmissionDate))) {
			return true;
		}
		
		try {
			Line line1 = new ValueLine(
					"DATE", 
					GedcomFormatter.getDate(transmissionDate), 
					followPathCreate("DATE"));
			Line line2 = new ValueLine(
					"TIME", 
					GedcomFormatter.getTime(transmissionDate), 
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
	public String getTransmissionDate() {
		return getValue("DATE", 0, "DATE");
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public String getTransmissionTime() {
		return getValue("TIME", 0, "DATE", "TIME");
	}
	
	/**
	 * 
	 * 
	 * @param transmissionDate
	 * @return
	 */
	public boolean setSubmitterRecordLink(String submitterRecordId) {
		if (setXRef("SUBM", submitterRecordId)) {
			return true;
		}
		
		try {
			Line line1 = new XRefLine(
					"SUBM", 
					submitterRecordId, 
					followPathCreate("SUBM"));
			
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
	public String setSubmitterRecordLink() {
		return getXRef("SUBM", 0, "SUBM");
	}
	
	/**
	 * 
	 * 
	 * @param gedcomVersion
	 * @param gedcomForm
	 * @return
	 */
	public boolean setGedcomInfo(String gedcomVersion, String gedcomForm) {
		if (setValue("VERS", gedcomVersion) 
				&& setValue("FORM", gedcomForm)) {
			return true;
		}
		
		try {
			Line line1 = new ValueLine(
					"VERS", 
					gedcomVersion, 
					followPathCreate("GEDC", "VERS"));
			Line line2 = new ValueLine(
					"FORM", 
					gedcomForm, 
					followPathCreate("GEDC", "FORM"));
			
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
	public String getGedcomVersion() {
		return getValue("VERS", 0, "GEDC", "VERS");
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public String getGedcomForm() {
		return getValue("FORM", 0, "GEDC", "FORM");
	}
	
	/**
	 * 
	 * 
	 * @param characterSet
	 * @return
	 */
	public boolean setCharacterSet(String characterSet) {
		if (setValue("CHAR", characterSet)) {
			return true;
		}
		
		try {
			Line line1 = new ValueLine(
					"CHAR", 
					characterSet, 
					followPathCreate("CHAR"));
			
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
	public String getCharacterSet() {
		return getValue("CHAR", 0, "CHAR");
	}
	
	/**
	 * 
	 * 
	 * @param language
	 * @return
	 */
	public boolean setLanguage(String language) {
		if (setValue("LANG", language)) {
			return true;
		}
		
		try {
			Line line1 = new ValueLine(
					"LANG", 
					language, 
					followPathCreate("LANG"));
			
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
	public String getLanguage() {
		return getValue("LANG", 0, "LANG");
	}
	

}
