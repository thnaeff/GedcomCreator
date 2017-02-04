# GedcomCreator
**A library to easily create basic [GEDCOM](http://en.wikipedia.org/wiki/GEDCOM)-structures. Those structures can then also be used to create more complex structures if needed.**

---


[![License](http://img.shields.io/badge/License-Apache v2.0-802879.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)
[![Java Version](http://img.shields.io/badge/Java-1.6%2B-2E6CB8.svg)](https://java.com)
[![Apache Maven ready](http://img.shields.io/badge/Apache Maven ready-3.3.9%2B-FF6804.svg)](https://maven.apache.org/)


---

GedcomCreator uses the [GedcomStore](https://github.com/thnaeff/GedcomStore) library to create the GEDCOM structures. GedcomCreator provides some basic classes and methods to get started on setting up the structures. The library comes with classes for:

* The header structure HEADER `GedcomHeader`
* The submitter record SUBMITTER_RECORD `GedcomSubmitter`
* The family record FAM_RECORD `GedcomFamily`
* The individual record INDIVIDUAL_RECORD `GedcomIndividual`
* The end-of-file line TRLR `GedcomEOF`

Those classes have methods implemented which will help with the most common situations in creating a GEDCOM structure. The `GedcomIndividual` class for example has methods to set the gender, birth date, death date, address etc. of an individual. Since the GedcomCreator depends on the GedcomStore library, each structure class needs a GedcomStore instance in the constructor.

The GedcomCreator is made for the GEDCOM specifications version 5.5 and 5.5.1. gedg files for both versions are included in the [GedcomStore](https://github.com/thnaeff/GedcomStore).


## Example
Here is a short example to get started with the GedcomCreator. All set/add-Methods also have a get- and remove-Method:

```Java
GedcomStore store = new GedcomStore();

store.showParsingOutput(false);

try {
	store.parse("PATH_TO_FILE/gedcomobjects_5.5.1.gedg");
} catch (GedcomParseException e) {
	e.printStackTrace();
}


GedcomIndividual indi = new GedcomIndividual(store, "1");
indi.setSex(Sex.MALE);
indi.setBirth(true, new Date());
indi.setDeath(true, new Date());
indi.setOccupation("occupation");
indi.setEducation("education");
indi.addName("Name", new String[] {"Georg", "Walter"});
indi.addName("Married-Name", NameType.MARRIED, new String[] {"Georg", "Walter"});
indi.addAddress("street11", "street12", "city1", "post1", "country1", 
		new String[] {"phone11", "phone12"}, new String[] {"email11", "email12"}, 
		new String[] {"fax1"}, new String[] {"www1"});
indi.addSpouseLink("link to spouse ID");
indi.addChildLink("link to this individuals family");
indi.addNote("A Note");
indi.addNote("Another Note");
indi.setChangeDate(new Date());

GedcomStructureTextPrinter textPrinter = new GedcomStructureTextPrinter();

System.out.println(textPrinter.print(indi.getTree()));

System.out.println("------");

GedcomFamily fam = new GedcomFamily(store, "1");
fam.setHusbandLink("1");
fam.setWifeLink("2");
fam.addChildLink("3");
fam.addChildLink("4");
fam.setMarried(true, new Date());
fam.setDivorced(true, null);
fam.addNote("A Family Note");
fam.setChangeDate(new Date());

System.out.println(textPrinter.print(fam.getTree()));	
```

The output of this example code is:
```
0 @1@ INDI
  1 SEX M
  1 BIRT Y
    2 DATE 09 FEB 2014
  1 DEAT Y
    2 DATE 09 FEB 2014
  1 OCCU occupation
  1 EDUC education
  1 NAME Georg, Walter /Name/
    2 GIVN Georg, Walter
    2 SURN Name
  1 NAME Georg, Walter /Married-Name/
    2 GIVN Georg, Walter
    2 SURN Married-Name
    2 TYPE married
  1 RESI
    2 ADDR street11, street12, post1, city1, country1
      3 ADR1 street11
      3 ADR2 street12
      3 CITY city1
      3 POST post1
      3 CTRY country1
    2 PHON phone11
    2 PHON phone12
    2 EMAIL email11
    2 EMAIL email12
    2 FAX fax1
    2 WWW www1
  1 FAMC @link to this individuals family@
  1 FAMS @link to spouse ID@
  1 CHAN
    2 DATE 09 FEB 2014
      3 TIME 12:10:01
  1 NOTE A Note
  1 NOTE Another Note


------
0 @1@ FAM
  1 HUSB @1@
  1 WIFE @2@
  1 CHIL @3@
  1 CHIL @4@
  1 MARR Y
    2 DATE 09 FEB 2014
  1 DIV
  1 CHAN
    2 DATE 09 FEB 2014
      3 TIME 12:10:01
  1 NOTE A Family Note
```

##Printing
As shown in the example above, a GedcomCreator structure can be printed using any of the `GedcomStructure*` printers (`GedcomStructureTextPrinter` for example) located in the GedcomStore. The internal node of the `GedcomCreator` structure tree has to be passed to the printer for printing.



##Access to the complete GEDCOM structure
The internal GEDCOM structure tree can be accessed for more advanced structure modifications. As the example above shows, each `GedcomCreator` structure has the `getTree()` method which returns the internal GEDCOM structure tree (a `GedcomNode` from the `GedcomStore`). See [GedcomStore](https://github.com/thnaeff/GedcomStore) for more information.



****************************************************************************



#Family Relations (GedcomCreatorStructureStorage)
The class `GedcomCreatorStructureStorage` serves three purposes:

* As a easy place to collect all `GedcomCreator` structures (`GedcomFamily`, `GedcomIndividual`, ...)
* To collect family relation informations (children/partners on an individual, families of a parent/child, ...)
* To locate missing individuals or families which are linked but not present

Just add all `GedcomCreator` structures to a `GedcomCreatorStructureStorage` instance and call the `buildFamilyRelations()` to build the family relations and locate any missing structures. The following methods can then be used for family relations:

* getChildrenOfIndividual: Returs all the children of the given individual
* getFamiliesOfParent: Returns all the families the given individual is a parent of
* getFamilyOfParents: Returns the family of which the two given individuals are the parents of
* getMissingFamilies: Returns a list of all the family link ID's which are given in individuals but which are not found as family
* getMissingIndividuals: Returns a list of all the individual link ID's which are given in families but which are not found as individual

Whenever new `GedcomCreator` structures are added/removed to a `GedcomCreatorStructureStorage`, the `buildFamilyRelations()` method has to be called again to update the family relations. However, if any of the get-method above is called and there are any changes, the `buildFamilyRelations()` method is called automatically before the get-method returns its result.


****************************************************************************



---


<img src="http://maven.apache.org/images/maven-logo-black-on-white.png" alt="Built with Maven" width="150">

This project can be built with Maven

Maven command:
```
$ mvn clean install
```

pom.xml entry in your project:
```
<dependency>
	<groupId>ch.thn.gedcom</groupId>
	<artifactId>creator</artifactId>
	<version>0.0.1-SNAPSHOT</version>
</dependency>
```

---