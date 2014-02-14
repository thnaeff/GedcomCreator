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
		super(store, "HEADER", node, "HEAD");
	}
	
	/**
	 * Not available for HEADER
	 * 
	 */
	@Override
	public boolean setChangeDate(String changeDate, String changeTime) {
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
		GedcomValue sour = new GedcomValue(false, systemId, 
				"SOUR");
		
		GedcomValue name = new GedcomValue(false, productName, sour, 
				"NAME");
		
		GedcomValue corp = new GedcomValue(false, businessName, sour, 
				"CORP");
		
		
		return apply(sour, name, corp);
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
	public String getName() {
		return getValue("SOUR", "NAME");
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
	 * @param transmissionDate
	 * @param transmissionTime
	 * @return
	 */
	public boolean setTransmissionDate(String transmissionDate, String transmissionTime) {
		GedcomValue date = new GedcomValue(false, transmissionDate, 
				"DATE");
		
		GedcomValue time = new GedcomValue(false, transmissionTime, date, 
				"TIME");
		
		return apply(date, time);
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
	public String getTransmissionTime() {
		return getValue("DATE", "TIME");
	}
	
	/**
	 * 
	 * 
	 * @param transmissionDate
	 * @return
	 */
	public boolean setSubmitterRecordLink(String submitterRecordId) {
		return apply(new GedcomXRef(false, submitterRecordId, "SUBM"));
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public String setSubmitterRecordLink() {
		return getXRef("SUBM");
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
		
		return apply(version, form);
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
	public String getGedcomForm() {
		return getValue("GEDC", "FORM");
	}
	
	/**
	 * 
	 * 
	 * @param characterSet
	 * @return
	 */
	public boolean setCharacterSet(String characterSet) {
		return apply(new GedcomValue(false, characterSet, 
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
	 * @param language
	 * @return
	 */
	public boolean setLanguage(String language) {
		return apply(new GedcomValue(false, language, 
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
	

}
