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
import java.util.Date;

import ch.thn.gedcom.GedcomFormatter;
import ch.thn.gedcom.creator.GedcomCreatorEnums.*;
import ch.thn.gedcom.data.GedcomError;
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
		
		addLines(new XRefLine("INDI", id, followPathCreate()));
		
	}
	
	/**
	 * 
	 * 
	 * @param store
	 * @param node
	 */
	public GedcomCreatorIndividual(GedcomStore store, GedcomNode node) {
		super(store, "INDIVIDUAL_RECORD", node);
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public String getId() {
		return getXRef("INDI", 0);
	}
	
	/**
	 * 
	 * 
	 * @param value
	 * @return
	 */
	public boolean setSex(Sex value) {
		if (setValue("SEX", value.value)) {
			return true;
		}
		
		try {
			Line line1 = new ValueLine(
					"SEX", 
					value.value, 
					followPathCreate("SEX"));
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
	public String getSex() {
		return getValue("SEX", 0, "SEX");
	}
	
	/**
	 * 
	 * 
	 * @param hasBirth
	 * @param birthDate
	 * @return
	 */
	public boolean setBirth(boolean hasBirth, Date birthDate) {
		if (setValue("BIRT", (hasBirth ? YesNo.YES.value : null)) 
				&& setValue("BIRT-DATE", GedcomFormatter.getDate(birthDate))) {
			return true;
		}
		
		try {
			Line line1 = new ValueLine(
					"BIRT", 
					(hasBirth ? YesNo.YES.value : null), 
					followPathCreate("INDIVIDUAL_EVENT_STRUCTURE;BIRT", "BIRT"));
			Line line2 = new ValueLine(
					"BIRT-DATE", 
					GedcomFormatter.getDate(birthDate), 
					followPathCreate(line1.node, "INDIVIDUAL_EVENT_DETAIL", "EVENT_DETAIL", "DATE"));
			
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
	public boolean getBirth() {
		return getValue("BIRT", 0, "INDIVIDUAL_EVENT_STRUCTURE;BIRT", "BIRT").equals(YesNo.YES.value);
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public String getBirthDate() {
		return getValue("BIRT-DATE", 0, "INDIVIDUAL_EVENT_STRUCTURE;BIRT", "BIRT", "INDIVIDUAL_EVENT_DETAIL", "EVENT_DETAIL", "DATE");
	}

	/**
	 * 
	 * 
	 * @param isDeath
	 * @param deathDate
	 * @return
	 */
	public boolean setDeath(boolean isDeath, Date deathDate) {
		if (setValue("DEAT", (isDeath ? YesNo.YES.value : null)) 
				&& setValue("DEAT-DATE", GedcomFormatter.getDate(deathDate))) {
			return true;
		}
		
		try {
			Line line1 = new ValueLine(
					"DEAT", 
					(isDeath ? YesNo.YES.value : null), 
					followPathCreate("INDIVIDUAL_EVENT_STRUCTURE;DEAT", "DEAT"));
			Line line2 = new ValueLine(
					"DEAT-DATE", 
					GedcomFormatter.getDate(deathDate), 
					followPathCreate(line1.node, "INDIVIDUAL_EVENT_DETAIL", "EVENT_DETAIL", "DATE"));
			
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
	public boolean getDeath() {
		return getValue("DEAT", 0, "INDIVIDUAL_EVENT_STRUCTURE;DEAT", "DEAT").equals(YesNo.YES.value);
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public String getDeathDate() {
		return getValue("DEAT-DATE", 0, "INDIVIDUAL_EVENT_STRUCTURE;DEAT", "DEAT", "INDIVIDUAL_EVENT_DETAIL", "EVENT_DETAIL", "DATE");
	}
	
	/**
	 * 
	 * 
	 * @param value
	 * @return
	 */
	public boolean setOccupation(String value) {
		if (setValue("OCCU", value)) {
			return true;
		}
		
		try {
			Line line1 = new ValueLine(
					"OCCU", 
					value, 
					followPathCreate("INDIVIDUAL_ATTRIBUTE_STRUCTURE;OCCU", "OCCU"));
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
	public String getOccupation() {
		return getValue("OCCU", 0, "INDIVIDUAL_ATTRIBUTE_STRUCTURE;OCCU", "OCCU");
	}
	
	/**
	 * 
	 * 
	 * @param value
	 * @return
	 */
	public boolean setEducation(String value) {
		if (setValue("EDUC", value)) {
			return true;
		}
		
		try {
			Line line1 = new ValueLine(
					"EDUC", 
					value, 
					followPathCreate("INDIVIDUAL_ATTRIBUTE_STRUCTURE;EDUC", "EDUC"));
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
	public String getEducation() {
		return getValue("EDUC", 0, "INDIVIDUAL_ATTRIBUTE_STRUCTURE;EDUC", "EDUC");
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
		String firstNamesString = GedcomFormatter.makeStringList(
				Arrays.asList(firstNames), ", ", "", "", false, null, false).toString();
		
		try {
			Line line1 = new ValueLine(
					"NAME", 
					firstNamesString + " /" + name + "/", 
					followPathCreate("PERSONAL_NAME_STRUCTURE", "NAME"));
			Line line2 = new ValueLine(
					"GIVN", 
					firstNamesString, 
					followPathCreate(line1.node, "PERSONAL_NAME_PIECES", "GIVN"));
			Line line3 = new ValueLine(
					"SURN", 
					name, 
					followPathCreate(line1.node, "PERSONAL_NAME_PIECES", "SURN"));
			Line line4 = new ValueLine(
					"NAME-TYPE", 
					nameType.value, 
					followPathCreate(line1.node, "TYPE"));
			
			return addLines(line1, line2, line3, line4);
		} catch (GedcomError e) {
			throw e;
		}
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
		String firstNamesString = GedcomFormatter.makeStringList(
				Arrays.asList(firstNames), ", ", "", "", false, null, false).toString();
		
		if (setValue("NAME", index, firstNamesString + " /" + name + "/") 
				&& setValue("GIVN", index, firstNamesString) 
				&& setValue("SURN", index, name) 
				&& setValue("NAME-TYPE", index, nameType.value)) {
			return true;
		}
		
		return addName(firstNamesString, nameType, firstNames);
	}
	
	/**
	 * 
	 * 
	 * @param index
	 * @return
	 */
	public String getName(int index) {
		return getValue("NAME", index, "PERSONAL_NAME_STRUCTURE" + GedcomNode.PATH_OPTION_DELIMITER + index, "NAME");
	}
	
	/**
	 * 
	 * 
	 * @param index
	 * @return
	 */
	public String getGivenName(int index) {
		return getValue("GIVN", index, "PERSONAL_NAME_STRUCTURE" + GedcomNode.PATH_OPTION_DELIMITER + index, "NAME", "PERSONAL_NAME_PIECES", "GIVN");
	}
	
	/**
	 * 
	 * 
	 * @param index
	 * @return
	 */
	public String getSurname(int index) {
		return getValue("SURN", index, "PERSONAL_NAME_STRUCTURE" + GedcomNode.PATH_OPTION_DELIMITER + index, "NAME", "PERSONAL_NAME_PIECES", "SURN");
	}
	
	/**
	 * 
	 * 
	 * @param index
	 * @return
	 */
	public String getNameType(int index) {
		return getValue("NAME-TYPE", index, "PERSONAL_NAME_STRUCTURE" + GedcomNode.PATH_OPTION_DELIMITER + index, "NAME", "TYPE");
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
		//Create the address string. Each string part should not have any commas since 
		//commas are used to separate the string parts.
		ArrayList<String> s = new ArrayList<String>(5);
		if (street1 != null) s.add(street1.replaceAll(",", ""));
		if (street2 != null) s.add(street2.replaceAll(",", ""));
		if (post != null) s.add(post.replaceAll(",", ""));
		if (city != null) s.add(city.replaceAll(",", ""));
		if (country != null) s.add(country.replaceAll(",", ""));
		String addrString = GedcomFormatter.makeStringList(s, ", ", null, null, true, "", false).toString();
		
		int numberOfAddresses = getNumberOfAddresses();
		
		GedcomNode addr = createPathEnd("INDIVIDUAL_ATTRIBUTE_STRUCTURE;RESI", "RESI", "INDIVIDUAL_EVENT_DETAIL", "EVENT_DETAIL", "ADDRESS_STRUCTURE");

		try {
			Line line1 = new ValueLine(
					"ADDR", 
					addrString, 
					followPathCreate(addr, "ADDR"));
			Line line2 = new ValueLine(
					"ADR1", 
					street1, 
					followPathCreate(line1.node, "ADR1"));
			Line line3 = new ValueLine(
					"ADR2", 
					street2, 
					followPathCreate(line1.node, "ADR2"));
			Line line4 = new ValueLine(
					"CITY", 
					city, 
					followPathCreate(line1.node, "CITY"));
			Line line5 = new ValueLine(
					"POST", 
					post, 
					followPathCreate(line1.node, "POST"));
			Line line6 = new ValueLine(
					"CTRY", 
					country, 
					followPathCreate(line1.node, "CTRY"));
			
			Line[] line7 = createMultiValueLine(phone, "PHON", numberOfAddresses, addr, "PHON");
			Line[] line8 = createMultiValueLine(email, "EMAIL", numberOfAddresses, addr, "EMAIL");
			Line[] line9 = createMultiValueLine(fax, "FAX", numberOfAddresses, addr, "FAX");
			Line[] line10 = createMultiValueLine(websites, "WWW", numberOfAddresses, addr, "WWW");
			
			if (addLines(line1, line2, line3, line4, line5, line6)) {
				if (addLines(line7, line8, line9, line10)) {
					return true;
				}
			}
			
			return false;
		} catch (GedcomError e) {
			throw e;
		}
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
		//Create the address string. Each string part should not have any commas since 
		//commas are used to separate the string parts.
		ArrayList<String> s = new ArrayList<String>(5);
		if (street1 != null) s.add(street1.replaceAll(",", ""));
		if (street2 != null) s.add(street2.replaceAll(",", ""));
		if (post != null) s.add(post.replaceAll(",", ""));
		if (city != null) s.add(city.replaceAll(",", ""));
		if (country != null) s.add(country.replaceAll(",", ""));
		String addrString = GedcomFormatter.makeStringList(s, ", ", null, null, true, "", false).toString();
		
		if (setValue("ADDR", index, addrString) 
				&& setValue("ADR1", index, street1) 
				&& setValue("ADR2", index, street2) 
				&& setValue("CITY", index, city) 
				&& setValue("POST", index, post)
				&& setValue("CTRY", index, country)
				&& setValues("PHON", index, phone) 
				&& setValues("EMAIL", index, email) 
				&& setValues("FAX", index, fax) 
				&& setValues("WWW", index, websites)) {
			return true;
		}
		
		return addAddress(street1, street2, city, post, country, phone, email, fax, websites);
	}
	
	/**
	 * 
	 * 
	 * @param index
	 * @return
	 */
	public String getAddress(int index) {
		return getValue("ADDR", index, "INDIVIDUAL_ATTRIBUTE_STRUCTURE;RESI" + GedcomNode.PATH_OPTION_DELIMITER + index, "RESI", "INDIVIDUAL_EVENT_DETAIL", "EVENT_DETAIL", "ADDRESS_STRUCTURE", "ADDR");
	}
	
	/**
	 * 
	 * 
	 * @param index
	 * @return
	 */
	public String getStreet1(int index) {
		return getValue("ADR1", index, "INDIVIDUAL_ATTRIBUTE_STRUCTURE;RESI" + GedcomNode.PATH_OPTION_DELIMITER + index, "RESI", "INDIVIDUAL_EVENT_DETAIL", "EVENT_DETAIL", "ADDRESS_STRUCTURE", "ADDR", "ADR1");
	}
	
	/**
	 * 
	 * 
	 * @param index
	 * @return
	 */
	public String getStreet2(int index) {
		return getValue("ADR2", index, "INDIVIDUAL_ATTRIBUTE_STRUCTURE;RESI" + GedcomNode.PATH_OPTION_DELIMITER + index, "RESI", "INDIVIDUAL_EVENT_DETAIL", "EVENT_DETAIL", "ADDRESS_STRUCTURE", "ADDR", "ADR2");
	}
	
	/**
	 * 
	 * 
	 * @param index
	 * @return
	 */
	public String getCity(int index) {
		return getValue("CITY", index, "INDIVIDUAL_ATTRIBUTE_STRUCTURE;RESI" + GedcomNode.PATH_OPTION_DELIMITER + index, "RESI", "INDIVIDUAL_EVENT_DETAIL", "EVENT_DETAIL", "ADDRESS_STRUCTURE", "ADDR", "CITY");
	}
	
	/**
	 * 
	 * 
	 * @param index
	 * @return
	 */
	public String getPost(int index) {
		return getValue("POST", index, "INDIVIDUAL_ATTRIBUTE_STRUCTURE;RESI" + GedcomNode.PATH_OPTION_DELIMITER + index, "RESI", "INDIVIDUAL_EVENT_DETAIL", "EVENT_DETAIL", "ADDRESS_STRUCTURE", "ADDR", "POST");
	}
	
	/**
	 * 
	 * 
	 * @param index
	 * @return
	 */
	public String getCountry(int index) {
		return getValue("CTRY", index, "INDIVIDUAL_ATTRIBUTE_STRUCTURE;RESI" + GedcomNode.PATH_OPTION_DELIMITER + index, "RESI", "INDIVIDUAL_EVENT_DETAIL", "EVENT_DETAIL", "ADDRESS_STRUCTURE", "ADDR", "CTRY");
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
		return getValue("PHON" + index, phoneIndex, "INDIVIDUAL_ATTRIBUTE_STRUCTURE;RESI" + GedcomNode.PATH_OPTION_DELIMITER + index, "RESI", "INDIVIDUAL_EVENT_DETAIL", "EVENT_DETAIL", "ADDRESS_STRUCTURE", "PHON" + GedcomNode.PATH_OPTION_DELIMITER + phoneIndex);
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
		return getValue("EMAIL" + index, emailIndex, "INDIVIDUAL_ATTRIBUTE_STRUCTURE;RESI" + GedcomNode.PATH_OPTION_DELIMITER + index, "RESI", "INDIVIDUAL_EVENT_DETAIL", "EVENT_DETAIL", "ADDRESS_STRUCTURE", "EMAIL" + GedcomNode.PATH_OPTION_DELIMITER + emailIndex);
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
		return getValue("FAX" + index, faxIndex, "INDIVIDUAL_ATTRIBUTE_STRUCTURE;RESI" + GedcomNode.PATH_OPTION_DELIMITER + index, "RESI", "INDIVIDUAL_EVENT_DETAIL", "EVENT_DETAIL", "ADDRESS_STRUCTURE", "FAX" + GedcomNode.PATH_OPTION_DELIMITER + faxIndex);
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
		return getValue("WWW" + index, wwwIndex, "INDIVIDUAL_ATTRIBUTE_STRUCTURE;RESI" + GedcomNode.PATH_OPTION_DELIMITER + index, "RESI", "INDIVIDUAL_EVENT_DETAIL", "EVENT_DETAIL", "ADDRESS_STRUCTURE", "WWW" + GedcomNode.PATH_OPTION_DELIMITER + wwwIndex);
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
		try {
			Line line1 = new XRefLine(
					"FAMS", 
					familyId, 
					createPathEnd("SPOUSE_TO_FAMILY_LINK", "FAMS"));
			
			return addLines(line1);
		} catch (GedcomError e) {
			throw e;
		}
	}
	
	/**
	 * 
	 * 
	 * @param familyId
	 * @return
	 */
	public boolean setSpouseLink(int index, String familyId) {
		if (setXRef("FAMS", index, familyId)) {
			return true;
		}
		
		return addSpouseLink(familyId);
	}
	
	/**
	 * 
	 * 
	 * @param index
	 * @return
	 */
	public String getSpouseLink(int index) {
		return getXRef("FAMS", index, "SPOUSE_TO_FAMILY_LINK" + GedcomNode.PATH_OPTION_DELIMITER + index, "FAMS");
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
		try {
			Line line1 = new XRefLine(
					"FAMC", 
					chilId, 
					createPathEnd("CHILD_TO_FAMILY_LINK", "FAMC"));
			
			return addLines(line1);
		} catch (GedcomError e) {
			throw e;
		}
	}
	
	/**
	 * 
	 * 
	 * @param chilId
	 * @return
	 */
	public boolean setChildLink(int index, String chilId) {
		if (setXRef("FAMC", index, chilId)) {
			return true;
		}
		
		return addChildLink(chilId);
	}
	
	/**
	 * 
	 * 
	 * @param index
	 * @return
	 */
	public String getChildLink(int index) {
		return getXRef("FAMC", index, "CHILD_TO_FAMILY_LINK" + GedcomNode.PATH_OPTION_DELIMITER + index, "FAMC");
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
