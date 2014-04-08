# GedcomCreator
**A library to easily create basic [GEDCOM](http://en.wikipedia.org/wiki/GEDCOM)-structures. Those structures can then also be used to create more complex structures if needed -> it serves as a great starting point to set up a structure.**

GedcomCreator uses the [GedcomStore](https://github.com/thnaeff/GedcomStore) library to create the GEDCOM structures. GedcomCreator, however, provides some basic classes and methods to get started on setting up the structures. The library comes with classes for:
* The header structure HEADER `GedcomCreatorHeader`
* The submitter record SUBMITTER_RECORD `GedcomCreatorSubmitter`
* The family record FAM_RECORD `GedcomCreatorFamily`
* The individual record INDIVIDUAL_RECORD `GedcomCreatorIndividual`
* The end-of-file line TRLR `GedcomCreatorEOF`

Those classes have methods implemented which will help with the most common situations in creating a GEDCOM structure. The `GedcomCreatorIndividual` class for example has methods to set the gender, birth date, death date, address etc. of an individual. Since the GedcomCreator depends on the GedcomStore library, each structure class needs a GedcomStore instance in the constructor.

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


GedcomCreatorIndividual indi = new GedcomCreatorIndividual(store, "1");
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

System.out.println(textPrinter.print(indi.getNode()));

System.out.println("------");

GedcomCreatorFamily fam = new GedcomCreatorFamily(store, "1");
fam.setHusbandLink("1");
fam.setWifeLink("2");
fam.addChildLink("3");
fam.addChildLink("4");
fam.setMarried(true, new Date());
fam.setDivorced(true, null);
fam.addNote("A Family Note");
fam.setChangeDate(new Date());

System.out.println(textPrinter.print(fam.getNode()));	
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


# Dependencies
* [GedcomStore](https://github.com/thnaeff/GedcomStore)
* [Joda-Time](http://http://www.joda.org) (GedcomStore needs this)
* My own utility library: [Util](http://github.com/thnaeff/Util)

