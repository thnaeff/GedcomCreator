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

import ch.thn.gedcom.data.GedcomNode;
import ch.thn.gedcom.store.GedcomStore;

/**
 * @author Thomas Naeff (github.com/thnaeff)
 *
 */
public class GedcomHeader extends AbstractGedcomStructure {

	
	/**
	 * A HEADER structures
	 * 
	 * @param store
	 */
	public GedcomHeader(GedcomStore store) {
		super(store, "HEADER", "HEAD");
				
	}
	
	/**
	 * 
	 * 
	 * @param store
	 * @param node
	 */
	public GedcomHeader(GedcomStore store, GedcomNode node) {
		super(store, "HEADER", node, "HEAD");
	}
	
	/**
	 * Not available for HEADER structures
	 * 
	 */
	@Override
	public boolean setChangeDate(String changeDate, String changeTime) {
		throw new UnsupportedOperationException("No change date available in HEADER structures.");
	}
	
	/**
	 * Not available for HEADER structures
	 * 
	 */
	@Override
	public String getChangeDate() {
		throw new UnsupportedOperationException("No change date available in HEADER structures.");
	}
	
	/**
	 * Not available for HEADER structures
	 * 
	 */
	@Override
	public boolean removeChangeDate() {
		throw new UnsupportedOperationException("No change date available in HEADER structures.");
	}
	
	/**
	 * Not available for HEADER structures
	 * 
	 */
	@Override
	public String getChangeTime() {
		throw new UnsupportedOperationException("No change time available in HEADER structures.");
	}
	
	/**
	 * Not available for HEADER structures
	 * 
	 */
	@Override
	public boolean removeChangeTime() {
		throw new UnsupportedOperationException("No change time available in HEADER structures.");
	}
	
	/**
	 * Not available for HEADER structures
	 * 
	 */
	@Override
	public boolean setNote(int index, String note) {
		throw new UnsupportedOperationException("No notes available in HEADER structures.");
	}
	
	/**
	 * Not available for HEADER structures
	 * 
	 */
	@Override
	public boolean addNote(String note) {
		throw new UnsupportedOperationException("No notes available in HEADER structures.");
	}
	
	/**
	 * Not available for HEADER structures
	 * 
	 */
	@Override
	public String getNote(int index) {
		throw new UnsupportedOperationException("No notes available in HEADER structures.");
	}
	
	/**
	 * Not available for HEADER structures
	 * 
	 */
	@Override
	public boolean removeNote(int index) {
		throw new UnsupportedOperationException("No notes available in HEADER structures.");
	}
	
	
	/**
	 * 
	 * 
	 * @param systemId SOUR
	 * @param productName NAME
	 * @param businessName CORP
	 * @return
	 */
	public boolean setSource(String systemId, String productName, String businessName) {
		GedcomValue sour = new GedcomValue(false, systemId, 
				"SOUR");
		
		GedcomValue name = new GedcomValue(false, productName, sour, 
				"NAME");
		
		GedcomValue corp = new GedcomValue(false, businessName, sour, 
				"CORP");
		
		
		return createAndSet(sour, name, corp);
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public String getSource() {
		return getValue("SOUR");
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public boolean removeSource() {
		return remove("SOUR");
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public String getName() {
		return getValue("SOUR", "NAME");
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public boolean removeName() {
		return remove("SOUR", "NAME");
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public String getCoorporation() {
		return getValue("SOUR", "CORP");
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public boolean removeCoorporation() {
		return remove("SOUR", "CORP");
	}
	
	/**
	 * 
	 * 
	 * @param transmissionDate
	 * @param transmissionTime
	 * @return
	 */
	public boolean setTransmissionDate(String transmissionDate, String transmissionTime) {
		GedcomValue date = new GedcomValue(false, transmissionDate, 
				"DATE");
		
		GedcomValue time = new GedcomValue(false, transmissionTime, date, 
				"TIME");
		
		return createAndSet(date, time);
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public String getTransmissionDate() {
		return getValue("DATE");
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public boolean removeTransmissionDate() {
		return remove("DATE");
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public String getTransmissionTime() {
		return getValue("DATE", "TIME");
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public boolean removeTransmissionTime() {
		return remove("DATE", "TIME");
	}
	
	/**
	 * 
	 * 
	 * @param transmissionDate
	 * @return
	 */
	public boolean setSubmitterRecordLink(String submitterRecordId) {
		return createAndSet(new GedcomXRef(false, submitterRecordId, "SUBM"));
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public String getSubmitterRecordLink() {
		return getXRef("SUBM");
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public boolean removeSubmitterRecordLink() {
		return remove("SUBM");
	}
	
	/**
	 * 
	 * 
	 * @param gedcomVersion
	 * @param gedcomForm
	 * @return
	 */
	public boolean setGedcomInfo(String gedcomVersion, String gedcomForm) {
		GedcomValue version = new GedcomValue(false, gedcomVersion, 
				"GEDC", "VERS");
		
		GedcomValue form = new GedcomValue(false, gedcomForm, 
				"GEDC", "FORM");
		
		return createAndSet(version, form);
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public String getGedcomVersion() {
		return getValue("GEDC", "VERS");
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public boolean removeGedcomVersion() {
		return remove("GEDC", "VERS");
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public String getGedcomForm() {
		return getValue("GEDC", "FORM");
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public boolean removeGedcomForm() {
		return remove("GEDC", "FORM");
	}
	
	/**
	 * 
	 * 
	 * @param characterSet
	 * @return
	 */
	public boolean setCharacterSet(String characterSet) {
		return createAndSet(new GedcomValue(false, characterSet, 
				"CHAR"));
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public String getCharacterSet() {
		return getValue("CHAR");
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public boolean removeCharacterSet() {
		return remove("CHAR");
	}
	
	/**
	 * 
	 * 
	 * @param language
	 * @return
	 */
	public boolean setLanguage(String language) {
		return createAndSet(new GedcomValue(false, language, 
				"LANG"));
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public String getLanguage() {
		return getValue("LANG");
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public boolean removeLanguage() {
		return remove("LANG");
	}
	

}
