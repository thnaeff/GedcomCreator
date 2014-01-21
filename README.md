# GedcomCreator
**A library to easily create basic [GEDCOM](http://en.wikipedia.org/wiki/GEDCOM)-structures. Those structures can then also be used to create more complex structures if needed.**

GedcomCreator uses the [GedcomStore](https://github.com/thnaeff/GedcomStore) library to create the GEDCOM structures. GedcomCreator, however, provides some basic classes and methods to get started on setting up the structures. The library comes with classes for:
* The header structure HEADER `GedcomCreatorHeader`
* The submitter record SUBMITTER_RECORD `GedcomCreatorSubmitter`
* The family record FAM_RECORD `GedcomCreatorFamily`
* The individual record INDIVIDUAL_RECORD `GedcomCreatorIndividual`
* The end-of-file line TRLR `GedcomCreatorEOF`

Those classes have methods implemented which will help with the most common situations in creating a GEDCOM structure. The `GedcomCreatorIndividual` class for example has methods to set the gender, birth date, death date, address etc. of an individual. Since the GedcomCreator depends on the GedcomStore library, each structure class needs a GedcomStore instance in the constructor.


## Example
Here is a short example to get started with the GedcomCreator:

```Java
GedcomStore store = new GedcomStore();

store.showParsingOutput(false);

try {
	store.parse("PATH_TO_FILE/GedcomNodes_5.5.1.gedg");
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

System.out.println(indi.getNode().print(new GedcomStructureTextPrinter()));

System.out.println("------");

GedcomCreatorFamily fam = new GedcomCreatorFamily(store, "1");
fam.setHusbandLink("1");
fam.setWifeLink("2");
fam.addChildLink("3");
fam.addChildLink("4");
fam.setMarried(true, new Date());
fam.setDivorced(true);
fam.addNote("A Family Note");
fam.setChangeDate(new Date());

System.out.println(fam.getNode().print(new GedcomStructureTextPrinter()));
```


# Dependencies
* [GedcomStore](https://github.com/thnaeff/GedcomStore)
* [Joda-Time](http://http://www.joda.org) (GedcomStore needs this)
* My own utility library: [Util](http://github.com/thnaeff/Util)

