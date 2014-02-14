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


import java.util.ArrayList;
import java.util.Arrays;

import ch.thn.gedcom.GedcomFormatter;
import ch.thn.gedcom.creator.GedcomCreatorEnums.NameType;
import ch.thn.gedcom.creator.GedcomCreatorEnums.Sex;
import ch.thn.gedcom.creator.GedcomCreatorEnums.YesNo;
import ch.thn.gedcom.data.GedcomNode;
import ch.thn.gedcom.store.GedcomStore;

/**
 * @author Thomas Naeff (github.com/thnaeff)
 *
 */
public class GedcomCreatorIndividual extends GedcomCreatorStructure {
		
	
	/**
	 * An INDIVIDUAL_RECORD
	 * 
	 * @param store
	 * @param id
	 */
	public GedcomCreatorIndividual(GedcomStore store, String id) {
		super(store, "INDIVIDUAL_RECORD", "INDI");
		
		if (!setId(id)) {
			throw new GedcomCreatorError("Failed to create individual with ID " + 
					id + ". Id could not be set.");
		}
	}
	
	/**
	 * 
	 * 
	 * @param store
	 * @param node
	 */
	public GedcomCreatorIndividual(GedcomStore store, GedcomNode node) {
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
		
		return apply(new GedcomXRef(false, id));
	}
	
	/**
	 * 
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
		return apply(new GedcomValue(false, sex.value, 
				"SEX"));
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public Sex getSex() {
		String sex = getValue("SEX");
		
		if (Sex.MALE.value.equals(sex)) {
			return Sex.MALE;
		} else if (Sex.FEMALE.value.equals(sex)) {
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
		GedcomValue born = new GedcomValue(false, (isBorn ? YesNo.YES.value : null), 
				"INDIVIDUAL_EVENT_STRUCTURE;BIRT", "BIRT");
		
		GedcomValue date = new GedcomValue(false, birthDate, born, 
				"INDIVIDUAL_EVENT_DETAIL", "EVENT_DETAIL", "DATE");
		
		return apply(born, date);
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public boolean isBorn() {
		String born = getValue("INDIVIDUAL_EVENT_STRUCTURE;BIRT", "BIRT");
		
		if (YesNo.YES.value.equals(born)) {
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
	public String getBirthDate() {
		return getValue("INDIVIDUAL_EVENT_STRUCTURE;BIRT", "BIRT", 
				"INDIVIDUAL_EVENT_DETAIL", "EVENT_DETAIL", "DATE");
	}
	
	/**
	 * 
	 * 
	 * @param isDead
	 * @param deathDate
	 * @return
	 */
	public boolean setDeath(boolean isDead, String deathDate) {
		GedcomValue dead = new GedcomValue(false, (isDead ? YesNo.YES.value : null), 
				"INDIVIDUAL_EVENT_STRUCTURE;DEAT", "DEAT");
		
		GedcomValue date = new GedcomValue(false, deathDate, dead, 
				"INDIVIDUAL_EVENT_DETAIL", "EVENT_DETAIL", "DATE");
		
		return apply(dead, date);
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public boolean isDead() {
		String born = getValue("INDIVIDUAL_EVENT_STRUCTURE;DEAT", "DEAT");
		
		if (YesNo.YES.value.equals(born)) {
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
	public String getDeathDate() {
		return getValue("INDIVIDUAL_EVENT_STRUCTURE;DEAT", "DEAT", 
				"INDIVIDUAL_EVENT_DETAIL", "EVENT_DETAIL", "DATE");
	}
	
	/**
	 * 
	 * 
	 * @param occupation
	 * @return
	 */
	public boolean setOccupation(String occupation) {
		return apply(new GedcomValue(false, occupation, 
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
	 * @param education
	 * @return
	 */
	public boolean setEducation(String education) {
		return apply(new GedcomValue(false, education, 
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
				"PERSONAL_NAME_PIECES", "GIVN");
		
		GedcomValue surn = new GedcomValue(add, name, n, 
				"PERSONAL_NAME_PIECES", "SURN");
		
		GedcomValue type = new GedcomValue(add, nameType.value, n, 
				"TYPE");
		
		return apply(n, givn, surn, type);
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
	 * given name = first name = forename
	 * 
	 * @param index
	 * @return
	 */
	public String getGivenName(int index) {
		return getValue("PERSONAL_NAME_STRUCTURE" + GedcomNode.PATH_OPTION_DELIMITER + index, "NAME", "PERSONAL_NAME_PIECES", "GIVN");
	}
	
	/**
	 * surname = family name = name = last name<br>
	 * Family name = surname (at least in western context -> http://en.wikipedia.org/wiki/Family_name)
	 * 
	 * @param index
	 * @return
	 */
	public String getSurname(int index) {
		return getValue("PERSONAL_NAME_STRUCTURE" + GedcomNode.PATH_OPTION_DELIMITER + index, "NAME", "PERSONAL_NAME_PIECES", "SURN");
	}
	
	/**
	 * 
	 * 
	 * @param index
	 * @return
	 */
	public String getNameType(int index) {
		return getValue("PERSONAL_NAME_STRUCTURE" + GedcomNode.PATH_OPTION_DELIMITER + index, "NAME", "TYPE");
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
				"INDIVIDUAL_ATTRIBUTE_STRUCTURE;RESI" + indexString, "RESI", "INDIVIDUAL_EVENT_DETAIL", "EVENT_DETAIL", "ADDRESS_STRUCTURE");
		
		
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
		
		
		
		return apply(addrstruct, addr, adr1, adr2, cit, pos, ctry, 
				phon1, phon2, phon3, email1, email2, email3, fax1, fax2, fax3, www1, www2, www3);
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
	 * 
	 * 
	 * @param index
	 * @return
	 */
	public String getAddress(int index) {
		return getValue("INDIVIDUAL_ATTRIBUTE_STRUCTURE;RESI" + GedcomNode.PATH_OPTION_DELIMITER + index, "RESI", "INDIVIDUAL_EVENT_DETAIL", "EVENT_DETAIL", "ADDRESS_STRUCTURE", "ADDR");
	}
	
	/**
	 * 
	 * 
	 * @param index
	 * @return
	 */
	public String getStreet1(int index) {
		return getValue("INDIVIDUAL_ATTRIBUTE_STRUCTURE;RESI" + GedcomNode.PATH_OPTION_DELIMITER + index, "RESI", "INDIVIDUAL_EVENT_DETAIL", "EVENT_DETAIL", "ADDRESS_STRUCTURE", "ADDR", "ADR1");
	}
	
	/**
	 * 
	 * 
	 * @param index
	 * @return
	 */
	public String getStreet2(int index) {
		return getValue("INDIVIDUAL_ATTRIBUTE_STRUCTURE;RESI" + GedcomNode.PATH_OPTION_DELIMITER + index, "RESI", "INDIVIDUAL_EVENT_DETAIL", "EVENT_DETAIL", "ADDRESS_STRUCTURE", "ADDR", "ADR2");
	}
	
	/**
	 * 
	 * 
	 * @param index
	 * @return
	 */
	public String getCity(int index) {
		return getValue("INDIVIDUAL_ATTRIBUTE_STRUCTURE;RESI" + GedcomNode.PATH_OPTION_DELIMITER + index, "RESI", "INDIVIDUAL_EVENT_DETAIL", "EVENT_DETAIL", "ADDRESS_STRUCTURE", "ADDR", "CITY");
	}
	
	/**
	 * 
	 * 
	 * @param index
	 * @return
	 */
	public String getPost(int index) {
		return getValue("INDIVIDUAL_ATTRIBUTE_STRUCTURE;RESI" + GedcomNode.PATH_OPTION_DELIMITER + index, "RESI", "INDIVIDUAL_EVENT_DETAIL", "EVENT_DETAIL", "ADDRESS_STRUCTURE", "ADDR", "POST");
	}
	
	/**
	 * 
	 * 
	 * @param index
	 * @return
	 */
	public String getCountry(int index) {
		return getValue("INDIVIDUAL_ATTRIBUTE_STRUCTURE;RESI" + GedcomNode.PATH_OPTION_DELIMITER + index, "RESI", "INDIVIDUAL_EVENT_DETAIL", "EVENT_DETAIL", "ADDRESS_STRUCTURE", "ADDR", "CTRY");
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
		return getValue("INDIVIDUAL_ATTRIBUTE_STRUCTURE;RESI" + GedcomNode.PATH_OPTION_DELIMITER + index, "RESI", "INDIVIDUAL_EVENT_DETAIL", "EVENT_DETAIL", "ADDRESS_STRUCTURE", "PHON" + GedcomNode.PATH_OPTION_DELIMITER + phoneIndex);
	}
	
	/**
	 * 
	 * 
	 * @param index
	 * @return
	 */
	public int getNumberOfPhones(int index) {
		return getNumberOfLines("INDIVIDUAL_ATTRIBUTE_STRUCTURE;RESI" + GedcomNode.PATH_OPTION_DELIMITER + index, "RESI", "INDIVIDUAL_EVENT_DETAIL", "EVENT_DETAIL", "ADDRESS_STRUCTURE", "PHON");
	}
	
	/**
	 * 
	 * 
	 * @param index Index of address structure
	 * @param emailIndex Index of e-mail within address structure
	 * @return
	 */
	public String getEMail(int index, int emailIndex) {
		return getValue("INDIVIDUAL_ATTRIBUTE_STRUCTURE;RESI" + GedcomNode.PATH_OPTION_DELIMITER + index, "RESI", "INDIVIDUAL_EVENT_DETAIL", "EVENT_DETAIL", "ADDRESS_STRUCTURE", "EMAIL" + GedcomNode.PATH_OPTION_DELIMITER + emailIndex);
	}
	
	/**
	 * 
	 * 
	 * @param index
	 * @return
	 */
	public int getNumberOfEMails(int index) {
		return getNumberOfLines("INDIVIDUAL_ATTRIBUTE_STRUCTURE;RESI" + GedcomNode.PATH_OPTION_DELIMITER + index, "RESI", "INDIVIDUAL_EVENT_DETAIL", "EVENT_DETAIL", "ADDRESS_STRUCTURE", "EMAIL");
	}
	
	/**
	 * 
	 * 
	 * @param index
	 * @param faxIndex
	 * @return
	 */
	public String getFax(int index, int faxIndex) {
		return getValue("INDIVIDUAL_ATTRIBUTE_STRUCTURE;RESI" + GedcomNode.PATH_OPTION_DELIMITER + index, "RESI", "INDIVIDUAL_EVENT_DETAIL", "EVENT_DETAIL", "ADDRESS_STRUCTURE", "FAX" + GedcomNode.PATH_OPTION_DELIMITER + faxIndex);
	}
	
	/**
	 * 
	 * 
	 * @param index
	 * @return
	 */
	public int getNumberOfFax(int index) {
		return getNumberOfLines("INDIVIDUAL_ATTRIBUTE_STRUCTURE;RESI" + GedcomNode.PATH_OPTION_DELIMITER + index, "RESI", "INDIVIDUAL_EVENT_DETAIL", "EVENT_DETAIL", "ADDRESS_STRUCTURE", "FAX");
	}
	
	/**
	 * 
	 * 
	 * @param index
	 * @param wwwIndex
	 * @return
	 */
	public String getWebsite(int index, int wwwIndex) {
		return getValue("INDIVIDUAL_ATTRIBUTE_STRUCTURE;RESI" + GedcomNode.PATH_OPTION_DELIMITER + index, "RESI", "INDIVIDUAL_EVENT_DETAIL", "EVENT_DETAIL", "ADDRESS_STRUCTURE", "WWW" + GedcomNode.PATH_OPTION_DELIMITER + wwwIndex);
	}
	
	/**
	 * 
	 * 
	 * @param index
	 * @return
	 */
	public int getNumberOfWebsites(int index) {
		return getNumberOfLines("INDIVIDUAL_ATTRIBUTE_STRUCTURE;RESI" + GedcomNode.PATH_OPTION_DELIMITER + index, "RESI", "INDIVIDUAL_EVENT_DETAIL", "EVENT_DETAIL", "ADDRESS_STRUCTURE", "WWW");
	}
	
	/**
	 * Links this individual as spouse to its family
	 * 
	 * @param familyId
	 * @return
	 */
	public boolean addSpouseLink(String familyId) {
		return apply(new GedcomXRef(true, familyId, 
				"SPOUSE_TO_FAMILY_LINK", "FAMS"));
	}
	
	/**
	 * 
	 * 
	 * @param familyId
	 * @return
	 */
	public boolean setSpouseLink(int index, String familyId) {
		return apply(new GedcomXRef(false, familyId, 
				"SPOUSE_TO_FAMILY_LINK" + GedcomNode.PATH_OPTION_DELIMITER + index, "FAMS"));
	}
	
	/**
	 * 
	 * 
	 * @param index
	 * @return
	 */
	public String getSpouseLink(int index) {
		return getXRef("SPOUSE_TO_FAMILY_LINK" + GedcomNode.PATH_OPTION_DELIMITER + index, "FAMS");
	}
	
	
	/**
	 * 
	 * 
	 * @param index
	 * @return
	 */
	public int getNumberOfSpouseLinks() {
		return getNumberOfLines("SPOUSE_TO_FAMILY_LINK");
	}
	
	/**
	 * Links this individual as child to its family
	 * 
	 * @param chilId
	 * @return
	 */
	public boolean addChildLink(String chilId) {
		return apply(new GedcomXRef(true, chilId, 
				"CHILD_TO_FAMILY_LINK", "FAMC"));
	}
	
	/**
	 * 
	 * 
	 * @param chilId
	 * @return
	 */
	public boolean setChildLink(int index, String chilId) {
		return apply(new GedcomXRef(false, chilId, 
				"CHILD_TO_FAMILY_LINK" + GedcomNode.PATH_OPTION_DELIMITER + index, "FAMC"));
	}
	
	/**
	 * 
	 * 
	 * @param index
	 * @return
	 */
	public String getChildLink(int index) {
		return getXRef("CHILD_TO_FAMILY_LINK" + GedcomNode.PATH_OPTION_DELIMITER + index, "FAMC");
	}
	
	/**
	 * 
	 * 
	 * @param index
	 * @return
	 */
	public int getNumberOfChildLinks() {
		return getNumberOfLines("CHILD_TO_FAMILY_LINK");
	}
	
}
