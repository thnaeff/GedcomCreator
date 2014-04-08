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
package ch.thn.gedcom.creator.test;

import java.util.Date;

import ch.thn.gedcom.GedcomFormatter;
import ch.thn.gedcom.creator.GedcomCreatorEOF;
import ch.thn.gedcom.creator.GedcomCreatorFamily;
import ch.thn.gedcom.creator.GedcomCreatorIndividual;
import ch.thn.gedcom.creator.GedcomCreatorEnums.*;
import ch.thn.gedcom.printer.GedcomStructureTextPrinter;
import ch.thn.gedcom.printer.GedcomStructureTreePrinter;
import ch.thn.gedcom.store.GedcomParseException;
import ch.thn.gedcom.store.GedcomStore;

/**
 * @author Thomas Naeff (github.com/thnaeff)
 *
 */
public class GedcomCreatorTest {
	
	
	public static void main(String[] args) {
		
		
		GedcomStore store = new GedcomStore();
		
		store.showParsingOutput(false);
		
		try {
			store.parse("/home/thomas/Projects/java/GedcomStore/gedcomobjects_5.5.1.gedg");
		} catch (GedcomParseException e) {
			e.printStackTrace();
		}
		
		
		GedcomStructureTreePrinter treePrinter = new GedcomStructureTreePrinter(true);
		GedcomStructureTextPrinter textPrinter = new GedcomStructureTextPrinter();
		
		
		GedcomCreatorIndividual indi = new GedcomCreatorIndividual(store, "1");
		indi.setSex(Sex.MALE);
		indi.setSex(Sex.FEMALE);
		indi.setBirth(true, GedcomFormatter.getGedcomDate(new Date(), true, true));
		indi.setDeath(true, GedcomFormatter.getGedcomDate(new Date(), true, true));
		indi.setOccupation("occupation");
		indi.setEducation("education");
		indi.addName("Naeff", "Thomas", "Thomas2");
		indi.addName("Naeff2", NameType.MARRIED, "Thomas", "Thomas2");
		indi.addAddress("street11", "street12", "city1", "post1", "country1", 
				new String[] {"phone11", "phone12"}, new String[] {"email11", "email12"}, 
				new String[] {"fax1"}, new String[] {"www1"});
		indi.addAddress("street21", "street22", "city2", "post2", "country2", 
				new String[] {"phone21", "phone22", "phone23"}, new String[] {"email21", "email22"}, 
				new String[] {"fax2"}, new String[] {"www2"});
		indi.addSpouseLink("spouse1");
		indi.addSpouseLink("spouse2");
		indi.addChildLink("child");
		indi.addNote("A Note");
		indi.addNote("Another Note");
		indi.setChangeDate(GedcomFormatter.getGedcomDate(new Date(), true, true), GedcomFormatter.getGedcomTime(new Date()));
		
//		indi.removeSex();
//		indi.removePhone(1, 1);
//		indi.removeChangeDate();
//		indi.removeAddressStructure(0);
		
//		indi.setName(1, "N", NameType.MAIDEN, "T", "T2");
		
//		System.out.println(textPrinter.print(indi.getTree()));
		System.out.println(treePrinter.print(indi.getTree()));
		
		System.out.println("------");
		
		GedcomCreatorFamily fam = new GedcomCreatorFamily(store, "1");
		fam.setHusbandLink("1");
		fam.setWifeLink("2");
		fam.addChildLink("4");
		fam.addChildLink("3");
		fam.setMarried(true, GedcomFormatter.getGedcomDate(new Date(), true, true));
		fam.setDivorced(true, null);
		
		fam.addNote("A Family Note");
		fam.setChangeDate(GedcomFormatter.getGedcomDate(new Date(), true, true), GedcomFormatter.getGedcomTime(new Date()));
		System.out.println(textPrinter.print(fam.getTree()));
		
		System.out.println("------");
		
		GedcomCreatorEOF eof = new GedcomCreatorEOF(store);
		System.out.println(textPrinter.print(eof.getTree()));
		
	}

}
