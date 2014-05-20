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


import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import ch.thn.gedcom.GedcomFormatter;
import ch.thn.gedcom.creator.GedcomCreatorError;
import ch.thn.gedcom.creator.GedcomEnums.NameType;
import ch.thn.gedcom.creator.GedcomEnums.Sex;
import ch.thn.gedcom.creator.GedcomEnums.YesNo;
import ch.thn.gedcom.data.GedcomAccessError;
import ch.thn.gedcom.data.GedcomNode;
import ch.thn.gedcom.store.GedcomStore;

/**
 * @author Thomas Naeff (github.com/thnaeff)
 *
 */
public class GedcomIndividual extends AbstractGedcomStructure {
	
	/**
	 * A new INDIVIDUAL_RECORD with the given ID
	 * 
	 * @param store
	 * @param id
	 */
	public GedcomIndividual(GedcomStore store, String id) {
		super(store, "INDIVIDUAL_RECORD", "INDI");
		
		if (!setId(id)) {
			throw new GedcomCreatorError("Failed to create individual with ID " + 
					id + ". Id could not be set.");
		}
	}
	
	/**
	 * Creates a new individual using the given gedcom node containing the 
	 * INDIVIDUAL_RECORD structure. The given structure at least has to include the 
	 * INDI tag line.
	 * 
	 * @param store
	 * @param node
	 */
	public GedcomIndividual(GedcomStore store, GedcomNode node) {
		super(store, "INDIVIDUAL_RECORD", node, "INDI");
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
	 * Returns the XRef ID of this individual
	 * 
	 * @return
	 */
	public String getId() {
		return getXRef();
	}
	
	/**
	 * 
	 * 
	 * @param sex
	 * @return
	 */
	public boolean setSex(Sex sex) {
		return createAndSet(new GedcomValue(false, (sex == null ? null : sex.getValue()), 
				"SEX"));
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public boolean removeSex() {
		return remove("SEX");
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public Sex getSex() {
		String sex = getValue("SEX");
		
		if (Sex.MALE.getValue().equals(sex)) {
			return Sex.MALE;
		} else if (Sex.FEMALE.getValue().equals(sex)) {
			return Sex.FEMALE;
		} else {
			return Sex.UNKNOWN;
		}
	}
	
	/**
	 * 
	 * 
	 * @param isBorn
	 * @param birthDate
	 * @return
	 */
	public boolean setBirth(boolean isBorn, String birthDate) {
		GedcomValue born = new GedcomValue(false, (isBorn ? YesNo.YES.getValue() : null), 
				"INDIVIDUAL_EVENT_STRUCTURE;BIRT", "BIRT");
		
		GedcomValue date = new GedcomValue(false, birthDate, born, 
				(!isV55() ? "INDIVIDUAL_EVENT_DETAIL" : null), "EVENT_DETAIL", "DATE");
		
		return createAndSet(born, date);
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public boolean isBorn() {
		String born = getValue("INDIVIDUAL_EVENT_STRUCTURE;BIRT", "BIRT");
		
		if (YesNo.YES.getValue().equals(born)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public boolean removeBirth(boolean isBorn, String birthDate) {
		return remove("INDIVIDUAL_EVENT_STRUCTURE;BIRT", "BIRT");
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public String getBirthDate() {
		return getValue("INDIVIDUAL_EVENT_STRUCTURE;BIRT", "BIRT", 
				(!isV55() ? "INDIVIDUAL_EVENT_DETAIL" : null), "EVENT_DETAIL", "DATE");
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public boolean removeBirthDate() {
		return remove("INDIVIDUAL_EVENT_STRUCTURE;BIRT", "BIRT", 
				(!isV55() ? "INDIVIDUAL_EVENT_DETAIL" : null), "EVENT_DETAIL", "DATE");
	}
	
	/**
	 * 
	 * 
	 * @param isDead
	 * @param deathDate
	 * @return
	 */
	public boolean setDeath(boolean isDead, String deathDate) {
		GedcomValue dead = new GedcomValue(false, (isDead ? YesNo.YES.getValue() : null), 
				"INDIVIDUAL_EVENT_STRUCTURE;DEAT", "DEAT");
		
		GedcomValue date = new GedcomValue(false, deathDate, dead, 
				(!isV55() ? "INDIVIDUAL_EVENT_DETAIL" : null), "EVENT_DETAIL", "DATE");
		
		return createAndSet(dead, date);
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public boolean isDead() {
		String born = getValue("INDIVIDUAL_EVENT_STRUCTURE;DEAT", "DEAT");
		
		if (YesNo.YES.getValue().equals(born)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public boolean removeDeath() {
		return remove("INDIVIDUAL_EVENT_STRUCTURE;DEAT", "DEAT");
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public String getDeathDate() {
		return getValue("INDIVIDUAL_EVENT_STRUCTURE;DEAT", "DEAT", 
				(!isV55() ? "INDIVIDUAL_EVENT_DETAIL" : null), "EVENT_DETAIL", "DATE");
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public boolean removeDeathDate() {
		return remove("INDIVIDUAL_EVENT_STRUCTURE;DEAT", "DEAT", 
				(!isV55() ? "INDIVIDUAL_EVENT_DETAIL" : null), "EVENT_DETAIL", "DATE");
	}
	
	/**
	 * 
	 * 
	 * @param occupation
	 * @return
	 */
	public boolean setOccupation(String occupation) {
		return createAndSet(new GedcomValue(false, occupation, 
				"INDIVIDUAL_ATTRIBUTE_STRUCTURE;OCCU", "OCCU"));
		
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public String getOccupation() {
		return getValue("INDIVIDUAL_ATTRIBUTE_STRUCTURE;OCCU", "OCCU");
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public boolean removeOccupation() {
		return remove("INDIVIDUAL_ATTRIBUTE_STRUCTURE;OCCU", "OCCU");
	}

	/**
	 * 
	 * 
	 * @param education
	 * @return
	 */
	public boolean setEducation(String education) {
		return createAndSet(new GedcomValue(false, education, 
				"INDIVIDUAL_ATTRIBUTE_STRUCTURE;EDUC", "EDUC"));
		
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public String getEducation() {
		return getValue("INDIVIDUAL_ATTRIBUTE_STRUCTURE;EDUC", "EDUC");
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public boolean removeEducation() {
		return remove("INDIVIDUAL_ATTRIBUTE_STRUCTURE;EDUC", "EDUC");
	}
	
	/**
	 * 
	 * @param add
	 * @param index
	 * @param name The family/former/married/... name
	 * @param nameType
	 * @param firstNames The name of the individual (first/middle/... name)
	 * @return
	 */
	private boolean setName(boolean add, int index, String name, NameType nameType, String... firstNames) {
		String firstNamesString = GedcomFormatter.makeStringList(
				Arrays.asList(firstNames), ", ", "", "", false, null, false).toString();
		
		String indexString = "";
		
		if (index != -1) {
			indexString = GedcomNode.PATH_OPTION_DELIMITER + index;
		}
		
		GedcomValue n = new GedcomValue(add, firstNamesString + " /" + name + "/", 
				"PERSONAL_NAME_STRUCTURE" + indexString, "NAME");
		
		GedcomValue givn = new GedcomValue(add, firstNamesString, n, 
				(!isV55() ? "PERSONAL_NAME_PIECES" : null), "GIVN");
		
		GedcomValue surn = new GedcomValue(add, name, n, 
				(!isV55() ? "PERSONAL_NAME_PIECES" : null), "SURN");
		
		GedcomValue type = new GedcomValue(add, (nameType == null ? null : nameType.getValue()), n, 
					"TYPE");
		
		return createAndSet(n, givn, surn, (!isV55() ? type : null));
	}
	
	/**
	 * 
	 * 
	 * @param name The family/former/married/... name
	 * @param firstNames The name of the individual (first/middle/... name)
	 * @return
	 */
	public boolean addName(String name, String... firstNames) {
		return addName(name, NameType.UNSPECIFIED, firstNames);
	}
	
	/**
	 * 
	 * 
	 * @param name The family/former/married/... name
	 * @param nameType
	 * @param firstNames The name of the individual (first/middle/... name)
	 * @return
	 */
	public boolean addName(String name, NameType nameType, String... firstNames) {
		return setName(true, -1, name, nameType, firstNames);
	}
	
	/**
	 * Sets the name and if there is no name with the given index, a new name 
	 * block is added
	 * 
	 * @param index
	 * @param name
	 * @param firstNames
	 * @return
	 */
	public boolean setName(int index, String name, String... firstNames) {
		return setName(index, name, NameType.UNSPECIFIED, firstNames);
	}
		
	/**
	 * Sets the name and if there is no name with the given index, a new name 
	 * block is added
	 * 
	 * @param index
	 * @param name
	 * @param nameType
	 * @param firstNames
	 * @return
	 */
	public boolean setName(int index, String name, NameType nameType, String... firstNames) {
		return setName(false, index, name, nameType, firstNames);
	}
	
	/**
	 * name = last name = family name = surname<br>
	 * Family name = surname (at least in western context -> http://en.wikipedia.org/wiki/Family_name)
	 * 
	 * @param index
	 * @return
	 */
	public String getName(int index) {
		return getValue("PERSONAL_NAME_STRUCTURE" + GedcomNode.PATH_OPTION_DELIMITER + index, "NAME");
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public List<String> getNames() {
		LinkedList<String> result = new LinkedList<>();
		int count = getNumberOfNames();
		while (count > 0) {
			result.add(getName(--count));
		}
		return result;
	}
	
	/**
	 * 
	 * @param index
	 * @return
	 */
	public boolean removeName(int index) {
		return remove("PERSONAL_NAME_STRUCTURE" + GedcomNode.PATH_OPTION_DELIMITER + index, "NAME");
	}
	
	/**
	 * given name = first name = forename
	 * 
	 * @param index
	 * @return
	 */
	public String getGivenName(int index) {
		return getValue("PERSONAL_NAME_STRUCTURE" + GedcomNode.PATH_OPTION_DELIMITER + index, "NAME", (!isV55() ? "PERSONAL_NAME_PIECES" : null), "GIVN");
	}
	
	/**
	 * 
	 * @param index
	 * @return
	 */
	public boolean removeGivenName(int index) {
		return remove("PERSONAL_NAME_STRUCTURE" + GedcomNode.PATH_OPTION_DELIMITER + index, "NAME", (!isV55() ? "PERSONAL_NAME_PIECES" : null), "GIVN");
	}
	
	/**
	 * surname = family name = name = last name<br>
	 * Family name = surname (at least in western context -> http://en.wikipedia.org/wiki/Family_name)
	 * 
	 * @param index
	 * @return
	 */
	public String getSurname(int index) {
		return getValue("PERSONAL_NAME_STRUCTURE" + GedcomNode.PATH_OPTION_DELIMITER + index, "NAME", (!isV55() ? "PERSONAL_NAME_PIECES" : null), "SURN");
	}
	
	/**
	 * 
	 * @param index
	 * @return
	 */
	public boolean removeSurname(int index) {
		return remove("PERSONAL_NAME_STRUCTURE" + GedcomNode.PATH_OPTION_DELIMITER + index, "NAME", (!isV55() ? "PERSONAL_NAME_PIECES" : null), "SURN");
	}
	
	/**
	 * 
	 * 
	 * @param index
	 * @return
	 */
	public NameType getNameType(int index) {
		if (isV55()) {
			throw new GedcomAccessError("NAME->TYPE not available in GEDCOM v5.5");
		}
		
		String type = getValue("PERSONAL_NAME_STRUCTURE" + GedcomNode.PATH_OPTION_DELIMITER + index, "NAME", "TYPE");
		
		if (NameType.MAIDEN.getValue().equals(type)) {
			return NameType.MAIDEN;
		} else if (NameType.MARRIED.getValue().equals(type)) {
			return NameType.MARRIED;
		} else if (NameType.BIRTH.getValue().equals(type)) {
			return NameType.BIRTH;
		} else if (NameType.AKA.getValue().equals(type)) {
			return NameType.AKA;
		} else if (NameType.IMMIGRANT.getValue().equals(type)) {
			return NameType.IMMIGRANT;
		} else {
			return NameType.UNSPECIFIED;
		}
	}
	
	/**
	 * 
	 * 
	 * @param index
	 * @return
	 */
	public boolean removeNameType(int index) {
		if (isV55()) {
			throw new GedcomAccessError("NAME->TYPE not available in GEDCOM v5.5");
		}
		
		return remove("PERSONAL_NAME_STRUCTURE" + GedcomNode.PATH_OPTION_DELIMITER + index, "NAME", "TYPE");
		
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public int getNumberOfNames() {
		return getNumberOfLines("PERSONAL_NAME_STRUCTURE");
	}
	
	
	
	/**
	 * 
	 * @param add
	 * @param index
	 * @param street1
	 * @param street2
	 * @param city
	 * @param post
	 * @param country
	 * @param phone
	 * @param email
	 * @param fax
	 * @param websites
	 * @return
	 */
	private boolean setAddress(boolean add, int index, String street1, String street2, String city, String post, String country, 
			String[] phone, String[] email, String[] fax, String[] websites) {
		//Create the address string. Each string part should not have any commas since 
		//commas are used to separate the string parts.
		ArrayList<String> s = new ArrayList<String>(5);
		if (street1 != null) s.add(street1.replaceAll(",", ""));
		if (street2 != null) s.add(street2.replaceAll(",", ""));
		if (post != null) s.add(post.replaceAll(",", ""));
		if (city != null) s.add(city.replaceAll(",", ""));
		if (country != null) s.add(country.replaceAll(",", ""));
		String addrString = GedcomFormatter.makeStringList(s, ", ", null, null, true, "", false).toString();
				
		String indexString = "";
		
		if (index != -1) {
			indexString = GedcomNode.PATH_OPTION_DELIMITER + index;
		}
		
		GedcomDataEmpty addrstruct = new GedcomDataEmpty(add, 
				"INDIVIDUAL_ATTRIBUTE_STRUCTURE;RESI" + indexString, "RESI", (!isV55() ? "INDIVIDUAL_EVENT_DETAIL" : null), "EVENT_DETAIL", "ADDRESS_STRUCTURE");
		
		
		GedcomValue addr = new GedcomValue(add, addrString, addrstruct, 
				"ADDR");
		
		GedcomValue adr1 = new GedcomValue(add, street1, addr, 
				"ADR1");
		
		GedcomValue adr2 = new GedcomValue(add, street2, addr, 
				"ADR2");
		
		GedcomValue cit = new GedcomValue(add, city, addr, 
				"CITY");
		
		GedcomValue pos = new GedcomValue(add, post, addr, 
				"POST");
		
		GedcomValue ctry = new GedcomValue(add, country, addr, 
				"CTRY");
		
		
		GedcomValue phon1 = (phone != null && phone.length > 0 ? 
				new GedcomValue(add, (phone[0] == null ? null : phone[0]), addrstruct, 
				"PHON") 
				: null);
		
		GedcomValue phon2 = (phone != null && phone.length > 1 ? 
				new GedcomValue(add, (phone[1] == null ? null : phone[1]), addrstruct, 
				"PHON") 
				: null);
		
		GedcomValue phon3 = (phone != null && phone.length > 2 ? 
				new GedcomValue(add, (phone[2] == null ? null : phone[2]), addrstruct, 
				"PHON") 
				: null);
		
		
		GedcomValue email1 = (email != null && email.length > 0 ? 
				new GedcomValue(add, (email[0] == null ? null : email[0]), addrstruct, 
				"EMAIL") 
				: null);
		
		GedcomValue email2 = (email != null && email.length > 1 ? 
				new GedcomValue(add, (email[1] == null ? null : email[1]), addrstruct, 
				"EMAIL") 
				: null);
		
		GedcomValue email3 = (email != null && email.length > 2 ? 
				new GedcomValue(add, (email[2] == null ? null : email[2]), addrstruct, 
				"EMAIL") 
				: null);
		
		
		GedcomValue fax1 = (fax != null && fax.length > 0 ? 
				new GedcomValue(add, (fax[0] == null ? null : fax[0]), addrstruct, 
				"FAX") 
				: null);
		
		GedcomValue fax2 = (fax != null && fax.length > 1 ? 
				new GedcomValue(add, (fax[1] == null ? null : fax[1]), addrstruct, 
				"FAX") 
				: null);
		
		GedcomValue fax3 = (fax != null && fax.length > 2 ? 
				new GedcomValue(add, (fax[2] == null ? null : fax[2]), addrstruct, 
				"FAX") 
				: null);
		
		
		GedcomValue www1 = (websites != null && websites.length > 0 ? 
				new GedcomValue(add, (websites[0] == null ? null : websites[0]), addrstruct, 
				"WWW") 
				: null);
		
		GedcomValue www2 = (websites != null && fax.length > 1 ? 
				new GedcomValue(add, (websites[1] == null ? null : websites[1]), addrstruct, 
				"WWW") 
				: null);
		
		GedcomValue www3 = (websites != null && fax.length > 2 ? 
				new GedcomValue(add, (websites[2] == null ? null : websites[2]), addrstruct, 
				"WWW") 
				: null);
		
		
		
		return createAndSet(addrstruct, addr, adr1, adr2, cit, pos, ctry, 
				phon1, phon2, phon3, 
				(!isV55() ? email1 : null), (!isV55() ? email2 : null), (!isV55() ? email3 : null), 
				(!isV55() ? fax1 : null), (!isV55() ? fax2 : null), (!isV55() ? fax3 : null), 
				(!isV55() ? www1 : null), (!isV55() ? www2 : null), (!isV55() ? www3 : null));
	}

	/**
	 * Adds a new address block
	 * 
	 * @param street1
	 * @param street2
	 * @param city
	 * @param post
	 * @param country
	 * @param phone
	 * @param email
	 * @param fax
	 * @param websites
	 * @return
	 */
	public boolean addAddress(String street1, String street2, String city, String post, String country, 
			String[] phone, String[] email, String[] fax, String[] websites) {
		return setAddress(true, -1, street1, street2, city, post, country, phone, email, fax, websites);
	}
	
	/**
	 * Sets the address and if there is no address with the given index, a new address 
	 * block is added
	 * 
	 * @param index
	 * @param street1
	 * @param street2
	 * @param city
	 * @param post
	 * @param country
	 * @param phone
	 * @param email
	 * @param fax
	 * @param websites
	 * @return
	 */
	public boolean setAddress(int index, String street1, String street2, String city, String post, String country, 
			String[] phone, String[] email, String[] fax, String[] websites) {
		return setAddress(false, index, street1, street2, city, post, country, phone, email, fax, websites);
	}
	
	/**
	 * Removes the ADDR part of the address structure
	 * 
	 * @param index
	 * @return
	 */
	public boolean removeAddress(int index) {
		return remove("INDIVIDUAL_ATTRIBUTE_STRUCTURE;RESI" + GedcomNode.PATH_OPTION_DELIMITER + index, "RESI", (!isV55() ? "INDIVIDUAL_EVENT_DETAIL" : null), "EVENT_DETAIL", "ADDRESS_STRUCTURE", "ADDR");
	}
	
	/**
	 * Removes the whole address structure
	 * 
	 * @param index
	 * @return
	 */
	public boolean removeAddressStructure(int index) {
		return remove("INDIVIDUAL_ATTRIBUTE_STRUCTURE;RESI" + GedcomNode.PATH_OPTION_DELIMITER + index, "RESI", (!isV55() ? "INDIVIDUAL_EVENT_DETAIL" : null), "EVENT_DETAIL", "ADDRESS_STRUCTURE");
	}
	
	/**
	 * 
	 * 
	 * @param index
	 * @return
	 */
	public String getAddress(int index) {
		return getValue("INDIVIDUAL_ATTRIBUTE_STRUCTURE;RESI" + GedcomNode.PATH_OPTION_DELIMITER + index, "RESI", (!isV55() ? "INDIVIDUAL_EVENT_DETAIL" : null), "EVENT_DETAIL", "ADDRESS_STRUCTURE", "ADDR");
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public List<String> getAddresses() {
		LinkedList<String> result = new LinkedList<>();
		int count = getNumberOfAddresses();
		while (count > 0) {
			result.add(getAddress(--count));
		}
		return result;
	}
	
	/**
	 * 
	 * 
	 * @param index
	 * @return
	 */
	public String getStreet1(int index) {
		return getValue("INDIVIDUAL_ATTRIBUTE_STRUCTURE;RESI" + GedcomNode.PATH_OPTION_DELIMITER + index, "RESI", (!isV55() ? "INDIVIDUAL_EVENT_DETAIL" : null), "EVENT_DETAIL", "ADDRESS_STRUCTURE", "ADDR", "ADR1");
	}
	
	/**
	 * 
	 * 
	 * @param index
	 * @return
	 */
	public boolean removeStreet1(int index) {
		return remove("INDIVIDUAL_ATTRIBUTE_STRUCTURE;RESI" + GedcomNode.PATH_OPTION_DELIMITER + index, "RESI", (!isV55() ? "INDIVIDUAL_EVENT_DETAIL" : null), "EVENT_DETAIL", "ADDRESS_STRUCTURE", "ADDR", "ADR1");
	}
	
	/**
	 * 
	 * 
	 * @param index
	 * @return
	 */
	public String getStreet2(int index) {
		return getValue("INDIVIDUAL_ATTRIBUTE_STRUCTURE;RESI" + GedcomNode.PATH_OPTION_DELIMITER + index, "RESI", (!isV55() ? "INDIVIDUAL_EVENT_DETAIL" : null), "EVENT_DETAIL", "ADDRESS_STRUCTURE", "ADDR", "ADR2");
	}
	
	/**
	 * 
	 * 
	 * @param index
	 * @return
	 */
	public boolean removeStreet2(int index) {
		return remove("INDIVIDUAL_ATTRIBUTE_STRUCTURE;RESI" + GedcomNode.PATH_OPTION_DELIMITER + index, "RESI", (!isV55() ? "INDIVIDUAL_EVENT_DETAIL" : null), "EVENT_DETAIL", "ADDRESS_STRUCTURE", "ADDR", "ADR2");
	}
	
	/**
	 * 
	 * 
	 * @param index
	 * @return
	 */
	public String getCity(int index) {
		return getValue("INDIVIDUAL_ATTRIBUTE_STRUCTURE;RESI" + GedcomNode.PATH_OPTION_DELIMITER + index, "RESI", (!isV55() ? "INDIVIDUAL_EVENT_DETAIL" : null), "EVENT_DETAIL", "ADDRESS_STRUCTURE", "ADDR", "CITY");
	}
	
	/**
	 * 
	 * 
	 * @param index
	 * @return
	 */
	public boolean removeCity(int index) {
		return remove("INDIVIDUAL_ATTRIBUTE_STRUCTURE;RESI" + GedcomNode.PATH_OPTION_DELIMITER + index, "RESI", (!isV55() ? "INDIVIDUAL_EVENT_DETAIL" : null), "EVENT_DETAIL", "ADDRESS_STRUCTURE", "ADDR", "CITY");
	}
	
	/**
	 * 
	 * 
	 * @param index
	 * @return
	 */
	public String getPost(int index) {
		return getValue("INDIVIDUAL_ATTRIBUTE_STRUCTURE;RESI" + GedcomNode.PATH_OPTION_DELIMITER + index, "RESI", (!isV55() ? "INDIVIDUAL_EVENT_DETAIL" : null), "EVENT_DETAIL", "ADDRESS_STRUCTURE", "ADDR", "POST");
	}
	
	/**
	 * 
	 * 
	 * @param index
	 * @return
	 */
	public boolean removePost(int index) {
		return remove("INDIVIDUAL_ATTRIBUTE_STRUCTURE;RESI" + GedcomNode.PATH_OPTION_DELIMITER + index, "RESI", (!isV55() ? "INDIVIDUAL_EVENT_DETAIL" : null), "EVENT_DETAIL", "ADDRESS_STRUCTURE", "ADDR", "POST");
	}
	
	/**
	 * 
	 * 
	 * @param index
	 * @return
	 */
	public String getCountry(int index) {
		return getValue("INDIVIDUAL_ATTRIBUTE_STRUCTURE;RESI" + GedcomNode.PATH_OPTION_DELIMITER + index, "RESI", (!isV55() ? "INDIVIDUAL_EVENT_DETAIL" : null), "EVENT_DETAIL", "ADDRESS_STRUCTURE", "ADDR", "CTRY");
	}
	
	/**
	 * 
	 * 
	 * @param index
	 * @return
	 */
	public boolean removeCountry(int index) {
		return remove("INDIVIDUAL_ATTRIBUTE_STRUCTURE;RESI" + GedcomNode.PATH_OPTION_DELIMITER + index, "RESI", (!isV55() ? "INDIVIDUAL_EVENT_DETAIL" : null), "EVENT_DETAIL", "ADDRESS_STRUCTURE", "ADDR", "CTRY");
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public int getNumberOfAddresses() {
		return getNumberOfLines("INDIVIDUAL_ATTRIBUTE_STRUCTURE;RESI");
	}
	
	/**
	 * 
	 * 
	 * @param index
	 * @param phoneIndex
	 * @return
	 */
	public String getPhone(int index, int phoneIndex) {
		return getValue("INDIVIDUAL_ATTRIBUTE_STRUCTURE;RESI" + GedcomNode.PATH_OPTION_DELIMITER + index, "RESI", (!isV55() ? "INDIVIDUAL_EVENT_DETAIL" : null), "EVENT_DETAIL", "ADDRESS_STRUCTURE", "PHON" + GedcomNode.PATH_OPTION_DELIMITER + phoneIndex);
	}
	
	/**
	 * 
	 * 
	 * @param index
	 * @return
	 */
	public List<String> getPhones(int index) {
		LinkedList<String> result = new LinkedList<>();
		int count = getNumberOfPhones(index);
		GedcomNode node = followPath("INDIVIDUAL_ATTRIBUTE_STRUCTURE;RESI" + GedcomNode.PATH_OPTION_DELIMITER + index, "RESI", (!isV55() ? "INDIVIDUAL_EVENT_DETAIL" : null), "EVENT_DETAIL", "ADDRESS_STRUCTURE");
		
		while (count > 0) {
			result.add(getValue(node, "PHON" + GedcomNode.PATH_OPTION_DELIMITER + (--count)));
		}
		return result;
	}
	
	/**
	 * 
	 * 
	 * @param index
	 * @param phoneIndex
	 * @return
	 */
	public boolean removePhone(int index, int phoneIndex) {
		return remove("INDIVIDUAL_ATTRIBUTE_STRUCTURE;RESI" + GedcomNode.PATH_OPTION_DELIMITER + index, "RESI", (!isV55() ? "INDIVIDUAL_EVENT_DETAIL" : null), "EVENT_DETAIL", "ADDRESS_STRUCTURE", "PHON" + GedcomNode.PATH_OPTION_DELIMITER + phoneIndex);
	}
	
	/**
	 * 
	 * 
	 * @param index
	 * @return
	 */
	public int getNumberOfPhones(int index) {
		return getNumberOfLines("INDIVIDUAL_ATTRIBUTE_STRUCTURE;RESI" + GedcomNode.PATH_OPTION_DELIMITER + index, "RESI", (!isV55() ? "INDIVIDUAL_EVENT_DETAIL" : null), "EVENT_DETAIL", "ADDRESS_STRUCTURE", "PHON");
	}
	
	/**
	 * 
	 * 
	 * @param index Index of address structure
	 * @param emailIndex Index of e-mail within address structure
	 * @return
	 */
	public String getEMail(int index, int emailIndex) {
		if (isV55()) {
			throw new GedcomAccessError("ADDRESS_STRUCTURE->EMAIL not available in GEDCOM v5.5");
		}
		
		return getValue("INDIVIDUAL_ATTRIBUTE_STRUCTURE;RESI" + GedcomNode.PATH_OPTION_DELIMITER + index, "RESI", (!isV55() ? "INDIVIDUAL_EVENT_DETAIL" : null), "EVENT_DETAIL", "ADDRESS_STRUCTURE", "EMAIL" + GedcomNode.PATH_OPTION_DELIMITER + emailIndex);
	}
	
	/**
	 * 
	 * 
	 * @param index
	 * @return
	 */
	public List<String> getEMails(int index) {
		LinkedList<String> result = new LinkedList<>();
		int count = getNumberOfPhones(index);
		GedcomNode node = followPath("INDIVIDUAL_ATTRIBUTE_STRUCTURE;RESI" + GedcomNode.PATH_OPTION_DELIMITER + index, "RESI", (!isV55() ? "INDIVIDUAL_EVENT_DETAIL" : null), "EVENT_DETAIL", "ADDRESS_STRUCTURE");
		
		while (count > 0) {
			result.add(getValue(node, "EMAIL" + GedcomNode.PATH_OPTION_DELIMITER + (--count)));
		}
		return result;
	}
	
	/**
	 * 
	 * 
	 * @param index Index of address structure
	 * @param emailIndex Index of e-mail within address structure
	 * @return
	 */
	public boolean removeEMail(int index, int emailIndex) {
		if (isV55()) {
			throw new GedcomAccessError("ADDRESS_STRUCTURE->EMAIL not available in GEDCOM v5.5");
		}
		
		return remove("INDIVIDUAL_ATTRIBUTE_STRUCTURE;RESI" + GedcomNode.PATH_OPTION_DELIMITER + index, "RESI", (!isV55() ? "INDIVIDUAL_EVENT_DETAIL" : null), "EVENT_DETAIL", "ADDRESS_STRUCTURE", "EMAIL" + GedcomNode.PATH_OPTION_DELIMITER + emailIndex);
	}
	
	/**
	 * 
	 * 
	 * @param index
	 * @return
	 */
	public int getNumberOfEMails(int index) {
		return getNumberOfLines("INDIVIDUAL_ATTRIBUTE_STRUCTURE;RESI" + GedcomNode.PATH_OPTION_DELIMITER + index, "RESI", (!isV55() ? "INDIVIDUAL_EVENT_DETAIL" : null), "EVENT_DETAIL", "ADDRESS_STRUCTURE", "EMAIL");
	}
	
	/**
	 * 
	 * 
	 * @param index
	 * @param faxIndex
	 * @return
	 */
	public String getFax(int index, int faxIndex) {
		return getValue("INDIVIDUAL_ATTRIBUTE_STRUCTURE;RESI" + GedcomNode.PATH_OPTION_DELIMITER + index, "RESI", (!isV55() ? "INDIVIDUAL_EVENT_DETAIL" : null), "EVENT_DETAIL", "ADDRESS_STRUCTURE", "FAX" + GedcomNode.PATH_OPTION_DELIMITER + faxIndex);
	}
	
	/**
	 * 
	 * 
	 * @param index
	 * @return
	 */
	public List<String> getFaxes(int index) {
		LinkedList<String> result = new LinkedList<>();
		int count = getNumberOfPhones(index);
		GedcomNode node = followPath("INDIVIDUAL_ATTRIBUTE_STRUCTURE;RESI" + GedcomNode.PATH_OPTION_DELIMITER + index, "RESI", (!isV55() ? "INDIVIDUAL_EVENT_DETAIL" : null), "EVENT_DETAIL", "ADDRESS_STRUCTURE");
		
		while (count > 0) {
			result.add(getValue(node, "FAX" + GedcomNode.PATH_OPTION_DELIMITER + (--count)));
		}
		return result;
	}
	
	/**
	 * 
	 * 
	 * @param index
	 * @param faxIndex
	 * @return
	 */
	public boolean removeFax(int index, int faxIndex) {
		return remove("INDIVIDUAL_ATTRIBUTE_STRUCTURE;RESI" + GedcomNode.PATH_OPTION_DELIMITER + index, "RESI", (!isV55() ? "INDIVIDUAL_EVENT_DETAIL" : null), "EVENT_DETAIL", "ADDRESS_STRUCTURE", "FAX" + GedcomNode.PATH_OPTION_DELIMITER + faxIndex);
	}
	
	/**
	 * 
	 * 
	 * @param index
	 * @return
	 */
	public int getNumberOfFax(int index) {
		return getNumberOfLines("INDIVIDUAL_ATTRIBUTE_STRUCTURE;RESI" + GedcomNode.PATH_OPTION_DELIMITER + index, "RESI", (!isV55() ? "INDIVIDUAL_EVENT_DETAIL" : null), "EVENT_DETAIL", "ADDRESS_STRUCTURE", "FAX");
	}
	
	/**
	 * 
	 * 
	 * @param index
	 * @param wwwIndex
	 * @return
	 */
	public String getWebsite(int index, int wwwIndex) {
		return getValue("INDIVIDUAL_ATTRIBUTE_STRUCTURE;RESI" + GedcomNode.PATH_OPTION_DELIMITER + index, "RESI", (!isV55() ? "INDIVIDUAL_EVENT_DETAIL" : null), "EVENT_DETAIL", "ADDRESS_STRUCTURE", "WWW" + GedcomNode.PATH_OPTION_DELIMITER + wwwIndex);
	}
	
	/**
	 * 
	 * 
	 * @param index
	 * @return
	 */
	public List<String> getWebsites(int index) {
		LinkedList<String> result = new LinkedList<>();
		int count = getNumberOfPhones(index);
		GedcomNode node = followPath("INDIVIDUAL_ATTRIBUTE_STRUCTURE;RESI" + GedcomNode.PATH_OPTION_DELIMITER + index, "RESI", (!isV55() ? "INDIVIDUAL_EVENT_DETAIL" : null), "EVENT_DETAIL", "ADDRESS_STRUCTURE");
		
		while (count > 0) {
			result.add(getValue(node, "WWW" + GedcomNode.PATH_OPTION_DELIMITER + (--count)));
		}
		return result;
	}
	
	/**
	 * 
	 * 
	 * @param index
	 * @param wwwIndex
	 * @return
	 */
	public boolean removeWebsite(int index, int wwwIndex) {
		return remove("INDIVIDUAL_ATTRIBUTE_STRUCTURE;RESI" + GedcomNode.PATH_OPTION_DELIMITER + index, "RESI", (!isV55() ? "INDIVIDUAL_EVENT_DETAIL" : null), "EVENT_DETAIL", "ADDRESS_STRUCTURE", "WWW" + GedcomNode.PATH_OPTION_DELIMITER + wwwIndex);
	}
	
	/**
	 * 
	 * 
	 * @param index
	 * @return
	 */
	public int getNumberOfWebsites(int index) {
		return getNumberOfLines("INDIVIDUAL_ATTRIBUTE_STRUCTURE;RESI" + GedcomNode.PATH_OPTION_DELIMITER + index, "RESI", (!isV55() ? "INDIVIDUAL_EVENT_DETAIL" : null), "EVENT_DETAIL", "ADDRESS_STRUCTURE", "WWW");
	}
	
	/**
	 * Links this individual as spouse to its family
	 * 
	 * @param familyId
	 * @return
	 */
	public boolean addSpouseFamilyLink(String familyId) {
		return createAndSet(new GedcomXRef(true, familyId, 
				"SPOUSE_TO_FAMILY_LINK", "FAMS"));
	}
	
	/**
	 * 
	 * 
	 * @param familyId
	 * @return
	 */
	public boolean setSpouseFamilyLink(int index, String familyId) {
		return createAndSet(new GedcomXRef(false, familyId, 
				"SPOUSE_TO_FAMILY_LINK" + GedcomNode.PATH_OPTION_DELIMITER + index, "FAMS"));
	}
	
	/**
	 * 
	 * 
	 * @param index
	 * @return
	 */
	public String getSpouseFamilyLink(int index) {
		return getXRef("SPOUSE_TO_FAMILY_LINK" + GedcomNode.PATH_OPTION_DELIMITER + index, "FAMS");
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public List<String> getSpouseFamilyLinks() {
		LinkedList<String> result = new LinkedList<>();
		int count = getNumberOfSpouseFamilyLinks();
		while (count > 0) {
			result.add(getSpouseFamilyLink(--count));
		}
		return result;
	}
	
	/**
	 * 
	 * 
	 * @param index
	 * @return
	 */
	public boolean removeSpouseFamilyLink(int index) {
		return remove("SPOUSE_TO_FAMILY_LINK" + GedcomNode.PATH_OPTION_DELIMITER + index, "FAMS");
	}
	
	
	/**
	 * 
	 * 
	 * @param index
	 * @return
	 */
	public int getNumberOfSpouseFamilyLinks() {
		return getNumberOfLines("SPOUSE_TO_FAMILY_LINK");
	}
	
	/**
	 * Links this individual as child to its family
	 * 
	 * @param chilId
	 * @return
	 */
	public boolean addChildFamilyLink(String chilId) {
		return createAndSet(new GedcomXRef(true, chilId, 
				"CHILD_TO_FAMILY_LINK", "FAMC"));
	}
	
	/**
	 * 
	 * 
	 * @param chilId
	 * @return
	 */
	public boolean setChildFamilyLink(int index, String chilId) {
		return createAndSet(new GedcomXRef(false, chilId, 
				"CHILD_TO_FAMILY_LINK" + GedcomNode.PATH_OPTION_DELIMITER + index, "FAMC"));
	}
	
	/**
	 * 
	 * 
	 * @param index
	 * @return
	 */
	public String getChildFamilyLink(int index) {
		return getXRef("CHILD_TO_FAMILY_LINK" + GedcomNode.PATH_OPTION_DELIMITER + index, "FAMC");
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public List<String> getChildFamilyLinks() {
		LinkedList<String> result = new LinkedList<>();
		int count = getNumberOfChildFamilyLinks();
		while (count > 0) {
			result.add(getChildFamilyLink(--count));
		}
		return result;
	}
	
	/**
	 * 
	 * 
	 * @param index
	 * @return
	 */
	public boolean removeChildFamilyLink(int index) {
		return remove("CHILD_TO_FAMILY_LINK" + GedcomNode.PATH_OPTION_DELIMITER + index, "FAMC");
	}
	
	/**
	 * 
	 * 
	 * @param index
	 * @return
	 */
	public int getNumberOfChildFamilyLinks() {
		return getNumberOfLines("CHILD_TO_FAMILY_LINK");
	}
	
	
	@Override
	public String toString() {
		return getId() + ": " + getName(0);
	}
	
}
